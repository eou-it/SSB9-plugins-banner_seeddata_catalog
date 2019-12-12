/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRCISP DML tables
 *
 */

public class TvrcispInstallmentPlanDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrcispActivityDate
    def tvrcispAmount
    def tvrcispDataOrigin
    def tvrcispDetailCode
    def tvrcispDocumentNumber
    def tvrcispDueDate
    def tvrcispInstallmentPlan
    def tvrcispPidm
    def tvrcispRefNumber
    def tvrcispRemainInd
    def tvrcispSeqNo
    def tvrcispSessionid
    def tvrcispSurrogateId
    def tvrcispTermCode
    def tvrcispTranType
    def tvrcispUserId
    def tvrcispVersion
    def tvrcispVpdiCode

    public TvrcispInstallmentPlanDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TvrcispInstallmentPlanDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRCISP()
    }

    def processTVRCISP() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrcispPidm = fetchUserPidm(newPage.BANNERID.text())
            if (newPage.TVRCISP_ACTIVITY_DATE?.text()) {
                this.tvrcispActivityDate = newPage.TVRCISP_ACTIVITY_DATE.text()
            }
            if (newPage.TVRCISP_AMOUNT?.text()) {
                this.tvrcispAmount = newPage.TVRCISP_AMOUNT.text()
            }
            if (newPage.TVRCISP_DATA_ORIGIN?.text()) {
                this.tvrcispDataOrigin = newPage.TVRCISP_DATA_ORIGIN.text()
            }
            if (newPage.TVRCISP_DETAIL_CODE?.text()) {
                this.tvrcispDetailCode = newPage.TVRCISP_DETAIL_CODE.text()
            }
            if (newPage.TVRCISP_DOCUMENT_NUMBER?.text()) {
                this.tvrcispDocumentNumber = newPage.TVRCISP_DOCUMENT_NUMBER.text()
            }
            if (newPage.TVRCISP_DUE_DATE?.text()) {
                this.tvrcispDueDate = newPage.TVRCISP_DUE_DATE.text()
            }
            if (newPage.TVRCISP_INSTALLMENT_PLAN?.text()) {
                this.tvrcispInstallmentPlan = newPage.TVRCISP_INSTALLMENT_PLAN.text()
            }
            if (newPage.TVRCISP_REF_NUMBER?.text()) {
                this.tvrcispRefNumber = newPage.TVRCISP_REF_NUMBER.text()
            }
            if (newPage.TVRCISP_REMAIN_IND?.text()) {
                this.tvrcispRemainInd = newPage.TVRCISP_REMAIN_IND.text()
            }
            if (newPage.TVRCISP_SEQ_NO?.text()) {
                this.tvrcispSeqNo = newPage.TVRCISP_SEQ_NO.text()
            }
            if (newPage.TVRCISP_SESSIONID?.text()) {
                this.tvrcispSessionid = newPage.TVRCISP_SESSIONID.text()
            }
            if (newPage.TVRCISP_SURROGATE_ID?.text()) {
                this.tvrcispSurrogateId = newPage.TVRCISP_SURROGATE_ID.text()
            }
            if (newPage.TVRCISP_TERM_CODE?.text()) {
                this.tvrcispTermCode = newPage.TVRCISP_TERM_CODE.text()
            }
            if (newPage.TVRCISP_TRAN_TYPE?.text()) {
                this.tvrcispTranType = newPage.TVRCISP_TRAN_TYPE.text()
            }
            if (newPage.TVRCISP_USER_ID?.text()) {
                this.tvrcispUserId = newPage.TVRCISP_USER_ID.text()
            }
            if (newPage.TVRCISP_VERSION?.text()) {
                this.tvrcispVersion = newPage.TVRCISP_VERSION.text()
            }
            if (newPage.TVRCISP_VPDI_CODE?.text()) {
                this.tvrcispVpdiCode = newPage.TVRCISP_VPDI_CODE.text()
            }

            createTVRCISPObject()
        }
    }

    private def createTVRCISPObject() {
        def sql = """insert into TVRCISP(TVRCISP_ACTIVITY_DATE,TVRCISP_AMOUNT,TVRCISP_DATA_ORIGIN,TVRCISP_DETAIL_CODE,TVRCISP_DOCUMENT_NUMBER,TVRCISP_DUE_DATE,TVRCISP_INSTALLMENT_PLAN,TVRCISP_PIDM,TVRCISP_REF_NUMBER,TVRCISP_REMAIN_IND,TVRCISP_SEQ_NO,TVRCISP_SESSIONID,TVRCISP_SURROGATE_ID,TVRCISP_TERM_CODE,TVRCISP_TRAN_TYPE,TVRCISP_USER_ID,TVRCISP_VERSION,TVRCISP_VPDI_CODE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [this.tvrcispActivityDate,this.tvrcispAmount,this.tvrcispDataOrigin,this.tvrcispDetailCode,this.tvrcispDocumentNumber,this.tvrcispDueDate,this.tvrcispInstallmentPlan,this.tvrcispPidm,this.tvrcispRefNumber,this.tvrcispRemainInd,this.tvrcispSeqNo,this.tvrcispSessionid,this.tvrcispSurrogateId,this.tvrcispTermCode,this.tvrcispTranType,this.tvrcispUserId,this.tvrcispVersion,this.tvrcispVpdiCode])
            connectInfo.tableUpdate('TVRCISP', 0, 1, 0, 0, 0)
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

}
