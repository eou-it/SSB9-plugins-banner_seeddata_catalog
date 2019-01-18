/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
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
    def multiplePidmColumn


    public ColumnValue(tableName, columnValue, columnType, columnScale, columnName,
                       InputData connectInfo, Sql conn, List indexColumns, columnLength,
                       multiplePidmColumn) {
        this.columnValue = columnValue
        this.columnType = columnType
        this.columnName = columnName
        this.dataScale = columnScale
        this.dataLength = columnLength
        this.connectInfo = connectInfo
        this.tableName = tableName
        this.conn = conn
        this.indexColumns = indexColumns
        this.multiplePidmColumn = multiplePidmColumn
    }


    private determineUserId(def columnName, def columnValue, def dataLength) {
        def findIfIndex = indexColumns.find { index ->
            index.COLUMN_NAME == columnName
        }
        if (findIfIndex) return columnValue
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
                conn.eachRow(colsql, [tableName, columnName]) { row ->
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
        if ((multiplePidmColumn && columnName == this.tableName + "_PIDM" && connectInfo.saveStudentPidm) ||
                (!multiplePidmColumn && columnName =~ "_PIDM" && connectInfo.saveStudentPidm)) {
            valsql = connectInfo.saveStudentPidm
        } else if (columnName =~ "PROXY_IDM" && connectInfo.saveProxyIdm) {
            valsql = connectInfo.saveProxyIdm
        } else if (columnName =~ "ACTIVITY_DATE")
        // activity date will always be set to 1/1/2010
        {
            valsql = "to_date('01012010','MMDDYYYY') "
        } else if (!doesIndexHaveSurrogateId() && columnName == "${this.tableName}_SURROGATE_ID") {
            valsql = "null"
        } else if (columnName =~ "VERSION") {
            valsql = "null"
        } else if (columnName =~ "_DATA_ORIGIN") {
            valsql = "'" + connectInfo.dataOrigin + "'"
        } else if (columnName == this.tableName + "_USER") {
            valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'"
        } else if (columnName == this.tableName + "_USER_ID") {
            valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'"
        } else if (columnName == this.tableName + "_USERID") {
            valsql = "'" + determineUserId(columnName, columnValue, dataLength) + "'"
        } else {
            if (columnType == "RAW") {
                valsql = "null"
            } else if (columnType == "NUMBER") {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) {
                    valsql = "null"
                } else {
                    if (this.dataScale) {
                        valsql = columnValue.toString()
                    } else valsql = columnValue.toString()

                }
            } else if ((columnType == "VARCHAR2") || (columnType == "CHAR") || (columnType == 'CLOB')) {
                if (( (columnType == "VARCHAR2") || (columnType == "CHAR") ) && ((columnValue =~ "null") || (!columnValue) || (columnValue == " "))) {
                    valsql = "null"
                } else {  // reduce to smaller size
                    String col = columnValue.toString()
                    if (col.length() > 3000 && !(columnName in ["GORRSQL_WHERE_CLAUSE", "GORRSQL_PARSED_SQL"])) {
                        //  column is too big ${columnName} ${columnValue.length()}
                        def colv = col.substring(0, 3000)
                        col = colv
                    }
                    // replace all ' with '' so they will load
                    if (( (columnType == "VARCHAR2") || (columnType == "CHAR") ) && (col =~ /\'/)) {
                        def newcolval = col.replaceAll(/'/, '')
                        valsql = "'${newcolval}'"
                    } else {
                        valsql = "'${col}'"
                    }

                }
            } else if (columnType == "DATE") {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) {
                    valsql = "null"
                }
                //  date is in dd-mon-yyyy, mm/dd/yyyy or mmddyyyy format
                else {
                    def ddate = new ColumnDateValue(columnValue.toString())
                    valsql = ddate.formatDateWithMask()
                }
            } else if (columnType == "TIMESTAMP(3)" ||columnType == "TIMESTAMP(6)" ) {
                if ((columnValue =~ "null") || (!columnValue) || (columnValue == " ")) {
                    valsql = "null"
                } else {
                    valsql = "to_timestamp('${columnValue.toString()}','YYYY-MM-DD HH24:MI:SS.FF','NLS_CALENDAR=GREGORIAN')"
                }
            }
        }

        if (connectInfo.debugThis && columnName =~ "PIDM") {
            println "MultiplePidms in table ${multiplePidmColumn} column name ${columnName} value ${valsql}"
        }

        return valsql
    }


    def Boolean doesIndexHaveSurrogateId() {

        def surrogateIndex = this.indexColumns.find { ind ->
            ind.column_name == "${this.tableName}_SURROGATE_ID"
        }
        return surrogateIndex ? true : false
    }
}
