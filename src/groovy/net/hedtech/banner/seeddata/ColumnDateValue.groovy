/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

/**
 *  Format a date that is read in as string value from an XML file
 *  This assumes the date coming in is one of these three formats
 *      dd-mon-yy   or dd-mon-yyyy must be english
 *      mm/dd/yy or mm/dd/yyyy
 *      mmddyy or mmddyyyy
 * */

public class ColumnDateValue {

    def inDate
    def separater
    def dateMask


    public ColumnDateValue(inDate) {
        this.inDate = inDate
        if (this.inDate =~ /-/) { this.separater = "-" }
        else if (this.inDate =~ /\//) { this.separater = "/" }
        else { this.separater = "" }
    }


    def String formatDateWithMask() {
        def MM = 0
        def DD = 0
        def YY = 0
        def HH = 0
        def MMM = 0
        def SS = 0
        def splCnt = 0
        def YYy = ""
        def AMPM = ""
        def inTime = ""
        def hasTime = false

        if (inDate.endsWith("PM") || inDate.endsWith("AM")) {
            hasTime = true
            inTime = getTimeFromDate(inDate)
        }
        String valsql = ""
        if (this.separater) {
            this.inDate.split(this.separater).each {
                splCnt++
                if (this.separater == "-") {
                    if (splCnt == 1) { DD = it }
                    else if (splCnt == 2) { MM = it }
                }
                else {
                    if (splCnt == 1) { MM = it }
                    else if (splCnt == 2) { DD = it }
                }
                if (splCnt == 3) {
                    YY = it
                    if (YY =~ / /) {
                        def ddate2 = it.split(" ")
                        YY = ddate2[0]
                    }

                    if (YY.length() > 4) {
                        YYy = YY.substring(0, 4).trim()
                    }
                    else { YYy = YY.trim() }
                }
            }
        }
        else {
            def stop = inDate.length()
            if (hasTime) {
                stop = inDate.indexOf(" ")
            }
            MM = inDate.substring(0, 2)
            DD = inDate.substring(2, 4)
            if (inDate.length() <= 6) {
                YYy = inDate.substring(4, stop)
            }
            else { YYy = inDate.substring(4, stop) }
        }
        MM = MM.toString().padLeft(2,'0')
        DD = DD.toString().padLeft(2,'0')
        def yycen = 0
        if (YYy.length() == 2) {
            if (YYy.toInteger() > 30) { yycen = YYy.toInteger() + 1900 }
            else { yycen = YYy.toInteger() + 2000 }
        }
        else { yycen = YYy.toInteger() }
        if (hasTime) {
            valsql = "to_date('${DD}${MM}${yycen.toString()} ${inTime}','DDMMYYYY HH24:MI:SS','NLS_CALENDAR=GREGORIAN')"
        }
        else {
            valsql = "to_date('${DD}${MM}${yycen.toString()}','DDMMYYYY','NLS_CALENDAR=GREGORIAN')"
        }

        return valsql
    }


    def String formatJavaDate() {

        def DD = 0
        def YY = 0
        def MM = 0
        def splCnt = 0
        def YYy = ""
        if (this.separater) {
            this.inDate.split(this.separater).each {
                splCnt++
                if (this.separater == "-") {
                    if (splCnt == 1) { DD = it }
                    else if (splCnt == 2) { MM = it }
                }
                else {
                    if (splCnt == 1) { MM = it }
                    else if (splCnt == 2) { DD = it }
                }
                if (splCnt == 3) {
                    YY = it
                    if (YY =~ / /) {
                        def ddate2 = it.split(" ")
                        YY = ddate2[0]
                    }

                    if (YY.length() > 4) {
                        YYy = YY.substring(0, 4).trim()
                    }
                    else { YYy = YY.trim() }
                }
            }
        }
        else {
            MM = inDate.substring(0, 2)
            DD = inDate.substring(2, 4)

            if (inDate.length() <= 6) {
                YYy = inDate.substring(4)
            }
            else {
                YYy = inDate.substring(4)
            }
        }

        // change the alpha month to numeric
        if (MM.toUpperCase() == "JAN") { MM = "01" }
        else if (MM.toUpperCase() == "FEB") { MM = "02" }
        else if (MM.toUpperCase() == "MAR") { MM = "03" }
        else if (MM.toUpperCase() == "APR") { MM = "04" }
        else if (MM.toUpperCase() == "MAY") { MM = "05" }
        else if (MM.toUpperCase() == "JUN") { MM = "06" }
        else if (MM.toUpperCase() == "JUL") { MM = "07" }
        else if (MM.toUpperCase() == "AUG") { MM = "08" }
        else if (MM.toUpperCase() == "SEP") { MM = "09" }
        else if (MM.toUpperCase() == "OCT") { MM = "10" }
        else if (MM.toUpperCase() == "NOV") { MM = "11" }
        else if (MM.toUpperCase() == "DEC") { MM = "12" }

        // make sure year has the century

        def yycen = 0
        if (YYy.length() == 2) {
            if (YYy.toInteger() > 30) { yycen = YYy.toInteger() + 1900 }
            else { yycen = YYy.toInteger() + 2000 }
        }
        else { yycen = YYy.toInteger() }
        String dateOut = "${yycen}-${MM}-${DD}"

        return dateOut
    }


    private def getTimeFromDate(def theDate) {
        def hours = ""
        def minutes = ""
        def seconds = ""
        def timeL = []

        def totalTime = theDate.substring(theDate.indexOf(" ")+1, theDate.length())
        def AMPM = totalTime.substring(totalTime.indexOf(" ")+1, totalTime.length())
        def justTime = totalTime.substring(0, totalTime.indexOf(" "))
        timeL = justTime.split(":")
        hours = withPadding(timeL[0], 2)
        minutes = withPadding(timeL[1], 2)
        seconds = withPadding(timeL[2], 2)

        if (AMPM == 'PM') {
            def hoursI = hours.toInteger() + 12
            hours = "" + hoursI
        }

        justTime = hours + ":" + minutes + ":" + seconds

        return justTime
    }


    private def withPadding(def theNumber, def theLength) {
        def returnMe = ""
        for (def i=theNumber.length(); i<theLength; i++ ) {
            returnMe = returnMe + "0"
        }
        returnMe = returnMe + theNumber
        return returnMe
    }

}
