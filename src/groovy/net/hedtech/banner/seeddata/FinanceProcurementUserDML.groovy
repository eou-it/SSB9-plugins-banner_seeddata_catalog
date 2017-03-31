/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement User
 */
public class FinanceProcurementUserDML {


    def oracle_id
    def spriden_id
    def pidm_role

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceProcurementUserDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.connectInfo.dataSource=null;


    }


    public FinanceProcurementUserDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectInfo.dataSource=null;
        def newConn = new ConnectDB(connectInfo)
        this.connectCall = newConn.getSqlConnection().connection
        this.xmlData = xmlData
        parseXmlData()
        createFinanceProcurementUser()
    }


    def parseXmlData() {
        def userCreationData = new XmlParser().parseText( xmlData )
        this.oracle_id = userCreationData.FINANCEUSER_ORACLE_ID.text()
        this.spriden_id = userCreationData.FINANCEUSER_SPRIDEN_ID.text()
        this.pidm_role = userCreationData.FINANCEUSER_PIDM_ROLE.text()
    }

    /**
     * Creates finance procurement user
     */
    def createFinanceProcurementUser() {
        try {
            final String apiQuery = "declare" +
                    "   oracle_id varchar2(10) :=? ;" +
                    "   spriden_id varchar2(10) :=?;" +
                    "   total_user number :=1;" +
                    "   gv_pidm                            PLS_INTEGER;" +
                    "   user_not_exist                     EXCEPTION;" +
                    "   PRAGMA EXCEPTION_INIT (user_not_exist, -01918);" +
                    "   user_already_exists                EXCEPTION;" +
                    "   PRAGMA EXCEPTION_INIT (user_already_exists, -01920);" +
                    "   PROCEDURE p_create_oracle_user (p_id VARCHAR2)" +
                    "   IS" +
                    "   BEGIN" +
                    "   BEGIN" +
                    "   EXECUTE IMMEDIATE 'drop  user ' || p_id || ' CASCADE';" +
                    "   EXCEPTION" +
                    "   WHEN others" +
                    "   THEN" +
                    "   NULL;" +
                    "   END;" +
                    "   BEGIN" +
                    "   EXECUTE IMMEDIATE 'create  user ' || p_id || ' identified by u_pick_it';" +
                    "   EXCEPTION" +
                    "   WHEN user_already_exists" +
                    "   THEN" +
                    "   NULL;" +
                    "   END;" +
                    "   EXECUTE IMMEDIATE 'grant CONNECT, resource, ban_default_m to ' || p_id;" +
                    "   EXECUTE IMMEDIATE 'alter user ' || p_id || ' grant connect through BANPROXY';" +
                    "   EXECUTE IMMEDIATE 'alter user ' || p_id || ' default role all except ban_default_m';" +
                    "   insert into gurucls ( gurucls_userid, gurucls_class_code, gurucls_activity_date, gurucls_user_id)" +
                    "   select  p_id, 'BAN_GENERAL_C', sysdate, user" +
                    "   from dual" +
                    "   where not exists (select 1 from gurucls" +
                    "   where gurucls_userid = p_id and" +
                    "   gurucls_class_code = 'BAN_GENERAL_C');" +
                    "   insert into gurucls ( gurucls_userid, gurucls_class_code, gurucls_activity_date, gurucls_user_id)" +
                    "   select  p_id, 'BAN_FINANCE_C', sysdate, user" +
                    "   from dual" +
                    "   where not exists (select 1 from gurucls" +
                    "   where gurucls_userid = p_id and" +
                    "   gurucls_class_code = 'BAN_FINANCE_C');" +
                    "   DBMS_OUTPUT.put_line ('Created oracle user : ' || p_id);" +
                    "   delete from FOBPROF where FOBPROF_USER_ID = p_id;" +
                    /* "   INSERT" +
                     "   INTO FOBPROF" +
                     "   (" +
                     "   FOBPROF_USER_ID," +
                     "   FOBPROF_ACTIVITY_DATE," +
                     "   FOBPROF_USER_NAME," +
                     "   FOBPROF_COAS_CODE," +
                     "   FOBPROF_SECG_CODE," +
                     "   FOBPROF_NSF_OVERRIDE," +
                     "   FOBPROF_TOLERANCE," +
                     "   FOBPROF_BUD_ID," +
                     "   FOBPROF_PIDM," +
                     "   FOBPROF_MASTER_FUND_IND," +
                     "   FOBPROF_MASTER_ORGN_IND," +
                     "   FOBPROF_MAX_TOLERANCE_AMT," +
                     "   FOBPROF_RCVD_OVERRIDE_IND," +
                     "   FOBPROF_RCVD_TOLERANCE_PCT," +
                     "   FOBPROF_TOL_OVERRIDE_IND," +
                     "   FOBPROF_DST_SPD_OVERRIDE_IND," +
                     "   FOBPROF_SPD_OVERRIDE_IND," +
                     "   FOBPROF_INT_RATE_OVERRIDE_IND," +
                     "   FOBPROF_USER_INV_PRIV," +
                     "   FOBPROF_EXP_END_POST_AUTH_IND," +
                     "   FOBPROF_ACCRUAL_POST_AUTH_IND," +
                     "   FOBPROF_REQUESTER_ORGN_CODE," +
                     "   FOBPROF_RCVD_TOLERANCE_QTY," +
                     "   FOBPROF_RCVD_TOLERANCE_AMT," +
                     "   FOBPROF_RCVD_TOLERANCE_AMT_PCT," +
                     "   FOBPROF_REQUESTOR_EMAIL_ADDR," +
                     "   FOBPROF_REQUESTOR_FAX_AREA," +
                     "   FOBPROF_REQUESTOR_FAX_NUMBER," +
                     "   FOBPROF_REQUESTOR_FAX_EXT," +
                     "   FOBPROF_REQUESTOR_PHONE_AREA," +
                     "   FOBPROF_REQUESTOR_PHONE_NUMBER," +
                     "   FOBPROF_REQUESTOR_PHONE_EXT," +
                     "   FOBPROF_REQUESTOR_SHIP_CODE," +
                     "   FOBPROF_EDI_OVERRIDE_IND," +
                     "   FOBPROF_ACH_OVERRIDE_IND," +
                     "   FOBPROF_CARD_OVERRIDE_IND," +
                     "   FOBPROF_REQ_MATCH_OVRRD_IND," +
                     "   FOBPROF_PO_MATCH_OVRRD_IND," +
                     "   FOBPROF_INV_MATCH_OVRRD_IND," +
                     "   FOBPROF_WEB_ACCESS_IND," +
                     "   FOBPROF_WBUD_ACCESS_IND," +
                     "   FOBPROF_WBUD_MSTR_FUND_IND," +
                     "   FOBPROF_WBUD_MSTR_ORGN_IND," +
                     "   FOBPROF_CTRY_CODE_REQ_PHONE," +
                     "   FOBPROF_CTRY_CODE_REQ_FAX," +
                     "   FOBPROF_WHRLD_ACCESS_IND," +
                     "   FOBPROF_PO_HOLD_OVRD_IND," +
                     "   FOBPROF_PMT_HOLD_OVRD_IND," +
                     "   FOBPROF_VERSION," +
                     "   FOBPROF_DATA_ORIGIN," +
                     "   FOBPROF_VPDI_CODE" +
                     "   ) select" +
                     "   p_id," +
                     "   FOBPROF_ACTIVITY_DATE," +
                     "   FOBPROF_USER_NAME," +
                     "   FOBPROF_COAS_CODE," +
                     "   FOBPROF_SECG_CODE," +
                     "   FOBPROF_NSF_OVERRIDE," +
                     "   FOBPROF_TOLERANCE," +
                     "   FOBPROF_BUD_ID," +
                     "   FOBPROF_PIDM," +
                     "   'B'," +
                     "   'B'," +
                     "   FOBPROF_MAX_TOLERANCE_AMT," +
                     "   FOBPROF_RCVD_OVERRIDE_IND," +
                     "   FOBPROF_RCVD_TOLERANCE_PCT," +
                     "   FOBPROF_TOL_OVERRIDE_IND," +
                     "   FOBPROF_DST_SPD_OVERRIDE_IND," +
                     "   FOBPROF_SPD_OVERRIDE_IND," +
                     "   FOBPROF_INT_RATE_OVERRIDE_IND," +
                     "   FOBPROF_USER_INV_PRIV," +
                     "   FOBPROF_EXP_END_POST_AUTH_IND," +
                     "   FOBPROF_ACCRUAL_POST_AUTH_IND," +
                     "   FOBPROF_REQUESTER_ORGN_CODE," +
                     "   FOBPROF_RCVD_TOLERANCE_QTY," +
                     "   FOBPROF_RCVD_TOLERANCE_AMT," +
                     "   FOBPROF_RCVD_TOLERANCE_AMT_PCT," +
                     "   FOBPROF_REQUESTOR_EMAIL_ADDR," +
                     "   FOBPROF_REQUESTOR_FAX_AREA," +
                     "   FOBPROF_REQUESTOR_FAX_NUMBER," +
                     "   FOBPROF_REQUESTOR_FAX_EXT," +
                     "   FOBPROF_REQUESTOR_PHONE_AREA," +
                     "   FOBPROF_REQUESTOR_PHONE_NUMBER," +
                     "   FOBPROF_REQUESTOR_PHONE_EXT," +
                     "   FOBPROF_REQUESTOR_SHIP_CODE," +
                     "   FOBPROF_EDI_OVERRIDE_IND," +
                     "   FOBPROF_ACH_OVERRIDE_IND," +
                     "   FOBPROF_CARD_OVERRIDE_IND," +
                     "   FOBPROF_REQ_MATCH_OVRRD_IND," +
                     "   FOBPROF_PO_MATCH_OVRRD_IND," +
                     "   FOBPROF_INV_MATCH_OVRRD_IND," +
                     "   FOBPROF_WEB_ACCESS_IND," +
                     "   FOBPROF_WBUD_ACCESS_IND," +
                     "   FOBPROF_WBUD_MSTR_FUND_IND," +
                     "   FOBPROF_WBUD_MSTR_ORGN_IND," +
                     "   FOBPROF_CTRY_CODE_REQ_PHONE," +
                     "   FOBPROF_CTRY_CODE_REQ_FAX," +
                     "   FOBPROF_WHRLD_ACCESS_IND," +
                     "   FOBPROF_PO_HOLD_OVRD_IND," +
                     "   FOBPROF_PMT_HOLD_OVRD_IND," +
                     "   FOBPROF_VERSION," +
                     "   FOBPROF_DATA_ORIGIN," +
                     "   FOBPROF_VPDI_CODE FROM FOBPROF where FOBPROF_USER_ID = ?;" +    */
                    "INSERT INTO FOBPROF  " +
                    "  (FOBPROF_USER_ID, " +
                    "   FOBPROF_ACTIVITY_DATE, " +
                    "   FOBPROF_USER_NAME, " +
                    "   FOBPROF_COAS_CODE, " +
                    "   FOBPROF_SECG_CODE, " +
                    "   FOBPROF_NSF_OVERRIDE, " +
                    "   FOBPROF_TOLERANCE, " +
                    "   FOBPROF_BUD_ID, " +
                    "   FOBPROF_PIDM, " +
                    "   FOBPROF_MASTER_FUND_IND, " +
                    "   FOBPROF_MASTER_ORGN_IND, " +
                    "   FOBPROF_MAX_TOLERANCE_AMT, " +
                    "   FOBPROF_RCVD_OVERRIDE_IND, " +
                    "   FOBPROF_RCVD_TOLERANCE_PCT, " +
                    "   FOBPROF_TOL_OVERRIDE_IND, " +
                    "   FOBPROF_DST_SPD_OVERRIDE_IND, " +
                    "   FOBPROF_SPD_OVERRIDE_IND, " +
                    "   FOBPROF_INT_RATE_OVERRIDE_IND, " +
                    "   FOBPROF_USER_INV_PRIV, " +
                    "   FOBPROF_EXP_END_POST_AUTH_IND, " +
                    "   FOBPROF_ACCRUAL_POST_AUTH_IND, " +
                    "   FOBPROF_REQUESTER_ORGN_CODE, " +
                    "   FOBPROF_RCVD_TOLERANCE_QTY, " +
                    "   FOBPROF_RCVD_TOLERANCE_AMT, " +
                    "   FOBPROF_RCVD_TOLERANCE_AMT_PCT, " +
                    "   FOBPROF_REQUESTOR_EMAIL_ADDR, " +
                    "   FOBPROF_REQUESTOR_FAX_AREA, " +
                    "   FOBPROF_REQUESTOR_FAX_NUMBER, " +
                    "   FOBPROF_REQUESTOR_FAX_EXT, " +
                    "   FOBPROF_REQUESTOR_PHONE_AREA, " +
                    "   FOBPROF_REQUESTOR_PHONE_NUMBER, " +
                    "   FOBPROF_REQUESTOR_PHONE_EXT, " +
                    "   FOBPROF_REQUESTOR_SHIP_CODE, " +
                    "   FOBPROF_EDI_OVERRIDE_IND, " +
                    "   FOBPROF_ACH_OVERRIDE_IND, " +
                    "   FOBPROF_CARD_OVERRIDE_IND, " +
                    "   FOBPROF_REQ_MATCH_OVRRD_IND, " +
                    "   FOBPROF_PO_MATCH_OVRRD_IND, " +
                    "   FOBPROF_INV_MATCH_OVRRD_IND, " +
                    "   FOBPROF_WEB_ACCESS_IND, " +
                    "   FOBPROF_WBUD_ACCESS_IND, " +
                    "   FOBPROF_WBUD_MSTR_FUND_IND, " +
                    "   FOBPROF_WBUD_MSTR_ORGN_IND, " +
                    "   FOBPROF_CTRY_CODE_REQ_PHONE, " +
                    "   FOBPROF_CTRY_CODE_REQ_FAX, " +
                    "   FOBPROF_WHRLD_ACCESS_IND, " +
                    "   FOBPROF_PO_HOLD_OVRD_IND, " +
                    "   FOBPROF_PMT_HOLD_OVRD_IND, " +
                    "   FOBPROF_VERSION, " +
                    "   FOBPROF_DATA_ORIGIN, " +
                    "   FOBPROF_VPDI_CODE)  " +
                    "   select " +
                    "   p_id, " +
                    "   sysdate, " +
                    "   'Finance Seed User - ' || p_id, " +
                    "   'B', " +
                    "   null, " +
                    "   'Y', " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   'B', " +
                    "   'B', " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   'A', " +
                    "   'N', " +
                    "   'N', " +
                    "   '11007', " +
                    "   null, " +
                    "   null, " +
                    "   null, " +
                    "   'seed user', " +
                    "   '252', " +
                    "   '6597777', " +
                    "   null, " +
                    "   '252', " +
                    "   '6591111', " +
                    "   null, " +
                    "   'ATHLDR', " +
                    "   'N', " +
                    "   'N', " +
                    "   'N', " +
                    "   'N', " +
                    "   'N', " +
                    "   'N', " +
                    "   'Y', " +
                    "   'Y', " +
                    "   'B', " +
                    "   'B', " +
                    "   null, " +
                    "   null, " +
                    "   'Y', " +
                    "   'N', " +
                    "   'N', " +
                    "   0, " +
                    "   null, " +
                    "   null from dual; " +
                    "   END p_create_oracle_user;" +
                    "   FUNCTION f_create_banner_id (p_id            VARCHAR2" +
                    "   ,p_spriden_id\tVARCHAR2" +
                    "   ,p_first_name    VARCHAR2" +
                    "   ,p_last_name     VARCHAR2)" +
                    "   RETURN PLS_INTEGER" +
                    "   IS" +
                    "   gv_id                              VARCHAR2 (12) := p_spriden_id;" +
                    "   gv_pidm1                           NUMBER := NULL;" +
                    "   gv_rowid1                          VARCHAR2 (18);" +
                    "   lv_result                          VARCHAR2 (10);" +
                    "   gv_format                          VARCHAR2(1); "  +
                    "   BEGIN" +
                    "   DELETE FROM gobeacc" +
                    "   WHERE gobeacc_pidm = (SELECT DISTINCT spriden_pidm" +
                    "   FROM spriden" +
                    "   WHERE spriden_id = p_spriden_id);" +
                    "   DELETE FROM gobeacc" +
                    "   WHERE gobeacc_username = p_id;" +
                    "   DELETE FROM gobtpac" +
                    "   WHERE gobtpac_pidm = (SELECT DISTINCT spriden_pidm" +
                    "   FROM spriden" +
                    "   WHERE spriden_id = p_spriden_id);" +
                    "   DELETE FROM twgrrole" +
                    "   WHERE twgrrole_pidm = (SELECT DISTINCT spriden_pidm" +
                    "   FROM spriden" +
                    "   WHERE spriden_id = p_spriden_id);" +
                    "   DELETE FROM spriden" +
                    "   WHERE spriden_pidm = (SELECT DISTINCT spriden_pidm" +
                    "   FROM spriden" +
                    "   WHERE spriden_id = p_spriden_id);" +
                    "   SELECT gubpprf_format into gv_format FROM gubpprf; " +
                    "   UPDATE gubpprf set gubpprf_format='A' ; " +
                    "   IF (gb_common.f_id_exists (p_id) = 'N')" +
                    "   THEN" +
                    "   gb_identification.p_create (p_id_inout      => gv_id" +
                    "   ,p_last_name     => p_last_name" +
                    "   ,p_first_name    => p_first_name" +
                    "   ,p_change_ind    => NULL" +
                    "   ,p_entity_ind    => 'P'" +
                    "   ,p_data_origin   => 'FPR Create User Script'" +
                    "   ,p_pidm_inout    => gv_pidm1" +
                    "   ,p_rowid_out     => gv_rowid1);" +
                    "   ELSE" +
                    "   SELECT spriden_id, spriden_pidm" +
                    "   INTO gv_id, gv_pidm1" +
                    "   FROM spriden" +
                    "   WHERE spriden_id = p_spriden_id AND spriden_change_ind IS NULL;" +
                    "   END IF;" +
                    "   DBMS_OUTPUT.put_line ('Created spriden id: ' || gv_id || ', pidm: ' || gv_pidm1);" +
                    "   IF gb_third_party_access.f_exists (gv_pidm1) = 'N'" +
                    "   THEN" +
                    "   goktpty.p_insert_gobtpac (pidm_in => gv_pidm1, result_out => lv_result);" +
                    "   " +
                    "   IF lv_result IS NOT NULL" +
                    "   THEN" +
                    "   DBMS_OUTPUT.put_line ('Third party: ' || lv_result);" +
                    "   END IF;" +
                    "   END IF;" +
                    "   gokmods.p_update_gobtpac (gv_pidm1, '111111', '01-JAN-2050');" +
                    "   p_create_oracle_user (p_id);" +
                    "   UPDATE gubpprf set gubpprf_format=gv_format ; " +
                    "   RETURN gv_pidm1;" +
                    "   END f_create_banner_id;" +
                    "   PROCEDURE p_create_gobeacc (p_pidm PLS_INTEGER, p_id VARCHAR2)" +
                    "   IS" +
                    "   BEGIN" +
                    "   DBMS_OUTPUT.put_line ('Gobeacc: ' || p_id || ', pidm: ' || p_pidm);" +
                    "   MERGE INTO gobeacc a" +
                    "   USING (SELECT p_pidm AS gobeacc_pidm" +
                    "   ,p_id AS gobeacc_username" +
                    "   ,USER AS gobeacc_user_id" +
                    "   ,SYSDATE AS gobeacc_activity_date" +
                    "   ,'FPR' AS gobeacc_data_origin" +
                    "   FROM DUAL) b" +
                    "   ON (a.gobeacc_pidm = b.gobeacc_pidm)" +
                    "   WHEN NOT MATCHED " +
                    "   THEN" +
                    "   INSERT     (gobeacc_pidm, gobeacc_username, gobeacc_user_id" +
                    "   ,gobeacc_activity_date, gobeacc_data_origin)" +
                    "   VALUES (b.gobeacc_pidm" +
                    "   ,b.gobeacc_username" +
                    "   ,b.gobeacc_user_id" +
                    "   ,b.gobeacc_activity_date" +
                    "   ,b.gobeacc_data_origin)" +
                    "   WHEN MATCHED" +
                    "   THEN" +
                    "   UPDATE SET a.gobeacc_username = b.gobeacc_username" +
                    "   ,a.gobeacc_user_id = b.gobeacc_user_id" +
                    "   ,a.gobeacc_activity_date = b.gobeacc_activity_date" +
                    "   ,a.gobeacc_data_origin = b.gobeacc_data_origin;" +
                    "   END p_create_gobeacc;" +
                    "   PROCEDURE p_assign_wt_role (p_pidm PLS_INTEGER, p_role VARCHAR2)" +
                    "   IS" +
                    "   BEGIN" +
                    "   DBMS_OUTPUT.put_line ('Granting WT role: ' || p_role || ', to  ' || p_pidm);" +
                    "   MERGE INTO twgrrole a" +
                    "   USING (SELECT p_pidm AS twgrrole_pidm" +
                    "   ,p_role AS twgrrole_role" +
                    "   ,SYSDATE AS twgrrole_activity_date" +
                    "   ,NULL AS twgrrole_surrogate_id" +
                    "   ,NULL AS twgrrole_version" +
                    "   ,'FPR' AS twgrrole_user_id" +
                    "   ,'FPR' AS twgrrole_data_origin" +
                    "   ,NULL AS twgrrole_vpdi_code" +
                    "   FROM DUAL) b" +
                    "   ON (a.twgrrole_pidm = b.twgrrole_pidm AND a.twgrrole_role = b.twgrrole_role)" +
                    "   WHEN NOT MATCHED" +
                    "   THEN" +
                    "   INSERT     (twgrrole_pidm, twgrrole_role, twgrrole_activity_date" +
                    "   ,twgrrole_surrogate_id, twgrrole_version, twgrrole_user_id" +
                    "   ,twgrrole_data_origin, twgrrole_vpdi_code)" +
                    "   VALUES (b.twgrrole_pidm" +
                    "   ,b.twgrrole_role" +
                    "   ,b.twgrrole_activity_date" +
                    "   ,b.twgrrole_surrogate_id" +
                    "   ,b.twgrrole_version" +
                    "   ,b.twgrrole_user_id ,b.twgrrole_data_origin ,b.twgrrole_vpdi_code) " +
                    "   WHEN MATCHED" +
                    "   THEN" +
                    "   UPDATE SET a.twgrrole_activity_date = b.twgrrole_activity_date" +
                    "   ,a.twgrrole_surrogate_id = b.twgrrole_surrogate_id" +
                    "   ,a.twgrrole_version = b.twgrrole_version" +
                    "   ,a.twgrrole_user_id = b.twgrrole_user_id" +
                    "   ,a.twgrrole_data_origin = b.twgrrole_data_origin" +
                    "   ,a.twgrrole_vpdi_code = b.twgrrole_vpdi_code;" +
                    "   END p_assign_wt_role;" +
                    "   BEGIN" +
                    "   GV_PIDM := F_CREATE_BANNER_ID (ORACLE_ID, SPRIDEN_ID, SPRIDEN_ID, SPRIDEN_ID);" +
                    "   p_create_gobeacc (gv_pidm, oracle_id);" +
                    "   p_assign_wt_role (gv_pidm, 'FINANCE');" +
                    "   commit;" +
                    "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            if (connectInfo.debugThis) {
                println "Executing script with ${this.oracle_id} ${this.spriden_id}"
            }
            try {
                // create  5 users
                for (int i = 1; i <= 5; i++) {
                    // parm 1 oracle_id
                    insertCall.setString( 1, this.oracle_id + i )

                    // parm 2 spriden_id
                    insertCall.setString( 2, this.spriden_id + i )

                    insertCall.execute()
                    connectInfo.tableUpdate( "SPRIDEN", 0, 1, 0, 0, 0 )
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "SPRIDEN", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script with ${this.oracle_id} ${this.spriden_id} ${this.source_oracle_id}"
                    println "Problem executing Update for table SPRIDEN from FinanceProcurementUserDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "SPRIDEN", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing script with ${this.oracle_id} ${this.spriden_id} ${this.source_oracle_id}"
                println "Problem executing Update for table SPRIDEN from FinanceProcurementUserDML.groovy: $e.message"
            }
        }
    }
}
