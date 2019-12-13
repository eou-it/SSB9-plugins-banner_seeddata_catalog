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
public class FinanceBudgetAvailabilityCleanPayrollEntriesDML {

    def seedPayrollPidm
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityCleanPayrollEntriesDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityCleanPayrollEntriesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        cleanExistingPayroll()
    }

    /**
     * cleans Existing Payroll
     */
    def cleanExistingPayroll() {
        def parser = new XmlParser().parseText(xmlData)
        this.seedPayrollPidm = parser.PAYROLL_PIDM?.text() as int
        try {
            final String apiQuery =
                    "  BEGIN\n" +
                            "    DELETE FROM NHRDIST where NHRDIST_PIDM= ?;\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall(apiQuery)
            insertCall.setInt(1, seedPayrollPidm)
            insertCall.execute()
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_PRE_CREATE_PAYROLL", 0, 1, 0, 0, 0)

        }
        catch (Exception e) {
            connectInfo.tableUpdate("BUDGET_AVAILABILITY_PRE_CREATE_PAYROLL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Executing BA Pre create payroll" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
