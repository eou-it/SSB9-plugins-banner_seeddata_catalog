/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import org.apache.commons.lang.StringUtils

import java.sql.Connection

/**
 * Gcrfldr tables
 * replace the curr rule in the xml
 */

public class GcrfldrDML {
    int folderSeq
    int querySeq
    int templateSeq
    int fieldSeq
    def folder = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public GcrfldrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGcrfldr()

    }

    /**
     * Process the gcrfldr records.   The folder attribute gives us the surrogate key to the gcrfldr which
     * is the folder ID on the gcbqury, gcrcfld, gcbtmpl records.  The template attribute on gcbemtl plus the
     * folder gets us the parent gcbtmpl record.
     *
     */

    def processGcrfldr() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser
        def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
        def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]

        def apiData = new XmlParser().parseText(StringUtils.replaceEach(xmlData, fromstring, tostring))


        String ssql = """select * from gcrfldr  where GCRFLDR_NAME = ? """
        // find if the FOLDER already exists in the database and use it's curr_rule for inserting into the db
        try {
            def folderSeqR = this.conn.firstRow(ssql, [apiData.FOLDER.text()])
            if ( folderSeqR) {
                folderSeq = folderSeqR?.GCRFLDR_SURROGATE_ID
            }
            else folderSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Folder ID in GcrfldrDML,  ${apiData.FOLDER.text()} from GCRFLDR for ${connectInfo.tableName}. $e.message"
            }
        }
        if (connectInfo.debugThis) {
            println "Selected from GCRFLDR ${folderSeq} for folder  ${apiData.FOLDER.text()} for ${connectInfo.tableName}."
        }

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCRFLDR") {
            // delete data so we can re-add instead of update so all children data is refreshed
            deleteData()
        }
        // update the major rule and the curr rule
        else if (connectInfo.tableName == "GCBQURY") {
            if (apiData.GCBQURY_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCBQURY_FOLDER_ID[0].setValue(folderSeq.toString())
            }
        } else if (connectInfo.tableName == "GCBTMPL") {
            if (apiData.GCBTMPL_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCBTMPL_FOLDER_ID[0].setValue(folderSeq.toString())
            }
        } else if (connectInfo.tableName == "GCRCFLD") {
            if (apiData.GCRCFLD_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCRCFLD_FOLDER_ID[0].setValue(folderSeq.toString())
            }
        }  else if (connectInfo.tableName == "GCRITPE") {
            if (apiData.GCRITPE_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCRITPE_FOLDER_ID[0].setValue(folderSeq.toString())
            }
        } else if(connectInfo.tableName == "GCRQRYV") {
            querySeq = getQueryValueId(apiData.QUERY_NAME.text(), folderSeq)
            if (apiData.GCRQRYV_QUERY_ID.text().toInteger() != querySeq) {
                apiData.GCRQRYV_QUERY_ID[0].setValue(querySeq.toString())
            }
        }  else if(connectInfo.tableName == "GCRTPFL") {
            templateSeq = getTemplateSurrogateId(apiData.TEMPLATE_NAME.text(), apiData.TEMPLATE_FOLDER.text())
            if (apiData.GCRTPFL_TEMPLATE_ID.text().toInteger() != templateSeq) {
                apiData.GCRTPFL_TEMPLATE_ID[0].setValue(templateSeq.toString())
            }
            fieldSeq = getFieldSurrogateId(apiData.FIELD_NAME.text(), apiData.FIELD_FOLDER.text())
            if (apiData.GCRTPFL_FIELD_ID.text().toInteger() != fieldSeq) {
                apiData.GCRTPFL_FIELD_ID[0].setValue(fieldSeq.toString())
            }
        }  else if (connectInfo.tableName == "GCBEMTL") {
            def templateSeq
            def tsql = """select * from gcbtmpl where gcbtmpl_folder_id = ? and GCBTMPL_NAME = ?"""
            try {
                def templateSeqRows = this.conn.firstRow(tsql, [folderSeq, apiData.TEMPLATE.text()])
                if ( templateSeqRows) {
                    templateSeq = templateSeqRows?.GCBTMPL_SURROGATE_ID
                }
                else templateSeq = 0
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select surrogate ID from gcbtmpl for gcbemtl in GcrfldrDML,  ${apiData.FOLDER.text()} ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}. $e.message"
                }
            }
            if (connectInfo.debugThis) {
                println "Selected from GCBEMTL ${templateSeq} for folder  ${apiData.FOLDER.text()} and template ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}."
            }
            if ( templateSeq > 0 ) {
                if (apiData.GCBEMTL_SURROGATE_ID.text().toInteger() != templateSeq) {
                    apiData.GCBEMTL_SURROGATE_ID[0].setValue(templateSeq.toString())
                }
                def isql
                try {
                    isql = """insert into gcbemtl
                   ( GCBEMTL_SURROGATE_ID ,
                    GCBEMTL_BCCLIST  ,
                    GCBEMTL_CCLIST ,
                    GCBEMTL_CONTENT,
                    GCBEMTL_FROMLIST ,
                    GCBEMTL_SUBJECT ,
                    GCBEMTL_TOLIST )
                 values (?,?,?,?,?,?,? )
                """
                    this.conn.executeInsert(isql, [
                            apiData.GCBEMTL_SURROGATE_ID.text().toInteger(),
                            apiData.GCBEMTL_BCCLIST.text(),
                            apiData.GCBEMTL_CCLIST.text(),
                            apiData.GCBEMTL_CONTENT.text(),
                            apiData.GCBEMTL_FROMLIST.text(),
                            apiData.GCBEMTL_SUBJECT.text(),
                            apiData.GCBEMTL_TOLIST.text()])
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    if (connectInfo.showErrors) {
                        connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                        println isql
                        println apiData
                        println "Could not insert into GCBEMTL  in GcrfldrDML, ${apiData.FOLDER.text()} ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}. $e.message"
                    }
                }
            }
        } else if (connectInfo.tableName == "GCBMNTL") {
            templateSeq = getTemplateSurrogateId(apiData.TEMPLATE.text(), apiData.FOLDER.text())
            if (apiData.GCBMNTL_SURROGATE_ID.text().toInteger() != templateSeq) {
                apiData.GCBMNTL_SURROGATE_ID[0].setValue(templateSeq.toString())
            }
            def isql
            try {
                isql = """insert into GCBMNTL
                ( GCBMNTL_SURROGATE_ID
                , GCBMNTL_PUSH
                , GCBMNTL_STICKY
                , GCBMNTL_EXPIRATION_POLICY
                , GCBMNTL_DURATION_UNIT
                , GCBMNTL_MOBILE_HEADLINE
                , GCBMNTL_HEADLINE
                , GCBMNTL_DESCRIPTION
                , GCBMNTL_DESTINATION_LINK
                , GCBMNTL_DESTINATION_LABEL) values (?,?,?,?,?,?,?,?,?,? ) """
                this.conn.executeInsert(isql, [
                        apiData.GCBMNTL_SURROGATE_ID.text().toInteger(),
                        apiData.GCBMNTL_PUSH.text(),
                        apiData.GCBMNTL_STICKY.text(),
                        apiData.GCBMNTL_EXPIRATION_POLICY.text(),
                        apiData.GCBMNTL_DURATION_UNIT.text(),
                        apiData.GCBMNTL_MOBILE_HEADLINE.text(),
                        apiData.GCBMNTL_HEADLINE.text(),
                        apiData.GCBMNTL_DESCRIPTION.text(),
                        apiData.GCBMNTL_DESTINATION_LINK.text(),
                        apiData.GCBMNTL_DESTINATION_LABEL.text()])
                connectInfo.tableUpdate(connectInfo.tableName, 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                    println isql
                    println apiData
                    println "Could not insert into GCBMNTL  in GcrfldrDML, ${apiData.FOLDER.text()} ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}. $e.message"
                }
            }
        }
        // update the concentration  rule and the curr rule

        // parse the xml  back into  gstring for the dynamic sql loader
        if (connectInfo.tableName != "GCBEMTL") {
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '&amp;').replaceAll(/'/, '&apos;').replaceAll(/>/, '&gt;').replaceAll(/</, '&lt;').replaceAll(/"/, '&quot;')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)


            if (connectInfo.saveThis) {
                conn.execute "{ call gb_common.p_commit() }"
            }
        }
    }


    def deleteData() {
        deleteData("GCRTPFL", "delete from GCRTPFL where GCRTPFL_TEMPLATE_ID in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
        deleteData("GCRTPFL", "delete from GCRTPFL where GCRTPFL_FIELD_ID in ( select GCRCFLD_surrogate_id from GCRCFLD where GCRCFLD_folder_id  = ?  )  ")
        deleteData("GCBEMTL", "delete from GCBEMTL where GCBEMTL_surrogate_id in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
        deleteData("GCBMNTL", "delete from GCBMNTL where GCBMNTL_surrogate_id in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
        deleteData("GCBTMPL", "delete from GCBTMPL where GCBTMPL_folder_id  = ?   ")
        deleteData("GCRQRYV", "delete from GCRQRYV where GCRQRYV_QUERY_ID in ( select GCBQURY_SURROGATE_ID from GCBQURY where GCBQURY_folder_id  = ?  )  ")
        deleteData("GCBQURY", "delete from GCBQURY where GCBQURY_folder_id  = ? ")
        deleteData("GCRCFLD", "delete from GCRCFLD where GCRCFLD_folder_id = ?  ")
        deleteData("GCRITPE", "delete from GCRITPE where GCRITPE_folder_id = ? ")
        deleteData("GCRFLDR", "delete from GCRFLDR where GCRFLDR_surrogate_id = ? and  NOT EXISTS (SELECT a.gcbactm_gcrfldr_id FROM gcbactm a WHERE a.gcbactm_gcrfldr_id = gcrfldr_surrogate_id) ")

    }


    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [folderSeq])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1,0)
                println "Problem executing delete for folder ${folder} ${folderSeq} from GcrfldrDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

    def updateTmplData(String tableName, String sql) {
        try {

            int updRows = conn.executeUpdate(sql)
            connectInfo.tableUpdate(tableName, 0, 0, updRows, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1,0)
                println "Problem executing update for GCBTMPL from GcrfldrDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

    def getQueryValueId( String queryName, def folderSeq ) {

        String qsql = """ select * FROM GCBQURY WHERE GCBQURY_NAME=? and GCBQURY_FOLDER_ID = ?"""

        try {
            def querySeqR = this.conn.firstRow(qsql, queryName, folderSeq)
            if ( querySeqR) {
                querySeq = querySeqR?.GCBQURY_SURROGATE_ID
            }
            else querySeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Query ID in GcrfldrDML,  ${apiData.QUERY_NAME.text()} from GCBQURY for ${connectInfo.tableName}. $e.message"
            }
        }

        return querySeq
    }

    def getTemplateSurrogateId(String templateName, def templateFolder) {

        def templateSeq
        def folderId
        String ssql = """select * from gcrfldr  where GCRFLDR_NAME = ? """
        def tsql = """select * from gcbtmpl where GCBTMPL_NAME = ? and gcbtmpl_folder_id = ? """

        try {
            def folderSeqR = this.conn.firstRow(ssql, templateFolder)
            if ( folderSeqR) {
                folderId = folderSeqR?.GCRFLDR_SURROGATE_ID
            }
            else folderId = 0

            def templateSeqRows = this.conn.firstRow(tsql, templateName, folderId)
            if ( templateSeqRows) {
                templateSeq = templateSeqRows?.GCBTMPL_SURROGATE_ID
            }
            else templateSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcbtmpl for gcrtpfl in GcrfldrDML,  ${templateFolder} ${templateName} for ${connectInfo.tableName}. $e.message"
            }
        }

        return templateSeq
    }

    def getFieldSurrogateId(String fieldName, def fieldFolder) {

        def fieldSeq
        def folderId
        String ssql = """select * from gcrfldr  where GCRFLDR_NAME = ? """
        def tsql = """select * from gcrcfld where gcrcfld_name = ? and gcrcfld_folder_id = ? """

        try {
            def folderSeqR = this.conn.firstRow(ssql, fieldFolder)
            if ( folderSeqR) {
                folderId = folderSeqR?.GCRFLDR_SURROGATE_ID
            }
            else folderId = 0

            def fieldSeqRows = this.conn.firstRow(tsql, fieldName, folderId)
            if ( fieldSeqRows) {
                fieldSeq = fieldSeqRows?.GCRCFLD_SURROGATE_ID
            }
            else fieldSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcrcfld for gcrtpfl in GcrfldrDML,  ${fieldFolder} ${fieldName} for ${connectInfo.tableName}. $e.message"
            }
        }

        return fieldSeq
    }
}
