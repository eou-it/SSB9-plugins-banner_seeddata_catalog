/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

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
        def apidm = null
        def userPidm = null
        def proxyPidm = null
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

        if (!setupFailure) {
            def timeData = [this.bannerid, this.perjobs_year, this.perjobs_pict_code, this.perjobs_payno, pidm, this.perjobs_posn,
                            this.perjobs_suff, this.perjobs_orgn_code_ts, this.perjobs_coas_code_ts, this.perjobs_action_ind]

            List extractInput = [Sql.in(Sql.VARCHAR.type, this.perjobs_year),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_pict_code),
                                 Sql.in(Sql.INTEGER.type, (this.perjobs_payno ? this.perjobs_payno.toInteger() : null)),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_action_ind),
                                 Sql.in(Sql.INTEGER.type, (pidm ? pidm.toInteger() : null)),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_posn),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_suff),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_coas_code_ts),
                                 Sql.in(Sql.VARCHAR.type, this.perjobs_orgn_code_ts),
                                 Sql.in(Sql.VARCHAR.type, this.user_role),
                                 Sql.in(Sql.INTEGER.type, (userPidm ? userPidm.toInteger() : null)),
                                 Sql.in(Sql.INTEGER.type, (proxyPidm ? proxyPidm.toInteger() : null)),
                                 Sql.in(Sql.VARCHAR.type, this.source),
                                 Sql.in(Sql.VARCHAR.type, 'N'),
                                 Sql.out(Sql.INTEGER.type),
                                 Sql.out(Sql.VARCHAR.type),
                                 Sql.out(Sql.VARCHAR.type),
                                 Sql.out(Sql.NUMERIC.type)]

            try {
                this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'Y', 'N'])
                this.conn.call("{call pekteex.p_extract_time(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", extractInput) { seqno, msgType, msgText, errorNum ->
                    if (seqno != null) {
                        connectInfo.tableUpdate("PERJOBS", 0, 1, 0, 0, 0)
                        println "Extracted time for job seqno: ${seqno.toString()}  data: " + timeData
                    } else if (errorNum != null) {
                        queueErrorNumber = errorNum
                        queueErrorMessage = "     Routing queue error: ${msgType} ${msgText}  errorNum: ${errorNum}  data: " + timeData
                    } else {
                        connectInfo.tableUpdate("PHRERRL", 0, 1, 0, 0, 0)
                        if (connectInfo.showErrors) {
                            println "Problem extracting time in EmployeeTimeEntryExtractDML.groovy: $msgText  data: " + timeData
                        }
                    }
                }
            } catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem extracting time in EmployeeTimeEntryExtractDML.groovy: $e.message"
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
                        println "Could not select Pidm in EmployeeTimeEntryExtractDML for Banner ID HOPTE0600 from SPRIDEN. $e.message"
                    }
                }

                extractInput = [Sql.in(Sql.VARCHAR.type, this.perjobs_year),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_pict_code),
                                Sql.in(Sql.INTEGER.type, (this.perjobs_payno ? this.perjobs_payno.toInteger() : null)),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_action_ind),
                                Sql.in(Sql.INTEGER.type, (pidm ? pidm.toInteger() : null)),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_posn),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_suff),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_coas_code_ts),
                                Sql.in(Sql.VARCHAR.type, this.perjobs_orgn_code_ts),
                                Sql.in(Sql.VARCHAR.type, this.user_role),
                                Sql.in(Sql.INTEGER.type, (userPidm ? userPidm.toInteger() : null)),
                                Sql.in(Sql.INTEGER.type, (proxyPidm ? proxyPidm.toInteger() : null)),
                                Sql.in(Sql.VARCHAR.type, this.source),
                                Sql.in(Sql.VARCHAR.type, 'Y'),
                                Sql.out(Sql.INTEGER.type),
                                Sql.out(Sql.VARCHAR.type),
                                Sql.out(Sql.VARCHAR.type),
                                Sql.out(Sql.NUMERIC.type)]

                List approverInput = [Sql.in(Sql.INTEGER.type, apidm),
                                      Sql.in(Sql.INTEGER.type, 1)]

                try {
                    this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'Y', 'N'])
                    this.conn.call("{call pektecm.p_add_queue_member_context(?,?)}", approverInput)
                    this.conn.call("{call pekteex.p_extract_time(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", extractInput) { seqno2, msgType2, msgText2, errorNum2 ->
                        if (seqno2 != null) {
                            connectInfo.tableUpdate("PERJOBS", 0, 1, 0, 0, 0)
                            println "Extracted time for job seqno: ${seqno2.toString()}  data: " + timeData
                        } else {
                            connectInfo.tableUpdate("PHRERRL", 0, 1, 0, 0, 0)
                            if (connectInfo.showErrors) {
                                println "Problem extracting time in EmployeeTimeEntryExtractDML.groovy: $msgText2  data: " + timeData
                                println queueErrorMessage
                            }
                        }
                    }
                } catch (Exception e) {
                    if (connectInfo.showErrors) {
                        println "Problem submitting time in EmployeeTimeEntryExtractDML.groovy: $e.message"
                    }
                } finally {
                    this.conn.call("{call pektecm.p_delete_queue_member_context()}", [])
                    this.conn.call("{call gb_common.p_set_context(?,?,?,?)}", ["TIMEENTRY", "XE_CALL_IND", 'N', 'N'])
                }
            }
        }
    }
}
