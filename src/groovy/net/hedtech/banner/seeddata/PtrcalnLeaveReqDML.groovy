/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ptrcaln LeaveRequest DML tables
 * Dynamically update the ptrcaln StartDate as Future Date
 */

public class PtrcalnLeaveReqDML {
    int currRule = 0
    def componentId = null
    def subComponentId = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public PtrcalnLeaveReqDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public PtrcalnLeaveReqDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processPtrcaln()
    }

    /**
     * Process the ptrcaln records.      */

    def processPtrcaln(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = false
        def ptrcalnParamList = [(apiData.ADD_MONTH[0]?.value()[0] - 1),
                         apiData.ADD_MONTH[0]?.value()[0],
                         apiData.PTRCALN_YEAR[0]?.value()[0],
                         apiData.PTRCALN_PICT_CODE[0]?.value()[0],
                         apiData.PTRCALN_PAYNO[0]?.value()[0]]


        def perhourParamList = [(apiData.ADD_MONTH[0]?.value()[0] - 1),
                                apiData.PTRCALN_YEAR[0]?.value()[0],
                                apiData.PTRCALN_PICT_CODE[0]?.value()[0],
                                apiData.PTRCALN_PAYNO[0]?.value()[0]]

        updatePtrcalnStartDate(ptrcalnParamList)

        updatePerhourTimeEntryDate(perhourParamList)

    }


    private def updatePtrcalnStartDate(List paramList){

        def count = -1
        //
        try {
           count = this.conn.executeUpdate("""
                            update ptrcaln set ptrcaln_start_date = ADD_MONTHS((LAST_DAY(SYSDATE)+1),?) ,
                            ptrcaln_end_date = ADD_MONTHS((LAST_DAY(SYSDATE)+1),? )-1
                            where ptrcaln_year = ? and ptrcaln_pict_code = ? and ptrcaln_payno = ?
                            and (ptrcaln_start_day is null or ptrcaln_start_day < SYSDATE)""",
                    paramList)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRCALN ID in PtrcalnDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRCALN ID for ${connectInfo.tableName}.")
        println "PTRCALN StartDates update count for LeaveRequest "+count
        return count
    }


    private def updatePerhourTimeEntryDate(List paramList){

        def count = -1
        //
        try {
            count = this.conn.executeUpdate("""
                            update perhour set perhour_time_entry_date = ADD_MONTHS((LAST_DAY(SYSDATE)+1),?) ,
                            where perhour_jobs_seqno in (select perjobs_seqno from perjobs where perjobs_year = ? 
                                    and perjobs_pict_code = ? and perjobs_payno = ?
                                    and (perhour_time_entry_date is null or perhour_time_entry_date < SYSDATE)
                                    and perjobs_action_ind = 'R'""",
                    paramList)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRCALN ID in PtrcalnDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRCALN ID for ${connectInfo.tableName}.")
        println "PERHOUR StartDates update count for LeaveRequest "+count
        return count
    }


}