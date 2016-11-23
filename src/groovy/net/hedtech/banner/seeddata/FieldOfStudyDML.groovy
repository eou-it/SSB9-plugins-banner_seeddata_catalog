/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement
import java.text.*
/**
 *  DML for SORLFOS
 */
public class FieldOfStudyDML {
    def bannerid
    def sorlfos_pidm
    def sorlfos_lcur_seqno
    def sorlfos_seqno
    def sorlfos_lfst_code
    def sorlfos_term_code
    def sorlfos_priority_no
    def sorlfos_csts_code
    def sorlfos_cact_code
    def sorlfos_majr_code
    def sorlfos_term_code_ctlg
    def sorlfos_term_code_end
    def sorlfos_dept_code
    def sorlfos_majr_code_attach
    def sorlfos_lfos_rule
    def sorlfos_conc_attach_rule
    def sorlfos_start_date
    def sorlfos_end_date
    def sorlfos_tmst_code
    def sorlfos_rolled_seqno
    def sorlfos_user_id_update
    def sorlfos_activity_date_update
    def sorlfos_current_cde
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public FieldOfStudyDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSorlfos()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.sorlfos_pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()

        if (connectInfo.debugThis) {
            println "--------- New XML SORLFOS record ----------"
            println "${apiData.BANNERID.text()}    ${apiData.SORLFOS_LCUR_SEQNO.text()}    ${apiData.SORLFOS_SEQNO.text()}    ${apiData.SORLFOS_LFST_CODE.text()}    "
        }

        this.sorlfos_lcur_seqno = apiData.SORLFOS_LCUR_SEQNO.text()
        this.sorlfos_seqno = apiData.SORLFOS_SEQNO.text()
        this.sorlfos_lfst_code = apiData.SORLFOS_LFST_CODE.text()
        this.sorlfos_term_code = apiData.SORLFOS_TERM_CODE.text()
        this.sorlfos_priority_no = apiData.SORLFOS_PRIORITY_NO.text()
        this.sorlfos_csts_code = apiData.SORLFOS_CSTS_CODE.text()
        this.sorlfos_cact_code = apiData.SORLFOS_CACT_CODE.text()
        this.sorlfos_majr_code = apiData.SORLFOS_MAJR_CODE.text()
        this.sorlfos_term_code_ctlg = apiData.SORLFOS_TERM_CODE_CTLG.text()
        this.sorlfos_term_code_end = apiData.SORLFOS_TERM_CODE_END.text()
        this.sorlfos_dept_code = apiData.SORLFOS_DEPT_CODE.text()
        this.sorlfos_majr_code_attach = apiData.SORLFOS_MAJR_CODE_ATTACH.text()
        this.sorlfos_lfos_rule = apiData.SORLFOS_LFOS_RULE.text()
        this.sorlfos_conc_attach_rule = apiData.SORLFOS_CONC_ATTACH_RULE.text()
        this.sorlfos_start_date = apiData.SORLFOS_START_DATE.text()
        this.sorlfos_end_date = apiData.SORLFOS_END_DATE.text()
        this.sorlfos_tmst_code = apiData.SORLFOS_TMST_CODE.text()
        this.sorlfos_rolled_seqno = apiData.SORLFOS_ROLLED_SEQNO.text()
        this.sorlfos_user_id_update = apiData.SORLFOS_USER_ID_UPDATE.text()
        this.sorlfos_activity_date_update = apiData.SORLFOS_ACTIVITY_DATE_UPDATE.text()
        this.sorlfos_current_cde = apiData.SORLFOS_CURRENT_CDE.text()

    }


    def processSorlfos() {
//        conn.executeUpdate("update sobctrl set sobctrl_curr_rule_ind = 'Y'")
//        conn.execute "{ call gb_common.p_commit() }"
        tableRow = null
        String rowSQL = """select rowid table_row from SORLFOS
           where sorlfos_pidm = ? and sorlfos_lcur_seqno = ? and sorlfos_seqno = ? """
        try {
            conn.eachRow(rowSQL, [this.sorlfos_pidm, this.sorlfos_lcur_seqno, this.sorlfos_seqno]) { row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SORLFOS rowid FieldOfStudyDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 28
            try {
                String API = "{call  sb_fieldofstudy.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sorlfos_pidm NUMBER
                if ((this.sorlfos_pidm == "") || (this.sorlfos_pidm == null) || (!this.sorlfos_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.sorlfos_pidm.toInteger())
                }

                // parm 2 p_lcur_seqno  sorlfos_lcur_seqno NUMBER
                if ((this.sorlfos_lcur_seqno == "") || (this.sorlfos_lcur_seqno == null) || (!this.sorlfos_lcur_seqno)) { insertCall.setNull(2, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(2, this.sorlfos_lcur_seqno.toInteger())
                }

                // parm 3 p_seqno  sorlfos_seqno NUMBER
                if ((this.sorlfos_seqno == "") || (this.sorlfos_seqno == null)
                        || (!this.sorlfos_seqno)) {
                    insertCall.setNull(3, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(3, this.sorlfos_seqno.toInteger())
                }

                // parm 4 p_lfst_code  sorlfos_lfst_code VARCHAR2
                insertCall.setString(4, this.sorlfos_lfst_code)

                // parm 5 p_term_code  sorlfos_term_code VARCHAR2
                insertCall.setString(5, this.sorlfos_term_code)

                // parm 6 p_priority_no  sorlfos_priority_no NUMBER
                if ((this.sorlfos_priority_no == "") || (this.sorlfos_priority_no == null)
                        || (!this.sorlfos_priority_no)) {
                    insertCall.setNull(6, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(6, this.sorlfos_priority_no.toInteger())
                }

                // parm 7 p_csts_code  sorlfos_csts_code VARCHAR2
                insertCall.setString(7, this.sorlfos_csts_code)

                // parm 8 p_cact_code  sorlfos_cact_code VARCHAR2
                insertCall.setString(8, this.sorlfos_cact_code)

                // parm 9 p_data_origin  sorlfos_data_origin VARCHAR2
                insertCall.setString(9, connectInfo.dataOrigin)
                // parm 10 p_user_id  sorlfos_user_id VARCHAR2
                insertCall.setString(10, connectInfo.userID)
                // parm 11 p_majr_code  sorlfos_majr_code VARCHAR2
                insertCall.setString(11, this.sorlfos_majr_code)

                // parm 12 p_term_code_ctlg  sorlfos_term_code_ctlg VARCHAR2
                insertCall.setString(12, this.sorlfos_term_code_ctlg)

                // parm 13 p_term_code_end  sorlfos_term_code_end VARCHAR2
                insertCall.setString(13, this.sorlfos_term_code_end)

                // parm 14 p_dept_code  sorlfos_dept_code VARCHAR2
                insertCall.setString(14, this.sorlfos_dept_code)

                // parm 15 p_lfos_rule  sorlfos_lfos_rule NUMBER
                if ((this.sorlfos_lfos_rule == "") || (this.sorlfos_lfos_rule == null)
                        || (!this.sorlfos_lfos_rule)) {
                    insertCall.setNull(15, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(15, this.sorlfos_lfos_rule.toInteger())
                }

                // parm 16 p_conc_attach_rule  sorlfos_conc_attach_rule NUMBER
                if ((this.sorlfos_conc_attach_rule == "") || (this.sorlfos_conc_attach_rule == null)
                        || (!this.sorlfos_conc_attach_rule)) {
                    insertCall.setNull(16, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(16, this.sorlfos_conc_attach_rule.toInteger())
                }

                // parm 17 p_start_date  sorlfos_start_date DATE
                if ((this.sorlfos_start_date == "") || (this.sorlfos_start_date == null) ||
                        (!this.sorlfos_start_date)) {
                    insertCall.setNull(17, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sorlfos_start_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(17, sqlDate)
                }

                // parm 18 p_end_date  sorlfos_end_date DATE
                if ((this.sorlfos_end_date == "") || (this.sorlfos_end_date == null) ||
                        (!this.sorlfos_end_date)) {
                    insertCall.setNull(18, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sorlfos_end_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(18, sqlDate)
                }

                // parm 19 p_tmst_code  sorlfos_tmst_code VARCHAR2
                insertCall.setString(19, this.sorlfos_tmst_code)

                // parm 20 p_majr_code_attach  sorlfos_majr_code_attach VARCHAR2
                insertCall.setString(20, this.sorlfos_majr_code_attach)

                // parm 21 p_rolled_seqno  sorlfos_rolled_seqno NUMBER
//                if ((this.sorlfos_rolled_seqno == "") || (this.sorlfos_rolled_seqno == null)
//                        || (!this.sorlfos_rolled_seqno)) {
//                    insertCall.setNull(21, java.sql.Types.INTEGER) }
//                else {
//                    insertCall.setInt(21, this.sorlfos_rolled_seqno.toInteger())
//                }
               insertCall.setNull(21, java.sql.Types.INTEGER)
                // parm 22 p_override_severity  sorlfos_override_severity NUMBER
                insertCall.setString(22, "N")

                // parm 23 p_rowid_out  sorlfos_rowid_out NUMBER
                insertCall.registerOutParameter(23, java.sql.Types.ROWID)
                // parm 24 p_curr_error_out  sorlfos_curr_error_out NUMBER

                insertCall.registerOutParameter(24, java.sql.Types.INTEGER)

                // parm 25 p_severity_out  sorlfos_severity_out NUMBER

                insertCall.registerOutParameter(25, java.sql.Types.VARCHAR)

                // parm 26 p_lfos_seqno_out  sorlfos_lfos_seqno_out NUMBER

                insertCall.registerOutParameter(26, java.sql.Types.INTEGER)

                // parm 27 p_user_id_update  sorlfos_user_id_update VARCHAR2
                insertCall.setString(27, this.sorlfos_user_id_update)

                // parm 28 p_current_cde  sorlfos_current_cde VARCHAR2
                insertCall.setString(28, this.sorlfos_current_cde)

                if (connectInfo.debugThis) {
                    println "Insert SORLFOS ${bannerid} ${this.sorlfos_lcur_seqno} ${this.sorlfos_seqno}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SORLFOS", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SORLFOS", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SORLFOS ${bannerid} ${this.sorlfos_lcur_seqno} ${this.sorlfos_seqno}"
                        println "Problem executing insert for table SORLFOS from FieldOfStudyDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SORLFOS", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SORLFOS ${bannerid} ${this.sorlfos_lcur_seqno} ${this.sorlfos_seqno}"
                    println "Problem setting up insert for table SORLFOS from FieldOfStudyDML.groovy: $e.message"
                }
            }
        }
    }
}
