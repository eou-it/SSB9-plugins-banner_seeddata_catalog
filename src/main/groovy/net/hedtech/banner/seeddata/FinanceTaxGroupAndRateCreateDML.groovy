package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceTaxGroupAndRateCreateDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def taxGroupAndRateData

    public FinanceTaxGroupAndRateCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceTaxGroupAndRateCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinancetaxGroupAndRate()
    }

    def parseXmlData() {
        taxGroupAndRateData = new XmlParser().parseText( xmlData )
    }

    def createFinancetaxGroupAndRate() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FTRTGTR WHERE FTRTGTR_TGTR_CODE = '" + taxGroupAndRateData.FTRTGTR_TGTR_CODE.text() + "' ;" +
                            "   INSERT INTO FTRTGTR ( FTRTGTR_TGTR_CODE, FTRTGTR_TRAT_CODE, FTRTGTR_EFF_DATE, FTRTGTR_ACTIVITY_DATE, FTRTGTR_USER_ID, " +
                            "   FTRTGTR_SURROGATE_ID, FTRTGTR_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, ?, ?, 'FORSED21', " +
                            "   ?, 0);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, taxGroupAndRateData.FTRTGTR_TGTR_CODE.text() )
                insertCall.setString( 2, taxGroupAndRateData.FTRTGTR_TRAT_CODE.text() )
                insertCall.setString( 3, taxGroupAndRateData.FTRTGTR_EFF_DATE.text() )
                insertCall.setString( 4, taxGroupAndRateData.FTRTGTR_ACTIVITY_DATE.text() )
                insertCall.setString( 5, taxGroupAndRateData.FTRTGTR_SURROGATE_ID.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTRTGTR", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTRTGTR", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Tax Rate with ..."
                    println "Problem executing insert record for Finance Tax Group and Rate: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTRTGTR", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTRTGTR from FinancetaxGroupAndRateCreateDML.groovy: $e.message"
            }
        }
    }
}