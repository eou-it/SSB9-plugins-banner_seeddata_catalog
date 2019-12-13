/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for Finance Procurement User
 */
public class BannerUserDML {


    def oracle_id

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public BannerUserDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.connectInfo.dataSource=null;
    }


    public BannerUserDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectInfo.dataSource=null;
        def newConn = new ConnectDB(connectInfo)
        this.connectCall = newConn.getSqlConnection().connection
        this.xmlData = xmlData
        parseXmlData()
        createBannerUser()
    }


    def parseXmlData() {
        def userCreationData = new XmlParser().parseText( xmlData )
        this.oracle_id = userCreationData.BANNERUSER_ORACLE_ID.text()
    }


    def createBannerUser() {
        try {
            final String createUser = "declare" +
                    "   oracle_id varchar2(10) :=? ;" +
                    "   total_user number :=1;" +
                    "   user_not_exist                     EXCEPTION;" +
                    "   PRAGMA EXCEPTION_INIT (user_not_exist, -01918);" +
                    "   user_already_exists                EXCEPTION;" +
                    "   PRAGMA EXCEPTION_INIT (user_already_exists, -01920);" +
                    "   PROCEDURE p_create_oracle_user (p_id VARCHAR2)" +
                    "   IS" +
                    "   BEGIN" +
                    "   BEGIN" +
                    "   EXECUTE IMMEDIATE 'drop  user ' || p_id || ' CASCADE';" +
                    "   EXCEPTION" +
                    "   WHEN others" +
                    "   THEN" +
                    "   NULL;" +
                    "   END;" +
                    "   BEGIN" +
                    "   EXECUTE IMMEDIATE 'create  user ' || p_id || ' identified by u_pick_it';" +
                    "   EXCEPTION" +
                    "   WHEN user_already_exists" +
                    "   THEN" +
                    "   NULL;" +
                    "   END;" +
                    "   EXECUTE IMMEDIATE 'grant CONNECT, resource, ban_default_m to ' || p_id;" +
                    "   EXECUTE IMMEDIATE 'alter user ' || p_id || ' grant connect through BANPROXY';" +
                    "   EXECUTE IMMEDIATE 'alter user ' || p_id || ' default role all except ban_default_m';" +
                    "   END p_create_oracle_user;" +
                    "   BEGIN"  +
                    "   p_create_oracle_user (oracle_id);" +
                    "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( createUser )
            if (connectInfo.debugThis) {
                println "Executing script with ${this.oracle_id} "
            }
            try {
                // create  3 users
                    insertCall.setString(1, this.oracle_id )
                    insertCall.execute()
                    connectInfo.tableUpdate("ORACLEUSER", 0, 1, 0, 0, 0)
                }
            catch (Exception e) {
                connectInfo.tableUpdate( "ORACLEUSER", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script with ${this.oracle_id} "
                    println "Problem executing create oracle user from BannerUserDML.groovy: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "ORACLEUSER", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing script with ${this.oracle_id} "
                println "Problem executing create oracle user from BannerUserDML.groovy : $e.message"
            }
        }
    }
}
