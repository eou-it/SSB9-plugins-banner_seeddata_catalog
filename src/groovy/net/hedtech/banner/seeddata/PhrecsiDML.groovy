/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Phrecsi DML tables
 * fetch associated ID for PHRECSI_PHRECRT_ID
 */

public class PhrecsiDML {
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


    public PhrecsiDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPhrecsi()

    }

    /**
     * Process the phrecsi records.  The PHRECSI_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPhrecsi(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PHRECSI_PHRECSN_ID[0]?.value()[0]
        def paramList = combinedCode.tokenize('-')
        Map idMap = [:]
        componentId = fetchPtrecsnId(paramList)

        if (componentId) {
            apiData.PHRECSI_PHRECSN_ID[0].setValue(componentId?.toString())
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


    private def fetchPtrecsnId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT s.PHRECSN_ID as ID FROM PTRECPD p , phrecrt e , phrecsn s WHERE PTRECPD_COAS_CODE = ?
                                        AND PTRECPD_ECPD_CODE = ? AND p.ptrecpd_id = E.PHRECRT_PTRECPD_ID  AND s.PHRECSN_PHRECRT_ID = e.PHRECRT_ID
                                        AND SPKLIBS.F_GET_SPRIDEN_ID(E.PHRECRT_PIDM) = ? AND s.PHRECSN_SECTION_TYPE = ? """,
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PHRECSI ID in PhrecsiDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PHRECSI ID for ${connectInfo.tableName}.")
        return id
    }


}