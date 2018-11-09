package net.hedtech.banner.seeddata.Data.student.ar

import groovy.xml.MarkupBuilder

class tbraccdScript {

        static main(args) {
            def fileWriter = new FileWriter("StudentAccountsAccountChargePaymentDetail.xml")
            def transactionBuilder = new MarkupBuilder( fileWriter)
            transactionBuilder.feeddata {
                tbraccd {
                    BANNERID ('ARUSER2')
                            TBRACCD_TRAN_NUMBER(1)
                            TBRACCD_TERM_CODE(201410)
                            TBRACCD_DETAIL_CODE('T101')
                            TBRACCD_USER('GRAILS')
                            TBRACCD_ENTRY_DATE('08-MAR-14')
                            TBRACCD_AMOUNT(3000)
                            TBRACCD_BALANCE(3000)
                            TBRACCD_EFFECTIVE_DATE('08-MAR-14')
                            TBRACCD_BILL_DATE('19-JUL-14')
                            TBRACCD_DUE_DATE('18-AUG-14')
                            TBRACCD_DESC('Undergraduate Tuition')
                            TBRACCD_RECEIPT_NUMBER()
                            TBRACCD_TRAN_NUMBER_PAID()
                            TBRACCD_CROSSREF_PIDM()
                            TBRACCD_CROSSREF_NUMBER()
                            TBRACCD_CROSSREF_DETAIL_CODE()
                            TBRACCD_SRCE_CODE('R')
                            TBRACCD_ACCT_FEED_IND('Y')
                            TBRACCD_ACTIVITY_DATE('01-JAN-10')
                            TBRACCD_SESSION_NUMBER(0)
                            TBRACCD_CSHR_END_DATE()
                            TBRACCD_CRN()
                            TBRACCD_CROSSREF_SRCE_CODE()
                            TBRACCD_LOC_MDT()
                            TBRACCD_LOC_MDT_SEQ()
                            TBRACCD_RATE()
                            TBRACCD_UNITS()
                            TBRACCD_DOCUMENT_NUMBER()
                            TBRACCD_TRANS_DATE('04-MAR-14')
                            TBRACCD_PAYMENT_ID()
                            TBRACCD_INVOICE_NUMBER()
                            TBRACCD_STATEMENT_DATE()
                            TBRACCD_INV_NUMBER_PAID()
                            TBRACCD_CURR_CODE()
                            TBRACCD_EXCHANGE_DIFF()
                            TBRACCD_FOREIGN_AMOUNT()
                            TBRACCD_LATE_DCAT_CODE()
                            TBRACCD_FEED_DATE()
                            TBRACCD_FEED_DOC_CODE()
                            TBRACCD_ATYP_CODE()
                            TBRACCD_ATYP_SEQNO()
                            TBRACCD_CARD_TYPE_VR()
                            TBRACCD_CARD_EXP_DATE_VR()
                            TBRACCD_CARD_AUTH_NUMBER_VR()
                            TBRACCD_CROSSREF_DCAT_CODE()
                            TBRACCD_ORIG_CHG_IND('Y')
                            TBRACCD_CCRD_CODE()
                            TBRACCD_MERCHANT_ID()
                            TBRACCD_TAX_REPT_YEAR()
                            TBRACCD_TAX_REPT_BOX()
                            TBRACCD_TAX_AMOUNT()
                            TBRACCD_TAX_FUTURE_IND()
                            TBRACCD_CREATE_SOURCE()
                            TBRACCD_CPDT_IND()
                            TBRACCD_AIDY_CODE()
                            TBRACCD_STSP_KEY_SEQUENCE()
                            TBRACCD_DATA_ORIGIN('GRAILS')
                            TBRACCD_USER_ID('GRAILS')
                            TBRACCD_PERIOD()
                }
            }
            fileWriter.close();
        }

}
