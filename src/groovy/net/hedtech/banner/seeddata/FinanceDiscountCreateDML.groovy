package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceDiscountCreateDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def discountData

    public FinanceDiscountCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceDiscountCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceDiscount()
    }

    def parseXmlData() {
        discountData = new XmlParser().parseText( xmlData )
    }

    def createFinanceDiscount() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FTVDISC WHERE FTVDISC_CODE = '" + discountData.FTVDISC_NUM.text() + "' ;" +
                            "   INSERT INTO FTVDISC ( FTVDISC_CODE, FTVDISC_EFF_DATE, FTVDISC_ACTIVITY_DATE, FTVDISC_USER_ID, " +
                            "	FTVDISC_DESC, FTVDISC_DAYS, FTVDISC_NET_DAYS, FTVDISC_PCT, FTVDISC_END_OF_MONTH_IND, "+
                            "   FTVDISC_SURROGATE_ID, FTVDISC_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, ?, 'FORSED21', " +
                            "   ?, ?, ?, ?, 'N', " +
                            "   ?, 0);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, discountData.FTVDISC_CODE.text() )
                insertCall.setString( 2, discountData.FTVDISC_EFF_DATE.text() )
                insertCall.setString( 3, discountData.FTVDISC_ACTIVITY_DATE.text() )
                insertCall.setString( 4, discountData.FTVDISC_DESC.text() )
                insertCall.setString( 5, discountData.FTVDISC_DAYS.text() )
                insertCall.setString( 6, discountData.FTVDISC_NET_DAYS.text() )
                insertCall.setString( 7, discountData.FTVDISC_PCT.text() )
                insertCall.setString( 8, discountData.FTVDISC_SURROGATE_ID.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTVDISC", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVDISC", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance discount with ..."
                    println "Problem executing insert record for Finance discount: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVDISC", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTVDISC from FinanceDiscountCreateDML.groovy: $e.message"
            }
        }
    }
}