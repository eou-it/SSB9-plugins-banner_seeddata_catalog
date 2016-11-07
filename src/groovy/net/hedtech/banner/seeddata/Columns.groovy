/*********************************************************************************
 Copyright 2010-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

/**
 * Columns that are part of xml table elements
 * Create sql to insert and sql to check for record existence
 */
public class Columns {


    def xmlString
    List columns
    List indexColumns
    def columnValues = []
    def parseColValues = []
    String insertSQL
    String indexSQL
    String updateSQL
    def InputData connectInfo
    def tableName
    def saveTable
    Sql conn
    def xmlRec

    def multiplePidmColumn


    public Columns(tableName, InputData connectInfo, Sql conn, xmlString, List columns, List indexColumns, xmlRec) {

        this.connectInfo = connectInfo
        this.tableName = tableName
        this.conn = conn
        this.xmlString = xmlString
        this.columns = columns
        this.indexColumns = indexColumns
        this.xmlRec = xmlRec
        this.multiplePidmColumn = columns.findAll { it.column_name =~ "PIDM" }?.size() > 1 ? true : false
        if (connectInfo.debugThis) {
            println "MultiplePidms in table ${tableName} ${multiplePidmColumn} "
        }

    }


    def dataFoundInXml = { columnName ->
        if (connectInfo.saveStudentPidm && columnName =~ "PIDM") return true
        def dataFoundInValues = columnValues.find { it.column == columnName }
        if (!dataFoundInValues) {
            if (columnName =~ "ACTIVITY_DATE" ||
                    columnName =~ "DATA_ORIGIN" ||
                    columnName =~ "USER_ID"
            ) return true
            else return false
        } else return true
    }
    /**
     *   Create the insert SQl statement
     */

    def String createInsertSQL() {

        // step 1:  put the values in the xml element into an array

        this.columnValues = []

        def data = new XmlSlurper().parseText(xmlRec)
        data.each { table ->
            table.children().each() { fields ->
                def value
                if ((this.tableName in ['GCBQURY','GCRCFLD','GCBTMPL']))
                    value = fields.text()
                else
                        value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '')
                if (value) {
                    def map = [column: fields.name(), value: value]
                    this.columnValues.add(map)
                }
            }
        }

        def colCnt = 0
        def insCnt = 0
        def ownerString = connectInfo?.owner ? connectInfo.owner + "." : ""
        String valsql = "insert into ${ownerString + this.tableName} ("
        // step 2: build insert part of statement pulling the column names from the columns array

        // loop through all columns and parse together an insert statement
        //  columns is from the all tab columns table
        //  the end result will be something like insert into tablename ( column1, column2, column3)
        columns.each { columns ->

            if ((columns.column_name == "${this.tableName}_SURROGATE_ID")
                    || (columns.column_name =~ "VPDI_CODE" &&
                    (this.tableName != 'GTVVPDI' && this.tableName != 'GURUSRI'))
                    || (columns.column_name =~ "VERSION")) {
            } else {
                def dataFound = dataFoundInXml.call(columns.column_name)
                if (dataFound) {
                    if (insCnt > 0) {
                        valsql += ", ${columns.column_name}\n"
                    } else {
                        valsql += "${columns.column_name}\n"
                    }
                    insCnt++
                }
            }

        }

        // step 3: build values part of statement
        // this assumes the xml is sending values for all not null columns
        valsql += ") values ( "
        colCnt = 0
        insCnt = 0
        columns.each { columns ->

            if ((columns.column_name == "${this.tableName}_SURROGATE_ID")
                    || (columns.column_name =~ "VPDI_CODE" &&
                    (this.tableName != 'GTVVPDI' && this.tableName != 'GURUSRI'))
                    || (columns.column_name =~ "VERSION")) {
            } else {
                def dataFound = dataFoundInXml.call(columns.column_name)
                if (dataFound) {
                    if (insCnt > 0) {
                        valsql += " , \n"
                    } else {
                        valsql += "  \n"
                    }
                    insCnt++

                    def columnNode = columnValues.find {
                        it.column.value =~ columns.column_name &&
                                it.column.length() == columns.column_name.length()
                    }

                    def colValue
                    if (columnNode) {
                        colValue = columnNode.value.value
                    } else {
                        colValue = null
                    }

                    def parseCol = new ColumnValue(this.tableName, colValue, columns.data_type, columns.data_scale,
                            columns.column_name, connectInfo, conn, this.indexColumns, columns.data_length,
                            this.multiplePidmColumn)
                    def svalSql = parseCol.formatColumnValue()


                    def colMap = [column_name: columns.column_name, value: svalSql]
                    parseColValues << colMap
                    valsql += svalSql
                }
            }
            colCnt++
        }

        valsql += ")\n "

        return valsql
    }

    /**
     * Parse index method must be called after the create sql because it needs the parsed column values
     * This method finds the columns for the primary key and creates the SQL used to match the
     * current value to an existing value
     */

    def String createIndexSQL() {
        this.indexSQL = ""

        // fetch the index columns into list
        def colCnt = 0
        def indCnt = 0
        def colPos = 0
        // ignore index for gubinst and sobctrl because it has 1 record
        if ((this.tableName != "GUBINST") && (this.tableName != "SOBCTRL")) {
            while (colCnt < indexColumns.size()) {

                def colInd = columns.find { col -> col.column_name == indexColumns.column_name[colCnt] }
                if (colInd != null) {
                    def colValue = parseColValues.find { it.column_name == indexColumns.column_name[colCnt] }
                    def dataFound = dataFoundInXml.call(indexColumns.column_name[colCnt])
                    // need to format the value so it is framed with qoutes or to date
                    def indVal = colValue?.value
                    def connector = ""
                    if (indVal == "null" || !dataFound) {
                        connector = "is"
                    } else {
                        connector = "="
                    }

                    if (indCnt > 0) {
                        indexSQL += " and ${indexColumns.column_name[colCnt]} ${connector} ${indVal} \n"
                    } else {
                        indexSQL = " where ${indexColumns.column_name[colCnt]} ${connector} ${indVal} \n"
                    }
                    indCnt++
                }
                colCnt++
            }
        }
        def ownerString = connectInfo?.owner ? connectInfo.owner + "." : ""
        String matchSQL = "Select 'x' from ${ownerString + this.tableName}  " + this.indexSQL
        return matchSQL
    }

    /**
     *  This method creates the sql to update the row with non key  elements, like the description
     *  it requires the createSQL and parseIndex be run before
     */

    def String createUpdateSQL() {
        def ownerString = connectInfo?.owner ? connectInfo.owner + "." : ""
        String updateSQL = "update ${ownerString + this.tableName} set "
        def colCnt = 0
        def colPos = 0
        def updCnt = 0

        def surrogateIndex = this.indexColumns.find { ind ->
            ind.column_name == "${this.tableName}_SURROGATE_ID"
        }
        Boolean indexHasSurrogate = surrogateIndex ? true : false
        while (colCnt < columns.size()) {
            // need to find if this is part of the index, we dont want to update it
            def inIndex = ""
            inIndex = this.indexColumns.find { ind -> ind.column_name == columns.column_name[colCnt] }

            if (((!doesIndexHaveSurrogateId() && columns.column_name[colCnt] == "${this.tableName}_SURROGATE_ID")) ||
                    (columns.column_name[colCnt] =~ "VERSION") ||
                    (columns.column_name[colCnt] =~ "VPDI_CODE") ||
                    ((inIndex != null) && (this.tableName != "SOBCTRL"))) {
                // do not include in the update if this is part of the index or the version,surrogate
            } else {
                def dataFound = dataFoundInXml.call(columns.column_name[colCnt])
                if (dataFound) {
                    def colValue = parseColValues.find { it.column_name == columns.column_name[colCnt] }
                    if (updCnt == 0) {
                        updateSQL += " ${columns.column_name[colCnt]} = ${colValue.value} \n "
                    } else {
                        updateSQL += " , ${columns.column_name[colCnt]} = ${colValue.value} \n"
                    }
                    updCnt++
                }
            }
            colCnt++
        }
        updateSQL += this.indexSQL
        return updateSQL
    }


    def Boolean doesIndexHaveSurrogateId() {

        def surrogateIndex = this.indexColumns.find { ind ->
            ind.column_name == "${this.tableName}_SURROGATE_ID"
        }
        return surrogateIndex ? true : false
    }

    /**
     *  This method creates the sql to delete row(s) with values in the xml record
     *  it requires the createSQL and parseIndex be run before
     */

    def String createDeleteSQL() {

        def deleteColumns = []

        def data = new XmlSlurper().parseText(xmlRec)
        data.each { table ->
            table.children().each() { fields ->
                def value = fields.text().replaceAll(/&/, '').replaceAll(/'/, '')
                if (value && fields.name() != "DELETE") {
                    def map = [column: fields.name(), value: value]
                    deleteColumns.add(map)
                }
            }
        }

        String deleteSQL = "delete from ${this.tableName} where "
        def colCnt = 0
        def updCnt = 0
        while (colCnt < deleteColumns.size()) {
            def columnData = columns.find { it.column_name == deleteColumns[colCnt].column }
            if (columnData) {

                def parseCol = new ColumnValue(this.tableName, deleteColumns[colCnt].value,
                        columnData?.data_type, columnData?.data_scale,
                        columnData?.column_name, connectInfo, conn, this.indexColumns, columns.data_length,
                        this.multiplePidmColumn)
                def deleteValue = parseCol.formatColumnValue()

                if (updCnt == 0) {
                    deleteSQL += " ${columnData.column_name} = ${deleteValue} \n "
                } else {
                    deleteSQL += " AND ${columnData.column_name} = ${deleteValue} \n"
                }
                updCnt++
            }
            colCnt++
        }

        return deleteSQL
    }

}


