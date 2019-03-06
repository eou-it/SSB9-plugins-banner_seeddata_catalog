/*********************************************************************************
  Copyright 2010-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import java.sql.Connection

/**
 * Class and method to manage DML into catalog table
 * Catalog DML to process the catalog entry and insert / update the entry
 */
public class CatalogDeleteDML {

    def scbcrse_subj_code
    def scbcrse_crse_numb

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public CatalogDeleteDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        processCat()
    }


    def parseXmlData() {
        def catalog = new XmlParser().parseText(xmlData)

        if (connectInfo.debugThis) {
            println "--------- New Catalog Delete record ----------"
            println "Subj / Crse: " + catalog.SCBCRSE_SUBJ_CODE.text() + " " + catalog.SCBCRSE_CRSE_NUMB.text()
        }
        this.scbcrse_subj_code = catalog.SCBCRSE_SUBJ_CODE.text()
        this.scbcrse_crse_numb = catalog.SCBCRSE_CRSE_NUMB.text()

    }


    def processCat() {

        java.sql.RowId findCatRow = null
       
        def findCat = """select  rowid scbcrse_rowid from scbcrse
                where scbcrse_eff_term = ?
                and scbcrse_subj_code = ? """
        try {
            conn.eachRow(findCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb]) {row ->
                findCatRow = row.scbcrse_rowid
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb}  "
                println "Problem executing rowid select for table SCBCRSE from CatalogDeleteDML.groovy: $e.message"
            }
        }


        def deleteTab = "SCRINTG"
        def deleteCat = """DELETE FROM SCRINTG
                              WHERE  SCRINTG_SUBJ_CODE = ?
                              and scrintg_data_origin != 'GRAILS'
                               AND  SCRINTG_CRSE_NUMB = ?   """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRFEES"
        deleteCat = """DELETE FROM SCRFEES
                               WHERE  SCRFEES_SUBJ_CODE = ?
                               and scrfees_data_origin != 'GRAILS'
                                AND  SCRFEES_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCBSUPP"
        deleteCat = """DELETE FROM SCBSUPP
                               WHERE  SCBSUPP_SUBJ_CODE = ?
                               and scbsupp_data_origin != 'GRAILS'
                                AND  SCBSUPP_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSBGI"
        deleteCat = """DELETE FROM SCRSBGI
                               WHERE  SCRSBGI_SUBJ_CODE = ?
                               and scrsbgi_data_origin != 'GRAILS'
                                AND  SCRSBGI_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRATTR"
        deleteCat = """DELETE FROM SCRATTR
                               WHERE  SCRATTR_SUBJ_CODE = ?
                               and scrattr_data_origin != 'GRAILS'
                                AND  SCRATTR_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRMEXC"
        deleteCat = """DELETE FROM SCRMEXC
                               WHERE  SCRMEXC_SUBJ_CODE = ?
                               and scrmexc_data_origin != 'GRAILS'
                                AND  SCRMEXC_CRSE_NUMB = ?  """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCREQIV"
        deleteCat = """DELETE FROM SCREQIV
                               WHERE  SCREQIV_SUBJ_CODE = ?
                               and screqiv_data_origin != 'GRAILS'
                                AND  SCREQIV_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRCLBD"
        deleteCat = """DELETE FROM SCRCLBD
                               WHERE  SCRCLBD_SUBJ_CODE = ?
                               and scrclbd_data_origin != 'GRAILS'
                                AND  SCRCLBD_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRCORQ"
        deleteCat = """DELETE FROM SCRCORQ
                               WHERE  SCRCORQ_SUBJ_CODE = ?
                               and scrcorq_data_origin != 'GRAILS'
                                AND  SCRCORQ_CRSE_NUMB = ?  """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRTEXT"
        deleteCat = """DELETE FROM SCRTEXT
                               WHERE  SCRTEXT_SUBJ_CODE = ?
                               and scrtext_data_origin != 'GRAILS'
                                AND  SCRTEXT_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRTRM"
        deleteCat = """DELETE FROM SCRRTRM
                               WHERE  SCRRTRM_SUBJ_CODE = ?
                               and scrrtrm_data_origin != 'GRAILS'
                                AND  SCRRTRM_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)
        deleteTab = "SCRRCMP"
        deleteCat = """DELETE FROM SCRRCMP
                               WHERE  SCRRCMP_SUBJ_CODE = ?
                                AND  SCRRCMP_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRCOL"
        deleteCat = """DELETE FROM SCRRCOL
                               WHERE  SCRRCOL_SUBJ_CODE = ?
                               and scrrcol_data_origin != 'GRAILS'
                                AND  SCRRCOL_CRSE_NUMB = ?"""
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRCAM"
        deleteCat = """DELETE FROM SCRRCAM
                               WHERE  SCRRCAM_SUBJ_CODE = ?
                               and scrrcam_data_origin != 'GRAILS'
                                AND  SCRRCAM_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)
        deleteTab = "SCRRDEP"
        deleteCat = """DELETE FROM SCRRDEP
                              WHERE  SCRRDEP_SUBJ_CODE = ?
                              and scrrdep_data_origin != 'GRAILS'
                               AND  SCRRDEP_CRSE_NUMB = ?"""
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRATT"
        deleteCat = """DELETE FROM SCRRATT
                               WHERE  SCRRATT_SUBJ_CODE = ?
                               and scrratt_data_origin != 'GRAILS'
                                AND  SCRRATT_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRCHR"
        deleteCat = """DELETE FROM SCRRCHR
                               WHERE  SCRRCHR_SUBJ_CODE = ?
                               and scrrchr_data_origin != 'GRAILS'
                                AND  SCRRCHR_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRTST"
        deleteCat = """DELETE FROM SCRRTST
                               WHERE  SCRRTST_SUBJ_CODE = ?
                               and scrrtst_data_origin != 'GRAILS'
                                AND  SCRRTST_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRMAJ"
        deleteCat = """DELETE FROM SCRRMAJ
                               WHERE  SCRRMAJ_SUBJ_CODE = ?
                               and scrrmaj_data_origin != 'GRAILS'
                                AND  SCRRMAJ_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRCLS"
        deleteCat = """DELETE FROM SCRRCLS
                               WHERE  SCRRCLS_SUBJ_CODE = ?
                               and scrrcls_data_origin != 'GRAILS'
                                AND  SCRRCLS_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRLVL"
        deleteCat = """DELETE FROM SCRRLVL
                               WHERE  SCRRLVL_SUBJ_CODE = ?
                               and scrrlvl_data_origin != 'GRAILS'
                                AND  SCRRLVL_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRDEG"
        deleteCat = """DELETE FROM SCRRDEG
                               WHERE  SCRRDEG_SUBJ_CODE = ?
                               and scrrdeg_data_origin != 'GRAILS'
                                AND  SCRRDEG_CRSE_NUMB = ?"""
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRRPRG"
        deleteCat = """DELETE FROM SCRRPRG
                               WHERE  SCRRPRG_SUBJ_CODE = ?
                               and scrrprg_data_origin != 'GRAILS'
                                AND  SCRRPRG_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)
        deleteTab = "SCRRARE"
        deleteCat = """DELETE FROM SCRRARE
                               WHERE  SCRRARE_SUBJ_CODE = ?
                               and scrrare_data_origin != 'GRAILS'
                                AND  SCRRARE_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRCPRT"
        deleteCat = """DELETE FROM SCRCPRT
                               WHERE  SCRCPRT_SUBJ_CODE = ?
                               and scrcprt_data_origin != 'GRAILS'
                                AND  SCRCPRT_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)
        deleteTab = "SCRCRDF"
        deleteCat = """DELETE FROM SCRCRDF
                               WHERE  SCRCRDF_SUBJ_CODE = ?
                               and scrcrdf_data_origin != 'GRAILS'
                                AND  SCRCRDF_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSYLN"
        deleteCat = """DELETE FROM SCRSYLN
                               WHERE  SCRSYLN_SUBJ_CODE = ?
                               and scrsyln_data_origin != 'GRAILS'
                                AND  SCRSYLN_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSYLO"
        deleteCat = """DELETE FROM SCRSYLO
                               WHERE  SCRSYLO_SUBJ_CODE = ?
                               and scrsylo_data_origin != 'GRAILS'
                                AND  SCRSYLO_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSYRM"
        deleteCat = """DELETE FROM SCRSYRM
                               WHERE  SCRSYRM_SUBJ_CODE = ?
                               and scrsyrm_data_origin != 'GRAILS'
                                AND  SCRSYRM_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSYTR"
        deleteCat = """DELETE FROM SCRSYTR
                               WHERE  SCRSYTR_SUBJ_CODE = ?
                               and scrsytr_data_origin != 'GRAILS'
                                AND  SCRSYTR_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCBDESC"
        deleteCat = """DELETE FROM SCBDESC
                               WHERE  SCBDESC_SUBJ_CODE = ?
                               and scbdesc_data_origin != 'GRAILS'
                                AND  SCBDESC_CRSE_NUMB = ?"""
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRLEVL"
        deleteCat = """DELETE FROM SCRLEVL
                                                  WHERE  SCRLEVL_SUBJ_CODE = ?
                                                  and scrlevl_data_origin != 'GRAILS'
                                                   AND  SCRLEVL_CRSE_NUMB = ?  """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRGMOD"
        deleteCat = """DELETE FROM SCRGMOD
                               WHERE  SCRGMOD_SUBJ_CODE = ?
                               and scrgmod_data_origin != 'GRAILS'
                                AND  SCRGMOD_CRSE_NUMB = ?  """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCRSCHD"
        deleteCat = """DELETE FROM SCRSCHD
                               WHERE  SCRSCHD_SUBJ_CODE = ?
                               and scrschd_data_origin != 'GRAILS'
                                AND  SCRSCHD_CRSE_NUMB = ?   """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

        deleteTab = "SCBCRSE"
        deleteCat = """DELETE FROM SCBCRSE
                               WHERE  SCBCRSE_SUBJ_CODE = ?
                               and scbcrse_data_origin != 'GRAILS'
                                AND  SCBCRSE_CRSE_NUMB = ? """
        deleteData(deleteTab, this.scbcrse_subj_code, this.scbcrse_crse_numb, deleteCat)

    }


    private def deleteData(String tableName, subject, crse, String sql) {
        try {

            int delRows = conn.executeUpdate(sql, [subject, crse])
            connectInfo.tableUpdate(tableName, 0, 0, 0, 0, delRows)
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem executing ${tableName} for {$subject} ${crse}from CatalogDeleteDML.groovy: $e.message"
                println "${sql}"
            }
        }
    }
}
