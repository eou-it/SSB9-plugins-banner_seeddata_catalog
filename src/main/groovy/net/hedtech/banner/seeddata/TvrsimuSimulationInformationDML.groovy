/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * TVRSIMU DML tables
 *
 */

public class TvrsimuSimulationInformationDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tvrsimuAcceptedDate
    def tvrsimuAcceptedInd
    def tvrsimuActivityDate
    def tvrsimuAuthDate
    def tvrsimuAuthUser
    def tvrsimuAutoCalcPlanInd
    def tvrsimuCalcEarnInts
    def tvrsimuCalcFinancing
    def tvrsimuCalcFine
    def tvrsimuCalcInts
    def tvrsimuCalcRefinan
    def tvrsimuCalcRemainAmt
    def tvrsimuCalcSubtotal
    def tvrsimuCalcTermAmount
    def tvrsimuCalcUnearnInts
    def tvrsimuCreateDate
    def tvrsimuCreateUser
    def tvrsimuDataOrigin
    def tvrsimuDebtAmount
    def tvrsimuDownPayment
    def tvrsimuDownPaymentInd
    def tvrsimuDownPaymentPct
    def tvrsimuFirstInstallDate
    def tvrsimuInclTermAmountInd
    def tvrsimuInstallmentDue
    def tvrsimuInstallmentPlan
    def tvrsimuIntDetailCode
    def tvrsimuIntRate
    def tvrsimuNegpCode
    def tvrsimuNumberOfPayments
    def tvrsimuPayPeriod
    def tvrsimuPidm
    def tvrsimuPlanAmount
    def tvrsimuPlanDate
    def tvrsimuPlanDesc
    def tvrsimuPlanDetailCode
    def tvrsimuPlanTermCode
    def tvrsimuPlanUser
    def tvrsimuPreauthDate
    def tvrsimuPreauthUser
    def tvrsimuPrinDetailCode
    def tvrsimuRefinanDate
    def tvrsimuRefinanUser
    def tvrsimuRefNumber
    def tvrsimuRemainPayment
    def tvrsimuRemitDebtAmt
    def tvrsimuRemitDebtInd
    def tvrsimuRemitDebtPct
    def tvrsimuRemitFineAmt
    def tvrsimuRemitFineInd
    def tvrsimuRemitFinePct
    def tvrsimuRemitIntsAmt
    def tvrsimuRemitIntsInd
    def tvrsimuRemitIntsPct
    def tvrsimuRemitType
    def tvrsimuSessionId
    def tvrsimuSignedDate
    def tvrsimuSignedInd
    def tvrsimuStatusCde
    def tvrsimuSurrogateId
    def tvrsimuTermCode
    def tvrsimuuserId
    def tvrsimuVersion
    def tvrsimuVpdiCode

    public TvrsimuSimulationInformationDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public TvrsimuSimulationInformationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTVRSIMU()
    }

    def processTVRSIMU() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {
            this.tvrsimuPidm = fetchUserPidm(newPage.BANNERID.text())
            if (newPage.TVRSIMU_ACCEPTED_DATE?.text()) {
                this.tvrsimuAcceptedDate = newPage.TVRSIMU_ACCEPTED_DATE.text()
            }
            if (newPage.TVRSIMU_ACCEPTED_IND?.text()) {
                this.tvrsimuAcceptedInd = newPage.TVRSIMU_ACCEPTED_IND.text()
            }
            if (newPage.TVRSIMU_ACTIVITY_DATE?.text()) {
                this.tvrsimuActivityDate = newPage.TVRSIMU_ACTIVITY_DATE.text()
            }
            if (newPage.TVRSIMU_AUTH_DATE?.text()) {
                this.tvrsimuAuthDate = newPage.TVRSIMU_AUTH_DATE.text()
            }
            if (newPage.TVRSIMU_AUTH_USER?.text()) {
                this.tvrsimuAuthUser = newPage.TVRSIMU_AUTH_USER.text()
            }
            if (newPage.TVRSIMU_AUTO_CALC_PLAN_IND?.text()) {
                this.tvrsimuAutoCalcPlanInd = newPage.TVRSIMU_AUTO_CALC_PLAN_IND.text()
            }
            if (newPage.TVRSIMU_CALC_EARN_INTS?.text()) {
                this.tvrsimuCalcEarnInts = newPage.TVRSIMU_CALC_EARN_INTS.text()
            }
            if (newPage.TVRSIMU_CALC_FINANCING?.text()) {
                this.tvrsimuCalcFinancing = newPage.TVRSIMU_CALC_FINANCING.text()
            }
            if (newPage.TVRSIMU_CALC_FINE?.text()) {
                this.tvrsimuCalcFine = newPage.TVRSIMU_CALC_FINE.text()
            }
            if (newPage.TVRSIMU_CALC_INTS?.text()) {
                this.tvrsimuCalcInts = newPage.TVRSIMU_CALC_INTS.text()
            }
            if (newPage.TVRSIMU_CALC_REFINAN?.text()) {
                this.tvrsimuCalcRefinan = newPage.TVRSIMU_CALC_REFINAN.text()
            }
            if (newPage.TVRSIMU_CALC_REMAIN_AMT?.text()) {
                this.tvrsimuCalcRemainAmt = newPage.TVRSIMU_CALC_REMAIN_AMT.text()
            }
            if (newPage.TVRSIMU_CALC_SUBTOTAL?.text()) {
                this.tvrsimuCalcSubtotal = newPage.TVRSIMU_CALC_SUBTOTAL.text()
            }
            if (newPage.TVRSIMU_CALC_TERM_AMOUNT?.text()) {
                this.tvrsimuCalcTermAmount = newPage.TVRSIMU_CALC_TERM_AMOUNT.text()
            }
            if (newPage.TVRSIMU_CALC_UNEARN_INTS?.text()) {
                this.tvrsimuCalcUnearnInts = newPage.TVRSIMU_CALC_UNEARN_INTS.text()
            }
            if (newPage.TVRSIMU_CREATE_DATE?.text()) {
                this.tvrsimuCreateDate = newPage.TVRSIMU_CREATE_DATE.text()
            }
            if (newPage.TVRSIMU_CREATE_USER?.text()) {
                this.tvrsimuCreateUser = newPage.TVRSIMU_CREATE_USER.text()
            }
            if (newPage.TVRSIMU_DATA_ORIGIN?.text()) {
                this.tvrsimuDataOrigin = newPage.TVRSIMU_DATA_ORIGIN.text()
            }
            if (newPage.TVRSIMU_DEBT_AMOUNT?.text()) {
                this.tvrsimuDebtAmount = newPage.TVRSIMU_DEBT_AMOUNT.text()
            }
            if (newPage.TVRSIMU_DOWN_PAYMENT?.text()) {
                this.tvrsimuDownPayment = newPage.TVRSIMU_DOWN_PAYMENT.text()
            }
            if (newPage.TVRSIMU_DOWN_PAYMENT_IND?.text()) {
                this.tvrsimuDownPaymentInd = newPage.TVRSIMU_DOWN_PAYMENT_IND.text()
            }
            if (newPage.TVRSIMU_DOWN_PAYMENT_PCT?.text()) {
                this.tvrsimuDownPaymentPct = newPage.TVRSIMU_DOWN_PAYMENT_PCT.text()
            }
            if (newPage.TVRSIMU_FIRST_INSTALL_DATE?.text()) {
                this.tvrsimuFirstInstallDate = newPage.TVRSIMU_FIRST_INSTALL_DATE.text()
            }
            if (newPage.TVRSIMU_INCL_TERM_AMOUNT_IND?.text()) {
                this.tvrsimuInclTermAmountInd = newPage.TVRSIMU_INCL_TERM_AMOUNT_IND.text()
            }
            if (newPage.TVRSIMU_INSTALLMENT_DUE?.text()) {
                this.tvrsimuInstallmentDue = newPage.TVRSIMU_INSTALLMENT_DUE.text()
            }
            if (newPage.TVRSIMU_INSTALLMENT_PLAN?.text()) {
                this.tvrsimuInstallmentPlan = newPage.TVRSIMU_INSTALLMENT_PLAN.text()
            }
            if (newPage.TVRSIMU_INT_DETAIL_CODE?.text()) {
                this.tvrsimuIntDetailCode = newPage.TVRSIMU_INT_DETAIL_CODE.text()
            }
            if (newPage.TVRSIMU_INT_RATE?.text()) {
                this.tvrsimuIntRate = newPage.TVRSIMU_INT_RATE.text()
            }
            if (newPage.TVRSIMU_NEGP_CODE?.text()) {
                this.tvrsimuNegpCode = newPage.TVRSIMU_NEGP_CODE.text()
            }
            if (newPage.TVRSIMU_NUMBER_OF_PAYMENTS?.text()) {
                this.tvrsimuNumberOfPayments = newPage.TVRSIMU_NUMBER_OF_PAYMENTS.text()
            }
            if (newPage.TVRSIMU_PLAN_AMOUNT?.text()) {
                this.tvrsimuPlanAmount = newPage.TVRSIMU_PLAN_AMOUNT.text()
            }
            if (newPage.TVRSIMU_PLAN_DATE?.text()) {
                this.tvrsimuPlanDate = newPage.TVRSIMU_PLAN_DATE.text()
            }
            if (newPage.TVRSIMU_PLAN_DESC?.text()) {
                this.tvrsimuPlanDesc = newPage.TVRSIMU_PLAN_DESC.text()
            }
            if (newPage.TVRSIMU_PLAN_DETAIL_CODE?.text()) {
                this.tvrsimuPlanDetailCode = newPage.TVRSIMU_PLAN_DETAIL_CODE.text()
            }
            if (newPage.TVRSIMU_PLAN_TERM_CODE?.text()) {
                this.tvrsimuPlanTermCode = newPage.TVRSIMU_PLAN_TERM_CODE.text()
            }
            if (newPage.TVRSIMU_PLAN_USER?.text()) {
                this.tvrsimuPlanUser = newPage.TVRSIMU_PLAN_USER.text()
            }
            if (newPage.TVRSIMU_PREAUTH_DATE?.text()) {
                this.tvrsimuPreauthDate = newPage.TVRSIMU_PREAUTH_DATE.text()
            }
            if (newPage.TVRSIMU_PREAUTH_USER?.text()) {
                this.tvrsimuPreauthUser = newPage.TVRSIMU_PREAUTH_USER.text()
            }
            if (newPage.TVRSIMU_PRIN_DETAIL_CODE?.text()) {
                this.tvrsimuPrinDetailCode = newPage.TVRSIMU_PRIN_DETAIL_CODE.text()
            }
            if (newPage.TVRSIMU_REFINAN_DATE?.text()) {
                this.tvrsimuRefinanDate = newPage.TVRSIMU_REFINAN_DATE.text()
            }
            if (newPage.TVRSIMU_REFINAN_USER?.text()) {
                this.tvrsimuRefinanUser = newPage.TVRSIMU_REFINAN_USER.text()
            }
            if (newPage.TVRSIMU_REF_NUMBER?.text()) {
                this.tvrsimuRefNumber = newPage.TVRSIMU_REF_NUMBER.text()
            }
            if (newPage.TVRSIMU_REMAIN_PAYMENT?.text()) {
                this.tvrsimuRemainPayment = newPage.TVRSIMU_REMAIN_PAYMENT.text()
            }
            if (newPage.TVRSIMU_REMIT_DEBT_AMT?.text()) {
                this.tvrsimuRemitDebtAmt = newPage.TVRSIMU_REMIT_DEBT_AMT.text()
            }
            if (newPage.TVRSIMU_REMIT_DEBT_IND?.text()) {
                this.tvrsimuRemitDebtInd = newPage.TVRSIMU_REMIT_DEBT_IND.text()
            }
            if (newPage.TVRSIMU_REMIT_DEBT_PCT?.text()) {
                this.tvrsimuRemitDebtPct = newPage.TVRSIMU_REMIT_DEBT_PCT.text()
            }
            if (newPage.TVRSIMU_REMIT_FINE_AMT?.text()) {
                this.tvrsimuRemitFineAmt = newPage.TVRSIMU_REMIT_FINE_AMT.text()
            }
            if (newPage.TVRSIMU_REMIT_FINE_IND?.text()) {
                this.tvrsimuRemitFineInd = newPage.TVRSIMU_REMIT_FINE_IND.text()
            }
            if (newPage.TVRSIMU_REMIT_FINE_PCT?.text()) {
                this.tvrsimuRemitFinePct = newPage.TVRSIMU_REMIT_FINE_PCT.text()
            }
            if (newPage.TVRSIMU_REMIT_INTS_AMT?.text()) {
                this.tvrsimuRemitIntsAmt = newPage.TVRSIMU_REMIT_INTS_AMT.text()
            }
            if (newPage.TVRSIMU_REMIT_INTS_IND?.text()) {
                this.tvrsimuRemitIntsInd = newPage.TVRSIMU_REMIT_INTS_IND.text()
            }
            if (newPage.TVRSIMU_REMIT_INTS_PCT?.text()) {
                this.tvrsimuRemitIntsPct = newPage.TVRSIMU_REMIT_INTS_PCT.text()
            }
            if (newPage.TVRSIMU_REMIT_TYPE?.text()) {
                this.tvrsimuRemitType = newPage.TVRSIMU_REMIT_TYPE.text()
            }
            if (newPage.TVRSIMU_SESSION_ID?.text()) {
                this.tvrsimuSessionId = newPage.TVRSIMU_SESSION_ID.text()
            }
            if (newPage.TVRSIMU_SIGNED_DATE?.text()) {
                this.tvrsimuSignedDate = newPage.TVRSIMU_SIGNED_DATE.text()
            }
            if (newPage.TVRSIMU_SIGNED_IND?.text()) {
                this.tvrsimuSignedInd = newPage.TVRSIMU_SIGNED_IND.text()
            }
            if (newPage.TVRSIMU_STATUS_CDE?.text()) {
                this.tvrsimuStatusCde = newPage.TVRSIMU_STATUS_CDE.text()
            }
            if (newPage.TVRSIMU_SURROGATE_ID?.text()) {
                this.tvrsimuSurrogateId = newPage.TVRSIMU_SURROGATE_ID.text()
            }
            if (newPage.TVRSIMU_TERM_CODE?.text()) {
                this.tvrsimuTermCode = newPage.TVRSIMU_TERM_CODE.text()
            }
            if (newPage.TVRSIMU_USER_ID?.text()) {
                this.tvrsimuuserId = newPage.TVRSIMU_USER_ID.text()
            }
            if (newPage.TVRSIMU_VERSION?.text()) {
                this.tvrsimuVersion = newPage.TVRSIMU_VERSION.text()
            }
            if (newPage.TVRSIMU_VPDI_CODE?.text()) {
                this.tvrsimuVpdiCode = newPage.TVRSIMU_VPDI_CODE.text()
            }

            createTVRSIMUObject()
        }
    }

    private def createTVRSIMUObject() {

        def sql = """insert into TVRSIMU(TVRSIMU_ACCEPTED_DATE,TVRSIMU_ACCEPTED_IND,TVRSIMU_ACTIVITY_DATE,TVRSIMU_AUTH_DATE,TVRSIMU_AUTH_USER,TVRSIMU_AUTO_CALC_PLAN_IND,TVRSIMU_CALC_EARN_INTS,TVRSIMU_CALC_FINANCING,TVRSIMU_CALC_FINE,TVRSIMU_CALC_INTS,TVRSIMU_CALC_REFINAN,TVRSIMU_CALC_REMAIN_AMT,TVRSIMU_CALC_SUBTOTAL,TVRSIMU_CALC_TERM_AMOUNT,TVRSIMU_CALC_UNEARN_INTS,TVRSIMU_CREATE_DATE,TVRSIMU_CREATE_USER,TVRSIMU_DATA_ORIGIN,TVRSIMU_DEBT_AMOUNT,TVRSIMU_DOWN_PAYMENT,TVRSIMU_DOWN_PAYMENT_IND,TVRSIMU_DOWN_PAYMENT_PCT,TVRSIMU_FIRST_INSTALL_DATE,TVRSIMU_INCL_TERM_AMOUNT_IND,TVRSIMU_INSTALLMENT_DUE,TVRSIMU_INSTALLMENT_PLAN,TVRSIMU_INT_DETAIL_CODE,TVRSIMU_INT_RATE,TVRSIMU_NEGP_CODE,TVRSIMU_NUMBER_OF_PAYMENTS,TVRSIMU_PAY_PERIOD,TVRSIMU_PIDM,TVRSIMU_PLAN_AMOUNT,TVRSIMU_PLAN_DATE,TVRSIMU_PLAN_DESC,TVRSIMU_PLAN_DETAIL_CODE,TVRSIMU_PLAN_TERM_CODE,TVRSIMU_PLAN_USER,TVRSIMU_PREAUTH_DATE,TVRSIMU_PREAUTH_USER,TVRSIMU_PRIN_DETAIL_CODE,TVRSIMU_REFINAN_DATE,TVRSIMU_REFINAN_USER,TVRSIMU_REF_NUMBER,TVRSIMU_REMAIN_PAYMENT,TVRSIMU_REMIT_DEBT_AMT,TVRSIMU_REMIT_DEBT_IND,TVRSIMU_REMIT_DEBT_PCT,TVRSIMU_REMIT_FINE_AMT,TVRSIMU_REMIT_FINE_IND,TVRSIMU_REMIT_FINE_PCT,TVRSIMU_REMIT_INTS_AMT,TVRSIMU_REMIT_INTS_IND,TVRSIMU_REMIT_INTS_PCT,TVRSIMU_REMIT_TYPE,TVRSIMU_SESSION_ID,TVRSIMU_SIGNED_DATE,TVRSIMU_SIGNED_IND,TVRSIMU_STATUS_CDE,TVRSIMU_SURROGATE_ID,TVRSIMU_TERM_CODE,TVRSIMU_USER_ID,TVRSIMU_VERSION,TVRSIMU_VPDI_CODE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [this.tvrsimuAcceptedDate,this.tvrsimuAcceptedInd,this.tvrsimuActivityDate,this.tvrsimuAuthDate,this.tvrsimuAuthUser,this.tvrsimuAutoCalcPlanInd,this.tvrsimuCalcEarnInts,this.tvrsimuCalcFinancing,this.tvrsimuCalcFine,this.tvrsimuCalcInts,this.tvrsimuCalcRefinan,this.tvrsimuCalcRemainAmt,this.tvrsimuCalcSubtotal,this.tvrsimuCalcTermAmount,this.tvrsimuCalcUnearnInts,this.tvrsimuCreateDate,this.tvrsimuCreateUser,this.tvrsimuDataOrigin,this.tvrsimuDebtAmount,this.tvrsimuDownPayment,this.tvrsimuDownPaymentInd,this.tvrsimuDownPaymentPct,this.tvrsimuFirstInstallDate,this.tvrsimuInclTermAmountInd,this.tvrsimuInstallmentDue,this.tvrsimuInstallmentPlan,this.tvrsimuIntDetailCode,this.tvrsimuIntRate,this.tvrsimuNegpCode,this.tvrsimuNumberOfPayments,this.tvrsimuPayPeriod,this.tvrsimuPidm,this.tvrsimuPlanAmount,this.tvrsimuPlanDate,this.tvrsimuPlanDesc,this.tvrsimuPlanDetailCode,this.tvrsimuPlanTermCode,this.tvrsimuPlanUser,this.tvrsimuPreauthDate,this.tvrsimuPreauthUser,this.tvrsimuPrinDetailCode,this.tvrsimuRefinanDate,this.tvrsimuRefinanUser,this.tvrsimuRefNumber,this.tvrsimuRemainPayment,this.tvrsimuRemitDebtAmt,this.tvrsimuRemitDebtInd,this.tvrsimuRemitDebtPct,this.tvrsimuRemitFineAmt,this.tvrsimuRemitFineInd,this.tvrsimuRemitFinePct,this.tvrsimuRemitIntsAmt,this.tvrsimuRemitIntsInd,this.tvrsimuRemitIntsPct,this.tvrsimuRemitType,this.tvrsimuSessionId,this.tvrsimuSignedDate,this.tvrsimuSignedInd,this.tvrsimuStatusCde,this.tvrsimuSurrogateId,this.tvrsimuTermCode,this.tvrsimuuserId,this.tvrsimuVersion,this.tvrsimuVpdiCode])
            connectInfo.tableUpdate('TVRSIMU', 0, 1, 0, 0, 0)
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
