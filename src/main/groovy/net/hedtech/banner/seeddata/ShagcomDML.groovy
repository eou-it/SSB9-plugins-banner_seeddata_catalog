/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * SHAGCOM tables
 * update the sequence generated IDs for component and subcomponent
 */

public class ShagcomDML {
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


    public ShagcomDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        if (deleteNode && deleteNode?.text() == "YES") processDelete()
        else processShagcom()

    }


    def processDelete( ) {
        def apiData = new XmlParser().parseText( xmlData )
        String delSql = """DELETE FROM ${connectInfo.tableName} where """
        if (connectInfo.tableName == "SHRGCOM") {
            delSql += """ SHRGCOM_TERM_CODE = ${apiData.SHRGCOM_TERM_CODE.text()} AND SHRGCOM_CRN = ${apiData.SHRGCOM_CRN.text()}"""
        } else if (connectInfo.tableName == "SHRSCOM") {
            delSql += """ SHRSCOM_TERM_CODE = ${apiData.SHRSCOM_TERM_CODE.text()} AND SHRSCOM_CRN = ${apiData.SHRSCOM_CRN.text()}"""
        } else if (connectInfo.tableName == "SHRMRKS") {
            delSql += """ SHRMRKS_TERM_CODE = ${apiData.SHRMRKS_TERM_CODE.text()} AND SHRMRKS_CRN = ${apiData.SHRMRKS_CRN.text()}"""
        } else if (connectInfo.tableName == "SHRSMRK") {
            delSql += """ SHRSMRK_TERM_CODE = ${apiData.SHRSMRK_TERM_CODE.text()} AND SHRSMRK_CRN = ${apiData.SHRSMRK_CRN.text()}"""
        }
        try {
            int delRows = conn.executeUpdate( delSql )
            connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 0, delRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  delete for ${connectInfo.tableName} from ShagcomDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

    /**
     * Process the shagcom records.  The component ID and subcomponent ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     * If it is a SHRSCOM record then the component ID has to be updated from the database.
     */

    def processShagcom( ) {
        def apiData = new XmlParser().parseText( xmlData )
        def isValid = false
        switch (connectInfo.tableName) {
            case 'SHRGCOM':
                componentId = fetchComponentId( apiData.SHRGCOM_TERM_CODE.text(), apiData.SHRGCOM_CRN.text(), apiData.SHRGCOM_NAME.text() )
                if (!componentId) {
                    componentId = fetchNextValFromSequenceGenerator()
                }
                if (componentId && apiData.SHRGCOM_ID.text().toInteger() != componentId) {
                    apiData.SHRGCOM_ID[0].setValue( componentId.toString() )
                }
                isValid = true
                break
            case 'SHRSCOM':
                componentId = fetchComponentId( apiData.SHRSCOM_TERM_CODE.text(), apiData.SHRSCOM_CRN.text(), apiData.COMPONENTNAME.text() )
                if (componentId) {
                    subComponentId = fetchSubComponentId( apiData.SHRSCOM_TERM_CODE.text(), apiData.SHRSCOM_CRN.text(), componentId, apiData.SHRSCOM_NAME.text() )
                    if (!subComponentId) {
                        subComponentId = fetchNextValFromSequenceGenerator()
                    }
                    if (apiData.SHRSCOM_GCOM_ID.text().toInteger() != componentId) {
                        apiData.SHRSCOM_GCOM_ID[0].setValue( componentId.toString() )
                    }
                    if (subComponentId && apiData.SHRSCOM_ID.text().toInteger() != subComponentId) {
                        apiData.SHRSCOM_ID[0].setValue( subComponentId.toString() )
                    }
                    isValid = true
                } else {
                    isValid = false
                    println( "Error: Could not find component for term:${apiData.SHRSCOM_TERM_CODE.text()}, crn:${apiData.SHRSCOM_CRN.text()} and component name:${apiData.COMPONENTNAME.text()} in ShagcomDML for ${connectInfo.tableName}" )
                    connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 1, 0 )
                }
                break
            case 'SHRMRKS':
                componentId = fetchComponentId( apiData.SHRMRKS_TERM_CODE.text(), apiData.SHRMRKS_CRN.text(), apiData.COMPONENTNAME.text() )
                if (componentId) {
                    if (apiData.SHRMRKS_GCOM_ID.text().toInteger() != componentId) {
                        apiData.SHRMRKS_GCOM_ID[0].setValue( componentId.toString() )
                    }
                    isValid = true
                } else {
                    isValid = false
                    println( "Error: Could not find component for term:${apiData.SHRMRKS_TERM_CODE.text()}, crn:${apiData.SHRMRKS_CRN.text()} and component name:${apiData.COMPONENTNAME.text()} in ShagcomDML for ${connectInfo.tableName}" )
                    connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 1, 0 )
                }
                break
            case 'SHRSMRK':
                componentId = fetchComponentId( apiData.SHRSMRK_TERM_CODE.text(), apiData.SHRSMRK_CRN.text(), apiData.COMPONENTNAME.text() )
                if (componentId) {
                    subComponentId = fetchSubComponentId( apiData.SHRSMRK_TERM_CODE.text(), apiData.SHRSMRK_CRN.text(), componentId, apiData.SUBCOMPONENTNAME.text() )
                    if (subComponentId) {
                        if (apiData.SHRSMRK_GCOM_ID.text().toInteger() != componentId) {
                            apiData.SHRSMRK_GCOM_ID[0].setValue( componentId.toString() )
                        }
                        if (apiData.SHRSMRK_SCOM_ID.text().toInteger() != subComponentId) {
                            apiData.SHRSMRK_SCOM_ID[0].setValue( subComponentId.toString() )
                        }
                        isValid = true
                    } else {
                        isValid = false
                        println( "Error: Could not find sub-component for term:${apiData.SHRSMRK_TERM_CODE.text()}, crn:${apiData.SHRSMRK_CRN.text()} and component name:${apiData.COMPONENTNAME.text()} in ShagcomDML for ${connectInfo.tableName}" )
                        connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 1, 0 )
                    }
                } else {
                    isValid = false
                    println( "Error: Could not find component for term:${apiData.SHRSMRK_TERM_CODE.text()}, crn:${apiData.SHRSMRK_CRN.text()} and component name:${apiData.COMPONENTNAME.text()} in ShagcomDML for ${connectInfo.tableName}" )
                    connectInfo.tableUpdate( connectInfo.tableName, 0, 0, 0, 1, 0 )
                }
                break
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


    private def fetchComponentId( String term, String courseReferenceNumber, String componentName ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT SHRGCOM_ID AS ID FROM SHRGCOM WHERE SHRGCOM_TERM_CODE = ? AND SHRGCOM_CRN = ? AND SHRGCOM_NAME = ?""",
                    [term, courseReferenceNumber, componentName] )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Error while checking for existing component ID in ShagcomDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Checking for existing component ID for ${connectInfo.tableName}." )
        return id
    }


    private def fetchSubComponentId( String term, String courseReferenceNumber, def componentId, String subComponentName ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT SHRSCOM_ID AS ID FROM SHRSCOM WHERE SHRSCOM_TERM_CODE = ? AND SHRSCOM_CRN = ? AND SHRSCOM_GCOM_ID=? AND SHRSCOM_NAME = ?""",
                    [term, courseReferenceNumber, componentId, subComponentName] )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Error while checking for existing subcomponent ID in ShagcomDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Checking for existing subcomponent ID for ${connectInfo.tableName}." )
        return id
    }


    private def fetchNextValFromSequenceGenerator( ) {
        def id = null
        try {
            id = this.conn.firstRow( """SELECT SHBGSEQ.NEXTVAL AS ID FROM DUAL""" )?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println( "Could not get next val from SHBGSEQ in ShagcomDML for ${connectInfo.tableName}. $e.message" )
        }
        if (connectInfo.debugThis) println( "Next val selected from SHBGSEQ for ${connectInfo.tableName}." )
        return id
    }
}
