/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import net.hedtech.banner.seeddata.InputData

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget availability view pending documents
 */
public class FinanceBudgetAvailabilityViewPendingDocumentsHistoryDML {


    def docNum
	def itemNum
    def submissionNum
	def seqNum
	def rulpCode
	def commitmentType
	def coasCode
	def fsyrCode
	def fspdCode
	def fundTran
	def orgTran
	def acctTran
	def progTran
    def adopAmt
    def adjtAmt
    def ytdAmt
    def encbAmt
    def rsrvAmt

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityViewPendingDocumentsHistoryDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityViewPendingDocumentsHistoryDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateViewPendingDocuments()
    }


    def parseXmlData() {
        def viewPendingDocumentsXMLData = new XmlParser().parseText( xmlData )
        this.docNum = viewPendingDocumentsXMLData.FGRBAKO_DOC_NUM.text()
        this.itemNum = viewPendingDocumentsXMLData.FGRBAKO_ITEM_NUM.text()
        this.submissionNum = viewPendingDocumentsXMLData.FGRBAKO_SUBMISSION_NUMBER.text()
        this.seqNum = viewPendingDocumentsXMLData.FGRBAKO_SEQ_NUM.text()
		this.rulpCode = viewPendingDocumentsXMLData.FGRBAKO_RULP_CODE.text()
        this.commitmentType = viewPendingDocumentsXMLData.FGRBAKO_CMT_TYPE.text()
		this.coasCode = viewPendingDocumentsXMLData.FGRBAKO_COAS_CODE.text()
        this.fsyrCode = viewPendingDocumentsXMLData.FGRBAKO_FSYR_CODE.text()
		this.fspdCode = viewPendingDocumentsXMLData.FGRBAKO_FSPD_CODE.text()
        this.fundTran = viewPendingDocumentsXMLData.FGRBAKO_FUND_TRAN.text()
        this.orgTran = viewPendingDocumentsXMLData.FGRBAKO_ORGN_TRAN.text()
        this.acctTran = viewPendingDocumentsXMLData.FGRBAKO_ACCT_TRAN.text()
        this.progTran = viewPendingDocumentsXMLData.FGRBAKO_PROG_TRAN.text()
        this.adopAmt = viewPendingDocumentsXMLData.FGRBAKO_ADOP_AMT.text() as double
        this.adjtAmt = viewPendingDocumentsXMLData.FGRBAKO_ADJT_AMT.text() as double
        this.ytdAmt = viewPendingDocumentsXMLData.FGRBAKO_YTD_AMT.text() as double
        this.encbAmt = viewPendingDocumentsXMLData.FGRBAKO_ENCB_AMT.text() as double
        this.rsrvAmt = viewPendingDocumentsXMLData.FGRBAKO_RSRV_AMT.text() as double
    }

    /**
     * Populate view pending documents
     */
    def populateViewPendingDocuments() {
        try {
            final String apiQuery =
			"""BEGIN
			DELETE FROM FGRBAKO WHERE FGRBAKO_DOC_NUM=? AND FGRBAKO_ITEM_NUM=?;
			INSERT INTO FGRBAKO (
			FGRBAKO_DOC_NUM,
			FGRBAKO_ITEM_NUM,
			FGRBAKO_SUBMISSION_NUMBER,
			FGRBAKO_SEQ_NUM,
			FGRBAKO_RULP_CODE,
			FGRBAKO_CMT_TYPE,
			FGRBAKO_REVERSAL_IND,
			FGRBAKO_COAS_CODE,
			FGRBAKO_FSYR_CODE,
			FGRBAKO_FSPD_CODE,
			FGRBAKO_FUND_TRAN,
			FGRBAKO_ORGN_TRAN,
			FGRBAKO_ACCT_TRAN,
			FGRBAKO_PROG_TRAN,
			FGRBAKO_ADOP_AMT,
			FGRBAKO_ADJT_AMT,
			FGRBAKO_YTD_AMT,
			FGRBAKO_ENCB_AMT,
			FGRBAKO_RSRV_AMT,
			FGRBAKO_ACTIVITY_DATE,
			FGRBAKO_DATA_ORIGIN,
			FGRBAKO_USER_ID,
			FGRBAKO_FSPD_ORIG)
			values(
			?,
			?,
			?,
			?,
			?,
			?,
			'N',
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
			'GRAILS',
			'GRAILS',
			'01');
			COMMIT;
			END;
			"""

            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setString( 1, this.docNum )
            insertCall.setString( 2, this.itemNum )
            insertCall.setString( 3, this.docNum)
            insertCall.setString( 4, this.itemNum)
            insertCall.setString( 5, this.submissionNum)
            insertCall.setString( 6, this.seqNum)
            insertCall.setString( 7, this.rulpCode )
            insertCall.setString( 8, this.commitmentType )
            insertCall.setString( 9, this.coasCode )
            insertCall.setString( 10, this.fsyrCode )
            insertCall.setString( 11, this.fspdCode )
			insertCall.setString( 12, this.fundTran )
            insertCall.setString( 13, this.orgTran )
            insertCall.setString( 14, this.acctTran )
			insertCall.setString( 15, this.progTran )
            insertCall.setDouble( 16, this.adopAmt )
            insertCall.setDouble( 17, this.adjtAmt )
            insertCall.setDouble( 18, this.ytdAmt )
            insertCall.setDouble( 19, this.encbAmt )
            insertCall.setDouble( 20, this.rsrvAmt )
            insertCall.execute()
            connectInfo.tableUpdate( "FVQ_PENDING_DOCUMENTS_DETAILS", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FVQ_PENDING_DOCUMENTS_DETAILS", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA view pending documents" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
