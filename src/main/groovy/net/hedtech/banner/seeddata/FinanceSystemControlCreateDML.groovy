/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Journal Header creation
 */
public class FinanceSystemControlCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def systemData


    public FinanceSystemControlCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceSystemControlCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceSystemControlRecords();
    }


    def parseXmlData() {
        systemData = new XmlParser().parseText( xmlData )
    }

    def createFinanceSystemControlRecords() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            " DELETE FROM FOBSYSC where FOBSYSC_USER_ID='GRAILS' AND FOBSYSC_EFF_DATE='"+systemData.FOBSYSC_EFF_DATE.text() +"' AND FOBSYSC_NCHG_DATE='"+ systemData.FOBSYSC_NCHG_DATE.text()+"';"  +
                            "   INSERT INTO FOBSYSC ( "+
                                "FOBSYSC_EFF_DATE               ," +
                                "FOBSYSC_ACTIVITY_DATE          ," +
                                "FOBSYSC_USER_ID                ," +
                                "FOBSYSC_STATUS_IND             ," +
                                "FOBSYSC_DEFAULT_TIMING_IND     ," +
                                "FOBSYSC_RUCL_SECURITY_IND      ," +
                                "FOBSYSC_FUND_ORG_SECURITY_IND  ," +
                                "FOBSYSC_TAX_PROCESSING_IND     ," +
                                "FOBSYSC_CONSOL_POST_IND        ," +
                                "FOBSYSC_JV_NSF_ON_OFF_IND      ," +
                                "FOBSYSC_PO_NSF_ON_OFF_IND      ," +
                                "FOBSYSC_ENC_NSF_ON_OFF_IND     ," +
                                "FOBSYSC_REQ_NSF_ON_OFF_IND     ," +
                                "FOBSYSC_INV_NSF_ON_OFF_IND     ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_REQ  ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_PO   ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_INV  ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_CO   ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_ENC  ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_JV   ," +
                                "FOBSYSC_ACCRUED_ON_OFF_IND     ," +
                                "FOBSYSC_NCHG_DATE              ," +
                                "FOBSYSC_ACCT_CODE_COAS_DUE_TO  ," +
                                "FOBSYSC_ACCT_CODE_COAS_DUE_FRM ," +
                                "FOBSYSC_AUTO_BUYR_IND          ," +
                                "FOBSYSC_EDIT_DEFER_IND         ," +
                                "FOBSYSC_TGRP_CODE_DEFAULT      ," +
                                "FOBSYSC_TAX_OVERRIDE_AMT       ," +
                                "FOBSYSC_TAX_OVERRIDE_PCT       ," +
                                "FOBSYSC_FEDERAL_EMPLOYER_ID    ," +
                                "FOBSYSC_MULTIPLE_FB_ACCT_IND   ," +
                                "FOBSYSC_ISS_NSF_ON_OFF_IND     ," +
                                "FOBSYSC_FA_NSF_ON_OFF_IND      ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_FA   ," +
                                "FOBSYSC_MIN_ASSET_CAP_AMT      ," +
                                "FOBSYSC_DCR_NSF_ON_OFF_IND     ," +
                                "FOBSYSC_APPR_OVERRIDE_IND_DCR  ," +
                                "FOBSYSC_DEFER_GRANT_IND        ," +
                                "FOBSYSC_IC_CS_ORDER_IND        ," +
                                "FOBSYSC_JV_DTAG_FEED_IND       ," +
                                "FOBSYSC_ISSU_DTAG_FEED_IND     ," +
                                "FOBSYSC_DCSR_DTAG_FEED_IND     ," +
                                "FOBSYSC_DOC_LVL_MATCH_IND      ," +
                                "FOBSYSC_WBUD_IND               ," +
                                "FOBSYSC_WBUD_TRACK_IND         ," +
                                "FOBSYSC_DFR_GRNT_HIST_IND      )" +
                                "   VALUES (" +
                                "   ?, sysdate, 'GRAILS', ?, ?, ?, ?, ?, ?, ?,  " +
                                "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                                "   ?, ?, ?, ?, ?, ? );" +
                            "   commit;" +
                    "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, systemData.FOBSYSC_EFF_DATE.text() )
                insertCall.setString( 2, systemData.FOBSYSC_STATUS_IND.text() )
                insertCall.setString( 3, systemData.FOBSYSC_DEFAULT_TIMING_IND.text() )
                insertCall.setString( 4, systemData.FOBSYSC_RUCL_SECURITY_IND.text() )
                insertCall.setString( 5, systemData.FOBSYSC_FUND_ORG_SECURITY_IND.text() )
                insertCall.setString( 6, systemData.FOBSYSC_TAX_PROCESSING_IND.text() )
                insertCall.setString( 7, systemData.FOBSYSC_CONSOL_POST_IND.text() )
                insertCall.setString( 8, systemData.FOBSYSC_JV_NSF_ON_OFF_IND.text() )
                insertCall.setString( 9, systemData.FOBSYSC_PO_NSF_ON_OFF_IND.text() )
                insertCall.setString( 10, systemData.FOBSYSC_ENC_NSF_ON_OFF_IND.text() )
                insertCall.setString( 11, systemData.FOBSYSC_REQ_NSF_ON_OFF_IND.text() )
                insertCall.setString( 12, systemData.FOBSYSC_INV_NSF_ON_OFF_IND.text() )
                insertCall.setString( 13, systemData.FOBSYSC_APPR_OVERRIDE_IND_REQ.text() )
                insertCall.setString( 14, systemData.FOBSYSC_APPR_OVERRIDE_IND_PO.text() )
                insertCall.setString( 15, systemData.FOBSYSC_APPR_OVERRIDE_IND_INV.text() )
                insertCall.setString( 16, systemData.FOBSYSC_APPR_OVERRIDE_IND_CO.text() )
                insertCall.setString( 17, systemData.FOBSYSC_APPR_OVERRIDE_IND_ENC.text() )
                insertCall.setString( 18, systemData.FOBSYSC_APPR_OVERRIDE_IND_JV.text() )
                insertCall.setString( 19, systemData.FOBSYSC_ACCRUED_ON_OFF_IND.text() )
                insertCall.setString( 20, systemData.FOBSYSC_NCHG_DATE.text() )
                insertCall.setString( 21, systemData.FOBSYSC_ACCT_CODE_COAS_DUE_TO.text() )
                insertCall.setString( 22, systemData.FOBSYSC_ACCT_CODE_COAS_DUE_FRM.text() )
                insertCall.setString( 23, systemData.FOBSYSC_AUTO_BUYR_IND.text() )
                insertCall.setString( 24, systemData.FOBSYSC_EDIT_DEFER_IND.text() )
                insertCall.setString( 25, systemData.FOBSYSC_TGRP_CODE_DEFAULT.text() )
                insertCall.setString( 26, systemData.FOBSYSC_TAX_OVERRIDE_AMT.text() )
                insertCall.setString( 27, systemData.FOBSYSC_TAX_OVERRIDE_PCT.text() )
                insertCall.setString( 28, systemData.FOBSYSC_FEDERAL_EMPLOYER_ID.text() )
                insertCall.setString( 29, systemData.FOBSYSC_MULTIPLE_FB_ACCT_IND.text() )
                insertCall.setString( 30, systemData.FOBSYSC_ISS_NSF_ON_OFF_IND.text() )
                insertCall.setString( 31, systemData.FOBSYSC_FA_NSF_ON_OFF_IND.text() )
                insertCall.setString( 32, systemData.FOBSYSC_APPR_OVERRIDE_IND_FA.text() )
                insertCall.setString( 33, systemData.FOBSYSC_MIN_ASSET_CAP_AMT.text() )
                insertCall.setString( 34, systemData.FOBSYSC_DCR_NSF_ON_OFF_IND.text() )
                insertCall.setString( 35, systemData.FOBSYSC_APPR_OVERRIDE_IND_DCR.text() )
                insertCall.setString( 36, systemData.FOBSYSC_DEFER_GRANT_IND.text() )
                insertCall.setString( 37, systemData.FOBSYSC_IC_CS_ORDER_IND.text() )
                insertCall.setString( 38, systemData.FOBSYSC_JV_DTAG_FEED_IND.text() )
                insertCall.setString( 39, systemData.FOBSYSC_ISSU_DTAG_FEED_IND.text() )
                insertCall.setString( 40, systemData.FOBSYSC_DCSR_DTAG_FEED_IND.text() )
                insertCall.setString( 41, systemData.FOBSYSC_DOC_LVL_MATCH_IND.text() )
                insertCall.setString( 42, systemData.FOBSYSC_WBUD_IND.text() )
                insertCall.setString( 43, systemData.FOBSYSC_WBUD_TRACK_IND.text() )
                insertCall.setString( 44, systemData.FOBSYSC_DFR_GRNT_HIST_IND.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FOBSYSC", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FOBSYSC", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance System Control with ..."
                    println "Problem executing insert record for Finance System Control: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FOBSYSC", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FOBSYSC from FinanceSystemControlCreateDML.groovy: $e.message"
            }
        }
    }
}
