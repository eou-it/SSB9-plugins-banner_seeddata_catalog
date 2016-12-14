/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability to populate Transaction Detail
 */
public class FinanceBudgetAvailabilityTransactionDetailsDML {


    def accountCode
    def amtInd
    def budgetPeriod
    def commitmentType
    def coasCode
    def docCode
    def docSeqCode
    def drCrInd
    def fieldCode
    def fsyrCode
    def fundCode
    def itemNumber
    def ledgerInd
    def ordnCode
    def postingPeriod
    def procCode
    def programCode
    def reversalInd
    def ruleClassCode
    def rulePCode
    def seqNum
    def serialNumber
    def submissionNum
    def postDocCode
    def transAmt
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityTransactionDetailsDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityTransactionDetailsDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateTransactionDetail()
    }


    def parseXmlData() {
        def transDetail = new XmlParser().parseText(xmlData)
        this.accountCode = transDetail.FGBTRND_ACCT_CODE.text()
        this.amtInd = transDetail.FGBTRND_AMT_IND.text()
        this.budgetPeriod = transDetail.FGBTRND_BUDGET_PERIOD.text()
        this.commitmentType = transDetail.FGBTRND_CMT_TYPE.text()
        this.coasCode = transDetail.FGBTRND_COAS_CODE.text()
        this.docCode = transDetail.FGBTRND_DOC_CODE.text()
        this.docSeqCode = transDetail.FGBTRND_DOC_SEQ_CODE.text()
        this.drCrInd = transDetail.FGBTRND_DR_CR_IND.text()
        this.fieldCode = transDetail.FGBTRND_FIELD_CODE.text()
        this.fsyrCode = transDetail.FGBTRND_FSYR_CODE.text()
        this.fundCode = transDetail.FGBTRND_FUND_CODE.text()
        this.itemNumber = transDetail.FGBTRND_ITEM_NUM.text()
        this.ledgerInd = transDetail.FGBTRND_LEDGER_IND.text()
        this.ordnCode = transDetail.FGBTRND_ORGN_CODE.text()
        this.postingPeriod = transDetail.FGBTRND_POSTING_PERIOD.text()
        this.procCode = transDetail.FGBTRND_PROC_CODE.text()
        this.programCode = transDetail.FGBTRND_PROG_CODE.text()
        this.reversalInd = transDetail.FGBTRND_REVERSAL_IND.text()
        this.ruleClassCode = transDetail.FGBTRND_RUCL_CODE.text()
        this.rulePCode = transDetail.FGBTRND_RULP_CODE.text()
        this.seqNum = transDetail.FGBTRND_SEQ_NUM.text()
        this.serialNumber = transDetail.FGBTRND_SERIAL_NUM.text()
        this.submissionNum = transDetail.FGBTRND_SUBMISSION_NUMBER.text()
        this.postDocCode = transDetail.FGBTRND_SUM_POST_DOC_CODE.text()
        this.transAmt = transDetail.FGBTRND_TRANS_AMT.text() as double
    }

    /**
     * Populate populate Transaction Detail
     */
    def populateTransactionDetail() {
        try {

            final String apiQuery =
                    """BEGIN
                       DELETE FROM FGBTRND WHERE FGBTRND_DOC_CODE=?;
                       INSERT INTO FGBTRND (
                       FGBTRND_ACCT_CODE,
                       FGBTRND_AMT_IND,
                       FGBTRND_BUDGET_PERIOD,
                       FGBTRND_CMT_TYPE ,
                       FGBTRND_COAS_CODE,
                       FGBTRND_DOC_CODE,
                       FGBTRND_DOC_SEQ_CODE,
                       FGBTRND_DR_CR_IND,
                       FGBTRND_FIELD_CODE,
                       FGBTRND_FSYR_CODE,
                       FGBTRND_FUND_CODE,
                       FGBTRND_ITEM_NUM,
                       FGBTRND_LEDGER_IND,
                       FGBTRND_ORGN_CODE,
                       FGBTRND_POSTING_PERIOD,
                       FGBTRND_PROC_CODE,
                       FGBTRND_PROG_CODE,
                       FGBTRND_REVERSAL_IND,
                       FGBTRND_RUCL_CODE,
                       FGBTRND_RULP_CODE,
                       FGBTRND_SEQ_NUM,
                       FGBTRND_SERIAL_NUM,
                       FGBTRND_SUBMISSION_NUMBER,
                       FGBTRND_SUM_POST_DOC_CODE,
                       FGBTRND_TRANS_AMT,
                       FGBTRND_USER_ID,
                       FGBTRND_ACTIVITY_DATE,
                       FGBTRND_DATA_ORIGIN
                       )
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
                       'GRAILS',
                       sysdate,
                       'GRAILS');
                       COMMIT;
                     END;
                    """
            CallableStatement insertCall = this.connectCall.prepareCall(apiQuery)
            insertCall.setString(1, this.docCode)

            insertCall.setString(2, this.accountCode)
            insertCall.setString(3, this.amtInd)
            insertCall.setString(4, this.budgetPeriod)
            insertCall.setString(5, this.commitmentType)
            insertCall.setString(6, this.coasCode)
            insertCall.setString(7, this.docCode)
            insertCall.setString(8, this.docSeqCode)
            insertCall.setString(9, this.drCrInd)
            insertCall.setString(10, this.fieldCode)
            insertCall.setString(11, this.fsyrCode)
            insertCall.setString(12, this.fundCode)
            insertCall.setString(13, this.itemNumber)
            insertCall.setString(14, this.ledgerInd)
            insertCall.setString(15, this.ordnCode)
            insertCall.setString(16, this.postingPeriod)
            insertCall.setString(17, this.procCode)
            insertCall.setString(18, this.programCode)
            insertCall.setString(19, this.reversalInd)
            insertCall.setString(20, this.ruleClassCode)
            insertCall.setString(21, this.rulePCode)
            insertCall.setString(22, this.seqNum)
            insertCall.setString(23, this.serialNumber)
            insertCall.setString(24, this.submissionNum)
            insertCall.setString(25, this.postDocCode)
            insertCall.setDouble(26, this.transAmt)

            insertCall.execute()
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_TRANSACTION_DETAILS", 0, 1, 0, 0, 0)

        }
        catch (Exception e) {
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_TRANSACTION_DETAILS", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Executing BA Transaction Detail" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
