/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GubApplDML {
    def appName
    def userId
    def dataOrigin
    def activityDate
    def appNum

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData

    def delete
    def deleteData
    def update
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    public GubApplDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GubApplDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

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
        this.appName = rule.GUBAPPL_APP_NAME.text()
        this.userId = rule.GUBAPPL_USER_ID.text()
        this.dataOrigin = rule.GUBAPPL_DATA_ORIGIN.text()
        this.activityDate = rule.GUBAPPL_ACTIVITY_DATE.text()
    }

    def insertGubApplData() {
        String appSql = """select GUBAPPL_APP_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_NAME)=? """
        def params = [this.appName.toUpperCase()]

        if (this.userId) {
            appSql += """ and GUBAPPL_USER_ID = ?"""
            params.add(this.userId)
        }
        else {
            appSql += """ and GUBAPPL_USER_ID  is null"""
        }

        if (this.dataOrigin) {
            appSql += """ and GUBAPPL_DATA_ORIGIN = ?"""
            params.add(this.dataOrigin)
        }
        else {
            appSql += """ and GUBAPPL_DATA_ORIGIN  is null"""
        }

        if (connectInfo.debugThis) println appSql
        this.conn.eachRow(appSql, params)
                {
                    trow ->
                        this.appNum = trow.seqValue
                }
        if (this.delete && this.delete == "YES") {
            deleteData(this.appNum, this.appName)
        } else {
            //Check whether it is a create or update
            if (this.appNum) {
                String appNameExistsSQL = """select 1 as blockCount from GUBAPPL  where GUBAPPL_APP_ID = ? and UPPER(GUBAPPL_APP_NAME)= ? """
                if (connectInfo.debugThis) println "block Application number ${appNameExistsSQL}"
                this.conn.eachRow(appNameExistsSQL, [this.appNum, this.appName.toUpperCase()]) {trow ->
                    if (trow.blockCount == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def blockUpdatesql = """update GUBAPPL set GUBAPPL_USER_ID=?,GUBAPPL_DATA_ORIGIN=?,GUBAPPL_ACTIVITY_DATE=to_date(?,'MMDDYYYY')
                                        where GUBAPPL_APP_ID =? and GUBAPPL_APP_NAME=?"""

                try {
                    conn.executeUpdate(blockUpdatesql, [this.userId, this.dataOrigin, this.activityDate, this.appNum, this.appName])
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update GUBAPPL ${this.appName}}"
                        println "Problem executing update for table GUBAPPL from GubApplDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into GUBAPPL (GUBAPPL_APP_ID,GUBAPPL_APP_NAME,GUBAPPL_USER_ID,GUBAPPL_DATA_ORIGIN,
                  GUBAPPL_ACTIVITY_DATE) values (?,?,?,?,to_date(?,'MMDDYYYY'))"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.appNum, this.appName,this.userId, this.dataOrigin, this.activityDate])
                    connectInfo.tableUpdate("GUBAPPL", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GUBAPPL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GUBAPPL ${this.appName}}"
                        println "Problem executing insert  for table GUBAPPL from GubApplDML.groovy: $e.message"
                    }
                }
            }
        }
    }

    def deleteData(appNum, appName) {
        deleteData("GUBAPPL", """delete FROM GUBAPPL where GUBAPPL_APP_ID=? and UPPER(GUBAPPL_APP_NAME) = UPPER('?')
                                     """, appNum, appName)
    }

}
