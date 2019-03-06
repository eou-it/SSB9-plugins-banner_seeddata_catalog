/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * Class to read the table xml file and insert records using dynamic SQL
 * The tables processed by this class do not have a Banner SQL API
 * The sql statements are created when the table is first encountered and stored in array
 * so they can be reused 
 */
public class DynamicSQLTableXMLRecord {

    def InputData connectInfo
    def Batch batch
    Sql conn
    def codeColumn = ""
    def codeValue = ""
    Connection connectCall
    def xmlData
    List columns
    List indexColumns


    public DynamicSQLTableXMLRecord(InputData connectInfo, Sql conn, Connection connectCall, xmlData,
                                    List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        // batch process was remove because it did not help in performance 
        this.batch = batch

        if (deleteNode && deleteNode?.text() == "YES") processDelete()
        else processFile()
    }

    /**
     *  Open the xml record for validation data and and parse
     */

    def processFile() {

        String validationSQL = ""
        def tableName = ""

        def validation = new XmlParser().parseText(xmlData)
        tableName = connectInfo.tableName

        // table is the first 7 chars of xml element string
        def newTable = tableName
        if (connectInfo.validTable) {
            Columns col = new Columns(newTable, connectInfo, conn, tableName, columns, indexColumns, xmlData)

            String insertSQL = col.createInsertSQL()
            if (connectInfo.debugThis) {println "Dynamic insert sql: ${insertSQL}" }

            String matchSQL = col.createIndexSQL()
            if (connectInfo.debugThis) { println "Dynamic match sql: ${matchSQL }" }

            String updateSQL = col.createUpdateSQL()
            if (connectInfo.debugThis) { println "Dynamic update sql: ${ updateSQL } " }


            def rowCount = 0
            try {
                rowCount = conn.rows(matchSQL).size()
            }
            catch (Exception e) {
                connectInfo.tableUpdate(newTable, 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Problem executing match sql for table ${newTable} from  DynamicSQLTableXMLRecord.groovy: $e.message"
                    println "${matchSQL}"
                }
            }
            if (connectInfo.debugThis) { println "Rows return from matchSql: ${ rowCount } " }
            if (rowCount > 0) {
                if (this.connectInfo.replaceData) {
                    try {
                        conn.executeUpdate(updateSQL)
                        connectInfo.tableUpdate(newTable, 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate(newTable, 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Problem executing update for table ${newTable} from  DynamicSQLTableXMLRecord.groovy: $e.message"
                            println "${updateSQL}"
                        }
                    }
                }
            }
            else {
                try {
                    conn.executeUpdate(insertSQL)
                    connectInfo.tableUpdate(newTable, 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate(newTable, 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Problem executing insert for table ${newTable} from  DynamicSQLTableXMLRecord.groovy: $e.message"
                        println "${insertSQL}"
                    }
                }
            }
        }
        else {
            println "New Table ${newTable} is not a valid table"
        }
    }


    def processDelete() {

        def validation = new XmlParser().parseText(xmlData)
        def tableName = connectInfo.tableName

        // table is the first 7 chars of xml element string
        def newTable = tableName
        Columns col = new Columns(newTable, connectInfo, conn, tableName, columns, indexColumns, xmlData)

        String deleteSQL = col.createDeleteSQL()
        if (connectInfo.debugThis) {println "Dynamic delete sql: ${deleteSQL}" }
        def delRows = 0
        try {
            delRows = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate(newTable, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            connectInfo.tableUpdate(newTable, 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete for table ${newTable} from  DynamicSQLTableXMLRecord.groovy: $e.message"
                println "${deleteSQL}"
            }
        }
    }
}
