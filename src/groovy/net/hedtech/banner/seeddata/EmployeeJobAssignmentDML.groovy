package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by apoliski on 6/9/2016.
 */
class EmployeeJobAssignmentDML {
    def bannerid
    def nbrbjob_pidm
    def nbrbjob_posn
    def nbrbjob_suff
    def nbrbjob_begin_date
    def nbrbjob_end_date
    def nbrbjob_defer_bal
    def nbrbjob_contract_type
    def nbrbjob_salary_encumbrance
    def nbrbjob_contract_begin_date
    def nbrbjob_contract_end_date
    def nbrbjob_total_contract_hrs
    def nbrbjob_total_encumbrance_hrs
    def nbrbjob_step_incr_mon
    def nbrbjob_step_incr_day
    def nbrbjob_coas_code
    def nbrbjob_activity_date
    def nbrbjob_accrue_leave_ind
    def nbrbjob_civil_service_ind
    def nbrbjob_encumbrance_change_ind
    def nbrbjob_fringe_encumbrance
    def nbrbjob_ipeds_rept_ind
    def nbrbjob_facl_statscan_rept_ind
    def nbrbjob_probation_begin_date
    def nbrbjob_probation_end_date
    def nbrbjob_probation_units
    def nbrbjob_eligible_date
    def nbrbjob_user_id
    def nbrbjob_data_origin
    def nbrbjob_future_salary_enc
    def nbrbjob_future_fringe_enc
    def nbrbjob_surrogate_id
    def nbrbjob_version
    def nbrbjob_vpdi_code

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    def PIDM
    def SUPERVISOR_PIDM

    public EmployeeJobAssignmentDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }
    public EmployeeJobAssignmentDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployeeJobAssignment()
    }
    def parseXmlData() {
        def nbrbjob = new XmlParser().parseText(xmlData)
        this.bannerid = nbrbjob.BANNERID
        this.nbrbjob_posn = nbrbjob.NBRBJOB_POSN.text()
        this.nbrbjob_suff = nbrbjob.NBRBJOB_SUFF.text()
        this.nbrbjob_begin_date = nbrbjob.NBRBJOB_BEGIN_DATE.text()
        this.nbrbjob_end_date = nbrbjob.NBRBJOB_END_DATE.text()
        this.nbrbjob_defer_bal = nbrbjob.NBRBJOB_DEFER_BAL.text()
        this.nbrbjob_contract_type = nbrbjob.NBRBJOB_CONTRACT_TYPE.text()
        this.nbrbjob_salary_encumbrance = nbrbjob.NBRBJOB_SALARY_ENCUMBRANCE.text()
        this.nbrbjob_contract_begin_date = nbrbjob.NBRBJOB_CONTRACT_BEGIN_DATE.text()
        this.nbrbjob_contract_end_date = nbrbjob.NBRBJOB_CONTRACT_END_DATE.text()
        this.nbrbjob_total_contract_hrs = nbrbjob.NBRBJOB_TOTAL_CONTRACT_HRS.text()
        this.nbrbjob_total_encumbrance_hrs = nbrbjob.NBRBJOB_TOTAL_ENCUMBRANCE_HRS.text()
        this.nbrbjob_step_incr_mon = nbrbjob.NBRBJOB_STEP_INCR_MON.text()
        this.nbrbjob_step_incr_day = nbrbjob.NBRBJOB_STEP_INCR_DAY.text()
        this.nbrbjob_coas_code = nbrbjob.NBRBJOB_COAS_CODE.text()
        this.nbrbjob_activity_date  = nbrbjob.NBRBJOB_ACTIVITY_DATE.text()
        this.nbrbjob_accrue_leave_ind  = nbrbjob.NBRBJOB_ACCRUE_LEAVE_IND.text()
        this.nbrbjob_civil_service_ind = nbrbjob.NBRBJOB_CIVIL_SERVICE_IND.text()
        this.nbrbjob_encumbrance_change_ind = nbrbjob.NBRBJOB_ENCUMBRANCE_CHANGE_IND.text()
        this.nbrbjob_fringe_encumbrance = nbrbjob.NBRBJOB_FRINGE_ENCUMBRANCE.text()
        this.nbrbjob_ipeds_rept_ind = nbrbjob.NBRBJOB_IPEDS_REPT_IND.text()
        this.nbrbjob_facl_statscan_rept_ind = nbrbjob.NBRBJOB_FACL_STATSCAN_REPT_IND.text()
        this.nbrbjob_probation_begin_date = nbrbjob.NBRBJOB_PROBATION_BEGIN_DATE.text()
        this.nbrbjob_probation_end_date = nbrbjob.NBRBJOB_PROBATION_END_DATE.text()
        this.nbrbjob_probation_units = nbrbjob.NBRBJOB_PROBATION_UNITS.text()
        this.nbrbjob_eligible_date = nbrbjob.NBRBJOB_ELIGIBLE_DATE.text()
        this.nbrbjob_user_id = nbrbjob.NBRBJOB_USER_ID.text()
        this.nbrbjob_data_origin = nbrbjob.NBRBJOB_DATA_ORIGIN.text()
        this.nbrbjob_future_salary_enc = nbrbjob.NBRBJOB_FUTURE_SALARY_ENC.text()
        this.nbrbjob_future_fringe_enc  = nbrbjob.NBRBJOB_FUTURE_FRINGE_ENC.text()
        this.nbrbjob_surrogate_id = nbrbjob.NBRBJOB_SURROGATE_ID.text()
        this.nbrbjob_version = nbrbjob.NBRBJOB_VERSION.text()
        this.nbrbjob_vpdi_code  = nbrbjob.NBRBJOB_VPDI_CODE.text()
    }
    def processEmployeeJobAssignment() {
        PIDM = null
        String apiMessageOut
        String pidmsql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeJobAssignmentDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }
        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (PIDM) {
            def findY = ""
            String findRow = """select 'Y' nbrbjob_find from nbrbjob where nbrbjob_pidm = ?
                                and nbrbjob_posn  = ?
                                and nbrbjob_suff =? """
            try {
                conn.eachRow(findRow, [PIDM, this.nbrbjob_posn, this.nbrbjob_suff]) { row ->
                    findY = row.nbrbjob_find
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("NBRBJOB", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "${findRow}"
                    println "Problem with select for table NBRBJOB from EmployeeJobAssignmentDML.groovy: $e.message"
                }
            }
            if (!findY) {
                try {
                    println "processing  ${this.bannerid} ${this.nbrbjob_posn} ${this.nbrbjob_suff}"
                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'OFF') }"
                    String API = "{call nb_job_base.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                    CallableStatement insertCall = this.connectCall.prepareCall(API)

                    insertCall.setInt(1, this.PIDM.toInteger())
                    insertCall.setString(2, this.nbrbjob_posn)
                    insertCall.setString(3, this.nbrbjob_suff)

                    if ((this.nbrbjob_begin_date == "") || (this.nbrbjob_begin_date == null) ||
                            (!this.nbrbjob_begin_date)) {
                        insertCall.setNull(4, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_begin_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(4, sqlDate)
                    }

                    insertCall.setString(5, this.nbrbjob_contract_type)

                    if ((this.nbrbjob_salary_encumbrance == "") || (this.nbrbjob_salary_encumbrance == null) ||
                            (!this.nbrbjob_salary_encumbrance)) {
                        insertCall.setNull(6, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(6, this.nbrbjob_salary_encumbrance.toDouble())
                    }

                    if ((this.nbrbjob_contract_begin_date == "") || (this.nbrbjob_contract_begin_date == null) ||
                            (!this.nbrbjob_contract_begin_date)) {
                        insertCall.setNull(7, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_contract_begin_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(7, sqlDate)
                    }
                    if ((this.nbrbjob_contract_end_date == "") || (this.nbrbjob_contract_end_date == null) ||
                            (!this.nbrbjob_contract_end_date)) {
                        insertCall.setNull(8, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_contract_end_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(8, sqlDate)
                    }

                    if ((this.nbrbjob_total_contract_hrs == "") || (this.nbrbjob_total_contract_hrs == null) ||
                            (!this.nbrbjob_total_contract_hrs)) {
                        insertCall.setNull(9, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(9, this.nbrbjob_total_contract_hrs.toDouble())
                    }

                    if ((this.nbrbjob_total_encumbrance_hrs == "") || (this.nbrbjob_total_encumbrance_hrs == null) ||
                            (!this.nbrbjob_total_encumbrance_hrs)) {
                        insertCall.setNull(10, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(10, this.nbrbjob_total_encumbrance_hrs.toDouble())
                    }

                    insertCall.setString(11, this.nbrbjob_step_incr_mon)
                    insertCall.setString(12, this.nbrbjob_step_incr_day)
                    insertCall.setString(13, this.nbrbjob_accrue_leave_ind)
                    insertCall.setString(14, this.nbrbjob_civil_service_ind)
                    insertCall.setString(15, this.nbrbjob_ipeds_rept_ind)
                    insertCall.setString(16, this.nbrbjob_facl_statscan_rept_ind)

                    if ((this.nbrbjob_probation_begin_date == "") || (this.nbrbjob_probation_begin_date == null) ||
                            (!this.nbrbjob_probation_begin_date)) {
                        insertCall.setNull(17, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_probation_begin_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(17, sqlDate)
                    }
                    if ((this.nbrbjob_probation_end_date == "") || (this.nbrbjob_probation_end_date == null) ||
                            (!this.nbrbjob_probation_end_date)) {
                        insertCall.setNull(18, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_probation_end_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(18, sqlDate)
                    }

                    if ((this.nbrbjob_probation_units == "") || (this.nbrbjob_probation_units == null) ||
                            (!this.nbrbjob_probation_units)) {
                        insertCall.setNull(19, java.sql.Types.INTEGER)
                    } else {
                        insertCall.setInt(19, this.nbrbjob_probation_units.toInteger())
                    }

                    if ((this.nbrbjob_eligible_date == "") || (this.nbrbjob_eligible_date == null) ||
                            (!this.nbrbjob_eligible_date)) {
                        insertCall.setNull(20, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrbjob_eligible_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(20, sqlDate)
                    }
                    insertCall.setString(21, this.nbrbjob_user_id)
                    insertCall.setString(22, this.nbrbjob_data_origin)
                    insertCall.registerOutParameter(23, java.sql.Types.ROWID)
                    insertCall.registerOutParameter(24, java.sql.Types.VARCHAR)
                    println ("done setting parm values")
                    try {
                        insertCall.executeUpdate()
                        connectInfo.tableUpdate("NBRBJOB", 0, 1, 0, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("NBRBJOB", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Insert NBRBJOB ${this.bannerid}}"
                            println "Problem executing insert for table NBRBJOB from EmployeeJobAssignmentIDDML.groovy: $e.message"
                        }
                    }
                    finally {
                        insertCall.close()
                    }
                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'ON') }"
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("NBRBJOB", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert  ${this.bannerid} ${this.nbrbjob_posn} ${this.nbrbjob_suff}"
                        println "Problem executing insert for table NBRBJOB from EmployeeJobAssignmentDML.groovy: $e.message"
                    }
                }
            }
        }
    }
}
