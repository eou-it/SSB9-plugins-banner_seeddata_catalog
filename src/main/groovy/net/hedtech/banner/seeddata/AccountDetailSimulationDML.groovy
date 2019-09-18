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
    def tvrtacdpidm
    def tvrtacdsessionid
    def tvrtacdseqno
    def tvrtacdtrannumber
    def tvrtacdtermcode
    def tvrtacdtrandetailcode
    def tvrtacdamount
    def tvrtacdeffectivedate
    def tvrtacdpaidind
    def tvrtacdcashind
    def tvrtacddiscountind
    def tvrtacduserid
    def tvrtacdactivitydate
    def tvrtacdintsamount
    def tvrtacdfineamount
    def tvrtacdtotal
    def tvrtacdplanints
    def tvrtacdaccdtrannumber
    def tvrtacdintstrannumber
    def tvrtacddocumentnumber
    def tvrtacdplantrannumber
    def tvrtacdfinetrannumber
    def tvrtacdpaytrannumber
    def tvrtacdorigtrannumber
    def tvrtacdorigdetailcode
    def tvrtacdcurrencyamount
    def tvrtacdcurrencyrate
    def tvrtacddataorigin
    def tvrtacdboletonumber
    def tvrtacddsctamount

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
                this.tvrtacdpidm = newPage.TVRTACD_PIDM.text()
            }
            if (newPage.TVRTACD_SESSION_ID?.text()) {
                this.tvrtacdsessionid = newPage.TVRTACD_SESSION_ID.text()
            }
            if (newPage.TVRTACD_SEQNO?.text()) {
                this.tvrtacdseqno = newPage.TVRTACD_SEQNO.text()
            }
            if (newPage.TVRTACD_TRAN_NUMBER?.text()) {
                this.tvrtacdtrannumber = newPage.TVRTACD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_TERM_CODE?.text()) {
                this.tvrtacdtermcode = newPage.TVRTACD_TERM_CODE.text()
            }
            if (newPage.TVRTACD_TRAN_DETAIL_CODE?.text()) {
                this.tvrtacdtrandetailcode = newPage.TVRTACD_TRAN_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_AMOUNT?.text()) {
                this.tvrtacdamount = newPage.TVRTACD_AMOUNT.text()
            }
            if (newPage.TVRTACD_EFFECTIVE_DATE?.text()) {
                this.tvrtacdeffectivedate = newPage.TVRTACD_EFFECTIVE_DATE.text()
            }
            if (newPage.TVRTACD_PAID_IND?.text()) {
                this.tvrtacdpaidind = newPage.TVRTACD_PAID_IND.text()
            }
            if (newPage.TVRTACD_CASH_IND?.text()) {
                this.tvrtacdcashind = newPage.TVRTACD_CASH_IND.text()
            }
            if (newPage.TVRTACD_DISCOUNT_IND?.text()) {
                this.tvrtacddiscountind = newPage.TVRTACD_DISCOUNT_IND.text()
            }
            if (newPage.TVRTACD_USER_ID?.text()) {
                this.tvrtacduserid = newPage.TVRTACD_USER_ID.text()
            }
            if (newPage.TVRTACD_ACTIVITY_DATE?.text()) {
                this.tvrtacdactivitydate = newPage.TVRTACD_ACTIVITY_DATE.text()
            }
            if (newPage.TVRTACD_FINE_AMOUNT?.text()) {
                this.tvrtacdfineamount = newPage.TVRTACD_FINE_AMOUNT.text()
            }
            if (newPage.TVRTACD_INTS_TRAN_NUMBER?.text()) {
                this.tvrtacdintsamount = newPage.TVRTACD_INTS_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_TOTAL?.text()) {
                this.tvrtacdtotal = newPage.TVRTACD_TOTAL.text()
            }
            if (newPage.TVRTACD_PLAN_INTS?.text()) {
                this.tvrtacdplanints = newPage.TVRTACD_PLAN_INTS.text()
            }
            if (newPage.TVRTACD_ACCD_TRAN_NUMBER?.text()) {
                this.tvrtacdaccdtrannumber = newPage.TVRTACD_ACCD_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_DOCUMENT_NUMBER?.text()) {
                this.tvrtacddocumentnumber = newPage.TVRTACD_DOCUMENT_NUMBER.text()
            }
            if (newPage.TVRTACD_PLAN_TRAN_NUMBER?.text()) {
                this.tvrtacdplantrannumber = newPage.TVRTACD_PLAN_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_FINE_TRAN_NUMBER?.text()) {
                this.tvrtacdfinetrannumber = newPage.TVRTACD_FINE_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_PAY_TRAN_NUMBER?.text()) {
                this.tvrtacdpaytrannumber = newPage.TVRTACD_PAY_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ORIG_TRAN_NUMBER?.text()) {
                this.tvrtacdorigtrannumber = newPage.TVRTACD_ORIG_TRAN_NUMBER.text()
            }
            if (newPage.TVRTACD_ORIG_DETAIL_CODE?.text()) {
                this.tvrtacdorigdetailcode = newPage.TVRTACD_ORIG_DETAIL_CODE.text()
            }
            if (newPage.TVRTACD_CURRENCY_AMOUNT?.text()) {
                this.tvrtacdcurrencyamount = newPage.TVRTACD_CURRENCY_AMOUNT.text()
            }
            if (newPage.TVRTACD_CURRENCY_RATE?.text()) {
                this.tvrtacdcurrencyrate = newPage.TVRTACD_CURRENCY_RATE.text()
            }
            if (newPage.TVRTACD_DATA_ORIGIN?.text()) {
                this.tvrtacddataorigin = newPage.TVRTACD_DATA_ORIGIN.text()
            }
            if (newPage.TVRTACD_BOLETO_NUMBER?.text()) {
                this.tvrtacdboletonumber = newPage.TVRTACD_BOLETO_NUMBER.text()
            }
            if (newPage.TVRTACD_DSCT_AMOUNT?.text()) {
                this.tvrtacddsctamount = newPage.TVRTACD_DSCT_AMOUNT.text()
            }

            def id = fetchBoletoId()
            if(id){
                this.tvrtacdboletonumber = id
            }
            createTVRTACDObject()
            }
        }

    private def createTVRTACDObject() {
        def sql = """insert into TVRTACD(TVRTACD_BOLETO_NUMBER, TVRTACD_PIDM, TVRTACD_SESSION_ID, TVRTACD_SEQNO,TVRTACD_TRAN_NUMBER,TVRTACD_TERM_CODE,TVRTACD_TRAN_DETAIL_CODE,TVRTACD_AMOUNT,TVRTACD_EFFECTIVE_DATE,TVRTACD_PAID_IND,TVRTACD_CASH_IND,TVRTACD_DISCOUNT_IND,TVRTACD_USER_ID,TVRTACD_ACTIVITY_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [tvrtacdboletonumber, tvrtacdpidm, tvrtacdsessionid, tvrtacdseqno, tvrtacdtrannumber, tvrtacdtermcode, tvrtacdtrandetailcode, tvrtacdamount, tvrtacdeffectivedate, tvrtacdpaidind, tvrtacdcashind, tvrtacddiscountind, tvrtacduserid, tvrtacdactivitydate])
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
            conn.eachRow(sql, [this.tvrtacdboletonumber]) {
                id = it.Id
            }
            println("boleto number : "+id)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not last inserted boleto number val from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not last inserted boleto number val from TVBBHDR ${connectInfo.tableName}.")
        return id
    }
}
