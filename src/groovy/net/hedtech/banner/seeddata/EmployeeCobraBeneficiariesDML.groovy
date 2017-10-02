/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * Created by rvenkate
 */
class EmployeeCobraBeneficiariesDML {
    def bannerid
    def bannerid_bene_pidm
    def pcrbene_pidm
    def PCRBENE_SEQ_NO
    def pcrbene_bene_pidm
    def PCRBENE_BENE_LAST_NAME
    def PCRBENE_BENE_FIRST_NAME
    def PCRBENE_BENE_MI
    def PCRBENE_BREL_CODE
    def PCRBENE_SEX_IND
    def PCRBENE_BIRTH_DATE
    def PCRBENE_COLLEGE_IND
    def PCRBENE_ACTIVITY_DATE
    def PCRBENE_SURNAME_PREFIX
    def PCRBENE_ACA_CHILD_REPORT_IND
    def PCRBENE_SSN
    def PCRBENE_SURROGATE_ID
    def PCRBENE_VERSION
    def PCRBENE_USER_ID
    def PCRBENE_DATA_ORIGIN
    def PCRBENE_VPDI_CODE


    def InputData connectInfo
    Sql conn
    Connection connectCall
    List columns
    List indexColumns
    def Batch batch
    def deleteNode

    def xmlData
    def PIDM
    def BENE_PIDM

    public EmployeeCobraBeneficiariesDML(InputData connectInfo, Sql conn, Connection connectCall) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public EmployeeCobraBeneficiariesDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns, List indexColumns, Batch batch, def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
    //    parseXmlData()
        processEmployeeCobraBeneficiaries()
    }

    def parseXmlData() {
        def pcrbene = new XmlParser().parseText(xmlData)
        this.bannerid = pcrbene.BANNERID
        this.bannerid_bene_pidm = pcrbene.BANNERID_BENE_PIDM
        this.PCRBENE_SEQ_NO = pcrbene.PCRBENE_SEQ_NO.text()
        this.PCRBENE_BENE_LAST_NAME = pcrbene.PCRBENE_BENE_LAST_NAME.text()
        this.PCRBENE_BENE_FIRST_NAME = pcrbene.PCRBENE_BENE_FIRST_NAME.text()
        this.PCRBENE_BENE_MI = pcrbene.PCRBENE_BENE_MI.text()
        this.PCRBENE_BREL_CODE = pcrbene.PCRBENE_BREL_CODE.text()
        this.PCRBENE_SEX_IND = pcrbene.PCRBENE_SEX_IND.text()
        this.PCRBENE_BIRTH_DATE = pcrbene.PCRBENE_BIRTH_DATE.text()
        this.PCRBENE_COLLEGE_IND = pcrbene.PCRBENE_COLLEGE_IND.text()
        this.PCRBENE_ACTIVITY_DATE = pcrbene.PCRBENE_ACTIVITY_DATE.text()
        this.PCRBENE_SURNAME_PREFIX = pcrbene.PCRBENE_SURNAME_PREFIX.text()
        this.PCRBENE_ACA_CHILD_REPORT_IND = pcrbene.PCRBENE_ACA_CHILD_REPORT_IND.text()
        this.PCRBENE_SSN = pcrbene.PCRBENE_SSN.text()
        this.PCRBENE_SURROGATE_ID = pcrbene.PCRBENE_SURROGATE_ID.text()
        this.PCRBENE_VERSION = pcrbene.PCRBENE_VERSION.text()
        this.PCRBENE_USER_ID = pcrbene.PCRBENE_USER_ID.text()
        this.PCRBENE_DATA_ORIGIN = pcrbene.PCRBENE_DATA_ORIGIN.text()
        this.PCRBENE_VPDI_CODE = pcrbene.PCRBENE_VPDI_CODE.text()
        this.pcrbene_bene_pidm = pcrbene.PCRBENE_BENE_PIDM.text()
    }

    def processEmployeeCobraBeneficiaries() {
        def pcrbene = new XmlParser().parseText(xmlData)
        this.bannerid = pcrbene.BANNERID
        this.bannerid_bene_pidm = pcrbene.BANNERID_BENE_PIDM
        this.PCRBENE_SEQ_NO = pcrbene.PCRBENE_SEQ_NO.text()
        this.PCRBENE_BENE_LAST_NAME = pcrbene.PCRBENE_BENE_LAST_NAME.text()
        this.PCRBENE_BENE_FIRST_NAME = pcrbene.PCRBENE_BENE_FIRST_NAME.text()
        this.PCRBENE_BENE_MI = pcrbene.PCRBENE_BENE_MI.text()
        this.PCRBENE_BREL_CODE = pcrbene.PCRBENE_BREL_CODE.text()
        this.PCRBENE_SEX_IND = pcrbene.PCRBENE_SEX_IND.text()
        this.PCRBENE_BIRTH_DATE = pcrbene.PCRBENE_BIRTH_DATE.text()
        this.PCRBENE_COLLEGE_IND = pcrbene.PCRBENE_COLLEGE_IND.text()
        this.PCRBENE_ACTIVITY_DATE = pcrbene.PCRBENE_ACTIVITY_DATE.text()
        this.PCRBENE_SURNAME_PREFIX = pcrbene.PCRBENE_SURNAME_PREFIX.text()
        this.PCRBENE_ACA_CHILD_REPORT_IND = pcrbene.PCRBENE_ACA_CHILD_REPORT_IND.text()
        this.PCRBENE_SSN = pcrbene.PCRBENE_SSN.text()
        this.PCRBENE_SURROGATE_ID = pcrbene.PCRBENE_SURROGATE_ID.text()
        this.PCRBENE_VERSION = pcrbene.PCRBENE_VERSION.text()
        this.PCRBENE_USER_ID = pcrbene.PCRBENE_USER_ID.text()
        this.PCRBENE_DATA_ORIGIN = pcrbene.PCRBENE_DATA_ORIGIN.text()
        this.PCRBENE_VPDI_CODE = pcrbene.PCRBENE_VPDI_CODE.text()


        PIDM = null
     //   bannerid_bene_pidm = null
        String pidmsql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
        try {
            this.conn.eachRow(pidmsql, [this.bannerid.text()]) { trow ->
                PIDM = trow.spriden_pidm
                connectInfo.savePidm = PIDM

            }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Could not select ID in EmployeeCobraBeneficiaries,  ${this.bannerid.text()}  from SPRIDEN. $e.message"
            }
        }

        if (this.bannerid_bene_pidm) {
            String benepidmsql = """select * from spriden  where spriden_id = ? and spriden_change_ind is null"""
            def spridenRow
            spridenRow = conn.firstRow(benepidmsql, [bannerid_bene_pidm.text()])
            if (spridenRow) {
                pcrbene.PCRBENE_BENE_PIDM[0].setValue(spridenRow.SPRIDEN_PIDM.toString())
            }
        }



        // parse the xml  back into  gstring for the dynamic sql loader
        def xmlRecNew = "<${pcrbene.name()}>\n"
        pcrbene.children().each() { fields ->
            def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '').replaceAll(/>/, '').replaceAll(/</, '')
            xmlRecNew += "<${fields.name()}>${value}</${fields.name()}>\n"
        }
        xmlRecNew += "</${pcrbene.name()}>\n"



        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlRecNew, columns, indexColumns, batch, deleteNode)

            }
        }


