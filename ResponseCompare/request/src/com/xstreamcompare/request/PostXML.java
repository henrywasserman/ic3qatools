package com.xstreamcompare.request;

import java.io.File;

import java.lang.Integer;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.xstreamcompare.utils.Utilities;

public class PostXML extends Request {
	static Logger logger = Logger.getLogger(PostXML.class);
	Integer TestNumber = null;
	static int testnumber = 0;
	private StringBuffer url1 = null;
	
	  public PostXML(TestCase test) {
		  super(test);
	  }	
	
	public void makeRequests() throws Exception {
		post = new StringBuffer(builddir.toString());
		post.append(file);
		
		HttpClient httpclient = new DefaultHttpClient();

		url1 = new StringBuffer("http://");
		
	    String request1 = test.getRequests().get(0).getURL();

		url1.append(request1.trim());


		try {
			logger.info("TestID: " + test.getTestCaseID());
			
			String postxmlfilename = Utilities.getResponseCompareRoot() + File.separator + "data"
			+ File.separator + "infogroup" + File.separator + "post_xml" 
			+ File.separator +	test.getTestCaseID() + ".xml";			
		
			String postxmlfile = FileUtils.readFileToString(
				new File(postxmlfilename));
			
			String replacementurl = url1.toString();
			
			logger.info("Here the request: " + replacementurl.trim());
			logger.info("And It's related post xml file: " + postxmlfile);
			
			HttpPost httppost = new HttpPost(replacementurl.trim());
			HttpEntity postentity = new StringEntity(postxmlfile);
			httppost.setEntity(postentity);

		    HttpResponse httpresponse = httpclient.execute(httppost);

		    setupAndOutput(httpresponse);
		    
		    validateHeaders(httpresponse,1);
		    
		    utils.logHeaders(httpresponse);
		} catch (Exception e) {
			Assert.fail(e.getMessage()+"\n "+StringUtils.join(e.getStackTrace()).substring(0,512));
		}
	}
}