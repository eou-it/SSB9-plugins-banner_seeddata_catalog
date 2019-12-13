/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * This API class updates FTVFUND and FTVORGN with the financial manager's pidm using the given Banner Id.
 */
class FinanceFoapalManagerUpdateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public FinanceFoapalManagerUpdateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processXmlData()
    }


    def processXmlData() {
        def apiData = new XmlParser().parseText(xmlData)

        if (connectInfo.tableName == "FTVFUND_MANAGER") {
            updateFtvfund(apiData)
        } else if (connectInfo.tableName == "FTVORGN_MANAGER") {
            updateFtvorgn(apiData)
        }else {
            println "XML row tag is invalid for API ${this.class.simpleName}: ${connectInfo.tableName}"
        }
    }


    def updateFtvfund(apiData) {
        def bindList = []

        try {
            def pidm = getPidm(apiData?.FMGR_CODE_BANNERID?.text())

            bindList = [pidm,
                        apiData.COAS_CODE.text(),
                        apiData.FUND_CODE.text(),
                        parseDate(apiData.EFF_DATE.text()),
                        parseDate(apiData.NCHG_DATE.text())]

            int count = this.conn.executeUpdate("""update ftvfund
                                                  set ftvfund_fmgr_code_pidm = ?
                                                where ftvfund_coas_code = ?
                                                  and ftvfund_fund_code = ?
                                                  and TRUNC(ftvfund_eff_date) = TRUNC(?)
                                                  and TRUNC(ftvfund_nchg_date) = TRUNC(?) """, bindList)

            connectInfo.tableUpdate("FTVFUND", 0, 0, count, 0, 0)
        } catch (Exception e) {
            connectInfo.tableUpdate("FTVFUND", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update FTVFUND failed for banner Id ${apiData?.FMGR_CODE_BANNERID?.text()}, key: " + bindList
                println "Problem executing update for table FTVFUND from ${this.class.simpleName}: $e.message"
            }
        }
    }


    def updateFtvorgn(apiData) {
        def bindList = []

        try {
            def pidm = getPidm(apiData?.FMGR_CODE_BANNERID?.text())

            bindList = [pidm,
                        apiData.COAS_CODE.text(),
                        apiData.ORGN_CODE.text(),
                        parseDate(apiData.EFF_DATE.text()),
                        parseDate(apiData.NCHG_DATE.text())]

            int count = this.conn.executeUpdate("""update ftvorgn
                                                  set ftvorgn_fmgr_code_pidm = ?
                                                where ftvorgn_coas_code = ?
                                                  and ftvorgn_orgn_code = ?
                                                  and TRUNC(ftvorgn_eff_date) = TRUNC(?)
                                                  and TRUNC(ftvorgn_nchg_date) = TRUNC(?) """, bindList)

            connectInfo.tableUpdate("FTVORGN", 0, 0, count, 0, 0)
        } catch (Exception e) {
            connectInfo.tableUpdate("FTVORGN", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update FTVORGN failed for banner Id ${apiData?.FMGR_CODE_BANNERID?.text()}, key: " + bindList
                println "Problem executing update for table FTVORGN from ${this.class.simpleName}: $e.message"
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


    private java.sql.Date parseDate(String stringDate) {
        def columnDate = new ColumnDateValue(stringDate)
        def javaDate = columnDate.formatJavaDate()
        def formatter = new SimpleDateFormat("yyyy-MM-dd");
        def sqlDate = new java.sql.Date(formatter.parse(javaDate).getTime());

        return sqlDate
    }
}
