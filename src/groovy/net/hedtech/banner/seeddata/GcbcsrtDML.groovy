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
    int itemPidm
    def item = null
    def itemTerm
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
        /*
        def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
        def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]
        def apiData = new XmlParser().parseText(StringUtils.replaceEach(xmlData, fromstring, tostring))
        */

        def apiData = new XmlParser().parseText(xmlData)

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCBCSRT") {

            def curid = apiData.GCBCSRT_SURROGATE_ID.text()

            processDelete("GCRCSRS", "delete from GCRCSRS where GCRCSRS_CSRT_ID  = ? ", curid)
            processDelete("GCBCSRT", "delete from GCBCSRT where GCBCSRT_SURROGATE_ID  = ?", curid)


            /*
            String ssql = """select * from gcbcsrt  where GCBCSRT_SURROGATE_ID = ? """

            def itemSeqR = this.conn.firstRow(ssql, [apiData.GCBCSRT_SURROGATE_ID.text()])

            if (itemSeqR) {
                itemSeq = itemSeqR?.GCBCSRT_SURROGATE_ID
            } else itemSeq = 0

            println "itemSeq: " + itemSeq

            if (itemSeq != 0) {

            }
            */

            def isql
            try {
                isql = """insert into gcbcsrt
                   (
                    GCBCSRT_SURROGATE_ID,
                    GCBCSRT_NAME  ,
                    GCBCSRT_ACTIVE ,
                    GCBCSRT_USER_ID,
                    GCBCSRT_ACTIVITY_DATE ,
                    GCBCSRT_DESCRIPTION ,
                    GCBCSRT_CREATOR_ID ,
                    GCBCSRT_CREATE_DATE
                     )
                 values (?,?,?,?,?,?,?,?)
                """
                this.conn.executeInsert(isql, [
                        apiData.GCBCSRT_SURROGATE_ID.text(),
                        apiData.GCBCSRT_NAME.text(),
                        apiData.GCBCSRT_ACTIVE.text(),
                        apiData.GCBCSRT_USER_ID.text(),
                        apiData.GCBCSRT_ACTIVITY_DATE.text(),
                        apiData.GCBCSRT_DESCRIPTION.text(),
                        apiData.GCBCSRT_CREATOR_ID.text(),
                        apiData.GCBCSRT_CREATOR_DATE.text()])
                connectInfo.tableUpdate(connectInfo.tableName, 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                    println isql
                    println apiData
                    println "Could not insert into GCBCSRT in GcbcsrtDML, ${apiData.GCBCSRT_NAME.text()} for ${connectInfo.tableName}. $e.message"
                }
            }
        }


        // parse the xml  back into  gstring for the dynamic sql loader

        if (connectInfo.tableName != "GCBCSRT") {
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '&amp;').replaceAll(/'/, '&apos;').replaceAll(/>/, '&gt;').replaceAll(/</, '&lt;').replaceAll(/"/, '&quot;')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        }

    }


    def processDelete(String tableName, String sql, String itemKey) {

        try {
            int delRows = conn.executeUpdate(sql, [itemKey])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1,0)
                println "Problem executing delete for item ${item} ${itemKey} from GcbcsrtDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

}
