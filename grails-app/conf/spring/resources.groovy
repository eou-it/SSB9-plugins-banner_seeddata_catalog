/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/** *****************************************************************************************
* Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
*****************************************************************************************/



/**
 * Spring bean configuration using Groovy DSL, versus normal Spring XML.
 */
beans = {
    dataSource(org.apache.commons.dbcp.BasicDataSource) {
        java.util.TimeZone defaultTimeZone = TimeZone.getTimeZone("America/New_York")
        java.util.TimeZone.setDefault(defaultTimeZone)
        println "\n \n configure spring/resources.groovy defaultTimeZone $defaultTimeZone"
    }
    
}

