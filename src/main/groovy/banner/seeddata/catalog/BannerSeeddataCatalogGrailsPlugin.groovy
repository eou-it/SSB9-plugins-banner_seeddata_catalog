/*********************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package banner.seeddata.catalog

import grails.plugins.Plugin
import grails.util.Holders as CH
import org.apache.commons.dbcp.BasicDataSource


/**
 * A Grails Plugin providing cross cutting concerns such as security and database access
 * for Banner web applications.
 * */
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

    def documentation = "http://sungardhe.com/development/horizon/plugins/banner-seedata-catalog"
	
    def profiles = ['web']


    Closure doWithSpring() { {->
		
			//initializing Datasource for runnin the grails target "seed-data"
			dataSource(BasicDataSource) {
				maxActive = 5
				maxIdle = 2
				defaultAutoCommit = "false"
				driverClassName = "${CH.config.bannerDataSource.driver}"
				url = "${CH.config.bannerDataSource.url}"
				password = "${CH.config.bannerDataSource.password}"
				username = "${CH.config.bannerDataSource.username}"
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
}
