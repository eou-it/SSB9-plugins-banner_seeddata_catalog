/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class GurctleppDML {

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

    public GurctleppDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public GurctleppDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

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
        this.delete = data.DELETE?.text()
        this.appName = data.GURCTLEP_APP_NAME.text()
        this.pageName = data.GURCTLEP_PAGE_URL.text()
        this.pageDescription = data.GURCTLEP_PAGE_DESCRIPTION.text()
        this.statusInd = data.GURCTLEP_STATUS_INDICATOR.text()
        this.displaySeq = data.GURCTLEP_DISPLAY_SEQUENCE.text()
        this.userId = data.GURCTLEP_USER_ID.text()
        this.dataOrigin = data.GURCTLEP_DATA_ORIGIN.text()
        this.activityDate = data.GURCTLEP_ACTIVITY_DATE.text()
    }

    def insertGuroCtleppData() {
        String appNumSql = """select GUBAPPL_APP_ID as seqValue from gubappl  where UPPER(GUBAPPL_APP_NAME)= ? and GUBAPPL_USER_ID = ? and GUBAPPL_DATA_ORIGIN = ?"""
        def params = [this.appName.toUpperCase(),this.userId,this.dataOrigin]
        if (connectInfo.debugThis) println appNumSql
        this.conn.eachRow(appNumSql, params)
                {
                    trow ->
                        this.appId = trow.seqValue
                }
        if (this.appId) {
        String pageIdSql = """select GURCTLEP_PAGE_ID as pageIdValue from GURCTLEP  where GURCTLEP_GUBAPPL_APP_ID= ? and UPPER(GURCTLEP_PAGE_URL) = ? and GURCTLEP_USER_ID = ? and GURCTLEP_DATA_ORIGIN = ?"""
        def pageIdparams = [this.appId,this.pageName.toUpperCase(),this.userId,this.dataOrigin]
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
                String pageNameExistsSQL = """select 1 as pageCount from GURCTLEP  where GURCTLEP_PAGE_ID = ? and GURCTLEP_GUBAPPL_APP_ID = ? and UPPER(GURCTLEP_PAGE_URL) = ? """
                if (connectInfo.debugThis) println "Page ID Exists Query${pageNameExistsSQL}"
                this.conn.eachRow(pageNameExistsSQL, [this.pageId, this.appId,this.pageName.toUpperCase()]) {trow ->
                    if (trow.pageCount == 1)
                        this.update = true
                }
            }
            if (this.update) {
                def updatesql = """update GURCTLEP set GURCTLEP_PAGE_URL = ?,GURCTLEP_DESCRIPTION = ?, GURCTLEP_STATUS_INDICATOR = ?,GURCTLEP_DISPLAY_SEQUENCE=?,GURCTLEP_USER_ID=?,GURCTLEP_DATA_ORIGIN=?,GURCTLEP_ACTIVITY_DATE=?
                                        where GURCTLEP_GUBAPPL_APP_ID =? and GURCTLEP_PAGE_ID=?"""

                try {
                    conn.executeUpdate(updatesql, [this.pageName, this.pageDescription, this.statusInd,this.displaySeq,this.userId,this.dataOrigin,this.activityDate, this.appId, this.pageId])
                    connectInfo.tableUpdate("GURCTLEP", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GURCTLEP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update GURCTLEP ${this.pageName}}"
                        println "Problem executing update for table GURCTLEP from GurctleppDML.groovy: $e.message"
                    }
                }
            } else {
                def insertSQL = """insert into GURCTLEP (GURCTLEP_PAGE_ID,GURCTLEP_GUBAPPL_APP_ID,GURCTLEP_PAGE_URL,GURCTLEP_DESCRIPTION,GURCTLEP_STATUS_INDICATOR,GURCTLEP_DISPLAY_SEQUENCE,GURCTLEP_USER_ID,GURCTLEP_DATA_ORIGIN,GURCTLEP_ACTIVITY_DATE) values (?,?,?,?,?,?,?,?,?)"""
                if (connectInfo.debugThis) println insertSQL
                try {
                    conn.executeUpdate(insertSQL, [this.pageId,this.appId,this.pageName,this.pageDescription,this.statusInd,this.displaySeq,this.userId,this.dataOrigin,this.activityDate])
                    connectInfo.tableUpdate("GURCTLEP", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GURCTLEP", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GURCTLEP ${this.appName}}"
                        println "Problem executing insert  for table GURCTLEP from GurctleppDML.groovy: $e.message"
                    }
                }
            }
        }
    }
    }

    def deleteData(pageId, appId) {
        int delRows
        def deleteSql = """delete FROM GURCTLEP where GURCTLEP_PAGE_ID=? and GURCTLEP_GUBAPPL_APP_ID=?"""
        if (connectInfo.debugThis) println deleteSql
        try {

            delRows = conn.executeUpdate(deleteSql, [pageId,appId])
            connectInfo.tableUpdate("GURCTLEP", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for GURCTLEP table with Page Id ${appName} from GurctleppDML.groovy: $e.message"
                println "${deleteSql}"
            }
        }
    }

}
