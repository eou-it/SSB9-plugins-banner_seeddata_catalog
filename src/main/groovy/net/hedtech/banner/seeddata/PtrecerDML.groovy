/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ptrecer DML tables
 * fetch associated ID for PTRECER_PTRECPD_ID
 */

public class PtrecerDML {
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


    public PtrecerDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPtrecer()

    }

    /**
     * Process the ptrecer records.  The PTRECER_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPtrecer(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PTRECER_PTRECPD_ID[0]?.value()[0]
        def paramList = combinedCode.tokenize('-')
        def ptrecerId = null

        componentId = fetchPtrecpdId(paramList)


        if (componentId) {
            apiData.PTRECER_PTRECPD_ID[0].setValue(componentId?.toString())
            ptrecerId = fetchPtrecerId([componentId,apiData.PTRECER_EARN_CODE[0]?.value()[0]])
                                        apiData.PTRECER_ID[0].setValue(ptrecerId?.toString())

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
            if (connectInfo.showErrors) println("Error while checking for existing PTRECER ID in PtrecerDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECER ID for ${connectInfo.tableNa me}.")
        return id
    }

    private def fetchPtrecerId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow(""" SELECT PTRECER_ID AS ID FROM PTRECER WHERE PTRECER_PTRECPD_ID = ? and PTRECER_EARN_CODE = ? """,
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECER ID in PtrecerDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECER ID for ${connectInfo.tableName}.")
        return id
    }


}