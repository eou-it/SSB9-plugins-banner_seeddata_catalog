/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.seeddata

import grails.test.GrailsUnitTestCase


class ColumnDateValueTests extends GrailsUnitTestCase {

    void testDateFormatOnlyDateSlash() {
        def dateToTest = "2/28/2015"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015','DDMMYYYY','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatOnlyDateDash() {
        def dateToTest = "28-02-2015"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015','DDMMYYYY','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatOnlyDateNoSep() {
        def dateToTest = "02282015"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015','DDMMYYYY','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeSlash() {
        def dateToTest = "2/28/2015 3:41:35 AM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 03:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeSlash24() {
        def dateToTest = "2/28/2015 4:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 16:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeSlashNoon() {
        def dateToTest = "2/28/2015 12:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 12:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeDash() {
        def dateToTest = "28-02-2015 3:41:35 AM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 03:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeDash24() {
        def dateToTest = "02282015 4:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 16:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }

    void testDateFormatWithTimeDashNoon() {
        def dateToTest = "28-02-2015 12:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 12:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeNoSep() {
        def dateToTest = "02282015 3:41:35 AM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 03:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeNoSep24() {
        def dateToTest = "28-02-2015 4:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 16:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }


    void testDateFormatWithTimeNoSepNoon() {
        def dateToTest = "02282015 12:41:35 PM"
        def formatter = new ColumnDateValue(dateToTest)
        assertEquals "to_date('28022015 12:41:35','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')", formatter.formatDateWithMask()
    }
}