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
import java.sql.CallableStatement
import java.text.*
/**
 *  DML for SRBRECR
 */
public class RecruitDML {
    def bannerid
    def srbrecr_pidm
    def srbrecr_term_code
    def srbrecr_admin_seqno
    def srbrecr_recr_code
    def srbrecr_rsta_code
    def srbrecr_select_ind
    def srbrecr_add_date
    def srbrecr_admt_code
    def srbrecr_camp_code
    def srbrecr_edlv_code
    def srbrecr_egol_code
    def srbrecr_full_part_ind
    def srbrecr_sbgi_code
    def srbrecr_wrsn_code
    def srbrecr_rtyp_code
    def srbrecr_resd_code
    def srbrecr_sess_code
    def srbrecr_site_code
    def srbrecr_styp_code
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public RecruitDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processSrbrecr()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.srbrecr_pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()
        if (connectInfo.debugThis) {
            println "--------- New XML SRBRECR record ----------"
            println "${bannerid}    ${apiData.SRBRECR_TERM_CODE.text()}    ${apiData.SRBRECR_ADMIN_SEQNO.text()}    ${apiData.SRBRECR_DEPT_CODE.text()}    "
        }

        this.srbrecr_term_code = apiData.SRBRECR_TERM_CODE.text()
        this.srbrecr_admin_seqno = apiData.SRBRECR_ADMIN_SEQNO.text()
        this.srbrecr_recr_code = apiData.SRBRECR_RECR_CODE.text()
        this.srbrecr_rsta_code = apiData.SRBRECR_RSTA_CODE.text()
        this.srbrecr_select_ind = apiData.SRBRECR_SELECT_IND.text()
        this.srbrecr_add_date = apiData.SRBRECR_ADD_DATE.text()
        this.srbrecr_admt_code = apiData.SRBRECR_ADMT_CODE.text()
        this.srbrecr_camp_code = apiData.SRBRECR_CAMP_CODE.text()
        this.srbrecr_edlv_code = apiData.SRBRECR_EDLV_CODE.text()
        this.srbrecr_egol_code = apiData.SRBRECR_EGOL_CODE.text()
        this.srbrecr_full_part_ind = apiData.SRBRECR_FULL_PART_IND.text()
        this.srbrecr_sbgi_code = apiData.SRBRECR_SBGI_CODE.text()
        this.srbrecr_wrsn_code = apiData.SRBRECR_WRSN_CODE.text()
        this.srbrecr_rtyp_code = apiData.SRBRECR_RTYP_CODE.text()
        this.srbrecr_resd_code = apiData.SRBRECR_RESD_CODE.text()
        this.srbrecr_sess_code = apiData.SRBRECR_SESS_CODE.text()
        this.srbrecr_site_code = apiData.SRBRECR_SITE_CODE.text()
        this.srbrecr_styp_code = apiData.SRBRECR_STYP_CODE.text()

    }


    def processSrbrecr() {
        tableRow = null

        String rowSQL = """select rowid table_row from SRBRECR
           where srbrecr_pidm = ?  and srbrecr_term_code = ? and srbrecr_admin_seqno = ? """
        try {
            conn.eachRow(rowSQL,[this.srbrecr_pidm, this.srbrecr_term_code, this.srbrecr_admin_seqno]) { row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SRBRECR rowid RecruitDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 21
            try {
                String API = "{call  sb_recruit.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  srbrecr_pidm NUMBER
                if ((this.srbrecr_pidm == "") || (this.srbrecr_pidm == null) ||
                        (!this.srbrecr_pidm)) {
                    insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.srbrecr_pidm.toInteger())
                }

                // parm 2 p_term_code  srbrecr_term_code VARCHAR2
                insertCall.setString(2, this.srbrecr_term_code)

                // parm 3 p_admin_seqno_inout  srbrecr_admin_seqno_inout VARCHAR2
               
                if( this.srbrecr_admin_seqno)
                    insertCall.registerOutParameter(3, this.srbrecr_admin_seqno.toInteger())
                else {
                    insertCall.registerOutParameter(3, java.sql.Types.INTEGER)
                }
               
                // parm 4 p_recr_code  srbrecr_recr_code VARCHAR2
                insertCall.setString(4, this.srbrecr_recr_code)

                // parm 5 p_rsta_code  srbrecr_rsta_code VARCHAR2
                insertCall.setString(5, this.srbrecr_rsta_code)

                // parm 6 p_select_ind  srbrecr_select_ind VARCHAR2
                insertCall.setString(6, this.srbrecr_select_ind)

                // parm 7 p_add_date  srbrecr_add_date DATE
                if ((this.srbrecr_add_date == "") || (this.srbrecr_add_date == null) ||
                        (!this.srbrecr_add_date)) {
                    insertCall.setNull(7, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.srbrecr_add_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(7, sqlDate)
                }

                // parm 8 p_admt_code  srbrecr_admt_code VARCHAR2
                insertCall.setString(8, this.srbrecr_admt_code)

                // parm 9 p_edlv_code  srbrecr_edlv_code VARCHAR2
                insertCall.setString(9, this.srbrecr_edlv_code)

                // parm 10 p_egol_code  srbrecr_egol_code VARCHAR2
                insertCall.setString(10, this.srbrecr_egol_code)

                // parm 11 p_full_part_ind  srbrecr_full_part_ind VARCHAR2
                insertCall.setString(11, this.srbrecr_full_part_ind)

                // parm 12 p_sbgi_code  srbrecr_sbgi_code VARCHAR2
                insertCall.setString(12, this.srbrecr_sbgi_code)

                // parm 13 p_wrsn_code  srbrecr_wrsn_code VARCHAR2
                insertCall.setString(13, this.srbrecr_wrsn_code)

                // parm 14 p_rtyp_code  srbrecr_rtyp_code VARCHAR2
                insertCall.setString(14, this.srbrecr_rtyp_code)

                // parm 15 p_resd_code  srbrecr_resd_code VARCHAR2
                insertCall.setString(15, this.srbrecr_resd_code)

                // parm 16 p_sess_code  srbrecr_sess_code VARCHAR2
                insertCall.setString(16, this.srbrecr_sess_code)

                // parm 17 p_site_code  srbrecr_site_code VARCHAR2
                insertCall.setString(17, this.srbrecr_site_code)

                // parm 18 p_styp_code  srbrecr_styp_code VARCHAR2
                insertCall.setString(18, this.srbrecr_styp_code)

                // parm 19 p_data_origin  srbrecr_data_origin VARCHAR2
                insertCall.setString(19, connectInfo.dataOrigin)
                // parm 20 p_user_id  srbrecr_user_id VARCHAR2
                insertCall.setString(20, connectInfo.userID)
                // parm 21 p_rowid_out  srbrecr_rowid_out VARCHAR2
                insertCall.registerOutParameter(21, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SRBRECR ${this.srbrecr_pidm} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SRBRECR", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SRBRECR ${bannerid} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                        println "Problem executing insert for table SRBRECR from RecruitDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SRBRECR ${bannerid} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                    println "Problem setting up insert for table SRBRECR from RecruitDML.groovy: $e.message"
                }
            }
        } else {  // update the data
            //  parm count is 21
            try {
                String API = "{call  sb_recruit.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  srbrecr_pidm NUMBER
                if ((this.srbrecr_pidm == "") || (this.srbrecr_pidm == null) || (!this.srbrecr_pidm))
                { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.srbrecr_pidm.toInteger())
                }

                // parm 2 p_term_code  srbrecr_term_code VARCHAR2
                insertCall.setString(2, this.srbrecr_term_code)

                // parm 3 p_admin_seqno  srbrecr_admin_seqno NUMBER

                insertCall.registerOutParameter(3, java.sql.Types.INTEGER)
                if (this.srbrecr_admin_seqno) {
                    insertCall.setInt(3, this.srbrecr_admin_seqno.toInteger()) }
                else {insertCall.setNull(3, java.sql.Types.INTEGER) }

                // parm 4 p_recr_code  srbrecr_recr_code VARCHAR2
                insertCall.setString(4, this.srbrecr_recr_code)

                // parm 5 p_rsta_code  srbrecr_rsta_code VARCHAR2
                insertCall.setString(5, this.srbrecr_rsta_code)

                // parm 6 p_select_ind  srbrecr_select_ind VARCHAR2
                insertCall.setString(6, this.srbrecr_select_ind)

                // parm 7 p_add_date  srbrecr_add_date DATE
                if ((this.srbrecr_add_date == "") || (this.srbrecr_add_date == null)
                        || (!this.srbrecr_add_date)) {
                    insertCall.setNull(7, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.srbrecr_add_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(7, sqlDate)
                }

                // parm 8 p_admt_code  srbrecr_admt_code VARCHAR2
                insertCall.setString(8, this.srbrecr_admt_code)

                // parm 9 p_edlv_code  srbrecr_edlv_code VARCHAR2
                insertCall.setString(9, this.srbrecr_edlv_code)

                // parm 10 p_egol_code  srbrecr_egol_code VARCHAR2
                insertCall.setString(10, this.srbrecr_egol_code)

                // parm 11 p_full_part_ind  srbrecr_full_part_ind VARCHAR2
                insertCall.setString(11, this.srbrecr_full_part_ind)

                // parm 12 p_sbgi_code  srbrecr_sbgi_code VARCHAR2
                insertCall.setString(12, this.srbrecr_sbgi_code)

                // parm 13 p_wrsn_code  srbrecr_wrsn_code VARCHAR2
                insertCall.setString(13, this.srbrecr_wrsn_code)

                // parm 14 p_rtyp_code  srbrecr_rtyp_code VARCHAR2
                insertCall.setString(14, this.srbrecr_rtyp_code)

                // parm 15 p_resd_code  srbrecr_resd_code VARCHAR2
                insertCall.setString(15, this.srbrecr_resd_code)

                // parm 16 p_sess_code  srbrecr_sess_code VARCHAR2
                insertCall.setString(16, this.srbrecr_sess_code)

                // parm 17 p_site_code  srbrecr_site_code VARCHAR2
                insertCall.setString(17, this.srbrecr_site_code)

                // parm 18 p_styp_code  srbrecr_styp_code VARCHAR2
                insertCall.setString(18, this.srbrecr_styp_code)

                // parm 19 p_data_origin  srbrecr_data_origin VARCHAR2
                insertCall.setString(19, connectInfo.dataOrigin)
                // parm 20 p_user_id  srbrecr_user_id VARCHAR2
                insertCall.setString(20, connectInfo.userID)
                // parm 21 p_rowid  srbrecr_rowid VARCHAR2
                insertCall.setRowId(21, tableRow)
                if (connectInfo.debugThis) {
                    println "Update SRBRECR ${this.srbrecr_pidm} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SRBRECR", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update SRBRECR ${bannerid} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                        println "Problem executing update for table SRBRECR from RecruitDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SRBRECR ${bannerid} ${this.srbrecr_term_code} ${this.srbrecr_admin_seqno}"
                    println "Problem setting up update for table SRBRECR from RecruitDML.groovy: $e.message"
                }
            }
        }
    }
}
