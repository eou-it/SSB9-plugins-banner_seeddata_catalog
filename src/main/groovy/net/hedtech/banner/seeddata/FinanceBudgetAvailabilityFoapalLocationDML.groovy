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
 *  DML for FinanceBudget Availability Foapal Location
 */
public class FinanceBudgetAvailabilityFoapalLocationDML {


    def locationCodePattern, locationCodeDescriptionPattern, startCount, totalCount, locationCoaCode,
            locationCodePred,locationAddressLine1, locationCity, locationState, locationZip, locationCountryCode, locationNationCode,
        locationPhoneArea, locationPhoneNumber, locationSquareFootage, locationSquareFootageRate

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityFoapalLocationDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityFoapalLocationDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateLocation()
    }


    def parseXmlData() {
        def accountCreation = new XmlParser().parseText( xmlData )
        this.locationCodePattern = accountCreation.LOCATION_CODE_PATTERN?.text()
        this.locationCodeDescriptionPattern = accountCreation.LOCATION_CODE_TITLE.text()
        boolean isEmpty = StringUtils.isEmpty accountCreation.START_COUNT?.text()
        this.startCount = isEmpty ? 0 : accountCreation.START_COUNT?.text() as int
        isEmpty = StringUtils.isEmpty accountCreation.TOTAL_COUNT?.text()
        this.totalCount = isEmpty ? 0 : accountCreation.TOTAL_COUNT?.text() as int
        this.locationCoaCode = accountCreation.LOCATION_COA_CODE?.text()
        this.locationCodePred =accountCreation.LOCATION_CODE_PRED?.text()
        this.locationAddressLine1 = accountCreation.LOCATION_ADD_LINE1?.text()
        this.locationCity = accountCreation.LOCATION_CITY?.text()
        this.locationState = accountCreation.LOCATION_STATE?.text()
        this.locationZip = accountCreation.LOCATION_ZIP?.text()
        this.locationCountryCode = accountCreation.LOCATION_COUNTRY_CODE?.text()
        this.locationNationCode = accountCreation.LOCATION_NATION_CODE?.text()
        this.locationPhoneArea = accountCreation.LOCATION_PHONE_AREA?.text()
        this.locationPhoneNumber = accountCreation.LOCATION_PHONE_NUMBER?.text()
        this.locationSquareFootage = accountCreation.LOCATION_SQUARE_FOOTAGE?.text()
        this.locationSquareFootageRate = accountCreation.LOCATION_SQUARE_FOOTAGE_RATE?.text()
    }

    /**
     * Populate Location
     */
    def populateLocation() {
        try {
            final String apiQuery =
                    "\tBEGIN\n" +
                            "  FOR ind IN ? .. ?\n" +
                            "  loop\n" +
                            "    delete from FTVLOCN where FTVLOCN_LOCN_CODE=replace(?\n" +
                            "        ||TO_CHAR(ind, DECODE(LENGTH(?),1,'00099','0099')),' ','') AND FTVLOCN_COAS_CODE = ?;\n" +
                            "INSERT INTO FTVLOCN \n" +
                            "  (\n" +
                            "\tFTVLOCN_LOCN_CODE,\n" +
                            "\tFTVLOCN_TITLE,\n" +
                            "\tFTVLOCN_COAS_CODE,\n" +
                            "\tFTVLOCN_EFF_DATE,\n" +
                            "\tFTVLOCN_ACTIVITY_DATE,\n" +
                            "\tFTVLOCN_USER_ID,\n" +
                            "\tFTVLOCN_NCHG_DATE,\n" +
                            "\tFTVLOCN_STATUS_IND,\n" +
                            "\tFTVLOCN_LOCN_CODE_PRED,\n" +
                            "\tFTVLOCN_ADDR_LINE1,\n" +
                            "\tFTVLOCN_CITY,\n" +
                            "\tFTVLOCN_STATE,\n" +
                            "\tFTVLOCN_ZIP,\n" +
                            "\tFTVLOCN_CNTY_CODE,\n" +
                            "\tFTVLOCN_NATN_CODE,\n" +
                            "\tFTVLOCN_PHONE_AREA,\n" +
                            "\tFTVLOCN_PHONE_NUMBER,\n" +
                            "\tFTVLOCN_SQUARE_FOOTAGE," +
                            "\tFTVLOCN_SQUARE_FOOTAGE_RATE,\n" +
                            "    FTVLOCN_DATA_ORIGIN\n" +
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
                            "        'GRAILS',\n" +
                            "        '31-DEC-2099',       \n" +
                            "        'A',\n" +
                            "        ?,\n" +//FTVLOCN_LOCN_CODE_PRED
                            "        ?,\n" + //tFTVLOCN_ADDR_LINE1
                            "        ?,\n" + //tFTVLOCN_CITY
                            "        ?,\n" + //tFTVLOCN_STATE
                            "        ?,\n" +//tFTVLOCN_ZIP
                            "        ?,\n" + //tFTVLOCN_CNTY_CODE
                            "        ?,\n" + //tFTVLOCN_NATN_CODE
                            "        ?,\n" + //tFTVLOCN_PHONE_AREA
                            "        ?,\n" + //tFTVLOCN_PHONE_NUMBER
                            "        ?,\n" + //tFTVLOCN_SQUARE_FOOTAGE
                            "        ?,\n" + //tFTVLOCN_SQUARE_FOOTAGE_RATE
                            "        'GRAILS'\n" +
                            "      );\n" +
                            "  END LOOP;\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            insertCall.setInt( 1, this.startCount )
            insertCall.setInt( 2, this.totalCount )
            insertCall.setString( 3, this.locationCodePattern )
            insertCall.setString( 4, this.locationCodePattern )
            insertCall.setString( 5, this.locationCoaCode )
            insertCall.setString( 6, this.locationCodePattern )
            insertCall.setString( 7, this.locationCodePattern )
            insertCall.setString( 8, this.locationCodeDescriptionPattern )
            insertCall.setString( 9, this.locationCoaCode )
            insertCall.setString( 10, this.locationCodePred )

            insertCall.setString( 11, this.locationAddressLine1 )
            insertCall.setString( 12, this.locationCity )
            insertCall.setString( 13, this.locationState )
            insertCall.setString( 14, this.locationZip )
            insertCall.setString( 15, this.locationCountryCode )
            insertCall.setString( 16, this.locationNationCode )
            insertCall.setString( 17, this.locationPhoneArea )
            insertCall.setString( 18, this.locationPhoneNumber )
            insertCall.setString( 19, this.locationSquareFootage )
            insertCall.setString( 20, this.locationSquareFootageRate )

            insertCall.execute()
            connectInfo.tableUpdate( "FTVLOCN", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVLOCN", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA_FOAPAL_FTVLOCN" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
