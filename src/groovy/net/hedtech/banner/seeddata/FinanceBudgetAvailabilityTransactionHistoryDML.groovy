/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability to populate Transaction History
 */
public class FinanceBudgetAvailabilityTransactionHistoryDML {
    def ruclCode
    def docSeqCode
    def docCode
    def submissionNumber
    def itemNum
    def seqNum
    def serialNum
    def userId
    def transDate
    def transAmt
    def transDesc
    def drCrInd
    def coasCode
    def fsyrCode
    def fundCode
    def orgnCode
    def acctCode
    def progCode
    def postingPeriod
    def encdNum
    def encdItemNum
    def encdSeqNum
    def reversalInd
    def cmtType
    def encbType
    def vendorPidm
    def abalOverride
    def convertAmt
    def exchangeAmt
    def exchangeDiff
    def sumPostDocCode

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityTransactionHistoryDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityTransactionHistoryDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateTransactionHistory()
    }


    def parseXmlData() {
        def transHistory = new XmlParser().parseText(xmlData)
        this.ruclCode = transHistory.FGBTRNH_RUCL_CODE.text()
        this.docSeqCode = transHistory.FGBTRNH_DOC_SEQ_CODE.text()
        this.docCode = transHistory.FGBTRNH_DOC_CODE.text()
        this.submissionNumber = transHistory.FGBTRNH_SUBMISSION_NUMBER.text()
        this.itemNum = transHistory.FGBTRNH_ITEM_NUM.text()
        this.seqNum = transHistory.FGBTRNH_SEQ_NUM.text()
        this.serialNum = transHistory.FGBTRNH_SERIAL_NUM.text()
        this.userId = transHistory.FGBTRNH_USER_ID.text()
        this.transDate = transHistory.FGBTRNH_TRANS_DATE.text()
        this.transAmt = transHistory.FGBTRNH_TRANS_AMT.text() as double
        this.transDesc = transHistory.FGBTRNH_TRANS_DESC.text()
        this.drCrInd = transHistory.FGBTRNH_DR_CR_IND.text()
        this.coasCode = transHistory.FGBTRNH_COAS_CODE.text()
        this.fsyrCode = transHistory.FGBTRNH_FSYR_CODE.text()
        this.fundCode = transHistory.FGBTRNH_FUND_CODE.text()
        this.orgnCode = transHistory.FGBTRNH_ORGN_CODE.text()
        this.acctCode = transHistory.FGBTRNH_ACCT_CODE.text()
        this.progCode = transHistory.FGBTRNH_PROG_CODE.text()
        this.postingPeriod = transHistory.FGBTRNH_POSTING_PERIOD.text()
        this.encdNum = transHistory.FGBTRNH_ENCD_NUM.text()
        this.encdItemNum = transHistory.FGBTRNH_ENCD_ITEM_NUM.text()
        this.encdSeqNum = transHistory.FGBTRNH_ENCD_SEQ_NUM.text()
        this.reversalInd = transHistory.FGBTRNH_REVERSAL_IND.text()
        this.cmtType = transHistory.FGBTRNH_CMT_TYPE.text()
        this.encbType = transHistory.FGBTRNH_ENCB_TYPE.text()
        this.vendorPidm = transHistory.FGBTRNH_VENDOR_PIDM.text()
        this.abalOverride = transHistory.FGBTRNH_ABAL_OVERRIDE.text()
        this.convertAmt = transHistory.FGBTRNH_CONVERT_AMT.text() as double
        this.exchangeAmt = transHistory.FGBTRNH_EXCHANGE_AMT.text() as double
        this.exchangeDiff = transHistory.FGBTRNH_EXCHANGE_DIFF.text() as double
        this.sumPostDocCode = transHistory.FGBTRNH_SUM_POST_DOC_CODE.text()
    }

    /**
     * Populate populate Transaction History
     */
    def populateTransactionHistory() {
        try {

            final String apiQuery =
                    """BEGIN
                   DELETE FROM FGBTRNH WHERE FGBTRNH_DOC_CODE=?;
                   INSERT INTO FGBTRNH (
                        FGBTRNH_RUCL_CODE,
                        FGBTRNH_DOC_SEQ_CODE,
                        FGBTRNH_DOC_CODE,
                        FGBTRNH_SUBMISSION_NUMBER,
                        FGBTRNH_ITEM_NUM,
                        FGBTRNH_SEQ_NUM,
                        FGBTRNH_SERIAL_NUM,
                        FGBTRNH_USER_ID,
                        FGBTRNH_TRANS_DATE,
                        FGBTRNH_TRANS_AMT,
                        FGBTRNH_TRANS_DESC,
                        FGBTRNH_DR_CR_IND,
                        FGBTRNH_COAS_CODE,
                        FGBTRNH_FSYR_CODE,
                        FGBTRNH_FUND_CODE,
                        FGBTRNH_ORGN_CODE,
                        FGBTRNH_ACCT_CODE,
                        FGBTRNH_PROG_CODE,
                        FGBTRNH_POSTING_PERIOD,
                        FGBTRNH_ENCD_NUM,
                        FGBTRNH_ENCD_ITEM_NUM,
                        FGBTRNH_ENCD_SEQ_NUM,
                        FGBTRNH_REVERSAL_IND,
                        FGBTRNH_CMT_TYPE,
                        FGBTRNH_ENCB_TYPE,
                        FGBTRNH_VENDOR_PIDM,
                        FGBTRNH_ABAL_OVERRIDE,
                        FGBTRNH_CONVERT_AMT,
                        FGBTRNH_EXCHANGE_AMT,
                        FGBTRNH_EXCHANGE_DIFF,
                        FGBTRNH_SUM_POST_DOC_CODE,
                        FGBTRNH_ACTIVITY_DATE,
                        FGBTRNH_DATA_ORIGIN)
                       VALUES (
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       ?,
                       sysdate,
                       'GRAILS');
                       COMMIT;
                     END;
                    """
            CallableStatement insertCall = this.connectCall.prepareCall(apiQuery)
            insertCall.setString(1, this.docCode)

            insertCall.setString(2, this.ruclCode)
            insertCall.setString(3, this.docSeqCode)
            insertCall.setString(4, this.docCode)
            insertCall.setString(5, this.submissionNumber)
            insertCall.setString(6, this.itemNum)
            insertCall.setString(7, this.seqNum)
            insertCall.setString(8, this.serialNum)
            insertCall.setString(9, this.userId)
            insertCall.setString(10, this.transDate)
            insertCall.setDouble(11, this.transAmt)
            insertCall.setString(12, this.transDesc)
            insertCall.setString(13, this.drCrInd)
            insertCall.setString(14, this.coasCode)
            insertCall.setString(15, this.fsyrCode)
            insertCall.setString(16, this.fundCode)
            insertCall.setString(17, this.orgnCode)
            insertCall.setString(18, this.acctCode)
            insertCall.setString(19, this.progCode)
            insertCall.setString(20, this.postingPeriod)
            insertCall.setString(21, this.encdNum)
            insertCall.setString(22, this.encdItemNum)
            insertCall.setString(23, this.encdSeqNum)
            insertCall.setString(24, this.reversalInd)
            insertCall.setString(25, this.cmtType)
            insertCall.setString(26, this.encbType)
            insertCall.setString(27, this.vendorPidm)
            insertCall.setString(28, this.abalOverride)
            insertCall.setDouble(29, this.convertAmt)
            insertCall.setDouble(30, this.exchangeAmt)
            insertCall.setDouble(31, this.exchangeDiff)
            insertCall.setString(32, this.sumPostDocCode)
            insertCall.execute()
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_TRANSACTION_HISTORY", 0, 1, 0, 0, 0)

        }
        catch (Exception e) {
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_TRANSACTION_HISTORY", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Executing BA Transaction history" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
