package com.xstreamcompare.hashtable;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Converter {
	
	String type = "";
	
	public Converter(String type) {
		this.type = type;
	}
	
	public void parse(String file) {
		try {

			file = file.trim();
			byte[] hashMessage = FileUtils.readFileToByteArray(new File(file));
			
			byte[] header = new byte[8];
			
			// store off first 8 bytes
			System.arraycopy(hashMessage, 0, header, 0, header.length);

			byte[] remainder = new byte[hashMessage.length - header.length];
			
			System.arraycopy(hashMessage, header.length, remainder, 0, remainder.length);
			
			//new Parser(file,remainder,type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}