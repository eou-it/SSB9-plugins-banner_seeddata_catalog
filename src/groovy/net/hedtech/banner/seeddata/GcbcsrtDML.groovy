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

public class GcbcsrtDML {

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


    public GcbcsrtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGcbcsrt()
    }

    /**
     * Process the Action Item Table records.
     *
     */
    def processGcbcsrt() {
        def apiData = new XmlParser().parseText(xmlData)

        def studentId = apiData.BANNERID?.text()
        def studentPidm
        def spridenRow

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBCSRT") {
            // delete data so we can re-add instead of update so all child data is refreshed
            deleteData()
        } else if (connectInfo.tableName == "GCRCSRS") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
                if (spridenRow) {
                    studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                    apiData.GCRCSRS_PIDM[0].setValue(studentPidm)
                    println "Gcrcsrs Record student ID ${studentId} pidm ${studentPidm}"
                } else {
                    println "No record found for student ID ${studentId}"
                }
            }
            //debug
            if (connectInfo.debugThis) {
                println "Gcrcsrs Record student ID ${studentId} pidm ${studentPidm}"
            }

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

    def deleteData() {
        deleteData("GCRCSRS", "delete from GCRCSRS where GCRCSRS_CSRT_ID in ( select GCBCSRT_SURROGATE_ID from GCBCSRT ) ")
        deleteData("GCBCSRT", "delete from GCBTMPL where GCBTMPL_folder_id  = ?   ")
    }

    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [templateSeq])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1, 0)
                println "Problem executing delete for template ${template} ${templateSeq} from GcbcsrtDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

}

