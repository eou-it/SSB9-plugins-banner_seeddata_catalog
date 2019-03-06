/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * SGRADVR tables
 * get advisor ID from banneridadv attribute
 */

public class SgradvrDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public SgradvrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processSgradvr()

    }

    /**
     * Process the sgradvr records.
     *
     */

    def processSgradvr() {
        def apiData = new XmlParser().parseText(xmlData)

        def advisorId = apiData.ADVBANNERID?.text()
        def spridenRow
        if (advisorId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow = conn.firstRow(findPidm, [advisorId])
            if (spridenRow) {
                apiData.SGRADVR_ADVR_PIDM[0].setValue(spridenRow.SPRIDEN_PIDM.toString())
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
            println "Sgradvr Record advisor ID ${advisorId} pidm ${apiData.SGRADVR_ADVR_PIDM.text()} spridenRowPidm ${spridenRow.SPRIDEN_PIDM} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

    }

}
