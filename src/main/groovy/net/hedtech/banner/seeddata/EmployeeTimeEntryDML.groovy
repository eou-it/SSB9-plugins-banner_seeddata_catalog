/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * This API class creates/updates time entry data assuming that the perjobs record exists.
 */
class EmployeeTimeEntryDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public EmployeeTimeEntryDML(
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

        if (connectInfo.tableName == "PEREARN"
                || connectInfo.tableName == "PERELBD"
                || connectInfo.tableName == "PERHOUR"
                || connectInfo.tableName == "PERLVTK"
                || connectInfo.tableName == "PERTITO") {
            processTable(apiData)
        } else {
            println "XML row tag is invalid for API ${this.class.simpleName}: ${connectInfo.tableName}"
        }
    }


    def processTable(apiData) {
        try {
            if (apiData.YEAR?.text()) {
                def jobsSeqno = findJobsSeqno(apiData)
                if (jobsSeqno) {
                    if (connectInfo.tableName == "PEREARN") {
                        apiData.PEREARN_JOBS_SEQNO[0].setValue(jobsSeqno.toString())
                    } else if (connectInfo.tableName == "PERELBD") {
                        apiData.PERELBD_JOBS_SEQNO[0].setValue(jobsSeqno.toString())
                    } else if (connectInfo.tableName == "PERHOUR") {
                        apiData.PERHOUR_JOBS_SEQNO[0].setValue(jobsSeqno.toString())
                    } else if (connectInfo.tableName == "PERLVTK") {
                        apiData.PERLVTK_JOBS_SEQNO[0].setValue(jobsSeqno.toString())
                    } else if (connectInfo.tableName == "PERTITO") {
                        apiData.PERTITO_JOBS_SEQNO[0].setValue(jobsSeqno.toString())
                    }
                }
            }
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = buildXmlRecord(apiData)
            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        } catch (Exception e) {
            connectInfo.tableUpdate(connectInfo.tableName, 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing CRUD for table ${connectInfo.tableName} in API ${this.class.simpleName}: ${e.message}"
            }
        }
    }


    private findJobsSeqno(apiData) {
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
        def bindList = [apiData.YEAR.text(),
                        apiData.PICT_CODE.text(),
                        apiData.PAYNO.text(),
                        apiData.ACTION_IND.text(),
                        apiData.BANNERID.text(),
                        apiData.POSN.text(),
                        apiData.SUFF.text(),
                        apiData.COAS_CODE_TS.text(),
                        apiData.ORGN_CODE_TS.text()]
        try {
            def perjobsRow = conn.firstRow(selectJobSeqno, bindList)
            if (perjobsRow) {
                jobsSeqno = perjobsRow.perjobs_seqno
            }
            if (!jobsSeqno && connectInfo.showErrors) {
                println "Problem finding the jobs seqno for table ${connectInfo.tableName} in API ${this.class.simpleName} for bind variables " + bindList
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem finding the jobs seqno for table ${connectInfo.tableName} in API ${this.class.simpleName} for bind variables " + bindList + " ${e.message}"
            }
        }

        return jobsSeqno
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
