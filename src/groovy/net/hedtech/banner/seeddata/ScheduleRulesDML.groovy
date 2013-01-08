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

/**
 *  DML for schedule rules tables (ssrclbd,  ssrrdep, ssrrcht, ssrratt )
 */


public class ScheduleRulesDML {

    def ssrratt_term_code
    def ssrratt_crn
    def ssrratt_rec_type
    def ssrratt_atts_ie_cde
    def ssrratt_atts_code

    def ssrrdep_term_code
    def ssrrdep_crn
    def ssrrdep_rec_type
    def ssrrdep_dept_ie_cde
    def ssrrdep_dept_code

    def ssrrchr_term_code
    def ssrrchr_crn
    def ssrrchr_rec_type
    def ssrrchr_chrt_ie_cde
    def ssrrchr_chrt_code

    def ssrclbd_term_code
    def ssrclbd_crn
    def ssrclbd_seq_no
    def ssrclbd_percent
    def ssrclbd_coas_code
    def ssrclbd_acci_code
    def ssrclbd_fund_code
    def ssrclbd_orgn_code
    def ssrclbd_acct_code
    def ssrclbd_prog_code
    def ssrclbd_actv_code
    def ssrclbd_locn_code
    def ssrclbd_proj_code
    def ssrclbd_ctyp_code
    def ssrclbd_acct_external_cde

    def ssbwlsc_term_code
    def ssbwlsc_crn
    def ssbwlsc_auto_notify_ind
    def ssbwlsc_wl_reg_check_ind
    def ssbwlsc_wl_pos_webc_disp_ind
    def ssbwlsc_deadline_notify
    def primaryKeyOut = 0

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public ScheduleRulesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
    }


    def parseXmlData() {
        def schRules = new XmlParser().parseText(xmlData)
        def tablename = schRules.toString()
        def newtab = tablename.substring(0, 7)

        if (newtab == "SSRRDEP") {
            this.ssrrdep_term_code = schRules.SSRRDEP_TERM_CODE.text()
            this.ssrrdep_crn = schRules.SSRRDEP_CRN.text()
            this.ssrrdep_rec_type = schRules.SSRRDEP_REC_TYPE.text()
            this.ssrrdep_dept_ie_cde = schRules.SSRRDEP_DEPT_IE_CDE.text()
            this.ssrrdep_dept_code = schRules.SSRRDEP_DEPT_CODE.text()
            ProcessSsrrdep()
        }
        else if (newtab == "SSRRATT") {
            this.ssrratt_term_code = schRules.SSRRATT_TERM_CODE.text()
            this.ssrratt_crn = schRules.SSRRATT_CRN.text()
            this.ssrratt_rec_type = schRules.SSRRATT_REC_TYPE.text()
            this.ssrratt_atts_ie_cde = schRules.SSRRATT_ATTS_IE_CDE.text()
            this.ssrratt_atts_code = schRules.SSRRATT_ATTS_CODE.text()
            processSsrratt()
        }
        else if (newtab == "SSRRCHR") {
            this.ssrrchr_term_code = schRules.SSRRCHR_TERM_CODE.text()
            this.ssrrchr_crn = schRules.SSRRCHR_CRN.text()
            this.ssrrchr_rec_type = schRules.SSRRCHR_REC_TYPE.text()
            this.ssrrchr_chrt_ie_cde = schRules.SSRRCHR_CHRT_IE_CDE.text()
            this.ssrrchr_chrt_code = schRules.SSRRCHR_CHRT_CODE.text()
            processSsrrchr()
        }
        else if (newtab == "SSRCLBD") {
            this.ssrclbd_term_code = schRules.SSRCLBD_TERM_CODE.text()
            this.ssrclbd_crn = schRules.SSRCLBD_CRN.text()
            this.ssrclbd_seq_no = schRules.SSRCLBD_SEQ_NO.text()
            this.ssrclbd_percent = schRules.SSRCLBD_PERCENT.text()
            this.ssrclbd_coas_code = schRules.SSRCLBD_COAS_CODE.text()
            this.ssrclbd_acci_code = schRules.SSRCLBD_ACCI_CODE.text()
            this.ssrclbd_fund_code = schRules.SSRCLBD_FUND_CODE.text()
            this.ssrclbd_orgn_code = schRules.SSRCLBD_ORGN_CODE.text()
            this.ssrclbd_acct_code = schRules.SSRCLBD_ACCT_CODE.text()
            this.ssrclbd_prog_code = schRules.SSRCLBD_PROG_CODE.text()
            this.ssrclbd_actv_code = schRules.SSRCLBD_ACTV_CODE.text()
            this.ssrclbd_locn_code = schRules.SSRCLBD_LOCN_CODE.text()
            this.ssrclbd_proj_code = schRules.SSRCLBD_PROJ_CODE.text()
            this.ssrclbd_ctyp_code = schRules.SSRCLBD_CTYP_CODE.text()
            this.ssrclbd_acct_external_cde = schRules.SSRCLBD_ACCT_EXTERNAL_CDE.text()
            processSsrclbd()
        }
        else if (newtab == "SSBWLSC") {
            this.ssbwlsc_term_code = schRules.SSBWLSC_TERM_CODE.text()
            this.ssbwlsc_crn = schRules.SSBWLSC_CRN.text()
            this.ssbwlsc_auto_notify_ind = schRules.SSBWLSC_AUTO_NOTIFY_IND.text()
            this.ssbwlsc_wl_reg_check_ind = schRules.SSBWLSC_WL_REG_CHECK_IND.text()
            this.ssbwlsc_wl_pos_webc_disp_ind = schRules.SSBWLSC_WL_POS_WEBC_DISP_IND.text()
            this.ssbwlsc_deadline_notify = schRules.SSBWLSC_DEADLINE_NOTIFY.text()
            processSsbwlsc()
        }
    }


    def processSsrratt() {
        def findY = null
        String findRow = """select 'Y' ssrratt_find from ssrratt where ssrratt_crn = ?
                      and ssrratt_term_code = ?
                      and ssrratt_rec_type = ?
                      and nvl(ssrratt_atts_code,'%') = nvl(? ,'%')  """
        try {
            conn.eachRow(findRow, [this.ssrratt_crn, this.ssrratt_term_code, this.ssrratt_rec_type, this.ssrratt_atts_code]) {row ->
                findY = row.ssrratt_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRRATT", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select for table SSRRATT from ScheduleRulesDML.groovy: $e.message"
                println "${findRow}"
            }
        }
        if (!findY) {
            try {
                //  parm count is 9
                String API = "{call  sb_sect_atts_restriction.p_create(?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)

                // parm 1 p_term_code  ssrratt_term_code VARCHAR2
                insertCall.setString(1, this.ssrratt_term_code)

                // parm 2 p_crn  ssrratt_crn VARCHAR2
                insertCall.setString(2, this.ssrratt_crn)

                // parm 3 p_rec_type  ssrratt_rec_type VARCHAR2
                insertCall.setString(3, this.ssrratt_rec_type)

                // parm 4 p_user_id  ssrratt_user_id VARCHAR2
                insertCall.setString(4, connectInfo.userID)
                // parm 5 p_atts_ie_cde  ssrratt_atts_ie_cde VARCHAR2
                insertCall.setString(5, this.ssrratt_atts_ie_cde)

                // parm 6 p_atts_code  ssrratt_atts_code VARCHAR2
                insertCall.setString(6, this.ssrratt_atts_code)

                // parm 7 p_data_origin  ssrratt_data_origin VARCHAR2
                insertCall.setString(7, connectInfo.dataOrigin)
                // parm 8 p_primary_key_out  ssrratt_primary_key_out VARCHAR2
                insertCall.registerOutParameter(8, java.sql.Types.INTEGER)
                insertCall.setInt(8, primaryKeyOut)

                // parm 9 p_rowid_out  ssrratt_rowid_out VARCHAR2
                insertCall.registerOutParameter(9, java.sql.Types.ROWID)


                try {
                    insertCall.executeUpdate()
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSRRATT", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSRRATT ${this.ssrratt_crn} ${this.ssrratt_term_code}  ${this.ssrratt_atts_code} ${this.ssrratt_rec_type}"
                        println "Problem executing insert for table SSRRATT from ScheduleRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
               
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRRATT", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSRRATT ${this.ssrratt_crn} ${this.ssrratt_term_code} ${this.ssrratt_atts_code}"
                    println "Problem executing insert for table SSRRATT from ScheduleRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def ProcessSsrrdep() {
        def findY = ""
        String findRow = """select 'Y' ssrrdep_find from ssrrdep where ssrrdep_crn = ?
                           and ssrrdep_term_code = ?
                           and ssrrdep_rec_type = ?
                           and nvl(ssrrdep_dept_code,'%') = nvl(?,'%') """
        try {
            conn.eachRow(findRow, [this.ssrrdep_crn, this.ssrrdep_term_code, this.ssrrdep_rec_type, this.ssrrdep_dept_code]) {row ->
                findY = row.ssrrdep_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRRDEP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select for table SSRRDEP from ScheduleRulesDML.groovy: $e.message"
                println "${findRow}"
            }
        }
        if (!findY) {
            try {
                //  parm count is 9
                String API = "{call  sb_sect_dept_restriction.p_create(?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)

                // parm 1 p_term_code      ssrrdep_term_code VARCHAR2
                insertCall.setString(1, this.ssrrdep_term_code)

                // parm 2 p_crn      ssrrdep_crn VARCHAR2
                insertCall.setString(2, this.ssrrdep_crn)

                // parm 3 p_rec_type      ssrrdep_rec_type VARCHAR2
                insertCall.setString(3, this.ssrrdep_rec_type)

                // parm 4 p_user_id      ssrrdep_user_id VARCHAR2
                insertCall.setString(4, connectInfo.userID)
                // parm 5 p_dept_ie_cde      ssrrdep_dept_ie_cde VARCHAR2
                insertCall.setString(5, this.ssrrdep_dept_ie_cde)

                // parm 6 p_dept_code      ssrrdep_dept_code VARCHAR2
                insertCall.setString(6, this.ssrrdep_dept_code)

                // parm 7 p_data_origin      ssrrdep_data_origin VARCHAR2
                insertCall.setString(7, connectInfo.dataOrigin)
                // parm 8 p_primary_key_out      ssrrdep_primary_key_out VARCHAR2
                insertCall.registerOutParameter(8, java.sql.Types.INTEGER)
                insertCall.setInt(8, primaryKeyOut)

                // parm 9 p_rowid_out      SSrdep_rowid_out VARCHAR2
                insertCall.registerOutParameter(9, java.sql.Types.ROWID)

                if (connectInfo.debugThis) { println "Insert SSRRDEP ${this.ssrrdep_crn} ${this.ssrrdep_term_code}" }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SSRRDEP", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSRRDEP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSRRDEP ${this.ssrrdep_crn} ${this.ssrrdep_term_code}"
                        println "Problem executing insert for table SSRRDEP from ScheduleRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRRDEP", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSRRDEP ${this.ssrrdep_crn} ${this.ssrrdep_term_code}"
                    println "Problem executing insert for table SSRRDEP from ScheduleRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processSsrrchr() {
        def findY = ""
        String findRow = """select 'Y' ssrrchr_find from ssrrchr where ssrrchr_crn = ?
               and ssrrchr_term_code = ?
               and ssrrchr_rec_type = ?
                and nvl(ssrrchr_chrt_code,'%') = nvl(?,'%')  """
        try {
            conn.eachRow(findRow, [this.ssrrchr_crn, this.ssrrchr_term_code, this.ssrrchr_rec_type, this.ssrrchr_chrt_code]) {row ->
                findY = row.ssrrchr_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRRCHR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select for table SSRRCHR from ScheduleRulesDML.groovy: $e.message"
                println "${findRow}"
            }
        }
        if (!findY) {
            try {
                //  parm count is 9
                String API = "{call  sb_sect_chrt_restriction.p_create(?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code      SSRRCHR_term_code VARCHAR2
                insertCall.setString(1, this.ssrrchr_term_code)

                // parm 2 p_crn      ssrrchr_crn VARCHAR2
                insertCall.setString(2, this.ssrrchr_crn)

                // parm 3 p_rec_type      ssrrchr_rec_type VARCHAR2
                insertCall.setString(3, this.ssrrchr_rec_type)

                // parm 4 p_user_id      ssrrchr_user_id VARCHAR2
                insertCall.setString(4, connectInfo.userID)
                // parm 5 p_chrt_ie_cde      ssrrchr_chrt_ie_cde VARCHAR2
                insertCall.setString(5, this.ssrrchr_chrt_ie_cde)

                // parm 6 p_chrt_code      ssrrchr_chrt_code VARCHAR2
                insertCall.setString(6, this.ssrrchr_chrt_code)

                // parm 7 p_data_origin      ssrrchr_data_origin VARCHAR2
                insertCall.setString(7, connectInfo.dataOrigin)
                // parm 8 p_primary_key_out      SSRRCHR_primary_key_out VARCHAR2
                insertCall.registerOutParameter(8, java.sql.Types.INTEGER)
                insertCall.setInt(8, primaryKeyOut)

                // parm 9 p_rowid_out      SSRRCHR_rowid_out VARCHAR2
                insertCall.registerOutParameter(9, java.sql.Types.ROWID)
                if (connectInfo.debugThis) { println "Insert into SSRRCHR ${this.ssrrchr_crn} ${this.ssrrchr_term_code}" }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SSRRCHR", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSRRCHR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSRRCHR ${this.ssrrchr_crn} ${this.ssrrchr_term_code}"
                        println "Problem executing insert for table SSRRCHR from ScheduleRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRRCHR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSRRCHR ${this.ssrrchr_crn} ${this.ssrrchr_term_code}"
                    println "Problem executing insert for table SSRRCHR from ScheduleRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processSsrclbd() {
        def findY = ""
        String findRow = """select 'Y' ssrclbd_find from ssrclbd where ssrclbd_crn = ?
                       and ssrclbd_term_code = ?
                       and ssrclbd_seq_no = ?   """
        try {
            conn.eachRow(findRow, [this.ssrclbd_crn, this.ssrclbd_term_code, this.ssrclbd_seq_no]) {row ->
                findY = row.ssrclbd_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRCLBD", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select for table SSRCLBD from CatalogRulesDML.groovy: $e.message"
                println "${findRow}"
            }
        }
        if (!findY) {

            //  parm count is 18
            try {
                String API = "{call  sb_section_labor.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code      ssrclbd_term_code VARCHAR2
                insertCall.setString(1, this.ssrclbd_term_code)

                // parm 2 p_crn      ssrclbd_crn VARCHAR2
                insertCall.setString(2, this.ssrclbd_crn)

                // parm 3 p_seq_no      ssrclbd_seq_no NUMBER
                if ((this.ssrclbd_seq_no == "") || (this.ssrclbd_seq_no == null) || (!this.ssrclbd_seq_no)) { insertCall.setNull(3, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(3, this.ssrclbd_seq_no.toInteger())
                }

                // parm 4 p_percent      ssrclbd_percent NUMBER
                if ((this.ssrclbd_percent == "") || (this.ssrclbd_percent == null) || (!this.ssrclbd_percent)) { insertCall.setNull(4, java.sql.Types.DOUBLE) }
                else {
                    insertCall.setDouble(4, this.ssrclbd_percent.toDouble())
                }

                // parm 5 p_user_id      ssrclbd_user_id VARCHAR2
                insertCall.setString(5, connectInfo.userID)
                // parm 6 p_data_origin      ssrclbd_data_origin VARCHAR2
                insertCall.setString(6, connectInfo.dataOrigin)
                // parm 7 p_coas_code      ssrclbd_coas_code VARCHAR2
                insertCall.setString(7, this.ssrclbd_coas_code)

                // parm 8 p_acci_code      ssrclbd_acci_code VARCHAR2
                insertCall.setString(8, this.ssrclbd_acci_code)

                // parm 9 p_fund_code      ssrclbd_fund_code VARCHAR2
                insertCall.setString(9, this.ssrclbd_fund_code)

                // parm 10 p_orgn_code      ssrclbd_orgn_code VARCHAR2
                insertCall.setString(10, this.ssrclbd_orgn_code)

                // parm 11 p_acct_code      ssrclbd_acct_code VARCHAR2
                insertCall.setString(11, this.ssrclbd_acct_code)

                // parm 12 p_prog_code      ssrclbd_prog_code VARCHAR2
                insertCall.setString(12, this.ssrclbd_prog_code)

                // parm 13 p_actv_code      ssrclbd_actv_code VARCHAR2
                insertCall.setString(13, this.ssrclbd_actv_code)

                // parm 14 p_locn_code      ssrclbd_locn_code VARCHAR2
                insertCall.setString(14, this.ssrclbd_locn_code)

                // parm 15 p_proj_code      ssrclbd_proj_code VARCHAR2
                insertCall.setString(15, this.ssrclbd_proj_code)

                // parm 16 p_ctyp_code      ssrclbd_ctyp_code VARCHAR2
                insertCall.setString(16, this.ssrclbd_ctyp_code)

                // parm 17 p_acct_external_cde      ssrclbd_acct_external_cde VARCHAR2
                insertCall.setString(17, this.ssrclbd_acct_external_cde)

                // parm 18 p_rowid_out      ssrclbd_rowid_out VARCHAR2
                insertCall.registerOutParameter(18, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SSRCLBD ${this.ssrclbd_term_code} ${this.ssrclbd_crn} ${this.ssrclbd_seq_no}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SSRCLBD", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSRCLBD", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSRCLBD ${this.ssrclbd_term_code} ${this.ssrclbd_crn} ${this.ssrclbd_seq_no}"
                        println "Problem executing insert for table SSRCLBD from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRCLBD", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSRCLBD ${this.ssrclbd_term_code} ${this.ssrclbd_crn} ${this.ssrclbd_seq_no}"
                    println "Problem executing insert for table SSRCLBD from CatalogRulesDML.groovy: $e.message"
                }
            }


        }
    }


    def processSsbwlsc() {
        def findY = ""
        String findRow = """select 'Y' ssbwlsc_find from ssbwlsc where ssbwlsc_crn = ?
                        and ssbwlsc_term_code = ?  """
        try {
            conn.eachRow(findRow, [this.ssbwlsc_crn, this.ssbwlsc_term_code]) {row ->
                findY = row.ssbwlsc_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSBWLSC", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select for table SSRWLSC from ScheduleRulesDML.groovy: $e.message"
                println "${findRow}"
            }
        }
        if (!findY) {
            try {
                //  parm count is 9
                String persAPI = "{call  sb_wl_section_ctrl.p_create(?,?,?,?,?,?,?,?,?,?)}"

                CallableStatement insertCall = this.connectCall.prepareCall(persAPI)
                // parm 1 p_term_code      ssbwlsc_term_code VARCHAR2
                insertCall.setString(1, this.ssbwlsc_term_code)

                // parm 2 p_crn      ssbwlsc_crn VARCHAR2
                insertCall.setString(2, this.ssbwlsc_crn)

                // parm 3 p_auto_notify_ind      ssbwlsc_auto_notify_ind VARCHAR2
                insertCall.setString(3, this.ssbwlsc_auto_notify_ind)

                // parm 4 p_wl_reg_check_ind      ssbwlsc_wl_reg_check_ind VARCHAR2
                insertCall.setString(4, this.ssbwlsc_wl_reg_check_ind)

                // parm 5 p_wl_pos_webc_disp_ind      ssbwlsc_wl_pos_webc_disp_ind VARCHAR2
                insertCall.setString(5, this.ssbwlsc_wl_pos_webc_disp_ind)

                // parm 6 p_user_id      ssbwlsc_user_id VARCHAR2
                insertCall.setString(6, connectInfo.userID)
                // parm 7 p_deadline_notify      ssbwlsc_deadline_notify NUMBER
                if ((this.ssbwlsc_deadline_notify == "") || (this.ssbwlsc_deadline_notify == null) || (!this.ssbwlsc_deadline_notify)) {
                    insertCall.setNull(7, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(7, this.ssbwlsc_deadline_notify.toInteger())
                }

                // parm 8 p_data_origin      SSBWLSC_data_origin VARCHAR2
                insertCall.setString(8, connectInfo.dataOrigin)
                // parm 9 p_max_resend_hrs
                insertCall.setNull(9,java.sql.Types.INTEGER)
                // parm 10 p_rowid_out      SSBWLSC_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)
                if (connectInfo.debugThis) { println "Insert into SSBWLSC ${this.ssrclbd_crn} ${this.ssrclbd_term_code}" }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SSBWLSC", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSBWLSC", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSBWLSC ${this.ssbwlsc_crn} ${this.ssbwlsc_term_code}"
                        println "Problem executing insert for table SSBWLSC from ScheduleRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSBWLSC", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSBWLSC ${this.ssbwlsc_crn} ${this.ssbwlsc_term_code}"
                    println "Problem executing insert for table SSBWLSC from ScheduleRulesDML.groovy: $e.message"
                }
            }
        }
    }
}
