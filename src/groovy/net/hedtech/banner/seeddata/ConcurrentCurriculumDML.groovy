/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 *  DML for SORLCUR
 */
public class ConcurrentCurriculumDML {
    def bannerid
    def sorlcur_pidm
    def sorlcur_seqno
    def sorlcur_lmod_code
    def sorlcur_term_code
    def sorlcur_key_seqno
    def sorlcur_priority_no
    def sorlcur_roll_ind
    def sorlcur_cact_code
    def sorlcur_levl_code
    def sorlcur_coll_code
    def sorlcur_degc_code
    def sorlcur_term_code_ctlg
    def sorlcur_term_code_end
    def sorlcur_term_code_matric
    def sorlcur_term_code_admit
    def sorlcur_admt_code
    def sorlcur_camp_code
    def sorlcur_program
    def sorlcur_start_date
    def sorlcur_end_date
    def sorlcur_curr_rule
    def sorlcur_rolled_seqno
    def sorlcur_styp_code
    def sorlcur_rate_code
    def sorlcur_leav_code
    def sorlcur_leav_from_date
    def sorlcur_leav_to_date
    def sorlcur_exp_grad_date
    def sorlcur_term_code_grad
    def sorlcur_acyr_code
    def sorlcur_site_code
    def sorlcur_appl_seqno
    def sorlcur_appl_key_seqno
    def sorlcur_user_id_update
    def sorlcur_activity_date_update
    def sorlcur_gapp_seqno
    def sorlcur_current_cde
    def curricCheckingOn
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null

    def outcome
    def seqout

    public ConcurrentCurriculumDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSorlcur()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.sorlcur_pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()
        if (connectInfo.debugThis) {
            println "--------- New XML SORLCUR record ----------"
            println "${bannerid}    ${apiData.SORLCUR_SEQNO.text()}    ${apiData.SORLCUR_LMOD_CODE.text()}    ${apiData.SORLCUR_TERM_CODE.text()}    "
        }
        this.curricCheckingOn = apiData?.CURRICON?.text() ? apiData.CURRICON?.text() : "Y"
        this.sorlcur_seqno = apiData.SORLCUR_SEQNO.text()
        this.sorlcur_lmod_code = apiData.SORLCUR_LMOD_CODE.text()
        this.sorlcur_term_code = apiData.SORLCUR_TERM_CODE.text()
        this.sorlcur_key_seqno = apiData.SORLCUR_KEY_SEQNO.text()
        this.sorlcur_priority_no = apiData.SORLCUR_PRIORITY_NO.text()
        this.sorlcur_roll_ind = apiData.SORLCUR_ROLL_IND.text()
        this.sorlcur_cact_code = apiData.SORLCUR_CACT_CODE.text()
        this.sorlcur_levl_code = apiData.SORLCUR_LEVL_CODE.text()
        this.sorlcur_coll_code = apiData.SORLCUR_COLL_CODE.text()
        this.sorlcur_degc_code = apiData.SORLCUR_DEGC_CODE.text()
        this.sorlcur_term_code_ctlg = apiData.SORLCUR_TERM_CODE_CTLG.text()
        this.sorlcur_term_code_end = apiData.SORLCUR_TERM_CODE_END.text()
        this.sorlcur_term_code_matric = apiData.SORLCUR_TERM_CODE_MATRIC.text()
        this.sorlcur_term_code_admit = apiData.SORLCUR_TERM_CODE_ADMIT.text()
        this.sorlcur_admt_code = apiData.SORLCUR_ADMT_CODE.text()
        this.sorlcur_camp_code = apiData.SORLCUR_CAMP_CODE.text()
        this.sorlcur_program = apiData.SORLCUR_PROGRAM.text()
        this.sorlcur_start_date = apiData.SORLCUR_START_DATE.text()
        this.sorlcur_end_date = apiData.SORLCUR_END_DATE.text()
        this.sorlcur_curr_rule = apiData.SORLCUR_CURR_RULE.text()
        this.sorlcur_rolled_seqno = apiData.SORLCUR_ROLLED_SEQNO.text()
        this.sorlcur_styp_code = apiData.SORLCUR_STYP_CODE.text()
        this.sorlcur_rate_code = apiData.SORLCUR_RATE_CODE.text()
        this.sorlcur_leav_code = apiData.SORLCUR_LEAV_CODE.text()
        this.sorlcur_leav_from_date = apiData.SORLCUR_LEAV_FROM_DATE.text()
        this.sorlcur_leav_to_date = apiData.SORLCUR_LEAV_TO_DATE.text()
        this.sorlcur_exp_grad_date = apiData.SORLCUR_EXP_GRAD_DATE.text()
        this.sorlcur_term_code_grad = apiData.SORLCUR_TERM_CODE_GRAD.text()
        this.sorlcur_acyr_code = apiData.SORLCUR_ACYR_CODE.text()
        this.sorlcur_site_code = apiData.SORLCUR_SITE_CODE.text()
        this.sorlcur_appl_seqno = apiData.SORLCUR_APPL_SEQNO.text()
        this.sorlcur_appl_key_seqno = apiData.SORLCUR_APPL_KEY_SEQNO.text()
        this.sorlcur_user_id_update = apiData.SORLCUR_USER_ID_UPDATE.text()
        this.sorlcur_activity_date_update = apiData.SORLCUR_ACTIVITY_DATE_UPDATE.text()
        this.sorlcur_gapp_seqno = apiData.SORLCUR_GAPP_SEQNO.text()
        this.sorlcur_current_cde = apiData.SORLCUR_CURRENT_CDE.text()


    }


    def processSorlcur() {
        // turn curriculum checking on

        conn.executeUpdate("update sobctrl set sobctrl_curr_rule_ind = ?", this.curricCheckingOn)
        conn.execute "{ call gb_common.p_commit() }"
        tableRow = null
        conn.call("{? = call sb_curriculum_str.f_outcome()}", [Sql.VARCHAR]) { result -> outcome = result }

        String rowSQL = """select rowid table_row from SORLCUR
           where sorlcur_pidm = ?  and sorlcur_seqno = ? """
        try {
            conn.eachRow(rowSQL, [this.sorlcur_pidm, this.sorlcur_seqno]) { row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SORLCUR rowid ConcurrentCurriculumDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 43
            try {
                String API = "{call  sb_curriculum.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sorlcur_pidm NUMBER
                if ((this.sorlcur_pidm == "") || (this.sorlcur_pidm == null) || (!this.sorlcur_pidm)) {
                    insertCall.setNull(1, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(1, this.sorlcur_pidm.toInteger())
                }

                // parm 2 p_seqno  sorlcur_seqno NUMBER
                if ((this.sorlcur_seqno == "") || (this.sorlcur_seqno == null) || (!this.sorlcur_seqno)) {
                    insertCall.setNull(2, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(2, this.sorlcur_seqno.toInteger())
                }

                // parm 3 p_lmod_code  sorlcur_lmod_code VARCHAR2
                insertCall.setString(3, this.sorlcur_lmod_code)

                // parm 4 p_term_code  sorlcur_term_code VARCHAR2
                insertCall.setString(4, this.sorlcur_term_code)

                // parm 5 p_key_seqno  sorlcur_key_seqno NUMBER
                if ((this.sorlcur_key_seqno == "") || (this.sorlcur_key_seqno == null) || (!this.sorlcur_key_seqno)) {
                    insertCall.setNull(5, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(5, this.sorlcur_key_seqno.toInteger())
                }

                // parm 6 p_priority_no  sorlcur_priority_no NUMBER
                if ((this.sorlcur_priority_no == "") || (this.sorlcur_priority_no == null) || (!this.sorlcur_priority_no)) {
                    insertCall.setNull(6, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(6, this.sorlcur_priority_no.toInteger())
                }

                // parm 7 p_roll_ind  sorlcur_roll_ind VARCHAR2
                insertCall.setString(7, this.sorlcur_roll_ind)

                // parm 8 p_cact_code  sorlcur_cact_code VARCHAR2
                insertCall.setString(8, this.sorlcur_cact_code)

                // parm 9 p_user_id  sorlcur_user_id VARCHAR2
                insertCall.setString(9, connectInfo.userID)
                // parm 10 p_data_origin  sorlcur_data_origin VARCHAR2
                insertCall.setString(10, connectInfo.dataOrigin)
                // parm 11 p_levl_code  sorlcur_levl_code VARCHAR2
                insertCall.setString(11, this.sorlcur_levl_code)

                // parm 12 p_coll_code  sorlcur_coll_code VARCHAR2
                insertCall.setString(12, this.sorlcur_coll_code)

                // parm 13 p_degc_code  sorlcur_degc_code VARCHAR2
                insertCall.setString(13, this.sorlcur_degc_code)

                // parm 14 p_term_code_ctlg  sorlcur_term_code_ctlg VARCHAR2
                insertCall.setString(14, this.sorlcur_term_code_ctlg)

                // parm 15 p_term_code_end  sorlcur_term_code_end VARCHAR2
                insertCall.setString(15, this.sorlcur_term_code_end)

                // parm 16 p_term_code_matric  sorlcur_term_code_matric VARCHAR2
                insertCall.setString(16, this.sorlcur_term_code_matric)

                // parm 17 p_term_code_admit  sorlcur_term_code_admit VARCHAR2
                insertCall.setString(17, this.sorlcur_term_code_admit)

                // parm 18 p_admt_code  sorlcur_admt_code VARCHAR2
                insertCall.setString(18, this.sorlcur_admt_code)

                // parm 19 p_camp_code  sorlcur_camp_code VARCHAR2
                insertCall.setString(19, this.sorlcur_camp_code)

                // parm 20 p_program  sorlcur_program VARCHAR2
                insertCall.setString(20, this.sorlcur_program)

                // parm 21 p_start_date  sorlcur_start_date DATE
                if ((this.sorlcur_start_date == "") || (this.sorlcur_start_date == null) ||
                        (!this.sorlcur_start_date)) {
                    insertCall.setNull(21, java.sql.Types.DATE)
                } else {
                    def ddate = new ColumnDateValue(this.sorlcur_start_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(21, sqlDate)
                }

                // parm 22 p_end_date  sorlcur_end_date DATE
                if ((this.sorlcur_end_date == "") || (this.sorlcur_end_date == null) ||
                        (!this.sorlcur_end_date)) {
                    insertCall.setNull(22, java.sql.Types.DATE)
                } else {
                    def ddate = new ColumnDateValue(this.sorlcur_end_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(22, sqlDate)
                }

                // parm 23 p_curr_rule  sorlcur_curr_rule NUMBER

                if ((this.sorlcur_curr_rule == "") || (
                        this.sorlcur_curr_rule == null) ||
                        (!this.sorlcur_curr_rule)) {
                    insertCall.setNull(23, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(23, this.sorlcur_curr_rule.toInteger())
                }

                // parm 24 p_rolled_seqno  sorlcur_rolled_seqno NUMBER
//                if ((this.sorlcur_rolled_seqno == "") || (this.sorlcur_rolled_seqno == null)
//                        || (!this.sorlcur_rolled_seqno)) {
//                    insertCall.setNull(24, java.sql.Types.INTEGER) }
//                else {
//                    insertCall.setInt(24, this.sorlcur_rolled_seqno.toInteger())
//                }
                insertCall.setNull(24, java.sql.Types.INTEGER)
                // parm 25 p_override_severity  sorlcur_override_severity

                insertCall.setString(25, "N")

                // parm 26 p_styp_code  sorlcur_styp_code VARCHAR2
                insertCall.setString(26, this.sorlcur_styp_code)

                // parm 27 p_exp_grad_date  sorlcur_exp_grad_date DATE
                if ((this.sorlcur_exp_grad_date == "") || (this.sorlcur_exp_grad_date == null) ||
                        (!this.sorlcur_exp_grad_date)) {
                    insertCall.setNull(27, java.sql.Types.DATE)
                } else {
                    def ddate = new ColumnDateValue(this.sorlcur_exp_grad_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(27, sqlDate)
                }

                // parm 28 p_leav_code  sorlcur_leav_code VARCHAR2
                insertCall.setString(28, this.sorlcur_leav_code)

                // parm 29 p_leav_from_date  sorlcur_leav_from_date DATE
                if ((this.sorlcur_leav_from_date == "") || (this.sorlcur_leav_from_date == null) ||
                        (!this.sorlcur_leav_from_date)) {
                    insertCall.setNull(29, java.sql.Types.DATE)
                } else {
                    def ddate = new ColumnDateValue(this.sorlcur_leav_from_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(29, sqlDate)
                }

                // parm 30 p_leav_to_date  sorlcur_leav_to_date DATE
                if ((this.sorlcur_leav_to_date == "") || (this.sorlcur_leav_to_date == null) ||
                        (!this.sorlcur_leav_to_date)) {
                    insertCall.setNull(30, java.sql.Types.DATE)
                } else {
                    def ddate = new ColumnDateValue(this.sorlcur_leav_to_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(30, sqlDate)
                }

                // parm 31 p_rate_code  sorlcur_rate_code VARCHAR2
                insertCall.setString(31, this.sorlcur_rate_code)

                // parm 32 p_term_code_grad  sorlcur_term_code_grad VARCHAR2
                insertCall.setString(32, this.sorlcur_term_code_grad)

                // parm 33 p_acyr_code  sorlcur_acyr_code VARCHAR2
                insertCall.setString(33, this.sorlcur_acyr_code)

                // parm 34 p_site_code  sorlcur_site_code VARCHAR2
                insertCall.setString(34, this.sorlcur_site_code)

                // parm 35 p_appl_seqno  sorlcur_appl_seqno NUMBER
                if ((this.sorlcur_appl_seqno == "") || (this.sorlcur_appl_seqno == null)
                        || (!this.sorlcur_appl_seqno)) {
                    insertCall.setNull(35, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(35, this.sorlcur_appl_seqno.toInteger())
                }

                // parm 36 p_appl_key_seqno  sorlcur_appl_key_seqno NUMBER
                if ((this.sorlcur_appl_key_seqno == "") || (this.sorlcur_appl_key_seqno == null)
                        || (!this.sorlcur_appl_key_seqno)) {
                    insertCall.setNull(36, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(36, this.sorlcur_appl_key_seqno.toInteger())
                }

                // parm 37 p_rowid_out  sorlcur_rowid_out NUMBER
                insertCall.registerOutParameter(37, java.sql.Types.ROWID)

                // parm 38 p_seqno_out  sorlcur_seqno_out NUMBER
                insertCall.registerOutParameter(38, java.sql.Types.INTEGER)

                // parm 39 p_curr_error_out  sorlcur_curr_error_out NUMBER
                insertCall.registerOutParameter(39, java.sql.Types.INTEGER)

                // parm 40 p_severity_out  sorlcur_severity_out NUMBER
                insertCall.registerOutParameter(40, java.sql.Types.VARCHAR)

                // parm 41 p_user_id_update  sorlcur_user_id_update VARCHAR2
                insertCall.setString(41, connectInfo.userID)

                // parm 42 p_gapp_seqno  sorlcur_gapp_seqno NUMBER
                if ((this.sorlcur_gapp_seqno == "") || (this.sorlcur_gapp_seqno == null)
                        || (!this.sorlcur_gapp_seqno)) {
                    insertCall.setNull(42, java.sql.Types.INTEGER)
                } else {
                    insertCall.setInt(42, this.sorlcur_gapp_seqno.toInteger())
                }

                // parm 43 p_current_cde  sorlcur_current_cde VARCHAR2
                insertCall.setString(43, this.sorlcur_current_cde)

                if (connectInfo.debugThis) {
                    println "Insert SORLCUR ${this.sorlcur_pidm} ${this.sorlcur_seqno} ${this.sorlcur_lmod_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SORLCUR", 0, 1, 0, 0, 0)
                    seqout = insertCall.getInt(38)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SORLCUR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SORLCUR ${bannerid} ${this.sorlcur_pidm} ${this.sorlcur_seqno} ${this.sorlcur_lmod_code}"
                        println "Problem executing insert for table SORLCUR from ConcurrentCurriculumDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SORLCUR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SORLCUR ${bannerid} ${this.sorlcur_pidm} ${this.sorlcur_seqno} ${this.sorlcur_lmod_code}"
                    println "Problem setting up insert for table SORLCUR from ConcurrentCurriculumDML.groovy: $e.message"
                }
            }

            if (this.sorlcur_lmod_code == outcome) {
                conn.call("call soklcur.p_create_sotvcur(?,?,sb_curriculum_str.f_learner)", [this.sorlcur_pidm.toInteger(),
                          this.sorlcur_term_code  ])
                String lcur = "call soklcur.p_learner_lcur_rolled(?,?,?,?,?,?,?,?,?)"
                CallableStatement lcurCall = this.connectCall.prepareCall(lcur)
                lcurCall.setInt(1, this.sorlcur_pidm.toInteger())
                lcurCall.setString(2, this.sorlcur_degc_code)
                lcurCall.setString(3, this.sorlcur_levl_code)
                lcurCall.setString(4, this.sorlcur_coll_code)
                lcurCall.setString(5, this.sorlcur_program)
                lcurCall.setInt(6, this.sorlcur_seqno.toInteger())
                lcurCall.setString(8, this.sorlcur_camp_code)
                def lcurout
                def priorityout
                lcurCall.registerOutParameter(7, java.sql.Types.VARCHAR)
                lcurCall.registerOutParameter(9, java.sql.Types.INTEGER)
                try {
                    lcurCall.executeUpdate()
                    connectInfo.tableUpdate("SORLCUR", 0, 0, 1, 0, 0)

                }
                catch (e) {
                    connectInfo.tableUpdate("SORLCUR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SORLCUR Rolled ${bannerid} ${this.sorlcur_pidm} ${this.sorlcur_seqno} ${this.sorlcur_lmod_code}"
                        println "Problem executing Update SORLCUR Rolled for table SORLCUR from ConcurrentCurriculumDML.groovy: $e.message"
                    }
                }
                finally {
                    lcurCall.close()
                }

            }
        }
    }
}
