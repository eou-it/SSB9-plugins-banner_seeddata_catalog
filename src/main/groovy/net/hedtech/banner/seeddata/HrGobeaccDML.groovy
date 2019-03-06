/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

class HrGobeaccDML {
    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData


    public HrGobeaccDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData

        deleteHrGobeacc()
        deleteDuplicateGobeacc()
    }
    def deleteHrGobeacc() {

        def apiData = new XmlParser().parseText( xmlData )
        def gobeaccDel = """DELETE FROM GOBEACC  WHERE GOBEACC_PIDM = (SELECT SPRIDEN_PIDM from SPRIDEN Where SPRIDEN_ID = ?  and spriden_change_ind is null )  """
        try {

            int delRows = conn.executeUpdate(gobeaccDel, [apiData.BANNERID.text()])
            connectInfo.tableUpdate("GOBEACC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${gobeaccDel}"
            }
        }
    }

    def deleteDuplicateGobeacc() {

        def apiData = new XmlParser().parseText( xmlData )
        def gobeaccDel = """delete from GOBEACC where SPKLIBS.F_GET_SPRIDEN_ID(GOBEACC_PIDM) || '-' || GOBEACC_USERNAME in ('HOPADM005-BBATES',    'HOPADM003-HRUSER01',
                                        'HOPADM001-KIJOHNSO',    'HOPADM002-MFISHER',    'HOPADM004-PLEICHLI')  """
        try {

            int delRows = conn.executeUpdate(gobeaccDel)
            connectInfo.tableUpdate("GOBEACC", 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "${gobeaccDel}"
            }
        }
    }


}
