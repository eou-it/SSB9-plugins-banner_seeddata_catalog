/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/** *******************************************************************************
 Copyright 2012 Ellucian Company L.P. and its affiliates.
 ******************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * RegistrationBlockPreAssignmentDML
 */
class RegistrationBlockPreAssignmentDML {

    def ruleSeqNum
    def id
    def version
    def lastModified
    def lastModifiedBy
    def dataOrigin
    def pidm
    def termCode
    def studyPathSequenceNumber
    def registrationPermitOverrideCode
    def blockCode
    def program
    def registrationGroupRule
    def rulePriority
    def classCode
    def collegeCode
    def departmentCode
    def majorCode
    def campusCode
    def degreeCode
    def levelCode
    def programCode
    def attributeCode
    def activityDate
    def userid

    def delete
    def update
    def create

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData

    public RegistrationBlockPreAssignmentDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public RegistrationBlockPreAssignmentDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertPreAssignmentData()
    }


    def parseXmlData() {
        def preAssignment = new XmlParser().parseText(xmlData)

        this.delete = preAssignment.DELETE?.text()

        this.termCode = preAssignment.SFRBLPA_TERM_CODE.text()
        //this.pidm = preAssignment.BANNERID.text()
        this.pidm = connectInfo.saveStudentPidm
        this.blockCode = preAssignment.SFRBLPA_BLCK_CODE.text()
        this.studyPathSequenceNumber = preAssignment.SFRBLPA_KEY_SEQNO.text()
        this.registrationPermitOverrideCode = preAssignment.SFRBLPA_ROVR_CODE.text()
        this.lastModifiedBy = preAssignment.SFRBLPA_USER_ID.text()
        this.registrationGroupRule = preAssignment.SFRBLPA_BRDH_SEQ_NUM.text()
        this.program = preAssignment.SFRBLPA_PROGRAM.text()
        this.dataOrigin = connectInfo.dataOrigin
        this.lastModified = "01012010"
        this.rulePriority = preAssignment.PRIORITY.text()
        this.classCode = preAssignment.CLASS_CODE.text()
        this.collegeCode = preAssignment.COLL_CODE.text()
        this.departmentCode = preAssignment.DEPT_CODE.text()
        this.majorCode = preAssignment.MAJR_CODE.text()
        this.campusCode = preAssignment.CAMP_CODE.text()
        this.degreeCode = preAssignment.DEGC_CODE.text()
        this.levelCode = preAssignment.LEVL_CODE.text()
        this.programCode = preAssignment.PROGRAM.text()
        this.attributeCode = preAssignment.ATTS_CODE.text()
        this.userid = connectInfo.dataOrigin
        this.dataOrigin = connectInfo.dataOrigin
        this.activityDate = '01012010'
    }


    def insertPreAssignmentData() {
        this.update = false
        if (this.delete && this.delete == "YES") {
            deleteData(this.pidm.toBigDecimal(), this.termCode, this.studyPathSequenceNumber.toInteger())
        } else {
            //Check whether it is a create or update
            if (connectInfo.saveStudentPidm) {
                String preAssignmentExistsSQL = """select 1 as studentCount from SFRBLPA where SFRBLPA_PIDM = ? and SFRBLPA_TERM_CODE = ? and SFRBLPA_KEY_SEQNO = ? """
                if (connectInfo.debugThis) println "block preAssignment ${preAssignmentExistsSQL}"
                this.conn.eachRow(preAssignmentExistsSQL, [connectInfo.saveStudentPidm, this.termCode, this.studyPathSequenceNumber]) {trow ->
                    if (trow.studentCount == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def preAssignmentUpdateSQL = """ update SFRBLPA set SFRBLPA_BLCK_CODE = ?, SFRBLPA_ROVR_CODE = ?,
                                                 SFRBLPA_USER_ID = ?, SFRBLPA_DATA_ORIGIN = ?, SFRBLPA_ACTIVITY_DATE = to_date(?,'MMDDYYYY')
                                                 where SFRBLPA_PIDM = ? and SFRBLPA_TERM_CODE = ? and SFRBLPA_KEY_SEQNO = ? """
                try {
                    conn.executeUpdate(preAssignmentUpdateSQL, [this.blockCode, this.registrationPermitOverrideCode,
                            this.userid, this.dataOrigin, this.activityDate, connectInfo.saveStudentPidm, this.termCode, this.studyPathSequenceNumber])
                    connectInfo.tableUpdate("SFRBLPA", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBLPA", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SFRBLPA ${this.blockCode}}"
                        println "Problem executing update for table SFRBLPA from RegistrationBlockPreAssignmentDML.groovy: $e.message"
                    }
                }
            }
            else {

                def insertSql = """insert into SFRBLPA (SFRBLPA_PIDM, SFRBLPA_TERM_CODE, SFRBLPA_BLCK_CODE, SFRBLPA_KEY_SEQNO, SFRBLPA_ROVR_CODE, SFRBLPA_USER_ID,
                                        SFRBLPA_ACTIVITY_DATE, SFRBLPA_BRDH_SEQ_NUM, SFRBLPA_PROGRAM, SFRBLPA_DATA_ORIGIN)
                                        values (?, ?, ?, ?, ?, ?, to_date(?,'MMDDYYYY'), ?, ?, ?)"""

                if (connectInfo.debugThis) println insertSql

                try {

                    String getRuleSeqSQL = """select SFRBRDH_SEQ_NUM  as ruleSeqNum from SFRBRDH  where SFRBRDH_TERM_CODE = ? """
                    def params = [this.termCode]

                    if (this.rulePriority) {
                        getRuleSeqSQL += """ and SFRBRDH_PRIORITY = ?"""
                        params.add(this.rulePriority)
                    }
                    else
                        getRuleSeqSQL += """ and SFRBRDH_PRIORITY is null"""
                    if (this.classCode) {
                        getRuleSeqSQL += """ and SFRBRDH_CLAS_CODE = ?"""
                        params.add(this.classCode)
                    }
                    else
                        getRuleSeqSQL += """ and SFRBRDH_CLAS_CODE is null"""
                    if (this.collegeCode) {
                        getRuleSeqSQL += """ and SFRBRDH_COLL_CODE = ?"""
                        params.add(this.collegeCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_COLL_CODE is null"""
                    }
                    if (this.departmentCode) {
                        getRuleSeqSQL += """ and SFRBRDH_DEPT_CODE = ?"""
                        params.add(this.departmentCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_DEPT_CODE is null"""
                    }
                    if (this.majorCode) {
                        getRuleSeqSQL += """ and SFRBRDH_MAJR_CODE = ?"""
                        params.add(this.majorCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_MAJR_CODE is null"""
                    }
                    if (this.campusCode) {
                        getRuleSeqSQL += """ and SFRBRDH_CAMP_CODE = ?"""
                        params.add(this.campusCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_CAMP_CODE is null"""
                    }
                    if (this.levelCode) {
                        getRuleSeqSQL += """ and SFRBRDH_LEVL_CODE = ?"""
                        params.add(this.levelCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_LEVL_CODE is null"""
                    }
                    if (this.programCode) {
                        getRuleSeqSQL += """ and SFRBRDH_PROGRAM = ?"""
                        params.add(this.programCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_PROGRAM is null"""
                    }
                    if (this.degreeCode) {
                        getRuleSeqSQL += """ and SFRBRDH_DEGC_CODE = ?"""
                        params.add(this.degreeCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_DEGC_CODE is null"""
                    }
                    if (this.attributeCode) {
                        getRuleSeqSQL += """ and SFRBRDH_ATTS_CODE = ?"""
                        params.add(this.attributeCode)
                    }
                    else {
                        getRuleSeqSQL += """ and SFRBRDH_ATTS_CODE is null"""
                    }

                    conn.eachRow(getRuleSeqSQL, params) {trow ->
                        ruleSeqNum = trow.ruleSeqNum
                    }

                    conn.executeUpdate(insertSql, [connectInfo.saveStudentPidm, this.termCode, this.blockCode, this.studyPathSequenceNumber, this.registrationPermitOverrideCode, this.lastModifiedBy,
                            this.lastModified, ruleSeqNum, this.program, this.dataOrigin])
                    connectInfo.tableUpdate("SFRBLPA", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBLPA", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Problem executing insert for table SFRBLPA from RegistrationBlockPreAssignmentDML.groovy: $e.message"
                    }
                }
            }
        }
    }


    def deleteData(studentPidm, studentTermCode, studentStudyPath) {
        deleteData("SFRBLPA", """delete FROM SFRBLPA where SFRBLPA_PIDM = ? and SFRBLPA_TERM_CODE = ? and SFRBLPA_KEY_SEQNO = ? """,
                studentPidm, studentTermCode, studentStudyPath)
    }


    def deleteData(String tableName, String sql, BigDecimal studentPidm, String studentTermCode, Integer studentStudyPath) {
        try {
            int delRows
            if (connectInfo.debugThis) println sql
            delRows = conn.executeUpdate(sql, [studentPidm, studentTermCode, studentStudyPath])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${blockCode} from RegistrationBlockPreAssignmentDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }
}
