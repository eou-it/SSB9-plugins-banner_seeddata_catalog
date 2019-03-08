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
public class FinanceBudgetAvailabilityInvoiceCreateDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def headerData


    public FinanceBudgetAvailabilityInvoiceCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityInvoiceCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
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
                            " DELETE FROM  FABINVH where FABINVH_CODE= '" + headerData.FABINVH_CODE.text() + "' ;" +
                            " INSERT INTO FABINVH (FABINVH_CODE, FABINVH_OPEN_PAID_IND, FABINVH_USER_ID," +
                            " FABINVH_CR_MEMO_IND, FABINVH_HOLD_IND, FABINVH_SUSP_IND, FABINVH_SUSP_IND_ADDL," +
                            " FABINVH_CANCEL_IND, FABINVH_INVOICE_TYPE_IND, FABINVH_MATCH_REQUIRED,FABINVH_VEND_HOLD_OVRD_IND," +
                            " FABINVH_SURROGATE_ID,FABINVH_VEND_PIDM,FABINVH_ATYP_SEQ_NUM,FABINVH_VERSION,FABINVH_ACTIVITY_DATE)" +
                            "   VALUES (" +
                                        "   ?, ?,'FORSED21', ?,?, " +
                                        "   ?,  ?, ?, ?, ? , ?," +
                                        "   ?,?,?, 0, sysdate );" +
                                        "   commit;" +
                                        "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, headerData.FABINVH_CODE.text() )
                insertCall.setString( 2, headerData.FABINVH_OPEN_PAID_IND.text() )
                insertCall.setString( 3, headerData.FABINVH_CR_MEMO_IND.text() )
                insertCall.setString( 4, headerData.FABINVH_HOLD_IND.text() )
                insertCall.setString( 5, headerData.FABINVH_SUSP_IND.text() )
                insertCall.setString( 6, headerData.FABINVH_SUSP_IND_ADDL.text() )
                insertCall.setString( 7, headerData.FABINVH_CANCEL_IND.text() )
                insertCall.setString( 8, headerData.FABINVH_INVOICE_TYPE_IND.text() )
                insertCall.setString( 9, headerData.FABINVH_MATCH_REQUIRED.text() )
                insertCall.setString( 10, headerData.FABINVH_VEND_HOLD_OVRD_IND.text() )
                insertCall.setString( 11, headerData.FABINVH_SURROGATE_ID.text() )
                insertCall.setString( 12, headerData.FABINVH_VEND_PIDM.text() )
                insertCall.setString( 13, headerData.FABINVH_ATYP_SEQ_NUM.text() )

                insertCall.execute()

                connectInfo.tableUpdate( "FABINVH", 0, 1, 0, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FABINVH", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for invoice ..."
                    println "Problem executing insert record for invoice: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FABINVH", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FABINVH from FinanceBudgetAvailabilityInvoiceCreateDML.groovy: $e.message"
            }
        }
    }
}
