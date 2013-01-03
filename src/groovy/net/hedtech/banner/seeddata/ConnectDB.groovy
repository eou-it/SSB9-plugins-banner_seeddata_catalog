/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * Provides access to a Groovy Sql instance as well as a database connection.
 * */
public class ConnectDB {

    InputData connectInfo
    Connection sessionConnection
    Sql db


    public ConnectDB(InputData connectionInfo) {
        this.connectInfo = connectionInfo
    }

    /**
     * Returns a new database connection with 'auto commit' turned off. The caller
     * is responsible for closing the Sql.
     * */

    def Sql getSqlConnection() {
        try {
            if (connectInfo.mepUserId) {
                connectInfo.url = connectInfo.getUrl()
                db = Sql.newInstance(connectInfo.url,   //  db =  new Sql( connectInfo.url,
                                     "bansecr",
                                     "u_pick_it",
                                     'oracle.jdbc.driver.OracleDriver')

                sessionConnection = db.getConnection()

                sessionConnection.autoCommit = false
                db.connection.autoCommit = false
                db
            }
            else if (connectInfo.dataSource) {
                sessionConnection = connectInfo.dataSource.getConnection()
                db = new Sql(sessionConnection)

                String rolePswd = ""
                try {
                    db.call("{$Sql.VARCHAR = call g\$_security.g\$_get_role_password_fnc('BAN_DEFAULT_M' ,'SEED-DATA')}") {role ->
                        rolePswd = role
                    }
                    String roleM = """SET ROLE "BAN_DEFAULT_M" IDENTIFIED BY "${rolePswd}" """
                    db.execute(roleM)
                }
                catch (e) {
                    println "ERROR: Could not establish role set up to the database. ${e.message}"
                }
                db
            } else {
                db = Sql.newInstance(connectInfo.url,   //  db =  new Sql( connectInfo.url,
                                     connectInfo.username,
                                     connectInfo.password,
                                     'oracle.jdbc.driver.OracleDriver')

                sessionConnection = db.getConnection()

                sessionConnection.autoCommit = false
                db.connection.autoCommit = false
                db
            }
        }
        catch (e) {
            println "ERROR: Could not establish connection to the database. ${e.message}"
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Returns the session connection for prepared callable statements.
     * */

    def Connection getSessionConnection() {
        sessionConnection
    }


}
