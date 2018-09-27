/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import java.sql.Connection
import groovy.sql.Sql
import java.sql.CallableStatement

/**
 * A DML class to populate data in GOBANSR
 */
class PersonPinResponseDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def pidm
    def answerNumber
    def questionId
    def questionNumber
    def questionDescription
    def answerDescription
    def answerSalt
    def userId
    def dataOrigin
    def bannerid
    def gobqstn_question_description
    def delete

    public PersonPinResponseDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public PersonPinResponseDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertQuestionData()
    }


    def parseXmlData() {
        def gobansr = new XmlParser().parseText(xmlData)
        this.questionDescription = gobansr.GOBQSTN_DESC.text()
        this.answerDescription = gobansr.GOBANSR_ANSR_DESC.text()
        this.userId = gobansr.GOBANSR_USER_ID.text()
        this.dataOrigin = gobansr.GOBANSR_DATA_ORIGIN.text()
        this.bannerid = gobansr.BANNERID.text()
        this.questionNumber = gobansr.GOBANSR_NUM.text()
        this.gobqstn_question_description = gobansr.GOBQSTN_DESC.text()
        this.delete = gobansr.DELETE.text()
    }

    //Always call delete of GOBQSTN before insert because, the question description is not a unique field & question id is generated.
    def insertQuestionData() {
         String pidmsql = """select spriden_pidm as pidmValue from spriden  where SPRIDEN_ID = ? and rownum<=1"""
            this.conn.eachRow(pidmsql, [this.bannerid]) {trow ->
                this.pidm = trow.pidmValue
            }
        if(this.delete && this.delete == "ALL"){
            deleteOldData()
        }
        else if (this.delete && this.delete == "YES") {
            deleteData('GOBANSR', 'delete from GOBANSR where GOBANSR_PIDM=?', this.pidm)
        } else {

            if (this.gobqstn_question_description){
                String gobqstnSql = """select gobqstn_id as gobqstnid from gobqstn  where GOBQSTN_DESC = ? and rownum<=1"""
                this.conn.eachRow(gobqstnSql, [this.gobqstn_question_description]) {trow ->
                    this.questionId = trow.gobqstnid
                }
            }
            def pinApi = "{call  GB_PIN_ANSWER.P_CREATE(?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCallGOBANSR = this.connectCall.prepareCall(pinApi)
            insertCallGOBANSR.setBigDecimal(1, this.pidm)
            if (!this.questionNumber) {
                insertCallGOBANSR.setNull(2, java.sql.Types.INTEGER)
            } else {
                insertCallGOBANSR.setLong(2, this.questionNumber.toInteger())
            }
            if (!this.questionId) {
                insertCallGOBANSR.setNull(3, java.sql.Types.VARCHAR)
            } else {
                insertCallGOBANSR.setString(3, this.questionId)
            }
            if (!this.questionDescription) {
                insertCallGOBANSR.setNull(4, java.sql.Types.VARCHAR)
            } else {
                insertCallGOBANSR.setString(4, this.questionDescription)
            }
            if (!this.answerDescription) {
                insertCallGOBANSR.setNull(5, java.sql.Types.VARCHAR)
            } else {
                insertCallGOBANSR.setString(5, this.answerDescription)
            }
            if (!this.answerSalt) {
                insertCallGOBANSR.setNull(6, java.sql.Types.VARCHAR)
            } else {
                insertCallGOBANSR.setString(6, this.answerSalt)
            }
            insertCallGOBANSR.setString(7, this.userId)
            insertCallGOBANSR.setString(8, this.dataOrigin)
            insertCallGOBANSR.registerOutParameter(9, java.sql.Types.ROWID)
            try {
                insertCallGOBANSR.executeUpdate()
                connectInfo.tableUpdate("GOBANSR", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GOBANSR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GOBANSR ${this.pidm} ${this.questionNumber}}"
                    println "Problem executing insert for table GOBANSR from PinQuestionDML.groovy: $e.message"
                }
            }
            finally {
                insertCallGOBANSR.close()
            }
        }
    }


    private def deleteData(String tableName, String sql, def pidm) {
        try {
            int delRows
            if (pidm) {
                delRows = conn.executeUpdate(sql, [pidm])
            }
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${questionDescription} from PersonPinResponseDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

    def deleteOldData() {
        int delRows
        def deleteSql = """delete FROM GOBANSR """
        if (connectInfo.debugThis) println deleteSql
        try {

            delRows = conn.executeUpdate(deleteSql)
            connectInfo.tableUpdate("GOBANSR", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GOBANSR table from PersonPinResponseDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }
}
