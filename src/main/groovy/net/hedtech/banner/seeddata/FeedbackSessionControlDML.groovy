/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * General Person address DML. This class only supports insert of an address, not update.
 */
public class FeedbackSessionControlDML {

    def bannerId
    def sfbffsc_term_code
    def sfbffsc_description
    def sfbffsc_suspend_feedback_ind
    def sfbffsc_start_date
    def sfbffsc_end_date
    def sfbffsc_add_comments_flag
    def sfbffsc_data_origin
    def sfbffsc_add_recommends_flag
    def sfbffsc_user_id
    def sfbffsc_activity_date

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null

    def xmlData

    public FeedbackSessionControlDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FeedbackSessionControlDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData

        parseXmlData()
        insertFeedbackSessionControl()
    }

    def parseXmlData() {
        def sessionControl = new XmlParser().parseText(xmlData)
        this.bannerId = sessionControl.BANNERID.text()
        this.sfbffsc_term_code = sessionControl.SFBFFSC_TERM_CODE.text()
        this.sfbffsc_description = sessionControl.SFBFFSC_DESCRIPTION.text()
        this.sfbffsc_suspend_feedback_ind = sessionControl.SFBFFSC_SUSPEND_FEEDBACK_IND.text()
        this.sfbffsc_start_date = sessionControl.SFBFFSC_START_DATE.text()
        this.sfbffsc_end_date = sessionControl.SFBFFSC_END_DATE.text()
        this.sfbffsc_add_comments_flag = sessionControl.SFBFFSC_ADD_COMMENTS_FLAG.text()
        this.sfbffsc_add_recommends_flag = sessionControl.SFBFFSC_ADD_RECOMMENDS_FLAG.text()
        this.sfbffsc_data_origin = sessionControl.SFBFFSC_DATA_ORIGIN.text()
        this.sfbffsc_user_id = sessionControl.SFBFFSC_USER_ID.text()
        this.sfbffsc_activity_date = sessionControl.SFBFFSC_ACTIVITY_DATE.text()
    }

    private def getJsonSqlString(bannerId) {
        String sql = """
                DECLARE
                    lv_json_out clob;
                BEGIN
                    gb_common.p_set_context('SS_ACC', 'LOG_ID', gb_common.f_get_pidm('${bannerId}'), 'N');
                    gokjson.initialize_clob_output;
                    BEGIN
                      gokjson.open_object(with_exception => true);
                      baninst1_ss9.bwlkfdad.p_add_session_post(?,?,?,?,?,?,?,?);
                      gokjson.close_object;
                    EXCEPTION
                      WHEN OTHERS THEN
                        gokjson.close_for_exception;
                        gokjson.put_exception_info(sqlcode, SQLERRM);
                        gokjson.close_object;
                    END;
                    lv_json_out := gokjson.get_clob_output;
                    gokjson.free_output;
                    goksels.p_clear_user_context;                    
                END;"""
        sql
    }

    private checkIfDataExists(term, description) {
        def findSessionControl = ""
        String findRow = """select 'Y' sfbffsc_find from sfbffsc where SFBFFSC_TERM_CODE = ?
           and SFBFFSC_DESCRIPTION = ? """
        try {
            conn.eachRow(findRow, [term, description]) { row ->
                findSessionControl = row.sfbffsc_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SFBFFSC", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table SFBFFSC from FeedbackSessionControlDML.groovy: $e.message"
            }
        }
        return findSessionControl ? true : false
    }

    def insertFeedbackSessionControl() {
        boolean ifDataExists = checkIfDataExists(this.sfbffsc_term_code, this.sfbffsc_description)
        if (!ifDataExists) {
            String API = getJsonSqlString(this.bannerId)
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            insertCall.setString(1, this.sfbffsc_term_code)
            insertCall.setString(2, this.sfbffsc_description)
            insertCall.setString(3, this.sfbffsc_start_date)
            insertCall.setString(4, this.sfbffsc_end_date)
            insertCall.setString(5, this.sfbffsc_suspend_feedback_ind)
            insertCall.setString(6, this.sfbffsc_add_comments_flag)
            insertCall.setString(7, this.sfbffsc_add_recommends_flag)
            insertCall.setString(8, 'Submit Changes')

            try {
                insertCall.execute()
                connectInfo.tableUpdate("SFBFFSC", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SFBFFSC", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Problem executing create session control from FeedbackSessionControlDML.groovy: $e.message"
                }
            }
        }
    }
}
