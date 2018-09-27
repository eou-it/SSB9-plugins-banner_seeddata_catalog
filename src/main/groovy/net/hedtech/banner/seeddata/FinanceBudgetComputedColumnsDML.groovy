/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Computed Columns
 */
public class FinanceBudgetComputedColumnsDML {


    def spridenId, forcompUserOracleId, forcompProcess, forcompTemplate, forcompCol1,
        forcompCol2, forcompCol3, forcompCompop, forcompTitle, forcompTemplateType

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetComputedColumnsDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetComputedColumnsDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateComputedColumn()
    }


    def parseXmlData() {
        def computedColumnsXMLData = new XmlParser().parseText( xmlData )

        this.spridenId = computedColumnsXMLData.FORCOMP_USER_ID.text()
        this.forcompUserOracleId = computedColumnsXMLData.FORCOMP_USER_ORACLE_ID.text()
        this.forcompProcess = computedColumnsXMLData.FORCOMP_PROCESS.text()
        this.forcompTemplate = computedColumnsXMLData.FORCOMP_TEMPLATE.text()
        this.forcompCol1 = computedColumnsXMLData.FORCOMP_COL1.text()
        this.forcompCol2 = computedColumnsXMLData.FORCOMP_COL2.text()
        this.forcompCol3 = computedColumnsXMLData.FORCOMP_COL3.text()
        this.forcompCompop = computedColumnsXMLData.FORCOMP_COMPOP.text()
        this.forcompTitle = computedColumnsXMLData.FORCOMP_TITLE.text()
        this.forcompTemplateType = computedColumnsXMLData.FORCOMP_TEMPLATE_TYPE.text()
    }

    /**
     * Populate Computed column
     */
    def populateComputedColumn() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  pidm SPRIDEN.SPRIDEN_PIDM%type;\n" +
                            "BEGIN\n" +
                            "BEGIN\n" +
                            "SELECT SPRIDEN_PIDM INTO pidm FROM SPRIDEN WHERE SPRIDEN_ID= ?;\n" +
                            "EXCEPTION\n" +
                            "\t\t\tWHEN NO_DATA_FOUND THEN\n" +
                            "\t\t\tNULL;\n" +
                            "END;\n" +
                            "DELETE FROM FORCOMP WHERE FORCOMP_PIDM = pidm AND FORCOMP_PROCESS= ? AND FORCOMP_TEMPLATE = ? AND FORCOMP_TITLE= ?;\n" +
                            "INSERT INTO FORCOMP(\n" +
                            "\t\t\tFORCOMP_PIDM, \n" +
                            "\t\t\tFORCOMP_USER_ID, \n" +
                            "\t\t\tFORCOMP_PROCESS, \n" +
                            "\t\t\tFORCOMP_TEMPLATE,\n" +
                            "\t\t\tFORCOMP_COL1, \n" +
                            "\t\t\tFORCOMP_COL2,\n" +
                            "\t\t\tFORCOMP_COL3,\n" +
                            "\t\t\tFORCOMP_COMPOP, \n" +
                            "\t\t\tFORCOMP_TITLE,\n" +
                            "\t\t\tFORCOMP_TEMPLATE_TYPE, \n" +
                            "\t\t\tFORCOMP_ACTIVITY_DATE, \n" +
                            "\t\t\tFORCOMP_DATA_ORIGIN)\n" +
                            "\t\t\tVALUES (pidm, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, 'GRAILS');\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setString( 1, this.spridenId )
            insertCall.setString( 2, this.forcompProcess )
            insertCall.setString( 3, this.forcompTemplate )
            insertCall.setString( 4, this.forcompTitle )
            insertCall.setString( 5, this.forcompUserOracleId )
            insertCall.setString( 6, this.forcompProcess )
            insertCall.setString( 7, this.forcompTemplate )
            insertCall.setString( 8, this.forcompCol1 )
            insertCall.setString( 9, this.forcompCol2 )
            insertCall.setString( 10, this.forcompCol3 )
            insertCall.setString( 11, this.forcompCompop )
            insertCall.setString( 12, this.forcompTitle )
            insertCall.setString( 13, this.forcompTemplateType )
            insertCall.execute()
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_COMPUTE_COLUMNS", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_COMPUTE_COLUMNS", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Query compute columns" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
