/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ptrecpr DML tables
 * fetch associated ID for PTRECPR_PTRECPD_ID
 */

public class PtrecprDML {
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


    public PtrecprDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPtrecpr()

    }

    /**
     * Process the ptrecpr records.  The PTRECPR_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPtrecpr(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PTRECPR_PTRECPD_ID[0]?.value()[0]
        def paramList = combinedCode.tokenize('-')
        def ptrecprId = null

        componentId = fetchPtrecpdId(paramList)


        if (componentId) {
            apiData.PTRECPR_PTRECPD_ID[0].setValue(componentId?.toString())
            ptrecprId = fetchPtrecprId([componentId,apiData.PTRECPR_PICT_CODE[0]?.value()[0],
                                        apiData.PTRECPR_START_YEAR[0]?.value()[0],
                                        apiData.PTRECPR_START_PAYNO[0]?.value()[0]])
            apiData.PTRECPR_ID[0].setValue(ptrecprId?.toString())

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


    private def fetchPtrecpdId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT PTRECPD_ID AS ID FROM PTRECPD WHERE PTRECPD_COAS_CODE = ?  AND PTRECPD_ECPD_CODE = ?""",
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPR ID in PtrecprDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPR ID for ${connectInfo.tableNa me}.")
        return id
    }

    private def fetchPtrecprId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow(""" SELECT PTRECPR_ID AS ID FROM PTRECPR WHERE PTRECPR_PTRECPD_ID = ? and PTRECPR_PICT_CODE = ?
                                                          and PTRECPR_START_YEAR = ? and PTRECPR_START_PAYNO = ?""",
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPR ID in PtrecprDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPR ID for ${connectInfo.tableName}.")
        return id
    }


}