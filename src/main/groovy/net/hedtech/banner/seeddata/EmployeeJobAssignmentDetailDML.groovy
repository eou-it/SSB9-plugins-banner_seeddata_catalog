/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by apoliski on 6/13/2016.
 */
class EmployeeJobAssignmentDetailDML {
    def bannerid
    def supervisor_bannerid
    def nbrjobs_pidm
    def nbrjobs_posn
    def nbrjobs_suff
    def nbrjobs_effective_date
    def nbrjobs_status
    def nbrjobs_desc
    def nbrjobs_ecls_code
    def nbrjobs_pict_code
    def nbrjobs_coas_code_ts
    def nbrjobs_orgn_code_ts
    def nbrjobs_sal_table
    def nbrjobs_sal_grade
    def nbrjobs_sal_step
    def nbrjobs_appt_pct
    def nbrjobs_fte
    def nbrjobs_hrs_day
    def nbrjobs_hrs_pay
    def nbrjobs_shift
    def nbrjobs_reg_rate
    def nbrjobs_assgn_salary
    def nbrjobs_factor
    def nbrjobs_ann_salary
    def nbrjobs_per_pay_salary
    def nbrjobs_pays
    def nbrjobs_per_pay_defer_amt
    def nbrjobs_activity_date
    def nbrjobs_jcre_code
    def nbrjobs_sgrp_code
    def nbrjobs_empr_code
    def nbrjobs_lgcd_code
    def nbrjobs_locn_code
    def nbrjobs_schl_code
    def nbrjobs_supervisor_pidm
    def nbrjobs_supervisor_posn
    def nbrjobs_supervisor_suff
    def nbrjobs_wkcp_code
    def nbrjobs_jbln_code
    def nbrjobs_pers_chg_date
    def nbrjobs_pcat_code
    def nbrjobs_dfpr_code
    def nbrjobs_encumbrance_hrs
    def nbrjobs_contract_no
    def nbrjobs_strs_assn_code
    def nbrjobs_strs_pay_code
    def nbrjobs_pers_pay_code
    def nbrjobs_time_entry_method
    def nbrjobs_time_entry_type
    def nbrjobs_time_in_out_ind
    def nbrjobs_lcat_code
    def nbrjobs_leav_rept_method
    def nbrjobs_pict_code_leav_rept
    def nbrjobs_user_id
    def nbrjobs_data_origin
    def nbrjobs_surrogate_id
    def nbrjobs_version
    def nbrjobs_vpdi_code

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    def PIDM
    def SUPERVISOR_PIDM

    public EmployeeJobAssignmentDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public EmployeeJobAssignmentDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployeeJobAssignmentDetail()
    }
    def parseXmlData() {
        def nbrjobs = new XmlParser().parseText(xmlData)
        this.bannerid = nbrjobs.BANNERID
        this.supervisor_bannerid = nbrjobs.SUPERVISOR_BANNERID
        this.nbrjobs_posn = nbrjobs.NBRJOBS_POSN.text()
        this.nbrjobs_suff = nbrjobs.NBRJOBS_SUFF.text()
        this.nbrjobs_effective_date = nbrjobs.NBRJOBS_EFFECTIVE_DATE.text()
        this.nbrjobs_status = nbrjobs.NBRJOBS_STATUS.text()
        this.nbrjobs_desc = nbrjobs.NBRJOBS_DESC.text()
        this.nbrjobs_ecls_code = nbrjobs.NBRJOBS_ECLS_CODE.text()
        this.nbrjobs_pict_code = nbrjobs.NBRJOBS_PICT_CODE.text()
        this.nbrjobs_coas_code_ts = nbrjobs.NBRJOBS_COAS_CODE_TS.text()
        this.nbrjobs_orgn_code_ts = nbrjobs.NBRJOBS_ORGN_CODE_TS.text()
        this.nbrjobs_sal_table = nbrjobs.NBRJOBS_SAL_TABLE.text()
        this.nbrjobs_sal_grade = nbrjobs.NBRJOBS_SAL_GRADE.text()
        this.nbrjobs_sal_step = nbrjobs.NBRJOBS_SAL_STEP.text()
        this.nbrjobs_appt_pct = nbrjobs.NBRJOBS_APPT_PCT.text()
        this.nbrjobs_fte = nbrjobs.NBRJOBS_FTE.text()
        this.nbrjobs_hrs_day = nbrjobs.NBRJOBS_HRS_DAY .text()
        this.nbrjobs_hrs_pay = nbrjobs.NBRJOBS_HRS_PAY.text()
        this.nbrjobs_shift = nbrjobs.NBRJOBS_SHIFT.text()
        this.nbrjobs_reg_rate = nbrjobs.NBRJOBS_REG_RATE.text()
        this.nbrjobs_assgn_salary = nbrjobs.NBRJOBS_ASSGN_SALARY.text()
        this.nbrjobs_factor = nbrjobs.NBRJOBS_FACTOR.text()
        this.nbrjobs_ann_salary = nbrjobs.NBRJOBS_ANN_SALARY.text()
        this.nbrjobs_per_pay_salary = nbrjobs.NBRJOBS_PER_PAY_SALARY.text()
        this.nbrjobs_pays = nbrjobs.NBRJOBS_PAYS.text()
        this.nbrjobs_per_pay_defer_amt = nbrjobs.NBRJOBS_PER_PAY_DEFER_AMT.text()
        this.nbrjobs_activity_date = nbrjobs.NBRJOBS_ACTIVITY_DATE.text()
        this.nbrjobs_jcre_code = nbrjobs.NBRJOBS_JCRE_CODE.text()
        this.nbrjobs_sgrp_code = nbrjobs.NBRJOBS_SGRP_CODE.text()
        this.nbrjobs_empr_code = nbrjobs.NBRJOBS_EMPR_CODE.text()
        this.nbrjobs_lgcd_code = nbrjobs.NBRJOBS_LGCD_CODE.text()
        this.nbrjobs_locn_code = nbrjobs.NBRJOBS_LOCN_CODE.text()
        this.nbrjobs_schl_code = nbrjobs.NBRJOBS_SCHL_CODE.text()
        this.nbrjobs_supervisor_posn = nbrjobs.NBRJOBS_SUPERVISOR_POSN.text()
        this.nbrjobs_supervisor_suff = nbrjobs.NBRJOBS_SUPERVISOR_SUFF.text()
        this.nbrjobs_wkcp_code = nbrjobs.NBRJOBS_WKCP_CODE.text()
        this.nbrjobs_jbln_code = nbrjobs.NBRJOBS_JBLN_CODE.text()
        this.nbrjobs_pers_chg_date = nbrjobs.NBRJOBS_PERS_CHG_DATE.text()
        this.nbrjobs_pcat_code = nbrjobs.NBRJOBS_PCAT_CODE.text()
        this.nbrjobs_dfpr_code = nbrjobs.NBRJOBS_DFPR_CODE.text()
        this.nbrjobs_encumbrance_hrs = nbrjobs.NBRJOBS_ENCUMBRANCE_HRS.text()
        this.nbrjobs_contract_no = nbrjobs.NBRJOBS_CONTRACT_NO.text()
        this.nbrjobs_strs_assn_code = nbrjobs.NBRJOBS_STRS_ASSN_CODE.text()
        this.nbrjobs_strs_pay_code = nbrjobs.NBRJOBS_STRS_PAY_CODE.text()
        this.nbrjobs_pers_pay_code = nbrjobs.NBRJOBS_PERS_PAY_CODE.text()
        this.nbrjobs_time_entry_method = nbrjobs.NBRJOBS_TIME_ENTRY_METHOD.text()
        this.nbrjobs_time_entry_type = nbrjobs.NBRJOBS_TIME_ENTRY_TYPE.text()
        this.nbrjobs_time_in_out_ind = nbrjobs.NBRJOBS_TIME_IN_OUT_IND.text()
        this.nbrjobs_lcat_code = nbrjobs.NBRJOBS_LCAT_CODE.text()
        this.nbrjobs_leav_rept_method = nbrjobs.NBRJOBS_LEAV_REPT_METHOD.text()
        this.nbrjobs_pict_code_leav_rept = nbrjobs.NBRJOBS_PICT_CODE_LEAV_REPT.text()
        this.nbrjobs_user_id = nbrjobs.NBRJOBS_USER_ID.text()
        this.nbrjobs_data_origin = nbrjobs.NBRJOBS_DATA_ORIGIN.text()
        this.nbrjobs_surrogate_id = nbrjobs.NBRJOBS_SURROGATE_ID.text()
        this.nbrjobs_version = nbrjobs.NBRJOBS_VERSION.text()
        this.nbrjobs_vpdi_code = nbrjobs.NBRJOBS_VPDI_CODE.text()
    }
    def processEmployeeJobAssignmentDetail() {
        PIDM = null
        SUPERVISOR_PIDM = null
        String pidmsql = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeJobAssignmentDetailDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }
        if (this.supervisor_bannerid) {
            String supvpidmsql = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""
            try {
                this.conn.eachRow(supvpidmsql, [this.supervisor_bannerid.text()]) { t2row ->
                    SUPERVISOR_PIDM = t2row.spriden_pidm
                }
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select Supervisor ID in EmployeeJobAssignmentDetailDML,  ${this.supervisor_bannerid.text()}  from SPRIDEN. $e.message"
                }
            }
        }

        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (PIDM) {
            def updateLeaveMethodSQL = ""
            def findY = ""
            String findRow = """select 'Y' nbrjobs_find from nbrjobs where nbrjobs_pidm = ?
                                and nbrjobs_posn  = ?
                                and nbrjobs_suff =?
                                and trunc(nbrjobs_effective_date) = trunc(to_date(?,'mm/dd/yyyy'))"""
            try {
                conn.eachRow(findRow, [PIDM, this.nbrjobs_posn, this.nbrjobs_suff, this.nbrjobs_effective_date]) { row ->
                    findY = row.nbrjobs_find
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("NBRJOBS", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "${findRow}"
                    println "Problem with select for table NBRJOBS from EmployeeJobAssignmentDetailDML.groovy: $e.message"
                }
            }
            if (!findY) {
                try {
                    // Set PTRINST leave method by Job and re-initialize session globals.
                    if (this.nbrjobs_lcat_code) {
                        updateLeaveMethodSQL = "update ptrinst set ptrinst_accrue_leave_method  = 'J'"
                        try {
                            def cntUpdt = conn.executeUpdate(updateLeaveMethodSQL)
                            connectInfo.tableUpdate("PTRINST", 0, 0, 1, 0, cntUpdt)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("PTRINST", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Problem executing update PTRINST to Leave By Job in EmployeeJobAssignmentDetailDML.groovy: $e.message"
                            }
                        }
                        conn.execute "{ call nokglob.p_init_session_globals() }"
                    }


                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'OFF') }"
                    String API = "{call nb_job_detail.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                    CallableStatement insertCall = this.connectCall.prepareCall(API)
                    insertCall.setInt(1, this.PIDM.toInteger())
                    insertCall.setString(2, this.nbrjobs_posn)
                    insertCall.setString(3, this.nbrjobs_suff)

                    if ((this.nbrjobs_effective_date == "") || (this.nbrjobs_effective_date == null) ||
                            (!this.nbrjobs_effective_date)) {
                        insertCall.setNull(4, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrjobs_effective_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(4, sqlDate)
                    }

                    insertCall.setString(5, this.nbrjobs_status)
                    insertCall.setString(6, this.nbrjobs_desc)
                    insertCall.setString(7, this.nbrjobs_ecls_code)
                    insertCall.setString(8, this.nbrjobs_pict_code)
                    insertCall.setString(9, this.nbrjobs_coas_code_ts)
                    insertCall.setString(10, this.nbrjobs_orgn_code_ts)
                    insertCall.setString(11, this.nbrjobs_sal_table)
                    insertCall.setString(12, this.nbrjobs_sal_grade)

                    if ((this.nbrjobs_sal_step == "") || (this.nbrjobs_sal_step == null) ||
                            (!this.nbrjobs_sal_step)) {
                        insertCall.setNull(13, java.sql.Types.INTEGER)
                    } else {
                        insertCall.setInt(13, this.nbrjobs_sal_step.toInteger())
                    }

                    if ((this.nbrjobs_appt_pct == "") || (this.nbrjobs_appt_pct == null) ||
                            (!this.nbrjobs_appt_pct)) {
                        insertCall.setNull(14, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(14, this.nbrjobs_appt_pct.toDouble())
                    }
                    if ((this.nbrjobs_fte == "") || (this.nbrjobs_fte == null) ||
                            (!this.nbrjobs_fte)) {
                        insertCall.setNull(15, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(15, this.nbrjobs_fte.toDouble())
                    }
                    if ((this.nbrjobs_hrs_day == "") || (this.nbrjobs_hrs_day == null) ||
                            (!this.nbrjobs_hrs_day)) {
                        insertCall.setNull(16, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(16, this.nbrjobs_hrs_day.toDouble())
                    }
                    if ((this.nbrjobs_hrs_pay == "") || (this.nbrjobs_hrs_pay == null) ||
                            (!this.nbrjobs_hrs_pay)) {
                        insertCall.setNull(17, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(17, this.nbrjobs_hrs_pay.toDouble())
                    }

                    insertCall.setString(18, this.nbrjobs_shift)

                    if ((this.nbrjobs_reg_rate == "") || (this.nbrjobs_reg_rate == null) ||
                            (!this.nbrjobs_reg_rate)) {
                        insertCall.setNull(19, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(19, this.nbrjobs_reg_rate.toDouble())
                    }

                    if ((this.nbrjobs_assgn_salary == "") || (this.nbrjobs_assgn_salary == null) ||
                            (!this.nbrjobs_assgn_salary)) {
                        insertCall.setNull(20, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(20, this.nbrjobs_assgn_salary.toDouble())
                    }

                    if ((this.nbrjobs_factor == "") || (this.nbrjobs_factor == null) ||
                            (!this.nbrjobs_factor)) {
                        insertCall.setNull(21, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(21, this.nbrjobs_factor.toDouble())
                    }

                    if ((this.nbrjobs_ann_salary == "") || (this.nbrjobs_ann_salary == null) ||
                            (!this.nbrjobs_ann_salary)) {
                        insertCall.setNull(22, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(22, this.nbrjobs_ann_salary.toDouble())
                    }

                    if ((this.nbrjobs_pays == "") || (this.nbrjobs_pays == null) ||
                            (!this.nbrjobs_pays)) {
                        insertCall.setNull(23, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(23, this.nbrjobs_pays.toDouble())
                    }

                    insertCall.setString(24, this.nbrjobs_jcre_code)
                    insertCall.setString(25, this.nbrjobs_sgrp_code)
                    insertCall.setString(26, this.nbrjobs_empr_code)
                    insertCall.setString(27, this.nbrjobs_lgcd_code)
                    insertCall.setString(28, this.nbrjobs_locn_code)
                    insertCall.setString(29, this.nbrjobs_schl_code)

                    // Supervisor info
                    if ((this.SUPERVISOR_PIDM == "") || (this.SUPERVISOR_PIDM == null) ||
                            (!this.SUPERVISOR_PIDM)) {
                        insertCall.setNull(30, java.sql.Types.INTEGER)
                    } else {
                        insertCall.setInt(30, this.SUPERVISOR_PIDM.toInteger())
                    }
                    insertCall.setString(31, this.nbrjobs_supervisor_posn)
                    insertCall.setString(32, this.nbrjobs_supervisor_suff)
                    insertCall.setString(33, this.nbrjobs_wkcp_code)
                    insertCall.setString(34, this.nbrjobs_jbln_code)

                    if ((this.nbrjobs_pers_chg_date == "") || (this.nbrjobs_pers_chg_date == null) ||
                            (!this.nbrjobs_pers_chg_date)) {
                        insertCall.setNull(35, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrjobs_pers_chg_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(35, sqlDate)
                    }
                    insertCall.setString(36, this.nbrjobs_pcat_code)
                    insertCall.setString(37, this.nbrjobs_dfpr_code)

                    if ((this.nbrjobs_encumbrance_hrs == "") || (this.nbrjobs_encumbrance_hrs == null) ||
                            (!this.nbrjobs_encumbrance_hrs)) {
                        insertCall.setNull(38, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(38, this.nbrjobs_encumbrance_hrs.toDouble())
                    }
                    insertCall.setString(39, this.nbrjobs_contract_no)
                    insertCall.setString(40, this.nbrjobs_strs_assn_code)
                    insertCall.setString(41, this.nbrjobs_strs_pay_code)
                    insertCall.setString(42, this.nbrjobs_pers_pay_code)
                    insertCall.setString(43, this.nbrjobs_time_entry_method)
                    insertCall.setString(44, this.nbrjobs_time_entry_type)
                    insertCall.setString(45, this.nbrjobs_time_in_out_ind)
                    insertCall.setString(46, this.nbrjobs_lcat_code)
                    insertCall.setString(47, this.nbrjobs_leav_rept_method)
                    insertCall.setString(48, this.nbrjobs_pict_code_leav_rept)

                    insertCall.setString(49, this.nbrjobs_user_id)
                    insertCall.setString(50, this.nbrjobs_data_origin)
                    insertCall.registerOutParameter(51, java.sql.Types.ROWID)
                    insertCall.registerOutParameter(52, java.sql.Types.VARCHAR)

                    //
                    try {
                        insertCall.executeUpdate()
                        connectInfo.tableUpdate("NBRJOBS", 0, 1, 0, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("NBRJOBS", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Insert NBRJOBS ${this.bannerid}}"
                            println "Problem executing insert for table NBRJOBS from EmployeeJobAssignmentDetailDML.groovy: $e.message"
                        }
                    }
                    finally {
                        insertCall.close()
                    }
                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'ON') }"

                    // Set PTRINST leave method back to by Employee and re-initialize session globals.
                    if (this.nbrjobs_lcat_code) {
                        updateLeaveMethodSQL = "update ptrinst set ptrinst_accrue_leave_method  = 'E'"
                       try {
                            def cntUpdt = conn.executeUpdate(updateLeaveMethodSQL)
                            connectInfo.tableUpdate("PTRINST", 0, 0, 0, 0, cntUpdt)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("PTRINST", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Problem executing update PTRINST to Leave By Employee in EmployeeLeaveByJobPersonIDDML.groovy: $e.message"
                            }
                        }
                        conn.execute "{ call nokglob.p_init_session_globals() }"
                    }
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("NBRJOBS", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert  ${this.bannerid} ${this.nbrjobs_posn} ${this.nbrjobs_suff} ${this.nbrjobs_effective_date}"
                        println "Problem executing insert for table NBRJOBS from EmployeeJobAssignmentDetailDML.groovy: $e.message"
                    }
                }
            }
        }
    }
}
