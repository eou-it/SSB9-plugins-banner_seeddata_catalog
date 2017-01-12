/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Creates hours data for timesheets
 */
class PerhourDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public PerhourDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPerhour()
    }

    def processPerhour() {
        def apiData = new XmlParser().parseText(xmlData)

        def pidm
        def selectPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(selectPidm, [apiData.BANNERID.text()]) { trow ->
                pidm = trow.spriden_pidm
                connectInfo.savePidm = pidm
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select Pidm in PerhourDML for Banner ID ${apiData.BANNERID.text()} from SPRIDEN. $e.message"
            }
        }

        def jobsSeqno
        if (pidm) {
            jobsSeqno = findJobsSeqno(apiData, pidm)
        }

        if (jobsSeqno) {
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                if (fields.name() == 'PERHOUR_JOBS_SEQNO') {
                    value = jobsSeqno.toString()
                }
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"
            if (connectInfo.debugThis) {
                println "Perhour record Job Seqno ${jobsSeqno.toString()} "
            }
            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        }
    }

    def findJobsSeqno(apiData, pidm) {
        def jobsSeqno
        def selectJobSeqno = """select * from perjobs
                                 where perjobs_year = ?
                                   and perjobs_pict_code = ?
                                   and perjobs_payno = ?
                                   and perjobs_action_ind = ?
                                   and perjobs_pidm = ?
                                   and perjobs_posn = ?
                                   and perjobs_suff = ?
                                   and NVL(perjobs_coas_code_ts, '*') = NVL(?, '*')
                                   and perjobs_orgn_code_ts = ?"""
        try {
            this.conn.eachRow(selectJobSeqno, [apiData.YEAR.text(),
                                               apiData.PICT_CODE.text(),
                                               apiData.PAYNO.text(),
                                               apiData.ACTION_IND.text(),
                                               pidm,
                                               apiData.POSN.text(),
                                               apiData.SUFF.text(),
                                               apiData.COAS_CODE_TS.text(),
                                               apiData.ORGN_CODE_TS.text()]) { trow ->
                jobsSeqno = trow.perjobs_seqno
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select jobs seqno in PerhourDML for Banner ID ${apiData.BANNERID.text()}. $e.message"
            }
        }

        return jobsSeqno
    }
}
