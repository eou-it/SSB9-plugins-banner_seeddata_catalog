/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ptrecpc DML tables
 * fetch associated ID for PTRECPC_PTRECPD_ID
 */

public class PtrecpcDML {
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


    public PtrecpcDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPtrecpc()

    }

    /**
     * Process the ptrecpc records.  The PTRECPC_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPtrecpc(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PTRECPC_PTRECPD_ID[0]?.value()[0]
        def paramList = combinedCode.tokenize('-')
        def ptrecpcId = null

        componentId = fetchPtrecpdId(paramList)


        if (componentId) {
            apiData.PTRECPC_PTRECPD_ID[0].setValue(componentId?.toString())
            ptrecpcId = fetchPtrecpcId([componentId,apiData.PTRECPC_PCLS_CODE[0]?.value()[0]])
            apiData.PTRECPC_ID[0].setValue(ptrecpcId?.toString())

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
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPC ID in PtrecpcDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPC ID for ${connectInfo.tableName}.")
        return id
    }

    private def fetchPtrecpcId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow(""" SELECT PTRECPC_ID AS ID FROM PTRECPC WHERE PTRECPC_PTRECPD_ID = ? and PTRECPC_PCLS_CODE = ?""",
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPC ID in PtrecpcDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPC ID for ${connectInfo.tableName}.")
        return id
    }


}