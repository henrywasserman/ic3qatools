package com.xstreamcompare.request;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.xstreamcompare.utils.Utilities;

public class TestCase {
	
	private String testCaseID="", testCaseDescription="";
	private int lineNum;
	private List<ParsedRequest> requests = null;
	private int reqcounter = -1;
	private boolean hash = false;
	private String comparisonType = "";
	private String imageType = "";
	private String requestfile = "";
	private int numImagesSaved = 0;
	private String xsl = "";
	private int testcasenumber = 0;
	private boolean skipcompare = false;
	private int status = 0;
	private boolean isWapi = false;
	private List<String> sqlStrings = new ArrayList<String>();
	private List<String> dataValidators = new ArrayList<String>();
	public TestCase() {
		requests = new ArrayList<ParsedRequest>();
	}		
	
	public void addCommand(String command, String param) throws UnsupportedEncodingException {
		
		if (param.toLowerCase().contains("responsetype=hashtable")){
			hash = true;
		}
		if (command.equals("GET")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("EXPECT_ERROR")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("GET_WAPI_IMAGE")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
			isWapi = true;
		}
		else if (command.equals("GET_IMAGE")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("GET_ADROTATION")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("GET_IMAGES")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("GET_WITH_SQL_TRACETABLES")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		//else if (command.equals("SAMPLE_KEYWORD")) {
		//	reqcounter++;
		//	requests.add(new ParsedRequest(command,param));
		//}
		else if (command.equals("GET_MINI")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("GETLIST_JASON")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("SILO_NA_NA")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("SILO_NA_EU")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("SILO_EU_EU")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("SILO_EU_NA")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("POST_IMAGE")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("POST_IMAGES")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("POST")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("POST_XML")) {
			reqcounter++;
			requests.add(new ParsedRequest(command,param));
		}
		else if (command.equals("BODY")) {
			requests.get(reqcounter).setBody(param);
		}
		else if (command.equals("POST_XML_SUBS")) {
			requests.get(reqcounter).setPostXMLSubs(param);
		}
		else if (command.equals("STATUS")) {
			requests.get(reqcounter).setStatus(param);
		}
		else if (command.equals("VALIDATE_HEADER")) {
			requests.get(reqcounter).setValidation(command,param);
		}
		else if (command.equals("VALIDATE_HEADER_SET")) {
			requests.get(reqcounter).setValidation(command,param);
		}
		else if (command.equals("VALIDATE_HEADER_NOTEXIST")) {
			requests.get(reqcounter).setValidation(command,param);
		}
		else if (command.equals("VALIDATE_RESPONSE")) {
			requests.get(reqcounter).setValidation(command,param);
			setXSL(param.trim());
		}
		else if (command.equals("SET_HEADER")) {
			requests.get(reqcounter).setHeader(param);
		}
		else if (command.equals("VALIDATE_RAW_RESPONSE")) {
			requests.get(reqcounter).setValidation(command,param);
		}
		else if (command.equals("SKIP_COMPARE")) {
			skipcompare = true;
		}
		else if (command.equals("SQL")) {
			 getSqlStrings().add(param);		
		}
		else if (command.equals("VALIDATE_DATA")) {
			 getDataValidators().add(param);		
		}
		else if (command.equals("VALIDATE_STATUS_CODE")) {
			setStatus(Integer.valueOf(param));
		}
		if (hash) {
			requests.get(reqcounter).setHash(hash);
		}
	}

	public List<ParsedRequest> getRequests() {
		return requests;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	public void saveRequestURLs() {
		String requestdir = Utilities.getResponseCompareRoot() +
			File.separator + "data" + File.separator +
			"infogroup" + File.separator + "request" +
			File.separator;
		
		String requestr= "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
							"<title>Response Compare Tester</title></head><body>";
		try {
			for (ParsedRequest pr : requests){
				if (pr.getBody() == null) { 
					requestr = requestr + "<a href=" + pr.getURL().trim() +
							">" + pr.getURL().trim() + "</a><br>" + "\n";
				}
				else {
					requestr = requestr +  "<h1>Response Compare Tester</h1><hr/>" +
						"<form action=\"http://localhost:8080/RCServer/RCServlet\" method=\"post\">" +
							"<input type=\"radio\" name=\"method\" value=\"post\" checked>POST" +
							"<input type=\"radio\" name=\"method\" value=\"get\">GET" +
							"<p>" +
							"<b>Server</b><p>" +
							"<input name=\"server-port\" type=\"text\" size=\"80\"" +
							"value=\"http://" + pr.getURL() + "\"/>" +
							"<input type=\"submit\" value=\"Submit\" />" +
							"<p>" +
							"<b>Headers</b><p>" +
							"<textarea name=\"headers\" rows=\"10\" cols=\"80\">" +
							pr.getBody().getContentType().getName() + ":" + 
							pr.getBody().getContentType().getValue() +
							String.valueOf("\n") + 
							makeHeaderString(pr.getHeaders()) +
							"</textarea><p>" +
							"<b>Body</b><p>" +
							"<textarea name=\"body\" rows=\"40\" cols=\"160\">" +
							IOUtils.toString(pr.getBody().getContent()) +
							"</textarea><p>" +
							"</form>" +
						"</body>" + 
					"</html>";
				}
				requestr = requestr + "</body></html>";
			
				FileUtils.writeStringToFile(new File(
						requestdir + testCaseID + ".html"), requestr);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getXSL() {
		return xsl;
	}
	
	public void setXSL(String xsl) {
		this.xsl = xsl;
	}

	public String getTestCaseID() {
		return testCaseID;
	}

	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}
	
	public void setStatus(int statuscode) {
		this.status = statuscode;
	}
	
	public int getStatus() {
		return this.status;
	}

	public String getTestCaseDescription() {
		return testCaseDescription;
	}

	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	
	public boolean getHash() {
		return hash;
	}
	
	public void setHash(boolean hash) {
		this.hash = hash;
	}
	
	public boolean getSkipCompare() {
		return skipcompare;
	}

	@Override
	public String toString() {
		String prettyPrint = "Line#: " + lineNum + " TESTCASE: " + testCaseID + ", " + testCaseDescription + "\n";
		for(ParsedRequest req : requests) {
			prettyPrint += req.toString() + "\n";
		}
		return prettyPrint;
	}

	public String getComparisonType() {
		return comparisonType;
	}

	public void setComparisonType() {
		String lastRequest = requests.get(requests.size() - 1).getRequestType();
		if (lastRequest.toLowerCase().contains("image")) {
			comparisonType = "image";
		} else {
			comparisonType = "xml";
		}
	}
	
	public void setRequestFile(String requestfile) {
		this.requestfile = requestfile; 
	}
	
	public String getRequestFile() {
		return requestfile;
	}
	
	public String getRequestFileName() {
		File file = new File(requestfile);
		return file.getName();
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public int getNumImagesSaved() {
		return numImagesSaved;
	}

	public void setNumImagesSaved(int numImagesSaved) {
		this.numImagesSaved = numImagesSaved;
	}
	
	public void setTestCaseNumber(int number) {
		testcasenumber = number;
	}
	public int getTestCaseNumber() {
		return testcasenumber;
	}

	public List<String> getSqlStrings() {
		return sqlStrings;
	}

	public void setSqlStrings(List<String> sqlStrings) {
		this.sqlStrings = sqlStrings;
	}

	public List<String> getDataValidators() {
		return dataValidators;
	}

	public void setDataValidators(List<String> dataValidators) {
		this.dataValidators = dataValidators;
	}

	public boolean isWapi() {
		return isWapi;
	}
	
	private String makeHeaderString(HashMap<String, String> headers) {
		String headerstring = "";
		for (Map.Entry<String, String> header : headers.entrySet()) {
			headerstring = headerstring + header.getKey() + ":" +
			header.getValue() + String.valueOf("\n");
		}
		return headerstring; 
	}
}
