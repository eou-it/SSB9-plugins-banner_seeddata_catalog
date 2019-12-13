/*********************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package banner.seeddata.catalog

import grails.plugins.Plugin
import grails.util.Holders as CH
import org.apache.commons.dbcp.BasicDataSource
import grails.util.Environment
import groovy.util.logging.Slf4j


/**
 * A Grails Plugin providing cross cutting concerns such as security and database access
 * for Banner web applications.
 * */
@Slf4j 
class BannerSeeddataCatalogGrailsPlugin extends Plugin {
	
	String version = "1.0.0"
    
	// the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.2 > *"
    
    String title = "BannerSeedData Plugin"
    String description = '''This plugin is BannerSeedDataCatalog.'''//.stripMargin()

    def profiles = ['web']

    Closure doWithSpring() { {->
	    Properties p = System.properties
        String st = System.getenv('CMD_LINE_ARGS')
        String[] stringList = st?.split('-D')
        stringList.each{it ->
            String[] strings = it.split("=");
            if (strings.length == 2)
                p.put(strings[0], strings[1])
        }
        System.setProperties(p)
		setupExternalConfig()
			//initializing Datasource for running the grails target "seed-data"
			dataSource(BasicDataSource) {
				maxActive = 5
				maxIdle = 2
				defaultAutoCommit = "false"
				driverClassName = "${CH.config.bannerDataSource.driver}"
				url = "${CH.config.bannerDataSource.url}"
				username = "${CH.config.bannerDataSource.username}"
				password = "${CH.config.bannerDataSource.password}"
			}
        }
    }

    void doWithDynamicMethods() {

    }

    void doWithApplicationContext() {

    }

    void onChange(Map<String, Object> event) {

    }

    void onConfigChange(Map<String, Object> event) {

    }

    void onShutdown(Map<String, Object> event) {

    }

	/*
		To load external configuration file
	*/
    private setupExternalConfig() {
		def config = CH.config
        def locations = config.grails.config.locations
        String filePathName
        locations.each { propertyName,  fileName ->
            String propertyValue = System.getProperty(propertyName) ?: System.getenv(propertyName)
            filePathName = getFilePath(propertyValue)
            if (Environment.getCurrent() != Environment.PRODUCTION) {
                if (!filePathName) {
                    filePathName = getFilePath("${System.getProperty('user.home')}/.grails/${fileName}")
                    if (filePathName) log.info "Using configuration file '\$HOME/.grails/$fileName'"
                }
                if (!filePathName) {
                    filePathName = getFilePath("${fileName}")
                    if (filePathName) log.info "Using configuration file '$fileName'"
                }
                if (!filePathName) {
                    filePathName = getFilePath("grails-app/conf/$fileName")
                    if (filePathName) log.info "Using configuration file 'grails-app/conf/$fileName'"
                }
                println "External configuration file: " + filePathName
            }
            if(filePathName) {
                try {
                    String configText = new File(filePathName).text
                    Map properties = configText ? new ConfigSlurper(Environment.current.name).parse(configText)?.flatten() : [:]
                    CH.config.merge(properties)
                }
                catch (e) {
                    println "NOTICE: Caught exception while loading configuration files (depending on current grails target, this may be ok): ${e.message}"
                }
            }
        }
    }

    private static String getFilePath( filePath ) {
        if (filePath && new File( filePath ).exists()) {
            "${filePath}"
        }
    }
	
}
