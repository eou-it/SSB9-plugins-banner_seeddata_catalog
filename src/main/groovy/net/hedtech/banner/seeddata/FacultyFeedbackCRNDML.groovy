/*********************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * General Faculty Feedback Session Control DML.
 */
public class FacultyFeedbackCRNDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public FacultyFeedbackCRNDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSFRFFBK()

    }

    /**
     * Process the SFRFFST records.
     *
     */
    def processSFRFFBK() {
        def apiData = new XmlParser().parseText( xmlData )

        def studentId = apiData.STUDENTBANNERID?.text()
        def facultyId = apiData.BANNERID?.text()
        if (studentId && facultyId) {
            String findId = """SELECT SFRFFST_SURROGATE_ID FROM SFRFFST ,SFBFFSC
                               WHERE SFRFFST.SFRFFST_SFBFFSC_ID = SFBFFSC.SFBFFSC_SURROGATE_ID
                               AND SFRFFST.SFRFFST_FACULTY_PIDM = (SELECT MAX(SPRIDEN_PIDM) FROM SPRIDEN WHERE SPRIDEN_ID=?)
                               AND SFRFFST.SFRFFST_STUDENT_PIDM = (SELECT MAX(SPRIDEN_PIDM) FROM SPRIDEN WHERE SPRIDEN_ID=?)
                               AND SFBFFSC_TERM_CODE = ?
                               AND SFBFFSC_DESCRIPTION = ?
                               AND SFRFFST.SFRFFST_CRN = ? """
            def spridenRow1 = conn.firstRow( findId, [facultyId, studentId, apiData.SFBFFSC_TERM_CODE?.text(),apiData.SFBFFSC_DESCRIPTION?.text(),apiData.SFRFFST_CRN?.text()] )
            if (spridenRow1) {
                apiData.SFRFFBK_SFRFFST_ID[0].setValue( spridenRow1.SFRFFST_SURROGATE_ID.toString() )
            }
        }


        def code = apiData.STVFFVA_CODE.text()
        if (code) {
            String findId = """SELECT STVFFVA_SURROGATE_ID FROM STVFFVA WHERE STVFFVA_CODE = ? """
            def spridenRow2 = conn.firstRow( findId, [code] )
            if (spridenRow2) {
                apiData.SFRFFBK_STVFFVA_ID[0].setValue( spridenRow2.STVFFVA_SURROGATE_ID.toString() )
            }
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

