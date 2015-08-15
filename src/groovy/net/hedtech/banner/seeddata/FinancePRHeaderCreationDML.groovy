/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/** *******************************************************************************
 Copyright 2012 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  General Person DDL - methods to insert / update general person data:  spriden,
 *  spbpers, spraddr, sprtele
 */
public class FinancePRHeaderCreationDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
     def xmlData


    public FinancePRHeaderCreationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        createHeader()

    }




    def createHeader() {
        String createHeaderSQL = ""

        createHeaderSQL = "INSERT INTO LOG_TEMP_SHIV VALUES (sysdate, 'TEST SEED'); COMMIT;"
        try {
            def cntDel = conn.executeUpdate(createHeaderSQL)
            connectInfo.tableUpdate("FPBREQH", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("FPBREQH", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing creating FPBREQH : $e.message"
            }
        }
    }
}
