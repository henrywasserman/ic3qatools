package com.xstreamcompare.request;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.xstreamcompare.compare.CompareResults;
import com.xstreamcompare.utils.Utilities;

public class MakeRequestTest extends junit.framework.TestCase {
	private RequestFactory rf = null;
	private Request req = null;
	private String results = "";
	private static StringBuffer builddir = null;
	private static StringBuffer requestFile = null;
	private static ArrayList<TestCase> testcaseList = null;
	private int testNumber = 0;
	static Logger logger = Logger.getLogger(MakeRequestTest.class);
	
	public MakeRequestTest(String testMethodName, int testNumber)
			throws Exception {

		super("MakeRequest");
		
		rf = new RequestFactory();
		this.testNumber = testNumber;
	}

	public static void main(String args[]) {
		TestRunner.run(suite());
	}

	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		String testname = "";
        String responseCompareRoot = Utilities.getResponseCompareRoot();
        System.out.println("Here is responseCompareRoot: " + responseCompareRoot);
		 builddir = new StringBuffer(responseCompareRoot + 
				File.separator + "data");

		testcaseList = new ArrayList<TestCase>();
		requestFile = new StringBuffer(builddir.toString());
		
		requestFile.append(File.separator + "infogroup");
		logger.info("Here is requestfile: " + requestFile);
		System.out.println("Here is requestfile: " + requestFile);
		
		if (System.getProperty("test.dir") == null ) {
			logger.info("test.dir is null");
		} else if (!System.getProperty("test.dir").isEmpty()) {
			//requestFile.append(File.separator + System.getProperty("test.dir"));
		}
		
		logger.info("Here is requestfile: " + requestFile);
		System.out.println("Here is requestfile: " + requestFile);
		
		String[] extension = {"req"};
		LinkedList<File> responsecomparefiles = 
			(LinkedList<File>)FileUtils.listFiles(
					new File(requestFile.toString()), extension, false);
      
		
        ScriptParser scriptParser = new ScriptParser(responsecomparefiles);
		try {
			testcaseList = scriptParser.parse();
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < testcaseList.size(); i++) {
			testname = testcaseList.get(i).getTestCaseID().toLowerCase();
			try {
				if (System.getProperty("tc") != null 
						&& !System.getProperty("tc").toLowerCase().equals("none")) {
					if (testname.equals(
						System.getProperty("tc"))) {
						suite.addTest((Test) new MakeRequestTest("MakeRequest", i));
						  System.out.println("ResponseCompare file path: "+testcaseList.get(i).getRequestFile());
						break;
					}
				}
				else if (System.getProperty("filelist") != null
						&& !System.getProperty("filelist").toLowerCase().equals("none")) {
					if (System.getProperty("filelist").toLowerCase().contains(
							testcaseList.get(i).getRequestFileName().toLowerCase())) {
						suite.addTest((Test) new MakeRequestTest("MakeRequest", i));
					}
				}
				else if (System.getProperty("excludefilelist") !=null
						&& !System.getProperty("excludefilelist").toLowerCase().equals("none")) {
					if (!System.getProperty("excludefilelist").toLowerCase().contains(
							testcaseList.get(i).getRequestFileName().toLowerCase())) {
						suite.addTest((Test) new MakeRequestTest("MakeRequest",i));
					}
				}
				else {
					suite.addTest((Test) new MakeRequestTest("MakeRequest", i));
				}
			} 
			catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}
		return suite;
	}

	public void setUp() throws Exception {
	}

	
	public void MakeRequest() throws Exception {
   		try {
			req = rf.getRequest(testcaseList.get(testNumber));
			this.setName(testcaseList.get(testNumber).getTestCaseID() + ": \n" +
					testcaseList.get(testNumber).getTestCaseDescription());
			req.makeRequests();
			req.test.setComparisonType();
			
			if (!req.test.getSkipCompare()) {
				new CompareResults(req.test);
			}
			
   		} catch (AssertionFailedError aex){
   			// process all the junit assertions here;
			req.test.saveRequestURLs();
   			logger.error(aex.getMessage());
   			Assert.fail(req.pathGenerator.getResponseFile()+ ".xml" + "***"+aex.getMessage());
   		} catch (java.lang.Exception e) {
			req.test.saveRequestURLs();
			results = req.pathGenerator.getResponseFile() +
				".xml" +"***"+"TestID: " + req.test.getTestCaseID()+ " failed: " +
				"\n "+e.getMessage()+"\n "+StringUtils.join(e.getStackTrace()).substring(0,512);
			logger.info("TestID: " + req.test.getTestCaseID()+ " failed: " + StringUtils.join(
					e.getStackTrace()).substring(0,512));
			e.printStackTrace();
			Assert.fail(results);
				
   		}
	}
}