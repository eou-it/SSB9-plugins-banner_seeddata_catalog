/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * GeneralAutoNewPagesDML
 * This class will take care of inserting the GURAOBJ table
 * entries for a Banner 9 only new page
 */

public class GeneralAutoNewPagesDML {

    def bannerid
    def oracleId
    def objectName
    def readonly

    def guraobjObject
    def guraobjDefaultRole
    def guraobjCurrentVersion
    def guraobjSysiCode
    def guraobjActivityDate
    def guraobjUserId
    def guraobjOwner
    def guraobjAccessTrackingInd
    def guraobjCurrentVersionAlt

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public GeneralAutoNewPagesDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public GeneralAutoNewPagesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()

        if (this.guraobjObject){
            createGuraobjObject()
        }
    }


    def parseXmlData() {
        def newPage = new XmlParser().parseText(xmlData)

        if (newPage.GURAOBJ_OBJECT?.text()) {
            this.guraobjObject = newPage.GURAOBJ_OBJECT.text()
        }
        if (newPage.GURAOBJ_DEFAULT_ROLE?.text()) {
            this.guraobjDefaultRole = newPage.GURAOBJ_DEFAULT_ROLE.text()
        }
        if (newPage.GURAOBJ_CURRENT_VERSION?.text()) {
            this.guraobjCurrentVersion = newPage.GURAOBJ_CURRENT_VERSION.text()
        }
        if (newPage.GURAOBJ_SYSI_CODE?.text()) {
            this.guraobjSysiCode = newPage.GURAOBJ_SYSI_CODE.text()
        }
        if (newPage.GURAOBJ_ACTIVITY_DATE?.text()) {
            this.guraobjActivityDate = newPage.GURAOBJ_ACTIVITY_DATE.text()
        }
        if (newPage.GURAOBJ_USER_ID?.text()) {
            this.guraobjUserId = newPage.GURAOBJ_USER_ID.text()
        }
        if (newPage.GURAOBJ_OWNER?.text()) {
            this.guraobjOwner = newPage.GURAOBJ_OWNER.text()
        }
        if (newPage.GURAOBJ_ACCESS_TRACKING_IND?.text()) {
            this.guraobjAccessTrackingInd = newPage.GURAOBJ_ACCESS_TRACKING_IND.text()
        }
        if (newPage.GURAOBJ_CURRENT_VERSION_ALT?.text()) {
            this.guraobjCurrentVersionAlt = newPage.GURAOBJ_CURRENT_VERSION_ALT.text()
        }

    }

    def createGuraobjObject() {
        def sqlf = "select count(*) cnt from  guraobj  Where Guraobj_Object = ?"
        def result
        try {
            result = conn.firstRow(sqlf, [this.guraobjObject])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could select GURAOBJ,  ${this.guraobjObject}. $e.message"
            }
        }
        if (result.cnt == 0) {
            def sql1 = """Insert Into Guraobj(Guraobj_Object,Guraobj_Default_Role, Guraobj_Current_Version, 
                          Guraobj_Sysi_Code, Guraobj_Activity_Date, Guraobj_User_id, Guraobj_Owner, Guraobj_Access_Tracking_Ind,
                              Guraobj_Current_Version_Alt)
                        values( ?,?,?,?,?,?,?,?,? )  """
            try {
                conn.executeInsert(sql1, [this.guraobjObject.toString(), this.guraobjDefaultRole.toString(), this.guraobjCurrentVersion.toString(),
                                          this.guraobjSysiCode.toString(), this.guraobjActivityDate.toString(), this.guraobjUserId.toString(), this.guraobjOwner.toString(), this.guraobjAccessTrackingInd.toString(),
                                          this.guraobjCurrentVersionAlt.toString() ])
                connectInfo.tableUpdate('GURAOBJ', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {

                if (connectInfo.showErrors) {
                    println "Could not create access to object,  ${this.oracleId.text()} ${this.objectName.text()}. $e.message"
                }
            }
        }
    }


}