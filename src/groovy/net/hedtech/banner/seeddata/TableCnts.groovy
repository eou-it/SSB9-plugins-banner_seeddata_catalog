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
