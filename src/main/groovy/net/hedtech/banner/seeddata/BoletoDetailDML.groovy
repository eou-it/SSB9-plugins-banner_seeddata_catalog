/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRBDTL DML tables
 *
 */

public class BoletoDetailDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrbdtlBoletoNumber
    def tvrbdtlBoletoVersion
    def tvrbdtlSequenceNumber
    def tvrbdtlPidm
    def tvrbdtlTranNumber
    def tvrbdtlTermCode
    def tvrbdtlDetailCode
    def tvrbdtlUserId
    def tvrbdtlEntryDate
    def tvrbdtlAmount
    def tvrbdtlBalance
    def tvrbdtlEffectiveDate
    def tvrbdtlSrceCode
    def tvrbdtlBillDate
    def tvrbdtlReceiptNumber
    def tvrbdtlTranNumberPaid
    def tvrbdtlCrossrefpidm
    def tvrbdtlCrossrefNumber
    def tvrbdtlCrossrefDetailCode
    def tvrbdtlCshrEndDate
    def tvrbdtlCrn
    def tvrbdtlCrossrefsrceCode
    def tvrbdtlLocMDt
    def tvrbdtlLocMDtSeq
    def tvrbdtlRate
    def tvrbdtlDocumentNumber
    def tvrbdtlTransDate
    def tvrbdtlPaymentId
    def tvrbdtlInvoiceNumber
    def tvrbdtlSessionNumber
    def tvrbdtlActivityDate
    def tvrbdtlDueDate
    def tvrbdtlDesc
    def tvrbdtlUnits
    def tvrbdtlAcctFeedInd
    def tvrbdtlStatementDate
    def tvrbdtlInvNumberPaid
    def tvrbdtlCurrCode
    def tvrbdtlExchangeDiff
    def tvrbdtlForeignAmount
    def tvrbdtlLateDcatCode
    def tvrbdtlFeedDate
    def tvrbdtlFeedDocCode
    def tvrbdtlAtypCode
    def tvrbdtlAtypSeqno
    def tvrbdtlCardTypeVR
    def tvrbdtlCardExpDateVR
    def tvrbdtlCardAuthNumberVR
    def tvrbdtlCrossrefDcatCode
    def tvrbdtlOrigChgInd
    def tvrbdtlCcrdCode
    def tvrbdtlMerchantId
    def tvrbdtlTaxReptYear
    def tvrbdtlTaxReptBox
    def tvrbdtlTaxAmount
    def tvrbdtlTaxFutureInd
    def tvrbdtlDataOrigin
    def tvrbdtlCreateSource

    public BoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public BoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRBDTL()
    }

    def processTVRBDTL() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrbdtlBoletoNumber = newPage.TVRBDTL_BOLETO_NUMBER.text()
            this.tvrbdtlBoletoNumber = fetchBoletoNumber()
            if (newPage.TVRBDTL_BOLETO_VERSION?.text()) {
                this.tvrbdtlBoletoVersion = newPage.TVRBDTL_BOLETO_VERSION.text()
            }
            if (newPage.TVRBDTL_SEQUENCE_NUMBER?.text()) {
                this.tvrbdtlSequenceNumber = newPage.TVRBDTL_SEQUENCE_NUMBER.text()
            }
            if (newPage.TVRBDTL_TRAN_NUMBER?.text()) {
                this.tvrbdtlTranNumber = newPage.TVRBDTL_TRAN_NUMBER.text()
            }
            if (newPage.TVRBDTL_PIDM?.text()) {
                this.tvrbdtlPidm = newPage.TVRBDTL_PIDM.text()
            }
            if (newPage.TVRBDTL_TERM_CODE?.text()) {
                this.tvrbdtlTermCode = newPage.TVRBDTL_TERM_CODE.text()
            }
            if (newPage.TVRBDTL_DETAIL_CODE?.text()) {
                this.tvrbdtlDetailCode = newPage.TVRBDTL_DETAIL_CODE.text()
            }
            if (newPage.TVRBDTL_ENTRY_DATE?.text()) {
                this.tvrbdtlEntryDate = newPage.TVRBDTL_ENTRY_DATE.text()
            }
            if (newPage.TVRBDTL_USER_ID?.text()) {
                this.tvrbdtlUserId = newPage.TVRBDTL_USER_ID.text()
            }
            if (newPage.TVRBDTL_AMOUNT?.text()) {
                this.tvrbdtlAmount = newPage.TVRBDTL_AMOUNT.text()
            }
            if (newPage.TVRBDTL_BALANCE?.text()) {
                this.tvrbdtlBalance = newPage.TVRBDTL_BALANCE.text()
            }
            if (newPage.TVRBDTL_EFFECTIVE_DATE?.text()) {
                this.tvrbdtlEffectiveDate = newPage.TVRBDTL_EFFECTIVE_DATE.text()
            }
            if (newPage.TVRBDTL_BILL_DATE?.text()) {
                this.tvrbdtlBillDate = newPage.TVRBDTL_BILL_DATE.text()
            }
            if (newPage.TVRBDTL_SRCE_CODE?.text()) {
                this.tvrbdtlSrceCode = newPage.TVRBDTL_SRCE_CODE.text()
            }
            if (newPage.TVRBDTL_RECEIPT_NUMBER?.text()) {
                this.tvrbdtlReceiptNumber = newPage.TVRBDTL_RECEIPT_NUMBER.text()
            }
            if (newPage.TVRBDTL_TRAN_NUMBER_PAID?.text()) {
                this.tvrbdtlTranNumberPaid = newPage.TVRBDTL_TRAN_NUMBER_PAID.text()
            }
            if (newPage.TVRBDTL_CROSSREF_PIDM?.text()) {
                this.tvrbdtlCrossrefpidm = newPage.TVRBDTL_CROSSREF_PIDM.text()
            }
            if (newPage.TVRBDTL_CROSSREF_NUMBER?.text()) {
                this.tvrbdtlCrossrefNumber = newPage.TVRBDTL_CROSSREF_NUMBER.text()
            }
            if (newPage.TVRBDTL_CROSSREF_DETAIL_CODE?.text()) {
                this.tvrbdtlCrossrefDetailCode = newPage.TVRBDTL_CROSSREF_DETAIL_CODE.text()
            }
            if (newPage.TVRBDTL_ACCT_FEED_IND?.text()) {
                this.tvrbdtlAcctFeedInd = newPage.TVRBDTL_ACCT_FEED_IND.text()
            }
            if (newPage.TVRBDTL_ACTIVITY_DATE?.text()) {
                this.tvrbdtlActivityDate = newPage.TVRBDTL_ACTIVITY_DATE.text()
            }
            if (newPage.TVRBDTL_SESSION_NUMBER?.text()) {
                this.tvrbdtlSessionNumber = newPage.TVRBDTL_SESSION_NUMBER.text()
            }
            if (newPage.TVRBDTL_TRANS_DATE?.text()) {
                this.tvrbdtlTransDate = newPage.TVRBDTL_TRANS_DATE.text()
            }
            if (newPage.TVRBDTL_CSHR_END_DATE?.text()) {
                this.tvrbdtlCshrEndDate = newPage.TVRBDTL_CSHR_END_DATE.text()
            }
            if (newPage.TVRBDTL_CRN?.text()) {
                this.tvrbdtlCrn = newPage.TVRBDTL_CRN.text()
            }
            if (newPage.TVRBDTL_CROSSREF_SRCE_CODE?.text()) {
                this.tvrbdtlCrossrefsrceCode = newPage.TVRBDTL_CROSSREF_SRCE_CODE.text()
            }
            if (newPage.TVRBDTL_LOC_MDT?.text()) {
                this.tvrbdtlLocMDt = newPage.TVRBDTL_LOC_MDT.text()
            }
            if (newPage.TVRBDTL_LOC_MDT_SEQ?.text()) {
                this.tvrbdtlLocMDtSeq = newPage.TVRBDTL_LOC_MDT_SEQ.text()
            }
            if (newPage.TVRBDTL_RATE?.text()) {
                this.tvrbdtlRate = newPage.TVRBDTL_RATE.text()
            }
            if (newPage.TVRBDTL_UNITS?.text()) {
                this.tvrbdtlUnits = newPage.TVRBDTL_UNITS.text()
            }
            if (newPage.TVRBDTL_DOCUMENT_NUMBER?.text()) {
                this.tvrbdtlDocumentNumber = newPage.TVRBDTL_DOCUMENT_NUMBER.text()
            }
            if (newPage.TVRBDTL_PAYMENT_ID?.text()) {
                this.tvrbdtlPaymentId = newPage.TVRBDTL_PAYMENT_ID.text()
            }
            if (newPage.TVRBDTL_INVOICE_NUMBER?.text()) {
                this.tvrbdtlInvoiceNumber = newPage.TVRBDTL_INVOICE_NUMBER.text()
            }
            if (newPage.TVRBDTL_STATEMENT_DATE?.text()) {
                this.tvrbdtlStatementDate = newPage.TVRBDTL_STATEMENT_DATE.text()
            }
            if (newPage.TVRBDTL_INV_NUMBER_PAID?.text()) {
                this.tvrbdtlInvNumberPaid = newPage.TVRBDTL_INV_NUMBER_PAID.text()
            }
            if (newPage.TVRBDTL_CURR_CODE?.text()) {
                this.tvrbdtlCurrCode = newPage.TVRBDTL_CURR_CODE.text()
            }
            if (newPage.TVRBDTL_EXCHANGE_DIFF?.text()) {
                this.tvrbdtlExchangeDiff = newPage.TVRBDTL_EXCHANGE_DIFF.text()
            }
            if (newPage.TVRBDTL_FOREIGN_AMOUNT?.text()) {
                this.tvrbdtlForeignAmount = newPage.TVRBDTL_FOREIGN_AMOUNT.text()
            }
            if (newPage.TVRBDTL_LATE_DCAT_CODE?.text()) {
                this.tvrbdtlLateDcatCode = newPage.TVRBDTL_LATE_DCAT_CODE.text()
            }
            if (newPage.TVRBDTL_FEED_DATE?.text()) {
                this.tvrbdtlFeedDate = newPage.TVRBDTL_FEED_DATE.text()
            }
            if (newPage.TVRBDTL_FEED_DOC_CODE?.text()) {
                this.tvrbdtlFeedDocCode = newPage.TVRBDTL_FEED_DOC_CODE.text()
            }
            if (newPage.TVRBDTL_ATYP_CODE?.text()) {
                this.tvrbdtlAtypCode = newPage.TVRBDTL_ATYP_CODE.text()
            }
            if (newPage.TVRBDTL_ATYP_SEQNO?.text()) {
                this.tvrbdtlAtypSeqno = newPage.TVRBDTL_ATYP_SEQNO.text()
            }
            if (newPage.TVRBDTL_CARD_TYPE_VR?.text()) {
                this.tvrbdtlCardTypeVR = newPage.TVRBDTL_CARD_TYPE_VR.text()
            }
            if (newPage.TVRBDTL_CARD_EXP_DATE_VR?.text()) {
                this.tvrbdtlCardExpDateVR = newPage.TVRBDTL_CARD_EXP_DATE_VR.text()
            }
            if (newPage.TVRBDTL_CARD_AUTH_NUMBER_VR?.text()) {
                this.tvrbdtlCardAuthNumberVR = newPage.TVRBDTL_CARD_AUTH_NUMBER_VR.text()
            }
            if (newPage.TVRBDTL_CROSSREF_DCAT_CODE?.text()) {
                this.tvrbdtlCrossrefDcatCode = newPage.TVRBDTL_CROSSREF_DCAT_CODE.text()
            }
            if (newPage.TVRBDTL_ORIG_CHG_IND?.text()) {
                this.tvrbdtlOrigChgInd = newPage.TVRBDTL_ORIG_CHG_IND.text()
            }
            if (newPage.TVRBDTL_CCRD_CODE?.text()) {
                this.tvrbdtlCcrdCode = newPage.TVRBDTL_CCRD_CODE.text()
            }
            if (newPage.TVRBDTL_MERCHANT_ID?.text()) {
                this.tvrbdtlMerchantId = newPage.TVRBDTL_MERCHANT_ID.text()
            }
            if (newPage.TVRBDTL_TAX_REPT_YEAR?.text()) {
                this.tvrbdtlTaxReptYear = newPage.TVRBDTL_TAX_REPT_YEAR.text()
            }
            if (newPage.TVRBDTL_TAX_REPT_BOX?.text()) {
                this.tvrbdtlTaxReptBox = newPage.TVRBDTL_TAX_REPT_BOX.text()
            }
            if (newPage.TVRBDTL_TAX_AMOUNT?.text()) {
                this.tvrbdtlTaxAmount = newPage.TVRBDTL_TAX_AMOUNT.text()
            }
            if (newPage.TVRBDTL_TAX_FUTURE_IND?.text()) {
                this.tvrbdtlTaxFutureInd = newPage.TVRBDTL_TAX_FUTURE_IND.text()
            }
            if (newPage.TVRBDTL_DATA_ORIGIN?.text()) {
                this.tvrbdtlDataOrigin = newPage.TVRBDTL_DATA_ORIGIN.text()
            }
            if (newPage.TVRBDTL_CREATE_SOURCE?.text()) {
                this.tvrbdtlCreateSource = newPage.TVRBDTL_CREATE_SOURCE.text()
            }
            if (newPage.TVRBDTL_USER_ID?.text()) {
                this.tvrbdtlUserId = newPage.TVRBDTL_USER_ID.text()
            }
            if (newPage.TVRBDTL_DESC?.text()) {
                this.tvrbdtlDesc = newPage.TVRBDTL_DESC.text()
            }
            if (newPage.TVRBDTL_DUE_DATE?.text()) {
                this.tvrbdtlDueDate = newPage.TVRBDTL_DUE_DATE.text()
            }

            createTVRBDTLObject()
        }
    }

    private def createTVRBDTLObject() {
        def sql = """insert into TVRBDTL (TVRBDTL_BOLETO_NUMBER,TVRBDTL_BOLETO_VERSION,TVRBDTL_SEQUENCE_NUMBER,TVRBDTL_PIDM,TVRBDTL_TRAN_NUMBER,TVRBDTL_TERM_CODE,TVRBDTL_DETAIL_CODE,TVRBDTL_USER_ID ,TVRBDTL_ENTRY_DATE,TVRBDTL_AMOUNT,TVRBDTL_BALANCE,TVRBDTL_EFFECTIVE_DATE,TVRBDTL_SRCE_CODE,TVRBDTL_BILL_DATE,TVRBDTL_RECEIPT_NUMBER,TVRBDTL_TRAN_NUMBER_PAID,TVRBDTL_CROSSREF_PIDM,TVRBDTL_CROSSREF_NUMBER,TVRBDTL_CROSSREF_DETAIL_CODE,TVRBDTL_CSHR_END_DATE,TVRBDTL_CRN,TVRBDTL_CROSSREF_SRCE_CODE,TVRBDTL_LOC_MDT,TVRBDTL_LOC_MDT_SEQ,TVRBDTL_RATE,TVRBDTL_DOCUMENT_NUMBER,TVRBDTL_TRANS_DATE,TVRBDTL_PAYMENT_ID,TVRBDTL_INVOICE_NUMBER,TVRBDTL_SESSION_NUMBER,TVRBDTL_ACTIVITY_DATE,TVRBDTL_DUE_DATE,TVRBDTL_DESC,TVRBDTL_UNITS,TVRBDTL_ACCT_FEED_IND,TVRBDTL_STATEMENT_DATE,TVRBDTL_INV_NUMBER_PAID,TVRBDTL_CURR_CODE,TVRBDTL_EXCHANGE_DIFF,TVRBDTL_FOREIGN_AMOUNT,TVRBDTL_LATE_DCAT_CODE,TVRBDTL_FEED_DATE,TVRBDTL_FEED_DOC_CODE,TVRBDTL_ATYP_CODE,TVRBDTL_ATYP_SEQNO,TVRBDTL_CARD_TYPE_VR,TVRBDTL_CARD_EXP_DATE_VR,TVRBDTL_CARD_AUTH_NUMBER_VR,TVRBDTL_CROSSREF_DCAT_CODE,TVRBDTL_ORIG_CHG_IND,TVRBDTL_CCRD_CODE,TVRBDTL_MERCHANT_ID,TVRBDTL_TAX_REPT_YEAR,TVRBDTL_TAX_REPT_BOX,TVRBDTL_TAX_AMOUNT,TVRBDTL_TAX_FUTURE_IND,TVRBDTL_DATA_ORIGIN,TVRBDTL_CREATE_SOURCE ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [tvrbdtlBoletoNumber, tvrbdtlBoletoVersion, tvrbdtlSequenceNumber, tvrbdtlPidm, tvrbdtlTranNumber, tvrbdtlTermCode, tvrbdtlDetailCode, tvrbdtlUserId, tvrbdtlEntryDate, tvrbdtlAmount, tvrbdtlBalance, tvrbdtlEffectiveDate, tvrbdtlSrceCode, tvrbdtlBillDate, tvrbdtlReceiptNumber, tvrbdtlTranNumberPaid, tvrbdtlCrossrefpidm, tvrbdtlCrossrefNumber, tvrbdtlCrossrefDetailCode, tvrbdtlCshrEndDate, tvrbdtlCrn, tvrbdtlCrossrefsrceCode, tvrbdtlLocMDt, tvrbdtlLocMDtSeq, tvrbdtlRate, tvrbdtlDocumentNumber, tvrbdtlTransDate, tvrbdtlPaymentId, tvrbdtlInvoiceNumber, tvrbdtlSessionNumber, tvrbdtlActivityDate, tvrbdtlDueDate, tvrbdtlDesc, tvrbdtlUnits, tvrbdtlAcctFeedInd, tvrbdtlStatementDate, tvrbdtlInvNumberPaid, tvrbdtlCurrCode, tvrbdtlExchangeDiff, tvrbdtlForeignAmount, tvrbdtlLateDcatCode, tvrbdtlFeedDate, tvrbdtlFeedDocCode, tvrbdtlAtypCode, tvrbdtlAtypSeqno, tvrbdtlCardTypeVR, tvrbdtlCardExpDateVR, tvrbdtlCardAuthNumberVR, tvrbdtlCrossrefDcatCode, tvrbdtlOrigChgInd, tvrbdtlCcrdCode, tvrbdtlMerchantId, tvrbdtlTaxReptYear, tvrbdtlTaxReptBox, tvrbdtlTaxAmount, tvrbdtlTaxFutureInd, tvrbdtlDataOrigin, tvrbdtlCreateSource])
            connectInfo.tableUpdate('TVRBDTL', 0, 1, 0, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not create access to object,  $e.message"
            }
        }
    }

    private def fetchBoletoNumber() {
        def tbn
        try {
            String crnsql = "SELECT TVBBHDR_BOLETO_NUMBER tbn FROM TVBBHDR WHERE TVBBHDR_COMMENT = ?"
            conn.eachRow(crnsql, [this.tvrbdtlBoletoNumber.toString()]) {
                tbn = it.tbn
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not boleto number from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not boleto number from TVBBHDR  ${connectInfo.tableName}.")
        return tbn
    }


}
