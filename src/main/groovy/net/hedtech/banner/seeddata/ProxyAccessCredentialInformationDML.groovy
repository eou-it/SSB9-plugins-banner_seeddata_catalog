/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection
import java.text.SimpleDateFormat

/**
 * ProxyAccessCredentialInformation DML
 */
class ProxyAccessCredentialInformationDML {

    def genidenOnly
    def genidenid
    def gidm
    def systemCode
    def entityCode
    def emailAddress
    def lastName
    def pidm
    def emailVerifiedDate
    def firstName
    def pin
    def pinDisabled
    def pinExpiryDate
    def lastLoginDate
    def noOfInvalidAttempts
    def salt
    def createdBy
    def creationDate
    def lastModifiedBy
    def optOutDate
    def middleName
    def proxyID
    def streetLine1
    def streetLine2
    def streetLine3
    def streetLine4
    def city
    def stateCode
    def zip
    def nationCode
    def countyCode
    def ssn
    def birthDate
    def sex
    def atypCode
    def telephoneTypeCode
    def emailTypeCode
    def addressSourceCode
    def houseNumber
    def surnamePrefix
    def phoneArea
    def phoneNumber
    def phoneExtension
    def countryCodePhone
    def namePrefix
    def nameSuffix
    def prefferedFirstName

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    def delete
    def update
    def create


    public ProxyAccessCredentialInformationDML(InputData connectInfo, Sql conn, Connection connectCall) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public ProxyAccessCredentialInformationDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        insertProxyAccessInfo()
    }


    def parseXmlData() {
        def geniden = new XmlParser().parseText(xmlData)
        this.genidenid = geniden.GENIDEN_ID.text()
        this.genidenOnly = geniden.GENIDEN_ONLY.text()
        this.emailAddress = geniden.GPBPRXY_EMAIL_ADDRESS.text()
        this.systemCode = geniden.GENIDEN_SYST_CODE.text() ? geniden.GENIDEN_SYST_CODE.text() : "EVENT_REGD_USER"
        this.lastName = geniden.GENIDEN_LAST_NAME.text()
        this.pidm = geniden.GENIDEN_PIDM.text() ? geniden.GPBPRXY_PIDM.text().toInteger() : null
        this.emailVerifiedDate = geniden.GENIDEN_EMAIL.text()
        this.firstName = geniden.GENIDEN_FIRST_NAME.text()
        this.pinDisabled = geniden.GPBPRXY_PIN_DISABLED_IND.text()
        this.pinExpiryDate = geniden.GPBPRXY_PIN_EXP_DATE.text()
        this.pin = geniden.GPBPRXY_PIN.text()
        this.lastLoginDate = geniden.GPBPRXY_LAST_LOGIN_DATE.text()
        this.noOfInvalidAttempts = geniden.GENIDEN_INV_LOGIN_CNT.text() ? geniden.GENIDEN_INV_LOGIN_CNT.text().toInteger() : null
        this.createdBy = geniden.GENIDEN_CREATE_USER.text()
        this.creationDate = geniden.GENIDEN_CREATE_DATE.text()
        this.lastModifiedBy = geniden.GENIDEN_USER_ID.text()
        this.optOutDate = geniden.GENIDEN_OPT_OUT_ADV_DATE.text()
        this.entityCode = geniden.GENIDEN_ENTITY_CDE.text() ? geniden.GENIDEN_ENTITY_CDE.text() : 'P'
        this.middleName = geniden.GENIDEN_MI.text()
        this.proxyID = geniden.GENIDEN_ID.text()
        this.streetLine1 = geniden.GENIDEN_STREET_LINE1.text()
        this.streetLine2 = geniden.GENIDEN_STREET_LINE2.text()
        this.streetLine3 = geniden.GENIDEN_STREET_LINE3.text()
        this.streetLine4 = geniden.GENIDEN_STREET_LINE4.text()
        this.city = geniden.GENIDEN_CITY.text()
        this.stateCode = geniden.GENIDEN_STAT_CODE.text()
        this.zip = geniden.GENIDEN_ZIP.text()
        this.nationCode = geniden.GENIDEN_NATN_CODE.text()
        this.countyCode = geniden.GENIDEN_CNTY_CODE.text()
        this.ssn = geniden.GENIDEN_SSN.text()
        this.birthDate = geniden.GENIDEN_BIRTH_DATE.text()
        this.sex = geniden.GENIDEN_SEX.text()
        this.atypCode = geniden.GENIDEN_ATYP_CODE.text()
        this.telephoneTypeCode = geniden.GENIDEN_TELE_CODE.text()
        this.emailTypeCode = geniden.GENIDEN_EMAL_CODE.text()
        this.addressSourceCode = geniden.GENIDEN_ASRC_CODE.text()
        this.houseNumber = geniden.GENIDEN_HOUSE_NUMBER.text()
        this.surnamePrefix = geniden.GENIDEN_SURNAME_PREFIX.text()
        this.phoneArea = geniden.GENIDEN_PHONE_AREA.text()
        this.phoneNumber = geniden.GENIDEN_PHONE_NUMBER.text()
        this.phoneExtension = geniden.GENIDEN_PHONE_EXT.text()
        this.countryCodePhone = geniden.GENIDEN_CTRY_CODE_PHONE.text()
        this.namePrefix = geniden.GENIDEN_NAME_PREFIX.text()
        this.nameSuffix = geniden.GENIDEN_NAME_SUFFIX.text()
        this.prefferedFirstName = geniden.GENIDEN_PREF_FIRST_NAME.text()
        this.delete = geniden.DELETE.text()
    }

    //Insert into GPBPRXY, GENIDEN or if exist an update happens. Reason there dependant records in DB & they become orphan.
    def insertProxyAccessInfo() {
        if (this.genidenid) {
            try {
                def cntGeniden = 0
                String genidensql = """select geniden_gidm as gidm from geniden  where GENIDEN_ID = ?"""
                this.conn.eachRow(genidensql, [this.genidenid]) {trow ->
                    cntGeniden++
                    this.gidm = trow.gidm
                }
                if (cntGeniden && this.delete == "YES") {
                    deleteData(this.genidenid)
                } else {
                    if (!cntGeniden) {
                        genidenCRUD(true)
                    } else {
                        genidenCRUD(false)
                    }
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("GENIDEN", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GPBPRXY,GENIDEN ${this.genidenid} }"
                    println "Problem executing insert for table GENIDEN,GPBPRXY from ProxyAccessCredentialInformationDML.groovy: $e.message"
                }
            }

        }
        else {
            println "Insert GPBPRXY,GENIDEN not performed. No GENIDEN_ID."
        }
    }


    def deleteData(genidenid) {
        deleteData("GPBPRXY", "delete FROM GPBPRXY where GPBPRXY_PROXY_IDM in (select distinct geniden_gidm from geniden where geniden_id =?)  ", genidenid)
        deleteData("GENIDEN", "delete FROM GENIDEN where GENIDEN_ID = ?  ", genidenid)
    }


    def genidenCRUD(create) {
        String GENIDENAPI = null
        if (create) {
            String genidenidgidmsql = """select PROXY_ACCESS_IDM_SEQUENCE.nextval as sequenceid from dual"""
            this.conn.eachRow(genidenidgidmsql) {trow ->
                this.gidm = trow.sequenceid
            }
            GENIDENAPI = "{call  gp_geniden.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
        } else {
            GENIDENAPI = "{call  gp_geniden.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
        }
        CallableStatement insertCallGENIDEN = this.connectCall.prepareCall(GENIDENAPI)
        insertCallGENIDEN.setBigDecimal(1, this.gidm)
        insertCallGENIDEN.setString(2, this.systemCode)
        insertCallGENIDEN.setString(3, this.entityCode)
        insertCallGENIDEN.setString(4, this.genidenid)
        insertCallGENIDEN.setString(5, this.lastName)
        insertCallGENIDEN.setString(6, this.firstName)
        insertCallGENIDEN.setString(7, this.middleName)
        insertCallGENIDEN.setString(8, this.houseNumber)
        insertCallGENIDEN.setString(9, this.streetLine1)
        insertCallGENIDEN.setString(10, this.streetLine2)
        insertCallGENIDEN.setString(11, this.streetLine3)
        insertCallGENIDEN.setString(12, this.streetLine4)
        insertCallGENIDEN.setString(13, this.city)
        insertCallGENIDEN.setString(14, this.stateCode)
        insertCallGENIDEN.setString(15, this.zip)
        insertCallGENIDEN.setString(16, this.nationCode)
        insertCallGENIDEN.setString(17, this.phoneArea)
        insertCallGENIDEN.setString(18, this.phoneNumber)
        insertCallGENIDEN.setString(19, this.phoneExtension)
        insertCallGENIDEN.setString(20, this.countryCodePhone)
        insertCallGENIDEN.setString(21, this.ssn)
        if (this.birthDate == "") { insertCallGENIDEN.setNull(22, java.sql.Types.DATE) }
        else {
            def ddate = new ColumnDateValue(this.birthDate)
            String unfDate = ddate.formatJavaDate()
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
            insertCallGENIDEN.setDate(22, sqlDate)
        }
        insertCallGENIDEN.setString(23, this.sex)
        insertCallGENIDEN.setString(24, this.emailAddress)
        insertCallGENIDEN.setString(25, this.atypCode)
        insertCallGENIDEN.setString(26, this.telephoneTypeCode)
        insertCallGENIDEN.setString(27, this.emailTypeCode)
        insertCallGENIDEN.setString(28, this.addressSourceCode)
        insertCallGENIDEN.setString(29, this.surnamePrefix)
        insertCallGENIDEN.setNull(30, java.sql.Types.VARCHAR)
        insertCallGENIDEN.setNull(31, java.sql.Types.VARCHAR)
        insertCallGENIDEN.setNull(32, java.sql.Types.VARCHAR)
        insertCallGENIDEN.setNull(33, java.sql.Types.VARCHAR)
        insertCallGENIDEN.setNull(34, java.sql.Types.VARCHAR)
        insertCallGENIDEN.setString(35, this.lastModifiedBy)
        insertCallGENIDEN.setString(36, this.namePrefix)
        insertCallGENIDEN.setString(37, this.nameSuffix)
        insertCallGENIDEN.setString(38, this.prefferedFirstName)
        insertCallGENIDEN.setString(39, this.countyCode)
        insertCallGENIDEN.registerOutParameter(40, java.sql.Types.ROWID)


        try {
            insertCallGENIDEN.executeUpdate()
            if (create) {
                connectInfo.tableUpdate("GENIDEN", 0, 1, 0, 0, 0)
            } else {
               connectInfo.tableUpdate("GENIDEN", 0, 0, 1, 0, 0)
            }

        }
        catch (Exception e) {
            connectInfo.tableUpdate("GENIDEN", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Insert GENIDEN ${this.genidenid}}"
                println "Problem executing insert for table GENIDEN from ProxyAccessCredentialInformationDML.groovy: $e.message"
            }
        }
        finally {
            insertCallGENIDEN.close()
        }
        if (!this.genidenOnly || this.genidenOnly == "FALSE") {
            def countGPBPRXY = 0
            String genidensql = """select 1 as proxyDataexist from GPBPRXY where GPBPRXY_PROXY_IDM in (select distinct geniden_gidm from geniden where geniden_id =?)"""
            this.conn.eachRow(genidensql, [this.genidenid]) {trow ->
                countGPBPRXY++
            }
            if (countGPBPRXY) {
                gpbPrxyCRUD(false)
            } else {
                gpbPrxyCRUD(true)
            }
        }
    }


    def gpbPrxyCRUD(create) {
        String GPBPRXYAPI
        if (create) {
            GPBPRXYAPI = "{call  gp_gpbprxy.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
        } else {
            GPBPRXYAPI = "{call  gp_gpbprxy.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
        }
        if (!this.genidenOnly || this.genidenOnly == "FALSE") {
            try {
                this.conn.eachRow("select gspcrpt.f_get_salt(8) as gpbprxy_salt from dual") {
                    this.salt = it.gpbprxy_salt
                }
                this.conn.call("{call gspcrpt.p_saltedhash(?,?,?)}", [this.pin, salt, Sql.VARCHAR]) {
                    userpasswd -> this.pin = userpasswd
                }
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Could not select ID in ThirdPartyAccessPinDML,  ${this.genidenid}  from SPRIDEN. $e.message"
                }
            }

            CallableStatement insertCallGPBPRXY = this.connectCall.prepareCall(GPBPRXYAPI)
            insertCallGPBPRXY.setBigDecimal(1, this.gidm)
            insertCallGPBPRXY.setString(2, this.emailAddress)
            insertCallGPBPRXY.setString(3, this.lastName)
            if (!this.pidm) {
                insertCallGPBPRXY.setNull(4, java.sql.Types.INTEGER)
            } else {
                insertCallGPBPRXY.setLong(4, this.pidm)
            }

            if (this.emailVerifiedDate == "") { insertCallGPBPRXY.setNull(5, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.emailVerifiedDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(5, sqlDate)
            }
            insertCallGPBPRXY.setString(6, this.firstName)
            insertCallGPBPRXY.setString(7, this.pin)
            insertCallGPBPRXY.setString(8, this.pinDisabled)
            if (this.pinExpiryDate == "") { insertCallGPBPRXY.setNull(9, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.pinExpiryDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(9, sqlDate)
            }
            if (this.lastLoginDate == "") { insertCallGPBPRXY.setNull(10, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.lastLoginDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(10, sqlDate)
            }
            if (!this.noOfInvalidAttempts) {
                insertCallGPBPRXY.setNull(11, java.sql.Types.INTEGER)
            } else {
                insertCallGPBPRXY.setInt(11, this.noOfInvalidAttempts)
            }
            insertCallGPBPRXY.setString(12, this.salt)
            insertCallGPBPRXY.setString(13, this.createdBy)
            if (this.creationDate == "") { insertCallGPBPRXY.setNull(14, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.creationDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(14, sqlDate)
            }
            insertCallGPBPRXY.setString(15, this.lastModifiedBy)
            if (this.optOutDate == "") { insertCallGPBPRXY.setNull(16, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.optOutDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(16, sqlDate)
            }
            insertCallGPBPRXY.setString(17, this.entityCode)
            insertCallGPBPRXY.setString(18, this.middleName)
            insertCallGPBPRXY.setString(19, this.proxyID)
            insertCallGPBPRXY.setString(20, this.streetLine1)
            insertCallGPBPRXY.setString(21, this.streetLine2)
            insertCallGPBPRXY.setString(22, this.streetLine3)
            insertCallGPBPRXY.setString(23, this.streetLine4)
            insertCallGPBPRXY.setString(24, this.city)
            insertCallGPBPRXY.setString(25, this.stateCode)
            insertCallGPBPRXY.setString(26, this.zip)
            insertCallGPBPRXY.setString(27, this.nationCode)
            insertCallGPBPRXY.setString(28, this.countyCode)
            insertCallGPBPRXY.setString(29, this.ssn)
            if (this.birthDate == "") { insertCallGPBPRXY.setNull(30, java.sql.Types.DATE) }
            else {
                def ddate = new ColumnDateValue(this.birthDate)
                String unfDate = ddate.formatJavaDate()
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime());
                insertCallGPBPRXY.setDate(30, sqlDate)
            }
            insertCallGPBPRXY.setString(31, this.sex)
            insertCallGPBPRXY.setString(32, this.atypCode)
            insertCallGPBPRXY.setString(33, this.telephoneTypeCode)
            insertCallGPBPRXY.setString(34, this.emailTypeCode)
            insertCallGPBPRXY.setString(35, this.addressSourceCode)
            insertCallGPBPRXY.setString(36, this.houseNumber)
            insertCallGPBPRXY.setString(37, this.surnamePrefix)
            insertCallGPBPRXY.setString(38, this.phoneArea)
            insertCallGPBPRXY.setString(39, this.phoneNumber)
            insertCallGPBPRXY.setString(40, this.phoneExtension)
            insertCallGPBPRXY.setString(41, this.countryCodePhone)
            insertCallGPBPRXY.setString(42, this.namePrefix)
            insertCallGPBPRXY.setString(43, this.nameSuffix)
            insertCallGPBPRXY.setString(44, this.prefferedFirstName)
            insertCallGPBPRXY.registerOutParameter(45, java.sql.Types.ROWID)


            try {
                insertCallGPBPRXY.executeUpdate()
                if (create) {
                  connectInfo.tableUpdate("GPBPRXY", 0, 1, 0, 0, 0)
                } else {
                   connectInfo.tableUpdate("GPBPRXY", 0, 0, 1, 0, 0)
                }

            }
            catch (Exception e) {
                connectInfo.tableUpdate("GPBPRXY", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert GPBPRXY ${this.genidenid}}"
                    println "Problem executing insert for table GPBPRXY from ProxyAccessCredentialInformationDML.groovy: $e.message"
                }
            }
            finally {
                insertCallGPBPRXY.close()
            }
        }
    }


    def deleteData(String tableName, String sql, String genidenid) {
        try {
            int delRows = conn.executeUpdate(sql, [genidenid])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing delete for ${tableName} ${gidm} from ProxyAccessCredentialInformationDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }


}
