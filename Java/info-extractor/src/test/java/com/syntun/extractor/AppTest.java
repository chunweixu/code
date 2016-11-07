package com.syntun.extractor;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	InfoExtractorConfig config = new InfoExtractorConfig();
    	assertTrue(config.loadConfig("./config/configTest.xml"));
    	HashMap<String, String> mapRawpage = config.getMapConfigRawpage();
    	assertTrue(mapRawpage != null);
    	assertEquals(mapRawpage.get("type"),"kafka");
    	assertEquals(mapRawpage.get("zookeeper"),"192.168.0.202:2181,192.168.0.203:2181,192.168.0.204:2181");
    	assertEquals(mapRawpage.get("topic"),"syntun_rawpage");
    	assertEquals(mapRawpage.get("group"),"infoExtractor");
    	
        assertTrue( true );
    }
}
