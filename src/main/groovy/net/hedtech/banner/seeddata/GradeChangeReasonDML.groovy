package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GradeChangeReasonDML {

    def code
    def description
    def permitDuplicate
    def egbIndicator
    def academicHistoryIndicator
    def calculatedIndicator
    def reasssessmentIndicator
    def exemptIndicator
    def resitIndicator
    def definitiveIndicator
    def gcatCode
    def systemRequiredIndicator
    def userId
    def dataOrigin
    def activityDate

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData

    boolean update = false

    def selectSQL = """SELECT STVGCHG_CODE FROM STVGCHG WHERE STVGCHG_CODE=?"""
    def insertSQL = """INSERT INTO STVGCHG (STVGCHG_CODE, STVGCHG_DESC, STVGCHG_PERMIT_DUP_IND, STVGCHG_EGB_IND, STVGCHG_ACADHIST_IND, STVGCHG_CALCULATED_IND, STVGCHG_REAS_GRDE_IND, STVGCHG_EXEMPT_IND, STVGCHG_RESIT_IND, STVGCHG_DEFINITIVE_IND, STVGCHG_GCAT_CODE, STVGCHG_SYSTEM_REQ_IND, STVGCHG_USER_ID, STVGCHG_DATA_ORIGIN, STVGCHG_ACTIVITY_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
    def updateSQL = """UPDATE STVGCHG  SET STVGCHG_CODE=?, STVGCHG_DESC=?, STVGCHG_PERMIT_DUP_IND=?, STVGCHG_EGB_IND=?, STVGCHG_ACADHIST_IND=?, STVGCHG_CALCULATED_IND=?, STVGCHG_REAS_GRDE_IND=?, STVGCHG_EXEMPT_IND=?, STVGCHG_RESIT_IND=?, STVGCHG_DEFINITIVE_IND=?, STVGCHG_GCAT_CODE=?, STVGCHG_SYSTEM_REQ_IND=?, STVGCHG_USER_ID=?, STVGCHG_DATA_ORIGIN=?, STVGCHG_ACTIVITY_DATE=? WHERE STVGCHG_CODE=?"""

    public GradeChangeReasonDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        updateGradeChangeReason()
    }

    def parseXmlData() {
        def rowData = new XmlParser().parseText( xmlData )

        code = rowData.STVGCHG_CODE.text()
        description = rowData.STVGCHG_DESC.text()
        permitDuplicate = rowData.STVGCHG_PERMIT_DUP_IND.text()
        egbIndicator = rowData.STVGCHG_EGB_IND.text()
        academicHistoryIndicator = rowData.STVGCHG_ACADHIST_IND.text()
        calculatedIndicator = rowData.STVGCHG_CALCULATED_IND.text()
        reasssessmentIndicator = rowData.STVGCHG_REAS_GRDE_IND.text()
        exemptIndicator = rowData.STVGCHG_EXEMPT_IND.text()
        resitIndicator = rowData.STVGCHG_RESIT_IND.text()
        definitiveIndicator = rowData.STVGCHG_DEFINITIVE_IND.text()
        gcatCode = rowData.STVGCHG_GCAT_CODE.text()
        systemRequiredIndicator = rowData.STVGCHG_SYSTEM_REQ_IND.text()
        userId = rowData.STVGCHG_USER_ID.text()
        dataOrigin = rowData.STVGCHG_DATA_ORIGIN.text()
        activityDate = rowData.STVGCHG_ACTIVITY_DATE.text()
    }

    def updateGradeChangeReason() {

        this.conn.eachRow(selectSQL, [code]) { trow ->
            //println "Found STVGCHG Record for ${code}}"
            update = true
        }

        try {
            if (update) {
                conn.executeUpdate(updateSQL, [code, description, permitDuplicate, egbIndicator, academicHistoryIndicator,
                                               calculatedIndicator, reasssessmentIndicator, exemptIndicator, resitIndicator,
                                               definitiveIndicator, gcatCode, systemRequiredIndicator, userId, dataOrigin, activityDate, code])
                connectInfo.tableUpdate("STVGCHG", 0, 0, 1, 0, 0)
                //println "Updated STVGCHG Record for ${ code }}"
            }
            else {
                conn.executeUpdate(insertSQL, [code, description, permitDuplicate, egbIndicator, academicHistoryIndicator,
                                               calculatedIndicator, reasssessmentIndicator, exemptIndicator, resitIndicator,
                                               definitiveIndicator, gcatCode, systemRequiredIndicator, userId, dataOrigin, activityDate])
                connectInfo.tableUpdate("STVGCHG", 0, 1, 0, 0, 0)
                //println "Inserted STVGCHG Record for ${ code }}"
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("STVGCHG", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing" + update? " UPDATE " : " INSERT " + "STVGCHG Record for ${ code }}"
            }
        }
    }
}
