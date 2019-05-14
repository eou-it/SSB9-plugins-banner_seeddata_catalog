/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection

/**
 * DML for Finance System Control update
 */
class FinanceSystemControlDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    def default_tax_Group_code


    public FinanceSystemControlDML( InputData connectInfo, Sql conn, Connection connectCall,xmlData ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        updateDefaultTaxCode()
    }
    def parseXmlData() {
        def updateData = new XmlParser().parseText( xmlData )
        this.default_tax_Group_code = updateData.FOBSYSC_TGRP_CODE_DEFAULT.text()
    }

    /**
     * update default tax group code
     */
    def updateDefaultTaxCode() {
        try {
            final String apiQuery =
                    "   BEGIN" +
                            " update FOBSYSC set FOBSYSC_TGRP_CODE_DEFAULT='"+this.default_tax_Group_code+"'"+
                            " WHERE TRUNC(FOBSYSC_EFF_DATE) <= TRUNC(CURRENT_DATE)"+
                            " AND FOBSYSC_STATUS_IND='A'"+
                            " AND (TRUNC(FOBSYSC_TERM_DATE) >= TRUNC(CURRENT_DATE) OR FOBSYSC_TERM_DATE IS NULL)"+
                            " AND (FOBSYSC_NCHG_DATE IS NULL OR TRUNC(FOBSYSC_NCHG_DATE) > TRUNC(CURRENT_DATE));"+
                            "   commit;" +
                            "   END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            try {
                insertCall.execute()

                connectInfo.tableUpdate( "FOBSYSC", 0, 0, 1, 0, 0 )

            }
            catch (Exception e) {
                connectInfo.tableUpdate( "FOBSYSC", 0, 0, 0, 1, 0 )
                if (connectInfo.showErrors) {
                    println "Executing script to update FOBSYSC record for default tax group"
                    println "Problem executing update record for FOBSYSC: $e.message"
                }
            }
            finally {
                insertCall.close()
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FOBSYSC", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Problem executing Update for table FOBSYSC from FinanceSystemControlDML.groovy: $e.message"
            }
        }
    }
}

