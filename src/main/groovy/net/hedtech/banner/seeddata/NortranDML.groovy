/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Nortran DML tables
 */

public class NortranDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public NortranDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNortran()

    }

    /**
     * Process the nortran records.
     */

    def processNortran() {
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def epafTransaction = apiData.BANNERID[0]?.value()[0] + '-' + apiData.NOBTRAN_ACAT_CODE[0]?.value()[0];
        def paramList = epafTransaction.tokenize('-')
        def transactionId = fetchTransactionId(paramList)

        if (transactionId) {
            apiData.NORTRAN_TRANSACTION_NO[0].setValue(transactionId?.toString())
            isValid = true
        }

        if (isValid) {
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

    private def fetchTransactionId(paramList) {
        def transactionId = null

        //
        try {
            transactionId = this.conn.firstRow("""SELECT nobtran_transaction_no as transactionId from NOBTRAN,SPRIDEN where nobtran_pidm = spriden_pidm AND spriden_id = ? and nobtran_acat_code = ? AND spriden_change_ind IS NULL """, paramList)?.transactionId
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while generating transaction Id in NortranDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Generating transaction Id for ${connectInfo.tableName}.")
        return transactionId
    }
}