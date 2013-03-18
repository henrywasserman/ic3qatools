package com.xstreamcompare.request;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import net.javacrumbs.json2xml.JsonXmlReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.custommonkey.xmlunit.Transform;
import org.xml.sax.InputSource;

import com.xstreamcompare.hashtable.Converter;
import com.xstreamcompare.utils.PathGenerator;
import com.xstreamcompare.utils.Utilities;

public abstract class Request {

	static Logger logger = Logger.getLogger(Request.class);
	
	protected String request = "";
	protected String output = "";
	protected String file = "";
	protected StringBuffer outputfile = null;
	protected StringBuffer fullpath = null;
	protected StringBuffer requestprops = null;
	protected StringBuffer builddir = null;
	protected StringBuffer post = null;
	protected StringBuffer postXML = null;
	protected StringBuffer response = new StringBuffer();
	protected StringBuffer cancel = null;
	protected Properties props = null;
	protected StringBuffer url1 = new StringBuffer("");
	protected StringBuffer url2 = new StringBuffer("");
	protected String ratecode = "";
	protected String indate = "";
	protected String outdate = "";
	protected StringBuffer results = null;
	protected boolean passfail = true;
	protected boolean reportfilename = false;
	protected boolean result = true;
	protected HashMap<String, String> map;
	protected ArrayList<String> requests = new ArrayList<String>();
	protected HashMap<String, String> replacements = new HashMap<String, String>();
	protected TestCase test = null;
	protected HttpGet httpget = null;
	protected HttpPost httppost = null;
	protected Utilities utils = null;
	protected String requestType = "";
	protected String validateHeaderRegEx = "";
	protected PathGenerator pathGenerator = null;
	protected StringEntity entity = null;
	
	protected Request() {
		this.utils = new Utilities();
		pathGenerator = new PathGenerator(test);
		props = utils.getRequestProperties();
		results = new StringBuffer(10);
		passfail = true;
		result = true;
		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator +
				"properties" + File.separator + "log4j.properties");
		if (builddir!=null) {
			response = new StringBuffer(builddir.toString());
		}

		response.append(Utilities.getResponseCompareRoot()+"/data"+"/response/");
		
	}
	
	protected void compensateForF5() {
		utils.compensateForF5(url1);
	}
	
	public TestCase getTest() {
		return test;
	}
	
	public PathGenerator getPathGenerator() {
		return pathGenerator;
	}

	protected Request(TestCase test) {
		this.utils = new Utilities();
		pathGenerator = new PathGenerator(test);
		results = new StringBuffer(10);
		passfail = true;
		result = true;

		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator +
				"properties" + File.separator + "log4j.properties");
		
		this.test = test;

		props = new Properties();

		builddir = new StringBuffer(System.getProperty("user.dir") +
				File.separator + ".." + File.separator);
		
		requestprops = new StringBuffer(System.getProperty("user.dir") +
				File.separator + ".." + File.separator +
				"properties" + File.separator +
				((System.getProperties().getProperty("propfile") == null) ? "responsecompare.properties" :
					System.getProperties().getProperty("propfile") + ".properties"));

		
		response.append(Utilities.getResponseCompareRoot() + File.separator+ "data"+File.separator + "response" + File.separator);

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
	}
	
	protected String getFileExtFromContentType(Request request, HttpResponse response) {
		String extension = ".xml"; // Default to an extension of .xml
		Pattern ptnImageExt = Pattern.compile("[a-z]+$");
		Matcher match = ptnImageExt.matcher(response.getFirstHeader("Content-Type").getValue().trim());
		
		// Note: we ignore Content-Type if it's text/html because this is set incorrectly in some cases
		if (match.find()) {
			if (match.group(0).toLowerCase().equals("png")) {
				extension = ".png";
				request.getTest().setImageType("png");
			} else if (match.group(0).toLowerCase().equals("jpeg")) {
				extension = ".jpg";
				request.getTest().setImageType("jpg");
			} else if (match.group(0).toLowerCase().equals("json")) {
				extension = ".json";
			}
		}
		
		if (test.getHash()) {
			extension = ".hash";
		}
		
		return extension;
	}
	
	protected void cleanResults() {
		results = new StringBuffer(10);
	}

	protected String getFilename(String filename) {
		int sep = filename.lastIndexOf(System.getProperties().getProperty(
				"file.separator"));
		int dot = filename.lastIndexOf('.');
		return filename.substring(sep + 1, dot);
	}

	protected boolean getPassFail() {
		return passfail;
	}

	protected boolean getResult() {
		return result;
	}

	protected String getResults() {
		return results.toString();
	}

	protected void hashToXML(String type, String outputfile) {
		Converter convert = new Converter(type);
		convert.parse(outputfile.toString());
	}

	protected abstract void makeRequests() throws Exception;

	protected void resetOut(PrintStream out) {
		System.setOut(out);
	}

	protected void setOut() {
		System.setOut(new PrintStream(new BufferedOutputStream(
				new FileOutputStream(java.io.FileDescriptor.out), 128), true));
	}
	
	protected void setupAndOutput(HttpResponse response) throws Exception {
		fullpath = new StringBuffer(file);
		outputfile = new StringBuffer(pathGenerator.getResponseDir() + test.getTestCaseID());
		
		// First, let getFileExtFromContentType try to figure out what kind of file this is
		String extension = getFileExtFromContentType(this, response);
		StringBuffer outputfileNoExt = new StringBuffer(outputfile);
		outputfile.append(extension);

		HttpEntity entity = response.getEntity();
		OutputStream outputstream = new FileOutputStream(outputfile.toString());
		entity.writeTo(outputstream);
		outputstream.close();
		
		// Second, since we could have defaulted to an extension of xml incorrectly,
		// examine the file content to see what it really is
		String fileContents = FileUtils.readFileToString(new File(outputfile.toString()));
		if (fileContents.contains("<html ")) {
			outputfileNoExt.append(".html");
			new File(outputfileNoExt.toString()).delete();
			new File(outputfile.toString()).renameTo(new File(outputfileNoExt.toString()));
			outputfile = outputfileNoExt;
		} //else if (fileContents.matches("^[\\s]*\\{.*")) {
			//outputfileNoExt.append(".json");
			//new File(outputfile.toString()).renameTo(new File(outputfileNoExt.toString()));
			//new File(outputfileNoExt.toString()).delete();
			//outputfile = outputfileNoExt;
		//}
		
		if (fileContents != null) {
		//	logger.debug("File: " + outputfile + " before any transformation: ");
		//	logger.debug(fileContents);
		}
	}
	
	protected void requestAndSaveImageToFile(Request request, String imageUrl, boolean doPost, String fileNamePostfix) throws Exception {
		imageUrl = StringEscapeUtils.unescapeXml(imageUrl.trim());
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		if (doPost) {
			HttpPost httppost = new HttpPost(imageUrl);
			request.addHeaders();
			response = httpclient.execute(httppost);
		} else {
			HttpGet httpget = new HttpGet(imageUrl);
			request.addHeaders();
			response = httpclient.execute(httpget);
		}
		request.validateHeaders(response,0);
		HttpEntity entity = response.getEntity();

		StringBuffer outputfile = new StringBuffer(request.getPathGenerator().getResponseDir() +
				request.getTest().getTestCaseID() + fileNamePostfix);
		
		outputfile.append(getFileExtFromContentType(request, response));
		OutputStream outputstream = new FileOutputStream(outputfile.toString());
		entity.writeTo(outputstream);
		outputstream.close();
		logger.info("Image URL: " + imageUrl);
		logger.info("Image file: " + outputfile.toString());
	}	
	
	protected String getResponseType(String url) {
		String type = "";
		
		if (url.contains("enduserregistration")) {
			type = "enduserid";
		}
		else if (url.contains("enduseractions")) {
			type = "enduserid";
		}
		else if (url.contains("endusercheckin")) {
			type = "endusercheckin";
		}
		else if (url.contains("enduserplaceidcheckin")) {
			type = "placeid";
		}
		else if (url.contains("enduserareacheckin")) {
			type = "endusercheckin";
		}
		//Right now when the url is Empty it means we are doing an
		//XML POST
		else if (url.contains("api2") || url.contains("adserver2")) {
			type = "enduserid";
		}
		return type;
	}
	
	protected void setValidateHeaderRegex(String regex) {
		validateHeaderRegEx = regex;
	}
	
	protected String getValidateHeaderRegex() {
		return validateHeaderRegEx;
	}
	
	public void addHeaders() {
	    HashMap <String,String> getheaders = test.getRequests().get(0).getHeaders();
	    for (Map.Entry<String,String> header : getheaders.entrySet()) {
			httpget.addHeader(header.getKey(),header.getValue());
		}
	}
	
	protected void setHeadersForAGet(int instance) {
		HashMap <String,String> headers = null;
		headers = test.getRequests().get(instance).getHeaders();
		for (Map.Entry<String,String> header : headers.entrySet()) {
			httpget.addHeader(header.getKey(),header.getValue());
		}
	}
	
	protected void setRequestType(String type) {
		requestType = type;
	}
	
	protected String getRequestType() {
		return requestType;
	}
	
	protected void setGetHeaders(int request) {
		HashMap<String,String> headers = test.getRequests().get(request).getHeaders();
		
		for (Map.Entry<String,String> header: headers.entrySet()) {
			httpget.addHeader(header.getKey(), header.getValue());
		}
		
	}
	
	protected boolean isJasonRequest(int request) {
		boolean isjason = false;
		HashMap<String,String> headers = test.getRequests().get(request).getHeaders();
		
		for (Map.Entry<String,String> header: headers.entrySet()) {
			if (header.getValue().equals("application/json")) {
				isjason = true;
				break;
			}
		}
		return isjason;
	}

	protected void setPostHeaders(int request) {
		HashMap<String,String> headers = test.getRequests().get(request).getHeaders();
		
		for (Map.Entry<String,String> header: headers.entrySet()) {
			if (header.getKey().equals("Accept")) {
				entity.setContentType(header.getValue());
			}
			httppost.addHeader(header.getKey(), header.getValue());
		}
		
	}
	
	protected String convertToXml(final String json, final String namespace, final boolean addTypeAttributes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
		InputSource source = new InputSource(new StringReader(json));
		Result result = new StreamResult(out);
		transformer.transform(new SAXSource(new JsonXmlReader(namespace, addTypeAttributes),source), result);
        return new String(out.toByteArray());
	}	
	
	protected void getImages() {
		try {
			String xmloutput = FileUtils.readFileToString(new File(outputfile.toString()));
			File xslfile = new File (pathGenerator.getXslDir() + "imageurls_response.xsl");
			Transform transform = new Transform(xmloutput, xslfile);
			String transformedxml = transform.getResultString();
			
			String[] lines = transformedxml.split("\n");
			
			boolean foundImageNode = false;
			
			int counter = 0;
			String type = "";
			String filename = "";
			
			for (String output : lines) {
				counter++;
				if (output.contains("<image>")) foundImageNode = true;
				if (output.contains("</image>")) foundImageNode = false;
				if ((output.contains("<url>")) && foundImageNode) {
					output = output.trim();
					output = output.replace("<![CDATA[", "");
					output = output.replace("]]>", "");
					output = output.replace("<url>", "");
					output = output.replace("</url>", "");

					if (output.toString().trim().equals("")) continue;
					
					type = lines[counter];
					type = type.replace("<type>","");
					type = type.replace("</type>","").trim();
					
					filename = URLDecoder.decode(output, "UTF-8");
					filename = StringUtils.substringAfterLast(filename, "/");
					filename = FilenameUtils.removeExtension(filename);
					
					requestAndSaveImageToFile(this, output, false, 
							"_" + filename + "_type_" + type);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validateHeaders(HttpResponse response, int request) {
		Pattern p = null;
		Matcher m = null;
		Header[] headers = response.getAllHeaders();
		HashMap<String, HashMap<String,String>> validations = null;
		HashMap<String,String> headerValidations = null;
		validations = test.getRequests().get(request).getValidations();
		boolean found = false;

		for (Map.Entry<String,HashMap<String,String>> validation : validations.entrySet()) {
			if (validation.getKey().toLowerCase().equals("validate_header")) {
				headerValidations = validation.getValue();
				for (Map.Entry<String,String> validate : headerValidations.entrySet()) {
					for (Header httpheader : headers) {
						logger.debug(httpheader.getName());
						logger.debug(httpheader.getValue());
						logger.debug(validate.getKey());
						logger.debug(validate.getValue());
						if (httpheader.getName().equals(validate.getKey())) {
						
							p = Pattern.compile(validate.getValue());
							m = p.matcher(httpheader.getValue());
							found = m.find();
							break;
						}
					}
					passfail = found;
					results = new StringBuffer(pathGenerator.getResponseDir() + test.getTestCaseID() + ".xml" + " Did not find Header: " + validate.getKey()
							+ ":" + validate.getValue() + " Here are the headers: " +
							StringUtils.join(headers)
					); 
					found = false;
				}
			}
			else if (validation.getKey().toLowerCase().equals("validate_header_notexist")) {
				headerValidations = validation.getValue();
				for (Map.Entry<String,String> validate : headerValidations.entrySet()) {
					for (Header httpheader : headers) {
						logger.debug(httpheader.getName());
						logger.debug(httpheader.getValue());
						logger.debug(validate.getKey());
						logger.debug(validate.getValue());
						if (httpheader.getName().equals(validate.getKey())) {
						
							p = Pattern.compile(validate.getValue());
							m = p.matcher(httpheader.getValue());
							found = m.find();
							break;
						}
					}
					passfail = !found;
					results = new StringBuffer(pathGenerator.getResponseDir() + test.getTestCaseID() + ".xml" + " Found Header: " + validate.getKey()
							+ ":" + validate.getValue());
					
					found = false;
				}
			}
			else if (validation.getKey().toLowerCase().equals("validate_header_set")) {
				List<String> listHeaders = new ArrayList<String>();
				for (Header httpheader : headers) {
					listHeaders.add(httpheader.getName().toLowerCase());
				}
				headerValidations = validation.getValue();
				for (Map.Entry<String,String> validate : headerValidations.entrySet()) {
					passfail = false;
					results = new StringBuffer ("The header: " + validate.getKey() + " is missing from the set of headers" +
							listHeaders.contains(validate.getKey().toLowerCase().trim()));
					listHeaders.remove(validate.getKey().toLowerCase().trim());
				}
				
				if (!listHeaders.isEmpty()) {
					passfail = false; 
					results = new StringBuffer("There are unexpected headers: " + listHeaders.toString());
				}
			}
		}
	}
}