/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GurApprDML {
    def appName
    def userId
    def dataOrigin
    def activityDate
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
    def appId
    def twtvRoleCode
    def pagePrimaryId
    def pageName
    def pageId

    public GurApprDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GurApprDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        parseXmlData()
        insertGurApprData()

    }

    def parseXmlData() {
        def data = new XmlParser().parseText(xmlData)
        this.appName = data.GUBAPPL_APP_NAME.text()
        this.pageName = data.GURCTLEPP_PAGE_NAME.text()
        this.twtvRoleCode = data.GURAPPR_TWTVROLE_CODE.text()
        this.userId = data.GURAPPR_USER_ID.text()
        this.dataOrigin = data.GURAPPR_DATA_ORIGIN.text()
        this.activityDate = data.GURAPPR_ACTIVITY_DATE.text()
    }

    def insertGurApprData(){
        String appNumSql = """select GUBAPPL_APP_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_NAME)= ? """
        def params = [this.appName.toUpperCase()]

        if (this.userId) {
            appNumSql += """ and GUBAPPL_USER_ID = ?"""
            params.add(this.userId)
        }
        else {
            appNumSql += """ and GUBAPPL_USER_ID  is null"""
        }

        if (this.dataOrigin) {
            appNumSql += """ and GUBAPPL_DATA_ORIGIN = ?"""
            params.add(this.dataOrigin)
        }
        else {
            appNumSql += """ and GUBAPPL_DATA_ORIGIN  is null"""
        }

        if (connectInfo.debugThis) println appNumSql
        this.conn.eachRow(appNumSql, params)
                {
                    trow ->
                        this.appId = trow.seqValue
                }
        if(this.appId){
            String pageIdSql = """select GURCTLEPP_PAGE_ID as pageIdValue from GURCTLEPP  where GURCTLEPP_GUBAPPL_APP_ID= ? and UPPER(GURCTLEPP_PAGE_NAME) = ? """
            def pageIdparams = [this.appId,this.pageName.toUpperCase()]

            if (this.userId) {
                pageIdSql += """ and GURCTLEPP_USER_ID = ?"""
                pageIdparams.add(this.userId)
            }
            else {
                pageIdSql += """ and GURCTLEPP_USER_ID  is null"""
            }

            if (this.dataOrigin) {
                pageIdSql += """ and GURCTLEPP_DATA_ORIGIN = ?"""
                pageIdparams.add(this.dataOrigin)
            }
            else {
                pageIdSql += """ and GURCTLEPPDATA_ORIGIN  is null"""
            }

            if (connectInfo.debugThis) println pageIdSql
            this.conn.eachRow(pageIdSql, pageIdparams)
                    {
                        trow ->
                            this.pageId = trow.pageIdValue
                    }
         }

        if (this.appId && this.pageId) {
            String pageRuleSql = """select GURAPPR_SURROGATE_ID as seqValue from gurappr  where GURAPPR_GUBAPPL_APP_ID= ? and GURAPPR_PAGE_ID = ? and UPPER(GURAPPR_TWTVROLE_CODE) = ? """
            def pageRuleParams = [this.appId,this.pageId,this.twtvRoleCode.toUpperCase()]

            if (this.userId) {
                pageRuleSql += """ and GURAPPR_USER_ID = ?"""
                pageRuleParams.add(this.userId)
            }
            else {
                pageRuleSql += """ and GURAPPR_USER_ID  is null"""
            }

            if (this.dataOrigin) {
                pageRuleSql += """ and GURAPPR_DATA_ORIGIN = ?"""
                pageRuleParams.add(this.dataOrigin)
            }
            else {
                pageRuleSql += """ and GURAPPR_DATA_ORIGIN  is null"""
            }

            if (connectInfo.debugThis) println pageRuleSql
            this.conn.eachRow(pageRuleSql, pageRuleParams)
                    {
                        trow ->
                            this.pagePrimaryId= trow.seqValue
                    }
            if (this.delete && this.delete == "YES") {
                deleteData(this.pagePrimaryId, this.appId,this.pageId)

            } else {
                //Check whether it is a create or update
                if (this.pagePrimaryId) {
                    String pageIdExistsSQL = """select 1 as pageCount from GURAPPR  where GURAPPR_SURROGATE_ID = ? and GURAPPR_GUBAPPL_APP_ID = ? and GURAPPR_PAGE_ID = ?"""
                    if (connectInfo.debugThis) println "Page Id ${pageIdExistsSQL}"
                    this.conn.eachRow(pageIdExistsSQL, [this.pagePrimaryId, this.appId,this.pageId]) {trow ->
                        if (trow.pageCount == 1)
                            this.update = true
                    }
                }
                if (this.update) {
                    def updateSql = """update GURAPPR set GURAPPR_TWTVROLE_CODE=?,GURAPPR_USER_ID=?,GURAPPR_DATA_ORIGIN=?,GURAPPR_ACTIVITY_DATE=to_date(?,'MMDDYYYY')
                                        where GURAPPR_SURROGATE_ID=? and GURAPPR_GUBAPPL_APP_ID =? and GURAPPR_PAGE_ID=?"""

                    try {
                        conn.executeUpdate(updateSql, [this.twtvRoleCode,this.userId,this.dataOrigin,this.activityDate, this.pagePrimaryId,this.appId,this.pageId])
                        connectInfo.tableUpdate("GURAPPR", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GURAPPR", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update GURAPPR ${this.pagePrimaryId}}"
                            println "Problem executing update for table GURAPPR from GurApprDML.groovy: $e.message"
                        }
                    }
                } else {
                    def insertSQL = """insert into GURAPPR (GURAPPR_PAGE_ID,GURAPPR_SURROGATE_ID,GURAPPR_TWTVROLE_CODE,GURAPPR_GUBAPPL_APP_ID,GURAPPR_USER_ID,GURAPPR_DATA_ORIGIN,GURAPPR_ACTIVITY_DATE) values (?,?,?,?,?,?,to_date(?,'MMDDYYYY'))"""
                    if (connectInfo.debugThis) println insertSQL
                    try {
                        conn.executeUpdate(insertSQL, [this.pageId,this.pagePrimaryId,this.twtvRoleCode,this.appId,this.userId,this.dataOrigin,this.activityDate])
                        connectInfo.tableUpdate("GURAPPR", 0, 1, 0, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("GURAPPR", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Insert GURAPPR ${this.appName}}"
                            println "Problem executing insert  for table GURAPPR from GurApprDML.groovy: $e.message"
                        }
                    }
                }
            }
        }

    }

    def deleteData(pagePrimaryId, appId,pageId) {
        deleteData("GURAPPR", """delete FROM GURAPPR where GURAPPR_SURROGATE_ID=? and GURPPR_GUBAPPL_APP_ID=? and GURAPPR_PAGE_ID = ?""", pagePrimaryId, appId,pageId)
    }
}
