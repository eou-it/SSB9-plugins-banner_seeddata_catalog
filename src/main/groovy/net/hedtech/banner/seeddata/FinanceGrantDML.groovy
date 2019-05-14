/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import net.hedtech.banner.seeddata.ColumnDateValue
import net.hedtech.banner.seeddata.InputData

import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * This API class creates/updates frbgrnt and frrgrpi data using the given Banner Id.
 */
class FinanceGrantDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public FinanceGrantDML(
            InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
            def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processXmlData()
    }


    def processXmlData() {
        def apiData = new XmlParser().parseText(xmlData)

        if (connectInfo.tableName == "FRBGRNT") {
            processFrbgrnt(apiData)
        } else if (connectInfo.tableName == "FRRGRPI") {
            processFrrgrpi(apiData)
        } else {
            println "XML row tag is invalid for API ${this.class.simpleName}: ${connectInfo.tableName}"
        }
    }


    def processFrbgrnt(apiData) {
        try {
            if (apiData?.PI_BANNERID?.text()) {
                def pidm = getPidm(apiData?.PI_BANNERID?.text())
                if (pidm) {
                    apiData.FRBGRNT_PI_PIDM[0].setValue(pidm.toString())
                }
            }
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = buildXmlRecord(apiData)
            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        } catch (Exception e) {
            connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing CRUD for table ${connectInfo.tableName} in API ${this.class.simpleName}: $e.message"
            }
        }
    }


    def processFrrgrpi(apiData) {
        try {
            if (apiData?.ID_BANNERID?.text()) {
                def pidm = getPidm(apiData?.ID_BANNERID?.text())
                if (pidm) {
                    apiData.FRRGRPI_ID_PIDM[0].setValue(pidm.toString())
                }
            }
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = buildXmlRecord(apiData)
            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        } catch (Exception e) {
            connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing CRUD for table ${connectInfo.tableName} in API ${this.class.simpleName}: $e.message"
            }
        }
    }


    private getPidm(bannerid) {
        def pidm
        def findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null"""

        try {
            def spridenRow = conn.firstRow(findPidm, [bannerid])
            if (spridenRow) {
                pidm = spridenRow.SPRIDEN_PIDM
            } else {
                if (connectInfo.showErrors) {
                    println "Could not find the pidm for table ${connectInfo.tableName} in API ${this.class.simpleName} for Banner Id ${bannerid}"
                }
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem finding the pidm for table ${connectInfo.tableName} in API ${this.class.simpleName} for Banner Id ${bannerid}: ${e.message}"
            }
        }

        return pidm
    }


    private buildXmlRecord(apiData) {
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        return xmlRecNew
    }
}
