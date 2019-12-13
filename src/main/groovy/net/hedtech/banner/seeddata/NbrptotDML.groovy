/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Created by apoliski on 6/8/2016.
 * Delete position budget data
 */
class NbrptotDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public NbrptotDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNbrptot()
    }
    /**
     * Process Position Budget Fiscal Year Delete.
     *
     */


    def processNbrptot() {
        def apiData = new XmlParser().parseText( xmlData )
        def tableName = "NBRPTOT"
        deletePosnBudgetData(tableName, apiData.NBRPTOT_POSN.text(), apiData.NBRPTOT_FISC_CODE.text() )

        tableName = "NBRRTOT"
        deletePosnBudgetData(tableName, apiData.NBRPTOT_POSN.text(), apiData.NBRPTOT_FISC_CODE.text() )

        tableName = "NBRFTOT"
        deletePosnBudgetData(tableName, apiData.NBRPTOT_POSN.text(), apiData.NBRPTOT_FISC_CODE.text() )

        tableName = "NBRPLBD"
        deletePosnBudgetData(tableName, apiData.NBRPTOT_POSN.text(), apiData.NBRPTOT_FISC_CODE.text() )

        tableName = "NBRBHIS"
        deletePosnBudgetData(tableName, apiData.NBRPTOT_POSN.text(), apiData.NBRPTOT_FISC_CODE.text() )

        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        if (connectInfo.debugThis) {
            println "Nbrptot Record Fiscal Year ${apiData.NBRPTOT_FISC_CODE} Position ${apiData.NBRPTOT_POSN} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
    }

    private def deletePosnBudgetData(String tableName, posn, fiscCode) {
        def budgetDel = """DELETE FROM ${tableName}  WHERE ${tableName}_posn = ? AND ${tableName}_fisc_code =? """
        try {

            int delRows = conn.executeUpdate(budgetDel, [posn, fiscCode])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} for position ${posn} fiscal year ${fisc_code} NbrptotDML.groovy: $e.message"
                println "${budgetDel}"
            }
        }
    }
}
