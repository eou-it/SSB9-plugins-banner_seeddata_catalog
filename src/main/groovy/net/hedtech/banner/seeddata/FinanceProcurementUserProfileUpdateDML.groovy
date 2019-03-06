/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement User profile Update
 */
public class FinanceProcurementUserProfileUpdateDML {


    def oracle_id

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceProcurementUserProfileUpdateDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceProcurementUserProfileUpdateDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        updateFinanceProcurementUser()
    }


    def parseXmlData() {
        def userCreationData = new XmlParser().parseText( xmlData )
        this.oracle_id = userCreationData.FINANCEUSER_ORACLE_ID.text()
    }

    /**
     * Update finance procurement user
     */
    def updateFinanceProcurementUser() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  UPDATE  FOBPROF SET  FOBPROF_MASTER_FUND_IND='B', FOBPROF_MASTER_ORGN_IND  ='B' WHERE FOBPROF_USER_ID = ?;" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            insertCall.setString( 1, this.oracle_id )
            insertCall.execute()
            connectInfo.tableUpdate( "FOBPROF", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FOBPROF", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing FOBPROF script with ${this.oracle_id}"
            }
        }
        finally {
            insertCall.close()
        }
    }
}
