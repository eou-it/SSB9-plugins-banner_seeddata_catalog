package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection
import java.sql.SQLException

class JobsubSavedOutputDml {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def Batch batch
    def deleteNode


    public JobsubSavedOutputDml(InputData connectInfo, Sql conn, Connection connectCall, xmlData, List columns,
                                List indexColumns, Batch batch,
                                def deleteNode) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        this.columns = columns
        this.indexColumns = indexColumns
        this.deleteNode = deleteNode
        processGjrjlis()

    }

    /**
     * Process the gjrjlis records.  Use the dynamic insert process  but load the file into the blob
     * using java file
     *
     */

    def processGjrjlis() {
        def apiData = new XmlParser().parseText(xmlData)

        // parse the data using dynamic sql for inserts and updates
        def valTable = new DynamicSQLTableXMLRecord(connectInfo, conn, connectCall, xmlData, columns, indexColumns, batch, deleteNode)
        def xmlFileParent = new File(connectInfo.xmlFilePath).getParent().toString()
        def reportFile
        if (apiData.GJRJLIS_FILE_NAME.text() =~ ".lis"){
            reportFile = "$xmlFileParent" + File.separator + "jobsubReportPlainText.lis"
        }
        else {
            reportFile = "$xmlFileParent" + File.separator + "jobsubReportOutput.pdf"
        }
        if (connectInfo.debugThis) {
            println "ProcessGjrjlis: upload file for report: ${apiData.GJRJLIS_FILE_NAME.text()} sample file: ${reportFile} "
        }
        FileInputStream pdfStream
        File repFile
        try {
            repFile = new File(reportFile)
            pdfStream = new FileInputStream(repFile)
            byte[] fileBytes = new byte[(int) repFile.length()];
            pdfStream.read(fileBytes);
            pdfStream.close();

            def jobRecord = "select gjrjlis_surrogate_id jlis from gjrjlis where gjrjlis_job = ? and gjrjlis_one_up_no = ? and gjrjlis_file_name = ?"
            def jobrec = conn.firstRow(jobRecord, [apiData.GJRJLIS_JOB.text(), apiData.GJRJLIS_ONE_UP_NO.text(), apiData.GJRJLIS_FILE_NAME.text()])
            println "surrogate ID: ${jobrec.jlis}"
            def updateSql
            try {
                String updateRec = "update gjrjlis set gjrjlis_file = ? where gjrjlis_surrogate_id = ?"
                conn.executeUpdate(updateRec, [fileBytes, jobrec.jlis])
                connectInfo.tableUpdate("GJRJLIS", 0, 0, 1, 0, 0)
            }
            catch (SQLException ae) {
                if (connectInfo.showErrors) {
                    println "Problem executing update of jobsub file ${apiData.GJRJLIS_JOB.text()} ${apiData.GJRJLIS_ONE_UP_NO.text()} from JobsubSavedOutputDML.groovy for ${connectInfo.tableName}: $ae.message"
                }
                connectInfo.tableUpdate("GJRJLIS", 0, 0, 0, 1, 0)

            }
            finally {
                try {
                    if (updateSql != null) updateSql.close();
                } catch (Exception e) { /* ignore */
                }
            }
        }
        catch (IOException io) {
            if (connectInfo.showErrors) {
                println "IOException executing getting file stream ${apiData.GJRJLIS_JOB.text()} ${apiData.GJRJLIS_ONE_UP_NO.text()} from JobsubSavedOutputDML.groovy for ${connectInfo.tableName}: $io.message"
            }
            io.printStackTrace();
        }
        catch (Exception ee) {
            if (connectInfo.showErrors) {
                println "Exception executing getting file stream ${apiData.GJRJLIS_JOB.text()} ${apiData.GJRJLIS_ONE_UP_NO.text()} from JobsubSavedOutputDML.groovy for ${connectInfo.tableName}: $ee.message"
            }
            ee.printStackTrace();
        }
        finally {
            try {
                if (pdfStream != null)
                    pdfStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }


    def getPluginDirectoryPathString() {
        def pInfo = pluginSettings.getPluginInfos().find { it.name.equals("banner-seeddata-catalog") }
        println "plugin directory ${pInfo.toString()}"
        pInfo.pluginDir.path.toString()
    }
}
