package com.xstreamcompare.request.test;

import junit.framework.Assert;

import org.junit.Test;


public class DataManipulationUtilsTest {

	@Test
	public void test() {
		 //String template = "<creativeid><![CDATA[3425]]></creativeid>";
	     //String start_tag = "<creativeid><![CDATA[";
	     //String end_tag = "]]></creativeid>";
		 DataManipulationUtils dataUtils = new DataManipulationUtils();
		 String item = dataUtils.extractValue("<creativeid><![CDATA[3425]]></creativeid>", "<creativeid><![CDATA[", "]]></creativeid>");
	     Assert.assertEquals(item,"3425"); 
	}

}
