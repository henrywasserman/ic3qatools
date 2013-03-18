package com.xstreamcompare.request;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.xstreamcompare.hashtable.Converter;

public class HashToXML {

	static Logger logger = Logger.getLogger(HashToXML.class);
	public static  void main (String[] args) {
		//Type is either enduserid or endusercheckin
		CommandLineParser parser = new PosixParser();
		
		Options options = new Options();
		options.addOption("t","type",true,
				"for endusercheckin or enduserid type xml");
		options.addOption("f","file",true,
				"path and filename to parse");
		
		
		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator +
				"properties" + File.separator + "log4j.properties");		
		
		//parse the command line arguments
		
		try {
			CommandLine line = parser.parse(options, args);
			
			String type = line.getOptionValue("type");
			String hashfile = line.getOptionValue("file");
		
			Converter convert = new Converter(type);
				convert.parse(hashfile.toString());
						
			
		}
		catch (ParseException exp)  {
			logger.info("Unexpected exception:" + exp.getMessage());
		}
		
	}
}