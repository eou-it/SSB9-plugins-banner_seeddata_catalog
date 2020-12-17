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
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceControlAccount();
    }

    def parseXmlData() {
        accountData = new XmlParser().parseText( xmlData )
    }



    def createFinanceControlAccount() {
        final String sql = """ INSERT INTO FTVACTL (FTVACTL_COAS_CODE, FTVACTL_ACTIVITY_DATE, FTVACTL_ATYP_CODE ,FTVACTL_SEQ_NUM, FTVACTL_EFF_DATE, FTVACTL_STATUS_IND, FTVACTL_ACCT_CODE_CONTROL, FTVACTL_ACCT_CODE_OFFSET, FTVACTL_ACCT_CODE_CONTROL_PY, FTVACTL_ACCT_CODE_OFFSET_PY, FTVACTL_USER_ID) VALUES ( ?, sysdate, ?, ?, ?, ?, ?, ?, ?, ?,'GRAILS') """;
        try {
                conn.execute(""" DELETE from FTVACTL where FTVACTL_COAS_CODE=? AND FTVACTL_ATYP_CODE=? AND FTVACTL_SEQ_NUM=? """, [accountData.FTVACTL_COAS_CODE.text(), accountData.FTVACTL_ATYP_CODE.text(), accountData.FTVACTL_SEQ_NUM.text()])
                conn.executeInsert(sql, [accountData.FTVACTL_COAS_CODE.text(), accountData.FTVACTL_ATYP_CODE.text(), accountData.FTVACTL_SEQ_NUM.text(), accountData.FTVACTL_EFF_DATE.text(), accountData.FTVACTL_STATUS_IND.text(), accountData.FTVACTL_ACCT_CODE_CONTROL.text(), accountData.FTVACTL_ACCT_CODE_OFFSET.text(), accountData.FTVACTL_ACCT_CODE_CONTROL_PY.text(), accountData.FTVACTL_ACCT_CODE_OFFSET_PY.text()]);
                connectInfo.tableUpdate('FTVACTL', 0, 1, 0, 0, 0)
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVACTL", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Control account with ..."
                    println "Problem executing insert record for Finance Control account: $e.message"
                }
            }
        }

}