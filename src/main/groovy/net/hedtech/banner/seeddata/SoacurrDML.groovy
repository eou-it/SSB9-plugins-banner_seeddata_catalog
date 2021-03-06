/*********************************************************************************
  Copyright 2010-2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * SOACURR tables
 * replace the curr rule in the xml 
 */

public class SoacurrDML {
    int currRule = 0
    def program = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SoacurrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSoacurr()

    }

    /**
     * Process the soacurr records.  The sobcurr_curr_rule is a one up across all records and needs to be re-calculated for any
     * new database so it does not conflict with existing data.  The same is true for the cmjr_rule, ccon_rule and cmnr_rule.  All
     * are one up across all records.
     *
     */
    def processSoacurr() {
        def apiData = new XmlParser().parseText(xmlData)
        def ssql = null

        //Determine the seed data we are working with
        if ( apiData.SOBCURR_LEVL_CODE.text() )   //SOBCURR requires level code
        {
            //Working with SOBCURR data
            ssql = """select * from sobcurr where sobcurr_levl_code=? and NVL(sobcurr_camp_code,'x')=NVL(?,'x') and NVL(sobcurr_coll_code,'x')=NVL(?,'x') 
                             and NVL(sobcurr_degc_code,'x')=NVL(?,'x') and NVL(sobcurr_program,'x')=NVL(?,'x') """
            // find if the program already exists in the database and use it's curr_rule for inserting into the db
            try {
                program = this.conn.firstRow(ssql, [apiData.SOBCURR_LEVL_CODE.text(), apiData.SOBCURR_CAMP_CODE.text(), apiData.SOBCURR_COLL_CODE.text(),
                                                    apiData.SOBCURR_DEGC_CODE.text(), apiData.PROGRAM.text()])
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select ID in SoacurrDML,  ${apiData.PROGRAM.text()} from sobcurr for ${connectInfo.tableName}. $e.message"
                }
            }
        }
        else {
            //Working with SOBCURR child data which, for seed purposes, requires a Program value
            ssql = """select * from sobcurr  where sobcurr_program = ? """
            try {
                program = this.conn.firstRow(ssql, [apiData.PROGRAM.text()])
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select ID in SoacurrDML,  ${apiData.PROGRAM.text()} from sobcurr for ${connectInfo.tableName}. $e.message"
                }
            }
        }

        if (!program) {
            ssql = """SELECT nvl(MAX(SOBCURR_CURR_RULE),0) + 1 rule  FROM sobcurr """
            currRule = this.conn.firstRow(ssql).RULE
        }
        else {
            currRule = program.SOBCURR_CURR_RULE
        }

        if (connectInfo.debugThis) {
            println "Curric Rule for ${currRule} for program  ${apiData.PROGRAM.text()} from sobcurr for ${connectInfo.tableName}."
        }

        // update the curr rule with the one that is selected
        if (connectInfo.tableName == "SOBCURR") {
            // delete data so we can re-add instead of update so all children data is refreshed
            if (connectInfo.replaceData) {
                deleteData()
            }
            if (apiData.SOBCURR_CURR_RULE.text().toInteger() != currRule) {
                apiData.SOBCURR_CURR_RULE[0].setValue(currRule.toString())
            }

        }
        // update the major rule and the curr rule
        else if (connectInfo.tableName == "SORCMJR") {
            if (apiData.SORCMJR_CURR_RULE.text().toInteger() != currRule) {
                apiData.SORCMJR_CURR_RULE[0].setValue(currRule.toString())
            }
            ssql = """SELECT nvl(MAX(SORCMJR_CMJR_RULE),0) + 1 rule  FROM sorcmjr """
            def cmjr = this.conn.firstRow(ssql).RULE
            apiData.SORCMJR_CMJR_RULE[0].setValue(cmjr.toString())

        }
        // update the concentration  rule and the curr rule
        else if (connectInfo.tableName == "SORCCON") {
            if (apiData.SORCCON_CURR_RULE.text().toInteger() != currRule) {
                apiData.SORCCON_CURR_RULE[0].setValue(currRule.toString())
            }
            if (apiData.MAJOR.text()) {
                ssql = """SELECT sorcmjr_cmjr_rule  FROM sorcmjr where sorcmjr_majr_code = ?   and sorcmjr_curr_rule = ? """
                def cmjr_rule = this.conn.firstRow(ssql, [apiData.MAJOR.text(), currRule])
                apiData.SORCCON_CMJR_RULE[0].setValue(cmjr_rule?.sorcmjr_cmjr_rule?.toString())
            }
            ssql = """SELECT nvl(MAX(SORCCON_CCON_RULE),0) + 1 rule  FROM sorccon """
            def cmjr = this.conn.firstRow(ssql).RULE
            apiData.SORCCON_CCON_RULE[0].setValue(cmjr.toString())

        }
        // update the minor curr rule and the minor rule
        else if (connectInfo.tableName == "SORCMNR") {
            if (apiData.SORCMNR_CURR_RULE.text().toInteger() != currRule) {
                apiData.SORCMNR_CURR_RULE[0].setValue(currRule.toString())
            }
            ssql = """SELECT nvl(MAX(SORCMNR_CMNR_RULE),0) + 1 rule  FROM sorcmnr """
            def cmjr = this.conn.firstRow(ssql).RULE
            apiData.SORCMNR_CMNR_RULE[0].setValue(cmjr.toString())

        }
        // update the curr rule on the control record
        else if (connectInfo.tableName == "SORMCRL") {

            if (apiData.SORMCRL_CURR_RULE.text().toInteger() != currRule) {
                apiData.SORMCRL_CURR_RULE[0].setValue(currRule.toString())
            }

        }
        // update the concentration  rule and the curr rule
        else if (connectInfo.tableName == "SARWAPC") {
            apiData.SARWAPC_CURR_RULE[0].setValue(currRule.toString())
            if (apiData.MAJOR.text()) {
                ssql = """SELECT sorcmjr_cmjr_rule  FROM sorcmjr where  sorcmjr_majr_code = ?  and sorcmjr_curr_rule = ? """
                def cmjr_rule = this.conn.firstRow(ssql, [apiData.MAJOR.text(), currRule])
                apiData.SARWAPC_CMJR_RULE[0].setValue(cmjr_rule?.sorcmjr_cmjr_rule?.toString())
            }

        }
          else if (connectInfo.tableName == "SARWADF") {
            apiData.SARWADF_CURR_RULE[0].setValue(currRule.toString())
            if (apiData.MAJOR.text()) {
                ssql = """SELECT sorcmjr_cmjr_rule  FROM sorcmjr where  sorcmjr_majr_code = ?   and sorcmjr_curr_rule = ? """
                def cmjr_rule = this.conn.firstRow(ssql, [apiData.MAJOR.text(), currRule])
                apiData.SARWADF_CMJR_RULE[0].setValue(cmjr_rule?.sorcmjr_cmjr_rule?.toString())
            }

        }
        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() {fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
    }


    def deleteData() {
        String selectSql = """delete from sormcrl where sorcmrl_curr_rule = ? """

        deleteData("SARWADF", "delete from sarwadf where  exists ( select 1 from sorcmjr where sorcmjr_cmjr_rule = sarwadf_cmjr_rule and sorcmjr_curr_rule = ? ) ")
        deleteData("SARWAPC", "delete from sarwapc where  exists ( select 1 from sorcmjr where sorcmjr_cmjr_rule = sarwapc_cmjr_rule and sorcmjr_curr_rule = ? ) ")
        deleteData("SORMCRL", "delete from sormcrl where sormcrl_curr_rule = ? ")
        deleteData("SORCMJR", "delete from sorcmjr where sorcmjr_curr_rule = ? ")
        deleteData("SORCMNR", "delete from sorcmnr where sorcmnr_curr_rule = ? ")
        deleteData("SORCCON", "delete from sorccon where sorccon_curr_rule = ? ")
        deleteData("SOBCURR", "delete from sobcurr where sobcurr_curr_rule = ? ")
    }


    def deleteData(String tableName, String sql) {
        try {
            int delRows = conn.executeUpdate(sql, [currRule])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for curriculum rule ${program.SOBCURR_PROGRAM} ${currRule} from SoacurrDML.groovy for ${connectInfo.tableName}: $e.message"
                println "${sql}"
            }
        }
    }

}
