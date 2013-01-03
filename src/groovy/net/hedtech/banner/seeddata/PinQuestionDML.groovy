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
        if (this.delete && this.delete == "YES") {
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

}
