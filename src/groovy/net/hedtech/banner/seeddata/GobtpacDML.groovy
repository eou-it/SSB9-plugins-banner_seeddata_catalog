/*********************************************************************************
  Copyright 2010-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * GOBTPAC tables
 * add pin and update the pin value to 111111
 */

public class GobtpacDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public GobtpacDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGobtpac()

    }

    /**
     * Process the gobtpac records.  Use the dynamic insert process and then use groovy sql to update the pin
     *
     */

    def processGobtpac() {
        def apiData = new XmlParser().parseText(xmlData)

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode)

        def bannerId = apiData.BANNERID.text()
        println "banner ID ${bannerId}   from xml ${apiData.BANNERID.text()}"
        def message="attempted to call GobtpacDML.processGobtpac with values ${bannerId}"
        try {

            conn.call("""
             Declare
               pidm number ;
               expirdate  date;
               Gobtpac_Cur     Gb_Third_Party_Access.Third_Party_Access_Ref;
               api_gobtpac_rec gb_third_party_access.third_party_access_rec;
             Begin
              expirdate := To_Date('01-JAN-2021','DD-MON-YYYY');
              Pidm :=  Gb_Common.F_Get_Pidm(${bannerId});
              Gobtpac_Cur := Gb_Third_Party_Access.F_Query_All (P_Pidm => Pidm);
              FETCH gobtpac_cur INTO api_gobtpac_rec;
              Gb_Third_Party_Access.P_Update (P_Pidm => Pidm,
                                                   P_Pin => '111111',
                                                   P_Pin_Validate_Ind => 'Y',
                                                   p_usage_accept_ind => 'Y',
                                                   p_rowid => api_gobtpac_rec.r_internal_record_id,
                                                   P_Pin_Exp_Date => Expirdate
                                                   );
              delete twgrrole where twgrrole_pidm = pidm;
              Insert Into Twgrrole ( Twgrrole_Pidm, Twgrrole_Role, Twgrrole_Activity_Date)
              Values ( Pidm, 'STUDENT', Sysdate);
              Insert Into Twgrrole ( Twgrrole_Pidm, Twgrrole_Role, Twgrrole_Activity_Date)
              values ( pidm, 'FACULTY', sysdate);
              close gobtpac_cur;
             End ;
            """
            )
            connectInfo.tableUpdate("GOBTPAC", 0, 0, 1, 0, 0)


        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing pin update ${apiData.GOBTPAC_PIDM.text()} ${apiData.BANNER_ID.text()} : $e.message"
                println "${message}"
            }
        }
    }

}
