/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Insert and update term rules
 */
public class TermRulesDML {
    def sobterm_term_code
    def sobterm_crn_oneup
    def sobterm_reg_allowed
    def sobterm_readm_req
    def sobterm_fee_assessment
    def sobterm_fee_assessmnt_eff_date
    def sobterm_dupl_severity
    def sobterm_link_severity
    def sobterm_preq_severity
    def sobterm_corq_severity
    def sobterm_time_severity
    def sobterm_capc_severity
    def sobterm_levl_severity
    def sobterm_coll_severity
    def sobterm_majr_severity
    def sobterm_clas_severity
    def sobterm_appr_severity
    def sobterm_maxh_severity
    def sobterm_hold_severity
    def sobterm_hold
    def sobterm_refund_ind
    def sobterm_bycrn_ind
    def sobterm_rept_severity
    def sobterm_rpth_severity
    def sobterm_test_severity
    def sobterm_camp_severity
    def sobterm_fee_assess_vr
    def sobterm_print_bill_vr
    def sobterm_tmst_calc_ind
    def sobterm_incl_attmpt_hrs_ind
    def sobterm_cred_web_upd_ind
    def sobterm_gmod_web_upd_ind
    def sobterm_levl_web_upd_ind
    def sobterm_closect_web_disp_ind
    def sobterm_mailer_web_ind
    def sobterm_schd_web_search_ind
    def sobterm_camp_web_search_ind
    def sobterm_sess_web_search_ind
    def sobterm_instr_web_search_ind
    def sobterm_facschd_web_disp_ind
    def sobterm_claslst_web_disp_ind
    def sobterm_overapp_web_upd_ind
    def sobterm_add_drp_web_upd_ind
    def sobterm_degree_severity
    def sobterm_program_severity
    def sobterm_inprogress_usage_ind
    def sobterm_grade_detail_web_ind
    def sobterm_midterm_web_ind
    def sobterm_profile_send_ind
    def sobterm_cutoff_date
    def sobterm_tiv_date_source
    def sobterm_web_capp_term_ind
    def sobterm_web_capp_catlg_ind
    def sobterm_attr_web_search_ind
    def sobterm_levl_web_search_ind
    def sobterm_insm_web_search_ind
    def sobterm_ls_title_webs_disp_ind
    def sobterm_ls_desc_webs_disp_ind
    def sobterm_duration_web_srch_ind
    def sobterm_levl_web_catl_srch_ind
    def sobterm_styp_web_catl_srch_ind
    def sobterm_coll_web_catl_srch_ind
    def sobterm_div_web_catl_srch_ind
    def sobterm_dept_web_catl_srch_ind
    def sobterm_prog_att_webc_srch_ind
    def sobterm_lc_title_webc_disp_ind
    def sobterm_lc_desc_webc_disp_ind
    def sobterm_dynamic_sched_term_ind
    def sobterm_assess_swap_ind
    def sobterm_assess_rev_nrf_ind
    def sobterm_assess_reg_grace_ind
    def sobterm_minh_severity
    def sobterm_dept_severity
    def sobterm_atts_severity
    def sobterm_chrt_severity
    def sobterm_mexc_severity
    def sobterm_study_path_ind
    def sobterm_future_repeat_ind
    def sobterm_sp_web_upd_ind
    def sobterm_sectionfee_ind
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public TermRulesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSobterm()
    }


    def parseXmlData() {
        def validation = new XmlParser().parseText(xmlData)

        if (connectInfo.debugThis) {
            println "--------- New Term record ----------"
            println "Term: " + validation.SOBTERM_TERM_CODE.text()
        }
        this.sobterm_term_code = validation.SOBTERM_TERM_CODE.text()
        this.sobterm_crn_oneup = validation.SOBTERM_CRN_ONEUP.text()
        this.sobterm_reg_allowed = validation.SOBTERM_REG_ALLOWED.text()
        this.sobterm_readm_req = validation.SOBTERM_READM_REQ.text()
        this.sobterm_fee_assessment = validation.SOBTERM_FEE_ASSESSMENT.text()
        this.sobterm_fee_assessmnt_eff_date = validation.SOBTERM_FEE_ASSESSMNT_EFF_DATE.text()
        this.sobterm_dupl_severity = validation.SOBTERM_DUPL_SEVERITY.text()
        this.sobterm_link_severity = validation.SOBTERM_LINK_SEVERITY.text()
        this.sobterm_preq_severity = validation.SOBTERM_PREQ_SEVERITY.text()
        this.sobterm_corq_severity = validation.SOBTERM_CORQ_SEVERITY.text()
        this.sobterm_time_severity = validation.SOBTERM_TIME_SEVERITY.text()
        this.sobterm_capc_severity = validation.SOBTERM_CAPC_SEVERITY.text()
        this.sobterm_levl_severity = validation.SOBTERM_LEVL_SEVERITY.text()
        this.sobterm_coll_severity = validation.SOBTERM_COLL_SEVERITY.text()
        this.sobterm_majr_severity = validation.SOBTERM_MAJR_SEVERITY.text()
        this.sobterm_clas_severity = validation.SOBTERM_CLAS_SEVERITY.text()
        this.sobterm_appr_severity = validation.SOBTERM_APPR_SEVERITY.text()
        this.sobterm_maxh_severity = validation.SOBTERM_MAXH_SEVERITY.text()
        this.sobterm_hold_severity = validation.SOBTERM_HOLD_SEVERITY.text()
        this.sobterm_hold = validation.SOBTERM_HOLD.text()
        this.sobterm_refund_ind = validation.SOBTERM_REFUND_IND.text()
        this.sobterm_bycrn_ind = validation.SOBTERM_BYCRN_IND.text()
        this.sobterm_rept_severity = validation.SOBTERM_REPT_SEVERITY.text()
        this.sobterm_rpth_severity = validation.SOBTERM_RPTH_SEVERITY.text()
        this.sobterm_test_severity = validation.SOBTERM_TEST_SEVERITY.text()
        this.sobterm_camp_severity = validation.SOBTERM_CAMP_SEVERITY.text()
        this.sobterm_fee_assess_vr = validation.SOBTERM_FEE_ASSESS_VR.text()
        this.sobterm_print_bill_vr = validation.SOBTERM_PRINT_BILL_VR.text()
        this.sobterm_tmst_calc_ind = validation.SOBTERM_TMST_CALC_IND.text()
        this.sobterm_incl_attmpt_hrs_ind = validation.SOBTERM_INCL_ATTMPT_HRS_IND.text()
        this.sobterm_cred_web_upd_ind = validation.SOBTERM_CRED_WEB_UPD_IND.text()
        this.sobterm_gmod_web_upd_ind = validation.SOBTERM_GMOD_WEB_UPD_IND.text()
        this.sobterm_levl_web_upd_ind = validation.SOBTERM_LEVL_WEB_UPD_IND.text()
        this.sobterm_closect_web_disp_ind = validation.SOBTERM_CLOSECT_WEB_DISP_IND.text()
        this.sobterm_mailer_web_ind = validation.SOBTERM_MAILER_WEB_IND.text()
        this.sobterm_schd_web_search_ind = validation.SOBTERM_SCHD_WEB_SEARCH_IND.text()
        this.sobterm_camp_web_search_ind = validation.SOBTERM_CAMP_WEB_SEARCH_IND.text()
        this.sobterm_sess_web_search_ind = validation.SOBTERM_SESS_WEB_SEARCH_IND.text()
        this.sobterm_instr_web_search_ind = validation.SOBTERM_INSTR_WEB_SEARCH_IND.text()
        this.sobterm_facschd_web_disp_ind = validation.SOBTERM_FACSCHD_WEB_DISP_IND.text()
        this.sobterm_claslst_web_disp_ind = validation.SOBTERM_CLASLST_WEB_DISP_IND.text()
        this.sobterm_overapp_web_upd_ind = validation.SOBTERM_OVERAPP_WEB_UPD_IND.text()
        this.sobterm_add_drp_web_upd_ind = validation.SOBTERM_ADD_DRP_WEB_UPD_IND.text()
        this.sobterm_degree_severity = validation.SOBTERM_DEGREE_SEVERITY.text()
        this.sobterm_program_severity = validation.SOBTERM_PROGRAM_SEVERITY.text()
        this.sobterm_inprogress_usage_ind = validation.SOBTERM_INPROGRESS_USAGE_IND.text()
        this.sobterm_grade_detail_web_ind = validation.SOBTERM_GRADE_DETAIL_WEB_IND.text()
        this.sobterm_midterm_web_ind = validation.SOBTERM_MIDTERM_WEB_IND.text()
        this.sobterm_profile_send_ind = validation.SOBTERM_PROFILE_SEND_IND.text()
        this.sobterm_cutoff_date = validation.SOBTERM_CUTOFF_DATE.text()
        this.sobterm_tiv_date_source = validation.SOBTERM_TIV_DATE_SOURCE.text()
        this.sobterm_web_capp_term_ind = validation.SOBTERM_WEB_CAPP_TERM_IND.text()
        this.sobterm_web_capp_catlg_ind = validation.SOBTERM_WEB_CAPP_CATLG_IND.text()
        this.sobterm_attr_web_search_ind = validation.SOBTERM_ATTR_WEB_SEARCH_IND.text()
        this.sobterm_levl_web_search_ind = validation.SOBTERM_LEVL_WEB_SEARCH_IND.text()
        this.sobterm_insm_web_search_ind = validation.SOBTERM_INSM_WEB_SEARCH_IND.text()
        this.sobterm_ls_title_webs_disp_ind = validation.SOBTERM_LS_TITLE_WEBS_DISP_IND.text()
        this.sobterm_ls_desc_webs_disp_ind = validation.SOBTERM_LS_DESC_WEBS_DISP_IND.text()
        this.sobterm_duration_web_srch_ind = validation.SOBTERM_DURATION_WEB_SRCH_IND.text()
        this.sobterm_levl_web_catl_srch_ind = validation.SOBTERM_LEVL_WEB_CATL_SRCH_IND.text()
        this.sobterm_styp_web_catl_srch_ind = validation.SOBTERM_STYP_WEB_CATL_SRCH_IND.text()
        this.sobterm_coll_web_catl_srch_ind = validation.SOBTERM_COLL_WEB_CATL_SRCH_IND.text()
        this.sobterm_div_web_catl_srch_ind = validation.SOBTERM_DIV_WEB_CATL_SRCH_IND.text()
        this.sobterm_dept_web_catl_srch_ind = validation.SOBTERM_DEPT_WEB_CATL_SRCH_IND.text()
        this.sobterm_prog_att_webc_srch_ind = validation.SOBTERM_PROG_ATT_WEBC_SRCH_IND.text()
        this.sobterm_lc_title_webc_disp_ind = validation.SOBTERM_LC_TITLE_WEBC_DISP_IND.text()
        this.sobterm_lc_desc_webc_disp_ind = validation.SOBTERM_LC_DESC_WEBC_DISP_IND.text()
        this.sobterm_dynamic_sched_term_ind = validation.SOBTERM_DYNAMIC_SCHED_TERM_IND.text()
        this.sobterm_assess_swap_ind = validation.SOBTERM_ASSESS_SWAP_IND.text()
        this.sobterm_assess_rev_nrf_ind = validation.SOBTERM_ASSESS_REV_NRF_IND.text()
        this.sobterm_assess_reg_grace_ind = validation.SOBTERM_ASSESS_REG_GRACE_IND.text()
        this.sobterm_minh_severity = validation.SOBTERM_MINH_SEVERITY.text()
        this.sobterm_dept_severity = validation.SOBTERM_DEPT_SEVERITY.text()
        this.sobterm_atts_severity = validation.SOBTERM_ATTS_SEVERITY.text()
        this.sobterm_chrt_severity = validation.SOBTERM_CHRT_SEVERITY.text()
        this.sobterm_mexc_severity = validation.SOBTERM_MEXC_SEVERITY.text()
        this.sobterm_study_path_ind = validation.SOBTERM_STUDY_PATH_IND.text()
        this.sobterm_future_repeat_ind = validation.SOBTERM_FUTURE_REPEAT_IND.text()

        this.sobterm_sp_web_upd_ind = validation.SOBTERM_SP_WEB_UPD_IND.text()
        this.sobterm_sectionfee_ind = validation.SOBTERM_SECTIONFEE_IND.text()


    }



    def processSobterm() {
        tableRow = null
        String rowSQL = """select rowid table_row from SOBTERM
                    where sobterm_term_code = ? """
        try {
            conn.eachRow(rowSQL, [this.sobterm_term_code]) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SOBTERM rowid TermRulesDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 81
            try {

                String contextAPI = "{call gb_common.p_set_context('SB_TERM','TERM_RULES', 'Y','N')}"
                CallableStatement setContext = this.connectCall.prepareCall(contextAPI)
                setContext.executeUpdate()

                String API = "{call  sb_term.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code  sobterm_term_code VARCHAR2
                insertCall.setString(1, this.sobterm_term_code)

                // parm 2 p_crn_oneup  sobterm_crn_oneup NUMBER
                // find the current value and use that, else if no record exists already use the one in the seed data
                def crn
                String crnsql = "select max(to_number(ssbsect_crn)) crn from ssbsect where ssbsect_term_code = ?"
                conn.eachRow(crnsql, [this.sobterm_term_code]) {
                    crn = it.crn
                }

                if (!crn) {

                    if ((this.sobterm_crn_oneup == "") || (this.sobterm_crn_oneup == null) || (!this.sobterm_crn_oneup)) {
                        insertCall.setNull(2, java.sql.Types.INTEGER)
                    }
                    else {
                        insertCall.setInt(2, this.sobterm_crn_oneup.toInteger())
                    }
                }
                else {

                    int newCrn = crn + 1

                    insertCall.setInt(2, newCrn.toInteger())
                }

                // parm 3 p_reg_allowed  sobterm_reg_allowed VARCHAR2
                insertCall.setString(3, this.sobterm_reg_allowed)

                // parm 4 p_readm_req  sobterm_readm_req VARCHAR2
                insertCall.setString(4, this.sobterm_readm_req)

                // parm 5 p_fee_assessment  sobterm_fee_assessment VARCHAR2
                insertCall.setString(5, this.sobterm_fee_assessment)

                // parm 6 p_fee_assessmnt_eff_date  sobterm_fee_assessmnt_eff_date DATE
                if ((this.sobterm_fee_assessmnt_eff_date == "") || (this.sobterm_fee_assessmnt_eff_date == null) || (!this.sobterm_fee_assessmnt_eff_date)) {
                    insertCall.setNull(6, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sobterm_fee_assessmnt_eff_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(6, sqlDate)
                }

                // parm 7 p_dupl_severity  sobterm_dupl_severity VARCHAR2
                insertCall.setString(7, this.sobterm_dupl_severity)

                // parm 8 p_link_severity  sobterm_link_severity VARCHAR2
                insertCall.setString(8, this.sobterm_link_severity)

                // parm 9 p_preq_severity  sobterm_preq_severity VARCHAR2
                insertCall.setString(9, this.sobterm_preq_severity)

                // parm 10 p_corq_severity  sobterm_corq_severity VARCHAR2
                insertCall.setString(10, this.sobterm_corq_severity)

                // parm 11 p_time_severity  sobterm_time_severity VARCHAR2
                insertCall.setString(11, this.sobterm_time_severity)

                // parm 12 p_capc_severity  sobterm_capc_severity VARCHAR2
                insertCall.setString(12, this.sobterm_capc_severity)

                // parm 13 p_levl_severity  sobterm_levl_severity VARCHAR2
                insertCall.setString(13, this.sobterm_levl_severity)

                // parm 14 p_coll_severity  sobterm_coll_severity VARCHAR2
                insertCall.setString(14, this.sobterm_coll_severity)

                // parm 15 p_majr_severity  sobterm_majr_severity VARCHAR2
                insertCall.setString(15, this.sobterm_majr_severity)

                // parm 16 p_clas_severity  sobterm_clas_severity VARCHAR2
                insertCall.setString(16, this.sobterm_clas_severity)

                // parm 17 p_appr_severity  sobterm_appr_severity VARCHAR2
                insertCall.setString(17, this.sobterm_appr_severity)

                // parm 18 p_minh_severity  sobterm_minh_severity VARCHAR2
                insertCall.setString(18, this.sobterm_minh_severity)

                // parm 19 p_maxh_severity  sobterm_maxh_severity VARCHAR2
                insertCall.setString(19, this.sobterm_maxh_severity)

                // parm 20 p_hold_severity  sobterm_hold_severity VARCHAR2
                insertCall.setString(20, this.sobterm_hold_severity)

                // parm 21 p_hold  sobterm_hold VARCHAR2
                insertCall.setString(21, this.sobterm_hold)

                // parm 22 p_refund_ind  sobterm_refund_ind VARCHAR2
                insertCall.setString(22, this.sobterm_refund_ind)

                // parm 23 p_bycrn_ind  sobterm_bycrn_ind VARCHAR2
                insertCall.setString(23, this.sobterm_bycrn_ind)

                // parm 24 p_rept_severity  sobterm_rept_severity VARCHAR2
                insertCall.setString(24, this.sobterm_rept_severity)

                // parm 25 p_rpth_severity  sobterm_rpth_severity VARCHAR2
                insertCall.setString(25, this.sobterm_rpth_severity)

                // parm 26 p_test_severity  sobterm_test_severity VARCHAR2
                insertCall.setString(26, this.sobterm_test_severity)

                // parm 27 p_camp_severity  sobterm_camp_severity VARCHAR2
                insertCall.setString(27, this.sobterm_camp_severity)

                // parm 28 p_fee_assess_vr  sobterm_fee_assess_vr VARCHAR2
                insertCall.setString(28, this.sobterm_fee_assess_vr)

                // parm 29 p_print_bill_vr  sobterm_print_bill_vr VARCHAR2
                insertCall.setString(29, this.sobterm_print_bill_vr)

                // parm 30 p_tmst_calc_ind  sobterm_tmst_calc_ind VARCHAR2
                insertCall.setString(30, this.sobterm_tmst_calc_ind)

                // parm 31 p_incl_attmpt_hrs_ind  sobterm_incl_attmpt_hrs_ind VARCHAR2
                insertCall.setString(31, this.sobterm_incl_attmpt_hrs_ind)

                // parm 32 p_cred_web_upd_ind  sobterm_cred_web_upd_ind VARCHAR2
                insertCall.setString(32, this.sobterm_cred_web_upd_ind)

                // parm 33 p_gmod_web_upd_ind  sobterm_gmod_web_upd_ind VARCHAR2
                insertCall.setString(33, this.sobterm_gmod_web_upd_ind)

                // parm 34 p_levl_web_upd_ind  sobterm_levl_web_upd_ind VARCHAR2
                insertCall.setString(34, this.sobterm_levl_web_upd_ind)

                // parm 35 p_closect_web_disp_ind  sobterm_closect_web_disp_ind VARCHAR2
                insertCall.setString(35, this.sobterm_closect_web_disp_ind)

                // parm 36 p_mailer_web_ind  sobterm_mailer_web_ind VARCHAR2
                insertCall.setString(36, this.sobterm_mailer_web_ind)

                // parm 37 p_schd_web_search_ind  sobterm_schd_web_search_ind VARCHAR2
                insertCall.setString(37, this.sobterm_schd_web_search_ind)

                // parm 38 p_camp_web_search_ind  sobterm_camp_web_search_ind VARCHAR2
                insertCall.setString(38, this.sobterm_camp_web_search_ind)

                // parm 39 p_sess_web_search_ind  sobterm_sess_web_search_ind VARCHAR2
                insertCall.setString(39, this.sobterm_sess_web_search_ind)

                // parm 40 p_instr_web_search_ind  sobterm_instr_web_search_ind VARCHAR2
                insertCall.setString(40, this.sobterm_instr_web_search_ind)

                // parm 41 p_facschd_web_disp_ind  sobterm_facschd_web_disp_ind VARCHAR2
                insertCall.setString(41, this.sobterm_facschd_web_disp_ind)

                // parm 42 p_claslst_web_disp_ind  sobterm_claslst_web_disp_ind VARCHAR2
                insertCall.setString(42, this.sobterm_claslst_web_disp_ind)

                // parm 43 p_overapp_web_upd_ind  sobterm_overapp_web_upd_ind VARCHAR2
                insertCall.setString(43, this.sobterm_overapp_web_upd_ind)

                // parm 44 p_add_drp_web_upd_ind  sobterm_add_drp_web_upd_ind VARCHAR2
                insertCall.setString(44, this.sobterm_add_drp_web_upd_ind)

                // parm 45 p_degree_severity  sobterm_degree_severity VARCHAR2
                insertCall.setString(45, this.sobterm_degree_severity)

                // parm 46 p_program_severity  sobterm_program_severity VARCHAR2
                insertCall.setString(46, this.sobterm_program_severity)

                // parm 47 p_dept_severity  sobterm_dept_severity VARCHAR2
                insertCall.setString(47, this.sobterm_dept_severity)

                // parm 48 p_atts_severity  sobterm_atts_severity VARCHAR2
                insertCall.setString(48, this.sobterm_atts_severity)

                // parm 49 p_chrt_severity  sobterm_chrt_severity VARCHAR2
                insertCall.setString(49, this.sobterm_chrt_severity)

                // parm 50 p_mexc_severity  sobterm_mexc_severity VARCHAR2
                insertCall.setString(50, this.sobterm_mexc_severity)

                // parm 51 p_inprogress_usage_ind  sobterm_inprogress_usage_ind VARCHAR2
                insertCall.setString(51, this.sobterm_inprogress_usage_ind)

                // parm 52 p_grade_detail_web_ind  sobterm_grade_detail_web_ind VARCHAR2
                insertCall.setString(52, this.sobterm_grade_detail_web_ind)

                // parm 53 p_midterm_web_ind  sobterm_midterm_web_ind VARCHAR2
                insertCall.setString(53, this.sobterm_midterm_web_ind)

                // parm 54 p_profile_send_ind  sobterm_profile_send_ind VARCHAR2
                insertCall.setString(54, this.sobterm_profile_send_ind)

                // parm 55 p_cutoff_date  sobterm_cutoff_date DATE
                if ((this.sobterm_cutoff_date == "") || (this.sobterm_cutoff_date == null) || (!this.sobterm_cutoff_date)) {
                    insertCall.setNull(55, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sobterm_cutoff_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(55, sqlDate)
                }

                // parm 56 p_tiv_date_source  sobterm_tiv_date_source VARCHAR2
                insertCall.setString(56, this.sobterm_tiv_date_source)

                // parm 57 p_web_capp_term_ind  sobterm_web_capp_term_ind VARCHAR2
                insertCall.setString(57, this.sobterm_web_capp_term_ind)

                // parm 58 p_web_capp_catlg_ind  sobterm_web_capp_catlg_ind VARCHAR2
                insertCall.setString(58, this.sobterm_web_capp_catlg_ind)

                // parm 59 p_attr_web_search_ind  sobterm_attr_web_search_ind VARCHAR2
                insertCall.setString(59, this.sobterm_attr_web_search_ind)

                // parm 60 p_levl_web_search_ind  sobterm_levl_web_search_ind VARCHAR2
                insertCall.setString(60, this.sobterm_levl_web_search_ind)

                // parm 61 p_insm_web_search_ind  sobterm_insm_web_search_ind VARCHAR2
                insertCall.setString(61, this.sobterm_insm_web_search_ind)

                // parm 62 p_ls_title_webs_disp_ind  sobterm_ls_title_webs_disp_ind VARCHAR2
                insertCall.setString(62, this.sobterm_ls_title_webs_disp_ind)

                // parm 63 p_ls_desc_webs_disp_ind  sobterm_ls_desc_webs_disp_ind VARCHAR2
                insertCall.setString(63, this.sobterm_ls_desc_webs_disp_ind)

                // parm 64 p_duration_web_srch_ind  sobterm_duration_web_srch_ind VARCHAR2
                insertCall.setString(64, this.sobterm_duration_web_srch_ind)

                // parm 65 p_levl_web_catl_srch_ind  sobterm_levl_web_catl_srch_ind VARCHAR2
                insertCall.setString(65, this.sobterm_levl_web_catl_srch_ind)

                // parm 66 p_styp_web_catl_srch_ind  sobterm_styp_web_catl_srch_ind VARCHAR2
                insertCall.setString(66, this.sobterm_styp_web_catl_srch_ind)

                // parm 67 p_coll_web_catl_srch_ind  sobterm_coll_web_catl_srch_ind VARCHAR2
                insertCall.setString(67, this.sobterm_coll_web_catl_srch_ind)

                // parm 68 p_div_web_catl_srch_ind  sobterm_div_web_catl_srch_ind VARCHAR2
                insertCall.setString(68, this.sobterm_div_web_catl_srch_ind)

                // parm 69 p_dept_web_catl_srch_ind  sobterm_dept_web_catl_srch_ind VARCHAR2
                insertCall.setString(69, this.sobterm_dept_web_catl_srch_ind)

                // parm 70 p_prog_att_webc_srch_ind  sobterm_prog_att_webc_srch_ind VARCHAR2
                insertCall.setString(70, this.sobterm_prog_att_webc_srch_ind)

                // parm 71 p_lc_title_webc_disp_ind  sobterm_lc_title_webc_disp_ind VARCHAR2
                insertCall.setString(71, this.sobterm_lc_title_webc_disp_ind)

                // parm 72 p_lc_desc_webc_disp_ind  sobterm_lc_desc_webc_disp_ind VARCHAR2
                insertCall.setString(72, this.sobterm_lc_desc_webc_disp_ind)

                // parm 73 p_dynamic_sched_term_ind  sobterm_dynamic_sched_term_ind VARCHAR2
                insertCall.setString(73, this.sobterm_dynamic_sched_term_ind)

                // parm 74 p_assess_swap_ind  sobterm_assess_swap_ind VARCHAR2
                insertCall.setString(74, this.sobterm_assess_swap_ind)

                // parm 75 p_assess_rev_nrf_ind  sobterm_assess_rev_nrf_ind VARCHAR2
                insertCall.setString(75, this.sobterm_assess_rev_nrf_ind)

                // parm 76 p_assess_reg_grace_ind  sobterm_assess_reg_grace_ind VARCHAR2
                insertCall.setString(76, this.sobterm_assess_reg_grace_ind)

                // parm 77 p_study_path_ind  sobterm_study_path_ind VARCHAR2
                if (!this.sobterm_study_path_ind) {
                    insertCall.setString(77, "N")
                }
                else {
                    insertCall.setString(77, this.sobterm_study_path_ind)
                }

                // parm 78 p_future_repeat_ind  sobterm_future_repeat_ind VARCHAR2

                if (!this.sobterm_future_repeat_ind) {
                    insertCall.setString(78, "N")
                }
                else {
                    insertCall.setString(78, this.sobterm_future_repeat_ind)
                }

                // parm 79 p_sp_web_upd_ind  sobterm_sp_web_upd_ind VARCHAR2
                if (!this.sobterm_sp_web_upd_ind) {
                    insertCall.setString(79, "N")
                }
                else {
                    insertCall.setString(79, this.sobterm_sp_web_upd_ind)
                }

                // parm 80 p_sectionfee_ind  sobterm_sectionfee_ind VARCHAR2
                if (!this.sobterm_sectionfee_ind) {
                    insertCall.setString(80, "N")
                }
                else {
                    insertCall.setString(80, this.sobterm_sectionfee_ind)
                }

                // parm 81 p_data_origin  sobterm_data_origin VARCHAR2
                insertCall.setString(81, connectInfo.dataOrigin)
                // parm 82 p_user_id  sobterm_user_id VARCHAR2
                insertCall.setString(82, connectInfo.userID)
                // parm 83 p_rowid_out  sobterm_rowid_out VARCHAR2
                insertCall.registerOutParameter(83, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SOBTERM ${this.sobterm_term_code} "
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SOBTERM", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SOBTERM", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SOBTERM ${this.sobterm_term_code} "
                        println "Problem executing insert for table SOBTERM from TermRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SOBTERM", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SOBTERM ${this.sobterm_term_code} "
                    println "Problem setting up insert for table SOBTERM from TermRulesDML.groovy: $e.message"
                }
            }
        }
        else if (connectInfo.replaceData) {
            updateSobterm()
        }
    }


    def updateSobterm() {

        //  parm count is 81
        try {
            String contextAPI = "{call gb_common.p_set_context('SB_TERM','TERM_RULES', 'Y','N')}"
            CallableStatement setContext = this.connectCall.prepareCall(contextAPI)
            setContext.executeUpdate()

            String API = "{call  sb_term.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            // parm 1 p_term_code  sobterm_term_code VARCHAR2
            insertCall.setString(1, this.sobterm_term_code)

            // parm 2 p_crn_oneup  sobterm_crn_oneup NUMBER
            def crn
            String crnsql = "select max(to_number(ssbsect_crn)) crn from ssbsect where ssbsect_term_code = ?"
            conn.eachRow(crnsql, [this.sobterm_term_code]) {
                crn = it.crn
            }

            if (!crn) {

                if ((this.sobterm_crn_oneup == "") || (this.sobterm_crn_oneup == null) || (!this.sobterm_crn_oneup)) {
                    insertCall.setNull(2, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(2, this.sobterm_crn_oneup.toInteger())
                }
            }
            else {

                int newCrn = crn + 1

                insertCall.setInt(2, newCrn.toInteger())
            }

            // parm 3 p_reg_allowed  sobterm_reg_allowed VARCHAR2
            insertCall.setString(3, this.sobterm_reg_allowed)

            // parm 4 p_readm_req  sobterm_readm_req VARCHAR2
            insertCall.setString(4, this.sobterm_readm_req)

            // parm 5 p_fee_assessment  sobterm_fee_assessment VARCHAR2
            insertCall.setString(5, this.sobterm_fee_assessment)

            // parm 6 p_fee_assessmnt_eff_date  sobterm_fee_assessmnt_eff_date DATE
            if ((this.sobterm_fee_assessmnt_eff_date == "") || (this.sobterm_fee_assessmnt_eff_date == null) || (!this.sobterm_fee_assessmnt_eff_date)) {
                insertCall.setNull(6, java.sql.Types.DATE)
            }
            else {
                def ddate = new ColumnDateValue(this.sobterm_fee_assessmnt_eff_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(6, sqlDate)
            }

            // parm 7 p_dupl_severity  sobterm_dupl_severity VARCHAR2
            insertCall.setString(7, this.sobterm_dupl_severity)

            // parm 8 p_link_severity  sobterm_link_severity VARCHAR2
            insertCall.setString(8, this.sobterm_link_severity)

            // parm 9 p_preq_severity  sobterm_preq_severity VARCHAR2
            insertCall.setString(9, this.sobterm_preq_severity)

            // parm 10 p_corq_severity  sobterm_corq_severity VARCHAR2
            insertCall.setString(10, this.sobterm_corq_severity)

            // parm 11 p_time_severity  sobterm_time_severity VARCHAR2
            insertCall.setString(11, this.sobterm_time_severity)

            // parm 12 p_capc_severity  sobterm_capc_severity VARCHAR2
            insertCall.setString(12, this.sobterm_capc_severity)

            // parm 13 p_levl_severity  sobterm_levl_severity VARCHAR2
            insertCall.setString(13, this.sobterm_levl_severity)

            // parm 14 p_coll_severity  sobterm_coll_severity VARCHAR2
            insertCall.setString(14, this.sobterm_coll_severity)

            // parm 15 p_majr_severity  sobterm_majr_severity VARCHAR2
            insertCall.setString(15, this.sobterm_majr_severity)

            // parm 16 p_clas_severity  sobterm_clas_severity VARCHAR2
            insertCall.setString(16, this.sobterm_clas_severity)

            // parm 17 p_appr_severity  sobterm_appr_severity VARCHAR2
            insertCall.setString(17, this.sobterm_appr_severity)

            // parm 18 p_minh_severity  sobterm_minh_severity VARCHAR2
            insertCall.setString(18, this.sobterm_minh_severity)

            // parm 19 p_maxh_severity  sobterm_maxh_severity VARCHAR2
            insertCall.setString(19, this.sobterm_maxh_severity)

            // parm 20 p_hold_severity  sobterm_hold_severity VARCHAR2
            insertCall.setString(20, this.sobterm_hold_severity)

            // parm 21 p_hold  sobterm_hold VARCHAR2
            insertCall.setString(21, this.sobterm_hold)

            // parm 22 p_refund_ind  sobterm_refund_ind VARCHAR2
            insertCall.setString(22, this.sobterm_refund_ind)

            // parm 23 p_bycrn_ind  sobterm_bycrn_ind VARCHAR2
            insertCall.setString(23, this.sobterm_bycrn_ind)

            // parm 24 p_rept_severity  sobterm_rept_severity VARCHAR2
            insertCall.setString(24, this.sobterm_rept_severity)

            // parm 25 p_rpth_severity  sobterm_rpth_severity VARCHAR2
            insertCall.setString(25, this.sobterm_rpth_severity)

            // parm 26 p_test_severity  sobterm_test_severity VARCHAR2
            insertCall.setString(26, this.sobterm_test_severity)

            // parm 27 p_camp_severity  sobterm_camp_severity VARCHAR2
            insertCall.setString(27, this.sobterm_camp_severity)

            // parm 28 p_fee_assess_vr  sobterm_fee_assess_vr VARCHAR2
            insertCall.setString(28, this.sobterm_fee_assess_vr)

            // parm 29 p_print_bill_vr  sobterm_print_bill_vr VARCHAR2
            insertCall.setString(29, this.sobterm_print_bill_vr)

            // parm 30 p_tmst_calc_ind  sobterm_tmst_calc_ind VARCHAR2
            insertCall.setString(30, this.sobterm_tmst_calc_ind)

            // parm 31 p_incl_attmpt_hrs_ind  sobterm_incl_attmpt_hrs_ind VARCHAR2
            insertCall.setString(31, this.sobterm_incl_attmpt_hrs_ind)

            // parm 32 p_cred_web_upd_ind  sobterm_cred_web_upd_ind VARCHAR2
            insertCall.setString(32, this.sobterm_cred_web_upd_ind)

            // parm 33 p_gmod_web_upd_ind  sobterm_gmod_web_upd_ind VARCHAR2
            insertCall.setString(33, this.sobterm_gmod_web_upd_ind)

            // parm 34 p_levl_web_upd_ind  sobterm_levl_web_upd_ind VARCHAR2
            insertCall.setString(34, this.sobterm_levl_web_upd_ind)

            // parm 35 p_closect_web_disp_ind  sobterm_closect_web_disp_ind VARCHAR2
            insertCall.setString(35, this.sobterm_closect_web_disp_ind)

            // parm 36 p_mailer_web_ind  sobterm_mailer_web_ind VARCHAR2
            insertCall.setString(36, this.sobterm_mailer_web_ind)

            // parm 37 p_schd_web_search_ind  sobterm_schd_web_search_ind VARCHAR2
            insertCall.setString(37, this.sobterm_schd_web_search_ind)

            // parm 38 p_camp_web_search_ind  sobterm_camp_web_search_ind VARCHAR2
            insertCall.setString(38, this.sobterm_camp_web_search_ind)

            // parm 39 p_sess_web_search_ind  sobterm_sess_web_search_ind VARCHAR2
            insertCall.setString(39, this.sobterm_sess_web_search_ind)

            // parm 40 p_instr_web_search_ind  sobterm_instr_web_search_ind VARCHAR2
            insertCall.setString(40, this.sobterm_instr_web_search_ind)

            // parm 41 p_facschd_web_disp_ind  sobterm_facschd_web_disp_ind VARCHAR2
            insertCall.setString(41, this.sobterm_facschd_web_disp_ind)

            // parm 42 p_claslst_web_disp_ind  sobterm_claslst_web_disp_ind VARCHAR2
            insertCall.setString(42, this.sobterm_claslst_web_disp_ind)

            // parm 43 p_overapp_web_upd_ind  sobterm_overapp_web_upd_ind VARCHAR2
            insertCall.setString(43, this.sobterm_overapp_web_upd_ind)

            // parm 44 p_add_drp_web_upd_ind  sobterm_add_drp_web_upd_ind VARCHAR2
            insertCall.setString(44, this.sobterm_add_drp_web_upd_ind)

            // parm 45 p_degree_severity  sobterm_degree_severity VARCHAR2
            insertCall.setString(45, this.sobterm_degree_severity)

            // parm 46 p_program_severity  sobterm_program_severity VARCHAR2
            insertCall.setString(46, this.sobterm_program_severity)

            // parm 47 p_dept_severity  sobterm_dept_severity VARCHAR2
            insertCall.setString(47, this.sobterm_dept_severity)

            // parm 48 p_atts_severity  sobterm_atts_severity VARCHAR2
            insertCall.setString(48, this.sobterm_atts_severity)

            // parm 49 p_chrt_severity  sobterm_chrt_severity VARCHAR2
            insertCall.setString(49, this.sobterm_chrt_severity)

            // parm 50 p_mexc_severity  sobterm_mexc_severity VARCHAR2
            insertCall.setString(50, this.sobterm_mexc_severity)

            // parm 51 p_inprogress_usage_ind  sobterm_inprogress_usage_ind VARCHAR2
            insertCall.setString(51, this.sobterm_inprogress_usage_ind)

            // parm 52 p_grade_detail_web_ind  sobterm_grade_detail_web_ind VARCHAR2
            insertCall.setString(52, this.sobterm_grade_detail_web_ind)

            // parm 53 p_midterm_web_ind  sobterm_midterm_web_ind VARCHAR2
            insertCall.setString(53, this.sobterm_midterm_web_ind)

            // parm 54 p_profile_send_ind  sobterm_profile_send_ind VARCHAR2
            insertCall.setString(54, this.sobterm_profile_send_ind)

            // parm 55 p_cutoff_date  sobterm_cutoff_date DATE
            if ((this.sobterm_cutoff_date == "") || (this.sobterm_cutoff_date == null) || (!this.sobterm_cutoff_date)) {
                insertCall.setNull(55, java.sql.Types.DATE)
            }
            else {
                def ddate = new ColumnDateValue(this.sobterm_cutoff_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(55, sqlDate)
            }

            // parm 56 p_tiv_date_source  sobterm_tiv_date_source VARCHAR2
            insertCall.setString(56, this.sobterm_tiv_date_source)

            // parm 57 p_web_capp_term_ind  sobterm_web_capp_term_ind VARCHAR2
            insertCall.setString(57, this.sobterm_web_capp_term_ind)

            // parm 58 p_web_capp_catlg_ind  sobterm_web_capp_catlg_ind VARCHAR2
            insertCall.setString(58, this.sobterm_web_capp_catlg_ind)

            // parm 59 p_attr_web_search_ind  sobterm_attr_web_search_ind VARCHAR2
            insertCall.setString(59, this.sobterm_attr_web_search_ind)

            // parm 60 p_levl_web_search_ind  sobterm_levl_web_search_ind VARCHAR2
            insertCall.setString(60, this.sobterm_levl_web_search_ind)

            // parm 61 p_insm_web_search_ind  sobterm_insm_web_search_ind VARCHAR2
            insertCall.setString(61, this.sobterm_insm_web_search_ind)

            // parm 62 p_ls_title_webs_disp_ind  sobterm_ls_title_webs_disp_ind VARCHAR2
            insertCall.setString(62, this.sobterm_ls_title_webs_disp_ind)

            // parm 63 p_ls_desc_webs_disp_ind  sobterm_ls_desc_webs_disp_ind VARCHAR2
            insertCall.setString(63, this.sobterm_ls_desc_webs_disp_ind)

            // parm 64 p_duration_web_srch_ind  sobterm_duration_web_srch_ind VARCHAR2
            insertCall.setString(64, this.sobterm_duration_web_srch_ind)

            // parm 65 p_levl_web_catl_srch_ind  sobterm_levl_web_catl_srch_ind VARCHAR2
            insertCall.setString(65, this.sobterm_levl_web_catl_srch_ind)

            // parm 66 p_styp_web_catl_srch_ind  sobterm_styp_web_catl_srch_ind VARCHAR2
            insertCall.setString(66, this.sobterm_styp_web_catl_srch_ind)

            // parm 67 p_coll_web_catl_srch_ind  sobterm_coll_web_catl_srch_ind VARCHAR2
            insertCall.setString(67, this.sobterm_coll_web_catl_srch_ind)

            // parm 68 p_div_web_catl_srch_ind  sobterm_div_web_catl_srch_ind VARCHAR2
            insertCall.setString(68, this.sobterm_div_web_catl_srch_ind)

            // parm 69 p_dept_web_catl_srch_ind  sobterm_dept_web_catl_srch_ind VARCHAR2
            insertCall.setString(69, this.sobterm_dept_web_catl_srch_ind)

            // parm 70 p_prog_att_webc_srch_ind  sobterm_prog_att_webc_srch_ind VARCHAR2
            insertCall.setString(70, this.sobterm_prog_att_webc_srch_ind)

            // parm 71 p_lc_title_webc_disp_ind  sobterm_lc_title_webc_disp_ind VARCHAR2
            insertCall.setString(71, this.sobterm_lc_title_webc_disp_ind)

            // parm 72 p_lc_desc_webc_disp_ind  sobterm_lc_desc_webc_disp_ind VARCHAR2
            insertCall.setString(72, this.sobterm_lc_desc_webc_disp_ind)

            // parm 73 p_dynamic_sched_term_ind  sobterm_dynamic_sched_term_ind VARCHAR2
            insertCall.setString(73, this.sobterm_dynamic_sched_term_ind)

            // parm 74 p_assess_swap_ind  sobterm_assess_swap_ind VARCHAR2
            insertCall.setString(74, this.sobterm_assess_swap_ind)

            // parm 75 p_assess_rev_nrf_ind  sobterm_assess_rev_nrf_ind VARCHAR2
            insertCall.setString(75, this.sobterm_assess_rev_nrf_ind)

            // parm 76 p_assess_reg_grace_ind  sobterm_assess_reg_grace_ind VARCHAR2
            insertCall.setString(76, this.sobterm_assess_reg_grace_ind)

            // parm 77 p_study_path_ind  sobterm_study_path_ind VARCHAR2
            if (!this.sobterm_study_path_ind) {
                insertCall.setString(77, "N")
            }
            else {
                insertCall.setString(77, this.sobterm_study_path_ind)
            }

            // parm 78 p_future_repeat_ind  sobterm_future_repeat_ind VARCHAR2

            if (!this.sobterm_future_repeat_ind) {
                insertCall.setString(78, "N")
            }
            else {
                insertCall.setString(78, this.sobterm_future_repeat_ind)
            }

            // parm 79 p_sp_web_upd_ind  sobterm_sp_web_upd_ind VARCHAR2
            if (!this.sobterm_sp_web_upd_ind) {
                insertCall.setString(79, "N")
            }
            else {
                insertCall.setString(79, this.sobterm_sp_web_upd_ind)
            }

            // parm 80 p_sectionfee_ind  sobterm_sectionfee_ind VARCHAR2
            if (!this.sobterm_sectionfee_ind) {
                insertCall.setString(80, "N")
            }
            else {
                insertCall.setString(80, this.sobterm_sectionfee_ind)
            }

            // parm 81 p_data_origin  sobterm_data_origin VARCHAR2
            insertCall.setString(81, connectInfo.dataOrigin)
            // parm 82 p_user_id  sobterm_user_id VARCHAR2
            insertCall.setString(82, connectInfo.userID)
            // parm 83 p_rowid  sobterm_rowid VARCHAR2
            insertCall.setNull(83, java.sql.Types.ROWID)
            if (connectInfo.debugThis) {
                println "Update SOBTERM ${this.sobterm_term_code}"
            }
            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("SOBTERM", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SOBTERM", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SOBTERM ${this.sobterm_term_code}"
                    println "Problem executing update for table SOBTERM from TermRulesDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SOBTERM", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update SOBTERM ${this.sobterm_term_code} "
                println "Problem setting up update for table SOBTERM from TermRulesDML.groovy: $e.message"
            }
        }
    }
}
