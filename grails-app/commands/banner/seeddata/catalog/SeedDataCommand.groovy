/** *****************************************************************************************
 * Copyright 2010-2015 Ellucian Company L.P. and its affiliates.                         *
 *****************************************************************************************/

package banner.seeddata.catalog


import grails.dev.commands.*
import grails.util.BuildSettings
import org.grails.build.parsing.CommandLine;
import grails.util.Holders;
import groovy.time.TimeCategory
import groovy.time.TimeDuration

class SeedDataCommand implements GrailsApplicationCommand {

	private static final String BAN83 = "BAN83"

    boolean handle() {

        println "==== System property user.dir " + System.properties['user.dir']

        File basedir = BuildSettings.BASE_DIR;
        String baseDirPath = basedir != null ? basedir.getCanonicalPath() : null;
        println "Base Directory Path ====> " + baseDirPath		

		println "Base Directory: ${basedir} target ${args} Base Directory Path ${baseDirPath}"
		String connectedDB = Holders.config.bannerDataSource.url
		if(!connectedDB.contains(BAN83)){
			println "Please connect to Local DB to run the seed data!"
			System.exit(-1)
		}else{
			executeSeedDataLoader()
		}

        return true
    }

	def executeSeedDataLoader() {
	
		//Get the base directory and plugin directory path
        File basedir = BuildSettings.BASE_DIR;
        String baseDirPath = basedir != null ? basedir.getCanonicalPath() : null;		
	
		//Get the dataSource bean from Application Context
		def appContext = getApplicationContext()
		def dataSource = appContext.getBean("dataSource")

		ClassLoader classLoader = getClass().getClassLoader()
		def clazzInputData = classLoader.loadClass("net.hedtech.banner.seeddata.InputData")
		def clazzSeedDataLoader = classLoader.loadClass("net.hedtech.banner.seeddata.SeedDataLoader")
		def inputData = clazzInputData.newInstance([dataSource: dataSource])

		//def inputData = new net.hedtech.banner.seeddata.InputData([dataSource: dataSource])
		def seedDataTargets = Holders.getConfig().seedDataTarget

		
		def pluginDirPath = getPluginDirectoryPathString()

		def reportErrors = 0
		def startDate = new Date()
		println "Seed data loader execution of all targets starting at ${new Date()}"
		

		if (seedDataTargets.size() > 0)
			inputData.targets << seedDataTargets

		def argsList = ["ALL","BCM"]

		
		/*** handle according to the arguments passed ***/	
		if (args) {
			def arg = args[0].toUpperCase()
			def target

			if (argsList.contains(arg)) {
				println "Execute ${arg} Seeddata"
				switch (arg) {
					case "BCM":
						target = inputData.bcmTargets
						break;
					default: //ALL
						target = inputData.targets
						break;
				}
				target.each {
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
						reportErrors += xmlInputData.totalErrors
					}
				}

			}

		   else if (args[0].toUpperCase() == "CALB") {
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
						reportErrors += xmlInputData.totalErrors
					}
				}
			}

			else {
				def xmlfiles = inputData.targets.find { it.key == args[0]}?.value
				if (!xmlfiles) xmlfiles = inputData.seleniumTargets.find { it.key == args[0]}?.value
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
						reportErrors += xmlInputData.totalErrors
					}
				}
			}
		}
		else {
			println "Executing the Seed Data Loader for data specified at input prompts ${new Date()}"

			inputData?.promptUserForInputData()

			def seedDataLoader = clazzSeedDataLoader.newInstance(inputData)
			seedDataLoader.execute()
			reportErrors += inputData.totalErrors

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

		println "Total errors reported: ${reportErrors}"
		println "Seed data loader execution started ${startDate} of all targets ended ${endDate}"
		println "Total execution time: ${elapsedHours} hours, ${elapsedMinutes} minutes, ${elapsedSeconds} secs"

			
	
	}

	def getPluginDirectoryPathString() {
		/*
		def pInfo = pluginSettings.getPluginInfos().find { it.name.equals( "banner-seeddata-catalog" ) }
		pInfo.pluginDir.path.toString()
		*/
		
		//TODO pluginDirPath is Current directory as run from plugin level
		File basedir = BuildSettings.BASE_DIR;
        String baseDirPath = basedir != null ? basedir.getCanonicalPath() : null;
		baseDirPath
	}	

}
