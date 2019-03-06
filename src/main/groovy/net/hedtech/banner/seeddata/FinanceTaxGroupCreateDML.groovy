package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

class FinanceTaxGroupCreateDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null
    def taxGroupData

    public FinanceTaxGroupCreateDML(InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FinanceTaxGroupCreateDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        createFinanceTaxGroup()
    }

    def parseXmlData() {
        taxGroupData = new XmlParser().parseText( xmlData )
    }

    def createFinanceTaxGroup() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            "  DELETE FROM FTVTGRP WHERE FTVTGRP_TGRP_CODE = '" + taxGroupData.FTVTGRP_TGRP_CODE.text() + "' ;" +
                            "   INSERT INTO FTVTGRP ( FTVTGRP_TGRP_CODE, FTVTGRP_EFF_DATE, FTVTGRP_ACTIVITY_DATE, FTVTGRP_USER_ID, " +
                            "	FTVTGRP_TITLE, FTVTGRP_STATUS_IND, FTVTGRP_COAS_CODE_VALID, FTVTGRP_NON_TAXABLE, "+
                            "   FTVTGRP_SURROGATE_ID, FTVTGRP_VERSION) " +
                            "   VALUES (" +
                            "   ?, ?, ?, 'FORSED21', " +
                            "   ?, ?, ?, ?, " +
                            "   ?, 0);" +
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.setString( 1, taxGroupData.FTVTGRP_TGRP_CODE.text() )
                insertCall.setString( 2, taxGroupData.FTVTGRP_EFF_DATE.text() )
                insertCall.setString( 3, taxGroupData.FTVTGRP_ACTIVITY_DATE.text() )
                insertCall.setString( 4, taxGroupData.FTVTGRP_TITLE.text() )
                insertCall.setString( 5, taxGroupData.FTVTGRP_STATUS_IND.text() )
                insertCall.setString( 6, taxGroupData.FTVTGRP_COAS_CODE_VALID.text() )
                insertCall.setString( 7, taxGroupData.FTVTGRP_NON_TAXABLE.text() )
                insertCall.setString( 8, taxGroupData.FTVTGRP_SURROGATE_ID.text() )
                insertCall.execute()

                connectInfo.tableUpdate( "FTVTGRP", 0, 1, 0, 0, 0 )
            } catch (Exception e) {
                connectInfo.tableUpdate( "FTVTGRP", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to insert record for Finance Tax Group with ..."
                    println "Problem executing insert record for Finance Tax Group: $e.message"
                }
            } finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVTGRP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FTVTGRP from FinanceTaxGroupCreateDML.groovy: $e.message"
            }
        }
    }
}