/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import org.apache.commons.lang.StringUtils
import java.sql.Connection


/**
 * Action Item tables
 * replace the curr rule in the xml
 */

public class GeneralActionItemDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    int folderId, templateId, statusId, actionItemId, actionGroupId, blockId, populationId, queryId


    public GeneralActionItemDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode

        processData()
    }

    /**
     * Process the action item records.   The item attribute gives us the surrogate key to the gcbactm which
     * is used as fk in other records.
     *
     */
    def processData() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser
        def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
        def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]

        def apiData = new XmlParser().parseText( StringUtils.replaceEach( xmlData, fromstring, tostring ) )
        def personId = apiData.BANNERID?.text()
        def personPidm

        // connectInfo.debugThis = true

        if (personId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            def spridenRow = conn.firstRow( findPidm, [personId] )
            if (spridenRow) {
                personPidm = spridenRow.SPRIDEN_PIDM.toString()
            }
        }

        if (connectInfo.tableName == "GCVASTS") {
            actionItemId = 0
            actionItemId = getStatusId( apiData.ACTIONITEMSTATUS[0]?.text().toString() )
            deleteData()
        }

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBACTM") {

            actionItemId = getActionItemId( apiData.GCBACTM_NAME[0]?.text().toString() )
            folderId = getFolderId( apiData.FOLDER[0]?.text().toString() )

            if (actionItemId == 0) {
                actionItemId = apiData.GCBACTM_SURROGATE_ID[0]?.text().toInteger()
            } else {
                //
            }

            if (folderId == 0) {
                folderId = apiData.GCBACTM_GCRFLDR_ID[0]?.text().toInteger()
            }

            apiData.GCBACTM_GCRFLDR_ID[0].setValue( folderId.toString() )

        }


        if (connectInfo.tableName == "GCVASTS") {
            if (statusId == 0) {
                statusId = apiData.GCVASTS_SURROGATE_ID[0]?.text().toInteger()
            }
        }


        if (connectInfo.tableName == "GCRAACT") {

            actionItemId = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            statusId = getStatusId( apiData.ACTIONITEMSTATUS[0]?.text().toString() )
            actionGroupId = getActionGroupId( apiData.ACTIONGROUPNAME[0]?.text().toString() )

            if (actionItemId == 0) {
                actionItemId = apiData.GCRAACT_GCBACTM_ID[0]?.text().toInteger()
            }
            if (statusId == 0) {
                statusId = apiData.GCRAACT_GCVASTS_ID[0]?.text().toInteger()
            }
            if (actionGroupId == 0) {
                actionGroupId = apiData.GCRAACT_GCBAGRP_ID[0]?.text()?.toInteger()
            }
            apiData.GCRAACT_PIDM[0].setValue( personPidm )
            apiData.GCRAACT_GCBACTM_ID[0].setValue( actionItemId.toString() )
            apiData.GCRAACT_GCVASTS_ID[0].setValue( statusId.toString() )
            apiData.GCRAACT_GCBAGRP_ID[0].setValue( actionGroupId.toString() )
        }

        if (connectInfo.tableName == "GCRACNT") {
            //replace sequence number with current
            actionItemId = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )

            if (actionItemId == 0) {
                actionItemId = apiData.GCRACNT_GCBACTM_ID[0]?.text().toInteger()
            }

            apiData.GCRACNT_GCBACTM_ID[0].setValue( actionItemId.toString() )

            if (apiData.TEMPLATENAME[0]?.text()) {
                templateId = getTemplateId( apiData.TEMPLATENAME[0]?.text().toString() )

                if (templateId == 0) {
                    templateId = apiData.GCRACNT_GCBPBTR_ID[0]?.text().toInteger()
                }
                apiData.GCRACNT_GCBPBTR_ID[0].setValue( templateId.toString() )
            }
        }

        if (connectInfo.tableName == "GCBPBTR") {
            //todo: set up pagebuilder records
        }

        if (connectInfo.tableName == "GCBAGRP") {
            //clear out current group data w/folder information in xml. gcrfldrdml will process new records.

            folderId = getFolderId( apiData.FOLDER[0]?.text().toString() )

            if (folderId == 0) {
                folderId = apiData.GCBAGRP_GCRFLDR_ID[0]?.text().toInteger()
            }

            apiData.GCBAGRP_GCRFLDR_ID[0].setValue( folderId.toString() )
        }

        if (connectInfo.tableName == "GCRAISR") {

            actionItemId = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            statusId = getStatusId( apiData.STATUSNAME[0]?.text().toString() )

            if (actionItemId == 0) {
                actionItemId = apiData.GCRAISR_GCBACTM_ID[0]?.text().toInteger()
            }

            if (statusId == 0) {
                println "Problem getting status id from GeneralActionItemDML.groovy for ${connectInfo.tableName}"
                statusId = apiData.GCRAISR_GCVASTS_ID[0]?.text().toInteger()
            }

            apiData.GCRAISR_GCBACTM_ID[0].setValue( actionItemId.toString() )
            apiData.GCRAISR_GCVASTS_ID[0].setValue( statusId.toString() )

        }

        if (connectInfo.tableName == "GCRABLK") {

            actionItemId = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            //statusId = getStatusId( apiData.STATUSNAME[0]?.text().toString() )

            if (actionItemId == 0) {
                actionItemId = apiData.GCRABLK_GCBACTM_ID[0]?.text().toInteger()
            }

            apiData.GCRABLK_GCBACTM_ID[0].setValue( actionItemId.toString() )

        }

        if (connectInfo.tableName == "GCRAGRA") {
            actionItemId = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            actionGroupId = getActionGroupId( apiData.ACTIONGROUPNAME[0]?.text().toString() )
            apiData.GCRAGRA_GCBACTM_ID[0].setValue( actionItemId.toString() )
            apiData.GCRAGRA_GCBAGRP_ID[0].setValue( actionGroupId.toString() )

        }
        if (connectInfo.tableName == "GCBPOPL") {
            folderId = getFolderId( apiData.FOLDERNAME[0]?.text().toString() )
            apiData.GCBPOPL_FOLDER_ID[0].setValue( folderId.toString() )
        }

        if (connectInfo.tableName == "GCRPOPV") {
            def populationId = getPopulationId( apiData.POPULATIONNAME[0]?.text().toString() )
            def slisId = getSelectionId()
            apiData.GCRPOPV_POPL_ID[0].setValue( populationId.toString() )
            apiData.GCRPOPV_INCLUDE_LIST_ID[0].setValue (slisId.toString())
        }
        /*
        if (connectInfo.tableName == "GCBQURY") {
            queryId = getQueryId( apiData.GCBQURY_NAME[0]?.text().toString() )
            deleteQuery()
            def folderId = getFolderId( apiData.FOLDERNAME[0]?.text().toString() )
            apiData.GCBQURY_FOLDER_ID[0].setValue( folderId.toString() )
        }
        */

        if (connectInfo.tableName == "GCRQRYV") {
            def queryId = getQueryId( apiData.QUERYNAME[0]?.text().toString() )
            apiData.GCRQRYV_QUERY_ID[0].setValue( queryId.toString() )
        }
        if (connectInfo.tableName == "GCRPQID") {
            def queryId = getQueryId( apiData.QUERYNAME[0]?.text().toString() )
            def populationId = getPopulationId( apiData.POPULATIONNAME[0]?.text().toString() )
            apiData.GCRPQID_POPL_ID[0].setValue( populationId.toString() )
            apiData.GCRPQID_QURY_ID[0].setValue( queryId.toString() )
        }

        if (connectInfo.tableName == "GCRPVID") {
            def queryId = getQueryId( apiData.QUERYNAME[0]?.text().toString() )
            def populationValueId = getPopulationValueId( apiData.POPULATIONNAME[0]?.text().toString() )
            apiData.GCRPVID_POPV_ID[0].setValue( populationValueId.toString() )
            apiData.GCRPVID_QUERY_ID[0].setValue( queryId.toString() )
        }
        if (connectInfo.tableName == "GCRLENT") {
            def userId = apiData.GCRLENT_USER_ID[0]?.text().toString()
            def selectionId = getSelectionId()
            apiData.GCRLENT_PIDM[0].setValue( personPidm )
            apiData.GCRLENT_SLIS_ID[0].setValue( selectionId.toString() )
            updateUserId(userId)
        }
        if (connectInfo.tableName == "GCRPOPC") {
            def populationValueId = getPopulationValueId( apiData.POPULATIONNAME[0]?.text().toString() )
            def queryValueId = getQueryValueId( apiData.QUERYNAME[0]?.text().toString() )
            def selectionId = getSelectionId()
            apiData.GCRPOPC_SLIS_ID[0].setValue( selectionId.toString() )
            apiData.GCRPOPC_POPV_ID[0].setValue( populationValueId.toString() )
            apiData.GCRPOPC_QRYV_ID[0].setValue( queryValueId.toString() )
        }
        if (connectInfo.tableName == "MARKACTIONITEMPOSTED") {
            conn.executeUpdate( "UPDATE GCBACTM SET GCBACTM_POSTED_IND = 'Y' WHERE GCBACTM_SURROGATE_ID  IN ( SELECT GCRAACT_GCBACTM_ID FROM GCRAACT) AND GCBACTM.GCBACTM_POSTED_IND='N'" )
        }
        if (connectInfo.tableName == "MARKACTIONITEMGROUPPOSTED") {
            conn.executeUpdate( "UPDATE GCBAGRP SET GCBAGRP_POSTED_IND = 'Y' WHERE GCBAGRP_SURROGATE_ID  IN ( SELECT GCRAACT_GCBAGRP_ID FROM GCRAACT) AND GCBAGRP_POSTED_IND='N'" )
        }
        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() {fields ->
            def value = fields.text().replaceAll( /&/, '&amp;' ).replaceAll( /'/, '&apos;' ).replaceAll( />/, '&gt;' ).replaceAll( /</, '&lt;' ).replaceAll( /"/, '&quot;' )
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )

    }

    def getSelectionId() {
        String fsql = """select * from GCRSLIS WHERE GCRSLIS_SURROGATE_ID = (select max(gcrslis_surrogate_id) from gcrslis) """
        int fId
        def fRow
        try {
            fRow = this.conn.firstRow( fsql )
            if (fRow) {
                fId = fRow?.GCRSLIS_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Selection ID in GeneralActionItemDML, from GCRSLIS for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }


    def getQueryValueId( String queryName ) {
        String fsql = """select * from GCRQRYV where GCRQRYV_QUERY_ID= (select GCBQURY_SURROGATE_ID FROM GCBQURY WHERE GCBQURY_NAME=?) """
        int fId
        def fRow


        try {
            fRow = this.conn.firstRow( fsql, [queryName] )
            if (fRow) {
                fId = fRow?.GCRQRYV_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Query Value ID in GeneralActionItemDML, from GCRQRYV for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }


    def getPopulationValueId( String queryName ) {
        String fsql = """select * from GCRPOPV where GCRPOPV_POPL_ID= (select GCBPOPL_SURROGATE_ID FROM GCBPOPL WHERE GCBPOPL_NAME=?) """
        int fId
        def fRow

        try {
            fRow = this.conn.firstRow( fsql, [queryName] )
            if (fRow) {
                fId = fRow?.GCRPOPV_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Population Value ID in GeneralActionItemDML, from GCRPOPV for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }

    /*

    def deleteQuery() {
        deleteQueryData( "GCRQRYV", "delete from GCRQRYV where GCRQRYV_SURROGATE_ID = ? " )
        deleteQueryData( "GCBQURY", "delete from GCBQURY where GCBQURY_SURROGATE_ID = ? " )
    }
    */


    def deleteData() {
        deleteData( "GCRPQID", "delete from GCRPQID where 0 <> ? " )
        deleteData( "GCRPVID", "delete from GCRPVID where 0 <> ? " )
        deleteData( "GCRPOPC", "delete from GCRPOPC where 0 <> ? " )
        deleteData( "GCRPOPV", "delete from GCRPOPV where 0 <> ? " )
        deleteData( "GCBPOPL", "delete from GCBPOPL where 0 <> ? " )
        deleteData( "GCRQRYV", "delete from GCRQRYV where 0 <> ? " )
        // deleteData( "GCBQURY", "delete from GCBQURY where 0 <> ? " )
        deleteData( "GCRSLIS", "delete from GCRSLIS where 0 <> ? " )
        deleteData( "GCRAACT", "delete from GCRAACT where 0 <> ? " )
        deleteData( "GCRAGRA", "delete from GCRAGRA where 0 <> ? " )
        deleteData( "GCRAISR", "delete from GCRAISR where 0 <> ? " )
        deleteData( "GCBAGRP", "delete from GCBAGRP where 0 <> ? " )
        deleteData( "GCRACNT", "delete from GCRACNT where 0 <> ? " )
        deleteData( "GCBPBTR", "delete from GCBPBTR where 0 <> ? " )
        deleteData( "GCRABLK", "delete from GCRABLK where 0 <> ? " )
        deleteData( "GCBACTM", "delete from GCBACTM where 0 <> ? " )
        deleteData( "GCVASTS", "delete from GCVASTS where 0 <> ? " )

    }

/*
    def deleteQueryData(String tableName, String sql ) {
        try {
            int delRows = conn.executeUpdate( sql, queryId )
            connectInfo.tableUpdate( tableName, 0, 0, 0, 0, delRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate( tableName, 0, 0, 0, 1, 0 )
                println "Problem executing delete for query ${sql} from GeneralActionItemDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }
    */


    def deleteData( String tableName, String sql ) {

        try {
            int delRows = conn.executeUpdate( sql, [actionItemId] )
            connectInfo.tableUpdate( tableName, 0, 0, 0, 0, delRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate( tableName, 0, 0, 0, 1, 0 )
                println "Problem executing delete for id ${actionItemId} from GeneralActionItemDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }


    def getFolderId( String folderName ) {
        String fsql = """select * from GCRFLDR where GCRFLDR_NAME= ? """
        int fId
        def fRow

        try {
            fRow = this.conn.firstRow( fsql, [folderName] )
            if (fRow) {
                fId = fRow?.GCRFLDR_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Folder ID in GeneralActionItemDML, from GCRFLDR for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }


    def getQueryId( String queryName ) {
        String fsql = """select * from GCBQURY where GCBQURY_NAME= ? """
        int fId
        def fRow


        try {
            fRow = this.conn.firstRow( fsql, [queryName] )
            if (fRow) {
                fId = fRow?.GCBQURY_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Query ID in GeneralActionItemDML, from GCBQURY for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }


    def getPopulationId( String populationName ) {
        String fsql = """select * from GCBPOPL where GCBPOPL_NAME= ? """
        int fId
        def fRow


        try {
            fRow = this.conn.firstRow( fsql, [populationName] )
            if (fRow) {
                fId = fRow?.GCBPOPL_SURROGATE_ID
            } else fId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Population ID in GeneralActionItemDML, from GCBPOPL for ${connectInfo.tableName}. $e.message"
            }
        }
        return fId
    }


    def getActionItemId( String actionItemName ) {
        String asql = """select * from GCBACTM where GCBACTM_NAME = ? """
        int aId
        def aRow
        try {
            aRow = this.conn.firstRow( asql, [actionItemName] )
            if (aRow) {
                aId = aRow?.GCBACTM_SURROGATE_ID
            } else aId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Action Item ID in GeneralActionItemDML, from GCBACTM for ${connectInfo.tableName}. $e.message"
            }
        }
        return aId
    }


    def getActionGroupId( String actionGroupName ) {
        String asql = """select * from GCBAGRP where GCBAGRP_NAME = ? """
        int agd
        def aRow
        try {
            aRow = this.conn.firstRow( asql, [actionGroupName] )
            if (aRow) {
                agd = aRow?.GCBAGRP_SURROGATE_ID
            } else agd = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Action Item ID in GeneralActionItemDML, from GCBAGRP for ${connectInfo.tableName}. $e.message"
            }
        }
        return agd
    }


    def getTemplateId( String templateName ) {
        String tsql = """select * from GCBPBTR where GCBPBTR_TEMPLATE_NAME= ? """
        int tId
        def tRow

        try {
            tRow = this.conn.firstRow( tsql, [templateName] )
            if (tRow) {
                tId = tRow?.GCBPBTR_SURROGATE_ID
            } else tId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Template ID in GeneralActionItemDML, from GCBPBTR for ${connectInfo.tableName}. $e.message"
            }
        }
        return tId
    }


    def getStatusId( String statusName ) {
        String ssql = """select * from GCVASTS where GCVASTS_STATUS_RULE_NAME= ? """
        int sId
        def sRow

        try {
            sRow = this.conn.firstRow( ssql, [statusName] )
            if (sRow) {
                sId = sRow?.GCVASTS_SURROGATE_ID
            } else sId = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Status ID in GeneralActionItemDML, from GCVASTS for ${connectInfo.tableName}. $e.message"
            }
        }
        return sId
    }

    def updateUserId(userId) {

        updateUserId( "GCBPOPL", "update GCBPOPL set GCBPOPL_USER_ID = '${userId}', GCBPOPL_INCLUDE_LIST_ID = (select max(GCRSLIS_SURROGATE_ID) from GCRSLIS) where GCBPOPL_SURROGATE_ID = (select max(GCBPOPL_SURROGATE_ID) from GCBPOPL)" )

        updateUserId( "GCRLENT", "update GCRLENT set GCRLENT_USER_ID = '${userId}' where GCRLENT_SLIS_ID = (select max(GCRSLIS_SURROGATE_ID) from GCRSLIS)" )

        updateUserId( "GCRSLIS", "update GCRSLIS set GCRSLIS_USER_ID = '${userId}' where GCRSLIS_SURROGATE_ID = (select max(GCRSLIS_SURROGATE_ID) from GCRSLIS)" )

    }

    def updateUserId(String tableName, String sql ) {

        try {
            int updateRows = conn.executeUpdate( sql )
            connectInfo.tableUpdate( tableName, 0, 0, 0, 0, updateRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate( tableName, 0, 0, 0, 1, 0 )
                println "Problem executing update for query ${sql} from GeneralActionItemDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }


}
