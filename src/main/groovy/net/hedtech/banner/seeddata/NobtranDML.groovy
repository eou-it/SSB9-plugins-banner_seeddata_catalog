/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Nobtran DML tables
 */

public class NobtranDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public NobtranDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNobtran()

    }

    /**
     * Process the nobtran records.  The nobtran_transaction_no is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processNobtran() {
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def epafTransaction = apiData.BANNERID[0]?.value()[0] + '-' + apiData.NOBTRAN_ACAT_CODE[0]?.value()[0];
        def paramList = epafTransaction.tokenize('-')
        deleteTransaction(paramList)
        def transactionId = fetchNewTransactionId()

        if (transactionId) {
            apiData.NOBTRAN_TRANSACTION_NO[0].setValue(transactionId?.toString())
            apiData.NOBTRAN_PIDM[0].setValue(fetchEmployeePidm([apiData.BANNERID[0]?.value()[0]]))

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


    private def deleteTransaction(paramList) {

        def transactionId = this.conn.firstRow("""SELECT nobtran_transaction_no as transactionId from NOBTRAN,SPRIDEN where nobtran_pidm = spriden_pidm AND spriden_id = ? and nobtran_acat_code = ? AND spriden_change_ind IS NULL""", paramList)?.transactionId
        deleteData("NOREAER", "delete from noreaer where noreaer_trans_no = ?", transactionId)
        deleteData("NORCMNT", "delete from norcmnt where norcmnt_transaction_no = ?", transactionId)
        deleteData("NORTERN", "delete from nortern where nortern_transaction_no = ?", transactionId)
        deleteData("NORTLBD", "delete from nortlbd where nortlbd_transaction_no = ?", transactionId)
        deleteData("NORROUT", "delete from norrout where norrout_transaction_no = ?", transactionId)
        deleteData("NORTRAN", "delete from nortran where nortran_transaction_no = ?", transactionId)
        deleteData("NOBTRAN", "delete from nobtran where nobtran_transaction_no = ?", transactionId)

    }

    private def fetchNewTransactionId() {
        def transactionId = null

        //
        try {
            transactionId = this.conn.firstRow("""SELECT nobtran_seq_no.nextval AS transactionId FROM DUAL """)?.transactionId
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while generating transaction Id in NobtranDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Generating transaction Id for ${connectInfo.tableName}.")
        return transactionId
    }

    private def fetchEmployeePidm(List paramList) {
        def id = null
        //
        try {
            id = this.conn.firstRow("""SELECT SPRIDEN_PIDM as PIDM from SPRIDEN where SPRIDEN_ID = ? and SPRIDEN_CHANGE_IND is null """,
                    paramList)?.PIDM
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for Employee Pidm in NobtranDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for Employee Pidm for ${connectInfo.tableName}.")
        return id
    }

    private def deleteData(String tableName, String sql, transactionId) {
        try {
            int delRows = conn.executeUpdate(sql, [transactionId])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing ${tableName} delete for ${transactionId} from NobtranDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }
}