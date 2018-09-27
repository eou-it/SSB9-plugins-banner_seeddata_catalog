/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection

/**
 *  DML for catalog rules tables (scrclbd,  scrrdep, scrrcht, scrratt, scrintg )
 */

public class CatalogRulesDML {

    def scrratt_subj_code
    def scrratt_crse_numb
    def scrratt_eff_term
    def scrratt_rec_type
    def scrratt_atts_ie_cde
    def scrratt_atts_code

    def scrintg_subj_code
    def scrintg_crse_numb
    def scrintg_term_code_eff
    def scrintg_intg_cde

    def scrrdep_subj_code
    def scrrdep_crse_numb
    def scrrdep_eff_term
    def scrrdep_rec_type
    def scrrdep_dept_ie_cde
    def scrrdep_dept_code

    def scrrchr_subj_code
    def scrrchr_crse_numb
    def scrrchr_eff_term
    def scrrchr_rec_type
    def scrrchr_chrt_ie_cde
    def scrrchr_chrt_code

    def scrclbd_subj_code
    def scrclbd_crse_numb
    def scrclbd_term_code_eff
    def scrclbd_seq_no
    def scrclbd_percent
    def scrclbd_coas_code
    def scrclbd_acci_code
    def scrclbd_fund_code
    def scrclbd_orgn_code
    def scrclbd_acct_code
    def scrclbd_prog_code
    def scrclbd_actv_code
    def scrclbd_locn_code
    def scrclbd_proj_code
    def scrclbd_ctyp_code
    def scrclbd_acct_external_cde


    def scrmexc_subj_code
    def scrmexc_crse_numb
    def scrmexc_eff_term
    def scrmexc_subj_code_mexc
    def scrmexc_crse_numb_mexc
    def scrmexc_levl_code
    def scrmexc_grde_code
    def scrmexc_start_term
    def scrmexc_end_term

    def primaryKeyOut = 0

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public CatalogRulesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
    }



    def parseXmlData() {
        def catRules = new XmlParser().parseText(xmlData)
        def tablename = catRules.toString()
        def newtab = tablename.substring(0, 7)

        if (newtab == "SCRRATT") {

            this.scrratt_subj_code = catRules.SCRRATT_SUBJ_CODE.text()
            this.scrratt_crse_numb = catRules.SCRRATT_CRSE_NUMB.text()
            this.scrratt_eff_term = catRules.SCRRATT_EFF_TERM.text()
            this.scrratt_rec_type = catRules.SCRRATT_REC_TYPE.text()
            this.scrratt_atts_ie_cde = catRules.SCRRATT_ATTS_IE_CDE.text()
            this.scrratt_atts_code = catRules.SCRRATT_ATTS_CODE.text()
            processScrratt()
        }
        else if (newtab == "SCRRDEP") {
            this.scrrdep_subj_code = catRules.SCRRDEP_SUBJ_CODE.text()
            this.scrrdep_crse_numb = catRules.SCRRDEP_CRSE_NUMB.text()
            this.scrrdep_eff_term = catRules.SCRRDEP_EFF_TERM.text()
            this.scrrdep_rec_type = catRules.SCRRDEP_REC_TYPE.text()
            this.scrrdep_dept_ie_cde = catRules.SCRRDEP_DEPT_IE_CDE.text()
            this.scrrdep_dept_code = catRules.SCRRDEP_DEPT_CODE.text()
            processScrrdep()
        }
        else if (newtab == "SCRRCHR") {
            this.scrrchr_subj_code = catRules.SCRRCHR_SUBJ_CODE.text()
            this.scrrchr_crse_numb = catRules.SCRRCHR_CRSE_NUMB.text()
            this.scrrchr_eff_term = catRules.SCRRCHR_EFF_TERM.text()
            this.scrrchr_rec_type = catRules.SCRRCHR_REC_TYPE.text()
            this.scrrchr_chrt_ie_cde = catRules.SCRRCHR_CHRT_IE_CDE.text()
            this.scrrchr_chrt_code = catRules.SCRRCHR_CHRT_CODE.text()
            processScrrchr()
        }
        else if (newtab == "SCRMEXC") {
            this.scrmexc_subj_code = catRules.SCRMEXC_SUBJ_CODE.text()
            this.scrmexc_crse_numb = catRules.SCRMEXC_CRSE_NUMB.text()
            this.scrmexc_eff_term = catRules.SCRMEXC_EFF_TERM.text()
            this.scrmexc_subj_code_mexc = catRules.SCRMEXC_SUBJ_CODE_MEXC.text()
            this.scrmexc_crse_numb_mexc = catRules.SCRMEXC_CRSE_NUMB_MEXC.text()
            this.scrmexc_levl_code = catRules.SCRMEXC_LEVL_CODE.text()
            this.scrmexc_grde_code = catRules.SCRMEXC_GRDE_CODE.text()
            this.scrmexc_start_term = catRules.SCRMEXC_START_TERM.text()
            this.scrmexc_end_term = catRules.SCRMEXC_END_TERM.text()
            processScrmexc()
        }
        else if (newtab == "SCRCLBD") {
            this.scrclbd_subj_code = catRules.SCRCLBD_SUBJ_CODE.text()
            this.scrclbd_crse_numb = catRules.SCRCLBD_CRSE_NUMB.text()
            this.scrclbd_term_code_eff = catRules.SCRCLBD_TERM_CODE_EFF.text()
            this.scrclbd_seq_no = catRules.SCRCLBD_SEQ_NO.text()
            this.scrclbd_percent = catRules.SCRCLBD_PERCENT.text()
            this.scrclbd_coas_code = catRules.SCRCLBD_COAS_CODE.text()
            this.scrclbd_acci_code = catRules.SCRCLBD_ACCI_CODE.text()
            this.scrclbd_fund_code = catRules.SCRCLBD_FUND_CODE.text()
            this.scrclbd_orgn_code = catRules.SCRCLBD_ORGN_CODE.text()
            this.scrclbd_acct_code = catRules.SCRCLBD_ACCT_CODE.text()
            this.scrclbd_prog_code = catRules.SCRCLBD_PROG_CODE.text()
            this.scrclbd_actv_code = catRules.SCRCLBD_ACTV_CODE.text()
            this.scrclbd_locn_code = catRules.SCRCLBD_LOCN_CODE.text()
            this.scrclbd_proj_code = catRules.SCRCLBD_PROJ_CODE.text()
            this.scrclbd_ctyp_code = catRules.SCRCLBD_CTYP_CODE.text()
            this.scrclbd_acct_external_cde = catRules.SCRCLBD_ACCT_EXTERNAL_CDE.text()

            processScrclbd()
        }
        else if (newtab == "SCRINTG") {
            this.scrintg_subj_code = catRules.SCRINTG_SUBJ_CODE.text()
            this.scrintg_crse_numb = catRules.SCRINTG_CRSE_NUMB.text()
            this.scrintg_term_code_eff = catRules.SCRINTG_TERM_CODE_EFF.text()
            this.scrintg_intg_cde = catRules.SCRINTG_INTG_CDE.text()
            processScrintg()
        }
    }


    def processScrratt() {
        if (connectInfo.debugThis) { println "Insert  into SCRRATT ${this.scrratt_crse_numb} ${this.scrratt_subj_code}" }
        def findY = ""
        String findRow = """select scrratt_subj_code from scrratt
          where scrratt_subj_code = ?
          and scrratt_crse_numb = ?
           and nvl(scrratt_atts_code,'x') = nvl( ?,'x')
           and scrratt_rec_type = ?
           and scrratt_eff_term = ?
         """
        try {
            conn.eachRow(findRow, [this.scrratt_subj_code, this.scrratt_crse_numb, this.scrratt_atts_code,
                         this.scrratt_rec_type, this.scrratt_eff_term]) {row ->
                findY = row.scrratt_subj_code
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCRRATT", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SCRRATT from ScheduleRulesDML.groovy: $e.message"

            }
        }

        if (!findY) {
            validateCrky(this.scrratt_subj_code, this.scrratt_crse_numb, this.scrratt_eff_term)
            try {

                //  parm count is 10

                String API = "{call  sb_crse_atts_restriction.p_create(?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrratt_subj_code VARCHAR2
                insertCall.setString(1, this.scrratt_subj_code)

                // parm 2 p_crse_numb  scrratt_crse_numb VARCHAR2
                insertCall.setString(2, this.scrratt_crse_numb)

                // parm 3 p_eff_term  scrratt_eff_term VARCHAR2
                insertCall.setString(3, this.scrratt_eff_term)

                // parm 4 p_rec_type  scrratt_rec_type VARCHAR2
                insertCall.setString(4, this.scrratt_rec_type)

                // parm 5 p_user_id  scrratt_user_id VARCHAR2
                insertCall.setString(5, connectInfo.userID)
                // parm 6 p_atts_ie_cde  scrratt_atts_ie_cde VARCHAR2
                insertCall.setString(6, this.scrratt_atts_ie_cde)

                // parm 7 p_atts_code  scrratt_atts_code VARCHAR2
                if ((!this.scrratt_atts_code) || (this.scrratt_atts_code == ""))
                    insertCall.setNull(7, java.sql.Types.VARCHAR)
                else insertCall.setString(7, this.scrratt_atts_code)

                // parm 8 p_data_origin  scrratt_data_origin VARCHAR2
                insertCall.setString(8, connectInfo.dataOrigin)
                // parm 9 p_primary_key_out  scrratt_primary_key_out VARCHAR2
                insertCall.registerOutParameter(9, java.sql.Types.INTEGER)
                insertCall.setInt(9, primaryKeyOut)

                // parm 10 p_rowid_out  scrratt_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRRATT", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRRATT", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRRATT ${this.scrratt_crse_numb} ${this.scrratt_subj_code}"
                        println "Problem executing insert for table SCRRATT from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRRATT", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRRATT ${this.scrratt_crse_numb} ${this.scrratt_subj_code}"
                    println "Problem setting up insert for table SCRRATT from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processScrrchr() {
        if (connectInfo.debugThis) { println "Insert  into SCRRCHR ${this.scrrchr_crse_numb} ${this.scrrchr_subj_code}" }
        def findY = ""
        String findRow = """select 'Y' scrrchr_find from scrrchr where scrrchr_subj_code = ?
           and scrrchr_crse_numb = ?
           and nvl(scrrchr_chrt_code,'x') = nvl(?,'x')
           and scrrchr_rec_type = ?
           and scrrchr_eff_term = ? """
        try {
            conn.eachRow(findRow, [this.scrrchr_subj_code, this.scrrchr_crse_numb,
                         this.scrrchr_chrt_code, this.scrrchr_rec_type, this.scrrchr_eff_term]) {row ->
                findY = row.scrrchr_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCRRCHR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SCRRCHR from ScheduleRulesDML.groovy: $e.message"
            }
        }

        if (!findY) {
            validateCrky(this.scrrchr_subj_code, this.scrrchr_crse_numb, this.scrrchr_eff_term)
            try {
                //  parm count is 10

                String API = "{call  sb_crse_chrt_restriction.p_create(?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrrchr_subj_code VARCHAR2
                insertCall.setString(1, this.scrrchr_subj_code)

                // parm 2 p_crse_numb  scrrchr_crse_numb VARCHAR2
                insertCall.setString(2, this.scrrchr_crse_numb)

                // parm 3 p_eff_term  scrrchr_eff_term VARCHAR2
                insertCall.setString(3, this.scrrchr_eff_term)

                // parm 4 p_rec_type  scrrchr_rec_type VARCHAR2
                insertCall.setString(4, this.scrrchr_rec_type)

                // parm 5 p_user_id  scrrchr_user_id VARCHAR2
                insertCall.setString(5, connectInfo.userID)
                // parm 6 p_chrt_ie_cde  scrrchr_chrt_ie_cde VARCHAR2
                insertCall.setString(6, this.scrrchr_chrt_ie_cde)

                // parm 7 p_chrt_code  scrrchr_chrt_code VARCHAR2
                insertCall.setString(7, this.scrrchr_chrt_code)

                // parm 8 p_data_origin  scrrchr_data_origin VARCHAR2
                insertCall.setString(8, connectInfo.dataOrigin)
                // parm 9 p_primary_key_out  scrrchr_primary_key_out VARCHAR2
                insertCall.registerOutParameter(9, java.sql.Types.INTEGER)
                insertCall.setInt(9, primaryKeyOut)
                // parm 10 p_rowid_out  scrrchr_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRRCHR", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRRCHR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRRCHR ${this.scrrchr_crse_numb} ${this.scrrchr_subj_code}"
                        println "Problem executing insert for table SCRRCHR from ScheduleRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRRCHR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRRCHR ${this.scrrchr_crse_numb} ${this.scrrchr_subj_code}"
                    println "Problem setting up insert for table SCRRCHR from ScheduleRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processScrrdep() {
        if (connectInfo.debugThis) { println "Insert  into SCRRDEP ${this.scrrdep_crse_numb} ${this.scrrdep_subj_code}" }
        def findY = ""
        String findRow = """select 'Y' scrrdep_find from scrrdep where scrrdep_subj_code = ?
           and scrrdep_crse_numb = ?
           and nvl(scrrdep_dept_code,'x') = nvl(?,'x')
           and scrrdep_rec_type = ?
           and scrrdep_eff_term = ? """
        try {
            conn.eachRow(findRow, [this.scrrdep_subj_code, this.scrrdep_crse_numb, this.scrrdep_dept_code,
                         this.scrrdep_rec_type, this.scrrdep_eff_term]) {row ->
                findY = row.scrrdep_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCRRDEP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SCRRDEP from CatalogRulesDML.groovy: $e.message"
            }
        }
        if (!findY) {
            validateCrky(this.scrrdep_subj_code, this.scrrdep_crse_numb, this.scrrdep_eff_term)
            try {
                //  parm count is 10
                String API = "{call  sb_crse_dept_restriction.p_create(?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrrdep_subj_code VARCHAR2
                insertCall.setString(1, this.scrrdep_subj_code)

                // parm 2 p_crse_numb  scrrdep_crse_numb VARCHAR2
                insertCall.setString(2, this.scrrdep_crse_numb)

                // parm 3 p_eff_term  scrrdep_eff_term VARCHAR2
                insertCall.setString(3, this.scrrdep_eff_term)

                // parm 4 p_rec_type  scrrdep_rec_type VARCHAR2
                insertCall.setString(4, this.scrrdep_rec_type)

                // parm 5 p_user_id  scrrdep_user_id VARCHAR2
                insertCall.setString(5, connectInfo.userID)
                // parm 6 p_dept_ie_cde  scrrdep_dept_ie_cde VARCHAR2
                insertCall.setString(6, this.scrrdep_dept_ie_cde)

                // parm 7 p_dept_code  scrrdep_dept_code VARCHAR2
                insertCall.setString(7, this.scrrdep_dept_code)

                // parm 8 p_data_origin  scrrdep_data_origin VARCHAR2
                insertCall.setString(8, connectInfo.dataOrigin)
                // parm 9 p_primary_key_out  scrrdep_primary_key_out VARCHAR2

                insertCall.registerOutParameter(9, java.sql.Types.INTEGER)
                insertCall.setInt(9, primaryKeyOut)

                // parm 10 p_rowid_out  scrrdep_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRRDEP", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRRDEP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRRDEP ${this.scrrdep_crse_numb} ${this.scrrdep_subj_code}"
                        println "Problem executing insert for table SCRRDEP from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRRDEP", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRRDEP ${this.scrrdep_crse_numb} ${this.scrrdep_subj_code}"
                    println "Problem setting up insert for table SCRRDEP from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processScrintg() {
        if (connectInfo.debugThis) { println "Insert  into SCRINTG ${this.scrintg_crse_numb} ${this.scrintg_subj_code} ${this.scrintg_intg_cde}" }
        def findY = ""
        String findRow = """select 'Y' scrintg_find from scrintg where scrintg_subj_code = ?
                      and scrintg_crse_numb = ?
                      and scrintg_intg_cde = ?
                     and scrintg_term_code_eff = ? """
        try {
            conn.eachRow(findRow, [this.scrintg_subj_code, this.scrintg_crse_numb, this.scrintg_intg_cde,
                         this.scrintg_term_code_eff]) {row ->
                findY = row.scrintg_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCRINTG", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SCRINTG from CatalogRulesDML.groovy: $e.message"
            }
        }
        if (!findY) {
            validateCrky(this.scrintg_subj_code, this.scrintg_crse_numb, this.scrintg_term_code_eff)
            try {
                //  parm count is 7
                String API = "{call  sb_catlg_int_partner.p_create(?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrintg_subj_code VARCHAR2
                insertCall.setString(1, this.scrintg_subj_code)

                // parm 2 p_crse_numb  scrintg_crse_numb VARCHAR2
                insertCall.setString(2, this.scrintg_crse_numb)

                // parm 3 p_term_code_eff  scrintg_term_code_eff VARCHAR2
                insertCall.setString(3, this.scrintg_term_code_eff)

                // parm 4 p_user_id  scrintg_user_id VARCHAR2
                insertCall.setString(4, connectInfo.userID)
                // parm 5 p_intg_cde  scrintg_intg_cde VARCHAR2
                insertCall.setString(5, this.scrintg_intg_cde)

                // parm 6 p_data_origin  scrintg_data_origin VARCHAR2
                insertCall.setString(6, connectInfo.dataOrigin)
                // parm 7 p_rowid_out  scrintg_rowid_out VARCHAR2
                insertCall.registerOutParameter(7, java.sql.Types.ROWID)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRINTG", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRINTG", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRINTG ${this.scrintg_crse_numb} ${this.scrintg_subj_code} ${this.scrintg_intg_cde}"
                        println "Problem executing insert for table SCRINTG from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRINTG", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRINTG ${this.scrintg_crse_numb} ${this.scrintg_subj_code} ${this.scrintg_intg_cde}"
                    println "Problem setting up insert for table SCRINTG from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processScrclbd() {
        if (connectInfo.debugThis) { println "Insert  into SSRCLBD ${this.scrclbd_crse_numb} ${this.scrclbd_subj_code}" }
        def findY = ""
        String findRow = """select 'Y' scrclbd_find from scrclbd
           where scrclbd_subj_code = ?
           and scrclbd_crse_numb = ?
           and scrclbd_seq_no = ?
           and scrclbd_term_code_eff = ? """
        try {
            conn.eachRow(findRow, [this.scrclbd_subj_code, this.scrclbd_crse_numb, this.scrclbd_seq_no, this.scrclbd_term_code_eff]) {row ->
                findY = row.scrclbd_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCRCLBD", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SCRCLBD from CatalogRulesDML.groovy: $e.message"
            }
        }
        if (!findY) {
            validateCrky(this.scrclbd_subj_code, this.scrclbd_crse_numb, this.scrclbd_term_code_eff)
            try {
                //  parm count is 19
                String API = "{call  sb_course_labor.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrclbd_subj_code VARCHAR2
                insertCall.setString(1, this.scrclbd_subj_code)

                // parm 2 p_crse_numb  scrclbd_crse_numb VARCHAR2
                insertCall.setString(2, this.scrclbd_crse_numb)

                // parm 3 p_term_code_eff  scrclbd_term_code_eff VARCHAR2
                insertCall.setString(3, this.scrclbd_term_code_eff)

                // parm 4 p_seq_no  scrclbd_seq_no NUMBER
                if ((this.scrclbd_seq_no == "") || (this.scrclbd_seq_no == null) || (!this.scrclbd_seq_no)) { insertCall.setNull(4, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(4, this.scrclbd_seq_no.toInteger())
                }

                // parm 5 p_percent  scrclbd_percent NUMBER
                if ((this.scrclbd_percent == "") || (this.scrclbd_percent == null) || (!this.scrclbd_percent)) { insertCall.setNull(5, java.sql.Types.DOUBLE) }
                else {
                    insertCall.setDouble(5, this.scrclbd_percent.toDouble())
                }

                // parm 6 p_user_id  scrclbd_user_id VARCHAR2
                insertCall.setString(6, connectInfo.userID)
                // parm 7 p_data_origin  scrclbd_data_origin VARCHAR2
                insertCall.setString(7, connectInfo.dataOrigin)
                // parm 8 p_coas_code  scrclbd_coas_code VARCHAR2
                insertCall.setString(8, this.scrclbd_coas_code)

                // parm 9 p_acci_code  scrclbd_acci_code VARCHAR2
                insertCall.setString(9, this.scrclbd_acci_code)

                // parm 10 p_fund_code  scrclbd_fund_code VARCHAR2
                insertCall.setString(10, this.scrclbd_fund_code)

                // parm 11 p_orgn_code  scrclbd_orgn_code VARCHAR2
                insertCall.setString(11, this.scrclbd_orgn_code)

                // parm 12 p_acct_code  scrclbd_acct_code VARCHAR2
                insertCall.setString(12, this.scrclbd_acct_code)

                // parm 13 p_prog_code  scrclbd_prog_code VARCHAR2
                insertCall.setString(13, this.scrclbd_prog_code)

                // parm 14 p_actv_code  scrclbd_actv_code VARCHAR2
                insertCall.setString(14, this.scrclbd_actv_code)

                // parm 15 p_locn_code  scrclbd_locn_code VARCHAR2
                insertCall.setString(15, this.scrclbd_locn_code)

                // parm 16 p_proj_code  scrclbd_proj_code VARCHAR2
                insertCall.setString(16, this.scrclbd_proj_code)

                // parm 17 p_ctyp_code  scrclbd_ctyp_code VARCHAR2
                insertCall.setString(17, this.scrclbd_ctyp_code)

                // parm 18 p_acct_external_cde  scrclbd_acct_external_cde VARCHAR2
                insertCall.setString(18, this.scrclbd_acct_external_cde)

                // parm 19 p_rowid_out  scrclbd_rowid_out VARCHAR2
                insertCall.registerOutParameter(19, java.sql.Types.ROWID)
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRCLBD", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRCLBD", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRCLBD ${this.scrclbd_crse_numb} ${this.scrclbd_subj_code}"
                        println "Problem executing insert for table SCRCLBD from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }


            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRCLBD", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRCLBD ${this.scrclbd_crse_numb} ${this.scrclbd_subj_code}"
                    println "Problem setting up insert for table SCRCLBD from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def processScrmexc() {
        def findY = ""
        String rowSQL = """select 'Y' found_scrmexc from SCRMEXC
           where scrmexc_subj_code = ?
           and scrmexc_crse_numb = ?
           and scrmexc_eff_term = ?
           and scrmexc_subj_code_mexc = ?
           and scrmexc_crse_numb_mexc = ?
           and scrmexc_levl_code = ?
           and scrmexc_start_term = ?
      """

        try {
            conn.eachRow(rowSQL, [this.scrmexc_subj_code, this.scrmexc_crse_numb, this.scrmexc_eff_term,
                         this.scrmexc_subj_code_mexc, this.scrmexc_crse_numb_mexc, this.scrmexc_levl_code, this.scrmexc_start_term]) {row ->
                findY = row.found_scrmexc
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SCRMEXC rowid CatalogRulesDML.groovy: $e.message"
            }
        }
        if (!findY) {

            validateCrky(this.scrmexc_subj_code, this.scrmexc_crse_numb, this.scrmexc_eff_term)
            //  parm count is 13
            try {
                String API = "{call  sb_crse_mutual_exclusion.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_subj_code  scrmexc_subj_code VARCHAR2
                insertCall.setString(1, this.scrmexc_subj_code)

                // parm 2 p_crse_numb  scrmexc_crse_numb VARCHAR2
                insertCall.setString(2, this.scrmexc_crse_numb)

                // parm 3 p_eff_term  scrmexc_eff_term VARCHAR2
                insertCall.setString(3, this.scrmexc_eff_term)

                // parm 4 p_user_id  scrmexc_user_id VARCHAR2
                insertCall.setString(4, connectInfo.userID)
                // parm 5 p_subj_code_mexc  scrmexc_subj_code_mexc VARCHAR2
                insertCall.setString(5, this.scrmexc_subj_code_mexc)

                // parm 6 p_crse_numb_mexc  scrmexc_crse_numb_mexc VARCHAR2
                insertCall.setString(6, this.scrmexc_crse_numb_mexc)

                // parm 7 p_levl_code  scrmexc_levl_code VARCHAR2
                insertCall.setString(7, this.scrmexc_levl_code)

                // parm 8 p_grde_code  scrmexc_grde_code VARCHAR2
                insertCall.setString(8, this.scrmexc_grde_code)

                // parm 9 p_start_term  scrmexc_start_term VARCHAR2
                insertCall.setString(9, this.scrmexc_start_term)

                // parm 10 p_end_term  scrmexc_end_term VARCHAR2
                insertCall.setString(10, this.scrmexc_end_term)

                // parm 11 p_data_origin  scrmexc_data_origin VARCHAR2
                insertCall.setString(11, connectInfo.dataOrigin)
                // parm 12 p_primary_key_out  scrmexc_primary_key_out VARCHAR2
                insertCall.registerOutParameter(12, java.sql.Types.INTEGER)
                insertCall.setInt(12, primaryKeyOut)
                // parm 13 p_rowid_out  scrmexc_rowid_out VARCHAR2
                insertCall.registerOutParameter(13, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SCRMEXC ${this.scrmexc_subj_code} ${this.scrmexc_crse_numb} ${this.scrmexc_eff_term}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCRMEXC", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCRMEXC", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SCRMEXC ${this.scrmexc_subj_code} ${this.scrmexc_crse_numb} ${this.scrmexc_eff_term}"
                        println "Problem executing insert for table SCRMEXC from CatalogRulesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCRMEXC", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SCRMEXC ${this.scrmexc_subj_code} ${this.scrmexc_crse_numb} ${this.scrmexc_eff_term}"
                    println "Problem setting up insert for table SCRMEXC from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }


    def validateCrky(subj_code, crse_numb, eff_term) {
        def maxTerm = ""
        java.sql.RowId findCrkyRow = null
        String findCat = """select  rowid scbcrky_rowid from scbcrky
             where  scbcrky_subj_code = ?
              and scbcrky_crse_numb = ? """
        try {
            conn.eachRow(findCat, [subj_code, crse_numb]) {row ->
                findCrkyRow = row.scbcrky_rowid
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${findCat}"
                println "Problem executing rowid select for table SCBCRKY from CatalogRulesDML.groovy: $e.message"
            }
        }

        if (!findCrkyRow) {
            findCat = """ insert into scbcrky (scbcrky_subj_code, scbcrky_crse_numb,
                scbcrky_term_code_start,
                scbcrky_term_code_end, scbcrky_activity_date)
                values ('${subj_code}', '${crse_numb}',
                    '000000','999999',sysdate  )
              """
            try {
                conn.execute findCat
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "${findCat}"
                    println "Problem executing insert for table SCBCRKY from CatalogRulesDML.groovy: $e.message"
                }
            }
        }
    }
}
