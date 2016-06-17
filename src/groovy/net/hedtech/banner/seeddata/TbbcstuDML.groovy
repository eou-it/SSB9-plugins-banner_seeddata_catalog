/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TBBCSTU table
 * get student ID from STUDENTBANNERID attribute
 * get third party ID from THIRDPARTYBANNERID attribute
 */

public class TbbcstuDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public TbbcstuDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processTbbcstu()

    }

    /**
     * Process the sgradvr records.
     *
     */

    def processTbbcstu() {
        def apiData = new XmlParser().parseText(xmlData)

        def studentId = apiData.STUDENTBANNERID?.text()
        def spridenRow1
        if (studentId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow1 = conn.firstRow(findPidm, [studentId])
            if (spridenRow1) {
                apiData.TBBCSTU_STU_PIDM[0].setValue(spridenRow1.SPRIDEN_PIDM.toString())
            }
        }

        def thirdPartyId = apiData.THIRDPARTYBANNERID?.text()
        def spridenRow2
        if (thirdPartyId) {
            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            spridenRow2 = conn.firstRow(findPidm, [thirdPartyId])
            if (spridenRow2) {
                apiData.TBBCSTU_CONTRACT_PIDM[0].setValue(spridenRow2.SPRIDEN_PIDM.toString())
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
            println "Tbbcstu Record Student ID ${studentId} pidm ${apiData.TBBCSTU_STU_PIDM.text()} spridenRow1Pidm ${spridenRow1.SPRIDEN_PIDM} "
            println "Tbbcstu Record ThirdParty ID ${thirdPartyId} pidm ${apiData.TBBCSTU_CONTRACT_PIDM.text()} spridenRow2Pidm ${spridenRow2.SPRIDEN_PIDM} "
        }
        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

    }

}
