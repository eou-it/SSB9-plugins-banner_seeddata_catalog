/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GurocfgDML {
    def configName
    def configType
    def configValue
    def appName
    def userId
    def dataOrigin
    def activityDate
    def ruleSeqNum

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData

    def delete
    def update
    def appId
    def configSeqNum
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GurocfgDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GurocfgDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        parseXmlData()
        insertGuroCfgData()

    }

    def parseXmlData() {
        def rule = new XmlParser().parseText(xmlData)
        this.delete = rule.DELETE?.text()
        this.configName = rule.GUROCFG_NAME.text()
        this.configType = rule.GUROCFG_TYPE.text()
        this.configValue = rule.GUROCFG_VALUE.text()
        this.appName = rule.GUBAPPL_APP_NAME.text()
        this.userId = rule.GUROCFG_USER_ID.text()
        this.dataOrigin = rule.GUROCFG_DATA_ORIGIN.text()
        this.activityDate = rule.GUROCFG_ACTIVITY_DATE.text()
    }

    def insertGuroCfgData() {
        deleteOldData()
        String appSql = """select GUBAPPL_APP_ID as appID from gubappl  where UPPER(GUBAPPL_APP_NAME)= ? and GUBAPPL_USER_ID = ? and GUBAPPL_DATA_ORIGIN = ?"""
        def params = [this.appName.toUpperCase(),this.userId,this.dataOrigin]
        if (connectInfo.debugThis) println appSql
        this.conn.eachRow(appSql, params)
                {
                    trow ->
                        this.appId = trow.appID
                }
        if (this.appId) {
                String configSql = """select GUROCFG_SURROGATE_ID as configSeqValue from gurocfg  where GUROCFG_GUBAPPL_APP_ID= ? and UPPER(GUROCFG_NAME) = ?  and UPPER(GUROCFG_TYPE) = ? and GUROCFG_USER_ID= ? and GUROCFG_DATA_ORIGIN = ?"""
                def configparams = [this.appId,this.configName.toUpperCase(),this.configType.toUpperCase(),this.userId,this.dataOrigin]
                if (connectInfo.debugThis) println configSql
                this.conn.eachRow(configSql, configparams)
                        {
                            trow ->
                                this.configSeqNum = trow.configSeqValue
                        }
                if (this.delete && this.delete == "YES") {
                    deleteData(this.configSeqNum, this.appId)
                } else {
                    //Check whether it is a create or update
                    if (this.configSeqNum) {
                        String configNameExistsSQL = """select 1 as configCount from GUROCFG  where GUROCFG_SURROGATE_ID = ? and GUROCFG_GUBAPPL_APP_ID = ? and UPPER(GUROCFG_NAME) = ? and UPPER(GUROCFG_TYPE) = ?"""
                        if (connectInfo.debugThis) println "Config Sequence Id ${configNameExistsSQL}"
                        this.conn.eachRow(configNameExistsSQL, [this.configSeqNum, this.appId,this.configName.toUpperCase(),this.configType.toUpperCase()]) {trow ->
                            if (trow.configCount == 1)
                                this.update = true
                        }
                    }
                    if (this.update) {
                        def updateSql = """update GUROCFG set GUROCFG_NAME = ?,GUROCFG_TYPE = ?, GUROCFG_VALUE = ?,GUROCFG_USER_ID=?,GUROCFG_DATA_ORIGIN=?,GUROCFG_ACTIVITY_DATE=?
                                        where GUROCFG_GUBAPPL_APP_ID =? and GUROCFG_SURROGATE_ID=?"""

                        try {
                            conn.executeUpdate(updateSql, [this.configName, this.configType, this.configValue,this.userId,this.dataOrigin,this.activityDate, this.appId, this.configSeqNum])
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 1, 0, 0)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Update GUROCFG ${this.configName}}"
                                println "Problem executing update for table GUROCFG from GurocfgDML.groovy: $e.message"
                            }
                        }
                    } else {
                        def insertSQL = """insert into GUROCFG (GUROCFG_NAME,GUROCFG_TYPE,GUROCFG_VALUE,GUROCFG_GUBAPPL_APP_ID,GUROCFG_USER_ID,GUROCFG_DATA_ORIGIN,GUROCFG_ACTIVITY_DATE) values (?,?,?,?,?,?,?)"""
                        if (connectInfo.debugThis) println insertSQL
                        try {
                            conn.executeUpdate(insertSQL, [this.configName,this.configType,this.configValue,this.appId,this.userId,this.dataOrigin,this.activityDate])
                            connectInfo.tableUpdate("GUROCFG", 0, 1, 0, 0, 0)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Insert GUROCFG ${this.appName}}"
                                println "Problem executing insert  for table GUROCFG from GurocfgDML.groovy: $e.message"
                            }
                        }
                    }
                }
            }
        }


    def deleteData(configSeqNum, appId) {
        int delRows
        def deleteSql = """delete FROM GUROCFG where GUROCFG_SURROGATE_ID=? and GUROCFG_GUBAPPL_APP_ID = ?"""
        if (connectInfo.debugThis) println deleteSql
        try {

            delRows = conn.executeUpdate(deleteSql, [configSeqNum, appId])
            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GUROCFG table with Config Seq number ${configSeqNum} from GurocfgDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }


    def deleteOldData() {
        int delRows
        def deleteSql = """delete FROM GUROCFG """
        if (connectInfo.debugThis) println deleteSql
        try {

            delRows = conn.executeUpdate(deleteSql)
            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GUROCFG table from GurocfgDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }
}
