/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  General Person DDL - methods to insert / update general person data:  spriden,
 *  spbpers, spraddr, sprtele
 */
public class GeneralPersonDML { 
    def ID
    def PIDM
    def lastName
    def firstName
    def middle
    def SSN
    def birthDate
    def legacy
    def ethn
    def ethnCde
    def confirmCD
    def confirmDT
    def mrtl
    def relg
    def sex
    def prefFirst
    def prefix
    def suffix
    def citz
    def dead

    def cntSpriden = 0

    def InputData connectInfo
    Sql conn
    Connection connectCall


    public GeneralPersonDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    def processPerson() {


        if (connectInfo.debugThis) {
            println "Person  def SSN birth ${birthDate} legacy ${legacy} ethn ${ethn}  ethcde ${ethnCde}  " +
                    " confcd ${confirmCD}  confirmdt ${confirmDT} mrtl ${mrtl} relg ${relg} sex ${sex} prefirst  ${prefFirst} " +
                    " prefix ${prefix}  suffix  ${suffix} citz ${citz} dead  ${dead}"
        }

        // find if spriden record already exists
        cntSpriden = 0
        String ssql = "select * from spriden "
        ssql += " where spriden_pidm = ${this.PIDM} "

        try {
            this.conn.eachRow(ssql) {trow ->
                cntSpriden++
            }
        }
        catch (Exception e) {
            println "Could not select PIDM ${this.PIDM} ID ${this.ID} from SPRIDEN. $e.message"
        }
        if (cntSpriden == 0) {
            insertSpriden()
            insertSpbpers()
        }
        // only replace the data if requested on the run parms
        else if (connectInfo.replaceData) {
            deletePersAddrEmailTele()
            insertSpbpers()
        }
    }


    def insertSpriden() {
        PersonIDDML iden = new PersonIDDML(connectInfo, conn, connectCall)
        iden.spriden_pidm = this.PIDM
        iden.spriden_id = this.ID
        iden.spriden_last_name = this.lastName
        iden.spriden_first_name = this.firstName
        iden.spriden_mi = this.middle
        iden.spriden_entity_ind = 'P'
        iden.spriden_user = connectInfo.userID
        iden.spriden_origin = connectInfo.dataOrigin
        iden.insertSpriden()

    }


    def insertSpbpers() {
        PersonBioDML pers = new PersonBioDML(connectInfo, conn, connectCall)
        pers.spbpers_pidm = this.PIDM
        pers.spbpers_ssn = this.SSN
        pers.spbpers_birth_date = this.birthDate
        pers.spbpers_lgcy_code = this.legacy
        pers.spbpers_ethn_code = this.ethn
        pers.spbpers_mrtl_code = this.mrtl
        pers.spbpers_relg_code = this.relg
        pers.spbpers_sex = this.sex
        pers.spbpers_dead_ind = this.dead
        pers.spbpers_pref_first_name = this.prefFirst
        pers.spbpers_name_prefix = this.prefix
        pers.spbpers_name_suffix = this.suffix
        pers.spbpers_citz_code = this.citz
        pers.spbpers_ethn_cde = this.ethnCde
        pers.spbpers_confirmed_re_cde = this.confirmCD
        pers.spbpers_confirmed_re_date = this.confirmDT
        pers.insertSpbpers()

    }


    def deletePersAddrEmailTele() {
        String deleteSQL = ""

        deleteSQL = "delete from spbpers where spbpers_pidm = ${this.PIDM}"
        try {
            def cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPBPERS", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPBPERS ${this.PIDM} ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from spraddr where spraddr_pidm = ${this.PIDM}"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPRADDR", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPRADDR ${this.PIDM} ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from sprtele where sprtele_pidm = ${this.PIDM}"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SPRTELE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete SPRTELE ${this.PIDM} ${this.ID} ${this.lastName} from GeneralPersonDML.groovy: $e.message"
            }
        }
        deleteSQL = "delete from goremal where goremal_pidm = ${this.PIDM}"
        try {
            int cntDel = conn.executeUpdate(deleteSQL)
            connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 0, cntDel)
        }
        catch (Exception e) {
            connectInfo.tableUpdate("GOREMAL", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Problem executing delete GOREMAL ${this.PIDM} ${this.ID} ${this.lastName}from GeneralPersonDML.groovy: $e.message"
            }
        }
    }
}
