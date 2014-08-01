/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * General Person ID DML for Students
 * Save the PIDM in the InputData.saveStudentPidm and delete existing student data
 */

public class StudentPersonIDDML {
    def spriden_pidm
    def spriden_id
    def spriden_last_name
    def spriden_first_name
    def spriden_mi
    def spriden_change_ind
    def spriden_entity_ind
    def spriden_user
    def spriden_origin
    def spriden_search_last_name
    def spriden_search_first_name
    def spriden_search_mi
    def spriden_soundex_last_name
    def spriden_soundex_first_name
    def spriden_ntyp_code
    def spriden_create_user
    def spriden_create_date
    def spriden_create_fdmn_code
    def spriden_surname_prefix


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def pidm


    public StudentPersonIDDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processStudent()

    }


    def processStudent() {
        def apiData = new XmlParser().parseText(xmlData)
        String ssql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        def cntSpriden = 0

        try {
            this.conn.eachRow(ssql, [apiData.BANNERID.text()]) {trow ->
                pidm = trow.spriden_pidm
                cntSpriden++
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in StudentPersonIDDML,  ${apiData.SPRIDEN_ID.text()}  from SPRIDEN. $e.message"
            }
        }
        if (cntSpriden == 0) {
            def newSpriden = new PersonIDDML(connectInfo, conn, connectCall, xmlData)
            connectInfo.saveStudentPidm = newSpriden.PIDM

        }
        else {
            connectInfo.saveStudentPidm = pidm
        }

        // delete data so we can re-add because most do not have an update process
        if (cntSpriden && connectInfo.replaceData) {
            deleteData()
        }

    }


    def deleteData() {

        String selectSql = """select srbrecr_term_code, srbrecr_admin_seqno   from srbrecr where srbrecr_pidm = ?
               order by 1,2"""
        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {

                conn.call("{call sb_recruit.p_delete(p_pidm => ?,p_term_code => ?, p_admin_seqno =>? ) }",
                          [connectInfo.saveStudentPidm.toInteger(),
                          it.srbrecr_term_code,
                          it.srbrecr_admin_seqno.toInteger()])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SRBRECR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete for table SRBRECR from StudentPersonIDDML.groovy: $e.message"
            }
        }

        selectSql = """select saradap_term_code_entry, saradap_appl_no   from saradap where saradap_pidm = ?
               order by 1,2"""
        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {
                conn.call("""{call sb_admissionsapplication.p_delete(p_pidm => ?,
                     p_term_code_entry => ?,p_appl_no => ?) }""",
                          [connectInfo.saveStudentPidm.toInteger(),
                          it.saradap_term_code_entry,
                          it.saradap_appl_no.toInteger()])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SARADAP", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete for table SARADAP from StudentPersonIDDML.groovy: $e.message"
            }
        }

        selectSql = """select sgbstdn_term_code_eff   from sgbstdn where sgbstdn_pidm = ?
               order by 1 desc"""
        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {
                conn.call("""{call sb_learner.p_delete(p_pidm => ?,
                            p_term_code_eff => ?) }""",
                          [connectInfo.saveStudentPidm.toInteger(), it.sgbstdn_term_code_eff])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SGBSTDN", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete for table SGBSTDN from StudentPersonIDDML.groovy: $e.message"
            }
        }

        selectSql = """select   shrdgmr_seq_no   from shrdgmr where shrdgmr_pidm = ?
                     order by 1 """
        try {
            conn.eachRow(selectSql, [connectInfo.saveStudentPidm]) {
                conn.call("{call sb_learneroutcome.p_delete(p_pidm => ?, p_seq_no => ? ) }",
                          [connectInfo.saveStudentPidm.toInteger(), it.shrdgmr_seq_no.toInteger()])
            }
        }
        catch (e) {
            connectInfo.tableUpdate("SHRDGMR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing select or delete for table SHRDGMR from StudentPersonIDDML.groovy: $e.message"
            }
        }

        String selectTerms = """select sfrstcr_term_code, sfrstcr_crn from sfrstcr where sfrstcr_pidm = ? group by sfrstcr_term_code, sfrstcr_crn"""
        def terms = conn.rows(selectTerms, [connectInfo.saveStudentPidm])
        deleteData("SORHSCH", "delete SORHSCH where  	SORHSCH_pidm = ?  ")
        deleteData("SORDEGR", "delete SORDEGR where  	SORDEGR_pidm = ?  ")
        deleteData("SORPCOL", "delete SORPCOL where  	SORPCOL_pidm = ?  ")
        deleteData("SORTEST", "delete SORTEST where  	SORTEST_pidm = ?  ")
        deleteData("TWGRROLE", "delete TWGRROLE where TWGRROLE_pidm = ?")
        deleteData("TBRACCD", "delete TBRACCD where  	TBRACCD_pidm = ?  ")
        deleteData("SGRSATT", "delete SGRSATT where  	SGRSATT_pidm = ?  ")
        deleteData("SGRCHRT", "delete SGRCHRT where  	SGRCHRT_pidm = ?  ")
        deleteData("SGRADVR", "delete SGRADVR where  	SGRADVR_pidm = ?  ")
        deleteData("SPRHOLD", "delete SPRHOLD where  	SPRHOLD_pidm = ?  ")
        deleteData("SPRAPIN", "delete SPRAPIN where  	SPRAPIN_pidm = ?  ")
        deleteData("SFRSTRD", "delete SFRSTRD where  	SFRSTRD_pidm = ?  ")
        deleteData("SFBSTRH", "delete SFBSTRH where  	SFBSTRH_pidm = ?  ")
        deleteData("SFRBTCH", "delete SFRBTCH where  	SFRBTCH_pidm = ?  ")
        deleteData("TBRCBRQ", "delete TBRCBRQ where  	TBRCBRQ_pidm = ?  ")
        deleteData("SFTAREA", "delete SFTAREA where  	SFTAREA_pidm = ?  ")
        deleteData("SFTDWER", "delete SFTDWER where  	SFTDWER_pidm = ?  ")
        deleteData("SFRFAUD", "delete SFRFAUD where  	SFRFAUD_pidm = ?  ")
        deleteData("SFRREGD", "delete SFRREGD where  	SFRREGD_pidm = ?  ")
        deleteData("SFTREGS", "delete SFTREGS where  	SFTREGS_pidm = ?  ")
        deleteData("SFRSTCR", "delete SFRSTCR where  	SFRSTCR_pidm = ?  ")
        deleteData("SFRAREG", "delete SFRAREG where  	SFRAREG_pidm = ?  ")
        deleteData("SFRSTCA", "delete SFRSTCA where  	SFRSTCA_pidm = ?  ")
        if (doesTableExist("SFRREGV")) deleteData("SFRREGV", "delete SFRREGV where  	SFRREGV_pidm = ?  ")
        if (doesTableExist("SFRRSCM")) deleteData("SFRRSCM", "delete SFRRSCM where  	SFRRSCM_pidm = ?  ")
        deleteData("SFRENSP", "delete SFRENSP where  	SFRENSP_pidm = ?  ")
        deleteData("SFBETRM", "delete SFBETRM where  	SFBETRM_pidm = ?  ")
        deleteData("SFTRGAM", "delete SFTRGAM where  	SFTRGAM_pidm = ?  ")
        if (doesTableExist("SFRBLPA")) deleteData("SFRBLPA", "delete SFRBLPA where  	SFRBLPA_pidm = ?  ")
        if (doesTableExist("SFRBSEL")) deleteData("SFRBSEL", "delete SFRBSEL where  	SFRBSEL_pidm = ?  ")
        deleteData("SFRENRL", "delete SFRENRL where  	SFRENRL_pidm = ?  ")
        deleteData("SFRSRPO", "delete SFRSRPO where  	SFRSRPO_pidm = ?  ")
        deleteData("SFRTHST", "delete SFRTHST where  	SFRTHST_pidm = ?  ")
        deleteData("SFRTIME", "delete SFRTIME where  	SFRTIME_pidm = ?  ")
        deleteData("SFRWDRL", "delete SFRWDRL where  	SFRWDRL_pidm = ?  ")
        deleteData("SFRWLNT", "delete SFRWLNT where  	SFRWLNT_pidm = ?  ")
        deleteData("SFRATWT", "delete SFRATWT where  	SFRATWT_pidm = ?  ")
        deleteData("SFRCOLR", "delete SFRCOLR where  	SFRCOLR_pidm = ?  ")
        deleteData("SFRTHST", "delete SFRTHST where  	SFRTHST_pidm = ?  ")
        deleteData("SFBRGRP", "delete SFBRGRP where  	SFBRGRP_pidm = ?  ")
        deleteData("SFRCBRQ", "delete SFRCBRQ where  	SFRCBRQ_pidm = ?  ")
        deleteData("SFREFEE", "delete SFREFEE where  	SFREFEE_pidm = ?  ")
        deleteData("SFRMESG", "delete SFRMESG where  	SFRMESG_pidm = ?  ")
        deleteData("SFRREGD", "delete SFRREGD where  	SFRREGD_pidm = ?  ")
        deleteData("SFRRSCM", "delete SFRRSCM where  	SFRRSCM_pidm = ?  ")
        deleteData("SFRRACL", "delete SFRRACL where  	SFRRACL_pidm = ?  ")
        deleteData("SFRTIME", "delete SFRTIME where  	SFRTIME_pidm = ?  ")
        deleteData("SFRSRPO", "delete SFRSRPO where  	SFRSRPO_pidm = ?  ")

        deleteData("SHBDIPL", "delete SHBDIPL where  	SHBDIPL_pidm = ?  ")
        deleteData("SHBCATT", "delete SHBCATT where   shbcatt_pidm = ? ")
        deleteData("SHBCOMI", "delete SHBCOMI where  	SHBCOMI_pidm = ?  ")
        deleteData("SHBGAPP", "delete SHBGAPP where  	SHBGAPP_pidm = ?  ")
        deleteData("SHBHEAD", "delete SHBHEAD where  	SHBHEAD_pidm = ?  ")
        deleteData("SHRATTR", "delete SHRATTR where   	SHRATTR_pidm = ?  ")
        deleteData("SHRCATT", "delete SHRCATT where  	SHRCATT_pidm = ?  ")
        deleteData("SHRCGPA", "delete SHRCGPA where  	SHRCGPA_pidm = ?  ")
        deleteData("SHRCHRT", "delete SHRCHRT where  	SHRCHRT_pidm = ?  ")
        deleteData("SHRCOMC", "delete SHRCOMC where   	SHRCOMC_pidm = ?  ")
        deleteData("SHRCOMM", "delete SHRCOMM where   	SHRCOMM_pidm = ?  ")
        deleteData("SHRDCMT", "delete SHRDCMT where  	SHRDCMT_pidm = ?  ")
        deleteData("SHRDGCM", "delete SHRDGCM where  SHRDGCM_pidm = ?  ")
        deleteData("SHRDGDH", "delete SHRDGDH where  	SHRDGDH_pidm = ?  ")
        deleteData("SHRDGIH", "delete SHRDGIH where  	SHRDGIH_pidm = ?  ")
        deleteData("SHREGPA", "delete SHREGPA where 	SHREGPA_pidm = ?  ")
        deleteData("SHREPTD", "delete SHREPTD where  	SHREPTD_pidm = ?  ")
        deleteData("SHREVNT", "delete SHREVNT where  	SHREVNT_pidm = ?  ")
        deleteData("SHRGCOL", "delete SHRGCOL where   	SHRGCOL_pidm = ?  ")
        deleteData("SHRGPAC", "delete SHRGPAC where  	SHRGPAC_pidm = ?  ")
        deleteData("SHRGPAL", "delete SHRGPAL where   	SHRGPAL_pidm = ?  ")
        deleteData("SHRINST", "delete SHRINST where 	SHRINST_pidm = ?  ")
        deleteData("SHRLGPA", "delete SHRLGPA where 	SHRLGPA_pidm = ?  ")
        deleteData("SHRNCRD", "delete SHRNCRD where  	SHRNCRD_pidm = ?  ")
        deleteData("SHRNCRS", "delete SHRNCRS where  	SHRNCRS_pidm = ?  ")
        deleteData("SHRQPND", "delete SHRQPND where  	SHRQPND_pidm = ?  ")
        deleteData("SHRQPNM", "delete SHRQPNM where  	SHRQPNM_pidm = ?  ")
        deleteData("SHRRPEQ", "delete SHRRPEQ where  	SHRRPEQ_pidm = ?  ")
        deleteData("SHRSGPA", "delete SHRSGPA where  	SHRSGPA_pidm = ?  ")
        deleteData("SHRTATT", "delete SHRTATT where  	SHRTATT_pidm = ?  ")
        deleteData("SHRTCKD", "delete SHRTCKD where  SHRTCKD_pidm = ?  ")
        deleteData("SHRTCKG", "delete SHRTCKG where  	SHRTCKG_pidm = ?  ")
        deleteData("SHRTCKL", "delete SHRTCKL where  	SHRTCKL_pidm = ?  ")
        deleteData("SHRTGPA", "delete SHRTGPA where  	SHRTGPA_pidm = ?  ")
        deleteData("SHRTCKN", "delete SHRTCKN where  	SHRTCKN_pidm = ?  ")

        deleteData("SHRTMCM", "delete SHRTMCM where  SHRTMCM_pidm = ?  ")
        deleteData("SHRTRCE", "delete SHRTRCE where  	SHRTRCE_pidm = ?  ")
        deleteData("SHRTRCR", "delete SHRTRCR where  	SHRTRCR_pidm = ?  ")
        deleteData("SHRTRCD", "delete SHRTRCD where  SHRTRCD_pidm = ?  ")
        deleteData("SHRTRAM", "delete SHRTRAM where  SHRTRAM_pidm = ?  ")
        deleteData("SHRTRIT", "delete SHRTRIT where  	SHRTRIT_pidm = ?  ")
        deleteData("SHRTREP", "delete SHRTREP where  	SHRTREP_pidm = ?  ")
        deleteData("SHRTRTA", "delete SHRTRTA where   	SHRTRTA_pidm = ?  ")
        deleteData("SHRTRTK", "delete SHRTRTK where  	SHRTRTK_pidm = ?  ")
        deleteData("SHRTTCM", "delete SHRTTCM where   	SHRTTCM_pidm = ?  ")
        deleteData("SHTIACT", "delete SHTIACT where   	SHTIACT_pidm = ?  ")
        deleteData("SHTTRAN", "delete SHTTRAN where   	SHTTRAN_pidm = ?  ")
        deleteData("SHRTRMN", "delete SHTTRNM where SHTTRNM_pidm = ? ")
        deleteData("SHRTTRM", "delete SHRTTRM where  	SHRTTRM_pidm = ?  ")
        deleteData("GOBEACC", "delete GOBEACC where  	GOBEACC_pidm = ?  ")


        // reset enrollment counts
        def enrlCnt = """UPDATE ssbsect
        SET (ssbsect_enrl, ssbsect_tot_credit_hrs) =
         (SELECT NVL(COUNT(sfrstcr_crn),0),
               NVL(SUM(sfrstcr_credit_hr),0)
           FROM stvrsts, sfrstcr
           WHERE sfrstcr_crn = ssbsect_crn
           AND sfrstcr_term_code = ssbsect_term_code
           AND NVL(sfrstcr_error_flag,'N') <> 'F'
           AND sfrstcr_rsts_code = stvrsts_code
           AND stvrsts_incl_sect_enrl = 'Y')
           where ssbsect_term_code = ?
           and ssbsect_crn = ?
     """
        def enrlCnt2 = """ UPDATE ssbsect
        SET ssbsect_seats_avail =  (ssbsect_max_enrl - ssbsect_enrl)
        WHERE ssbsect_seats_avail <> (ssbsect_max_enrl - ssbsect_enrl)
        AND ssbsect_term_code = ? and ssbsect_crn = ?"""

        if (connectInfo.saveThis) {
            conn.execute "{ call gb_common.p_commit() }"
        }
        try {
            terms.each {
                updateTermData("SSBSECT", enrlCnt, it.sfrstcr_term_code, it.sfrstcr_crn)
                conn.execute "{ call gb_common.p_commit() }"
                updateTermData("SSBSECT", enrlCnt2, it.sfrstcr_term_code, it.sfrstcr_crn)
            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing enrollment count update ${connectInfo.saveStudentPidm} from StudentPersonIDDML.groovy: $e.message"
            }
        }
        if (connectInfo.saveThis) {
            conn.execute "{ call gb_common.p_commit() }"
        }
    }


    def deleteData(String tableName, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [connectInfo.saveStudentPidm.toInteger()])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for person ${connectInfo.saveStudentPidm} from StudentPersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }


    private def updateTermData(String tableName, String sql, String term, String crn) {
        try {
            int delRows = conn.executeUpdate(sql, [term, crn])
            connectInfo.tableUpdate(tableName, 0, 0, delRows, 0, 0)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing update term data ${connectInfo.saveStudentPidm} ${term} ${crn} from StudentPersonIDDML.groovy: $e.message"
                println "${sql}"
            }
        }

    }


    private def doesTableExist(String tableName) {
        def findTableSql = """select table_name from all_tables where table_name = ?"""
        def findTable = conn.firstRow(findTableSql, [tableName])?.table_name
        if (findTable) return true
        else return false
    }

}
