/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * General Person ID DML for Students
 * Save the PIDM in the InputData.saveStudentPidm
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

    def PIDM

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



 /*   public EmployeePersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        def pebempl = new XmlParser().parseText(xmlData)
    }*/


    def processEmployee() {
        PIDM = null
        String pidmsql = """select * from spriden  where spriden_id = ?"""

        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeePersonIDDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }


        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (PIDM) {
            try {
                def cntEmployee = 0
                String employeeSql = """select * from pebempl  where pebempl_pidm = ?"""
                this.conn.eachRow(employeeSql, [PIDM]) { trow ->
                    cntEmployee++
                }
                if (cntEmployee) deleteData()

                String API = "{call pb_employee.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                insertCall.setInt(1, this.PIDM.toInteger())
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
             //   insertCall.setDate(10, this.pebempl_first_hire_date)

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

            //    insertCall.setDate(11, this.pebempl_current_hire_date)


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

            //    insertCall.setDate(12, this.pebempl_adj_service_date)

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

              //  insertCall.setDate(13, this.pebempl_seniority_date)



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

              //  insertCall.setString(15, this.pebempl_loa_beg_date)

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
               // insertCall.setString(16, this.pebempl_loa_end_date)

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
              //  insertCall.setDate(18, this.pebempl_term_date)
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
              //  insertCall.setDate(20, this.pebempl_i9_date)

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

              //  insertCall.setDate(21, this.pebempl_i9_expire_date)
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
              //  insertCall.setDate(31, this.pebempl_first_work_date)


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

              //  insertCall.setDate(32, this.pebempl_last_work_date)
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
            //    insertCall.setDate(44, this.pebempl_ew2_consent_date)
                insertCall.setString(46, this.pebempl_ew2_consent_user_id)
                // parm 13 p_rowid_out      spriden_rowid_out VARCHAR2
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
                insertCall.setString(54, this.pebempl_1095tx_consent_ind)


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
                        println "Problem executing insert for table PEBEMPL from EMployeePersonIDDML.groovy: $e.message"
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
        String selectSql = """select pebempl_pidm from pebempl where pebempl_pidm = ?"""
        try {
            conn.eachRow(selectSql, [connectInfo.savePidm]) {
                deleteData("PEREHIS","delete from perehis where perehis_pidm = ?")
                deleteData("PERREVW","delete from perrevw where perrevw_pidm = ?")
                deleteData("PERLEAV","""delete from perleav where EXISTS
                        (SELECT 'X'
                        FROM ptrinst
                        WHERE  ptrinst_accrue_leave_method = 'E'
                        AND  ptrinst_code = 'PAYROLL')
                        and perleav_pidm = ?""")
                deleteData("PERLHIS","""delete from perlhis where EXISTS
                        (SELECT 'X'
                        FROM ptrinst
                        WHERE  ptrinst_accrue_leave_method = 'E'
                        AND  ptrinst_code = 'PAYROLL')
                        and perlhis_pidm = ?""")
                deleteData("PDRBENE","""delete from pdrbene where pdrbene_pidm = ?""")
                deleteData("PEBEMPL","""delete from pebempl where pebempl_pidm = ?""")
            }
        }
        catch (e) {
            connectInfo.tableUpdate("PEBEMPL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete  for table PEBEMP from EmployeePersonIDDML.groovy: $e.message"
            }
        }


    }


    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [connectInfo.saveStudentPidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for person ${connectInfo.saveStudentPidm} from StudentPersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
