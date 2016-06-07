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
 *  DML for FinanceBudget Availability Foapal Organization
 */
public class FinanceBudgetAvailabilityFoapalOrganizationDML {


    def ORG_CODE, ORG_DESC, FTVORGN_COAS_CODE, FTVORGN_ORGN_CODE_PRED, TOTAL_COUNT, ORGCODE_PATTERN, START_COUNT, ORGDESC_PATTERN

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityFoapalOrganizationDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityFoapalOrganizationDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateOrganization()
    }


    def parseXmlData() {
        def userCreationData = new XmlParser().parseText( xmlData )
        this.ORG_CODE = userCreationData.ORG_CODE?.text()
        this.FTVORGN_COAS_CODE = userCreationData.FTVORGN_COAS_CODE?.text()
        boolean isEmpty = StringUtils.isEmpty userCreationData.TOTAL_COUNT?.text()
        this.TOTAL_COUNT = isEmpty ? 0 : userCreationData.TOTAL_COUNT?.text() as int
        this.FTVORGN_ORGN_CODE_PRED = userCreationData.FTVORGN_ORGN_CODE_PRED?.text()
        this.ORG_DESC = userCreationData.ORG_DESC?.text()
        this.FTVORGN_ORGN_CODE_PRED = userCreationData.FTVORGN_ORGN_CODE_PRED?.text()
        isEmpty = StringUtils.isEmpty userCreationData.START_COUNT?.text()
        this.START_COUNT = isEmpty ? 0 : userCreationData.START_COUNT?.text() as int
        this.ORGCODE_PATTERN = userCreationData.ORGCODE_PATTERN?.text()
        this.ORGDESC_PATTERN = userCreationData.ORGDESC_PATTERN?.text()
    }

    /**
     * Populate Organization
     */
    def populateOrganization() {
        try {
            final String apiQuery =
                    "   BEGIN\n" +
                            " IF ? IS NULL THEN\n" +
                            " DELETE FROM FTVORGN WHERE FTVORGN_COAS_CODE=? AND FTVORGN_ORGN_CODE = ? ;\n" +
                            " INSERT INTO  FTVORGN (FTVORGN_COAS_CODE,\n" +
                            " FTVORGN_ORGN_CODE,\n" +
                            " FTVORGN_EFF_DATE,\n" +
                            " FTVORGN_ACTIVITY_DATE,\n" +
                            " FTVORGN_USER_ID,\n" +
                            " FTVORGN_NCHG_DATE,\n" +
                            " FTVORGN_TITLE,\n" +
                            " FTVORGN_STATUS_IND,\n" +
                            " FTVORGN_ORGN_CODE_PRED,\n" +
                            " FTVORGN_DATA_ENTRY_IND,\n" +
                            " FTVORGN_DATA_ORIGIN,\n" +
                            " FTVORGN_HIERARCHY_TABLE_IND) VALUES(?, ?, '31-MAY-10', \n" +
                            " '31-MAY-10', 'GRAILS', '31-DEC-2099', ?, 'A', ?, 'Y', 'GRAILS', 'N');\n" +
                            " ElSIF ? IS NOT NULL THEN\n" +
                            "  FOR ind in ?..? LOOP\n" +
                            " DELETE FROM FTVORGN WHERE FTVORGN_COAS_CODE=? AND FTVORGN_ORGN_CODE = ? || replace(TO_CHAR(ind, '00099'), ' ', '') ;\n" +
                            " INSERT INTO  FTVORGN (FTVORGN_COAS_CODE,\n" +
                            " FTVORGN_ORGN_CODE,\n" +
                            " FTVORGN_EFF_DATE,\n" +
                            " FTVORGN_ACTIVITY_DATE,\n" +
                            " FTVORGN_USER_ID,\n" +
                            " FTVORGN_NCHG_DATE,\n" +
                            " FTVORGN_TITLE,\n" +
                            " FTVORGN_STATUS_IND,\n" +
                            " FTVORGN_ORGN_CODE_PRED,\n" +
                            " FTVORGN_DATA_ENTRY_IND,\n" +
                            " FTVORGN_DATA_ORIGIN,\n" +
                            " FTVORGN_HIERARCHY_TABLE_IND) VALUES(?, ? || replace( TO_CHAR(ind, '00099'), ' ', ''), '31-MAY-10', \n" +
                            " '31-MAY-10', 'GRAILS', '31-DEC-2099', ? || replace(TO_CHAR(ind, '00099'), ' ', ''), 'A', ?, 'Y', 'GRAILS', 'N');\n" +
                            "  END LOOP;\n" +
                            " END  IF ;\n" +
                            " commit;\n" +
                            " END;\n"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            insertCall.setString( 1, StringUtils.isEmpty( this.ORGCODE_PATTERN ) ? null : this.ORGCODE_PATTERN )
            insertCall.setString( 2, this.FTVORGN_COAS_CODE )
            insertCall.setString( 3, this.ORG_CODE )
            insertCall.setString( 4, this.FTVORGN_COAS_CODE )
            insertCall.setString( 5, this.ORG_CODE )
            insertCall.setString( 6, this.ORG_DESC )
            insertCall.setString( 7, this.FTVORGN_ORGN_CODE_PRED )
            insertCall.setString( 8, StringUtils.isEmpty( this.ORGCODE_PATTERN ) ? null : this.ORGCODE_PATTERN )
            insertCall.setInt( 9, this.START_COUNT )
            insertCall.setInt( 10, this.TOTAL_COUNT )
            insertCall.setString( 11, this.FTVORGN_COAS_CODE )
            insertCall.setString( 12, this.ORGCODE_PATTERN)
            insertCall.setString( 13, this.FTVORGN_COAS_CODE )
            insertCall.setString( 14, this.ORGCODE_PATTERN )
            insertCall.setString( 15, this.ORGDESC_PATTERN )
            insertCall.setString( 16, this.FTVORGN_ORGN_CODE_PRED )
            insertCall.execute()
            connectInfo.tableUpdate( "FTVORGN", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVORGN", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA_FOAPAL_ORGANIZATIONS"
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
