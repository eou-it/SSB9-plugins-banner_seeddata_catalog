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
        this.conn = conn
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
            def systemDate=new java.sql.Date(System.currentTimeMillis());
            final String sql = """ INSERT INTO FOBSYSC(FOBSYSC_EFF_DATE, FOBSYSC_ACTIVITY_DATE, FOBSYSC_USER_ID, FOBSYSC_STATUS_IND, FOBSYSC_DEFAULT_TIMING_IND, FOBSYSC_RUCL_SECURITY_IND, FOBSYSC_FUND_ORG_SECURITY_IND, FOBSYSC_TAX_PROCESSING_IND, FOBSYSC_CONSOL_POST_IND, FOBSYSC_JV_NSF_ON_OFF_IND, FOBSYSC_PO_NSF_ON_OFF_IND, FOBSYSC_ENC_NSF_ON_OFF_IND, FOBSYSC_REQ_NSF_ON_OFF_IND, FOBSYSC_INV_NSF_ON_OFF_IND, FOBSYSC_APPR_OVERRIDE_IND_REQ, FOBSYSC_APPR_OVERRIDE_IND_PO, FOBSYSC_APPR_OVERRIDE_IND_INV, FOBSYSC_APPR_OVERRIDE_IND_CO, FOBSYSC_APPR_OVERRIDE_IND_ENC, FOBSYSC_APPR_OVERRIDE_IND_JV, FOBSYSC_ACCRUED_ON_OFF_IND, FOBSYSC_NCHG_DATE, FOBSYSC_ACCT_CODE_COAS_DUE_TO, FOBSYSC_ACCT_CODE_COAS_DUE_FRM, FOBSYSC_AUTO_BUYR_IND, FOBSYSC_EDIT_DEFER_IND, FOBSYSC_TGRP_CODE_DEFAULT, FOBSYSC_TAX_OVERRIDE_AMT, FOBSYSC_TAX_OVERRIDE_PCT, FOBSYSC_FEDERAL_EMPLOYER_ID, FOBSYSC_MULTIPLE_FB_ACCT_IND, FOBSYSC_ISS_NSF_ON_OFF_IND, FOBSYSC_FA_NSF_ON_OFF_IND, FOBSYSC_APPR_OVERRIDE_IND_FA, FOBSYSC_MIN_ASSET_CAP_AMT, FOBSYSC_DCR_NSF_ON_OFF_IND, FOBSYSC_APPR_OVERRIDE_IND_DCR, FOBSYSC_DEFER_GRANT_IND, FOBSYSC_IC_CS_ORDER_IND, FOBSYSC_JV_DTAG_FEED_IND, FOBSYSC_ISSU_DTAG_FEED_IND, FOBSYSC_DCSR_DTAG_FEED_IND, FOBSYSC_DOC_LVL_MATCH_IND, FOBSYSC_WBUD_IND, FOBSYSC_WBUD_TRACK_IND, FOBSYSC_DFR_GRNT_HIST_IND) VALUES(?, ?, 'GRAILS', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) """
            try {
                conn.execute(""" DELETE FROM FOBSYSC where FOBSYSC_USER_ID='GRAILS' AND FOBSYSC_EFF_DATE=? AND FOBSYSC_NCHG_DATE=? """, [systemData.FOBSYSC_EFF_DATE.text(), systemData.FOBSYSC_NCHG_DATE.text()]);
                conn.executeInsert(sql, [systemData.FOBSYSC_EFF_DATE.text(), systemDate, systemData.FOBSYSC_STATUS_IND.text(),systemData.FOBSYSC_DEFAULT_TIMING_IND.text(),systemData.FOBSYSC_RUCL_SECURITY_IND.text(),systemData.FOBSYSC_FUND_ORG_SECURITY_IND.text(),systemData.FOBSYSC_TAX_PROCESSING_IND.text(),systemData.FOBSYSC_CONSOL_POST_IND.text(),systemData.FOBSYSC_JV_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_PO_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_ENC_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_REQ_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_INV_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_REQ.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_PO.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_INV.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_CO.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_ENC.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_JV.text(),systemData.FOBSYSC_ACCRUED_ON_OFF_IND.text(),systemData.FOBSYSC_NCHG_DATE.text(),systemData.FOBSYSC_ACCT_CODE_COAS_DUE_TO.text(),systemData.FOBSYSC_ACCT_CODE_COAS_DUE_FRM.text(),systemData.FOBSYSC_AUTO_BUYR_IND.text(),systemData.FOBSYSC_EDIT_DEFER_IND.text(),systemData.FOBSYSC_TGRP_CODE_DEFAULT.text(),systemData.FOBSYSC_TAX_OVERRIDE_AMT.text(),systemData.FOBSYSC_TAX_OVERRIDE_PCT.text(),systemData.FOBSYSC_FEDERAL_EMPLOYER_ID.text(),systemData.FOBSYSC_MULTIPLE_FB_ACCT_IND.text(),systemData.FOBSYSC_ISS_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_FA_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_FA.text(),systemData.FOBSYSC_MIN_ASSET_CAP_AMT.text(),systemData.FOBSYSC_DCR_NSF_ON_OFF_IND.text(),systemData.FOBSYSC_APPR_OVERRIDE_IND_DCR.text(),systemData.FOBSYSC_DEFER_GRANT_IND.text(),systemData.FOBSYSC_IC_CS_ORDER_IND.text(),systemData.FOBSYSC_JV_DTAG_FEED_IND.text(),systemData.FOBSYSC_ISSU_DTAG_FEED_IND.text(),systemData.FOBSYSC_DCSR_DTAG_FEED_IND.text(),systemData.FOBSYSC_DOC_LVL_MATCH_IND.text(),systemData.FOBSYSC_WBUD_IND.text(),systemData.FOBSYSC_WBUD_TRACK_IND.text(),systemData.FOBSYSC_DFR_GRNT_HIST_IND.text()])
                connectInfo.tableUpdate('FOBSYSC', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FOBSYSC", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance System Control with ..."
                    println "Problem executing insert record for Finance System Control: $e.message"
                }
            }

    }
}
