/*********************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
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
	
	String version = "1.0.52"
    
	// the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.2 > *"
    
	// resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def author = "SunGard Higher Education"
    def authorEmail = "horizon-support@sungardhe.com"
    def title = "BannerSeedData Plugin"
    def description = '''This plugin is BannerSeedDataCatalog.'''//.stripMargin()  // TODO Enable this once we adopt Groovy 1.7.3

    def documentation = "http://grails.org/plugin/banner-seeddata-catalog"
	
    def profiles = ['web']


    Closure doWithSpring() { {->
	
			println "Banner Seed Data Catalog Plugin loading =====> "
			
			//load external config
			setupExternalConfig()
			
		
			//initializing Datasource for runnin the grails target "seed-data"
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
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
	
	
	/*
		To load external configuration file
	*/
	
	private setupExternalConfig() {
        
		def config = CH.config
        def locations = config.grails.config.locations
        String filePathName

        locations.each { propertyName,  fileName ->
            filePathName = getFilePath(System.getProperty(propertyName))
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
            } else {
                filePathName = Thread.currentThread().getContextClassLoader().getResource( "$fileName" )?.getFile()
                if (filePathName) {
                    println "Using configuration file $fileName from the classpath"
                    log.info "Using configuration file $fileName from the classpath (e.g., from within the war file)"
                }
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
