/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

public class ProxyEltrDML {

    def bannerid
    def proxy_idm
    def person_pidm
    def syst_code
    def ctyp_code
    def ctyp_url
    def ctyp_exp_date
    def ctyp_exe_date
    def create_date
    def create_user
    boolean addToken


    java.sql.RowId tableRow = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData

    public ProxyEltrDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ProxyEltrDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertGpbeltr()
    }


    def parseXmlData() {
        def eltr = new XmlParser().parseText(xmlData)

        this.proxy_idm = eltr.GPBELTR_PROXY_IDM.text()

        if (eltr.PROXY_ID) {
            this.proxy_idm = connectInfo.saveProxyIdm
        }

        if (eltr.BANNERID) {
            this.person_pidm = connectInfo.saveStudentPidm
            this.bannerid = eltr.BANNERID.text()
        }
        else {
            this.person_pidm = eltr.GPBELTR_PERSON_PIDM.text()
        }

        this.syst_code = eltr.GPBELTR_SYST_CODE.text()
        this.ctyp_code = eltr.GPBELTR_CTYP_CODE.text()
        this.ctyp_url = eltr.GPBELTR_CTYP_URL.text()
        this.ctyp_exp_date = eltr.GPBELTR_CTYP_EXP_DATE.text()
        this.ctyp_exe_date = eltr.GPBELTR_CTYP_EXE_DATE.text()
        this.addToken = eltr.ADDTOKEN.text().equals('Y')

        this.create_date = eltr.GPBELTR_CREATE_DATE.text()
        this.create_user = eltr.GPBELTR_CREATE_USER.text()
    }

    def insertGpbeltr() {
        tableRow = null
        String rowSQL = """select rowid table_row from GPBELTR
          where gpbeltr_proxy_idm = ?
          and gpbeltr_syst_code = ?
          and gpbeltr_ctyp_code = ? """
        try {
            conn.eachRow(rowSQL, [this.proxy_idm.toInteger(), this.syst_code, this.ctyp_code]) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting GPRXREF rowid ProxyEltrDML.groovy: $e.message"
            }
        }

        String API
        if (!tableRow) {
            API = "{call  gp_gpbeltr.P_Create(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
        }
        else {
            return
        }
        CallableStatement insertCall = this.connectCall.prepareCall(API)
        // parm 1 p_syst_code                 gpbeltr.gpbeltr_syst_code%TYPE VARCHAR2
        insertCall.setString(1, this.syst_code)

        // parm 2 p_ctyp_code                 gpbeltr.gpbeltr_ctyp_code%TYPE VARCHAR2
        insertCall.setString(2, this.ctyp_code)

        // parm 3 p_ctyp_url                  gpbeltr.gpbeltr_ctyp_url%TYPE DEFAULT NULL VARCHAR2
        if(this.addToken) {
            insertCall.setNull(3, java.sql.Types.VARCHAR)
        }
        else {
            insertCall.setString(3, this.ctyp_url)
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // parm 4 p_ctyp_exp_date             gpbeltr.gpbeltr_ctyp_exp_date%TYPE DEFAULT NULL DATE
        if (isNull(this.ctyp_exp_date))
        {
            insertCall.setNull(4, java.sql.Types.DATE)
        }
        else {
            def ddate = new ColumnDateValue(this.ctyp_exp_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(4, sqlDate)
        }

        // parm 5 p_ctyp_exe_date             gpbeltr.gpbeltr_ctyp_exe_date%TYPE DEFAULT NULL DATE
        if (isNull(this.ctyp_exe_date))
        {
            insertCall.setNull(5, java.sql.Types.DATE)
        }
        else {
            def ddate = new ColumnDateValue(this.ctyp_exe_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(5, sqlDate)
        }

        // parm 6 gpbeltr.gpbeltr_transmit_date%TYPE DEFAULT NULL DATE
        insertCall.setNull(6, java.sql.Types.DATE)

        // parm 7 p_proxy_idm                 gpbeltr.gpbeltr_proxy_idm%TYPE
        if (isNull(this.proxy_idm)) {
            insertCall.setNull(7, java.sql.Types.INTEGER)
        }
        else {
            insertCall.setInt(7, this.proxy_idm.toInteger())
        }

        // parm 8 p_proxy_old_data            gpbeltr.gpbeltr_proxy_old_data%TYPE DEFAULT NULL VARCHAR2
        insertCall.setNull(8, java.sql.Types.VARCHAR)

        // parm 9 p_proxy_new_data            gpbeltr.gpbeltr_proxy_new_data%TYPE DEFAULT NULL VARCHAR2
        insertCall.setNull(9, java.sql.Types.VARCHAR)

        // parm 10 p_person_pidm               gpbeltr.gpbeltr_person_pidm%TYPE DEFAULT NULL
        if (isNull(this.person_pidm)) {
            insertCall.setNull(10, java.sql.Types.INTEGER)
        }
        else {
            insertCall.setInt(10, this.person_pidm.toInteger())
        }

        // parm 11 p_user_id                   gpbeltr.gpbeltr_user_id%TYPE DEFAULT gb_common.f_sct_user VARCHAR2
        insertCall.setString(11, connectInfo.userID)

        // parm 12 p_create_date               gpbeltr.gpbeltr_create_date%TYPE DEFAULT NULL DATE
        if (isNull(this.create_date))
        {
            insertCall.setNull(12, java.sql.Types.DATE)
        }
        else {
            def ddate = new ColumnDateValue(this.create_date)
            String unfDate = ddate.formatJavaDate()
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCall.setDate(12, sqlDate)
        }

        // parm 13 p_create_user               gpbeltr.gpbeltr_create_user%TYPE DEFAULT gb_common.f_sct_user VARCHAR2
        insertCall.setString(13, this.create_user)

        // parm 14 p_rowid_out      OUT NOCOPY gb_common.internal_record_id_type ROWID
        insertCall.registerOutParameter(14, java.sql.Types.ROWID)


        if (connectInfo.debugThis) {
            println "Insert GPBELTR ${this.proxy_idm} ${this.person_pidm} ${this.ctyp_code} ${this.ctyp_url}"
        }
        try {
            insertCall.executeUpdate()
            if(this.addToken) {
                def rowID = insertCall.getRowId(14)
                CallableStatement updateCall = this.connectCall.prepareCall("""{call gp_gpbeltr.P_Update (
      p_ctyp_code      => ?,
      p_ctyp_url => ? || twbkbssf.F_Encode (?),
      p_user_id  => ?,
      p_rowid    => ?
      )}""")
                updateCall.setString(1, this.ctyp_code)
                updateCall.setString(2, this.ctyp_url)
                updateCall.setRowId(3, rowID)
                updateCall.setString(4, connectInfo.userID)
                updateCall.setRowId(5, rowID)
                updateCall.executeUpdate()
            }

            if(!tableRow) {
                connectInfo.tableUpdate("GPBELTR", 0, 1, 0, 0, 0)
            }
//            else {
//                connectInfo.tableUpdate("GPBELTR", 0, 0, 1, 0, 0)
//            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GPBELTR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert GPBELTR ${this.proxy_idm} ${this.person_pidm} ${this.ctyp_code} ${this.ctyp_url}"
                println "Problem executing insert for table GPBELTR from ProxyEltrDML.groovy: $e.message"
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
