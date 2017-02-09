/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GuroCfgDML {
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
    def deleteData
    def update
    def appExists
    def appNum
    def configSeqNum
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GuroCfgDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GuroCfgDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

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
        this.configName = rule.GUROCFG_CONFIG_NAME.text()
        this.configType = rule.GUROCFG_CONFIG_TYPE.text()
        this.configValue = rule.GUROCFG_CONFIG_VALUE.text()
        this.appName = rule.GUBAPPL_APP_NAME.text()
        this.userId = rule.GUROCFG_USER_ID.text()
        this.dataOrigin = rule.GUROCFG_DATA_ORIGIN.text()
        this.activityDate = rule.GUROCFG_ACTIVITY_DATE.text()
    }

    def insertGuroCfgData() {
        String ruleSql = """select GUBAPPL_APP_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_NAME)= ? """
        def params = [this.appName.toUpperCase()]

        if (this.userId) {
            ruleSql += """ and GUBAPPL_USER_ID = ?"""
            params.add(this.userId)
        }
        else {
            ruleSql += """ and GUBAPPL_USER_ID  is null"""
        }

        if (this.dataOrigin) {
            ruleSql += """ and GUBAPPL_DATA_ORIGIN = ?"""
            params.add(this.dataOrigin)
        }
        else {
            ruleSql += """ and GUBAPPL_DATA_ORIGIN  is null"""
        }

        if (connectInfo.debugThis) println ruleSql
        this.conn.eachRow(ruleSql, params)
                {
                    trow ->
                        this.appNum = trow.seqValue
                }
        if (this.delete && this.delete == "YES") {
            deleteData(this.appNum, this.appName)
        } else {
            //Check whether it is a create or update
            if (this.appNum) {
                String appNameExistsSQL = """select 1 as blockCount from GUBAPPL  where GUBAPPL_APP_ID = ? and UPPER(GUBAPPL_APP_NAME) = ? """
                if (connectInfo.debugThis) println "block rule number ${appNameExistsSQL}"
                this.conn.eachRow(appNameExistsSQL, [this.appNum, this.appName.toUpperCase()]) {trow ->
                    if (trow.blockCount == 1)
                        this.appExists = true
                }
            }
            if (this.appExists) {
                String configRuleSql = """select GUROCFG_SURROGATE_ID as configSeqValue from gurocfg  where GUROCFG_GUBAPPL_APP_ID= ? and GUROCFG_CONFIG_NAME = ? """
                def configparams = [this.appNum,this.configName]

                if (this.userId) {
                    configRuleSql += """ and GUROCFG_USER_ID = ?"""
                    configparams.add(this.userId)
                }
                else {
                    configRuleSql += """ and GUROCFG_USER_ID  is null"""
                }

                if (this.dataOrigin) {
                    configRuleSql += """ and GUROCFG_DATA_ORIGIN = ?"""
                    configparams.add(this.dataOrigin)
                }
                else {
                    configRuleSql += """ and GUROCFG_DATA_ORIGIN  is null"""
                }

                if (connectInfo.debugThis) println configRuleSql
                this.conn.eachRow(configRuleSql, configparams)
                        {
                            trow ->
                                this.configSeqNum = trow.configSeqValue
                        }
                if (this.delete && this.delete == "YES") {
                    deleteData(this.configSeqNum, this.appNum)
                } else {
                    //Check whether it is a create or update
                    if (this.configSeqNum) {
                        String configNameExistsSQL = """select 1 as configCount from GUROCFG  where GUROCFG_SURROGATE_ID = ? and GUROCFG_GUBAPPL_APP_ID = ? and GUROCFG_CONFIG_NAME = ? """
                        if (connectInfo.debugThis) println "block rule number ${configNameExistsSQL}"
                        this.conn.eachRow(configNameExistsSQL, [this.configSeqNum, this.appNum,this.configName]) {trow ->
                            if (trow.configCount == 1)
                                this.update = true
                        }
                    }
                    if (this.update) {
                        def blockUpdatesql = """update GUROCFG set GUROCFG_CONFIG_NAME = ?,GUROCFG_CONFIG_TYPE = ?, GUROCFG_CONFIG_VALUE = ?,GUROCFG_USER_ID=?,GUROCFG_DATA_ORIGIN=?,GUROCFG_ACTIVITY_DATE=to_date(?,'MMDDYYYY')
                                        where GUROCFG_GUBAPPL_APP_ID =? and GUROCFG_SURROGATE_ID=?"""

                        try {
                            conn.executeUpdate(blockUpdatesql, [this.configName, this.configType, this.configValue,this.userId,this.dataOrigin,this.activityDate, this.appNum, this.configSeqNum])
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 1, 0, 0)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Update GUROCFG ${this.configName}}"
                                println "Problem executing update for table GUROCFG from GuroCfgDML.groovy: $e.message"
                            }
                        }
                    } else {
                        def insertSQL = """insert into GUROCFG (GUROCFG_CONFIG_NAME,GUROCFG_CONFIG_TYPE,GUROCFG_CONFIG_VALUE,GUROCFG_GUBAPPL_APP_ID,GUROCFG_USER_ID,GUROCFG_DATA_ORIGIN,GUROCFG_ACTIVITY_DATE) values (?,?,?,?,?,?,to_date(?,'MMDDYYYY'))"""
                        if (connectInfo.debugThis) println insertSQL
                        try {
                            conn.executeUpdate(insertSQL, [this.configName,this.configType,this.configValue,this.appNum,this.userId,this.dataOrigin,this.activityDate])
                            connectInfo.tableUpdate("GUROCFG", 0, 1, 0, 0, 0)
                        }
                        catch (Exception e) {
                            connectInfo.tableUpdate("GUROCFG", 0, 0, 0, 1, 0)
                            if (connectInfo.showErrors) {
                                println "Insert GUROCFG ${this.appName}}"
                                println "Problem executing insert  for table GUROCFG from GuroCfgDML.groovy: $e.message"
                            }
                        }
                    }
                }
            }
        }


    }
}
