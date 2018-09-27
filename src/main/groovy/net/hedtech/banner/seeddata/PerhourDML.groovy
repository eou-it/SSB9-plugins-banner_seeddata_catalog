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
        def jobsSeqno = findJobsSeqno(apiData)


        if (jobsSeqno) {

            if(!apiData.PERHOUR_TIME_ENTRY_DATE[0]?.value()[0])
            {
               def startDate  = fetchPayPeriodStartDate(apiData)
                apiData.PERHOUR_TIME_ENTRY_DATE[0].setValue(startDate?.toString())
            }


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

    def findJobsSeqno(apiData) {
        def jobsSeqno
        def selectJobSeqno = """select * from perjobs
                                 where perjobs_year = ?
                                   and perjobs_pict_code = ?
                                   and perjobs_payno = ?
                                   and perjobs_action_ind = ?
                                   and perjobs_pidm = (select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null)
                                   and perjobs_posn = ?
                                   and perjobs_suff = ?
                                   and NVL(perjobs_coas_code_ts, '*') = NVL(?, '*')
                                   and perjobs_orgn_code_ts = ?"""
        try {
            this.conn.eachRow(selectJobSeqno, [apiData.YEAR.text(),
                                               apiData.PICT_CODE.text(),
                                               apiData.PAYNO.text(),
                                               apiData.ACTION_IND.text(),
                                               apiData.BANNERID.text(),
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


    
    def fetchPayPeriodStartDate(apiData) {
        def startDate
        def selectStartDate = """select TO_CHAR(ptrcaln_start_date,'MM/DD/YYYY') startDate from ptrcaln
                                 where ptrcaln_year = ?
                                   and ptrcaln_pict_code = ?
                                   and ptrcaln_payno = ?    """
        try {
            this.conn.eachRow(selectStartDate, [apiData.YEAR.text(),
                                               apiData.PICT_CODE.text(),
                                               apiData.PAYNO.text()]) { trow ->
                startDate = trow.startDate
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not pick startDate from ptrcaln in PerhourDML for PayPeriod " +
                        "${apiData.YEAR.text()} ${apiData.PICT_CODE.text()} ${apiData.PAYNO.text()} $e.message"
            }
        }

        return startDate
    }
}
