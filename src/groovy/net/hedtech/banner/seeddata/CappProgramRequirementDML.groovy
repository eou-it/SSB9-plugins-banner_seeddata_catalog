/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * SMBPGEN tables
 * delete all data associated with program
 */

public class CappProgramRequirementDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public CappProgramRequirementDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode

        processSmbpgen()

    }

    /**
     * Process the Smbpgen records.  Use the dynamic insert process but first delete all the children records
     *
     */

    def processSmbpgen() {
        def apiData = new XmlParser().parseText(xmlData)
        def program_code = apiData.SMBPGEN_PROGRAM.text()

        if (connectInfo.debugThis) {
            println "CappProgramRequirementDML Process program xmlData ${program_code}"
        }

        deleteData('SMRGCAA','delete SMRGCAA where exists ( select 1 from  smragrl, smbagrl,smragam, smrpaap  ' +
                'where  smrgcaa_group = smragrl_group and smragrl_area = smbagrl_area and smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );
        deleteData('SMRGRUL','delete SMRGRUL where exists ( select 1 from  smrgcaa, smragrl, smbagrl,smragam, smrpaap   ' +
                'where   smrgrul_key_rule = smrgcaa_rule and smrgcaa_group = smragrl_group and   smragrl_area = smbagrl_area and  ' +
                'smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );
        deleteData('SMBGRUL','delete SMBGRUL where exists ( select 1 from  smrgcaa, smragrl, smbagrl,smragam, smrpaap   ' +
                'where   smbgrul_key_rule = smrgcaa_rule and smrgcaa_group = smragrl_group and   smragrl_area = smbagrl_area and  ' +
                'smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );


        deleteData('SMBGRUL','delete SMBGRUL where exists ( select 1 from   smbagrl, smragam, smrpaap where  SMBGRUL_group = smragam_group and smragam_area = smrpaap_area and  smrpaap_program = ? ) ', program_code );
        deleteData('SMRGRUL','delete SMRGRUL where exists ( select 1 from   smbagrl, smragam, smrpaap where  SMRGRUL_group = smragam_group and smragam_area = smrpaap_area and  smrpaap_program = ? ) ', program_code );

        deleteData('SMBAOGN','delete SMBAOGN where SMBAOGN_PROGRAM = ?',  program_code)
        deleteData('SMBDRRQ','delete SMBDRRQ where SMBDRRQ_PROGRAM = ?',  program_code)
        deleteData('SMBGOGN','delete SMBGOGN where SMBGOGN_PROGRAM = ?',  program_code)
        deleteData('SMBPOGN','delete SMBPOGN where SMBPOGN_PROGRAM = ?',  program_code)
        deleteData('SMBPTRK','delete SMBPTRK where SMBPTRK_PROGRAM = ?',  program_code)
        deleteData('SMBPVPR','delete SMBPVPR where SMBPVPR_PROGRAM = ?',  program_code)
        deleteData('SMBSPGN','delete SMBSPGN where SMBSPGN_PROGRAM = ?',  program_code)
        deleteData('SMRAOGD','delete SMRAOGD where SMRAOGD_PROGRAM = ?',  program_code)
        deleteData('SMRAOLV','delete SMRAOLV where SMRAOLV_PROGRAM = ?',  program_code)
        deleteData('SMRAOSA','delete SMRAOSA where SMRAOSA_PROGRAM = ?',  program_code)
        deleteData('SMRAOST','delete SMRAOST where SMRAOST_PROGRAM = ?',  program_code)
        deleteData('SMRAUSE','delete SMRAUSE where SMRAUSE_PROGRAM = ?',  program_code)
        deleteData('SMRDOAN','delete SMRDOAN where SMRDOAN_PROGRAM = ?',  program_code)
        deleteData('SMRDOCN','delete SMRDOCN where SMRDOCN_PROGRAM = ?',  program_code)
        deleteData('SMRDOEX','delete SMRDOEX where SMRDOEX_PROGRAM = ?',  program_code)
        deleteData('SMRDOLV','delete SMRDOLV where SMRDOLV_PROGRAM = ?',  program_code)
        deleteData('SMRDORJ','delete SMRDORJ where SMRDORJ_PROGRAM = ?',  program_code)
        deleteData('SMRDORQ','delete SMRDORQ where SMRDORQ_PROGRAM = ?',  program_code)
        deleteData('SMRDOST','delete SMRDOST where SMRDOST_PROGRAM = ?',  program_code)
        deleteData('SMRDOUS','delete SMRDOUS where SMRDOUS_PROGRAM = ?',  program_code)
        deleteData('SMRDREX','delete SMRDREX where SMRDREX_PROGRAM = ?',  program_code)
        deleteData('SMRDRLV','delete SMRDRLV where SMRDRLV_PROGRAM = ?',  program_code)
        deleteData('SMRDRRQ','delete SMRDRRQ where SMRDRRQ_PROGRAM = ?',  program_code)
        deleteData('SMRGOAT','delete SMRGOAT where SMRGOAT_PROGRAM = ?',  program_code)
        deleteData('SMRGOGD','delete SMRGOGD where SMRGOGD_PROGRAM = ?',  program_code)
        deleteData('SMRGOLV','delete SMRGOLV where SMRGOLV_PROGRAM = ?',  program_code)
        deleteData('SMRGOSA','delete SMRGOSA where SMRGOSA_PROGRAM = ?',  program_code)
        deleteData('SMRPAAP','delete SMRPAAP where SMRPAAP_PROGRAM = ?',  program_code)
        deleteData('SMRPATR','delete SMRPATR where SMRPATR_PROGRAM = ?',  program_code)
        deleteData('SMRPCMT','delete SMRPCMT where SMRPCMT_PROGRAM = ?',  program_code)
        deleteData('SMRPLVL','delete SMRPLVL where SMRPLVL_PROGRAM = ?',  program_code)
        deleteData('SMRPNCR','delete SMRPNCR where SMRPNCR_PROGRAM = ?',  program_code)
        deleteData('SMRPOAN','delete SMRPOAN where SMRPOAN_PROGRAM = ?',  program_code)
        deleteData('SMRPOAT','delete SMRPOAT where SMRPOAT_PROGRAM = ?',  program_code)
        deleteData('SMRPOGD','delete SMRPOGD where SMRPOGD_PROGRAM = ?',  program_code)
        deleteData('SMRPOLV','delete SMRPOLV where SMRPOLV_PROGRAM = ?',  program_code)
        deleteData('SMRPONC','delete SMRPONC where SMRPONC_PROGRAM = ?',  program_code)
        deleteData('SMRPOSA','delete SMRPOSA where SMRPOSA_PROGRAM = ?',  program_code)
        deleteData('SMRPRGC','delete SMRPRGC where SMRPRGC_PROGRAM = ?',  program_code)
        deleteData('SMRPRGD','delete SMRPRGD where SMRPRGD_PROGRAM = ?',  program_code)
        deleteData('SMRPRSA','delete SMRPRSA where SMRPRSA_PROGRAM = ?',  program_code)
        deleteData('SMRPRSC','delete SMRPRSC where SMRPRSC_PROGRAM = ?',  program_code)
        deleteData('SMRPTRK','delete SMRPTRK where SMRPTRK_PROGRAM = ?',  program_code)
        deleteData('SMRRQCM','delete SMRRQCM where SMRRQCM_PROGRAM = ?',  program_code)
        deleteData('SMRSAPV','delete SMRSAPV where SMRSAPV_PROGRAM = ?',  program_code)
        deleteData('SMRSPAP','delete SMRSPAP where SMRSPAP_PROGRAM = ?',  program_code)
        deleteData('SMRSPAT','delete SMRSPAT where SMRSPAT_PROGRAM = ?',  program_code)
        deleteData('SMRSPCM','delete SMRSPCM where SMRSPCM_PROGRAM = ?',  program_code)
        deleteData('SMRSPGC','delete SMRSPGC where SMRSPGC_PROGRAM = ?',  program_code)
        deleteData('SMRSPLV','delete SMRSPLV where SMRSPLV_PROGRAM = ?',  program_code)
        deleteData('SMRSPNC','delete SMRSPNC where SMRSPNC_PROGRAM = ?',  program_code)
        deleteData('SMRSPRC','delete SMRSPRC where SMRSPRC_PROGRAM = ?',  program_code)
        deleteData('SMRSPRG','delete SMRSPRG where SMRSPRG_PROGRAM = ?',  program_code)
        deleteData('SMRSPRS','delete SMRSPRS where SMRSPRS_PROGRAM = ?',  program_code)
        deleteData('SMRSSPV','delete SMRSSPV where SMRSSPV_PROGRAM = ?',  program_code)
        deleteData('SMRSTPV','delete SMRSTPV where SMRSTPV_PROGRAM = ?',  program_code)
        deleteData('SMRSWPV','delete SMRSWPV where SMRSWPV_PROGRAM = ?',  program_code)
        deleteData('SMTCUSE','delete SMTCUSE where SMTCUSE_PROGRAM = ?',  program_code)
        deleteData('SMBPGEN','delete SMBPGEN where SMBPGEN_PROGRAM = ?',  program_code)

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode)


    }

    def deleteData(String tableName, String sql, program_code) {
        try {

            int delRows = conn.executeUpdate(sql, [program_code])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for program ${program_code} from CappProgramRequirementDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }
}
