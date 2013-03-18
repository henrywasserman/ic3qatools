package com.xstreamcompare.request;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class GetHashTable extends Get {

	static Logger logger = Logger.getLogger(GetHashTable.class);
	public GetHashTable(TestCase test) {
		  super(test);
	}	
	
	public void makeRequests() throws Exception {
		
		test.getRequests().get(0).getHeaders();
		
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    
	    url1.append("http://" + test.getRequests().get(0).getURL().trim());
		
	    compensateForF5();

		logger.info("TestID: " + test.getTestCaseID());
		logger.info("GET Request for hash: ");
		logger.info(url1.toString());
		
		httpget = new HttpGet(url1.toString().trim());
	    HttpResponse response = httpclient.execute(httpget);
	    validateHeaders(response,0);

	    setupAndOutput(response);
	    
	    utils.logHeaders(response);
	    
		String type = getResponseType(url1.toString());
		
		// Now open up that hash and convert it to *.xml
		hashToXML(type,outputfile.toString());
	}
}