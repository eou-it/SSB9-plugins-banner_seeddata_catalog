/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceControlAccountCreateDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def accountData

    public FinanceControlAccountCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceControlAccountCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        if(checkRecords()) {
            createFinanceControlAccount()
        }
    }

    def parseXmlData() {
        accountData = new XmlParser().parseText( xmlData )
    }

    private checkRecords() {
        def record =true;
        def findRecord = """select * from FTVACTL where FTVACTL_ATYP_CODE in ('50', '60', '70', '80')"""

        try {
            def accountRows = conn.firstRow(findRecord)
            if (accountRows) {
                record = false;
            }
        } catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem finding the records for FTVACTL table"
            }
        }

        return record
    }

    def createFinanceControlAccount() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "   INSERT INTO FTVACTL (FTVACTL_COAS_CODE, FTVACTL_ACTIVITY_DATE, FTVACTL_ATYP_CODE ,FTVACTL_SEQ_NUM, FTVACTL_EFF_DATE, FTVACTL_STATUS_IND, " +
                            "	FTVACTL_ACCT_CODE_CONTROL, FTVACTL_ACCT_CODE_OFFSET, FTVACTL_ACCT_CODE_CONTROL_PY, FTVACTL_ACCT_CODE_OFFSET_PY, FTVACTL_USER_ID) "+
                            "   VALUES (" +
                            "   ?, sysdate, ?, ?, ?, " +
                            "   ?, ?, ?, ?, ?,'GRAILS' " +
                            "   commit;" +
                    "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, accountData.FTVACTL_COAS_CODE.text() )
                insertCall.setString( 2, accountData.FTVACTL_ATYP_CODE.text() )
                insertCall.setString( 3, accountData.FTVACTL_SEQ_NUM.text() )
                insertCall.setString( 4, accountData.FTVACTL_EFF_DATE.text() )
                insertCall.setString( 5, accountData.FTVACTL_STATUS_IND.text() )
                insertCall.setString( 6, accountData.FTVACTL_ACCT_CODE_CONTROL.text() )
                insertCall.setString( 7, accountData.FTVACTL_ACCT_CODE_OFFSET.text() )
                insertCall.setString( 8, accountData.FTVACTL_ACCT_CODE_CONTROL_PY.text() )
                insertCall.setString( 9, accountData.FTVACTL_ACCT_CODE_OFFSET_PY.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTVACTL", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVACTL", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Control account with ..."
                    println "Problem executing insert record for Finance Control account: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVACTL", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTVACTL from FinanceControlAccountCreateDML.groovy: $e.message"
            }
        }
    }
}