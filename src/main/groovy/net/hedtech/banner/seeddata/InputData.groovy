/*********************************************************************************
 Copyright 2010-2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import grails.util.Holders as CH
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
    def saveProxyIdm = null
    def saveLcurSeqno = null
    def saveSeqno = null
    def saveCurrRule = null
    def tableName = null
    def owner = null

    def validTable = null

    // map of run time targets to specify file names
    def targets = [
            'seed-cleanup'                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogSeedDelete.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTermDelete.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ValidationDataCleanup.xml'],
            'api-extensibility'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilityConfiguration.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySqlProcessRules.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySqlValidation.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/extensibility/ApiExtensibilitySampleSdeSetup.xml'],
            'catalog'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/genpersonValidationXML.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogValidationSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentValidation.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogDataGradeModeWriting103.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20009.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20116.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20441.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleData_201310.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleData_201320.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleStructureData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScrlevlData.xml'],
            'curriculumdefault'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SobctrlDefaultData.xml'],
            'employee'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GtvemalData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GtvdicdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/StvteleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/StvatypData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GordmclData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GordmskData.xml',
                                                  // Finance data
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvfsyrData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvfspdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvobudData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvobphData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALFund.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALOrganization.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALAccount.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALProgram.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALActivity.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFOAPALLocation.xml',
                                                  // HR Validation
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvacatData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtveccgData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvecgrData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvqprtData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtvwkshData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvecipData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtveeogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvegrpData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvesklData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvshcdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvlcatData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvjctyData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvlgcdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvpcatData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvrqstData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/Ptv1099Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvbdtyData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvorgnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvcdesData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/HrDirectDepositValidationData.xml',
                                                  // HR Rules
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtralvlData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrqprtData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrqgrpData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrecdqData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/SalaryGroupData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrinstData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrfiniData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrrqueData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrwkshData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbcatData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtremprData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrpictData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrcalnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrdfprData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreclsData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreclcData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrearnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreernData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecbcData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtreholData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrjblnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrjcreData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrleavData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvasData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrtreaData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrshftData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrteshData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrwkprData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtresocData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrinstData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvacData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrlvprData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrwstbData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdclData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PxrtxcdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdcaData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbcdnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdpdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdplData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdcgData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdidData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdxdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdxeData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrbdldData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrpclsData.xml',
                                                  // Position class and Posn budget
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NbbfiscData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/PositionData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/PositionHistoryData.xml',
                                                  // Employee Information
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/HRBannerUser.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralUsers.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeBasicData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/EmployeeJobData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/EmployeeJobHistoryData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeDeductionData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeDeductionHistoryData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeLeaveData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeLeaveHistoryData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeHrAccessData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeRoleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/HrAdminUserData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceFoapalManagerUpdate.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrant.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrantEffort.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/foapal/FinanceGrantPersonnel.xml',
                                                  // Employee History
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PeretotData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerjtotData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerdtotData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrhistData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhraccrData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrjacrData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrjobsData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrearnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrelbdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrdednData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PhrdocmData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerjobsData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/history/PerlvtkData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/Pxrw2fdData.xml',
                                                  // ERLR
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/posnctl/NtrlraqData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecrcData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtvecpdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecpdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecpcData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecerData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrecprData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrtData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecstData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecsnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecscData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecsiData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecalData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecfdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PtrececData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrqData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecrsData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PhrecdtData.xml',
                                                  // Time Entry
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeTimeEntryExtractData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PerearnData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PerhourData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PertitoData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PerelbdData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PerlvtkData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/EmployeeTimeEntrySubmitData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/payroll/PprhnawData.xml'
            ],
            'functionaltest'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/functional_catsch_testdata.xml'],
            'curriculum-validation'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumValidationData.xml'],
            'catalog-selenium'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogSelenium.xml'],
            'sde'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SdeData.xml'],
            'extensibility'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData3.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/PageBuilderData4.xml'],
            'program-data'                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgramData.xml'],
            'generalstudent'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApplicantData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApplicantQuickEntryRule.xml'],
            'registration-rule'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationManagementControl.xml'],
            'selfserviceuser'                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceUserData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/surveyflow.xml'],
            'schedule-selenium'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleSelenium.xml'],
            'courseDetail-selenium'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CourseDetailSelenium.xml'],
            'courseRestriction-selenium'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRestrictionSelenium.xml'],
            'mep-data'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/mepdata.xml'],
            'catalog-additional'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogAdditionalSelenium.xml'],
            'schedule-additional'              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleAdditionalSelenium.xml'],
            'cat-sch-readonly'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogScheduleQueryOnlyPagesSelenium.xml'],
            'event'                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventValidationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRoomAndBuilding.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/EventPerson.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/EventData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/EventSdeData.xml'],
            'registration-waitlist'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitList.xml'],
            'registration-waitlist-remove'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitListRemove.xml'],
            'registration-waitlist-seed'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeed.xml'],
            'fge'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceValidationSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceTermSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceCatalogSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceCatalogSeedSelenium.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_201110.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203020.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeed_203030.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeedSelenium_204010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceFacultySeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201110.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203020.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_203030.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeedSelenium_204010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_201110.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_203010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGeneralStudentSeed_203020.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeedSelenium_204010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_201110.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_203010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeed_203020.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedSelenium_204010.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/FgeEgbEnhancement.xml'],
            'egb'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/EGBTermData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/EGBScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/EGBData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/EGBComponentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/EGBStudentData.xml'],
            'ssbadvisor'                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorTermSeed3.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonaSeed3.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSearchSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeSeed3.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorBannerCourse.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorCourseSection.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorRegistration.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPriorEducationAndTesting.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorFacultyAccess.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorNotes.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorHolds.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorAdviseeGradeSeed_201410.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorPersonNameDisplay.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorHierarchyNameDisplay.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSecuritySeed1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceAdvisorSecuritySeed2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProxyAccessSystemValidationGTVSYST.xml'],
            'aip'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPTerm.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsers.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPCurriculum.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity3.xml'],

            'registration-general'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationGeneral.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentCentricPeriod.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationCourseRegistrationDropStatus.xml'],
            'ssbgeneralstudent'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            'finance-validation'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvshipData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtvbuyrData.xml'],
            'finance-procurement'              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FtrcommData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseOrderHeader.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToOrganization.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToFund.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceFiscalYear.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionBuyerVerification.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionHeader.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCommodity.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionAccounting.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionText.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceGeneralTickler.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserProfileUpdateData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionApprovalHistory.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionApprovalInProgress.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionUnApprovedDocument.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionPO.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceSystemControlUpdateData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceProjectCustomerCharge.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceFund.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceAccountIndex.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCurrencyCode.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/PurchaseRequisitionCurrencyRate.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceApprovalQueueDefinition.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/VendorEmail.xml'],
            'ssbgeneralstudentblockreg'        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlockReg.xml'],
            'blockregistration'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/BlockRegistration.xml'],
            'registrationDataBlockReg'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationDataBlockReg.xml'],
            'ssbgeneralstudent2'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            'ssbgeneralstudent3'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew3.xml'],
            'ssbgeneralstudentfa'              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataFA.xml'],
            'feeSummaryData'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FeeSummaryData.xml'],
            'feeSummaryDataRemove'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FeeSummaryDataRemove.xml'],
            'ssbgeneralstudentblock'           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlock.xml'],
            'ssbgeneralstudentblockrule'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataBlockWithRule.xml'],
            'attr'                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingCumulativePercentageSetupSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingAdminRuleSetupSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingStudentAttendanceSeed8001.xml'],
            'ssb-curriculumarea-display'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AreaLibrary_smralib.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupLibrary_smrglib.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SectionAreaRestriction_ssrrare.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupRestrictedSubjectCourseAttachmentRuleBase_smbgrul.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupSubjectCourseAttachmentRule_smrgrul.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGroupSubjectCourseAttributeAttachment_smrgcaa.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGeneralGroupRequirement_smbggen.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentRuleBase_smbagrl.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentRule_smragrl.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaRestrictedSubjectCourseAttachmentRuleBase_smbarul.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaSubjectCourseAttachmentRule_smrarul.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumGeneralAreaRequirement_smbagen.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaCourseAttributeAttachment_smracaa.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumAreaGroupAttachmentAndManagement_smragam.xml'],
            'studentcurriculumdata'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentCurriculumData.xml'],
            'admissions'                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AdmissionsValidationSeed.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RecruitData.xml'],
            'academic-history'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryComponentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryStudentData.xml'],
            'registration-withdrawal'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHOSR24796.xml'],
            'registration-planning'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationPlanSeed.xml'],
            'transcript-award'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentTranscriptAwardedDegree.xml'],
            'student-adv-admin-persona'        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentAdvisorAdminPersonData.xml'],
            'flat-rate-fee-rules'              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FeeAssessmentFlatRate.xml'],
            'facultySecurityPlanAhead'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FacultySecurityPageData.xml'],
            'generalstudentcapp'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithCompliance.xml'],
            'reg-rsql'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionStructuredRegSql.xml'],
            'generalstudentsd01'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithSD01Program.xml'],
            'generalstudentsd02'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithSD02Program.xml'],
            'generalstudentdynamic'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentWithDynamicCappArea.xml'],
            'structured-reg'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationStructureHeader.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationStructureDetail.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationTwo.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationThree.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationProgram21.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationProgram21Grp.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21AcrossTerms.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21GrpAcrossTerms.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21IntegrationAcrossTerms.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21GrpIntegrationAcrossTerms.xml'],
            'generalstudentcappreg'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappreg01'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration01.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg02'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration02.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg03'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration03.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg04'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration04.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg05'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration05.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg06'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration06.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg07'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration07.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg15'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration15.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg16'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration16.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg17'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration17.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg18'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration18.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg19'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration19.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg20'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration20.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg21'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration21.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg22'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration22.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg23'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration23.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg24'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration24.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg25'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration25.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg26'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration26.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg27'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration27.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg28'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration28.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg29'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration29.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg30'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration30.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg31'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration31.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg33'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration33.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg36'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration36.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg37'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration37.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappreg38'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration38.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregKW1023'      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationKW1023.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregKW1028'      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationKW1028.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappStructureRule.xml'],
            'generalstudentcappregbareg20'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg20.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg23'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg23.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg25'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg25.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbareg26'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBareg26.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'generalstudentcappregbaregattr'   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201415.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201415.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationBaregAttr.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/StvsubjData.xml'],
            'studentcappstructuredreg'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentCappStructuredRegistration.xml'],
            'studentApiData'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiDeriveTerm.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiPersonMatchData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiSecurityData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/Api_GTVICSN.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/Api_GOBICPS.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiIntegrationConfigurationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiCountryValidationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiCurriculumData.xml'],
            'capp-programs'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/artprogam01.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/baengllit.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/baengllitx.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/baseedsp1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/baseedsp2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/dynamicproa1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jentest01.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jimsprog.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jmcptv.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jmnotcptv.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jxcptv.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jxnocptv.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/jzcptv.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/leeds.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/capp/multiterm01.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappProgramSRPGMGroup.xml'],
            'structured-progcat'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbproo.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbareo.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbcrso.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbares.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbcrss.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbgros.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbpros.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgCat_skbruls.xml'],
            'projected-reg'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermDataForProjections.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogDataForProjections.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CappComplianceDataForProjections.xml'],
            'projected-reg-config'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionConfigData.xml'],
            'projected-reg-data'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionData.xml'],
            'projected-reg-data-remove'        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationProjectionDataRemove.xml'],
            'student-academic-review'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewAdministrator.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewTermData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewCatalogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewGradeScaleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentAcademicReviewComponentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/2Curriculum2Program1StudyPathStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/2Curriculum1Program2StudyPathsStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/1Curriculum1ProgramNoStudyPath.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/UnRolledStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentGpaHoursCampusAndLevelRule.xml'],
            'bcm'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralCommunicationData.xml'],
            'api-person-filters'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiPersonFiltersData.xml'],
            'admissions-student'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AdmissionsStudentData.xml'],
            'api-hedm-registration'            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMTermData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMProgramData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMCatalogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMScheduleData.xml'],
            'api-hedm-student'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMStudentData.xml'],
            'api-hedm-registration-grade'      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHEDMGradeData.xml'],
            'finance-procurement-user-creation': ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserCreationData.xml'],
            'brainstorm-registration'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormTermData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormIntegrationPartnerData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormProgramData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormCatalogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormScheduleData.xml'],
            'brainstorm-student'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormStudentData.xml'],
            'brainstorm-registration-planning' : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationBrainstormPlanningData.xml'],
            'reg-ar'                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentAccountsReceivableData.xml'],
            'api-student-registration'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiStudentRegistrationHistoryData.xml'],
            'api-general-student'              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiGeneralStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiStudentPriorEducationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiStudentRegistrationHistoryData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ApiGeneralData.xml'],
            'general-ledger'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralLedger.xml'],
            'general-ledger-clean'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerClean.xml'],
            'fiscal-period-year'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FiscalPeriodAndYear.xml'],
            'fiscal-period-year-clean'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FiscalPeriodAndYearClean.xml'],
            'fgbtrni'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBTRNI.xml'],
            'fgbtrni-clean'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBTRNIClean.xml'],
            'fgbjvch'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBJVCH.xml'],
            'fgbjvch-clean'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBJVCHClean.xml'],
            'fgbtrnh'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBTRNH.xml'],
            'fgbtrnh-clean'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBTRNHClean.xml'],
            'fgbarcd'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBARCD.xml'],
            'fgbarcd-clean'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/FGBARCDClean.xml'],
            'general-ledger-gurfeed'           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerGURFEED.xml'],
            'general-ledger-gurfeed-clean'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralLedgerGURFEEDClean.xml'],
            'stvcomf'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/STVCOMF_Data.xml'],
            'stvcomf-clean'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/STVCOMF_DataClean.xml'],
            'general'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/general/SprmediData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/NonPersonData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GoradrlData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/general/GtvzipcData.xml'],
            'general-auto'                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/MDUUSecurityData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsers1.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsers2.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsersBANADMIN.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoUsersBANGENUSR.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoNewPages.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAutoSDEData.xml'],
            'gender-pronoun'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GtvgndrData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GtvpprnData.xml'],
            'direct-deposit'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/DirectDepositUsers.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/DirectDepositValidationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/DirectDepositData.xml'],
            'personal-info'                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/PersonalInfoData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/PersonalInfoDirectoryProfileData.xml'],
            'proxy-access'                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ProxyMenuData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProxyUsers.xml'],
            'general-common'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GorvisaData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/SqlProcessData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralGtvsdaxData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CommonMatchingData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GtvduntData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/AuthenticationUserData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/LetterGenerationData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/TabBasedSecurity.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/WebTailorMenu.xml'],
            'program-prerequisite'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgramRestriction_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgramTestScoreRestrictionAndPrerequisite_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProgramAreaRestriction_Data.xml'],
            'classList'                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListTermData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListCatalogData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListComponentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024925.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024831.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024932.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024934.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024935.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024937.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListStudentData_A00024919.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ClassListAdministrator.xml'],
            'droproster'                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/GTVDICD.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SFRRORL.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SFRSECH.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterDropStatusData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_PTUCHMAN.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_ARTHINC.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SFBDRTC.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SFBDRSC.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU01.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU02.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/DropRosterStudentData_DROPSTU03.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ReRegisterHOSH00023.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_GTVDICD.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_STVRSTS.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Term.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_Term.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Schedule.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_Schedule.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_Catalog.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201751_SFRRORL.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201752_SFRRORL.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00037841.xml'],
            'academic-standing'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201763_Term.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201763_Schedule.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201764_Term.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_201764_Schedule.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00010069.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/SeedData_A00017091.xml'],
            'zero-textbook-cost'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCGoriccrCodes.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCTerm_201840.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCStudent_A00017137.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCWebUser_A00017136.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCFaculty_A00017133.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCFacultyAndAdvisor_A00017135.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCAdvisorData_A00017134.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCScheduleData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ZTCStudent_A00017847.xml'],
            'holds-by-studypath'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_201830.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017115.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017116.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/HoldsByStudyPath_A00017118.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201841.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201842.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201830.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201830.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201841.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201842.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017869.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017871.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017897.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017898.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017895.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017899.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017900.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017901.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017906.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00017890.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/FacultyXMLA00010040.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AdvisorXMLA00010041.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/FacultyAndAdvisorXMLA00010042.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201847_plan.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201847_plan.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00010063_plan.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML205006_blockReg.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML205006_blockReg.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00072282_blockReg.xml'],
            'attributes-by-studypath'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/TermXML201801.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/ScheduleXML201801.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/RegistrarXMLA00010045.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/FacultyXMLA00010055.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00010176.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00010179.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLA00010178.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10174.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10187.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10222.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10267.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10177.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10189.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10190.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10160.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/history/StudentXMLHOSH10027.xml'],
            'registration-planning-region'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationPlanningByRegionData.xml'],
            'GrailsReadOnly'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GrailsUserReadOnly.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GrailsSAMLUser.xml'],
            'finance-budget-availability-data' : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccountIndex.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinancePayrollUserData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccountType.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalFundType.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalFund.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalOrganization.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalAccount.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalProgram.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalLocation.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceFoapalActivity.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityVendors.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceUserRuleGroup.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityJournalVoucher.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPurchaseRequisitions.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPurchaseOrders.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUserToOrganization.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUserToFund.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityOperatingLedger.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityTransactionHistory.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbLedgerHeaders.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityTransactionDetails.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbDistribution.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityCleanPayrollExpenseData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityPayrollExpense.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityUpdatePidmPayrollExpenseData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityEncumbPeriodDetails.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityQueryPrototypes.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityFavQueryPrototypes.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityComputedColumns.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/DocumentApprovalQueueDefinition.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceGrantHeader.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/DirectCashReceiptHeader.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceGrantLedger.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityViewPendingDocumentsHistory.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceDiscount.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxGroup.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxRate.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceTaxGroupAndRate.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/budgetavailability/FinanceBudgetAvailabilityInvoiceDetails.xml'],
            'registration-student-attribute'   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentRegistrationAttribute_Data.xml'],

            'finance-journals' : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/IncomeClass.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/EndowmentPool.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/EndowmentFund.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/AutoJournal.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalApprovalHistory.xml' ,
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalApprovalInProgress.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalCommodity.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalHeader.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalDetail.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceText.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/journal/JournalUnApprovedDocument.xml',
                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceUserToRuleClass.xml' ],

            'projection-used-courses'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumComplianceUsedCourses_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/ProjectionTermData.xml'],
            'finance-cifoapal-cleanup'         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance/FinanceCifoapalpClean.xml'],

            'finaid-validation'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/FinAidValidationData.xml'],
            'finaid'                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/FinancialAidData.xml'],

            'finaid-ss-validation'             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RoainstConfigData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/BudgetConfig.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/SayCodeData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/EnrollRuleData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/FundConfigData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvacyrData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvtrmtData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvtermData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvaprdData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvcampData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvtreqData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvsaprData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvtrstData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvholdData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvwtxtData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvmesgData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvarscData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvalgoData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvawstData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvfsrcData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvftypData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RfrbaseData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvpbgpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvpbtpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RormesgData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RorprdsData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvbgrpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvinfcData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvwebqData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvfaspData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvabrcData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvcompData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvbtypData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/GobqstnData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvcollData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvresdData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvtgrpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvwebqData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvclasData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvwdrlData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvsayrData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/GtvinsmData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvschdData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvlnstData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvstypData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvedlvData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvegolData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/WithdrawalTerms_ValidationData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/GtvletrData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/StvsbgiData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvpgrpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvpbcpData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvenrrData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RorenrrData.xml',
                                               '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/validation/RtvbcatData.xml'],

            'finaid-aidy-1718'                 :  ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RobprdsData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/BudgetConfig_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/PeriodTerm_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/EnrollRuleData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/FundConfigData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RarpagdData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RarpagsData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RbrabrcData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RbrbcatData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RbrpbcpData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RbrpbtpData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RfraschData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RfraspcData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RfrmesgData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RfrdefaData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RjrplrlData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RlrdmpoData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RlrdmpsData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RobinstData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RobsayrData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RoralgoData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorclveData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorpostData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RortprdData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RoruserData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorwebqData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorwbqaData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorwsqlData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorwtxtData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RorwtabData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RprsscsData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/RprsspbData_1718.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1718/EfcData_1718.xml'],

            'finaid-aidy-1819'                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RobaprdData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RobprdsData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RbrbcatData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RbrpbcpData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RbrpbgpData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RbrpbtpData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RfraschData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RfraspcData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RfrdefaData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwebaData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RobinstData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/Rorwebr1819AidyData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorcodiData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RormvalData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RortprdData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwebqData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwbqaData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwsqlData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwtxtData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RorwtabData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RtvintlData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/RobsayrData_1819.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1819/Rfrmesg1819Data.xml'],

            'finaid-aidy-1314'                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1314/RobinstData_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1314/RobprdsData_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1314/RpboptsData_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1314/RormvalData_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/aidy1314/Rorwebr1314Data.xml'],

            'finaid-ss-student'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50112_StudentReq_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50001_StudentReq_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/WithdrawalInfoAidy1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/WithdrawalInfoAidy1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40001_Award_Aid_Years_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40002_Award_Aid_Years_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50008_Aid_Year_Msg.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50009_Award_Active_Aid_Years.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50010_WebRequirement_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40011_Web_Requirement_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50011_TermsAndConditions_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50021_TermsAndConditions_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50012_Student_Supplied_Info.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50123_Payment_Scheduled_History_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50008_AidYearMsg_1718_and_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50017_RormesgWithoutMsg_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50013_PackageGroup_1819_Aid_Year.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50014_PackageGroup_1718_Aid_Year.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50016_Student_Holds_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50015_Student_Holds_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50007_Award_Schedule_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50115_Webq_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass20070_Award_History_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40004_Federal_Shopping_Sheet_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40015_Federal_Shopping_Sheet_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40016_Federal_Shopping_Sheet_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass30012_Coa_Aid_Year_Data.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass30002_Coa_Period_Data.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass30003AwardInfo1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass30004AwardInfo1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50023_Enrollment_Status_1314.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50024_AwardInformation_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass50025_AwardInformation_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40025_Award_Offer_1819.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass40024_Award_Offer_1718.xml',
                                                   '/src/main/groovy/net/hedtech/banner/seeddata/Data/finaid/student/Fass_70001_ContractsAndExemptions.xml'],

            'api-payroll'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRLREA_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRAPPS_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTVASRC_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTVPUBT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRSKLV_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRBREA_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRCERT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTREXAM_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTRSKIL_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRREFE_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRCERT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTVSCET_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTREMTY_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPREXPE_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPREXAM_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTVREVT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRPUBL_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PERREVW_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PABAPPL_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PTVENDS_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/payroll/PPRSKIL_Data.xml'],
            'api-general'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/api/general/APRCATG_Data.xml'],
            'api-finance'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FABCHKS_FABINCK_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FTVHRSN_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FTVVEND_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FTVVTYP_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FTVVENT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/finance/FBBBLIN_Data.xml'],
            'schedule-registration-status'     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRegistrationStatusByTerm_201410.xml'],
            'scheduleTerm201410'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'scheduleData201410Crn20201'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml'],
            'scheduleData201410Crn20202'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml'],
            'scheduleData201410Crn20210'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml'],
            'scheduleData201410Crn20211'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml'],
            'scheduleData201410Crn20222'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml'],
            'generalstudenthos00001'           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml'],
            'ssbgeneralstudent2hosweb007'      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            'ssbgeneralstudenthosweb001'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml'],
            'ssbgeneralstudenthosweb006'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            'catalogGradeModeWriting103'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogDataGradeModeWriting103.xml'],
            'addauth'                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentAddAuthData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/AddAuthTermSetupData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogSectionAddAuthData.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/AddAuthValidationSetupData.xml'],
            'addauth-planning'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AddAuthPlanningData.xml'],
            'scheduleTerm201810'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201810.xml'],
            'scheduleData201810'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201810.xml'],
            'registrationTerm201850'           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationTerm201850.xml'],
            'generalstudenthosapi005'          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHosapi005.xml'],
            'jobsub'                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/jobsub.xml'],
            'self-service-configuration'       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceConfiguration.xml'],
            'reset-password'                   :['/src/main/groovy/net/hedtech/banner/seeddata/Data/resetPasswordUserSetupData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/GuestUserSetupData.xml'],
            'ar-data'                          :['/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/AccountReceivableStudentUsers.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/StudentAccountsAccountChargePaymentDetail.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/AccountsReceivableTermControlData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/AccountsReceivableMemoData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/AccountsReceivableDetailCodeData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/installmentPlan/AccountsReceivableInstallmentPlanData.xml',
                                                '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/installmentPlan/InstallmentPlanTransactions.xml',
                                                '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/financialAid/AccountsReceivaleFinancialAidData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/tax/AccountsReceivableTaxNotificationData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/tax/AccountsReceivableTaxInformationData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/tax/AccountsReceivableSupplementalInformationData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/accountSummary/AccountReceivableEnrollmentPeriod.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/hold/AccountReceivableHoldInformation.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/deposit/AccountsReceivableDepositData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/accountSummary/AccountReceivableEnrollmentPeriod.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/statement/AccountReceivableStatementData.xml',
                                                 '/src/main/groovy/net/hedtech/banner/seeddata/Data/student/ar/deposit/AccountsReceivableDepositProcessingData.xml'],
            'api-student'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLBTERM_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRLMFE_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/STVARTP_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRMSCD_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRRASG_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRMASG_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLBRMAP_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SHRDGIH_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SHRDGDH_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SPRCMNT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RORSAPR_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RORPRDS_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRASCD_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/ROBPRDS_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RPRADSB_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RPRAWRD_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RORTPRD_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/RORSTAT_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SHRTCKG_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/STVRDEF_Data.xml',
                                                  '/src/main/groovy/net/hedtech/banner/seeddata/Data/api/student/SLRPREQ_Data.xml'],
            'proxy-roles'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ProxyMgmtUsers.xml'],
            'selfService-faculty-acceleration' :  [
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/FacultyIssuesAndRecommendationData.xml',
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/TermXML201913.xml',
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/StudentXMLA00040170.xml',
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/StudentXMLA00040167.xml',
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/ScheduleXML201913.xml',
			'/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/FacultyIssuesAssignmentData_A00040170.xml',
            '/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/FacultyStudentFeedbackCommentA00040170.xml',
            '/src/main/groovy/net/hedtech/banner/seeddata/Data/faculty/FacultyFeedbackCRNData.xml'
                                                   ]
    ]
    /**
     *  Map of selenium targets
     *  Some of these do not need to appear in the targets because they are for specific actions required for selenium
     *  example, if you need to delete the data for a specific form so you can test an add. The delete actions should not
     *  be executed when the all seed data is executed.
     *  You can associate one target with multiple files like what is done in the targets map
     */


    def seleniumTargets = [
            'catalog-selenium'                                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogSelenium.xml'],
            'finance-on'                                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance_on.xml'],
            'finance-off'                                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/finance_off.xml'],
            'schedule-selenium'                                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleSelenium.xml'],
            'schedule-term-selenium'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'schedule-syllabus-remove-selenium'                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSyllabusSelenium.xml'],
            'schedule-section-comment-remove-selenium'                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionCommentSelenium.xml'],
            'schedule-reserved-seats-remove-selenium'                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveReservedSeatsSelenium.xml'],
            'schedule-wailist-automation-section-update-selenium'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleUpdateWaitlistAutomationSectionSelenium.xml'],
            'schedule-wailist-automation-section-remove-selenium'                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionSelenium.xml'],
            'schedule-academic-calendar-rules-remove-selenium'                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveAcademicCalendarRulesSelenium.xml'],
            'schedule-waitlist'                                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionControl.xml'],
            'courseDetailRemove-selenium'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseDetailInformationSelenium.xml'],
            'courseDetail-selenium'                                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CourseDetailSelenium.xml'],
            'schedule-campus-security'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleCampusSecurity.xml'],
            'schedule-removecampus-security'                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCampusSecurity.xml'],
            'schedule-removescheduledetail-selenium'                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetail.xml'],
            'college-and-department-text-remove-selenium'                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCollegeAndDepartmentTextSelenium.xml'],
            'catalogMutuallyExclusiveRemove-selenium'                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveMutuallyExclusiveSelenium.xml'],
            'catalogCourseSyllabusRemove-selenium'                               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseSyllabusSelenium.xml'],
            'catalogScheduleRestrictionsRemove-selenium'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCatalogScheduleRestrictions.xml'],
            'courseRestriction-selenium'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRestrictionSelenium.xml'],
            'courseRestrictionRemove-selenium'                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseRegistrationRestrictionsSelenium.xml'],
            'courseSubject-selenium'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogSubjectRemoveSelenium.xml'],
            'sectionCrossList-selenium'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SectionCrossListRemoveSelenium.xml'],
            'sectionCrossEnroll-selenium'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleCrossListEnrollmentSelenium.xml'],
            'sectionCrossRemoveEnroll-selenium'                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCrossListEnrollmentSelenium.xml'],
            'sectionFeeAssessmentControlRemove-selenium'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionFeeAssessmentControlSelenium.xml'],
            'schedule-remove-Restrictions-selenium'                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveRestrictionSelenium.xml'],
            'scabase_remove_catalog'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemove_SCABASETest.xml'],
            'schedule-remove-detail-preferences-selenium'                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetailPreferencesSelenium.xml'],
            'schedule-remove-meeting-times-selenium'                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveMeetingTimeSelenium.xml'],
            'schedule-remove-section-preferences-selenium'                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionPreferencesSelenium.xml'],
            'schedule-remove-schedule-labor-distribution-selenium'               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleLaborDistributionSelenium.xml'],
            'schedule-remove-block-schedule-selenium'                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveBlockScheduleControlSelenium.xml'],
            'catalog-removecourselabordistribution-selenium'                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseLaborDistributionSelenium.xml'],
            'enrollment-status-rules-selenium'                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesSelenium.xml'],
            'enrollment-status-rules-remove-selenium'                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesRemoveSelenium.xml'],
            'course-registration-status-selenium'                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusSelenium.xml'],
            'course-registration-status-remove-selenium'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusRemoveSelenium.xml'],
            'registration-group-control-selenium'                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlSelenium.xml'],
            'registration-group-control-remove-selenium'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlRemoveSelenium.xml'],
            'overall-termbase-remove-selenium'                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermBaseRemoveSelenium.xml'],
            'degree-works-crosswalk-off-selenium'                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOffSelenium.xml'],
            'degree-works-crosswalk-on-selenium'                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOnSelenium.xml'],
            'catalog-additional'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CatalogAdditionalSelenium.xml'],
            'schedule-additional'                                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleAdditionalSelenium.xml'],
            'cat-sch-readonly'                                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/catalogScheduleQueryOnlyPagesSelenium.xml'],
            'cat-sch-removereadonly'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RemoveCatalogScheduleQueryOnlyPagesSelenium.xml'],
            'fge-instructor'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeed_201410.xml'],
            'fge-reg-selenium'                                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeedSelenium_204010.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeedSelenium_204010.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeedSelenium_204010.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedSelenium_204010.xml'],
            'fge-remove-reg-selenium'                                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedRemoveSelenium_204010.xml'],
            'event-function-remove'                                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventAndFunctionRemove.xml'],
            'event-function'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventAndFunction.xml'],
            'event-remove-function-comment'                                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRemoveFunctionComment.xml'],
            'event-remove-invitee'                                               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRemoveInvitee.xml'],
            'event-remove-participant'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRemoveParticipant.xml'],
            'event-remove-registration'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRemoveRegistration.xml'],
            'event-registration'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventRegistration.xml'],
            'event-attendance-tracking'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTracking.xml'],
            'event-attendance-tracking-remove'                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTrackingRemove.xml'],
            'remove-event-registration'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RemoveEventRegistration.xml'],
            'registration-waitlist'                                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitList.xml'],
            'registration-waitlist-remove'                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitListRemove.xml'],
            'registration-waitlist-seed'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeed.xml'],
            'registration-waitlist-seed-remove'                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeedRemove.xml'],
            'registration-ssb-selenium'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSelenium.xml'],
            'registration-ssb-selenium-remove'                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeleniumRemove.xml'],
            'registration-error-message-selenium-remove'                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationErrorMessageSeleniumRemove.xml'],
            'registration-time-status-selenium-remove'                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationTimeStatusSeleniumRemove.xml'],
            'registration-third-party-registration-time-controls-selenium-remove': ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ThirdPartyRegistrationTimeControlsSeleniumRemove.xml'],
            'hold-information'                                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/holdInformationSelenium.xml'],
            'hold-information-remove'                                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/holdInformationRemoveSelenium.xml'],
            'validation-cleanup'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ValidationDataCleanup.xml'],
            'curriculum-validation'                                              : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumValidationData.xml'],
            'curriculum-rules-control'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlSelenium.xml'],
            'curriculum-rules-control-remove'                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlRemoveSelenium.xml'],
            'attendance-tracking-rule-remove'                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingRuleRemoveSelenium.xml'],
            'permit-override-remove'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/StudentRegistraionPermitOverrideSeleniumRemove.xml'],
            'study-path-off'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOffSelenium.xml'],
            'study-path-on'                                                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOnSelenium.xml'],
            'subject-web-registration-off'                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOffSelenium.xml'],
            'subject-web-registration-on'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOnSelenium.xml'],
            'term-study-path-on'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOnSelenium.xml'],
            'term-study-path-off'                                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOffSelenium.xml'],
            'term-control-seed'                                                  : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlSeedSelenium.xml'],
            'term-lcdesc-on-lsdesc-on'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOnSelenium.xml'],
            'term-lcdesc-on-lsdesc-off'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOffSelenium.xml'],
            'term-lcdesc-off-lsdesc-on'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOnSelenium.xml'],
            'term-lcdesc-off-lsdesc-off'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOffSelenium.xml'],
            'term-lctitle-on-lstitle-on'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOnSelenium.xml'],
            'term-lctitle-on-lstitle-off'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOffSelenium.xml'],
            'term-lctitle-off-lstitle-on'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOnSelenium.xml'],
            'term-lctitle-off-lstitle-off'                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOffSelenium.xml'],
            'crosswalk-webmancont-off'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOffSelenium.xml'],
            'crosswalk-webmancont-on'                                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOnSelenium.xml'],
            'keyword-seed'                                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSelenium.xml'],
            'keyword-seed-remove'                                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSeleniumRemove.xml'],
            'block-registration'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/BlockRegistration.xml'],
            'block-registration-remove'                                          : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationRemove.xml'],
            'overall-page-config-seed'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSelenium.xml'],
            'overall-page-config-seed-remove'                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSeleniumRemove.xml'],
            'student-centric-period-selenium'                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentCentricPeriodSeleniumRemove.xml'],
            'block-registration-group'                                           : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationGroup.xml'],
            'recruiting-selenium'                                                : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RecruitingSelenium.xml'],
            'recruiting-selenium-remove'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RecruitingSeleniumRemove.xml'],
            'source-background-institution-base-selenium'                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSelenium.xml'],
            'source-background-institution-base-selenium-remove'                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSeleniumRemove.xml'],
            'gradable-component-definition-selenium-remove'                      : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GradableComponentDefinitionSeleniumRemove.xml'],
            'repeat-multiple-course-rules-selenium-remove'                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RepeatMultipleCourseRulesSeleniumRemove.xml'],
            'gpa-rules-round'                                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesRound.xml'],
            'gpa-rules-truncate'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesTruncate.xml'],
            'gpa-rules-delete'                                                   : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesDelete.xml'],
            'scheduleTerm201410'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm201410.xml'],
            'scheduleData201410'                                                 : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410.xml'],
            'scheduleData201410Crn20201'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20201.xml'],
            'scheduleData201410Crn20202'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20202.xml'],
            'scheduleData201410Crn20210'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20210.xml'],
            'scheduleData201410Crn20211'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20211.xml'],
            'scheduleData201410Crn20222'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20222.xml'],
            'scheduleData201410Crn20009'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20009.xml'],
            'crn20116-reset'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20116.xml'],
            'crn20441-reset'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/scheduleData_201410Crn20441.xml'],
            'generalstudenthos00001'                                             : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataHos00001.xml'],
            'ssbgeneralstudent2hosweb007'                                        : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew2Hosweb007.xml'],
            'ssbgeneralstudenthosweb001'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNew.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb001.xml'],
            'ssbgeneralstudenthosweb006'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentDataNewHosweb006.xml'],
            'schedule-reset'                                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset.xml'],
            'schedule-20151-reset'                                               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Reset.xml'],
            'schedule-20151-approval'                                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Approval.xml'],
            'schedule-20122-reset'                                               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset20122.xml'],
            'schedule-20122-approval'                                            : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleApproval20122.xml'],
            'registrationTerm201850-reset'                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationTerm201850.xml'],
            'registrationDataForBAs-reset'                                       : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationDataForBAs.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationDataForBAsCrns.xml'],
            'registrationDataForBAsBlockReg-reset'                               : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationDataForBAsBlockReg.xml'],
            'registrationDataBlockReg-reset'                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationDataBlockReg.xml'],
            'registration-history-reset'                                         : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/RegistrationHistoryTestRefresh.xml'],
            'structured-reg-functional-data'                                     : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/CappProgramSRPGMGroup.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationTwo.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/CourseInformationDRAM.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationProgram21.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistrationProgram21Grp.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleData_201310.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/ScheduleData_201320.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21AcrossTerms.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappProgram21GrpAcrossTerms.xml'],
            //corrections made to integration data to keep it in sync with functional data are being added to a new target so that they can be run in Daily environment for review
            //as more corrections are made, those data files may be added to this target as needed
            'structured-reg-integration-data'                                    : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration17.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration37.xml',
                                                                                    '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralStudentCappRegistration38.xml']
    ]

    def calbTargets = [
            'student': ['/src/main/groovy/net/hedtech/banner/seeddata/Data/calb/student/CalbstuMisProgramData.xml']]

    def bcmTargets = [
            'bcm' : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralCommunicationData.xml']
    ]

    def aipTargets = [
            'aip' : ['/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPTerm.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsers.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPCurriculum.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPData.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity1.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity2.xml',
                     '/src/main/groovy/net/hedtech/banner/seeddata/Data/GeneralAIPUsersSecurity3.xml']
    ]

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
            locateConfigFile()
            def config = CH.config
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
            locateConfigFile()
            def config = CH.config
            localurl = config.get("bannerDataSource").url
        }
        def db = Sql.newInstance(localurl,
                "saturn",
                "u_pick_it",
                CH.config.bannerDataSource.driver)

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
        String propertyValue = System.getProperty(propertyName) ?: System.getenv(propertyName)
        String filePathName = getFilePath(propertyValue)
        if (!filePathName) {
            filePathName = getFilePath("${System.getProperty('user.home')}/.grails/${fileName}")
        }
        if (!filePathName) {
            filePathName = getFilePath("${fileName}")
        }
        if (!filePathName) {
            filePathName = getFilePath("grails-app/conf/$fileName")
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
