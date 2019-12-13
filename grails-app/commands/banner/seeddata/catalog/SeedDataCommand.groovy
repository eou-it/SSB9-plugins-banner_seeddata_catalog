/** *****************************************************************************************
 * Copyright 2010-2019 Ellucian Company L.P. and its affiliates.                         *
 *****************************************************************************************/

package banner.seeddata.catalog


import grails.dev.commands.*
import grails.util.BuildSettings
import org.grails.build.parsing.CommandLine
import grails.util.Holders;
import groovy.time.TimeCategory
import groovy.time.TimeDuration

class SeedDataCommand implements GrailsApplicationCommand {

    boolean handle() {
        File basedir = BuildSettings.BASE_DIR
        String baseDirPath = basedir != null ? basedir.getCanonicalPath() : null;
		println "Base Directory: ${basedir} target ${args} Base Directory Path ${baseDirPath}"

		String databaseURL = (Holders.config.bannerDataSource.url)?.toLowerCase()
		String seedDataTargetDB = Holders.config.seedDataTargetDB.toString()?.toLowerCase()
		if(!databaseURL.contains(seedDataTargetDB)) {
			println "**********************************************************************************************"
			println "*                                                                                            *"
			println "*  You can't run the seed data on ${databaseURL}                                             *"
			println "*  Want to still run on this DB? try changing configuration for  property 'seedDataTargetDB' *"
			println "**********************************************************************************************"
			System.exit(0)
		}else{
			executeSeedDataLoader()
		}

        return true
    }

	def executeSeedDataLoader() {
		//Get the base directory and plugin directory path
        File basedir = BuildSettings.BASE_DIR

		//Get the dataSource bean from Application Context
		def appContext = getApplicationContext()
		def dataSource = appContext.getBean("dataSource")

		ClassLoader classLoader = getClass().getClassLoader()
		def clazzInputData = classLoader.loadClass("net.hedtech.banner.seeddata.InputData")
		def clazzSeedDataLoader = classLoader.loadClass("net.hedtech.banner.seeddata.SeedDataLoader")
		def inputData = clazzInputData.newInstance([dataSource: dataSource])

		def seedDataTargets = Holders.getConfig().seedDataTarget

		def pluginDirPath = getPluginDirectoryPathString()

		def reportErrors = 0
		def reportRead = 0
		def reportTables = 0
		def reportInsert = 0
		def reportUpdate = 0
		def reportDelete = 0
		def reportFiles = 0
		def startDate = new Date()
		println "Seed data loader execution of all targets starting at ${new Date()}"
		if (seedDataTargets.size() > 0)
			inputData.targets << seedDataTargets
		def argsList = ["ALL","BCM","AIP","SELENIUM"]
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
					case "AIP":
						target = inputData.aipTargets
						break;
					case "SELENIUM" :
						target = inputData.seleniumTargets
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
						reportRead  += xmlInputData.totalFilesRead
						reportTables  += xmlInputData.totalTables
						reportInsert += xmlInputData.totalInsert
						reportUpdate += xmlInputData.totalUpdate
						reportDelete += xmlInputData.totalDelete
						reportFiles += 1

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
						reportRead  += xmlInputData.totalFilesRead
						reportTables  += xmlInputData.totalTables
						reportInsert += xmlInputData.totalInsert
						reportUpdate += xmlInputData.totalUpdate
						reportDelete += xmlInputData.totalDelete
						reportFiles += 1
					}
				}
			}

			else if (args[0].toUpperCase() == "CALBHR") {
				println "Execute CALBHR Seeddata"
				inputData.calbHrTargets.each {
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
						reportRead  += xmlInputData.totalFilesRead
						reportTables  += xmlInputData.totalTables
						reportInsert += xmlInputData.totalInsert
						reportUpdate += xmlInputData.totalUpdate
						reportDelete += xmlInputData.totalDelete
						reportFiles += 1
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
						reportRead  += xmlInputData.totalFilesRead
						reportTables  += xmlInputData.totalTables
						reportInsert += xmlInputData.totalInsert
						reportUpdate += xmlInputData.totalUpdate
						reportDelete += xmlInputData.totalDelete
						reportFiles += 1
					}
				}
			}
		}
		else {
			println "Executing the Seed Data Loader for data specified at input prompts ${new Date()}"

			inputData?.promptUserForInputData()

			def seedDataLoader = clazzSeedDataLoader.newInstance(inputData)
			seedDataLoader.execute()
			reportErrors += xmlInputData.totalErrors
			reportRead  += xmlInputData.totalFilesRead
			reportTables  += xmlInputData.totalTables
			reportInsert += xmlInputData.totalInsert
			reportUpdate += xmlInputData.totalUpdate
			reportDelete += xmlInputData.totalDelete
			reportFiles += 1

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

		println "Total errors reported: ${reportErrors} search for 'Errors are present in file' to find errors"
		println "\nRun Totals, Files  ${reportFiles.toString().padLeft(4, ' ')} " +
				"\tTables: ${reportTables.toString().padLeft(4, ' ')} " +
				" \tRead: ${reportRead.toString().padLeft(4, ' ')} " +
				" \tInsert: ${reportInsert.toString().padLeft(4, ' ')} " +
				" \tUpdate: ${reportUpdate.toString().padLeft(4, ' ')} " +
				" \tDeletes: ${reportDelete.toString().padLeft(4, ' ')} " +
				" \tErrors: ${reportErrors.toString().padLeft(4, ' ')} "
		println "Seed data loader execution started ${startDate} of all targets ended ${endDate}"
		println "Total execution time: ${elapsedHours} hours, ${elapsedMinutes} minutes, ${elapsedSeconds} secs"
	}

	def getPluginDirectoryPathString() {
		File basedir = BuildSettings.BASE_DIR
        String baseDirPath = basedir != null ? basedir.getCanonicalPath() : null
		baseDirPath
	}	

}
