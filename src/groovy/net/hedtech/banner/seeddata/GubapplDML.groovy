/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GubapplDML {
    def appName
    def userId
    def dataOrigin
    def activityDate
    def appNum
    def appShortName

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData

    def delete
    def update
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GubapplDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GubapplDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        parseXmlData()
        insertGubApplData()

    }
    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)
        this.delete = rule.DELETE?.text()
        this.appName = rule.GUBAPPL_APP_NAME.text()
        this.userId = rule.GUBAPPL_USER_ID.text()
        this.dataOrigin = rule.GUBAPPL_DATA_ORIGIN.text()
        this.activityDate = rule.GUBAPPL_ACTIVITY_DATE.text()
        this.appShortName = rule.GUBAPPL_APP_ID.text()
    }

    def insertGubApplData() {
        String appSql = """select GUBAPPL_SURROGATE_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_ID)=? and UPPER(GUBAPPL_APP_NAME)=?"""
        def params = [this.appShortName,this.appName.toUpperCase()]
        if (connectInfo.debugThis) println appSql
        this.conn.eachRow(appSql, params)
                {
                    trow ->
                        this.appNum = trow.seqValue
                }
        if (this.delete && this.delete == "YES") {
            deleteData(this.appNum, this.appName, this.appShortName)
        } else {
            //Check whether it is a create or update
            if (this.appNum) {
                String appNameExistsSQL = """select 1 as appIdSeq from GUBAPPL  where GUBAPPL_SURROGATE_ID = ? and UPPER(GUBAPPL_APP_ID) = ? and UPPER(GUBAPPL_APP_NAME)= ? """
                if (connectInfo.debugThis) println "Application number ${appNameExistsSQL}"
                this.conn.eachRow(appNameExistsSQL, [this.appNum, this.appShortName.toUpperCase(), this.appName.toUpperCase()]) {trow ->
                    if (trow.appIdSeq == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def updateSql = """update GUBAPPL set GUBAPPL_USER_ID=?,GUBAPPL_DATA_ORIGIN=?,GUBAPPL_ACTIVITY_DATE=?
                                        where GUBAPPL_SURROGATE_ID=? and GUBAPPL_APP_ID =? and GUBAPPL_APP_NAME=?"""

                try {
                    conn.executeUpdate(updateSql, [this.userId, this.dataOrigin, this.activityDate, this.appNum, this.appShortName, this.appName])
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update GUBAPPL ${this.appName}}"
                        println "Problem executing update for table GUBAPPL from GubapplDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into GUBAPPL (GUBAPPL_SURROGATE_ID,GUBAPPL_APP_ID,GUBAPPL_APP_NAME,GUBAPPL_USER_ID,GUBAPPL_DATA_ORIGIN,
                  GUBAPPL_ACTIVITY_DATE) values (?,?,?,?,?,?)"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.appNum, this.appShortName, this.appName,this.userId, this.dataOrigin, this.activityDate])
                    connectInfo.tableUpdate("GUBAPPL", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GUBAPPL ${this.appName}}"
                        println "Problem executing insert  for table GUBAPPL from GubapplDML.groovy: $e.message"
                    }
                }
            }
        }
    }

    def deleteData(appNum, appName, appShortName) {
        int delRows
        def deleteSql = """delete FROM GUBAPPL where GUBAPPL_SURROGATE_ID=? and UPPER(GUBAPPL_APP_ID)= ? and UPPER(GUBAPPL_APP_NAME) = ? """
        if (connectInfo.debugThis) println deleteSql
       try {

            delRows = conn.executeUpdate(deleteSql, [appNum,appShortName?.toUpperCase(),appName.toUpperCase()])
            connectInfo.tableUpdate("GUBAPPL", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GUBAPPL table with App Name ${appName} from GubapplDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }
}
