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
public class FinanceFavoriteBudgetQueryProtoTypesDML {


    def fobprtoName, fobprtoType, spridenId, fobprtoViewby

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceFavoriteBudgetQueryProtoTypesDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceFavoriteBudgetQueryProtoTypesDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateFavoriteQueryPrototypes()
    }


    def parseXmlData() {
        def favoriteQueryPrototypeXMLData = new XmlParser().parseText( xmlData )
        this.fobprtoName = favoriteQueryPrototypeXMLData.FORPRTF_NAME.text()
        this.fobprtoType = favoriteQueryPrototypeXMLData.FORPRTF_TYPE.text()
        this.spridenId = favoriteQueryPrototypeXMLData.FORPRTF_SPRIDEN_ID.text()
        this.fobprtoViewby = favoriteQueryPrototypeXMLData.FORPRTF_VIEWBY.text()
    }

    /**
     * Populate Favorite Query Prototypes
     */
    def populateFavoriteQueryPrototypes() {
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
                            "DELETE FROM FORPRTF WHERE FORPRTF_NAME= ? AND FORPRTF_PRTO_TYPE = ?;\n" +
                            "INSERT INTO FORPRTF(\n" +
                            "\t\t\tFORPRTF_NAME, \n" +
                            "\t\t\tFORPRTF_PRTO_TYPE, \n" +
                            "\t\t\tFORPRTF_PRTO_VIEWBY, \n" +
                            "\t\t\tFORPRTF_PRTO_PIDM,\n" +
                            "\t\t\tFORPRTF_PIDM, \n" +
                            "\t\t\tFORPRTF_ACTIVITY_DATE,\n" +
                            "\t\t\tFORPRTF_USER_ID,\n" +
                            "\t\t\tFORPRTF_DATA_ORIGIN)\n" +                            
                            "\t\t\tVALUES (?, ?, ?, pidm, pidm, sysdate, 'GRAILS', 'GRAILS');\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )

            insertCall.setString( 1, this.spridenId )
            insertCall.setString( 2, this.fobprtoName )
            insertCall.setString( 3, this.fobprtoType )
            insertCall.setString( 4, this.fobprtoName )
            insertCall.setString( 5, this.fobprtoType )
			insertCall.setString( 6, this.fobprtoViewby )
            insertCall.execute()
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_FAVORITE_QUERY_PROTOTYPE", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "BUDGET_AVAILABILITY_FAVORITE_QUERY_PROTOTYPE", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA Favorite Query Prototype" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
