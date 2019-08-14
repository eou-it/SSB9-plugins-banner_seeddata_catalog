/*********************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * General Faculty Feedback Session Control DML.
 */
public class FacultyStudentFeedBackCommentsDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public FacultyStudentFeedBackCommentsDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSFRFFST()

    }

    /**
     * Process the SFRFFST records.
     *
     */

    def processSFRFFST() {
        def apiData = new XmlParser().parseText( xmlData )

        def studentId = apiData.STUDENTBANNERID?.text()
        def spridenRow1
        if (studentId) {
            String findPidm = """SELECT SPRIDEN_PIDM FROM SPRIDEN WHERE SPRIDEN_ID = ? """
            spridenRow1 = conn.firstRow( findPidm, [studentId] )
            if (spridenRow1) {
                apiData.SFRFFST_STUDENT_PIDM[0].setValue( spridenRow1.SPRIDEN_PIDM.toString() )
            }
        }

        def thirdPartyId = apiData.BANNERID?.text()
        def spridenRow2
        if (thirdPartyId) {
            String findPidm = """SELECT SPRIDEN_PIDM FROM SPRIDEN WHERE SPRIDEN_ID = ? """
            spridenRow2 = conn.firstRow( findPidm, [thirdPartyId] )
            if (spridenRow2) {
                apiData.SFRFFST_FACULTY_PIDM[0].setValue( spridenRow2.SPRIDEN_PIDM.toString() )
            }
        }

        def termCode = apiData.SFBFFSC_TERM_CODE?.text()
        def startDate = apiData.SFBFFSC_START_DATE?.text()
        def endDate = apiData.SFBFFSC_END_DATE?.text()
        def scId
        String findId = """SELECT SFBFFSC_SURROGATE_ID FROM SFBFFSC WHERE SFBFFSC_TERM_CODE = ? AND SFBFFSC_START_DATE= ? AND  SFBFFSC_END_DATE = ?"""
        scId = conn.firstRow( findId, [termCode,startDate,endDate] )
        if (scId) {
            apiData.SFRFFST_SFBFFSC_ID[0].setValue( scId.SFBFFSC_SURROGATE_ID.toString() )
        }


        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() {fields ->
            def value = fields.text().replaceAll(/&|'|>|</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        if (connectInfo.debugThis) {
            println "SFRFFST Record Student ID ${studentId} pidm ${apiData.SFRFFST_STUDENT_PIDM.text()} spridenRow1Pidm ${spridenRow1.SPRIDEN_PIDM} "
            println "SFRFFST Record Faculty ID ${thirdPartyId} pidm ${apiData.SFRFFST_FACULTY_PIDM.text()} spridenRow2Pidm ${spridenRow2.SPRIDEN_PIDM} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )

    }
}

