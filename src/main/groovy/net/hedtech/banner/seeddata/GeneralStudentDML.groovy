/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement
import java.text.*
/**
 *  DML for SGBSTDN
 */
public class GeneralStudentDML {
    def bannerid
    def sgbstdn_pidm
    def sgbstdn_term_code_eff
    def sgbstdn_stst_code
    def sgbstdn_levl_code
    def sgbstdn_styp_code
    def sgbstdn_exp_grad_date
    def sgbstdn_camp_code
    def sgbstdn_full_part_ind
    def sgbstdn_sess_code
    def sgbstdn_resd_code
    def sgbstdn_orsn_code
    def sgbstdn_prac_code
    def sgbstdn_advr_pidm
    def sgbstdn_grad_credit_appr_ind
    def sgbstdn_capl_code
    def sgbstdn_leav_code
    def sgbstdn_leav_from_date
    def sgbstdn_leav_to_date
    def sgbstdn_astd_code
    def sgbstdn_term_code_astd
    def sgbstdn_rate_code
     def sgbstdn_edlv_code
    def sgbstdn_incm_code
    def sgbstdn_admt_code
    def sgbstdn_emex_code
    def sgbstdn_aprn_code
    def sgbstdn_trcn_code
    def sgbstdn_gain_code
    def sgbstdn_voed_code
    def sgbstdn_blck_code
    def sgbstdn_term_code_grad
    def sgbstdn_acyr_code
     def sgbstdn_site_code
     def sgbstdn_egol_code
    def sgbstdn_degc_code_dual
    def sgbstdn_levl_code_dual
    def sgbstdn_dept_code_dual
    def sgbstdn_coll_code_dual
    def sgbstdn_majr_code_dual
    def sgbstdn_bskl_code
    def sgbstdn_prev_code
    def sgbstdn_term_code_prev
    def sgbstdn_cast_code
    def sgbstdn_term_code_cast
    def sgbstdn_scpc_code

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null
    def termCode

    // General student SGBSTDN API
    public GeneralStudentDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSgbstdn()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.sgbstdn_pidm =  connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()
        if (connectInfo.debugThis) {
            println "--------- New XML SGBSTDN record ----------"
            println "${bannerid}  ${this.bannerid} ${apiData.SGBSTDN_TERM_CODE_EFF.text()}  ${apiData.SGBSTDN_STST_CODE.text()}    ${apiData.SGBSTDN_LEVL_CODE.text()}    "
        }

        this.sgbstdn_term_code_eff = apiData.SGBSTDN_TERM_CODE_EFF.text()
        this.sgbstdn_stst_code = apiData.SGBSTDN_STST_CODE.text()
        this.sgbstdn_styp_code = apiData.SGBSTDN_STYP_CODE.text()
         this.sgbstdn_exp_grad_date = apiData.SGBSTDN_EXP_GRAD_DATE.text()
        this.sgbstdn_camp_code = apiData.SGBSTDN_CAMP_CODE.text()
        this.sgbstdn_full_part_ind = apiData.SGBSTDN_FULL_PART_IND.text()
        this.sgbstdn_sess_code = apiData.SGBSTDN_SESS_CODE.text()
        this.sgbstdn_resd_code = apiData.SGBSTDN_RESD_CODE.text()
        this.sgbstdn_orsn_code = apiData.SGBSTDN_ORSN_CODE.text()
        this.sgbstdn_prac_code = apiData.SGBSTDN_PRAC_CODE.text()
        this.sgbstdn_advr_pidm = apiData.SGBSTDN_ADVR_PIDM.text()
        this.sgbstdn_grad_credit_appr_ind = apiData.SGBSTDN_GRAD_CREDIT_APPR_IND.text()
        this.sgbstdn_capl_code = apiData.SGBSTDN_CAPL_CODE.text()
        this.sgbstdn_leav_code = apiData.SGBSTDN_LEAV_CODE.text()
        this.sgbstdn_leav_from_date = apiData.SGBSTDN_LEAV_FROM_DATE.text()
        this.sgbstdn_leav_to_date = apiData.SGBSTDN_LEAV_TO_DATE.text()
        this.sgbstdn_astd_code = apiData.SGBSTDN_ASTD_CODE.text()
        this.sgbstdn_term_code_astd = apiData.SGBSTDN_TERM_CODE_ASTD.text()
        this.sgbstdn_rate_code = apiData.SGBSTDN_RATE_CODE.text()
        this.sgbstdn_edlv_code = apiData.SGBSTDN_EDLV_CODE.text()
        this.sgbstdn_incm_code = apiData.SGBSTDN_INCM_CODE.text()
        this.sgbstdn_admt_code = apiData.SGBSTDN_ADMT_CODE.text()
        this.sgbstdn_emex_code = apiData.SGBSTDN_EMEX_CODE.text()
        this.sgbstdn_aprn_code = apiData.SGBSTDN_APRN_CODE.text()
        this.sgbstdn_trcn_code = apiData.SGBSTDN_TRCN_CODE.text()
        this.sgbstdn_gain_code = apiData.SGBSTDN_GAIN_CODE.text()
        this.sgbstdn_voed_code = apiData.SGBSTDN_VOED_CODE.text()
        this.sgbstdn_blck_code = apiData.SGBSTDN_BLCK_CODE.text()
        this.sgbstdn_term_code_grad = apiData.SGBSTDN_TERM_CODE_GRAD.text()
        this.sgbstdn_acyr_code = apiData.SGBSTDN_ACYR_CODE.text()
        this.sgbstdn_site_code = apiData.SGBSTDN_SITE_CODE.text()
         this.sgbstdn_egol_code = apiData.SGBSTDN_EGOL_CODE.text()
        this.sgbstdn_degc_code_dual = apiData.SGBSTDN_DEGC_CODE_DUAL.text()
        this.sgbstdn_levl_code_dual = apiData.SGBSTDN_LEVL_CODE_DUAL.text()
        this.sgbstdn_dept_code_dual = apiData.SGBSTDN_DEPT_CODE_DUAL.text()
        this.sgbstdn_coll_code_dual = apiData.SGBSTDN_COLL_CODE_DUAL.text()
        this.sgbstdn_majr_code_dual = apiData.SGBSTDN_MAJR_CODE_DUAL.text()
        this.sgbstdn_bskl_code = apiData.SGBSTDN_BSKL_CODE.text()
          this.sgbstdn_prev_code = apiData.SGBSTDN_PREV_CODE.text()
        this.sgbstdn_term_code_prev = apiData.SGBSTDN_TERM_CODE_PREV.text()
        this.sgbstdn_cast_code = apiData.SGBSTDN_CAST_CODE.text()
        this.sgbstdn_term_code_cast = apiData.SGBSTDN_TERM_CODE_CAST.text()
        this.sgbstdn_scpc_code = apiData.SGBSTDN_SCPC_CODE.text()

    }


    def processSgbstdn() {
        tableRow = null

        String rowSQL = """select rowid table_row  from SGBSTDN
           where sgbstdn_pidm = ?  and sgbstdn_term_code_eff = ? """
        try {
            conn.eachRow(rowSQL, [this.sgbstdn_pidm, this.sgbstdn_term_code_eff]) { row ->
                tableRow = row.table_row 
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SGBSTDN rowid GeneralStudentDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 45
            try {
                String API = "{call  sb_learner.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sgbstdn_pidm NUMBER
              
               if ((connectInfo.saveStudentPidm == "") || (connectInfo.saveStudentPidm == null) || (!connectInfo.saveStudentPidm))
                    { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, connectInfo.saveStudentPidm.toInteger())
                }

                // parm 2 p_term_code_eff  sgbstdn_term_code_eff VARCHAR2
                insertCall.setString(2, this.sgbstdn_term_code_eff)

                // parm 3 p_stst_code  sgbstdn_stst_code VARCHAR2
                insertCall.setString(3, this.sgbstdn_stst_code)

                // parm 4 p_styp_code  sgbstdn_styp_code VARCHAR2
                insertCall.setString(4, this.sgbstdn_styp_code)

                // parm 5 p_exp_grad_date  sgbstdn_exp_grad_date DATE
                if ((this.sgbstdn_exp_grad_date == "") || (this.sgbstdn_exp_grad_date == null) ||
                        (!this.sgbstdn_exp_grad_date)) {
                    insertCall.setNull(5, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_exp_grad_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(5, sqlDate)
                }

                // parm 6 p_full_part_ind  sgbstdn_full_part_ind VARCHAR2
                insertCall.setString(6, this.sgbstdn_full_part_ind)

                // parm 7 p_sess_code  sgbstdn_sess_code VARCHAR2
                insertCall.setString(7, this.sgbstdn_sess_code)

                // parm 8 p_resd_code  sgbstdn_resd_code VARCHAR2
                insertCall.setString(8, this.sgbstdn_resd_code)

                // parm 9 p_orsn_code  sgbstdn_orsn_code VARCHAR2
                insertCall.setString(9, this.sgbstdn_orsn_code)

                // parm 10 p_prac_code  sgbstdn_prac_code VARCHAR2
                insertCall.setString(10, this.sgbstdn_prac_code)

                // parm 11 p_advr_pidm  sgbstdn_advr_pidm NUMBER
                if ((this.sgbstdn_advr_pidm == "") || (this.sgbstdn_advr_pidm == null) || (!this.sgbstdn_advr_pidm)) { insertCall.setNull(11, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(11, this.sgbstdn_advr_pidm.toInteger())
                }

                // parm 12 p_grad_credit_appr_ind  sgbstdn_grad_credit_appr_ind VARCHAR2
                insertCall.setString(12, this.sgbstdn_grad_credit_appr_ind)

                // parm 13 p_capl_code  sgbstdn_capl_code VARCHAR2
                insertCall.setString(13, this.sgbstdn_capl_code)

                // parm 14 p_leav_code  sgbstdn_leav_code VARCHAR2
                insertCall.setString(14, this.sgbstdn_leav_code)

                // parm 15 p_leav_from_date  sgbstdn_leav_from_date DATE
                if ((this.sgbstdn_leav_from_date == "") || (this.sgbstdn_leav_from_date == null) ||
                        (!this.sgbstdn_leav_from_date)) {
                    insertCall.setNull(15, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_leav_from_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(15, sqlDate)
                }

                // parm 16 p_leav_to_date  sgbstdn_leav_to_date DATE
                if ((this.sgbstdn_leav_to_date == "") || (this.sgbstdn_leav_to_date == null) ||
                        (!this.sgbstdn_leav_to_date)) {
                    insertCall.setNull(16, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_leav_to_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(16, sqlDate)
                }

                // parm 17 p_astd_code  sgbstdn_astd_code VARCHAR2
                insertCall.setString(17, this.sgbstdn_astd_code)

                // parm 18 p_term_code_astd  sgbstdn_term_code_astd VARCHAR2
                insertCall.setString(18, this.sgbstdn_term_code_astd)

                // parm 19 p_rate_code  sgbstdn_rate_code VARCHAR2
                insertCall.setString(19, this.sgbstdn_rate_code)

                // parm 20 p_edlv_code  sgbstdn_edlv_code VARCHAR2
                insertCall.setString(20, this.sgbstdn_edlv_code)

                // parm 21 p_incm_code  sgbstdn_incm_code VARCHAR2
                insertCall.setString(21, this.sgbstdn_incm_code)

                // parm 22 p_emex_code  sgbstdn_emex_code VARCHAR2
                insertCall.setString(22, this.sgbstdn_emex_code)

                // parm 23 p_aprn_code  sgbstdn_aprn_code VARCHAR2
                insertCall.setString(23, this.sgbstdn_aprn_code)

                // parm 24 p_trcn_code  sgbstdn_trcn_code VARCHAR2
                insertCall.setString(24, this.sgbstdn_trcn_code)

                // parm 25 p_gain_code  sgbstdn_gain_code VARCHAR2
                insertCall.setString(25, this.sgbstdn_gain_code)

                // parm 26 p_voed_code  sgbstdn_voed_code VARCHAR2
                insertCall.setString(26, this.sgbstdn_voed_code)

                // parm 27 p_blck_code  sgbstdn_blck_code VARCHAR2
                insertCall.setString(27, this.sgbstdn_blck_code)

                // parm 28 p_term_code_grad  sgbstdn_term_code_grad VARCHAR2
                insertCall.setString(28, this.sgbstdn_term_code_grad)

                // parm 29 p_acyr_code  sgbstdn_acyr_code VARCHAR2
                insertCall.setString(29, this.sgbstdn_acyr_code)

                // parm 30 p_site_code  sgbstdn_site_code VARCHAR2
                insertCall.setString(30, this.sgbstdn_site_code)

                // parm 31 p_egol_code  sgbstdn_egol_code VARCHAR2
                insertCall.setString(31, this.sgbstdn_egol_code)

                // parm 32 p_degc_code_dual  sgbstdn_degc_code_dual VARCHAR2
                insertCall.setString(32, this.sgbstdn_degc_code_dual)

                // parm 33 p_levl_code_dual  sgbstdn_levl_code_dual VARCHAR2
                insertCall.setString(33, this.sgbstdn_levl_code_dual)

                // parm 34 p_dept_code_dual  sgbstdn_dept_code_dual VARCHAR2
                insertCall.setString(34, this.sgbstdn_dept_code_dual)

                // parm 35 p_coll_code_dual  sgbstdn_coll_code_dual VARCHAR2
                insertCall.setString(35, this.sgbstdn_coll_code_dual)

                // parm 36 p_majr_code_dual  sgbstdn_majr_code_dual VARCHAR2
                insertCall.setString(36, this.sgbstdn_majr_code_dual)

                // parm 37 p_bskl_code  sgbstdn_bskl_code VARCHAR2
                insertCall.setString(37, this.sgbstdn_bskl_code)

                // parm 38 p_prev_code  sgbstdn_prev_code VARCHAR2
                insertCall.setString(38, this.sgbstdn_prev_code)

                // parm 39 p_term_code_prev  sgbstdn_term_code_prev VARCHAR2
                insertCall.setString(39, this.sgbstdn_term_code_prev)

                // parm 40 p_cast_code  sgbstdn_cast_code VARCHAR2
                insertCall.setString(40, this.sgbstdn_cast_code)

                // parm 41 p_term_code_cast  sgbstdn_term_code_cast VARCHAR2
                insertCall.setString(41, this.sgbstdn_term_code_cast)

                // parm 42 p_user_id  sgbstdn_user_id VARCHAR2
                insertCall.setString(42, connectInfo.userID)
                // parm 43 p_data_origin  sgbstdn_data_origin VARCHAR2
                insertCall.setString(43, connectInfo.dataOrigin)
                // parm 44 p_scpc_code								  sgbstdn_scpc_code								 VARCHAR2
                insertCall.setString(44, this.sgbstdn_scpc_code)

                // parm 45 p_rowid_out  sgbstdn_rowid_out VARCHAR2
                insertCall.registerOutParameter(45, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SGBSTDN ${this.sgbstdn_pidm} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SGBSTDN", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SGBSTDN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SGBSTDN ${bannerid} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                        println "Problem executing insert for table SGBSTDN from GeneralStudentDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SGBSTDN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SGBSTDN ${bannerid} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                    println "Problem setting up insert for table SGBSTDN from GeneralStudentDML.groovy: $e.message"
                }
            }
        } else {  // update the data
            //  parm count is 45
            try {
                String API = "{call  sb_learner.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sgbstdn_pidm NUMBER
                if ((this.sgbstdn_pidm == "") || (this.sgbstdn_pidm == null) || (!this.sgbstdn_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.sgbstdn_pidm.toInteger())
                }

                // parm 2 p_term_code_eff  sgbstdn_term_code_eff VARCHAR2
                insertCall.setString(2, this.sgbstdn_term_code_eff)

                // parm 3 p_stst_code  sgbstdn_stst_code VARCHAR2
                insertCall.setString(3, this.sgbstdn_stst_code)

                // parm 4 p_styp_code  sgbstdn_styp_code VARCHAR2
                insertCall.setString(4, this.sgbstdn_styp_code)

                // parm 5 p_exp_grad_date  sgbstdn_exp_grad_date DATE
                if ((this.sgbstdn_exp_grad_date == "") || (this.sgbstdn_exp_grad_date == null) || (!this.sgbstdn_exp_grad_date)) { insertCall.setNull(5, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_exp_grad_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(5, sqlDate)
                }

                // parm 6 p_full_part_ind  sgbstdn_full_part_ind VARCHAR2
                insertCall.setString(6, this.sgbstdn_full_part_ind)

                // parm 7 p_sess_code  sgbstdn_sess_code VARCHAR2
                insertCall.setString(7, this.sgbstdn_sess_code)

                // parm 8 p_resd_code  sgbstdn_resd_code VARCHAR2
                insertCall.setString(8, this.sgbstdn_resd_code)

                // parm 9 p_orsn_code  sgbstdn_orsn_code VARCHAR2
                insertCall.setString(9, this.sgbstdn_orsn_code)

                // parm 10 p_prac_code  sgbstdn_prac_code VARCHAR2
                insertCall.setString(10, this.sgbstdn_prac_code)

                // parm 11 p_advr_pidm  sgbstdn_advr_pidm NUMBER
                if ((this.sgbstdn_advr_pidm == "") || (this.sgbstdn_advr_pidm == null) || (!this.sgbstdn_advr_pidm)) { insertCall.setNull(11, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(11, this.sgbstdn_advr_pidm.toInteger())
                }

                // parm 12 p_grad_credit_appr_ind  sgbstdn_grad_credit_appr_ind VARCHAR2
                insertCall.setString(12, this.sgbstdn_grad_credit_appr_ind)

                // parm 13 p_capl_code  sgbstdn_capl_code VARCHAR2
                insertCall.setString(13, this.sgbstdn_capl_code)

                // parm 14 p_leav_code  sgbstdn_leav_code VARCHAR2
                insertCall.setString(14, this.sgbstdn_leav_code)

                // parm 15 p_leav_from_date  sgbstdn_leav_from_date DATE
                if ((this.sgbstdn_leav_from_date == "") || (this.sgbstdn_leav_from_date == null) || (!this.sgbstdn_leav_from_date)) { insertCall.setNull(15, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_leav_from_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(15, sqlDate)
                }

                // parm 16 p_leav_to_date  sgbstdn_leav_to_date DATE
                if ((this.sgbstdn_leav_to_date == "") || (this.sgbstdn_leav_to_date == null) || (!this.sgbstdn_leav_to_date)) { insertCall.setNull(16, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.sgbstdn_leav_to_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(16, sqlDate)
                }

                // parm 17 p_astd_code  sgbstdn_astd_code VARCHAR2
                insertCall.setString(17, this.sgbstdn_astd_code)

                // parm 18 p_term_code_astd  sgbstdn_term_code_astd VARCHAR2
                insertCall.setString(18, this.sgbstdn_term_code_astd)

                // parm 19 p_rate_code  sgbstdn_rate_code VARCHAR2
                insertCall.setString(19, this.sgbstdn_rate_code)

                // parm 20 p_edlv_code  sgbstdn_edlv_code VARCHAR2
                insertCall.setString(20, this.sgbstdn_edlv_code)

                // parm 21 p_incm_code  sgbstdn_incm_code VARCHAR2
                insertCall.setString(21, this.sgbstdn_incm_code)

                // parm 22 p_emex_code  sgbstdn_emex_code VARCHAR2
                insertCall.setString(22, this.sgbstdn_emex_code)

                // parm 23 p_aprn_code  sgbstdn_aprn_code VARCHAR2
                insertCall.setString(23, this.sgbstdn_aprn_code)

                // parm 24 p_trcn_code  sgbstdn_trcn_code VARCHAR2
                insertCall.setString(24, this.sgbstdn_trcn_code)

                // parm 25 p_gain_code  sgbstdn_gain_code VARCHAR2
                insertCall.setString(25, this.sgbstdn_gain_code)

                // parm 26 p_voed_code  sgbstdn_voed_code VARCHAR2
                insertCall.setString(26, this.sgbstdn_voed_code)

                // parm 27 p_blck_code  sgbstdn_blck_code VARCHAR2
                insertCall.setString(27, this.sgbstdn_blck_code)

                // parm 28 p_term_code_grad  sgbstdn_term_code_grad VARCHAR2
                insertCall.setString(28, this.sgbstdn_term_code_grad)

                // parm 29 p_acyr_code  sgbstdn_acyr_code VARCHAR2
                insertCall.setString(29, this.sgbstdn_acyr_code)

                // parm 30 p_site_code  sgbstdn_site_code VARCHAR2
                insertCall.setString(30, this.sgbstdn_site_code)

                // parm 31 p_egol_code  sgbstdn_egol_code VARCHAR2
                insertCall.setString(31, this.sgbstdn_egol_code)

                // parm 32 p_degc_code_dual  sgbstdn_degc_code_dual VARCHAR2
                insertCall.setString(32, this.sgbstdn_degc_code_dual)

                // parm 33 p_levl_code_dual  sgbstdn_levl_code_dual VARCHAR2
                insertCall.setString(33, this.sgbstdn_levl_code_dual)

                // parm 34 p_dept_code_dual  sgbstdn_dept_code_dual VARCHAR2
                insertCall.setString(34, this.sgbstdn_dept_code_dual)

                // parm 35 p_coll_code_dual  sgbstdn_coll_code_dual VARCHAR2
                insertCall.setString(35, this.sgbstdn_coll_code_dual)

                // parm 36 p_majr_code_dual  sgbstdn_majr_code_dual VARCHAR2
                insertCall.setString(36, this.sgbstdn_majr_code_dual)

                // parm 37 p_bskl_code  sgbstdn_bskl_code VARCHAR2
                insertCall.setString(37, this.sgbstdn_bskl_code)

                // parm 38 p_prev_code  sgbstdn_prev_code VARCHAR2
                insertCall.setString(38, this.sgbstdn_prev_code)

                // parm 39 p_term_code_prev  sgbstdn_term_code_prev VARCHAR2
                insertCall.setString(39, this.sgbstdn_term_code_prev)

                // parm 40 p_cast_code  sgbstdn_cast_code VARCHAR2
                insertCall.setString(40, this.sgbstdn_cast_code)

                // parm 41 p_term_code_cast  sgbstdn_term_code_cast VARCHAR2
                insertCall.setString(41, this.sgbstdn_term_code_cast)

                // parm 42 p_user_id  sgbstdn_user_id VARCHAR2
                insertCall.setString(42, connectInfo.userID)
                // parm 43 p_data_origin  sgbstdn_data_origin VARCHAR2
                insertCall.setString(43, connectInfo.dataOrigin)
                // parm 44 p_scpc_code								  sgbstdn_scpc_code								 VARCHAR2
                insertCall.setString(44, this.sgbstdn_scpc_code)

                // parm 45 p_rowid  sgbstdn_rowid VARCHAR2
                 insertCall.setNull(45, java.sql.Types.ROWID)

                if (connectInfo.debugThis) {
                    println "Update SGBSTDN ${bannerid} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SGBSTDN", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SGBSTDN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SGBSTDN ${bannerid} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                        println "Problem executing update for table SGBSTDN from GeneralStudentDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SGBSTDN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SGBSTDN ${bannerid} ${this.sgbstdn_term_code_eff} ${this.sgbstdn_stst_code}"
                    println "Problem setting up update for table SGBSTDN from GeneralStudentDML.groovy: $e.message"
                }
            }
        }
    }
}
