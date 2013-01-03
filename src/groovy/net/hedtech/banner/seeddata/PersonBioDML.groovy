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
import java.text.SimpleDateFormat

/**
 * General Person Bio DML
 */


public class PersonBioDML {
    def bannerid
    def spbpers_pidm
    def spbpers_ssn
    def spbpers_birth_date
    def spbpers_lgcy_code
    def spbpers_ethn_code
    def spbpers_mrtl_code
    def spbpers_relg_code
    def spbpers_sex
    def spbpers_confid_ind
    def spbpers_dead_ind
    def spbpers_vetc_file_number
    def spbpers_legal_name
    def spbpers_pref_first_name
    def spbpers_name_prefix
    def spbpers_name_suffix
    def spbpers_vera_ind
    def spbpers_citz_ind
    def spbpers_dead_date
    def spbpers_pin
    def spbpers_citz_code
    def spbpers_hair_code
    def spbpers_eyes_code
    def spbpers_city_birth
    def spbpers_stat_code_birth
    def spbpers_driver_license
    def spbpers_stat_code_driver
    def spbpers_natn_code_driver
    def spbpers_uoms_code_height
    def spbpers_height
    def spbpers_uoms_code_weight
    def spbpers_weight
    def spbpers_sdvet_ind
    def spbpers_license_issued_date
    def spbpers_license_expires_date
    def spbpers_incar_ind
    def spbpers_webid
    def spbpers_web_last_access
    def spbpers_pin_disabled_ind
    def spbpers_itin
    def spbpers_active_duty_sepr_date
    def spbpers_ethn_cde
    def spbpers_confirmed_re_cde
    def spbpers_confirmed_re_date
    def spbpers_armed_serv_med_vet_ind
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public PersonBioDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public PersonBioDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertSpbpers()
    }


    def parseXmlData() {
        def bio = new XmlParser().parseText(xmlData)
        if (bio.BANNERID) {
            this.spbpers_pidm = connectInfo.saveStudentPidm
            this.bannerid = bio.BANNERID.text()
        }
        else {
            this.spbpers_pidm = bio.SPBPERS_PIDM.text()
        }

        this.spbpers_ssn = bio.SPBPERS_SSN.text()
        this.spbpers_birth_date = bio.SPBPERS_BIRTH_DATE.text()
        this.spbpers_lgcy_code = bio.SPBPERS_LGCY_CODE.text()
        this.spbpers_ethn_code = bio.SPBPERS_ETHN_CODE.text()
        this.spbpers_mrtl_code = bio.SPBPERS_MRTL_CODE.text()
        this.spbpers_relg_code = bio.SPBPERS_RELG_CODE.text()
        this.spbpers_sex = bio.SPBPERS_SEX.text()
        this.spbpers_confid_ind = bio.SPBPERS_CONFID_IND.text()
        this.spbpers_dead_ind = bio.SPBPERS_DEAD_IND.text()
        this.spbpers_vetc_file_number = bio.SPBPERS_VETC_FILE_NUMBER.text()
        this.spbpers_legal_name = bio.SPBPERS_LEGAL_NAME.text()
        this.spbpers_pref_first_name = bio.SPBPERS_PREF_FIRST_NAME.text()
        this.spbpers_name_prefix = bio.SPBPERS_NAME_PREFIX.text()
        this.spbpers_name_suffix = bio.SPBPERS_NAME_SUFFIX.text()
        this.spbpers_vera_ind = bio.SPBPERS_VERA_IND.text()
        this.spbpers_citz_ind = bio.SPBPERS_CITZ_IND.text()
        this.spbpers_dead_date = bio.SPBPERS_DEAD_DATE.text()
        this.spbpers_pin = bio.SPBPERS_PIN.text()
        this.spbpers_citz_code = bio.SPBPERS_CITZ_CODE.text()
        this.spbpers_hair_code = bio.SPBPERS_HAIR_CODE.text()
        this.spbpers_eyes_code = bio.SPBPERS_EYES_CODE.text()
        this.spbpers_city_birth = bio.SPBPERS_CITY_BIRTH.text()
        this.spbpers_stat_code_birth = bio.SPBPERS_STAT_CODE_BIRTH.text()
        this.spbpers_driver_license = bio.SPBPERS_DRIVER_LICENSE.text()
        this.spbpers_stat_code_driver = bio.SPBPERS_STAT_CODE_DRIVER.text()
        this.spbpers_natn_code_driver = bio.SPBPERS_NATN_CODE_DRIVER.text()
        this.spbpers_uoms_code_height = bio.SPBPERS_UOMS_CODE_HEIGHT.text()
        this.spbpers_height = bio.SPBPERS_HEIGHT.text()
        this.spbpers_uoms_code_weight = bio.SPBPERS_UOMS_CODE_WEIGHT.text()
        this.spbpers_weight = bio.SPBPERS_WEIGHT.text()
        this.spbpers_sdvet_ind = bio.SPBPERS_SDVET_IND.text()
        this.spbpers_license_issued_date = bio.SPBPERS_LICENSE_ISSUED_DATE.text()
        this.spbpers_license_expires_date = bio.SPBPERS_LICENSE_EXPIRES_DATE.text()
        this.spbpers_incar_ind = bio.SPBPERS_INCAR_IND.text()
        this.spbpers_webid = bio.SPBPERS_WEBID.text()
        this.spbpers_web_last_access = bio.SPBPERS_WEB_LAST_ACCESS.text()
        this.spbpers_pin_disabled_ind = bio.SPBPERS_PIN_DISABLED_IND.text()
        this.spbpers_itin = bio.SPBPERS_ITIN.text()
        this.spbpers_active_duty_sepr_date = bio.SPBPERS_ACTIVE_DUTY_SEPR_DATE.text()
        this.spbpers_ethn_cde = bio.SPBPERS_ETHN_CDE.text()
        this.spbpers_confirmed_re_cde = bio.SPBPERS_CONFIRMED_RE_CDE.text()
        this.spbpers_confirmed_re_date = bio.SPBPERS_CONFIRMED_RE_DATE.text()
        this.spbpers_armed_serv_med_vet_ind = bio.SPBPERS_ARMED_SERV_MED_VET_IND.text()
    }


    def insertSpbpers() {
        tableRow = null
        String rowSQL = "select rowid table_row from SPBPERS \n "
        rowSQL += " where spbpers_pidm = ${this.spbpers_pidm} "
        try {
            conn.eachRow(rowSQL) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SPBPERS rowid PersonBioDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 42
            try {
                String API = "{call  gb_bio.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm      spbpers_pidm NUMBER
                if ((this.spbpers_pidm == "") || (this.spbpers_pidm == null) || (!this.spbpers_pidm)) {
                    insertCall.setNull(1, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(1, this.spbpers_pidm.toInteger())
                }

                // parm 2 p_ssn      spbpers_ssn VARCHAR2
                insertCall.setString(2, this.spbpers_ssn)

                // parm 3 p_birth_date      spbpers_birth_date DATE
                if ((this.spbpers_birth_date == "") || (this.spbpers_birth_date == null)
                        || (!this.spbpers_birth_date)) {
                    insertCall.setNull(3, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_birth_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(3, sqlDate)
                }

                // parm 4 p_lgcy_code      spbpers_lgcy_code VARCHAR2
                insertCall.setString(4, this.spbpers_lgcy_code)

                // parm 5 p_ethn_code      spbpers_ethn_code VARCHAR2
                insertCall.setString(5, this.spbpers_ethn_code)

                // parm 6 p_mrtl_code      spbpers_mrtl_code VARCHAR2
                insertCall.setString(6, this.spbpers_mrtl_code)

                // parm 7 p_relg_code      spbpers_relg_code VARCHAR2
                insertCall.setString(7, this.spbpers_relg_code)

                // parm 8 p_sex      spbpers_sex VARCHAR2
                insertCall.setString(8, this.spbpers_sex)

                // parm 9 p_confid_ind      spbpers_confid_ind VARCHAR2
                insertCall.setString(9, this.spbpers_confid_ind)

                // parm 10 p_dead_ind      spbpers_dead_ind VARCHAR2
                insertCall.setString(10, this.spbpers_dead_ind)

                // parm 11 p_vetc_file_number      spbpers_vetc_file_number VARCHAR2
                insertCall.setString(11, this.spbpers_vetc_file_number)

                // parm 12 p_legal_name      spbpers_legal_name VARCHAR2
                insertCall.setString(12, this.spbpers_legal_name)

                // parm 13 p_pref_first_name      spbpers_pref_first_name VARCHAR2
                insertCall.setString(13, this.spbpers_pref_first_name)

                // parm 14 p_name_prefix      spbpers_name_prefix VARCHAR2
                insertCall.setString(14, this.spbpers_name_prefix)

                // parm 15 p_name_suffix      spbpers_name_suffix VARCHAR2
                insertCall.setString(15, this.spbpers_name_suffix)

                // parm 16 p_vera_ind      spbpers_vera_ind VARCHAR2
                insertCall.setString(16, this.spbpers_vera_ind)

                // parm 17 p_citz_ind      spbpers_citz_ind VARCHAR2
                insertCall.setString(17, this.spbpers_citz_ind)

                // parm 18 p_dead_date      spbpers_dead_date DATE
                if ((this.spbpers_dead_date == "") || (this.spbpers_dead_date == null) ||
                        (!this.spbpers_dead_date)) {
                    insertCall.setNull(18, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_dead_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(18, sqlDate)
                }

                // parm 19 p_citz_code      spbpers_citz_code VARCHAR2
                insertCall.setString(19, this.spbpers_citz_code)

                // parm 20 p_hair_code      spbpers_hair_code VARCHAR2
                insertCall.setString(20, this.spbpers_hair_code)

                // parm 21 p_eyes_code      spbpers_eyes_code VARCHAR2
                insertCall.setString(21, this.spbpers_eyes_code)

                // parm 22 p_city_birth      spbpers_city_birth VARCHAR2
                insertCall.setString(22, this.spbpers_city_birth)

                // parm 23 p_stat_code_birth      spbpers_stat_code_birth VARCHAR2
                insertCall.setString(23, this.spbpers_stat_code_birth)

                // parm 24 p_driver_license      spbpers_driver_license VARCHAR2
                insertCall.setString(24, this.spbpers_driver_license)

                // parm 25 p_stat_code_driver      spbpers_stat_code_driver VARCHAR2
                insertCall.setString(25, this.spbpers_stat_code_driver)

                // parm 26 p_natn_code_driver      spbpers_natn_code_driver VARCHAR2
                insertCall.setString(26, this.spbpers_natn_code_driver)

                // parm 27 p_uoms_code_height      spbpers_uoms_code_height VARCHAR2
                insertCall.setString(27, this.spbpers_uoms_code_height)

                // parm 28 p_height      spbpers_height NUMBER
                if ((this.spbpers_height == "") || (this.spbpers_height == null) ||
                        (!this.spbpers_height)) {
                    insertCall.setNull(28, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(28, this.spbpers_height.toInteger())
                }

                // parm 29 p_uoms_code_weight      spbpers_uoms_code_weight VARCHAR2
                insertCall.setString(29, this.spbpers_uoms_code_weight)

                // parm 30 p_weight      spbpers_weight NUMBER
                if ((this.spbpers_weight == "") || (this.spbpers_weight == null) ||
                        (!this.spbpers_weight)) {
                    insertCall.setNull(30, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(30, this.spbpers_weight.toInteger())
                }

                // parm 31 p_sdvet_ind      spbpers_sdvet_ind VARCHAR2
                insertCall.setString(31, this.spbpers_sdvet_ind)

                // parm 32 p_license_issued_date   sp spbpers_license_issued_dates VARCHAR2

                if ((this.spbpers_license_issued_date == "") || (this.spbpers_license_issued_date == null) || (!this.spbpers_license_issued_date)) { insertCall.setNull(32, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_license_issued_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(32, sqlDate)
                }

                // parm 33 p_license_expires_date  spb spbpers_license_expires_datesp VARCHAR2
                if ((this.spbpers_license_expires_date == "") || (this.spbpers_license_expires_date == null) || (!this.spbpers_license_expires_date)) { insertCall.setNull(33, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_license_expires_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(33, sqlDate)
                }

                // parm 34 p_incar_ind      spbpers_incar_ind VARCHAR2
                insertCall.setString(34, this.spbpers_incar_ind)

                // parm 35 p_itin      spbpers_itin NUMBER
                if ((this.spbpers_itin == "") || (this.spbpers_itin == null) || (!this.spbpers_itin)) {
                    insertCall.setNull(35, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(35, this.spbpers_itin.toInteger())
                }

                // parm 36 p_active_duty_sepr_date spbp spbpers_active_duty_sepr_datespb NUMBER
                if ((this.spbpers_active_duty_sepr_date == "") ||
                        (this.spbpers_active_duty_sepr_date == null) ||
                        (!this.spbpers_active_duty_sepr_date)) {
                    insertCall.setNull(36, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_active_duty_sepr_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(36, sqlDate)
                }

                // parm 37 p_data_origin      spbpers_data_origin VARCHAR2
                insertCall.setString(37, connectInfo.dataOrigin)
                // parm 38 p_user_id      spbpers_user_id VARCHAR2
                insertCall.setString(38, connectInfo.userID)
                // parm 39 p_ethn_cde      spbpers_ethn_cde VARCHAR2
                insertCall.setString(39, this.spbpers_ethn_cde)

                // parm 40 p_confirmed_re_cde      spbpers_confirmed_re_cde VARCHAR2
                insertCall.setString(40, this.spbpers_confirmed_re_cde)

                // parm 41 p_confirmed_re_date      spbpers_confirmed_re_date DATE
                if ((this.spbpers_confirmed_re_date == "")
                        || (this.spbpers_confirmed_re_date == null) ||
                        (!this.spbpers_confirmed_re_date)) {
                    insertCall.setNull(41, java.sql.Types.DATE)
                }
                else {
                    def ddate = new ColumnDateValue(this.spbpers_confirmed_re_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(41, sqlDate)
                }

                // parm 42 p_armed_serv_med_vet_ind  spbpers_armed_serv_med_vet_ind VARCHAR2
                if (this.spbpers_armed_serv_med_vet_ind)
                    insertCall.setString(42, this.spbpers_armed_serv_med_vet_ind)
                else insertCall.setString(42, "N")

                // parm 43 p_rowid      spbpers_rowid  DATE
                insertCall.registerOutParameter(43, java.sql.Types.ROWID)

                if (connectInfo.debugThis) {
                    println "Insert SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SPBPERS", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
                        println "Problem executing insert for table SPBPERS from PersonBioDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
                    println "Problem setting up insert for table SPBPERS from PersonBioDML.groovy: $e.message"
                }
            }

        }
        else {
            updateSpbpers()
        }
    }


    def updateSpbpers() {

        //  parm count is 42
        try {
            String API = "{call  gb_bio.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            // parm 1 p_pidm      spbpers_pidm NUMBER
            if ((this.spbpers_pidm == "") || (this.spbpers_pidm == null) || (!this.spbpers_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(1, this.spbpers_pidm.toInteger())
            }

            // parm 2 p_ssn      spbpers_ssn VARCHAR2
            insertCall.setString(2, this.spbpers_ssn)

            // parm 3 p_birth_date      spbpers_birth_date DATE
            if ((this.spbpers_birth_date == "") || (this.spbpers_birth_date == null) || (!this.spbpers_birth_date)) { insertCall.setNull(3, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_birth_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(3, sqlDate)
            }

            // parm 4 p_lgcy_code      spbpers_lgcy_code VARCHAR2
            insertCall.setString(4, this.spbpers_lgcy_code)

            // parm 5 p_ethn_code      spbpers_ethn_code VARCHAR2
            insertCall.setString(5, this.spbpers_ethn_code)

            // parm 6 p_mrtl_code      spbpers_mrtl_code VARCHAR2
            insertCall.setString(6, this.spbpers_mrtl_code)

            // parm 7 p_relg_code      spbpers_relg_code VARCHAR2
            insertCall.setString(7, this.spbpers_relg_code)

            // parm 8 p_sex      spbpers_sex VARCHAR2
            insertCall.setString(8, this.spbpers_sex)

            // parm 9 p_confid_ind      spbpers_confid_ind VARCHAR2
            insertCall.setString(9, this.spbpers_confid_ind)

            // parm 10 p_dead_ind      spbpers_dead_ind VARCHAR2
            insertCall.setString(10, this.spbpers_dead_ind)

            // parm 11 p_vetc_file_number      spbpers_vetc_file_number VARCHAR2
            insertCall.setString(11, this.spbpers_vetc_file_number)

            // parm 12 p_legal_name      spbpers_legal_name VARCHAR2
            insertCall.setString(12, this.spbpers_legal_name)

            // parm 13 p_pref_first_name      spbpers_pref_first_name VARCHAR2
            insertCall.setString(13, this.spbpers_pref_first_name)

            // parm 14 p_name_prefix      spbpers_name_prefix VARCHAR2
            insertCall.setString(14, this.spbpers_name_prefix)

            // parm 15 p_name_suffix      spbpers_name_suffix VARCHAR2
            insertCall.setString(15, this.spbpers_name_suffix)

            // parm 16 p_vera_ind      spbpers_vera_ind VARCHAR2
            insertCall.setString(16, this.spbpers_vera_ind)

            // parm 17 p_citz_ind      spbpers_citz_ind VARCHAR2
            insertCall.setString(17, this.spbpers_citz_ind)

            // parm 18 p_dead_date      spbpers_dead_date DATE
            if ((this.spbpers_dead_date == "") || (this.spbpers_dead_date == null) || (!this.spbpers_dead_date)) { insertCall.setNull(18, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_dead_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(18, sqlDate)
            }

            // parm 19 p_citz_code      spbpers_citz_code VARCHAR2
            insertCall.setString(19, this.spbpers_citz_code)

            // parm 20 p_hair_code      spbpers_hair_code VARCHAR2
            insertCall.setString(20, this.spbpers_hair_code)

            // parm 21 p_eyes_code      spbpers_eyes_code VARCHAR2
            insertCall.setString(21, this.spbpers_eyes_code)

            // parm 22 p_city_birth      spbpers_city_birth VARCHAR2
            insertCall.setString(22, this.spbpers_city_birth)

            // parm 23 p_stat_code_birth      spbpers_stat_code_birth VARCHAR2
            insertCall.setString(23, this.spbpers_stat_code_birth)

            // parm 24 p_driver_license      spbpers_driver_license VARCHAR2
            insertCall.setString(24, this.spbpers_driver_license)

            // parm 25 p_stat_code_driver     spbpers_stat_code_driver VARCHAR2
            insertCall.setString(25, this.spbpers_stat_code_driver)

            // parm 26 p_natn_code_driver      spbpers_natn_code_driver VARCHAR2
            insertCall.setString(26, this.spbpers_natn_code_driver)

            // parm 27 p_uoms_code_height      spbpers_uoms_code_height VARCHAR2
            insertCall.setString(27, this.spbpers_uoms_code_height)

            // parm 28 p_height      spbpers_height NUMBER
            if ((this.spbpers_height == "") || (this.spbpers_height == null) || (!this.spbpers_height)) { insertCall.setNull(28, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(28, this.spbpers_height.toInteger())
            }

            // parm 29 p_uoms_code_weight      spbpers_uoms_code_weight VARCHAR2
            insertCall.setString(29, this.spbpers_uoms_code_weight)

            // parm 30 p_weight      spbpers_weight NUMBER
            if ((this.spbpers_weight == "") || (this.spbpers_weight == null) || (!this.spbpers_weight)) { insertCall.setNull(30, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(30, this.spbpers_weight.toInteger())
            }

            // parm 31 p_sdvet_ind      spbpers_sdvet_ind VARCHAR2
            insertCall.setString(31, this.spbpers_sdvet_ind)

            // parm 32 p_license_issued_date   sp spbpers_license_issued_dates VARCHAR2

            if ((this.spbpers_license_issued_date == "") || (this.spbpers_license_issued_date == null) || (!this.spbpers_license_issued_date)) { insertCall.setNull(32, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_license_issued_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(32, sqlDate)
            }

            // parm 33 p_license_expires_date  spb spbpers_license_expires_datesp VARCHAR2
            if ((this.spbpers_license_expires_date == "") || (this.spbpers_license_expires_date == null) || (!this.spbpers_license_expires_date)) { insertCall.setNull(33, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_license_expires_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(33, sqlDate)
            }

            // parm 34 p_incar_ind      spbpers_incar_ind VARCHAR2
            insertCall.setString(34, this.spbpers_incar_ind)

            // parm 35 p_itin      spbpers_itin NUMBER
            if ((this.spbpers_itin == "") || (this.spbpers_itin == null) || (!this.spbpers_itin)) { insertCall.setNull(35, java.sql.Types.INTEGER) }
            else {
                insertCall.setInt(35, this.spbpers_itin.toInteger())
            }

            // parm 36 p_active_duty_sepr_date   spbpers_active_duty_sepr_dates  NUMBER
            if ((this.spbpers_active_duty_sepr_date == "") || (this.spbpers_active_duty_sepr_date == null) || (!this.spbpers_active_duty_sepr_date)) { insertCall.setNull(36, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_active_duty_sepr_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(36, sqlDate)
            }

            // parm 37 p_data_origin      spbpers_data_origin VARCHAR2
            insertCall.setString(37, connectInfo.dataOrigin)
            // parm 38 p_user_id      spbpers_user_id VARCHAR2
            insertCall.setString(38, connectInfo.userID)
            // parm 39 p_ethn_cde      spbpers_ethn_cde VARCHAR2
            insertCall.setString(39, this.spbpers_ethn_cde)

            // parm 40 p_confirmed_re_cde      spbpers_confirmed_re_cde VARCHAR2
            insertCall.setString(40, this.spbpers_confirmed_re_cde)

            // parm 41 p_confirmed_re_date      spbpers_confirmed_re_date DATE
            if ((this.spbpers_confirmed_re_date == "") || (this.spbpers_confirmed_re_date == null) || (!this.spbpers_confirmed_re_date)) { insertCall.setNull(41, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.spbpers_confirmed_re_date)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCall.setDate(41, sqlDate)
            }

            // parm 42 p_armed_serv_med_vet_ind  spbpers_armed_serv_med_vet_ind VARCHAR2
            insertCall.setString(42, this.spbpers_armed_serv_med_vet_ind)

            // parm 43 p_rowid_out  spbpers_rowid_out VARCHAR2
            //  insertCall.setRowId(42,tableRow)
            insertCall.setNull(43, java.sql.Types.ROWID)

            if (connectInfo.debugThis) {
                println "Update SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
            }
            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("SPBPERS", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
                    println "Problem executing update for table SPBPERS from PersonBioDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update SPBPERS ${this.spbpers_pidm} ${this.spbpers_ssn} ${this.spbpers_birth_date}"
                println "Problem setting up update for table SPBPERS from PersonBioDML.groovy: $e.message"
            }
        }
    }

}
