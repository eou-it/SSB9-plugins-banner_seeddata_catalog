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

public class TvrtacdAccountDetailDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrtacdAccdTranNumber
    def tvrtacdActivityDate
    def tvrtacdAmount
    def tvrtacdBoletoNumber
    def tvrtacdCashInd
    def tvrtacdCurrencyAmount
    def tvrtacdCurrencyRate
    def tvrtacdDataOrigin
    def tvrtacdDiscountInd
    def tvrtacdDocumentNumber
    def tvrtacdDsctAmount
    def tvrtacdEffectiveDate
    def tvrtacdFineAmount
    def tvrtacdFineTranNumber
    def tvrtacdIntsAmount
    def tvrtacdIntsTranNumber
    def tvrtacdOrigDetailCode
    def tvrtacdOrigTranNumber
    def tvrtacdPaidInd
    def tvrtacdPayTranNumber
    def tvrtacdPidm
    def tvrtacdPlanInts
    def tvrtacdPlannTranNumber
    def tvrtacdSeqno
    def tvrtacdSessionid
    def tvrtacdSurrogateId
    def tvrtacdTermCode
    def tvrtacdTotal
    def tvrtacdTranDetailCode
    def tvrtacdTranNumber
    def tvrtacdUserId
    def tvrtacdVersion
    def tvrtacdVpdiCode

    public TvrtacdAccountDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TvrtacdAccountDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRTACD()
    }

    def processTVRTACD() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrtacdBoletoNumber = newPage.TVRTACD_BOLETO_NUMBER.text()
            this.tvrtacdBoletoNumber = fetchBoletoNumber()
            this.tvrtacdPidm = fetchUserPidm(newPage.BANNERID.text())
            if (newPage.TVRTACD_ACCD_TRAN_NUMBER?.text()) {
                this.tvrtacdAccdTranNumber = newPage.TVRTACD_ACCD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ACTIVITY_DATE?.text()) {
                this.tvrtacdActivityDate = newPage.TVRTACD_ACTIVITY_DATE.text()
            }
            if (newPage.TVRTACD_AMOUNT?.text()) {
                this.tvrtacdAmount = newPage.TVRTACD_AMOUNT.text()
            }
            if (newPage.TVRTACD_CASH_IND?.text()) {
                this.tvrtacdCashInd = newPage.TVRTACD_CASH_IND.text()
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
            if (newPage.TVRTACD_DISCOUNT_IND?.text()) {
                this.tvrtacdDiscountInd = newPage.TVRTACD_DISCOUNT_IND.text()
            }
            if (newPage.TVRTACD_DOCUMENT_NUMBER?.text()) {
                this.tvrtacdDocumentNumber = newPage.TVRTACD_DOCUMENT_NUMBER.text()
            }
            if (newPage.TVRTACD_DSCT_AMOUNT?.text()) {
                this.tvrtacdDsctAmount = newPage.TVRTACD_DSCT_AMOUNT.text()
            }
            if (newPage.TVRTACD_EFFECTIVE_DATE?.text()) {
                this.tvrtacdEffectiveDate = newPage.TVRTACD_EFFECTIVE_DATE.text()
            }
            if (newPage.TVRTACD_FINE_AMOUNT?.text()) {
                this.tvrtacdFineAmount = newPage.TVRTACD_FINE_AMOUNT.text()
            }
            if (newPage.TVRTACD_VERSION?.text()) {
                this.tvrtacdVersion = newPage.TVRTACD_VERSION.text()
            }
            if (newPage.TVRTACD_USER_ID?.text()) {
                this.tvrtacdUserId = newPage.TVRTACD_USER_ID.text()
            }
            if (newPage.TVRTACD_TRAN_NUMBER?.text()) {
                this.tvrtacdTranNumber = newPage.TVRTACD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_TRAN_DETAIL_CODE?.text()) {
                this.tvrtacdTranDetailCode = newPage.TVRTACD_TRAN_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_TOTAL?.text()) {
                this.tvrtacdTotal = newPage.TVRTACD_TOTAL.text()
            }
            if (newPage.TVRTACD_TERM_CODE?.text()) {
                this.tvrtacdTermCode = newPage.TVRTACD_TERM_CODE.text()
            }
            if (newPage.TVRTACD_SURROGATE_ID?.text()) {
                this.tvrtacdSurrogateId = newPage.TVRTACD_SURROGATE_ID.text()
            }
            if (newPage.TVRTACD_SESSION_ID?.text()) {
                this.tvrtacdSessionid = newPage.TVRTACD_SESSION_ID.text()
            }
            if (newPage.TVRTACD_SEQNO?.text()) {
                this.tvrtacdSeqno = newPage.TVRTACD_SEQNO.text()
            }
            if (newPage.TVRTACD_VPDI_CODE?.text()) {
                this.tvrtacdVpdiCode = newPage.TVRTACD_VPDI_CODE.text()
            }
            if (newPage.TVRTACD_PLAN_TRAN_NUMBER?.text()) {
                this.tvrtacdPlannTranNumber = newPage.TVRTACD_PLAN_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_PLAN_INTS?.text()) {
                this.tvrtacdPlanInts = newPage.TVRTACD_PLAN_INTS.text()
            }
            if (newPage.TVRTACD_PAY_TRAN_NUMBER?.text()) {
                this.tvrtacdPayTranNumber = newPage.TVRTACD_PAY_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_PAID_IND?.text()) {
                this.tvrtacdPaidInd = newPage.TVRTACD_PAID_IND.text()
            }
            if (newPage.TVRTACD_ORIG_TRAN_NUMBER?.text()) {
                this.tvrtacdOrigTranNumber = newPage.TVRTACD_ORIG_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ORIG_DETAIL_CODE?.text()) {
                this.tvrtacdOrigDetailCode = newPage.TVRTACD_ORIG_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_INTS_TRAN_NUMBER?.text()) {
                this.tvrtacdIntsTranNumber = newPage.TVRTACD_INTS_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_INTS_AMOUNT?.text()) {
                this.tvrtacdIntsAmount = newPage.TVRTACD_INTS_AMOUNT.text()
            }
            if (newPage.TVRTACD_FINE_TRAN_NUMBER?.text()) {
                this.tvrtacdFineTranNumber = newPage.TVRTACD_FINE_TRAN_NUMBER.text()
            }
            createTVRTACDObject()
        }
    }

    private def createTVRTACDObject() {
        def sql = """insert into TVRTACD(TVRTACD_ACCD_TRAN_NUMBER,TVRTACD_ACTIVITY_DATE,TVRTACD_AMOUNT,TVRTACD_BOLETO_NUMBER,TVRTACD_CASH_IND,TVRTACD_CURRENCY_AMOUNT,TVRTACD_CURRENCY_RATE,TVRTACD_DATA_ORIGIN,TVRTACD_DISCOUNT_IND,TVRTACD_DOCUMENT_NUMBER,TVRTACD_DSCT_AMOUNT,TVRTACD_EFFECTIVE_DATE,TVRTACD_FINE_AMOUNT,TVRTACD_FINE_TRAN_NUMBER,TVRTACD_INTS_AMOUNT,TVRTACD_INTS_TRAN_NUMBER,TVRTACD_ORIG_DETAIL_CODE,TVRTACD_ORIG_TRAN_NUMBER,TVRTACD_PAID_IND,TVRTACD_PAY_TRAN_NUMBER,TVRTACD_PIDM,TVRTACD_PLAN_INTS,TVRTACD_PLAN_TRAN_NUMBER,TVRTACD_SEQNO,TVRTACD_SESSION_ID,TVRTACD_SURROGATE_ID,TVRTACD_TERM_CODE,TVRTACD_TOTAL,TVRTACD_TRAN_DETAIL_CODE,TVRTACD_TRAN_NUMBER,TVRTACD_USER_ID,TVRTACD_VERSION,TVRTACD_VPDI_CODE ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [this.tvrtacdAccdTranNumber, this.tvrtacdActivityDate, this.tvrtacdAmount, this.tvrtacdBoletoNumber, this.tvrtacdCashInd, this.tvrtacdCurrencyAmount, this.tvrtacdCurrencyRate, this.tvrtacdDataOrigin, this.tvrtacdDiscountInd, this.tvrtacdDocumentNumber, this.tvrtacdDsctAmount, this.tvrtacdEffectiveDate, this.tvrtacdFineAmount, this.tvrtacdFineTranNumber, this.tvrtacdIntsAmount, this.tvrtacdIntsTranNumber, this.tvrtacdOrigDetailCode, this.tvrtacdOrigTranNumber, this.tvrtacdPaidInd, this.tvrtacdPayTranNumber, this.tvrtacdPidm, this.tvrtacdPlanInts, this.tvrtacdPlannTranNumber, this.tvrtacdSeqno, this.tvrtacdSessionid, this.tvrtacdSurrogateId, this.tvrtacdTermCode, this.tvrtacdTotal, this.tvrtacdTranDetailCode, this.tvrtacdTranNumber, this.tvrtacdUserId, this.tvrtacdVersion, this.tvrtacdVpdiCode])
            connectInfo.tableUpdate('TVRTACD', 0, 1, 0, 0, 0)
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
            String crnsql = "SELECT max(TVBBHDR_BOLETO_NUMBER) tbn FROM TVBBHDR WHERE TVBBHDR_COMMENT = ?"
            conn.eachRow(crnsql, [this.tvrtacdBoletoNumber.toString()]) {
                tbn = it.tbn
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors)
                println("Could not boleto number from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis)
            println("Could not boleto number from TVBBHDR  ${connectInfo.tableName}.")
        return tbn
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

}
