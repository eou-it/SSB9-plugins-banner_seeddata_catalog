/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 *  General Person View - methods to insert / update general person data:  spriden,
 *  spbpers, spraddr, sprtele
 */

public class GeneralPersonView {

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
  def atyp
  def street1
  def street2
  def street3
  def city
  def ST
  def zip
  def cnty
  def natn
  def status
  def asrc
  def addrSeq
  def emailType
  def emailAddr
  def emailSt
  def preferred
  def comment
  def tele
  def area
  def number
  def ext
  def teleStatus
  def teleAtyp
  def primary
  def intl
  def savePidm
  def newAddrSeq

  def InputData connectInfo
  Sql conn
  Connection connectCall 

  public GeneralPersonView(InputData connectInfo, Sql conn, Connection connectCall ) {

    this.conn = conn
    this.connectInfo = connectInfo
    this.connectCall = connectCall
    this.savePidm = connectInfo.savePidm
  }

  def processGeneralPersonView() {
    // data may come in thru the view with the word null instead of actual blank or space
    if (this.ID == "NULL") { this.ID = "" }
    if (this.PIDM == "NULL") { this.PIDM = "" }
    if (this.lastName == "NULL") { this.lastName = "" }
    if (this.firstName == "NULL") { this.firstName = "" }
    if (this.middle == "NULL") { this.middle = "" }
    if (this.SSN == "NULL") { this.SSN = "" }
    if (this.birthDate == "NULL") { this.birthDate = "" }
    if (this.legacy == "NULL") { this.legacy = "" }
    if (this.ethn == "NULL") { this.ethn = "" }
    if (this.ethnCde == "NULL") { this.ethnCde = "" }
    if (this.confirmCD == "NULL") { this.confirmCD = "" }
    if (this.confirmDT == "NULL") { this.confirmDT = "" }
    if (this.mrtl == "NULL") { this.mrtl = "" }
    if (this.relg == "NULL") { this.relg = "" }
    if (this.sex == "NULL") { this.sex = "" }
    if (this.prefFirst == "NULL") { this.prefFirst = "" }
    if (this.prefix == "NULL") { this.prefix = "" }
    if (this.suffix == "NULL") { this.suffix = "" }
    if (this.citz == "NULL") { this.citz = "" }
    if (this.dead == "NULL") { this.dead = "" }
    if (this.emailType == "NULL") { this.emailType = "" }
    if (this.emailAddr == "NULL") { this.emailAddr = "" }
    if (this.emailSt == "NULL") { this.emailSt = "" }
    if (this.preferred == "NULL") { this.preferred = "" }
    if (this.comment == "NULL") { this.comment = "" }
    if (this.tele == "NULL") { this.tele = "" }
    if (this.area == "NULL") { this.area = "" }
    if (this.number == "NULL") { this.number = "" }
    if (this.ext == "NULL") { this.ext = "" }
    if (this.teleStatus == "NULL") { this.teleStatus = "" }
    if (this.teleAtyp == "NULL") { this.teleAtyp = "" }
    if (this.addrSeq == "NULL") { this.addrSeq = "" }
    if (this.primary == "NULL") { this.primary = "" }
    if (this.intl == "NULL") { this.intl = "" }
    if (this.atyp == "NULL") { this.atyp = "" }
    if (this.street1 == "NULL") { this.street1 = "" }
    if (this.street2 == "NULL") { this.street2 = "" }
    if (this.street3 == "NULL") { this.street3 = "" }
    if (this.city == "NULL") { this.city = "" }
    if (this.ST == "NULL") { this.ST = "" }
    if (this.zip == "NULL") { this.zip = "" }
    if (this.cnty == "NULL") { this.cnty = "" }
    if (this.natn == "NULL") { this.natn = "" }
    if (this.status == "NULL") { this.status = "" }
    if (this.asrc == "NULL") { this.asrc = "" }
    if (this.addrSeq == "NULL") { this.addrSeq = "" }

    if ((savePidm == "") || (savePidm != this.PIDM)) {
      GeneralPersonDML genPers = new GeneralPersonDML(connectInfo, conn, connectCall )
      genPers.PIDM = this.PIDM
      genPers.ID = this.ID
      genPers.lastName = this.lastName
      genPers.firstName = this.firstName
      genPers.middle = this.middle
      genPers.SSN = this.SSN
      genPers.birthDate = this.birthDate
      genPers.legacy = this.legacy
      genPers.ethn = this.ethn
      genPers.ethnCde = this.ethnCde
      genPers.confirmCD = this.confirmCD
      genPers.confirmDT = this.confirmDT
      genPers.mrtl = this.mrtl
      genPers.relg = this.relg
      genPers.sex = this.sex
      genPers.prefFirst = this.prefFirst
      genPers.prefix = this.prefix
      genPers.suffix = this.suffix
      genPers.citz = this.citz
      genPers.dead = this.dead
      genPers.processPerson()
    }

    // process the addresses
    if (this.atyp) {
      AddressDML addr = new AddressDML(connectInfo, conn, connectCall )
      addr.spraddr_pidm = this.PIDM
      addr.spraddr_atyp_code = this.atyp
      addr.spraddr_seqno = this.addrSeq
      addr.spraddr_street_line1 = this.street1
      addr.spraddr_street_line2 = this.street2
      addr.spraddr_street_line3 = this.street3
      addr.spraddr_city = this.city
      addr.spraddr_stat_code = this.ST
      addr.spraddr_zip = this.zip
      addr.spraddr_cnty_code = this.cnty
      addr.spraddr_natn_code = this.natn
      addr.spraddr_status_ind = this.status
      addr.spraddr_asrc_code = this.asrc
      addr.insertSpraddr()
      newAddrSeq = addr.newAddrSeq
    }
    // process the email
    if (this.emailAddr) {
      EmailDML emal = new EmailDML(connectInfo, conn, connectCall )
      emal.goremal_pidm = this.PIDM
      emal.goremal_emal_code = this.emailType
      emal.goremal_email_address = this.emailAddr
      emal.goremal_status_ind = this.emailSt
      emal.goremal_preferred_ind = this.preferred
      emal.goremal_comment = this.comment
      emal.insertGoremal()

    }

    if (this.tele) {
      TelephoneDML tele = new TelephoneDML(connectInfo, conn, connectCall )
      tele.sprtele_pidm = this.PIDM
      tele.sprtele_tele_code = this.tele
      tele.sprtele_phone_area = this.area
      tele.sprtele_phone_number = this.number
      tele.sprtele_phone_ext = this.ext
      tele.sprtele_status_ind = this.teleStatus
      tele.sprtele_atyp_code = this.teleAtyp
      tele.sprtele_addr_seqno = this.newAddrSeq
      tele.sprtele_primary_ind = this.primary
      tele.sprtele_intl_access = this.intl
      tele.insertSprtele()

    }
  }
}
