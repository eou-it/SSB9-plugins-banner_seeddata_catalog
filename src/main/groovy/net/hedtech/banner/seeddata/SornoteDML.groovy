/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * SORNOTE tables
 * Provide PIDM's to author and student.
 */

public class SornoteDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SornoteDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSornote()

    }

    /**
     * Process the sornote records.
     *
     */

    def processSornote() {
        def apiData = new XmlParser().parseText(xmlData)

        def authorId = apiData.AUTHBANNERID?.text()
        def studentId = apiData.STUDENTBANNERID?.text()
        def authorPidm
        def studentPidm
        def spridenRow
        if (authorId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow = conn.firstRow(findPidm, [authorId])
            if (spridenRow) {
                authorPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.SORNOTE_PIDM[0].setValue(authorPidm)
            }
        }
        if (studentId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow = conn.firstRow(findPidm, [studentId])
            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.SORNOTE_STUDENT_PIDM[0].setValue(studentPidm)
            }
        }

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        if (connectInfo.debugThis) {
            println "SORNOTE Record author ID ${authorId} pidm ${authorPidm} student ID ${studentId} pidm ${studentPidm}"
        }

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

    }

}
