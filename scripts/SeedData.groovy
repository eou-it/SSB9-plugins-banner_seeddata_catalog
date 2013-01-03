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
    def seedDataTargets = org.codehaus.groovy.grails.commons.ConfigurationHolder.config.seedDataTarget

    def pluginDirPath = getPluginDirectoryPathString()
    
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

        else {
            def xmlfiles = inputData.targets.find { it.key == args}?.value
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
                    println xmlInputData
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
}

setDefaultTarget "main"

def getPluginDirectoryPathString() {
    def pInfo = pluginSettings.getPluginInfos().find { it.name.equals( "banner-seeddata-catalog" ) }
    pInfo.pluginDir.path.toString()
}
