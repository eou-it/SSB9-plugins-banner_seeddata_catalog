/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class ShbcgpaDML {

    def userId
    def dataOrigin
    def activityDate
    def surrogateId
    def version
    def campGpaInd
    def tprtCode
    def trmtCode
    def instFice
    def gscaleWebInd
    def xmlHostName
    def xmlUsername
    def xmlPassword
    def awardPrevInd
    def rollStudyPathInd
    def rollLevelInd
    def rollDegreeInd
    def rollCollegeInd
    def rollProgramInd
    def rollPrimeMajorInd
    def gradtermCde
    def studyPathGpaCde

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData

    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public ShbcgpaDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        parseXmlData()
        processShbcgpaData()

    }
    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)

        this.campGpaInd = rule.SHBCGPA_CAMP_GPA_IND.text()
        this.activityDate = rule.SHBCGPA_ACTIVITY_DATE.text()
        this.tprtCode = rule.SHBCGPA_TPRT_CODE.text()
        this.trmtCode = rule.SHBCGPA_TRMT_CODE.text()
        this.instFice = rule.SHBCGPA_INST_FICE.text()
        this.gscaleWebInd = rule.SHBCGPA_GSCALE_WEB_IND.text()
        this.xmlHostName = rule.SHBCGPA_XML_HOST_NAME.text()
        this.xmlUsername = rule.SHBCGPA_XML_USERNAME.text()
        this.xmlPassword = rule.SHBCGPA_XML_PASSWORD.text()
        this.awardPrevInd = rule.SHBCGPA_AWARD_PREV_IND.text()
        this.rollStudyPathInd = rule.SHBCGPA_ROLL_STUDY_PATH_IND.text()
        this.rollLevelInd = rule.SHBCGPA_ROLL_LEVEL_IND.text()
        this.rollDegreeInd = rule.SHBCGPA_ROLL_DEGREE_IND.text()
        this.rollCollegeInd = rule.SHBCGPA_ROLL_COLLEGE_IND.text()
        this.rollProgramInd = rule.SHBCGPA_ROLL_PROGRAM_IND.text()
        this.rollPrimeMajorInd = rule.SHBCGPA_ROLL_PRIME_MAJOR_IND.text()
        this.surrogateId = rule.SHBCGPA_SURROGATE_ID.text()
        this.version = rule.SHBCGPA_VERSION.text()
        this.userId = rule.SHBCGPA_USER_ID.text()
        this.dataOrigin = rule.SHBCGPA_DATA_ORIGIN.text()
        this.gradtermCde = rule.SHBCGPA_GRADTERM_CDE.text()
        this.studyPathGpaCde = rule.SHBCGPA_STUDY_PATH_GPA_CDE.text()
    }

    def processShbcgpaData() {
        String entryExistsSQL = """select * from SHBCGPA """

        if (connectInfo.debugThis) println "Row count number ${entryExistsSQL}"

        int count = conn.executeUpdate(entryExistsSQL, [])

        if (count) {
            def deleteSQL = """DELETE FROM SHBCGPA """

            try {
                int delRows = conn.executeUpdate(deleteSQL, [])
                connectInfo.tableUpdate("SHBCGPA", 0, 0, 0, 0, delRows)
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem executing delete from ShbcgpaDML.groovy: $e.message"
                    println "${deleteSQL}"
                }
            }
        }

        insertShbcgpaData()
    }

    def insertShbcgpaData() {

        def insertSQL = """insert into SHBCGPA (SHBCGPA_CAMP_GPA_IND, SHBCGPA_ACTIVITY_DATE, SHBCGPA_TPRT_CODE, SHBCGPA_TRMT_CODE, SHBCGPA_INST_FICE,
                                                SHBCGPA_GSCALE_WEB_IND, SHBCGPA_XML_HOST_NAME, SHBCGPA_XML_USERNAME, SHBCGPA_XML_PASSWORD, SHBCGPA_AWARD_PREV_IND,
                                                SHBCGPA_ROLL_STUDY_PATH_IND, SHBCGPA_ROLL_LEVEL_IND, SHBCGPA_ROLL_DEGREE_IND, SHBCGPA_ROLL_COLLEGE_IND, SHBCGPA_ROLL_PROGRAM_IND,
                                                SHBCGPA_ROLL_PRIME_MAJOR_IND, SHBCGPA_SURROGATE_ID, SHBCGPA_VERSION, SHBCGPA_USER_ID, SHBCGPA_DATA_ORIGIN,
                                                SHBCGPA_GRADTERM_CDE, SHBCGPA_STUDY_PATH_GPA_CDE)
                                       values  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        if (connectInfo.debugThis) println insertSQL
        try {
            conn.executeUpdate(insertSQL, [this.campGpaInd, this.activityDate, this.tprtCode, this.trmtCode, this.instFice,
                                           this.gscaleWebInd, this.xmlHostName, this.xmlUsername, this.xmlPassword, this.awardPrevInd,
                                           this.rollStudyPathInd, this.rollLevelInd, this.rollDegreeInd, this.rollCollegeInd, this.rollProgramInd,
                                           this.rollPrimeMajorInd, this.surrogateId, this.version, this.userId, this.dataOrigin,
                                           this.gradtermCde, this.studyPathGpaCde])
            connectInfo.tableUpdate("SHBCGPA", 0, 1, 0, 0, 0)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SHBCGPA", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert SHBCGPA ${this.xmlHostName}}"
                println "Problem executing insert  for table SHBCGPA from ShbcgpaDML.groovy: $e.message"
            }
        }
    }
}
