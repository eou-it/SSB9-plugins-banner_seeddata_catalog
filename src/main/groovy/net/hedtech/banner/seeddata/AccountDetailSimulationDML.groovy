/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVBBHDR DML tables
 * update the sequence generated IDs for TVBBHDR_ID
 */

public class AccountDetailSimulationDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrtacdPidm
    def tvrtacdSessionId
    def tvrtacdSeqNo
    def tvrtacdTranNumber
    def tvrtacdTermCode
    def tvrtacdTranDetailCode
    def tvrtacdAmount
    def tvrtacdEffectiveDate
    def tvrtacdPaidInd
    def tvrtacdCashInd
    def tvrtacdDiscountInd
    def tvrtacdUserId
    def tvrtacdActivityDate
    def tvrtacdIntsAmount
    def tvrtacdFineAmount
    def tvrtacdTotal
    def tvrtacdPlanInts
    def tvrtacdAccdTranNumber
    def tvrtacdIntsTranNumber
    def tvrtacdDocumentNumber
    def tvrtacdPlanTranNumber
    def tvrtacdFineTranNumber
    def tvrtacdPayTranNumber
    def tvrtacdOrigTranNumber
    def tvrtacdOrigDetailCode
    def tvrtacdCurrencyAmount
    def tvrtacdCurrencyRate
    def tvrtacdDataOrigin
    def tvrtacdBoletoNumber
    def tvrtacdDsctAmount

    public AccountDetailSimulationDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public AccountDetailSimulationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRTACD()
    }
    def processTVRTACD() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            if (newPage.TVRTACD_PIDM?.text()) {
                this.tvrtacdPidm = newPage.TVRTACD_PIDM.text()
            }
            if (newPage.TVRTACD_SESSION_ID?.text()) {
                this.tvrtacdSessionId = newPage.TVRTACD_SESSION_ID.text()
            }
            if (newPage.TVRTACD_SEQNO?.text()) {
                this.tvrtacdSeqNo = newPage.TVRTACD_SEQNO.text()
            }
            if (newPage.TVRTACD_TRAN_NUMBER?.text()) {
                this.tvrtacdTranNumber = newPage.TVRTACD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_TERM_CODE?.text()) {
                this.tvrtacdTermCode = newPage.TVRTACD_TERM_CODE.text()
            }
            if (newPage.TVRTACD_TRAN_DETAIL_CODE?.text()) {
                this.tvrtacdTranDetailCode = newPage.TVRTACD_TRAN_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_AMOUNT?.text()) {
                this.tvrtacdAmount = newPage.TVRTACD_AMOUNT.text()
            }
            if (newPage.TVRTACD_EFFECTIVE_DATE?.text()) {
                this.tvrtacdEffectiveDate = newPage.TVRTACD_EFFECTIVE_DATE.text()
            }
            if (newPage.TVRTACD_PAID_IND?.text()) {
                this.tvrtacdPaidInd = newPage.TVRTACD_PAID_IND.text()
            }
            if (newPage.TVRTACD_CASH_IND?.text()) {
                this.tvrtacdCashInd = newPage.TVRTACD_CASH_IND.text()
            }
            if (newPage.TVRTACD_DISCOUNT_IND?.text()) {
                this.tvrtacdDiscountInd = newPage.TVRTACD_DISCOUNT_IND.text()
            }
            if (newPage.TVRTACD_USER_ID?.text()) {
                this.tvrtacdUserId = newPage.TVRTACD_USER_ID.text()
            }
            if (newPage.TVRTACD_ACTIVITY_DATE?.text()) {
                this.tvrtacdActivityDate = newPage.TVRTACD_ACTIVITY_DATE.text()
            }
            if (newPage.TVRTACD_FINE_AMOUNT?.text()) {
                this.tvrtacdFineAmount = newPage.TVRTACD_FINE_AMOUNT.text()
            }
            if (newPage.TVRTACD_INTS_TRAN_NUMBER?.text()) {
                this.tvrtacdIntsAmount = newPage.TVRTACD_INTS_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_TOTAL?.text()) {
                this.tvrtacdTotal = newPage.TVRTACD_TOTAL.text()
            }
            if (newPage.TVRTACD_PLAN_INTS?.text()) {
                this.tvrtacdPlanInts = newPage.TVRTACD_PLAN_INTS.text()
            }
            if (newPage.TVRTACD_ACCD_TRAN_NUMBER?.text()) {
                this.tvrtacdAccdTranNumber = newPage.TVRTACD_ACCD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_DOCUMENT_NUMBER?.text()) {
                this.tvrtacdDocumentNumber = newPage.TVRTACD_DOCUMENT_NUMBER.text()
            }
            if (newPage.TVRTACD_PLAN_TRAN_NUMBER?.text()) {
                this.tvrtacdPlanTranNumber = newPage.TVRTACD_PLAN_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_FINE_TRAN_NUMBER?.text()) {
                this.tvrtacdFineTranNumber = newPage.TVRTACD_FINE_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_PAY_TRAN_NUMBER?.text()) {
                this.tvrtacdPayTranNumber = newPage.TVRTACD_PAY_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ORIG_TRAN_NUMBER?.text()) {
                this.tvrtacdOrigTranNumber = newPage.TVRTACD_ORIG_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ORIG_DETAIL_CODE?.text()) {
                this.tvrtacdOrigDetailCode = newPage.TVRTACD_ORIG_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_CURRENCY_AMOUNT?.text()) {
                this.tvrtacdCurrencyAmount = newPage.TVRTACD_CURRENCY_AMOUNT.text()
            }
            if (newPage.TVRTACD_CURRENCY_RATE?.text()) {
                this.tvrtacdCurrencyRate = newPage.TVRTACD_CURRENCY_RATE.text()
            }
            if (newPage.TVRTACD_DATA_ORIGIN?.text()) {
                this.tvrtacdDataOrigin = newPage.TVRTACD_DATA_ORIGIN.text()
            }
            if (newPage.TVRTACD_BOLETO_NUMBER?.text()) {
                this.tvrtacdBoletoNumber = newPage.TVRTACD_BOLETO_NUMBER.text()
            }
            if (newPage.TVRTACD_DSCT_AMOUNT?.text()) {
                this.tvrtacdDsctAmount = newPage.TVRTACD_DSCT_AMOUNT.text()
            }

            def id = fetchBoletoId()
            if(id){
                this.tvrtacdBoletoNumber = id
            }
            createTVRTACDObject()
            }
        }

    private def createTVRTACDObject() {
        def sql = """insert into TVRTACD(TVRTACD_BOLETO_NUMBER, TVRTACD_PIDM, TVRTACD_SESSION_ID, TVRTACD_SEQNO,TVRTACD_TRAN_NUMBER,TVRTACD_TERM_CODE,TVRTACD_TRAN_DETAIL_CODE,TVRTACD_AMOUNT,TVRTACD_EFFECTIVE_DATE,TVRTACD_PAID_IND,TVRTACD_CASH_IND,TVRTACD_DISCOUNT_IND,TVRTACD_USER_ID,TVRTACD_ACTIVITY_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [tvrtacdBoletoNumber, tvrtacdPidm, tvrtacdSessionId, tvrtacdSeqNo, tvrtacdTranNumber, tvrtacdTermCode, tvrtacdTranDetailCode, tvrtacdAmount, tvrtacdEffectiveDate, tvrtacdPaidInd, tvrtacdCashInd, tvrtacdDiscountInd, tvrtacdUserId, tvrtacdActivityDate])
            connectInfo.tableUpdate('TVRTACD', 0, 1, 0, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not create access to object,  $e.message"
            }
        }
    }


    private def fetchBoletoId() {
        def id
        try {
            String sql = "select TVBBHDR_BOLETO_NUMBER Id from TVBBHDR where TVBBHDR_COMMENT=?"
            conn.eachRow(sql, [this.tvrtacdBoletoNumber]) {
                id = it.Id
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not last inserted boleto number val from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not last inserted boleto number val from TVBBHDR ${connectInfo.tableName}.")
        return id
    }
}
