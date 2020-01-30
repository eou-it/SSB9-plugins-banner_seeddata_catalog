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
public class FeedbackIssuesRecommendationsDML {

    def bannerId
    def stvffva_code
    def stvffva_desc
    def stvffva_type
    def stvffva_active_ind

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null

    def xmlData

    public FeedbackIssuesRecommendationsDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public FeedbackIssuesRecommendationsDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

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
        this.stvffva_code = sessionControl.STVFFVA_CODE.text()
        this.stvffva_desc = sessionControl.STVFFVA_DESC.text()
        this.stvffva_type = sessionControl.STVFFVA_TYPE.text()
        this.stvffva_active_ind = sessionControl.STVFFVA_ACTIVE_IND.text()
    }

    private def getJsonSqlString(bannerId, type) {
        String procedure
        if(type == 'ISSUE'){
            procedure = 'baninst1_ss9.bwlkfdad.p_define_issues'
        }else{
            procedure = 'baninst1_ss9.bwlkfdad.p_define_recommendations'
        }
        String sql = """
                DECLARE
                    lv_json_out clob;
                BEGIN
                    gb_common.p_set_context('SS_ACC', 'LOG_ID', gb_common.f_get_pidm('${bannerId}'), 'N');
                    gokjson.initialize_clob_output;
                    BEGIN
                      gokjson.open_object(with_exception => true);
                      ${procedure}(mode_in=>?,p_code=>?,p_desc=>?,p_active_ind=>?);
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

    private checkIfDataExists(code) {
        def findCode = ""
        String findRow = """select 'Y' stvffva_find from stvffva where STVFFVA_CODE = ?"""
        try {
            conn.eachRow(findRow, [code]) { row ->
                findCode = row.stvffva_find
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("STVFFVA", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findRow}"
                println "Problem executing select for table STVFFVA from FeedbackIssuesRecommendationsDML.groovy: $e.message"
            }
        }
        return findCode ? true : false
    }

    def insertFeedbackSessionControl() {
        boolean ifDataExists = checkIfDataExists(this.stvffva_code)
        if (!ifDataExists) {
            String mode = (this.stvffva_type == 'RECOMMENDATION') ? 'Save New Recommendation' : 'Save New Issue'
            println('stvffva_type - '+this.stvffva_type)
            println('mode - '+mode)
            String API = getJsonSqlString(this.bannerId, this.stvffva_type)
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            insertCall.setString(1, mode)
            insertCall.setString(2, this.stvffva_code)
            insertCall.setString(3, this.stvffva_desc)
            insertCall.setString(4, this.stvffva_active_ind)
            try {
                insertCall.execute()
                connectInfo.tableUpdate("STVFFVA", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("STVFFVA", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Problem executing create Feedback issue or recommendation from FeedbackIssuesRecommendationsDML.groovy: $e.message"
                }
            }
        }
    }
}

