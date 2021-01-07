/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * General Person address DML. This class only supports insert of an address, not update.
 */
public class AddressDML {
    def bannerid
    def spraddr_pidm
    def spraddr_atyp_code
    def spraddr_seqno
    def spraddr_from_date
    def spraddr_to_date
    def spraddr_street_line1
    def spraddr_street_line2
    def spraddr_street_line3
    def spraddr_city
    def spraddr_stat_code
    def spraddr_zip
    def spraddr_cnty_code
    def spraddr_natn_code
    def spraddr_phone_area
    def spraddr_phone_number
    def spraddr_phone_ext
    def spraddr_status_ind
    def spraddr_user
    def spraddr_asrc_code
    def spraddr_delivery_point
    def spraddr_correction_digit
    def spraddr_carrier_route
    def spraddr_gst_tax_id
    def spraddr_reviewed_ind
    def spraddr_reviewed_user
    def spraddr_ctry_code_phone
    def spraddr_house_number
    def spraddr_street_line4
    def spraddr_finance_rules
    def newAddrSeq
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null

    def xmlData


    public AddressDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public AddressDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData

        parseXmlData()
        insertSpraddr()
    }


    def parseXmlData() {
        def address = new XmlParser().parseText(xmlData)
        if (address.BANNERID) {
            this.spraddr_pidm = connectInfo.saveStudentPidm
            this.bannerid = address.BANNERID.text()
        }
        else {
            this.spraddr_pidm = address.SPRADDR_PIDM.text()
        }
        this.spraddr_atyp_code = address.SPRADDR_ATYP_CODE.text()
        this.spraddr_seqno = address.SPRADDR_SEQNO.text()
        this.spraddr_from_date = address.SPRADDR_FROM_DATE.text()
        this.spraddr_to_date = address.SPRADDR_TO_DATE.text()
        this.spraddr_street_line1 = address.SPRADDR_STREET_LINE1.text()
        this.spraddr_street_line2 = address.SPRADDR_STREET_LINE2.text()
        this.spraddr_street_line3 = address.SPRADDR_STREET_LINE3.text()
        this.spraddr_city = address.SPRADDR_CITY.text()
        this.spraddr_stat_code = address.SPRADDR_STAT_CODE.text()
        this.spraddr_zip = address.SPRADDR_ZIP.text()
        this.spraddr_cnty_code = address.SPRADDR_CNTY_CODE.text()
        this.spraddr_natn_code = address.SPRADDR_NATN_CODE.text()
        this.spraddr_phone_area = address.SPRADDR_PHONE_AREA.text()
        this.spraddr_phone_number = address.SPRADDR_PHONE_NUMBER.text()
        this.spraddr_phone_ext = address.SPRADDR_PHONE_EXT.text()
        this.spraddr_status_ind = address.SPRADDR_STATUS_IND.text()
        this.spraddr_user = address.SPRADDR_USER.text()
        this.spraddr_asrc_code = address.SPRADDR_ASRC_CODE.text()
        this.spraddr_delivery_point = address.SPRADDR_DELIVERY_POINT.text()
        this.spraddr_correction_digit = address.SPRADDR_CORRECTION_DIGaddress.text()
        this.spraddr_carrier_route = address.SPRADDR_CARRIER_ROUTE.text()
        this.spraddr_gst_tax_id = address.SPRADDR_GST_TAX_ID.text()
        this.spraddr_reviewed_ind = address.SPRADDR_REVIEWED_IND.text()
        this.spraddr_reviewed_user = address.SPRADDR_REVIEWED_USER.text()
        this.spraddr_ctry_code_phone = address.SPRADDR_CTRY_CODE_PHONE.text()
        this.spraddr_house_number = address.SPRADDR_HOUSE_NUMBER.text()
        this.spraddr_street_line4 = address.SPRADDR_STREET_LINE4.text()

    }


    def insertSpraddr() {

        tableRow = null
        String rowSQL = """select rowid table_row from SPRADDR
          where spraddr_pidm = ?
          and spraddr_atyp_code = ? 
          and spraddr_seqno = ?"""
        try {
            conn.eachRow(rowSQL, [this.spraddr_pidm.toInteger(), this.spraddr_atyp_code, this.spraddr_seqno]) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting SPRADDR rowid AddressDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 25
            try {
                String API = "{call  gb_address.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm  spraddr_pidm NUMBER
                if ((this.spraddr_pidm == "") || (this.spraddr_pidm == null) || (!this.spraddr_pidm)) { insertCall.setNull(1, java.sql.Types.INTEGER) }
                else {
                    insertCall.setInt(1, this.spraddr_pidm.toInteger())
                }

                // parm 2 p_atyp_code  spraddr_atyp_code VARCHAR2
                insertCall.setString(2, this.spraddr_atyp_code)

                // parm 3 p_from_date  spraddr_from_date DATE
                if ((this.spraddr_from_date == "") || (this.spraddr_from_date == null) || (!this.spraddr_from_date)) { insertCall.setNull(3, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.spraddr_from_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(3, sqlDate)
                }

                // parm 4 p_to_date  spraddr_to_date DATE
                if ((this.spraddr_to_date == "") || (this.spraddr_to_date == null) || (!this.spraddr_to_date)) { insertCall.setNull(4, java.sql.Types.DATE) }
                else {
                    def ddate = new ColumnDateValue(this.spraddr_to_date)
                    String unfDate = ddate.formatJavaDate()
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(4, sqlDate)
                }

                // parm 5 p_street_line1  spraddr_street_line1 VARCHAR2
                insertCall.setString(5, this.spraddr_street_line1)

                // parm 6 p_street_line2  spraddr_street_line2 VARCHAR2
                insertCall.setString(6, this.spraddr_street_line2)

                // parm 7 p_street_line3  spraddr_street_line3 VARCHAR2
                insertCall.setString(7, this.spraddr_street_line3)

                // parm 8 p_city  spraddr_city VARCHAR2
                insertCall.setString(8, this.spraddr_city)

                // parm 9 p_stat_code  spraddr_stat_code VARCHAR2
                insertCall.setString(9, this.spraddr_stat_code)

                // parm 10 p_zip  spraddr_zip VARCHAR2
                insertCall.setString(10, this.spraddr_zip)

                // parm 11 p_cnty_code  spraddr_cnty_code VARCHAR2
                insertCall.setString(11, this.spraddr_cnty_code)

                // parm 12 p_natn_code  spraddr_natn_code VARCHAR2
                insertCall.setString(12, this.spraddr_natn_code)

                // parm 13 p_status_ind  spraddr_status_ind VARCHAR2
                insertCall.setString(13, this.spraddr_status_ind)

                // parm 14 p_user  spraddr_user VARCHAR2
                insertCall.setString(14, this.spraddr_user)

                // parm 15 p_asrc_code  spraddr_asrc_code VARCHAR2
                insertCall.setString(15, this.spraddr_asrc_code)

                // parm 16 p_delivery_point  spraddr_delivery_point NUMBER
                if ((this.spraddr_delivery_point == "") || (this.spraddr_delivery_point == null) || (!this.spraddr_delivery_point)) {
                    insertCall.setNull(16, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(16, this.spraddr_delivery_point.toInteger())
                }

                // parm 17 p_correction_digit  spraddr_correction_digit NUMBER
                if ((this.spraddr_correction_digit == "") || (this.spraddr_correction_digit == null) || (!this.spraddr_correction_digit)) {
                    insertCall.setNull(17, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(17, this.spraddr_correction_digit.toInteger())
                }

                // parm 18 p_carrier_route  spraddr_carrier_route VARCHAR2
                insertCall.setString(18, this.spraddr_carrier_route)

                // parm 19 p_gst_tax_id  spraddr_gst_tax_id VARCHAR2
                insertCall.setString(19, this.spraddr_gst_tax_id)

                // parm 20 p_reviewed_ind  spraddr_reviewed_ind VARCHAR2
                insertCall.setString(20, this.spraddr_reviewed_ind)

                // parm 21 p_reviewed_user  spraddr_reviewed_user VARCHAR2
                insertCall.setString(21, this.spraddr_reviewed_user)

                // parm 22 p_data_origin  spraddr_data_origin VARCHAR2
                insertCall.setString(22, connectInfo.dataOrigin)
                // parm 23 p_ctry_code_phone  spraddr_ctry_code_phone VARCHAR2
                insertCall.setString(23, this.spraddr_ctry_code_phone)

                // parm 24 p_house_number  spraddr_house_number VARCHAR2
                insertCall.setString(24, this.spraddr_house_number)

                // parm 25 p_street_line4  spraddr_street_line4 VARCHAR2
                insertCall.setString(25, this.spraddr_street_line4)

                // parm 26 p_finance_rules  spraddr_finance_rules VARCHAR2
                insertCall.setString(26, this.spraddr_finance_rules)

                // parm 27 p_seqno_inout  spraddr_seqno_inout VARCHAR2
                insertCall.registerOutParameter(27, java.sql.Types.INTEGER)
                if (this.spraddr_seqno) {insertCall.setInt(27, this.spraddr_seqno.toInteger()) }
                else {insertCall.setNull(27, java.sql.Types.INTEGER) }
                this.newAddrSeq = this.spraddr_seqno

                // parm 28 p_rowid_out  spraddr_rowid_out VARCHAR2
                insertCall.registerOutParameter(28, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert SPRADDR ${this.spraddr_pidm} ${this.spraddr_atyp_code} ${this.spraddr_from_date}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SPRADDR", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert SPRADDR ${this.spraddr_pidm} ${this.spraddr_atyp_code} ${this.spraddr_from_date}"
                        println "Problem executing insert for table SPRADDR from AddressDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert SPRADDR ${this.spraddr_pidm} ${this.spraddr_atyp_code} ${this.spraddr_from_date}"
                    println "Problem setting up insert for table SPRADDR from AddressDML.groovy: $e.message"
                }
            }
        }
    }
}
