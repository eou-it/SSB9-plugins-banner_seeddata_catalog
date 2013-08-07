/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 *  DML for SHRDGMR
 */
public class HistoryOutcomeDML {
    def bannerid
    def shrdgmr_pidm
    def shrdgmr_seq_no
    def shrdgmr_degs_code
    def shrdgmr_appl_date
    def shrdgmr_grad_date
    def shrdgmr_acyr_code_bulletin
    def shrdgmr_term_code_sturec
    def shrdgmr_term_code_grad
    def shrdgmr_acyr_code
    def shrdgmr_grst_code
    def shrdgmr_fee_ind
    def shrdgmr_fee_date
    def shrdgmr_authorized
    def shrdgmr_term_code_completed
    def shrdgmr_degc_code_dual
    def shrdgmr_levl_code_dual
    def shrdgmr_dept_code_dual
    def shrdgmr_coll_code_dual
    def shrdgmr_majr_code_dual
    def shrdgmr_stsp_key_sequence

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public HistoryOutcomeDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processShrdgmr()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.shrdgmr_pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()

        if (connectInfo.debugThis) {
            println "--------- New XML SHRDGMR record ----------"
            println "${bannerid}    ${apiData.SHRDGMR_SEQ_NO.text()}    ${apiData.SHRDGMR_DEGS_CODE.text()}    ${apiData.SHRDGMR_DEGS_CODE.text()}    "
        }
        this.shrdgmr_seq_no = apiData.SHRDGMR_SEQ_NO.text()
        this.shrdgmr_degs_code = apiData.SHRDGMR_DEGS_CODE.text()
        this.shrdgmr_appl_date = apiData.SHRDGMR_APPL_DATE.text()
        this.shrdgmr_grad_date = apiData.SHRDGMR_GRAD_DATE.text()
        this.shrdgmr_acyr_code_bulletin = apiData.SHRDGMR_ACYR_CODE_BULLETIN.text()
        this.shrdgmr_term_code_sturec = apiData.SHRDGMR_TERM_CODE_STUREC.text()
        this.shrdgmr_term_code_grad = apiData.SHRDGMR_TERM_CODE_GRAD.text()
        this.shrdgmr_acyr_code = apiData.SHRDGMR_ACYR_CODE.text()
        this.shrdgmr_grst_code = apiData.SHRDGMR_GRST_CODE.text()
        this.shrdgmr_fee_ind = apiData.SHRDGMR_FEE_IND.text()
        this.shrdgmr_fee_date = apiData.SHRDGMR_FEE_DATE.text()
        this.shrdgmr_authorized = apiData.SHRDGMR_AUTHORIZED.text()
        this.shrdgmr_term_code_completed = apiData.SHRDGMR_TERM_CODE_COMPLETED.text()
        this.shrdgmr_degc_code_dual = apiData.SHRDGMR_DEGC_CODE_DUAL.text()
        this.shrdgmr_levl_code_dual = apiData.SHRDGMR_LEVL_CODE_DUAL.text()
        this.shrdgmr_dept_code_dual = apiData.SHRDGMR_DEPT_CODE_DUAL.text()
        this.shrdgmr_coll_code_dual = apiData.SHRDGMR_COLL_CODE_DUAL.text()
        this.shrdgmr_majr_code_dual = apiData.SHRDGMR_MAJR_CODE_DUAL.text()
        this.shrdgmr_stsp_key_sequence = apiData.SHRDGMR_STSP_KEY_SEQUENCE.text()

    }


    def processShrdgmr() {
        tableRow = null

        String rowSQL = """select rowid table_row from SHRDGMR
           where shrdgmr_pidm = ?  and shrdgmr_seq_no = ? """
        try {
            conn.eachRow(rowSQL, [this.shrdgmr_pidm, this.shrdgmr_seq_no]) { row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SHRDGMR rowid HistoryOutcomeDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 23
            try {
                String API = "{call  sb_learneroutcome.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  shrdgmr_pidm NUMBER
                if ((this.shrdgmr_pidm == "") || (this.shrdgmr_pidm == null) || (!this.shrdgmr_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.shrdgmr_pidm.toInteger())
                }

                // parm 2 p_seq_no  shrdgmr_seq_no NUMBER
                if ((this.shrdgmr_seq_no == "") || (this.shrdgmr_seq_no == null) || (!this.shrdgmr_seq_no)) { insertCall.setNull(2, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(2, this.shrdgmr_seq_no.toInteger())
                }

                // parm 3 p_degs_code  shrdgmr_degs_code VARCHAR2
                insertCall.setString(3, this.shrdgmr_degs_code)

                // parm 4 p_appl_date  shrdgmr_appl_date DATE
                if ((this.shrdgmr_appl_date == "") || (this.shrdgmr_appl_date == null) ||
                        (!this.shrdgmr_appl_date)) {
                    insertCall.setNull(4, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_appl_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(4, sqlDate)
                }

                // parm 5 p_grad_date  shrdgmr_grad_date DATE
                if ((this.shrdgmr_grad_date == "") || (this.shrdgmr_grad_date == null) ||
                        (!this.shrdgmr_grad_date)) {
                    insertCall.setNull(5, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_grad_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(5, sqlDate)
                }

                // parm 6 p_acyr_code_bulletin  shrdgmr_acyr_code_bulletin VARCHAR2
                insertCall.setString(6, this.shrdgmr_acyr_code_bulletin)

                // parm 7 p_term_code_sturec  shrdgmr_term_code_sturec VARCHAR2
                insertCall.setString(7, this.shrdgmr_term_code_sturec)

                // parm 8 p_term_code_grad  shrdgmr_term_code_grad VARCHAR2
                insertCall.setString(8, this.shrdgmr_term_code_grad)

                // parm 9 p_acyr_code  shrdgmr_acyr_code VARCHAR2
                insertCall.setString(9, this.shrdgmr_acyr_code)

                // parm 10 p_grst_code  shrdgmr_grst_code VARCHAR2
                insertCall.setString(10, this.shrdgmr_grst_code)

                // parm 11 p_fee_ind  shrdgmr_fee_ind VARCHAR2
                insertCall.setString(11, this.shrdgmr_fee_ind)

                // parm 12 p_fee_date  shrdgmr_fee_date DATE
                if ((this.shrdgmr_fee_date == "") || (this.shrdgmr_fee_date == null) ||
                        (!this.shrdgmr_fee_date)) {
                    insertCall.setNull(12, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_fee_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(12, sqlDate)
                }

                // parm 13 p_authorized  shrdgmr_authorized VARCHAR2
                insertCall.setString(13, this.shrdgmr_authorized)

                // parm 14 p_term_code_completed  shrdgmr_term_code_completed VARCHAR2
                insertCall.setString(14, this.shrdgmr_term_code_completed)

                // parm 15 p_degc_code_dual  shrdgmr_degc_code_dual VARCHAR2
                insertCall.setString(15, this.shrdgmr_degc_code_dual)

                // parm 16 p_levl_code_dual  shrdgmr_levl_code_dual VARCHAR2
                insertCall.setString(16, this.shrdgmr_levl_code_dual)

                // parm 17 p_dept_code_dual  shrdgmr_dept_code_dual VARCHAR2
                insertCall.setString(17, this.shrdgmr_dept_code_dual)

                // parm 18 p_coll_code_dual  shrdgmr_coll_code_dual VARCHAR2
                insertCall.setString(18, this.shrdgmr_coll_code_dual)

                // parm 19 p_majr_code_dual  shrdgmr_majr_code_dual VARCHAR2
                insertCall.setString(19, this.shrdgmr_majr_code_dual)

                // parm 20 p_user_id  shrdgmr_user_id VARCHAR2
                insertCall.setString(20, connectInfo.userID)
                // parm 21 p_data_origin  shrdgmr_data_origin VARCHAR2
                insertCall.setString(21, connectInfo.dataOrigin)
                // parm 22 p_rowid_out  shrdgmr_rowid_out VARCHAR2
                insertCall.registerOutParameter(22, java.sql.Types.ROWID)
                // parm 23 p_stsp_key_sequence  shrdgmr_stsp_key_sequence NUMBER
                if ((this.shrdgmr_stsp_key_sequence == "") || (this.shrdgmr_stsp_key_sequence == null) || (!this.shrdgmr_stsp_key_sequence)) { insertCall.setNull(23, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(23, this.shrdgmr_stsp_key_sequence.toInteger())
                }

                if (connectInfo.debugThis) {
                    println "Insert SHRDGMR ${this.shrdgmr_pidm} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SHRDGMR", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SHRDGMR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SHRDGMR ${bannerid} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                        println "Problem executing insert for table SHRDGMR from HistoryOutcomeDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SHRDGMR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SHRDGMR ${bannerid} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                    println "Problem setting up insert for table SHRDGMR from HistoryOutcomeDML.groovy: $e.message"
                }
            }
        } else {  // update the data
            //  parm count is 23
            try {
                String API = "{call  sb_learneroutcome.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  shrdgmr_pidm NUMBER
                if ((this.shrdgmr_pidm == "") || (this.shrdgmr_pidm == null) || (!this.shrdgmr_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.shrdgmr_pidm.toInteger())
                }

                // parm 2 p_seq_no  shrdgmr_seq_no NUMBER
                if ((this.shrdgmr_seq_no == "") || (this.shrdgmr_seq_no == null) || (!this.shrdgmr_seq_no)) { insertCall.setNull(2, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(2, this.shrdgmr_seq_no.toInteger())
                }

                // parm 3 p_degs_code  shrdgmr_degs_code VARCHAR2
                insertCall.setString(3, this.shrdgmr_degs_code)

                // parm 4 p_appl_date  shrdgmr_appl_date DATE
                if ((this.shrdgmr_appl_date == "") || (this.shrdgmr_appl_date == null) || (!this.shrdgmr_appl_date)) { insertCall.setNull(4, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_appl_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(4, sqlDate)
                }

                // parm 5 p_grad_date  shrdgmr_grad_date DATE
                if ((this.shrdgmr_grad_date == "") || (this.shrdgmr_grad_date == null) || (!this.shrdgmr_grad_date)) { insertCall.setNull(5, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_grad_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(5, sqlDate)
                }

                // parm 6 p_acyr_code_bulletin  shrdgmr_acyr_code_bulletin VARCHAR2
                insertCall.setString(6, this.shrdgmr_acyr_code_bulletin)

                // parm 7 p_term_code_sturec  shrdgmr_term_code_sturec VARCHAR2
                insertCall.setString(7, this.shrdgmr_term_code_sturec)

                // parm 8 p_term_code_grad  shrdgmr_term_code_grad VARCHAR2
                insertCall.setString(8, this.shrdgmr_term_code_grad)

                // parm 9 p_acyr_code  shrdgmr_acyr_code VARCHAR2
                insertCall.setString(9, this.shrdgmr_acyr_code)

                // parm 10 p_grst_code  shrdgmr_grst_code VARCHAR2
                insertCall.setString(10, this.shrdgmr_grst_code)

                // parm 11 p_fee_ind  shrdgmr_fee_ind VARCHAR2
                insertCall.setString(11, this.shrdgmr_fee_ind)

                // parm 12 p_fee_date  shrdgmr_fee_date DATE
                if ((this.shrdgmr_fee_date == "") || (this.shrdgmr_fee_date == null) || (!this.shrdgmr_fee_date)) { insertCall.setNull(12, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.shrdgmr_fee_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(12, sqlDate)
                }

                // parm 13 p_authorized  shrdgmr_authorized VARCHAR2
                insertCall.setString(13, this.shrdgmr_authorized)

                // parm 14 p_term_code_completed  shrdgmr_term_code_completed VARCHAR2
                insertCall.setString(14, this.shrdgmr_term_code_completed)

                // parm 15 p_degc_code_dual  shrdgmr_degc_code_dual VARCHAR2
                insertCall.setString(15, this.shrdgmr_degc_code_dual)

                // parm 16 p_levl_code_dual  shrdgmr_levl_code_dual VARCHAR2
                insertCall.setString(16, this.shrdgmr_levl_code_dual)

                // parm 17 p_dept_code_dual  shrdgmr_dept_code_dual VARCHAR2
                insertCall.setString(17, this.shrdgmr_dept_code_dual)

                // parm 18 p_coll_code_dual  shrdgmr_coll_code_dual VARCHAR2
                insertCall.setString(18, this.shrdgmr_coll_code_dual)

                // parm 19 p_majr_code_dual  shrdgmr_majr_code_dual VARCHAR2
                insertCall.setString(19, this.shrdgmr_majr_code_dual)

                // parm 20 p_user_id  shrdgmr_user_id VARCHAR2
                insertCall.setString(20, connectInfo.userID)
                // parm 21 p_data_origin  shrdgmr_data_origin VARCHAR2
                insertCall.setString(21, connectInfo.dataOrigin)
                // parm 22 p_rowid  shrdgmr_rowid VARCHAR2
                insertCall.setRowId(22, tableRow)

                // parm 23 p_stsp_key_sequence  shrdgmr_stsp_key_sequence NUMBER
                if ((this.shrdgmr_stsp_key_sequence == "") || (this.shrdgmr_stsp_key_sequence == null) || (!this.shrdgmr_stsp_key_sequence)) { insertCall.setNull(23, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(23, this.shrdgmr_stsp_key_sequence.toInteger())
                }

                if (connectInfo.debugThis) {
                    println "Update SHRDGMR ${this.shrdgmr_pidm} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SHRDGMR", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SHRDGMR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SHRDGMR ${bannerid} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                        println "Problem executing update for table SHRDGMR from HistoryOutcomeDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SHRDGMR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SHRDGMR ${bannerid} ${this.shrdgmr_seq_no} ${this.shrdgmr_degs_code}"
                    println "Problem setting up update for table SHRDGMR from HistoryOutcomeDML.groovy: $e.message"
                }
            }
        }
    }
}
