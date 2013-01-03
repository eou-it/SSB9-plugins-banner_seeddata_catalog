/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 *  DML for schedule tables (ssbsect)
 */

public class ScheduleDML {
    def ssbsect_term_code = ""
    def ssbsect_crn
    def ssbsect_ptrm_code
    def ssbsect_subj_code
    def ssbsect_crse_numb
    def ssbsect_seq_numb
    def ssbsect_ssts_code
    def ssbsect_schd_code
    def ssbsect_camp_code
    def ssbsect_crse_title
    def ssbsect_credit_hrs
    def ssbsect_bill_hrs
    def ssbsect_gmod_code
    def ssbsect_sapr_code
    def ssbsect_sess_code
    def ssbsect_link_ident
    def ssbsect_prnt_ind
    def ssbsect_gradable_ind
    def ssbsect_tuiw_ind
    def ssbsect_reg_oneup
    def ssbsect_prior_enrl
    def ssbsect_proj_enrl
    def ssbsect_max_enrl
    def ssbsect_enrl
    def ssbsect_seats_avail
    def ssbsect_tot_credit_hrs
    def ssbsect_census_enrl
    def ssbsect_census_enrl_date
    def ssbsect_activity_date
    def ssbsect_ptrm_start_date
    def ssbsect_ptrm_end_date
    def ssbsect_ptrm_weeks
    def ssbsect_reserved_ind
    def ssbsect_wait_capacity
    def ssbsect_wait_count
    def ssbsect_wait_avail
    def ssbsect_lec_hr
    def ssbsect_lab_hr
    def ssbsect_oth_hr
    def ssbsect_cont_hr
    def ssbsect_acct_code
    def ssbsect_accl_code
    def ssbsect_census_2_date
    def ssbsect_enrl_cut_off_date
    def ssbsect_acad_cut_off_date
    def ssbsect_drop_cut_off_date
    def ssbsect_census_2_enrl
    def ssbsect_voice_avail
    def ssbsect_capp_prereq_test_ind
    def ssbsect_gsch_name
    def ssbsect_best_of_comp
    def ssbsect_subset_of_comp
    def ssbsect_insm_code
    def ssbsect_reg_from_date
    def ssbsect_reg_to_date
    def ssbsect_learner_regstart_fdate
    def ssbsect_learner_regstart_tdate
    def ssbsect_dunt_code
    def ssbsect_number_of_units
    def ssbsect_number_of_extensions
    def ssbsect_intg_cde
    def ssbsect_prereq_chk_method_cde
    java.sql.RowId ssbsectRow = null
    // database connection information
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public ScheduleDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processScheduleFile()

    }


    def parseXmlData() {
        def sch = new XmlParser().parseText(xmlData)

        if (connectInfo.debugThis) {
            println "--------- New Schedule record ----------"
            println "Section: " + sch.SSBSECT_TERM_CODE.text() + " " + sch.SSBSECT_CRN.text()
        }
        this.ssbsect_term_code = sch.SSBSECT_TERM_CODE.text()
        this.ssbsect_crn = sch.SSBSECT_CRN.text()
        this.ssbsect_ptrm_code = sch.SSBSECT_PTRM_CODE.text()
        this.ssbsect_subj_code = sch.SSBSECT_SUBJ_CODE.text()
        this.ssbsect_crse_numb = sch.SSBSECT_CRSE_NUMB.text()
        this.ssbsect_seq_numb = sch.SSBSECT_SEQ_NUMB.text()
        this.ssbsect_ssts_code = sch.SSBSECT_SSTS_CODE.text()
        this.ssbsect_schd_code = sch.SSBSECT_SCHD_CODE.text()
        this.ssbsect_camp_code = sch.SSBSECT_CAMP_CODE.text()
        this.ssbsect_crse_title = sch.SSBSECT_CRSE_TITLE.text()
        this.ssbsect_credit_hrs = sch.SSBSECT_CREDIT_HRS.text()
        this.ssbsect_bill_hrs = sch.SSBSECT_BILL_HRS.text()
        this.ssbsect_gmod_code = sch.SSBSECT_GMOD_CODE.text()
        this.ssbsect_sapr_code = sch.SSBSECT_SAPR_CODE.text()
        this.ssbsect_sess_code = sch.SSBSECT_SESS_CODE.text()
        this.ssbsect_link_ident = sch.SSBSECT_LINK_IDENT.text()
        this.ssbsect_prnt_ind = sch.SSBSECT_PRNT_IND.text()
        this.ssbsect_gradable_ind = sch.SSBSECT_GRADABLE_IND.text()
        this.ssbsect_tuiw_ind = sch.SSBSECT_TUIW_IND.text()
        this.ssbsect_reg_oneup = sch.SSBSECT_REG_ONEUP.text()
        this.ssbsect_prior_enrl = sch.SSBSECT_PRIOR_ENRL.text()
        this.ssbsect_proj_enrl = sch.SSBSECT_PROJ_ENRL.text()
        this.ssbsect_max_enrl = sch.SSBSECT_MAX_ENRL.text()
        this.ssbsect_enrl = sch.SSBSECT_ENRL.text()
        this.ssbsect_seats_avail = sch.SSBSECT_SEATS_AVAIL.text()
        this.ssbsect_tot_credit_hrs = sch.SSBSECT_TOT_CREDIT_HRS.text()
        this.ssbsect_census_enrl = sch.SSBSECT_CENSUS_ENRL.text()
        this.ssbsect_census_enrl_date = sch.SSBSECT_CENSUS_ENRL_DATE.text()
        this.ssbsect_ptrm_start_date = sch.SSBSECT_PTRM_START_DATE.text()
        this.ssbsect_ptrm_end_date = sch.SSBSECT_PTRM_END_DATE.text()
        this.ssbsect_ptrm_weeks = sch.SSBSECT_PTRM_WEEKS.text()
        this.ssbsect_reserved_ind = sch.SSBSECT_RESERVED_IND.text()
        this.ssbsect_wait_capacity = sch.SSBSECT_WAIT_CAPACITY.text()
        this.ssbsect_wait_count = sch.SSBSECT_WAIT_COUNT.text()
        this.ssbsect_wait_avail = sch.SSBSECT_WAIT_AVAIL.text()
        this.ssbsect_lec_hr = sch.SSBSECT_LEC_HR.text()
        this.ssbsect_lab_hr = sch.SSBSECT_LAB_HR.text()
        this.ssbsect_oth_hr = sch.SSBSECT_OTH_HR.text()
        this.ssbsect_cont_hr = sch.SSBSECT_CONT_HR.text()
        this.ssbsect_acct_code = sch.SSBSECT_ACCT_CODE.text()
        this.ssbsect_accl_code = sch.SSBSECT_ACCL_CODE.text()
        this.ssbsect_census_2_date = sch.SSBSECT_CENSUS_2_DATE.text()
        this.ssbsect_enrl_cut_off_date = sch.SSBSECT_ENRL_CUT_OFF_DATE.text()
        this.ssbsect_acad_cut_off_date = sch.SSBSECT_ACAD_CUT_OFF_DATE.text()
        this.ssbsect_drop_cut_off_date = sch.SSBSECT_DROP_CUT_OFF_DATE.text()
        this.ssbsect_census_2_enrl = sch.SSBSECT_CENSUS_2_ENRL.text()
        this.ssbsect_voice_avail = sch.SSBSECT_VOICE_AVAIL.text()
        this.ssbsect_capp_prereq_test_ind = sch.SSBSECT_CAPP_PREREQ_TEST_IND.text()
        this.ssbsect_gsch_name = sch.SSBSECT_GSCH_NAME.text()
        this.ssbsect_best_of_comp = sch.SSBSECT_BEST_OF_COMP.text()
        this.ssbsect_subset_of_comp = sch.SSBSECT_SUBSET_OF_COMP.text()
        this.ssbsect_insm_code = sch.SSBSECT_INSM_CODE.text()
        this.ssbsect_reg_from_date = sch.SSBSECT_REG_FROM_DATE.text()
        this.ssbsect_reg_to_date = sch.SSBSECT_REG_TO_DATE.text()
        this.ssbsect_learner_regstart_fdate = sch.SSBSECT_LEARNER_REGSTART_FDATE.text()
        this.ssbsect_learner_regstart_tdate = sch.SSBSECT_LEARNER_REGSTART_TDATE.text()
        this.ssbsect_dunt_code = sch.SSBSECT_DUNT_CODE.text()
        this.ssbsect_number_of_units = sch.SSBSECT_NUMBER_OF_UNITS.text()
        this.ssbsect_number_of_extensions = sch.SSBSECT_NUMBER_OF_EXTENSIONS.text()
        this.ssbsect_intg_cde = sch.SSBSECT_INTG_CDE.text()
        this.ssbsect_prereq_chk_method_cde = sch.SSBSECT_PREREQ_CHK_METHOD_CDE.text()
    }


    def processScheduleFile() {

        def sectionExists = ""
        String sectionSQL = """SELECT rowid  ssbsect_rowid
                            FROM ssbsect
                            WHERE ssbsect_crn = ?
                            AND ssbsect_term_code = ? """
        try {
            conn.eachRow(sectionSQL, [this.ssbsect_crn, this.ssbsect_term_code]) {sect ->
                ssbsectRow = sect.ssbsect_rowid
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${sectionSQL}"
                println "Problem selecting section rowid ScheduleDML.groovy: $e.message"
            }
        }

        if (ssbsectRow) {
            if ((connectInfo.replaceData)) {
                deleteSchedule()
                insertSchedule() // updateSchedule()
            }
        }
        else {
            insertSchedule()
        }
    }


    def deleteSchedule() {

        def stcrExists = ""
        String stcrRec = """SELECT sfrstcr_crn FROM sfrstcr
                       WHERE sfrstcr_crn = ? and sfrstcr_term_code = ? """
        try {
            conn.eachRow(stcrRec, [this.ssbsect_crn, this.ssbsect_term_code]) {row ->
                stcrExists = row.sfrstcr_crn
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${stcrRec}"
                println "Problem selecting registrations in ScheduleDML.groovy: $e.message"
            }
        }
        String schDel = ""
        //  if ((!stcrExists)) {
        try {
            schDel = """DELETE  FROM ssrmeet WHERE ssrmeet_term_code = ? AND ssrmeet_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }

        try {
            schDel = """DELETE FROM sirasgn WHERE sirasgn_term_code = ? AND sirasgn_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SIRASGN", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrlink   WHERE ssrlink_term_code = ? AND ssrlink_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRLINK", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrcorq WHERE ssrcorq_term_code = ?  AND ssrcorq_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRCORQ", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM SFRWLNT WHERE SFRWLNT_term_code = ? AND SFRWLNT_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SFRWLNT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrfees WHERE ssrfees_term_code = ? AND ssrfees_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRFEES", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM SSRRATT  WHERE ssrratt_term_code = ? AND ssrratt_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRATT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM SSRRCHR   WHERE ssrrchr_term_code = ? AND ssrrchr_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRCHR", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM SSRRDEP   WHERE ssrrdep_term_code = ? AND ssrrdep_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRDEP", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM SSBWLSC   WHERE ssbwlsc_term_code = ? AND ssbwlsc_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSBWLSC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrcol  WHERE ssrrcol_term_code = ? AND ssrrcol_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRCOL", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrdeg  WHERE ssrrdeg_term_code = ? AND ssrrdeg_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRDEG", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrprg WHERE ssrrprg_term_code = ? AND ssrrprg_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRPRG", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrrare  WHERE ssrrare_term_code = ? AND ssrrare_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRARE", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrcmp WHERE ssrrcmp_term_code = ? AND ssrrcmp_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRCMP", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrmaj WHERE ssrrmaj_term_code = ? AND ssrrmaj_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRMAJ", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrcls   WHERE ssrrcls_term_code = ?  AND ssrrcls_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRCLS", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrrlvl WHERE ssrrlvl_term_code = ? AND ssrrlvl_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRLVL", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrresv WHERE ssrresv_term_code = ? AND ssrresv_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRESV", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrattr  WHERE ssrattr_term_code = ? AND ssrattr_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRATTR", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrxlst  WHERE ssrxlst_term_code = ? AND ssrxlst_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRXLST", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrblck  WHERE ssrblck_term_code = ? AND ssrblck_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRBLCK", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrsccd  WHERE ssrsccd_term_code = ? AND ssrsccd_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSCCD", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssbovrr  WHERE ssbovrr_term_code = ? AND ssbovrr_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSBOVRR", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrrtst WHERE ssrrtst_term_code = ? AND ssrrtst_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRTST", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssreval  WHERE ssreval_term_code = ? AND ssreval_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSREVAL", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrsprt  WHERE ssrsprt_term_code = ? AND ssrsprt_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSPRT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrmprt   WHERE ssrmprt_term_code = ? AND ssrmprt_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRMPRT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrsrdf WHERE ssrsrdf_term_code = ? AND ssrsrdf_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSRDF", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrmrdf WHERE ssrmrdf_term_code = ? AND ssrmrdf_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRMRDF", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssbssec WHERE ssbssec_term_code = ?   AND ssbssec_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSBSSEC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssbfsec  WHERE ssbfsec_term_code = ?  AND ssbfsec_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRFSEC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE    FROM ssrrsts  WHERE ssrrsts_term_code = ? AND ssrrsts_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRSTS", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE    FROM ssrextn  WHERE ssrextn_term_code = ? AND ssrextn_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSREXTN", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrrfnd  WHERE ssrrfnd_term_code = ? AND ssrrfnd_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRRFND", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssbdesc WHERE ssbdesc_term_code = ? AND ssbdesc_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSBDESC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrtext WHERE ssrtext_term_code = ? AND ssrtext_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRTEXT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrsyln   WHERE ssrsyln_term_code = ? AND ssrsyln_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSYLN", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrsylo WHERE ssrsylo_term_code = ? AND ssrsylo_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSYLO", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE   FROM ssrsyrm   WHERE ssrsyrm_term_code = ? AND ssrsyrm_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSYRM", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM ssrsytr   WHERE ssrsytr_term_code = ? AND ssrsytr_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRSYTR", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE  FROM shrgcom  WHERE shrgcom_term_code = ? AND shrgcom_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SHRGCOM", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM shrscom  WHERE shrscom_term_code = ? AND shrscom_crn = ?"""
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code as String, this.ssbsect_crn])
            connectInfo.tableUpdate("SHRSCOM", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssrclbd  WHERE ssrclbd_term_code = ? AND ssrclbd_crn = ? """
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code, this.ssbsect_crn])
            connectInfo.tableUpdate("SSRCLBD", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }
        try {
            schDel = """DELETE FROM ssbsect  WHERE ssbsect_term_code = ? AND ssbsect_crn = ? """
            int delRows = conn.executeUpdate(schDel, [this.ssbsect_term_code, this.ssbsect_crn])
            connectInfo.tableUpdate("SSBSECT", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete SSBSECT from ScheduleDML.groovy: $e.message"
                println "${schDel}"
            }
        }

    }


    def updateSchedule() {
        //  parm count is 63
        try {
            String API = "{call  sb_section.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            // parm 1 p_term_code        ssbsect_term_code VARCHAR2
            insertCall.setString(1, this.ssbsect_term_code)

            // parm 2 p_crn        ssbsect_crn VARCHAR2
            insertCall.setString(2, this.ssbsect_crn)

            // parm 3 p_ptrm_code        ssbsect_ptrm_code VARCHAR2
            insertCall.setString(3, this.ssbsect_ptrm_code)

            // parm 4 p_subj_code        ssbsect_subj_code VARCHAR2
            insertCall.setString(4, this.ssbsect_subj_code)

            // parm 5 p_crse_numb        ssbsect_crse_numb VARCHAR2
            insertCall.setString(5, this.ssbsect_crse_numb)

            // parm 6 p_seq_numb        ssbsect_seq_numb VARCHAR2
            insertCall.setString(6, this.ssbsect_seq_numb)

            // parm 7 p_ssts_code        ssbsect_ssts_code VARCHAR2
            insertCall.setString(7, this.ssbsect_ssts_code)

            // parm 8 p_schd_code        ssbsect_schd_code VARCHAR2
            insertCall.setString(8, this.ssbsect_schd_code)

            // parm 9 p_camp_code        ssbsect_camp_code VARCHAR2
            insertCall.setString(9, this.ssbsect_camp_code)

            // parm 10 p_crse_title        ssbsect_crse_title VARCHAR2
            insertCall.setString(10, this.ssbsect_crse_title)

            // parm 11 p_credit_hrs        ssbsect_credit_hrs NUMBER
            if ((this.ssbsect_credit_hrs == "") || (this.ssbsect_credit_hrs == null) || (!this.ssbsect_credit_hrs)) { insertCall.setNull(11, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(11, this.ssbsect_credit_hrs.toDouble())
            }

            // parm 12 p_bill_hrs        ssbsect_bill_hrs NUMBER
            if ((this.ssbsect_bill_hrs == "") || (this.ssbsect_bill_hrs == null) || (!this.ssbsect_bill_hrs)) { insertCall.setNull(12, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(12, this.ssbsect_bill_hrs.toDouble())
            }

            // parm 13 p_gmod_code        ssbsect_gmod_code VARCHAR2
            insertCall.setString(13, this.ssbsect_gmod_code)

            // parm 14 p_sapr_code        ssbsect_sapr_code VARCHAR2
            insertCall.setString(14, this.ssbsect_sapr_code)

            // parm 15 p_sess_code        ssbsect_sess_code VARCHAR2
            insertCall.setString(15, this.ssbsect_sess_code)

            // parm 16 p_link_ident        ssbsect_link_ident VARCHAR2
            insertCall.setString(16, this.ssbsect_link_ident)

            // parm 17 p_prnt_ind        ssbsect_prnt_ind VARCHAR2
            insertCall.setString(17, this.ssbsect_prnt_ind)

            // parm 18 p_gradable_ind        ssbsect_gradable_ind VARCHAR2
            insertCall.setString(18, this.ssbsect_gradable_ind)

            // parm 19 p_tuiw_ind        ssbsect_tuiw_ind VARCHAR2
            insertCall.setString(19, this.ssbsect_tuiw_ind)

            // parm 20 p_reg_oneup        ssbsect_reg_oneup NUMBER
            if ((this.ssbsect_reg_oneup == "") || (this.ssbsect_reg_oneup == null) || (!this.ssbsect_reg_oneup)) { insertCall.setNull(20, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(20, this.ssbsect_reg_oneup.toInteger())
            }

            // parm 21 p_prior_enrl        ssbsect_prior_enrl NUMBER
            if ((this.ssbsect_prior_enrl == "") || (this.ssbsect_prior_enrl == null) || (!this.ssbsect_prior_enrl)) { insertCall.setNull(21, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(21, this.ssbsect_prior_enrl.toInteger())
            }

            // parm 22 p_proj_enrl        ssbsect_proj_enrl NUMBER
            if ((this.ssbsect_proj_enrl == "") || (this.ssbsect_proj_enrl == null) || (!this.ssbsect_proj_enrl)) { insertCall.setNull(22, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(22, this.ssbsect_proj_enrl.toInteger())
            }

            // parm 23 p_max_enrl        ssbsect_max_enrl NUMBER
            if ((this.ssbsect_max_enrl == "") || (this.ssbsect_max_enrl == null) || (!this.ssbsect_max_enrl)) { insertCall.setNull(23, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(23, this.ssbsect_max_enrl.toInteger())
            }

            // parm 24 p_enrl        ssbsect_enrl NUMBER
            if ((this.ssbsect_enrl == "") || (this.ssbsect_enrl == null) || (!this.ssbsect_enrl)) { insertCall.setNull(24, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(24, this.ssbsect_enrl.toInteger())
            }

            // parm 25 p_seats_avail        ssbsect_seats_avail NUMBER
            if ((this.ssbsect_seats_avail == "") || (this.ssbsect_seats_avail == null) || (!this.ssbsect_seats_avail)) { insertCall.setNull(25, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(25, this.ssbsect_seats_avail.toInteger())
            }

            // parm 26 p_tot_credit_hrs        ssbsect_tot_credit_hrs NUMBER
            if ((this.ssbsect_tot_credit_hrs == "") || (this.ssbsect_tot_credit_hrs == null) || (!this.ssbsect_tot_credit_hrs)) { insertCall.setNull(26, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(26, this.ssbsect_tot_credit_hrs.toDouble())
            }

            // parm 27 p_census_enrl        ssbsect_census_enrl NUMBER
            if ((this.ssbsect_census_enrl == "") || (this.ssbsect_census_enrl == null) || (!this.ssbsect_census_enrl)) { insertCall.setNull(27, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(27, this.ssbsect_census_enrl.toInteger())
            }

            // parm 28 p_census_enrl_date        ssbsect_census_enrl_date DATE
            if ((this.ssbsect_census_enrl_date == "") || (this.ssbsect_census_enrl_date == null) || (!this.ssbsect_census_enrl_date)) { insertCall.setNull(28, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_census_enrl_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(28, sqlDate)
            }

            // parm 29 p_ptrm_start_date        ssbsect_ptrm_start_date DATE
            if ((this.ssbsect_ptrm_start_date == "") || (this.ssbsect_ptrm_start_date == null) || (!this.ssbsect_ptrm_start_date)) { insertCall.setNull(29, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_ptrm_start_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(29, sqlDate)
            }

            // parm 30 p_ptrm_end_date        ssbsect_ptrm_end_date DATE
            if ((this.ssbsect_ptrm_end_date == "") || (this.ssbsect_ptrm_end_date == null) || (!this.ssbsect_ptrm_end_date)) { insertCall.setNull(30, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_ptrm_end_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(30, sqlDate)
            }

            // parm 31 p_ptrm_weeks        ssbsect_ptrm_weeks NUMBER
            if ((this.ssbsect_ptrm_weeks == "") || (this.ssbsect_ptrm_weeks == null) || (!this.ssbsect_ptrm_weeks)) { insertCall.setNull(31, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(31, this.ssbsect_ptrm_weeks.toInteger())
            }

            // parm 32 p_reserved_ind        ssbsect_reserved_ind VARCHAR2
            insertCall.setString(32, this.ssbsect_reserved_ind)

            // parm 33 p_wait_capacity        ssbsect_wait_capacity NUMBER
            if ((this.ssbsect_wait_capacity == "") || (this.ssbsect_wait_capacity == null) || (!this.ssbsect_wait_capacity)) { insertCall.setNull(33, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(33, this.ssbsect_wait_capacity.toInteger())
            }

            // parm 34 p_wait_count        ssbsect_wait_count NUMBER
            if ((this.ssbsect_wait_count == "") || (this.ssbsect_wait_count == null) || (!this.ssbsect_wait_count)) { insertCall.setNull(34, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(34, this.ssbsect_wait_count.toInteger())
            }

            // parm 35 p_wait_avail        ssbsect_wait_avail NUMBER
            if ((this.ssbsect_wait_avail == "") || (this.ssbsect_wait_avail == null) || (!this.ssbsect_wait_avail)) { insertCall.setNull(35, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(35, this.ssbsect_wait_avail.toInteger())
            }

            // parm 36 p_lec_hr        ssbsect_lec_hr NUMBER
            if ((this.ssbsect_lec_hr == "") || (this.ssbsect_lec_hr == null) || (!this.ssbsect_lec_hr)) { insertCall.setNull(36, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(36, this.ssbsect_lec_hr.toDouble())
            }

            // parm 37 p_lab_hr        ssbsect_lab_hr NUMBER
            if ((this.ssbsect_lab_hr == "") || (this.ssbsect_lab_hr == null) || (!this.ssbsect_lab_hr)) { insertCall.setNull(37, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(37, this.ssbsect_lab_hr.toDouble())
            }

            // parm 38 p_oth_hr        ssbsect_oth_hr NUMBER
            if ((this.ssbsect_oth_hr == "") || (this.ssbsect_oth_hr == null) || (!this.ssbsect_oth_hr)) { insertCall.setNull(38, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(38, this.ssbsect_oth_hr.toDouble())
            }

            // parm 39 p_cont_hr        ssbsect_cont_hr NUMBER
            if ((this.ssbsect_cont_hr == "") || (this.ssbsect_cont_hr == null) || (!this.ssbsect_cont_hr)) { insertCall.setNull(39, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(39, this.ssbsect_cont_hr.toDouble())
            }

            // parm 40 p_acct_code        ssbsect_acct_code VARCHAR2
            insertCall.setString(40, this.ssbsect_acct_code)

            // parm 41 p_accl_code        ssbsect_accl_code VARCHAR2
            insertCall.setString(41, this.ssbsect_accl_code)

            // parm 42 p_census_2_date        ssbsect_census_2_date DATE
            if ((this.ssbsect_census_2_date == "") || (this.ssbsect_census_2_date == null) || (!this.ssbsect_census_2_date)) { insertCall.setNull(42, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_census_2_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(42, sqlDate)
            }

            // parm 43 p_enrl_cut_off_date        ssbsect_enrl_cut_off_date DATE
            if ((this.ssbsect_enrl_cut_off_date == "") || (this.ssbsect_enrl_cut_off_date == null) || (!this.ssbsect_enrl_cut_off_date)) { insertCall.setNull(43, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_enrl_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(43, sqlDate)
            }

            // parm 44 p_acad_cut_off_date        ssbsect_acad_cut_off_date DATE
            if ((this.ssbsect_acad_cut_off_date == "") || (this.ssbsect_acad_cut_off_date == null) || (!this.ssbsect_acad_cut_off_date)) { insertCall.setNull(44, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_acad_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(44, sqlDate)
            }

            // parm 45 p_drop_cut_off_date        ssbsect_drop_cut_off_date DATE
            if ((this.ssbsect_drop_cut_off_date == "") || (this.ssbsect_drop_cut_off_date == null) || (!this.ssbsect_drop_cut_off_date)) { insertCall.setNull(45, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_drop_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(45, sqlDate)
            }

            // parm 46 p_census_2_enrl        ssbsect_census_2_enrl NUMBER
            if ((this.ssbsect_census_2_enrl == "") || (this.ssbsect_census_2_enrl == null) || (!this.ssbsect_census_2_enrl)) { insertCall.setNull(46, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(46, this.ssbsect_census_2_enrl.toInteger())
            }

            // parm 47 p_voice_avail        ssbsect_voice_avail VARCHAR2
            insertCall.setString(47, this.ssbsect_voice_avail)

            // parm 48 p_capp_prereq_test_ind     ss ssbsect_capp_prereq_test_inds VARCHAR2
            insertCall.setString(48, this.ssbsect_capp_prereq_test_ind)

            // parm 49 p_gsch_name        ssbsect_gsch_name VARCHAR2
            insertCall.setString(49, this.ssbsect_gsch_name)

            // parm 50 p_best_of_comp        ssbsect_best_of_comp NUMBER
            if ((this.ssbsect_best_of_comp == "") || (this.ssbsect_best_of_comp == null) || (!this.ssbsect_best_of_comp)) { insertCall.setNull(50, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(50, this.ssbsect_best_of_comp.toInteger())
            }

            // parm 51 p_subset_of_comp        ssbsect_subset_of_comp NUMBER
            if ((this.ssbsect_subset_of_comp == "") || (this.ssbsect_subset_of_comp == null) || (!this.ssbsect_subset_of_comp)) { insertCall.setNull(51, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(51, this.ssbsect_subset_of_comp.toInteger())
            }

            // parm 52 p_insm_code        ssbsect_insm_code VARCHAR2
            insertCall.setString(52, this.ssbsect_insm_code)

            // parm 53 p_reg_from_date        ssbsect_reg_from_date DATE
            if ((this.ssbsect_reg_from_date == "") || (this.ssbsect_reg_from_date == null) || (!this.ssbsect_reg_from_date)) { insertCall.setNull(53, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_reg_from_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(53, sqlDate)
            }

            // parm 54 p_reg_to_date        ssbsect_reg_to_date DATE
            if ((this.ssbsect_reg_to_date == "") || (this.ssbsect_reg_to_date == null) || (!this.ssbsect_reg_to_date)) { insertCall.setNull(54, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_reg_to_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(54, sqlDate)
            }

            // parm 55 p_learner_regstart_fdate   ssbs ssbsect_learner_regstart_fdatessb DATE
            if ((this.ssbsect_learner_regstart_fdate == "") || (this.ssbsect_learner_regstart_fdate == null) || (!this.ssbsect_learner_regstart_fdate)) { insertCall.setNull(55, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_learner_regstart_fdate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(55, sqlDate)
            }

            // parm 56 p_learner_regstart_tdate   ssbs ssbsect_learner_regstart_tdatessb DATE
            if ((this.ssbsect_learner_regstart_tdate == "") || (this.ssbsect_learner_regstart_tdate == null) || (!this.ssbsect_learner_regstart_tdate)) { insertCall.setNull(56, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_learner_regstart_tdate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(56, sqlDate)
            }

            // parm 57 p_dunt_code        ssbsect_dunt_code VARCHAR2
            insertCall.setString(57, this.ssbsect_dunt_code)

            // parm 58 p_number_of_units        ssbsect_number_of_units NUMBER
            if ((this.ssbsect_number_of_units == "") || (this.ssbsect_number_of_units == null) || (!this.ssbsect_number_of_units)) { insertCall.setNull(58, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(58, this.ssbsect_number_of_units.toDouble())
            }

            // parm 59 p_number_of_extensions     ss ssbsect_number_of_extensionss NUMBER
            if ((this.ssbsect_number_of_extensions == "") || (this.ssbsect_number_of_extensions == null) || (!this.ssbsect_number_of_extensions)) { insertCall.setNull(59, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(59, this.ssbsect_number_of_extensions.toDouble())
            }

            // parm 60 p_data_origin        ssbsect_data_origin VARCHAR2
            insertCall.setString(60, connectInfo.dataOrigin)
            // parm 61 p_user_id        ssbsect_user_id VARCHAR2
            insertCall.setString(61, connectInfo.userID)
            // parm 62 p_intg_cde        ssbsect_intg_cde VARCHAR2
            insertCall.setString(62, this.ssbsect_intg_cde)

            // parm 63 p_rowid        ssbsect_rowid VARCHAR2

            if (!this.ssbsect_prereq_chk_method_cde)
                insertCall.setString(63, "B")
            else insertCall.setString(63, this.ssbsect_prereq_chk_method_cde)

            // do not send the rowid because setRowId is broken
            insertCall.setNull(64, java.sql.Types.ROWID)
            //  insertCall.setRowId(63,ssbsectRow)
            if (connectInfo.debugThis) {
                println "Update SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn} ${this.ssbsect_ptrm_code}"
            }

            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("SSBSECT", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSBSECT", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn} ${this.ssbsect_ptrm_code}"
                    println "Problem executing update for table SSBSECT from ScheduleDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSBSECT", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn} ${this.ssbsect_ptrm_code}"
                println "Problem setting up update for table SSBSECT from ScheduleDML.groovy: $e.message"
            }
        }

    }


    def insertSchedule() {

        //  parm count is 63
        try {
            String API = "{call  sb_section.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)

            // parm 1 p_term_code  ssbsect_term_code VARCHAR2
            insertCall.setString(1, this.ssbsect_term_code)

            // parm 2 p_crn  ssbsect_crn VARCHAR2
            insertCall.setString(2, this.ssbsect_crn)

            // parm 3 p_ptrm_code  ssbsect_ptrm_code VARCHAR2
            insertCall.setString(3, this.ssbsect_ptrm_code)

            // parm 4 p_subj_code  ssbsect_subj_code VARCHAR2
            insertCall.setString(4, this.ssbsect_subj_code)

            // parm 5 p_crse_numb  ssbsect_crse_numb VARCHAR2
            insertCall.setString(5, this.ssbsect_crse_numb)

            // parm 6 p_seq_numb  ssbsect_seq_numb VARCHAR2
            insertCall.setString(6, this.ssbsect_seq_numb)

            // parm 7 p_ssts_code  ssbsect_ssts_code VARCHAR2
            insertCall.setString(7, this.ssbsect_ssts_code)

            // parm 8 p_schd_code  ssbsect_schd_code VARCHAR2
            insertCall.setString(8, this.ssbsect_schd_code)

            // parm 9 p_camp_code  ssbsect_camp_code VARCHAR2
            insertCall.setString(9, this.ssbsect_camp_code)

            // parm 10 p_crse_title  ssbsect_crse_title VARCHAR2
            insertCall.setString(10, this.ssbsect_crse_title)

            // parm 11 p_credit_hrs  ssbsect_credit_hrs NUMBER
            if ((this.ssbsect_credit_hrs == "") || (this.ssbsect_credit_hrs == null) || (!this.ssbsect_credit_hrs)) { insertCall.setNull(11, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(11, this.ssbsect_credit_hrs.toDouble())
            }

            // parm 12 p_bill_hrs  ssbsect_bill_hrs NUMBER
            if ((this.ssbsect_bill_hrs == "") || (this.ssbsect_bill_hrs == null) || (!this.ssbsect_bill_hrs)) { insertCall.setNull(12, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(12, this.ssbsect_bill_hrs.toDouble())
            }

            // parm 13 p_gmod_code  ssbsect_gmod_code VARCHAR2
            insertCall.setString(13, this.ssbsect_gmod_code)

            // parm 14 p_sapr_code  ssbsect_sapr_code VARCHAR2
            insertCall.setString(14, this.ssbsect_sapr_code)

            // parm 15 p_sess_code  ssbsect_sess_code VARCHAR2
            insertCall.setString(15, this.ssbsect_sess_code)

            // parm 16 p_link_ident  ssbsect_link_ident VARCHAR2
            insertCall.setString(16, this.ssbsect_link_ident)

            // parm 17 p_prnt_ind  ssbsect_prnt_ind VARCHAR2
            insertCall.setString(17, this.ssbsect_prnt_ind)

            // parm 18 p_gradable_ind  ssbsect_gradable_ind VARCHAR2
            insertCall.setString(18, this.ssbsect_gradable_ind)

            // parm 19 p_tuiw_ind  ssbsect_tuiw_ind VARCHAR2
            insertCall.setString(19, this.ssbsect_tuiw_ind)

            // parm 20 p_reg_oneup  ssbsect_reg_oneup NUMBER
            if ((this.ssbsect_reg_oneup == "") || (this.ssbsect_reg_oneup == null)
                    || (!this.ssbsect_reg_oneup)) {
                insertCall.setNull(20, java.sql.Types.INTEGER)
            }
            else {
                insertCall.setInt(20, this.ssbsect_reg_oneup.toInteger())
            }

            // parm 21 p_prior_enrl  ssbsect_prior_enrl NUMBER
            if ((this.ssbsect_prior_enrl == "") || (this.ssbsect_prior_enrl == null) || (!this.ssbsect_prior_enrl)) { insertCall.setNull(21, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(21, this.ssbsect_prior_enrl.toInteger())
            }

            // parm 22 p_proj_enrl  ssbsect_proj_enrl NUMBER
            if ((this.ssbsect_proj_enrl == "") || (this.ssbsect_proj_enrl == null) || (!this.ssbsect_proj_enrl)) { insertCall.setNull(22, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(22, this.ssbsect_proj_enrl.toInteger())
            }

            // parm 23 p_max_enrl  ssbsect_max_enrl NUMBER
            if ((this.ssbsect_max_enrl == "") || (this.ssbsect_max_enrl == null) || (!this.ssbsect_max_enrl)) { insertCall.setNull(23, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(23, this.ssbsect_max_enrl.toInteger())
            }

            // parm 24 p_enrl  ssbsect_enrl NUMBER
            if ((this.ssbsect_enrl == "") || (this.ssbsect_enrl == null) || (!this.ssbsect_enrl)) { insertCall.setNull(24, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(24, this.ssbsect_enrl.toInteger())
            }

            // parm 25 p_seats_avail  ssbsect_seats_avail NUMBER
            if ((this.ssbsect_seats_avail == "") || (this.ssbsect_seats_avail == null) || (!this.ssbsect_seats_avail)) { insertCall.setNull(25, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(25, this.ssbsect_seats_avail.toInteger())
            }

            // parm 26 p_tot_credit_hrs  ssbsect_tot_credit_hrs NUMBER
            if ((this.ssbsect_tot_credit_hrs == "") || (this.ssbsect_tot_credit_hrs == null) || (!this.ssbsect_tot_credit_hrs)) { insertCall.setNull(26, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(26, this.ssbsect_tot_credit_hrs.toDouble())
            }

            // parm 27 p_census_enrl  ssbsect_census_enrl NUMBER
            if ((this.ssbsect_census_enrl == "") || (this.ssbsect_census_enrl == null) || (!this.ssbsect_census_enrl)) { insertCall.setNull(27, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(27, this.ssbsect_census_enrl.toInteger())
            }

            // parm 28 p_census_enrl_date  ssbsect_census_enrl_date DATE
            if ((this.ssbsect_census_enrl_date == "") || (this.ssbsect_census_enrl_date == null) || (!this.ssbsect_census_enrl_date)) { insertCall.setNull(28, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_census_enrl_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(28, sqlDate)
            }

            // parm 29 p_ptrm_start_date  ssbsect_ptrm_start_date DATE
            if ((this.ssbsect_ptrm_start_date == "") || (this.ssbsect_ptrm_start_date == null) || (!this.ssbsect_ptrm_start_date)) { insertCall.setNull(29, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_ptrm_start_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(29, sqlDate)
            }

            // parm 30 p_ptrm_end_date  ssbsect_ptrm_end_date DATE
            if ((this.ssbsect_ptrm_end_date == "") || (this.ssbsect_ptrm_end_date == null) || (!this.ssbsect_ptrm_end_date)) { insertCall.setNull(30, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_ptrm_end_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(30, sqlDate)
            }

            // parm 31 p_ptrm_weeks  ssbsect_ptrm_weeks NUMBER
            if ((this.ssbsect_ptrm_weeks == "") || (this.ssbsect_ptrm_weeks == null) || (!this.ssbsect_ptrm_weeks)) { insertCall.setNull(31, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(31, this.ssbsect_ptrm_weeks.toInteger())
            }

            // parm 32 p_reserved_ind  ssbsect_reserved_ind VARCHAR2
            insertCall.setString(32, this.ssbsect_reserved_ind)

            // parm 33 p_wait_capacity  ssbsect_wait_capacity NUMBER
            if ((this.ssbsect_wait_capacity == "") || (this.ssbsect_wait_capacity == null) || (!this.ssbsect_wait_capacity)) { insertCall.setNull(33, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(33, this.ssbsect_wait_capacity.toInteger())
            }

            // parm 34 p_wait_count  ssbsect_wait_count NUMBER
            if ((this.ssbsect_wait_count == "") || (this.ssbsect_wait_count == null) || (!this.ssbsect_wait_count)) { insertCall.setNull(34, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(34, this.ssbsect_wait_count.toInteger())
            }

            // parm 35 p_wait_avail  ssbsect_wait_avail NUMBER
            if ((this.ssbsect_wait_avail == "") || (this.ssbsect_wait_avail == null) || (!this.ssbsect_wait_avail)) { insertCall.setNull(35, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(35, this.ssbsect_wait_avail.toInteger())
            }

            // parm 36 p_lec_hr  ssbsect_lec_hr NUMBER
            if ((this.ssbsect_lec_hr == "") || (this.ssbsect_lec_hr == null) || (!this.ssbsect_lec_hr)) { insertCall.setNull(36, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(36, this.ssbsect_lec_hr.toDouble())
            }

            // parm 37 p_lab_hr  ssbsect_lab_hr NUMBER
            if ((this.ssbsect_lab_hr == "") || (this.ssbsect_lab_hr == null) || (!this.ssbsect_lab_hr)) { insertCall.setNull(37, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(37, this.ssbsect_lab_hr.toDouble())
            }

            // parm 38 p_oth_hr  ssbsect_oth_hr NUMBER
            if ((this.ssbsect_oth_hr == "") || (this.ssbsect_oth_hr == null) || (!this.ssbsect_oth_hr)) { insertCall.setNull(38, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(38, this.ssbsect_oth_hr.toDouble())
            }

            // parm 39 p_cont_hr  ssbsect_cont_hr NUMBER
            if ((this.ssbsect_cont_hr == "") || (this.ssbsect_cont_hr == null) || (!this.ssbsect_cont_hr)) { insertCall.setNull(39, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(39, this.ssbsect_cont_hr.toDouble())
            }

            // parm 40 p_acct_code  ssbsect_acct_code VARCHAR2
            insertCall.setString(40, this.ssbsect_acct_code)

            // parm 41 p_accl_code  ssbsect_accl_code VARCHAR2
            insertCall.setString(41, this.ssbsect_accl_code)

            // parm 42 p_census_2_date  ssbsect_census_2_date DATE
            if ((this.ssbsect_census_2_date == "") || (this.ssbsect_census_2_date == null) || (!this.ssbsect_census_2_date)) { insertCall.setNull(42, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_census_2_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(42, sqlDate)
            }

            // parm 43 p_enrl_cut_off_date  ssbsect_enrl_cut_off_date DATE
            if ((this.ssbsect_enrl_cut_off_date == "") || (this.ssbsect_enrl_cut_off_date == null) || (!this.ssbsect_enrl_cut_off_date)) { insertCall.setNull(43, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_enrl_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(43, sqlDate)
            }

            // parm 44 p_acad_cut_off_date  ssbsect_acad_cut_off_date DATE
            if ((this.ssbsect_acad_cut_off_date == "") || (this.ssbsect_acad_cut_off_date == null) || (!this.ssbsect_acad_cut_off_date)) { insertCall.setNull(44, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_acad_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(44, sqlDate)
            }

            // parm 45 p_drop_cut_off_date  ssbsect_drop_cut_off_date DATE
            if ((this.ssbsect_drop_cut_off_date == "") || (this.ssbsect_drop_cut_off_date == null) || (!this.ssbsect_drop_cut_off_date)) { insertCall.setNull(45, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_drop_cut_off_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(45, sqlDate)
            }

            // parm 46 p_census_2_enrl  ssbsect_census_2_enrl NUMBER
            if ((this.ssbsect_census_2_enrl == "") || (this.ssbsect_census_2_enrl == null) || (!this.ssbsect_census_2_enrl)) { insertCall.setNull(46, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(46, this.ssbsect_census_2_enrl.toInteger())
            }

            // parm 47 p_voice_avail  ssbsect_voice_avail VARCHAR2
            insertCall.setString(47, this.ssbsect_voice_avail)

            // parm 48 p_capp_prereq_test_ind  ssbsect_capp_prereq_test_ind VARCHAR2
            insertCall.setString(48, this.ssbsect_capp_prereq_test_ind)

            // parm 49 p_gsch_name  ssbsect_gsch_name VARCHAR2
            insertCall.setString(49, this.ssbsect_gsch_name)

            // parm 50 p_best_of_comp  ssbsect_best_of_comp NUMBER
            if ((this.ssbsect_best_of_comp == "") || (this.ssbsect_best_of_comp == null) || (!this.ssbsect_best_of_comp)) { insertCall.setNull(50, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(50, this.ssbsect_best_of_comp.toInteger())
            }

            // parm 51 p_subset_of_comp  ssbsect_subset_of_comp NUMBER
            if ((this.ssbsect_subset_of_comp == "") || (this.ssbsect_subset_of_comp == null) || (!this.ssbsect_subset_of_comp)) { insertCall.setNull(51, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(51, this.ssbsect_subset_of_comp.toInteger())
            }

            // parm 52 p_insm_code  ssbsect_insm_code VARCHAR2
            insertCall.setString(52, this.ssbsect_insm_code)

            // parm 53 p_reg_from_date  ssbsect_reg_from_date DATE
            if ((this.ssbsect_reg_from_date == "") || (this.ssbsect_reg_from_date == null) || (!this.ssbsect_reg_from_date)) { insertCall.setNull(53, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_reg_from_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(53, sqlDate)
            }

            // parm 54 p_reg_to_date  ssbsect_reg_to_date DATE
            if ((this.ssbsect_reg_to_date == "") || (this.ssbsect_reg_to_date == null) || (!this.ssbsect_reg_to_date)) { insertCall.setNull(54, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_reg_to_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(54, sqlDate)
            }

            // parm 55 p_learner_regstart_fdate  ssbsect_learner_regstart_fdate DATE
            if ((this.ssbsect_learner_regstart_fdate == "") || (this.ssbsect_learner_regstart_fdate == null) || (!this.ssbsect_learner_regstart_fdate)) { insertCall.setNull(55, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_learner_regstart_fdate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(55, sqlDate)
            }

            // parm 56 p_learner_regstart_tdate  ssbsect_learner_regstart_tdate DATE
            if ((this.ssbsect_learner_regstart_tdate == "") || (this.ssbsect_learner_regstart_tdate == null) || (!this.ssbsect_learner_regstart_tdate)) { insertCall.setNull(56, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.ssbsect_learner_regstart_tdate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(56, sqlDate)
            }

            // parm 57 p_dunt_code  ssbsect_dunt_code VARCHAR2
            insertCall.setString(57, this.ssbsect_dunt_code)

            // parm 58 p_number_of_units  ssbsect_number_of_units NUMBER
            if ((this.ssbsect_number_of_units == "") || (this.ssbsect_number_of_units == null) || (!this.ssbsect_number_of_units)) { insertCall.setNull(58, java.sql.Types.DOUBLE) }
            else {
                insertCall.setDouble(58, this.ssbsect_number_of_units.toDouble())
            }

            // parm 59 p_number_of_extensions  ssbsect_number_of_extensions NUMBER
            if ((this.ssbsect_number_of_extensions == "") || (this.ssbsect_number_of_extensions == null) || (!this.ssbsect_number_of_extensions)) { insertCall.setNull(59, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(59, this.ssbsect_number_of_extensions.toInteger())
            }

            // parm 60 p_data_origin  ssbsect_data_origin VARCHAR2
            insertCall.setString(60, connectInfo.dataOrigin)
            // parm 61 p_user_id  ssbsect_user_id VARCHAR2
            insertCall.setString(61, connectInfo.userID)
            // parm 62 p_intg_cde  ssbsect_intg_cde VARCHAR2
            insertCall.setString(62, this.ssbsect_intg_cde)
            if (!this.ssbsect_prereq_chk_method_cde)
                insertCall.setString(63, "B")
            else insertCall.setString(63, this.ssbsect_prereq_chk_method_cde)
            // parm 63 p_rowid_out  ssbsect_rowid_out VARCHAR2
            insertCall.registerOutParameter(64, java.sql.Types.ROWID)



            if (connectInfo.debugThis) {
                println "Insert SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn}  "
            }
            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("SSBSECT", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSBSECT", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn} ${this.ssbsect_ptrm_code}"
                    println "Problem executing insert for table SSBSECT from ScheduleDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSBSECT", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert SSBSECT ${this.ssbsect_term_code} ${this.ssbsect_crn}"
                println "Problem setting up insert for table SSBSECT from ScheduleDML.groovy: $e.message"
            }
        }
    }


}
