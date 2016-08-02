/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import org.apache.commons.lang.StringUtils

import java.sql.Connection

/**
 * Gcbcsrt tables
 * replace the curr rule in the xml
 */

public class GcbcsrtDML {
    int itemSeq
    int folderSeq
    def item = null
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GcbcsrtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGcbcsrt()
    }

    /**
     * Process the gcbcsrt records.   The item attribute gives us the surrogate key to the gcbcsrt which
     * is the item ID on the gcbqury, gcrcfld, gcbtmpl records.  The template attribute on gcbemtl plus the
     * item gets us the parent gcbtmpl record.
     *
     */
    def processGcbcsrt() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser

        def apiData = new XmlParser().parseText(xmlData)
        def personId = apiData.BANNERID?.text()
        def personPidm
        def actionItemName = apiData.GCRACCT_ACTION_ITEM_ID.text()

        if (actionItemName != null) {
            try {

                String ssql = """select * from GCBACTM where GCBACTM_NAME = ? """
                def itemSeqR = this.conn.firstRow(ssql, [apiData.ACTIONITEMNAME.text()])
                if (itemSeqR) {
                    itemSeq = itemSeqR?.GCBACTM_SURROGATE_ID
                } else itemSeq = 0

            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select Action Item ID in GcbcsrtDML, from GCBACTM for ${connectInfo.tableName}. $e.message"
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
        }
        
        if (connectInfo.tableName == "GCRACCT") {
            //replace sequence number with current
            // connectInfo.debugThis = true
            apiData.GCRACCT_PIDM[0].setValue(personPidm)
            apiData.GCRACCT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
            //println apiData.ACTIONITEMNAME.text() + " " + apiData.GCRACCT_ACTION_ITEM_ID.text()

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
        deleteData("GCRACCT", "delete from GCRACCT where GCRACCT_ACTION_ITEM_ID  = ? ")
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
                println "Problem executing delete for id ${itemSeq} from GcbcsrtDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

}
