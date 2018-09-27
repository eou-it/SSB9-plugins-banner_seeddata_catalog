/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

/**
 * Class to define table object to store crud activity that is printed at the end of the process 
 */
public class TableCnts {

    def tableName
    long insertCnt
    long errorCnt
    long updateCnt
    long readCnt
    long deleteCnt

     public TableCnts(tableName, readCnt, insertCnt, updateCnt, errorCnt, deleteCnt) {

     this.tableName = tableName
     this.insertCnt = insertCnt
     this.errorCnt = errorCnt
     this.updateCnt = updateCnt
     this.readCnt = readCnt
     this.deleteCnt = deleteCnt

   }

}
