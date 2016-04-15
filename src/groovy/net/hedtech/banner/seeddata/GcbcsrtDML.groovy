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

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBCSRT") {
            updateFk(apiData)

            deleteData()
        }
        
        if (connectInfo.tableName == "GCRCSRS") {
            //replace sequence number with current
            updateFk(apiData)

            apiData.GCRCSRS_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
            if (itemSeq == 0) {
                println connectInfo.tableName + " " + apiData.GCRCSRS_ACTION_ITEM_ID.text() + " " + itemSeq
            }

        }
        if (connectInfo.tableName == "GCRACNT") {
            updateFk(apiData)
            //replace sequence number with current

            apiData.GCRACNT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
        }

         if (connectInfo.tableName == "GCBAGRP") {
            //clear out current group data w/folder information in xml. gcrfldrdml will process new records.
            updateFk(apiData)
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


    def updateFk(apiData) {
        try {
            if (connectInfo.tableName == "GCBAGRP") {

                String fsql = """select * from GCRFLDR where GCRFLDR_NAME= ? """
                def folderSeqR = this.conn.firstRow(fsql, [apiData.FOLDER.text()])
                itemSeq = folderSeqR?.GCRFLDR_SURROGATE_ID
               // println apiData.FOLDER.text() + " " + itemSeq + " " + apiData.GCBAGRP_SURROGATE_ID.text()

            } else {

                String ssql = """select * from GCBCSRT where GCBCSRT_NAME = ? """
                def itemSeqR = this.conn.firstRow(ssql, [apiData.ACTIONITEMNAME.text()])
                if (itemSeqR) {
                    itemSeq = itemSeqR?.GCBCSRT_SURROGATE_ID
                } else itemSeq = 0


               // println connectInfo.tableName + " " + apiData.ACTIONITEMNAME.text() + " " + itemSeq
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Action Item ID in GcbcsrtDML, from GCBCSRT for ${connectInfo.tableName}. $e.message"
            }
        }
    }


    def deleteData() {
        deleteData("GCBAGRP", "delete from GCBAGRP where GCBAGRP_FOLDER_ID  = ? ")
        deleteData("GCRCSRS", "delete from GCRCSRS where GCRCSRS_ACTION_ITEM_ID  = ?  OR GCRCSRS_ACTION_ITEM_ID IN(select GCBCSRT_SURROGATE_ID from GCBCSRT)")
        deleteData("GCRACNT", "delete from GCRACNT where GCRACNT_ACTION_ITEM_ID  = ?  OR GCRACNT_ACTION_ITEM_ID IN(select GCBCSRT_SURROGATE_ID from GCBCSRT)")
        deleteData("GCBCSRT", "delete from GCBCSRT where GCBCSRT_SURROGATE_ID  = ? ")
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
