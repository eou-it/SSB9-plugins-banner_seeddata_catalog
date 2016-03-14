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



        if (connectInfo.tableName == "SPRIDEN") {
            //lookup pidm and set based on banner id

            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }

            if (spridenRow) {
              studentPidm = spridenRow.SPRIDEN_PIDM.toString()
              apiData.SPRIDEN_PIDM[0].setValue(studentPidm)
              println "SPRIDEN Record student ID ${studentId} pidm ${studentPidm}"
            }
            //debug
            if (connectInfo.debugThis) {
                println "SPRIDEN Record student ID ${studentId} pidm ${studentPidm}"
            }
        }

        if (connectInfo.tableName == "GOBTPAC") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }

            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.GOBTPAC_PIDM[0].setValue(studentPidm)
                println "GOBTPAC Record student ID ${studentId} pidm ${studentPidm}"
            }
            //debug
            if (connectInfo.debugThis) {
                println "GOBTPAC Record student ID ${studentId} pidm ${studentPidm}"
            }
        }

        if (connectInfo.tableName == "GOBEACC") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }

            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.GOBEACC_PIDM[0].setValue(studentPidm)
                println "GOBEACC Record student ID ${studentId} pidm ${studentPidm}"
            }
            //debug
            if (connectInfo.debugThis) {
                println "GOBEACC Record student ID ${studentId} pidm ${studentPidm}"
            }
        }

        if (connectInfo.tableName == "SGBSTDN") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }
            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.SGBSTDN_PIDM[0].setValue(studentPidm)
                println "SGBSTDN Record student ID ${studentId} pidm ${studentPidm}"
            }
            //debug
            if (connectInfo.debugThis) {
                println "SGBSTDN Record student ID ${studentId} pidm ${studentPidm}"
            }
        }

        if (connectInfo.tableName == "SORLFOS") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }

            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.SORLFOS_PIDM[0].setValue(studentPidm)
                println "SORLFOS Record student ID ${studentId} pidm ${studentPidm}"
            }
            //debug
            if (connectInfo.debugThis) {
                println "SORLFOS Record student ID ${studentId} pidm ${studentPidm}"
            }
        }

        if (connectInfo.tableName == "GCRCSRS") {
            //lookup pidm and set based on banner id
            if (studentId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [studentId])
            }
            else {
                println "No record found for student ID ${studentId}"
            }
            if (spridenRow) {
                studentPidm = spridenRow.SPRIDEN_PIDM.toString()
                apiData.GCRCSRS_PIDM[0].setValue(studentPidm)
                println "Gcrcsrs Record student ID ${studentId} pidm ${studentPidm}"
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
}

