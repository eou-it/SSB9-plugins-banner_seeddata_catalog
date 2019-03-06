/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 *  DML for faculty appointments for schedule data  
 */


public class FacultyDML {
    def bannerid
    def sibinst_pidm
    def sibinst_term_code_eff
    def sibinst_fcst_code
    def sibinst_fctg_code
    def sibinst_fstp_code
    def sibinst_fcnt_code
    def sibinst_schd_ind
    def sibinst_advr_ind
    def sibinst_fcst_date
    def sibinst_wkld_code
    def sibinst_cntr_code
    def sibinst_appoint_date
    def sibinst_override_process_ind
    def sibinst_override_proc_userid
    def sibinst_override_proc_date


    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public FacultyDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        appointFaculty()
    }


    def parseXmlData() {
        def faculty = new XmlParser().parseText(xmlData)
        this.sibinst_pidm = connectInfo.saveStudentPidm
        this.bannerid = faculty.BANNERID.text()
        if (connectInfo.debugThis) {
            println "--------- New Faculty record ----------"
            println "Faculty: " + this.bannerid + " " + faculty.SIBINST_PIDM.text() + " " + faculty.SIBINST_TERM_CODE_EFF.text()
        }

        this.sibinst_term_code_eff = faculty.SIBINST_TERM_CODE_EFF.text()
        this.sibinst_fcst_code = faculty.SIBINST_FCST_CODE.text()
        this.sibinst_fctg_code = faculty.SIBINST_FCTG_CODE.text()
        this.sibinst_fstp_code = faculty.SIBINST_FSTP_CODE.text()
        this.sibinst_fcnt_code = faculty.SIBINST_FCNT_CODE.text()
        this.sibinst_schd_ind = faculty.SIBINST_SCHD_IND.text()
        this.sibinst_advr_ind = faculty.SIBINST_ADVR_IND.text()
        this.sibinst_fcst_date = faculty.SIBINST_FCST_DATE.text()
        this.sibinst_wkld_code = faculty.SIBINST_WKLD_CODE.text()
        this.sibinst_cntr_code = faculty.SIBINST_CNTR_CODE.text()
        this.sibinst_appoint_date = faculty.SIBINST_APPOINT_DATE.text()
        this.sibinst_override_process_ind = faculty.SIBINST_OVERRIDE_PROCESS_IND.text()
        this.sibinst_override_proc_userid = faculty.SIBINST_OVERRIDE_PROC_USERID.text()
        this.sibinst_override_proc_date = faculty.SIBINST_OVERRIDE_PROC_DATE.text()
    }


    def appointFaculty() {

        def findY = ""
        String findRow = """select 'Y' sibinst_find from sibinst where sibinst_pidm = ?
           and sibinst_term_code_eff = ? """
        try {
            conn.eachRow(findRow, [this.sibinst_pidm, this.sibinst_term_code_eff]) {row ->
                findY = row.sibinst_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SIBINST", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SIBINST from FacultyAssisngmentsDML.groovy: $e.message"
            }
        }
        if (!findY) {
            //  parm count is 18
            try {
                String API = "{call  sb_faculty.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sibinst_pidm NUMBER
                if ((this.sibinst_pidm == "") || (this.sibinst_pidm == null) || (!this.sibinst_pidm)) {
                    insertCall.setNull(1, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(1, this.sibinst_pidm.toInteger())
                }

                // parm 2 p_term_code_eff  sibinst_term_code_eff VARCHAR2
                insertCall.setString(2, this.sibinst_term_code_eff)

                // parm 3 p_fcst_code  sibinst_fcst_code VARCHAR2
                insertCall.setString(3, this.sibinst_fcst_code)

                // parm 4 p_fctg_code  sibinst_fctg_code VARCHAR2
                insertCall.setString(4, this.sibinst_fctg_code)

                // parm 5 p_fstp_code  sibinst_fstp_code VARCHAR2
                insertCall.setString(5, this.sibinst_fstp_code)

                // parm 6 p_fcnt_code  sibinst_fcnt_code VARCHAR2
                insertCall.setString(6, this.sibinst_fcnt_code)

                // parm 7 p_schd_ind  sibinst_schd_ind VARCHAR2
                insertCall.setString(7, this.sibinst_schd_ind)

                // parm 8 p_advr_ind  sibinst_advr_ind VARCHAR2
                insertCall.setString(8, this.sibinst_advr_ind)

                // parm 9 p_fcst_date  sibinst_fcst_date DATE
                if ((this.sibinst_fcst_date == "") || (this.sibinst_fcst_date == null)
                        || (!this.sibinst_fcst_date)) {
                    insertCall.setNull(9, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sibinst_fcst_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(9, sqlDate)
                }

                // parm 10 p_wkld_code  sibinst_wkld_code VARCHAR2
                insertCall.setString(10, this.sibinst_wkld_code)

                // parm 11 p_cntr_code  sibinst_cntr_code VARCHAR2
                insertCall.setString(11, this.sibinst_cntr_code)

                // parm 12 p_appoint_date  sibinst_appoint_date DATE
                if ((this.sibinst_appoint_date == "") || (this.sibinst_appoint_date == null) ||
                        (!this.sibinst_appoint_date)) {
                    insertCall.setNull(12, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sibinst_appoint_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(12, sqlDate)
                }

                // parm 13 p_data_origin  sibinst_data_origin VARCHAR2
                insertCall.setString(13, connectInfo.dataOrigin)
                // parm 14 p_user_id  sibinst_user_id VARCHAR2
                insertCall.setString(14, connectInfo.userID)

                // parm 15 p_override_process_ind  sibinst_override_process_ind VARCHAR2
                if (!this.sibinst_override_process_ind || this.sibinst_override_process_ind == "" ) {
                    insertCall.setString(15, "N")
                }
                else {
                    insertCall.setString(15,this.sibinst_override_process_ind )
                }
                // parm 16 p_override_proc_userid  sibinst_override_proc_userid VARCHAR2
                insertCall.setString(16, this.sibinst_override_proc_userid)

                // parm 17 p_override_proc_date  sibinst_override_proc_date DATE
                if ((this.sibinst_override_proc_date == "") || (this.sibinst_override_proc_date == null) ||
                        (!this.sibinst_override_proc_date)) {
                    insertCall.setNull(17, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.sibinst_override_proc_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(17, sqlDate)
                }

                // parm 18 p_rowid_out  sibinst_rowid_out DATE
                insertCall.registerOutParameter(18, java.sql.Types.ROWID)


                if (connectInfo.debugThis) {
                    println "Insert SIBINST ${this.sibinst_pidm} ${this.sibinst_term_code_eff} ${this.sibinst_fcst_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SIBINST", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SIBINST", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SIBINST ${this.sibinst_pidm} ${this.sibinst_term_code_eff} ${this.sibinst_fcst_code}"
                        println "Problem executing insert for table SIBINST from FacultyDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SIBINST", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SIBINST ${this.sibinst_pidm} ${this.sibinst_term_code_eff} ${this.sibinst_fcst_code}"
                    println "Problem executing insert for table SIBINST from FacultyDML.groovy: $e.message"
                }
            }
        }
    }
}
