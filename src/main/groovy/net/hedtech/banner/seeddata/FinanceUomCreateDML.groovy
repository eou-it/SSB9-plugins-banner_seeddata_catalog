package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceUomCreateDML{
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def uomData

    public FinanceUomCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceUomCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceUnitOfMeasure()
    }

    def parseXmlData() {
        uomData = new XmlParser().parseText( xmlData )
    }

    def createFinanceUnitOfMeasure() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FTVUOMS WHERE FTVUOMS_CODE = '" + uomData.FTVUOMS_CODE.text() + "' ;" +
                            "   INSERT INTO FTVUOMS (FTVUOMS_CODE,FTVUOMS_EFF_DATE,FTVUOMS_ACTIVITY_DATE,FTVUOMS_USER_ID," +
                            "	FTVUOMS_TERM_DATE,FTVUOMS_DESC,FTVUOMS_SCOD_CODE_X12,FTVUOMS_SCOD_CODE_EDIFACT,FTVUOMS_SURROGATE_ID, "+
                            "   FTVUOMS_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, ?, 'FORSED21', " +
                            "   ?, ?, ?, ?, " +
                            "   ?, 0);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, uomData.FTVUOMS_CODE.text() )
                insertCall.setString( 2, uomData.FTVUOMS_EFF_DATE.text() )
                insertCall.setString( 3, uomData.FTVUOMS_ACTIVITY_DATE.text() )
                insertCall.setString( 4, uomData.FTVUOMS_TERM_DATE.text() )
                insertCall.setString( 5, uomData.FTVUOMS_DESC.text() )
                insertCall.setString( 6, uomData.FTVUOMS_SCOD_CODE_X12.text() )
                insertCall.setString( 7, uomData.FTVUOMS_SCOD_CODE_EDIFACT.text() )
                insertCall.setString( 8, uomData.FTVUOMS_SURROGATE_ID.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTVUOMS", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVUOMS", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Unit Of Measure ..."
                    println "Problem executing insert record for Finance Unit Of Measure : $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVUOMS", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTVUOMS from FinanceUomCreateDML.groovy: $e.message"
            }
        }
    }
}