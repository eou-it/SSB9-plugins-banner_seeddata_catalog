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
 *  DML for FinanceBudget Availability Vendor setup
 */
public class FinanceBudgetAvailabilityVendorDML {


    def oracleId, spridenId, vendorNamePattern, totalVendors
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityVendorDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityVendorDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateProgram()
    }


    def parseXmlData() {
        def accountCreation = new XmlParser().parseText( xmlData )
        this.oracleId = accountCreation.ORACLE_ID.text()
        this.spridenId = accountCreation.SPRIDEN_ID?.text()
        this.vendorNamePattern = accountCreation.VENODR_NAME_PATTERN?.text()
        boolean isEmpty = StringUtils.isEmpty accountCreation.TOTAL_VENDORS?.text()
        this.totalVendors = isEmpty ? 0 : accountCreation.TOTAL_VENDORS?.text() as int
    }

    /**
     * Populate Vendor
     */
    def populateVendor() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  -- Input for user creation\n" +
                            "  oracle_id   VARCHAR2(10) :=? ;     -- oracle id pattern\n" +
                            "  spriden_id  VARCHAR2(10) :=? ;     -- Spriden id pattern\n" +
                            "  vendor_name VARCHAR2(35) :=? ; -- Spriden id pattern\n" +
                            "  total_user  NUMBER       :=?;        -- No of vendors required to create\n" +
                            "  ------------------------------------\n" +
                            "  gv_pidm PLS_INTEGER;\n" +
                            "  user_not_exist EXCEPTION;\n" +
                            "  PRAGMA EXCEPTION_INIT (user_not_exist, -01918);\n" +
                            "  user_already_exists EXCEPTION;\n" +
                            "  PRAGMA EXCEPTION_INIT (user_already_exists, -01920);\n" +
                            "  FUNCTION f_create_banner_id(\n" +
                            "      p_id         VARCHAR2 ,\n" +
                            "      p_spriden_id VARCHAR2 ,\n" +
                            "      p_first_name VARCHAR2 ,\n" +
                            "      p_last_name  VARCHAR2)\n" +
                            "    RETURN PLS_INTEGER\n" +
                            "  IS\n" +
                            "    gv_id     VARCHAR2 (12) := p_spriden_id;\n" +
                            "    gv_pidm1  NUMBER        := NULL;\n" +
                            "    gv_rowid1 VARCHAR2 (18);\n" +
                            "    lv_result VARCHAR2 (10);\n" +
                            "  BEGIN\n" +
                            "    DELETE\n" +
                            "    FROM gobeacc\n" +
                            "    WHERE gobeacc_pidm =\n" +
                            "      (SELECT DISTINCT spriden_pidm FROM spriden WHERE spriden_id = p_id\n" +
                            "      );\n" +
                            "    DELETE FROM gobeacc WHERE gobeacc_username = p_id;\n" +
                            "    DELETE\n" +
                            "    FROM gobtpac\n" +
                            "    WHERE gobtpac_pidm =\n" +
                            "      (SELECT DISTINCT spriden_pidm FROM spriden WHERE spriden_id = p_id\n" +
                            "      );\n" +
                            "    DELETE\n" +
                            "    FROM twgrrole\n" +
                            "    WHERE twgrrole_pidm =\n" +
                            "      (SELECT DISTINCT spriden_pidm FROM spriden WHERE spriden_id = p_id\n" +
                            "      );\n" +
                            "    DELETE\n" +
                            "    FROM spriden\n" +
                            "    WHERE spriden_pidm =\n" +
                            "      (SELECT DISTINCT spriden_pidm FROM spriden WHERE spriden_id = p_id\n" +
                            "      );\n" +
                            "    IF (gb_common.f_id_exists (p_id) = 'N') THEN\n" +
                            "      gb_identification.p_create (p_id_inout => gv_id ,p_last_name => p_last_name ,p_first_name => p_first_name ,p_change_ind => NULL ,p_entity_ind => 'C' ,p_data_origin => 'FPR Create Vendor Script' ,p_pidm_inout => gv_pidm1 ,p_rowid_out => gv_rowid1);\n" +
                            "    ELSE\n" +
                            "      SELECT spriden_id,\n" +
                            "        spriden_pidm\n" +
                            "      INTO gv_id,\n" +
                            "        gv_pidm1\n" +
                            "      FROM spriden\n" +
                            "      WHERE spriden_id        = p_id\n" +
                            "      AND spriden_change_ind IS NULL;\n" +
                            "    END IF;\n" +
                            "    DBMS_OUTPUT.put_line ('Created spriden id: ' || gv_id || ', pidm: ' || gv_pidm1);\n" +
                            "    RETURN gv_pidm1;\n" +
                            "  END f_create_banner_id;\n" +
                            "  BEGIN\n" +
                            "    /*\n" +
                            "    Create the regular  test Vendors\n" +
                            "    */\n" +
                            "    FOR ind IN 1..total_user\n" +
                            "    LOOP\n" +
                            "      GV_PIDM := F_CREATE_BANNER_ID (REPLACE(ORACLE_ID||TO_CHAR(ind, '00099'),' ',''), REPLACE(SPRIDEN_ID||TO_CHAR(ind, '00099'),' ',''), NULL, vendor_name||ind);\n" +
                            "      INSERT\n" +
                            "      INTO FTVVEND\n" +
                            "        (\n" +
                            "          FTVVEND_PIDM,\n" +
                            "          FTVVEND_EFF_DATE,\n" +
                            "          FTVVEND_ACTIVITY_DATE,\n" +
                            "          FTVVEND_USER_ID,\n" +
                            "          FTVVEND_IN_ST_IND,\n" +
                            "          FTVVEND_GROUPING_IND,\n" +
                            "          FTVVEND_CARRIER_IND,         \n" +
                            "          FTVVEND_ATYP_CODE,\n" +
                            "          FTVVEND_ADDR_SEQNO,         \n" +
                            "          FTVVEND_COLLECT_TAX,\n" +
                            "          FTVVEND_ENTITY_IND,\n" +
                            "          FTVVEND_EPROC_IND,\n" +
                            "          FTVVEND_DATA_ORIGIN\n" +
                            "        )\n" +
                            "      select gv_pidm,\n" +
                            "        '01-MAY-10',\n" +
                            "        '01-MAY-10',\n" +
                            "        'GRAILS',\n" +
                            "        'I',\n" +
                            "        'M',\n" +
                            "        'D',\n" +
                            "        'BU',\n" +
                            "        1,     \n" +
                            "        'N',     \n" +
                            "        'C',\n" +
                            "        'N',\n" +
                            "        'GRAILS'\n" +
                            "      FROM DUAL\n" +
                            "      WHERE NOT EXISTS\n" +
                            "        (SELECT 1 FROM FTVVEND WHERE FTVVEND_PIDM = GV_PIDM\n" +
                            "        );\n" +
                            "      INSERT\n" +
                            "      INTO SPRADDR\n" +
                            "        (\n" +
                            "          SPRADDR_PIDM,\n" +
                            "          SPRADDR_ATYP_CODE,\n" +
                            "          SPRADDR_SEQNO,         \n" +
                            "          SPRADDR_STREET_LINE1,\n" +
                            "          SPRADDR_STREET_LINE2,\n" +
                            "          SPRADDR_CITY,\n" +
                            "          SPRADDR_STAT_CODE,\n" +
                            "          SPRADDR_ZIP,\n" +
                            "          \n" +
                            "          SPRADDR_STATUS_IND,\n" +
                            "          SPRADDR_ACTIVITY_DATE,\n" +
                            "          SPRADDR_USER,          \n" +
                            "          SPRADDR_DATA_ORIGIN\n" +
                            "        )\n" +
                            "      SELECT GV_PIDM,\n" +
                            "        'BU',\n" +
                            "        1,\n" +
                            "        '888 Pacific Avenue',\n" +
                            "        'Apt #22',\n" +
                            "        'San Francisco',\n" +
                            "        'CA',\n" +
                            "        '98773',\n" +
                            "        'A',\n" +
                            "        '01-MAY-10',\n" +
                            "        'GRAILS',     \n" +
                            "        'GRAILS'\n" +
                            "      FROM DUAL\n" +
                            "      WHERE NOT EXISTS\n" +
                            "        (SELECT 1\n" +
                            "        FROM SPRADDR\n" +
                            "        WHERE SPRADDR_PIDM   = GV_PIDM\n" +
                            "        AND SPRADDR_ATYP_CODE='BU'\n" +
                            "        );\n" +
                            "      COMMIT;\n" +
                            "    END LOOP;\n" +
                            "  END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setString( 1, this.oracleId )
            insertCall.setString( 2, this.spridenId )
            insertCall.setString( 3, this.vendorNamePattern )
            insertCall.setInt( 4, this.totalVendors )
            insertCall.execute()
            connectInfo.tableUpdate( "FTVVEND", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVVEND", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Vendor setup" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
