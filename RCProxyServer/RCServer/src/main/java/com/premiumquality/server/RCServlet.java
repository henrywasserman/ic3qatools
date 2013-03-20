package com.premiumquality.server;

import java.io.IOException;
import org.apache.log4j.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RCServlet
 */
public class RCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HttpPost httppost = null;
    private HttpGet httpget = null;
    
	static Logger logger = Logger.getLogger(RCServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RCServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doGet has been called...");
		
		String url = request.getParameter("server-port");
		String body = request.getParameter("body");
		String headers = request.getParameter("headers");
		
		HttpClient httpclient = new DefaultHttpClient();
		
		httpget = new HttpGet(url);
		addHeaders("get",headers);
		
		HttpResponse httpresponse = httpclient.execute(httpget);
		response.getWriter().write(IOUtils.toString(httpresponse.getEntity().getContent()));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("doPost has been called...");
		if (request.getParameter("method").equals("get"))
		{
			doGet(request,response);
			return;
		}

		String url = request.getParameter("server-port");
		String body = request.getParameter("body");
		String headers = request.getParameter("headers");
		
		HttpClient httpclient = new DefaultHttpClient();
		httppost = new HttpPost(url);
		addHeaders("post",headers);
		
		HttpEntity postentity = new StringEntity(body);
		httppost.setEntity(postentity);
		
		HttpResponse httpresponse = httpclient.execute(httppost);
		response.getWriter().write(IOUtils.toString(httpresponse.getEntity().getContent()));
	}
	
	private void addHeaders(String type, String headers) 
	{
		
		String[] headerlist = headers.split("\n");
		for (String header : headerlist)
		{
			String[] keyvaluepair = header.split(":");
			
			if (keyvaluepair.length == 2)
			{
				if (type.equals("post")) {
					httppost.addHeader(keyvaluepair[0],keyvaluepair[1]);
				}
				else //type equals get
				{
					httpget.addHeader(keyvaluepair[0],keyvaluepair[1]);
				}
			}
			if (keyvaluepair.length == 1) 
			{
				if (type.equals("post")) {
					httppost.addHeader(keyvaluepair[0],"");
				}
				else //type equals get
				{
					httpget.addHeader(keyvaluepair[0],"");
				}
			}
		}
	}

}
