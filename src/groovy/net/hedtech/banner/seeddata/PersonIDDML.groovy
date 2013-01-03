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

/**
 * General Person ID DML
 */

public class PersonIDDML {
    def bannerid
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
    def spriden_alternate_id

    def PIDM
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public PersonIDDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public PersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertSpriden()
    }


    def parseXmlData() {
        def person = new XmlParser().parseText(xmlData)
        this.spriden_pidm = person.SPRIDEN_PIDM.text()
        this.spriden_id = person.SPRIDEN_ID.text()
        this.spriden_last_name = person.SPRIDEN_LAST_NAME.text()
        this.spriden_first_name = person.SPRIDEN_FIRST_NAME.text()
        this.spriden_mi = person.SPRIDEN_MI.text()
        this.spriden_change_ind = person.SPRIDEN_CHANGE_IND.text()
        this.spriden_entity_ind = person.SPRIDEN_ENTITY_IND.text()
        this.spriden_user = person.SPRIDEN_USER.text()
        this.spriden_origin = person.SPRIDEN_ORIGIN.text()
        this.spriden_search_last_name = person.SPRIDEN_SEARCH_LAST_NAME.text()
        this.spriden_search_first_name = person.SPRIDEN_SEARCH_FIRST_NAME.text()
        this.spriden_search_mi = person.SPRIDEN_SEARCH_MI.text()
        this.spriden_soundex_last_name = person.SPRIDEN_SOUNDEX_LAST_NAME.text()
        this.spriden_soundex_first_name = person.SPRIDEN_SOUNDEX_FIRST_NAME.text()
        this.spriden_ntyp_code = person.SPRIDEN_NTYP_CODE.text()
        this.spriden_create_user = person.SPRIDEN_CREATE_USER.text()
        this.spriden_create_date = person.SPRIDEN_CREATE_DATE.text()
        this.spriden_create_fdmn_code = person.SPRIDEN_CREATE_FDMN_CODE.text()
        this.spriden_surname_prefix = person.SPRIDEN_SURNAME_PREFIX.text()
        this.spriden_alternate_id = person.SPRIDEN_ALTERNATE_ID.text()
    }


    def insertSpriden() {
        PIDM = null
        String rowSQL = "select spriden_pidm from SPRIDEN  where spriden_id = ? "
        try {
            conn.eachRow(rowSQL, [this.spriden_id]) {row ->
                PIDM = row.spriden_pidm
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SPRIDEN rowid PersonIDDML.groovy: $e.message"
            }
        }
        if (!PIDM) {
            //  parm count is 13
            try {
                String API = "{call  gb_identification.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_id_inout      spriden_id_inout
                insertCall.registerOutParameter(1, java.sql.Types.VARCHAR)
                if (this.spriden_id) {insertCall.setString(1, this.spriden_id) }
                else {insertCall.setNull(1, this.spriden_id) }

                // parm 2 p_last_name  spriden_last_name VARCHAR2
                insertCall.setString(2, this.spriden_last_name)

                // parm 3 p_first_name  spriden_first_name VARCHAR2
                insertCall.setString(3, this.spriden_first_name)

                // parm 4 p_mi  spriden_mi VARCHAR2
                insertCall.setString(4, this.spriden_mi)

                // parm 5 p_change_ind  spriden_change_ind VARCHAR2
                insertCall.setString(5, this.spriden_change_ind)

                // parm 6 p_entity_ind  spriden_entity_ind VARCHAR2
                insertCall.setString(6, this.spriden_entity_ind)

                // parm 7 p_user  spriden_user VARCHAR2
                insertCall.setString(7, this.spriden_user)

                // parm 8 p_origin  spriden_origin VARCHAR2
                insertCall.setString(8, this.spriden_origin)

                // parm 9 p_ntyp_code  spriden_ntyp_code VARCHAR2
                insertCall.setString(9, this.spriden_ntyp_code)

                // parm 10 p_data_origin  spriden_data_origin VARCHAR2
                insertCall.setString(10, connectInfo.dataOrigin)
                // parm 11 p_surname_prefix  spriden_surname_prefix VARCHAR2
                insertCall.setString(11, this.spriden_surname_prefix)

                // parm 12 p_pidm_inout      spriden_pidm_inout VARCHAR2
                insertCall.registerOutParameter(12, java.sql.Types.INTEGER)
                if (this.spriden_pidm) {
                    rowSQL = "select spriden_pidm from SPRIDEN  where spriden_change_ind is null and spriden_pidm = ? "
                    def pidm = conn.firstRow(rowSQL, [this.spriden_pidm])
                    if (!pidm) {
                        insertCall.setInt(12, this.spriden_pidm.toInteger())
                    }
                    else {
                        insertCall.setNull(12, java.sql.Types.INTEGER)
                    }
                }

                // parm 13 p_rowid_out      spriden_rowid_out VARCHAR2
                insertCall.registerOutParameter(13, java.sql.Types.ROWID)

                if (connectInfo.debugThis) {
                    println "Insert SPRIDEN ${this.spriden_id} ${this.spriden_last_name} ${this.spriden_first_name}"
                }
                try {
                    insertCall.executeUpdate()
                    PIDM = insertCall.getInt(12)
                    connectInfo.tableUpdate("SPRIDEN", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SPRIDEN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SPRIDEN ${this.spriden_id} ${this.spriden_last_name} ${this.spriden_first_name}"
                        println "Problem executing insert for table SPRIDEN from PersonIDDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPRIDEN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SPRIDEN ${this.spriden_id} ${this.spriden_last_name} ${this.spriden_first_name}"
                    println "Problem executing insert for table SPRIDEN from PersonIDDML.groovy: $e.message"
                }
            }

        } else {

            if (this.spriden_alternate_id) {

                try {
                    String API = """{call  gb_identification.p_update(p_pidm => ?,
                                                                        p_id => ?,
                                                                        p_user => ?,
                                                                        p_data_origin => ?,
                                                                        p_rowid => ?)}"""
                    CallableStatement updateCall = this.connectCall.prepareCall(API)

                    // parm 1 p_pidm  old records pidm
                    updateCall.setInt(1, this.PIDM.toInteger())

                    // parm 2 p_id  New Spriden Id
                    updateCall.setString(2, this.spriden_alternate_id)

                    // parm 3 p_user  spriden_user VARCHAR2
                    updateCall.setString(3, this.spriden_user)

                    // parm 4 p_origin  spriden_origin VARCHAR2
                    updateCall.setString(4, this.spriden_origin)

                    // parm 5 p_rowid_out      spriden_rowid_out VARCHAR2
                    updateCall.registerOutParameter(5, java.sql.Types.ROWID)

                    if (connectInfo.debugThis) {
                        println "Update SPRIDEN id ${this.spriden_id} to alternate id  ${this.spriden_alternate_id}"
                    }
                    try {
                        updateCall.executeUpdate()
                        connectInfo.tableUpdate("SPRIDEN", 0, 0, 1, 0, 0)
                    }
                    catch (Exception e) {
                        connectInfo.tableUpdate("SPRIDEN", 0, 0, 0, 1, 0)
                        if (connectInfo.showErrors) {
                            println "Update SPRIDEN id ${this.spriden_id} to alternate id  ${this.spriden_alternate_id}"
                            println "Problem executing Update for table SPRIDEN from PersonIDDML.groovy: $e.message"
                        }
                    }
                    finally {
                        updateCall.close()
                    }
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SPRIDEN", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SPRIDEN id ${this.spriden_id} to alternate id  ${this.spriden_alternate_id}"
                        println "Problem executing Update for table SPRIDEN from PersonIDDML.groovy: $e.message"
                    }
                }


            }
        }
    }
}