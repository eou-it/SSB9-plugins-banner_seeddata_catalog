/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat


/**
 *  Insert, update and delete Proxy/Student cross references for seed data
 */
public class ProxyCrossReferenceDML {
    
    def bannerid
    def proxy_idm
    def person_pidm
    def retp_code
    def proxy_desc
    def start_date
    def stop_date
    def create_date
    def create_user
    def passphrase
    
    java.sql.RowId tableRow = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    
    public ProxyCrossReferenceDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ProxyCrossReferenceDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertGprxref()
    }


    def parseXmlData() {
        def crossRef = new XmlParser().parseText(xmlData)
        
        this.proxy_idm = crossRef.GPRXREF_PROXY_IDM.text()

        if (crossRef.PROXY_ID) {
            this.proxy_idm = connectInfo.saveProxyIdm
        }
        
        if (crossRef.BANNERID) {
            this.person_pidm = connectInfo.saveStudentPidm
            this.bannerid = crossRef.BANNERID.text()
        }
        else {
            this.person_pidm = crossRef.GPRXREF_PERSON_PIDM.text()
        }

        this.retp_code = crossRef.GPRXREF_RETP_CODE.text()
        this.proxy_desc = crossRef.GPRXREF_PROXY_DESC.text()
        this.start_date = crossRef.GPRXREF_START_DATE.text()
        this.stop_date = crossRef.GPRXREF_STOP_DATE.text()
        this.create_date = crossRef.GPRXREF_CREATE_DATE.text()
        this.create_user = crossRef.GPRXREF_CREATE_USER.text()
        this.passphrase = crossRef.GPRXREF_PASSPHRASE.text()
    }
    
    
    def insertGprxref() {
        tableRow = null
        String rowSQL = """select rowid table_row from GPRXREF
          where gprxref_proxy_idm = ?
          and gprxref_person_pidm = ? """
        try {
            conn.eachRow(rowSQL, [this.proxy_idm.toInteger(), this.person_pidm.toInteger()]) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting GPRXREF rowid ProxyCrossReferenceDML.groovy: $e.message"
            }
        }
        
        String API
        if (!tableRow) {
            API = "{call  gp_gprxref.p_create( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
        }
        else {
            API = "{call  gp_gprxref.p_update( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
        }
        CallableStatement insertCall = this.connectCall.prepareCall(API)
        // parm 1 p_proxy_idm                 gprxref.gprxref_proxy_idm%TYPE NUMBER
        if (isNull(this.proxy_idm)) {
            insertCall.setNull(1, java.sql.Types.INTEGER)
        }
        else {
            insertCall.setInt(1, this.proxy_idm.toInteger())
        }
        
        // parm 2 p_person_pidm               gprxref.gprxref_person_pidm%TYPE NUMBER
        if (isNull(this.person_pidm)) {
            insertCall.setNull(2, java.sql.Types.INTEGER)
        }
        else {
            insertCall.setInt(2, this.person_pidm.toInteger())
        }
        
        // parm 3 p_retp_code                 gprxref.gprxref_retp_code%TYPE VARCHAR2
        insertCall.setString(3, this.retp_code)
        
        // parm 4 p_proxy_desc                gprxref.gprxref_proxy_desc%TYPE DEFAULT NULL VARCHAR2
        insertCall.setString(4, this.proxy_desc)
            
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // parm 5 p_start_date                gprxref.gprxref_start_date%TYPE DATE
        if (isNull(this.start_date)) 
        { 
            insertCall.setNull(5, java.sql.Types.DATE) 
        }
        else {
            def ddate = new ColumnDateValue(this.start_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(5, sqlDate)
        }
        
        // parm 6 p_stop_date                 gprxref.gprxref_stop_date%TYPE DATE
        if (isNull(this.stop_date)) 
        { 
            insertCall.setNull(6, java.sql.Types.DATE) 
        }
        else {
            def ddate = new ColumnDateValue(this.stop_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(6, sqlDate)
        }
        
        // parm 7 p_create_user               gprxref.gprxref_create_user%TYPE DEFAULT gb_common.f_sct_user VARCHAR2
        insertCall.setString(7, this.create_user)
        
        // parm 8 p_create_date               gprxref.gprxref_create_date%TYPE DEFAULT NULL DATE
        if (isNull(this.create_date)) 
        { 
            insertCall.setNull(8, java.sql.Types.DATE) 
        }
        else {
            def ddate = new ColumnDateValue(this.create_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(8, sqlDate)
        }
        
        // parm 9 p_user_id                   gprxref.gprxref_user_id%TYPE DEFAULT gb_common.f_sct_user VARCHAR2
        insertCall.setString(9, connectInfo.userID)
        
        // parm 10 p_passphrase                gprxref.gprxref_passphrase%TYPE DEFAULT NULL VARCHAR2
        insertCall.setString(10, this.passphrase)
        
        // parm 11 p_rowid_out             OUT gb_common.internal_record_id_type ROWID
        insertCall.registerOutParameter(11, java.sql.Types.ROWID)
        
        if (connectInfo.debugThis) {
            println "Insert GPRXREF ${this.proxy_idm} ${this.person_pidm} ${this.retp_code}"
        }
        try {
            insertCall.executeUpdate()
            if(!tableRow) {
                connectInfo.tableUpdate("GPRXREF", 0, 1, 0, 0, 0)
            }
            else {
                connectInfo.tableUpdate("GPRXREF", 0, 0, 1, 0, 0)
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GPRXREF", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert GPRXREF ${this.proxy_idm} ${this.person_pidm} ${this.retp_code}"
                println "Problem executing insert for table GPRXREF from ProxyCrossReferenceDML.groovy: $e.message"
            }
        }
        finally {
            insertCall.close()
        }
    }
    
    private boolean isNull(def value) {
        return (value == "" || value == null || !value) 
    }
}
