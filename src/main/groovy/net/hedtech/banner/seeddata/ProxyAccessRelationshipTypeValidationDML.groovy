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
class ProxyAccessRelationshipTypeValidationDML {
    def gtvretpCode
    def gtvretpDesc
    def gtvretpSystemReqInd
    def gtvretpActivityDate
    def gtvretpUserId
    def gtvretpCreateDate
    def gtvretpCreateUser
    def gtvretpDataOrigin
    def toDelete //Set to Y or N


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public ProxyAccessRelationshipTypeValidationDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ProxyAccessRelationshipTypeValidationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()

        if (this.toDelete == "Y"){
            gtvretpDelete()
        }
        else{
            gtvretpInsert()
        }
    }


    def parseXmlData() {
        def gtvretp = new XmlParser().parseText(xmlData)

        this.gtvretpCode = gtvretp?.GTVRETP_CODE?.text()
        this.gtvretpDesc = gtvretp?.GTVRETP_DESCRIPTION?.text()
        this.gtvretpSystemReqInd = gtvretp?.GTVRETP_SYSTEM_REQ_IND?.text()
        this.gtvretpActivityDate = gtvretp?.GTVRETP_ACTIVITY_DATE?.text()
        this.gtvretpUserId = gtvretp?.GTVRETP_USER_ID?.text()
        this.gtvretpCreateDate = gtvretp?.GTVRETP_CREATE_DATE?.text()
        this.gtvretpCreateUser = gtvretp?.GTVRETP_CREATE_USER?.text()
        this.gtvretpDataOrigin = gtvretp?.GTVRETP_DATA_ORIGIN?.text()
        this.toDelete = gtvretp?.TO_DELETE?.text()
    }


    def gtvretpInsert() {
        String GTVRETPAPI = """INSERT INTO gtvretp (gtvretp_code, gtvretp_desc, gtvretp_system_req_ind, gtvretp_activity_date, gtvretp_user_id, gtvretp_create_date, gtvretp_create_user, gtvretp_data_origin) VALUES (?,?,?,?,?,?,?,?) """

        CallableStatement insertCallGTVRETP = this.connectCall.prepareCall(GTVRETPAPI)
        insertCallGTVRETP.setString(1, this.gtvretpCode)
        insertCallGTVRETP.setString(2, this.gtvretpDesc)
        insertCallGTVRETP.setString(3, this.gtvretpSystemReqInd)
        insertCallGTVRETP.setString(4, this.gtvretpActivityDate)
        insertCallGTVRETP.setString(5, this.gtvretpUserId)
        insertCallGTVRETP.setString(6, this.gtvretpCreateDate)
        insertCallGTVRETP.setString(7, this.gtvretpCreateUser)
        insertCallGTVRETP.setString(8, this.gtvretpDataOrigin)


        try {
            insertCallGTVRETP.execute()
            connectInfo.tableUpdate("GTVRETP", 0, 1, 0, 0, 0)
        } catch (Exception e) {
            connectInfo.tableUpdate("GTVRETP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert GTVRETP ${this.gtvretpUserId}}"
                println "Problem executing insert for table GTVRETP from ProxyAccessRelationshipTypeValidationDML.groovy: $e.message"
            }
        }
        finally {
            insertCallGTVRETP.close()
        }
    }

    def gtvretpDelete(){
        String GTVRETPAPI = """DELETE FROM GTVRETP WHERE GTVRETP_CODE = ?"""

        CallableStatement deleteCallGTVRETP = this.connectCall.prepareCall(GTVRETPAPI)
        deleteCallGTVRETP.setString(1, this.gtvretpCode)


        try {
            def deleteRows = deleteCallGTVRETP.executeUpdate()
            connectInfo.tableUpdate("GTVRETP", 0, 0, 0, 0, deleteRows)
        } catch (Exception e) {
            connectInfo.tableUpdate("GTVRETP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Delete GTVRETP ${this.gtvretpUserId}}"
                println "Problem executing delete for table GTVRETP from ProxyAccessRelationshipTypeValidationDML.groovy: $e.message"
            }
        }
        finally {
            deleteCallGTVRETP.close()
        }
    }


}
