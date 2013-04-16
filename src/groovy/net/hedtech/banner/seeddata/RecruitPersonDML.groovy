/** *******************************************************************************
 Copyright 2012 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  General Person DDL - methods to insert / update general person data:  spriden,
 *  spbpers, spraddr, sprtele
 */
public class RecruitPersonDML {
    def ID
    def PIDM
    def lastName
    def firstName
    def middle
    def SSN
    def birthDate
    def legacy
    def ethn
    def ethnCde
    def confirmCD
    def confirmDT
    def mrtl
    def relg
    def sex
    def prefFirst
    def prefix
    def suffix
    def citz
    def dead
    def pidm

    def cntSpriden = 0

    def InputData connectInfo
    Sql conn
    Connection connectCall
     def xmlData


    public RecruitPersonDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processRecruit()

    }


     def processRecruit() {
        def apiData = new XmlParser().parseText(xmlData)
        String ssql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        def cntSpriden = 0
        try {
            this.conn.eachRow(ssql, [apiData.SPRIDEN_ID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in StudentPersonIDDML,  ${apiData.SPRIDEN_ID.text()}  from SPRIDEN. $e.message"
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
        String deleteSQL = ""

        deleteSQL = "delete from spbpers where spbpers_pidm = ${connectInfo.saveStudentPidm }"
        try {
            def cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPBPERS ${connectInfo.saveStudentPidm } ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from spraddr where spraddr_pidm = ${connectInfo.saveStudentPidm }"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPRADDR ${this.PIDM} ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from sprtele where sprtele_pidm = ${connectInfo.saveStudentPidm }"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPRTELE ${this.PIDM} ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from goremal where goremal_pidm = ${connectInfo.saveStudentPidm}"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete GOREMAL ${this.PIDM} ${this.ID} ${this.lastName}from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from srbrecr where srbrecr_pidm = ${connectInfo.saveStudentPidm }"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SRBRECR ${this.PIDM} ${this.ID} ${this.lastName}from RecruitPersonDML.groovy: $e.message"
            }
        }
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SORFOLK", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SORFOLK", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SORFOLK ${this.PIDM} ${this.ID} ${this.lastName}from RecruitPersonDML.groovy: $e.message"
            }
        }
    }
}
