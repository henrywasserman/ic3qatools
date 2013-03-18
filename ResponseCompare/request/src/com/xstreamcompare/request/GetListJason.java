package com.xstreamcompare.request;

import java.io.File;


import junit.framework.Assert;


import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class GetListJason extends Request {

	@Override
	protected void makeRequests() throws Exception {
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    
	    url1.append("http://"+test.getRequests().get(0).getURL().trim());
	    
	    compensateForF5();

	    logger.info("TestID: " + test.getTestCaseID());
		logger.info("GET Request: " + url1.toString());
		
		test.getRequests().get(0).setURL(url1.toString());
	  
		httpget = new HttpGet(url1.toString().trim());
	    
	    setGetHeaders(0);
	    utils.logHeaders(httpget);
	    HttpResponse response = httpclient.execute(httpget);
	    
	    int status  = response.getStatusLine().getStatusCode();
	    
	    if (test.getStatus() > 0) {
		    Assert.assertTrue("Response was not equal to " + test.getStatus(), test.getStatus() == status);
	    }
	    else {
	    	Assert.assertTrue( "Status: "+ response.getStatusLine().getStatusCode()+" The request "+url1+" was not sucessful",response.getStatusLine().getStatusCode()<300);
	    
	    
	    	validateHeaders(response,0);
	    
	    	setupAndOutput(response);

	    	String jsonString = FileUtils.readFileToString(new File(outputfile.toString()));
	    
	    	String res = convertToXml(jsonString,"",true);
	    
	    	logger.info(res);
		
	    	FileUtils.writeStringToFile(new File(outputfile.toString().replace(".json", ".xml")), res);
		
	    	utils.logHeaders(response);
	    }
	}
	
	public GetListJason(TestCase test) {
		  super(test);
	}
	
	/*
	private String convertToXml(final String json, final String namespace, final boolean addTypeAttributes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
		InputSource source = new InputSource(new StringReader(json));
		Result result = new StreamResult(out);
		transformer.transform(new SAXSource(new JsonXmlReader(namespace, addTypeAttributes),source), result);
        return new String(out.toByteArray());
	}
	*/
}