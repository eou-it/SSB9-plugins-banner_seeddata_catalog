/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
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
    int itemSeq
    int folderId
    def item = null
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GeneralActionItemDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {
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
     * Process the gcbcsrt records.   The item attribute gives us the surrogate key to the gcbactm which
     * is used as fk in other records.
     *
     */
    def processData() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser

        def apiData = new XmlParser().parseText(xmlData)
        def personId = apiData.BANNERID?.text()
        def personPidm
        def actionItemName = apiData.GCBACTM_ACTION_ITEM_ID.text()
        def actionItemFolder = apiData.FOLDER.text()

        println "testing folder"
        println actionItemFolder

        if (actionItemName != null) {
            try {
                String ssql = """select * from GCBACTM where GCBACTM_NAME = ? """
                def itemSeqR = this.conn.firstRow(ssql, [apiData.ACTIONITEMNAME.text()])
                if (itemSeqR) {
                    itemSeq = itemSeqR?.GCBACTM_SURROGATE_ID
                } else itemSeq = 0

            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select Action Item ID in GeneralActionItemDML, from GCBACTM for ${connectInfo.tableName}. $e.message"
                }
            }
        }

        if (actionItemFolder != null) {

            try {
                String ssql = """select * from GCRFLDR where GCRFLDR_NAME = ? """
                def folderIdR = this.conn.firstRow(ssql, [apiData.FOLDER.text()])
                if (folderIdR) {
                    folderId = folderIdR?.GCRFLDR_SURROGATE_ID
                } else folderId = 0

            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select Action Item ID in GeneralActionItemDML, from GCRFLDR for ${connectInfo.tableName}. $e.message"
                }
            }
        }

        if (personId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            def spridenRow = conn.firstRow(findPidm, [personId])
            if (spridenRow) {
                personPidm = spridenRow.SPRIDEN_PIDM.toString()
            }
        }

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBACTM") {
            deleteData()
            println "what is the folder"
            println folderId
            if (folderId !=null) {
                apiData.FOLDER[0].setValue(folderId.toString())
            }
        }

        if (connectInfo.tableName == "GCRAACT") {
            //replace sequence number with current
            // connectInfo.debugThis = true
            apiData.GCRAACT_PIDM[0].setValue(personPidm)
            apiData.GCRAACT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())

        }
        if (connectInfo.tableName == "GCRACNT") {
            //replace sequence number with current
            apiData.GCRACNT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
        }

        if (connectInfo.tableName == "GCBAGRP") {
            //clear out current group data w/folder information in xml. gcrfldrdml will process new records.
            String fsql = """select * from GCRFLDR where GCRFLDR_NAME= ? """
            def folderSeqR = this.conn.firstRow(fsql, [apiData.FOLDER.text()])
            itemSeq = folderSeqR?.GCRFLDR_SURROGATE_ID
            apiData.GCBAGRP_FOLDER_ID[0].setValue(itemSeq.toString())
        }

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '&amp;').replaceAll(/'/, '&apos;').replaceAll(/>/, '&gt;').replaceAll(/</, '&lt;').replaceAll(/"/, '&quot;')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
    }


    def deleteData() {
        deleteData("GCBAGRP", "delete from GCBAGRP where 0 <> ? ")
        deleteData("GCRAACT", "delete from GCRAACT where GCRAACT_ACTION_ITEM_ID  = ? ")
        deleteData("GCRACNT", "delete from GCRACNT where GCRACNT_ACTION_ITEM_ID  = ?  ")
        deleteData("GCBACTM", "delete from GCBACTM where GCBACTM_SURROGATE_ID  = ? ")
    }

    def deleteData(String tableName, String sql) {
        try {
            int delRows = conn.executeUpdate(sql, [itemSeq])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1, 0)
                println "Problem executing delete for id ${itemSeq} from GeneralActionItemDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

}
