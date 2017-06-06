/*********************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Employee Person ID DML for Leave by Employee
 */
public class EmployeePersonIDDML {
    def bannerid
    def pebempl_pidm
    def pebempl_empl_status
    def pebempl_coas_code_home
    def pebempl_orgn_code_home
    def pebempl_coas_code_dist
    def pebempl_orgn_code_dist
    def pebempl_ecls_code
    def pebempl_lcat_code
    def pebempl_bcat_code
    def pebempl_first_hire_date
    def pebempl_current_hire_date
    def pebempl_adj_service_date
    def pebempl_seniority_date
    def pebempl_lrea_code
    def pebempl_loa_beg_date
    def pebempl_loa_end_date
    def pebempl_trea_code
    def pebempl_term_date
    def pebempl_i9_form_ind
    def pebempl_i9_date
    def pebempl_i9_expire_date
    def pebempl_activity_date
    def pebempl_wkpr_code
    def pebempl_flsa_ind
    def pebempl_stgr_code
    def pebempl_days_in_canada
    def pebempl_1042_recipient_cd
    def pebempl_internal_ft_pt_ind
    def pebempl_dicd_code
    def pebempl_egrp_code
    def pebempl_ipeds_soft_money_ind
    def pebempl_first_work_date
    def pebempl_last_work_date
    def pebempl_calif_pension_ind
    def pebempl_nrsi_code
    def pebempl_ssn_last_name
    def pebempl_ssn_first_name
    def pebempl_ssn_mi
    def pebempl_ssn_suffix
    def pebempl_jbln_code
    def pebempl_coll_code
    def pebempl_camp_code
    def pebempl_user_id
    def pebempl_data_origin
    def pebempl_ew2_consent_ind
    def pebempl_ew2_consent_date
    def pebempl_ew2_consent_user_id
    def pebempl_ipeds_primary_function
    def pebempl_ipeds_med_dental_ind
    def pebempl_etax_consent_ind
    def pebempl_etax_consent_date
    def pebempl_etax_consent_user_id
    def pebempl_new_hire_ind
    def pebempl_1095tx_consent_ind
    def pebempl_1095tx_consent_date
    def pebempl_1095tx_consent_user_id
    def pebempl_surrogate_id
    def pebempl_version
    def pebempl_vpdi_code

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData


    public EmployeePersonIDDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmployeePersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployee()
    }


    def parseXmlData() {
        def pebempl = new XmlParser().parseText(xmlData)
        this.bannerid = pebempl.BANNERID
        this.pebempl_empl_status = pebempl.PEBEMPL_EMPL_STATUS.text()
        this.pebempl_coas_code_home = pebempl.PEBEMPL_COAS_CODE_HOME.text()
        this.pebempl_orgn_code_home = pebempl.PEBEMPL_ORGN_CODE_HOME.text()
        this.pebempl_coas_code_dist = pebempl.PEBEMPL_COAS_CODE_DIST.text()
        this.pebempl_orgn_code_dist = pebempl.PEBEMPL_ORGN_CODE_DIST.text()
        this.pebempl_ecls_code = pebempl.PEBEMPL_ECLS_CODE.text()
        this.pebempl_lcat_code = pebempl.PEBEMPL_LCAT_CODE.text()
        this.pebempl_bcat_code = pebempl.PEBEMPL_BCAT_CODE.text()
        this.pebempl_first_hire_date = pebempl.PEBEMPL_FIRST_HIRE_DATE.text()
        this.pebempl_current_hire_date = pebempl.PEBEMPL_CURRENT_HIRE_DATE.text()
        this.pebempl_adj_service_date = pebempl.PEBEMPL_ADJ_SERVICE_DATE.text()
        this.pebempl_seniority_date = pebempl.PEBEMPL_SENIORITY_DATE.text()
        this.pebempl_lrea_code = pebempl.PEBEMPL_LREA_CODE.text()
        this.pebempl_loa_beg_date = pebempl.PEBEMPL_LOA_BEG_DATE.text()
        this.pebempl_loa_end_date = pebempl.PEBEMPL_LOA_END_DATE.text()
        this.pebempl_trea_code = pebempl.PEBEMPL_TREA_CODE.text()
        this.pebempl_term_date = pebempl.PEBEMPL_TERM_DATE.text()
        this.pebempl_i9_form_ind = pebempl.PEBEMPL_I9_FORM_IND.text()
        this.pebempl_i9_date = pebempl.PEBEMPL_I9_DATE.text()
        this.pebempl_i9_expire_date = pebempl.PEBEMPL_I9_EXPIRE_DATE.text()
        this.pebempl_activity_date = pebempl.PEBEMPL_ACTIVITY_DATE.text()
        this.pebempl_wkpr_code = pebempl.PEBEMPL_WKPR_CODE.text()
        this.pebempl_flsa_ind = pebempl.PEBEMPL_FLSA_IND.text()
        this.pebempl_stgr_code = pebempl.PEBEMPL_STGR_CODE.text()
        this.pebempl_days_in_canada = pebempl.PEBEMPL_DAYS_IN_CANADA.text()
        this.pebempl_1042_recipient_cd = pebempl.PEBEMPL_1042_RECIPIENT_CD.text()
        this.pebempl_internal_ft_pt_ind = pebempl.PEBEMPL_INTERNAL_FT_PT_IND.text()
        this.pebempl_dicd_code = pebempl.PEBEMPL_DICD_CODE.text()
        this.pebempl_egrp_code = pebempl.PEBEMPL_EGRP_CODE.text()
        this.pebempl_ipeds_soft_money_ind = pebempl.PEBEMPL_IPEDS_SOFT_MONEY_IND.text()
        this.pebempl_first_work_date = pebempl.PEBEMPL_FIRST_WORK_DATE.text()
        this.pebempl_last_work_date = pebempl.PEBEMPL_LAST_WORK_DATE.text()
        this.pebempl_calif_pension_ind = pebempl.PEBEMPL_CALIF_PENSION_IND.text()
        this.pebempl_nrsi_code = pebempl.PEBEMPL_NRSI_CODE.text()
        this.pebempl_ssn_last_name = pebempl.PEBEMPL_SSN_LAST_NAME.text()
        this.pebempl_ssn_first_name = pebempl.PEBEMPL_SSN_FIRST_NAME.text()
        this.pebempl_ssn_mi = pebempl.PEBEMPL_SSN_MI.text()
        this.pebempl_ssn_suffix = pebempl.PEBEMPL_SSN_SUFFIX.text()
        this.pebempl_jbln_code = pebempl.PEBEMPL_JBLN_CODE.text()
        this.pebempl_coll_code = pebempl.PEBEMPL_COLL_CODE.text()
        this.pebempl_camp_code = pebempl.PEBEMPL_CAMP_CODE.text()
        this.pebempl_user_id = pebempl.PEBEMPL_USER_ID.text()
        this.pebempl_data_origin = pebempl.PEBEMPL_DATA_ORIGIN.text()
        this.pebempl_ew2_consent_ind = pebempl.PEBEMPL_EW2_CONSENT_IND.text()
        this.pebempl_ew2_consent_date = pebempl.PEBEMPL_EW2_CONSENT_DATE.text()
        this.pebempl_ew2_consent_user_id = pebempl.PEBEMPL_EW2_CONSENT_USER_ID.text()
        this.pebempl_ipeds_primary_function = pebempl.PEBEMPL_IPEDS_PRIMARY_FUNCTION.text()
        this.pebempl_ipeds_med_dental_ind = pebempl.PEBEMPL_IPEDS_MED_DENTAL_IND.text()
        this.pebempl_etax_consent_ind = pebempl.PEBEMPL_ETAX_CONSENT_IND.text()
        this.pebempl_etax_consent_date = pebempl.PEBEMPL_ETAX_CONSENT_DATE.text()
        this.pebempl_etax_consent_user_id = pebempl.PEBEMPL_ETAX_CONSENT_USER_ID.text()
        this.pebempl_new_hire_ind = pebempl.PEBEMPL_NEW_HIRE_IND.text()
        this.pebempl_1095tx_consent_ind = pebempl.PEBEMPL_1095TX_CONSENT_IND.text()
        this.pebempl_1095tx_consent_date = pebempl.PEBEMPL_1095TX_CONSENT_DATE.text()
        this.pebempl_1095tx_consent_user_id = pebempl.PEBEMPL_1095TX_CONSENT_USER_ID.text()
        this.pebempl_surrogate_id = pebempl.PEBEMPL_SURROGATE_ID.text()
        this.pebempl_version = pebempl.PEBEMPL_VERSION.text()
        this.pebempl_vpdi_code = pebempl.PEBEMPL_VPDI_CODE.text()
    }


    def processEmployee() {
        def pidm
        String pidmsql = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""

        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                pidm = trow.spriden_pidm
                connectInfo.savePidm = pidm
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeePersonIDDML, ${this.bannerid.text()} from SPRIDEN. $e.message"
            }
        }

        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (pidm) {
            try {
                deleteData()

                String API = "{call pb_employee.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                insertCall.setInt(1, pidm.toInteger())
                insertCall.setString(2, this.pebempl_empl_status)
                insertCall.setString(3, this.pebempl_coas_code_home)
                insertCall.setString(4, this.pebempl_orgn_code_home)
                insertCall.setString(5, this.pebempl_coas_code_dist)
                insertCall.setString(6, this.pebempl_orgn_code_dist)
                insertCall.setString(7, this.pebempl_ecls_code)
                insertCall.setString(8, this.pebempl_lcat_code)
                insertCall.setString(9, this.pebempl_bcat_code)

                if ((this.pebempl_first_hire_date == "") || (this.pebempl_first_hire_date == null) ||
                        (!this.pebempl_first_hire_date)) {
                    insertCall.setNull(10, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_first_hire_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(10, sqlDate)
                }

                if ((this.pebempl_current_hire_date == "") || (this.pebempl_current_hire_date == null) ||
                        (!this.pebempl_current_hire_date)) {
                    insertCall.setNull(11, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_current_hire_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(11, sqlDate)
                }

                if ((this.pebempl_adj_service_date == "") || (this.pebempl_adj_service_date == null) ||
                        (!this.pebempl_adj_service_date)) {
                    insertCall.setNull(12, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_adj_service_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(12, sqlDate)
                }

                if ((this.pebempl_seniority_date == "") || (this.pebempl_seniority_date == null) ||
                        (!this.pebempl_seniority_date)) {
                    insertCall.setNull(13, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_seniority_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(13, sqlDate)
                }

                insertCall.setString(14, this.pebempl_lrea_code)

                if ((this.pebempl_loa_beg_date == "") || (this.pebempl_loa_beg_date == null) ||
                        (!this.pebempl_loa_beg_date)) {
                    insertCall.setNull(15, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_loa_beg_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(15, sqlDate)
                }

                if ((this.pebempl_loa_end_date == "") || (this.pebempl_loa_end_date == null) ||
                        (!this.pebempl_loa_end_date)) {
                    insertCall.setNull(16, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_loa_end_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(16, sqlDate)
                }

                insertCall.setString(17, this.pebempl_trea_code)

                if ((this.pebempl_term_date == "") || (this.pebempl_term_date == null) ||
                        (!this.pebempl_term_date)) {
                    insertCall.setNull(18, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_term_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(18, sqlDate)
                }
                insertCall.setString(19, this.pebempl_i9_form_ind)


                if ((this.pebempl_i9_date == "") || (this.pebempl_i9_date == null) ||
                        (!this.pebempl_i9_date)) {
                    insertCall.setNull(20, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_i9_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(20, sqlDate)
                }

                if ((this.pebempl_i9_expire_date == "") || (this.pebempl_i9_expire_date == null) ||
                        (!this.pebempl_i9_expire_date)) {
                    insertCall.setNull(21, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_i9_expire_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(21, sqlDate)
                }

                insertCall.setString(22, this.pebempl_wkpr_code)
                insertCall.setString(23, this.pebempl_flsa_ind)
                insertCall.setString(24, this.pebempl_stgr_code)

                if ((this.pebempl_days_in_canada == "") || (this.pebempl_days_in_canada == null) || (!this.pebempl_days_in_canada)) {
                    insertCall.setNull(25, java.sql.Types.INTEGER)
                } else
                    insertCall.setInt(25, this.pebempl_days_in_canada.toInteger())
                insertCall.setString(26, this.pebempl_1042_recipient_cd)
                insertCall.setString(27, this.pebempl_internal_ft_pt_ind)
                insertCall.setString(28, this.pebempl_dicd_code)
                insertCall.setString(29, this.pebempl_egrp_code)
                insertCall.setString(30, this.pebempl_ipeds_soft_money_ind)

                if ((this.pebempl_first_work_date == "") || (this.pebempl_first_work_date == null) ||
                        (!this.pebempl_first_work_date)) {
                    insertCall.setNull(31, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_first_work_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(31, sqlDate)
                }

                if ((this.pebempl_last_work_date == "") || (this.pebempl_last_work_date == null) ||
                        (!this.pebempl_last_work_date)) {
                    insertCall.setNull(32, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_last_work_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(32, sqlDate)
                }

                insertCall.setString(33, this.pebempl_calif_pension_ind)
                insertCall.setString(34, this.pebempl_nrsi_code)
                insertCall.setString(35, this.pebempl_ssn_last_name)
                insertCall.setString(36, this.pebempl_ssn_first_name)
                insertCall.setString(37, this.pebempl_ssn_mi)
                insertCall.setString(38, this.pebempl_ssn_suffix)
                insertCall.setString(39, this.pebempl_jbln_code)
                insertCall.setString(40, this.pebempl_coll_code)
                insertCall.setString(41, this.pebempl_camp_code)
                insertCall.setString(42, this.pebempl_user_id)
                insertCall.setString(43, this.pebempl_data_origin)
                insertCall.setString(44, this.pebempl_ew2_consent_ind)

                if ((this.pebempl_ew2_consent_date == "") || (this.pebempl_ew2_consent_date == null) ||
                        (!this.pebempl_ew2_consent_date)) {
                    insertCall.setNull(45, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_ew2_consent_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(45, sqlDate)
                }
                insertCall.setString(46, this.pebempl_ew2_consent_user_id)
                insertCall.registerOutParameter(47, java.sql.Types.ROWID)
                insertCall.setString(48, this.pebempl_ipeds_primary_function)
                insertCall.setString(49, this.pebempl_ipeds_med_dental_ind)
                insertCall.setString(50, this.pebempl_etax_consent_ind)

                if ((this.pebempl_etax_consent_date == "") || (this.pebempl_etax_consent_date == null) ||
                        (!this.pebempl_etax_consent_date)) {
                    insertCall.setNull(51, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_etax_consent_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(51, sqlDate)
                }

                insertCall.setString(52, this.pebempl_etax_consent_user_id)
                insertCall.setString(53, this.pebempl_new_hire_ind)

                if (!this.pebempl_1095tx_consent_ind) {
                    insertCall.setString(54, "N")
                } else {
                    insertCall.setString(54, this.pebempl_1095tx_consent_ind)
                }

                if ((this.pebempl_1095tx_consent_date == "") || (this.pebempl_1095tx_consent_date == null) ||
                        (!this.pebempl_1095tx_consent_date)) {
                    insertCall.setNull(55, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pebempl_1095tx_consent_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(55, sqlDate)
                }
                insertCall.setString(56, this.pebempl_1095tx_consent_user_id)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("PEBEMPL", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("PEBEMPL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert PEBEMPL ${this.bannerid}}"
                        println "Problem executing insert for table PEBEMPL from EMployeePersonIDDML.groovy: $e.message" + this.pebempl_ecls_code + this.pebempl_bcat_code + this.pebempl_lcat_code
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("PEBEMPL", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert PEBEMPL ${this.bannerid} }"
                    println "Problem executing insert for table PEBEMPL from EmployeePersonIDDML.groovy: $e.message"
                }
            }
        }
    }


    def deleteData() {
        // Pay History
        deleteData("PHRACCR", "delete from phraccr where phraccr_pidm = ?")
        deleteData("PHRJACR", "delete from phrjacr where phrjacr_pidm = ?")
        deleteData("PHRDOCM", "delete from phrdocm where phrdocm_pidm = ?")
        deleteData("PHREARN", "delete from phrearn where phrearn_pidm = ?")
        deleteData("PHRELBD", "delete from phrelbd where phrelbd_pidm = ?")
        deleteData("PHRERRL", "delete from phrerrl where phrerrl_pidm = ?")
        deleteData("PHRJOBS", "delete from phrjobs where phrjobs_pidm = ?")
        deleteData("PHRATND", "delete from phratnd where phratnd_pidm = ?")
        deleteData("PHRPENS", "delete from phrpens where phrpens_pidm = ?")
        deleteData("PHRREDS", "delete from phrreds where phrreds_pidm = ?")
        deleteData("PHRFLSA", "delete from phrflsa where phrflsa_pidm = ?")
        deleteData("PHRTERN", "delete from phrtern where phrtern_pidm = ?")
        deleteData("PHRTHRS", "delete from phrthrs where phrthrs_pidm = ?")
        deleteData("PHRTLBD", "delete from phrtlbd where phrtlbd_pidm = ?")
        deleteData("PHRUICW", "delete from phruicw where phruicw_pidm = ?")
        deleteData("PHRPENS", "delete from phrpens where phrpens_pidm = ?")
        deleteData("PHTRETO", "delete from phtreto where phtreto_pidm = ?")
        deleteData("PHRCMNT", "delete from phrcmnt where phrcmnt_pidm = ?")
        deleteData("PHRHIST", "delete from phrhist where phrhist_pidm = ?")
        deleteData("PHRERRL", "delete from phrerrl where phrerrl_pidm = ?")
        deleteData("PERDTOT", "delete from perdtot where perdtot_pidm = ?")
        deleteData("PERJTOT", "delete from perjtot where perjtot_pidm = ?")
        deleteData("PERETOT", "delete from peretot where peretot_pidm = ?")
        // Time Entry
        deleteData("PERTETH", "delete from perteth where perteth_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERROUH", "delete from perrouh where perrouh_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERELBH", "delete from perelbh where perelbh_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERTITH", "delete from pertith where pertith_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERHOUH", "delete from perhouh where perhouh_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PEREARH", "delete from perearh where perearh_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERDAYH", "delete from perdayh where perdayh_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERJOBH", "delete from perjobh where perjobh_seqno      in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERLVTK", "delete from perlvtk where perlvtk_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERROUT", "delete from perrout where perrout_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERELBD", "delete from perelbd where perelbd_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERTITO", "delete from pertito where pertito_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERHOUR", "delete from perhour where perhour_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PEREARN", "delete from perearn where perearn_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERDAYS", "delete from perdays where perdays_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_pidm = ?)")
        deleteData("PERJOBS", "delete from perjobs where perjobs_pidm = ?")
        // Cobra and Non-Cobra Beneficiary and Dependents
        deleteData("PCRDEDN", "delete from pcrdedn where pcrdedn_pidm = ?")
        deleteData("PCRBENE", "delete from pcrbene where pcrbene_pidm = ?")
        deleteData("PCBPERS", "delete from pcbpers where pcbpers_pidm = ?")
        deleteData("PCRCPAY", "delete from pcrcpay where pcrcpay_pidm = ?")
        deleteData("PCRBCOV", "delete from pcrbcov where pcrbcov_pidm = ?")
        deleteData("PDRBALC", "delete from pdrbalc where pdrbalc_pidm = ?")
        deleteData("PDRBAHS", "delete from pdrbahs where pdrbahs_pidm = ?")
        deleteData("PDRBCHS", "delete from pdrbchs where pdrbchs_pidm = ?")
        deleteData("PDRBCOV", "delete from pdrbcov where pdrbcov_pidm = ?")
        deleteData("PDRBENE", "delete from pdrbene where pdrbene_pidm = ?")
        deleteData("PDRBEHS", "delete from pdrbehs where pdrbehs_pidm = ?")
        // Benefits and Deductions
        deleteData("PDRFLEX", "delete from pdrflex where pdrflex_pidm = ?")
        deleteData("PDRBFLX", "delete from pdrbflx where pdrbflx_pidm = ?")
        deleteData("PERPCRE", "delete from perpcre where perpcre_pidm = ?")
        deleteData("PDRXPID", "delete from pdrxpid where pdrxpid_pidm = ?")
        deleteData("PDRPERS", "delete from pdrpers where pdrpers_pidm = ?")
        deleteData("PHRDEDN", "delete from phrdedn where phrdedn_pidm = ?")
        deleteData("PERDHIS", "delete from perdhis where perdhis_pidm = ?")
        deleteData("PDRDEDN", "delete from pdrdedn where pdrdedn_pidm = ?")
        deleteData("PDRBDED", "delete from pdrbded where pdrbded_pidm = ?")
        // Employee Jobs
        deleteData("NBRXDED", "delete from nbrxded where nbrxded_pidm = ?")
        deleteData("NBRJFTE", "delete from nbrjfte where nbrjfte_pidm = ?")
        deleteData("NBRWKSH", "delete from nbrwksh where nbrwksh_pidm = ?")
        deleteData("NBBWKSH", "delete from nbbwksh where nbbwksh_pidm = ?")
        deleteData("NBRJLBD", "delete from nbrjlbd where nbrjlbd_pidm = ?")
        deleteData("NBREARN", "delete from nbrearn where nbrearn_pidm = ?")
        deleteData("NBRJOBS", "delete from nbrjobs where nbrjobs_pidm = ?")
        deleteData("NBRBJLH", "delete from nbrjlhs where nbrjlhs_pidm = ?")
        deleteData("NBRJLHS", "delete from nbrjlhs where nbrjlhs_pidm = ?")
        deleteData("NBRBJOB", "delete from nbrbjob where nbrbjob_pidm = ?")
        // Employee essential data
        deleteData("PERREVW", "delete from perrevw where perrevw_pidm = ?")
        deleteData("PERRHOL", "delete from perrhol where perrhol_pidm = ?")
        deleteData("PERDIRD", "delete from perdird where perdird_pidm = ?")
        deleteData("PERROTH", "delete from perroth where perroth_pidm = ?")
        deleteData("PERRCMT", "delete from perrcmt where perrcmt_pidm = ?")
        deleteData("PERLEAV", "delete from perleav where perleav_pidm = ?")
        deleteData("PERLHIS", "delete from perlhis where perlhis_pidm = ?")
        deleteData("PEREHIS", "delete from perehis where perehis_pidm = ?")
        deleteData("PERJHIS", "delete from perjhis where perjhis_pidm = ?")
        deleteData("PERJLHS", "delete from perjlhs where perjlhs_pidm = ?")
        deleteData("PERJLEV", "delete from perjlev where perjlev_pidm = ?")
        deleteData("PERJOBH", "delete from perjobh where perjobh_pidm = ?")
        // Others
        deleteData("PERJBBG", "delete from perjbbg where perjbbg_pidm = ?")
        deleteData("PERBARG", "delete from perbarg where perbarg_pidm = ?")
        deleteData("PERRANK", "delete from perrank where perrank_pidm = ?")
        deleteData("PERROTH", "delete from perroth where perroth_pidm = ?")
        deleteData("PERSAHS", "delete from persahs where persahs_pidm = ?")
        deleteData("PERSABB", "delete from persabb where persabb_pidm = ?")
        deleteData("PERSNBL", "delete from persnbl where persnbl_pidm = ?")
        deleteData("PERSTAN", "delete from perstan where perstan_pidm = ?")
        deleteData("PERTWFA", "delete from pertwfa where pertwfa_pidm = ?")
        deleteData("PERGRPA", "delete from pergrpa where pergrpa_pidm = ?")
        deleteData("PEBPAYM", "delete from pebpaym where pebpaym_pidm = ?")
        deleteData("PERJOBP", "delete from perjobp where perjobp_pidm = ?")
        deleteData("PERINDV", "delete from perindv where perindv_pidm = ?")
        deleteData("PERSNBL", "delete from persnbl where persnbl_pidm = ?")
        deleteData("PERXJOB", "delete from perxjob where perxjob_pidm = ?")
        // Tax
        deleteData("PXRW2FD", "delete from pxrw2fd where pxrw2fd_pidm = ?")
        // Open Enrollment
        deleteData("PDRBERR", "delete from pdrberr where pdrberr_pidm = ?")
        deleteData("PDRTCOV", "delete from pdrtcov where pdrtcov_pidm = ?")
        deleteData("PDRTBAL", "delete from pdrtbal where pdrtbal_pidm = ?")
        deleteData("PDRDTOE", "delete from pdrdtoe where pdrdtoe_pidm = ?")
        deleteData("PDRBDOE", "delete from pdrbdoe where pdrbdoe_pidm = ?")
        // Applicant data
        deleteData("PATRECR", "delete from patrecr where patrecr_pidm = ?")
        deleteData("PARAPST", "delete from parapst where parapst_pidm = ?")
        deleteData("PARAPIN", "delete from parapin where parapin_pidm = ?")
        deleteData("PABREQU", "delete from pabrequ where pabrequ_appr_pidm = ?")
        deleteData("PABAPPL", "delete from pabappl where pabappl_pidm = ?")
        // Employee Effort Reports
        deleteData("PHRECRQ", "delete from phrecrq where phrecrq_member_pidm = ?")
        deleteData("PHRECRQ", "delete from phrecrq where phrecrq_user_pidm = ?")
        deleteData("PHRECRQ", "delete from phrecrq where phrecrq_phrecrt_id in ( select phrecrt_id from phrecrt where phrecrt_pidm = ? )")
        deleteData("PHRECDT", "delete from phrecdt where phrecdt_pidm = ?")
        deleteData("PHRECDT", "delete from phrecdt where  phrecdt_phrecrt_id in ( select phrecrt_id from phrecrt where phrecrt_pidm = ? )")
        deleteData("PHRECFD", "delete from phrecfd where  phrecfd_phrecrt_id in ( select phrecrt_id from phrecrt where phrecrt_pidm = ? )")
        deleteData("PHRECST", "delete from phrecst where phrecst_acting_employee_pidm = ?")
        deleteData("PHRECST", "delete from phrecst where phrecst_acting_employee_pidm = ?")
        deleteData("PHRECST", "delete from phrecst where phrecst_phrecrt_id in ( select phrecrt_id from phrecrt where phrecrt_pidm = ? )")
        deleteData("PHRECSI", """delete from phrecsi where phrecsi_phrecsn_id in (select phrecsn_id from phrecsn where
                                            phrecsn_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?))""")

        deleteData("PHRECSC", """delete from phrecsc where phrecsc_phrecsn_id in (select phrecsn_id from phrecsn where
                                            phrecsn_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?))""")

        deleteData("PHRECAL", """delete from phrecal where phrecal_phrecsn_id in (select phrecsn_id from phrecsn where
                                            phrecsn_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?))""")

        deleteData("PHRECSN", """delete from phrecsn where
                                            phrecsn_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?)""")
        deleteData("PHRECST", "delete from phrecst where phrecst_acting_employee_pidm = ?")
        deleteData("PHRECST", "delete from phrecst where phrecst_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?)")

        deleteData("PHRECRS", "delete from phrecrs where phrecrs_acting_employee_pidm = ?")
        deleteData("PHRECRS", "delete from phrecrs where phrecrs_phrecrt_id in (select phrecrt_id from phrecrt where phrecrt_pidm = ?)")
        deleteData("PHRECRT", "delete from phrecrt where phrecrt_pidm = ?")
        // Employee
        deleteData("PEBEMPL", "delete from pebempl where pebempl_pidm = ?")
    }


    def deleteData(String tableName, String sql) {
        try {
            int delRows = conn.executeUpdate(sql, [connectInfo.savePidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing ${tableName} delete for ${connectInfo.savePidm} from EmployeePersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
