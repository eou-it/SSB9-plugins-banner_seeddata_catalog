/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * General Person ID DML
 */

public class CreateOracleUserDML {

    def bannerid
    def newOracleId
    def oracleId
    def generalClass
    def objectName
    def objectRole
    def readonly


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public CreateOracleUserDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public CreateOracleUserDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()

        if (this.newOracleId) {
            createUserId()
        }
        if (this.generalClass) {
            createGeneralClass()
        }
        if (this.objectName) {
            createObject()
        }
    }


    def parseXmlData() {
        def oracleUser = new XmlParser().parseText(xmlData)
        if (oracleUser.CREATE_USER?.text()) {
            this.newOracleId = oracleUser.CREATE_USER.text()
            this.readonly = oracleUser.READONLY.text()
        }
        if (oracleUser.GENERAL_CLASS?.text()) {
            this.oracleId = oracleUser.ORACLEID.text()
            this.generalClass = oracleUser.GENERAL_CLASS.text()
        }
        if (oracleUser.OBJECT?.text()) {
            this.oracleId = oracleUser.ORACLEID.text()
            this.objectName = oracleUser.OBJECT.text()
            this.objectRole = oracleUser.OBJECT_ROLE.text()
        }
    }


    def createUserId() {

        def sqlR = 'select count(*) cnt from dba_users where username = ?'
        def result
        try {
            result = conn.firstRow(sqlR, [this.newOracleId])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could select create Oracle Id,  ${this.newOracleId}. $e.message"
            }
        }

        if (result.cnt == 0) {

            def sql1 = "create user " + newOracleId + "  identified by u_pick_it"
            def sql2
            if (readonly == "YES") {
                sql2 = "grant connect, resource, ban_default_q to " + newOracleId
            } else {
                sql2 = "grant connect, resource, ban_default_q, ban_default_m to " + newOracleId
            }

            def sql3 = "alter user " + newOracleId + " grant connect through banproxy "
            println "sql1: ${sql1} ${sql2} ${sql3}"
            try {
                conn.executeUpdate(sql1)
                conn.executeUpdate(sql2)
                conn.executeUpdate(sql3)
                connectInfo.tableUpdate('DBA_USERS', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not create Oracle Id,  ${this.newOracleId}. $e.message"
                }
            }
        }

    }


    def createGeneralClass() {
        def sqlf = "select count(*) cnt from gurucls where gurucls_userid = ? and gurucls_class_code = ?"
        def result
        try {
            result = conn.firstRow(sqlf, [this.oracleId, this.generalClass])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could select GURUCLS,  ${this.oracleId} ${this.generalClass}. $e.message"
            }
        }
        if (result.cnt == 0) {
            def sql1 = """insert into gurucls ( gurucls_userid, gurucls_class_code, gurucls_activity_date,
                                              gurucls_user_id)
                          values(  ?, ?, sysdate, user)  """

            try {
                conn.executeInsert(sql1, [this.oracleId.toString(), this.generalClass.toString()])
                connectInfo.tableUpdate('GURUCLS', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not create General Class,  ${this.oracleId} ${this.generalClass}. $e.message"
                }
            }
        }
    }


    def createObject() {
        def sqlf = "select count(*) cnt from  guruobj  Where Guruobj_Userid = ? And Guruobj_Object = ?"
        def result
        try {
            result = conn.firstRow(sqlf, [this.oracleId, this.generalClass])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could select GURUOBJ,  ${this.oracleId} ${this.generalClass}. $e.message"
            }
        }
        if (result.cnt == 0) {
            def sql1 = """Insert Into Guruobj(Guruobj_Object,Guruobj_Role,Guruobj_Userid,Guruobj_Activity_Date,
                              Guruobj_User_Id,Guruobj_Comments,Guruobj_Data_Origin)
                        values( ?,?,?,sysdate,user,'TEST','BANNER' )  """
            try {
                conn.executeInsert(sql1, [this.objectName.toString(), this.objectRole.toString(),
                                          this.oracleId.toString() ])
                connectInfo.tableUpdate('GURUOBJ', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {

                if (connectInfo.showErrors) {
                    println "Could not create access to object,  ${this.oracleId.text()} ${this.objectName.text()}. $e.message"
                }
            }
        }
    }

}