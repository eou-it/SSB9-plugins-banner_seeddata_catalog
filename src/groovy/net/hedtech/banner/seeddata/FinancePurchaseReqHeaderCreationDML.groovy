/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Purchase Requisition header
 */
public class FinancePurchaseReqHeaderCreationDML {


    def oracle_id
    def spriden_id
    def source_oracle_id


    def fpbreqh_code
    //def fpbreqh_reqh_date = new java.sql.Date()
    //def fpbreqh_trans_date = new java.sql.Date()
    def fpbreqh_name
    def fpbreqh_phone_area
    def fpbreqh_phone_num
    def fpbreqh_vend_pidm
    def fpbreqh_atyp_code
    def fpbreqh_atyp_seq_num
    def fpbreqh_coas_code
    def fpbreqh_orgn_code
    //def fpbreqh_reqd_date = new java.sql.Date() + 1
    def fpbreqh_complete_ind
    def fpbreqh_susp_ind
    def fpbreqh_appr_ind
    def fpbreqh_text_ind
    def fpbreqh_edit_defer_ind
    def fpbreqh_nsf_on_off_ind
    def fpbreqh_single_acctg_ind
    def fpbreqh_ship_code
    def fpbreqh_rqst_type_ind
    def fpbreqh_email_addr
    def fpbreqh_fax_area
    def fpbreqh_fax_number
    def fpbreqh_attention_to
    def fpbreqh_vendor_contact
    def fpbreqh_tgrp_code
    def fpbreqh_match_required
    def fpbreqh_user_id
    def fpbreqh_origin_code
    def fpbreqh_version

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinancePurchaseReqHeaderCreationDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinancePurchaseReqHeaderCreationDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        createFinanceProcurementUser()
    }


    def parseXmlData() {
        def header = new XmlParser().parseText( xmlData )
        this.fpbreqh_code = header.FPBREQH_CODE.text()
        //this.fpbreqh_reqh_date = new Date()
        //this.fpbreqh_trans_date = new Date()
        this.fpbreqh_name = header.FPBREQH_NAME.text()
        this.fpbreqh_phone_area = header.FPBREQH_PHONE_AREA.text()
        this.fpbreqh_phone_num = header.FPBREQH_PHONE_NUM.text()
        this.fpbreqh_vend_pidm = header.FPBREQH_VEND_PIDM.text()
        this.fpbreqh_atyp_code = header.FPBREQH_ATYP_CODE.text()
        this.fpbreqh_atyp_seq_num = header.FPBREQH_ATYP_SEQ_NUM.text()
        this.fpbreqh_coas_code = header.FPBREQH_COAS_CODE.text()
        this.fpbreqh_orgn_code = header.FPBREQH_ORGN_CODE.text()
        // this.fpbreqh_reqd_date = new Date() + 1
        this.fpbreqh_complete_ind = header.FPBREQH_COMPLETE_IND.text()
        this.fpbreqh_susp_ind = header.FPBREQH_SUSP_IND.text()
        this.fpbreqh_appr_ind = header.FPBREQH_APPR_IND.text()
        this.fpbreqh_text_ind = header.FPBREQH_TEXT_IND.text()
        this.fpbreqh_edit_defer_ind = header.FPBREQH_EDIT_DEFER_IND.text()
        this.fpbreqh_nsf_on_off_ind = header.FPBREQH_NSF_ON_OFF_IND.text()
        this.fpbreqh_single_acctg_ind = header.FPBREQH_SINGLE_ACCTG_IND.text()
        this.fpbreqh_ship_code = header.FPBREQH_SHIP_CODE.text()
        this.fpbreqh_rqst_type_ind = header.FPBREQH_RQST_TYPE_IND.text()
        this.fpbreqh_email_addr = header.FPBREQH_EMAIL_ADDR.text()
        this.fpbreqh_fax_area = header.FPBREQH_FAX_AREA.text()
        this.fpbreqh_fax_number = header.FPBREQH_FAX_NUMBER.text()
        this.fpbreqh_attention_to = header.FPBREQH_ATTENTION_TO.text()
        this.fpbreqh_vendor_contact = header.FPBREQH_VENDOR_CONTACT.text()
        this.fpbreqh_tgrp_code = header.FPBREQH_TGRP_CODE.text()
        this.fpbreqh_match_required = header.FPBREQH_MATCH_REQUIRED.text()
        this.fpbreqh_user_id = header.FPBREQH_USER_ID.text()
        this.fpbreqh_origin_code = header.FPBREQH_ORIGIN_CODE.text()
        this.fpbreqh_version = header.FPBREQH_VERSION.text()
    }

    /**
     * Creates finance procurement user
     */
    def createFinanceProcurementUser() {
        try {
            final String apiQuery = 'INSERT INTO LOG_TEMP_SHIV VALUES (sysdate,?);'
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1,'TEST SEED'  )
               /* insertCall.setString( 2, 'Accounting Clerk II' )
                insertCall.setString( 3, '412' )
                insertCall.setString( 4, '291-0123' )
                insertCall.setString( 5, '2510' )
                insertCall.setString( 6, 'BU' )
                insertCall.setString( 7, '1' )
                insertCall.setString( 8, 'B' )
                insertCall.setString( 9, '11003' )
                insertCall.setString( 10, 'N' )
                insertCall.setString( 11, 'N' )
                insertCall.setString( 12, 'N' )
                insertCall.setString( 13, 'N' )
                insertCall.setString( 14, 'N' )
                insertCall.setString( 15, 'N' )
                insertCall.setString( 16, 'Y' )
                insertCall.setString( 17, 'ADMISS' )
                insertCall.setString( 18, 'P' )
                insertCall.setString( 19, 'FIMSPRD@testplan.org' )
                insertCall.setString( 20, '412' )
                insertCall.setString( 21, '291-7789' )
                insertCall.setString( 22, 'R. David Davies IV' )
                insertCall.setString( 23, 'Linda Jones' )
                insertCall.setString( 24, 'AD' )
                insertCall.setString( 25, 'U' )
                insertCall.setString( 26, 'GRAILS' )
                insertCall.setString( 27, 'GRAILS' )
                insertCall.setString( 28, '0' )*/
                insertCall.executeUpdate()
                connectInfo.tableUpdate( "FPBREQH", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FPBREQH", 0, 0, 0, 1, 0 )
                e.printStackTrace()
                if (connectInfo.showErrors) {
                    println "Problem executing Update for table FPBREQH from FinancePurchaseReqHeaderCreationDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FPBREQH", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FPBREQH from FinancePurchaseReqHeaderCreationDML.groovy: $e.message"
            }
        }
    }
}
