/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Ptrecpd DML tables
 * update the sequence generated IDs for PTRECPD_ID
 */

public class PtrecpdDML {
    int currRule = 0
    def componentId = null
    def subComponentId = null

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public PtrecpdDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch,
                      def deleteNode){

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processPtrecpd()
        deletePhrecrtRecords()
    }

    /**
     * Process the ptrecpd records.  The PTRECPD_ID is generated from a sequence and has to be fetched for any
     * new database so it does not conflict with existing data.
     */

    def processPtrecpd(){
        def apiData = new XmlParser().parseText(xmlData)
        def isValid = true


        componentId = fetchPtrecpdId(apiData.PTRECPD_COAS_CODE[0]?.value()[0] , apiData.PTRECPD_ECPD_CODE[0]?.value()[0])

        if (componentId) {
            apiData.PTRECPD_ID[0].setValue(componentId?.toString())
            isValid = true
        }


        if (isValid) {
            // parse the xml  back into  gstring for the dynamic sql loader
            def xmlRecNew = "<${apiData.name()}>\n"
            apiData.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
                xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
            }
            xmlRecNew += "</${apiData.name()}>\n"

            // parse the data using dynamic sql for inserts and updates
            def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)
        }
    }


    private def fetchPtrecpdId(String coasCode, String ecpdCode){
        def id = null

        //
        try {
            id = this.conn.firstRow("""SELECT PTRECPD_ID AS ID FROM PTRECPD WHERE PTRECPD_COAS_CODE = ?  AND PTRECPD_ECPD_CODE = ?""",
                    [coasCode,ecpdCode])?.ID
        }
        catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPD ID in PtrecpdDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPD ID for ${connectInfo.tableName}.")
        return id
    }


    private def deletePhrecrtRecords()
    {
        def count = null
        try{
          count =  this.conn.executeUpdate("""DELETE
                            FROM phrecdt
                            WHERE phrecdt_phrecrt_id in
                              (SELECT phrecrt_id
                              FROM phrecrt e,
                                ptrecpd p
                              WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                              AND ptrecpd_coas_code                         = 'B'
                              AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                              AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                            )""")



            count =  this.conn.executeUpdate("""DELETE
                                    FROM phrecrs
                                    WHERE phrecrs_phrecrt_id in
                                      (SELECT phrecrt_id
                                      FROM phrecrt e,
                                        ptrecpd p
                                      WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                      AND ptrecpd_coas_code                         = 'B'
                                      AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                      AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                      )""")



            count =  this.conn.executeUpdate("""DELETE
                                            FROM PHRECRQ
                                            WHERE PHRECRQ_PHRECRT_ID in
                                              (SELECT phrecrt_id
                                              FROM phrecrt e,
                                                ptrecpd p
                                              WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                              AND ptrecpd_coas_code                         = 'B'
                                              AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                              AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                              )""")


            count =  this.conn.executeUpdate("""DELETE
                                        FROM PHRECSC
                                        WHERE PHRECSC_PHRECSN_ID IN
                                          (SELECT PHRECSN_ID
                                          FROM PHRECSN
                                          WHERE PHRECSN_PHRECRT_ID in
                                            (SELECT phrecrt_id
                                            FROM phrecrt e,
                                              ptrecpd p
                                            WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                            AND ptrecpd_coas_code                         = 'B'
                                            AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                            AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                            )
                                          )""")


            count =  this.conn.executeUpdate("""DELETE
                                        FROM PHRECSI
                                        WHERE PHRECSI_PHRECSN_ID IN
                                          (SELECT PHRECSN_ID
                                          FROM PHRECSN
                                          WHERE PHRECSN_PHRECRT_ID IN
                                            (SELECT phrecrt_id
                                            FROM phrecrt e,
                                              ptrecpd p
                                            WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                            AND ptrecpd_coas_code                         = 'B'
                                            AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                            AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                            )
                                          )""")


            count =  this.conn.executeUpdate("""DELETE
                                            FROM phreccm
                                            WHERE PHRECCM_PHRECRT_ID IN
                                              (SELECT phrecrt_id
                                              FROM phrecrt e,
                                                ptrecpd p
                                              WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                              AND ptrecpd_coas_code                         = 'B'
                                              AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                              AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                              )""")



            count =  this.conn.executeUpdate("""DELETE
                    FROM PHRECST
                    WHERE PHRECST_PHRECRT_ID IN
                      (SELECT phrecrt_id
                      FROM phrecrt e,
                        ptrecpd p
                      WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                      AND ptrecpd_coas_code                         = 'B'
                      AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                      AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                      )""")


            count =  this.conn.executeUpdate("""DELETE
                        FROM PHRECSN
                        WHERE PHRECSN_PHRECRT_ID IN
                          (SELECT phrecrt_id
                          FROM phrecrt e,
                            ptrecpd p
                          WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                          AND ptrecpd_coas_code                         = 'B'
                          AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                          AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                          )""")

            count =  this.conn.executeUpdate("""DELETE
                                    FROM phrecrt
                                    WHERE phrecrt_id IN
                                      (SELECT phrecrt_id
                                      FROM phrecrt e,
                                        ptrecpd p
                                      WHERE e.phrecrt_ptrecpd_id                    = ptrecpd_id
                                      AND ptrecpd_coas_code                         = 'B'
                                      AND ptrecpd_ecpd_code                        IN ('FA0115','FA0215','RS0115','RS0215','GR0115','GR0215','GS1115')
                                      AND SPKLIBS.F_GET_SPRIDEN_ID(e.PHRECRT_PIDM) IN ('HOPSD0001','HOPSD0002','HOPSD0003','HOPSD0004','HOPSD0005','HOPSD0006')
                                      )""")


        }catch (Exception e) {
            if (connectInfo.showErrors) println("Error while checking for existing PTRECPD ID in PtrecpdDML for ${connectInfo.tableName}. $e.message")
        }
        if (connectInfo.debugThis) println("Checking for existing PTRECPD ID for ${connectInfo.tableName}.")


    }
}