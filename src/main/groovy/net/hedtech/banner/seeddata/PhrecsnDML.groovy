/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Phrecsn DML tables
 * fetch associated ID for PHRECSN_PTRECPD_ID
 */

public class PhrecsnDML {
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


    public PhrecsnDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPhrecsn()

    }

    /**
     * Process the phrecsn records.  The PHRECSN_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPhrecsn(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PHRECSN_PHRECRT_ID[0]?.value()[0];
        def paramList = combinedCode.tokenize('-')
        Map idMap = [:]
        componentId = fetchPtrecrtId(paramList)

        if (componentId) {
            apiData.PHRECSN_PHRECRT_ID[0].setValue(componentId?.toString())

            idMap = fetchPhrecsnIds([componentId])
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


    private def fetchPtrecrtId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT e.PHRECRT_ID as ID FROM PTRECPD p , phrecrt e WHERE PTRECPD_COAS_CODE = ?  AND PTRECPD_ECPD_CODE = ? and p.ptrecpd_id = E.PHRECRT_PTRECPD_ID
                                        and SPKLIBS.F_GET_SPRIDEN_ID(E.PHRECRT_PIDM) = ? """,
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PHRECSN ID in PhrecsnDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PHRECSN ID for ${connectInfo.tableName}.")
        return id
    }


    private Map fetchPhrecsnIds(List paramList){
        Map result = [:]
        //
        try {
            result = this.conn.firstRow("""SELECT PHRECSN_ID as UNIQUEID, PHRECSN_SURROGATE_ID AS ID FROM PHRECSN WHERE PHRECSN_PHRECRT_ID = ? """,
                    paramList)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing fetchPhrecsnIds in PhrecsnDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing fetchPhrecsnIds for ${connectInfo.tableName}.")
        return result
    }


}