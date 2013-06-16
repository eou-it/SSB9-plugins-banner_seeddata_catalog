/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * General Person ID DML for Students
 * Save the PIDM in the InputData.saveStudentPidm and delete existing student data
 */

public class SSUserPersonIDDML {
    def spriden_pidm
    def spriden_id
    def spriden_last_name
    def spriden_first_name
    def spriden_mi
    def spriden_change_ind
    def spriden_entity_ind
    def spriden_user
    def spriden_origin
    def spriden_search_last_name
    def spriden_search_first_name
    def spriden_search_mi
    def spriden_soundex_last_name
    def spriden_soundex_first_name
    def spriden_ntyp_code
    def spriden_create_user
    def spriden_create_date
    def spriden_create_fdmn_code
    def spriden_surname_prefix


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def pidm


    public SSUserPersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processSelfserviceUser()

    }


    def processSelfserviceUser() {
        def apiData = new XmlParser().parseText(xmlData)
        String ssql = """select * from spriden where spriden_id = ? and spriden_change_ind is null"""
        def cntSpriden = 0

        try {
            this.conn.eachRow(ssql, [apiData.BANNERID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in SSUserPersonIDDML,  ${apiData.SPRIDEN_ID.text()}  from SPRIDEN. $e.message"
            }
        }

        if (cntSpriden == 0) {
            def newSpriden = new PersonIDDML(connectInfo, conn, connectCall, xmlData)
            connectInfo.savePidm = newSpriden.PIDM
        }
        else {
            connectInfo.savePidm = pidm
        }


        // delete data so we can re-add because most do not have an update process
        if (cntSpriden && connectInfo.replaceData) {
            deleteData()
        }

    }


    def deleteData() {
        deleteData("GOBTPAC", "delete GOBTPAC where GOBTPAC_pidm = ?  ")
        deleteData("TWGRROLE", "delete TWGRROLE where TWGRROLE_pidm = ? ")
    }


    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [connectInfo.savePidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for person ${connectInfo.savePidm} from StudentPersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
