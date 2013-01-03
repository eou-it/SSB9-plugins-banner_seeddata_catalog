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

/**
 *  DML for SHRINCG
 */
public class IncompleteGradesDML {
    def shrincg_primary_key
    def shrincg_term_code_eff
    def shrincg_incmp_grading_ind
    def shrincg_incmp_grde_over_ind
    def shrincg_incmp_date_over_type
    def shrincg_disp_web_ind
    def shrincg_system_req_ind
    def shrincg_levl_code
    def shrincg_surrogate_id
    def shrincg_version
    def shrincg_vpdi_code
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null

    public IncompleteGradesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processShrincg()
    }

    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)

        if ( connectInfo.debugThis ) {
            println "--------- New XML SHRINCG record ----------"
            println "${apiData.SHRINCG_PRIMARY_KEY.text()}${apiData.SHRINCG_TERM_CODE_EFF.text()} ${apiData.SHRINCG_INCMP_GRADING_IND.text()} ${apiData.SHRINCG_INCMP_GRDE_OVER_IND.text()}"
        }
        this.shrincg_primary_key = apiData.SHRINCG_PRIMARY_KEY.text()
        this.shrincg_term_code_eff = apiData.SHRINCG_TERM_CODE_EFF.text()
        this.shrincg_incmp_grading_ind = apiData.SHRINCG_INCMP_GRADING_IND.text()
        this.shrincg_incmp_grde_over_ind = apiData.SHRINCG_INCMP_GRDE_OVER_IND.text()
        this.shrincg_incmp_date_over_type = apiData.SHRINCG_INCMP_DATE_OVER_TYPE.text()
        this.shrincg_disp_web_ind = apiData.SHRINCG_DISP_WEB_IND.text()
        this.shrincg_system_req_ind = apiData.SHRINCG_SYSTEM_REQ_IND.text()
        this.shrincg_levl_code = apiData.SHRINCG_LEVL_CODE.text()
        this.shrincg_surrogate_id = apiData.SHRINCG_SURROGATE_ID.text()
        this.shrincg_version = apiData.SHRINCG_VERSION.text()
        this.shrincg_vpdi_code = apiData.SHRINCG_VPDI_CODE.text()
    }

    def processShrincg() {
        tableRow = null
        String rowSQL = """select rowid table_row
                             from SHRINCG
                            where shrincg_term_code_eff = ?
                              and NVL(shrincg_levl_code,'zz') = NVL('${this.shrincg_levl_code}','zz') """
        try {
            conn.eachRow(rowSQL, [this.shrincg_term_code_eff]) { row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if ( connectInfo.showErrors ) {
                println "${rowSQL}"
                println "Problem selecting SHRINCG rowid IncompleteGradesDML.groovy: $e.message "
            }
        }
        if ( !tableRow ) {
            //  parm count is 10
            try {
                String API = "{call sb_incmp_grading.p_create(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_term_code_eff  shrincg_term_code_eff VARCHAR2
                insertCall.setString(1, this.shrincg_term_code_eff)

                // parm 2 p_incmp_grading_ind  shrincg_incmp_grading_ind VARCHAR2
                insertCall.setString(2, this.shrincg_incmp_grading_ind)

                // parm 3 p_incmp_grde_over_ind  shrincg_incmp_grde_over_ind VARCHAR2
                insertCall.setString(3, this.shrincg_incmp_grde_over_ind)

                // parm 4 p_incmp_date_over_type  shrincg_incmp_date_over_type VARCHAR2
                insertCall.setString(4, this.shrincg_incmp_date_over_type)

                // parm 5 p_disp_web_ind  shrincg_disp_web_ind VARCHAR2
                insertCall.setString(5, this.shrincg_disp_web_ind)

                // parm 6 p_system_req_ind  shrincg_system_req_ind VARCHAR2
                insertCall.setString(6, this.shrincg_system_req_ind)

                // parm 7 p_user_id  shrincg_user_id VARCHAR2
                insertCall.setString(7, "GRAILS")
                // parm 8 p_levl_code  shrincg_levl_code VARCHAR2
                insertCall.setString(8, this.shrincg_levl_code)

                // parm 9 p_data_origin  shrincg_data_origin VARCHAR2
                insertCall.setString(9, "GRAILS")
                // parm 10 p_rowid_out  shrincg_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)
                if ( connectInfo.debugThis ) {
                    println "Insert SHRINCG ${this.shrincg_term_code_eff} ${this.shrincg_incmp_grading_ind} ${this.shrincg_incmp_grde_over_ind}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SHRINCG", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SHRINCG", 0, 0, 0, 1, 0)
                    if (
                        connectInfo.showErrors ) {
                        println "Insert SHRINCG ${this.shrincg_term_code_eff} ${this.shrincg_incmp_grading_ind} ${this.shrincg_incmp_grde_over_ind}"
                        println "Problem executing insert for table SHRINCG from IncompleteGradesDML.groovy: $e.message "
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SHRINCG", 0, 0, 0, 1, 0)

                if ( connectInfo.showErrors ) {
                    println "Insert SHRINCG ${this.shrincg_term_code_eff} ${this.shrincg_incmp_grading_ind} ${this.shrincg_incmp_grde_over_ind}"
                    println "Problem setting up insert for table SHRINCG from IncompleteGradesDML.groovy: $e.message "
                }
            }
        }
    }
}                                                                               
