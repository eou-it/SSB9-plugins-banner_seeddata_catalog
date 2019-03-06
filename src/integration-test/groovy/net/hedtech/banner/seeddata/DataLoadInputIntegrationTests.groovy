/*********************************************************************************
 Copyright 2009-2014 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import org.junit.Before
import org.junit.Test


/**
 * An integration test for the data load tool.
 **/
class DataLoadInputIntegrationTests extends GroovyTestCase {
	
    @Before
	public void setUp() {
        super.setUp()
    }



    @Test
    void testProgrammaticallySettingInputData() {
        // Note that database connection properties are expected to be provided in upper case, when provided via the constructor. 
        def inputData = new InputData( [ username:'SOME_NAME', password:'SOME_PW', hostname:'SOME_HOST', instance:'SOME_INSTANCE' ] ) 
        assertEquals( 'SOME_NAME', inputData.username ) 
        assertEquals( 'SOME_PW', inputData.password )
        assertEquals( 'SOME_HOST', inputData.hostname )
        assertEquals( 'SOME_INSTANCE', inputData.instance )
    }   
    

    
}
    
