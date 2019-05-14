/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/** *******************************************************************************
 Copyright 2009-2012 Ellucian. All Rights Reserved.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * SORATMT tables
 * add to ssrmeet_surrogate_id into the seed data before the record is inserted
 */

public class SoratmtDML {
    def surrogateId = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SoratmtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        if (deleteNode && deleteNode?.text() == "YES") processDelete()
        else processSoratmt()

    }


    def processDelete() {
        String schDel = ""
        schDel = """DELETE FROM soratmt  WHERE soratmt_data_origin = 'GRAILS' """
        def tableName = "SORATMT"
        deleteData(tableName, schDel)
    }


    private def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql)
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} from soratmtDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

    /**
     * Process the soratmt records.  Use the dynamic insert process but update the ssrmeet_surrogate_id before the insert
     *
     */

    def processSoratmt() {
        def apiData = new XmlParser().parseText(xmlData)

        String sql = "select ssrmeet_surrogate_id ID from ssrmeet where ssrmeet_term_code = ? and ssrmeet_crn = ? and ssrmeet_catagory = ?" +
                " and ssrmeet_start_date = to_date(?,'DD-MON-YYYY') and ssrmeet_end_date = to_date(?,'DD-MON-YYYY')" +
                " and ssrmeet_begin_time = ? and ssrmeet_end_time = ?"

        try {
            surrogateId = conn.firstRow(sql, [apiData.SSRMEET_TERM_CODE.text(), apiData.SSRMEET_CRN.text(), apiData.SSRMEET_CATAGORY.text(),
                                        apiData.SSRMEET_START_DATE.text(), apiData.SSRMEET_END_DATE.text(), apiData.SSRMEET_BEGIN_TIME.text(),
                                        apiData.SSRMEET_END_TIME.text()])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in SoratmtDML,  [${apiData.SSRMEET_TERM_CODE.text()}, ${apiData.SSRMEET_CRN.text()}, ${apiData.SSRMEET_CATAGORY.text()}, " +
                                "${apiData.SSRMEET_START_DATE.text()}, ${apiData.SSRMEET_END_DATE.text()}, ${apiData.SSRMEET_BEGIN_TIME.text()}, " +
                                "${apiData.SSRMEET_END_TIME.text()}] from ssrmeet for ${connectInfo.tableName}. $e.message"
            }
        }

        if (surrogateId) {

            // update the meeting surrogate id with the one that is selected
            if (connectInfo.tableName == "SORATMT") {
                if (apiData.SORATMT_SURROGATE_ID_SSRMEET.text().toInteger() != surrogateId.ID) {
                    apiData.SORATMT_SURROGATE_ID_SSRMEET[0].setValue(surrogateId.ID.toString())
                }

            }

            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() {fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

        }
        else {
            connectInfo.tableUpdate('SORATMT', 0, 0, 0, 1, 0)
        }

    }

}
