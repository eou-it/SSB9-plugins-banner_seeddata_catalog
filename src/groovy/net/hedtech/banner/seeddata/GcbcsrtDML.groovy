/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Action Item General Tables
 */

public class GcbcsrtDML {
    int templateSeq
    def template = null
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public GcbcsrtDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {
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
     Process the Gcbcsrt records.
        The surrogate id attribute gives us the surrogate key to the Gcbcsrt which
     * is the csrt_id on the gcrcsrs records.
    */

    def processGcbcsrt() {
        //special xml characters are getting scrubbed from the xml for some reason. So doing this hack to re-introduce them into
    //the xml before it gets parsed by the xml parser
    def String[] fromstring = ["LesserThanCHAR", "GreaterThanCHAR", "AmpersandCHAR", "DoubleQuoteCHAR", "ApostropheCHAR"]
    def String[] tostring = ["&lt;", "&gt;", "&amp;", "&quot;", "&apos;"]

    def apiData = new XmlParser().parseText(xmlData)

    String ssql = """select * from GCBCSRT where GCBCSRT_NAME = ? """
    // find if the TEMPLATE already exists in the database and use it's curr_rule for inserting into the db
    try {
        def templateSeq = this.conn.firstRow(ssql, [apiData.gcbcsrt_name.text()])
        if ( templateSeq) {
            templateSeq = templateSeq?.GCBCSRT_SURROGATE_ID
            template = templateSeq?.GCBCSRT_NAME
        }
        else templateSeq = 0
    }
    catch (Exception e) {
        if (connectInfo.showErrors) {
            println "Could not select Template ID in GcbcsrtDML,  ${apiData.gcbcsrt_name.text()} from GCBCSRT for " + "${connectInfo.tableName}. $e.message"
        }
    }
    if (connectInfo.debugThis) {
        println "Selected from GCBCSRT ${templateSeq} for template ${apiData.gcbcsrt_name.text()} for ${connectInfo.tableName}."
    }

    // update the curr rule with the one that is selected
    if (connectInfo.tableName == "GCBCSRT") {
        // delete data so we can re-add instead of update so all children data is refreshed
        deleteData()
    }
    /*
    else if (connectInfo.tableName == "GCRCSRS") {
            if (apiData.GCBCSRT_SURROGATE_ID.text().toInteger() != templateSeq) {
                apiData.GCBCSRT_SURROGATE_ID[0].setValue(templateSeq.toString())
            }
    }
    */
    // parse the xml  back into  gstring for the dynamic sql loader
    if (connectInfo.tableName != "GCRCSRS") {
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

def deleteData() {
    deleteData("GCRCSRS", "delete from GCRCSRS where GCRCSRS_csrt_id in ( select gcbcsrt_surrogate_id from gcbcsrt)  ")
    deleteData("GCBCSRT", "delete from GCBCSRT where GCBCSRT_surrogate_id = ?  ")
}

def deleteData(String tableName, String sql) {
    try {

        int delRows = conn.executeUpdate(sql, [templateSeq])
        connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
    }
    catch (Exception e) {
        if (connectInfo.showErrors) {
            connectInfo.tableUpdate(tableName, 0, 0, 0, 1,0)
            println "Problem executing delete for template ${template} ${templateSeq} from GcbcsrtDML.groovy for " +
                    "${connectInfo.tableName}: $e.message"
            println "${sql}"
        }
    }
}

}
