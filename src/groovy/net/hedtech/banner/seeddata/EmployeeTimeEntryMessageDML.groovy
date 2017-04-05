/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection

/**
 * Creates time sheet with messages logged in the PHRERRL table for employees using a PL/SQL package call
 */
class EmployeeTimeEntryMessageDML {

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


    public EmployeeTimeEntryMessageDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmployeeTimeEntryMessageDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        submitEmployeeTimeSheet()
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


    def submitEmployeeTimeSheet() {
        boolean setupFailure = false
        def pidm = null
        def userPidm = null
        def proxyPidm = null
        def jobSequenceNo = null
        def selectPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

        try {
            this.conn.eachRow(selectPidm, [this.bannerid]) { trow ->
                pidm = trow.spriden_pidm
                connectInfo.savePidm = pidm
            }
        } catch (Exception e) {
            setupFailure = true
            if (connectInfo.showErrors) {
                println "Could not select Pidm in EmployeeTimeEntryMessageDML for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
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
                    println "Could not select User Pidm in EmployeeTimeEntryMessageDML for Banner ID ${this.bannerid_user} from SPRIDEN. $e.message"
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
                    println "Could not select Proxy Pidm in EmployeeTimeEntryMessageDML for Banner ID ${this.bannerid_proxy} from SPRIDEN. $e.message"
                }
            }
        }

        if (pidm) {
            def selectJobSequenceNo = """select perjobs_seqno from perjobs where perjobs_year = ?
               and perjobs_pict_code = ?
               and perjobs_payno = ?
               and perjobs_pidm = ?
               and perjobs_posn = ?
               and perjobs_suff = ?
               and perjobs_orgn_code_ts =  ?
               and nvl(perjobs_coas_code_ts, '*') = nvl(?, '*')
               and perjobs_action_ind = ?"""

            try {
                this.conn.eachRow(selectJobSequenceNo, [this.perjobs_year, this.perjobs_pict_code, this.perjobs_payno, pidm, this.perjobs_posn, this.perjobs_suff,
                                                        this.perjobs_orgn_code_ts, this.perjobs_coas_code_ts, this.perjobs_action_ind,]) { t4row ->
                    jobSequenceNo = t4row.perjobs_seqno
                    connectInfo.savePidm = pidm
                }
            } catch (Exception e) {
                setupFailure = true
                if (connectInfo.showErrors) {
                    println "Could not select Job Sequence No in EmployeeTimeEntryMessageDML for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
                }
            }
        }

        CallableStatement sqlCall

        if (!setupFailure) {
            if (jobSequenceNo) {
                sqlCall = this.connectCall.prepareCall("{ call pekteap.p_submit_time(?,?,?,?,?,?,?) }")

                if (jobSequenceNo) {
                    sqlCall.setLong(1, jobSequenceNo.toInteger())
                } else {
                    sqlCall.setNull(1, java.sql.Types.INTEGER)
                }
                sqlCall.setString(2, this.user_role)
                if (userPidm) {
                    sqlCall.setInt(3, userPidm.toInteger())
                } else {
                    sqlCall.setNull(3, java.sql.Types.INTEGER)
                }
                if (proxyPidm) {
                    sqlCall.setInt(4, proxyPidm.toInteger())
                } else {
                    sqlCall.setNull(4, java.sql.Types.INTEGER)
                }
                sqlCall.setString(5, this.source)
                sqlCall.registerOutParameter(6, java.sql.Types.VARCHAR)
                sqlCall.registerOutParameter(7, java.sql.Types.VARCHAR)

                try {
                    sqlCall.executeUpdate()
                    connectInfo.tableUpdate("PHRERRL", 0, 1, 0, 0, 0)
                    println "Created timesheet messages for job seqno: ${jobSequenceNo.toString()}"
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("PHRERRL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert PHRERRL for Banner ID ${this.bannerid}, pidm ${pidm.toString()}"
                        println "Problem creating timesheet messages from EmployeeTimeEntryMessageDML.groovy: $e.message"
                    }
                }
                finally {
                    sqlCall.close()
                }
            }
        }
    }
}
