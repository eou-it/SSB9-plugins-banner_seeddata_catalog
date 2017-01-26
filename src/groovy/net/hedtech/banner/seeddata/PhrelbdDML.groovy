/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Phrelbd DML tables
 * update the sequence generated IDs for PHRELBD_ID
 */

public class PhrelbdDML {
    int currRule = 0
    def componentId = null
    def subComponentId = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public PhrelbdDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPhrelbd()
    }

    /**
     * Process the phrelbd records.  The PHRELBD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPhrelbd(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = true

        if (!(apiData.PHRELBD_ID[0]?.value()[0])) {
            List paramList = [apiData.PHRELBD_YEAR[0]?.value()[0], apiData.PHRELBD_PICT_CODE[0]?.value()[0],
                             apiData.PHRELBD_PAYNO[0]?.value()[0], apiData.PHRELBD_ORGN_CODE_TS[0]?.value()[0],
                             apiData.PHRELBD_TS_ROSTER_IND[0]?.value()[0], fetchEmployeePidm([apiData.BANNERID[0]?.value()[0]]),
                             apiData.PHRELBD_SEQ_NO[0]?.value()[0], apiData.PHRELBD_POSN[0]?.value()[0],
                             apiData.PHRELBD_SUFF[0]?.value()[0], apiData.PHRELBD_EARN_CODE[0]?.value()[0],
                             apiData.PHRELBD_SHIFT[0]?.value()[0], apiData.PHRELBD_GEN_IND[0]?.value()[0]]

            componentId = fetchPhrelbdId(paramList)

            if (componentId) {
                apiData.PHRELBD_ID[0].setValue(componentId?.toString())
                isValid = true
            }
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


    private def fetchPhrelbdId(String coasCode, String ecpdCode){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT PHRELBD_ID AS ID FROM PHRELBD WHERE where PHRELBD_YEAR = ? AND PHRELBD_PICT_CODE = ?
                                                         AND PHRELBD_PAYNO = ? AND PHRELBD_ORGN_CODE_TS = ? AND PHRELBD_TS_ROSTER_IND = ?
                                                         AND PHRELBD_PIDM = ? AND PHRELBD_SEQ_NO = ? AND PHRELBD_POSN = ? AND PHRELBD_SUFF = ?
                                                         AND PHRELBD_EARN_CODE = ? AND PHRELBD_SHIFT = ? AND PHRELBD_GEN_IND = ? """,
                    [coasCode,ecpdCode])?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PHRELBD ID in PhrelbdDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PHRELBD ID for ${connectInfo.tableName}.")
        return id
    }


    private def fetchEmployeePidm(List paramList){
        def id = null
        //
        try {
            id = this.conn.firstRow("""SELECT SPRIDEN_PIDM as PIDM from SPRIDEN where SPRIDEN_ID = ? and SPRIDEN_CHANGE_IND is null """,
                    paramList)?.PIDM
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for Employee Pidm in PhrecrsDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for Employee Pidm for ${connectInfo.tableName}.")
        return id
    }
}