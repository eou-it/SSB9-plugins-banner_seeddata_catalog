/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * TVRBDSC DML tables
 *
 */

public class WebRenegotiationDatesDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    List columns
    List indexColumns
    java.sql.RowId tableRow = null
    def tbrrwrdTermCode
    def tbrrwrdSeqNum
    def tbrrwrdStartDate
    def tbrrwrdEndDate
    def tbrrwrdActivityDate
    def tbrrwrdUserId
    def tbrrwrdSurrogateId
    def tbrrwrdVersion
    def tbrrwrdDataOrigin
    def tbrrwrdVpdiCode

    public WebRenegotiationDatesDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public WebRenegotiationDatesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processTBRRWRD()
    }

    def processTBRRWRD() {
        def newPage = new XmlParser().parseText(xmlData)
        if (newPage) {

            if (newPage.TBRRWRD_TERM_CODE?.text()) {
                this.tbrrwrdTermCode = newPage.TBRRWRD_TERM_CODE.text()
            }
            if (newPage.TBRRWRD_DATA_ORIGIN?.text() == 'CUSTOMDATE') {
                this.tbrrwrdStartDate = calculateDate(-1)
                this.tbrrwrdEndDate = calculateDate(365)
            } else {
                this.tbrrwrdStartDate = newPage.TBRRWRD_START_DATE.text()
                this.tbrrwrdEndDate = newPage.TBRRWRD_END_DATE.text()
            }
            if (newPage.TBRRWRD_SEQ_NUM?.text()) {
                this.tbrrwrdSeqNum = newPage.TBRRWRD_SEQ_NUM.text()
            }
            if (newPage.TBRRWRD_ACTIVITY_DATE?.text()) {
                this.tbrrwrdActivityDate = newPage.TBRRWRD_ACTIVITY_DATE.text()
            }
            if (newPage.TBRRWRD_USER_ID?.text()) {
                this.tbrrwrdUserId = newPage.TBRRWRD_USER_ID.text()
            }
            if (newPage.TBRRWRD_SURROGATE_ID?.text()) {
                this.tbrrwrdSurrogateId = newPage.TBRRWRD_SURROGATE_ID.text()
            }
            if (newPage.TBRRWRD_VERSION?.text()) {
                this.tbrrwrdVersion = newPage.TBRRWRD_VERSION.text()
            }
            if (newPage.TBRRWRD_VPDI_CODE?.text()) {
                this.tbrrwrdVpdiCode = newPage.TBRRWRD_VPDI_CODE.text()
            }
            if (newPage.TBRRWRD_DATA_ORIGIN?.text()) {
                this.tbrrwrdDataOrigin = 'GRAILS'
            }
            createTbrrwrdObject()
        }
    }

    private def createTbrrwrdObject() {
        def sql = """insert into TBRRWRD(TBRRWRD_TERM_CODE,TBRRWRD_SEQ_NUM,TBRRWRD_START_DATE,
TBRRWRD_END_DATE,TBRRWRD_ACTIVITY_DATE,TBRRWRD_USER_ID,TBRRWRD_SURROGATE_ID,TBRRWRD_VERSION,TBRRWRD_VPDI_CODE,
TBRRWRD_DATA_ORIGIN ) values (?,?,?,?,?,?,?,?,?,?)"""
        try {
            conn.executeInsert(sql, [tbrrwrdTermCode, tbrrwrdSeqNum, tbrrwrdStartDate, tbrrwrdEndDate, tbrrwrdActivityDate, tbrrwrdUserId, tbrrwrdSurrogateId, tvrbdscDiscEndDate, tbrrwrdVersion, tbrrwrdVpdiCode, tbrrwrdDataOrigin])
            connectInfo.tableUpdate('TBRRWRD', 0, 1, 0, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not create access to object,  $e.message"
            }
        }
    }

    private String calculateDate(int days) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        date = c.getTime();
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        formatter.format(date);
    }


}
