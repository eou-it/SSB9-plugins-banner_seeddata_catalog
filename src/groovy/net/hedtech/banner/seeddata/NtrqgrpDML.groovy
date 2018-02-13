/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Creates group participants
 */
public class NtrqgrpDML {
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


    public NtrqgrpDML (InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNtrqgrp()

    }


    /**
     * Process the ntrqgrp records.  The NTRQGRP_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processNtrqgrp( ) {
        def apiData = new XmlParser().parseText( xmlData )
        def isValid = false

        componentId = fetchNextValFromSequenceGenerator()

        if (componentId ) {
            apiData.NTRQGRP_ID[0].setValue(componentId?.toString())
            def ntrqprtId = apiData.NTRQGRP_NTRQPRT_ID[0]?.value()[0]
            def paramList = ntrqprtId.tokenize('-')
            def qprtId = fetchNtrqprtId(paramList)
            apiData.NTRQGRP_NTRQPRT_ID[0].setValue(qprtId)

            deleteNtrqprtById([qprtId, apiData.NTRQGRP_MEMBER_USER_ID[0]?.value()[0]])
            isValid = true;
        }


        if (isValid) {
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() {fields ->
                def value = fields.text().replaceAll( /&/, '' ).replaceAll( /'/, '' ).replaceAll( />/, '' ).replaceAll( /</, '' )
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            if (connectInfo.debugThis) {
                println "Ntrqgrp Record member ${apiData.NTRQGRP_MEMBER_USER_ID.text()} "
            }

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )
        }
    }

    private def fetchNextValFromSequenceGenerator( ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT NTRQGRP_SEQUENCE.NEXTVAL AS ID FROM DUAL""" )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get next val from NTRQGRP_SEQUENCE in NtrqgrpDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Next val selected from NTRQGRP_SEQUENCE for ${connectInfo.tableName}." )
        return id
    }


    private def fetchNtrqprtId(List paramList ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT NTRQPRT_ID as ID FROM NTRQPRT where NTRQPRT_COAS_CODE = ? and NTRQPRT_QPRT_CODE = ? """ ,  paramList )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get NTRQPRT_ID in NtrqgrpDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "NTRQPRT_ID for ${connectInfo.tableName}." )
        return id
    }

    private def deleteNtrqprtById(List paramList ) {
        def count = 0
        try {
            count = this.conn.executeUpdate( """DELETE FROM NTRQGRP where NTRQGRP_NTRQPRT_ID = ? AND NTRQGRP_MEMBER_USER_ID = ? """ ,  paramList )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get Delete existing Ntrqgrp  record in NtrqgrpDML for ${connectInfo.tableName}. $e.message" )
        }
        return count
    }
}


