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

public class BoletoHeaderDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvbbhdrBoletoNumber
    def tvbbhdrBoletoversion
    def tvbbhdrPIDM
    def tvbbhdrActive
    def tvbbhdrGenerateddate
    def tvbbhdrPFTPCode
    def tvbbhdrBankCode
    def tvbbhdrtStatusCode
    def tvbbhdrSatusDate
    def tvbbhdrdLocCode
    def tvbbhdrLocationDate
    def tvbbhdrRegisterBoleto
    def tvbbhdrUserId
    def tvbbhdrActivityDate
    def tvbbhdrTermCode
    def tvbbhdrPayDate
    def tvbbhdrDueDate01
    def tvbbhdrDueDate02
    def tvbbhdrDueDate03
    def tvbbhdrAmount01
    def tvbbhdrAmount02
    def tvbbhdrAmount03
    def tvbbhdrDiscountAmt
    def tvbbhdrOtherDedAmt
    def tvbbhdrFinesAmt
    def tvbbhdrOtherChgAmt
    def tvbbhdrNetAmount
    def tvbbhdrDataOrigin
    def tvbbhdrDueDate
    def tvbbhdrDepositDate
    def tvbbhdrFinRespPIDM
    def tvbbhdrComment
    def tvbbhdrBoletoNumberBank
    def tvbbhdrfileseqno
    def tvbbhdrpabffileIid

    public BoletoHeaderDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public BoletoHeaderDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVBBHDR()
    }

    def processTVBBHDR() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            def boletoNumber = fetchNextValFromSequenceGenerator()

            if (boletoNumber) {
                this.tvbbhdrBoletoNumber = boletoNumber + 1
            } else {
                this.tvbbhdrBoletoNumber = newPage.TVBBHDR_BOLETO_NUMBER.text()
            }
            if (newPage.TVBBHDR_BOLETO_VERSION?.text()) {
                this.tvbbhdrBoletoversion = newPage.TVBBHDR_BOLETO_VERSION.text()
            }
            if (newPage.TVBBHDR_PIDM?.text()) {
                this.tvbbhdrPIDM = newPage.TVBBHDR_PIDM.text()
            }
            if (newPage.TVBBHDR_ACTIVE_IND?.text()) {
                this.tvbbhdrActive = newPage.TVBBHDR_ACTIVE_IND.text()
            }
            if (newPage.TVBBHDR_GENERATED_DATE?.text()) {
                this.tvbbhdrGenerateddate = newPage.TVBBHDR_GENERATED_DATE.text()
            }
            if (newPage.TVBBHDR_PABF_FILE_ID?.text()) {
                this.tvbbhdrpabffileIid = newPage.TVBBHDR_PABF_FILE_ID.text()
            }
            if (newPage.TVBBHDR_FILE_SEQ_NO?.text()) {
                this.tvbbhdrfileseqno = newPage.TVBBHDR_FILE_SEQ_NO.text()
            }
            if (newPage.TVBBHDR_BOLETO_NUMBER_BANK?.text()) {
                this.tvbbhdrBoletoNumberBank = newPage.TVBBHDR_BOLETO_NUMBER_BANK.text()
            }
            if (newPage.TVBBHDR_COMMENT?.text()) {
                this.tvbbhdrComment = newPage.TVBBHDR_COMMENT.text()
            }
            if (newPage.TVBBHDR_FIN_RESP_PIDM?.text()) {
                this.tvbbhdrFinRespPIDM = newPage.TVBBHDR_FIN_RESP_PIDM.text()
            }
            if (newPage.TVBBHDR_DEPOSIT_DATE?.text()) {
                this.tvbbhdrDepositDate = newPage.TVBBHDR_DEPOSIT_DATE.text()
            }
            if (newPage.TVBBHDR_DUE_DATE?.text()) {
                this.tvbbhdrDueDate = newPage.TVBBHDR_DUE_DATE.text()
            }
            if (newPage.TVBBHDR_DATA_ORIGIN?.text()) {
                this.tvbbhdrDataOrigin = newPage.TVBBHDR_DATA_ORIGIN.text()
            }
            if (newPage.TVBBHDR_NET_AMOUNT?.text()) {
                this.tvbbhdrNetAmount = newPage.TVBBHDR_NET_AMOUNT.text()
            }
            if (newPage.TVBBHDR_OTHER_CHG_AMT?.text()) {
                this.tvbbhdrOtherChgAmt = newPage.TVBBHDR_OTHER_CHG_AMT.text()
            }
            if (newPage.TVBBHDR_FINES_AMT?.text()) {
                this.tvbbhdrFinesAmt = newPage.TVBBHDR_FINES_AMT.text()
            }
            if (newPage.TVBBHDR_OTHER_DED_AMT?.text()) {
                this.tvbbhdrOtherDedAmt = newPage.TVBBHDR_OTHER_DED_AMT.text()
            }
            if (newPage.TVBBHDR_DISCOUNT_AMT?.text()) {
                this.tvbbhdrDiscountAmt = newPage.TVBBHDR_DISCOUNT_AMT.text()
            }
            if (newPage.TVBBHDR_03_AMOUNT?.text()) {
                this.tvbbhdrAmount03 = newPage.TVBBHDR_03_AMOUNT.text()
            }
            if (newPage.TVBBHDR_02_AMOUNT?.text()) {
                this.tvbbhdrAmount02 = newPage.TVBBHDR_02_AMOUNT.text()
            }
            if (newPage.TVBBHDR_01_AMOUNT?.text()) {
                this.tvbbhdrAmount01 = newPage.TVBBHDR_01_AMOUNT.text()
            }
            if (newPage.TVBBHDR_03_DUE_DATE?.text()) {
                this.tvbbhdrDueDate03 = newPage.TVBBHDR_03_DUE_DATE.text()
            }
            if (newPage.TVBBHDR_02_DUE_DATE?.text()) {
                this.tvbbhdrDueDate02 = newPage.TVBBHDR_02_DUE_DATE.text()
            }
            if (newPage.TVBBHDR_01_DUE_DATE?.text()) {
                this.tvbbhdrDueDate01 = newPage.TVBBHDR_01_DUE_DATE.text()
            }
            if (newPage.TVBBHDR_GENERATED_DATE?.text()) {
                this.tvbbhdrGenerateddate = newPage.TVBBHDR_GENERATED_DATE.text()
            }
            if (newPage.TVBBHDR_PFTP_CODE?.text()) {
                this.tvbbhdrPFTPCode = newPage.TVBBHDR_PFTP_CODE.text()
            }
            if (newPage.TVBBHDR_BANK_CODE?.text()) {
                this.tvbbhdrBankCode = newPage.TVBBHDR_BANK_CODE.text()
            }
            if (newPage.TVBBHDR_TSTA_CODE?.text()) {
                this.tvbbhdrtStatusCode = newPage.TVBBHDR_TSTA_CODE.text()
            }
            if (newPage.TVBBHDR_STATUS_DATE?.text()) {
                this.tvbbhdrSatusDate = newPage.TVBBHDR_STATUS_DATE.text()
            }
            if (newPage.TVBBHDR_DLOC_CODE?.text()) {
                this.tvbbhdrdLocCode = newPage.TVBBHDR_DLOC_CODE.text()
            }
            if (newPage.TVBBHDR_DLOC_CODE?.text()) {
                this.tvbbhdrdLocCode = newPage.TVBBHDR_DLOC_CODE.text()
            }
            if (newPage.TVBBHDR_LOCATION_DATE?.text()) {
                this.tvbbhdrLocationDate = newPage.TVBBHDR_LOCATION_DATE.text()
            }
            if (newPage.TVBBHDR_REGISTER_BOLETO_IND?.text()) {
                this.tvbbhdrRegisterBoleto = newPage.TVBBHDR_REGISTER_BOLETO_IND.text()
            }
            if (newPage.TVBBHDR_USER_ID?.text()) {
                this.tvbbhdrUserId = newPage.TVBBHDR_USER_ID.text()
            }
            if (newPage.TVBBHDR_ACTIVITY_DATE?.text()) {
                this.tvbbhdrActivityDate = newPage.TVBBHDR_ACTIVITY_DATE.text()
            }
            if (newPage.TVBBHDR_TERM_CODE?.text()) {
                this.tvbbhdrTermCode = newPage.TVBBHDR_TERM_CODE.text()
            }
            if (newPage.TVBBHDR_PAY_DATE?.text()) {
                this.tvbbhdrPayDate = newPage.TVBBHDR_PAY_DATE.text()
            }
            def count = fetchBoletoCount()
            if (count > 0) {
                def boletoId = fetchBoletoId()
                deleteData("TVRBDTL", "DELETE FROM TVRBDTL WHERE TVRBDTL_BOLETO_NUMBER = ? ", boletoId)
                deleteData("TVRTACD", "DELETE FROM TVRTACD WHERE TVRTACD_BOLETO_NUMBER = ?", boletoId)
                deleteData("TVBBHDR", "DELETE FROM TVBBHDR WHERE TVBBHDR_BOLETO_NUMBER = ?", boletoId)
            }
            createTVBBHDRObject()
        }
    }

    private def createTVBBHDRObject() {
        def sql = """insert into TVBBHDR (TVBBHDR_BOLETO_NUMBER, TVBBHDR_BOLETO_VERSION, TVBBHDR_PIDM, TVBBHDR_ACTIVE_IND, TVBBHDR_GENERATED_DATE, TVBBHDR_PFTP_CODE, TVBBHDR_BANK_CODE, TVBBHDR_TSTA_CODE, TVBBHDR_STATUS_DATE, TVBBHDR_DLOC_CODE,TVBBHDR_LOCATION_DATE, TVBBHDR_REGISTER_BOLETO_IND, TVBBHDR_USER_ID, TVBBHDR_ACTIVITY_DATE, TVBBHDR_TERM_CODE, TVBBHDR_01_DUE_DATE, TVBBHDR_01_AMOUNT, TVBBHDR_02_AMOUNT, TVBBHDR_03_AMOUNT,TVBBHDR_DISCOUNT_AMT, TVBBHDR_OTHER_DED_AMT, TVBBHDR_FINES_AMT, TVBBHDR_OTHER_CHG_AMT, TVBBHDR_NET_AMOUNT, TVBBHDR_DATA_ORIGIN,TVBBHDR_DUE_DATE, TVBBHDR_FIN_RESP_PIDM, TVBBHDR_COMMENT,TVBBHDR_BOLETO_NUMBER_BANK,TVBBHDR_FILE_SEQ_NO,TVBBHDR_PABF_FILE_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )"""
        try {
            conn.executeInsert(sql, [tvbbhdrBoletoNumber, tvbbhdrBoletoversion, tvbbhdrPIDM, tvbbhdrActive.toString(), tvbbhdrGenerateddate.toString(), tvbbhdrPFTPCode.toString(), tvbbhdrBankCode.toString(), tvbbhdrtStatusCode.toString(), tvbbhdrSatusDate.toString(), tvbbhdrdLocCode.toString(), tvbbhdrLocationDate.toString(), tvbbhdrRegisterBoleto.toString(), tvbbhdrUserId.toString(), tvbbhdrActivityDate.toString(), tvbbhdrTermCode.toString(), tvbbhdrDueDate01.toString(), tvbbhdrAmount01, tvbbhdrAmount02, tvbbhdrAmount03, tvbbhdrDiscountAmt, tvbbhdrOtherDedAmt, tvbbhdrFinesAmt, tvbbhdrOtherChgAmt, tvbbhdrNetAmount, tvbbhdrDataOrigin.toString(), tvbbhdrDueDate.toString(), tvbbhdrFinRespPIDM, tvbbhdrComment.toString(), tvbbhdrBoletoNumberBank, tvbbhdrfileseqno, tvbbhdrpabffileIid])
            connectInfo.tableUpdate('TVBBHDR', 0, 1, 0, 0, 0)
        }
        catch (Exception e) {

            if (connectInfo.showErrors) {
                println "Could not create access to object,  $e.message"
            }
        }
    }

    private def fetchNextValFromSequenceGenerator() {

        def crn
        try {
            String crnsql = "SELECT max(to_number(TVBBHDR_BOLETO_NUMBER)) crn from TVBBHDR"
            conn.eachRow(crnsql) {
                crn = it.crn
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not last inserted boleto number val from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not last inserted boleto number val from TVBBHDR ${connectInfo.tableName}.")
        return crn
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

    private def fetchBoletoId() {
        def bln
        try {
            String sql = "select max(TVBBHDR_BOLETO_NUMBER) bln from TVBBHDR where TVBBHDR_COMMENT=?"
            conn.eachRow(sql, [this.tvbbhdrComment]) {
                bln = it.bln
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Could not last inserted boleto number val from TVBBHDR in BoletoHeaderDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Could not last inserted boleto number val from TVBBHDR ${connectInfo.tableName}.")
        return bln
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
