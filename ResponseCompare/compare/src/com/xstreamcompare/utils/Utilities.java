package com.xstreamcompare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

public class Utilities {
	public static  Properties responsecompare_props = null;
	private static String responseCompareRoot = null;
	
	static Logger logger = Logger.getLogger(Utilities.class);
	public void fileChecker(String filename) {
		File file = null;
		file = new File(filename);
		if (!file.exists()) {
			try {
				FileUtils.touch(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void logHeaders(HttpResponse response) {
		Header headers[] = null;
	    
	    headers = response.getAllHeaders();
	    StringBuffer sb = new StringBuffer();
	    sb.append("Here are the response headers: ");
	    for (Header header : headers) {
	    	sb.append(header.toString());
	    }
	    
	    logger.info(sb.toString());
	}
	
	public void logHeaders(HttpGet request) {
		Header headers[] = null;
	    
	    headers = request.getAllHeaders();
	    StringBuffer sb = new StringBuffer();
	    for (Header header : headers) {
	    	sb.append(header.toString());
	    }
	    
	    if(sb.length()>0){
	    	logger.info("Here are the request headers: "+sb.toString());
	    } 
	}
	
	public String compensateForF5(String urlstring) {
		if (getRequestProperties().getProperty("UseF5").toLowerCase().equals("no")) {
			//This code is for when we are not going through the F5
			
			if (urlstring.contains("/1.1")) {
				urlstring = urlstring.replace("/1.1/api1.aspx?", "/infogroup/adserver?ver=1.1&");
			}
			else if (urlstring.contains("/1.2")) {
				urlstring = urlstring.replace("/1.2/api1.aspx?", "/infogroup/adserver?ver=1.1&");
			}
			else if (urlstring.contains("/lpa/api1.aspx?")) {
				urlstring = urlstring.replace("/lpa/api1.aspx?","/infogroup/adserver?");
			}
		}
		return urlstring;
	}

	public StringBuffer compensateForF5(StringBuffer url) {
		String urlstring = url.toString();
		return (new StringBuffer(compensateForF5(urlstring)));
	}
	
	/**
	 * Fill in properties.
	 * ToDO make Singleton
	 * @return loaded properties
	 */
	public synchronized Properties  getTestProperties(){
	//make singleton	
		if (Utilities.responsecompare_props==null){
	    	populateResponseCompareProperties();
		}	
		return Utilities.responsecompare_props;

	}
	
	protected static synchronized void populateResponseCompareProperties(){
		//already  populated. Nothing to do.
		if (Utilities.responsecompare_props!=null) return;
		StringBuffer requestprops = null;
		if (System.getProperties().getProperty("data.dir") == null) {
			requestprops = new StringBuffer(System.getProperty("user.dir") +
					File.separator + ".." + File.separator +
					"properties/" + ((System.getProperties().getProperty("propfile") == null) ? "responsecompare.properties" :
						System.getProperties().getProperty("propfile") + ".properties"));
		}
		Utilities.responsecompare_props = new Properties();

		
		try {
				FileInputStream fis = new FileInputStream(
					requestprops.toString());
				Utilities.responsecompare_props.load(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	
	}
	
	public  Properties getRequestProperties() {
		Properties props = new Properties();
		StringBuffer builddir = null, requestprops = null;
		
		if (System.getProperties().getProperty("data.dir") == null) {
			builddir = new StringBuffer(System.getProperty("user.dir"));
			requestprops = new StringBuffer("properties/request.properties");
		}

		else {

			if (System.getProperties().getProperty("data.dir")
					.equalsIgnoreCase("${dir.xstream.data}")) {
				builddir = new StringBuffer(System.getProperties().getProperty(
						"user.dir"));
				builddir = new StringBuffer(builddir.substring(0,
						builddir.indexOf("request")));

				requestprops = new StringBuffer(builddir.toString());
				requestprops.append("request/properties/request.properties");

			} else {
				builddir = new StringBuffer(System.getProperties().getProperty(
						"data.dir"));
				builddir = new StringBuffer(builddir.substring(0,
						builddir.indexOf("data")));
			}
		}
		if (requestprops != null) {
			try {
				FileInputStream fis = new FileInputStream(
						requestprops.toString());
				props.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return props;
	}
	
	public String extractVariablesFromRequest(String url) throws Exception {
		//The second url must contain at least one ${variable}
		if (!url.contains("${")) {
			throw new Exception("The url: " + url + " must contain at least one ${} variable");
		}
		Pattern p = Pattern.compile("\\$\\{[\\w]+\\}");
		Matcher m = p.matcher(url);
		String variables = "";

	    boolean result = m.find();
	    // Loop through and create a new String with the replacements
	    while(result) {
	    	if (!variables.contains(m.group())) {
	    		variables = variables + (m.group()) + ",";
	    	}
	    	result = m.find();
	    }
	    return variables;
	}
	public static List<String> getAllNodeValuesFromResponse(String nodeName, HttpResponse response){
				List<String> values = new ArrayList<String>();
				String content = null;
				try {
					InputStream is = response.getEntity().getContent();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					List<String> nodes = new ArrayList<String>();
					nodes.add(nodeName);
					while ((content = br.readLine()) != null) {
						for (String node : nodes) {
							if (content.contains(node)) {
								content = content.trim();
								content = content.replace("<![CDATA[", "");
								content = content.replace("]]>", "");
								content = content.substring(("<" + node + ">").length(),
								content.indexOf("</" + node + ">"));
								values.add(content);
							}
						}
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return values;

			}
	
	public static String getInfogroupEndPoint(){
		Utilities utils = new Utilities();
		Properties props = utils.getTestProperties();
		String endPoint = "http://"
				+ props.getProperty("infogroup-server")
				+ (props.getProperty("infogroup-port").isEmpty() ? "" : ":"
						+ props.getProperty("infogroup-port")) + "/"
				+ props.getProperty("infogroup-end-point");
	     return  endPoint;
	}
	
	public static String getLpaEndPoint(){
		Utilities utils = new Utilities();
		Properties props = utils.getTestProperties();
		String endPoint = "http://"
				+ props.getProperty("lpa-server").trim()
				+ (props.getProperty("lpa-port").isEmpty() ? "" : ":"
						+ props.getProperty("lpa-port").trim()) + "/"
				+ props.getProperty("lpa-end-point").trim();
	     return  endPoint;
	}
	
	/**
	 * Address branching
	 * 
	 * @return the top most directory name for ResponseCompare
	 */
	public static String getResponseCompareRoot() {
		String branch=System.getProperty("branch");
		if(System.getProperty("branch")==null || System.getProperty("branch").isEmpty()) {
				responseCompareRoot = System.getProperty("user.dir") + File.separator + "..";
			} else {
				responseCompareRoot =   "../../../../infogroup-testing-tools/branches/"+branch+"/ResponseCompare";
				
			}
		return responseCompareRoot;
	}
}