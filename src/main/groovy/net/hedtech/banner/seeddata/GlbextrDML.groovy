/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * GLBEXTR tables
 * get pidm from key attribute
 */

public class GlbextrDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public GlbextrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        if (deleteNode && deleteNode?.text() == "YES") processDelete()
        else processGlbextr()

    }

    def processDelete() {

        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode)

    }


    def processGlbextr() {
        def apiData = new XmlParser().parseText(xmlData)

        def xmlRecNew
        def bannerId = apiData.BANNERID?.text()
        if ( bannerId ) {
            def spridenRow
            if (bannerId) {
                String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
                spridenRow = conn.firstRow(findPidm, [bannerId])
                if (spridenRow) {
                    apiData.GLBEXTR_KEY[0].setValue(spridenRow.SPRIDEN_PIDM.toString())
                }
            }
            // parse the xml  back into  gstring for the dynamic sql loader
            xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"
            if (connectInfo.debugThis) {
                println "Glbextr ${bannerId} pidm ${apiData.GLBEXTR_KEY.text()} spridenRowPidm ${spridenRow.SPRIDEN_PIDM} "
            }
        }
        else {
            xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"
            if (connectInfo.debugThis) {
                println "Glbextr doesnt have ${bannerId} pidm ${apiData.GLBEXTR_KEY.text()}   "
            }
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

    }

}
