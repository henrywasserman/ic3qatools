package com.xstreamcompare.utils;

import java.io.File;
import com.xstreamcompare.request.TestCase;

public class PathGenerator {
	

	private String response = "";
	private String responseDir = "";
	private String responseTransformed = "";
	private String expectedTransformed = "";
	private String expected = "";
	private String dataInfogroupDir = "";
	private String xslDir = "";
	
	public PathGenerator(TestCase test) {
		String responseCompareDir = "";
		responseCompareDir = Utilities.getResponseCompareRoot();
		String dataDir = responseCompareDir + File.separator +  "data"; 
		dataInfogroupDir = dataDir+ File.separator + "infogroup" + File.separator;
		if (test!=null) {
			response = dataInfogroupDir + "response"	+ File.separator + test.getTestCaseID();
			responseTransformed = dataInfogroupDir + "response"	+ File.separator + "transformed" 
					+ File.separator + test.getTestCaseID();
			expectedTransformed = dataInfogroupDir + "expectedresponse" + File.separator
					+ "transformed" + File.separator + test.getTestCaseID();
			xslDir = responseCompareDir + File.separator + "compare" 
					+ File.separator + "xsl" + File.separator;
			expected = dataInfogroupDir + "expectedresponse" + File.separator + test.getTestCaseID();
			responseDir = dataInfogroupDir + "response" + File.separator;
		}
	}

	public String getResponseFile() {
		return response;
	}

	public String getXslDir() {
		return xslDir;
	}
	
	public String getResponseTransformed() {
		return responseTransformed;
	}
		
	public String getExpectedTransformed() {
		return expectedTransformed;
	}
	
	public String getExpected() {
		return expected;
	}
	
	public String getResponseDir() {
		return responseDir;
	}
}
