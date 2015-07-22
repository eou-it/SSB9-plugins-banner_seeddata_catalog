/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection
import java.sql.CallableStatement
import java.text.*

/**
 *  DML for TABLE_NAME
 */
public class FinanceProcurementUserDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    java.sql.RowId tableRow = null


    public FinanceProcurementUserDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processTable_Name()
    }


    def parseXmlData() {
        def apiData = new XmlParser().parseText( xmlData )

        if (connectInfo.debugThis) {
            println "--------- New XML TABLE_NAME record ----------"
            println ""
        }
    }


    def processTable_Name() {
        tableRow = null
        //TODO modify this select to use key data
        String rowSQL = """select rowid table_row from TABLE_NAME
           where ..<add conditions>.. """
        try {
            conn.eachRow( rowSQL ) {row ->
                tableRow = row.table_row
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${rowSQL}"
                println "Problem selecting TABLE_NAME rowid FinanceProcurementUserDML.groovy: $e.message"
            }
        }
        if (!tableRow) {
            //  parm count is 0
            try {
                String API = "{call  .p_create()}"
                CallableStatement insertCall = this.connectCall.prepareCall( API )
                if (connectInfo.debugThis) {
                    println "Insert TABLE_NAME ${} ${} ${}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate( "TABLE_NAME", 0, 1, 0, 0, 0 )
                }
                catch (Exception e) {
                    connectInfo.tableUpdate( "TABLE_NAME", 0, 0, 0, 1, 0 )
                    if (connectInfo.showErrors) {
                        println "Insert TABLE_NAME ${} ${} ${}"
                        println "Problem executing insert for table TABLE_NAME from FinanceProcurementUserDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate( "TABLE_NAME", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Insert TABLE_NAME ${} ${} ${}"
                    println "Problem setting up insert for table TABLE_NAME from FinanceProcurementUserDML.groovy: $e.message"
                }
            }
        } else {  // update the data
            //  parm count is 0
            try {
                String API = "{call  .p_update()}"
                CallableStatement insertCall = this.connectCall.prepareCall( API )
                if (connectInfo.debugThis) {
                    println "Update TABLE_NAME ${} ${} ${}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate( "TABLE_NAME", 0, 0, 1, 0, 0 )
                }
                catch (Exception e) {
                    connectInfo.tableUpdate( "TABLE_NAME", 0, 0, 0, 1, 0 )
                    if (connectInfo.showErrors) {
                        println "Update TABLE_NAME ${} ${} ${}"
                        println "Problem executing update for table TABLE_NAME from FinanceProcurementUserDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate( "TABLE_NAME", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Update TABLE_NAME ${} ${} ${}"
                    println "Problem setting up update for table TABLE_NAME from FinanceProcurementUserDML.groovy: $e.message"
                }
            }
        }
    }
}
