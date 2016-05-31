/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import oracle.net.aso.e

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by apoliski on 5/25/2016.
 */
class EmployeeBenefitsIDDML {
    def bannerid
    def pdrbded_pidm
    def pdrbded_bdca_code
    def pdrbded_add_repl_ind
    def pdrbded_add_repl_empl
    def pdrbded_add_repl_empr
    def pdrbded_add_repl_tax_base
    def pdrbded_arr_status
    def pdrbded_arr_balance
    def pdrbded_bond_balance
    def pdrbded_begin_date
    def pdrbded_end_date
    def pdrbded_activity_date
    def pdrbded_arr_recover_max
    def pdrbded_add_repl_pict_code
    def pdrbded_user_id
    def pdrbded_data_origin
    def pdrbded_surrogate_id
    def pdrbded_version
    def pdrbded_vpdi_code

    def InputData connectInfo
    Sql conn
    Connection connectCall

    def xmlData

    def PIDM

    public EmployeePersonIDDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }

    public EmployeeBenefitsIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processEmployeeBenefits()
    }

    def parseXmlData() {
        def pdrbded = new XmlParser().parseText(xmlData)
        this.bannerid = pdrbded.BANNERID
        this.pdrbded_bdca_code = pdrbded.PDRBDED_BDCA_CODE.text()
        this.pdrbded_add_repl_ind = pdrbded.PDRBDED_ADD_REPL_IND.text()
        this.pdrbded_add_repl_empl = pdrbded.PDRBDED_ADD_REPL_EMPL.text()
        this.pdrbded_add_repl_empr = pdrbded.PDRBDED_ADD_REPL_EMPR.text()
        this.pdrbded_add_repl_tax_base = pdrbded.PDRBDED_ADD_REPL_TAX_BASE.text()
        this.pdrbded_arr_status = pdrbded.PDRBDED_ARR_STATUS.text()
        this.pdrbded_arr_balance = pdrbded.PDRBDED_ARR_BALANCE.text()
        this.pdrbded_bond_balance = pdrbded.PDRBDED_BOND_BALANCE.text()
        this.pdrbded_begin_date = pdrbded.PDRBDED_BEGIN_DATE.text()
        this.pdrbded_end_date = pdrbded.PDRBDED_END_DATE.text()
        this.pdrbded_activity_date = pdrbded.PDRBDED_ACTIVITY_DATE.text()
        this.pdrbded_arr_recover_max = pdrbded.PDRBDED_ARR_RECOVER_MAX.text()
        this.pdrbded_add_repl_pict_code = pdrbded.PDRBDED_ADD_REPL_PICT_CODE.text()
        this.pdrbded_user_id = pdrbded.PDRBDED_USER_ID.text()
        this.pdrbded_data_origin = pdrbded.PDRBDED_DATA_ORIGIN.text()
        this.pdrbded_surrogate_id = pdrbded.PDRBDED_SURROGATE_ID.text()
        this.pdrbded_version = pdrbded.PDRBDED_VERSION.text()
        this.pdrbded_vpdi_code = pdrbded.PDRBDED_VPDI_CODE.text()
    }

    def processEmployeeBenefits() {
        PIDM = null
        String pidmsql = """select * from spriden  where spriden_id = ?"""

        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeBenefitsIDDML,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }
        ColumnDateValue ddate
        SimpleDateFormat formatter
        String unfDate
        java.sql.Date sqlDate

        if (PIDM) {
            try {
                def cntDeductions = 0
                String employeeDeductionsSql = """select * from pdrbded  where pdrbded_pidm = ?"""
                this.conn.eachRow(employeeDeductionsSql, [PIDM]) { trow ->
                    cntDeductions++
                }
                if (cntDeductions) {
                    deleteData()
                }
                String API = "{call pb_deduction_base.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                insertCall.setInt(1, this.PIDM.toInteger())
                insertCall.setString(2, this.pdrbded_bdca_code)
                insertCall.setString(3, this.pdrbded_add_repl_ind)
                // parm 4
                if ((this.pdrbded_add_repl_empl == "") || (this.pdrbded_add_repl_empl == null) ||
                        (!this.pdrbded_add_repl_empl)) {
                    insertCall.setNull(4, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(4, this.pdrbded_add_repl_empl.toDouble())
                }
                // parm 5
                if ((this.pdrbded_add_repl_empr == "") || (this.pdrbded_add_repl_empr == null) ||
                        (!this.pdrbded_add_repl_empr)) {
                    insertCall.setNull(5, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(5, this.pdrbded_add_repl_empr.toDouble())
                }
                // parm 6
                if ((this.pdrbded_add_repl_tax_base == "") || (this.pdrbded_add_repl_tax_base == null) ||
                        (!this.pdrbded_add_repl_tax_base)) {
                    insertCall.setNull(6, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(6, this.pdrbded_add_repl_tax_base.toDouble())
                }
                //parm 7
                insertCall.setString(7, this.pdrbded_arr_status)
                // parm 8
                if ((this.pdrbded_arr_balance == "") || (this.pdrbded_arr_balance == null) ||
                        (!this.pdrbded_arr_balance)) {
                    insertCall.setNull(8, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(8, this.pdrbded_arr_balance.toDouble())
                }
                // parm 9
                if ((this.pdrbded_bond_balance == "") || (this.pdrbded_bond_balance == null) ||
                        (!this.pdrbded_bond_balance)) {
                    insertCall.setNull(9, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(9, this.pdrbded_bond_balance.toDouble())
                }
                // parm 10
                if ((this.pdrbded_begin_date == "") || (this.pdrbded_begin_date == null) ||
                        (!this.pdrbded_begin_date)) {
                    insertCall.setNull(10, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pdrbded_begin_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(10, sqlDate)
                }
                // parm 11
                if ((this.pdrbded_end_date == "") || (this.pdrbded_end_date == null) ||
                        (!this.pdrbded_end_date)) {
                    insertCall.setNull(11, java.sql.Types.DATE)
                } else {
                    ddate = new ColumnDateValue(this.pdrbded_begin_date)
                    unfDate = ddate.formatJavaDate()
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                    insertCall.setDate(11, sqlDate)
                }
                // parm 12
                if ((this.pdrbded_arr_recover_max == "") || (this.pdrbded_arr_recover_max == null) ||
                        (!this.pdrbded_arr_recover_max)) {
                    insertCall.setNull(12, java.sql.Types.DOUBLE)
                } else {
                    insertCall.setDouble(12, this.getPdrbded_arr_recover_max().toDouble())
                }
                // parm 13
                insertCall.setString(13, this.pdrbded_add_repl_pict_code)

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("PDRBDED", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("PDRBDED", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert PEBEMPL ${this.bannerid}}"
                        println "Problem executing insert for table PDRBDED from EmployeeBenefitsIDDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("PDRBDED", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert PDRBDED ${this.bannerid} }"
                    println "Problem executing insert for table PDRBDED from EmployeeBenefitsIDDML.groovy: $e.message"
                }
            }
        }

    }

    def deleteData() {
        String selectSql = """select pdrbded_pidm from pdrbded where pdrbded_pidm = ?"""
        try {
            conn.eachRow(selectSql, [connectInfo.savePidm]) {
                // Benefits and Deductions
                deleteData("PDRFLEX", "delete from pdrflex where pdrflex_pidm = ?")
                deleteData("PDRBFLX", "delete from pdrbflx where pdrbflx_pidm = ?")
                deleteData("PERPCRE", "delete from perpcre where perpcre_pidm = ?")
                deleteData("PDRXPID", "delete from pdrxpid where pdrxpid_pidm = ?")
                deleteData("PDRPERS", "delete from pdrpers where pdrpers_pidm = ?")
                deleteData("PHRDEDN", "delete from phrdedn where phrdedn_pidm = ?")
                deleteData("PERDHIS", "delete from perdhis where perdhis_pidm = ?")
                deleteData("PDRDEDN", "delete from pdrdedn where pdrdedn_pidm = ?")
                deleteData("PDRBDED", "delete from pdrbded where pdrbded_pidm = ?")
            }
        }
        catch ( e ) {
           connectInfo.tableUpdate("PDRBDED", 0, 0, 0, 1, 0)
           if (connectInfo.showErrors) {
               println "Problem executing select or delete for table PDRBDED from EmployeeBenefitsIDDML.groovy: $e.message"
           }
        }
    }
    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [connectInfo.savePidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for employee base deduction ${connectInfo.savePidm} from EmployeeBenefitsIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }

}
