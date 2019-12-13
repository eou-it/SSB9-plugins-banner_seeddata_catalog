/*********************************************************************************
 Copyright 2010-2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.RowId

/**
 * General Person ID DML
 */

public class CreateWebtailorUserDML {

    def wtRole
    def userPidm
    def bannerId


    def InputData connectInfo
    Sql conn
    Connection connectCall
    RowId tableRow = null
    def xmlData

    public CreateWebtailorUserDML( InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public CreateWebtailorUserDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        /*
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectInfo.dataSource=null
        def newConn = new ConnectDB(connectInfo)
        this.connectCall = newConn.getSqlConnection().connection
        this.xmlData = xmlData
        */

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()

        if (this.wtRole) {
            createWtRole()
        }
    }


    def parseXmlData() {
        def webTailorUser = new XmlParser().parseText(xmlData)

        if (webTailorUser.WEBTAILORROLE?.text()) {
            this.bannerId = webTailorUser.BANNERID.text()

            String findPidm = """select spriden_pidm from spriden where spriden_id = ? and spriden_change_ind is null """
            def spridenRow = conn.firstRow( findPidm, [bannerId] )
            if (spridenRow) {
                this.userPidm = spridenRow.SPRIDEN_PIDM.toString()
            }

            this.wtRole = webTailorUser.WEBTAILORROLE.text()
        }
    }


    def createWtRole() {
        def sqlf = "select count(*) cnt from  twgrrole  Where twgrrole_pidm = ? And twgrrole_role= ?"
        def result
        try {
            result = conn.firstRow(sqlf, [this.userPidm, this.wtRole.toString()])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could select TWGRROLE, ${this.bannerId} ${this.wtRole.toString()}. $e.message"
            }
        }
        if (result.cnt == 0) {
            def sql1 = """Insert Into TWGRROLE(TWGRROLE_PIDM,TWGRROLE_ROLE,TWGRROLE_ACTIVITY_DATE,TWGRROLE_USER_ID,TWGRROLE_DATA_ORIGIN)
                        values( ?,?,sysdate,user,'BANNER' )  """
            try {
                conn.executeInsert(sql1, [this.userPidm.toString(), this.wtRole.toString()])
                connectInfo.tableUpdate('TWGRROLE', 0, 1, 0, 0, 0)
            }
            catch (Exception e) {

                if (connectInfo.showErrors) {
                    println "Could not create webtailor record,  ${this.bannerId.text()} ${this.wtRole.text()}. $e.message"
                }
            }
            finally {

                //this.conn.close()
            }
        }
    }

}