/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement
import java.text.*
/**
 *  DML for SARADAP
 */
public class AdmissionsApplicationDML {
    def bannerid
    def saradap_pidm
    def saradap_term_code_entry
    def saradap_appl_no
    def saradap_appl_date
    def saradap_apst_code
    def saradap_apst_date
    def saradap_maint_ind
    def saradap_admt_code
    def saradap_styp_code
    def saradap_site_code
    def saradap_resd_code
    def saradap_full_part_ind
    def saradap_sess_code
    def saradap_wrsn_code
    def saradap_intv_code
    def saradap_fee_ind
    def saradap_fee_date
    def saradap_rate_code
    def saradap_egol_code
    def saradap_edlv_code
    def saradap_sbgi_code
    def saradap_recr_code
    def saradap_rtyp_code
    def saradap_web_acct_misc_ind
    def saradap_web_cashier_user
    def saradap_web_trans_no
    def saradap_web_amount
    def saradap_web_receipt_number
    def saradap_waiv_code
    def saradap_appl_preference
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public AdmissionsApplicationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSaradap()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.saradap_pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()

        if (connectInfo.debugThis) {
            println "--------- New XML SARADAP record ----------"
            println "${bannerid}   ${apiData.SARADAP_TERM_CODE_ENTRY.text()}  ${apiData.SARADAP_APPL_NO.text()}    ${apiData.SARADAP_LEVL_CODE.text()}    "
        }

        this.saradap_term_code_entry = apiData.SARADAP_TERM_CODE_ENTRY.text()
        this.saradap_appl_no = apiData.SARADAP_APPL_NO.text()
        this.saradap_appl_date = apiData.SARADAP_APPL_DATE.text()
        this.saradap_apst_code = apiData.SARADAP_APST_CODE.text()
        this.saradap_apst_date = apiData.SARADAP_APST_DATE.text()
        this.saradap_maint_ind = apiData.SARADAP_MAINT_IND.text()
        this.saradap_admt_code = apiData.SARADAP_ADMT_CODE.text()
        this.saradap_styp_code = apiData.SARADAP_STYP_CODE.text()
        this.saradap_site_code = apiData.SARADAP_SITE_CODE.text()
        this.saradap_resd_code = apiData.SARADAP_RESD_CODE.text()
        this.saradap_full_part_ind = apiData.SARADAP_FULL_PART_IND.text()
        this.saradap_sess_code = apiData.SARADAP_SESS_CODE.text()
        this.saradap_wrsn_code = apiData.SARADAP_WRSN_CODE.text()
        this.saradap_intv_code = apiData.SARADAP_INTV_CODE.text()
        this.saradap_fee_ind = apiData.SARADAP_FEE_IND.text()
        this.saradap_fee_date = apiData.SARADAP_FEE_DATE.text()
        this.saradap_rate_code = apiData.SARADAP_RATE_CODE.text()
        this.saradap_egol_code = apiData.SARADAP_EGOL_CODE.text()
        this.saradap_edlv_code = apiData.SARADAP_EDLV_CODE.text()
        this.saradap_sbgi_code = apiData.SARADAP_SBGI_CODE.text()
        this.saradap_recr_code = apiData.SARADAP_RECR_CODE.text()
        this.saradap_rtyp_code = apiData.SARADAP_RTYP_CODE.text()
        this.saradap_web_acct_misc_ind = apiData.SARADAP_WEB_ACCT_MISC_IND.text()
        this.saradap_web_cashier_user = apiData.SARADAP_WEB_CASHIER_USER.text()
        this.saradap_web_trans_no = apiData.SARADAP_WEB_TRANS_NO.text()
        this.saradap_web_amount = apiData.SARADAP_WEB_AMOUNT.text()
        this.saradap_web_receipt_number = apiData.SARADAP_WEB_RECEIPT_NUMBER.text()
        this.saradap_waiv_code = apiData.SARADAP_WAIV_CODE.text()
        this.saradap_appl_preference = apiData.SARADAP_APPL_PREFERENCE.text()

    }


    def processSaradap() {
        tableRow = null
        String rowSQL = """select rowid table_row from SARADAP
           where saradap_pidm = ? and saradap_appl_no = ? """
        try {
           tableRow =   conn.firstRow(rowSQL, [connectInfo.saveStudentPidm, this.saradap_appl_no])?.TABLE_ROW
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SARADAP rowid AdmissionsApplicationDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 33
            try {
                String API = "{call  sb_admissionsapplication.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  saradap_pidm NUMBER
                if ((connectInfo.saveStudentPidm == "") || (connectInfo.saveStudentPidm == null) || (!connectInfo.saveStudentPidm))
                    { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, connectInfo.saveStudentPidm.toInteger())
                }

                // parm 2 p_term_code_entry  saradap_term_code_entry VARCHAR2
                insertCall.setString(2, this.saradap_term_code_entry)

                // parm 3 p_appl_no_inout  saradap_appl_no_inout VARCHAR2
             
                insertCall.registerOutParameter(3, java.sql.Types.INTEGER)
                if (this.saradap_appl_no) {insertCall.setInt(3, this.saradap_appl_no.toInteger()) }
                else {insertCall.setNull(3, java.sql.Types.INTEGER) }

                // parm 4 p_appl_date  saradap_appl_date DATE
                if ((this.saradap_appl_date == "") || (this.saradap_appl_date == null) ||
                        (!this.saradap_appl_date)) {
                    insertCall.setNull(4, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.saradap_appl_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(4, sqlDate)
                }

                // parm 5 p_apst_code  saradap_apst_code VARCHAR2
                insertCall.setString(5, this.saradap_apst_code)

                // parm 6 p_apst_date  saradap_apst_date DATE
                if ((this.saradap_apst_date == "") || (this.saradap_apst_date == null) ||
                        (!this.saradap_apst_date)) {
                    insertCall.setNull(6, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.saradap_apst_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(6, sqlDate)
                }

                // parm 7 p_maint_ind  saradap_maint_ind VARCHAR2
                insertCall.setString(7, this.saradap_maint_ind)

                // parm 8 p_admt_code  saradap_admt_code VARCHAR2
                insertCall.setString(8, this.saradap_admt_code)

                // parm 9 p_styp_code  saradap_styp_code VARCHAR2
                insertCall.setString(9, this.saradap_styp_code)

                // parm 10 p_site_code  saradap_site_code VARCHAR2
                insertCall.setString(10, this.saradap_site_code)

                // parm 11 p_resd_code  saradap_resd_code VARCHAR2
                insertCall.setString(11, this.saradap_resd_code)

                // parm 12 p_full_part_ind  saradap_full_part_ind VARCHAR2
                insertCall.setString(12, this.saradap_full_part_ind)

                // parm 13 p_sess_code  saradap_sess_code VARCHAR2
                insertCall.setString(13, this.saradap_sess_code)

                // parm 14 p_wrsn_code  saradap_wrsn_code VARCHAR2
                insertCall.setString(14, this.saradap_wrsn_code)

                // parm 15 p_intv_code  saradap_intv_code VARCHAR2
                insertCall.setString(15, this.saradap_intv_code)

                // parm 16 p_fee_ind  saradap_fee_ind VARCHAR2
                insertCall.setString(16, this.saradap_fee_ind)

                // parm 17 p_fee_date  saradap_fee_date DATE
                if ((this.saradap_fee_date == "") || (this.saradap_fee_date == null) ||
                        (!this.saradap_fee_date)) {
                    insertCall.setNull(17, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.saradap_fee_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(17, sqlDate)
                }

                // parm 18 p_rate_code  saradap_rate_code VARCHAR2
                insertCall.setString(18, this.saradap_rate_code)

                // parm 19 p_egol_code  saradap_egol_code VARCHAR2
                insertCall.setString(19, this.saradap_egol_code)

                // parm 20 p_edlv_code  saradap_edlv_code VARCHAR2
                insertCall.setString(20, this.saradap_edlv_code)

                // parm 21 p_sbgi_code  saradap_sbgi_code VARCHAR2
                insertCall.setString(21, this.saradap_sbgi_code)

                // parm 22 p_recr_code  saradap_recr_code VARCHAR2
                insertCall.setString(22, this.saradap_recr_code)

                // parm 23 p_rtyp_code  saradap_rtyp_code VARCHAR2
                insertCall.setString(23, this.saradap_rtyp_code)

                // parm 24 p_web_acct_misc_ind  saradap_web_acct_misc_ind VARCHAR2
                insertCall.setString(24, this.saradap_web_acct_misc_ind)

                // parm 25 p_web_cashier_user  saradap_web_cashier_user VARCHAR2
                insertCall.setString(25, this.saradap_web_cashier_user)

                // parm 26 p_web_trans_no  saradap_web_trans_no NUMBER
                if ((this.saradap_web_trans_no == "") || (this.saradap_web_trans_no == null) || (!this.saradap_web_trans_no)) { insertCall.setNull(26, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(26, this.saradap_web_trans_no.toInteger())
                }

                // parm 27 p_web_amount  saradap_web_amount NUMBER
                if ((this.saradap_web_amount == "") || (this.saradap_web_amount == null) || (!this.saradap_web_amount)) {
                    insertCall.setNull(27, java.sql.Types.DOUBLE)
                }
                else {
                    insertCall.setDouble(27, this.saradap_web_amount.toDouble())
                }

                // parm 28 p_web_receipt_number  saradap_web_receipt_number NUMBER
                if ((this.saradap_web_receipt_number == "") || (this.saradap_web_receipt_number == null) || (!this.saradap_web_receipt_number)) { insertCall.setNull(28, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(28, this.saradap_web_receipt_number.toInteger())
                }

                // parm 29 p_waiv_code  saradap_waiv_code VARCHAR2
                insertCall.setString(29, this.saradap_waiv_code)

                // parm 30 p_data_origin  saradap_data_origin VARCHAR2
                insertCall.setString(30, connectInfo.dataOrigin)
                // parm 31 p_user_id  saradap_user_id VARCHAR2
                insertCall.setString(31, connectInfo.userID)
                // parm 32 p_rowid_out  saradap_rowid_out VARCHAR2
                insertCall.registerOutParameter(32, java.sql.Types.ROWID)
                // parm 33 p_appl_preference  saradap_appl_preference NUMBER
                if ((this.saradap_appl_preference == "") ||
                        (this.saradap_appl_preference == null) ||
                        (!this.saradap_appl_preference)) {
                    insertCall.setNull(33, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(33, this.saradap_appl_preference.toInteger())
                }

                if (connectInfo.debugThis) {
                    println "Insert SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SARADAP", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SARADAP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                        println "Problem executing insert for table SARADAP from AdmissionsApplicationDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SARADAP", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                    println "Problem setting up insert for table SARADAP from AdmissionsApplicationDML.groovy: $e.message"
                }
            }
        } else {  // update the data
            //  parm count is 33
            try {
                String API = "{call  sb_admissionsapplication.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  saradap_pidm NUMBER
                if ((connectInfo.saveStudentPidm == "") || (connectInfo.saveStudentPidm == null)
                        || (!connectInfo.saveStudentPidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, connectInfo.saveStudentPidm.toInteger())
                }

                // parm 2 p_term_code_entry  saradap_term_code_entry VARCHAR2
                insertCall.setString(2, this.saradap_term_code_entry)

                // parm 3 p_appl_no  saradap_appl_no NUMBER
                if ((this.saradap_appl_no == "") || (this.saradap_appl_no == null)
                        || (!this.saradap_appl_no)) { insertCall.setNull(3, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(3, this.saradap_appl_no.toInteger())
                }

                // parm 4 p_appl_date  saradap_appl_date DATE
                if ((this.saradap_appl_date == "") || (this.saradap_appl_date == null)
                        || (!this.saradap_appl_date)) { insertCall.setNull(4, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.saradap_appl_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(4, sqlDate)
                }

                // parm 5 p_apst_code  saradap_apst_code VARCHAR2
                insertCall.setString(5, this.saradap_apst_code)

                // parm 6 p_apst_date  saradap_apst_date DATE
                if ((this.saradap_apst_date == "") || (this.saradap_apst_date == null) || (!this.saradap_apst_date)) { insertCall.setNull(6, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.saradap_apst_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(6, sqlDate)
                }

                // parm 7 p_maint_ind  saradap_maint_ind VARCHAR2
                insertCall.setString(7, this.saradap_maint_ind)

                // parm 8 p_admt_code  saradap_admt_code VARCHAR2
                insertCall.setString(8, this.saradap_admt_code)

                // parm 9 p_styp_code  saradap_styp_code VARCHAR2
                insertCall.setString(9, this.saradap_styp_code)

                // parm 10 p_site_code  saradap_site_code VARCHAR2
                insertCall.setString(10, this.saradap_site_code)

                // parm 11 p_resd_code  saradap_resd_code VARCHAR2
                insertCall.setString(11, this.saradap_resd_code)

                // parm 12 p_full_part_ind  saradap_full_part_ind VARCHAR2
                insertCall.setString(12, this.saradap_full_part_ind)

                // parm 13 p_sess_code  saradap_sess_code VARCHAR2
                insertCall.setString(13, this.saradap_sess_code)

                // parm 14 p_wrsn_code  saradap_wrsn_code VARCHAR2
                insertCall.setString(14, this.saradap_wrsn_code)

                // parm 15 p_intv_code  saradap_intv_code VARCHAR2
                insertCall.setString(15, this.saradap_intv_code)

                // parm 16 p_fee_ind  saradap_fee_ind VARCHAR2
                insertCall.setString(16, this.saradap_fee_ind)

                // parm 17 p_fee_date  saradap_fee_date DATE
                if ((this.saradap_fee_date == "") || (this.saradap_fee_date == null)
                        || (!this.saradap_fee_date)) {
                    insertCall.setNull(17, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.saradap_fee_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(17, sqlDate)
                }

                // parm 18 p_rate_code  saradap_rate_code VARCHAR2
                insertCall.setString(18, this.saradap_rate_code)

                // parm 19 p_egol_code  saradap_egol_code VARCHAR2
                insertCall.setString(19, this.saradap_egol_code)

                // parm 20 p_edlv_code  saradap_edlv_code VARCHAR2
                insertCall.setString(20, this.saradap_edlv_code)

                // parm 21 p_sbgi_code  saradap_sbgi_code VARCHAR2
                insertCall.setString(21, this.saradap_sbgi_code)

                // parm 22 p_recr_code  saradap_recr_code VARCHAR2
                insertCall.setString(22, this.saradap_recr_code)

                // parm 23 p_rtyp_code  saradap_rtyp_code VARCHAR2
                insertCall.setString(23, this.saradap_rtyp_code)

                // parm 24 p_web_acct_misc_ind  saradap_web_acct_misc_ind VARCHAR2
                insertCall.setString(24, this.saradap_web_acct_misc_ind)

                // parm 25 p_web_cashier_user  saradap_web_cashier_user VARCHAR2
                insertCall.setString(25, this.saradap_web_cashier_user)

                // parm 26 p_web_trans_no  saradap_web_trans_no NUMBER
                if ((this.saradap_web_trans_no == "") || (this.saradap_web_trans_no == null)
                        || (!this.saradap_web_trans_no)) { insertCall.setNull(26, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(26, this.saradap_web_trans_no.toInteger())
                }

                // parm 27 p_web_amount  saradap_web_amount NUMBER
                if ((this.saradap_web_amount == "") || (this.saradap_web_amount == null)
                        || (!this.saradap_web_amount)) { insertCall.setNull(27, java.sql.Types.DOUBLE) }
                else {
                    insertCall.setDouble(27, this.saradap_web_amount.toDouble())
                }

                // parm 28 p_web_receipt_number  saradap_web_receipt_number NUMBER
                if ((this.saradap_web_receipt_number == "") || (this.saradap_web_receipt_number == null)
                        || (!this.saradap_web_receipt_number)) { insertCall.setNull(28, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(28, this.saradap_web_receipt_number.toInteger())
                }

                // parm 29 p_waiv_code  saradap_waiv_code VARCHAR2
                insertCall.setString(29, this.saradap_waiv_code)

                // parm 30 p_data_origin  saradap_data_origin VARCHAR2
                insertCall.setString(30, connectInfo.dataOrigin)
                // parm 31 p_user_id  saradap_user_id VARCHAR2
                insertCall.setString(31, connectInfo.userID)
                // parm 32 p_rowid  saradap_rowid VARCHAR2
                insertCall.setNull(32, java.sql.Types.ROWID) 
                // parm 33 p_appl_preference  saradap_appl_preference NUMBER
                if ((this.saradap_appl_preference == "") || (this.saradap_appl_preference == null)
                        || (!this.saradap_appl_preference)) { insertCall.setNull(33, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(33, this.saradap_appl_preference.toInteger())
                }

                if (connectInfo.debugThis) {
                    println "Update SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SARADAP", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SARADAP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                        println "Problem executing update for table SARADAP from AdmissionsApplicationDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SARADAP", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SARADAP ${bannerid} ${this.saradap_term_code_entry} ${this.saradap_appl_no}"
                    println "Problem setting up update for table SARADAP from AdmissionsApplicationDML.groovy: $e.message"
                }
            }
        }
    }
}
