/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * SORATRK tables
 * Generate the sequence number
 */

public class SoratrkDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SoratrkDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSoratrk()

    }

    /**
     * Process the soratrk records.
     *
     */

    def processSoratrk() {
        def apiData = new XmlParser().parseText(xmlData)
        if (apiData?.DELETE[0] && apiData?.SORATRK_TERM_CODE[0]) {
            this.conn.execute("""delete from soratrk where SORATRK_TERM_CODE = ?""", [apiData?.SORATRK_TERM_CODE[0]])
        } else {

            def alreadyExists = isExistingSoratrk([
                    apiData.SORATRK_TERM_CODE[0].value()[0],
                    apiData.SORATRK_PTRM_CODE[0].value() ? apiData.SORATRK_PTRM_CODE[0].value()[0] : 'NULL',
                    apiData.SORATRK_SUBJ_CODE[0].value() ? apiData.SORATRK_SUBJ_CODE[0].value()[0] : 'NULL',
                    apiData.SORATRK_CRSE_NUMB[0].value() ? apiData.SORATRK_CRSE_NUMB[0].value()[0] : 'NULL',
                    apiData.SORATRK_CRN[0].value() ? apiData.SORATRK_CRN[0].value()[0] : 'NULL'])

            if (!alreadyExists) { // not currently supporting update. if it exists, skip it
                def nextSeq = this.conn.firstRow("""SELECT SORATRK_SEQUENCE.NEXTVAL AS ID FROM DUAL""")?.ID
                if (nextSeq) {
                    apiData.SORATRK_SEQ_NO[0].setValue(nextSeq)
                }

                // parse the xml  back into gstring for the dynamic sql loader
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
    }

    private def isExistingSoratrk( List myData ) {
        def exists

        def theQuery = "SELECT SORATRK_SEQ_NO AS ID FROM SORATRK where SORATRK_TERM_CODE = ? " + 
            "AND SORATRK_PTRM_CODE" + (myData[1] != 'NULL' ? ' = ? ' : ' IS NULL ') +
            "AND SORATRK_SUBJ_CODE" + (myData[2] != 'NULL' ? ' = ? ' : ' IS NULL ') +
            "AND SORATRK_CRSE_NUMB" + (myData[3] != 'NULL' ? ' = ? ' : ' IS NULL ') +
            "AND SORATRK_CRN"       + (myData[4] != 'NULL' ? ' = ? ' : ' IS NULL')

        if (myData[4] == 'NULL') {myData.removeAt(4)}
        if (myData[3] == 'NULL') {myData.removeAt(3)}
        if (myData[2] == 'NULL') {myData.removeAt(2)}
        if (myData[1] == 'NULL') {myData.removeAt(1)}

        try {
            exists = this.conn.firstRow( theQuery, myData)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not query SORATRK record in SoratrkDML for ${connectInfo.tableName}. $e.message" )
        }
        return exists
    }
}
