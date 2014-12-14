/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

/**
 * Select columns from table and store in arrays
 * The arrays are used to parse the insert, update and match statements
 * for tables that do not have APIs
 */
public class Tables {

    List columns
    List columnNames
    List indexColumns

    def InputData connectInfo
    def tableName
    Sql conn


    public Tables(tableName, InputData connectInfo, Sql conn, List columnNames) {

        this.connectInfo = connectInfo
        this.tableName = tableName
        this.conn = conn
        this.columnNames = columnNames
        selectColumns()

    }


    def selectColumns() {

        String ownerSql = """ select owner from all_tables where table_name = ?"""

        def owner = conn.firstRow(ownerSql, [this.tableName])
        if (!owner) {
            ownerSql = """ select owner from all_views where view_name = ?"""
            owner = conn.firstRow(ownerSql, [this.tableName])

        }

        String colsql = """select table_name, column_name, data_type, column_id , DATA_SCALE , data_length
                              from all_tab_columns where table_name = ?
                              and owner = ?"""

        if (connectInfo.debugThis) {
            println "${colsql}"
        }

        columns = []
        def tabColumns = []
        try {
            tabColumns = conn.rows(colsql, [this.tableName, owner.owner])
        }
        catch (Exception e) {
            if (connectInfo.showErrors) {
                println "Problem selecting columns for ${this.tableName} in Tables.groovy: $e.message"
                println "${colsql}"
            }
        }
        // siphon off the columns from all tables with xml data
//        columnNames.each {col ->
//            def columnQuery = tabColumns.find {  it.column_name == col}
//            if (columnQuery != null) {
//                columns << columnQuery
//            }
//        }
        // copy all table columns to the list columns which is saved off later on
        tabColumns.each { col ->
            columns << col
        }

        // add the user ID and data origin column  if they don't exist in the xml but
        // it does in the all tables
//        def dataOrigin = this.tableName.toUpperCase() + "_DATA_ORIGIN"
//        def userId = this.tableName.toUpperCase() + "_USER_ID"
//
//        def dataFound = tabColumns.find {it.column_name == dataOrigin}
//        if (dataFound) {
//            def dataXML = columnNames.find { it == dataOrigin}
//            if (dataXML == null) {
//                columns << dataFound
//            }
//        }
//        dataFound = tabColumns.find {it.column_name == userId}
//        if (dataFound != null) {
//            def dataXML = columnNames.find { it == userId}
//            if (dataXML == null) {
//                columns << dataFound
//            }
//        }

        // find out if there is only 1 index
        String selIndexCntSQL = """select * from all_indexes ind where ind.table_name = ?
        and ind.owner = ?"""
        def cntIndexes = conn.rows(selIndexCntSQL, [this.tableName, owner.owner])?.size()

        if (cntIndexes > 1) {
            // find columns associated with the index
            String selIndexSQL = """select  cc.table_name table_name,
                cc.column_name column_name
                from  all_ind_columns cc, all_indexes ind
                where ind.table_name = ?
                  and ind.owner = ?
                  and cc.column_name not like '%SURROGATE_ID'
                  and cc.table_name = ind.table_name
                  and cc.table_owner = ind.table_owner
                  and cc.index_name = ind.index_name
                  and   ind.uniqueness = 'UNIQUE'
                  and ( ind.index_name like 'PK%'  or
                  ind.index_name like  ind.table_name || '_KEY_INDEX'  or
                  ind.index_name like  'UK_%'  or
                  ind.index_name like  'UK2_%'  or
                  ind.index_name like  ind.table_name || '_KEY_INDEX1'  or
                  ind.index_name like  ind.table_name || '_KEY1_INDEX')
                  order by ind.index_name, cc.column_position  """
            if (connectInfo.debugThis) {
                println "${selIndexSQL}"
            }

            // fetch the index columns into list
            def colcnt = 0
            try {
                indexColumns = conn.rows(selIndexSQL, [this.tableName, owner.owner])
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem selecting indexes for ${this.tableName} in Tables.groovy: $e.message"
                    println "${selIndexSQL}"
                }
            }
        } else {
            String selIndexSQL = """select  cc.table_name table_name,
               cc.column_name column_name
               from  all_ind_columns cc, all_indexes ind
               where ind.table_name = ?
                  and ind.owner = ?
                  and cc.table_name = ind.table_name
                  and cc.table_owner = ind.table_owner
                  and cc.index_name = ind.index_name
                  order by ind.index_name, cc.column_position  """
            if (connectInfo.debugThis) {
                println "${selIndexSQL}"
            }

            // fetch the index columns into list
            def colcnt = 0
            try {
                indexColumns = conn.rows(selIndexSQL, [this.tableName, owner.owner])
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem selecting indexes for ${this.tableName} in Tables.groovy: $e.message"
                    println "${selIndexSQL}"
                }
            }
        }
        if (connectInfo.debugThis) {
            println "${this.tableName} index columns selected ${columns?.size()} index columns selected ${indexColumns?.size()}"
            println "${columns}"
        }
        if (indexColumns.size() == 0) {
            def selIndexSQL2 = """select table_name, column_name, data_type, column_id , DATA_SCALE
                                    from all_tab_columns where table_name = ?
                                     and owner = ?
                                     and data_type not in ('CLOB','RAW')
                                     and ( column_name not like  '%ACTIVITY_DATE'
                                     and column_name not like  '%USER_ID'
                                     and column_name not like  '%DATA_ORIGIN' )
                                    order by column_id"""
            try {
                indexColumns = conn.rows(selIndexSQL2, [this.tableName, owner.owner])
            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem selecting indexes on table with no index for ${this.tableName} in Tables.groovy: $e.message"
                    println "${selIndexSQL2}"
                }
            }
            if (connectInfo.debugThis) {
                println "${this.tableName} columns for table with no index selected ${columns.size()} columns selected ${indexColumns.size()}"
            }
        }
    }

    // this may not be used, it is kind of expensive


    def boolean foreignKeyRefExists() {

        String keyCheck = """select constraint_name
              from all_constraints
              where constraint_type='R'
               and constraint_name like '%' || ? || '%'
               and table_name <> ?"""

        if (conn.rows(keyCheck, [this.tableName, this.tableName]).size() > 0) {
            return true
        }
        return false
    }

}
