/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRBDSC DML tables
 *
 */

public class TvrbdscBoletoDetailDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrbdscBoletoNumber
    def tvrbdscBoletoVersion
    def tvrbdscTermCode
    def tvrbdscRuleNumber
    def tvrbdscBilrSeqNo
    def tvrbdscRangeCode
    def tvrbdscDiscBeginDate
    def tvrbdscDiscEndDate
    def tvrbdscPercentage
    def tvrbdscDetailCode
    def tvrbdscAmount
    def tvrbdscApplyInd
    def tvrbdscTranNumber
    def tvrbdscUserId
    def tvrbdscActivityDate
    def tvrbdscDataOrigin

    public TvrbdscBoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TvrbdscBoletoDetailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRBDSC()
    }

    def processTVRBDSC() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrbdscBoletoNumber = newPage.TVRBDSC_BOLETO_NUMBER.text()
            this.tvrbdscBoletoNumber = fetchBoletoNumber()
            if (newPage.TVRBDSC_BOLETO_VERSION?.text()) {
                this.tvrbdscBoletoVersion = newPage.TVRBDSC_BOLETO_VERSION.text()
            }
            if (newPage.TVRBDSC_TERM_CODE?.text()) {
                this.tvrbdscTermCode = newPage.TVRBDSC_TERM_CODE.text()
            }
            if (newPage.TVRBDSC_RULE_NUMBER?.text()) {
                this.tvrbdscRuleNumber = newPage.TVRBDSC_RULE_NUMBER.text()
            }
            if (newPage.TVRBDSC_BILR_SEQ_NO?.text()) {
                this.tvrbdscBilrSeqNo = newPage.TVRBDSC_BILR_SEQ_NO.text()
            }
            if (newPage.TVRBDSC_RNGE_CODE?.text()) {
                this.tvrbdscRangeCode = newPage.TVRBDSC_RNGE_CODE.text()
            }
            if (newPage.TVRBDSC_DISC_BEGIN_DATE?.text()) {
                this.tvrbdscDiscBeginDate = newPage.TVRBDSC_DISC_BEGIN_DATE.text()
            }
            if (newPage.TVRBDSC_DISC_END_DATE?.text()) {
                this.tvrbdscDiscEndDate = newPage.TVRBDSC_DISC_END_DATE.text()
            }
            if (newPage.TVRBDSC_PERCENTAGE?.text()) {
                this.tvrbdscPercentage = newPage.TVRBDSC_PERCENTAGE.text()
            }
            if (newPage.TVRBDSC_DETAIL_CODE?.text()) {
                this.tvrbdscDetailCode = newPage.TVRBDSC_DETAIL_CODE.text()
            }
            if (newPage.TVRBDSC_AMOUNT?.text()) {
                this.tvrbdscAmount = newPage.TVRBDSC_AMOUNT.text()
            }
            if (newPage.TVRBDSC_APPLY_IND?.text()) {
                this.tvrbdscApplyInd = newPage.TVRBDSC_APPLY_IND.text()
            }
            if (newPage.TVRBDSC_TRAN_NUMBER?.text()) {
                this.tvrbdscTranNumber = newPage.TVRBDSC_TRAN_NUMBER.text()
            }
            if (newPage.TVRBDSC_USER_ID?.text()) {
                this.tvrbdscUserId = newPage.TVRBDSC_USER_ID.text()
            }
            if (newPage.tvrbdsc_ACTIVITY_DATE?.text()) {
                this.tvrbdscActivityDate = newPage.tvrbdsc_ACTIVITY_DATE.text()
            }
            if (newPage.tvrbdsc_DATA_ORIGIN?.text()) {
                this.tvrbdscDataOrigin = newPage.tvrbdsc_DATA_ORIGIN.text()
            }

            createTvrbdscObject()
        }
    }

    private def createTvrbdscObject() {
        def sql = """insert into TVRBDSC(TVRBDSC_BOLETO_NUMBER,TVRBDSC_BOLETO_VERSION,TVRBDSC_TERM_CODE,
TVRBDSC_RULE_NUMBER,TVRBDSC_BILR_SEQ_NO,TVRBDSC_RNGE_CODE,TVRBDSC_DISC_BEGIN_DATE,TVRBDSC_DISC_END_DATE,
TVRBDSC_PERCENTAGE,TVRBDSC_DETAIL_CODE,TVRBDSC_AMOUNT,TVRBDSC_APPLY_IND,TVRBDSC_TRAN_NUMBER,TVRBDSC_USER_ID,
TVRBDSC_ACTIVITY_DATE,TVRBDSC_DATA_ORIGIN ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [tvrbdscBoletoNumber,tvrbdscBoletoVersion,tvrbdscTermCode,tvrbdscRuleNumber,tvrbdscBilrSeqNo,tvrbdscRangeCode,tvrbdscDiscBeginDate,tvrbdscDiscEndDate,tvrbdscPercentage,tvrbdscDetailCode,tvrbdscAmount,tvrbdscApplyInd,tvrbdscTranNumber,tvrbdscUserId,tvrbdscActivityDate,tvrbdscDataOrigin])
            connectInfo.tableUpdate('TVRBDSC', 0, 1, 0, 0, 0)
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
            conn.eachRow(crnsql, [this.tvrbdscBoletoNumber.toString()]) {
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
