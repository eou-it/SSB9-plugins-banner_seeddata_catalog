/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  DML for schedule tables (ssbsect)
 */

public class ScheduleTermDeleteDML {
    def ssbsect_term_code = ""
    def ssbsect_crn = ""
    java.sql.RowId ssbsectRow = null
    // database connection information
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public ScheduleTermDeleteDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processScheduleFile()

    }


    def parseXmlData() {
        def sch = new XmlParser().parseText(xmlData)

        if (connectInfo.debugThis) {
            println "--------- New Schedule Term Delete record ----------"
            println "Section: " + sch.SSBSECT_TERM_CODE.text()
        }
        this.ssbsect_term_code = sch.SSBSECT_TERM_CODE.text()
    }


    def processScheduleFile() {

        def sectionExists = 0
        String sectionSQL = """SELECT count(*) count
                            FROM ssbsect
                            WHERE  ssbsect_term_code = ? """
        try {
            sectionExists = conn.firstRow(sectionSQL, [this.ssbsect_term_code]).count

        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${sectionSQL}"
                println "Problem selecting section rowid ScheduleTermDeleteDML.groovy: $e.message"
            }
        }

        if (sectionExists) {
            deleteSchedule()
        }

    }


    def deleteSchedule() {
        String ssbsectRec = """SELECT ssbsect_crn FROM ssbsect
                       WHERE  ssbsect_term_code = ? """
        conn.eachRow(ssbsectRec, [this.ssbsect_term_code]) {row ->
            this.ssbsect_crn = row.ssbsect_crn
            processDelete()
        }
        processTermDeletes()
    }


    def processDelete() {

         String schDel = ""
        schDel = """DELETE FROM ssrmeet  WHERE ssrmeet_term_code = ? AND ssrmeet_crn = ? """
        def tableName = "SSRMEET"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM sirasgn WHERE sirasgn_term_code = ? AND sirasgn_crn = ?"""
        tableName = "SIRASGN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrlink   WHERE ssrlink_term_code = ? AND ssrlink_crn = ?"""
        tableName = "SSRLINK"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM sfrwlnt   WHERE sfrwlnt_term_code = ? AND sfrwlnt_crn = ?"""
        tableName = "SFRWLNT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)


        schDel = """DELETE  FROM ssrcorq WHERE ssrcorq_term_code = ?  AND ssrcorq_crn = ?"""
        tableName = "SSRCORQ"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)


        schDel = """DELETE  FROM ssrfees WHERE ssrfees_term_code = ? AND ssrfees_crn = ?"""
        tableName = "SSRFEES"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)


        schDel = """DELETE FROM SSRRATT  WHERE ssrratt_term_code = ? AND ssrratt_crn = ?"""
        tableName = "SSRRATT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)


        schDel = """DELETE FROM SSRRCHR   WHERE ssrrchr_term_code = ? AND ssrrchr_crn = ?"""

        tableName = "SSRRCHR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)


        schDel = """DELETE FROM SSRRDEP   WHERE ssrrdep_term_code = ? AND ssrrdep_crn = ?"""
        tableName = "SSRRDEP"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM SSBWLSC   WHERE ssbwlsc_term_code = ? AND ssbwlsc_crn = ?"""
        tableName = "SSBWLSC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrcol  WHERE ssrrcol_term_code = ? AND ssrrcol_crn = ?"""
        tableName = "SSRRCOL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrdeg  WHERE ssrrdeg_term_code = ? AND ssrrdeg_crn = ?"""
        tableName = "SSRRDEG"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrprg WHERE ssrrprg_term_code = ? AND ssrrprg_crn = ?"""
        tableName = "SSRRPRG"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrrare  WHERE ssrrare_term_code = ? AND ssrrare_crn = ?"""
        tableName = "SSRRARE"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrcmp WHERE ssrrcmp_term_code = ? AND ssrrcmp_crn = ?"""
        tableName = "SSRRCMP"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrmaj WHERE ssrrmaj_term_code = ? AND ssrrmaj_crn = ?"""
        tableName = "SSRRMAJ"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrcls   WHERE ssrrcls_term_code = ?  AND ssrrcls_crn = ?"""
        tableName = "SSRRCLS"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrrlvl WHERE ssrrlvl_term_code = ? AND ssrrlvl_crn = ?"""
        tableName = "SSRRLVL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrresv WHERE ssrresv_term_code = ? AND ssrresv_crn = ?"""
        tableName = "SSRRESV"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrattr  WHERE ssrattr_term_code = ? AND ssrattr_crn = ?"""
        tableName = "SSRATTR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrxlst  WHERE ssrxlst_term_code = ? AND ssrxlst_crn = ?"""
        tableName = "SSRXLST"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrblck  WHERE ssrblck_term_code = ? AND ssrblck_crn = ?"""
        tableName = "SSRBLCK"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrsccd  WHERE ssrsccd_term_code = ? AND ssrsccd_crn = ?"""
        tableName = "SSRSCCD"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssbovrr  WHERE ssbovrr_term_code = ? AND ssbovrr_crn = ?"""
        tableName = "SSBOVRR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrrtst WHERE ssrrtst_term_code = ? AND ssrrtst_crn = ?"""
        tableName = "SSRRTST"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssreval  WHERE ssreval_term_code = ? AND ssreval_crn = ?"""
        tableName = "SSREVAL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrsprt  WHERE ssrsprt_term_code = ? AND ssrsprt_crn = ?"""
        tableName = "SSRSPRT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrmprt   WHERE ssrmprt_term_code = ? AND ssrmprt_crn = ?"""
        tableName = "SSRMPRT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrsrdf WHERE ssrsrdf_term_code = ? AND ssrsrdf_crn = ?"""
        tableName = "SSRSRDF"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrmrdf WHERE ssrmrdf_term_code = ? AND ssrmrdf_crn = ?"""
        tableName = "SSRMRDF"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssbssec WHERE ssbssec_term_code = ?   AND ssbssec_crn = ?"""
        tableName = "SSBSSEC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssbfsec  WHERE ssbfsec_term_code = ?  AND ssbfsec_crn = ?"""
        tableName = "SSRFSEC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE    FROM ssrrsts  WHERE ssrrsts_term_code = ? AND ssrrsts_crn = ?"""
        tableName = "SSRRSTS"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE    FROM ssrextn  WHERE ssrextn_term_code = ? AND ssrextn_crn = ?"""
        tableName = "SSREXTN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrrfnd  WHERE ssrrfnd_term_code = ? AND ssrrfnd_crn = ?"""
        tableName = "SSRRFND"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssbdesc WHERE ssbdesc_term_code = ? AND ssbdesc_crn = ?"""
        tableName = "SSBDESC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrtext WHERE ssrtext_term_code = ? AND ssrtext_crn = ?"""
        tableName = "SSRTEXT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrsyln   WHERE ssrsyln_term_code = ? AND ssrsyln_crn = ?"""
        tableName = "SSRSYLN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrsylo WHERE ssrsylo_term_code = ? AND ssrsylo_crn = ?"""
        tableName = "SSRSYLO"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE   FROM ssrsyrm   WHERE ssrsyrm_term_code = ? AND ssrsyrm_crn = ?"""
        tableName = "SSRSYRM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM ssrsytr   WHERE ssrsytr_term_code = ? AND ssrsytr_crn = ?"""
        tableName = "SSRSYTR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE  FROM shrgcom  WHERE shrgcom_term_code = ? AND shrgcom_crn = ?"""
        tableName = "SHRGCOM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM shrscom  WHERE shrscom_term_code = ? AND shrscom_crn = ?"""
        tableName = "SHRSCOM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssrclbd  WHERE ssrclbd_term_code = ? AND ssrclbd_crn = ? """
        tableName = "SSRCLBD"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM sfrwlnt  WHERE sfrwlnt_term_code = ? AND sfrwlnt_crn = ?"""
        tableName = "SFRWLNT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM sfrstcr  WHERE sfrstcr_term_code = ? AND sfrstcr_crn = ?"""
        tableName = "SFRSTCR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

        schDel = """DELETE FROM ssbsect  WHERE ssbsect_term_code = ? AND ssbsect_crn = ? """
        tableName = "SSBSECT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn, schDel)

    }


    def processTermDeletes() {
        def tableName = "SSBACRL"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFRMHRS"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SORRTRM"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFBRFST"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFBESTS"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFRRFCR"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFRRSTS"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFRRGFE"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SORRSTS"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SOREXTN"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SOBODTE"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SOBPTRM"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SOBTERM"
        deleteTermData(tableName, this.ssbsect_term_code)
    }


    private def deleteData(String tableName, term, crn, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [term, crn])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} for ${term} ${crn} from SScheduleTermDeleteDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }


    private def deleteTermData(String tableName, term) {
        def schDel = """DELETE FROM ${tableName}  WHERE ${tableName}_term_code = ? """
        try {

            int delRows = conn.executeUpdate(schDel, [term])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} for ${term} from SScheduleTermDeleteDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }


}
