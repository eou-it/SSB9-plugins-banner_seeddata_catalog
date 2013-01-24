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
 * RegistrationSelectedBlockDML
 */
class RegistrationSelectedBlockDML {

    def ruleSeqNum
    def blockCode
    def termCodeEff
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
    def pidm

    def delete
    def update
    def create


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public RegistrationSelectedBlockDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public RegistrationSelectedBlockDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertRuleDetailData()
    }


    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)
        this.termCodeEff = rule.SFRBSEL_TERM_CODE.text()
        this.rulePriority = rule.PRIORITY.text()
        this.college = rule.COLL_CODE.text()
        this.dept = rule.DEPT_CODE.text()
        this.majr = rule.MAJR_CODE.text()
        this.camp = rule.CAMP_CODE.text()
        this.degc = rule.DEGC_CODE.text()
        this.levl = rule.LEVL_CODE.text()
        this.prog = rule.PROGRAM.text()
        this.atts = rule.ATTS_CODE.text()
        this.blockCode = rule.SFRBSEL_BLCK_CODE.text()
        if (connectInfo.debugThis) println "Block Assignment ${rule.BANNERID.text()} rerm ${this.termCodeEff} blck ${this.blockCode}"

    }


    def insertRuleDetailData() {
        this.ruleSeqNum = null
        // create the sfrbrdb and sfrbrdh rules for the block assignment
        if (this.rulePriority ||
                this.college ||
                this.dept ||
                this.majr ||
                this.camp ||
                this.degc ||
                this.levl ||
                this.prog ||
                this.atts) {
            String ruleSql = """select sfrbrdh_seq_num  as seqValue from sfrbrdh  where SFRBRDH_PRIORITY = ? and SFRBRDH_TERM_CODE_EFF = ? """
            def params = [this.rulePriority, this.termCodeEff]

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

            //Check whether the rule already exists
            if (this.ruleSeqNum) {
                String blockExistsSQL = """select 1 as blockCount from sfrbrdb  where SFRBRDB_BRDH_SEQ_NUM = ? and SFRBRDB_BLCK_CODE = ? """
                if (connectInfo.debugThis) println "block rule number ${blockExistsSQL}"
                this.conn.eachRow(blockExistsSQL, [this.ruleSeqNum, this.blockCode]) {trow ->
                    if (trow.blockCount == 1)
                        this.update = true
                }
            }
            if (!this.update) {
                def insertSQL = """insert into SFRBRDB (SFRBRDB_BRDH_SEQ_NUM,SFRBRDB_BLCK_CODE,SFRBRDB_ASSIGN_IND,SFRBRDB_COURSE_RESTRICTION,
                  SFRBRDB_USER_ID,SFRBRDB_DATA_ORIGIN,SFRBRDB_ACTIVITY_DATE) values (?,?,?,?,?,?,to_date(?, 'MMDDYYYY'))"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.ruleSeqNum, this.blockCode, this.termCodeEff, this.blockAssignInd, this.courseRestriction,
                                       this.classCode, this.userid, this.dataOrigin, "01012010"])
                    connectInfo.tableUpdate("SFRBRDB", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SFRBRDB", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SFRBRDB ${this.blockCode}}"
                        println "Problem executing insert  for table SFRBRDB from RegistrationSelectedBlockDML.groovy: $e.message"
                    }
                }
            }
        }

        def selectSql = """select sfrbsel_blck_code block from sfrbsel where sfrbsel_pidm = ? and
                           sfrbsel_term_code = ?"""
        def selectFound
        if (connectInfo.debugThis) println selectSql
        try {
            selectFound = conn.firstRow(selectSql, [connectInfo.saveStudentPidm, this.termCodeEff])
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SFRBSEL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select SFRBSEL ${this.pidm} ${this.blockCode}}"
                println "Problem executing select  for table SFRBSEL from RegistrationSelectedBlockDML.groovy: $e.message"
            }
        }
        if (!selectFound) {
            def assignedSql = """insert into sfrbsel ( sfrbsel_pidm, sfrbsel_term_code,
                             sfrbsel_blck_code,  sfrbsel_brdh_seq_num,
                             sfrbsel_activity_date, sfrbsel_data_origin,
                             sfrbsel_user_id)
                             values ( ?, ?, ?, ?, to_date(?, 'MMDDYYYY'), ? , ?) """
            if (connectInfo.debugThis) println assignedSql
            try {
                conn.executeUpdate(assignedSql, [connectInfo.saveStudentPidm, this.termCodeEff,
                                   this.blockCode, this.ruleSeqNum,
                                   '01012010', connectInfo.dataOrigin,
                                   connectInfo.dataOrigin])
                selectFound = this.blockCode
                connectInfo.tableUpdate("SFRBSEL", 0, 1, 0, 0, 0)
            }

            catch (Exception e) {
                connectInfo.tableUpdate("SFRBSEL", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SFRBSEL ${this.blockCode}}"
                    println "Problem executing insert  for table SFRBSEL from RegistrationSelectedBlockDML.groovy: $e.message"
                }
            }
        }
        if (this.ruleSeqNum && this.blockCode) {
            def registrationSql = """update sfrstcr
                                       set sfrstcr_brdh_seq_num = ?
                                       where sfrstcr_term_code = ?
                                       and sfrstcr_pidm = ?
                                       and sfrstcr_blck_code = ?  """
            if (connectInfo.debugThis) println registrationSql
            try {
                def updcnt = conn.executeUpdate(registrationSql, [this.ruleSeqNum, this.termCodeEff,
                                                connectInfo.saveStudentPidm,
                                                this.blockCode])
                connectInfo.tableUpdate("SFRSTCR", 0, 0, updcnt, 0, 0)
            }

            catch (Exception e) {
                connectInfo.tableUpdate("SFRSTCR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SFTSTCR ${this.blockCode}}"
                    println "Problem executing update of SFRSTCR from RegistrationSelectedBlockDML.groovy: $e.message"
                }
            }
        }

    }

}
