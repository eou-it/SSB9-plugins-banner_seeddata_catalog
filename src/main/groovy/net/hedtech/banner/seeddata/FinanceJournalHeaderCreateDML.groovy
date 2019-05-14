/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Journal Header creation
 */
public class FinanceJournalHeaderCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def headerData


    public FinanceJournalHeaderCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceJournalHeaderCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceJournalHeader()
    }


    def parseXmlData() {
        headerData = new XmlParser().parseText( xmlData )
    }

    /**
     * Creates finance Journal Header record
     */
    def createFinanceJournalHeader() {
        try {
            final String updateToAutoJournal = headerData.FGBJVCH_AUTO_JRNL_ID.text()=='A1'?
                    " UPDATE FGBJVCH set FGBJVCH_AUTO_JRNL_ID = 'A1' WHERE FGBJVCH_DOC_NUM = '" + headerData.FGBJVCH_DOC_NUM.text() + "' ;" : ""
            final String updateToCompleted = headerData.FGBJVCH_STATUS_IND.text()=='C'?
                    " UPDATE FGBJVCH set FGBJVCH_STATUS_IND = 'C' WHERE FGBJVCH_DOC_NUM = '" + headerData.FGBJVCH_DOC_NUM.text() + "' ;" : ""
            final String updateToApproved =  headerData.FGBJVCH_APPROVAL_IND.text() =='Y'?
                    " UPDATE FGBJVCH set FGBJVCH_APPROVAL_IND = 'Y' WHERE FGBJVCH_DOC_NUM = '" + headerData.FGBJVCH_DOC_NUM.text() + "' ;" : ""
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FGBJVCD WHERE FGBJVCD_DOC_NUM = '" + headerData.FGBJVCH_DOC_NUM.text() + "' ;" +
                            "  DELETE FROM FGBJVCH WHERE FGBJVCH_DOC_NUM = '" + headerData.FGBJVCH_DOC_NUM.text() + "' ;" +
                            "   INSERT INTO FV_FGBJVCH ( FGBJVCH_DOC_NUM, FGBJVCH_SUBMISSION_NUMBER, FGBJVCH_ACTIVITY_DATE, FGBJVCH_USER_ID, FGBJVCH_TRANS_DATE , " +
                            "	FGBJVCH_DOC_DESCRIPTION, FGBJVCH_DOC_AMT, FGBJVCH_EDIT_DEFER_IND, FGBJVCH_STATUS_IND, FGBJVCH_APPROVAL_IND, "+
                            "   FGBJVCH_DATA_ORIGIN, FGBJVCH_CREATE_SOURCE, FGBJVCH_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, sysdate, 'FORSED21', ?," +
                            "   ?,  ?, ?, 'I', 'N', " +
                            "   'GRAILS','Banner', 0);" + updateToCompleted + updateToApproved + updateToAutoJournal +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, headerData.FGBJVCH_DOC_NUM.text() )
                insertCall.setString( 2, headerData.FGBJVCH_SUBMISSION_NUMBER.text() )
                insertCall.setString( 3, headerData.FGBJVCH_TRANS_DATE.text() )
                insertCall.setString( 4, headerData.FGBJVCH_DOC_DESCRIPTION.text() )
                insertCall.setString( 5, headerData.FGBJVCH_DOC_AMT.text() )
                insertCall.setString( 6, headerData.FGBJVCH_EDIT_DEFER_IND.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FV_FGBJVCH", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FV_FGBJVCH", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Journal Header with ..."
                    println "Problem executing insert record for Journal Header: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FV_FGBJVCH", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FGBJVCH from FinanceJournalHeaderCreateDML.groovy: $e.message"
            }
        }
    }
}
