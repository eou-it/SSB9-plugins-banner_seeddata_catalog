/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Query Prototypes
 */
public class FinanceBudgetQueryProtoTypesDML {


    def fobprtoName, fobprtoType, spridenId, fobprtoCoasCode, fobprtoOrgnCode,
        fobprtoFsyrCode, fobprtoFsyrCode2, fobprtoFspdCode, fobprtoFspdCode2, fobprtoViewby, fobprtoIncludeRevenues, fobprtoCmttypeCode,
        fobprtoAdoptOption, fobprtoBudadjtOption, fobprtoAdjtbudOption, fobprtoTempbudOption, fobprtoCommitedOption,fobprtoSharedType


    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetQueryProtoTypesDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetQueryProtoTypesDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateQueryPrototypes()
    }


    def parseXmlData() {
        def queryPrototypeXMLData = new XmlParser().parseText( xmlData )
        this.fobprtoName = queryPrototypeXMLData.FOBPRTO_NAME.text()
        this.fobprtoType = queryPrototypeXMLData.FOBPRTO_TYPE.text()
        this.spridenId = queryPrototypeXMLData.FOBPRTO_SPRIDEN_ID.text()
        this.fobprtoCoasCode = queryPrototypeXMLData.FOBPRTO_COAS_CODE.text()
        this.fobprtoOrgnCode = queryPrototypeXMLData.FOBPRTO_ORGN_CODE.text()
        this.fobprtoFsyrCode = queryPrototypeXMLData.FOBPRTO_FSYR_CODE.text()
        this.fobprtoFsyrCode2 = queryPrototypeXMLData.FOBPRTO_FSYR_CODE2.text()
        this.fobprtoFspdCode = queryPrototypeXMLData.FOBPRTO_FSPD_CODE.text()
        this.fobprtoFspdCode2 = queryPrototypeXMLData.FOBPRTO_FSPD_CODE2.text()
        this.fobprtoViewby = queryPrototypeXMLData.FOBPRTO_VIEWBY.text()
        this.fobprtoIncludeRevenues = queryPrototypeXMLData.FOBPRTO_INCLUDE_REVENUES.text()
        this.fobprtoCmttypeCode = queryPrototypeXMLData.FOBPRTO_CMTTYPE_CODE.text()
        this.fobprtoAdoptOption = queryPrototypeXMLData.FOBPRTO_ADOPT_OPTION.text()
        this.fobprtoBudadjtOption = queryPrototypeXMLData.FOBPRTO_BUDADJT_OPTION.text()
        this.fobprtoAdjtbudOption = queryPrototypeXMLData.FOBPRTO_ADJTBUD_OPTION.text()
        this.fobprtoTempbudOption = queryPrototypeXMLData.FOBPRTO_TEMPBUD_OPTION.text()
        this.fobprtoCommitedOption = queryPrototypeXMLData.FOBPRTO_COMMITED_OPTION.text()
        this.fobprtoSharedType = queryPrototypeXMLData.FOBPRTO_SHARED_TYPE.text()
    }

    /**
     * Populate Query Prototypes
     */
    def populateQueryPrototypes() {
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
                            "DELETE FROM FOBPRTO WHERE FOBPRTO_PIDM = pidm AND FOBPRTO_NAME= ? AND FOBPRTO_TYPE = ?;\n" +
                            "INSERT INTO FOBPRTO(\n" +
                            "\t\t\tFOBPRTO_NAME, \n" +
                            "\t\t\tFOBPRTO_TYPE, \n" +
                            "\t\t\tFOBPRTO_PIDM, \n" +
                            "\t\t\tFOBPRTO_USER_ID,\n" +
                            "\t\t\tFOBPRTO_COAS_CODE, \n" +
                            "\t\t\tFOBPRTO_ORGN_CODE,\n" +
                            "\t\t\tFOBPRTO_FSYR_CODE,\n" +
                            "\t\t\tFOBPRTO_FSYR_CODE2, \n" +
                            "\t\t\tFOBPRTO_FSPD_CODE,\n" +
                            "\t\t\tFOBPRTO_FSPD_CODE2, \n" +
                            "\t\t\tFOBPRTO_VIEWBY,\n" +
                            "\t\t\tFOBPRTO_INCLUDE_REVENUES,\n" +
                            "\t\t\tFOBPRTO_CMTTYPE_CODE,\n" +
                            "\t\t\tFOBPRTO_ADOPT_OPTION,\n" +
                            "\t\t\tFOBPRTO_BUDADJT_OPTION,\n" +
                            "\t\t\tFOBPRTO_ADJTBUD_OPTION,\n" +
                            "\t\t\tFOBPRTO_TEMPBUD_OPTION,\n" +
                            "\t\t\tFOBPRTO_COMMITED_OPTION,\n" +
                            "\t\t\tFOBPRTO_ACTIVITY_DATE, \n" +
                            "\t\t\tFOBPRTO_DATA_ORIGIN,\n" +
                            "\t\t\tFOBPRTO_SHARED_TYPE)\n" +
                            "\t\t\tVALUES (?, ?, pidm, 'GRAILS', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, 'GRAILS', ?);\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setString( 1, this.spridenId )
            insertCall.setString( 2, this.fobprtoName )
            insertCall.setString( 3, this.fobprtoType )
            insertCall.setString( 4, this.fobprtoName )
            insertCall.setString( 5, this.fobprtoType )
            insertCall.setString( 6, this.fobprtoCoasCode )
            insertCall.setString( 7, this.fobprtoOrgnCode )
            insertCall.setString( 8, this.fobprtoFsyrCode )
            insertCall.setString( 9, this.fobprtoFsyrCode2 )
            insertCall.setString( 10, this.fobprtoFspdCode )
            insertCall.setString( 11, this.fobprtoFspdCode2 )
            insertCall.setString( 12, this.fobprtoViewby )
            insertCall.setString( 13, this.fobprtoIncludeRevenues )
            insertCall.setString( 14, this.fobprtoCmttypeCode )
            insertCall.setString( 15, this.fobprtoAdoptOption )
            insertCall.setString( 16, this.fobprtoBudadjtOption )
            insertCall.setString( 17, this.fobprtoAdjtbudOption )
            insertCall.setString( 18, this.fobprtoTempbudOption )
            insertCall.setString( 19, this.fobprtoCommitedOption )
            insertCall.setString( 20, this.fobprtoSharedType )

            insertCall.execute()
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_QUERY_PROTOTYPE", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_QUERY_PROTOTYPE", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Query Prototype" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
