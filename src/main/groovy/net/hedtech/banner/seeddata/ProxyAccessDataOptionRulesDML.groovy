/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * ProxyAccessRelationshipTypeValidation DML
 */
class ProxyAccessDataOptionRulesDML {
    def gebsrtpSystCode
    def gebsrtpRetpCode
    def gebsrtpActivityDate
    def gebsrtpUserId
    def gebsrtpCreateDate
    def gebsrtpCreateUser
    def gebsrtpDataOrigin
    def toDelete //Set to Y or N


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public ProxyAccessDataOptionRulesDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ProxyAccessDataOptionRulesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()

        if (this.toDelete == "Y"){
            gebsrtpDelete()
        }
        else{
            gebsrtpInsert()
        }
    }


    def parseXmlData() {
        def gebsrtp = new XmlParser().parseText(xmlData)

        this.gebsrtpSystCode = gebsrtp?.GEBSRTP_SYST_CODE?.text()
        this.gebsrtpRetpCode = gebsrtp?.GEBSRTP_RETP_CODE?.text()
        this.gebsrtpActivityDate = gebsrtp?.GEBSRTP_ACTIVITY_DATE?.text()
        this.gebsrtpUserId = gebsrtp?.GEBSRTP_USER_ID?.text()
        this.gebsrtpCreateDate = gebsrtp?.GEBSRTP_CREATE_DATE?.text()
        this.gebsrtpCreateUser = gebsrtp?.GEBSRTP_CREATE_USER?.text()
        this.gebsrtpDataOrigin = gebsrtp?.GEBSRTP_DATA_ORIGIN?.text()
        this.toDelete = gebsrtp?.TO_DELETE?.text()
    }


    def gebsrtpInsert() {
        String GEBSRTPAPI = """INSERT INTO gebsrtp (gebsrtp_syst_code, gebsrtp_retp_code, gebsrtp_activity_date, gebsrtp_user_id, gebsrtp_create_date, gebsrtp_create_user, gebsrtp_data_origin) VALUES (?,?,?,?,?,?,?)"""

        CallableStatement insertCallGEBSRTP = this.connectCall.prepareCall(GEBSRTPAPI)
        insertCallGEBSRTP.setString(1, this.gebsrtpSystCode)
        insertCallGEBSRTP.setString(2, this.gebsrtpRetpCode)
        insertCallGEBSRTP.setString(3, this.gebsrtpActivityDate)
        insertCallGEBSRTP.setString(4, this.gebsrtpUserId)
        insertCallGEBSRTP.setString(5, this.gebsrtpCreateDate)
        insertCallGEBSRTP.setString(6, this.gebsrtpCreateUser)
        insertCallGEBSRTP.setString(7, this.gebsrtpDataOrigin)

        try {
            insertCallGEBSRTP.execute()
            connectInfo.tableUpdate("GEBSRTP", 0, 1, 0, 0, 0)
        } catch (Exception e) {
            connectInfo.tableUpdate("GEBSRTP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert GEBSRTP ${this.gebsrtpUserId}}"
                println "Problem executing insert for table GEBSRTP from ProxyAccessDataOptionRulesDML.groovy: $e.message"
            }
        }
        finally {
            insertCallGEBSRTP.close()
        }
    }

    def gebsrtpDelete(){
        String GEBSRTPAPI = """DELETE FROM GEBSRTP WHERE GEBSRTP_SYST_CODE = ? AND GEBSRTP_RETP_CODE = ?"""

        CallableStatement deleteCallGEBSRTP = this.connectCall.prepareCall(GEBSRTPAPI)
        deleteCallGEBSRTP.setString(1, this.gebsrtpSystCode)
        deleteCallGEBSRTP.setString(2, this.gebsrtpRetpCode)


        try {
            def deleteRows = deleteCallGEBSRTP.executeUpdate()
            connectInfo.tableUpdate("GEBSRTP", 0, 0, 0, 0, deleteRows)
        } catch (Exception e) {
            connectInfo.tableUpdate("GEBSRTP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Delete GEBSRTP ${this.gebsrtpUserId}}"
                println "Problem executing delete for table GEBSRTP from ProxyAccessDataOptionRulesDML.groovy: $e.message"
            }
        }
        finally {
            deleteCallGEBSRTP.close()
        }
    }


}
