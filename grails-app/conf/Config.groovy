/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder


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
grails.config.locations = [] // leave this initialized to an empty list, and add your locations
                             // to the map below below.
def locationAdder = ConfigFinder.&addLocation.curry( grails.config.locations )


[ BANNER_APP_CONFIG:                  "banner_configuration.groovy",
  BANNER_CATALOG_SCHEDULE_CONFIG:     "${appName}_configuration.groovy",
  customRepresentationConfig:         "CustomRepresentationConfig.groovy",
  releaseInfo:                        "release_info.groovy",
].each { envName, defaultFileName -> locationAdder( envName, defaultFileName ) }




grails.project.groupId = "net.hedtech" // used when deploying to a maven repo

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false


