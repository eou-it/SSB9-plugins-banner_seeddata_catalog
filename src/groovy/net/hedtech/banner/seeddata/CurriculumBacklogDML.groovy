/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
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
