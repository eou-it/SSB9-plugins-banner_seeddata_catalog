/*********************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection
import java.sql.SQLException

class BinaryObjectDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public BinaryObjectDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns,
                            List indexColumns, Batch batch,
                            def deleteNode ) {
        println( "BinaryObjectDML called" )
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGorblob()
        println( "BinaryObjectDML end" )

    }

    /**
     * Process the GORBLOB records.  Use the dynamic insert process  but load the file into the blob
     * using java file
     *
     */

    def processGorblob() {
        def apiData = new XmlParser().parseText( xmlData )

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord( connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode )
        def xmlFileParent = new File( connectInfo.xmlFilePath ).getParent().toString()
        def reportFile = "$xmlFileParent" + File.separator + "${apiData.GORBLOB_MEDIA_ID.text()}.txt"
        if (connectInfo.debugThis) {
            println "processGorblob: upload file for report: ${apiData.GORBLOB_MEDIA_ID.text()} sample file: ${reportFile} "
        }
        FileInputStream stream
        File repFile
        try {
            repFile = new File( reportFile )
            stream = new FileInputStream( repFile )
            byte[] fileBytes = new byte[(int) repFile.length()]
            stream.read( fileBytes )

            def jobRecord = "select gorblob_surrogate_id gblob from gorblob where GORBLOB_MEDIA_ID = ? "
            def jobrec = conn.firstRow( jobRecord, [apiData.GORBLOB_MEDIA_ID.text()] )

            def updateSql
            try {
                String updateRec = "update gorblob set GORBLOB_BLOB = ? where gorblob_surrogate_id = ?"
                conn.executeUpdate( updateRec, [fileBytes, jobrec.gblob] )
                connectInfo.tableUpdate( "GORBLOB", 0, 0, 1, 0, 0 )
            }
            catch (SQLException ae) {
                if (connectInfo.showErrors) {
                    println "Problem executing update of jobsub file ${apiData.GORBLOB_MEDIA_ID.text()}  from BinaryObjectDML.groovy for ${connectInfo.tableName}: $ae.message"
                }
                connectInfo.tableUpdate( "GORBLOB", 0, 0, 0, 1, 0 )

            }
            finally {
                try {
                    if (updateSql != null) updateSql.close()
                } catch (Exception e) { /* ignore */
                }
            }
        }
        catch (IOException io) {
            if (connectInfo.showErrors) {
                println "IOException executing getting file stream ${apiData.GORBLOB_MEDIA_ID.text()} from BinaryObjectDML.groovy for ${connectInfo.tableName}: $io.message"
            }
            io.printStackTrace()
        }
        catch (Exception ee) {
            if (connectInfo.showErrors) {
                println "Exception executing getting file stream ${apiData.GORBLOB_MEDIA_ID.text()} from BinaryObjectDML.groovy for ${connectInfo.tableName}: $ee.message"
            }
            ee.printStackTrace()
        }
        finally {
            try {
                if (stream != null)
                    stream.close()
            } catch (IOException ex) {
                ex.printStackTrace()
            }
        }


    }


    def getPluginDirectoryPathString() {
        def pInfo = pluginSettings.getPluginInfos().find {it.name.equals( "banner-seeddata-catalog" )}
        println "plugin directory ${pInfo.toString()}"
        pInfo.pluginDir.path.toString()
    }
}
