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
    int orgSeq
    int querySeq
    int queryVersionSeq
    int templateSeq
    int fieldSeq
    int paramSeq
    def senderPropSeq
    def receivePropSeq
    def senderMailBoxSeq
    def replyMailBoxSeq
    def parentOrgSeq
    def childOrgSeq
    def interactionTypeSeq
    def folder = null
    def organization = null

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
            apiData.GCRFLDR_SURROGATE_ID[0].setValue(folderSeq.toString())
        }
        // update the major rule and the curr rule
        else if (connectInfo.tableName == "GCBQURY") {
            if (apiData.GCBQURY_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCBQURY_FOLDER_ID[0].setValue(folderSeq.toString())
            }
            querySeq = getQuerySurrogateId(apiData.QUERY_NAME.text(), folderSeq)
            if(querySeq != 0) {
                apiData.GCBQURY_SURROGATE_ID[0].setValue(querySeq.toString())
            }
        } else if (connectInfo.tableName == "GCBTMPL") {
            if (apiData.GCBTMPL_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCBTMPL_FOLDER_ID[0].setValue(folderSeq.toString())
            }
            templateSeq = getTemplateSurrogateId(apiData.GCBTMPL_NAME.text(), folderSeq)
            if (apiData.GCBTMPL_SURROGATE_ID.text().toInteger() != templateSeq) {
                apiData.GCBTMPL_SURROGATE_ID[0].setValue(templateSeq.toString())
            }
        } else if (connectInfo.tableName == "GCRCFLD") {
            if (apiData.GCRCFLD_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCRCFLD_FOLDER_ID[0].setValue(folderSeq.toString())
            }
            fieldSeq = getFieldSurrogateId(apiData.GCRCFLD_NAME.text(), folderSeq)
            if(fieldSeq != 0) {
                apiData.GCRCFLD_SURROGATE_ID[0].setValue(fieldSeq.toString())
            }
        } else if(connectInfo.tableName == "GCRPARM") {
            paramSeq = getParameterSurrogateId(apiData.GCRPARM_NAME.text())
            if (apiData.GCRPARM_SURROGATE_ID.text().toInteger() != paramSeq) {
                apiData.GCRPARM_SURROGATE_ID[0].setValue(paramSeq.toString())
            }
        }  else if (connectInfo.tableName == "GCRFLPM") {
            fieldSeq = getFieldSurrogateId(apiData.FIELD_NAME.text(), apiData.FIELD_FOLDER.text())
            if (apiData.GCRFLPM_FIELD_ID.text().toInteger() != fieldSeq) {
                apiData.GCRFLPM_FIELD_ID[0].setValue(fieldSeq.toString())
            }
            paramSeq = getParameterSurrogateId(apiData.PARAMETER_NAME.text())
            if (apiData.GCRFLPM_PARAMETER_ID.text().toInteger() != paramSeq) {
                apiData.GCRFLPM_PARAMETER_ID[0].setValue(paramSeq.toString())
            }
        }  else if (connectInfo.tableName == "GCRITPE") {
            if (apiData.GCRITPE_FOLDER_ID.text().toInteger() != folderSeq) {
                apiData.GCRITPE_FOLDER_ID[0].setValue(folderSeq.toString())
            }
            interactionTypeSeq = getInteractionTypeSurrogateId(apiData.GCRITPE_NAME.text(), folderSeq)
            if(interactionTypeSeq != 0) {
                apiData.GCRITPE_SURROGATE_ID[0].setValue(interactionTypeSeq.toString())
            }
        } else if(connectInfo.tableName == "GCRQRYV") {
            querySeq = getQuerySurrogateId(apiData.QUERY_NAME.text(), folderSeq)
            if (apiData.GCRQRYV_QUERY_ID.text().toInteger() != querySeq) {
                apiData.GCRQRYV_QUERY_ID[0].setValue(querySeq.toString())
            }
            queryVersionSeq = getQueryVersionId(querySeq, apiData.GCRQRYV_CREATE_DATE.text())
            if(queryVersionSeq != 0) {
                apiData.GCRQRYV_SURROGATE_ID[0].setValue(queryVersionSeq.toString())
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
        } else if(connectInfo.tableName == "GCRORAN") {
            if(apiData.GCRORAN_UPDATE_MODE) {
                //Organization is in update mode to set the srevr properties and mailbox accounts
                senderPropSeq = getServerPropertiesSurrogateId(apiData.SEND_EMAILPROP_NAME.text(), apiData.SEND_EMAILPROP_HOST.text(), apiData.SEND_EMAILPROP_PORT.text())
                if (apiData.GCRORAN_SEND_EMAILPROP_ID.text().toInteger() != senderPropSeq) {
                    apiData.GCRORAN_SEND_EMAILPROP_ID[0].setValue(senderPropSeq.toString())
                }
                senderMailBoxSeq = getMailBoxSurrogateId(apiData.SEND_MAILBOX_TYPE.text(), apiData.SEND_MAILBOX_USERNAME.text())
                if (apiData.GCRORAN_SEND_MAILBOX_ID.text().toInteger() != senderMailBoxSeq) {
                    apiData.GCRORAN_SEND_MAILBOX_ID[0].setValue(senderMailBoxSeq.toString())
                }
                replyMailBoxSeq = getMailBoxSurrogateId(apiData.REPLY_MAILBOX_TYPE.text(), apiData.REPLY_MAILBOX_USERNAME.text())
                if (apiData.GCRORAN_REPLY_MAILBOX_ID.text().toInteger() != replyMailBoxSeq) {
                    apiData.GCRORAN_REPLY_MAILBOX_ID[0].setValue(replyMailBoxSeq.toString())
                }
            } else {
                processGcroran()
            }
            parentOrgSeq = getParentOrgId()
            if(apiData.PARENT_NAME.text()) {
                if (apiData.GCRORAN_PARENT_ID.text().toInteger() != parentOrgSeq) {
                    apiData.GCRORAN_PARENT_ID[0].setValue(parentOrgSeq.toString())
                }
                //Check if child org already exist, if then update
                childOrgSeq = getOrgSurrogateIdByName(apiData.GCRORAN_NAME.text())
                if(childOrgSeq > 0) {
                    if (apiData.GCRORAN_SURROGATE_ID.text().toInteger() != childOrgSeq) {
                        apiData.GCRORAN_SURROGATE_ID[0].setValue(childOrgSeq.toString())
                    }
                }
            } else {
                //Data is of Parent org, just update
                if (apiData.GCRORAN_SURROGATE_ID.text().toInteger() != parentOrgSeq) {
                    apiData.GCRORAN_SURROGATE_ID[0].setValue(parentOrgSeq.toString())
                }
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

                def sql
                try {
                    boolean isExist = isEmailTemplateExist(templateSeq)
                    if (isExist) {
                        sql = """update gcbemtl set GCBEMTL_BCCLIST = ?, GCBEMTL_CCLIST =?, GCBEMTL_CONTENT =?, GCBEMTL_FROMLIST =?, GCBEMTL_SUBJECT =?, GCBEMTL_TOLIST =?
                        WHERE GCBEMTL_SURROGATE_ID = ?
                        """
                        this.conn.executeUpdate(sql, [
                                apiData.GCBEMTL_BCCLIST.text(),
                                apiData.GCBEMTL_CCLIST.text(),
                                apiData.GCBEMTL_CONTENT.text(),
                                apiData.GCBEMTL_FROMLIST.text(),
                                apiData.GCBEMTL_SUBJECT.text(),
                                apiData.GCBEMTL_TOLIST.text(),
                                apiData.GCBEMTL_SURROGATE_ID.text().toInteger(),])
                        connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 1, 0, 0)
                    } else {
                        sql = """insert into gcbemtl
                       ( GCBEMTL_SURROGATE_ID ,
                        GCBEMTL_BCCLIST  ,
                        GCBEMTL_CCLIST ,
                        GCBEMTL_CONTENT,
                        GCBEMTL_FROMLIST ,
                        GCBEMTL_SUBJECT ,
                        GCBEMTL_TOLIST )
                        values (?,?,?,?,?,?,? )
                        """
                        this.conn.executeInsert(sql, [
                                apiData.GCBEMTL_SURROGATE_ID.text().toInteger(),
                                apiData.GCBEMTL_BCCLIST.text(),
                                apiData.GCBEMTL_CCLIST.text(),
                                apiData.GCBEMTL_CONTENT.text(),
                                apiData.GCBEMTL_FROMLIST.text(),
                                apiData.GCBEMTL_SUBJECT.text(),
                                apiData.GCBEMTL_TOLIST.text()])
                        connectInfo.tableUpdate(connectInfo.tableName, 0, 1, 0, 0, 0)
                    }
                } catch (Exception e) {
                    if (connectInfo.showErrors) {
                        connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                        println sql
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
            def sql
            try {
                boolean isExist = isMobileTemplateExist(templateSeq)
                if (isExist) {
                    sql = """UPDATE GCBMNTL SET GCBMNTL_PUSH = ?, GCBMNTL_STICKY = ?, GCBMNTL_EXPIRATION_POLICY = ?, GCBMNTL_DURATION_UNIT =?, GCBMNTL_MOBILE_HEADLINE = ?, GCBMNTL_HEADLINE =?
                    , GCBMNTL_DESCRIPTION = ?, GCBMNTL_DESTINATION_LINK = ?, GCBMNTL_DESTINATION_LABEL = ? WHERE GCBMNTL_SURROGATE_ID = ? """
                    this.conn.executeUpdate(sql, [
                            apiData.GCBMNTL_PUSH.text(),
                            apiData.GCBMNTL_STICKY.text(),
                            apiData.GCBMNTL_EXPIRATION_POLICY.text(),
                            apiData.GCBMNTL_DURATION_UNIT.text(),
                            apiData.GCBMNTL_MOBILE_HEADLINE.text(),
                            apiData.GCBMNTL_HEADLINE.text(),
                            apiData.GCBMNTL_DESCRIPTION.text(),
                            apiData.GCBMNTL_DESTINATION_LINK.text(),
                            apiData.GCBMNTL_DESTINATION_LABEL.text(),
                            apiData.GCBMNTL_SURROGATE_ID.text().toInteger(),])
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 1, 0, 0)
                } else {
                    sql = """insert into GCBMNTL
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
                    this.conn.executeInsert(sql, [
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
            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                    println sql
                    println apiData
                    println "Could not insert into GCBMNTL  in GcrfldrDML, ${apiData.FOLDER.text()} ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}. $e.message"
                }
            }
        } else if (connectInfo.tableName == "GCBLTPL") {
            templateSeq = getTemplateSurrogateId(apiData.TEMPLATE.text(), apiData.FOLDER.text())
            if (apiData.GCBLTPL_SURROGATE_ID.text().toInteger() != templateSeq) {
                apiData.GCBLTPL_SURROGATE_ID[0].setValue(templateSeq.toString())
            }
            def sql
            try {
                boolean isExist = isLetterTemplateExist(templateSeq)
                if (isExist) {
                    sql = """UPDATE GCBLTPL SET GCBLTPL_TOADDRESS = ?, GCBLTPL_STYLE = ?, GCBLTPL_CONTENT = ? WHERE GCBLTPL_SURROGATE_ID = ? """
                    this.conn.executeUpdate(sql, [
                            apiData.GCBLTPL_TOADDRESS.text(),
                            apiData.GCBLTPL_STYLE.text(),
                            apiData.GCBLTPL_CONTENT.text(),
                            apiData.GCBLTPL_SURROGATE_ID.text().toInteger()])
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 1, 0, 0)

                } else {
                    sql = """insert into GCBLTPL
                    ( GCBLTPL_SURROGATE_ID
                    , GCBLTPL_TOADDRESS
                    , GCBLTPL_STYLE
                    , GCBLTPL_CONTENT) values ( ?,?,?,? ) """
                    this.conn.executeInsert(sql, [
                            apiData.GCBLTPL_SURROGATE_ID.text().toInteger(),
                            apiData.GCBLTPL_TOADDRESS.text(),
                            apiData.GCBLTPL_STYLE.text(),
                            apiData.GCBLTPL_CONTENT.text()])
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 1, 0, 0, 0)
                }
            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
                    println sql
                    println apiData
                    println "Could not insert into GCBLTPL  in GcrfldrDML, ${apiData.FOLDER.text()} ${apiData.TEMPLATE.text()} for ${connectInfo.tableName}. $e.message"
                }
            }
        }
        // update the concentration  rule and the curr rule

        // parse the xml  back into  gstring for the dynamic sql loader
        switch(connectInfo.tableName) {
            case "GCBLTPL" : break;
            case "GCBEMTL" : break;
            case "GCBMNTL" : break;
            default :   def xmlRecNew = "<${apiData.name()}>\n"
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
                    break;
        }
    }

    def processGcroran() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
        //the xml before it gets parsed by the xml parser
        def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
        def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]

        def apiData = new XmlParser().parseText(StringUtils.replaceEach(xmlData, fromstring, tostring))


        String ssql = """select * from gcroran  where gcroran_name = ? """
        // find if the FOLDER already exists in the database and use it's curr_rule for inserting into the db
        try {
            def orgSeqR = this.conn.firstRow(ssql, [apiData.GCRORAN_NAME.text()])
            if ( orgSeqR) {
                orgSeq = orgSeqR?.GCRORAN_SURROGATE_ID
            }
            else orgSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Organization ID in GcrfldrDML,  ${apiData.GCRORAN_NAME.text()} from GCRORAN for ${connectInfo.tableName}. $e.message"
            }
        }
        if (connectInfo.debugThis) {
            println "Selected from GCRORAN ${orgSeq} for organization  ${apiData.GCRORAN_NAME.text()} for ${connectInfo.tableName}."
        }

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "GCRORAN") {
            // delete data so we can re-add instead of update so all children data is refreshed
            deleteOrganizationRelatedData()
        }
    }

    def deleteData() {
        deleteData("GCRTPFL", "delete from GCRTPFL where GCRTPFL_TEMPLATE_ID in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
        deleteData("GCRTPFL", "delete from GCRTPFL where GCRTPFL_FIELD_ID in ( select GCRCFLD_surrogate_id from GCRCFLD where GCRCFLD_folder_id  = ?  )  ")
//        deleteData("GCBEMTL", "delete from GCBEMTL where GCBEMTL_surrogate_id in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
//        deleteData("GCBMNTL", "delete from GCBMNTL where GCBMNTL_surrogate_id in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
//        deleteData("GCBLTPL", "delete from GCBLTPL where GCBLTPL_surrogate_id in ( select gcbtmpl_surrogate_id from gcbtmpl where gcbtmpl_folder_id  = ?  )  ")
//        deleteData("GCBTMPL", "delete from GCBTMPL where GCBTMPL_folder_id  = ?   ")
//        deleteData("GCRQRYV", "delete from GCRQRYV where GCRQRYV_QUERY_ID in ( select GCBQURY_SURROGATE_ID from GCBQURY where GCBQURY_folder_id  = ?  )  ")
//        deleteData("GCBQURY", "delete from GCBQURY where GCBQURY_folder_id  = ? ")
        deleteData("GCRFLPM", "delete from gcrflpm where GCRFLPM_FIELD_ID in ( select gcrcfld_surrogate_id from gcrcfld where GCRCFLD_FOLDER_ID = ? )")
//        deleteData("GCRCFLD", "delete from GCRCFLD where GCRCFLD_folder_id = ?  ")
//        deleteData("GCRITPE", "delete from GCRITPE where GCRITPE_folder_id = ? ")
//        deleteData("GCRFLDR", "delete from GCRFLDR where GCRFLDR_surrogate_id = ? and  NOT EXISTS (SELECT a.gcbactm_gcrfldr_id FROM gcbactm a WHERE a.gcbactm_gcrfldr_id = gcrfldr_surrogate_id) ")

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

    def deleteOrganizationRelatedData() {
        deleteOrganizationRelatedData("GCBSPRP", "delete from gcbsprp where gcbsprp_surrogate_id in (select GCRORAN_SEND_EMAILPROP_ID from gcroran where gcroran_surrogate_id = ?)")
        deleteOrganizationRelatedData("GCBSPRP", "delete from gcbsprp where gcbsprp_surrogate_id in (select GCRORAN_RECEIVE_EMAILPROP_ID from gcroran where gcroran_surrogate_id = ?)")
        deleteOrganizationRelatedData("GCRMBAC", "delete from gcrmbac where gcrmbac_surrogate_id in (select GCRORAN_SEND_MAILBOX_ID from gcroran where gcroran_surrogate_id = ?)")
        deleteOrganizationRelatedData("GCRMBAC", "delete from gcrmbac where gcrmbac_surrogate_id in (select GCRORAN_REPLY_MAILBOX_ID from gcroran where gcroran_surrogate_id = ?)")
    }

    def deleteOrganizationRelatedData(String tableName, String sql) {
        try {
            int delRows = conn.executeUpdate(sql, [orgSeq])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate(tableName, 0, 0, 0, 1,0)
                println "Problem executing delete for organization ${organization} ${orgSeq} from GcrfldrDML.groovy for ${connectInfo.tableName}: $e.message"
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

    def getQuerySurrogateId( String queryName, def folderSeq ) {

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

    def getQueryVersionId( def queryId, def createdDate ) {

        String qsql = """ select * FROM GCRQRYV WHERE GCRQRYV_QUERY_ID = ? AND GCRQRYV_CREATE_DATE = to_date( ?, 'dd-mon-yy hh24:mi:ss' )"""

        try {
            def queryVersionSeqR = this.conn.firstRow(qsql, queryId, createdDate)
            if ( queryVersionSeqR) {
                queryVersionSeq = queryVersionSeqR?.GCRQRYV_SURROGATE_ID
            }
            else queryVersionSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Query Version ID in GcrfldrDML,  ${apiData.QUERY_NAME.text()} from GCRQRYV for ${connectInfo.tableName}. $e.message"
            }
        }

        return queryVersionSeq
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

    def getTemplateSurrogateId(String templateName, int folderSeq) {

        def templateSeq
        def tsql = """select * from gcbtmpl where GCBTMPL_NAME = ? and gcbtmpl_folder_id = ? """

        try {
            def templateSeqRows = this.conn.firstRow(tsql, templateName, folderSeq)
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

    def isEmailTemplateExist(def templateSeq) {

        def tsql = """select * from GCBEMTL where GCBEMTL_SURROGATE_ID = ? """

        try {
            def templateSeqRows = this.conn.firstRow(tsql, templateSeq)
            if ( templateSeqRows) {
                return true
            }
            else return false
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcbtmpl for gcbemtl in GcrfldrDML,  ${templateSeq} for ${connectInfo.tableName}. $e.message"
            }
        }
    }

    def isMobileTemplateExist(def templateSeq) {

        def tsql = """select * from GCBMNTL where GCBMNTL_SURROGATE_ID = ? """

        try {
            def templateSeqRows = this.conn.firstRow(tsql, templateSeq)
            if ( templateSeqRows) {
                return true
            }
            else return false
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcbtmpl for gcbmntl in GcrfldrDML,  ${templateSeq} for ${connectInfo.tableName}. $e.message"
            }
        }
    }

    def isLetterTemplateExist(def templateSeq) {

        def tsql = """select * from GCBLTPL where GCBLTPL_SURROGATE_ID = ? """

        try {
            def templateSeqRows = this.conn.firstRow(tsql, templateSeq)
            if ( templateSeqRows) {
                return true
            }
            else return false
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcbtmpl for gcbltpl in GcrfldrDML,  ${templateSeq} for ${connectInfo.tableName}. $e.message"
            }
        }
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

    def getFieldSurrogateId(String fieldName, int folderSeq) {

        def fieldSeq
        def tsql = """select * from gcrcfld where gcrcfld_name = ? and gcrcfld_folder_id = ? """

        try {
                def fieldSeqRows = this.conn.firstRow(tsql, fieldName, folderSeq)
                if ( fieldSeqRows) {
                    fieldSeq = fieldSeqRows?.GCRCFLD_SURROGATE_ID
                }
                else fieldSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcrcfld for gcrtpfl in GcrfldrDML,  ${fieldName} ${folderSeq} for ${connectInfo.tableName}. $e.message"
            }
        }

        return fieldSeq
    }

    def getParameterSurrogateId(String parameterName) {

        def parameterSeq
        def tsql = """select * from gcrparm where gcrparm_NAME = ? """

        try {
            def paramSeqRows = this.conn.firstRow(tsql, parameterName)
            if ( paramSeqRows) {
                parameterSeq = paramSeqRows?.GCRPARM_SURROGATE_ID
            }
            else parameterSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcrparm for gcrflpm in GcrfldrDML,  ${parameterName} for ${connectInfo.tableName}. $e.message"
            }
        }

        return parameterSeq
    }

    def getServerPropertiesSurrogateId(String propertyType, String host, String port) {

        def seq
        def tsql = """select * from gcbsprp where gcbsprp_type = ? and gcbsprp_host = ? and gcbsprp_port = ?"""

        try {
            def rows = this.conn.firstRow(tsql, propertyType, host, port)
            if ( rows) {
                seq = rows?.GCBSPRP_SURROGATE_ID
            }
            else seq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcbsprp for gcroran in GcrfldrDML,  ${propertyType} ${host} {$port} for ${connectInfo.tableName}. $e.message"
            }
        }
        return seq
    }

    def getMailBoxSurrogateId(String type, String userName) {

        def seq
        def tsql = """select * from gcrmbac where GCRMBAC_TYPE = ? and GCRMBAC_USERNAME = ? """

        try {
            def rows = this.conn.firstRow(tsql, type, userName)
            if ( rows) {
                seq = rows?.GCRMBAC_SURROGATE_ID
            }
            else seq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcrmbac for gcroran in GcrfldrDML,  ${displayName} for ${connectInfo.tableName}. $e.message"
            }
        }
        return seq
    }

    def getOrgSurrogateIdByName(String organizationName) {
        def seq
        def tsql = """select * from gcroran where GCRORAN_NAME = ? """

        try {
            def rows = this.conn.firstRow(tsql, organizationName)
            if ( rows) {
                seq = rows?.GCRORAN_SURROGATE_ID
            }
            else seq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcroran for gcroran in GcrfldrDML,  ${parentName} for ${connectInfo.tableName}. $e.message"
            }
        }
        return seq
    }

    def getParentOrgId() {
        def seq
        def tsql = """select * from gcroran where GCRORAN_PARENT_ID IS NULL """

        try {
            def rows = this.conn.firstRow(tsql)
            if ( rows) {
                seq = rows?.GCRORAN_SURROGATE_ID
            }
            else seq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcroran for gcroran in GcrfldrDML,  ${parentName} for ${connectInfo.tableName}. $e.message"
            }
        }
        return seq
    }

    def getInteractionTypeSurrogateId(String interactionTypeName, int folderSeq) {

        def interactionTypeSeq
        def tsql = """select * from gcritpe where gcritpe_name = ? and gcritpe_folder_id = ? """

        try {
            def interactionTypeSeqRows = this.conn.firstRow(tsql, interactionTypeName, folderSeq)
            if ( interactionTypeSeqRows) {
                interactionTypeSeq = interactionTypeSeqRows?.GCRITPE_SURROGATE_ID
            }
            else interactionTypeSeq = 0
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select surrogate ID from gcritpe for gcritpe in GcrfldrDML,  ${interactionTypeName} ${folderSeq} for ${connectInfo.tableName}. $e.message"
            }
        }

        return interactionTypeSeq
    }
}
