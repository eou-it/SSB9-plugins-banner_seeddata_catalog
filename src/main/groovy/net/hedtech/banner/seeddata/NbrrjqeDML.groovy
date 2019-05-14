/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Creates time entry routing rules at the employee's job level.
 */
class NbrrjqeDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public NbrrjqeDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processNbrrjqe()
    }

    def processNbrrjqe() {
        def apiData = new XmlParser().parseText(xmlData)
        def setupFailure = false

        def apprPidm = null
        def selectPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

        if (apiData?.APPR_BANNERID?.text()) {
            try {
                this.conn.eachRow(selectPidm, [apiData.APPR_BANNERID.text()]) { t2row ->
                    apprPidm = t2row.spriden_pidm
                }
            } catch (Exception e) {
                setupFailure = true
                if (connectInfo.showErrors) {
                    println "Could not select User Pidm in EmployeeTimeEntryExtractDML for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
                }
            }
        }

        if (!setupFailure) {
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                if (fields.name() == 'NBRRJQE_APPR_PIDM') {
                    value = apprPidm.toString()
                }
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"
            if (connectInfo.debugThis) {
                println "Nbrrjqe record Banner ID ${apiData.BANNERID.text()} "
            }
            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        }
    }

}
