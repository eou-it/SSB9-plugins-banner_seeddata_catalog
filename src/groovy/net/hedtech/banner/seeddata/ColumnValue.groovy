/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

/**
 * Class to define column value and format it according to the table data
 */
public class ColumnValue {

    def columnValue
    def columnType
    def columnName
    def dataScale
    def dataLength
    def InputData connectInfo
    def tableName
    Sql conn
    def indexColumns = []


    public ColumnValue(tableName, columnValue, columnType, columnScale, columnName,
                       InputData connectInfo, Sql conn, List indexColumns, columnLength) {
        this.columnValue = columnValue
        this.columnType = columnType
        this.columnName = columnName
        this.dataScale = columnScale
        this.dataLength = columnLength
        this.connectInfo = connectInfo
        this.tableName = tableName
        this.conn = conn
        this.indexColumns = indexColumns
    }

    private determineUserId(def columnName, def columnValue, def dataLength) {
        def findIfIndex = indexColumns.find { index ->
              index.COLUMN_NAME == columnName
        }
        if ( findIfIndex ) return columnValue
        else return connectInfo.userID
    }
    /**
     *  Format or frame the column value with the appropriate annotations for the sql statement
     */

    def String formatColumnValue() {
        if ((!columnType) && (!columnName) && (!tableName)) {
            String colsql = """select data_type, data_scale, data_length
                    from all_tab_columns where table_name = ?
                    and column_name = ?"""
            try {
                conn.eachRow(colsql, [tableName, columnName]) {row ->
                    columnType = row.data_type
                    dataScale = row.data_precision
                    dataLength = row.data_length
                }
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "${colsql}"
                    println "Problem selecting columns in formatColumnValue ColumnValueDML.groovy: $e.message"
                }
            }
        }
        String valsql = ""
        if (columnName =~ "PIDM" && connectInfo.saveStudentPidm) { valsql = connectInfo.saveStudentPidm}
        else if (columnName =~ "ACTIVITY_DATE")
        // activity date will always be set to 1/1/2010
        { valsql = "to_date('01012010','MMDDYYYY') " }
        else if (columnName == "${this.tableName}_SURROGATE_ID") { valsql = "null"}
        else if (columnName =~ "VERSION") { valsql = "null" }
        else if (columnName =~ "_DATA_ORIGIN") {valsql = "'" + connectInfo.dataOrigin + "'" }
        else if (columnName == this.tableName + "_USER") {valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'" }
        else if (columnName == this.tableName + "_USER_ID") {valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'" }
        else if (columnName == this.tableName + "_USERID") {valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'" }

        else {
            if (columnType == "RAW") { valsql = "null" }
            else if (columnType == "NUMBER") {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) { valsql = "null"}
                else {
                    if (this.dataScale){
                        valsql = columnValue.toString()
                    }
                    else  valsql = columnValue.toString()

                }
            }
            else if ((columnType == "VARCHAR2") || (columnType == 'CLOB')) {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) {
                    valsql = "null"
                }
                else {  // reduce to smaller size
                    String col = columnValue.toString()
                    if (col.length() > 3000) {
                        //  column is too big ${columnName} ${columnValue.length()} 
                        def colv = col.substring(0, 3000)
                        col = colv
                    }

                    // replace all ' with '' so they will load
                    if (col =~ /\'/) {
                        def newcolval = col.replaceAll(/'/, '')
                        valsql = "'${newcolval}'"
                    }
                    else {valsql = "'${col}'" }
                }
            }
            else if (columnType == "DATE") {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) { valsql = "null" }
                //  date is in dd-mon-yyyy, mm/dd/yyyy or mmddyyyy format
                else {
                    def ddate = new ColumnDateValue(columnValue.toString() )
                    valsql = ddate.formatDateWithMask()
                }
            }
        }

        return valsql
    }

}
