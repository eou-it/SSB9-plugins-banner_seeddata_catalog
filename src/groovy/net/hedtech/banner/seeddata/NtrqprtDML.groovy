/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ntrecdq DML tables
 * update the sequence generated IDs for Ntrecdq_ID
 */

public class NtrqprtDML {
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


    public NtrqprtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
         processNtrqprt()

    }


    /**
     * Process the ntrqprt records.  The NTRQPRT_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processNtrqprt( ) {
        def apiData = new XmlParser().parseText( xmlData )
        def isValid = false

                componentId = fetchNextValFromSequenceGenerator()

                if (componentId ) {
                    apiData.NTRQPRT_ID[0].setValue(componentId?.toString())
                    isValid = true;
                    List paramList = [apiData.NTRQPRT_COAS_CODE[0]?.value()[0],apiData.NTRQPRT_QPRT_CODE[0]?.value()[0]]
                    if(fetchNtrqprtId(paramList))
                    {
                        isValid = false;
                    }

                }


        if (isValid) {
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() {fields ->
                def value = fields.text().replaceAll( /&/, '' ).replaceAll( /'/, '' ).replaceAll( />/, '' ).replaceAll( /</, '' )
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )
        }
    }

    private def fetchNextValFromSequenceGenerator( ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT NTRQPRT_SEQUENCE.NEXTVAL AS ID FROM DUAL""" )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get next val from NTRQPRT_SEQUENCE in NtrqprtDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Next val selected from NTRQPRT_SEQUENCE for ${connectInfo.tableName}." )
        return id
    }


    private def fetchNtrqprtId(List paramList ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT NTRQPRT_ID as ID FROM NTRQPRT where NTRQPRT_COAS_CODE = ? and NTRQPRT_QPRT_CODE = ? """ ,  paramList )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get NTRQPRT_ID in NtrecdqDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "NTRQPRT_ID for ${connectInfo.tableName}." )
        return id
    }


}


