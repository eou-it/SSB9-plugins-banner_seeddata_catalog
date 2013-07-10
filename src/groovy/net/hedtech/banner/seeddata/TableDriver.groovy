/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import javax.xml.parsers.ParserConfigurationException
import org.xml.sax.SAXException

/**
 * Class to parse the input file and send the XML to the correct API prepared statement class,  or
 * or the dynamic sql drive for any non validation with no api.
 * Tables with APIs will be processed using the Banner API in separate class that uses a prepared statement
 */
public class TableDriver {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def fileName

    def tableColumns = []
    def tableIndexColumns = []


    public TableDriver(InputData connectInfo, Sql conn, Connection connectCall, fileName) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.fileName = fileName
        openFile()
    }


    def openFile() {
        List columns
        List columnNames
        List indexColumns
        def apiClass = new APIClasses()
        def saveTable = ""
        def className = ""
        def isBothAPIAndDynamic = ""
        def batch = new Batch(connectCall, connectInfo)

        try {
            def xmlTable = new XmlParser().parse(new File("${fileName}"))
            xmlTable.each() {node ->
                def newTable = node.name()
                connectInfo.tableName = newTable
                def xmlRec = "<${node.name()}>\n"
                node.children().each() {fields ->
                    def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                    xmlRec += "<${fields.name()}>${value}</${fields.name()}>\n"
                }
                xmlRec += "</${node.name()}>\n"

                if (newTable == "USERID") {
                    def userId = new XmlParser().parseText(xmlRec)
                    if (userId.USERIDNAME.text()) {
                        connectInfo.userID = userId.USERIDNAME.text()
                    }
                }
                else {  // process table

                    connectInfo.tableUpdate(newTable, 1, 0, 0, 0, 0)
                    // validate this is a valid table,  some sites may not have all products installed
                    connectInfo.validateTable(conn)
                    //  find pidm value
                    def bannerIdNode = node.children().find {it.name() =~ "BANNERID" }
                    def programNode = node.children().find {it.name() =~ "PROGRAM" }
                    def deleteNode = node.children().find {it.name() =~ "DELETE" }
                    if (node.children().find {it.name() =~ "BANSECR" }) {
                        connectInfo.mepUserId = true
                        def newConn = new ConnectDB(connectInfo)
                        conn = newConn.getSqlConnection()
                        connectCall = newConn.getSessionConnection()
                    }
                    else if (connectInfo.mepUserId) {
                        connectInfo.mepUserId = false
                        def newConn = new ConnectDB(connectInfo)
                        conn = newConn.getSqlConnection()
                        connectCall = newConn.getSessionConnection()
                    }

                    if (programNode) {
                        def currRule = node.children().find { it.name() =~ "CURR_RULE"}
                        if (!currRule) {
                            programNode = null
                        }
                    }

                    if (bannerIdNode) {
                        String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                        def spridenRow = conn.firstRow(findPidm, [bannerIdNode.text()])
                        if (spridenRow) {
                            connectInfo.saveStudentPidm = spridenRow.SPRIDEN_PIDM
                        }
                        else {
                            connectInfo.saveStudentPidm = null
                        }
                    }
                    else {
                        connectInfo.saveStudentPidm = null
                    }



                    columnNames = []
                    if ((!saveTable) || (saveTable != newTable)) {

                        node.children().each() {fields ->
                            columnNames.add(fields.name())
                        }
                        className = apiClass.findClass(newTable)
                        isBothAPIAndDynamic = apiClass.isBoth(newTable)

                        if (((!className) || (className && isBothAPIAndDynamic)) && (connectInfo.validTable)) {
                            if (connectInfo.debugThis) {
                                println "--------- New Table ${newTable} ----------"
                            }
                            def savedTableColumns = tableColumns.find { it.key == newTable}

                            if (savedTableColumns == null) {
                                Tables tabs = new Tables(newTable, connectInfo, conn, columnNames)
                                columns = tabs.columns
                                indexColumns = tabs.indexColumns
                                tableColumns << [key: newTable, value: columns]
                                tableIndexColumns << [key: newTable, value: indexColumns]
                            }
                            else {
                                columns = savedTableColumns.value
                                indexColumns = tableIndexColumns.find() { it.key == newTable}.value

                            }
                        }
                        else {
                            columns = []
                            indexColumns = []
                        }

                        if (connectInfo.debugThis) {
                            if (className == null)
                                println "New Table ${newTable} Does not have an API. Columns returned ${columns.size()} index columns ${indexColumns.size()}"
                            else
                                println "New Table ${newTable} has an API class ${className}"
                        }
                    }
                    if (connectInfo.debugThis) {
                        println "--------- New Record for Table ${newTable} ----------"
                        println "XML data: ${xmlRec}"
                    }


                    if (className) {
                        if (isBothAPIAndDynamic) {
                            Thread.currentThread().contextClassLoader.loadClass(className).newInstance(connectInfo, conn, connectCall, xmlRec, columns, indexColumns, batch, deleteNode)
                        }
                        else {
                            Thread.currentThread().contextClassLoader.loadClass(className).newInstance(connectInfo, conn, connectCall, xmlRec)
                        }
                    }

                    else {
                        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRec, columns, indexColumns, batch, deleteNode)

                    }

                    saveTable = newTable

                }

                if (connectInfo.mepUserId) {
                    if (!connectInfo.saveThis) {
                        conn.execute "{ call gb_common.p_rollback() }"
                    }
                    else {
                        conn.execute "{ call gb_common.p_commit() }"
                    }
                }
            }

        }


        catch (IOException io) {
            println "The file ${fileName} does not appear to be a valid file. ${io.message}"
        } catch (ParserConfigurationException pare) {
            println "The file ${fileName} has a problem with the parser XML configuration. ${pare.message}"
        } catch (SAXException saxe) {
            println "The file ${fileName} has a problem with the XML data. ${saxe.message} "
        }

    }
}
