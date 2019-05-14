/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Journal Detail creation
 */
public class FinanceJournalDetailCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def headerData


    public FinanceJournalDetailCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceJournalDetailCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinancePrJournalDetail()
    }


    def parseXmlData() {
        headerData = new XmlParser().parseText( xmlData )
    }

    /**
     * Creates finance Journal Detail
     */
    def createFinancePrJournalDetail() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "   INSERT INTO FGBJVCD (FGBJVCD_DOC_NUM, FGBJVCD_SUBMISSION_NUMBER, FGBJVCD_SEQ_NUM, FGBJVCD_RUCL_CODE, FGBJVCD_TRANS_AMT," +
                            "   FGBJVCD_TRANS_DESC, FGBJVCD_DR_CR_IND, FGBJVCD_FSYR_CODE, FGBJVCD_COAS_CODE, FGBJVCD_FUND_CODE," +
                            "   FGBJVCD_ORGN_CODE, FGBJVCD_ACCT_CODE, FGBJVCD_PROG_CODE, FGBJVCD_BANK_CODE, FGBJVCD_ACCT_CODE_CASH," +
                            "   FGBJVCD_POSTING_PERIOD, FGBJVCD_BUDGET_PERIOD, FGBJVCD_STATUS_IND, FGBJVCD_ABAL_OVERRIDE, FGBJVCD_POST_BAVL," +
                            "   FGBJVCD_DIST_PCT, FGBJVCD_GIFT_DATE," +
                            "   FGBJVCD_ACTIVITY_DATE, FGBJVCD_USER_ID) " +
                            "   VALUES (" +
                            "   ?, ?, ?, ?, ?," +
                            "   ?, ?, ?, ?, ?," +
                            "   ?, ?, ?, ?, ?," +
                            "   ?, ?, ?, ?, ?," +
                            "   ?, ?," +
                            "   sysdate, 'FORSED21');" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, headerData.FGBJVCD_DOC_NUM.text() )
                insertCall.setString( 2, headerData.FGBJVCD_SUBMISSION_NUMBER.text() )
                insertCall.setString( 3, headerData.FGBJVCD_SEQ_NUM.text() )
                insertCall.setString( 4, headerData.FGBJVCD_RUCL_CODE.text() )
                insertCall.setString( 5, headerData.FGBJVCD_TRANS_AMT.text() )

                insertCall.setString( 6, headerData.FGBJVCD_TRANS_DESC.text() )
                insertCall.setString( 7, headerData.FGBJVCD_DR_CR_IND.text() )
                insertCall.setString( 8, headerData.FGBJVCD_FSYR_CODE.text() )
                insertCall.setString( 9, headerData.FGBJVCD_COAS_CODE.text() )
                insertCall.setString( 10, headerData.FGBJVCD_FUND_CODE.text() )

                insertCall.setString( 11, headerData.FGBJVCD_ORGN_CODE.text() )
                insertCall.setString( 12, headerData.FGBJVCD_ACCT_CODE.text() )
                insertCall.setString( 13, headerData.FGBJVCD_PROG_CODE.text() )
                insertCall.setString( 14, headerData.FGBJVCD_BANK_CODE.text() )
                insertCall.setString( 15, headerData.FGBJVCD_ACCT_CODE_CASH.text() )

                insertCall.setString( 16, headerData.FGBJVCD_POSTING_PERIOD.text() )
                insertCall.setString( 17, headerData.FGBJVCD_BUDGET_PERIOD.text() )
                insertCall.setString( 18, headerData.FGBJVCD_STATUS_IND.text() )
                insertCall.setString( 19, headerData.FGBJVCD_ABAL_OVERRIDE.text() )
                insertCall.setString( 20, headerData.FGBJVCD_POST_BAVL?.text() )

                insertCall.setString( 21, headerData.FGBJVCD_DIST_PCT?.text())
                insertCall.setString( 22, headerData.FGBJVCD_GIFT_DATE?.text())
                insertCall.execute()

                connectInfo.tableUpdate( "FV_FGBJVCD", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FV_FGBJVCD", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Journal Detail with ..."
                    println "Problem executing insert record for Journal Detail : $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FV_FGBJVCD", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FGBJVCD from FinanceJournalDetailCreateDML.groovy: $e.message"
            }
        }
    }
}
