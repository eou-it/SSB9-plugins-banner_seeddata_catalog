/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by apoliski on 6/15/2016.
 */
class EmployeeJobEarningsDML {
    def bannerid
    def nbrearn_pidm
    def nbrearn_posn
    def nbrearn_suff
    def nbrearn_effective_date
    def nbrearn_earn_code
    def nbrearn_active_ind
    def nbrearn_hrs
    def nbrearn_special_rate
    def nbrearn_shift
    def nbrearn_activity_date
    def nbrearn_deemed_hrs
    def nbrearn_user_id
    def nbrearn_data_origin
    def nbrearn_surrogate_id
    def nbrearn_version
    def nbrearn_vpdi_code

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData
    def PIDM
    public EmployeeJobEarningsDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }
    public EmployeeJobEarningsDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployeeJobEarnings()
    }
    def parseXmlData() {
        def nbrearn = new XmlParser().parseText(xmlData)
        this.bannerid = nbrearn.BANNERID
        this.nbrearn_pidm = nbrearn.NBREARN_PIDM.text()
        this.nbrearn_posn = nbrearn.NBREARN_POSN.text()
        this.nbrearn_suff = nbrearn.NBREARN_SUFF.text()
        this.nbrearn_effective_date = nbrearn.NBREARN_EFFECTIVE_DATE.text()
        this.nbrearn_earn_code = nbrearn.NBREARN_EARN_CODE.text()
        this.nbrearn_active_ind = nbrearn.NBREARN_ACTIVE_IND.text()
        this.nbrearn_hrs = nbrearn.NBREARN_HRS.text()
        this.nbrearn_special_rate  = nbrearn.NBREARN_SPECIAL_RATE.text()
        this.nbrearn_shift = nbrearn.NBREARN_SHIFT.text()
        this.nbrearn_activity_date = nbrearn.NBREARN_ACTIVITY_DATE.text()
        this.nbrearn_deemed_hrs = nbrearn.NBREARN_DEEMED_HRS.text()
        this.nbrearn_user_id = nbrearn.NBREARN_USER_ID.text()
        this.nbrearn_data_origin = nbrearn.NBREARN_DATA_ORIGIN.text()
        this.nbrearn_surrogate_id  = nbrearn.NBREARN_SURROGATE_ID.text()
        this.nbrearn_version = nbrearn.NBREARN_VERSION.text()
        this.nbrearn_vpdi_code  = nbrearn.NBREARN_VPDI_CODE.text()
    }
    def processEmployeeJobEarnings() {
        PIDM = null
        String pidmsql = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeJobEarningsDML,  ${this.bannerid}  from SPRIDEN. $e.message"
            }
        }
        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate
        if (PIDM) {
            def findY = ""
            String findRow = """select 'Y' nbrearn_find from nbrearn where nbrearn_pidm = ?
                                and nbrearn_posn  = ?
                                and nbrearn_suff = ?
                                and trunc(nbrearn_effective_date) = trunc(to_date(?,'mm/dd/yyyy'))
                                and nbrearn_earn_code = ?
                                and nbrearn_active_ind = ?"""
            try {
                conn.eachRow(findRow, [PIDM, this.nbrearn_posn, this.nbrearn_suff, this.nbrearn_effective_date,
                                       this.nbrearn_earn_code, this.nbrearn_active_ind]) { row ->
                    findY = row.nbrearn_find
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("NBREARN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "${findRow}"
                    println "Problem with select for table NBREARN from EmployeeJobEarningsDML.groovy: $e.message"
                }
            }
            if (!findY) {
                try {
                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'OFF') }"
                    String API = "{call nb_job_earnings.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                    CallableStatement insertCall = this.connectCall.prepareCall(API)
                    insertCall.setInt(1, this.PIDM.toInteger())
                    insertCall.setString(2, this.nbrearn_posn)
                    insertCall.setString(3, this.nbrearn_suff)

                    if ((this.nbrearn_effective_date == "") || (this.nbrearn_effective_date == null) ||
                            (!this.nbrearn_effective_date)) {
                        insertCall.setNull(4, java.sql.Types.DATE)
                    } else {
                        ddate = new ColumnDateValue(this.nbrearn_effective_date)
                        unfDate = ddate.formatJavaDate()
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                        insertCall.setDate(4, sqlDate)
                    }
                    insertCall.setString(5, this.nbrearn_earn_code)
                    insertCall.setString(6, this.nbrearn_active_ind)

                    if ((this.nbrearn_hrs == "") || (this.nbrearn_hrs == null) ||
                            (!this.nbrearn_hrs)) {
                        insertCall.setNull(7, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(7, this.nbrearn_hrs.toDouble())
                    }

                    if ((this.nbrearn_special_rate == "") || (this.nbrearn_special_rate == null) ||
                            (!this.nbrearn_hrs)) {
                        insertCall.setNull(8, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(8, this.nbrearn_special_rate.toDouble())
                    }

                    insertCall.setInt(9, this.nbrearn_shift.toInteger())

                    if ((this.nbrearn_deemed_hrs == "") || (this.nbrearn_deemed_hrs == null) ||
                            (!this.nbrearn_deemed_hrs)) {
                        insertCall.setNull(10, java.sql.Types.DOUBLE)
                    } else {
                        insertCall.setDouble(10, this.nbrearn_deemed_hrs.toDouble())
                    }
                    insertCall.setString(11, this.nbrearn_user_id)
                    insertCall.setString(12, this.nbrearn_data_origin)
                    insertCall.registerOutParameter(13, java.sql.Types.ROWID)

                    try {
                        insertCall.executeUpdate()
                        connectInfo.tableUpdate("NBREARN", 0, 1, 0, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("NBREARN", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Insert NBREARN ${this.bannerid}}"
                            println "Problem executing insert for table NBREARN from EmployeeJobEarningsDML.groovy: $e.message"
                        }
                    }
                    finally {
                        insertCall.close()
                    }
                    conn.execute "{call nokglob.p_set_global ('HR_SECURITY_MODE', 'ON') }"
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("NBREARN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert  ${this.bannerid} ${this.nbrearn_posn} ${this.nbrearn_suff} ${this.nbrearn_earn_code} ${this.nbrearn_effective_date}"
                        println "Problem executing insert for table NBREARN from EmployeeJobEarningsDML.groovy: $e.message"
                    }
                }
            }

        }
    }



}
