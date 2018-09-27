/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * Class to read the general person xml file and process the entities
 */
public class GeneralPersonXML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public GeneralPersonXML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processFile()
    }

    /**
     *  Open the xml file and read using the xml reader, and parse
     */

    def processFile() {
        // define the file to xml parser
        def genPersonCnt = 0

        def genPerson = new XmlParser().parseText(xmlData)

        GeneralPersonView genPers = new GeneralPersonView(connectInfo, conn, connectCall)
        genPers.PIDM = genPerson.PIDM.text()
        genPers.ID = genPerson.ID.text()
        genPers.lastName = genPerson.Last_Name.text()
        genPers.firstName = genPerson.First_Name.text()
        genPers.middle = genPerson.Middle.text()
        genPers.SSN = genPerson.SSN.text()
        genPers.birthDate = genPerson.birthDate2.text()
        genPers.legacy = genPerson.Legacy.text()
        genPers.ethn = genPerson.Ethn.text()
        genPers.ethnCde = genPerson.EthnCde.text()
        genPers.confirmCD = genPerson.ConfirmCD.text()
        genPers.confirmDT = genPerson.ConfirmDT.text()
        genPers.mrtl = genPerson.Mrtl.text()
        genPers.relg = genPerson.Relg.text()
        genPers.sex = genPerson.Sex.text()
        genPers.prefFirst = genPerson.PrefFirst.text()
        genPers.prefix = genPerson.Prefix.text()
        genPers.suffix = genPerson.Suffix.text()
        genPers.citz = genPerson.Citz.text()
        genPers.dead = genPerson.Dead.text()
        genPers.atyp = genPerson.Atyp.text()
        genPers.street1 = genPerson.Street1.text()
        genPers.street2 = genPerson.Street2.text()
        genPers.street3 = genPerson.Street3.text()
        genPers.city = genPerson.city.text()
        genPers.ST = genPerson.ST.text()
        genPers.zip = genPerson.Zip.text()
        genPers.cnty = genPerson.Cnty.text()
        genPers.natn = genPerson.Natn.text()
        genPers.status = genPerson.Status.text()
        genPers.asrc = genPerson.Asrc.text()
        genPers.addrSeq = genPerson.AddrSeq.text()
        genPers.emailType = genPerson.EmailType.text()
        genPers.emailAddr = genPerson.EmailAddr2.text()
        genPers.emailSt = genPerson.EmailSt.text()
        genPers.preferred = genPerson.Preferred.text()
        genPers.comment = genPerson.comment.text()
        genPers.atyp = genPerson.Atyp.text()
        genPers.tele = genPerson.Tele.text()
        genPers.area = genPerson.Area.text()
        genPers.number = genPerson.Number.text()
        genPers.ext = genPerson.Ext.text()
        genPers.teleStatus = genPerson.Tele_Status.text()
        genPers.teleAtyp = genPerson.Tele_Atype.text()
        genPers.primary = genPerson.Primary.text()
        genPers.intl = genPerson.Intl.text()
        genPers.processGeneralPersonView()
        connectInfo.savePidm = genPerson.PIDM.text()

        if (connectInfo.debugThis) {
            println genPersonCnt + " Person: " + genPerson.ID.text() + " LastName: " + genPerson.Last_Name.text()
        }

    }

}

 
