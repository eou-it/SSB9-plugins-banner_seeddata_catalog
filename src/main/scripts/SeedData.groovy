/** *****************************************************************************************
* Copyright 2010-2015 Ellucian Company L.P. and its affiliates.                         *
*****************************************************************************************/
import grails.util.Holders;
import groovy.time.TimeCategory
import groovy.time.TimeDuration



includeTargets << grailsScript("_GrailsArgParsing")
includeTargets << new File("${grailsHome}/scripts/Bootstrap.groovy")
/**
 * Grails target for loading and refreshing seed data.
 * Usage:  'grails seed-data'   for input prompt to enter file name
 *         'grails seed-data all'   for all seed data files
 *         'grails seed-data catalog'  for all data related to catalog and schedule, including  faculty
 *         'grails seed-data person'   for all data related to person
 *         'grails seed-data catalogSelenium'  several catalog courses for destructive selenium tests
 * */

target(main: "Refreshes seed data, using file from arguements or after prompting for input data") {
    println "base dir: ${basedir} target ${args}"
    depends(parseArguments, bootstrap)

    getClass().classLoader.rootLoader?.addURL(new File(classesDirPath).toURL())

    def dataSource = appCtx.getBean("dataSource")

    def clazzInputData = classLoader.loadClass("net.hedtech.banner.seeddata.InputData")
    def clazzSeedDataLoader = classLoader.loadClass("net.hedtech.banner.seeddata.SeedDataLoader")
    def inputData = clazzInputData.newInstance([dataSource: dataSource])

    //def inputData = new net.hedtech.banner.seeddata.InputData([dataSource: dataSource])
    def seedDataTargets = Holders.getConfig().seedDataTarget

    def pluginDirPath = getPluginDirectoryPathString()

    def startDate = new Date()
    println "Seed data loader execution of all targets starting at ${new Date()}"
    
    if (seedDataTargets.size() > 0)
        inputData.targets << seedDataTargets

    if (args) {
        if (args.toUpperCase() == "ALL") {
            println "Execute ALL Seeddata"
            inputData.targets.each {
                def xmlFiles = it.value
                xmlFiles.each {
                    //def xmlInputData = new net.hedtech.banner.seeddata.InputData([dataSource: dataSource])
                    def xmlInputData = clazzInputData.newInstance([dataSource: dataSource])

                    xmlInputData.xmlFile = "$pluginDirPath/$it.value"
                    def inputFile = new File(xmlInputData.xmlFile)
                    if (!inputFile.exists())
                        xmlInputData.xmlFile = "${basedir}${it.value}"

                    xmlInputData.replaceData = true
                    println xmlInputData.xmlFile
                    def seedDataLoader = clazzSeedDataLoader.newInstance(xmlInputData)
                    //def seedDataLoader = new net.hedtech.banner.seeddata.SeedDataLoader(xmlInputData)
                    seedDataLoader.execute()
                }
            }
        }

       else if (args.toUpperCase() == "CALB") {
            println "Execute ALL Seeddata"
            inputData.calbTargets.each {
                def xmlFiles = it.value
                xmlFiles.each {
                    //def xmlInputData = new net.hedtech.banner.seeddata.InputData([dataSource: dataSource])
                    def xmlInputData = clazzInputData.newInstance([dataSource: dataSource])

                    xmlInputData.xmlFile = "$pluginDirPath/$it.value"
                    def inputFile = new File(xmlInputData.xmlFile)
                    if (!inputFile.exists())
                        xmlInputData.xmlFile = "${basedir}${it.value}"

                    xmlInputData.replaceData = true
                    println xmlInputData.xmlFile
                    def seedDataLoader = clazzSeedDataLoader.newInstance(xmlInputData)
                    //def seedDataLoader = new net.hedtech.banner.seeddata.SeedDataLoader(xmlInputData)
                    seedDataLoader.execute()
                }
            }
        }

        else {
            def xmlfiles = inputData.targets.find { it.key == args}?.value
            if (!xmlfiles) xmlFiles = inputData.seleniumTargets.find { it.key == args}?.value
            if (!xmlfiles) {
                println "No files were specified in the targets map in the InputData class for ${args}"
            }
            else {
                xmlfiles.each {
                    //def xmlInputData = new net.hedtech.banner.seeddata.InputData([dataSource: dataSource])
                    def xmlInputData = clazzInputData.newInstance([dataSource: dataSource])
                    xmlInputData.xmlFile = "$pluginDirPath/$it.value"
                    def inputFile = new File(xmlInputData.xmlFile)
                    if (!inputFile.exists())
                        xmlInputData.xmlFile = "${basedir}${it.value}"
                    xmlInputData.replaceData = true
                    println xmlInputData.xmlFile
                    def seedDataLoader = clazzSeedDataLoader.newInstance(xmlInputData)
                    seedDataLoader.execute()
                }
            }
        }
    }
    else {
        println "Executing the Seed Data Loader for data specified at input prompts ${new Date()}"

        inputData?.promptUserForInputData()

        def seedDataLoader = clazzSeedDataLoader.newInstance(inputData)
        seedDataLoader.execute()

    }

    def endDate = new Date()
    TimeDuration execTime = TimeCategory.minus( endDate, startDate )
    long l1 = startDate.getTime();
    long l2 = endDate.getTime();
    long diff = l2 - l1;

    long secondInMillis = 1000;
    long minuteInMillis = secondInMillis * 60;
    long hourInMillis = minuteInMillis * 60;

    long elapsedHours = diff / hourInMillis;
    diff = diff % hourInMillis;
    long elapsedMinutes = diff / minuteInMillis;
    diff = diff % minuteInMillis;
    long elapsedSeconds = diff / secondInMillis;

    println "Seed data loader execution started ${startDate} of all targets ended ${endDate}"
    println "Total execution time: ${elapsedHours} hours, ${elapsedMinutes} minutes, ${elapsedSeconds} secs"
}

setDefaultTarget "main"

def getPluginDirectoryPathString() {
    def pInfo = pluginSettings.getPluginInfos().find { it.name.equals( "banner-seeddata-catalog" ) }
    pInfo.pluginDir.path.toString()
}
