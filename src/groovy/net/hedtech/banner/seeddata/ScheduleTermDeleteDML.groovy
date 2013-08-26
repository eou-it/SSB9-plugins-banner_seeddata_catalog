/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
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
        processTermDeletes()
    }


    def deleteSchedule() {
        String ssbsectRec = """SELECT ssbsect_crn FROM ssbsect
                       WHERE  ssbsect_term_code = ? """
        conn.eachRow(ssbsectRec, [this.ssbsect_term_code]) {row ->
            this.ssbsect_crn = row.ssbsect_crn
            processDelete()
        }

    }


    def processDelete() {

        def tableName = "SSRMEET"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SFRBKOP"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SIRASGN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRLINK"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SFRWLNT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRCORQ"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRFEES"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRATT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRCHR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRDEP"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBWLSC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRCOL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRDEG"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRPRG"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRARE"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRCMP"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRMAJ"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRCLS"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRLVL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRESV"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRATTR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRXLST"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRBLCK"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSCCD"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBOVRR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRTST"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSREVAL"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSPRT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRMPRT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSRDF"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRMRDF"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBSSEC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBFSEC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRSTS"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSREXTN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRRFND"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBDESC"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRTEXT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSYLN"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSYLO"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSYRM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRSYTR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SHRGCOM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SHRSCOM"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSRCLBD"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SFRWLNT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SFRSTCR"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

        tableName = "SSBSECT"
        deleteData(tableName, this.ssbsect_term_code, this.ssbsect_crn)

    }


    def processTermDeletes() {

        def tableName = "SSBACRL"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFBBLCD"
        deleteTermData(tableName, this.ssbsect_term_code)

        tableName = "SFRPABC"
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


    private def deleteData(String tableName, term, crn) {
        def schDel = """DELETE FROM ${tableName}  WHERE ${tableName}_term_code = ? and ${tableName}_crn = ? """
        try {

            int delRows = conn.executeUpdate(schDel, [term, crn])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName} for ${term} ${crn} from SScheduleTermDeleteDML.groovy: $e.message"
                println "${schDel}"
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
                println "${schDel}"
            }
        }
    }


    private def deleteData(String tableName) {
        def schDel = """DELETE ${tableName}"""
        try {

            int delRows = conn.executeUpdate(schDel)
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing  ${tableName}  from SScheduleTermDeleteDML.groovy: $e.message"
                println "${schDel}"
            }
        }
    }

}
