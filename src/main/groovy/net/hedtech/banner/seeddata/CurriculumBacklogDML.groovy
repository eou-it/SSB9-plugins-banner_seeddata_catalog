/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  Execute sfkedit for courses
 */
public class CurriculumBacklogDML {
    def bannerid
    def term_code
    def pidm
    def key_seqno
    def learnerModule

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null
    def termCode


    public CurriculumBacklogDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processBacklog()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText(xmlData)
        this.pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID.text()
        this.term_code = apiData.TERM_CODE.text()
        this.key_seqno = apiData.KEY_SEQNO.text()
        this.learnerModule = apiData.LMOD.text()
        if (connectInfo.debugThis) {
            println "--------- New XML CURRICULUM BACKLOG record ----------"
            println "${bannerid} ${learnerModule} ${key_seqno}   ${term_code}   "
        }
    }

    def processBacklog() {

        try {
            def regDate = new Date()
            if (connectInfo.debugThis) {
                println "Execute Backlog ${bannerid} ${learnerModule} ${key_seqno}   ${term_code}  "
            }
            conn.call( " {call soklcur.p_backload_curr(?,?,?,?)  }",
                             [learnerModule,
                             term_code, 
                             key_seqno.toInteger(),
                             connectInfo.saveStudentPidm.toInteger() ])



        }
        catch (Exception e) {
            connectInfo.tableUpdate("CURRICULUMBACK", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Execute Backlog ${bannerid} ${learnerModule} ${key_seqno}   ${term_code} "
                println "Problem executing executing SOKLCUR in CurriculumBacklogDML.groovy: $e.message"
            }
        }
    }
}
