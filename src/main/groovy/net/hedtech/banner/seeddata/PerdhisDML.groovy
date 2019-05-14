/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Created by apoliski on 6/8/2016.
 * Get Banner ID for W-4 Signed ID from W4_SIGNED_BANNERID attribute
 */
class PerdhisDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public PerdhisDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPerdhis()
    }
    /**
     * Process the perdhis records.
     *
     */

    def processPerdhis() {
        def apiData = new XmlParser().parseText(xmlData)

        def w4Id = apiData?.W4_SIGNED_BANNERID?.text()
        def spridenRow
        if (w4Id) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow = conn.firstRow(findPidm, [w4Id])
            if (spridenRow) {
                apiData.PERDHIS_W4_SIGNED_PIDM[0].setValue(spridenRow.SPRIDEN_PIDM.toString())
            }
        }
        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${apiData.name()}>\n"
        apiData.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${apiData.name()}>\n"
        if (connectInfo.debugThis) {
            println "Perdhis Record W-4 Signed ID ${w4Id} pidm ${apiData.PERDHIS_W4_SIGNED_PIDM.text()} spridenRowPidm ${spridenRow.SPRIDEN_PIDM} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

    }
}
