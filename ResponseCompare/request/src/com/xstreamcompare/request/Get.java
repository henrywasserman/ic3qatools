package com.xstreamcompare.request;

import java.io.File;
import java.lang.Integer;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class Get extends Request {

  static Logger logger = Logger.getLogger(Get.class);
  Integer TestNumber = null;

  public Get(TestCase test) {
	  super(test);
  }
  
  public Get() {
	  super();
  }
  
  public void makeRequests() throws Exception {

    DefaultHttpClient httpclient = new DefaultHttpClient();
    
    url1.append( props.getProperty("infogroup-protocol") + 
    		"://"+test.getRequests().get(0).getURL().trim());
    
    compensateForF5();
	
    logger.info("TestID: " + test.getTestCaseID());
	logger.info("GET Request: " + url1.toString());
	
	test.getRequests().get(0).setURL(url1.toString());
  
	httpget = new HttpGet(url1.toString().trim());
    
    setGetHeaders(0);
    utils.logHeaders(httpget);
    HttpResponse response = httpclient.execute(httpget);
    Assert.assertTrue( "Status: "+ response.getStatusLine().getStatusCode()+" The request "+url1+" was not sucessful",response.getStatusLine().getStatusCode()<300);
    
    validateHeaders(response,0);

    setupAndOutput(response);
    
    if (isJasonRequest(0)) {
    
    	String jsonString = FileUtils.readFileToString(new File(outputfile.toString()));
    	
    	jsonString = "{\"root\":" + jsonString + "}"; 
    
    	String res = convertToXml(jsonString,"",true);

    	logger.debug(res);

    	FileUtils.writeStringToFile(new File(outputfile.toString().replace(".json", ".xml")), res);
    }

	utils.logHeaders(response);    

  }
}