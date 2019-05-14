/*********************************************************************************
  Copyright 2010-2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

// ******************************************************************************
//
//                       +++ EXTERNALIZED CONFIGURATION +++
//
// ******************************************************************************
//
// Config locations should be added to the map used below. They will be loaded based upon this search order:
// 1. Load the configuration file if its location was specified on the command line using -DmyEnvName=myConfigLocation
// 2. Load the configuration file if it exists within the user's .grails directory (i.e., convenient for developers)
// 3. Load the configuration file if its location was specified as a system environment variable
//
// Map [ environment variable or -D command line argument name : file path ]

grails.config.locations = [
        BANNER_APP_CONFIG:        			"banner_configuration.groovy",
        BANNER_SEEDDATA_CATALOG_CONFIG:     "BannerSeeddataCatalog_configuration.groovy",
        customRepresentationConfig:         "CustomRepresentationConfig.groovy",
        releaseInfo:                        "release_info.groovy"       
 ]


// environment specific settings for datasource
environments {
    development {
        dataSource {

        }
    }
    test {
        dataSource {
        }
    }
    production {
        dataSource {
        }
    }
}
