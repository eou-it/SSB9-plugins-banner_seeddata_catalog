/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Created by apoliski on 6/30/2016.
 */
class NbbposnDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public NbbposnDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNbbposn()
    }
    def processNbbposn() {
        def apiData = new XmlParser().parseText( xmlData )
        def tableName

        tableName= "NBBPOSN"
        deletePosn(tableName, apiData.NBRBPOSN_POSN.text() )

        tableName = "NBRPOSH"
        deletePosn(tableName, apiData.NBBPOSN_POSN.text() )

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        if (connectInfo.debugThis) {
            println "Nbbposn Record Position ${apiData.NBBPOSN_POSN} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
    }

    private def deletePosn(String tableName, posn) {
        def posnDel = """DELETE FROM ${tableName}  WHERE ${tableName}_posn = ?  """
        try {

            int delRows = conn.executeUpdate(posnDel, [posn])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} for position ${posn} NbbposnDML.groovy: $e.message"
                println "${posnDel}"
            }
        }
    }
}
