/*********************************************************************************
 Copyright 2010-2016 Ellucian Company L.P. and its affiliates.
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

    // Database configuration. Note: if dataSource is available, the remaining database configuration fields are not used
    def dataSource
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
  'regqaautomated_terms': ['/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101610.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101615.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101620.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101625.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101635.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101640.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101641.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101642.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101645.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101646.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101647.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101650.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101655.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101660.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101670.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101680.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term101690.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term201410.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term201611.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600010.xml',  
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600011.xml',  
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600012.xml', 
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600013.xml', 
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600014.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/01Term600015.xml'],
  'regqaautomated_stv': ['/src/groovy/net/hedtech/banner/seeddata/Data/qa/STVATTR.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/STVCOLL.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/STVDEPT.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/STVROVR.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/STVSCHD.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/SCBCDEP.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/SSBXLST.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/SFRROVR.xml'],
  'regqaautomated_catalog': ['/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogABC_ATC30.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogABC_ATC3O.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogBIO_AT101.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogBIO_AT202.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogBIOL_AT101.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogBIOL_AT201.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogBIOL_AT202.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogCHEM_AT201.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogCHEM_AT202.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogENGL_1600.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogENGL_AT101.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogENGL_ATC1T.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogENGL_ATUO2.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogENGL_ATUT2.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogESL_ATC2T.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogESL_1601.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogESL_ATC3O.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT101.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT102.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT103.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT201.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT301.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_AT302.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATC1O.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATC1T.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATUO1.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATUO2.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATUT1.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogHIST_ATUT2.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogMATH_AT101.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogMATH_AT102.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogMATH_AT201.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogMATH_AT202.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/02CatalogPERG_ATC5T.xml'],               
  'regqaautomated_schedule': ['/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101610.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101615.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101620.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101625.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101635.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101640.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101641.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101642.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101645.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101646.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101647.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101650.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101655.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101660.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101670.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101680.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule101690.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule201611.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule600010.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule600012.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule600013.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule600014.xml',
                  '/src/groovy/net/hedtech/banner/seeddata/Data/qa/03Schedule600015.xml'],
  'regqaautomated_student': [
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000001.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000002.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000003.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000004.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000005.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000008.xml',    
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000010.xml',          
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000012.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000013.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000014.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000015.xml', 
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000016.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000017.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000018.xml', 
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000019.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000020.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000021.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000022.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000023.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000024.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000025.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000026.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000027.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000028.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000033.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000034.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000036.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000037.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentCALCUSER.xml'], 
      'regqaautomated_student_errors': [
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/delete_gobtpac.xml',
                   '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000009.xml',
                   '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000011.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/qa/05StudentAT0000029.xml']                        
          
            ]
    

    /**
     *  Map of selenium targets
     *  Some of these do not need to appear in the targets because they are for specific actions required for selenium
     *  example, if you need to delete the data for a specific form so you can test an add. The delete actions should not
     *  be executed when the all seed data is executed.
     *  You can associate one target with multiple files like what is done in the targets map
     */


    def seleniumTargets = [
            'catalog-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSelenium.xml'],
            'finance-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/finance_on.xml'],
            'finance-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/finance_off.xml'],
            'schedule-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleSelenium.xml'],
            'schedule-term-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleTerm.xml'],
            'schedule-syllabus-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSyllabusSelenium.xml'],
            'schedule-section-comment-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionCommentSelenium.xml'],
            'schedule-reserved-seats-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveReservedSeatsSelenium.xml'],
            'schedule-wailist-automation-section-update-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleUpdateWaitlistAutomationSectionSelenium.xml'],
            'schedule-wailist-automation-section-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionSelenium.xml'],
            'schedule-academic-calendar-rules-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveAcademicCalendarRulesSelenium.xml'],
            'schedule-waitlist': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveWaitlistAutomationSectionControl.xml'],
            'courseDetailRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseDetailInformationSelenium.xml'],
            'courseDetail-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseDetailSelenium.xml'],
            'schedule-campus-security': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleCampusSecurity.xml'],
            'schedule-removecampus-security': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCampusSecurity.xml'],
            'schedule-removescheduledetail-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetail.xml'],
            'college-and-department-text-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCollegeAndDepartmentTextSelenium.xml'],
            'catalogMutuallyExclusiveRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveMutuallyExclusiveSelenium.xml'],
            'catalogCourseSyllabusRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseSyllabusSelenium.xml'],
            'catalogScheduleRestrictionsRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCatalogScheduleRestrictions.xml'],
            'courseRestriction-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRestrictionSelenium.xml'],
            'courseRestrictionRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseRegistrationRestrictionsSelenium.xml'],
            'courseSubject-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogSubjectRemoveSelenium.xml'],
            'sectionCrossList-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/SectionCrossListRemoveSelenium.xml'],
            'sectionCrossEnroll-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleCrossListEnrollmentSelenium.xml'],
            'sectionCrossRemoveEnroll-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveCrossListEnrollmentSelenium.xml'],
            'sectionFeeAssessmentControlRemove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionFeeAssessmentControlSelenium.xml'],
            'schedule-remove-Restrictions-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveRestrictionSelenium.xml'],
            'scabase_remove_catalog': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemove_SCABASETest.xml'],
            'schedule-remove-detail-preferences-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleDetailPreferencesSelenium.xml'],
            'schedule-remove-meeting-times-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveMeetingTimeSelenium.xml'],
            'schedule-remove-section-preferences-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveSectionPreferencesSelenium.xml'],
            'schedule-remove-schedule-labor-distribution-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveScheduleLaborDistributionSelenium.xml'],
            'schedule-remove-block-schedule-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleRemoveBlockScheduleControlSelenium.xml'],
            'catalog-removecourselabordistribution-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogRemoveCourseLaborDistributionSelenium.xml'],
            'enrollment-status-rules-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesSelenium.xml'],
            'enrollment-status-rules-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/EnrollmentStatusRulesRemoveSelenium.xml'],
            'course-registration-status-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusSelenium.xml'],
            'course-registration-status-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/CourseRegistrationStatusRemoveSelenium.xml'],
            'registration-group-control-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlSelenium.xml'],
            'registration-group-control-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationGroupControlRemoveSelenium.xml'],
            'overall-termbase-remove-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermBaseRemoveSelenium.xml'],
            'degree-works-crosswalk-off-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOffSelenium.xml'],
            'degree-works-crosswalk-on-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/DegreeWorksCrosswalkOnSelenium.xml'],
            'catalog-additional': ['/src/groovy/net/hedtech/banner/seeddata/Data/CatalogAdditionalSelenium.xml'],
            'schedule-additional': ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleAdditionalSelenium.xml'],
            'cat-sch-readonly': ['/src/groovy/net/hedtech/banner/seeddata/Data/catalogScheduleQueryOnlyPagesSelenium.xml'],
            'cat-sch-removereadonly': ['/src/groovy/net/hedtech/banner/seeddata/Data/RemoveCatalogScheduleQueryOnlyPagesSelenium.xml'],
            'fge-reg-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceGradesSeedSelenium_204010.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSectionSeedSelenium_204010.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceInstructorAssignmentSeedSelenium_204010.xml',
                    '/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedSelenium_204010.xml'],
            'fge-remove-reg-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeedRemoveSelenium_204010.xml'],
            'event-function-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAndFunctionRemove.xml'],
            'event-function': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAndFunction.xml'],
            'event-remove-function-comment': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveFunctionComment.xml'],
            'event-remove-invitee': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveInvitee.xml'],
            'event-remove-participant': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveParticipant.xml'],
            'event-remove-registration': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRemoveRegistration.xml'],
            'event-registration': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventRegistration.xml'],
            'event-attendance-tracking': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTracking.xml'],
            'event-attendance-tracking-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/EventAttendanceTrackingRemove.xml'],
            'remove-event-registration': ['/src/groovy/net/hedtech/banner/seeddata/Data/RemoveEventRegistration.xml'],
            'registration-waitlist': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitList.xml'],
            'registration-waitlist-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitListRemove.xml'],
            'registration-waitlist-seed': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeed.xml'],
            'registration-waitlist-seed-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationWaitlistSeedRemove.xml'],
            'registration-ssb-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSelenium.xml'],
            'registration-ssb-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceStudentRegistrationSeleniumRemove.xml'],
            'registration-error-message-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationErrorMessageSeleniumRemove.xml'],
            'registration-time-status-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationTimeStatusSeleniumRemove.xml'],
            'registration-third-party-registration-time-controls-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/ThirdPartyRegistrationTimeControlsSeleniumRemove.xml'],
            'hold-information': ['/src/groovy/net/hedtech/banner/seeddata/Data/holdInformationSelenium.xml'],
            'hold-information-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/holdInformationRemoveSelenium.xml'],
            'validation-cleanup': ['/src/groovy/net/hedtech/banner/seeddata/Data/ValidationDataCleanup.xml'],
            'curriculum-validation': ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumValidationData.xml'],
            'curriculum-rules-control': ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlSelenium.xml'],
            'curriculum-rules-control-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlRemoveSelenium.xml'],
            'attendance-tracking-rule-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/AttendanceTrackingRuleRemoveSelenium.xml'],
            'permit-override-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/StudentRegistraionPermitOverrideSeleniumRemove.xml'],
            'study-path-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOffSelenium.xml'],
            'study-path-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/CurriculumRulesControlStudyPathOnSelenium.xml'],
            'subject-web-registration-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOffSelenium.xml'],
            'subject-web-registration-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/SubjectWebRegistrationOnSelenium.xml'],
            'term-study-path-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOnSelenium.xml'],
            'term-study-path-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlStudyPathOffSelenium.xml'],
            'term-control-seed': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlSeedSelenium.xml'],
            'term-lcdesc-on-lsdesc-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOnSelenium.xml'],
            'term-lcdesc-on-lsdesc-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOnLSDescOffSelenium.xml'],
            'term-lcdesc-off-lsdesc-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOnSelenium.xml'],
            'term-lcdesc-off-lsdesc-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCDescOffLSDescOffSelenium.xml'],
            'term-lctitle-on-lstitle-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOnSelenium.xml'],
            'term-lctitle-on-lstitle-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOnLSTitleOffSelenium.xml'],
            'term-lctitle-off-lstitle-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOnSelenium.xml'],
            'term-lctitle-off-lstitle-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/TermControlLCTitleOffLSTitleOffSelenium.xml'],
            'crosswalk-webmancont-off': ['/src/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOffSelenium.xml'],
            'crosswalk-webmancont-on': ['/src/groovy/net/hedtech/banner/seeddata/Data/CrosswalkWebUseManagementControlsOnSelenium.xml'],
            'keyword-seed': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSelenium.xml'],
            'keyword-seed-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/SelfServiceSearchByKeywordSeleniumRemove.xml'],
            'block-registration': ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistration.xml'],
            'block-registration-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationRemove.xml'],
            'overall-page-config-seed': ['/src/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSelenium.xml'],
            'overall-page-config-seed-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/OverallPageConfigurationSeleniumRemove.xml'],
            'student-centric-period-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationStudentCentricPeriodSeleniumRemove.xml'],
            'block-registration-group': ['/src/groovy/net/hedtech/banner/seeddata/Data/BlockRegistrationGroup.xml'],
            'recruiting-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/RecruitingSelenium.xml'],
            'recruiting-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RecruitingSeleniumRemove.xml'],
            'source-background-institution-base-selenium': ['/src/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSelenium.xml'],
            'source-background-institution-base-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/SourceBackgroundInstitutionBaseSeleniumRemove.xml'],
            'gradable-component-definition-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/GradableComponentDefinitionSeleniumRemove.xml'],
            'repeat-multiple-course-rules-selenium-remove': ['/src/groovy/net/hedtech/banner/seeddata/Data/RepeatMultipleCourseRulesSeleniumRemove.xml'],
            'gpa-rules-round' :    ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesRound.xml'],
            'gpa-rules-truncate' : ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesTruncate.xml'],
            'gpa-rules-delete' :   ['/src/groovy/net/hedtech/banner/seeddata/Data/history/AcademicHistoryGpaRulesDelete.xml'],
            'schedule-reset' : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset.xml'],
            'schedule-20151-reset' : ['/src/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Reset.xml'],
            'schedule-20151-approval' : ['/src/groovy/net/hedtech/banner/seeddata/Data/Schedule20151Approval.xml'],
            'schedule-20122-reset' : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleReset20122.xml'],
            'schedule-20122-approval' : ['/src/groovy/net/hedtech/banner/seeddata/Data/ScheduleApproval20122.xml'],
            'registration-history-reset' : ['/src/groovy/net/hedtech/banner/seeddata/Data/RegistrationHistoryTestRefresh.xml']
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
            xmlFile = prompts[0]
            def argSaveThis = prompts[1]
            saveThis = ("N" == argSaveThis ? false : true)
            def argReplaceData = prompts[2]
            replaceData = ("Y" == argReplaceData ? true : false)
            def argDebugThis = prompts[3]
            debugThis = ("Y" == argDebugThis ? true : false)
            def argShowErrors = prompts[4]
            showErrors = ("N" == argShowErrors ? false : true)
            if (!dataSource) {
                username = prompts[5]
                password = prompts[6]
                hostname = prompts[7]
                instance = prompts[8]
            }
        } else {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
            print "Enter path and file name of XML file take 3: "
            xmlFile = br.readLine()

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
        def dbConf = dataSource ? "Will use DataSource: $dataSource" : "Database connect info: $username/$password, url: $url"
        println "XML file: ${xmlFile} "


    }

    // Override the url property accessor, as it may not be initialized yet if set via interactive input
    // of the hostname and instance/sid values.


    public String getUrl() {
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
        def configFile = locateConfigFile()
        def slurper = new ConfigSlurper(GrailsUtil.environment)
        def config = slurper.parse(configFile.toURI().toURL())
        def url = config.get("bannerDataSource").url
        def db = Sql.newInstance(url,   //  db =  new Sql( connectInfo.url,
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
        def filePathName = getFilePath( System.getProperty( propertyName ) )
        if (!filePathName) {
            filePathName = getFilePath( "${System.getProperty( 'user.home' )}/.grails/${fileName}" )
        }
        if (!filePathName) {
            filePathName = getFilePath( "${fileName}" )
        }
        if (!filePathName) {
            filePathName = getFilePath( "grails-app/conf/${fileName}" )
        }
        if (!filePathName) {
            filePathName = getFilePath( System.getenv( propertyName ) )
        }
        if (!filePathName) {
            throw new RuntimeException( "Unable to locate ${fileName}" )
        }
        def configFile = new File( filePathName )
        println "using configuration: " + configFile
        return configFile
    }


    private String getFilePath( filePath ) {
        if (filePath && new File( filePath ).exists()) {
            "${filePath}"
        }
    }
}
