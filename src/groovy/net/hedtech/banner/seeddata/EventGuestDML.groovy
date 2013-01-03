/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * EventGuestDML
 */
class EventGuestDML {

    def eventCourseReferenceNumber
    def functionCode
    def pidm
    def guestPidm
    def userid
    def dataOrigin
    def relationShipCode
    def gidm
    def guestGidm
    def genidenid
    def bannerid
    def guestGenidenid
    def guestBannerid
    def update
    def delete
    def create
    def activityDate

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public EventGuestDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EventGuestDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertGuestData()
    }


    def parseXmlData() {
        def gerfgst = new XmlParser().parseText(xmlData)
        this.eventCourseReferenceNumber = gerfgst.GERFGST_EVNT_CRN.text()
        this.functionCode = gerfgst.GERFGST_FUNC_CODE.text()
        this.pidm = gerfgst.GERFGST_PIDM.text()
        this.guestPidm = gerfgst.GERFGST_GUEST_PIDM.text()
        this.userid = gerfgst.GERFGST_USER_ID.text()
        this.dataOrigin = gerfgst.GERFGST_DATA_ORIGIN.text()
        this.relationShipCode = gerfgst.GERFGST_RELT_CODE.text()
        this.gidm = gerfgst.GERFGST_GIDM.text()
        this.guestGidm = gerfgst.GERFGST_GUEST_GIDM.text()
        this.delete = gerfgst.DELETE.text()
        this.bannerid = gerfgst.BANNERID.text()
        this.genidenid = gerfgst.GENIDEN_ID.text()
        this.guestBannerid = gerfgst.GUEST_BANNER_ID.text()
        this.guestGenidenid = gerfgst.GUEST_GENIDEN_ID.text()
        this.activityDate=gerfgst.GERFGST_ACTIVITY_DATE.text()
    }


    def insertGuestData() {
        if (this.delete && this.delete == "YES") {
            deleteData(this.genidenid, this.bannerid)
        } else {

            String gidmsql = """select geniden_gidm as gidmValue from geniden  where GENIDEN_ID = ?"""
            this.conn.eachRow(gidmsql, [this.genidenid]) {trow ->
                this.gidm = trow.gidmValue
            }
            this.conn.eachRow(gidmsql, [this.guestGenidenid]) {trow ->
                this.guestGidm = trow.gidmValue
            }

            String pidmsql = """select spriden_pidm as pidmValue from spriden  where SPRIDEN_ID = ? and rownum<=1"""
            this.conn.eachRow(pidmsql, [this.bannerid]) {trow ->
                this.pidm = trow.pidmValue
            }
            this.conn.eachRow(pidmsql, [this.guestBannerid]) {trow ->
                this.guestPidm = trow.pidmValue
            }

            //Check whether it is a create or update
            if (this.pidm) {
                String gerfgstPidmExistsSQL = null
                if (this.guestPidm) {
                    gerfgstPidmExistsSQL = """select 1 as guestCount from gerfgst  where gerfgst_EVNT_CRN = ? and gerfgst_func_code = ? and gerfgst_pidm = ? and gerfgst_guest_pidm = ?"""

                    this.conn.eachRow(gerfgstPidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode, this.pidm, this.guestPidm]) {trow ->
                        if (trow.guestCount == 1)
                            this.update = true
                    }
                } else if (this.guestGidm) {
                    gerfgstPidmExistsSQL = """select 1 as guestCount from gerfgst  where gerfgst_EVNT_CRN = ? and gerfgst_func_code = ? and gerfgst_pidm = ? and gerfgst_guest_gidm = ?"""
                    this.conn.eachRow(gerfgstPidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode, this.pidm, this.guestGidm]) {trow ->
                        if (trow.guestCount == 1)
                            this.update = true
                    }
                }


            }
            if (this.gidm) {
                String gerfgstGidmExistsSQL = null
                if (this.guestPidm) {
                    gerfgstGidmExistsSQL = """select 1 as guestCount from gerfgst  where gerfgst_EVNT_CRN = ? and gerfgst_func_code = ? and gerfgst_gidm = ? and gerfgst_guest_pidm = ?"""
                    this.conn.eachRow(gerfgstGidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode, this.gidm, this.guestPidm]) {trow ->
                        if (trow.guestCount == 1)
                            this.update = true
                    }
                } else if (this.guestGidm) {
                    gerfgstGidmExistsSQL = """select 1 as guestCount from gerfgst  where gerfgst_EVNT_CRN = ? and gerfgst_func_code = ? and gerfgst_gidm = ? and gerfgst_guest_gidm = ?"""
                    this.conn.eachRow(gerfgstGidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode, this.gidm, this.guestGidm]) {trow ->
                        if (trow.guestCount == 1)
                            this.update = true
                    }
                }
            }
            if (this.update) {
                def pidmUpdatesql = """update GERFGST set  GERFGST_USER_ID=?, GERFGST_RELT_CODE= ?
                                where GERFGST_EVNT_CRN=? and GERFGST_FUNC_CODE=? and GERFGST_PIDM=?"""

                def gidmUpdatesql = """update GERFGST set GERFGST_USER_ID=?, GERFGST_RELT_CODE= ?
                                       where GERFGST_EVNT_CRN=? and GERFGST_FUNC_CODE=? and GERFGST_GIDM=?"""
                if (this.pidm) {
                    try {
                        conn.executeUpdate(pidmUpdatesql, [this.userid,this.relationShipCode,
                                this.eventCourseReferenceNumber, this.functionCode, this.pidm])
                        connectInfo.tableUpdate("GERFGST", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GERFGST", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update GERFGST ${this.eventCourseReferenceNumber} ${this.functionCode}"
                            println "Problem executing insert for table GERFGST from EventGuestDML.groovy: $e.message"
                        }
                    }
                    //inTableName, long inReadCnt, long inInsertCnt, long inUpdateCnt, long inErrorCnt, long inDeleteCnt
                } else if (this.gidm) {
                    try {
                        conn.executeUpdate(gidmUpdatesql, [this.userid,this.relationShipCode
                                , this.eventCourseReferenceNumber, this.functionCode, this.gidm])
                        connectInfo.tableUpdate("GERFGST", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GERFGST", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update GERFGST ${this.genidenid}}"
                            println "Problem executing insert for table GERFGST from EventRegistrantDML.groovy: $e.message"
                        }
                    }
                }


            } else {
                def insertSQL = """insert into GERFGST (GERFGST_EVNT_CRN,GERFGST_FUNC_CODE,GERFGST_PIDM,GERFGST_GUEST_PIDM,GERFGST_ACTIVITY_DATE,
                      GERFGST_USER_ID,GERFGST_DATA_ORIGIN,GERFGST_RELT_CODE,GERFGST_GIDM,GERFGST_GUEST_GIDM) values (?,?,?,?,?,?,?,?,?,?)"""
                try {
                    conn.executeUpdate(insertSQL, [this.eventCourseReferenceNumber, this.functionCode, this.pidm, this.guestPidm,this.activityDate,
                    this.userid, this.dataOrigin, this.relationShipCode, this.gidm,this.guestGidm ])
                    connectInfo.tableUpdate("GERFGST", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GERFGST", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GERFGST ${this.eventCourseReferenceNumber}, ${this.functionCode}"
                        println "Problem executing insert for table GERFGST from EventGuestDML.groovy: $e.message"
                    }
                }
            }
        }
    }


    def deleteData(genidenid, bannerid) {
        if (genidenid)  {
            deleteData("GERFGST", """delete FROM GERFGST where GERFGST_GIDM in (select distinct geniden_gidm from geniden where geniden_id =?) and
                                     GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ? """, genidenid)
        }
        if (bannerid) {
            deleteData("GERFGST", """delete FROM GERFGST where GERFGST_PIDM in (select distinct spriden_pidm from spriden where spriden_id =?) and
                        GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ?   """, bannerid)
        }  else {
            deleteData("GERFGST", """delete FROM GERFGST where  GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ?""", null)
        }
    }


    private def deleteData(String tableName, String sql, String genidenid) {
        try {
            int delRows
            if (genidenid) {
                  delRows = conn.executeUpdate(sql, [genidenid, this.eventCourseReferenceNumber, this.functionCode])
            }  else {
                delRows = conn.executeUpdate(sql, [this.eventCourseReferenceNumber, this.functionCode])
            }

            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${gidm} from EventGuestDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }


}
