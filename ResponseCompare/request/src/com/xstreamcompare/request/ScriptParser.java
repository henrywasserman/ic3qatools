package com.xstreamcompare.request;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ScriptParser {

	private Collection<File> responsecomparefiles = new ArrayList<File>();
	
	public ScriptParser(Collection<File>responsecomparefiles) {
		this.responsecomparefiles =  responsecomparefiles;
	}
	
	
	public ArrayList<TestCase> parse() throws Exception {

    	ArrayList<TestCase> allTestCases = new ArrayList<TestCase>();
		
		for (File responsecomparefile : responsecomparefiles) {
		
			BufferedReader in = new BufferedReader(new FileReader(responsecomparefile));
		    String line;
		    int lineNum = 0;
	    	Pattern ptnCommand = Pattern.compile("^([a-zA-Z_]+)(.*)"); // Expects "COMMAND and then other stuff"
	    	Pattern ptnTestCase = Pattern.compile("^[a-z}A-Z_]+\\s+([\\da-zA-Z_]+),(.*)"); // Expects "TESTCASE my_id, some description"
	    	
	    	ArrayList<String> allValidCommands = new ArrayList<String>
	    		(Arrays.asList( 
	    			"BODY",
	    			"EXPECT_ERROR",
	    			"GET",
	    			"GET_IMAGE",
	    			"GET_IMAGES",
	    			"GET_WAPI_IMAGE",
	    			"GET_ADROTATION",
	    			//"SAMPLE_KEYWORD",
	    			"VALIDATE_STATUS_CODE",
	    			"GETLIST_JASON",
	    			"GET_MINI",
	    			"POST_IMAGE",
	    			"POST_IMAGES",
	    			"POST",
	    			"POST_XML",
	    			"POST_XML_SUBS",
	    			"VALIDATE_HEADER",
	    			"VALIDATE_HEADER_SET",
	    			"VALIDATE_HEADER_NOTEXIST",
	    			"VALIDATE_IMAGE",
	    			"VALIDATE_IMAGES",
	    			"VALIDATE_RESPONSE",
	    			"VALIDATE_RAW_RESPONSE",
	    			"SET_HEADER",
	    			"SKIP_COMPARE",
	    			"STATUS",
	    			"GET_WITH_SQL_TRACETABLES",
	    			"SQL",
	    			"SILO_NA_NA",
	    			"SILO_NA_EU",
	    			"SILO_EU_EU",
	    			"SILO_EU_NA",
	    			"VALIDATE_DATA"

	    	));
	    	
	    	Matcher match = null;

	    	TestCase currentTestCase = null;
	    	
		    while ((line = in.readLine()) != null) {
		    	line = line.trim();
		    	lineNum++;
		    	if (line.startsWith("#") || line.equals("")) continue; // Skip comments and blanks
		    	match = ptnCommand.matcher(line);
		    	if (match.find()) {
		    		if (match.group(1).equals("TESTCASE")) { // We found a new TESTCASE line
		    			match = ptnTestCase.matcher(line);
		    			if (match.find()) {
			    			currentTestCase = new TestCase();
			    			currentTestCase.setRequestFile(responsecomparefile.toString());
			    			currentTestCase.setLineNum(lineNum);
			    			currentTestCase.setTestCaseID(match.group(1).trim());
		    				currentTestCase.setTestCaseDescription(match.group(2).trim());
			    			allTestCases.add(currentTestCase);
		    			} else {
		    				throwParseError(lineNum, line, "Incorrect TESTCASE line");
		    			}
		    		} else {
		    			if (allValidCommands.contains(match.group(1))) {
		    				// Found supported commands so add them to the testcase object
		    				if (currentTestCase == null) throwParseError(lineNum, line, "Initial TESTCASE not defined");
		    				currentTestCase.addCommand(match.group(1).trim(), match.group(2).trim());
		    			} else {
		    				throwParseError(lineNum, line, "Invalid command. Supported commands are: " + allValidCommands.toString());
		    			}
		    		}
		    	} else {
		    		throwParseError(lineNum, line, "Could not match a command on line using regex pattern: " + ptnCommand.pattern());
		    	}
		    }
		    in.close();
		}
		return allTestCases;
	}
	
	private void throwParseError(int lineNum, String line, String message) throws Exception {
		throw new Exception("Parse error on line " + lineNum + ": \"" + line + "\" -- message is: " + message);
	}

}