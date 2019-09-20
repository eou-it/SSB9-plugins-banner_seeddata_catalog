/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata


import groovy.sql.Sql
import java.sql.CallableStatement
import java.sql.Connection

/**
 * Class and method to manage DML into catalog table
 * Catalog DML to process the catalog entry and insert / update the entry
 */
public class CatalogDML {

    def scbcrse_subj_code
    def scbcrse_crse_numb
    def scbcrse_eff_term
    def scbcrse_csta_code
    def scbcrse_repeat_limit
    def scbcrse_max_rpt_units
    def scbcrse_reps_code
    def scbcrse_coll_code
    def scbcrse_dept_code
    def scbcrse_divs_code
    def scbcrse_cipc_code
    def scbcrse_ceu_ind
    def scbcrse_credit_hr_low
    def scbcrse_credit_hr_high
    def scbcrse_credit_hr_ind
    def scbcrse_bill_hr_low
    def scbcrse_bill_hr_high
    def scbcrse_bill_hr_ind
    def scbcrse_cont_hr_low
    def scbcrse_cont_hr_high
    def scbcrse_cont_hr_ind
    def scbcrse_lec_hr_low
    def scbcrse_lec_hr_high
    def scbcrse_lec_hr_ind
    def scbcrse_lab_hr_low
    def scbcrse_lab_hr_high
    def scbcrse_lab_hr_ind
    def scbcrse_oth_hr_low
    def scbcrse_oth_hr_high
    def scbcrse_oth_hr_ind
    def scbcrse_title
    def scbcrse_aprv_code
    def scbcrse_dunt_code
    def scbcrse_number_of_units
    def scrgmod_gmod_code
    def scrlevl_levl_code1
    def scrschd_schd_code
    def scrschd_workload
    def scrscdh_max_enrl
    def scrschd_adj_workload
    def scbcrse_capp_prereq_test_ind
    def scbcrse_pwav_code
    def scbcrse_tuiw_ind
    def scbcrse_add_fees_ind
    def scbcrse_prereq_chk_method_cde
    def removeAll
    def deleteNode


    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData


    public CatalogDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        parseXmlData()
        if (deleteNode == "YES") processCatDelete()
        else processCat()
    }


    def parseXmlData() {
        def catalog = new XmlParser().parseText(xmlData)

        deleteNode = catalog.DELETE?.text()

        if (connectInfo.debugThis) {
            println "--------- New Catalog record ----------"
            println "Subj / Crse: " + catalog.SCBCRSE_SUBJ_CODE.text() + " " + catalog.SCBCRSE_CRSE_NUMB.text()
        }
        this.scbcrse_subj_code = catalog.SCBCRSE_SUBJ_CODE.text()
        this.scbcrse_crse_numb = catalog.SCBCRSE_CRSE_NUMB.text()
        this.scbcrse_eff_term = catalog.SCBCRSE_EFF_TERM.text()
        this.scbcrse_csta_code = catalog.SCBCRSE_CSTA_CODE.text()
        this.scbcrse_repeat_limit = catalog.SCBCRSE_REPEAT_LIMcatalog.text()
        this.scbcrse_max_rpt_units = catalog.SCBCRSE_MAX_RPT_UNITS.text()
        this.scbcrse_reps_code = catalog.SCBCRSE_REPS_CODE.text()
        this.scbcrse_coll_code = catalog.SCBCRSE_COLL_CODE.text()
        this.scbcrse_dept_code = catalog.SCBCRSE_DEPT_CODE.text()
        this.scbcrse_divs_code = catalog.SCBCRSE_DIVS_CODE.text()
        this.scbcrse_cipc_code = catalog.SCBCRSE_CIPC_CODE.text()
        this.scbcrse_ceu_ind = catalog.SCBCRSE_CEU_IND.text()
        this.scbcrse_credit_hr_low = catalog.SCBCRSE_CREDIT_HR_LOW.text()
        this.scbcrse_credit_hr_high = catalog.SCBCRSE_CREDIT_HR_HIGH.text()
        this.scbcrse_credit_hr_ind = catalog.SCBCRSE_CREDIT_HR_IND.text()
        this.scbcrse_bill_hr_low = catalog.SCBCRSE_BILL_HR_LOW.text()
        this.scbcrse_bill_hr_high = catalog.SCBCRSE_BILL_HR_HIGH.text()
        this.scbcrse_bill_hr_ind = catalog.SCBCRSE_BILL_HR_IND.text()
        this.scbcrse_cont_hr_low = catalog.SCBCRSE_CONT_HR_LOW.text()
        this.scbcrse_cont_hr_high = catalog.SCBCRSE_CONT_HR_HIGH.text()
        this.scbcrse_cont_hr_ind = catalog.SCBCRSE_CONT_HR_IND.text()
        this.scbcrse_lec_hr_low = catalog.SCBCRSE_LEC_HR_LOW.text()
        this.scbcrse_lec_hr_high = catalog.SCBCRSE_LEC_HR_HIGH.text()
        this.scbcrse_lec_hr_ind = catalog.SCBCRSE_LEC_HR_IND.text()
        this.scbcrse_lab_hr_low = catalog.SCBCRSE_LAB_HR_LOW.text()
        this.scbcrse_lab_hr_high = catalog.SCBCRSE_LAB_HR_HIGH.text()
        this.scbcrse_lab_hr_ind = catalog.SCBCRSE_LAB_HR_IND.text()
        this.scbcrse_oth_hr_low = catalog.SCBCRSE_OTH_HR_LOW.text()
        this.scbcrse_oth_hr_high = catalog.SCBCRSE_OTH_HR_HIGH.text()
        this.scbcrse_oth_hr_ind = catalog.SCBCRSE_OTH_HR_IND.text()
        this.scbcrse_title = catalog.SCBCRSE_TITLE.text()
        this.scbcrse_aprv_code = catalog.SCBCRSE_APRV_CODE.text()
        this.scbcrse_dunt_code = catalog.SCBCRSE_DUNT_CODE.text()
        this.scbcrse_number_of_units = catalog.SCBCRSE_NUMBER_OF_UNITS.text()
        this.scrgmod_gmod_code = catalog.SCRGMOD_GMOD_CODE.text()
        this.scrlevl_levl_code1 = catalog.SCRLEVL_LEVL_CODE1.text()
        this.scrschd_schd_code = catalog.SCRSCHD_SCHD_CODE.text()
        this.scrschd_workload = catalog.SCRSCHD_WORKLOAD.text()
        this.scrscdh_max_enrl = catalog.SCRSCDH_MAX_ENRL.text()
        this.scrschd_adj_workload = catalog.SCRSCHD_ADJ_WORKLOAD.text()
        this.scbcrse_capp_prereq_test_ind = catalog.SCBCRSE_CAPP_PREREQ_TEST_IND.text()
        this.scbcrse_pwav_code = catalog.SCBCRSE_PWAV_CODE.text()
        this.scbcrse_tuiw_ind = catalog.SCBCRSE_TUIW_IND.text()
        this.scbcrse_add_fees_ind = catalog.SCBCRSE_ADD_FEES_IND.text()
        this.scbcrse_prereq_chk_method_cde = catalog.SCBCRSE_PREREQ_CHK_METHOD_CDE.text()
        this.removeAll = catalog?.SCBCRSE_REMOVE_ALL?.text()

    }


    def processCatDelete() {

        try {
            conn.execute("""call gb_common.p_set_context('SB_COURSE', 'COURSE_DELETE_RULES', 'Y')""")
            int delRows = conn.executeUpdate(""" DELETE FROM SV_SCBCRSE WHERE  SCBCRSE_SUBJ_CODE = ? AND SCBCRSE_CRSE_NUMB = ?
                           AND SCBCRSE_EFF_TERM = ( select min(a.scbcrse_eff_term)
                               from scbcrse a
                               where a.scbcrse_subj_code = sv_scbcrse.scbcrse_subj_code
                                and a.scbcrse_crse_numb = sv_scbcrse.scbcrse_crse_numb)""",
                                             [this.scbcrse_subj_code, this.scbcrse_crse_numb])
            connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 0, delRows)

            conn.executeUpdate(""" DELETE FROM SCRINTG WHERE  SCRINTG_SUBJ_CODE = ? AND SCRINTG_CRSE_NUMB = ?""",
                               [this.scbcrse_subj_code, this.scbcrse_crse_numb])

            conn.executeUpdate(""" DELETE FROM SCRSECT WHERE  SCRSECT_SUBJ_CODE = ? AND SCRSECT_CRSE_NUMB = ?""",
                               [this.scbcrse_subj_code, this.scbcrse_crse_numb])
            conn.executeUpdate(""" delete from scbcrky where scbcrky_subj_code = ? and scbcrky_crse_numb = ?""",
                               [this.scbcrse_subj_code, this.scbcrse_crse_numb])
            connectInfo.tableUpdate("SCBCRKY", 0, 0, 0, 0, 1)
            if (connectInfo.debugThis) {println "Delete Catalog: ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb}" }
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                connectInfo.tableUpdate("SCBCRKY", 0, 0, 0, 1, 0)
                println "Problem setting up context variable for catalog delete ${e} ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb}"
            }
        }
    }


    def processCat() {

        java.sql.RowId findCatRow = null
        def findTerm = null
        String findCat = null
        findCat = """select  rowid scbcrse_rowid from scbcrse
                where scbcrse_eff_term = ?
                and scbcrse_subj_code = ?
                and scbcrse_crse_numb = ? """
        try {
            conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code, this.scbcrse_crse_numb]) {row ->
                findCatRow = row.scbcrse_rowid
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                println "Problem executing rowid select for table SCBCRSE from CatalogDML.groovy: $e.message"
            }
        }
        findCat = """select  scbcrse_eff_term from scbcrse
                  where scbcrse_eff_term != ?
                  and scbcrse_subj_code = ?
                  and scbcrse_crse_numb = ?
                  and rownum = 1"""
        try {
            conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code, this.scbcrse_crse_numb]) {row ->
                findTerm = row.scbcrse_eff_term
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                println "Problem executing eff term select for table SCBCRSE from CatalogDML.groovy: $e.message"
            }
        }
        def sectionExists = ""
        String sectionSQL = """SELECT   ssbsect_term_code
                              FROM ssbsect
                              WHERE ssbsect_subj_code = ?
                              AND ssbsect_crse_numb = ?
                              AND ssbsect_term_code >= ?
                              AND ssbsect_term_code < '999999' """
        try {
            conn.eachRow(sectionSQL, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term]) {sect ->
                sectionExists = sect.ssbsect_term_code
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "Select Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                println "Problem executing section select for table SCBCRSE from CatalogDML.groovy: $e.message"
            }
        }
        // already exists, delete children data and then update the course record

        if (((findCatRow) || (findTerm)) && (connectInfo.replaceData)) {
            String deleteCat = null
            def deleteTab = null
            int delRows = 0
            try {
                if (findTerm && !removeAll) {   // future term exists for the course {

                    deleteTab = "SCRLEVL"
                    deleteCat = """DELETE FROM SCRLEVL
                              WHERE  SCRLEVL_SUBJ_CODE = ?
                               AND  SCRLEVL_CRSE_NUMB = ?
                               AND  SCRLEVL_EFF_TERM = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRINTG"
                    deleteCat = """DELETE FROM SCRINTG
                              WHERE  SCRINTG_SUBJ_CODE = ?
                               AND  SCRINTG_CRSE_NUMB = ?
                               AND  SCRINTG_TERM_CODE_EFF = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRGMOD"
                    deleteCat = """DELETE FROM SCRGMOD
                               WHERE  SCRGMOD_SUBJ_CODE = ?
                                AND  SCRGMOD_CRSE_NUMB = ?   
                                AND  SCRGMOD_EFF_TERM = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])

                    deleteTab = "SCRSCHD"
                    deleteCat = """DELETE FROM SCRSCHD
                               WHERE  SCRSCHD_SUBJ_CODE = ?
                                AND  SCRSCHD_CRSE_NUMB = ?
                                AND  SCRSCHD_EFF_TERM = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRFEES"
                    deleteCat = """DELETE FROM SCRFEES
                               WHERE  SCRFEES_SUBJ_CODE = ?
                                AND  SCRFEES_CRSE_NUMB = ?
                                AND  SCRFEES_EFF_TERM = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCBSUPP"
                    deleteCat = """DELETE FROM SCBSUPP
                               WHERE  SCBSUPP_SUBJ_CODE = ?
                                AND  SCBSUPP_CRSE_NUMB = ?
                                AND  SCBSUPP_EFF_TERM = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSBGI"
                    deleteCat = """DELETE FROM SCRSBGI
                               WHERE  SCRSBGI_SUBJ_CODE = ?
                                AND  SCRSBGI_CRSE_NUMB = ?
                                AND  SCRSBGI_EFF_TERM = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRATTR"
                    deleteCat = """DELETE FROM SCRATTR
                               WHERE  SCRATTR_SUBJ_CODE = ?
                                AND  SCRATTR_CRSE_NUMB = ?
                                AND  SCRATTR_EFF_TERM = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRMEXC"
                    deleteCat = """DELETE FROM SCRMEXC
                               WHERE  SCRMEXC_SUBJ_CODE = ?
                                AND  SCRMEXC_CRSE_NUMB = ?
                                AND  SCRMEXC_EFF_TERM = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCREQIV"
                    deleteCat = """DELETE FROM SCREQIV
                               WHERE  SCREQIV_SUBJ_CODE = ?
                                AND  SCREQIV_CRSE_NUMB = ?
                                AND  SCREQIV_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCLBD"
                    deleteCat = """DELETE FROM SCRCLBD
                               WHERE  SCRCLBD_SUBJ_CODE = ?
                                AND  SCRCLBD_CRSE_NUMB = ?
                                AND  SCRCLBD_TERM_CODE_EFF = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCORQ"
                    deleteCat = """DELETE FROM SCRCORQ
                               WHERE  SCRCORQ_SUBJ_CODE = ?
                                AND  SCRCORQ_CRSE_NUMB = ?
                                 AND  SCRCORQ_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRTEXT"
                    deleteCat = """DELETE FROM SCRTEXT
                               WHERE  SCRTEXT_SUBJ_CODE = ?
                                AND  SCRTEXT_CRSE_NUMB = ?
                                AND  SCRTEXT_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRTRM"
                    deleteCat = """DELETE FROM SCRRTRM
                               WHERE  SCRRTRM_SUBJ_CODE = ?
                                AND  SCRRTRM_CRSE_NUMB = ?
                                AND  SCRRTRM_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCMP"
                    deleteCat = """DELETE FROM SCRRCMP
                               WHERE  SCRRCMP_SUBJ_CODE = ?
                                AND  SCRRCMP_CRSE_NUMB = ?
                                AND  SCRRCMP_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCOL"
                    deleteCat = """DELETE FROM SCRRCOL
                               WHERE  SCRRCOL_SUBJ_CODE = ?
                                AND  SCRRCOL_CRSE_NUMB = ?
                                AND  SCRRCOL_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCAM"
                    deleteCat = """DELETE FROM SCRRCAM
                               WHERE  SCRRCAM_SUBJ_CODE = ?
                                AND  SCRRCAM_CRSE_NUMB = ?
                                AND  SCRRCAM_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRDEP"
                    deleteCat = """DELETE FROM SCRRDEP
                              WHERE  SCRRDEP_SUBJ_CODE = ?
                               AND  SCRRDEP_CRSE_NUMB = ?
                             AND SCRRDEP_EFF_TERM = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRATT"
                    deleteCat = """DELETE FROM SCRRATT
                               WHERE  SCRRATT_SUBJ_CODE = ?
                                AND  SCRRATT_CRSE_NUMB = ?
                               AND SCRRATT_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCHR"
                    deleteCat = """DELETE FROM SCRRCHR
                               WHERE  SCRRCHR_SUBJ_CODE = ?
                                AND  SCRRCHR_CRSE_NUMB = ?
                              AND SCRRCHR_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRTST"
                    deleteCat = """DELETE FROM SCRRTST
                               WHERE  SCRRTST_SUBJ_CODE = ?
                                AND  SCRRTST_CRSE_NUMB = ?
                                AND  SCRRTST_TERM_CODE_EFF = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRMAJ"
                    deleteCat = """DELETE FROM SCRRMAJ
                               WHERE  SCRRMAJ_SUBJ_CODE = ?
                                AND  SCRRMAJ_CRSE_NUMB = ?
                                AND  SCRRMAJ_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCLS"
                    deleteCat = """DELETE FROM SCRRCLS
                               WHERE  SCRRCLS_SUBJ_CODE = ?
                                AND  SCRRCLS_CRSE_NUMB = ?
                                AND  SCRRCLS_EFF_TERM = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRLVL"
                    deleteCat = """DELETE FROM SCRRLVL
                               WHERE  SCRRLVL_SUBJ_CODE = ?
                                AND  SCRRLVL_CRSE_NUMB = ?
                                AND  SCRRLVL_EFF_TERM = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRDEG"
                    deleteCat = """DELETE FROM SCRRDEG
                               WHERE  SCRRDEG_SUBJ_CODE = ?
                                AND  SCRRDEG_CRSE_NUMB = ?
                                AND  SCRRDEG_TERM_CODE_EFFECTIVE = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRPRG"
                    deleteCat = """DELETE FROM SCRRPRG
                               WHERE  SCRRPRG_SUBJ_CODE = ?
                                AND  SCRRPRG_CRSE_NUMB = ?
                                AND  SCRRPRG_TERM_CODE_EFFECTIVE = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRARE"
                    deleteCat = """DELETE FROM SCRRARE
                               WHERE  SCRRARE_SUBJ_CODE = ?
                                AND  SCRRARE_CRSE_NUMB = ?
                                AND  SCRRARE_TERM_CODE_EFFECTIVE = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCPRT"
                    deleteCat = """DELETE FROM SCRCPRT
                               WHERE  SCRCPRT_SUBJ_CODE = ?
                                AND  SCRCPRT_CRSE_NUMB = ?
                                AND  SCRCPRT_TERM_CODE_EFF = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCRDF"
                    deleteCat = """DELETE FROM SCRCRDF
                               WHERE  SCRCRDF_SUBJ_CODE = ?
                                AND  SCRCRDF_CRSE_NUMB = ?
                                AND  SCRCRDF_TERM_CODE_EFF = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYLN"
                    deleteCat = """DELETE FROM SCRSYLN
                               WHERE  SCRSYLN_SUBJ_CODE = ?
                                AND  SCRSYLN_CRSE_NUMB = ?
                                AND  SCRSYLN_TERM_CODE_EFF = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYLO"
                    deleteCat = """DELETE FROM SCRSYLO
                               WHERE  SCRSYLO_SUBJ_CODE = ?
                                AND  SCRSYLO_CRSE_NUMB = ?
                                AND  SCRSYLO_TERM_CODE_EFF = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYRM"
                    deleteCat = """DELETE FROM SCRSYRM
                               WHERE  SCRSYRM_SUBJ_CODE = ?
                                AND  SCRSYRM_CRSE_NUMB = ?
                                AND  SCRSYRM_TERM_CODE_EFF = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYTR"
                    deleteCat = """DELETE FROM SCRSYTR
                               WHERE  SCRSYTR_SUBJ_CODE = ?
                                AND  SCRSYTR_CRSE_NUMB = ?
                                AND  SCRSYTR_TERM_CODE_EFF = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCBDESC"
                    deleteCat = """DELETE FROM SCBDESC
                               WHERE  SCBDESC_SUBJ_CODE = ?
                                AND  SCBDESC_CRSE_NUMB = ?
                                AND  SCBDESC_TERM_CODE_EFF = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                }
                else {


                    deleteTab = "SCRINTG"
                    deleteCat = """DELETE FROM SCRINTG
                              WHERE  SCRINTG_SUBJ_CODE = ?
                               AND  SCRINTG_CRSE_NUMB = ?   """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRFEES"
                    deleteCat = """DELETE FROM SCRFEES
                               WHERE  SCRFEES_SUBJ_CODE = ?
                                AND  SCRFEES_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCBSUPP"
                    deleteCat = """DELETE FROM SCBSUPP
                               WHERE  SCBSUPP_SUBJ_CODE = ?
                                AND  SCBSUPP_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSBGI"
                    deleteCat = """DELETE FROM SCRSBGI
                               WHERE  SCRSBGI_SUBJ_CODE = ?
                                AND  SCRSBGI_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRATTR"
                    deleteCat = """DELETE FROM SCRATTR
                               WHERE  SCRATTR_SUBJ_CODE = ?
                                AND  SCRATTR_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRMEXC"
                    deleteCat = """DELETE FROM SCRMEXC
                               WHERE  SCRMEXC_SUBJ_CODE = ?
                                AND  SCRMEXC_CRSE_NUMB = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCREQIV"
                    deleteCat = """DELETE FROM SCREQIV
                               WHERE  SCREQIV_SUBJ_CODE = ?
                                AND  SCREQIV_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCLBD"
                    deleteCat = """DELETE FROM SCRCLBD
                               WHERE  SCRCLBD_SUBJ_CODE = ?
                                AND  SCRCLBD_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCORQ"
                    deleteCat = """DELETE FROM SCRCORQ
                               WHERE  SCRCORQ_SUBJ_CODE = ?
                                AND  SCRCORQ_CRSE_NUMB = ?  """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRTEXT"
                    deleteCat = """DELETE FROM SCRTEXT
                               WHERE  SCRTEXT_SUBJ_CODE = ?
                                AND  SCRTEXT_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRTRM"
                    deleteCat = """DELETE FROM SCRRTRM
                               WHERE  SCRRTRM_SUBJ_CODE = ?
                                AND  SCRRTRM_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCMP"
                    deleteCat = """DELETE FROM SCRRCMP
                               WHERE  SCRRCMP_SUBJ_CODE = ?
                                AND  SCRRCMP_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCOL"
                    deleteCat = """DELETE FROM SCRRCOL
                               WHERE  SCRRCOL_SUBJ_CODE = ?
                                AND  SCRRCOL_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCAM"
                    deleteCat = """DELETE FROM SCRRCAM
                               WHERE  SCRRCAM_SUBJ_CODE = ?
                                AND  SCRRCAM_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRDEP"
                    deleteCat = """DELETE FROM SCRRDEP
                              WHERE  SCRRDEP_SUBJ_CODE = ?
                               AND  SCRRDEP_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRATT"
                    deleteCat = """DELETE FROM SCRRATT
                               WHERE  SCRRATT_SUBJ_CODE = ?
                                AND  SCRRATT_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCHR"
                    deleteCat = """DELETE FROM SCRRCHR
                               WHERE  SCRRCHR_SUBJ_CODE = ?
                                AND  SCRRCHR_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRTST"
                    deleteCat = """DELETE FROM SCRRTST
                               WHERE  SCRRTST_SUBJ_CODE = ?
                                AND  SCRRTST_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRMAJ"
                    deleteCat = """DELETE FROM SCRRMAJ
                               WHERE  SCRRMAJ_SUBJ_CODE = ?
                                AND  SCRRMAJ_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRCLS"
                    deleteCat = """DELETE FROM SCRRCLS
                               WHERE  SCRRCLS_SUBJ_CODE = ?
                                AND  SCRRCLS_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRLVL"
                    deleteCat = """DELETE FROM SCRRLVL
                               WHERE  SCRRLVL_SUBJ_CODE = ?
                                AND  SCRRLVL_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRDEG"
                    deleteCat = """DELETE FROM SCRRDEG
                               WHERE  SCRRDEG_SUBJ_CODE = ?
                                AND  SCRRDEG_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRPRG"
                    deleteCat = """DELETE FROM SCRRPRG
                               WHERE  SCRRPRG_SUBJ_CODE = ?
                                AND  SCRRPRG_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRRARE"
                    deleteCat = """DELETE FROM SCRRARE
                               WHERE  SCRRARE_SUBJ_CODE = ?
                                AND  SCRRARE_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCPRT"
                    deleteCat = """DELETE FROM SCRCPRT
                               WHERE  SCRCPRT_SUBJ_CODE = ?
                                AND  SCRCPRT_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRCRDF"
                    deleteCat = """DELETE FROM SCRCRDF
                               WHERE  SCRCRDF_SUBJ_CODE = ?
                                AND  SCRCRDF_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYLN"
                    deleteCat = """DELETE FROM SCRSYLN
                               WHERE  SCRSYLN_SUBJ_CODE = ?
                                AND  SCRSYLN_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYLO"
                    deleteCat = """DELETE FROM SCRSYLO
                               WHERE  SCRSYLO_SUBJ_CODE = ?
                                AND  SCRSYLO_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYRM"
                    deleteCat = """DELETE FROM SCRSYRM
                               WHERE  SCRSYRM_SUBJ_CODE = ?
                                AND  SCRSYRM_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCRSYTR"
                    deleteCat = """DELETE FROM SCRSYTR
                               WHERE  SCRSYTR_SUBJ_CODE = ?
                                AND  SCRSYTR_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                    deleteTab = "SCBDESC"
                    deleteCat = """DELETE FROM SCBDESC
                               WHERE  SCBDESC_SUBJ_CODE = ?
                                AND  SCBDESC_CRSE_NUMB = ? """
                    delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                    connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)
                    if (!sectionExists) {
                        deleteTab = "SCBCRKY"
                        deleteCat = """DELETE FROM SCBCRKY
                               WHERE  SCBCRKY_SUBJ_CODE = ?
                                AND  SCBCRKY_CRSE_NUMB = ? """
                        delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                        connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                        deleteTab = "SCRLEVL"
                        deleteCat = """DELETE FROM SCRLEVL
                                                  WHERE  SCRLEVL_SUBJ_CODE = ?
                                                   AND  SCRLEVL_CRSE_NUMB = ?  """
                        delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                        connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                        deleteTab = "SCRGMOD"
                        deleteCat = """DELETE FROM SCRGMOD
                               WHERE  SCRGMOD_SUBJ_CODE = ?
                                AND  SCRGMOD_CRSE_NUMB = ?  """
                        delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                        connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                        deleteTab = "SCRSCHD"
                        deleteCat = """DELETE FROM SCRSCHD
                               WHERE  SCRSCHD_SUBJ_CODE = ?
                                AND  SCRSCHD_CRSE_NUMB = ?   """
                        delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                        connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)

                        deleteTab = "SCBCRSE"
                        deleteCat = """DELETE FROM SCBCRSE
                               WHERE  SCBCRSE_SUBJ_CODE = ?
                                AND  SCBCRSE_CRSE_NUMB = ? """
                        delRows = conn.executeUpdate(deleteCat, [this.scbcrse_subj_code, this.scbcrse_crse_numb])
                        connectInfo.tableUpdate(deleteTab, 0, 0, 0, 0, delRows)
                        findCatRow = null
                    }

                }
                findTerm = null
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem executing delete for table ${deleteTab} from CatalogDML.groovy: $e.message"
                    println "${deleteCat}"
                }
            }

        }
        // update the course record
        if (findCatRow) {
            createCrky(this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term)

            try {

                String crseAPI = "{call sb_course.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(crseAPI)

                insertCall.setString(1, this.scbcrse_subj_code)
                insertCall.setString(2, this.scbcrse_crse_numb)
                insertCall.setString(3, this.scbcrse_eff_term)
                insertCall.setString(4, this.scbcrse_coll_code)
                insertCall.setString(5, this.scbcrse_divs_code)
                insertCall.setString(6, this.scbcrse_dept_code)
                insertCall.setString(7, this.scbcrse_csta_code)
                insertCall.setString(8, this.scbcrse_title)
                insertCall.setString(9, this.scbcrse_cipc_code)

                insertCall.setString(10, this.scbcrse_credit_hr_ind)
                if ((this.scbcrse_credit_hr_low == null) || (this.scbcrse_credit_hr_low == " ")
                        || (!this.scbcrse_credit_hr_low)) {insertCall.setNull(11, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(11, this.scbcrse_credit_hr_low.toDouble()) }

                if ((this.scbcrse_credit_hr_high == null) || (this.scbcrse_credit_hr_high == " ")
                        || (!this.scbcrse_credit_hr_high)) {insertCall.setNull(12, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(12, this.scbcrse_credit_hr_high.toDouble()) }

                insertCall.setString(13, this.scbcrse_lec_hr_ind)
                if ((this.scbcrse_lec_hr_low == null) || (this.scbcrse_lec_hr_low == " ")
                        || (!this.scbcrse_lec_hr_low)) {insertCall.setNull(14, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(14, this.scbcrse_lec_hr_low.toDouble()) }
                if ((this.scbcrse_lec_hr_high == null) || (this.scbcrse_lec_hr_high == " ")
                        || (!this.scbcrse_lec_hr_high)) {insertCall.setNull(15, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(15, this.scbcrse_lec_hr_high.toDouble()) }

                insertCall.setString(16, this.scbcrse_lab_hr_ind)
                if ((this.scbcrse_lab_hr_low == null) || (this.scbcrse_lab_hr_low == " ")
                        || (!this.scbcrse_lab_hr_low)) {
                    insertCall.setNull(17, java.sql.Types.DOUBLE)
                }
                else { insertCall.setDouble(17, this.scbcrse_lab_hr_low.toDouble()) }
                if ((this.scbcrse_lab_hr_high == null) || (this.scbcrse_lab_hr_high == " ")
                        || (!this.scbcrse_lab_hr_high)) {insertCall.setNull(18, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(18, this.scbcrse_lab_hr_high.toDouble()) }

                insertCall.setString(19, this.scbcrse_oth_hr_ind)
                if ((this.scbcrse_oth_hr_low == null) || (this.scbcrse_oth_hr_low == " ")
                        || (!this.scbcrse_oth_hr_low)) {insertCall.setNull(20, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(20, this.scbcrse_oth_hr_low.toDouble()) }
                if ((this.scbcrse_oth_hr_high == null) || (this.scbcrse_oth_hr_high == " ")
                        || (!this.scbcrse_oth_hr_high)) {insertCall.setNull(21, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(21, this.scbcrse_oth_hr_high.toDouble()) }

                insertCall.setString(22, this.scbcrse_bill_hr_ind)
                if ((this.scbcrse_bill_hr_low == null) || (this.scbcrse_bill_hr_low == " ")
                        || (!this.scbcrse_bill_hr_low)) {insertCall.setNull(23, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(23, this.scbcrse_bill_hr_low.toDouble()) }
                if ((this.scbcrse_bill_hr_high == null) || (this.scbcrse_bill_hr_high == null)
                        || (!this.scbcrse_bill_hr_high)) {insertCall.setNull(24, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(24, this.scbcrse_bill_hr_high.toDouble()) }
                insertCall.setString(25, this.scbcrse_aprv_code)
                if ((this.scbcrse_repeat_limit == null) || (this.scbcrse_repeat_limit == " ")
                        || (!this.scbcrse_repeat_limit)) {insertCall.setNull(26, java.sql.Types.NUMERIC) }
                else { insertCall.setInt(26, this.scbcrse_repeat_limit.toInteger())}
                insertCall.setString(27, this.scbcrse_pwav_code)
                insertCall.setString(28, this.scbcrse_tuiw_ind)
                insertCall.setString(29, this.scbcrse_add_fees_ind)
                if ((this.scbcrse_cont_hr_low == null) || (this.scbcrse_cont_hr_low == " ")
                        || (!this.scbcrse_cont_hr_low)) {insertCall.setNull(30, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(30, this.scbcrse_cont_hr_low.toDouble()) }
                if ((this.scbcrse_cont_hr_high == null) || (this.scbcrse_cont_hr_high == " ")
                        || (!this.scbcrse_cont_hr_high)) {insertCall.setNull(32, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(32, this.scbcrse_cont_hr_high.toDouble()) }
                insertCall.setString(31, this.scbcrse_cont_hr_ind)

                if (!this.scbcrse_ceu_ind) insertCall.setString(33, "N")
                else insertCall.setString(33, this.scbcrse_ceu_ind)
                insertCall.setString(34, this.scbcrse_reps_code)
                if ((this.scbcrse_max_rpt_units == null) || (this.scbcrse_max_rpt_units == " ")
                        || (!this.scbcrse_max_rpt_units)) {insertCall.setNull(35, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(35, this.scbcrse_max_rpt_units.toDouble())}

                insertCall.setString(36, this.scbcrse_capp_prereq_test_ind)
                insertCall.setString(37, this.scbcrse_dunt_code)
                if ((this.scbcrse_number_of_units == null) || (this.scbcrse_number_of_units == " ")
                        || (!this.scbcrse_number_of_units)) {insertCall.setNull(38, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(38, this.scbcrse_number_of_units.toDouble()) }


                insertCall.setString(39, connectInfo.dataOrigin)
                insertCall.setString(40, connectInfo.userID)
                // disable setRowId for now
                /// insertCall.setRowId(41,findCatRow)
                if (!this.scbcrse_prereq_chk_method_cde) insertCall.setString(41, "B")
                else insertCall.setString(41, this.scbcrse_prereq_chk_method_cde)
                insertCall.setNull(42, java.sql.Types.ROWID)

                if (connectInfo.debugThis == "Y") {
                    println "Update Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                }
                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCBCRSE", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Update Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                        println "Problem executing update for table SCBCRSE from CatalogDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Update Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                    println "Problem setting up update for table SCBCRSE from CatalogDML.groovy: $e.message"
                    //  e.printStackTrace()
                }
            }

        }
        // insert new catalog using the api sb_course
        //  cannot insert if a record exists in the future
        if ((!findCatRow) && (!findTerm)) {
            createCrky(this.scbcrse_subj_code, this.scbcrse_crse_numb, this.scbcrse_eff_term)

            try {

                String crseAPI = "{call sb_course.p_create(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
                CallableStatement insertCall = this.connectCall.prepareCall(crseAPI)

                insertCall.setString(1, this.scbcrse_subj_code)
                insertCall.setString(2, this.scbcrse_crse_numb)
                insertCall.setString(3, this.scbcrse_eff_term)
                insertCall.setString(4, this.scbcrse_coll_code)
                insertCall.setString(5, this.scbcrse_divs_code)
                insertCall.setString(6, this.scbcrse_dept_code)
                insertCall.setString(7, this.scbcrse_csta_code)
                insertCall.setString(8, this.scbcrse_title)
                insertCall.setString(9, this.scbcrse_cipc_code)

                insertCall.setString(10, this.scbcrse_credit_hr_ind)
                if ((this.scbcrse_credit_hr_low == null) || (this.scbcrse_credit_hr_low == " ")
                        || (!this.scbcrse_credit_hr_low)) {insertCall.setNull(11, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(11, this.scbcrse_credit_hr_low.toDouble()) }

                if ((this.scbcrse_credit_hr_high == null) || (this.scbcrse_credit_hr_high == " ")
                        || (!this.scbcrse_credit_hr_high)) {insertCall.setNull(12, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(12, this.scbcrse_credit_hr_high.toDouble()) }

                insertCall.setString(13, this.scbcrse_lec_hr_ind)
                if ((this.scbcrse_lec_hr_low == null) || (this.scbcrse_lec_hr_low == " ")
                        || (!this.scbcrse_lec_hr_low)) {insertCall.setNull(14, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(14, this.scbcrse_lec_hr_low.toDouble()) }
                if ((this.scbcrse_lec_hr_high == null) || (this.scbcrse_lec_hr_high == " ")
                        || (!this.scbcrse_lec_hr_high)) {insertCall.setNull(15, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(15, this.scbcrse_lec_hr_high.toDouble()) }

                insertCall.setString(16, this.scbcrse_lab_hr_ind)
                if ((this.scbcrse_lab_hr_low == null) || (this.scbcrse_lab_hr_low == " ")
                        || (!this.scbcrse_lab_hr_low)) {insertCall.setNull(17, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(17, this.scbcrse_lab_hr_low.toDouble()) }
                if ((this.scbcrse_lab_hr_high == null) || (this.scbcrse_lab_hr_high == " ")
                        || (!this.scbcrse_lab_hr_high)) {insertCall.setNull(18, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(18, this.scbcrse_lab_hr_high.toDouble()) }

                insertCall.setString(19, this.scbcrse_oth_hr_ind)
                if ((this.scbcrse_oth_hr_low == null) || (this.scbcrse_oth_hr_low == " ")
                        || (!this.scbcrse_oth_hr_low)) {insertCall.setNull(20, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(20, this.scbcrse_oth_hr_low.toDouble()) }
                if ((this.scbcrse_oth_hr_high == null) || (this.scbcrse_oth_hr_high == " ")
                        || (!this.scbcrse_oth_hr_high)) {insertCall.setNull(21, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(21, this.scbcrse_oth_hr_high.toDouble()) }

                insertCall.setString(22, this.scbcrse_bill_hr_ind)
                if ((this.scbcrse_bill_hr_low == null) || (this.scbcrse_bill_hr_low == " ")
                        || (!this.scbcrse_bill_hr_low)) {insertCall.setNull(23, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(23, this.scbcrse_bill_hr_low.toDouble()) }
                if ((this.scbcrse_bill_hr_high == null) || (this.scbcrse_bill_hr_high == null)
                        || (!this.scbcrse_bill_hr_high)) {insertCall.setNull(24, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(24, this.scbcrse_bill_hr_high.toDouble()) }
                insertCall.setString(25, this.scbcrse_aprv_code)
                if ((this.scbcrse_repeat_limit == null) || (this.scbcrse_repeat_limit == " ")
                        || (!this.scbcrse_repeat_limit)) {insertCall.setNull(26, java.sql.Types.NUMERIC) }
                else { insertCall.setInt(26, this.scbcrse_repeat_limit.toInteger())}
                insertCall.setString(27, this.scbcrse_pwav_code)
                insertCall.setString(28, this.scbcrse_tuiw_ind)
                insertCall.setString(29, this.scbcrse_add_fees_ind)
                if ((this.scbcrse_cont_hr_low == null) || (this.scbcrse_cont_hr_low == " ")
                        || (!this.scbcrse_cont_hr_low)) {insertCall.setNull(30, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(30, this.scbcrse_cont_hr_low.toDouble()) }
                if ((this.scbcrse_cont_hr_high == null) || (this.scbcrse_cont_hr_high == " ")
                        || (!this.scbcrse_cont_hr_high)) {insertCall.setNull(32, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(32, this.scbcrse_cont_hr_high.toDouble()) }
                insertCall.setString(31, this.scbcrse_cont_hr_ind)

                if (!this.scbcrse_ceu_ind) insertCall.setString(33, "N")
                else insertCall.setString(33, this.scbcrse_ceu_ind)
                insertCall.setString(34, this.scbcrse_reps_code)
                if ((this.scbcrse_max_rpt_units == null) || (this.scbcrse_max_rpt_units == " ")
                        || (!this.scbcrse_max_rpt_units)) {insertCall.setNull(35, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(35, this.scbcrse_max_rpt_units.toDouble())}

                insertCall.setString(36, this.scbcrse_capp_prereq_test_ind)
                insertCall.setString(37, this.scbcrse_dunt_code)
                if ((this.scbcrse_number_of_units == null) || (this.scbcrse_number_of_units == " ")
                        || (!this.scbcrse_number_of_units)) {insertCall.setNull(38, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(38, this.scbcrse_number_of_units.toDouble()) }

                def findLevl = ""
                findCat = """select  'Y' scrlevl_rowid from scrlevl
                where scrlevl_eff_term = ?
                and scrlevl_subj_code = ?
                and scrlevl_levl_code = ?
                and scrlevl_crse_numb = ? """
                if ((this.scrlevl_levl_code1 == null) || (this.scrlevl_levl_code1 == " ")
                        || (!this.scrlevl_levl_code1)) {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code, "00",
                                 this.scbcrse_crse_numb]) {row ->
                        findLevl = row.scrlevl_rowid
                    }
                }
                else {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code,
                                 this.scrlevl_levl_code1, this.scbcrse_crse_numb]) {row ->
                        findLevl = row.scrlevl_rowid
                    }
                }

                if (!findLevl) {
                    if ((this.scrlevl_levl_code1 == null) || (this.scrlevl_levl_code1 == " ")
                            || (!this.scrlevl_levl_code1)) {
                        insertCall.setString(39, "00")
                        insertCall.setString(33, "N")
                    }
                    else { insertCall.setString(39, this.scrlevl_levl_code1) }
                }
                else insertCall.setNull(39, java.sql.Types.VARCHAR)

                def findGmod = ""
                findCat = """select  'Y' scrgmod_rowid from scrgmod
                where scrgmod_eff_term = ?
                and scrgmod_subj_code = ?
                and scrgmod_gmod_code = ?
                and scrgmod_crse_numb = ? """
                if (this.scrgmod_gmod_code) {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code,
                                 this.scrgmod_gmod_code, this.scbcrse_crse_numb]) {row ->
                        findGmod = row.scrgmod_rowid
                    }
                }
                else {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code,
                                 "S", this.scbcrse_crse_numb]) {row ->
                        findGmod = row.scrgmod_rowid
                    }
                }
                if (!findGmod) {
                    if (this.scrgmod_gmod_code)
                        insertCall.setString(40, this.scrgmod_gmod_code)
                    else insertCall.setString(40, "S")
                }
                else insertCall.setNull(40, java.sql.Types.VARCHAR)

                def findSchd = ""
                findCat = """select  'Y' scrschd_rowid from scrschd
                where scrschd_eff_term = ?
                and scrschd_subj_code = ?
                and scrschd_schd_code = ?
                and scrschd_crse_numb = ? """
                if (this.scrschd_schd_code) {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code,
                                 this.scrschd_schd_code, this.scbcrse_crse_numb]) {row ->
                        findSchd = row.scrschd_rowid
                    }
                }
                else {
                    conn.eachRow(findCat, [this.scbcrse_eff_term, this.scbcrse_subj_code,
                                 "L", this.scbcrse_crse_numb]) {row ->
                        findSchd = row.scrschd_rowid
                    }
                }
                if (!findSchd) {
                    if (this.scrschd_schd_code) insertCall.setString(41, this.scrschd_schd_code)
                    else insertCall.setString(41, "L")
                }
                else insertCall.setNull(41, java.sql.Types.VARCHAR)

                if ((this.scrschd_workload == null) || (this.scrschd_workload == " ")
                        || (!this.scrschd_workload)) {insertCall.setNull(42, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(42, this.scrschd_workload.toDouble())}
                if ((this.scrscdh_max_enrl == null) || (this.scrscdh_max_enrl == " ")
                        || (!this.scrscdh_max_enrl)) {insertCall.setNull(43, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(43, this.scrscdh_max_enrl.toDouble())}
                if ((this.scrschd_adj_workload == null) || (this.scrschd_adj_workload == " ")
                        || (!this.scrschd_adj_workload)) {insertCall.setNull(44, java.sql.Types.DOUBLE) }
                else { insertCall.setDouble(44, this.scrschd_adj_workload.toDouble())}
                insertCall.setNull(45, java.sql.Types.VARCHAR)
                insertCall.setString(46, connectInfo.dataOrigin)
                insertCall.setString(47, connectInfo.userID)
                if (!this.scbcrse_prereq_chk_method_cde) insertCall.setString(48, "B")
                else insertCall.setString(48, this.scbcrse_prereq_chk_method_cde)
                insertCall.registerOutParameter(49, java.sql.Types.ROWID)
                if (connectInfo.debugThis) { println "Insert Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}" }

                try {
                    insertCall.executeUpdate()
                    connectInfo.tableUpdate("SCBCRSE", 0, 1, 0, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "Insert Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                        println "Problem executing insert for table SCBCRSE from CatalogDML.groovy: $e.message"
                    }
                }
                finally {
                    insertCall.close()
                }
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCBCRSE", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "Insert Course ${this.scbcrse_subj_code} ${this.scbcrse_crse_numb} ${this.scbcrse_eff_term}"
                    println "Problem setting up insert for table SCBCRSE from CatalogDML.groovy: $e.message"
                }
            }
        }
    }


    def createCrky(String subj, String crse, String term) {
        String findCat = null
        def maxTerm = null
        java.sql.RowId findCrkyRow = null
        findCat = """select  rowid scbcrky_rowid, scbcrky_term_code_start term  from scbcrky
              where  scbcrky_subj_code = ?
              and scbcrky_crse_numb = ? """
        try {
            conn.eachRow(findCat, [subj, crse]) {row ->
                findCrkyRow = row.scbcrky_rowid
                maxTerm = row.term
            }
        }
        catch (Exception e) {
            connectInfo.tableUpdate("SCBCRKY", 0, 0, 0, 1, 0)
            if (connectInfo.showErrors) {
                println "${findCat}"
                println "Problem executing rowid select for table SCBCRSE ${subj} ${crse} ${term} from CatalogDML.groovy: $e.message"
            }
        }

        if (!findCrkyRow) {
            findCat = """ insert into scbcrky (scbcrky_subj_code, scbcrky_crse_numb,
                scbcrky_term_code_start,
                scbcrky_term_code_end, scbcrky_activity_date)
                values ('${subj}', '${crse}',
                    '${term}','999999',sysdate  )
              """
            try {
                conn.execute findCat
                connectInfo.tableUpdate("SCBCRKY", 0, 1, 0, 0, 0)
            }
            catch (Exception e) {
                connectInfo.tableUpdate("SCBCRKY", 0, 0, 0, 1, 0)
                if (connectInfo.showErrors) {
                    println "${findCat}"
                    println "Problem executing insert for table SCBCRKY ${subj} ${crse} ${term} from CatalogDML.groovy: $e.message"
                }
            }
        }
        else {
            if (maxTerm > term) {
                findCat = """ update  scbcrky
                   set scbcrky_term_code_start = ?
                   where scbcrky_subj_code = ? and
                   scbcrky_crse_numb = ?
              """
                try {
                    conn.execute(findCat, [term, subj, crse])
                    connectInfo.tableUpdate("SCBCRKY", 0, 0, 1, 0, 0)
                }
                catch (Exception e) {
                    connectInfo.tableUpdate("SCBCRKY", 0, 0, 0, 1, 0)
                    if (connectInfo.showErrors) {
                        println "${findCat}"
                        println "Problem executing update for table SCBCRKY ${subj} ${crse} ${term} from CatalogDML.groovy: $e.message"
                    }
                }
            }
        }
    }

}
