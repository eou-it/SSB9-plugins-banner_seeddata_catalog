/*********************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * General Faculty Feedback Session Control DML.
 */
public class FacultyFeedbackSessionControlDML {
    def sfbffsc_surrogate_id
    def sfbffsc_term_code
    def sfbffsc_description
    def sfbffsc_suspend_feedback_ind
    def sfbffsc_start_date
    def sfbffsc_end_date
    def sfbffsc_add_comments_flag
    def sfbffsc_add_recommends_flag
    def sfbffsc_version
    def sfbffsc_data_origin
    def sfbffsc_user_id
    def sfbffsc_activity_date
    def sfbffsc_vpdi_code
    InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public FacultyFeedbackSessionControlDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FacultyFeedbackSessionControlDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertSFBFFSC()
    }


    def parseXmlData() {
        def fbSession = new XmlParser().parseText( xmlData )

        this.sfbffsc_surrogate_id = fbSession.SFBFFSC_SURROGATE_ID.text()
        this.sfbffsc_term_code = fbSession.SFBFFSC_TERM_CODE.text()
        this.sfbffsc_description = fbSession.SFBFFSC_DESCRIPTION.text()
        this.sfbffsc_suspend_feedback_ind = fbSession.SFBFFSC_SUSPEND_FEEDBACK_IND.text()
        this.sfbffsc_start_date = fbSession.SFBFFSC_START_DATE.text()
        this.sfbffsc_end_date = fbSession.SFBFFSC_END_DATE.text()
        this.sfbffsc_add_comments_flag = fbSession.SFBFFSC_ADD_COMMENTS_FLAG.text()
        this.sfbffsc_add_recommends_flag = fbSession.SFBFFSC_ADD_RECOMMENDS_FLAG.text()
        this.sfbffsc_version = fbSession.SFBFFSC_VERSION.text()
        this.sfbffsc_data_origin = fbSession.SFBFFSC_DATA_ORIGIN.text()
        this.sfbffsc_user_id = fbSession.SFBFFSC_USER_ID.text()
        this.sfbffsc_activity_date = fbSession.SFBFFSC_ACTIVITY_DATE.text()
        this.sfbffsc_vpdi_code = fbSession.SFBFFSC_VPDI_CODE.text()

    }


    /**
     * Insert using sb_feedback_session.p_create
     * Or Update
     * @return
     */
    def insertSFBFFSC() {
        deleteSFBFFSC()
        try {
            createSFBFFSC()
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "SFBFFSC", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Insert SFBFFSC  ${this.sfbffsc_term_code} ${this.sfbffsc_description}"
                println "Problem setting up insert for table SFBFFSC from FacultyFeedbackSessionControlDML.groovy: $e.message"
            }
        }

    }


    /**
     * Delete if exists.
     * @return
     */
    private def deleteSFBFFSC() {
        String rowSQL = """delete from SFBFFSC 
                           where SFBFFSC_TERM_CODE = ?
                           AND SFBFFSC_DESCRIPTION = ?"""
        try {

            def deletedRows = conn.executeUpdate( rowSQL, [this.sfbffsc_term_code, this.sfbffsc_description] )
            connectInfo.tableUpdate( "SFBFFSC", 0, 0, 0, 0, deletedRows )
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem deleting SFBFFSC rowid FacultyFeedbackSessionControlDML.groovy: $e.message"
            }
        }
    }


    /**
     * Create sb_feedback_session.p_create
     * @return
     */
    private def createSFBFFSC() {
        String API = "{call  sb_feedback_session.p_create(?,?,?,?,?,?,?,?,?,?)}"
        CallableStatement insertCall = this.connectCall.prepareCall( API )
        insertCall.registerOutParameter( 1, java.sql.Types.INTEGER )

        // parm 2 SFBFFSC_TERM_CODE	VARCHAR2(6 CHAR)
        insertCall.setString( 2, this.sfbffsc_term_code )

        // parm 3 SFBFFSC_DESCRIPTION	VARCHAR2(30 CHAR)
        insertCall.setString( 3, this.sfbffsc_description )

        // parm 4 SFBFFSC_SUSPEND_FEEDBACK_IND	VARCHAR2(1 CHAR)
        insertCall.setString( 4, this.sfbffsc_suspend_feedback_ind )

        // parm 5 SFBFFSC_START_DATE	DATE
        if ((this.sfbffsc_start_date == "") || (this.sfbffsc_start_date == null) ||
                (!this.sfbffsc_start_date)) {
            insertCall.setNull( 5, java.sql.Types.DATE )
        } else {
            def ddate = new ColumnDateValue( this.sfbffsc_start_date )
            String unfDate = ddate.formatJavaDate()
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
            java.sql.Date sqlDate = new java.sql.Date( formatter.parse( unfDate ).getTime() );
            insertCall.setDate( 5, sqlDate )
        }

        // parm 6 SFBFFSC_END_DATE	DATE
        if ((this.sfbffsc_end_date == "") || (this.sfbffsc_end_date == null) ||
                (!this.sfbffsc_end_date)) {
            insertCall.setNull( 6, java.sql.Types.DATE )
        } else {
            def ddate = new ColumnDateValue( this.sfbffsc_end_date )
            String unfDate = ddate.formatJavaDate()
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
            java.sql.Date sqlDate = new java.sql.Date( formatter.parse( unfDate ).getTime() );
            insertCall.setDate( 6, sqlDate )
        }

        // parm 7 SFBFFSC_ADD_COMMENTS_FLAG	VARCHAR2(1 CHAR)
        insertCall.setString( 7, this.sfbffsc_add_comments_flag )

        // parm 8 SFBFFSC_ADD_RECOMMENDS_FLAG	VARCHAR2(1 CHAR)
        insertCall.setString( 8, this.sfbffsc_add_recommends_flag )

        // parm 9 SFBFFSC_DATA_ORIGIN	VARCHAR2(30 CHAR)
        insertCall.setString( 9, connectInfo.dataOrigin )

        // parm 10 SFBFFSC_USER_ID	VARCHAR2(30 CHAR)
        insertCall.setString( 10, connectInfo.userID )

        if (connectInfo.debugThis) {
            println "Insert SFBFFSC ${this.sfbffsc_term_code} ${this.sfbffsc_description}"
        }
        try {
            insertCall.executeUpdate()
            connectInfo.tableUpdate( "SFBFFSC", 0, 1, 0, 0, 0 )
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "SFBFFSC", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Insert SFBFFSC ${this.sfbffsc_term_code} ${this.sfbffsc_description}"
                println "Problem executing insert for table SFBFFSC from FacultyFeedbackSessionControlDML.groovy: $e.message"
            }
        }
        finally {
            insertCall.close()
        }
    }

}

