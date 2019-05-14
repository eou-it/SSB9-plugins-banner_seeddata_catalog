/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Submits timesheets with messages logged in the PHRERRL table for employees using a PL/SQL package call.
 */
class EmployeeTimeEntrySubmitDML {

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


    public EmployeeTimeEntrySubmitDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmployeeTimeEntrySubmitDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
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
        def apidm = null
        def proxyPidm = null
        def jobSequenceNo = null
        def queueErrorNumber = null
        def queueErrorMessage = null
        def selectPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

        try {
            this.conn.eachRow(selectPidm, [this.bannerid]) { trow ->
                pidm = trow.spriden_pidm
                connectInfo.savePidm = pidm
            }
        } catch (Exception e) {
            setupFailure = true
            if (connectInfo.showErrors) {
                println "Could not select Pidm in ${this.class.simpleName} for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
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
                    println "Could not select User Pidm in ${this.class.simpleName} for Banner ID ${this.bannerid_user} from SPRIDEN. $e.message"
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
                    println "Could not select Proxy Pidm in ${this.class.simpleName} for Banner ID ${this.bannerid_proxy} from SPRIDEN. $e.message"
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
                    println "Could not select Job Sequence No in ${this.class.simpleName} for Banner ID ${this.bannerid} from SPRIDEN. $e.message"
                }
            }
        }

        if (!setupFailure) {
            def timeData = [this.bannerid, this.perjobs_year, this.perjobs_pict_code, this.perjobs_payno, pidm, this.perjobs_posn,
                            this.perjobs_suff, this.perjobs_orgn_code_ts, this.perjobs_coas_code_ts, this.perjobs_action_ind]

            List extractInput = [Sql.in(Sql.INTEGER.type, (jobSequenceNo ? jobSequenceNo.toInteger() : null)),
                                 Sql.in(Sql.VARCHAR.type, this.user_role),
                                 Sql.in(Sql.INTEGER.type, (userPidm ? userPidm.toInteger() : null)),
                                 Sql.in(Sql.INTEGER.type, (proxyPidm ? proxyPidm.toInteger() : null)),
                                 Sql.in(Sql.VARCHAR.type, this.source),
                                 Sql.in(Sql.VARCHAR.type, 'N'),
                                 Sql.out(Sql.VARCHAR.type),
                                 Sql.out(Sql.VARCHAR.type),
                                 Sql.out(Sql.NUMERIC.type)]

            try {
                this.conn.call("{ call gb_common.p_set_context(?,?,?,?) }", ["TIMEENTRY", "XE_CALL_IND", 'Y', 'N'])
                this.conn.call("{ call pekteap.p_submit_time(?,?,?,?,?,?,?,?,?) }", extractInput) { msgType, msgText, errorNum ->
                    if (errorNum != null) {
                        queueErrorNumber = errorNum
                        queueErrorMessage = "     Routing queue error: ${msgType} ${msgText}  errorNum: ${errorNum}  jobSequenceNumber: ${jobSequenceNo.toString()}"
                    } else if (msgType == 'ERROR') {
                        connectInfo.tableUpdate("PHRERRL", 0, 1, 0, 0, 0)
                        if (connectInfo.showErrors) {
                            println "Submitted time response: $msgText  jobSequenceNumber: ${jobSequenceNo.toString()}  data: " + timeData
                        }
                    } else {
                        connectInfo.tableUpdate("PERJOBS", 0, 0, 1, 0, 0)
                        println "Submitted time for job seqno: ${jobSequenceNo.toString()}  data: " + timeData
                    }
                }
            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem submitting time from ${this.class.simpleName}: $e.message"
                }
            } finally {
                this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'N', 'N'])
            }

            if (queueErrorNumber) {
                // Routing queue was not built.  Attempt a submit with a specified approver.
                def approverPidm = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

                try {
                    this.conn.eachRow(approverPidm, ["HOPTE0600"]) { trow ->
                        apidm = trow.spriden_pidm
                    }
                } catch (Exception e) {
                    if (connectInfo.showErrors) {
                        println "Could not select Pidm in ${this.class.simpleName} for Banner ID HOPTE0600 from SPRIDEN. $e.message"
                    }
                }

                extractInput = [Sql.in(Sql.INTEGER.type, (jobSequenceNo ? jobSequenceNo.toInteger() : null)),
                                Sql.in(Sql.VARCHAR.type, this.user_role),
                                Sql.in(Sql.INTEGER.type, (userPidm ? userPidm.toInteger() : null)),
                                Sql.in(Sql.INTEGER.type, (proxyPidm ? proxyPidm.toInteger() : null)),
                                Sql.in(Sql.VARCHAR.type, this.source),
                                Sql.in(Sql.VARCHAR.type, 'Y'),
                                Sql.out(Sql.VARCHAR.type),
                                Sql.out(Sql.VARCHAR.type),
                                Sql.out(Sql.NUMERIC.type)]

                List approverInput = [Sql.in(Sql.INTEGER.type, apidm),
                                      Sql.in(Sql.INTEGER.type, 1)]

                try {
                    this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'Y', 'N'])
                    this.conn.call("{call pektecm.p_add_queue_member_context(?,?)}", approverInput)
                    this.conn.call("{ call pekteap.p_submit_time(?,?,?,?,?,?,?,?,?) }", extractInput) { msgType2, msgText2, errorNum2 ->
                        if (msgType2 == 'ERROR') {
                            connectInfo.tableUpdate("PHRERRL", 0, 1, 0, 0, 0)
                            if (connectInfo.showErrors) {
                                println "Submitted time response: $msgText2  jobSequenceNumber: ${jobSequenceNo.toString()}  data: " + timeData
                                println queueErrorMessage
                            }
                        } else {
                            connectInfo.tableUpdate("PERJOBS", 0, 0, 1, 0, 0)
                            println "Submitted time for job seqno: ${jobSequenceNo.toString()}  data: " + timeData
                        }
                    }
                } catch (Exception e) {
                    if (connectInfo.showErrors) {
                        println "Problem submitting time from ${this.class.simpleName}: $e.message"
                    }
                } finally {
                    this.conn.call("{call pektecm.p_delete_queue_member_context()}", [])
                    this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'N', 'N'])
                }
            }
        }
    }
}
