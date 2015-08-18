/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement Header creation
 */
public class FinanceProcurementHeaderCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def headerData


    public FinanceProcurementHeaderCreateDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceProcurementHeaderCreateDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceProcurementHeader()
    }


    def parseXmlData() {
        headerData = new XmlParser().parseText( xmlData )
    }

    /**
     * Creates finance procurement user
     */
    def createFinanceProcurementHeader() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FPBREQH WHERE FPBREQH_CODE = '" + headerData.FPBREQH_CODE.text() + "' ;" +
                            "   INSERT INTO FPBREQH (FPBREQH_CODE, FPBREQH_REQH_DATE, FPBREQH_TRANS_DATE," +
                            "   FPBREQH_NAME, FPBREQH_VEND_PIDM, FPBREQH_ATYP_CODE, FPBREQH_ATYP_SEQ_NUM, FPBREQH_COAS_CODE, FPBREQH_ORGN_CODE," +
                            "   FPBREQH_REQD_DATE, FPBREQH_COMPLETE_IND, FPBREQH_APPR_IND, FPBREQH_NSF_ON_OFF_IND, FPBREQH_SINGLE_ACCTG_IND, FPBREQH_SHIP_CODE," +
                            "   FPBREQH_RQST_TYPE_IND, FPBREQH_ATTENTION_TO, FPBREQH_VENDOR_CONTACT, FPBREQH_TGRP_CODE, FPBREQH_MATCH_REQUIRED," +
                            "   FPBREQH_USER_ID, FPBREQH_ORIGIN_CODE, FPBREQH_VERSION, FPBREQH_ACTIVITY_DATE) " +
                            "   VALUES (" +
                            "   ?, sysdate, sysdate, " +
                            "   ?,  ?, ?, ?, ?, ?, " +
                            "   sysdate, ?, ?, ?, ?, ?, " +
                            "   ?, ?, ?, ?, ?, " +
                            "   ?, 'GRAILS', 0, sysdate);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, headerData.FPBREQH_CODE.text() ) // Line 1

                insertCall.setString( 2, headerData.FPBREQH_NAME.text() )  // Line 2
                insertCall.setString( 3, headerData.FPBREQH_VEND_PIDM.text() )
                insertCall.setString( 4, headerData.FPBREQH_ATYP_CODE.text() )
                insertCall.setString( 5, headerData.FPBREQH_ATYP_SEQ_NUM.text() )
                insertCall.setString( 6, headerData.FPBREQH_COAS_CODE.text() )
                insertCall.setString( 7, headerData.FPBREQH_ORGN_CODE.text() )

                insertCall.setString( 8, headerData.FPBREQH_COMPLETE_IND.text() ) // Line 3
                insertCall.setString( 9, headerData.FPBREQH_APPR_IND.text() )
                insertCall.setString( 10, headerData.FPBREQH_NSF_ON_OFF_IND.text() )
                insertCall.setString( 11, headerData.FPBREQH_SINGLE_ACCTG_IND.text() )
                insertCall.setString( 12, headerData.FPBREQH_SHIP_CODE.text() )

                insertCall.setString( 13, headerData.FPBREQH_RQST_TYPE_IND.text() ) // Line 4
                insertCall.setString( 14, headerData.FPBREQH_ATTENTION_TO.text() )
                insertCall.setString( 15, headerData.FPBREQH_VENDOR_CONTACT.text() )
                insertCall.setString( 16, headerData.FPBREQH_TGRP_CODE.text() )
                insertCall.setString( 17, headerData.FPBREQH_MATCH_REQUIRED.text() )

                insertCall.setString( 18, headerData.FPBREQH_USER_ID.text() ) // Line 5
                insertCall.execute()

                connectInfo.tableUpdate( "FPBREQH", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FPBREQH", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Header with ..."
                    println "Problem executing insert record for Header: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FPBREQH", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FPBREQH from FinanceProcurementHeaderCreateDML.groovy: $e.message"
            }
        }
    }
}
