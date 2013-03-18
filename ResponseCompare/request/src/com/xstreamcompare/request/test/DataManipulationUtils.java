package com.xstreamcompare.request.test;

public class DataManipulationUtils {
	 public  String extractValue(String template, String start_tag, String end_tag){
		String item = new String();
		 //String template = "<creativeid><![CDATA[3425]]></creativeid>";
	     //String start_tag = "<creativeid><![CDATA[";
	     //String end_tag = "]]></creativeid>";
	     item = template.substring(start_tag.length()-1);
		 item = template.replace(start_tag,"" );
	     item = item.replace(end_tag, "");
		 return item.trim();
	}
}
