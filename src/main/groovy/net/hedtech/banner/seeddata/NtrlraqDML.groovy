/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ntrlrrq DML tables
 * update the sequence generated IDs for NTRLRRQ_ID
 */

public class NtrlraqDML {
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


    public NtrlraqDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
         processNtrlrrq()

    }


    def deleteNtrlraq( ) {
        def apiData = new XmlParser().parseText( xmlData )
        String delSql = """DELETE FROM NTRLRAQ WHERE NTRLRAQ_COAS_CODE = ?  AND NTRLRAQ_ORGN_CODE = ?
                                                    AND NTRLRAQ_SEQ_NO = ? AND NTRLRAQ_MEMBER_USER_ID = ? """

        try {
            int delRows = conn.executeUpdate( delSql , [apiData.NTRLRAQ_COAS_CODE.text(), apiData.NTRLRAQ_ORGN_CODE.text(),
                                                        apiData.NTRLRAQ_SEQ_NO.text() , apiData.NTRLRAQ_MEMBER_USER_ID.text()] )
            connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 0, delRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  delete for ${connectInfo.tableName} from NtrlraqDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

    /**
     * Process the ntrlraq records.  The NTRLRAQ_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processNtrlrrq( ) {
        def apiData = new XmlParser().parseText( xmlData )
        def isValid = false

                deleteNtrlraq()
                componentId = fetchNextValFromSequenceGenerator()

                if (componentId ) {
                    apiData.NTRLRAQ_ID[0].setValue( componentId?.toString())
                    isValid = true
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
                println "Ntrlrrq Record Position ${apiData.NTRLRAQ_MEMBER_USER_ID.text()} "
            }

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )
        }
    }


    private def fetchNtrlraqId( String coasCode, String orgnCode, String seqNumber, String memberUserId ) {
        def id = null
        
        //
        try {
            id = this.conn.firstRow( """SELECT NTRLRAQ_ID AS ID FROM NTRLRAQ WHERE NTRLRAQ_COAS_CODE = ?  AND NTRLRAQ_ORGN_CODE = ?
                                                    AND NTRLRAQ_SEQ_NO = ? AND NTRLRAQ_MEMBER_USER_ID = ?""",
                    [coasCode,  orgnCode,  seqNumber, memberUserId] )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Error while checking for existing component ID in NtrlraqDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Checking for existing component ID for ${connectInfo.tableName}." )
        return id
    }

    private def fetchNextValFromSequenceGenerator( ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT NTRLRAQ_SEQUENCE.NEXTVAL AS ID FROM DUAL""" )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get next val from NTRLRAQ_SEQUENCE in NtrlraqDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Next val selected from NTRLRAQ_SEQUENCE for ${connectInfo.tableName}." )
        return id
    }
}
