/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement

/**
 * A dml class for GOBQSTN table
 */
class PinQuestionDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def questionId
    def questionDescription
    def displayInd
    def userId
    def dataOrigin
    def activityDate
    def delete


    public PinQuestionDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public PinQuestionDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertQuestionData()
    }


    def parseXmlData() {
        def gobqstn = new XmlParser().parseText(xmlData)
        this.questionDescription = gobqstn.GOBQSTN_DESC.text()
        this.displayInd = gobqstn.GOBQSTN_DISPLAY_IND.text()
        this.userId = gobqstn.GOBQSTN_USER_ID.text()
        this.activityDate = gobqstn.GOBQSTN_ACTIVITY_DATE.text()
        this.dataOrigin = gobqstn.GOBQSTN_DATA_ORIGIN.text()
        this.delete = gobqstn.DELETE.text()
    }

    //Always call delete of GOBQSTN before insert because, the question description is not a unique field & question id is generated.
    def insertQuestionData() {
        if(this.delete && this.delete == "ALL"){
            deleteOldData()
        }
        else if (this.delete && this.delete == "YES") {
            deleteData('GOBQSTN', 'delete from GOBQSTN where GOBQSTN_DESC=?', this.questionDescription)
        } else {
            def questionidSql = "SELECT NVL((max(To_NUMBER(GOBQSTN.GOBQSTN_ID))+1),1) as questionId from GOBQSTN"
            this.conn.eachRow(questionidSql) {trow ->
                this.questionId = trow.questionId
            }
            def GOBQSTNAPI = "{call  GB_PIN_QUESTION.P_CREATE(?,?,?,?,?,?)}"
            CallableStatement insertCallGOBQSTN = this.connectCall.prepareCall(GOBQSTNAPI)
            insertCallGOBQSTN.setString(1, this.questionId.toString())
            insertCallGOBQSTN.setString(2, this.questionDescription)
            insertCallGOBQSTN.setString(3, this.displayInd)
            insertCallGOBQSTN.setString(4, this.userId)
            insertCallGOBQSTN.setString(5, this.dataOrigin)
            insertCallGOBQSTN.registerOutParameter(6, java.sql.Types.ROWID)
            try {
                insertCallGOBQSTN.executeUpdate()
                connectInfo.tableUpdate("GOBQSTN", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GOBQSTN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GOBQSTN ${this.questionDescription}}"
                    println "Problem executing insert for table GOBQSTN from PinQuestionDML.groovy: $e.message"
                }
            }
            finally {
                insertCallGOBQSTN.close()
            }
        }

    }


    private def deleteData(String tableName, String sql, String questionDescription) {
        try {
            int delRows
            if (questionDescription) {
                delRows = conn.executeUpdate(sql, [questionDescription])
            }
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${questionDescription} from PinQuestionDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

    def deleteOldData() {
        int delRows
        def deleteSql = """delete FROM GOBQSTN """
        if (connectInfo.debugThis) println deleteSql
        try {
            delRows = conn.executeUpdate(deleteSql)
            connectInfo.tableUpdate("GOBQSTN", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GOBQSTN table from PinQuestionDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }

}
