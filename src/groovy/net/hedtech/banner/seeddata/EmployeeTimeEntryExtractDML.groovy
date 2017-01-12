/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Creates time sheet data for employees using a PL/SQL api
 */
class EmployeeTimeEntryExtractDML {

    def bannerid
    def bannerid_user
    def bannerid_proxy
    def perjobs_action_ind
    def perjobs_year
    def perjobs_pict_code
    def perjobs_payno
    def perjobs_posn
    def perjobs_suff
    def perjobs_coas_code_ts
    def perjobs_orgn_code_ts
    def user_role
    def source

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public EmployeeTimeEntryExtractDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmployeeTimeEntryExtractDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        extractEmployeeTimeSheet()
    }


    def parseXmlData() {
        def timesheet = new XmlParser().parseText(xmlData)

        this.bannerid = timesheet.BANNERID.text()
        this.bannerid_user = timesheet.BANNERID_USER.text()
        this.bannerid_proxy = timesheet.BANNERID_PROXY.text()
        this.perjobs_action_ind = timesheet.PERJOBS_ACTION_IND.text()
        this.perjobs_year = timesheet.PERJOBS_YEAR.text()
        this.perjobs_pict_code = timesheet.PERJOBS_PICT_CODE.text()
        this.perjobs_payno = timesheet.PERJOBS_PAYNO.text()
        this.perjobs_posn = timesheet.PERJOBS_POSN.text()
        this.perjobs_suff = timesheet.PERJOBS_SUFF.text()
        this.perjobs_coas_code_ts = timesheet.PERJOBS_COAS_CODE_TS.text()
        this.perjobs_orgn_code_ts = timesheet.PERJOBS_ORGN_CODE_TS.text()
        this.user_role = timesheet.USER_ROLE.text()
        this.source = timesheet.SOURCE.text()
    }


    def extractEmployeeTimeSheet() {
        boolean setupFailure = false
        def pidm = null
        def userPidm = null
        def proxyPidm = null
        def selectPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

        try {
            this.conn.eachRow(selectPidm, [this.bannerid]) { trow ->
                pidm = trow.spriden_pidm
                connectInfo.savePidm = pidm
            }
        } catch (Exception e) {
            setupFailure = true
            if (connectInfo.showErrors) {
                println "Could not select Pidm in EmployeeTimeEntryExtractDML for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
            }
        }

        if (this.bannerid_user) {
            try {
                this.conn.eachRow(selectPidm, [this.bannerid_user]) { t2row ->
                    userPidm = t2row.spriden_pidm
                }
            } catch (Exception e) {
                setupFailure = true
                if (connectInfo.showErrors) {
                    println "Could not select User Pidm in EmployeeTimeEntryExtractDML for Banner ID ${this.bannerid_user} from SPRIDEN. $e.message"
                }
            }
        }

        if (this.bannerid_proxy) {
            try {
                this.conn.eachRow(selectPidm, [this.bannerid_proxy]) { t3row ->
                    proxyPidm = t3row.spriden_pidm
                }
            } catch (Exception e) {
                setupFailure = true
                if (connectInfo.showErrors) {
                    println "Could not select Proxy Pidm in EmployeeTimeEntryExtractDML for Banner ID ${this.bannerid_proxy} from SPRIDEN. $e.message"
                }
            }
        }

        CallableStatement sqlCall

        if (!setupFailure) {
            sqlCall = this.connectCall.prepareCall("{ call pektess.p_extract_time(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }")

            sqlCall.setString(1, this.perjobs_year)
            sqlCall.setString(2, this.perjobs_pict_code)
            if (this.perjobs_payno) {
                sqlCall.setInt(3, this.perjobs_payno.toInteger())
            } else {
                sqlCall.setNull(3, java.sql.Types.INTEGER)
            }
            sqlCall.setString(4, this.perjobs_action_ind)
            if (pidm) {
                sqlCall.setInt(5, pidm.toInteger())
            } else {
                sqlCall.setNull(5, java.sql.Types.INTEGER)
            }
            sqlCall.setString(6, this.perjobs_posn)
            sqlCall.setString(7, this.perjobs_suff)
            sqlCall.setString(8, this.perjobs_coas_code_ts)
            sqlCall.setString(9, this.perjobs_orgn_code_ts)
            sqlCall.setString(10, this.user_role)
            if (userPidm) {
                sqlCall.setInt(11, userPidm.toInteger())
            } else {
                sqlCall.setNull(11, java.sql.Types.INTEGER)
            }
            if (proxyPidm) {
                sqlCall.setInt(12, proxyPidm.toInteger())
            } else {
                sqlCall.setNull(12, java.sql.Types.INTEGER)
            }
            sqlCall.setString(13, this.source)
            sqlCall.registerOutParameter(14, java.sql.Types.INTEGER)
            sqlCall.registerOutParameter(15, java.sql.Types.VARCHAR)
            sqlCall.registerOutParameter(16, java.sql.Types.VARCHAR)

            try {
                sqlCall.executeUpdate()

                if (sqlCall.getString(15) == 'ERROR') {
                    connectInfo.tableUpdate("PERJOBS", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert PERJOBS for Banner ID ${this.bannerid}, pidm ${pidm.toString()}"
                        println "Problem creating timesheets from EmployeeTimeEntryExtractDML.groovy: ${sqlCall.getString(16)}"
                    }
                } else {
                    connectInfo.tableUpdate("PERJOBS", 0, 1, 0, 0, 0)
                    println "Created timesheet job seqno: ${sqlCall.getLong(14).toString()}"
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("PERJOBS", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert PERJOBS for Banner ID ${this.bannerid}, pidm ${pidm.toString()}"
                    println "Problem creating timesheets from EmployeeTimeEntryExtractDML.groovy: $e.message"
                }
            }
            finally {
                sqlCall.close()
            }
        }
    }
}
