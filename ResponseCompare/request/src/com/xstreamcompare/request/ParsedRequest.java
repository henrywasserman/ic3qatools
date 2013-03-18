package com.xstreamcompare.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.http.entity.StringEntity;

public class ParsedRequest {
	private String requestType = "";
	private String url = "";
	private String finalurl = "";
	private HashMap<String,String> headers = null;
	private HashMap<String, HashMap<String,String>> validations = null;
	private String postXMLSubs = "";
	private int status = 0;
	private StringBuffer requestprops = null;
	private Properties props = null;
	private boolean hash = false;
	private StringEntity body = null;
	
	public ParsedRequest(String requestType, String param) {
		
		props = new Properties();

		if (System.getProperties().getProperty("data.dir") == null) {
			requestprops = new StringBuffer(System.getProperty("user.dir") +
					File.separator + ".." + File.separator +
					"properties/" + ((System.getProperties().getProperty("propfile") == null) ? "responsecompare.properties" :
						System.getProperties().getProperty("propfile") + ".properties"));
		}
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
		
		this.requestType = requestType;
		param = formatURL(param);
		this.url = param;
		headers = new HashMap<String,String>();
		validations = new HashMap<String, HashMap<String,String>>();
	}
	
	public String getRequestType() {
		return requestType;
	}
	
	public String getURL() {
		String returnurl = "";
		
		if (finalurl.isEmpty()) {
			returnurl = url;
		}
		else {
			returnurl = finalurl;
		}
			
		return returnurl;
	}
	
	public void setURL(String url) {
		this.url = url;
	}

	public void setFinalURL(String url) {
		finalurl = url;
	}
	
	public String getFinalURL() {
		return finalurl.replace("http://", "");
	}
	
	public void setHeader(String header) {
		String name, value = null;
		String[] headerstring = header.split(":");
		
		if (headerstring.length == 1) {
			name = headerstring[0];
			value = "";
		}
		else {
			name = headerstring[0].trim();
			value = headerstring[1].trim();
		}
		headers.put(name,value);
	}
	
	public HashMap<String,String> getHeaders () {
		return headers;
	}
	
	public void setPostXMLSubs(String subs) {
		postXMLSubs = subs;
	}
	
	public String getPostXMLSubs() {
		return postXMLSubs;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = Integer.valueOf(status);
	}
	
	public void setValidation(String validationtype, String param) {
		if (validations.get(validationtype) == null) {
			validations.put(validationtype, new HashMap<String,String>());
		}
		
		String[] params = {"",""};
		if (param.trim().matches("\\(.*\\)")) {
			String headerSet = param.replaceAll("[\\(\\)]", "");
			for (String next : headerSet.split(",")) {
				validations.get(validationtype).put(next.toLowerCase(), "");
			}
		} else if (param.split(":",2).length == 1){
			params[0] = param.trim();
			validations.get(validationtype).put(params[0].trim(), params[1].trim());
		} else {
			params = param.split(":",2);
			validations.get(validationtype).put(params[0].trim(), params[1].trim());
		}
	}
	
	public HashMap<String, HashMap<String,String>> getValidations() {
		return validations;
	}
	
	public void setHash(boolean isHash) {
		hash = isHash;
	}
	
	public boolean getHash() {
		return hash;
	}
	
	public void setBody(String entity) throws UnsupportedEncodingException {
		this.body = new StringEntity(entity);
	}
	
	public StringEntity getBody() {
		return this.body;
	}
	
	private String formatURL(String param) {
		
		param = replaceParam(param);

		if (!props.getProperty("lpa-port").isEmpty()) {
			param = param.replace(
					"${lpa_host_port}", 
					props.getProperty("lpa-server") + ":" 
					+ props.getProperty("lpa-port"));
		}
		else {
			param = param.replace("${lpa_host_port}",
					props.getProperty("lpa-server"));
		}
		
		if (!props.getProperty("proxbanner-port").isEmpty()) {
			param = param.replace(
					"${proxbanner_host_port}", 
					props.getProperty("proxbanner-server") + ":" 
					+ props.getProperty("proxbanner-port"));
		}
		else {
			param = param.replace("${proxbanner_host_port}",
					props.getProperty("proxbanner-server"));
		}		
		
		param = param.replace("${companiesuri-end-point}",
					props.getProperty("companiesuri-end-point"));
		
		param = param.replace("${peopleuri-end-point}",
				props.getProperty("peopleuri-end-point"));

		param = param.replace("${miniuri-end-point}",
				props.getProperty("miniuri-end-point"));
		
		param = param.replace("${matchuri-end-point}",
				props.getProperty("matchuri-end-point"));
		
		param = param.replace("${multimatchuri-end-point}",
				props.getProperty("multimatchuri-end-point"));

		param = param.replace("${sicsuri-end-point}",
				props.getProperty("sicsuri-end-point"));
		
		param = param.replace("${apikey}",
				props.getProperty("apikey"));
		
		param = param.replace("${version}",
				props.getProperty("version"));
		
		param = param.replace("${lpa_end_point}",
				props.getProperty("lpa-end-point"));
		
		param = param.replace("${lpa_version}",
				props.getProperty("lpa-version"));
		
		param = param.replace("${infogroup_end_point2}",
				props.getProperty("infogroup-end-point2"));
		
		param = param.replace("${proxbanner_end_point}", 
				props.getProperty("proxbanner-end-point"));
		
		param = param.replace("${proxbanner_icon_url_24x24}",
				props.getProperty("proxbanner-icon-url-24x24"));
		
		param = param.replace("${proxbanner_icon_url_32x32}",
				props.getProperty("proxbanner-icon-url-32x32"));
		
		param = param.replace("${proxbanner_icon_url_64x64}",
				props.getProperty("proxbanner-icon-url-64x64"));
		
		return param;
	}
	
	private String replaceParam(String param) {
		String port = param.substring(param.indexOf("{") + 1,param.indexOf("}"));
		String endpoint = param.substring(param.indexOf("/") + 1);
		if (!props.getProperty(port).isEmpty()) {
			
			if (requestType.equals("POST_XML")) {
				param = param.replace(
						param, 
						props.getProperty("iusamatch-server") + ":"
						+ props.getProperty("iusamatch-port"));
			} else if (requestType.equals("GET_MINI")) {
				param = param.replace(
						param, 
						props.getProperty("mini-server") + ":" 
						+ props.getProperty("miniuri-port"));
			} else {
			
				param = param.replace(
					param, 
					props.getProperty("infogroup-server") + ":" 
					+ props.getProperty(port));
			}
		} else {
			param = param.replace("${infogroup_host_port}",
					props.getProperty("infogroup-server"));
		}
		
		if (requestType.equals("POST_XML")) {
			return param;
		}
		else {
			return param + "/" + endpoint;
		}
		
	}
	
}