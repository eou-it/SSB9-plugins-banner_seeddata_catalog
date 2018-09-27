/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * SFRSTCR tables
 * add to enrollment count in schedule seed data after the record is inserted
 */

public class SfrstcrDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SfrstcrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSfrstcr()

    }

    /**
     * Process the sfrstcr records.  Use the dynamic insert process but update the enrollment counts in ssbsect after the insert
     *
     */

    def processSfrstcr() {
        def apiData = new XmlParser().parseText(xmlData)

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode)

        String sql = "select ssbsect_surrogate_id ID, ssbsect_enrl ENRL, ssbsect_seats_avail AVAIL, ssbsect_max_enrl MAXENRL , SSBSECT_TOT_CREDIT_HRS CREDITS from ssbsect  where ssbsect_term_code = ? and ssbsect_crn = ?"
        def enrl = conn.firstRow(sql, [apiData.SFRSTCR_TERM_CODE.text(), apiData.SFRSTCR_CRN.text()])

        String sql2 = "select count(sfrstcr_pidm) cnt, sum(sfrstcr_credit_hr) hrs   from sfrstcr  where sfrstcr_term_code = ? and sfrstcr_crn = ?"
        def stcrenrl = conn.firstRow(sql2, [apiData.SFRSTCR_TERM_CODE.text(), apiData.SFRSTCR_CRN.text()])

        if (enrl && stcrenrl) {
            enrl.ENRL = stcrenrl.CNT
            enrl.AVAIL = enrl.MAXENRL - stcrenrl.CNT
            if (apiData.SFRSTCR_CREDIT_HR.text()) {
                enrl.CREDITS = stcrenrl.hrs
            }

            try {

                conn.call("""begin
                    update ssbsect
                    set ssbsect_enrl = ?   ,
                    ssbsect_seats_avail = ?,
                    ssbsect_tot_credit_hrs = ?
                    where ssbsect_surrogate_id = ?;
                    end ;""", [enrl.ENRL, enrl.AVAIL, enrl.CREDITS, enrl.ID])
                connectInfo.tableUpdate("SSBSECT", 0, 0, 1, 0, 0)


            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem executing update of enrollment cnts on ssbsect ${apiData.SFRSTCR_TERM_CODE.text()} ${apiData.SFRSTCR_CRN.text()} from SfrstcrDML.groovy for ${connectInfo.tableName}: $e.message"
                    println "${sql}"
                }
            }
        }
    }

}
