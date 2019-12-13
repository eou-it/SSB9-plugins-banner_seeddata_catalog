/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement order Header creation
 */
public class FinancePurchaseOrderHeaderCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def headerData


    public FinancePurchaseOrderHeaderCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinancePurchaseOrderHeaderCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinancePurchaseOrderHeader()
    }


    def parseXmlData() {
        headerData = new XmlParser().parseText( xmlData )
    }

    /**
     * Creates finance procurement user
     */
    def createFinancePurchaseOrderHeader() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            " DELETE FROM  FPBPOHD where FPBPOHD_CODE= '" + headerData.FPBPOHD_CODE.text() + "' ;" +
                            " INSERT INTO FPBPOHD (FPBPOHD_CODE, FPBPOHD_USER_ID, FPBPOHD_PO_DATE, FPBPOHD_TRANS_DATE," +
                            " FPBPOHD_PO_TYPE_IND, FPBPOHD_MATCH_REQUIRED, FPBPOHD_APPR_IND, FPBPOHD_COMPLETE_IND," +
                            " FPBPOHD_DATA_ORIGIN, FPBPOHD_VERSION, FPBPOHD_ACTIVITY_DATE)" +
                            "   VALUES (" +
                                        "   ?, ?, ?, ?, " +
                                        "   ?,  ?, ?, ?," +
                                        "  'GRAILS', 0, sysdate );" +
                                        "   commit;" +
                                        "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, headerData.FPBPOHD_CODE.text() )
                insertCall.setString( 2, headerData.FPBPOHD_USER_ID.text() )
                insertCall.setString( 3, headerData.FPBPOHD_PO_DATE.text() )
                insertCall.setString( 4, headerData.FPBPOHD_TRANS_DATE.text() )
                insertCall.setString( 5, headerData.FPBPOHD_PO_TYPE_IND.text() )
                insertCall.setString( 6, headerData.FPBPOHD_MATCH_REQUIRED.text() )
                insertCall.setString( 7, headerData.FPBPOHD_APPR_IND.text() )
                insertCall.setString( 8, headerData.FPBPOHD_COMPLETE_IND.text() )

                insertCall.execute()

                connectInfo.tableUpdate( "FPBPOHD", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FPBPOHD", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Header with ..."
                    println "Problem executing insert record for Header: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FPBPOHD", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FPBREQH from FinancePurchaseOrderHeaderCreateDML.groovy: $e.message"
            }
        }
    }
}
