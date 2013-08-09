/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection

/**
 *  DML for faculty assignments for term and crn for schedule data  
 */


public class FacultyAssignmentsDML {
    def bannerid
    def sirasgn_term_code
    def sirasgn_crn
    def sirasgn_pidm
    def sirasgn_category
    def sirasgn_percent_response
    def sirasgn_workload_adjust
    def sirasgn_percent_sess
    def sirasgn_primary_ind
    def sirasgn_over_ride
    def sirasgn_position
    def sirasgn_fcnt_code
    def sirasgn_posn
    def sirasgn_suff
    def sirasgn_asty_code
    def sirasgn_workload_incr
    def sirasgn_incr_enrl
    def sirasgn_incr_enrl_date

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public FacultyAssignmentsDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        assignFaculty()
    }


    def parseXmlData() {
        def faculty = new XmlParser().parseText(xmlData)
        this.sirasgn_pidm =  connectInfo.saveStudentPidm
        this.bannerid = faculty.BANNERID.text()
        if (connectInfo.debugThis) {
            println "--------- New Faculty Assignment record ----------"
            println "Faculty Assignment: " + this.bannerid + " " + faculty.SIRASGN_TERM_CODE.text() + " " + faculty.SIRASGN_CRN.text() + " " + faculty.SIRASGN_PIDM.text()
        }
        this.sirasgn_term_code = faculty.SIRASGN_TERM_CODE.text()
        this.sirasgn_crn = faculty.SIRASGN_CRN.text() 
        this.sirasgn_category = faculty.SIRASGN_CATEGORY.text()
        this.sirasgn_percent_response = faculty.SIRASGN_PERCENT_RESPONSE.text()
        this.sirasgn_workload_adjust = faculty.SIRASGN_WORKLOAD_ADJUST.text()
        this.sirasgn_percent_sess = faculty.SIRASGN_PERCENT_SESS.text()
        this.sirasgn_primary_ind = faculty.SIRASGN_PRIMARY_IND.text()
        this.sirasgn_over_ride = faculty.SIRASGN_OVER_RIDE.text()
        this.sirasgn_position = faculty.SIRASGN_POSITION.text()
        this.sirasgn_fcnt_code = faculty.SIRASGN_FCNT_CODE.text()
        this.sirasgn_posn = faculty.SIRASGN_POSN.text()
        this.sirasgn_suff = faculty.SIRASGN_SUFF.text()
        this.sirasgn_asty_code = faculty.SIRASGN_ASTY_CODE.text()
        this.sirasgn_workload_incr = faculty.SIRASGN_WORKLOAD_INCR.text()
        this.sirasgn_incr_enrl = faculty.SIRASGN_INCR_ENRL.text()
        this.sirasgn_incr_enrl_date = faculty.SIRASGN_INCR_ENRL_DATE.text()

    }


    def assignFaculty() {

        def findY = ""
        String findRow = """select 'Y' sirasgn_find from sirasgn where sirasgn_pidm = ?
      and sirasgn_term_code  = ?  and sirasgn_crn = ? """
        try {
            conn.eachRow(findRow, [this.sirasgn_pidm, this.sirasgn_term_code, this.sirasgn_crn]) {row ->
                findY = row.sirasgn_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SIRASGN", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem with select for table SIRASGN from FacultyAssisngmentsDML.groovy: $e.message"
            }
        }
        if (!findY) {

//  parm count is 17
            try {
                String API = "{call  sb_facassignment.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code  sirasgn_term_code VARCHAR2
                insertCall.setString(1, this.sirasgn_term_code)

                // parm 2 p_crn  sirasgn_crn VARCHAR2
                insertCall.setString(2, this.sirasgn_crn)

                // parm 3 p_pidm  sirasgn_pidm NUMBER
                if ((this.sirasgn_pidm == "") || (this.sirasgn_pidm == null) || (!this.sirasgn_pidm)) {
                    insertCall.setNull(3, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(3, this.sirasgn_pidm.toInteger())
                }

                // parm 4 p_category  sirasgn_category VARCHAR2
                insertCall.setString(4, this.sirasgn_category)

                // parm 5 p_percent_response  sirasgn_percent_response NUMBER
                if ((this.sirasgn_percent_response == "") || (this.sirasgn_percent_response == null) ||
                        (!this.sirasgn_percent_response)) {
                    insertCall.setNull(5, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(5, this.sirasgn_percent_response.toInteger())
                }

                // parm 6 p_workload_adjust  sirasgn_workload_adjust NUMBER
                if ((this.sirasgn_workload_adjust == "") || (this.sirasgn_workload_adjust == null) ||
                        (!this.sirasgn_workload_adjust)) {
                    insertCall.setNull(6, java.sql.Types.DOUBLE)
                }
                else {
                    insertCall.setDouble(6, this.sirasgn_workload_adjust.toDouble())
                }

                // parm 7 p_percent_sess  sirasgn_percent_sess NUMBER
                if ((this.sirasgn_percent_sess == "") || (this.sirasgn_percent_sess == null)
                        || (!this.sirasgn_percent_sess)) {
                    insertCall.setNull(7, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(7, this.sirasgn_percent_sess.toInteger())
                }

                // parm 8 p_primary_ind  sirasgn_primary_ind VARCHAR2
                insertCall.setString(8, this.sirasgn_primary_ind)

                // parm 9 p_over_ride  sirasgn_over_ride VARCHAR2
                insertCall.setString(9, this.sirasgn_over_ride)

                // parm 10 p_position  sirasgn_position NUMBER
                if ((this.sirasgn_position == "") || (this.sirasgn_position == null)
                        || (!this.sirasgn_position)) {
                    insertCall.setNull(10, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(10, this.sirasgn_position.toInteger())
                }

                // parm 11 p_fcnt_code  sirasgn_fcnt_code VARCHAR2
                insertCall.setString(11, this.sirasgn_fcnt_code)

                // parm 12 p_posn  sirasgn_posn VARCHAR2
                insertCall.setString(12, this.sirasgn_posn)

                // parm 13 p_suff  sirasgn_suff VARCHAR2
                insertCall.setString(13, this.sirasgn_suff)

                // parm 14 p_asty_code  sirasgn_asty_code VARCHAR2
                insertCall.setString(14, this.sirasgn_asty_code)

                // parm 15 p_data_origin  sirasgn_data_origin VARCHAR2
                insertCall.setString(15, connectInfo.dataOrigin)
                // parm 16 p_user_id  sirasgn_user_id VARCHAR2
                insertCall.setString(16, connectInfo.userID)
                // parm 17 p_rowid_out  sirasgn_rowid_out VARCHAR2
                insertCall.registerOutParameter(17, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SIRASGN ${this.sirasgn_term_code} ${this.sirasgn_crn} ${this.sirasgn_pidm}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SIRASGN", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SIRASGN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SIRASGN ${this.sirasgn_term_code} ${this.sirasgn_crn} ${this.sirasgn_pidm}"
                        println "Problem executing insert for table SIRASGN from FacultyAssisngmentsDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("SIRASGN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SIRASGN ${this.sirasgn_term_code} ${this.sirasgn_crn} ${this.sirasgn_pidm}"
                    println "Problem creating prepared call to insert table SIRASGN from FacultyAssisngmentsDML.groovy: $e.message"
                }
            }

        }
    }
}
