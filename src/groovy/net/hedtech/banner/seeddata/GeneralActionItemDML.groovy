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
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode
    int itemSeq
    int folderId
    int templateId
    int statusId

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
     * Process the action item records.   The item attribute gives us the surrogate key to the gcbactm which
     * is used as fk in other records.
     *
     */
    def processData() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser
        def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
        def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]
        def apiData = new XmlParser().parseText(StringUtils.replaceEach(xmlData, fromstring, tostring))

        def personId = apiData.BANNERID?.text()
        def personPidm

        if (personId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            def spridenRow = conn.firstRow(findPidm, [personId])
            if (spridenRow) {
                personPidm = spridenRow.SPRIDEN_PIDM.toString()
            }
        }

        //

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBACTM") {

            itemSeq = getActionItemId( apiData.GCBACTM_NAME[0]?.text().toString() )
            folderId = getFolderId( apiData.FOLDER[0]?.text().toString() )


           // println "folder returned: " + folderId

            if (itemSeq == 0) {
                itemSeq = apiData.GCBACTM_SURROGATE_ID[0]?.text().toInteger()
            } else {
                deleteData( )
            }

            if (folderId == 0) {
                folderId = apiData.GCBACTM_FOLDER_ID[0]?.text().toInteger()
            }

            apiData.GCBACTM_FOLDER_ID[0].setValue(folderId.toString())
            println  "folder for GCBACTM " + apiData.GCBACTM_FOLDER_ID?.text() + " itemseq: " + itemSeq

        }

        if (connectInfo.tableName == "GCVASTS") {
            println "status " +  apiData.GCVASTS_ACTION_ITEM_STATUS?.text()

            /*
            if (statusId == 0) {
                statusId = apiData.GCVASTS_SURROGATE_ID[0]?.text().toInteger()
            }
            */
        }


        if (connectInfo.tableName == "GCRAACT") {

            itemSeq = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            statusId = getStatusId( apiData.ACTIONITEMSTATUS[0]?.text().toString() )
            println "action item id: " + itemSeq

            if (itemSeq == 0) {
                itemSeq = apiData.GCRAACT_ACTION_ITEM_ID[0]?.text().toInteger()
            }

            if (statusId == 0) {
                statusId = apiData.GCRAACT_STATUS_ID[0]?.text().toInteger()
            }

            apiData.GCRAACT_PIDM[0].setValue(personPidm)
            apiData.GCRAACT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
            apiData.GCRAACT_STATUS_ID[0].setValue(statusId.toString())


        }
        if (connectInfo.tableName == "GCRACNT") {
            //replace sequence number with current
            itemSeq = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
           // templateId = getTemplateId( apiData.ACTIONITEMTEMPLATE[0]?.text().toString() )

            if (itemSeq == 0) {
                itemSeq = apiData.GCRACNT_ACTION_ITEM_ID[0]?.text().toInteger()
            }



            /* --to be added
            if (templateId == 0) {
                templateId = apiData.GCBPBTR_TEMPLATE_ID[0]?.text().toInteger()
            }
            apiData.GCBPBTR_TEMPLATE_ID[0].setValue(templateId.toString())
            */

            apiData.GCRACNT_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
        }

        if (connectInfo.tableName == "GCBAGRP") {
            //clear out current group data w/folder information in xml. gcrfldrdml will process new records.

            folderId = getFolderId( apiData.FOLDER[0]?.text().toString() )

            if (folderId == 0) {
                folderId = apiData.GCBAGRP_FOLDER_ID[0]?.text().toInteger()
            }

            apiData.GCBAGRP_FOLDER_ID[0].setValue(folderId.toString())
        }

        if (connectInfo.tableName == "GCRAISR") {
            //clear out current group data w/folder information in xml. gcrfldrdml will process new records.
            println "GCRAISR"
        }

        if (connectInfo.tableName == "GCRAISR") {

            itemSeq = getActionItemId( apiData.ACTIONITEMNAME[0]?.text().toString() )
            statusId = getStatusId( apiData.STATUSNAME[0]?.text().toString() )
            println "action item id: " + itemSeq

            if (itemSeq == 0) {
                itemSeq = apiData.GCRAISR_ACTION_ITEM_ID[0]?.text().toInteger()
            }

            if (statusId == 0) {
                statusId = apiData.GCRAISR_ACTION_ITEM_STATUS_ID[0]?.text().toInteger()
            }

            apiData.GCRAISR_ACTION_ITEM_ID[0].setValue(itemSeq.toString())
            apiData.GCRAISR_ACTION_ITEM_STATUS_ID[0].setValue(statusId.toString())

        }

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '&amp;').replaceAll(/'/, '&apos;').replaceAll(/>/, '&gt;').replaceAll(/</, '&lt;').replaceAll(/"/, '&quot;')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode )

    }

    def deleteData() {
        //deleteData("GCRFLDR", "delete from GCRFLDR where GCRFLDR_NAME like 'AIP%' and 0 <> ?")
        //deleteData("GCVASTS", "delete from GCVASTS where 0 <> ? ")
        deleteData("GCBPBTR", "delete from GCBPBTR where 0 <> ? ")
        deleteData("GCRAISR", "delete from GCRAISR where 0 <> ? ")
        deleteData("GCBAGRP", "delete from GCBAGRP where 0 <> ? ")
        deleteData("GCRAACT", "delete from GCRAACT where GCRAACT_ACTION_ITEM_ID  = ? ")
        deleteData("GCRACNT", "delete from GCRACNT where GCRACNT_ACTION_ITEM_ID  = ?  ")
        deleteData("GCBACTM", "delete from GCBACTM where GCBACTM_SURROGATE_ID  = ? ")
        deleteData("GCVASTS", "delete from GCVASTS where 0 <> ? ")
    }

    def deleteData(String tableName, String sql) {


        println "delete " + tableName + " " + itemSeq.toString( )

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

    def getFolderId(String folderName) {
        String fsql = """select * from GCRFLDR where GCRFLDR_NAME= ? """
        int fId
        def fRow

        println "getting folder id for: " + folderName

        try {
            fRow = this.conn.firstRow(fsql, [folderName])
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

    def getActionItemId(String actionItemName) {
        String asql = """select * from GCBACTM where GCBACTM_NAME = ? """
        int aId
        def aRow
        try {
            aRow = this.conn.firstRow(asql, [actionItemName])
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

    def getTemplateId(String templateName) {
        String tsql = """select * from GCBPBTR where GCBPBTR_TEMPLATE_NAME= ? """
        int tId
        def tRow

        println "getting template id for: " + templateName

        try {
            tRow = this.conn.firstRow(tsql, [templateName])
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

    def getStatusId(String statusName) {
        String ssql = """select * from GCVASTS where GCVASTS_ACTION_ITEM_STATUS= ? """
        int sId
        def sRow

        println "getting status id for: " + statusName

        try {
            sRow = this.conn.firstRow(ssql, [statusName])
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


}
