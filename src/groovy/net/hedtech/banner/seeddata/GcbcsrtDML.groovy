/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * Action Item tables
 * Provide PIDM to student.
 */

public class GcbcsrtDML{

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode
    def pidm
    def bannerid
    def apiData = new XmlParser().parseText(xmlData)

    public GcbcsrtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processStudent()
        processGcbcsrt()
    }

    /**
     * Process the Action Item Table records.
     *
     */

    def processStudent() {

        String ssql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        def cntSpriden = 0

        try {
            this.conn.eachRow(ssql, [apiData.SPRIDEN_ID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in StudentPersonIDDML,  ${apiData.SPRIDEN_ID.text()}  from SPRIDEN. $e.message"
            }
        }
        if (cntSpriden == 0) {
            def newSpriden = new StudentPersonIDDML(connectInfo, conn, connectCall, xmlData)
            connectInfo.saveStudentPidm = newSpriden.pidm
        }
        else {
            connectInfo.saveStudentPidm = pidm
        }

    }

    def processGcbcsrt() {


        if (connectInfo.tableName == "GCRCSRS") {
          apiData.GCRCSRS_PIDM[0].setValue(connectInfo.saveStudentPidm)
        }

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
    }
}

