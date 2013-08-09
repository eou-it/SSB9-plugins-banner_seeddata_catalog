/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import java.sql.Connection
import java.sql.Statement

/**
 * Inserts or refreshes seed data for banner entities.  This may be executed within 
 * the 'Horizon' grails environment by using 'grails seed-data catalog'. It may also be executed 
 * directly via the command line, using only groovy (i.e., having no dependency on a grails project). 
 * When run outside of grails, this script requires groovy-binary-1.6.7.zip or above, and requires that GROOVY_HOME be set.
 * This script requires an Oracle jdbc connection, and has been tested using ojdbc6.jar (which, if running 
 * outside of the horizon grails project must be added to the classpath (e.g., including it in $groovy_home/lib)
 *
 * The command line arguments are:
 *   1) General Person XML file name
 *   2) if you want to save (Y/N)
 *   3) do you want to run in debug (display sql statements) Y/N
 *   4-7) database connect info:  username, password, instance, hostname of database
 * */
public class SeedDataLoader {

    def inputData

    /**
     * Command line support for loading seed data outside of a grails project.
     * */

    static void main(args) {

        println "Seed data loader beginning at ${new Date()}  version 2.0"
        def inputData = new InputData([username: 'BANINST1', password: 'u_pick_it', hostname: 'localhost', instance: 'BAN83'])
        if (args.size()) inputData.prompts = args
        inputData.promptUserForInputData()
        def seedDataLoader = new SeedDataLoader(inputData)
        seedDataLoader.execute()
    }


    public SeedDataLoader(inputData) {
        this.inputData = inputData
    }


    public void execute() {
        ConnectDB connectionFactory
        def groovySql
        Connection connection
        try {
            connectionFactory = new ConnectDB(inputData)
            groovySql = connectionFactory.getSqlConnection()
            connection = connectionFactory.getSessionConnection()
            if (inputData.sqlTrace) {
                try {
                    String sqlTrace = "ALTER SESSION SET SQL_TRACE TRUE"
                    groovySql.execute sqlTrace
                }
                catch (Exception e) {
                    println "Problem enabling sqlTrace from SeedDataLoaderDML.groovy: $e.message"
                }
            }
            // turn messaging off so it does not interfere with the commit, in case logging is not turned on
            String mesg = """update gurmesg set gurmesg_enabled_ind = 'N'"""
            try {
                groovySql.execute mesg
                groovySql.execute "{ call gokfgac.p_turn_fgac_off }"
            }
            catch (Exception e) {
                println "Problem executing update to disable messaging from SeedDataLoaderDML.groovy: $e.message"
            }
            if (inputData.xmlFile) {
                println "\n\nSeed data loader starting at ${new Date()}"
                def valXML = new TableDriver(inputData, groovySql, connection, inputData.xmlFile)

                if (!inputData.saveThis) {
                    println "Rolling back seed data changes"
                    groovySql.execute "{ call gb_common.p_rollback() }"
                }
                else {
                    println "Committing  seed data"
                    groovySql.execute "{ call gb_common.p_commit() }"
                }
            }

            inputData.tableListCnts()
            println "\n\nSeed data loader completed at ${new Date()}"
        } catch (e) {
            groovySql.execute "{ call gb_common.p_rollback() }"
            println "Sorry, an error has occurred:  ${e.message}"
            e.printStackTrace()
        } finally {
              try {

                  connection.close()
              }catch(Exception ex) {
                  ex.printStackTrace()
              }
        }
    }
}
