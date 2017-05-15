includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsClean")
includeTargets << grailsScript("_GrailsCompile")


target(seeddataJar: "The description of the script goes here!") {
    // TODO: Implement script here
    //depends(compile)
    depends(cleanAll, compile)
    print " .........Executing Seeddata-Jar Target"

    print "  basedir  is "+basedir
	ant.delete(file: "${basedir}/target/banner_seeddata_catalog.jar")
	ant.delete(file: "${basedir}/target/SeedDataUtility.zip.jar")
    ant.delete(dir: "${basedir}/target/lib")
	
    Ant.jar(destfile: "${basedir}/target/banner_seeddata_catalog.jar", basedir: "${basedir}/target/classes"){
        manifest {
            attribute( name: 'Main-Class', value: net.hedtech.banner.seeddata.SeedDataLoader )
            attribute( name: 'Class-Path', value: 'lib/grails-core-2.5.0.jar lib/groovy-all-2.4.3.jar lib/ojdbc6.jar lib/commons-logging-1.0.4.jar lib/grails-bootstrap-2.5.0.jar lib/xdb6-11.2.0.4.jar' )
        }
    }
    ant.copy( todir:"${basedir}/target/lib" ) {
        fileset( dir:"${basedir}/lib", includes:"*.jar" )
        fileset( dir:"${basedir}/lib", includes:"SeedData.bat" )
        fileset( dir:"${basedir}/lib", includes:"SeedData.sh" )

    }
    ant.copy( todir:"${basedir}/target" ) {
        fileset( dir:"${basedir}", includes:"SeedData.bat" )
        fileset( dir:"${basedir}", includes:"SeedData.sh" )

    }
    Ant.zip( destFile: "target/SeedDataUtility.zip") {
        fileset( dir: "${basedir}/target" ) {
            include( name:"banner_seeddata_catalog.jar" )
            include( name:"lib/*.jar" )
			include( name:"SeedData.bat" )
			include( name:"SeedData.sh" )
        }
    }


}

setDefaultTarget(seeddataJar)
