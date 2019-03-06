/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import java.sql.Connection
import java.sql.Statement
import java.sql.BatchUpdateException
import java.sql.SQLException

/**
 * Class to establish the JDBC statement that is used for batch SQL processing
 * The addBatch is not supported for procedures with inout or out parameters
 * which includes all Banner APIs
 */
public class Batch {
    Statement addBatchSt = null
    Connection connectCall
    def InputData connectInfo
    def cntAdds = 0


    public Batch(Connection connectCall, InputData connectInfo) {
        this.connectCall = connectCall
        this.connectInfo = connectInfo
        createBatch()

    }


    def addBatchSQL(String sql) {
        addBatchSt.addBatch(sql);
        cntAdds++
    }


    def createBatch() {
        addBatchSt = this.connectCall.createStatement()
    }


    def executeBatch() {
        if ((addBatchSt != null) && (cntAdds > 10000)) {
            processBatch()
            createBatch()
        }

    }


    def executeEndBatch() {
        if (addBatchSt != null) {
            processBatch()
        }
    }


    def processBatch() {
        try {
            def noRows = addBatchSt.executeBatch()
            if (connectInfo.showErrors) println "Number of rows processed in addBatch ${noRows.size()}, number expected ${cntAdds}"
        }
        catch (BatchUpdateException e) {
            if (connectInfo.showErrors) println "Batch Update ERROR: Could not execute the batch statement. ${e.message}"
            if (connectInfo.showErrors) println "Number of rows processed in addBatch ${e.getUpdateCounts().size() }, number expected ${cntAdds}"

        }
        catch (SQLException se) {
            if (connectInfo.showErrors) println "At SQL exception: ${se}"
        }
        catch (Exception ex) {
            if (connectInfo.showErrors) println "At  exception: ${ex}"
        }
        finally {
            addBatchSt.close()
            addBatchSt = null
            cntAdds = 0
        }

    }

}
