/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import org.apache.commons.lang.StringUtils

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability Foapal program
 */
public class FinanceBudgetAvailabilityFoapalProgramDML {


    def programCodePattern, programCodeDescriptionPattern, startCount, totalCount, coaCode

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityFoapalProgramDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityFoapalProgramDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateProgram()
    }


    def parseXmlData() {
        def accountCreation = new XmlParser().parseText( xmlData )
        this.programCodePattern = accountCreation.PROGRAM_CODE_PATTERN?.text()
        this.programCodeDescriptionPattern = accountCreation.PROGRAM_CODE_DESCRIPTION_PATTERN?.text()
        this.coaCode = accountCreation.PROGRAM_COA_CODE?.text()
        boolean isEmpty = StringUtils.isEmpty accountCreation.START_COUNT?.text()
        this.startCount = isEmpty ? 0 : accountCreation.START_COUNT?.text() as int
        isEmpty = StringUtils.isEmpty accountCreation.TOTAL_COUNT?.text()
        this.totalCount = isEmpty ? 0 : accountCreation.TOTAL_COUNT?.text() as int
    }

    /**
     * Populate Program
     */
    def populateProgram() {
        try {
            final String apiQuery =
                            "\tbegin\n" +
                            "  FOR ind IN ? .. ?\n" +
                            "  loop\n" +
                            "    delete from FTVPROG where FTVPROG_PROG_CODE=replace(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ','') AND FTVPROG_COAS_CODE = ?;\n" +
                            "      insert into  ftvprog (\n" +
                            "   FTVPROG_ACTIVITY_DATE,\n" +
                            "    FTVPROG_COAS_CODE,\n" +
                            "   FTVPROG_DATA_ENTRY_IND,\n" +
                            "    FTVPROG_DATA_ORIGIN,\n" +
                            "    FTVPROG_EFF_DATE,\n" +
                            "   FTVPROG_NCHG_DATE,\n" +
                            "    FTVPROG_PROG_CODE,\n" +
                            "    FTVPROG_STATUS_IND,\n" +
                            "    ftvprog_title,\n" +
                            "    FTVPROG_USER_ID\n)\n" +
                            "   values(\n" +
                            "        '01-MAY-2010',\n" +
                            "        ?,\n" +
                            "        'Y',\n" +
                            "        'GRAILS',\n" +
                            "        '01-MAY-2010',\n" +
                            "        '31-DEC-2099',\n" +
                            "        REPLACE(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ',''),\n" +
                            "        'A',\n" +
                            "        REPLACE(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ',''),\n" +
                            "        'GRAILS'\n" +
                            ");\n" +
                            "  END LOOP;\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setInt( 1, this.startCount )
            insertCall.setInt( 2, this.totalCount )

            insertCall.setString( 3, this.programCodePattern )
            insertCall.setString( 4, this.coaCode )
            insertCall.setString( 5, this.coaCode )

            insertCall.setString( 6, this.programCodePattern )

            insertCall.setString( 7, this.programCodeDescriptionPattern )
            insertCall.execute()
            connectInfo.tableUpdate( "FTVPROG", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVPROG", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA_FOAPAL_FTVPROG" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
