/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * General Person ID DML
 */

public class ThirdPartyAccessPinDML {

    def bannerid
    def gobtpac_pidm
    def gobtpac_pin_disabled_ind
    def gobtpac_usage_accept_ind
    def gobtpac_activity_date
    def gobtpac_user
    def gobtpac_pin
    def gobptpac_pin_exp_date
    def gobtpac_external_user
    def gobtpac_question
    def gobtpac_response
    def gobtpac_insert_source
    def gobtpac_ldap_user
    def gobtpac_data_origin
    def gobtpac_salt


    def PIDM
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public ThirdPartyAccessPinDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ThirdPartyAccessPinDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertGobtpac()
    }


    def parseXmlData() {
        def gobtpac = new XmlParser().parseText(xmlData)
        this.bannerid = gobtpac.BANNERID
        this.gobtpac_pidm = gobtpac.GOBTPAC_PIDM.text()
        this.gobtpac_pin_disabled_ind = gobtpac.GOBTPAC_PIN_DISABLED_IND.text()
        this.gobtpac_usage_accept_ind = gobtpac.GOBTPAC_USAGE_ACCEPT_IND.text()
        this.gobtpac_activity_date = gobtpac.GOBTPAC_ACTIVITY_DATE.text()
        this.gobtpac_user = gobtpac.GOBTPAC_USER.text()
        this.gobtpac_pin = gobtpac.GOBTPAC_PIN.text()
        this.gobptpac_pin_exp_date = gobtpac.GOBTPAC_PIN_EXP_DATE.text()
        this.gobtpac_external_user = gobtpac.GOBTPAC_EXTERNAL_USER.text()
        this.gobtpac_question = gobtpac.GOBTPAC_QUESTION.text()
        this.gobtpac_response = gobtpac.GOBTPAC_RESPONSE.text()
        this.gobtpac_insert_source = gobtpac.GOBTPAC_INSERT_SOURCE.text()
        this.gobtpac_ldap_user = gobtpac.GOBTPAC_LDAP_USER.text()
        this.gobtpac_data_origin = gobtpac.GOBTPAC_DATA_ORIGIN.text()
    }


    def insertGobtpac() {
        PIDM = null
        String pidmsql = """select * from spriden  where spriden_id = ?"""

        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) {trow ->
                PIDM = trow.spriden_pidm
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in ThirdPartyAccessPinDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }


        if (PIDM) {
            try {
                def cntGobtpac = 0
                String gobtpacsql = """select * from gobtpac  where gobtpac_pidm = ?"""
                this.conn.eachRow(gobtpacsql, [PIDM]) {trow ->
                    cntGobtpac++
                }
                if (cntGobtpac) deleteData(PIDM)


                String API = "{call  gb_third_party_access.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                insertCall.setInt(1, this.PIDM.toInteger())
                insertCall.setString(2, this.gobtpac_pin_disabled_ind)
                insertCall.setString(3, this.gobtpac_usage_accept_ind)
                insertCall.setString(4, this.gobtpac_user)
                insertCall.setString(5, this.gobtpac_pin)
                if (this.gobptpac_pin_exp_date == "") { insertCall.setNull(6, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.gobptpac_pin_exp_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(6, sqlDate)
                }
                insertCall.setString(7, this.gobtpac_question)
                insertCall.setString(8, this.gobtpac_response)
                insertCall.setString(9, this.gobtpac_insert_source)
                insertCall.setString(10, this.gobtpac_ldap_user)
                insertCall.setString(11, this.gobtpac_data_origin)
                insertCall.setNull(12, java.sql.Types.VARCHAR)
                insertCall.setString(13, 'Y')
                insertCall.setString(14, 'N')
                insertCall.setString(15, 'N')
                insertCall.setString(16, 'Y')

                insertCall.registerOutParameter(17, java.sql.Types.VARCHAR)
                insertCall.registerOutParameter(18, java.sql.Types.ROWID)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("GOBTPAC", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GOBTPAC", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GOBTPAC ${this.bannerid}}"
                        println "Problem executing insert for table GOBTPAC from ThirdPartyAccessPinDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GOBTPAC", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GOBTPAC ${this.bannerid} }"
                    println "Problem executing insert for table GOBTPAC from ThirdPartyAccessPinDML.groovy: $e.message"
                }
            }

        }
    }


    def deleteData(pidm) {
        deleteData("GOBTPAC", "delete GOBTPAC where GOBTPAC_pidm = ?  ", pidm)
        deleteData("GORPAUD", "delete GORPAUD where GORPAUD_pidm = ?  ", pidm)
    }


    private def deleteData(String tableName, String sql, BigDecimal pidm) {
        try {
            int delRows = conn.executeUpdate(sql, [pidm])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for gobtpac ${pidm} from ThirdPartyAccessPinDM.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
