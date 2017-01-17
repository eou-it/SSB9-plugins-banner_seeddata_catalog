/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Phrecrs DML tables
 * fetch associated ID for PHRECRS_PHRECRT_ID
 */

public class PhrecrsDML {
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


    public PhrecrsDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPhrecrs()

    }

    /**
     * Process the phrecrs records.  The PHRECRS_PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPhrecrs(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def combinedCode = apiData.PHRECRS_PHRECRT_ID[0]?.value()[0];
        def paramList = combinedCode.tokenize('-')
        Map idMap = [:]
        componentId = fetchPhrecrtId(paramList)

        if (componentId) {
            apiData.PHRECRS_PHRECRT_ID[0].setValue(componentId?.toString())
            apiData.PHRECRS_ACTING_EMPLOYEE_PIDM[0].setValue(fetchEmployeePidm([apiData.PHRECRS_ACTING_EMPLOYEE_PIDM[0]?.value()[0]]))
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


    private def fetchPhrecrtId(List paramList){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT e.PHRECRT_ID as ID FROM PTRECPD p , phrecrt e WHERE PTRECPD_COAS_CODE = ?  AND PTRECPD_ECPD_CODE = ? and p.ptrecpd_id = E.PHRECRT_PTRECPD_ID
                                        and SPKLIBS.F_GET_SPRIDEN_ID(E.PHRECRT_PIDM) = ? """,
                    paramList)?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PHRECRS ID in PhrecrsDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PHRECRS ID for ${connectInfo.tableName}.")
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