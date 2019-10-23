/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRBRDC DML tables
 *
 */

public class TvrbrdcBoletoDetailDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrbrdcBoletoNumber
    def tvrbrdcBoletoVersion
    def tvrbrdcTermCode
    def tvrbrdcRuleNumber
    def tvrbrdcBilrSeqNo
    def tvrbrdcRangeCode
    def tvrbrdcDetailCode
    def tvrbrdcAmount
    def tvrbrdcUserId
    def tvrbrdcActivityDate
    def tvrbrdcDataOrigin

    public TvrbrdcBoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TvrbrdcBoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRBRDC()
    }

    def processTVRBRDC() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrbrdcBoletoNumber = newPage.TVRBRDC_BOLETO_NUMBER.text()
            this.tvrbrdcBoletoNumber = fetchBoletoNumber()
            if (newPage.TVRBRDC_BOLETO_VERSION?.text()) {
                this.tvrbrdcBoletoVersion = newPage.TVRBRDC_BOLETO_VERSION.text()
            }
            if (newPage.TVRBRDC_TERM_CODE?.text()) {
                this.tvrbrdcTermCode = newPage.TVRBRDC_TERM_CODE.text()
            }
            if (newPage.TVRBRDC_RULE_NUMBER?.text()) {
                this.tvrbrdcRuleNumber = newPage.TVRBRDC_RULE_NUMBER.text()
            }
            if (newPage.TVRBRDC_BILR_SEQ_NO?.text()) {
                this.tvrbrdcBilrSeqNo = newPage.TVRBRDC_BILR_SEQ_NO.text()
            }
            if (newPage.TVRBRDC_RNGE_CODE?.text()) {
                this.tvrbrdcRangeCode = newPage.TVRBRDC_RNGE_CODE.text()
            }
            if (newPage.TVRBRDC_DETAIL_CODE?.text()) {
                this.tvrbrdcDetailCode = newPage.TVRBRDC_DETAIL_CODE.text()
            }
            if (newPage.TVRBRDC_AMOUNT?.text()) {
                this.tvrbrdcAmount = newPage.TVRBRDC_AMOUNT.text()
            }
            if (newPage.TVRBRDC_USER_ID?.text()) {
                this.tvrbrdcUserId = newPage.TVRBRDC_USER_ID.text()
            }
            if (newPage.TVRBRDC_ACTIVITY_DATE?.text()) {
                this.tvrbrdcActivityDate = newPage.TVRBRDC_ACTIVITY_DATE.text()
            }
            if (newPage.TVRBRDC_DATA_ORIGIN?.text()) {
                this.tvrbrdcDataOrigin = newPage.TVRBRDC_DATA_ORIGIN.text()
            }

            createTVRBRDCObject()
        }
    }

    private def createTVRBRDCObject() {
        def sql = """insert into TVRBRDC(TVRBRDC_BOLETO_NUMBER,TVRBRDC_BOLETO_VERSION,TVRBRDC_TERM_CODE,
TVRBRDC_RULE_NUMBER,TVRBRDC_BILR_SEQ_NO,TVRBRDC_RNGE_CODE,TVRBRDC_DETAIL_CODE,TVRBRDC_AMOUNT,TVRBRDC_USER_ID,
TVRBRDC_ACTIVITY_DATE,TVRBRDC_DATA_ORIGIN ) values (?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [ tvrbrdcBoletoNumber, tvrbrdcBoletoVersion, tvrbrdcTermCode, tvrbrdcRuleNumber,
                      tvrbrdcBilrSeqNo, tvrbrdcRangeCode, tvrbrdcDetailCode, tvrbrdcAmount, tvrbrdcUserId,
                      tvrbrdcActivityDate, tvrbrdcDataOrigin ])
            connectInfo.tableUpdate('TVRBRDC', 0, 1, 0, 0, 0)
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
            conn.eachRow(crnsql, [ this.tvrbrdcBoletoNumber.toString() ]) {
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


}
