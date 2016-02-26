/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement Fiscal year
 */
public class FinanceProcurementFiscalYearDML {


    def oracle_id

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceProcurementFiscalYearDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceProcurementFiscalYearDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceProcurementUser()
    }


    def parseXmlData() {
        def userCreationData = new XmlParser().parseText( xmlData )
        this.oracle_id = userCreationData.FINANCEFISCAYEAR_USER.text()
    }

    /**
     * Creates finance procurement user
     */
    def createFinanceProcurementUser() {
        try {

            final String apiQuery = " DECLARE" +
                    " user_id varchar2(30)   := ?; " +
                    " update_mode varchar2(1):= 'U';" +
                    " coas varchar2(1)       := 'B';" +
                    " fsyr_start_yr number   := TO_CHAR(SYSDATE, 'YYYY');" +
                    " FSYR_END_YR   NUMBER   := TO_CHAR(SYSDATE, 'YYYY');" +
                    " PD_START   VARCHAR2(6) := '01-JUL';" +
                    " PD_INCR    NUMBER      := 1;" +
                    " PD_EOQ_IND NUMBER      := 3;" +
                    " PRD_STATUS VARCHAR2(1) := 'O';" +
                    " EOY_IND VARCHAR2(1)    := 'N';" +
                    " st_date DATE;" +
                    " end_date DATE;" +
                    " fsyr VARCHAR2(2);" +
                    " pd_per_yr NUMBER;" +
                    " pd_last VARCHAR2(2);" +
                    " pd_code VARCHAR2(2);" +
                    " pd_st_date DATE;" +
                    " pd_end_date DATE;" +
                    " pd_eoq VARCHAR2(1);" +

                    " BEGIN" +
                    " IF LENGTH(USER_ID) = 0 THEN USER_ID := USER; END IF;" +
                    " pd_per_yr := 12 / PD_INCR;" +
                    " pd_last := ltrim(to_char(pd_per_yr,'09'));" +
                    " FOR yr IN FSYR_START_YR .. FSYR_END_YR LOOP" +
                    "   fsyr := substr(to_char(yr+1),3,2);" +
                    " st_date := to_date(PD_START || '-' || to_char(yr));" +
                    " end_date := last_day(add_months(st_date,11));" +
                    " IF update_mode = 'U' THEN" +
                    " BEGIN" +
                    " INSERT INTO FTVFSYR (ftvfsyr_coas_code," +
                    " ftvfsyr_fsyr_code, ftvfsyr_activity_date," +
                    " ftvfsyr_user_id, ftvfsyr_start_date, ftvfsyr_end_date," +
                    " ftvfsyr_last_period, ftvfsyr_eoy_accr_status_ind)" +
                    " VALUES (COAS, fsyr, sysdate, USER_ID, st_date, end_date," +
                    " pd_last, EOY_IND);" +
                    " exception when others then" +
                    " NULL; " +
                    " END;" +
                    " END IF;" +
                    " pd_st_date := st_date;" +
                    " FOR pd IN 1 .. pd_per_yr LOOP" +
                    " pd_code := ltrim(to_char(pd,'09'));" +
                    " pd_end_date := last_day(pd_st_date);" +
                    " IF MOD(pd,PD_EOQ_IND) = 0 THEN" +
                    " pd_eoq := 'Y';" +
                    " ELSE" +
                    " pd_eoq := 'N';" +
                    " END IF;" +

                    " IF update_mode = 'U' THEN" +
                    " BEGIN" +
                    " INSERT INTO FTVFSPD (ftvfspd_coas_code, ftvfspd_fsyr_code," +
                    " ftvfspd_fspd_code, ftvfspd_activity_date, ftvfspd_user_id," +
                    " ftvfspd_prd_start_date, ftvfspd_prd_end_date," +
                    " ftvfspd_prd_status_ind, ftvfspd_eoq_ind)" +
                    " VALUES (COAS, fsyr, pd_code, sysdate, USER_ID, pd_st_date, pd_end_date," +
                    " PRD_STATUS, pd_eoq);" +
                    " exception when others then" +
                    " NULL;" +
                    " END;" +
                    " END IF;" +
                    " pd_st_date := add_months(pd_st_date, PD_INCR);" +
                    " END LOOP;" +
                    " end loop;" +
                    " commit;" +
                    " END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, this.oracle_id )
                insertCall.execute()
                connectInfo.tableUpdate( "FINANCEFISCAYEAR", 0, 1, 0, 0, 0 )
            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FINANCEFISCAYEAR", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Problem executing Update for table FINANCEFISCAYEAR from FinanceProcurementFiscalYearDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FINANCEFISCAYEAR", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing script with ${this.oracle_id} ${this.spriden_id} ${this.source_oracle_id}"
                println "Problem executing Update for table FINANCEFISCAYEAR from FinanceProcurementFiscalYearDML.groovy: $e.message"
            }
        }
    }
}
