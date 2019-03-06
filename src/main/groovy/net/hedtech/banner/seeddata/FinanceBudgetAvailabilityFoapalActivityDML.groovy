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
 *  DML for FinanceBudget Availability Foapal Activity
 */
public class FinanceBudgetAvailabilityFoapalActivityDML {


    def activityCodePattern, activityCodeDescriptionPattern, startCount, totalCount, coaCode

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityFoapalActivityDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityFoapalActivityDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populaActivity()
    }


    def parseXmlData() {
        def accountCreation = new XmlParser().parseText( xmlData )
        this.activityCodePattern = accountCreation.ACTIVITY_CODE_PATTERN?.text()
        this.activityCodeDescriptionPattern = accountCreation.ACTIVITY_TITLE_PATTERN?.text()
        this.coaCode = accountCreation.ACTIVITY_COA_CODE?.text()
        boolean isEmpty = StringUtils.isEmpty accountCreation.START_COUNT?.text()
        this.startCount = isEmpty ? 0 : accountCreation.START_COUNT?.text() as int
        isEmpty = StringUtils.isEmpty accountCreation.TOTAL_COUNT?.text()
        this.totalCount = isEmpty ? 0 : accountCreation.TOTAL_COUNT?.text() as int
    }

    /**
     * Populate Acitvity
     */
    def populaActivity() {
        try {
            final String apiQuery =
                    "\t BEGIN\n" +
                            "  FOR ind IN ? .. ?\n" +
                            "  loop\n" +
                            "    delete from FTVACTV where FTVACTV_ACTV_CODE=replace(?\n" +
                            "        ||TO_CHAR(ind, DECODE(LENGTH(?),1,'00099','0099')),' ','') AND FTVACTV_COAS_CODE = ?;\n" +
                            "INSERT INTO FTVACTV \n" +
                            "  (\n" +
                            "\t FTVACTV_ACTV_CODE,\n" +
                            "\t FTVACTV_TITLE,\n" +
                            "\t FTVACTV_COAS_CODE,\n" +
                            "\t FTVACTV_EFF_DATE,\n" +
                            "\t FTVACTV_ACTIVITY_DATE,\n" +
                            "\t FTVACTV_NCHG_DATE,\n" +
                            "\t FTVACTV_STATUS_IND,\n" +
                            "\t FTVACTV_DATA_ORIGIN,\n" +
                            "\t FTVACTV_USER_ID\n" +
                            "  )" +
                            "      VALUES\n" +
                            "      (\n" +
                            "        REPLACE(?\n" +
                            "        ||TO_CHAR(ind, DECODE(LENGTH(?),1,'00099','0099')),' ',''),\n" +
                            "        REPLACE(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ',''),\n" +
                            "        ?,\n" +
                            "        '01-MAY-2005',\n" +
                            "        '01-MAY-2005',\n" +
                            "        '31-DEC-2099',\n" +
                            "        'A',\n" +
                            "        'GRAILS',\n" +
                            "        'GRAILS'\n" +
                            "      );\n" +
                            "  END LOOP;\n" +
                            "  COMMIT;\n" +
                            "\t END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setInt( 1, this.startCount )
            insertCall.setInt( 2, this.totalCount )
            insertCall.setString( 3, this.activityCodePattern )
            insertCall.setString( 4, this.activityCodePattern )
            insertCall.setString( 5, this.coaCode )

            insertCall.setString( 6, this.activityCodePattern )
            insertCall.setString( 7, this.activityCodePattern )
            insertCall.setString( 8, this.activityCodeDescriptionPattern )
            insertCall.setString( 9, this.coaCode )

            insertCall.execute()
            connectInfo.tableUpdate( "FTVACTV", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVACTV", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA_FOAPAL_FTVACTV" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
