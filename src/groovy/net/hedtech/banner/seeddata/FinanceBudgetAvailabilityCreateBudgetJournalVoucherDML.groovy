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
public class FinanceBudgetAvailabilityCreateBudgetJournalVoucherDML {


    def startFsyCode, endFsyCode, docCount, incrementDays, documentNumber, ruleCode, baseAmount, oracleUserName
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityCreateBudgetJournalVoucherDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityCreateBudgetJournalVoucherDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateJournalVoucher()
    }


    def parseXmlData() {
        def journalVoucherData = new XmlParser().parseText( xmlData )
        this.startFsyCode = journalVoucherData.START_FSY_CODE.text() as int
        this.endFsyCode = journalVoucherData.END_FSY_CODE.text() as int
        this.docCount = journalVoucherData.DOC_COUNT.text() as int
        this.incrementDays = journalVoucherData.INCREMENT_DAYS.text() as int
        this.documentNumber = journalVoucherData.DOCUMENT_NUMBER.text()
        this.ruleCode = journalVoucherData.RULE_CODE.text()
        this.baseAmount = journalVoucherData.BASE_AMOUNT.text() as double
        this.oracleUserName = journalVoucherData.ORACLE_USER_NAME.text()
    }

    /**
     * Populate Journal Voucher
     */
    def populateJournalVoucher() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  l_start_fsyr_code NUMBER       := ?;--2015; --EXTERNAL\n" +
                            "  l_end_fsyr_code   NUMBER       := ?;--2016; --EXTERNAL\n" +
                            "  l_doc_cnt         NUMBER       := ?;--2;    -- per day document count --EXTERNAL\n" +
                            "  p_user            VARCHAR2(30) := ?;--'FORSED21'; --EXTERNAL\n" +
                            "  l_doc_acct_cnt    NUMBER       :=40;     -- Accounting Distributions Per Document\n" +
                            "  lv_increment_days NUMBER       :=?;--30; EXTERNAL     -- Transaction Date Frequency\n" +
                            "  L_COMPLETE_IND    VARCHAR2(1)  := 'Y';   -- Document to be completed / to be in progress\n" +
                            "  jv_doc_num        VARCHAR2(10) := ?;--'XYZ'; --EXTERNAL\n" +
                            "  lv_trans_date     DATE;\n" +
                            "  lv_jv_seq_num     NUMBER :=0 ;\n" +
                            "  lv_jvh_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_jvd1_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_jvd2_rowid gb_common.internal_record_id_type;\n" +
                            "  lv_fatal_error  BOOLEAN :=FALSE;\n" +
                            "  p_error_msg_out VARCHAR2(2000);\n" +
                            "  p_success_msg_out gb_common_strings.err_type;\n" +
                            "  p_warn_msg_out gb_common_strings.err_type;\n" +
                            "  lv_error_msg gb_common_strings.err_type;\n" +
                            "  lv_rucl_code VARCHAR2(10) := ?;--'BD01'; --EXTERNAL\n" +
                            "  lv_status_ind fgbjvcd.fgbjvcd_status_ind%TYPE;\n" +
                            "  lv_dr_cr_ind       VARCHAR2(1);\n" +
                            "  LV_DOC_CNT         NUMBER :=0;\n" +
                            "  exit_no            NUMBER := 0;\n" +
                            "  l_jv_acct_code     VARCHAR2(10);\n" +
                            "  l_single_acctg_ind VARCHAR2(1) := 'Y';\n" +
                            "  l_comm_total       NUMBER      :=0;\n" +
                            "  lv_acct_amt        NUMBER      :=0;\n" +
                            "  l_start_date       DATE;\n" +
                            "  l_end_date         DATE;\n" +
                            "  l_d_increment      DATE;\n" +
                            "  lv_base_amt        NUMBER := ?;--323.23; --EXTERNAL\n" +
                            "  lv_tran_amt        NUMBER := 0;\n" +
                            "  lv_tot_tran_amt    NUMBER := 0;\n" +
                            "  l_fund_code        VARCHAR2(10);\n" +
                            "  l_orgn_code        VARCHAR2(10);\n" +
                            "  l_acct_code        VARCHAR2(10);\n" +
                            "  l_prog_code        VARCHAR2(10);\n" +
                            "  l_actv_code        VARCHAR2(10);\n" +
                            "  l_locn_code        VARCHAR2(10);\n" +
                            "  L_FOAP_RETURN      BOOLEAN;\n" +
                            "  auto_increment_var NUMBER :=1;\n" +
                            "  FUNCTION get_foapal(\n" +
                            "      cnt NUMBER,\n" +
                            "      k   NUMBER)\n" +
                            "    RETURN BOOLEAN\n" +
                            "  IS\n" +
                            "    foap_code NUMBER := MOD(CNT,100);\n" +
                            "    ACCT_CODE NUMBER := MOD(CNT,999);\n" +
                            "  BEGIN\n" +
                            "    l_fund_code       := 'FS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "    l_orgn_code       := 'OS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "    l_acct_code       := 'AS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "    l_prog_code       := 'PS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "    IF mod(foap_code,2)< 1 THEN\n" +
                            "      l_actv_code     := 'TS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "      l_locn_code     := 'LS' || TRIM(TO_CHAR(foap_code, '00099'));\n" +
                            "    ELSE\n" +
                            "      l_actv_code := NULL;\n" +
                            "      l_locn_code := NULL;\n" +
                            "    END IF;\n" +
                            "    RETURN TRUE;\n" +
                            "  END;\n" +
                            "  BEGIN\n" +
                            "    BEGIN\n" +
                            "      SELECT COUNT(1)\n" +
                            "      INTO exit_no\n" +
                            "      FROM FGBJVCH\n" +
                            "      WHERE fgbjvch_doc_num LIKE '%'\n" +
                            "        ||JV_DOC_NUM\n" +
                            "        ||'%'\n" +
                            "      AND FGBJVCH_SUBMISSION_NUMBER =0;\n" +
                            "      IF EXIT_NO                    > 0 THEN        \n" +
                            "        RETURN;\n" +
                            "      END IF;\n" +
                            "    EXCEPTION\n" +
                            "    WHEN NO_DATA_FOUND THEN\n" +
                            "      NULL;\n" +
                            "    END;\n" +
                            "    l_start_date      := to_date('01-JUL-'||TO_CHAR(l_start_fsyr_code-1),'DD-MON-YYYY');\n" +
                            "    l_end_date        := to_date('30-JUN-'||TO_CHAR(l_end_fsyr_code),'DD-MON-YYYY');\n" +
                            "    l_d_increment     := l_start_date;\n" +
                            "    WHILE l_d_increment<l_end_date\n" +
                            "    LOOP\n" +
                            "      lv_trans_date := l_d_increment;\n" +
                            "      lv_doc_cnt    := lv_doc_cnt +1;\n" +
                            "      FOR i                      IN 1..l_doc_cnt\n" +
                            "      LOOP\n" +
                            "        BEGIN\n" +
                            "          JV_DOC_NUM         := REPLACE(JV_DOC_NUM||TO_CHAR(AUTO_INCREMENT_VAR, '0099'),' ','');\n" +
                            "          auto_increment_var := auto_increment_var +1;\n" +
                            "          LV_JV_SEQ_NUM :=0 ;\n" +
                            "          fb_jv_header.p_create( p_doc_num_in_out => jv_doc_num, P_SUBMISSION_NUMBER => 0, p_user_id => p_user, p_trans_date => lv_trans_date, p_doc_description => 'Expenses Budget Load ', p_doc_amt => 1, -- Updated at the end of process with more values\n" +
                            "          p_obud_code => NULL, p_obph_code => NULL, p_budg_dur_code => NULL, p_edit_defer_ind => 'N', p_status_ind => 'I', p_approval_ind => 'N', p_data_origin => 'GRAILS', p_rowid_out => lv_jvh_rowid);\n" +
                            "          lv_tot_tran_amt :=0;\n" +
                            "          BEGIN\n" +
                            "            FOR k IN 1..l_doc_acct_cnt\n" +
                            "            LOOP\n" +
                            "              BEGIN\n" +
                            "                l_foap_return := get_foapal(k *i, k);\n" +
                            "                lv_jv_seq_num := lv_jv_seq_num+1;\n" +
                            "                lv_dr_cr_ind  := '+';\n" +
                            "                lv_rucl_code  := 'BD01';\n" +
                            "                lv_tran_amt   := k * lv_base_amt * 50;\n" +
                            "                FB_JV_DETAIL.P_CREATE( p_doc_num => jv_doc_num, p_submission_number => 0, P_SEQ_NUM => LV_JV_SEQ_NUM, p_user_id => p_user, p_rucl_code => lv_rucl_code, p_trans_amt => lv_tran_amt, p_trans_desc => 'Expenses Budget Load - ' || lv_jv_seq_num, p_dr_cr_ind => lv_dr_cr_ind, p_acci_code => NULL, p_coas_code => 'B', p_fund_code => l_fund_code, p_orgn_code => l_orgn_code, p_acct_code => l_acct_code, p_prog_code => l_prog_code, p_actv_code => l_actv_code, p_locn_code => l_locn_code, p_bank_code => 'A1', p_doc_ref_num => NULL, p_vendor_pidm => NULL, p_encb_num => NULL, p_encd_item_num => NULL, p_encd_seq_num => NULL, p_encb_type => NULL, p_bud_dispn => NULL, p_bud_id => NULL, p_cmt_type => 'U', -- Uncommitted\n" +
                            "                p_cmt_pct => NULL, p_dep_num => NULL, p_encb_action_ind => NULL, p_prjd_code => NULL, p_dist_pct => NULL, p_budget_period => '01', p_accrual_ind => NULL, p_status_in_out => lv_status_ind, p_abal_override => 'N', p_appr_ind => 'N', p_gift_date => NULL, p_data_origin => 'GRAILS', p_warning_out => p_warn_msg_out, p_abal_severity_out => p_success_msg_out, p_rowid_out => lv_jvd1_rowid);\n" +
                            "                LV_TOT_TRAN_AMT := LV_TOT_TRAN_AMT + LV_TRAN_AMT;\n" +
                            "              END;\n" +
                            "            END LOOP;\n" +
                            "          END;\n" +
                            "          fb_jv_header.p_update(p_doc_num => jv_doc_num, P_SUBMISSION_NUMBER => 0, p_user_id =>p_user, p_trans_date => lv_trans_date, p_doc_description => 'Expenses Budget - ' ||lv_jv_seq_num , p_doc_amt => lv_tot_tran_amt, p_auto_jrnl_id => NULL, p_reversal_ind => NULL, p_obud_code => NULL, p_obph_code => NULL, p_budg_dur_code => NULL, p_edit_defer_ind => 'N', p_status_ind => 'I', p_approval_ind => 'N', p_distrib_amt => lv_tot_tran_amt, p_nsf_on_off_ind => NULL, p_data_origin => 'GRAILS', p_rowid => lv_jvh_rowid);\n" +
                            "          -- Complete the JV document\n" +
                            "          FP_JOURNAL_VOUCHER.P_COMPLETE( P_DOC_NUM => JV_DOC_NUM, P_SUBMISSION_NUMBER => 0, P_COMPLETE_REQUESTED => L_COMPLETE_IND, P_USER_ID => P_USER, P_MSG_OUT => LV_ERROR_MSG);\n" +
                            "          COMMIT;\n" +
                            "          JV_DOC_NUM :=?;\n" +
                            "        END;\n" +
                            "      END LOOP;\n" +
                            "      l_d_increment := l_d_increment+lv_increment_days;\n" +
                            "    END LOOP;\n" +
                            "  END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setInt( 1, this.startFsyCode )
            insertCall.setInt( 2, this.endFsyCode )
            insertCall.setInt( 3, this.docCount )
            insertCall.setString( 4, this.oracleUserName )
            insertCall.setInt( 5, this.incrementDays )
            insertCall.setString( 6, this.documentNumber )
            insertCall.setString( 7, this.ruleCode )
            insertCall.setDouble( 8, this.baseAmount )
            insertCall.setString( 9, this.documentNumber )

            insertCall.execute()
            connectInfo.tableUpdate( "BA_JOURNAL_VOUCHER", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BA_JOURNAL_VOUCHER", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Journal Voucher" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
