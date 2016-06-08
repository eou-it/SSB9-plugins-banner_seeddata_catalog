/*********************************************************************************
  Copyright 2010-2016 Ellucian Company L.P. and its affiliates.
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
        deleteData('SCRPARE','delete SCRPARE where SCRPARE_PROGRAM = ?',  program_code)
        deleteData('SCRCLPP','delete SCRCLPP where SCRCLPP_PROGRAM = ?',  program_code)
        deleteData('SCRPRTS','delete SCRPRTS where SCRPRTS_PROGRAM = ?',  program_code)

        deleteData('SKBRULS','delete SKBRULS  where exists ( select 1 from SKBRULS del, SMRAGAM, SMRPAAP where del.SKBRULS_GROUP = SKBRULS.SKBRULS_GROUP and del.SKBRULS_GROUP = SMRAGAM_GROUP AND SMRAGAM_AREA = SMRPAAP_AREA AND SMRPAAP_PROGRAM = ? ) ' , program_code );
        deleteData('SKBRULS','delete SKBRULS  where exists ( select 1 from SKBRULS del, SMRPAAP where del.SKBRULS_AREA = SKBRULS.SKBRULS_AREA and del.SKBRULS_AREA = SMRPAAP_AREA AND SMRPAAP_PROGRAM = ? ) ' , program_code );
        deleteData('SKBGROS','delete SKBGROS  where exists ( select 1 from SKBGROS del, SMRAGAM, SMRPAAP where del.SKBGROS_GROUP = SKBGROS.SKBGROS_GROUP and del.SKBGROS_GROUP = SMRAGAM_GROUP AND SMRAGAM_AREA = SMRPAAP_AREA AND SMRPAAP_PROGRAM = ? ) ' , program_code );
        deleteData('SKBARES','delete SKBARES  where exists ( select 1 from SKBARES del, SMRPAAP where del.SKBARES_AREA = SKBARES.SKBARES_AREA and del.SKBARES_AREA = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SKBPROS','delete SKBPROS where SKBPROS_PROGRAM = ?',  program_code)

        deleteData('SKBAREO','delete SKBAREO  where exists ( select 1 from SKBAREO del, SMRPAAP where del.SKBAREO_AREA = SKBAREO.SKBAREO_AREA and del.SKBAREO_AREA = SMRPAAP_AREA and SMRPAAP_PROGRAM = ? ) ' , program_code );
        deleteData('SKBPROO','delete SKBPROO where SKBPROO_PROGRAM = ?',  program_code)

        deleteData('SMRGCAA','delete SMRGCAA where exists ( select 1 from  smragrl, smbagrl,smragam, smrpaap  ' +
                'where  smrgcaa_group = smragrl_group and smragrl_area = smbagrl_area and smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );
        deleteData('SMRGRUL','delete SMRGRUL where exists ( select 1 from  smrgcaa, smragrl, smbagrl,smragam, smrpaap   ' +
                'where   smrgrul_key_rule = smrgcaa_rule and smrgcaa_group = smragrl_group and   smragrl_area = smbagrl_area and  ' +
                'smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );
        deleteData('SMBGRUL','delete SMBGRUL where exists ( select 1 from  smrgcaa, smragrl, smbagrl,smragam, smrpaap   ' +
                'where   smbgrul_key_rule = smrgcaa_rule and smrgcaa_group = smragrl_group and   smragrl_area = smbagrl_area and  ' +
                'smragrl_key_rule = smbagrl_key_rule and smbagrl_area = smragam_area and smragam_area = smrpaap_area and  smrpaap_program  = ? ) ', program_code );

         deleteData('SMRACCM','delete SMRACCM  where exists ( select 1 from SMRACCM del,  smrpaap where del.SMRACCM_area = SMRACCM.SMRACCM_area and del.SMRACCM_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRACLV','delete SMRACLV  where exists ( select 1 from SMRACLV del,  smrpaap where del.SMRACLV_area = SMRACLV.SMRACLV_area and del.SMRACLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRACMT','delete SMRACMT  where exists ( select 1 from SMRACMT del,  smrpaap where del.SMRACMT_area = SMRACMT.SMRACMT_area and del.SMRACMT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAEXL','delete SMRAEXL  where exists ( select 1 from SMRAEXL del,  smrpaap where del.SMRAEXL_area = SMRAEXL.SMRAEXL_area and del.SMRAEXL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAGAM','delete SMRAGAM  where exists ( select 1 from SMRAGAM del,  smrpaap where del.SMRAGAM_area = SMRAGAM.SMRAGAM_area and del.SMRAGAM_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAGRL','delete SMRAGRL  where exists ( select 1 from SMRAGRL del,  smrpaap where del.SMRAGRL_area = SMRAGRL.SMRAGRL_area and del.SMRAGRL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAGRT','delete SMRAGRT  where exists ( select 1 from SMRAGRT del,  smrpaap where del.SMRAGRT_area = SMRAGRT.SMRAGRT_area and del.SMRAGRT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRALVL','delete SMRALVL  where exists ( select 1 from SMRALVL del,  smrpaap where del.SMRALVL_area = SMRALVL.SMRALVL_area and del.SMRALVL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAOGD','delete SMRAOGD  where exists ( select 1 from SMRAOGD del,  smrpaap where del.SMRAOGD_area = SMRAOGD.SMRAOGD_area and del.SMRAOGD_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAOLV','delete SMRAOLV  where exists ( select 1 from SMRAOLV del,  smrpaap where del.SMRAOLV_area = SMRAOLV.SMRAOLV_area and del.SMRAOLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAOSA','delete SMRAOSA  where exists ( select 1 from SMRAOSA del,  smrpaap where del.SMRAOSA_area = SMRAOSA.SMRAOSA_area and del.SMRAOSA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAOST','delete SMRAOST  where exists ( select 1 from SMRAOST del,  smrpaap where del.SMRAOST_area = SMRAOST.SMRAOST_area and del.SMRAOST_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIEAT','delete SMRIEAT  where exists ( select 1 from SMRIEAT del,  smrpaap where del.SMRIEAT_area = SMRIEAT.SMRIEAT_area and del.SMRIEAT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIECC','delete SMRIECC  where exists ( select 1 from SMRIECC del,  smrpaap where del.SMRIECC_area = SMRIECC.SMRIECC_area and del.SMRIECC_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIECO','delete SMRIECO  where exists ( select 1 from SMRIECO del,  smrpaap where del.SMRIECO_area = SMRIECO.SMRIECO_area and del.SMRIECO_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIECP','delete SMRIECP  where exists ( select 1 from SMRIECP del,  smrpaap where del.SMRIECP_area = SMRIECP.SMRIECP_area and del.SMRIECP_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIEDG','delete SMRIEDG  where exists ( select 1 from SMRIEDG del,  smrpaap where del.SMRIEDG_area = SMRIEDG.SMRIEDG_area and del.SMRIEDG_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIEDP','delete SMRIEDP  where exists ( select 1 from SMRIEDP del,  smrpaap where del.SMRIEDP_area = SMRIEDP.SMRIEDP_area and del.SMRIEDP_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIEMJ','delete SMRIEMJ  where exists ( select 1 from SMRIEMJ del,  smrpaap where del.SMRIEMJ_area = SMRIEMJ.SMRIEMJ_area and del.SMRIEMJ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRIEMN','delete SMRIEMN  where exists ( select 1 from SMRIEMN del,  smrpaap where del.SMRIEMN_area = SMRIEMN.SMRIEMN_area and del.SMRIEMN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );

        deleteData('SMRAQUA','delete SMRAQUA  where exists ( select 1 from SMRAQUA del,  smrpaap where del.SMRAQUA_area = SMRAQUA.SMRAQUA_area and del.SMRAQUA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARCM','delete SMRARCM  where exists ( select 1 from SMRARCM del,  smrpaap where del.SMRARCM_area = SMRARCM.SMRARCM_area and del.SMRARCM_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAREX','delete SMRAREX  where exists ( select 1 from SMRAREX del,  smrpaap where del.SMRAREX_area = SMRAREX.SMRAREX_area and del.SMRAREX_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARGC','delete SMRARGC  where exists ( select 1 from SMRARGC del,  smrpaap where del.SMRARGC_area = SMRARGC.SMRARGC_area and del.SMRARGC_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARGD','delete SMRARGD  where exists ( select 1 from SMRARGD del,  smrpaap where del.SMRARGD_area = SMRARGD.SMRARGD_area and del.SMRARGD_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARLT','delete SMRARLT  where exists ( select 1 from SMRARLT del,  smrpaap where del.SMRARLT_area = SMRARLT.SMRARLT_area and del.SMRARLT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARLV','delete SMRARLV  where exists ( select 1 from SMRARLV del,  smrpaap where del.SMRARLV_area = SMRARLV.SMRARLV_area and del.SMRARLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARSA','delete SMRARSA  where exists ( select 1 from SMRARSA del,  smrpaap where del.SMRARSA_area = SMRARSA.SMRARSA_area and del.SMRARSA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARSC','delete SMRARSC  where exists ( select 1 from SMRARSC del,  smrpaap where del.SMRARSC_area = SMRARSC.SMRARSC_area and del.SMRARSC_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRARUL','delete SMRARUL  where exists ( select 1 from SMRARUL del,  smrpaap where del.SMRARUL_area = SMRARUL.SMRARUL_area and del.SMRARUL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRATRK','delete SMRATRK  where exists ( select 1 from SMRATRK del,  smrpaap where del.SMRATRK_area = SMRATRK.SMRATRK_area and del.SMRATRK_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRAUSE','delete SMRAUSE  where exists ( select 1 from SMRAUSE del,  smrpaap where del.SMRAUSE_area = SMRAUSE.SMRAUSE_area and del.SMRAUSE_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDOEX','delete SMRDOEX  where exists ( select 1 from SMRDOEX del,  smrpaap where del.SMRDOEX_area = SMRDOEX.SMRDOEX_area and del.SMRDOEX_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDOLV','delete SMRDOLV  where exists ( select 1 from SMRDOLV del,  smrpaap where del.SMRDOLV_area = SMRDOLV.SMRDOLV_area and del.SMRDOLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDORJ','delete SMRDORJ  where exists ( select 1 from SMRDORJ del,  smrpaap where del.SMRDORJ_area = SMRDORJ.SMRDORJ_area and del.SMRDORJ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDORQ','delete SMRDORQ  where exists ( select 1 from SMRDORQ del,  smrpaap where del.SMRDORQ_area = SMRDORQ.SMRDORQ_area and del.SMRDORQ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDOST','delete SMRDOST  where exists ( select 1 from SMRDOST del,  smrpaap where del.SMRDOST_area = SMRDOST.SMRDOST_area and del.SMRDOST_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDOUS','delete SMRDOUS  where exists ( select 1 from SMRDOUS del,  smrpaap where del.SMRDOUS_area = SMRDOUS.SMRDOUS_area and del.SMRDOUS_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDREX','delete SMRDREX  where exists ( select 1 from SMRDREX del,  smrpaap where del.SMRDREX_area = SMRDREX.SMRDREX_area and del.SMRDREX_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDRLV','delete SMRDRLV  where exists ( select 1 from SMRDRLV del,  smrpaap where del.SMRDRLV_area = SMRDRLV.SMRDRLV_area and del.SMRDRLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRDRRQ','delete SMRDRRQ  where exists ( select 1 from SMRDRRQ del,  smrpaap where del.SMRDRRQ_area = SMRDRRQ.SMRDRRQ_area and del.SMRDRRQ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGOAT','delete SMRGOAT  where exists ( select 1 from SMRGOAT del,  smrpaap where del.SMRGOAT_area = SMRGOAT.SMRGOAT_area and del.SMRGOAT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGOGD','delete SMRGOGD  where exists ( select 1 from SMRGOGD del,  smrpaap where del.SMRGOGD_area = SMRGOGD.SMRGOGD_area and del.SMRGOGD_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGOLV','delete SMRGOLV  where exists ( select 1 from SMRGOLV del,  smrpaap where del.SMRGOLV_area = SMRGOLV.SMRGOLV_area and del.SMRGOLV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGOSA','delete SMRGOSA  where exists ( select 1 from SMRGOSA del,  smrpaap where del.SMRGOSA_area = SMRGOSA.SMRGOSA_area and del.SMRGOSA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGRRQ','delete SMRGRRQ  where exists ( select 1 from SMRGRRQ del,  smrpaap where del.SMRGRRQ_area = SMRGRRQ.SMRGRRQ_area and del.SMRGRRQ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRGUSE','delete SMRGUSE  where exists ( select 1 from SMRGUSE del,  smrpaap where del.SMRGUSE_area = SMRGUSE.SMRGUSE_area and del.SMRGUSE_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
           deleteData('SMRPOAN','delete SMRPOAN  where exists ( select 1 from SMRPOAN del,  smrpaap where del.SMRPOAN_area = SMRPOAN.SMRPOAN_area and del.SMRPOAN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSACA','delete SMRSACA  where exists ( select 1 from SMRSACA del,  smrpaap where del.SMRSACA_area = SMRSACA.SMRSACA_area and del.SMRSACA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSACE','delete SMRSACE  where exists ( select 1 from SMRSACE del,  smrpaap where del.SMRSACE_area = SMRSACE.SMRSACE_area and del.SMRSACE_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSACL','delete SMRSACL  where exists ( select 1 from SMRSACL del,  smrpaap where del.SMRSACL_area = SMRSACL.SMRSACL_area and del.SMRSACL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSACM','delete SMRSACM  where exists ( select 1 from SMRSACM del,  smrpaap where del.SMRSACM_area = SMRSACM.SMRSACM_area and del.SMRSACM_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSACT','delete SMRSACT  where exists ( select 1 from SMRSACT del,  smrpaap where del.SMRSACT_area = SMRSACT.SMRSACT_area and del.SMRSACT_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSAGC','delete SMRSAGC  where exists ( select 1 from SMRSAGC del,  smrpaap where del.SMRSAGC_area = SMRSAGC.SMRSAGC_area and del.SMRSAGC_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSAGD','delete SMRSAGD  where exists ( select 1 from SMRSAGD del,  smrpaap where del.SMRSAGD_area = SMRSAGD.SMRSAGD_area and del.SMRSAGD_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSAGM','delete SMRSAGM  where exists ( select 1 from SMRSAGM del,  smrpaap where del.SMRSAGM_area = SMRSAGM.SMRSAGM_area and del.SMRSAGM_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSAGR','delete SMRSAGR  where exists ( select 1 from SMRSAGR del,  smrpaap where del.SMRSAGR_area = SMRSAGR.SMRSAGR_area and del.SMRSAGR_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSALV','delete SMRSALV  where exists ( select 1 from SMRSALV del,  smrpaap where del.SMRSALV_area = SMRSALV.SMRSALV_area and del.SMRSALV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSAPV','delete SMRSAPV  where exists ( select 1 from SMRSAPV del,  smrpaap where del.SMRSAPV_area = SMRSAPV.SMRSAPV_area and del.SMRSAPV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARC','delete SMRSARC  where exists ( select 1 from SMRSARC del,  smrpaap where del.SMRSARC_area = SMRSARC.SMRSARC_area and del.SMRSARC_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARD','delete SMRSARD  where exists ( select 1 from SMRSARD del,  smrpaap where del.SMRSARD_area = SMRSARD.SMRSARD_area and del.SMRSARD_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARE','delete SMRSARE  where exists ( select 1 from SMRSARE del,  smrpaap where del.SMRSARE_area = SMRSARE.SMRSARE_area and del.SMRSARE_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARG','delete SMRSARG  where exists ( select 1 from SMRSARG del,  smrpaap where del.SMRSARG_area = SMRSARG.SMRSARG_area and del.SMRSARG_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARL','delete SMRSARL  where exists ( select 1 from SMRSARL del,  smrpaap where del.SMRSARL_area = SMRSARL.SMRSARL_area and del.SMRSARL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARS','delete SMRSARS  where exists ( select 1 from SMRSARS del,  smrpaap where del.SMRSARS_area = SMRSARS.SMRSARS_area and del.SMRSARS_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSART','delete SMRSART  where exists ( select 1 from SMRSART del,  smrpaap where del.SMRSART_area = SMRSART.SMRSART_area and del.SMRSART_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSARU','delete SMRSARU  where exists ( select 1 from SMRSARU del,  smrpaap where del.SMRSARU_area = SMRSARU.SMRSARU_area and del.SMRSARU_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSGAV','delete SMRSGAV  where exists ( select 1 from SMRSGAV del,  smrpaap where del.SMRSGAV_area = SMRSGAV.SMRSGAV_area and del.SMRSGAV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSPAP','delete SMRSPAP  where exists ( select 1 from SMRSPAP del,  smrpaap where del.SMRSPAP_area = SMRSPAP.SMRSPAP_area and del.SMRSPAP_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSSUB','delete SMRSSUB  where exists ( select 1 from SMRSSUB del,  smrpaap where del.SMRSSUB_area = SMRSSUB.SMRSSUB_area and del.SMRSSUB_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSTRG','delete SMRSTRG  where exists ( select 1 from SMRSTRG del,  smrpaap where del.SMRSTRG_area = SMRSTRG.SMRSTRG_area and del.SMRSTRG_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRSWAV','delete SMRSWAV  where exists ( select 1 from SMRSWAV del,  smrpaap where del.SMRSWAV_area = SMRSWAV.SMRSWAV_area and del.SMRSWAV_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMTASEL','delete SMTASEL  where exists ( select 1 from SMTASEL del,  smrpaap where del.SMTASEL_area = SMTASEL.SMTASEL_area and del.SMTASEL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMTCUSE','delete SMTCUSE  where exists ( select 1 from SMTCUSE del,  smrpaap where del.SMTCUSE_area = SMTCUSE.SMTCUSE_area and del.SMTCUSE_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMTSTRG','delete SMTSTRG  where exists ( select 1 from SMTSTRG del,  smrpaap where del.SMTSTRG_area = SMTSTRG.SMTSTRG_area and del.SMTSTRG_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBAGRL','delete SMBAGRL  where exists ( select 1 from SMBAGRL del,  smrpaap where del.SMBAGRL_area = SMBAGRL.SMBAGRL_area and del.SMBAGRL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBAOGN','delete SMBAOGN  where exists ( select 1 from SMBAOGN del,  smrpaap where del.SMBAOGN_area = SMBAOGN.SMBAOGN_area and del.SMBAOGN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBARUL','delete SMBARUL  where exists ( select 1 from SMBARUL del,  smrpaap where del.SMBARUL_area = SMBARUL.SMBARUL_area and del.SMBARUL_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBATRK','delete SMBATRK  where exists ( select 1 from SMBATRK del,  smrpaap where del.SMBATRK_area = SMBATRK.SMBATRK_area and del.SMBATRK_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBDRRQ','delete SMBDRRQ  where exists ( select 1 from SMBDRRQ del,  smrpaap where del.SMBDRRQ_area = SMBDRRQ.SMBDRRQ_area and del.SMBDRRQ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBGOGN','delete SMBGOGN  where exists ( select 1 from SMBGOGN del,  smrpaap where del.SMBGOGN_area = SMBGOGN.SMBGOGN_area and del.SMBGOGN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBGRRQ','delete SMBGRRQ  where exists ( select 1 from SMBGRRQ del,  smrpaap where del.SMBGRRQ_area = SMBGRRQ.SMBGRRQ_area and del.SMBGRRQ_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBSAGN','delete SMBSAGN  where exists ( select 1 from SMBSAGN del,  smrpaap where del.SMBSAGN_area = SMBSAGN.SMBSAGN_area and del.SMBSAGN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBSAGR','delete SMBSAGR  where exists ( select 1 from SMBSAGR del,  smrpaap where del.SMBSAGR_area = SMBSAGR.SMBSAGR_area and del.SMBSAGR_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBSARU','delete SMBSARU  where exists ( select 1 from SMBSARU del,  smrpaap where del.SMBSARU_area = SMBSARU.SMBSARU_area and del.SMBSARU_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMRACAA','delete SMRACAA  where exists ( select 1 from SMRACAA del,  smrpaap where del.SMRACAA_area = SMRACAA.SMRACAA_area and del.SMRACAA_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SMBAGEN','delete SMBAGEN  where exists ( select 1 from SMBAGEN del,  smrpaap where del.SMBAGEN_area = SMBAGEN.SMBAGEN_area and del.SMBAGEN_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );
        deleteData('SSRRARE','delete SSRRARE  where exists ( select 1 from SSRRARE del, smralib,  smrpaap \n' +
                '        where del.SSRRARE_area = ssrrare.ssrrare_area and del.SSRRARE_area = smrpaap_area  and  smrpaap_program = ? )' , program_code );

        deleteData('SMRALIB','delete SMRALIB  where exists ( select 1 from SMRALIB del,  smrpaap where del.SMRALIB_area = SMRALIB.SMRALIB_area and del.SMRALIB_area = smrpaap_area  and  smrpaap_program = ? ) ' , program_code );


        deleteData('SMRGRUL','delete SMRGRUL where exists ( select 1 from   smbagrl, smragam, smrpaap where  SMRGRUL_group = smragam_group and smragam_area = smrpaap_area and  smrpaap_program = ? ) ', program_code );
        deleteData('SMBGRUL','delete SMBGRUL where exists ( select 1 from   smbagrl, smragam, smrpaap where  SMBGRUL_group = smragam_group and smragam_area = smrpaap_area and  smrpaap_program = ? ) ', program_code );

        deleteData('SMBAOGN','delete SMBAOGN where SMBAOGN_PROGRAM = ?',  program_code)
        deleteData('SFRPFCR','delete SFRPFCR where SFRPFCR_PROGRAM = ?',  program_code)
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
        deleteData('SMRPRRQ','delete SMRPRRQ where exists (select 1 from SMRRQCM ' +
                            ' where  SMRPRRQ_PIDM = SMRRQCM_PIDM and SMRPRRQ_REQUEST_NO = SMRRQCM_REQUEST_NO ' +
                            ' and    SMRRQCM_PROGRAM = ?)',  program_code)
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
