/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability Create Purchase Order
 */
public class FinanceBudgetAvailabilityCreatePurchaseOrderDML {


    def startFsyrCode, endFsyrCode, documentNumber, oracleUserName
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityCreatePurchaseOrderDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityCreatePurchaseOrderDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populatePurchaseOrders()
    }


    def parseXmlData() {
        def journalVoucherData = new XmlParser().parseText( xmlData )
        this.startFsyrCode = journalVoucherData.START_FSYR_CODE.text() as int
        this.endFsyrCode = journalVoucherData.END_FSYR_CODE.text() as int
        this.documentNumber = journalVoucherData.DOCUMENT_NUMBER.text()
        this.oracleUserName = journalVoucherData.ORACLE_USER_NAME.text()
    }

    /**
     * Populate Purchase Orders
     */
    def populatePurchaseOrders() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  L_START_FSYR_CODE  NUMBER      := ?;--External\n" +
                            "  l_end_fsyr_code    NUMBER      := ?;--External\n" +
                            "  l_doc_cnt          NUMBER      := 10;  -- per day document count\n" +
                            "  l_comm_cnt         NUMBER      := 3;   -- per document commodity count\n" +
                            "  l_acct_cnt         NUMBER      := 3;   -- per document accounting count\n" +
                            "  l_single_acctg_ind VARCHAR2(1) := 'Y'; -- commodity/document accounting\n" +
                            "  lv_increment_days  NUMBER      := 10;  -- number of days to be incremented\n" +
                            "  l_foap_code        NUMBER      := 21;\n" +
                            "  l_comm_total       NUMBER      :=0;\n" +
                            "  EXISTING_FOBSYSC   VARCHAR2(1) :='N';\n" +
                            "  auto_increment_var NUMBER      :=1;\n" +
                            "  EXIT_NO            NUMBER      :=0;\n" +
                            "  lv_acct_amt        NUMBER      :=0;\n" +
                            "  l_start_date       DATE;\n" +
                            "  l_end_date         DATE;\n" +
                            "  l_d_increment      DATE;\n" +
                            "  lv_complete_po     VARCHAR2(1);\n" +
                            "  LV_TOT_DOC_CNT     NUMBER       :=0;\n" +
                            "  po_doc_num         VARCHAR2(10) := ?;--External\n" +
                            "  po_ch_seq_num      NUMBER       := NULL; -- for original\n" +
                            "  lv_trans_date      DATE;\n" +
                            "  lv_vend_pidm       NUMBER;\n" +
                            "  lv_head_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_comm_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_acctg_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_fatal_error  BOOLEAN :=FALSE;\n" +
                            "  p_error_msg_out VARCHAR2(2000);\n" +
                            "  p_success_msg_out string_nt;\n" +
                            "  lv_error_msg gb_common_strings.err_type;\n" +
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
                            "  l_fund_code         := 'FS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
                            "  l_orgn_code         := 'OS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
                            "  l_acct_code         := 'AS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
                            "  l_prog_code         := 'PS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
                            "  IF mod(l_foap_code,2)< 1 THEN\n" +
                            "    l_actv_code       := 'TS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
                            "    l_locn_code       := 'LS' || TRIM(TO_CHAR(l_foap_code, '0099'));\n" +
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
                            "    FROM FPBPOHD\n" +
                            "    WHERE FPBPOHD_CODE LIKE '%'\n" +
                            "      ||po_doc_num\n" +
                            "      ||'%';\n" +
                            "  EXCEPTION\n" +
                            "  WHEN NO_DATA_FOUND THEN\n" +
                            "    NULL;\n" +
                            "  END;\n" +
                            "  IF EXIT_NO > 0 THEN\n" +
                            "    RETURN;\n" +
                            "  END IF;\n" +
                            "  SELECT FOBSYSC_PO_NSF_ON_OFF_IND\n" +
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
                            "    SET FOBSYSC_PO_NSF_ON_OFF_IND                       ='N'\n" +
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
                            "  lv_complete_po     := 'N';\n" +
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
                            "        PO_DOC_NUM         := REPLACE(PO_DOC_NUM||TO_CHAR(AUTO_INCREMENT_VAR, '0099'),' ','');\n" +
                            "        auto_increment_var := auto_increment_var                                                                                                                                                                                                                                                                    +1;\n" +
                            "        fb_purchase_order.p_create_header(p_code => po_doc_num, p_change_seq_num => po_ch_seq_num, p_user_id => ?, p_po_date => lv_trans_date, p_trans_date => lv_trans_date, p_vendor_pidm => lv_vend_pidm, p_atyp_code => 'BU', p_atyp_seq_num =>'1', p_buyr_code => 'NCAP', p_reqd_date => lv_trans_date+10, p_coas_code => 'B', p_orgn_code => '11001', p_ship_code => 'BIO', p_disc_code => NULL, p_print_text => NULL, p_noprint_text => NULL, p_tgrp_code => 'PA', p_curr_code => 'USD', p_single_acctg_ind => l_single_acctg_ind, p_email_addr => NULL, p_fax_area => NULL, p_fax_number => NULL, p_fax_ext => '0987654321', p_attention_to => 'MJP', p_vendor_contact => NULL, p_delivery_comment => NULL, p_vend_email_addr => 'jack.sprague@am.apbiotech.com', p_name => ?, p_phone_area => NULL, p_phone_num => '5787917', p_phone_ext => '1234567890', p_edi_ind => NULL, p_ftrm_code => NULL, p_origin_code => 'BANNER', p_po_type_ind => 'R', p_doc_ref_code => NULL, p_blanket_code =>NULL,\n" +
                            "        p_blanket_ind => NULL, p_blanket_term_date => NULL, p_closed_ind => NULL, p_complete_ind => lv_complete_po, p_print_ind => NULL, p_po_printed_date => NULL, p_trat_code => NULL, p_fob_code => NULL, p_text_ind => NULL, p_clause_ind => NULL, p_carrier_pidm => NULL, p_trsk_code => NULL, p_pmnt_code => NULL, p_prt_ack => NULL, p_ack_date => NULL, p_appr_ind => lv_complete_po, p_rush_ind => NULL, p_rexp_date => NULL, p_addl_chrg_amt => 0, p_rush_amt => NULL, p_susp_ind => NULL, p_susp_ind_addl => NULL, p_cancel_ind => NULL, p_cancel_date => NULL, p_post_date => NULL, p_req_bid_ind => NULL, p_edit_defer_ind => 'N', p_pcls_code => NULL, p_recomm_vend_name => NULL, p_disb_agent_ind => NULL, p_nsf_on_off_ind => NULL, p_inventory_po_ind => 'N', p_doc_cntrl_ind => NULL, p_tele_code => NULL, p_crsn_code => NULL, p_copied_from => NULL, p_closed_date => NULL, p_match_required => 'Y', p_ctry_code_phone => NULL, p_ctry_code_fax =>NULL, p_rowid => lv_head_rowid);\n" +
                            "      END;\n" +
                            "      l_comm_total :=0;\n" +
                            "      FOR j IN 1..l_comm_cnt\n" +
                            "      LOOP\n" +
                            "        BEGIN\n" +
                            "          fb_purchase_order.p_create_item(p_pohd_code =>po_doc_num, p_change_seq_num => po_ch_seq_num, p_item => j, p_comm_code => 'C' || j, p_comm_desc => NULL, -- 'Test Commodity - ' || j,\n" +
                            "          p_uoms_code => 'EA', p_unit_price => j*5, p_qty => j*2, p_disc_amt => 0, p_tax_amt => 0, p_addl_chrg_amt => 0, p_tgrp_code => 'PA', p_ship_code => 'BIO', p_reqd_date => lv_trans_date+10, p_print_text => 'N', p_noprint_text => 'Test comment', p_liq_amt => 0, p_vend_ref_num => NULL, p_agre_code =>NULL, p_susp_ind => 'N', p_closed_ind => NULL, p_cancel_ind => NULL, p_cancel_date => NULL, p_ttag_num => NULL, p_text_usage =>'S', p_convert_unit_price => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL, p_convert_addl_chrg_amt => NULL, p_ext_amt => NULL, p_bo_remain_bal => NULL, p_desc_chge_ind => NULL, p_data_changed => lv_comm_rowid,p_REQH_CODE => NULL, p_REQD_ITEM => NULL, p_rowid =>lv_comm_rowid );\n" +
                            "          l_comm_total := l_comm_total          + ((j*2)*(j*5));\n" +
                            "        EXCEPTION\n" +
                            "        WHEN OTHERS THEN\n" +
                            "          p_error_msg_out := gb_common_strings.f_append_error(p_error_msg_out,ltrim(SQLERRM,1500));\n" +
                            "          lv_fatal_error  := true;\n" +
                            "        END;\n" +
                            "\n" +
                            "        IF l_single_acctg_ind <> 'Y' THEN\n" +
                            "          FOR k IN 1..l_acct_cnt\n" +
                            "          LOOP\n" +
                            "            BEGIN\n" +
                            "              lv_acct_amt   := l_comm_total / l_acct_cnt;\n" +
                            "              l_foap_return := get_foapal(k);\n" +
                            "              fb_purchase_order.p_create_accounting(p_pohd_code => po_doc_num, p_change_seq_num => po_ch_seq_num, P_ITEM => TO_CHAR(J), p_seq_num => k, p_amt => lv_acct_amt, p_disc_amt => NULL, p_tax_amt =>NULL, p_addl_chrg_amt => 0, p_coas_code => 'B', p_acci_code => NULL, p_fund_code => l_fund_code, p_orgn_code => l_orgn_code, p_acct_code => l_acct_code, p_prog_code => l_prog_code, p_actv_code => l_actv_code, p_locn_code => l_locn_code, p_proj_code => NULL, p_nsf_override_ind =>NULL, p_appr_amt_pct => NULL, p_disc_amt_pct => NULL, p_addl_amt_pct => NULL, p_tax_amt_pct => NULL, p_activity_date => NULL, p_user_id => ?, p_fsyr_code => NULL, p_period => NULL, p_rucl_code_po => NULL, p_disc_rucl_code => NULL, p_tax_rucl_code =>NULL, p_addl_rucl_code => NULL, p_rucl_code_liq => NULL, p_susp_ind => NULL, p_nsf_susp_ind => NULL, p_post_date => NULL, p_appr_ind => NULL, p_closed_ind => NULL, p_convert_amt => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL,\n" +
                            "              p_convert_addl_chrg_amt => NULL,  p_data_changed => NULL,p_reqh_code => NULL, p_reqd_item => NULL,p_reqa_seq =>NULL,p_rowid => lv_acctg_rowid);\n" +
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
                            "            lv_acct_amt   := l_comm_total / l_acct_cnt;\n" +
                            "            l_foap_return := get_foapal(k);\n" +
                            "            fb_purchase_order.p_create_accounting(p_pohd_code => po_doc_num, p_change_seq_num => NULL, p_item => '0', p_seq_num => k, p_amt => lv_acct_amt, p_disc_amt => NULL, p_tax_amt =>NULL, p_addl_chrg_amt => 0, p_coas_code => 'B', p_acci_code => NULL, p_fund_code => l_fund_code, p_orgn_code => l_orgn_code, p_acct_code => l_acct_code, p_prog_code => l_prog_code, p_actv_code => l_actv_code, p_locn_code => l_locn_code, p_proj_code => NULL, p_nsf_override_ind =>NULL, p_appr_amt_pct => NULL, p_disc_amt_pct => NULL, p_addl_amt_pct => NULL, p_tax_amt_pct => NULL, p_activity_date => NULL, p_user_id => ?, p_fsyr_code => NULL, p_period => NULL, p_rucl_code_po => NULL, p_disc_rucl_code => NULL, p_tax_rucl_code =>NULL, p_addl_rucl_code => NULL, p_rucl_code_liq => NULL, p_susp_ind => NULL, p_nsf_susp_ind => NULL, p_post_date => NULL, p_appr_ind => NULL, p_closed_ind => NULL, p_convert_amt => NULL, p_convert_disc_amt => NULL, p_convert_tax_amt => NULL, p_convert_addl_chrg_amt => NULL\n" +
                            "            , p_data_changed => NULL,p_reqh_code => NULL, p_reqd_item => NULL,p_reqa_seq =>NULL,p_rowid => lv_acctg_rowid);\n" +
                            "          EXCEPTION\n" +
                            "          WHEN OTHERS THEN\n" +
                            "            p_error_msg_out := gb_common_strings.f_append_error(p_error_msg_out, ltrim(SQLERRM,1500));\n" +
                            "            lv_fatal_error  := true;\n" +
                            "          END;\n" +
                            "        END LOOP;\n" +
                            "\n" +
                            "        l_comm_total :=0;\n" +
                            "      END IF;\n" +
                            "      -- Complete the PO and forward to next process\n" +
                            "      IF (NOT lv_fatal_error)THEN\n" +
                            "        BEGIN\n" +
                            "          lv_error_msg := NULL;\n" +
                            "          p_success_msg_out := fb_purchase_order.f_complete_order(po_doc_num);\n" +
                            "        END;\n" +
                            "      END IF;\n" +
                            "      PO_DOC_NUM := ?;--External\n" +
                            "    END LOOP;\n" +
                            "    l_d_increment := l_d_increment+lv_increment_days;\n" +
                            "  END LOOP;\n" +
                            "  UPDATE FOBSYSC FINANCESYSTEMCONTROL\n" +
                            "  SET FOBSYSC_PO_NSF_ON_OFF_IND                       = EXisting_fobsysc\n" +
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
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_PURCHASE_ORDERS", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_PURCHASE_ORDERS", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Purchase Orders" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
