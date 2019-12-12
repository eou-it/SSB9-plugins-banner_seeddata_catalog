/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRTACD DML tables
 *
 */

public class TbraccdAccountDetailDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tbraccdAcctFeedInd
    def tbraccdActivityDate
    def tbraccdAidyCode
    def tbraccdAmount
    def tbraccdAtypCode
    def tbraccdAtypSeqno
    def tbraccdBalance
    def tbraccdBillDate
    def tbraccdCardAuthNumberVr
    def tbraccdCardExpDateVr
    def tbraccdCardTypeVr
    def tbraccdCcrdCode
    def tbraccdCpdtInd
    def tbraccdCreateSource
    def tbraccdCrn
    def tbraccdCrossrefDcatCode
    def tbraccdCrossrefDetailCode
    def tbraccdCrossrefNumber
    def tbraccdCrossrefPidm
    def tbraccdCrossrefSrceCode
    def tbraccdCshrEndDate
    def tbraccdCurrCode
    def tbraccdDataOrigin
    def tbraccdDesc
    def tbraccdDetailCode
    def tbraccdDocumentNumber
    def tbraccdDueDate
    def tbraccdEffectiveDate
    def tbraccdEntryDate
    def tbraccdExchangeDiff
    def tbraccdFeedDate
    def tbraccdFeedDocCode
    def tbraccdForeignAmount
    def tbraccdInvoiceNumber
    def tbraccdInvNumberPaid
    def tbraccdLateDcatCode
    def tbraccdLocMdt
    def tbraccdLocMdtSeq
    def tbraccdMerchantId
    def tbraccdOrigChgInd
    def tbraccdPaymentId
    def tbraccdPeriod
    def tbraccdPidm
    def tbraccdRate
    def tbraccdReceiptNumber
    def tbraccdSessionNumber
    def tbraccdSrceCode
    def tbraccdStatementDate
    def tbraccdStspKeySequence
    def tbraccdSurrogateId
    def tbraccdTaxAmount
    def tbraccdTaxFutureInd
    def tbraccdTaxFutureIndPr
    def tbraccdTaxFutureIndSg
    def tbraccdTaxReptBox
    def tbraccdTaxReptBoxPr
    def tbraccdTaxReptBoxSg
    def tbraccdTaxReptYear
    def tbraccdTermCode
    def tbraccdTransDate
    def tbraccdTranNumber
    def tbraccdTranNumberPaid
    def tbraccdUnits
    def tbraccdUser
    def tbraccdUserId
    def tbraccdVersion
    def tbraccdVpdiCode

    public TbraccdAccountDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TbraccdAccountDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTBRACCD()
    }

    def processTBRACCD() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tbraccdPidm = fetchUserPidm(newPage.BANNERID.text())
            if (newPage.TBRACCD_ACCT_FEED_IND?.text()) {
                this.tbraccdAcctFeedInd = newPage.TBRACCD_ACCT_FEED_IND.text()
            }
            if (newPage.TBRACCD_ACTIVITY_DATE?.text()) {
                this.tbraccdActivityDate = newPage.TBRACCD_ACTIVITY_DATE.text()
            }
            if (newPage.TBRACCD_AIDY_CODE?.text()) {
                this.tbraccdAidyCode = newPage.TBRACCD_AIDY_CODE.text()
            }
            if (newPage.TBRACCD_CURR_CODE?.text()) {
                this.tbraccdCurrCode = newPage.TBRACCD_CURR_CODE.text()
            }
            if (newPage.TBRACCD_CSHR_END_DATE?.text()) {
                this.tbraccdCshrEndDate = newPage.TBRACCD_CSHR_END_DATE.text()
            }
            if (newPage.TBRACCD_CROSSREF_SRCE_CODE?.text()) {
                this.tbraccdCrossrefSrceCode = newPage.TBRACCD_CROSSREF_SRCE_CODE.text()
            }
            if (newPage.TBRACCD_CROSSREF_PIDM?.text()) {
                this.tbraccdCrossrefPidm = newPage.TBRACCD_CROSSREF_PIDM.text()
            }
            if (newPage.TBRACCD_CROSSREF_NUMBER?.text()) {
                this.tbraccdCrossrefNumber = newPage.TBRACCD_CROSSREF_NUMBER.text()
            }
            if (newPage.TBRACCD_CROSSREF_DETAIL_CODE?.text()) {
                this.tbraccdCrossrefDetailCode = newPage.TBRACCD_CROSSREF_DETAIL_CODE.text()
            }
            if (newPage.TBRACCD_CROSSREF_DCAT_CODE?.text()) {
                this.tbraccdCrossrefDcatCode = newPage.TBRACCD_CROSSREF_DCAT_CODE.text()
            }
            if (newPage.TBRACCD_CRN?.text()) {
                this.tbraccdCrn = newPage.TBRACCD_CRN.text()
            }
            if (newPage.TBRACCD_CREATE_SOURCE?.text()) {
                this.tbraccdCreateSource = newPage.TBRACCD_CREATE_SOURCE.text()
            }
            if (newPage.TBRACCD_AMOUNT?.text()) {
                this.tbraccdAmount = newPage.TBRACCD_AMOUNT.text()
            }
            if (newPage.TBRACCD_CPDT_IND?.text()) {
                this.tbraccdCpdtInd = newPage.TBRACCD_CPDT_IND.text()
            }
            if (newPage.TBRACCD_CCRD_CODE?.text()) {
                this.tbraccdCcrdCode = newPage.TBRACCD_CCRD_CODE.text()
            }
            if (newPage.TBRACCD_CARD_TYPE_VR?.text()) {
                this.tbraccdCardTypeVr = newPage.TBRACCD_CARD_TYPE_VR.text()
            }
            if (newPage.TBRACCD_CARD_EXP_DATE_VR?.text()) {
                this.tbraccdCardExpDateVr = newPage.TBRACCD_CARD_EXP_DATE_VR.text()
            }
            if (newPage.TBRACCD_CARD_AUTH_NUMBER_VR?.text()) {
                this.tbraccdCardAuthNumberVr = newPage.TBRACCD_CARD_AUTH_NUMBER_VR.text()
            }
            if (newPage.TBRACCD_BILL_DATE?.text()) {
                this.tbraccdBillDate = newPage.TBRACCD_BILL_DATE.text()
            }
            if (newPage.TBRACCD_BALANCE?.text()) {
                this.tbraccdBalance = newPage.TBRACCD_BALANCE.text()
            }
            if (newPage.TBRACCD_ATYP_SEQNO?.text()) {
                this.tbraccdAtypSeqno = newPage.TBRACCD_ATYP_SEQNO.text()
            }
            if (newPage.TBRACCD_ATYP_CODE?.text()) {
                this.tbraccdAtypCode = newPage.TBRACCD_ATYP_CODE.text()
            }
            if (newPage.TBRACCD_DATA_ORIGIN?.text()) {
                this.tbraccdDataOrigin = newPage.TBRACCD_DATA_ORIGIN.text()
            }
            if (newPage.TBRACCD_DESC?.text()) {
                this.tbraccdDesc = newPage.TBRACCD_DESC.text()
            }
            if (newPage.TBRACCD_DETAIL_CODE?.text()) {
                this.tbraccdDetailCode = newPage.TBRACCD_DETAIL_CODE.text()
            }
            if (newPage.TBRACCD_DOCUMENT_NUMBER?.text()) {
                this.tbraccdDocumentNumber = newPage.TBRACCD_DOCUMENT_NUMBER.text()
            }
            if (newPage.TBRACCD_DUE_DATE?.text()) {
                this.tbraccdDueDate = newPage.TBRACCD_DUE_DATE.text()
            }
            if (newPage.TBRACCD_EFFECTIVE_DATE?.text()) {
                this.tbraccdEffectiveDate = newPage.TBRACCD_EFFECTIVE_DATE.text()
            }
            if (newPage.TBRACCD_ENTRY_DATE?.text()) {
                this.tbraccdEntryDate = newPage.TBRACCD_ENTRY_DATE.text()
            }
            if (newPage.TBRACCD_EXCHANGE_DIFF?.text()) {
                this.tbraccdExchangeDiff = newPage.TBRACCD_EXCHANGE_DIFF.text()
            }
            if (newPage.TBRACCD_FEED_DATE?.text()) {
                this.tbraccdFeedDate = newPage.TBRACCD_FEED_DATE.text()
            }
            if (newPage.TBRACCD_FEED_DOC_CODE?.text()) {
                this.tbraccdFeedDocCode = newPage.TBRACCD_FEED_DOC_CODE.text()
            }
            if (newPage.TBRACCD_FOREIGN_AMOUNT?.text()) {
                this.tbraccdForeignAmount = newPage.TBRACCD_FOREIGN_AMOUNT.text()
            }
            if (newPage.TBRACCD_INVOICE_NUMBER?.text()) {
                this.tbraccdInvoiceNumber = newPage.TBRACCD_INVOICE_NUMBER.text()
            }
            if (newPage.TBRACCD_INV_NUMBER_PAID?.text()) {
                this.tbraccdInvNumberPaid = newPage.TBRACCD_INV_NUMBER_PAID.text()
            }
            if (newPage.TBRACCD_LATE_DCAT_CODE?.text()) {
                this.tbraccdLateDcatCode = newPage.TBRACCD_LATE_DCAT_CODE.text()
            }
            if (newPage.TBRACCD_ORIG_CHG_IND?.text()) {
                this.tbraccdOrigChgInd = newPage.TBRACCD_ORIG_CHG_IND.text()
            }
            if (newPage.TBRACCD_LOC_MDT_SEQ?.text()) {
                this.tbraccdLocMdtSeq = newPage.TBRACCD_LOC_MDT_SEQ.text()
            }
            if (newPage.TBRACCD_MERCHANT_ID?.text()) {
                this.tbraccdMerchantId = newPage.TBRACCD_MERCHANT_ID.text()
            }
            if (newPage.TBRACCD_PAYMENT_ID?.text()) {
                this.tbraccdPaymentId = newPage.TBRACCD_PAYMENT_ID.text()
            }
            if (newPage.TBRACCD_PERIOD?.text()) {
                this.tbraccdPeriod = newPage.TBRACCD_PERIOD.text()
            }
            if (newPage.TBRACCD_RATE?.text()) {
                this.tbraccdRate = newPage.TBRACCD_RATE.text()
            }
            if (newPage.TBRACCD_RECEIPT_NUMBER?.text()) {
                this.tbraccdReceiptNumber = newPage.TBRACCD_RECEIPT_NUMBER.text()
            }
            if (newPage.TBRACCD_SESSION_NUMBER?.text()) {
                this.tbraccdSessionNumber = newPage.TBRACCD_SESSION_NUMBER.text()
            }
            if (newPage.TBRACCD_SRCE_CODE?.text()) {
                this.tbraccdSrceCode = newPage.TBRACCD_SRCE_CODE.text()
            }
            if (newPage.TBRACCD_STATEMENT_DATE?.text()) {
                this.tbraccdStatementDate = newPage.TBRACCD_STATEMENT_DATE.text()
            }
            if (newPage.TBRACCD_STSP_KEY_SEQUENCE?.text()) {
                this.tbraccdStspKeySequence = newPage.TBRACCD_STSP_KEY_SEQUENCE.text()
            }
            if (newPage.TBRACCD_SURROGATE_ID?.text()) {
                this.tbraccdSurrogateId = newPage.TBRACCD_SURROGATE_ID.text()
            }
            if (newPage.TBRACCD_VPDI_CODE?.text()) {
                this.tbraccdVpdiCode = newPage.TBRACCD_VPDI_CODE.text()
            }
            if (newPage.TBRACCD_VERSION?.text()) {
                this.tbraccdVersion = newPage.TBRACCD_VERSION.text()
            }
            if (newPage.TBRACCD_USER_ID?.text()) {
                this.tbraccdUserId = newPage.TBRACCD_USER_ID.text()
            }
            if (newPage.TBRACCD_USER?.text()) {
                this.tbraccdUser = newPage.TBRACCD_USER.text()
            }
            if (newPage.TBRACCD_UNITS?.text()) {
                this.tbraccdUnits = newPage.TBRACCD_UNITS.text()
            }
            if (newPage.TBRACCD_TRAN_NUMBER_PAID?.text()) {
                this.tbraccdTranNumberPaid = newPage.TBRACCD_TRAN_NUMBER_PAID.text()
            }
            if (newPage.TBRACCD_TAX_AMOUNT?.text()) {
                this.tbraccdTaxAmount = newPage.TBRACCD_TAX_AMOUNT.text()
            }
            if (newPage.TBRACCD_TAX_FUTURE_IND?.text()) {
                this.tbraccdTaxFutureInd = newPage.TBRACCD_TAX_FUTURE_IND.text()
            }
            if (newPage.TBRACCD_TAX_FUTURE_IND_PR?.text()) {
                this.tbraccdTaxFutureIndPr = newPage.TBRACCD_TAX_FUTURE_IND_PR.text()
            }
            if (newPage.TBRACCD_TAX_FUTURE_IND_SG?.text()) {
                this.tbraccdTaxFutureIndSg = newPage.TBRACCD_TAX_FUTURE_IND_SG.text()
            }
            if (newPage.TBRACCD_TAX_REPT_BOX?.text()) {
                this.tbraccdTaxReptBox = newPage.TBRACCD_TAX_REPT_BOX.text()
            }
            if (newPage.TBRACCD_TAX_REPT_BOX_PR?.text()) {
                this.tbraccdTaxReptBoxPr = newPage.TBRACCD_TAX_REPT_BOX_PR.text()
            }
            if (newPage.TBRACCD_TAX_REPT_BOX_SG?.text()) {
                this.tbraccdTaxReptBoxSg = newPage.TBRACCD_TAX_REPT_BOX_SG.text()
            }
            if (newPage.TBRACCD_TAX_REPT_YEAR?.text()) {
                this.tbraccdTaxReptYear = newPage.TBRACCD_TAX_REPT_YEAR.text()
            }
            if (newPage.TBRACCD_TERM_CODE?.text()) {
                this.tbraccdTermCode = newPage.TBRACCD_TERM_CODE.text()
            }
            if (newPage.TBRACCD_TRANS_DATE?.text()) {
                this.tbraccdTransDate = newPage.TBRACCD_TRANS_DATE.text()
            }
            if (newPage.TBRACCD_TRAN_NUMBER?.text()) {
                this.tbraccdTranNumber = newPage.TBRACCD_TRAN_NUMBER.text()
            }

            if (this.tbraccdTranNumber == '1') {
                deleteData("TVRBRDC", "DELETE FROM TVRBRDC WHERE TVRBRDC_BOLETO_NUMBER in (SELECT tvbbhdr_boleto_number FROM TVBBHDR WHERE TVBBHDR_PIDM = ?)", tbraccdPidm)
                deleteData("TVRBDSC", "DELETE FROM TVRBDSC WHERE TVRBDSC_BOLETO_NUMBER in (SELECT tvbbhdr_boleto_number FROM TVBBHDR WHERE TVBBHDR_PIDM = ?)", tbraccdPidm)
                deleteData("TVRBDTL", "DELETE FROM TVRBDTL WHERE TVRBDTL_PIDM = ? ", tbraccdPidm)
                deleteData("TVRTACD", "DELETE FROM TVRTACD WHERE TVRTACD_PIDM = ?", tbraccdPidm)
                deleteData("TVRCISP", "DELETE FROM TVRCISP WHERE TVRCISP_PIDM = ?", tbraccdPidm)
                deleteData("TVRSIMU", "DELETE FROM TVRSIMU WHERE TVRSIMU_PIDM = ?", tbraccdPidm)
                deleteData("TVBBHDR", "DELETE FROM TVBBHDR WHERE TVBBHDR_PIDM = ?", tbraccdPidm)
                deleteData("TBRACCD", "DELETE FROM TBRACCD WHERE TBRACCD_PIDM = ?", tbraccdPidm)
            }
            createTBRACCDObject()
        }
    }

    private def createTBRACCDObject() {
        def sql = """insert into TBRACCD(TBRACCD_ACCT_FEED_IND,TBRACCD_ACTIVITY_DATE,TBRACCD_AIDY_CODE,TBRACCD_AMOUNT,TBRACCD_ATYP_CODE,TBRACCD_ATYP_SEQNO,TBRACCD_BALANCE,TBRACCD_BILL_DATE,TBRACCD_CARD_AUTH_NUMBER_VR,TBRACCD_CARD_EXP_DATE_VR,TBRACCD_CARD_TYPE_VR,TBRACCD_CCRD_CODE,TBRACCD_CPDT_IND,TBRACCD_CREATE_SOURCE,TBRACCD_CRN,TBRACCD_CROSSREF_DCAT_CODE,TBRACCD_CROSSREF_DETAIL_CODE,TBRACCD_CROSSREF_NUMBER,TBRACCD_CROSSREF_PIDM,TBRACCD_CROSSREF_SRCE_CODE,TBRACCD_CSHR_END_DATE,TBRACCD_CURR_CODE,TBRACCD_DATA_ORIGIN,TBRACCD_DESC,TBRACCD_DETAIL_CODE,TBRACCD_DOCUMENT_NUMBER,TBRACCD_DUE_DATE,TBRACCD_EFFECTIVE_DATE,TBRACCD_ENTRY_DATE,TBRACCD_EXCHANGE_DIFF,TBRACCD_FEED_DATE,TBRACCD_FEED_DOC_CODE,TBRACCD_FOREIGN_AMOUNT,TBRACCD_INVOICE_NUMBER,TBRACCD_INV_NUMBER_PAID,TBRACCD_LATE_DCAT_CODE,TBRACCD_LOC_MDT,TBRACCD_LOC_MDT_SEQ,TBRACCD_MERCHANT_ID,TBRACCD_ORIG_CHG_IND,TBRACCD_PAYMENT_ID,TBRACCD_PERIOD,TBRACCD_PIDM,TBRACCD_RATE,TBRACCD_RECEIPT_NUMBER,TBRACCD_SESSION_NUMBER,TBRACCD_SRCE_CODE,TBRACCD_STATEMENT_DATE,TBRACCD_STSP_KEY_SEQUENCE,TBRACCD_SURROGATE_ID,TBRACCD_TAX_AMOUNT,TBRACCD_TAX_FUTURE_IND,TBRACCD_TAX_FUTURE_IND_PR,TBRACCD_TAX_FUTURE_IND_SG,TBRACCD_TAX_REPT_BOX,TBRACCD_TAX_REPT_BOX_PR,TBRACCD_TAX_REPT_BOX_SG,TBRACCD_TAX_REPT_YEAR,TBRACCD_TERM_CODE,TBRACCD_TRANS_DATE,TBRACCD_TRAN_NUMBER,TBRACCD_TRAN_NUMBER_PAID,TBRACCD_UNITS,TBRACCD_USER,TBRACCD_USER_ID,TBRACCD_VERSION,TBRACCD_VPDI_CODE ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [this.tbraccdAcctFeedInd, this.tbraccdActivityDate, this.tbraccdAidyCode, this.tbraccdAmount, this.tbraccdAtypCode, this.tbraccdAtypSeqno, this.tbraccdBalance, this.tbraccdBillDate, this.tbraccdCardAuthNumberVr, this.tbraccdCardExpDateVr, this.tbraccdCardTypeVr, this.tbraccdCcrdCode, this.tbraccdCpdtInd, this.tbraccdCreateSource, this.tbraccdCrn, this.tbraccdCrossrefDcatCode, this.tbraccdCrossrefDetailCode, this.tbraccdCrossrefNumber, this.tbraccdCrossrefPidm, this.tbraccdCrossrefSrceCode, this.tbraccdCshrEndDate, this.tbraccdCurrCode, this.tbraccdDataOrigin, this.tbraccdDesc, this.tbraccdDetailCode, this.tbraccdDocumentNumber, this.tbraccdDueDate, this.tbraccdEffectiveDate, this.tbraccdEntryDate, this.tbraccdExchangeDiff, this.tbraccdFeedDate, this.tbraccdFeedDocCode, this.tbraccdForeignAmount, this.tbraccdInvoiceNumber, this.tbraccdInvNumberPaid, this.tbraccdLateDcatCode, this.tbraccdLocMdt, this.tbraccdLocMdtSeq, this.tbraccdMerchantId, this.tbraccdOrigChgInd, this.tbraccdPaymentId, this.tbraccdPeriod, this.tbraccdPidm, this.tbraccdRate, this.tbraccdReceiptNumber, this.tbraccdSessionNumber, this.tbraccdSrceCode, this.tbraccdStatementDate, this.tbraccdStspKeySequence, this.tbraccdSurrogateId, this.tbraccdTaxAmount, this.tbraccdTaxFutureInd, this.tbraccdTaxFutureIndPr, this.tbraccdTaxFutureIndSg, this.tbraccdTaxReptBox, this.tbraccdTaxReptBoxPr, this.tbraccdTaxReptBoxSg, this.tbraccdTaxReptYear, this.tbraccdTermCode, this.tbraccdTransDate, this.tbraccdTranNumber, this.tbraccdTranNumberPaid, this.tbraccdUnits, this.tbraccdUser, this.tbraccdUserId, this.tbraccdVersion, this.tbraccdVpdiCode])
            connectInfo.tableUpdate('TBRACCD', 0, 1, 0, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not create access to object,  $e.message"
            }
        }
    }

    private String fetchUserPidm(String bannerId) {
        String userPidm
        try {
            String crnsql = "SELECT GB_COMMON.F_GET_PIDM(?) pidm FROM DUAL"
            conn.eachRow(crnsql, [bannerId]) {
                userPidm = it.pidm
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not find pidm from TVRTACD in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not find pidm from TVRTACD  ${connectInfo.tableName}.")
        return userPidm
    }

    private def fetchBoletoCount() {
        def cnt
        try {
            String sql = "select count(TVBBHDR_BOLETO_NUMBER) cnt from TVBBHDR where TVBBHDR_COMMENT=?"
            conn.eachRow(sql, [this.tvbbhdrComment]) {
                cnt = it.cnt
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not last inserted boleto number val from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not last inserted boleto number val from TVBBHDR ${connectInfo.tableName}.")
        return cnt
    }

    private def deleteData(tableName, sql, boletoId) {
        try {
            int delRows = conn.executeUpdate(sql, [boletoId])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${boletoId} from ProxyAccessCredentialInformationDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }
}
