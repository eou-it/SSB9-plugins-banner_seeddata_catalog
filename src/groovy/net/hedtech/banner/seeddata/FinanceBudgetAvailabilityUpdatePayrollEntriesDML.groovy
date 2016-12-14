/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability clean existing payroll entries
 */
public class FinanceBudgetAvailabilityUpdatePayrollEntriesDML {

    def spridenId
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityUpdatePayrollEntriesDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityUpdatePayrollEntriesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        updatePidmForExistingPayroll()
    }

    /**
     * Update pidm for Existing Payroll
     */
    def updatePidmForExistingPayroll() {
        def parser = new XmlParser().parseText(xmlData)
        this.spridenId = parser.SPRIDEN_ID?.text()
        try {
            final String apiQuery =
                    "  BEGIN\n" +
                            "    UPDATE  NHRDIST set NHRDIST_PIDM = (SELECT SPRIDEN_PIDM FROM SPRIDEN WHERE SPRIDEN_ID=?) where NHRDIST_PIDM= -99999999;\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall(apiQuery)
            insertCall.setString(1, spridenId)
            insertCall.execute()
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_UPDATE_PAYROLL_PIDM", 0, 1, 0, 0, 0)

        }
        catch (Exception e) {
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_UPDATE_PAYROLL_PIDM", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Executing BA Update pidm for  payroll entries" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
