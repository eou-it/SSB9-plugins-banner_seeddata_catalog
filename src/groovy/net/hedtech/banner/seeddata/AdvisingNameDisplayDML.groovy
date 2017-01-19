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

public class AdvisingNameDisplayDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode
    //int itemSeq


    public AdvisingNameDisplayDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
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

       // connectInfo.debugThis = true

        if (personId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            def spridenRow = conn.firstRow(findPidm, [personId])
            if (spridenRow) {
                personPidm = spridenRow.SPRIDEN_PIDM.toString()
            }
        }

        //

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GURNDSP") {

           println apiData.GURNDSP_USAGE?.text()
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

        if (connectInfo.saveThis) {
            conn.execute "{ call gb_common.p_commit() }"
        }
    }

    def deleteData() {
        deleteData("GURNHIR", "delete from GURNHIR where GURNHIR_SURROGATE_ID ? ")
        deleteData("GURNDSP", "delete from GURNDSP where GURNDSP_SURROGATE_ID ? ")
    }

    def deleteData(String tableName, String sql) {

        try {
            int delRows = conn.executeUpdate(sql, [actionItemId])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1, 0)
                println "Problem executing delete for id ${actionItemId} from GeneralActionItemDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

    /*

    def getNameDisplayId(String actionItemName) {
        String asql = """select * from GURNDSP where GCBACTM_NAME = ? """
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
    */

    /*
    def getFolderId(String folderName) {
        String fsql = """select * from GCRFLDR where GCRFLDR_NAME= ? """
        int fId
        def fRow


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
    */

}