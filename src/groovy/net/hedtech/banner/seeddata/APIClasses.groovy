/*********************************************************************************
  Copyright 2010-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

/**
 * Class to define table object to store crud activity that is printed at the end of the process
 */
public class APIClasses {

    /**
     * To add a new class to execute the BannerAPI,  add the entry to the API map
     * The class TableDriver will will instantiate the class
     *        Class.forName(className).newInstance(connectInfo, conn, connectCall, xmlRec)
     */


    def API = [
            'SOBTERM': 'net.hedtech.banner.seeddata.TermRulesDML',
            'SPRIDEN_VIEW': 'net.hedtech.banner.seeddata.GeneralPersonXML',
            'SPRIDEN': 'net.hedtech.banner.seeddata.PersonIDDML',
            'SPRTELE': 'net.hedtech.banner.seeddata.TelephoneDML',
            'SPBPERS': 'net.hedtech.banner.seeddata.PersonBioDML',
            'SPRADDR': 'net.hedtech.banner.seeddata.AddressDML',
            'GOREMAL': 'net.hedtech.banner.seeddata.EmailDML',
            'SIBINST': 'net.hedtech.banner.seeddata.FacultyDML',
            'SIRASGN': 'net.hedtech.banner.seeddata.FacultyAssignmentsDML',
            'SSBSECT': 'net.hedtech.banner.seeddata.ScheduleDML',
            'SSRMEET': 'net.hedtech.banner.seeddata.ClassTimesDML',
            'SCBCRSE': 'net.hedtech.banner.seeddata.CatalogDML',
            'SCRRATT': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SCRRCHR': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SCRRDEP': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SCRINTG': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SCRCLBD': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SCRMEXC': 'net.hedtech.banner.seeddata.CatalogRulesDML',
            'SSRRATT': 'net.hedtech.banner.seeddata.ScheduleRulesDML',
            'SSRRCHR': 'net.hedtech.banner.seeddata.ScheduleRulesDML',
            'SSRRDEP': 'net.hedtech.banner.seeddata.ScheduleRulesDML',
            'SSRCLBD': 'net.hedtech.banner.seeddata.ScheduleRulesDML',
            'SSBWLSC': 'net.hedtech.banner.seeddata.ScheduleRulesDML',
            'SGBSTDN': 'net.hedtech.banner.seeddata.GeneralStudentDML',
            'SORLCUR': 'net.hedtech.banner.seeddata.ConcurrentCurriculumDML',
            'SORLFOS': 'net.hedtech.banner.seeddata.FieldOfStudyDML',
            'SHRDGMR': 'net.hedtech.banner.seeddata.HistoryOutcomeDML',
            'SARADAP': 'net.hedtech.banner.seeddata.AdmissionsApplicationDML',
            'SRBRECR': 'net.hedtech.banner.seeddata.RecruitDML',
            'STUDENT_SPRIDEN': 'net.hedtech.banner.seeddata.StudentPersonIDDML',
            'FACULTY_SPRIDEN': 'net.hedtech.banner.seeddata.FacultyPersonIDDML',
            'ADVISING_SPRIDEN': 'net.hedtech.banner.seeddata.AdvisingPersonIDDML',
            'CURRICULUMBACK': 'net.hedtech.banner.seeddata.CurriculumBacklogDML',
            'SOBCURR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORMCRL': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCMNR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCCON': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCMJR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SARWAPC' : 'net.hedtech.banner.seeddata.SoacurrDML',
            'SARWADF' : 'net.hedtech.banner.seeddata.SoacurrDML',
            'SFRSTCR': 'net.hedtech.banner.seeddata.SfrstcrDML',
            'GOBTPAC': 'net.hedtech.banner.seeddata.GobtpacDML',
            'SSUSER_SPRIDEN': 'net.hedtech.banner.seeddata.SSUserPersonIDDML',
            'GOBTPAC_RECORD': 'net.hedtech.banner.seeddata.ThirdPartyAccessPinDML',
            'GENIDEN': 'net.hedtech.banner.seeddata.ProxyAccessCredentialInformationDML',
            'GERATTD': 'net.hedtech.banner.seeddata.EventRegistrantDML',
            'GERFGST': 'net.hedtech.banner.seeddata.EventGuestDML',
            'GOBQSTN': 'net.hedtech.banner.seeddata.PinQuestionDML',
            'GOBANSR': 'net.hedtech.banner.seeddata.PersonPinResponseDML',
            'EVENT_SSB_AVAILABLE': 'net.hedtech.banner.seeddata.EventSSBAvailabilityDML',
            'SHRINCG': 'net.hedtech.banner.seeddata.IncompleteGradesDML',
            'SSBSECT_DELETE': 'net.hedtech.banner.seeddata.ScheduleTermDeleteDML',
            'CATALOG_DELETE': 'net.hedtech.banner.seeddata.CatalogDeleteDML' ,
            'SORATMT' : 'net.hedtech.banner.seeddata.SoratmtDML' ,
            'SFRBRDB': 'net.hedtech.banner.seeddata.RegistrationGroupRuleDetailDML',
            'SFRBRDH': 'net.hedtech.banner.seeddata.RegistrationGroupRuleDML',
            'SORSATR' : 'net.hedtech.banner.seeddata.SorsatrDML'  ,
            'SFRBSEL' : 'net.hedtech.banner.seeddata.RegistrationSelectedBlockDML',
            'SFRBLPA' : 'net.hedtech.banner.seeddata.RegistrationBlockPreAssignmentDML',
            'RECRUIT_SPRIDEN' : 'net.hedtech.banner.seeddata.RecruitPersonDML',
            'SHRGCOM' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRSCOM' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRMRKS' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRSMRK' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SGRADVR' : 'net.hedtech.banner.seeddata.SgradvrDML',
            'SORNOTE' : 'net.hedtech.banner.seeddata.SornoteDML',
            'FLEXREGFEE' : 'net.hedtech.banner.seeddata.FlexRegFeeDML',
            'SMBPGEN' : 'net.hedtech.banner.seeddata.CappProgramRequirementDML',
            'STVGCHG' : 'net.hedtech.banner.seeddata.GradeChangeReasonDML',
            'GCRFLDR' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCRFLDR' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRCFLD' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBQURY' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBTMPL' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBEMTL' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GLBEXTR' : 'net.hedtech.banner.seeddata.GlbextrDML',
            'GCBACTM' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCVASTS' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRAACT' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRACNT' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCBAGRP' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCBPBTR' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRAISR' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'FINANCEFISCAYEAR' :'net.hedtech.banner.seeddata.FinanceProcurementFiscalYearDML',
            'FINANCEUSER'      :'net.hedtech.banner.seeddata.FinanceProcurementUserDML',
            'UPDATEFOBPROF'    : 'net.hedtech.banner.seeddata.FinanceProcurementUserProfileUpdateDML',
            'UPDATEFOBSYSC'    : 'net.hedtech.banner.seeddata.FinanceSystemControlDML',
            'FPBREQH'          : 'net.hedtech.banner.seeddata.FinanceProcurementHeaderCreateDML',
            'FOAPAL_ORGANIZATIONS': 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityFoapalOrganizationDML',
            'FOAPAL_ACCUNT'       : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityFoapalAccountDML',
            'FOAPAL_PROGRAMS'     : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityFoapalProgramDML',
            'FOAPAL_LOCATION'     : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityFoapalLocationDML',
            'FOAPAL_ACTIVITY'     : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityFoapalActivityDML',
            'BA_VENDORS'          : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityVendorDML',
            'BA_JOUNRAL_VOUCHER'  : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityCreateBudgetJournalVoucherDML',
            'PURCHASE_REQUISITION': 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityCreatePurchaseRequisitionsDML',
            'PURCHASE_ORDER'      : 'net.hedtech.banner.seeddata.FinanceBudgetAvailabilityCreatePurchaseOrderDML',
            'BA_QUERY_PROTOTYPE'  : 'net.hedtech.banner.seeddata.FinanceBudgetQueryProtoTypesDML',
            'BA_FORCOMP'          : 'net.hedtech.banner.seeddata.FinanceBudgetComputedColumnsDML',
            'TBBCSTU'          : 'net.hedtech.banner.seeddata.TbbcstuDML',
            'PEBEMPL'          : 'net.hedtech.banner.seeddata.EmployeePersonIDDML',
            'PEBEMPLEAVEBYJOB' : 'net.hedtech.banner.seeddata.EmployeeLeaveByJobPersonIDDML',
            'PDRBDED'          : 'net.hedtech.banner.seeddata.EmployeeBenefitsIDDML',
            'PDRDEDN'          : 'net.hedtech.banner.seeddata.EmployeeBenefitsDetailDML',
            'PERDHIS'          : 'net.hedtech.banner.seeddata.PerdhisDML',
            'PERJHIS'          : 'net.hedtech.banner.seeddata.PerjhisDML',
            'NBBPOSN'          : 'net.hedtech.banner.seeddata.NbbposnDML',
            'NBRPTOT'          : 'net.hedtech.banner.seeddata.NbrptotDML',
            'NBRBJOB'          : 'net.hedtech.banner.seeddata.EmployeeJobAssignmentDML',
            'NBRJOBS'          : 'net.hedtech.banner.seeddata.EmployeeJobAssignmentDetailDML',
            'NBREARN'          : 'net.hedtech.banner.seeddata.EmployeeJobEarningsDML',
            'BANNERUSER'       : 'net.hedtech.banner.seeddata.BannerUserDML',
            'HRGOBEACCUSER'      : 'net.hedtech.banner.seeddata.HrGobeaccDML',
            'ORACLEUSER'      : 'net.hedtech.banner.seeddata.CreateOracleUserDML',
            'NTRLRAQ'          : 'net.hedtech.banner.seeddata.NtrlraqDML'

    ]
    // map of tables that require both an api class but also will update / insert via the dynamic sql table process
    def both = [
            'SOBCURR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORMCRL': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCMNR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCCON': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SORCMJR': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SARWAPC': 'net.hedtech.banner.seeddata.SoacurrDML',
            'SARWADF' : 'net.hedtech.banner.seeddata.SoacurrDML',
            'GOBTPAC': 'net.hedtech.banner.seeddata.GobtpacDML',
            'SFRSTCR': 'net.hedtech.banner.seeddata.SfrstcrDML',
            'SORATMT' : 'net.hedtech.banner.seeddata.SoratmtDML',
            'SORSATR' : 'net.hedtech.banner.seeddata.SorsatrDML',
            'SHRGCOM' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRSCOM' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRMRKS' : 'net.hedtech.banner.seeddata.ShagcomDML',
            'SHRSMRK' : 'net.hedtech.banner.seeddata.ShagcomDML' ,
            'SGRADVR' : 'net.hedtech.banner.seeddata.SgradvrDML',
            'SORNOTE' : 'net.hedtech.banner.seeddata.SornoteDML',
            'SMBPGEN' : 'net.hedtech.banner.seeddata.CappProgramRequirementDML',
            'GCRFLDR' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCRCFLD' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBQURY' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBTMPL' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GCBEMTL' : 'net.hedtech.banner.seeddata.GcrfldrDML',
            'GLBEXTR' : 'net.hedtech.banner.seeddata.GlbextrDML',
            'GCBACTM' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCVASTS' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRAACT' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRACNT' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCBAGRP' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCBPBTR' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'GCRAISR' : 'net.hedtech.banner.seeddata.GeneralActionItemDML',
            'TBBCSTU' : 'net.hedtech.banner.seeddata.TbbcstuDML',
            'PERDHIS' : 'net.hedtech.banner.seeddata.PerdhisDML',
            'NBBPOSN' : 'net.hedtech.banner.seeddata.NbbposnDML',
            'NBRPTOT' : 'net.hedtech.banner.seeddata.NbrptotDML',
            'PERJHIS' : 'net.hedtech.banner.seeddata.PerjhisDML',
            'NTRLRAQ' : 'net.hedtech.banner.seeddata.NtrlraqDML'
    ]


    def findClass(tableName) {
        def apiMap = API.find { it.key == tableName}
        return apiMap?.value
    }


    def isBoth(tableName) {
        def apiMap = both.find { it.key == tableName}
        return apiMap?.value
    }


}
