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
 * RegistrationGroupRuleDML
 */
class RegistrationGroupRuleDML {

    def ruleSeqNum
    def blockCode
    def termCodeInit
    def blockAssignInd
    def courseRestriction
    def classCode
    def rulePriority
    def college
    def dept
    def majr
    def camp
    def degc
    def levl
    def prog
    def atts
    def mandInd
    def blockReg
    def status
    def activityDate
    def id
    def userid
    def version
    def lastModified
    def lastModifiedBy
    def dataOrigin

    def delete
    def update
    def create


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public RegistrationGroupRuleDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public RegistrationGroupRuleDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertRuleData()
    }


    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)
        this.termCodeInit = rule.SFRBRDH_TERM_CODE_INIT.text()
        this.classCode = rule.SFRBRDH_CLAS_CODE.text()
        this.rulePriority = rule.SFRBRDH_PRIORITY.text()
        this.mandInd = rule.SFRBRDH_MANDATORY_IND.text()
        this.blockReg = rule.SFRBRDH_BLOCK_RESTRICTION_IND.text()
        this.status = rule.SFRBRDH_STATUS_IND.text()
        this.college = rule.SFRBRDH_COLL_CODE.text()
        this.dept = rule.SFRBRDH_DEPT_CODE.text()
        this.majr = rule.SFRBRDH_MAJR_CODE.text()
        this.camp = rule.SFRBRDH_CAMP_CODE.text()
        this.degc = rule.SFRBRDH_DEGC_CODE.text()
        this.levl = rule.SFRBRDH_LEVL_CODE.text()
        this.prog = rule.SFRBRDH_PROG_CODE.text()
        this.atts = rule.SFRBRDH_ATTS_CODE.text()
        this.userid = rule.SFRBRDH_USER_ID.text()
        this.dataOrigin = rule.SFRBRDH_DATA_ORIGIN.text()
        this.activityDate = rule.SFRBRDH_ACTIVITY_DATE.text()
    }


    def insertRuleData() {
        this.update = false
        String ruleSql = """select sfrbrdh_seq_num  as seqValue from sfrbrdh  where SFRBRDH_PRIORITY = ? and SFRBRDH_TERM_CODE_INIT = ? """
        def params = [this.rulePriority, this.termCodeInit]

        if (this.classCode) {
            ruleSql += """ and SFRBRDH_CLAS_CODE = ?"""
            params.add(this.classCode)
        }
        else
            ruleSql += """ and SFRBRDH_CLAS_CODE is null"""
        if (this.college) {
            ruleSql += """ and SFRBRDH_COLL_CODE = ?"""
            params.add(this.college)
        }
        else {
            ruleSql += """ and SFRBRDH_COLL_CODE is null"""
        }
        if (this.dept) {
            ruleSql += """ and SFRBRDH_DEPT_CODE = ?"""
            params.add(this.dept)
        }
        else {
            ruleSql += """ and SFRBRDH_DEPT_CODE is null"""
        }
        if (this.majr) {
            ruleSql += """ and SFRBRDH_MAJR_CODE = ?"""
            params.add(this.majr)
        }
        else {
            ruleSql += """ and SFRBRDH_MAJR_CODE is null"""
        }
        if (this.camp) {
            ruleSql += """ and SFRBRDH_CAMP_CODE = ?"""
            params.add(this.camp)
        }
        else {
            ruleSql += """ and SFRBRDH_CAMP_CODE is null"""
        }
        if (this.levl) {
            ruleSql += """ and SFRBRDH_LEVL_CODE = ?"""
            params.add(this.levl)
        }
        else {
            ruleSql += """ and SFRBRDH_LEVL_CODE is null"""
        }
        if (this.prog) {
            ruleSql += """ and SFRBRDH_PROG_CODE = ?"""
            params.add(this.prog)
        }
        else {
            ruleSql += """ and SFRBRDH_PROG_CODE is null"""
        }
        if (this.degc) {
            ruleSql += """ and SFRBRDH_DEGC_CODE = ?"""
            params.add(this.degc)
        }
        else {
            ruleSql += """ and SFRBRDH_DEGC_CODE is null"""
        }
        if (this.atts) {
            ruleSql += """ and SFRBRDH_ATTS_CODE = ?"""
            params.add(this.atts)
        }
        else {
            ruleSql += """ and SFRBRDH_ATTS_CODE is null"""
        }

        this.conn.eachRow(ruleSql, params)
                {
                    trow ->
                    this.ruleSeqNum = trow.seqValue
                }
        if (this.delete && this.delete == "YES") {
            deleteData(this.ruleSeqNum, this.blockCode)
        } else {
            //Check whether it is a create or update
            if (this.ruleSeqNum) {
                this.update = true
            }
            if (this.update) {
                def blockUpdatesql = """update SFRBRDH set SFRBRDH_PRIORITY = ?,SFRBRDH_TERM_CODE_INIT = ?,SFRBRDH_CLAS_CODE = ?,SFRBRDH_COLL_CODE = ?,
                                                        SFRBRDH_DEPT_CODE = ? ,SFRBRDH_MAJR_CODE = ?,SFRBRDH_CAMP_CODE = ? ,SFRBRDH_LEVL_CODE = ?,SFRBRDH_PROG_CODE = ?,
                                                        SFRBRDH_DEGC_CODE = ? ,SFRBRDH_ATTS_CODE =? where SFRBRDH_SEQ_NUM =?"""
                try {
                    conn.executeUpdate(blockUpdatesql, [this.rulePriority, this.termCodeInit, this.classCode, this.college, this.dept, this.majr, this.camp, this.levl, this.prog, this.degc, this.atts, this.ruleSeqNum])
                    connectInfo.tableUpdate("SFRBRDH", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBRDH", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SFRBRDH ${this.ruleSeqNum}}"
                        println "Problem executing insert for table SFRBRDH from RegistrationGroupRuleDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into SFRBRDH (SFRBRDH_PRIORITY,SFRBRDH_TERM_CODE_INIT,SFRBRDH_CLAS_CODE,SFRBRDH_COLL_CODE,
                                                                        SFRBRDH_DEPT_CODE,SFRBRDH_MAJR_CODE,SFRBRDH_CAMP_CODE,SFRBRDH_LEVL_CODE,SFRBRDH_PROG_CODE,
                                                                        SFRBRDH_DEGC_CODE,SFRBRDH_ATTS_CODE,SFRBRDH_MANDATORY_IND,SFRBRDH_BLOCK_RESTRICTION_IND,SFRBRDH_STATUS_IND,
                                                                        SFRBRDH_ACTIVITY_DATE,SFRBRDH_USER_ID,SFRBRDH_DATA_ORIGIN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
                try {
                    conn.executeUpdate(insertSQL, [this.rulePriority, this.termCodeInit, this.classCode, this.college, this.dept, this.majr, this.camp, this.levl, this.prog,
                            this.degc, this.atts, this.mandInd, this.blockReg, this.status, this.activityDate, this.userid, this.dataOrigin])
                    connectInfo.tableUpdate("SFRBRDH", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBRDH", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        //println "Insert SFRBRDH ${this.blockCode}}"
                        println "Problem executing insert for table SFRBRDH from RegistrationGroupRuleDML.groovy: $e.message"
                    }
                }
            }
        }
    }


    def deleteData(ruleSeqNum, blockCode) {

        deleteData("SFRBRDH", """delete FROM SFRBRDH where SFRBRDH_SEQ_NUM = ? """, ruleSeqNum)
    }


    private def deleteData(String tableName, String sql, String ruleSeqNum) {
        try {
            int delRows

            delRows = conn.executeUpdate(sql, [ruleSeqNum])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${ruleSeqNum} from RegistrationGroupRuleDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
