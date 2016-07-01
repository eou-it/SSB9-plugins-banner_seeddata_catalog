/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability Create Budget Journal Voucher
 */
public class FinanceBudgetAvailabilityCreatePurchaseRequisitionsDML {


    def startFsyrCode, endFsyrCode, documentNumber, oracleUserName
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityCreatePurchaseRequisitionsDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityCreatePurchaseRequisitionsDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateRequisitions()
    }


    def parseXmlData() {
        def journalVoucherData = new XmlParser().parseText( xmlData )
        this.startFsyrCode = journalVoucherData.START_FSYR_CODE.text() as int
        this.endFsyrCode = journalVoucherData.END_FSYR_CODE.text() as int
        this.documentNumber = journalVoucherData.DOCUMENT_NUMBER.text()
        this.oracleUserName = journalVoucherData.ORACLE_USER_NAME.text()
    }

    /**
     * Populate Requisitions
     */
    def populateRequisitions() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  l_start_fsyr_code  NUMBER      := ?; --EXTERNAL\n" +
                            "  L_END_FSYR_CODE    NUMBER      := ?; --EXTERNAL\n" +
                            "  l_doc_cnt          NUMBER      := 10;   -- per day document count\n" +
                            "  l_comm_cnt         NUMBER      := 3;    -- per document commodity count\n" +
                            "  l_acct_cnt         NUMBER      := 3;    -- per document accounting count\n" +
                            "  L_SINGLE_ACCTG_IND VARCHAR2(1) := 'N';  -- commodity/document accounting\n" +
                            "  lv_increment_days  NUMBER      := 10;   -- number of days to be incremented\n" +
                            "  l_foap_code        NUMBER      := 21;\n" +
                            "  l_comm_total       NUMBER      :=0;\n" +
                            "  lv_acct_amt        NUMBER      :=0;\n" +
                            "  EXIT_NO            NUMBER      :=0;\n" +
                            "  EXisting_fobsysc   VARCHAR2(1) :='N';\n" +
                            "  l_start_date       DATE;\n" +
                            "  l_end_date         DATE;\n" +
                            "  l_d_increment      DATE;\n" +
                            "  lv_complete_req    VARCHAR2(1);\n" +
                            "  auto_increment_var NUMBER       :=1;\n" +
                            "  req_doc_num        VARCHAR2(10) := ?;--EXTERNAL\n" +
                            "  lv_trans_date      DATE;\n" +
                            "  lv_vend_pidm       NUMBER := 499;\n" +
                            "  lv_head_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_comm_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_acctg_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_tot_doc_cnt  NUMBER  :=0;\n" +
                            "  lv_fatal_error  BOOLEAN :=FALSE;\n" +
                            "  p_error_msg_out VARCHAR2(2000);\n" +
                            "  p_success_msg_out string_nt;\n" +
                            "  LV_ERROR_MSG GB_COMMON_STRINGS.ERR_TYPE;\n" +
                            "  p_warn_msg_out gb_common_strings.err_type;\n" +
                            "  l_fund_code   VARCHAR2(10);\n" +
                            "  l_orgn_code   VARCHAR2(10);\n" +
                            "  l_acct_code   VARCHAR2(10);\n" +
                            "  l_prog_code   VARCHAR2(10);\n" +
                            "  l_actv_code   VARCHAR2(10);\n" +
                            "  l_locn_code   VARCHAR2(10);\n" +
                            "  l_foap_return BOOLEAN;\n" +
                            "  l_doc_inc_cnt NUMBER := 0;\n" +
                            "  FUNCTION get_vend_pidm(\n" +
                            "      vend_id VARCHAR2)\n" +
                            "    RETURN PLS_INTEGER\n" +
                            "  IS\n" +
                            "    gv_id     VARCHAR2 (12);\n" +
                            "    gv_pidm1  NUMBER := NULL;\n" +
                            "    gv_rowid1 VARCHAR2 (18);\n" +
                            "    lv_result VARCHAR2 (10);\n" +
                            "  BEGIN\n" +
                            "    SELECT spriden_id,\n" +
                            "      spriden_pidm\n" +
                            "    INTO gv_id,\n" +
                            "      gv_pidm1\n" +
                            "    FROM spriden\n" +
                            "    WHERE spriden_id        = vend_id\n" +
                            "    AND spriden_change_ind IS NULL\n" +
                            "    AND ROWNUM              =1;\n" +
                            "    RETURN gv_pidm1;\n" +
                            "  END;\n" +
                            "FUNCTION get_foapal(\n" +
                            "    cnt NUMBER)\n" +
                            "  RETURN BOOLEAN\n" +
                            "IS\n" +
                            "BEGIN\n" +
                            "  l_foap_code   := l_foap_code + 1;\n" +
                            "  IF l_foap_code > 80 THEN\n" +
                            "    l_foap_code := 21;\n" +
                            "  END IF;\n" +
                            "  L_FUND_CODE         := 'F' || TRIM(TO_CHAR(L_FOAP_CODE, '00099'));\n" +
                            "  l_orgn_code         := 'O' || TRIM(TO_CHAR(l_foap_code, '00099'));\n" +
                            "  l_acct_code         := 'A' || TRIM(TO_CHAR(l_foap_code, '00099'));\n" +
                            "  l_prog_code         := 'P' || TRIM(TO_CHAR(l_foap_code, '00099'));\n" +
                            "  IF mod(l_foap_code,2)< 1 THEN\n" +
                            "    l_actv_code       := 'T' || TRIM(TO_CHAR(l_foap_code, '00099'));\n" +
                            "    l_locn_code       := 'L' || TRIM(TO_CHAR(l_foap_code, '00099'));\n" +
                            "  ELSE\n" +
                            "    l_actv_code := NULL;\n" +
                            "    l_locn_code := NULL;\n" +
                            "  END IF;\n" +
                            "  RETURN TRUE;\n" +
                            "END;\n" +
                            "BEGIN\n" +
                            "  BEGIN\n" +
                            "    SELECT COUNT(1)\n" +
                            "    INTO exit_no\n" +
                            "    FROM FPBREQH\n" +
                            "    WHERE FPBREQH_CODE LIKE '%'\n" +
                            "      ||req_doc_num\n" +
                            "      ||'%';\n" +
                            "  EXCEPTION\n" +
                            "  WHEN NO_DATA_FOUND THEN\n" +
                            "    NULL;\n" +
                            "  END;\n" +
                            "  IF EXIT_NO > 0 THEN\n" +
                            "    RETURN;\n" +
                            "  END IF;\n" +
                            "  SELECT FOBSYSC_REQ_NSF_ON_OFF_IND\n" +
                            "  INTO EXisting_fobsysc\n" +
                            "  FROM FOBSYSC FINANCESYSTEMCONTROL\n" +
                            "  WHERE TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_EFF_DATE) <= sysdate\n" +
                            "  AND FINANCESYSTEMCONTROL.FOBSYSC_STATUS_IND         ='A'\n" +
                            "  AND (TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE) >= sysdate\n" +
                            "  OR FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE          IS NULL)\n" +
                            "  AND (FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE        IS NULL\n" +
                            "  OR TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE)    > sysdate);\n" +
                            "  IF EXisting_fobsysc                                 ='Y' THEN\n" +
                            "    UPDATE FOBSYSC FINANCESYSTEMCONTROL\n" +
                            "    SET FOBSYSC_REQ_NSF_ON_OFF_IND                      ='N'\n" +
                            "    WHERE TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_EFF_DATE) <= sysdate\n" +
                            "    AND FINANCESYSTEMCONTROL.FOBSYSC_STATUS_IND         ='A'\n" +
                            "    AND (TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE) >= sysdate\n" +
                            "    OR FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE          IS NULL)\n" +
                            "    AND (FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE        IS NULL\n" +
                            "    OR TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE)    > sysdate);\n" +
                            "  END IF;\n" +
                            "  l_start_date       := to_date('01-JUL-'||TO_CHAR(l_start_fsyr_code-1),'DD-MON-YYYY');\n" +
                            "  l_end_date         := to_date('30-JUN-'||TO_CHAR(l_end_fsyr_code),'DD-MON-YYYY');\n" +
                            "  l_d_increment      := l_start_date;\n" +
                            "  lv_complete_req    := 'N';\n" +
                            "  WHILE l_d_increment<=l_end_date\n" +
                            "  LOOP\n" +
                            "    lv_trans_date := l_d_increment;\n" +
                            "    FOR i IN 1..l_doc_cnt\n" +
                            "    LOOP\n" +
                            "      BEGIN\n" +
                            "        l_doc_inc_cnt         := l_doc_inc_cnt +1;\n" +
                            "        IF mod(l_doc_inc_cnt,2)=1 THEN\n" +
                            "          l_single_acctg_ind  := 'Y';\n" +
                            "        ELSE\n" +
                            "          l_single_acctg_ind := 'N';\n" +
                            "        END IF;\n" +
                            "        IF (mod(l_doc_inc_cnt,10)) =0 THEN\n" +
                            "          lv_vend_pidm            := get_vend_pidm('FTV00011');\n" +
                            "        ELSE\n" +
                            "          lv_vend_pidm := get_vend_pidm(REPLACE('FTV'||TO_CHAR(mod(l_doc_inc_cnt,10), '00099'),' ',''));\n" +
                            "        END IF;\n" +
                            "        REQ_DOC_NUM        := REPLACE(REQ_DOC_NUM||TO_CHAR(AUTO_INCREMENT_VAR, '0099'),' ','');\n" +
                            "        auto_increment_var := auto_increment_var                                                                                                                                                                                                                                                                                                                                                                                    +1;\n" +
                            "        FB_REQUISITION.P_CREATE_HEADER(P_CODE => REQ_DOC_NUM, P_ACTIVITY_DATE => sysdate, P_USER_ID => ?, P_REQH_DATE => LV_TRANS_DATE, P_TRANS_DATE => LV_TRANS_DATE, P_NAME => 'Finance User', P_PHONE_AREA => NULL, P_PHONE_NUM => 578791, P_PHONE_EXT => 1234567890, P_VEND_PIDM => LV_VEND_PIDM, P_ATYP_CODE => 'BU', P_ATYP_SEQ_NUM => '1', P_COAS_CODE => 'B', P_ORGN_CODE => '11001', P_REQD_DATE => LV_TRANS_DATE + 10, P_COMPLETE_IND => LV_COMPLETE_REQ, P_PRINT_IND => NULL, P_ENCUMB_IND => NULL, P_SUSP_IND => NULL, P_CANCEL_IND => NULL, P_CANCEL_DATE => NULL, P_POST_DATE => NULL, P_APPR_IND => NULL, P_TEXT_IND => NULL, P_EDIT_DEFER_IND => 'N', P_RECOMM_VEND_NAME => NULL, P_CURR_CODE => 'USD', P_NSF_ON_OFF_IND => NULL, P_SINGLE_ACCTG_IND => L_SINGLE_ACCTG_IND, P_CLOSED_IND => NULL, P_SHIP_CODE => 'BIO', P_RQST_TYPE_IND => 'P', P_INVENTORY_REQ_IND => 'N', P_CRSN_CODE => NULL, P_DELIVERY_COMMENT => NULL, P_EMAIL_ADDR => NULL, P_FAX_AREA =>NULL, P_FAX_NUMBER => NULL, P_FAX_EXT =>\n" +
                            "        '0987654321', p_attention_to => 'mjp', p_vendor_contact => NULL, p_disc_code => NULL, p_vend_email_addr => NULL, p_copied_from => NULL, p_tgrp_code => 'NT', p_req_print_date => NULL, p_closed_date => NULL, p_match_required => 'N', p_origin_code => 'GRAILS', p_doc_ref_code => NULL, p_print_text => NULL, p_noprint_text => NULL, p_ctry_code_phone => NULL, p_ctry_code_fax => NULL, p_rowid => lv_head_rowid);\n" +
                            "      END;\n" +
                            "      l_comm_total :=0;\n" +
                            "      FOR j IN 1..l_comm_cnt\n" +
                            "      LOOP\n" +
                            "        BEGIN\n" +
                            "          fb_requisition.p_create_item(p_reqh_code =>req_doc_num, p_item => j, p_activity_date => SYSDATE, p_user_id => ?, p_comm_code => NULL, p_comm_desc => 'Test Commodity - ' || J, p_coas_code => 'B', p_orgn_code => '11001', p_buyr_code => 'JFB', p_qty => j*4, p_uoms_code => 'EA', p_unit_price => j*2, p_agre_code => NULL, p_reqd_date => lv_trans_date + 10, p_ship_code => 'BIO', p_vend_pidm => '234310', p_vend_ref_num => NULL, p_proj_code => NULL, p_pohd_code => NULL, p_pohd_item => NULL, p_bids_code => NULL, p_complete_ind => NULL, p_susp_ind => NULL, p_cancel_ind =>NULL, p_cancel_date => NULL, p_closed_ind => NULL, p_post_date => NULL, p_text_usage => NULL, p_atyp_code => 'BU', p_atyp_seq_num => '1', p_recomm_vend_name => NULL, p_curr_code => 'USD', p_converted_unit_price => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL, p_convert_addl_chrg_amt => NULL, p_tgrp_code => 'PA', p_amt => (j*4)*(j*2), p_desc_chge_ind => 'N', p_print_text => NULL, p_noprint_text\n" +
                            "          => NULL, p_rowid => lv_comm_rowid);\n" +
                            "          l_comm_total := l_comm_total + ((j*2)*(j*4));\n" +
                            "        EXCEPTION\n" +
                            "        WHEN OTHERS THEN\n" +
                            "          p_error_msg_out := gb_common_strings.f_append_error(p_error_msg_out,ltrim(SQLERRM,1500));\n" +
                            "          lv_fatal_error  := true;\n" +
                            "        END;\n" +
                            "        IF l_single_acctg_ind <> 'Y' THEN\n" +
                            "          FOR k IN 1..l_acct_cnt\n" +
                            "          LOOP\n" +
                            "            BEGIN\n" +
                            "              l_foap_return := get_foapal(k);\n" +
                            "              lv_acct_amt   := l_comm_total / l_acct_cnt;\n" +
                            "              fb_requisition.p_create_accounting(p_reqh_code => req_doc_num, p_item => TO_CHAR(j), p_seq_num => TO_CHAR(k), p_activity_date => sysdate, p_user_id => ?, p_amt => lv_acct_amt, p_coas_code => 'B', p_acci_code => NULL, p_fund_code => l_fund_code, p_orgn_code => l_orgn_code, p_acct_code => l_acct_code, p_prog_code => l_prog_code, p_actv_code => l_actv_code, p_locn_code => l_locn_code, p_susp_ind => NULL, p_nsf_susp_ind => NULL, p_cancel_ind => NULL, p_cancel_date => NULL, p_proj_code => NULL, p_appr_ind => NULL, p_nsf_override_ind => NULL, p_abal_ind => NULL, p_converted_amt => NULL, p_closed_ind => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL, p_convert_addl_chrg_amt => NULL, p_tax_rucl_code => NULL, p_rucl_code_liq => NULL, p_rowid =>lv_acctg_rowid);\n" +
                            "            EXCEPTION\n" +
                            "            WHEN OTHERS THEN\n" +
                            "              p_error_msg_out := gb_common_strings.f_append_error(p_error_msg_out, ltrim(SQLERRM,1500));\n" +
                            "              lv_fatal_error  := true;\n" +
                            "            END;\n" +
                            "          END LOOP;\n" +
                            "          l_comm_total := 0;\n" +
                            "        END IF;\n" +
                            "      END LOOP;\n" +
                            "      IF l_single_acctg_ind = 'Y' THEN\n" +
                            "        FOR k IN 1..l_acct_cnt\n" +
                            "        LOOP\n" +
                            "          BEGIN\n" +
                            "            l_foap_return := get_foapal(k);\n" +
                            "            lv_acct_amt   := l_comm_total / l_acct_cnt;\n" +
                            "            fb_requisition.p_create_accounting(p_reqh_code => req_doc_num, p_item => '0', p_seq_num => TO_CHAR(k), p_activity_date => sysdate, p_user_id => ?, p_amt => lv_acct_amt, p_coas_code => 'B', p_acci_code => NULL, p_fund_code => l_fund_code, p_orgn_code => l_orgn_code, p_acct_code => l_acct_code, p_prog_code => l_prog_code, p_actv_code => l_actv_code, p_locn_code => l_locn_code, p_susp_ind => NULL, p_nsf_susp_ind => NULL, p_cancel_ind => NULL, p_cancel_date => NULL, p_proj_code => NULL, p_appr_ind => NULL, p_nsf_override_ind => NULL, p_abal_ind => NULL, p_converted_amt => NULL, p_closed_ind => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL, p_convert_addl_chrg_amt => NULL, p_tax_rucl_code => NULL, p_rucl_code_liq => NULL, p_rowid =>lv_acctg_rowid);\n" +
                            "          EXCEPTION\n" +
                            "          WHEN OTHERS THEN\n" +
                            "            p_error_msg_out := gb_common_strings.f_append_error(p_error_msg_out, ltrim(SQLERRM,1500));\n" +
                            "            lv_fatal_error  := true;\n" +
                            "          END;\n" +
                            "        END LOOP;\n" +
                            "        l_comm_total :=0;\n" +
                            "      END IF;\n" +
                            "      IF (NOT lv_fatal_error)THEN\n" +
                            "        BEGIN\n" +
                            "          LV_ERROR_MSG      := NULL;\n" +
                            "          p_success_msg_out := fb_requisition.f_complete_req(p_code => req_doc_num);\n" +
                            "        END;\n" +
                            "      END IF;\n" +
                            "      REQ_DOC_NUM :=?;----EXTERNAL\n" +
                            "    END LOOP;\n" +
                            "    l_d_increment := l_d_increment+lv_increment_days;\n" +
                            "  END LOOP;\n" +
                            "  UPDATE FOBSYSC FINANCESYSTEMCONTROL\n" +
                            "  SET FOBSYSC_REQ_NSF_ON_OFF_IND                      = EXisting_fobsysc\n" +
                            "  WHERE TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_EFF_DATE) <= sysdate\n" +
                            "  AND FINANCESYSTEMCONTROL.FOBSYSC_STATUS_IND         ='A'\n" +
                            "  AND (TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE) >= sysdate\n" +
                            "  OR FINANCESYSTEMCONTROL.FOBSYSC_TERM_DATE          IS NULL)\n" +
                            "  AND (FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE        IS NULL\n" +
                            "  OR TRUNC(FINANCESYSTEMCONTROL.FOBSYSC_NCHG_DATE)    > sysdate);\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setInt( 1, this.startFsyrCode )
            insertCall.setInt( 2, this.endFsyrCode )
            insertCall.setString( 3, this.documentNumber )

            insertCall.setString( 4, this.oracleUserName )
            insertCall.setString( 5, this.oracleUserName )
            insertCall.setString( 6, this.oracleUserName )
            insertCall.setString( 7, this.oracleUserName )

            insertCall.setString( 8, this.documentNumber )


            insertCall.execute()
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_REQUISITIONS", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_REQUISITIONS", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Requisitions" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
