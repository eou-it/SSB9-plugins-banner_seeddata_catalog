/*********************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection

/**
 * General Faculty Feedback Session Control DML.
 */
public class FacultyIssuesAndRecommendationsDML {

    def stvffva_surrogate_id
    def stvffva_code
    def stvffva_desc
    def stvffva_type
    def stvffva_active_ind
    def stvffva_version
    def stvffva_data_origin
    def stvffva_user_id
    def stvffva_activity_date
    def stvffva_vpdi_code
    InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public FacultyIssuesAndRecommendationsDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FacultyIssuesAndRecommendationsDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertSTVFFVA()
    }


    /**
     * Parse data.
     * @return
     */
    private def parseXmlData() {
        def xmlValues = new XmlParser().parseText( xmlData )

        this.stvffva_surrogate_id = xmlValues.STVFFVA_SURROGATE_ID.text()
        this.stvffva_code = xmlValues.STVFFVA_CODE.text()
        this.stvffva_desc = xmlValues.STVFFVA_DESC.text()
        this.stvffva_type = xmlValues.STVFFVA_TYPE.text()
        this.stvffva_active_ind = xmlValues.STVFFVA_ACTIVE_IND.text()
        this.stvffva_version = xmlValues.STVFFVA_VERSION.text()
        this.stvffva_data_origin = xmlValues.STVFFVA_DATA_ORIGIN.text()
        this.stvffva_user_id = xmlValues.STVFFVA_USER_ID.text()
        this.stvffva_activity_date = xmlValues.STVFFVA_ACTIVITY_DATE.text()
        this.stvffva_vpdi_code = xmlValues.STVFFVA_VPDI_CODE.text()

    }


    /**
     * Insert using sb_feedback_codes.p_create
     * @return
     */
    private def insertSTVFFVA() {
        try {
            deleteSTVFFVA()
            createSTVFFVA()
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "STVFFVA", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Insert STVFFVA  ${this.stvffva_code} ${this.stvffva_desc}"
                println "Problem setting up insert for table STVFFVA from FacultyIssuesAndRecommendationsDML.groovy: $e.message"
            }
        }

    }

    /**
     * Delete if exists.
     * @return
     */
    private def deleteSTVFFVA() {
        String rowSQL = """delete from STVFFVA 
                           where STVFFVA_CODE = ?
                           AND STVFFVA_DESC = ?
                           AND STVFFVA_TYPE = ?"""
        try {

            def deletedRows = conn.executeUpdate( rowSQL, [this.stvffva_code, this.stvffva_desc, this.stvffva_type] )
            connectInfo.tableUpdate( "STVFFVA", 0, 0, 0, 0, deletedRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem deleting STVFFVA rowid FacultyIssuesAndRecommendationsDML.groovy: $e.message"
            }
        }
    }


    /**
     * Create STVFFVA
     * @return
     */
    private def createSTVFFVA() {
        String API = "{call  sb_feedback_codes.p_create(?,?,?,?,?,?,?)}"
        CallableStatement insertCall = this.connectCall.prepareCall( API )
        insertCall.registerOutParameter( 1, java.sql.Types.INTEGER )

        // parm 2 STVFFVA_CODE	VARCHAR2(30 CHAR)
        insertCall.setString( 2, this.stvffva_code )

        // parm 3 STVFFVA_DESC	VARCHAR2(255 CHAR)
        insertCall.setString( 3, this.stvffva_desc )

        // parm 4 STVFFVA_TYPE	VARCHAR2(20 CHAR)
        insertCall.setString( 4, this.stvffva_type )

        // parm 5 STVFFVA_ACTIVE_IND	VARCHAR2(1 CHAR)
        insertCall.setString( 5, this.stvffva_active_ind )

        // parm 6 STVFFVA_DATA_ORIGIN	VARCHAR2(30 CHAR)
        insertCall.setString( 6, connectInfo.dataOrigin )

        // parm 7 STVFFVA_USER_ID	VARCHAR2(30 CHAR)
        insertCall.setString( 7, connectInfo.userID )

        if (connectInfo.debugThis) {
            println "Insert STVFFVA ${this.stvffva_code} ${this.stvffva_desc}"
        }
        try {
            insertCall.executeUpdate()
            connectInfo.tableUpdate( "STVFFVA", 0, 1, 0, 0, 0 )
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "STVFFVA", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Insert STVFFVA ${this.stvffva_code} ${this.stvffva_desc}"
                println "Problem executing insert for table STVFFVA from FacultyIssuesAndRecommendationsDML.groovy: $e.message"
            }
        }
        finally {
            insertCall.close()
        }
    }
}

