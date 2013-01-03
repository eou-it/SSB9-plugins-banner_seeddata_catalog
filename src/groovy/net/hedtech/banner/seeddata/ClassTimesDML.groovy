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
import java.text.SimpleDateFormat

/**
 *  DML for schedule meeting table (ssrmeet)
 */
public class ClassTimesDML {

    // meeting data
    def ssrmeet_term_code
    def ssrmeet_crn
    def ssrmeet_days_code
    def ssrmeet_day_number
    def ssrmeet_begin_time
    def ssrmeet_end_time
    def ssrmeet_bldg_code
    def ssrmeet_room_code
    def ssrmeet_activity_date
    def ssrmeet_start_date
    def ssrmeet_end_date
    def ssrmeet_catagory
    def ssrmeet_sun_day
    def ssrmeet_mon_day
    def ssrmeet_tue_day
    def ssrmeet_wed_day
    def ssrmeet_thu_day
    def ssrmeet_fri_day
    def ssrmeet_sat_day
    def ssrmeet_schd_code
    def ssrmeet_over_ride
    def ssrmeet_credit_hr_sess
    def ssrmeet_meet_no
    def ssrmeet_hrs_week
    def ssrmeet_func_code
    def ssrmeet_comt_code
    def ssrmeet_schs_code
    def ssrmeet_mtyp_code
    def ssrmeet_data_origin
    def ssrmeet_user_id
    def deleteNode
    // database connection information
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns


    public ClassTimesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        if (deleteNode == "YES") processMeetingDelete()
        else processMeetingFile()
    }


    def parseXmlData() {
        def classMeet = new XmlParser().parseText(xmlData)

        if (connectInfo.debugThis) {
            println "--------- New Class Time record ----------"
            println "Term: " + classMeet.SSRMEET_TERM_CODE.text() + " " + classMeet.SSRMEET_CRN.text()
        }
        deleteNode = classMeet.DELETE?.text()
        this.ssrmeet_term_code = classMeet.SSRMEET_TERM_CODE.text()
        this.ssrmeet_crn = classMeet.SSRMEET_CRN.text()
        this.ssrmeet_days_code = classMeet.SSRMEET_DAYS_CODE.text()
        this.ssrmeet_day_number = classMeet.SSRMEET_DAY_NUMBER.text()
        this.ssrmeet_begin_time = classMeet.SSRMEET_BEGIN_TIME.text()
        this.ssrmeet_end_time = classMeet.SSRMEET_END_TIME.text()
        this.ssrmeet_bldg_code = classMeet.SSRMEET_BLDG_CODE.text()
        this.ssrmeet_room_code = classMeet.SSRMEET_ROOM_CODE.text()
        this.ssrmeet_start_date = classMeet.SSRMEET_START_DATE.text()
        this.ssrmeet_end_date = classMeet.SSRMEET_END_DATE.text()
        this.ssrmeet_catagory = classMeet.SSRMEET_CATAGORY.text()
        this.ssrmeet_sun_day = classMeet.SSRMEET_SUN_DAY.text()
        this.ssrmeet_mon_day = classMeet.SSRMEET_MON_DAY.text()
        this.ssrmeet_tue_day = classMeet.SSRMEET_TUE_DAY.text()
        this.ssrmeet_wed_day = classMeet.SSRMEET_WED_DAY.text()
        this.ssrmeet_thu_day = classMeet.SSRMEET_THU_DAY.text()
        this.ssrmeet_fri_day = classMeet.SSRMEET_FRI_DAY.text()
        this.ssrmeet_sat_day = classMeet.SSRMEET_SAT_DAY.text()
        this.ssrmeet_schd_code = classMeet.SSRMEET_SCHD_CODE.text()
        this.ssrmeet_over_ride = classMeet.SSRMEET_OVER_RIDE.text()
        this.ssrmeet_credit_hr_sess = classMeet.SSRMEET_CREDIT_HR_SESS.text()
        this.ssrmeet_meet_no = classMeet.SSRMEET_MEET_NO.text()
        this.ssrmeet_hrs_week = classMeet.SSRMEET_HRS_WEEK.text()
        this.ssrmeet_func_code = classMeet.SSRMEET_FUNC_CODE.text()
        this.ssrmeet_comt_code = classMeet.SSRMEET_COMT_CODE.text()
        this.ssrmeet_schs_code = classMeet.SSRMEET_SCHS_CODE.text()
        this.ssrmeet_mtyp_code = classMeet.SSRMEET_MTYP_CODE.text()

    }


    def processMeetingDelete() {

        def delSql = "select rowid MEETROW from ssrmeet where ssrmeet_term_code = ? and ssrmeet_crn = ?"
        try {
            conn.eachRow(delSql, [this.ssrmeet_term_code, this.ssrmeet_crn]) {row ->
                def delRows = conn.executeUpdate("delete ssrmeet where rowid = ?", row.MEETROW)
                connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 0, delRows)
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Delete SSRMEET ${this.ssrmeet_term_code} ${this.ssrmeet_crn} ${this.ssrmeet_days_code}"
                println "Problem executing delete for table SSRMEET from ClassTimesDML.groovy: $e.message"
            }
        }
    }


    def processMeetingFile() {
        def findY = ""
        String findRow = """select 'Y' ssrmeet_find from ssrmeet where ssrmeet_crn = '${this.ssrmeet_crn}'
             AND ssrmeet_term_code = '${this.ssrmeet_term_code}'
             AND nvl(ssrmeet_begin_time,'x') =  nvl('${ssrmeet_begin_time}','x')  
              AND nvl(ssrmeet_end_time,'x') =  nvl('${ssrmeet_end_time}','x')  
              AND nvl(ssrmeet_bldg_code,'x') =  nvl('${ssrmeet_bldg_code}','x')  
              AND nvl(ssrmeet_room_code,'x') =  nvl('${ssrmeet_room_code}','x')  
              AND nvl(ssrmeet_catagory,'x') =  nvl('${ssrmeet_catagory}','x')  
              AND nvl(ssrmeet_sun_day,'x') =  nvl('${ssrmeet_sun_day}','x')  
              AND nvl(ssrmeet_mon_day,'x') =  nvl('${ssrmeet_mon_day}','x')  
              AND nvl(ssrmeet_tue_day,'x') =  nvl('${ssrmeet_tue_day}','x')  
              AND nvl(ssrmeet_wed_day,'x') =  nvl('${ssrmeet_wed_day}','x')  
               AND nvl(ssrmeet_thu_day,'x') =  nvl('${ssrmeet_thu_day}','x')  
              AND nvl(ssrmeet_fri_day,'x') =  nvl('${ssrmeet_fri_day}','x')  
               AND nvl(ssrmeet_sat_day,'x') =  nvl('${ssrmeet_sat_day}','x')  
               AND nvl(ssrmeet_schd_code,'x') =  nvl('${ssrmeet_schd_code}','x')  
               AND nvl(ssrmeet_over_ride,'x') =  nvl('${ssrmeet_over_ride}','x')  
               AND nvl(ssrmeet_func_code,'x') =  nvl('${ssrmeet_func_code}','x')  
                 AND nvl(ssrmeet_comt_code,'x') =  nvl('${ssrmeet_comt_code}','x')  
               AND nvl(ssrmeet_schs_code,'x') =  nvl('${ssrmeet_schs_code}','x')  
               AND nvl(ssrmeet_mtyp_code,'x') =  nvl('${ssrmeet_mtyp_code}','x') """


        try {
            conn.eachRow(findRow) {row ->
                findY = row.ssrmeet_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select SSRMEET ${this.ssrmeet_term_code} ${this.ssrmeet_crn} ${this.ssrmeet_days_code}"
                println "Problem executing select for table SSRMEET from ClassTimesDML.groovy: $e.message"
            }
        }
        if (!findY) {

            //  parm count is 30
            try {
                String API = "{call  gb_classtimes.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code  ssrmeet_term_code VARCHAR2
                insertCall.setString(1, this.ssrmeet_term_code)

                // parm 2 p_crn  ssrmeet_crn VARCHAR2
                insertCall.setString(2, this.ssrmeet_crn)

                // parm 3 p_days_code  ssrmeet_days_code VARCHAR2
                insertCall.setString(3, this.ssrmeet_days_code)

                // parm 4 p_day_number  ssrmeet_day_number NUMBER
                if ((this.ssrmeet_day_number == "") || (this.ssrmeet_day_number == null) || (!this.ssrmeet_day_number)) { insertCall.setNull(4, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(4, this.ssrmeet_day_number.toInteger())
                }

                // parm 5 p_begin_time  ssrmeet_begin_time VARCHAR2
                insertCall.setString(5, this.ssrmeet_begin_time)

                // parm 6 p_end_time  ssrmeet_end_time VARCHAR2
                insertCall.setString(6, this.ssrmeet_end_time)

                // parm 7 p_bldg_code  ssrmeet_bldg_code VARCHAR2
                insertCall.setString(7, this.ssrmeet_bldg_code)

                // parm 8 p_room_code  ssrmeet_room_code VARCHAR2
                insertCall.setString(8, this.ssrmeet_room_code)

                // parm 9 p_start_date  ssrmeet_start_date DATE
                if ((this.ssrmeet_start_date == "") || (this.ssrmeet_start_date == null) || (!this.ssrmeet_start_date)) { insertCall.setNull(9, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.ssrmeet_start_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(9, sqlDate)
                }

                // parm 10 p_end_date  ssrmeet_end_date DATE
                if ((this.ssrmeet_end_date == "") || (this.ssrmeet_end_date == null) || (!this.ssrmeet_end_date)) { insertCall.setNull(10, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.ssrmeet_end_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(10, sqlDate)
                }

                // parm 11 p_catagory  ssrmeet_catagory VARCHAR2
                insertCall.setString(11, this.ssrmeet_catagory)

                // parm 12 p_sun_day  ssrmeet_sun_day VARCHAR2
                insertCall.setString(12, this.ssrmeet_sun_day)

                // parm 13 p_mon_day  ssrmeet_mon_day VARCHAR2
                insertCall.setString(13, this.ssrmeet_mon_day)

                // parm 14 p_tue_day  ssrmeet_tue_day VARCHAR2
                insertCall.setString(14, this.ssrmeet_tue_day)

                // parm 15 p_wed_day  ssrmeet_wed_day VARCHAR2
                insertCall.setString(15, this.ssrmeet_wed_day)

                // parm 16 p_thu_day  ssrmeet_thu_day VARCHAR2
                insertCall.setString(16, this.ssrmeet_thu_day)

                // parm 17 p_fri_day  ssrmeet_fri_day VARCHAR2
                insertCall.setString(17, this.ssrmeet_fri_day)

                // parm 18 p_sat_day  ssrmeet_sat_day VARCHAR2
                insertCall.setString(18, this.ssrmeet_sat_day)

                // parm 19 p_schd_code  ssrmeet_schd_code VARCHAR2
                insertCall.setString(19, this.ssrmeet_schd_code)

                // parm 20 p_over_ride  ssrmeet_over_ride VARCHAR2
                insertCall.setString(20, this.ssrmeet_over_ride)

                // parm 21 p_credit_hr_sess  ssrmeet_credit_hr_sess NUMBER
                if ((this.ssrmeet_credit_hr_sess == "") || (this.ssrmeet_credit_hr_sess == null) || (!this.ssrmeet_credit_hr_sess)) { insertCall.setNull(21, java.sql.Types.DOUBLE) }
                else {
                    insertCall.setDouble(21, this.ssrmeet_credit_hr_sess.toDouble())
                }

                // parm 22 p_meet_no  ssrmeet_meet_no NUMBER
                if ((this.ssrmeet_meet_no == "") || (this.ssrmeet_meet_no == null) || (!this.ssrmeet_meet_no)) { insertCall.setNull(22, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(22, this.ssrmeet_meet_no.toInteger())
                }

                // parm 23 p_hrs_week  ssrmeet_hrs_week NUMBER
                if ((this.ssrmeet_hrs_week == "") || (this.ssrmeet_hrs_week == null) || (!this.ssrmeet_hrs_week)) { insertCall.setNull(23, java.sql.Types.DOUBLE) }
                else {
                    insertCall.setDouble(23, this.ssrmeet_hrs_week.toDouble())
                }

                // parm 24 p_func_code  ssrmeet_func_code VARCHAR2
                insertCall.setString(24, this.ssrmeet_func_code)

                // parm 25 p_comt_code  ssrmeet_comt_code VARCHAR2
                insertCall.setString(25, this.ssrmeet_comt_code)

                // parm 26 p_schs_code  ssrmeet_schs_code VARCHAR2
                insertCall.setString(26, this.ssrmeet_schs_code)

                // parm 27 p_mtyp_code  ssrmeet_mtyp_code VARCHAR2
                insertCall.setString(27, this.ssrmeet_mtyp_code)

                // parm 28 p_data_origin  ssrmeet_data_origin VARCHAR2
                insertCall.setString(28, connectInfo.dataOrigin)
                // parm 29 p_user_id  ssrmeet_user_id VARCHAR2
                insertCall.setString(29, connectInfo.userID)
                // parm 30 p_rowid_out  ssrmeet_rowid_out VARCHAR2
                insertCall.registerOutParameter(30, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SSRMEET ${this.ssrmeet_term_code} ${this.ssrmeet_crn} ${this.ssrmeet_days_code}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SSRMEET", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SSRMEET ${this.ssrmeet_term_code} ${this.ssrmeet_crn} ${this.ssrmeet_days_code}"
                        println "Problem executing insert for table SSRMEET from ClasTimesDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SSRMEET ${this.ssrmeet_term_code} ${this.ssrmeet_crn} ${this.ssrmeet_days_code}"
                    println "Problem executing insert for table SSRMEET from ClassTimesDML.groovy: $e.message"
                }
            }
        }
    }
}
