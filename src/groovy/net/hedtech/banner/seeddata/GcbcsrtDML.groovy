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
    int itemSeq
    def item = null

    public GcbcsrtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        //processStudent()
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
            this.conn.eachRow(ssql, [apiData.BANNERID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in StudentPersonIDDML,  ${apiData.BANNERID.text()}  from SPRIDEN. $e.message"
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

        String ssql = """select * from gcbcsrt  where gcbcsrt_name = ? """
        // find if the action item already exists in the database and use it's curr_rule for inserting into the db
        try {
            def itemSeqR = this.conn.firstRow(ssql, [apiData.GCBCSRT_NAME.text()])

            println "itemSeqR: " + itemSeqR

            if ( itemSeqR) {
                itemSeq = itemSeqR?.GCBCSRT_SURROGATE_ID
            }
            else itemSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Action Item ID in GcbcsrtDML,  ${apiData.GCBCSRT_NAME.text()} from GCBCSRT " +
                        "for ${connectInfo.tableName}. $e.message"
            }
        }

        if (connectInfo.tableName == "GCBCSRT") {
            deleteData()
            println "deleting data"
        }

        if (connectInfo.tableName == "GCRCSRS") {
          apiData.GCRCSRS_PIDM[0].setValue(connectInfo.saveStudentPidm)
        if (apiData.GCRCSRS_CSRT_ID.text().toInteger() != itemSeq) {
            apiData.GCRCSRS_CSRT_ID[0].setValue(itemSeq.toString()
            )

            println itemSeq.toString()
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
        String deleteSQL = "delete from gcrcsrs where gcrcsrs_pidm = ${connectInfo.saveStudentPidm}"
        try {
            def cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("GCRCSRS", 0, 0, 0, 0, cntDel)
            //println "delete GCRCSRS ${connectInfo.saveStudentPidm} ${this.ID} ${this.lastName}"
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GCRCSRS", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete GCRCSRS ${connectInfo.saveStudentPidm} ${this.ID} ${this.lastName}" +
                        " from GcbcsrtDML.groovy: $e.message"
            }
        }

        deleteSQL = "delete from gcbcsrt"
        try {
            def cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("GCBCSRT", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GCBCSRT", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete GCBCSRT ${connectInfo.saveStudentPidm} ${this.ID} ${this.lastName}" +
                        " from GcbcsrtDML.groovy: $e.message"
            }
        }
    }

}

