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
import java.sql.Connection

/**
 * EventSSBAvailabilityDML  makes an event's function and schedule open to use in Events SSB
 */
class EventSSBAvailabilityDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def eventCourseReferenceNumber
    def functionCode
    def updateMeetingTime


    public EventSSBAvailabilityDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EventSSBAvailabilityDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        makeItAvailableInEventsSSB()
    }


    def parseXmlData() {
        def availableInSSB = new XmlParser().parseText(xmlData)
        this.eventCourseReferenceNumber = availableInSSB.EVNT_CRN.text()
        this.functionCode = availableInSSB.FUNC_CODE.text()
        this.updateMeetingTime = availableInSSB.UPDATE_MEETING_TIME.text()
    }


    def makeItAvailableInEventsSSB() {
        def functionHeaderSql = "update gebfunc set gebfunc_date_publish_to=sysdate+10 where gebfunc_evnt_crn=? and gebfunc_func_code=? and trunc(gebfunc_date_publish_to)<trunc(sysdate)"
        try {
            conn.executeUpdate(functionHeaderSql, [this.eventCourseReferenceNumber, this.functionCode])
            connectInfo.tableUpdate("GEBFUNC", 0, 0, 1, 0, 0)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GEBFUNC", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update GEBFUNC ${this.eventCourseReferenceNumber} ${this.functionCode}"
                println "Problem executing insert for table GEBFUNC from EventSSBAvailabilityDML.groovy: $e.message"
            }
        }
        if (updateMeetingTime && updateMeetingTime=='YES') {
            def sectionMeetingTimeSql = "update ssrmeet set ssrmeet_end_date=sysdate+10 where ssrmeet_crn=? and ssrmeet_func_code=? and ssrmeet_term_code=?  and trunc(ssrmeet_end_date)<trunc(sysdate)"
            try {
                conn.executeUpdate(sectionMeetingTimeSql, [this.eventCourseReferenceNumber, this.functionCode,'EVENT'])
                connectInfo.tableUpdate("SSRMEET", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SSRMEET", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SSRMEET ${this.eventCourseReferenceNumber} ${this.functionCode}"
                    println "Problem executing insert for table SSRMEET from EventSSBAvailabilityDML.groovy: $e.message"
                }
            }
        }
    }
}
