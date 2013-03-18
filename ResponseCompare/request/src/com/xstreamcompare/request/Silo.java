package com.xstreamcompare.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.*;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Silo extends Request {
	
	String campaignid = "";
	String processedin = "";
	String output = "";
	String silorequestport = "";
	
	public Silo(TestCase test) {
		  super(test);
	}
	
	@Override
	protected void makeRequests() throws Exception {
		
	}
	
	protected String getCampaignID() {
		return campaignid;
	}
	
	protected void testSilo(String replacestring, String siloprocessedport) throws Exception {
		
		String url = "";
		String req = "";
	    DefaultHttpClient httpclient = new DefaultHttpClient();   
	    url1.append("http://"+ test.getRequests().get(0).getURL().trim());
			    
	    compensateForF5();
				
	    url = url1.toString().replace(replacestring, props.getProperty("infogroup-server") + 
	    		":" + silorequestport).trim();
	    
	    req = url.substring(url.indexOf("?") + 1);
			    
	    logger.info("TestID: " + test.getTestCaseID());
		logger.info("GET Request: " + url);
				
		test.getRequests().get(0).setURL(url1.toString());
			  
		httpget = new HttpGet(url);
			    
		HttpResponse response = httpclient.execute(httpget);
		
		Assert.assertTrue("Status code was greater than 300", response.getStatusLine().getStatusCode() < 300);
		
		InputStream content = null;
		String line = "";
		
		try {
				content = response.getEntity().getContent();
				InputStreamReader isr = new InputStreamReader(content);
				BufferedReader br = new BufferedReader(isr);
			
				while ((line = br.readLine()) != null) {
					logger.info(line);
					if (line.contains("getPage")) {
						line = line.trim();
						line = line.replace("\"","");
						line = line.replace(">", "");
						break;
					}
					if (line.contains("wapiDnsUrl")) {
						processedin = line.substring(output.indexOf("http://") + "http://".length());
					}
				}
				br.close();
				isr.close();
				content.close();
			} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue("",processedin.contains(siloprocessedport));
		
		output = line.substring(line.indexOf("href=") + "href=".length());
		campaignid = line.substring(line.indexOf("transactionid=") + "transactionid=".length());
		
		
		
		url = "http://" + props.getProperty("infogroup-server") 
				+ ":" + silorequestport + "/infogroup" + output + "&" + req;

	    logger.info("TestID: " + test.getTestCaseID());
		logger.info("Second GET Request: " + url);
		
		httpget = new HttpGet(url);
		response = httpclient.execute(httpget);
		
		Assert.assertTrue("Status code was greater than 300", response.getStatusLine().getStatusCode() < 300);
		
		response.getEntity().getContent();
		
		content = response.getEntity().getContent();
		String res = IOUtils.toString(content);
		content.close();
		if (test.getTestCaseID().equals("800")) {
			return;
		}
		Assert.assertTrue("Transactionid/Campaignid was not found in response",res.contains(campaignid));
				
	}

}

