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
import java.sql.CallableStatement
import java.sql.Connection

/**
 * Insert and update telephone data
 * The update will not work correctly because of the missing rowid
 */
public class TelephoneDML {
    def bannerid
    def sprtele_pidm
    def sprtele_seqno
    def sprtele_tele_code
    def sprtele_phone_area
    def sprtele_phone_number
    def sprtele_phone_ext
    def sprtele_status_ind
    def sprtele_atyp_code
    def sprtele_addr_seqno
    def sprtele_primary_ind
    def sprtele_unlist_ind
    def sprtele_comment
    def sprtele_intl_access
    def sprtele_ctry_code_phone

    java.sql.RowId tableRow = null
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public TelephoneDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public TelephoneDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertSprtele()
    }


    def parseXmlData() {
        def telephone = new XmlParser().parseText(xmlData)
        if (telephone.BANNERID) {
            this.sprtele_pidm = connectInfo.saveStudentPidm
            this.bannerid = telephone.BANNERID.text()
        }
        else {
            this.sprtele_pidm = telephone.SPRTELE_PIDM.text()
        }

        this.sprtele_seqno = telephone.SPRTELE_SEQNO.text()
        this.sprtele_tele_code = telephone.SPRTELE_TELE_CODE.text()
        this.sprtele_phone_area = telephone.SPRTELE_PHONE_AREA.text()
        this.sprtele_phone_number = telephone.SPRTELE_PHONE_NUMBER.text()
        this.sprtele_phone_ext = telephone.SPRTELE_PHONE_EXT.text()
        this.sprtele_status_ind = telephone.SPRTELE_STATUS_IND.text()
        this.sprtele_atyp_code = telephone.SPRTELE_ATYP_CODE.text()
        this.sprtele_addr_seqno = telephone.SPRTELE_ADDR_SEQNO.text()
        this.sprtele_primary_ind = telephone.SPRTELE_PRIMARY_IND.text()
        this.sprtele_unlist_ind = telephone.SPRTELE_UNLIST_IND.text()
        this.sprtele_comment = telephone.SPRTELE_COMMENT.text()
        this.sprtele_intl_access = telephone.SPRTELE_INTL_ACCESS.text()
        this.sprtele_ctry_code_phone = telephone.SPRTELE_CTRY_CODE_PHONE.text()

    }


    def insertSprtele() {

        tableRow = null
        String rowSQL = "select rowid table_row from SPRTELE \n "
        rowSQL += " where sprtele_pidm = ${this.sprtele_pidm} \n "
        rowSQL += " and sprtele_tele_code = '${this.sprtele_tele_code}' \n "
        rowSQL += " and sprtele_phone_number= '${this.sprtele_phone_number}'"
        try {
            conn.eachRow(rowSQL) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SPRTELE rowid TelephoneDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 17
            try {
                String API = "{call  gb_telephone.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  sprtele_pidm NUMBER
                if ((this.sprtele_pidm == "") || (this.sprtele_pidm == null) || (!this.sprtele_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.sprtele_pidm.toInteger())
                }

                // parm 2 p_tele_code  sprtele_tele_code VARCHAR2
                insertCall.setString(2, this.sprtele_tele_code)

                // parm 3 p_phone_area  sprtele_phone_area VARCHAR2
                insertCall.setString(3, this.sprtele_phone_area)

                // parm 4 p_phone_number  sprtele_phone_number VARCHAR2
                insertCall.setString(4, this.sprtele_phone_number)

                // parm 5 p_phone_ext  sprtele_phone_ext VARCHAR2
                insertCall.setString(5, this.sprtele_phone_ext)

                // parm 6 p_status_ind  sprtele_status_ind VARCHAR2
                insertCall.setString(6, this.sprtele_status_ind)

                // parm 7 p_atyp_code  sprtele_atyp_code VARCHAR2
                insertCall.setString(7, this.sprtele_atyp_code)

                // parm 8 p_addr_seqno  sprtele_addr_seqno NUMBER
                if ((this.sprtele_addr_seqno == "") || (this.sprtele_addr_seqno == null) || (!this.sprtele_addr_seqno)) { insertCall.setNull(8, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(8, this.sprtele_addr_seqno.toInteger())
                }

                // parm 9 p_primary_ind  sprtele_primary_ind VARCHAR2
                insertCall.setString(9, this.sprtele_primary_ind)

                // parm 10 p_unlist_ind  sprtele_unlist_ind VARCHAR2
                insertCall.setString(10, this.sprtele_unlist_ind)

                // parm 11 p_comment  sprtele_comment VARCHAR2
                insertCall.setString(11, this.sprtele_comment)

                // parm 12 p_intl_access  sprtele_intl_access VARCHAR2
                insertCall.setString(12, this.sprtele_intl_access)

                // parm 13 p_data_origin  sprtele_data_origin VARCHAR2
                insertCall.setString(13, connectInfo.dataOrigin)
                // parm 14 p_user_id  sprtele_user_id VARCHAR2
                insertCall.setString(14, connectInfo.userID)
                // parm 15 p_ctry_code_phone  sprtele_ctry_code_phone VARCHAR2
                insertCall.setString(15, this.sprtele_ctry_code_phone)

                // parm 16 p_seqno_out  sprtele_seqno_out VARCHAR2
                insertCall.registerOutParameter(16, java.sql.Types.INTEGER)

                // parm 17 p_rowid_out  sprtele_rowid_out VARCHAR2
                insertCall.registerOutParameter(17, java.sql.Types.ROWID)

                if (connectInfo.debugThis) {
                    println "Insert SPRTELE ${this.sprtele_pidm} ${this.sprtele_tele_code} ${this.sprtele_phone_area}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SPRTELE", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SPRTELE ${this.sprtele_pidm} ${this.sprtele_tele_code} ${this.sprtele_phone_area}"
                        println "Problem executing insert for table SPRTELE from TelephoneDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SPRTELE ${this.sprtele_pidm} ${this.sprtele_tele_code} ${this.sprtele_phone_area}"
                    println "Problem setting up insert for table SPRTELE from TelephoneDML.groovy: $e.message"
                }
            }
        }
        else if (connectInfo.replaceData) {
            updateSprtele()
        }
    }


    def updateSprtele() {

        //  parm count is 17
        try {
            String API = "{call  gb_telephone.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            // parm 1 p_pidm  sprtele_pidm NUMBER
            if ((this.sprtele_pidm == "") || (this.sprtele_pidm == null) || (!this.sprtele_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(1, this.sprtele_pidm.toInteger())
            }

            // parm 2 p_seqno  sprtele_seqno NUMBER
            if ((this.sprtele_seqno == "") || (this.sprtele_seqno == null) || (!this.sprtele_seqno)) { insertCall.setNull(2, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(2, this.sprtele_seqno.toInteger())
            }

            // parm 3 p_tele_code  sprtele_tele_code VARCHAR2
            insertCall.setString(3, this.sprtele_tele_code)

            // parm 4 p_phone_area  sprtele_phone_area VARCHAR2
            insertCall.setString(4, this.sprtele_phone_area)

            // parm 5 p_phone_number  sprtele_phone_number VARCHAR2
            insertCall.setString(5, this.sprtele_phone_number)

            // parm 6 p_phone_ext  sprtele_phone_ext VARCHAR2
            insertCall.setString(6, this.sprtele_phone_ext)

            // parm 7 p_status_ind  sprtele_status_ind VARCHAR2
            insertCall.setString(7, this.sprtele_status_ind)

            // parm 8 p_atyp_code  sprtele_atyp_code VARCHAR2
            insertCall.setString(8, this.sprtele_atyp_code)

            // parm 9 p_addr_seqno  sprtele_addr_seqno NUMBER
            if ((this.sprtele_addr_seqno == "") || (this.sprtele_addr_seqno == null) || (!this.sprtele_addr_seqno)) { insertCall.setNull(9, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(9, this.sprtele_addr_seqno.toInteger())
            }

            // parm 10 p_primary_ind  sprtele_primary_ind VARCHAR2
            insertCall.setString(10, this.sprtele_primary_ind)

            // parm 11 p_unlist_ind  sprtele_unlist_ind VARCHAR2
            insertCall.setString(11, this.sprtele_unlist_ind)

            // parm 12 p_comment  sprtele_comment VARCHAR2
            insertCall.setString(12, this.sprtele_comment)

            // parm 13 p_intl_access  sprtele_intl_access VARCHAR2
            insertCall.setString(13, this.sprtele_intl_access)

            // parm 14 p_data_origin  sprtele_data_origin VARCHAR2
            insertCall.setString(14, connectInfo.dataOrigin)
            // parm 15 p_user_id  sprtele_user_id VARCHAR2
            insertCall.setString(15, connectInfo.userID)
            // parm 16 p_ctry_code_phone  sprtele_ctry_code_phone VARCHAR2
            insertCall.setString(16, this.sprtele_ctry_code_phone)

            // parm 17 p_rowid  sprtele_rowid VARCHAR2
            insertCall.setNull(17, java.sql.Types.ROWID)
            //  comment out till this works with groovy insertCall.setRowId(17,tableRow)

            if (connectInfo.debugThis) {
                println "Update SPRTELE ${this.sprtele_pidm} ${this.sprtele_seqno} ${this.sprtele_tele_code}"
            }
            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("SPRTELE", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SPRTELE ${this.sprtele_pidm} ${this.sprtele_seqno} ${this.sprtele_tele_code}"
                    println "Problem executing update for table SPRTELE from TelephoneDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update SPRTELE ${this.sprtele_pidm} ${this.sprtele_seqno} ${this.sprtele_tele_code}"
                println "Problem setting up update for table SPRTELE from TelephoneDML.groovy: $e.message"
            }
        }

    }
}
