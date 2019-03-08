package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceTaxRateCreateDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def taxRateData

    public FinanceTaxRateCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceTaxRateCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceTaxRate()
    }

    def parseXmlData() {
        taxRateData = new XmlParser().parseText( xmlData )
    }

    def createFinanceTaxRate() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FTVTRAT WHERE FTVTRAT_CODE = '" + taxRateData.FTVTRAT_CODE.text() + "' ;" +
                            "   INSERT INTO FTVTRAT ( FTVTRAT_CODE, FTVTRAT_EFF_DATE, FTVTRAT_NCHG_DATE, FTVTRAT_ACTIVITY_DATE, FTVTRAT_USER_ID, " +
                            "	FTVTRAT_DESC, FTVTRAT_RATE, FTVTRAT_STATUS_IND, FTVTRAT_PIDM_TAXING_AUTHORITY, FTVTRAT_PAY_TAX_TO, "+
                            "   FTVTRAT_COAS_CODE, FTVTRAT_FUND_CODE, FTVTRAT_ACCT_CODE, FTVTRAT_PRIORITY_CODE, FTVTRAT_ADDL_CHRG_IND, FTVTRAT_SURROGATE_ID, FTVTRAT_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, ?, ?, 'FORSED21', " +
                            "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                            "   ?, 0);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, taxRateData.FTVTRAT_CODE.text() )
                insertCall.setString( 2, taxRateData.FTVTRAT_EFF_DATE.text() )
                insertCall.setString( 3, taxRateData.FTVTRAT_NCHG_DATE.text() )
                insertCall.setString( 4, taxRateData.FTVTRAT_ACTIVITY_DATE.text() )
                insertCall.setString( 5, taxRateData.FTVTRAT_DESC.text() )
                insertCall.setString( 6, taxRateData.FTVTRAT_RATE.text() )
                insertCall.setString( 7, taxRateData.FTVTRAT_STATUS_IND.text() )
                insertCall.setString( 8, taxRateData.FTVTRAT_PIDM_TAXING_AUTHORITY.text() )
                insertCall.setString( 9, taxRateData.FTVTRAT_PAY_TAX_TO.text() )
                insertCall.setString( 10, taxRateData.FTVTRAT_COAS_CODE.text() )
                insertCall.setString( 11, taxRateData.FTVTRAT_FUND_CODE.text() )
                insertCall.setString( 12, taxRateData.FTVTRAT_ACCT_CODE.text() )
                insertCall.setString( 13, taxRateData.FTVTRAT_PRIORITY_CODE.text() )
                insertCall.setString( 14, taxRateData.FTVTRAT_ADDL_CHRG_IND.text() )
                insertCall.setString( 15, taxRateData.FTVTRAT_SURROGATE_ID.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTVTRAT", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVTRAT", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Tax Rate with ..."
                    println "Problem executing insert record for Finance Tax Rate: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVTRAT", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTVTRAT from FinanceTaxRateCreateDML.groovy: $e.message"
            }
        }
    }
}