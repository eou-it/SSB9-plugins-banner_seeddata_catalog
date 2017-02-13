/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GurCtleppDML {

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
    def pageId
    def pageName
    def pageDescription
    def statusInd
    def displaySeq

    public GurCtleppDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GurCtleppDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        parseXmlData()
        insertGuroCtleppData()

    }

    def parseXmlData() {
        def data = new XmlParser().parseText(xmlData)
        this.appName = data.GURCTLEPP_APP_NAME.text()
        this.pageName = data.GURCTLEPP_PAGE_NAME.text()
        this.pageDescription = data.GURCTLEPP_PAGE_DESCRIPTION.text()
        this.statusInd = data.GURCTLEPP_STATUS_INDICATOR.text()
        this.displaySeq = data.GURCTLEPP_DISPLAY_SEQUENCE.text()
        this.userId = data.GURCTLEPP_USER_ID.text()
        this.dataOrigin = data.GURCTLEPP_DATA_ORIGIN.text()
        this.activityDate = data.GURCTLEPP_ACTIVITY_DATE.text()
    }

    def insertGuroCtleppData() {
        String appNumSql = """select GUBAPPL_APP_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_NAME)= ? """
        def params = [this.appName.toUpperCase()]

        if (this.userId) {
            appNumSql += """ and GUBAPPL_USER_ID = ?"""
            params.add(this.userId)
        } else {
            appNumSql += """ and GUBAPPL_USER_ID  is null"""
        }

        if (this.dataOrigin) {
            appNumSql += """ and GUBAPPL_DATA_ORIGIN = ?"""
            params.add(this.dataOrigin)
        } else {
            appNumSql += """ and GUBAPPL_DATA_ORIGIN  is null"""
        }

        if (connectInfo.debugThis) println appNumSql
        this.conn.eachRow(appNumSql, params)
                {
                    trow ->
                        this.appId = trow.seqValue
                }
        if (this.appId) {
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
        if (this.delete && this.delete == "YES") {
            deleteData(this.pageId, this.appId)
        } else {
            //Check whether it is a create or update
            if (this.pageId) {
                String pageNameExistsSQL = """select 1 as pageCount from GURCTLEPP  where GURCTLEPP_PAGE_ID = ? and GURCTLEPP_GUBAPPL_APP_ID = ? and UPPER(GURCTLEPP_PAGE_NAME) = ? """
                if (connectInfo.debugThis) println "Page ID Exists Query${pageNameExistsSQL}"
                this.conn.eachRow(pageNameExistsSQL, [this.pageId, this.appId,this.pageName.toUpperCase()]) {trow ->
                    if (trow.pageCount == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def updatesql = """update GURCTLEPP set GURCTLEPP_PAGE_NAME = ?,GURCTLEPP_DESCRIPTION = ?, GURCTLEPP_STATUS_INDICATOR = ?,GURCTLEPP_DISPLAY_SEQUENCE=?,GURCTLEPP_USER_ID=?,GURCTLEPP_DATA_ORIGIN=?,GURCTLEPP_ACTIVITY_DATE=to_date(?,'MMDDYYYY')
                                        where GURCTLEPP_GUBAPPL_APP_ID =? and GURCTLEPP_PAGE_ID=?"""

                try {
                    conn.executeUpdate(updatesql, [this.pageName, this.pageDescription, this.statusInd,this.displaySeq,this.userId,this.dataOrigin,this.activityDate, this.appId, this.pageId])
                    connectInfo.tableUpdate("GURCTLEPP", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GURCTLEPP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update GURCTLEPP ${this.pageName}}"
                        println "Problem executing update for table GURCTLEPP from GurCtleppDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into GURCTLEPP (GURCTLEPP_PAGE_ID,GURCTLEPP_GUBAPPL_APP_ID,GURCTLEPP_PAGE_NAME,GURCTLEPP_DESCRIPTION,GURCTLEPP_STATUS_INDICATOR,GURCTLEPP_DISPLAY_SEQUENCE,GURCTLEPP_USER_ID,GURCTLEPP_DATA_ORIGIN,GURCTLEPP_ACTIVITY_DATE) values (?,?,?,?,?,?,?,?,to_date(?,'MMDDYYYY'))"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.pageId,this.appId,this.pageName,this.pageDescription,this.statusInd,this.displaySeq,this.userId,this.dataOrigin,this.activityDate])
                    connectInfo.tableUpdate("GURCTLEPP", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GURCTLEPP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GURCTLEPP ${this.appName}}"
                        println "Problem executing insert  for table GURCTLEPP from GurCtleppDML.groovy: $e.message"
                    }
                }
            }
        }
    }

    }


    def deleteData(pageId, appId) {
        deleteData("GURCTLEPP", """delete FROM GURCTLEPP where GURCTLEPP_PAGE_ID=? and GURCTLEPP_GUBAPPL_APP_ID=?""", pageId, appId)
    }

}
