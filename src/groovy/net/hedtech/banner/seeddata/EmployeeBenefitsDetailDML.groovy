/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by apoliski on 6/6/2016.
 */
class EmployeeBenefitsDetailDML {
    def bannerid
    def w4_signed_bannerid
    def pdrdedn_pidm
    def pdrdedn_bdca_code
    def pdrdedn_effective_date
    def pdrdedn_status
    def pdrdedn_ref_no
    def pdrdedn_amount1
    def pdrdedn_amount2
    def pdrdedn_amount3
    def pdrdedn_amount4
    def pdrdedn_opt_code1
    def pdrdedn_opt_code2
    def pdrdedn_opt_code3
    def pdrdedn_opt_code4
    def pdrdedn_opt_code5
    def pdrdedn_activity_date
    def pdrdedn_coverage_date
    def pdrdedn_bdcl_code
    def pdrdedn_w4_name_change_ind
    def pdrdedn_w4_signed_pidm
    def pdrdedn_w4_signed_date
    def pdrdedn_lockin_letter_status
    def pdrdedn_lockin_letter_date
    def pdrdedn_lockin_fsta_fil_st
    def pdrdedn_lockin_withhold_allow
    def pdrdedn_comment
    def pdrdedn_comment_date
    def pdrdedn_comment_user_id
    def pdrdedn_user_id
    def pdrdedn_data_origin
    def pdrdedn_brea_code
    def pdrdedn_event_date
    def pdrdedn_surrogate_id
    def pdrdedn_version
    def pdrdedn_vpdi_code
    def pdrdedn_1042s_limit_ben_cde

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    def PIDM
    def W4_SIGNED_PIDM

    public EmployeeBenefitsDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }
    public EmployeeBenefitsDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployeeBenefitsDetail()
    }

    def parseXmlData() {
        def pdrdedn = new XmlParser().parseText(xmlData)
        this.bannerid = pdrdedn.BANNERID
        this.w4_signed_bannerid = pdrdedn.W4_SIGNED_BANNERID
        this.pdrdedn_bdca_code = pdrdedn.PDRDEDN_BDCA_CODE.text()
        this.pdrdedn_effective_date = pdrdedn.PDRDEDN_EFFECTIVE_DATE.text()
        this.pdrdedn_status = pdrdedn.PDRDEDN_STATUS.text()
        this.pdrdedn_ref_no = pdrdedn.PDRDEDN_REF_NO.text()
        this.pdrdedn_amount1 = pdrdedn.PDRDEDN_AMOUNT1.text()
        this.pdrdedn_amount2 = pdrdedn.PDRDEDN_AMOUNT2.text()
        this.pdrdedn_amount3 = pdrdedn.PDRDEDN_AMOUNT3.text()
        this.pdrdedn_amount4 = pdrdedn.PDRDEDN_AMOUNT4.text()
        this.pdrdedn_opt_code1 = pdrdedn.PDRDEDN_OPT_CODE1.text()
        this.pdrdedn_opt_code2 = pdrdedn.PDRDEDN_OPT_CODE2.text()
        this.pdrdedn_opt_code3 = pdrdedn.PDRDEDN_OPT_CODE3.text()
        this.pdrdedn_opt_code4 = pdrdedn.PDRDEDN_OPT_CODE4.text()
        this.pdrdedn_opt_code5 = pdrdedn.PDRDEDN_OPT_CODE5.text()
        this.pdrdedn_activity_date = pdrdedn.PDRDEDN_ACTIVITY_DATE.text()
        this.pdrdedn_coverage_date = pdrdedn.PDRDEDN_COVERAGE_DATE.text()
        this.pdrdedn_bdcl_code = pdrdedn.PDRDEDN_BDCL_CODE.text()
        this.pdrdedn_w4_name_change_ind = pdrdedn.PDRDEDN_W4_NAME_CHANGE_IND.text()
        this.pdrdedn_w4_signed_pidm = pdrdedn.PDRDEDN_W4_SGNED_PIDM.text()
        this.pdrdedn_w4_signed_date = pdrdedn.PDRDEDN_W4_SIGNED_DATE.text()
        this.pdrdedn_lockin_letter_status = pdrdedn.PDRDEDN_LOCKIN_LETTER_STATUS.text()
        this.pdrdedn_lockin_letter_date = pdrdedn.PDRDEDN_LOCKIN_LETTER_DATE.text()
        this.pdrdedn_lockin_fsta_fil_st = pdrdedn.PDRDEDN_LOCKIN_FSTA_FIL_ST.text()
        this.pdrdedn_lockin_withhold_allow = pdrdedn.PDRDEDN_LOCKIN_WITHHOLD_ALLOW.text()
        this.pdrdedn_comment = pdrdedn.PDRDEDN_COMMENT.text()
        this.pdrdedn_comment_date = pdrdedn.PDRDEDN_COMMENT_DATE.text()
        this.pdrdedn_comment_user_id = pdrdedn.PDRDEDN_COMMENT_USER_ID.text()
        this.pdrdedn_user_id = pdrdedn.PDRDEDN_USER_ID.text()
        this.pdrdedn_data_origin = pdrdedn.PDRDEDN_DATA_ORIGIN.text()
        this.pdrdedn_brea_code = pdrdedn.PDRDEDN_BREA_CODE.text()
        this.pdrdedn_event_date = pdrdedn.PDRDEDN_EVENT_DATE.text()
        this.pdrdedn_surrogate_id = pdrdedn.PDRDEDN_SURROGATE_ID.text()
        this.pdrdedn_version = pdrdedn.PDRDEDN_VERSION.text()
        this.pdrdedn_vpdi_code = pdrdedn.PDRDEDN_VPDI_CODE.text()
        this.pdrdedn_1042s_limit_ben_cde = pdrdedn.PDRDEDN_1042S_LIMIT_BEN_CDE.text()
    }

    def processEmployeeBenefitsDetail() {
        PIDM = null
        W4_SIGNED_PIDM = null
        String pidmsql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeBenefitsDetailDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }
        if (this.w4_signed_bannerid) {
            String w4pidmsql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
            try {
                this.conn.eachRow(w4pidmsql, [this.w4_signed_bannerid.text()]) { t2row ->
                    W4_SIGNED_PIDM = t2row.spriden_pidm
                }
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                   println "Could not select W4 ID in EmployeeBenefitsDetailDML,  ${this.w4_signed_bannerid.text()}  from SPRIDEN. $e.message"
                }
            }
        }
        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (PIDM) {
            def findY = ""
            String findRow = """select 'Y' pdrdedn_find from pdrdedn where pdrdedn_pidm = ?
                                and pdrdedn_bdca_code  = ?
                                and trunc(pdrdedn_effective_date) = trunc(to_date(?,'mm/dd/yyyy'))"""
            try {
                conn.eachRow(findRow, [PIDM, this.pdrdedn_bdca_code, this.pdrdedn_effective_date]) { row ->
                    findY = row.pdrdedn_find
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("PDRDEDN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "${findRow}"
                    println "Problem with select for table PDRDEDN from EmployeeBenefitsDetailDML.groovy: $e.message"
                }
            }
            if (!findY) {
                try {
                    String API = "{call pb_deduction_detail.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                    CallableStatement insertCall = this.connectCall.prepareCall(API)

                    insertCall.setInt(1, this.PIDM.toInteger())
                    insertCall.setString(2, this.pdrdedn_bdca_code)

                    if ((this.pdrdedn_effective_date == "") || (this.pdrdedn_effective_date == null) ||
                            (!this.pdrdedn_effective_date)) {
                        insertCall.setNull(3, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_effective_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(3, sqlDate)
                    }

                    insertCall.setString(4, this.pdrdedn_status)
                    insertCall.setString(5, this.pdrdedn_ref_no)

                    if ((this.pdrdedn_amount1 == "") || (this.pdrdedn_amount1 == null) ||
                            (!this.pdrdedn_amount1)) {
                        insertCall.setNull(6, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(6, this.pdrdedn_amount1.toDouble())
                    }
                    if ((this.pdrdedn_amount2 == "") || (this.pdrdedn_amount2 == null) ||
                            (!this.pdrdedn_amount2)) {
                        insertCall.setNull(7, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(7, this.pdrdedn_amount2.toDouble())
                    }
                    if ((this.pdrdedn_amount3 == "") || (this.pdrdedn_amount3 == null) ||
                            (!this.pdrdedn_amount3)) {
                        insertCall.setNull(8, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(8, this.pdrdedn_amount3.toDouble())
                    }
                    if ((this.pdrdedn_amount4 == "") || (this.pdrdedn_amount4 == null) ||
                            (!this.pdrdedn_amount4)) {
                        insertCall.setNull(9, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(9, this.pdrdedn_amount4.toDouble())
                    }

                    insertCall.setString(10, this.pdrdedn_opt_code1)
                    insertCall.setString(11, this.pdrdedn_opt_code2)
                    insertCall.setString(12, this.pdrdedn_opt_code3)
                    insertCall.setString(13, this.pdrdedn_opt_code4)
                    insertCall.setString(14, this.pdrdedn_opt_code5)

                    if ((this.pdrdedn_coverage_date == "") || (this.pdrdedn_coverage_date == null) ||
                            (!this.pdrdedn_coverage_date)) {
                        insertCall.setNull(15, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_coverage_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(15, sqlDate)
                    }

                    insertCall.setString(16, this.pdrdedn_bdcl_code)

                    // W-4 info
                    insertCall.setString(17, this.pdrdedn_w4_name_change_ind)
                    if ((this.W4_SIGNED_PIDM == "") || (this.W4_SIGNED_PIDM == null) ||
                            (!this.W4_SIGNED_PIDM)) {
                        insertCall.setNull(18, java.sql.Types.INTEGER)
                    } else {
                        insertCall.setInt(18, this.W4_SIGNED_PIDM.toInteger())
                    }

                    if ((this.pdrdedn_w4_signed_date == "") || (this.pdrdedn_w4_signed_date == null) ||
                            (!this.pdrdedn_w4_signed_date)) {
                        insertCall.setNull(19, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_w4_signed_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(19, sqlDate)
                    }
                    // lockin letter info
                    insertCall.setString(20, this.pdrdedn_lockin_letter_status)

                    if ((this.pdrdedn_lockin_letter_date == "") || (this.pdrdedn_lockin_letter_date == null) ||
                            (!this.pdrdedn_lockin_letter_date)) {
                        insertCall.setNull(21, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_lockin_letter_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(21, sqlDate)
                    }

                    insertCall.setString(22, this.pdrdedn_lockin_fsta_fil_st)

                    if ((this.pdrdedn_lockin_withhold_allow == "") || (this.pdrdedn_lockin_withhold_allow == null) ||
                            (!this.pdrdedn_lockin_withhold_allow)) {
                        insertCall.setNull(23, java.sql.Types.INTEGER)
                    } else {
                        insertCall.setInt(23, this.pdrdedn_lockin_withhold_allow.toInteger())
                    }

                    insertCall.setString(24, this.pdrdedn_comment)
                    if ((this.pdrdedn_comment_date == "") || (this.pdrdedn_comment_date == null) ||
                            (!this.pdrdedn_comment_date)) {
                        insertCall.setNull(25, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_comment_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(25, sqlDate)
                    }

                    insertCall.setString(26, this.pdrdedn_comment_user_id)
                    insertCall.setString(27, this.pdrdedn_user_id)
                    insertCall.setString(28, this.pdrdedn_data_origin)
                    insertCall.setString(29, this.pdrdedn_brea_code)

                    if ((this.pdrdedn_event_date == "") || (this.pdrdedn_event_date == null) ||
                            (!this.pdrdedn_event_date)) {
                        insertCall.setNull(30, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.pdrdedn_event_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(30, sqlDate)
                    }

                    insertCall.setString(31, pdrdedn_1042s_limit_ben_cde)

                    insertCall.registerOutParameter(32, java.sql.Types.ROWID)
                    try {
                        insertCall.executeUpdate()
                        connectInfo.tableUpdate("PDRDEDN", 0, 1, 0, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("PDRDEDN", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Insert PDRDEDN ${this.bannerid} ${this.pdrdedn_bdca_code} ${this.pdrdedn_effective_date}"
                            println "Problem executing insert for table PDRDEDN from EmployeeBenefitsDetailDML.groovy: $e.message"
                        }
                    }
                    finally {
                        insertCall.close()
                    }
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("PDRDEDN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert PDRDEDN ${this.bannerid} ${this.pdrdedn_bdca_code} ${this.pdrdedn_effective_date}"
                        println "Problem executing insert for table PDRDEDN from EmployeeBenefitsDetailDML.groovy: $e.message"
                    }
                }
            }
        }
    }
}
