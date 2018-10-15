/*********************************************************************************
 Copyright 2010-2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import grails.util.GrailsUtil
import groovy.sql.Sql

/**
 * Prompts user via the command line for input data needed to load seed data.
 * */
public class InputData {

    // Seed data files
    def xmlFile
    def xmlControlFile
    String batchSeed
    String baseDirectory
    def xmlFilePath

    // Database configuration. Note: if dataSource is available, the remaining database configuration fields are not used
    def dataSource = null
    def username
    def password
    def url
    def hostname
    def instance
    def userID = "GRAILS"
    def dataOrigin = "GRAILS"

    /**
     * This value triggers getting a connection using bansecr not banproxy
     */
    def mepUserId = false

    def saveThis = true
    def debugThis = false
    def showErrors = true
    def replaceData = true
    def sqlTrace = false

    def yyyy
    def mm
    def day

    def tableCnts = []
    def tableSize = 0
    def totalErrors = 0

    List prompts
    // save the PIDM value for the general person view processing
    def savePidm = null
    def saveAdvisingPidm = null
    def saveStudentPidm = null
    def saveLcurSeqno = null
    def saveSeqno = null
    def saveCurrRule = null
    def tableName = null
    def owner = null

    def validTable = null

    // map of run time targets to specify file names
    def targets = [
            'seed-cleanup'                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSeedDelete.xml',
                                                      '/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTermDelete.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ValidationDataCleanup.xml'],
            'api-extensibility'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilityConfiguration.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySqlProcessRules.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySqlValidation.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySampleSdeSetup.xml'],
            'catalog'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/genpersonValidationXML.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CatalogValidationSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StudentValidation.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CatalogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CatalogDataGradeModeWriting103.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20009.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20116.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20441.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleStructureData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ScrlevlData.xml'],
            'curriculumdefault'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/SobctrlDefaultData.xml'],
            'employee'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/general/GtvemalData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/GtvdicdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/StvteleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/StvatypData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/GordmclData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/GordmskData.xml',
                                                  // Finance data
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvfsyrData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvfspdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvobudData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvobphData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALFund.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALOrganization.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALAccount.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALProgram.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALActivity.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALLocation.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrant.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrantPersonnel.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrantEffort.xml',
                                                  // HR Validation
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvacatData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtveccgData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvecgrData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvqprtData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvwkshData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvecipData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtveeogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvegrpData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvesklData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvshcdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvlcatData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvjctyData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvlgcdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvpcatData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvrqstData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/Ptv1099Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvbdtyData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvorgnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvcdesData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/HrDirectDepositValidationData.xml',
                                                  // HR Rules
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtralvlData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrqprtData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrqgrpData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrecdqData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/SalaryGroupData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrinstData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrfiniData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrrqueData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrwkshData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbcatData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtremprData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrpictData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrcalnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrdfprData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreclsData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreclcData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrearnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreernData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecbcData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreholData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrjblnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrjcreData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrleavData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvasData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrtreaData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrshftData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrteshData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrwkprData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtresocData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrinstData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvacData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvprData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrwstbData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdclData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PxrtxcdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdcaData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbcdnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdpdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdplData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdcgData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdidData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdxdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdxeData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdldData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrpclsData.xml',
                                                  // Position class and Posn budget
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NbbfiscData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/PositionData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/PositionHistoryData.xml',
                                                  // Employee Information
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/HRBannerUser.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralUsers.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeBasicData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/EmployeeJobData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/EmployeeJobHistoryData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeDeductionData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeDeductionHistoryData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeLeaveData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeLeaveHistoryData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeHrAccessData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeRoleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/HrAdminUserData.xml',
                                                  // Employee History
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PeretotData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerjtotData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerdtotData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrhistData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhraccrData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrjacrData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrjobsData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrearnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrelbdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrdednData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrdocmData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerjobsData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerlvtkData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/Pxrw2fdData.xml',
                                                  // ERLR
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrlraqData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecrcData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvecpdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecpdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecpcData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecerData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecprData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrtData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecstData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecsnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecscData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecsiData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecalData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecfdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrececData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrqData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrsData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecdtData.xml',
                                                  // Time Entry
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeTimeEntryExtractData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PerearnData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PerhourData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PertitoData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PerelbdData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PerlvtkData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeTimeEntryMessageData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PprhnawData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrcalnLeaveRequestData.xml',
            ],
            'functionaltest'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/functional_catsch_testdata.xml'],
            'curriculum-validation'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumValidationData.xml'],
            'catalog-selenium'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSelenium.xml'],
            'sde'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/SdeData.xml'],
            'extensibility'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData2.xml'],
            //TODO Remove SMBPGEN seed for program BA-ECON once it gets added to vagrant
            'program-data'                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/ProgramData.xml'],
            'generalstudent'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApplicantData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApplicantQuickEntryRule.xml'],
            'registration-rule'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationManagementControl.xml'],
            'selfserviceuser'                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceUserData.xml','/src/groovy/net/hedtech/banner/seeddata/Data/surveyflow.xml'],
            'schedule-selenium'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleSelenium.xml'],
            'courseDetail-selenium'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseDetailSelenium.xml'],
            'courseRestriction-selenium'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRestrictionSelenium.xml'],
            'mep-data'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/mepdata.xml'],
            'catalog-additional'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogAdditionalSelenium.xml'],
            'schedule-additional'              : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleAdditionalSelenium.xml'],
            'cat-sch-readonly'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogScheduleQueryOnlyPagesSelenium.xml'],
            'event'                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventValidationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/EventRoomAndBuilding.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/EventPerson.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/EventData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/EventSdeData.xml'],
            'registration-waitlist'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitList.xml'],
            'registration-waitlist-remove'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitListRemove.xml'],
            'registration-waitlist-seed'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeed.xml'],
            'fge'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceValidationSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceTermSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceCatalogSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceCatalogSeedSelenium.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_201110.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203020.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203030.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeedSelenium_204010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceFacultySeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201110.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203020.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203030.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeedSelenium_204010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_201110.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_203010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_203020.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeedSelenium_204010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_201110.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_203010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_203020.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedSelenium_204010.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/FgeEgbEnhancement.xml'],
            'egb'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/EGBTermData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/EGBScheduleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/EGBData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/EGBComponentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/EGBStudentData.xml'],
            'ssbadvisor'                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed3.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed3.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSearchSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed3.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorBannerCourse.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorCourseSection.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorRegistration.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPriorEducationAndTesting.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorFacultyAccess.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorNotes.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorHolds.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeGradeSeed_201410.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonNameDisplay.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorHierarchyNameDisplay.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSecuritySeed1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSecuritySeed2.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/ProxyAccessSystemValidationGTVSYST.xml'],
            'aip'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPTerm.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsers.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPCurriculum.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity3.xml'],

            'registration-general'             : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationGeneral.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentCentricPeriod.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationCourseRegistrationDropStatus.xml'],
            'ssbgeneralstudent'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            //TODO Need to revisit; this is a duplicate call
            'ssbgeneralstudenthosweb001'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml'],
            'finance-validation'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvshipData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FtvbuyrData.xml'],
            'finance-procurement'              : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseOrderHeader.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToOrganization.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToFund.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceFiscalYear.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionBuyerVerification.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionHeader.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCommodity.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionAccounting.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionText.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceGeneralTickler.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserProfileUpdateData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionApprovalHistory.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionApprovalInProgress.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionUnApprovedDocument.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionPO.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceSystemControlUpdateData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceProjectCustomerCharge.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceFund.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceAccountIndex.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCurrencyCode.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCurrencyRate.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceApprovalQueueDefinition.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/VendorEmail.xml'],
            'ssbgeneralstudentblockreg'        : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlockReg.xml'],
            'blockregistration'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistration.xml'],
            'ssbgeneralstudent2'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            'ssbgeneralstudent3'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew3.xml'],
            'ssbgeneralstudentfa'              : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataFA.xml'],
            'feeSummaryData'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/FeeSummaryData.xml'],
            'feeSummaryDataRemove'             : ['/src/groovy/net/hedtech/banner/seeddata/Data/FeeSummaryDataRemove.xml'],
            'ssbgeneralstudentblock'           : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlock.xml'],
            'ssbgeneralstudentblockrule'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlockWithRule.xml'],
            'attr'                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingCumulativePercentageSetupSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingAdminRuleSetupSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingStudentAttendanceSeed8001.xml'],
            'ssb-curriculumarea-display'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/AreaLibrary_smralib.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupLibrary_smrglib.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SectionAreaRestriction_ssrrare.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupRestrictedSubjectCourseAttachmentRuleBase_smbgrul.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupSubjectCourseAttachmentRule_smrgrul.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupSubjectCourseAttributeAttachment_smrgcaa.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGeneralGroupRequirement_smbggen.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentRuleBase_smbagrl.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentRule_smragrl.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaRestrictedSubjectCourseAttachmentRuleBase_smbarul.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaSubjectCourseAttachmentRule_smrarul.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumGeneralAreaRequirement_smbagen.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaCourseAttributeAttachment_smracaa.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentAndManagement_smragam.xml'],
            'studentcurriculumdata'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentCurriculumData.xml'],
            'admissions'                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/AdmissionsValidationSeed.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RecruitData.xml'],
            'academic-history'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryScheduleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryComponentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryStudentData.xml'],
            'registration-withdrawal'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHOSR24796.xml'],
            'registration-planning'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationPlanSeed.xml'],
            'transcript-award'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentTranscriptAwardedDegree.xml'],
            'student-adv-admin-persona'        : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentAdvisorAdminPersonData.xml'],
            'flat-rate-fee-rules'              : ['/src/groovy/net/hedtech/banner/seeddata/Data/FeeAssessmentFlatRate.xml'],
            'facultySecurityPlanAhead'         : ['/src/groovy/net/hedtech/banner/seeddata/Data/FacultySecurityPageData.xml'],
            'generalstudentcapp'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithCompliance.xml'],
            'reg-rsql'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionStructuredRegSql.xml'],
            'generalstudentsd01'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithSD01Program.xml'],
            'generalstudentsd02'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithSD02Program.xml'],
            'generalstudentdynamic'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithDynamicCappArea.xml'],
            'structured-reg'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStructureHeader.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStructureDetail.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationTwo.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationThree.xml'],
            'generalstudentcappreg'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappreg01'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration01.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg02'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration02.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg03'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration03.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg04'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration04.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg05'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration05.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg06'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration06.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg07'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration07.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg15'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration15.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg16'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration16.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg17'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration17.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg18'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration18.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg19'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration19.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg20'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration20.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg21'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration21.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg22'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration22.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg23'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration23.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg24'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration24.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg25'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration25.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg26'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration26.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg27'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration27.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg28'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration28.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg29'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration29.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg30'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration30.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg31'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration31.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg33'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration33.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg36'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration36.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg37'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration37.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg38'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration38.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregKW1023'      : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationKW1023.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregKW1028'      : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationKW1028.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregbareg20'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg20.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg23'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg23.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg25'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg25.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg26'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg26.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'studentcappstructuredreg'         : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentCappStructuredRegistration.xml'],
            'studentApiData'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/ApiDeriveTerm.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiPersonMatchData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiSecurityData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/Api_GTVICSN.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/Api_GOBICPS.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiIntegrationConfigurationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiCountryValidationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiCurriculumData.xml'],
            'capp-programs'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/capp/artprogam01.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/baengllit.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/baengllitx.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/baseedsp1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/baseedsp2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/dynamicproa1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jentest01.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jimsprog.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jmcptv.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jmnotcptv.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jxcptv.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jxnocptv.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/jzcptv.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/leeds.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/capp/multiterm01.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappProgramSRPGMGroup.xml'],
            'structured-progcat'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbproo.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbareo.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbcrso.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbares.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbcrss.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbgros.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbpros.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbruls.xml'],
            'projected-reg'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermDataForProjections.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CatalogDataForProjections.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CappComplianceDataForProjections.xml'],
            'projected-reg-config'             : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionConfigData.xml'],
            'projected-reg-data'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionData.xml'],
            'projected-reg-data-remove'        : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionDataRemove.xml'],
            'student-academic-review'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewAdministrator.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewTermData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewCatalogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewScheduleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewGradeScaleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewComponentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/2Curriculum2Program1StudyPathStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/2Curriculum1Program2StudyPathsStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/1Curriculum1ProgramNoStudyPath.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/UnRolledStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentGpaHoursCampusAndLevelRule.xml'],
            'bcm'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralCommunicationData.xml'],
            'api-person-filters'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ApiPersonFiltersData.xml'],
            'admissions-student'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/AdmissionsStudentData.xml'],
            'api-hedm-registration'            : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMTermData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMProgramData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMCatalogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMScheduleData.xml'],
            'api-hedm-student'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMStudentData.xml'],
            'api-hedm-registration-grade'      : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMGradeData.xml'],
            'finance-procurement-user-creation': ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserCreationData.xml'],
            'brainstorm-registration'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormTermData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormIntegrationPartnerData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormProgramData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormCatalogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormScheduleData.xml'],
            'brainstorm-student'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormStudentData.xml'],
            'brainstorm-registration-planning' : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormPlanningData.xml'],
            'reg-ar'                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentAccountsReceivableData.xml'],
            'api-student-registration'         : ['/src/groovy/net/hedtech/banner/seeddata/Data/ApiStudentRegistrationHistoryData.xml'],
            'api-general-student'              : ['/src/groovy/net/hedtech/banner/seeddata/Data/ApiGeneralStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiStudentPriorEducationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiStudentRegistrationHistoryData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ApiGeneralData.xml'],
            'general-ledger'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralLedger.xml'],
            'general-ledger-clean'             : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerClean.xml'],
            'fiscal-period-year'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/FiscalPeriodAndYear.xml'],
            'fiscal-period-year-clean'         : ['/src/groovy/net/hedtech/banner/seeddata/Data/FiscalPeriodAndYearClean.xml'],
            'fgbtrni'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBTRNI.xml'],
            'fgbtrni-clean'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBTRNIClean.xml'],
            'fgbjvch'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBJVCH.xml'],
            'fgbjvch-clean'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBJVCHClean.xml'],
            'fgbtrnh'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBTRNH.xml'],
            'fgbtrnh-clean'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBTRNHClean.xml'],
            'fgbarcd'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBARCD.xml'],
            'fgbarcd-clean'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/FGBARCDClean.xml'],
            'general-ledger-gurfeed'           : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerGURFEED.xml'],
            'general-ledger-gurfeed-clean'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerGURFEEDClean.xml'],
            'stvcomf'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/STVCOMF_Data.xml'],
            'stvcomf-clean'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/STVCOMF_DataClean.xml'],
            'general'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/general/SprmediData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/NonPersonData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/GoradrlData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/general/GtvzipcData.xml'],
            'general-auto'                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/MDUUSecurityData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsers1.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsers2.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoNewPages.xml'],
            'gender-pronoun'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/GtvgndrData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GtvpprnData.xml'],
            'direct-deposit'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/DirectDepositUsers.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/DirectDepositValidationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/DirectDepositData.xml'],
            'personal-info'                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/PersonalInfoData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/PersonalInfoDirectoryProfileData.xml'],
            'general-common'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/GorvisaData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/SqlProcessData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralGtvsdaxData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CommonMatchingData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GtvduntData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/AuthenticationUserData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/LetterGenerationData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/TabBasedSecurity.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/WebTailorMenu.xml'],
            'program-prerequisite'             : ['/src/groovy/net/hedtech/banner/seeddata/Data/ProgramRestriction_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgramTestScoreRestrictionAndPrerequisite_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProgramAreaRestriction_Data.xml'],
            'classList'                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListTermData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListCatalogData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListScheduleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListComponentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024925.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024831.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024932.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024934.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024935.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024937.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024919.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ClassListAdministrator.xml'],
            'droproster'                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/GTVDICD.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SFRRORL.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SFRSECH.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterDropStatusData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterScheduleData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_PTUCHMAN.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_ARTHINC.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SFBDRTC.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SFBDRSC.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU01.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU02.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU03.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ReRegisterHOSH00023.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_GTVDICD.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_STVRSTS.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Term.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_Term.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Schedule.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_Schedule.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Catalog.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_SFRRORL.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_SFRRORL.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00037841.xml'],
            'academic-standing'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201763_Term.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201763_Schedule.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201764_Term.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201764_Schedule.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00010069.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00017091.xml'],
            'zero-textbook-cost'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCGoriccrCodes.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCTerm_201840.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCStudent_A00017137.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCWebUser_A00017136.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCFaculty_A00017133.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCFacultyAndAdvisor_A00017135.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCAdvisorData_A00017134.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCScheduleData.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ZTCStudent_A00017847.xml'],
			'holds-by-studypath'			   : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_201830.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017115.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017116.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017118.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201841.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201842.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201830.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201830.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201841.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201842.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017869.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017871.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017897.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017898.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017895.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017899.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017900.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017901.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017906.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017890.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/FacultyXMLA00010040.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/AdvisorXMLA00010041.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/FacultyAndAdvisorXMLA00010042.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201847_plan.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201847_plan.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00010063_plan.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/TermXML205006_blockReg.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML205006_blockReg.xml',
												  '/src/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00072282_blockReg.xml'],
            'registration-planning-region'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationPlanningByRegionData.xml'],
            'GrailsReadOnly'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/GrailsUserReadOnly.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GrailsSAMLUser.xml'],
            'finance-budget-availability-data' : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccountIndex.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinancePayrollUserData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccountType.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalFundType.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalFund.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalOrganization.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccount.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalProgram.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalLocation.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalActivity.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityVendors.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceUserRuleGroup.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityJournalVoucher.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPurchaseRequisitions.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPurchaseOrders.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUserToOrganization.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUserToFund.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityOperatingLedger.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityTransactionHistory.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbLedgerHeaders.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityTransactionDetails.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbDistribution.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityCleanPayrollExpenseData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPayrollExpense.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUpdatePidmPayrollExpenseData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbPeriodDetails.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityQueryPrototypes.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityFavQueryPrototypes.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityComputedColumns.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/DocumentApprovalQueueDefinition.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceGrantHeader.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/DirectCashReceiptHeader.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceGrantLedger.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityViewPendingDocumentsHistory.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceDiscount.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxGroup.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxRate.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxGroupAndRate.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityInvoiceDetails.xml'],
            'registration-student-attribute'   : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentRegistrationAttribute_Data.xml'],

            'finance-journals' : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/IncomeClass.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/EndowmentPool.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/EndowmentFund.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/AutoJournal.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalApprovalHistory.xml' ,
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalApprovalInProgress.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalCommodity.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalHeader.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalDetail.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceText.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalUnApprovedDocument.xml',
                                  '/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToRuleClass.xml' ],

            'projection-used-courses'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumComplianceUsedCourses_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/ProjectionTermData.xml'],
            'finance-cifoapal-cleanup'         : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceCifoapalpClean.xml'],

            'finaid-validation'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/finaid/FinAidValidationData.xml'],
            'finaid'                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/finaid/FinancialAidData.xml'],
            'api-payroll'                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRLREA_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRBREA_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRCERT_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTREXAM_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRSKIL_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRREFE_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRCERT_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPREXAM_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRSKIL_Data.xml'],
            'api-finance'                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/api/finance/FTVHRSN_Data.xml'],
            'api-student'                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLBTERM_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRLMFE_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/STVARTP_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRMSCD_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRRASG_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRMASG_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SLBRMAP_Data.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/api/student/SPRCMNT_Data.xml'],
            'schedule-registration-status'     : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRegistrationStatusByTerm_201410.xml'],
            'scheduleTerm201410'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'scheduleData201410Crn20201'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml'],
            'scheduleData201410Crn20202'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml'],
            'scheduleData201410Crn20210'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml'],
            'scheduleData201410Crn20211'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml'],
            'scheduleData201410Crn20222'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml'],
            'generalstudenthos00001'           : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml'],
            'ssbgeneralstudent2hosweb007'      : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            //TODO Need to revisit; this is a duplicate call
            'ssbgeneralstudenthosweb001'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml'],
            'ssbgeneralstudenthosweb006'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            'catalogGradeModeWriting103'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogDataGradeModeWriting103.xml'],
            'addauth'                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentAddAuthData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/AddAuthTermSetupData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSectionAddAuthData.xml',
                                                  '/src/groovy/net/hedtech/banner/seeddata/Data/AddAuthValidationSetupData.xml'],
            'addauth-planning'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/AddAuthPlanningData.xml'],
            'scheduleTerm201810'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201810.xml'],
            'scheduleData201810'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201810.xml'],
            'registrationTerm201850'           : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationTerm201850.xml'],
            'generalstudenthosapi005'          : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHosapi005.xml'],
            'jobsub'                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/jobsub.xml'],
            'self-service-configuration'       : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceConfiguration.xml'],
            'reset-password'                   :['/src/groovy/net/hedtech/banner/seeddata/Data/resetPasswordUserSetupData.xml',
                                                 '/src/groovy/net/hedtech/banner/seeddata/Data/GuestUserSetupData.xml'],
            'ar-data'                          :['/src/groovy/net/hedtech/banner/seeddata/Data/student/ar/AccountReceivableStudentUsers.xml',
                                                 '/src/groovy/net/hedtech/banner/seeddata/Data/student/ar/StudentAccountsAccountChargePaymentDetail.xml']
    ]
    /**
     *  Map of selenium targets
     *  Some of these do not need to appear in the targets because they are for specific actions required for selenium
     *  example, if you need to delete the data for a specific form so you can test an add. The delete actions should not
     *  be executed when the all seed data is executed.
     *  You can associate one target with multiple files like what is done in the targets map
     */


    def seleniumTargets = [
            'catalog-selenium'                                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSelenium.xml'],
            'finance-on'                                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance_on.xml'],
            'finance-off'                                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/finance_off.xml'],
            'schedule-selenium'                                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleSelenium.xml'],
            'schedule-term-selenium'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'schedule-syllabus-remove-selenium'                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSyllabusSelenium.xml'],
            'schedule-section-comment-remove-selenium'                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionCommentSelenium.xml'],
            'schedule-reserved-seats-remove-selenium'                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveReservedSeatsSelenium.xml'],
            'schedule-wailist-automation-section-update-selenium'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleUpdateWaitlistAutomationSectionSelenium.xml'],
            'schedule-wailist-automation-section-remove-selenium'                : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionSelenium.xml'],
            'schedule-academic-calendar-rules-remove-selenium'                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveAcademicCalendarRulesSelenium.xml'],
            'schedule-waitlist'                                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionControl.xml'],
            'courseDetailRemove-selenium'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseDetailInformationSelenium.xml'],
            'courseDetail-selenium'                                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseDetailSelenium.xml'],
            'schedule-campus-security'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleCampusSecurity.xml'],
            'schedule-removecampus-security'                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCampusSecurity.xml'],
            'schedule-removescheduledetail-selenium'                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetail.xml'],
            'college-and-department-text-remove-selenium'                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCollegeAndDepartmentTextSelenium.xml'],
            'catalogMutuallyExclusiveRemove-selenium'                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveMutuallyExclusiveSelenium.xml'],
            'catalogCourseSyllabusRemove-selenium'                               : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseSyllabusSelenium.xml'],
            'catalogScheduleRestrictionsRemove-selenium'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCatalogScheduleRestrictions.xml'],
            'courseRestriction-selenium'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRestrictionSelenium.xml'],
            'courseRestrictionRemove-selenium'                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseRegistrationRestrictionsSelenium.xml'],
            'courseSubject-selenium'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSubjectRemoveSelenium.xml'],
            'sectionCrossList-selenium'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/SectionCrossListRemoveSelenium.xml'],
            'sectionCrossEnroll-selenium'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleCrossListEnrollmentSelenium.xml'],
            'sectionCrossRemoveEnroll-selenium'                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCrossListEnrollmentSelenium.xml'],
            'sectionFeeAssessmentControlRemove-selenium'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionFeeAssessmentControlSelenium.xml'],
            'schedule-remove-Restrictions-selenium'                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveRestrictionSelenium.xml'],
            'scabase_remove_catalog'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemove_SCABASETest.xml'],
            'schedule-remove-detail-preferences-selenium'                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetailPreferencesSelenium.xml'],
            'schedule-remove-meeting-times-selenium'                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveMeetingTimeSelenium.xml'],
            'schedule-remove-section-preferences-selenium'                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionPreferencesSelenium.xml'],
            'schedule-remove-schedule-labor-distribution-selenium'               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleLaborDistributionSelenium.xml'],
            'schedule-remove-block-schedule-selenium'                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveBlockScheduleControlSelenium.xml'],
            'catalog-removecourselabordistribution-selenium'                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseLaborDistributionSelenium.xml'],
            'enrollment-status-rules-selenium'                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesSelenium.xml'],
            'enrollment-status-rules-remove-selenium'                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesRemoveSelenium.xml'],
            'course-registration-status-selenium'                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusSelenium.xml'],
            'course-registration-status-remove-selenium'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusRemoveSelenium.xml'],
            'registration-group-control-selenium'                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlSelenium.xml'],
            'registration-group-control-remove-selenium'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlRemoveSelenium.xml'],
            'overall-termbase-remove-selenium'                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermBaseRemoveSelenium.xml'],
            'degree-works-crosswalk-off-selenium'                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOffSelenium.xml'],
            'degree-works-crosswalk-on-selenium'                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOnSelenium.xml'],
            'catalog-additional'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogAdditionalSelenium.xml'],
            'schedule-additional'                                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleAdditionalSelenium.xml'],
            'cat-sch-readonly'                                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/catalogScheduleQueryOnlyPagesSelenium.xml'],
            'cat-sch-removereadonly'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/RemoveCatalogScheduleQueryOnlyPagesSelenium.xml'],
            'fge-instructor'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201410.xml'],
            'fge-reg-selenium'                                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeedSelenium_204010.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeedSelenium_204010.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeedSelenium_204010.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedSelenium_204010.xml'],
            'fge-remove-reg-selenium'                                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedRemoveSelenium_204010.xml'],
            'event-function-remove'                                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAndFunctionRemove.xml'],
            'event-function'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAndFunction.xml'],
            'event-remove-function-comment'                                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveFunctionComment.xml'],
            'event-remove-invitee'                                               : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveInvitee.xml'],
            'event-remove-participant'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveParticipant.xml'],
            'event-remove-registration'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveRegistration.xml'],
            'event-registration'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRegistration.xml'],
            'event-attendance-tracking'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTracking.xml'],
            'event-attendance-tracking-remove'                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTrackingRemove.xml'],
            'remove-event-registration'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/RemoveEventRegistration.xml'],
            'registration-waitlist'                                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitList.xml'],
            'registration-waitlist-remove'                                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitListRemove.xml'],
            'registration-waitlist-seed'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeed.xml'],
            'registration-waitlist-seed-remove'                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeedRemove.xml'],
            'registration-ssb-selenium'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSelenium.xml'],
            'registration-ssb-selenium-remove'                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeleniumRemove.xml'],
            'registration-error-message-selenium-remove'                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationErrorMessageSeleniumRemove.xml'],
            'registration-time-status-selenium-remove'                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationTimeStatusSeleniumRemove.xml'],
            'registration-third-party-registration-time-controls-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/ThirdPartyRegistrationTimeControlsSeleniumRemove.xml'],
            'hold-information'                                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/holdInformationSelenium.xml'],
            'hold-information-remove'                                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/holdInformationRemoveSelenium.xml'],
            'validation-cleanup'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/ValidationDataCleanup.xml'],
            'curriculum-validation'                                              : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumValidationData.xml'],
            'curriculum-rules-control'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlSelenium.xml'],
            'curriculum-rules-control-remove'                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlRemoveSelenium.xml'],
            'attendance-tracking-rule-remove'                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingRuleRemoveSelenium.xml'],
            'permit-override-remove'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentRegistraionPermitOverrideSeleniumRemove.xml'],
            'study-path-off'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOffSelenium.xml'],
            'study-path-on'                                                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOnSelenium.xml'],
            'subject-web-registration-off'                                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOffSelenium.xml'],
            'subject-web-registration-on'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOnSelenium.xml'],
            'term-study-path-on'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOnSelenium.xml'],
            'term-study-path-off'                                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOffSelenium.xml'],
            'term-control-seed'                                                  : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlSeedSelenium.xml'],
            'term-lcdesc-on-lsdesc-on'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOnSelenium.xml'],
            'term-lcdesc-on-lsdesc-off'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOffSelenium.xml'],
            'term-lcdesc-off-lsdesc-on'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOnSelenium.xml'],
            'term-lcdesc-off-lsdesc-off'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOffSelenium.xml'],
            'term-lctitle-on-lstitle-on'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOnSelenium.xml'],
            'term-lctitle-on-lstitle-off'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOffSelenium.xml'],
            'term-lctitle-off-lstitle-on'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOnSelenium.xml'],
            'term-lctitle-off-lstitle-off'                                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOffSelenium.xml'],
            'crosswalk-webmancont-off'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOffSelenium.xml'],
            'crosswalk-webmancont-on'                                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOnSelenium.xml'],
            'keyword-seed'                                                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSelenium.xml'],
            'keyword-seed-remove'                                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSeleniumRemove.xml'],
            'block-registration'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistration.xml'],
            'block-registration-remove'                                          : ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationRemove.xml'],
            'overall-page-config-seed'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSelenium.xml'],
            'overall-page-config-seed-remove'                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSeleniumRemove.xml'],
            'student-centric-period-selenium'                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentCentricPeriodSeleniumRemove.xml'],
            'block-registration-group'                                           : ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationGroup.xml'],
            'recruiting-selenium'                                                : ['/src/groovy/net/hedtech/banner/seeddata/Data/RecruitingSelenium.xml'],
            'recruiting-selenium-remove'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RecruitingSeleniumRemove.xml'],
            'source-background-institution-base-selenium'                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSelenium.xml'],
            'source-background-institution-base-selenium-remove'                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSeleniumRemove.xml'],
            'gradable-component-definition-selenium-remove'                      : ['/src/groovy/net/hedtech/banner/seeddata/Data/GradableComponentDefinitionSeleniumRemove.xml'],
            'repeat-multiple-course-rules-selenium-remove'                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/RepeatMultipleCourseRulesSeleniumRemove.xml'],
            'gpa-rules-round'                                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesRound.xml'],
            'gpa-rules-truncate'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesTruncate.xml'],
            'gpa-rules-delete'                                                   : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesDelete.xml'],
            'scheduleTerm201410'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'scheduleData201410'                                                 : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410.xml'],
            'scheduleData201410Crn20201'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml'],
            'scheduleData201410Crn20202'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml'],
            'scheduleData201410Crn20210'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml'],
            'scheduleData201410Crn20211'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml'],
            'scheduleData201410Crn20222'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml'],
            'scheduleData201410Crn20009'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20009.xml'],
            'crn20116-reset'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20116.xml'],
            'crn20441-reset'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20441.xml'],
            'generalstudenthos00001'                                             : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml'],
            'ssbgeneralstudent2hosweb007'                                        : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            'ssbgeneralstudenthosweb001'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml'],
            'ssbgeneralstudenthosweb006'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            'schedule-reset'                                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset.xml'],
            'schedule-20151-reset'                                               : ['/src/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Reset.xml'],
            'schedule-20151-approval'                                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Approval.xml'],
            'schedule-20122-reset'                                               : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset20122.xml'],
            'schedule-20122-approval'                                            : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleApproval20122.xml'],
            'registrationTerm201850-reset'                                       : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationTerm201850.xml'],
            'registration-history-reset'                                         : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHistoryTestRefresh.xml'],
            'structured-reg-functional-data'                                     : ['/src/groovy/net/hedtech/banner/seeddata/Data/CappProgramSRPGMGroup.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationTwo.xml'],
            //corrections made to integration data to keep it in sync with functional data are being added to a new target so that they can be run in Daily environment for review
            //as more corrections are made, those data files may be added to this target as needed
            'structured-reg-integration-data'                                    : ['/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration17.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration37.xml',
                                                                                    '/src/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration38.xml']
    ]

    def calbTargets = [
            'student': ['/src/groovy/net/hedtech/banner/seeddata/Data/calb/student/CalbstuMisProgramData.xml']]


    def validateTable(Sql conn) {
        String ownerSql = """select owner from all_tables where table_name = ?"""

        def owner = conn.firstRow(ownerSql, [this.tableName])
        if (!owner) {
            ownerSql = """ select owner from all_views where view_name = ?"""
            owner = conn.firstRow(ownerSql, [this.tableName])
        }
        this.owner = owner?.owner
        if (!owner) {
            validTable = null
        } else validTable = owner
    }

    /**
     * Sets the current year, month, and day input data.
     * This is used to populate the activity date
     * */

    def setCurrentDate() {
        def cal = Calendar.instance
        yyyy = cal.get(Calendar.YEAR)
        mm = String.format('%02d', cal.get(Calendar.MONTH) + 1)   // pad with 0
        day = String.format('%02d', cal.get(Calendar.DATE))   // pad with 0

    }

    /**
     * Prompts the user via the command line for input data needed in order to load seed data.
     * */

    public def promptUserForInputData(args) {
        setCurrentDate()
        if (prompts) {
            batchSeed = prompts[0]

            switch (batchSeed.toUpperCase()) {
                case "Y":
                    baseDirectory = prompts[1]
                    break
                case "N":
                    xmlFile = prompts[1]
                    break
                case "I":
                    xmlControlFile = prompts[1]
                    break
                default:
                    break;
            }
            def argSaveThis = prompts[2]
            saveThis = ("N" == argSaveThis ? false : true)
            def argReplaceData = prompts[3]
            replaceData = ("Y" == argReplaceData ? true : false)
            def argDebugThis = prompts[4]
            debugThis = ("Y" == argDebugThis ? true : false)
            def argShowErrors = prompts[5]
            showErrors = ("N" == argShowErrors ? false : true)
            if (!dataSource) {
                username = prompts[6]
                password = prompts[7]
                hostname = prompts[8]
                instance = prompts[9]
            }
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
            println "Seed  BULK XML's: >> seed-data all "
            print "Seed  BULK XML's: Press 'Y' to seed multiple XML files , 'N' to seed Single XML file, 'I' control file with list : "
            batchSeed = br.readLine()
            switch (batchSeed.toUpperCase()) {
                case "Y":
                    print "Enter Root directory containing the seed data XML files: "
                    baseDirectory = br.readLine()
                    break
                case "N":
                    print "Enter path and file name of XML file: "
                    xmlFile = br.readLine()
                    break
                case "I":
                    print "Enter path and file name of control file: "
                    xmlControlFile = br.readLine()
                    break
                default:
                    break;
            }

            print "Enter Y or N to save your transaction [${saveThis ? 'Y' : 'N'}]: "
            def inSaveThis = br.readLine()
            saveThis = ("N" == inSaveThis ? false : true)

            print "Enter Y or N to delete existing seed data with same key and re-add [${replaceData ? 'Y' : 'N'}]: "
            def inReplaceData = br.readLine()
            replaceData = ("Y" == inReplaceData || !inReplaceData ? true : false)

            print "Enter Y or N to print details of data and generated SQL [${debugThis ? 'Y' : 'N'}]: "
            def inDebugThis = br.readLine()
            debugThis = ("Y" == inDebugThis ? true : false)

            print "Enter Y or N to show SQL exceptions [${showErrors ? 'Y' : 'N'}]: "
            def inShowErrors = br.readLine()
            showErrors = ("N" == inShowErrors ? false : true)

            if (!dataSource) {
                print "Enter your Oracle user id or press enter to use default [$username]: "
                def inUserId = br.readLine()
                if (inUserId) username = inUserId  // .toUpperCase()

                print "Enter your password or press enter to use default [$password]: "
                def inPass = br.readLine()
                if (inPass) password = inPass // .toUpperCase()

                print "Enter your database server or press enter to use default [$hostname]: "
                def inHost = br.readLine()
                if (inHost) hostname = inHost // .toUpperCase()

                print "Enter your database instance or press enter to use default [$instance]: "
                def inInst = br.readLine()
                if (inInst) instance = inInst // .toUpperCase()
            }
        }
        println " "
        println "You entered:"
        println "Save: ${this.saveThis}"
        println "Replace: ${this.replaceData}"
        println "Debug: ${this.debugThis}"
        url = "jdbc:oracle:thin:@${hostname}:1521/${instance}"
        def dbConf = dataSource ? "Will use DataSource: $dataSource" : "Database connect info: $username/$password, url: $url"
        println "XML file: ${xmlFile} "
        println "batchSeed : ${batchSeed} "
        println "baseDirectory : ${baseDirectory} "
        println "URL: ${url}"


    }

    // Override the url property accessor, as it may not be initialized yet if set via interactive input
    // of the hostname and instance/sid values.


    public String getUrl() {
        // println "Get url: ${url}"
        if (!url) {
            //url = CH?.config?.CH?.bannerDataSource.url
            //println "DB URL  ${url} "
            def configFile = locateConfigFile()
            def slurper = new ConfigSlurper(GrailsUtil.environment)
            def config = slurper.parse(configFile.toURI().toURL())
            url = config.get("bannerDataSource").url
        }
        url
    }


    public void setUrl(String urlString) {
        url = urlString
    }

    /**
     * Maintains counts for processed tables.
     * */

    def tableUpdate(inTableName, long inReadCnt, long inInsertCnt, long inUpdateCnt, long inErrorCnt, long inDeleteCnt) {

        if (!(inReadCnt == 0 && inInsertCnt == 0 && inUpdateCnt == 0 && inErrorCnt == 0 && inDeleteCnt == 0)) {

            if (tableSize > 0) {
                def findcnt = 0
                def cc = 0
                def itt = 0
                tableCnts.each { tab ->
                    if (tab.tableName == inTableName) {
                        findcnt++
                        tab.readCnt += inReadCnt
                        tab.insertCnt += inInsertCnt
                        tab.updateCnt += inUpdateCnt
                        tab.errorCnt += inErrorCnt
                        tab.deleteCnt += inDeleteCnt
                    }
                    cc++
                }
                if (findcnt == 0) {
                    def tabi = new TableCnts(inTableName, inReadCnt, inInsertCnt, inUpdateCnt, inErrorCnt, inDeleteCnt)
                    tableCnts << tabi
                    tableSize++
                }
            } else {
                def tab = new TableCnts(inTableName, inReadCnt, inInsertCnt, inUpdateCnt, inErrorCnt, inDeleteCnt)
                tableCnts << tab
                tableSize++
            }
        }
    }


    def tableListCnts() {
        def readTot = 0
        def insertTot = 0
        def errorTot = 0
        def updateTot = 0
        def deleteTot = 0
        def tableCnt = 0
        tableCnts.each { tab ->
            println "Total for Table: ${tab.tableName} " +
                    " \tRead: ${tab.readCnt.toString().padLeft(4, ' ')} " +
                    " \tInsert: ${tab.insertCnt.toString().padLeft(4, ' ')} " +
                    " \tUpdate: ${tab.updateCnt.toString().padLeft(4, ' ')} " +
                    " \tDeletes: ${tab.deleteCnt.toString().padLeft(4, ' ')} " +
                    " \tErrors: ${tab.errorCnt.toString().padLeft(4, ' ')} "

            tableCnt++
            readTot += tab.readCnt
            insertTot += tab.insertCnt
            updateTot += tab.updateCnt
            errorTot += tab.errorCnt
            deleteTot += tab.deleteCnt
        }

        println "\nTotal Tables: ${tableCnt.toString().padLeft(4, ' ')} " +
                " \t\tRead: ${readTot.toString().padLeft(4, ' ')} " +
                " \tInsert: ${insertTot.toString().padLeft(4, ' ')} " +
                " \tUpdate: ${updateTot.toString().padLeft(4, ' ')} " +
                " \tDeletes: ${deleteTot.toString().padLeft(4, ' ')} " +
                " \tErrors: ${errorTot.toString().padLeft(4, ' ')} "
        totalErrors = errorTot
        if (errorTot > 0){
            println "\n Errors are present in file ${xmlFile}"
        }
    }


    public String toString() {
        println """Data Load Input Data tostring:
                   XmlFile = ${xmlFile}
                   saveThis = ${saveThis}
                   debugThis = ${debugThis}
                   showErrors = ${showErrors}
                   replaceData = ${replaceData}
         """
    }


    public syncSsbSectOracleTextIndex() {
        def localurl = url
        if (!url) {
            def configFile = locateConfigFile()
            def slurper = new ConfigSlurper(GrailsUtil.environment)
            def config = slurper.parse(configFile.toURI().toURL())
            localurl = config.get("bannerDataSource").url
        }
        def db = Sql.newInstance(localurl,   //  db =  new Sql( connectInfo.url,
                "saturn",
                "u_pick_it",
                'oracle.jdbc.driver.OracleDriver')

        def rows = db.rows("""SELECT Pnd_Index_Name name, count(*) cnt,
                                     max(To_Char(Pnd_Timestamp, 'dd-mon-yyyyhh24:mi:ss')) Timestamp
                                FROM Ctxsys.Ctx_User_Pending
                               WHERE Pnd_Index_name IN ('SSBSECT_SS_IDX','SCBCRSE_SC_IDX')
                            GROUP BY Pnd_Index_Name""")
        //println "before sync ${rows}"
        def syncRequired = false
        rows.each {
            if (it.CNT > 0) syncRequired = true
        }
        //println "sync required ${syncRequired}"
        def user2 = db.firstRow("select user from dual")
        //  println "user2 ${user2}"
        if (syncRequired) {
            // println "sync in progress"
            db.call("""Begin
                       Ctxsys.ctx_ddl.sync_index('ssbsect_ss_idx');
                       Ctxsys.CTX_DDL.OPTIMIZE_INDEX('ssbsect_ss_idx','FULL');
                       Ctxsys.ctx_ddl.sync_index('scbcrse_sc_idx');
                       Ctxsys.CTX_DDL.OPTIMIZE_INDEX('scbcrse_sc_idx','FULL');
                   End;""")
        }
        rows = db.rows("""Select Pnd_Index_Name name, count(*) cnt,
                                  max(To_Char(Pnd_Timestamp, 'dd-mon-yyyyhh24:mi:ss')) Timestamp
                                  From Ctxsys.Ctx_User_Pending group by Pnd_Index_Name """)
        // println "after sync ${rows}"
        db.close()
    }


    private locateConfigFile() {
        def propertyName = "BANNER_APP_CONFIG"
        def fileName = "banner_configuration.groovy"
        def filePathName = getFilePath(System.getProperty(propertyName))
        if (!filePathName) {
            filePathName = getFilePath("${System.getProperty('user.home')}/.grails/${fileName}")
        }
        if (!filePathName) {
            filePathName = getFilePath("${fileName}")
        }
        if (!filePathName) {
            filePathName = getFilePath("grails-app/conf/${fileName}")
        }
        if (!filePathName) {
            filePathName = getFilePath(System.getenv(propertyName))
        }
        if (!filePathName) {
            throw new RuntimeException("Unable to locate ${fileName}")
        }
        def configFile = new File(filePathName)
        println "using configuration: " + configFile
        return configFile
    }


    private String getFilePath(filePath) {
        if (filePath && new File(filePath).exists()) {
            "${filePath}"
        }
    }
}
