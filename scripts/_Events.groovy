import org.codehaus.groovy.grails.plugins.GrailsPluginUtils
import groovy.xml.StreamingMarkupBuilder
import grails.util.Environment
/** *****************************************************************************************
* Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
*****************************************************************************************/

eventCreateWarStart = { warName, stagingDir ->
    ant.delete( file: "${stagingDir}/WEB-INF/lib/ojdbc6.jar" )
}