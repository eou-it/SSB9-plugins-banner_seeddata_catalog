/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * RegistrationGroupRuleDetailDML
 */
class RegistrationGroupRuleDetailDML {

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


    public RegistrationGroupRuleDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public RegistrationGroupRuleDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertRuleDetailData()
    }


    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)
        this.blockCode = rule.SFRBRDB_BLCK_CODE.text()
        this.termCodeInit = rule.SFRBRDB_TERM_CODE_EFF.text()
        this.blockAssignInd = rule.SFRBRDB_ASSIGN_IND.text()
        this.courseRestriction = rule.SFRBRDB_COURSE_RESTRICTION.text()
        this.classCode = rule.SFRBRDB_CLAS_CODE.text()
        this.rulePriority = rule.PRIORITY.text()
        this.college = rule.COLL_CODE.text()
        this.dept = rule.DEPT_CODE.text()
        this.majr = rule.MAJR_CODE.text()
        this.camp = rule.CAMP_CODE.text()
        this.degc = rule.DEGC_CODE.text()
        this.levl = rule.LEVL_CODE.text()
        this.prog = rule.PROGRAM.text()
        this.atts = rule.ATTS_CODE.text()
        this.userid = connectInfo.dataOrigin
        this.dataOrigin = connectInfo.dataOrigin
        this.activityDate =  '01012010'
    }


    def insertRuleDetailData() {
        String ruleSql = """select sfrbrdh_seq_num  as seqValue from sfrbrdh  where SFRBRDH_PRIORITY = ? and SFRBRDH_TERM_CODE = ? """
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
            ruleSql += """ and SFRBRDH_PROGRAM = ?"""
            params.add(this.prog)
        }
        else {
            ruleSql += """ and SFRBRDH_PROGRAM is null"""
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
        if (connectInfo.debugThis) println ruleSql
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
                String blockExistsSQL = """select 1 as blockCount from sfrbrdb  where SFRBRDB_BRDH_SEQ_NUM = ? and SFRBRDB_BLCK_CODE = ? """
                if (connectInfo.debugThis) println "block rule number ${blockExistsSQL}"
                this.conn.eachRow(blockExistsSQL, [this.ruleSeqNum, this.blockCode]) {trow ->
                    if (trow.blockCount == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def blockUpdatesql = """update SFRBRDB set  SFRBRDB_ASSIGN_IND=?,
                                        SFRBRDB_COURSE_RESTRICTION=?,
                                        SFRBRDB_USER_ID=?,SFRBRDB_DATA_ORIGIN=?,SFRBRDB_ACTIVITY_DATE=to_date(?,'MMDDYYYY')
                                        where SFRBRDB_BRDH_SEQ_NUM =? and SFRBRDB_BLCK_CODE=?"""

                try {
                    conn.executeUpdate(blockUpdatesql, [ this.blockAssignInd, this.courseRestriction,
                            this.userid, this.dataOrigin, this.activityDate, this.ruleSeqNum, this.blockCode])
                    connectInfo.tableUpdate("SFRBRDB", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBRDB", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SFRBRDB ${this.blockCode}}"
                        println "Problem executing update for table SFRBRDB from RegistrationGroupRuleDetailDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into SFRBRDB (SFRBRDB_BRDH_SEQ_NUM,SFRBRDB_BLCK_CODE,SFRBRDB_ASSIGN_IND,SFRBRDB_COURSE_RESTRICTION,
                  SFRBRDB_USER_ID,SFRBRDB_DATA_ORIGIN,SFRBRDB_ACTIVITY_DATE) values (?,?,?,?,?,?,to_date(?,'MMDDYYYY'))"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.ruleSeqNum, this.blockCode, this.blockAssignInd, this.courseRestriction,
                             this.userid, this.dataOrigin, this.activityDate])
                    connectInfo.tableUpdate("SFRBRDB", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBRDB", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SFRBRDB ${this.blockCode}}"
                        println "Problem executing insert  for table SFRBRDB from RegistrationGroupRuleDetailDML.groovy: $e.message"
                    }
                }
            }
        }
    }


    def deleteData(ruleSeqNum, blockCode) {

        deleteData("SFRBRDB", """delete FROM SFRBRDB where SFRBRDB_BRDH_SEQ_NUM = ? and SFRBRDB_BLCK_CODE = ?
                                     """, ruleSeqNum, blockCode)

    }


    def deleteData(String tableName, String sql, String ruleSeqNUm, String blockCode) {
        try {
            int delRows
            if (connectInfo.debugThis) println sql
            delRows = conn.executeUpdate(sql, [ruleSeqNum, blockCode])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${blockCode} from RegistrationGroupRuleDetailDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
