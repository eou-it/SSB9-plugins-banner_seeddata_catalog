/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection
import java.sql.RowId

/**
 * Action Item tables updates
 * replace the curr rule in the xml
 */

public class MarkActionItemPostedDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public MarkActionItemPostedDML( InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        processData()
    }


    public MarkActionItemPostedDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        processData()
    }

    /**
     * Process the action item records.
     * is used as fk in other records.
     *
     */
    def processData() {
        conn.executeUpdate( "UPDATE GCBACTM SET GCBACTM_POSTED_IND = 'Y' WHERE GCBACTM_SURROGATE_ID  IN ( SELECT GCRAACT_GCBACTM_ID FROM GCRAACT) AND GCBACTM.GCBACTM_POSTED_IND='N'" )

    }
}
