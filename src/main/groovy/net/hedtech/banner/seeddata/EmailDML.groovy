/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection

/**
 *  Insert, update and delete email for seed data
 */
public class EmailDML {
    def bannerid
    def goremal_pidm
    def goremal_emal_code
    def goremal_email_address
    def goremal_status_ind
    def goremal_preferred_ind
    def goremal_comment
    def goremal_disp_web_ind

    java.sql.RowId tableRow = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public EmailDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmailDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertGoremal()
    }


    def parseXmlData() {
        def email = new XmlParser().parseText(xmlData)
        if (email.BANNERID) {
            this.goremal_pidm = connectInfo.saveStudentPidm
            this.bannerid = email.BANNERID.text()
        }
        else {
            this.goremal_pidm = email.GOREMAL_PIDM.text()
        }

        this.goremal_emal_code = email.GOREMAL_EMAL_CODE.text()
        this.goremal_email_address = email.GOREMAL_EMAIL_ADDRESS.text()
        this.goremal_status_ind = email.GOREMAL_STATUS_IND.text()
        this.goremal_preferred_ind = email.GOREMAL_PREFERRED_IND.text()
        this.goremal_comment = email.GOREMAL_COMMENT.text()
        this.goremal_disp_web_ind = email.GOREMAL_DISP_WEB_IND.text()
    }


    def insertGoremal() {
        tableRow = null
        String rowSQL = """select rowid table_row from GOREMAL
          where goremal_pidm = ?
          and (( goremal_emal_code = ?
          and goremal_email_address = ? )
          or ( goremal_preferred_ind = 'Y' and ? = 'Y' ) ) """
        try {
            conn.eachRow(rowSQL, [this.goremal_pidm, this.goremal_emal_code, this.goremal_email_address, this.goremal_preferred_ind]) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting GOREMAL rowid emailDML.groovy: $e.message"
            }
        }
        // println """Insert GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}
        //         rowid = ${tableRow} """
        if (!tableRow) {
            //  parm count is 10
            try {
                String API = "{call  gb_email.p_create(?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(API)
                // parm 1 p_pidm      goremal_pidm NUMBER
                if ((this.goremal_pidm == "") || (this.goremal_pidm == null) || (!this.goremal_pidm)) {
                    insertCall.setNull(1, java.sql.Types.INTEGER)
                }
                else {
                    insertCall.setInt(1, this.goremal_pidm.toInteger())
                }

                // parm 2 p_emal_code      goremal_emal_code VARCHAR2
                insertCall.setString(2, this.goremal_emal_code)

                // parm 3 p_email_address      goremal_email_address VARCHAR2
                insertCall.setString(3, this.goremal_email_address)

                // parm 4 p_status_ind      goremal_status_ind VARCHAR2
                insertCall.setString(4, this.goremal_status_ind)

                // parm 5 p_preferred_ind      goremal_preferred_ind VARCHAR2
                insertCall.setString(5, this.goremal_preferred_ind)

                // parm 6 p_user_id      goremal_user_id VARCHAR2
                insertCall.setString(6, connectInfo.userID)
                // parm 7 p_comment      goremal_comment VARCHAR2
                insertCall.setString(7, this.goremal_comment)

                // parm 8 p_disp_web_ind      goremal_disp_web_ind VARCHAR2
                if (!this.goremal_disp_web_ind) { insertCall.setString(8, "Y") }
                else { insertCall.setString(8, this.goremal_disp_web_ind) }

                // parm 9 p_data_origin      goremal_data_origin VARCHAR2
                insertCall.setString(9, connectInfo.dataOrigin)
                // parm 10 p_rowid_out      goremal_rowid_out VARCHAR2
                insertCall.registerOutParameter(10, java.sql.Types.ROWID)
                if (connectInfo.debugThis) {
                    println "Insert GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("GOREMAL", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
                        println "Problem executing insert for table GOREMAL from emailDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
                    println "Problem setting up insert for table GOREMAL from emailDML.groovy: $e.message"
                }
            }
        }
        // disable update until setRowId is fixed
        //  else {
        //      updateGoremal() }
    }


    def updateGoremal() {

        //  parm count is 10
        try {
            String API = "{call  gb_email.p_update(?,?,?,?,?,?,?,?,?,?)}"
            CallableStatement insertCall = this.connectCall.prepareCall(API)
            // parm 1 p_pidm      goremal_pidm NUMBER
            if ((this.goremal_pidm == "") || (this.goremal_pidm == null) || (!this.goremal_pidm)) {
                insertCall.setNull(1, java.sql.Types.INTEGER)
            }
            else {
                insertCall.setInt(1, this.goremal_pidm.toInteger())
            }

            // parm 2 p_emal_code      goremal_emal_code VARCHAR2
            insertCall.setString(2, this.goremal_emal_code)

            // parm 3 p_email_address   goremal_email_address VARCHAR2
            insertCall.setString(3, this.goremal_email_address)

            // parm 4 p_status_ind      goremal_status_ind VARCHAR2
            insertCall.setString(4, this.goremal_status_ind)

            // parm 5 p_preferred_ind  gor goremal_preferred_indgo VARCHAR2
            if (!this.goremal_preferred_ind) { insertCall.setString(5, this.goremal_preferred_ind) }
            else { insertCall.setString(5, "N") }
            // parm 6 p_user_id      goremal_user_id VARCHAR2
            insertCall.setString(6, connectInfo.userID)
            // parm 7 p_comment      goremal_comment VARCHAR2
            insertCall.setString(7, this.goremal_comment)

            // parm 8 p_disp_web_ind   go goremal_disp_web_indg VARCHAR2
            if (!this.goremal_disp_web_ind) { insertCall.setString(8, "Y") }
            else { insertCall.setString(8, this.goremal_disp_web_ind) }

            // parm 9 p_data_origin     goremal_data_origin VARCHAR2
            insertCall.setString(9, connectInfo.dataOrigin)

            // parm 10 p_rowid      goremal_rowid VARCHAR2
            if (tableRow) insertCall.setRowId(10, tableRow)
            else insertCall.setNull(10, java.sql.Types.ROWID)
            //   insertCall.setStgring(10, tableRow.toString())

            if (connectInfo.debugThis) {
                println "Update GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
            }
            try {
                insertCall.executeUpdate()
                connectInfo.tableUpdate("GOREMAL", 0, 0, 1, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
                    println "Problem executing update for table GOREMAL from emailDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Update GOREMAL ${this.goremal_pidm} ${this.goremal_emal_code} ${this.goremal_email_address}"
                println "Problem setting up update for table GOREMAL from emailDML.groovy: $e.message"
            }
        }

    }
}

  
