/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Action Item group table update
 * replace the curr rule in the xml
 */

public class MarkActionItemGroupPostedDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public MarkActionItemGroupPostedDML( InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        processData()
    }


    public MarkActionItemGroupPostedDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
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
        conn.executeUpdate( "UPDATE GCBAGRP SET GCBAGRP_POSTED_IND = 'Y' WHERE GCBAGRP_SURROGATE_ID  IN ( SELECT GCRAACT_GCBAGRP_ID FROM GCRAACT) AND GCBAGRP_POSTED_IND='N'" )

    }
}
