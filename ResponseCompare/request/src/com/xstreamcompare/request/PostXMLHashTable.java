/**
 * 
 */
package com.xstreamcompare.request;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.xstreamcompare.utils.Utilities;

/**
 * @author hwasserm
 *
 */
public class PostXMLHashTable extends Request {
	static Logger logger = Logger.getLogger(PostXMLHashTable.class);
	Integer TestNumber = null;
	static int testnumber = 0;
	
	public PostXMLHashTable(TestCase test) {
		super(test);
	}			
	
	public void makeRequests() throws Exception {
		
		post = new StringBuffer(builddir.toString());
		post.append(file);
		
		HttpClient httpclient = new DefaultHttpClient();

		url1 = new StringBuffer("http://");
		url2 = new StringBuffer("http://");
		
	    String request1 = test.getRequests().get(0).getURL();
		String request2 = test.getRequests().get(1).getURL();

		url1.append(request1.trim());
		url2.append(request2.trim());

		if (props.getProperty("UseF5").toLowerCase().equals("no")) {
			//This code is for when we are not going through the F5
		
			String url1string = url1.toString();
			if (url1string.contains("/1.1")) {
				url1string = url1string.replace("/1.1/api1.aspx?", "/nmg/adserver?ver=1.1&");
			}
			else if (url1string.contains("/1.2")) {
				url1string = url1string.replace("/1.2/api1.aspx?", "/nmg/adserver?ver=1.1&");
			}
			else if (url1string.contains("/lpa/api1.aspx?")) {
				url1string = url1string.replace("/lpa/api1.aspx?","/nmg/adserver?");
			}
			
			url1 = new StringBuffer(url1string);
			
			String url2string = url2.toString();
			if (url2string.contains("/1.1")) {
				url2string = url2string.replace("/1.1/api2.aspx?", "/nmg/adserver2?ver=1.1&");
			}
			else if (url1string.contains("/1.2")) {
				url2string = url2string.replace("/1.2/api2.aspx?", "/nmg/adserver2?ver=1.1&");
			}
			else if (url2string.contains("/lpa/api2.aspx")) {
				url2string = url2string.replace("/lpa/api2.aspx?","/nmg/adserver2?");
			}
			
			url2 = new StringBuffer(url2string);
			
			/*********************************************/
		}

		try {
			logger.info("TestID: " + test.getTestCaseID());
			logger.info("PostXML Request: " + url1.toString().trim());
			HttpGet httpget = new HttpGet(url1.toString().trim());
			HttpResponse response = httpclient.execute(httpget);

			validateHeaders(response,0);
			HttpEntity entity = response.getEntity();
			
			utils.logHeaders(response);
			
			TestNumber = new Integer(++testnumber);
			
			setIDs(entity,"${campaignid},${storefrontid}");
			
			String postxmlfilename = Utilities.getResponseCompareRoot()
			+ File.separator + "/"+"data" +"/infogroup/post_xml/" +
			test.getTestCaseID() + ".xml";			
		
			String postxmlfile = FileUtils.readFileToString(
				new File(postxmlfilename));
			
			
			// 1 in this case should always be ${nodename1},${nodename2},${nodename..} separated by commas
			if (!postxmlfile.contains("${")) {
				throw new Exception("Post xml file must contain ${} variables");
			}
			
			String replacementurl = url2.toString();
			
			String fixreplace = "";

			for (String replace : replacements.keySet()) {
				fixreplace = replace.replace("$","\\$");
				fixreplace = fixreplace.replace("{","\\{");
				fixreplace = fixreplace.replace("}","\\}");
				postxmlfile = postxmlfile.replaceAll(fixreplace, replacements.get(replace));
			}
			
			postxmlfile = fixTime(postxmlfile);
			
			logger.info("Here's the next request: " + replacementurl.trim());
			logger.info("And It's related post xml file: " + postxmlfile);
			
			HttpPost httppost = new HttpPost(replacementurl.trim());
			HttpEntity postentity = new StringEntity(postxmlfile);
			httppost.setEntity(postentity);

		    HttpResponse httpresponse = httpclient.execute(httppost);
		    
		    setupAndOutput(httpresponse);

		    validateHeaders(httpresponse,1);
		    
		    utils.logHeaders(response);
		    
			String type = getResponseType(url2.toString());
			
			logger.info("Here is type: " + type);
			logger.info("Here is outputfile: " + outputfile);
			
			hashToXML(type,outputfile.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setIDs(HttpEntity entity, String nodes) {

		nodes = nodes.replace("${", "");
		nodes = nodes.replace("}", "");
		
		String[] nodenames = nodes.split(",");
		try {
			InputStream is = entity.getContent();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
	
			while ((output = br.readLine()) != null) {
				logger.info(output);
				for (String node : nodenames) {
					if (output.contains(node)) {
						output = output.trim();
						output = output.replace("<![CDATA[", "");
						output = output.replace("]]>", "");
						output = output.substring(("<" + node + ">").length(),
								output.indexOf("</" + node + ">"));
						replacements.put("${" + node + "}", output);
					}
				}
			}
			br.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	private String fixTime(String postxmlfile) {
	
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		Date date = new Date(currentDate.getTime());
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss");
		String stringdate = "    <tim>" + 
			formatter.format(date) + "</tim>";
	
		Pattern ptnTime = Pattern.compile(".*\\<tim\\>(.*)\\<\\/tim\\>");
		Matcher match = ptnTime.matcher(postxmlfile);
		postxmlfile = match.replaceAll(stringdate);
		return postxmlfile;
	}
}