/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement
import java.text.*
/**
 *  DML for PLSql
 */
public class PlsqlDML {
    def sqlcode
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public PlsqlDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData

        parseXmlData()
        if (this.sqlcode ) {
            processSql()
        }
    }


    def parseXmlData() {

        def posOfSql = -1
        posOfSql = xmlData.values?.findIndexOf{it == "SQL_CODE"}
        if (posOfSql > -1){
            posOfSql += 1
            this.sqlcode = xmlData.values[posOfSql]
        }



    }

    def processSql(){
        try {

            conn.call(this.sqlcode)
            connectInfo.tableUpdate("PLSQL", 0, 0, 1, 0, 0)


        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing PLsqlCode from PlsqlDML.groovy for ${connectInfo.tableName}: $e.message"
            }
        }
    }


}

