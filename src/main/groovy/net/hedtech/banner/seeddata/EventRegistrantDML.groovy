/*********************************************************************************
  Copyright 2010-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata


import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * EventRegistrantDML
 */
class EventRegistrantDML {

    def activityDate
    def id
    def userid
    def version
    def pidm
    def gidm
    def numberOfGuests
    def guestsAttended
    def lastModified
    def lastModifiedBy
    def dataOrigin
    def eventCourseReferenceNumber
    def functionCode
    def involveIndicator
    def attendanceIndicator
    def planToAttendIndicator
    def feeDate
    def rsvpDate
    def ticketCount
    def invitationSent
    def dateSent
    def nameTagName
    def placeCardName
    def commentData
    def registrantCommentData
    def rsvpCode
    def feeStatusCode
    def addressTypeCode
    def menuCode
    def registeredFrom
    def genidenid
    def bannerid
    def feeDescription
    def delete
    def update
    def create


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public EventRegistrantDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EventRegistrantDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertRegistrantData()
    }


    def parseXmlData() {
        def gerattd = new XmlParser().parseText(xmlData)
        this.eventCourseReferenceNumber = gerattd.GERATTD_EVNT_CRN.text()
        this.functionCode = gerattd.GERATTD_FUNC_CODE.text()
        this.bannerid = gerattd.BANNERID.text()
        this.rsvpCode = gerattd.GERATTD_RSVP_CODE.text()
        this.rsvpDate = gerattd.GERATTD_RSVP_DATE.text()
        this.feeStatusCode = gerattd.GERATTD_FEES_CODE.text()
        this.feeDate = gerattd.GERATTD_FEE_DATE.text()
        this.addressTypeCode = gerattd.GERATTD_ATYP_CODE.text()
        this.nameTagName = gerattd.GERATTD_NAME_TAG_NAME.text()
        this.placeCardName = gerattd.GERATTD_PLACE_CARD_NAME.text()
        this.ticketCount = gerattd.GERATTD_TICKET_CNT.text()
        this.attendanceIndicator = gerattd.GERATTD_ATTENDANCE_IND.text()
        this.involveIndicator = gerattd.GERATTD_INVOLVE_IND.text()
        this.commentData = gerattd.GERATTD_COMMENT.text()
        this.feeDescription = gerattd.GERATTD_FEES_DESC.text()
        this.userid = gerattd.GERATTD_USER_ID.text()
        this.dataOrigin = gerattd.GERATTD_DATA_ORIGIN.text()
        this.numberOfGuests = gerattd.GERATTD_NUMBER_OF_GUESTS.text()
        this.guestsAttended = gerattd.GERATTD_GUESTS_ATTENDED.text()
        this.genidenid = gerattd.GENIDEN_ID.text()
        this.registeredFrom = gerattd.GERATTD_REGD_FROM.text()
        this.registrantCommentData = gerattd.GERATTD_REGISTRANT_COMMENT.text()
        this.delete = gerattd.DELETE.text()
        this.menuCode = gerattd.GERATTD_MENU_CODE.text()
        this.activityDate = gerattd.GERATTD_ACTIVITY_DATE.text()
    }


    def insertRegistrantData() {
        if (this.delete && this.delete == "YES") {
             deleteData(this.genidenid, this.bannerid)
        } else {

            String gidmsql = """select geniden_gidm as gidmValue from geniden  where GENIDEN_ID = ?"""
            this.conn.eachRow(gidmsql, [this.genidenid]) {trow ->
                this.gidm = trow.gidmValue
            }

            String pidmsql = """select spriden_pidm as pidmValue from spriden  where SPRIDEN_ID = ? and rownum<=1"""
            this.conn.eachRow(pidmsql, [this.bannerid]) {trow ->
                this.pidm = trow.pidmValue
            }
              //Check whether it is a create or update
            if (this.pidm) {
                String gerattdPidmExistsSQL = """select 1 as inviteCount from gerattd  where GERATTD_EVNT_CRN = ? and gerattd_func_code = ? and gerattd_pidm = ?"""
                this.conn.eachRow(gerattdPidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode,this.pidm]) {trow ->
                    if (trow.inviteCount == 1)
                         this.update = true
                }
            }
            if (this.gidm){
                String gerattdGidmExistsSQL = """select 1 as inviteCount from gerattd  where GERATTD_EVNT_CRN = ? and gerattd_func_code = ? and gerattd_gidm = ?"""
                this.conn.eachRow(gerattdGidmExistsSQL, [this.eventCourseReferenceNumber, this.functionCode,this.gidm]) {trow ->
                    if (trow.inviteCount == 1)
                         this.update = true
                }
            }
            if (this.update) {
                def pidmUpdatesql = """update GERATTD set GERATTD_RSVP_CODE=?, GERATTD_RSVP_DATE=?, GERATTD_FEES_CODE=?, GERATTD_FEE_DATE=?,
                                        GERATTD_ATYP_CODE=?,GERATTD_NAME_TAG_NAME=?,GERATTD_PLACE_CARD_NAME=?,GERATTD_TICKET_CNT=?,
                                        GERATTD_MENU_CODE=?,GERATTD_ATTENDANCE_IND=?,GERATTD_INVOLVE_IND=?, GERATTD_COMMENT=?, GERATTD_FEES_DESC=?,
                                        GERATTD_USER_ID=?,GERATTD_DATA_ORIGIN=?,GERATTD_NUMBER_OF_GUESTS=?,GERATTD_GUESTS_ATTENDED=?,
                                        GERATTD_REGD_FROM=?, GERATTD_REGISTRANT_COMMENT=? where GERATTD_EVNT_CRN=? and GERATTD_FUNC_CODE=? and GERATTD_PIDM=?"""

                def gidmUpdatesql = """update GERATTD set GERATTD_RSVP_CODE=?, GERATTD_RSVP_DATE=?, GERATTD_FEES_CODE=?, GERATTD_FEE_DATE=?,
                                        GERATTD_ATYP_CODE=?,GERATTD_NAME_TAG_NAME=?,GERATTD_PLACE_CARD_NAME=?,GERATTD_TICKET_CNT=?,
                                        GERATTD_MENU_CODE=?,GERATTD_ATTENDANCE_IND=?,GERATTD_INVOLVE_IND=?, GERATTD_COMMENT=?, GERATTD_FEES_DESC=?,
                                        GERATTD_USER_ID=?,GERATTD_DATA_ORIGIN=?,GERATTD_NUMBER_OF_GUESTS=?,GERATTD_GUESTS_ATTENDED=?,
                                        GERATTD_REGD_FROM=?, GERATTD_REGISTRANT_COMMENT=? where GERATTD_EVNT_CRN=? and GERATTD_FUNC_CODE=? and GERATTD_GIDM=?"""
                if (this.pidm) {
                    try {
                        conn.executeUpdate(pidmUpdatesql, [this.rsvpCode, this.rsvpDate, this.feeStatusCode, this.feeDate, this.addressTypeCode,
                                this.nameTagName, this.placeCardName, this.ticketCount, this.menuCode,
                                this.attendanceIndicator, this.involveIndicator, this.commentData, this.feeDescription,
                                this.userid, this.dataOrigin, this.numberOfGuests, this.guestsAttended, this.registeredFrom,
                                this.registrantCommentData, this.eventCourseReferenceNumber, this.functionCode, this.pidm])
                        connectInfo.tableUpdate("GERATTD", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GERATTD", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update GERATTD ${this.bannerid} crn ${this.eventCourseReferenceNumber} func ${this.functionCode}  "
                            println "Problem executing insert for table GERATTD from EventRegistrantDML.groovy: $e.message"
                        }
                    }
                    //inTableName, long inReadCnt, long inInsertCnt, long inUpdateCnt, long inErrorCnt, long inDeleteCnt
                } else if (this.gidm) {
                    try {
                        conn.executeUpdate(gidmUpdatesql, [this.rsvpCode, this.rsvpDate, this.feeStatusCode, this.feeDate, this.addressTypeCode,
                                this.nameTagName, this.placeCardName, this.ticketCount, this.menuCode,
                                this.attendanceIndicator, this.involveIndicator, this.commentData, this.feeDescription,
                                this.userid, this.dataOrigin, this.numberOfGuests, this.guestsAttended, this.registeredFrom,
                                this.registrantCommentData, this.eventCourseReferenceNumber, this.functionCode, this.gidm])
                        connectInfo.tableUpdate("GERATTD", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GERATTD", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update GERATTD ${this.genidenid} crn ${this.eventCourseReferenceNumber} func ${this.functionCode} "
                            println "Problem executing insert for table GERATTD from EventRegistrantDML.groovy: $e.message"
                        }
                    }
                }


            } else {
                  def insertSQL= """insert into gerattd (GERATTD_EVNT_CRN,GERATTD_FUNC_CODE,GERATTD_PIDM,GERATTD_RSVP_CODE,GERATTD_RSVP_DATE,
                  GERATTD_FEES_CODE,GERATTD_FEE_DATE,GERATTD_ATYP_CODE,GERATTD_NAME_TAG_NAME,GERATTD_PLACE_CARD_NAME,GERATTD_TICKET_CNT,GERATTD_MENU_CODE,
                  GERATTD_ATTENDANCE_IND,GERATTD_INVOLVE_IND,GERATTD_COMMENT,GERATTD_FEES_DESC,GERATTD_USER_ID,GERATTD_DATA_ORIGIN,
                  GERATTD_NUMBER_OF_GUESTS,GERATTD_GUESTS_ATTENDED,GERATTD_GIDM,GERATTD_REGD_FROM,GERATTD_REGISTRANT_COMMENT,GERATTD_ACTIVITY_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
                try {
                    conn.executeUpdate(insertSQL, [this.eventCourseReferenceNumber, this.functionCode, this.pidm, this.rsvpCode, this.rsvpDate,
                                                  this.feeStatusCode, this.feeDate, this.addressTypeCode, this.nameTagName, this.placeCardName,
                                                  this.ticketCount, this.menuCode, this.attendanceIndicator, this.involveIndicator, this.commentData,
                                                  this.feeDescription, this.userid, this.dataOrigin, this.numberOfGuests, this.guestsAttended,
                                                  this.gidm, this.registeredFrom,this.registrantCommentData,this.activityDate])
                    connectInfo.tableUpdate("GERATTD", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GERATTD", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GERATTD ${this.genidenid} "
                        println "Problem executing insert for table GERATTD from EventRegistrantDML.groovy: $e.message"
                    }
                }
            }
        }
    }


    def deleteData(genidenid, bannerid) {
        if (genidenid)  {
            deleteData("GERFGST", """delete FROM GERFGST where GERFGST_GIDM in (select distinct geniden_gidm from geniden where geniden_id =?) and
                                     GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ? """, genidenid)
            deleteData("GERATTD", """delete FROM GERATTD where GERATTD_GIDM in (select distinct geniden_gidm from geniden where geniden_id =?) and
                                    GERATTD_EVNT_CRN = ? and GERATTD_FUNC_CODE = ? """, genidenid)
        }  else if (bannerid) {
            deleteData("GERFGST", """delete FROM GERFGST where GERFGST_PIDM in (select distinct spriden_pidm from spriden where spriden_id =?) and
                        GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ?   """, bannerid)
            deleteData("GERATTD", """delete FROM GERATTD where GERATTD_PIDM  in (select distinct spriden_pidm from spriden where spriden_id =?)
                        GERATTD_EVNT_CRN = ? and GERATTD_FUNC_CODE = ? """, bannerid)
        }  else {
            deleteData("GERFGST", """delete FROM GERFGST where  GERFGST_EVNT_CRN = ? and GERFGST_FUNC_CODE = ?""", null)
            deleteData("GERATTD", """delete FROM GERATTD where GERATTD_EVNT_CRN = ? and GERATTD_FUNC_CODE = ? """, null)
        }

    }


    def deleteData(String tableName, String sql, String genidenid) {
        try {
            int delRows
            if (genidenid) {
               delRows = conn.executeUpdate(sql, [genidenid, this.eventCourseReferenceNumber, this.functionCode])
            } else {
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
