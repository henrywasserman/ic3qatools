package com.xstreamcompare.compare;

import org.custommonkey.xmlunit.XMLTestCase;
import com.xstreamcompare.request.TestCase;
import com.xstreamcompare.utils.PathGenerator;
import com.xstreamcompare.utils.Utilities;

public abstract class Compare extends XMLTestCase {
	protected String responseFile = "";
	protected String expectedTransformed = "";
	protected String expected = "";
	protected String fileName = "";
	protected TestCase test = null;
	protected PathGenerator pathGenerator = null;
	protected Utilities utils = null;

	public Compare(TestCase test) {
		this.test = test;
		this.utils = new Utilities();
		pathGenerator = new PathGenerator(test);
		responseFile = pathGenerator.getResponseFile();
		expectedTransformed = pathGenerator.getExpectedTransformed();
		expected = pathGenerator.getExpected();
		
	}
	
	abstract void results() throws Exception;
	
	public Compare(String testMethodName) {
		super(testMethodName);
	}

	public Compare(String testMethodName, String fileName) {
		super(testMethodName);
		this.fileName = fileName;
	}

	public String getResponseFile() {
		return responseFile;
	}
	
	public String getExpectedTransformed() {
		return expectedTransformed;
	}
	
	public String getExpected() {
		return expected;
	}

}