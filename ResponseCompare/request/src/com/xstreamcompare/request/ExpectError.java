package com.xstreamcompare.request;

import java.io.File;
import java.lang.Integer;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class ExpectError extends Request {

  static Logger logger = Logger.getLogger(ExpectError.class);
  Integer TestNumber = null;

  public ExpectError(TestCase test) {
	  super(test);
  }
  
  public ExpectError() {
	  super();
  }
  
  public void makeRequests() throws Exception {

		post = new StringBuffer(builddir.toString());
		post.append(file);

		url1.append(props.getProperty("infogroup-protocol") +
				"://" + test.getRequests().get(0).getURL().trim());
		
		HttpClient httpclient = new DefaultHttpClient();
		
		compensateForF5();
		
		try {
		
			logger.info("TestID: " + test.getTestCaseID());
			logger.info("POST Request: " + url1);			

			httppost = new HttpPost(url1.toString().trim());
			entity = test.getRequests().get(0).getBody();
			
			setPostHeaders(0);
			
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			validateHeaders(response,0);
			
			Assert.assertTrue( "Status: "+ response.getStatusLine().getStatusCode()+" The request "+url1+" was not successful",response.getStatusLine().getStatusCode() == 
					test.getRequests().get(0).getStatus());

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
		catch (Exception e) {
			e.printStackTrace();
		}
	}
  }
