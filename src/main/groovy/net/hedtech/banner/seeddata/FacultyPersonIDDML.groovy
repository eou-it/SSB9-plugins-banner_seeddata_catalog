/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * General Person ID DML for Students
 * Save the PIDM in the InputData.saveStudentPidm
 */

public class FacultyPersonIDDML {
    def spriden_pidm
    def spriden_id
    def spriden_last_name
    def spriden_first_name
    def spriden_mi
    def spriden_change_ind
    def spriden_entity_ind
    def spriden_user
    def spriden_origin
    def spriden_search_last_name
    def spriden_search_first_name
    def spriden_search_mi
    def spriden_soundex_last_name
    def spriden_soundex_first_name
    def spriden_ntyp_code
    def spriden_create_user
    def spriden_create_date
    def spriden_create_fdmn_code
    def spriden_surname_prefix


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def pidm


    public FacultyPersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processFaculty()

    }


    def processFaculty() {
        def apiData = new XmlParser().parseText(xmlData)
        String ssql = """select * from spriden  where spriden_id = ?"""
        def cntSpriden = 0

        try {
            this.conn.eachRow(ssql, [apiData.SPRIDEN_ID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in FacultyPersonIDDML,  ${apiData.SPRIDEN_ID.text()}  from SPRIDEN. $e.message"
            }
        }
        if (cntSpriden == 0) {
            def newSpriden = new PersonIDDML(connectInfo, conn, connectCall, xmlData)
            connectInfo.saveStudentPidm = newSpriden.PIDM

        }
        else {
            connectInfo.saveStudentPidm = pidm
        }

        // delete data so we can re-add because most do not have an update process
        if (cntSpriden && connectInfo.replaceData) {
            deleteData()
        }

    }


   def deleteData() {
     
        String selectSql = """select sirasgn_term_code , sirasgn_crn, sirasgn_category   from sirasgn where sirasgn_pidm = ?
               order by 1,2"""

        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {
                conn.call("""{call SB_FACASSIGNMENT.p_delete(p_pidm => ?,
                     p_term_code  => ?,p_crn => ?,  p_category => ?) }""",
                          [connectInfo.saveStudentPidm.toInteger(),
                          it.sirasgn_term_code,
                          it.sirasgn_crn,
                          it.sirasgn_category])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SIRASGN", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing  select or delete for table SIRASGN from FacultyPersonIDDML.groovy: $e.message"
            }
        }

        selectSql = """select sibinst_term_code_eff  from sibinst where sibinst_pidm = ?
               order by 1 """
        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {
                deleteData("SIRDPCL", "delete sirdpcl where sirdpcl_pidm = ?  ")
                deleteData("SIRATTR", "delete sirattr where sirattr_pidm = ?  ")
                deleteData("SIRCMNT", "delete sircmnt where sircmnt_pidm = ?  ")
                deleteData("SIRASGN", "delete sirasgn where sirasgn_pidm = ?  ")
                deleteData("SIRNIST", "delete sirnist where sirnist_pidm = ?  ")

                conn.call("{call sb_faculty.p_delete(p_pidm => ?,p_term_code_eff => ?  ) }",
                          [connectInfo.saveStudentPidm.toInteger(),
                          it.sibinst_term_code_eff])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SIBINST", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete  for table SIBINST from FacultyPersonIDDML.groovy: $e.message"
            }
        }


    }



    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [connectInfo.saveStudentPidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for person ${connectInfo.saveStudentPidm} from StudentPersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
