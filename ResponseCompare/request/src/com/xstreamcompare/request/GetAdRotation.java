package com.xstreamcompare.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Integer;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class GetAdRotation extends Request {

  static Logger logger = Logger.getLogger(GetAdRotation.class);
  Integer TestNumber = null;
  String advertisername = "";
  boolean testresult = false;

  public GetAdRotation(TestCase test) {
	  super(test);
  }
  
  public GetAdRotation() {
	  super();
  }
  
  public void makeRequests() throws Exception {

    DefaultHttpClient httpclient = new DefaultHttpClient();
    
    url1.append("http://" + test.getRequests().get(0).getURL().trim());
    
    compensateForF5();
	
    logger.info("TestID: " + test.getTestCaseID());
	logger.info("GET Request: " + url1.toString());
	
    httpget = new HttpGet(url1.toString().trim());
    
    setGetHeaders(0);
    utils.logHeaders(httpget);
    
    String previousAdvertisername = advertisername;
    for (int n = 0; n < 10; n++ ) {
    	HttpResponse response = httpclient.execute(httpget);
    	validateHeaders(response,0);
    	
    	checkAdrotation(response.getEntity());
    	
    	if (!previousAdvertisername.isEmpty() &&
    			!previousAdvertisername.equals(advertisername)) {
    		testresult = true;
    		break;
    	}
    	previousAdvertisername = advertisername;
    	utils.logHeaders(response);
    }
    Assert.assertTrue("Ad Rotation did not rotate an ad after ten tries", testresult);
  }
  
  protected void checkAdrotation(HttpEntity entity) {
		try {
			InputStream is = entity.getContent();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while ((output = br.readLine()) != null) {
				if (output.contains("advertisername")) {
					logger.info(output);
					advertisername = output;
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	  
}