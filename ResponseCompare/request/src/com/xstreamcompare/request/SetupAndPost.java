package com.xstreamcompare.request;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.Integer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class SetupAndPost extends Request {
	static Logger logger = Logger.getLogger(SetupAndPost.class);
	Integer TestNumber = null;
	static int testnumber = 0;
	private String variables = "";
	Header[] headers = null;

	public SetupAndPost(TestCase test) {
		super(test);
	}

	public void makeRequests() throws Exception {

		HttpClient httpclient = new DefaultHttpClient();

		String request1 = test.getRequests().get(0).getURL();
		String request2 = test.getRequests().get(1).getURL();

		if (props.getProperty("UseF5").toLowerCase().equals("no")) {
			// This code is for when we are not going through the F5

			if (request1.contains("/1.1")) {
				request1 = request1.replace("/1.1/api1.aspx?",
						"/nmg/adserver?ver=1.1&");
			} else if (request1.contains("/1.2")) {
				request1 = request1.replace("/1.2/api1.aspx?",
						"/nmg/adserver?ver=1.1&");
			} else if (request1.contains("/lpa/api1.aspx?")) {
				request1 = request1
						.replace("/lpa/api1.aspx?", "/nmg/adserver?");
			}

			/*********************************************/
		}

		url1.append("http://" + request1.trim());
		variables = utils.extractVariablesFromRequest(request2);

		try {

			logger.info("First Request for Setup and Post: ");
			logger.info(url1.toString().trim());

			httppost = new HttpPost(url1.toString().trim());
			setPostHeaders(0);
			HttpResponse response = httpclient.execute(httppost);

			validateHeaders(response,0);
			HttpEntity entity = response.getEntity();

			utils.logHeaders(response);

			TestNumber = new Integer(++testnumber);

			setIDs(entity, variables);

			String fixreplace = "";
			for (String replace : replacements.keySet()) {
				fixreplace = replace.replace("$", "\\$");
				fixreplace = fixreplace.replace("{", "\\{");
				fixreplace = fixreplace.replace("}", "\\}");
				request2 = request2.replaceAll(fixreplace,
						replacements.get(replace));
			}

			url2.append("http://" + request2.trim());

			if (props.getProperty("UseF5").toLowerCase().equals("no")) {
				// This code is for when we are not going through the F5

				String url2string = url2.toString();
				if (url2string.contains("/1.1")) {
					url2string = url2string.replace("/1.1/api1.aspx?",
							"/nmg/adserver?ver=1.1&");
				} else if (url2string.contains("/1.2")) {
					url2string = url2string.replace("/1.2/api1.aspx?",
							"/nmg/adserver?ver=1.1&");
				} else if (url2string.contains("/lpa/api1.aspx?")) {
					url2string = url2string.replace("/lpa/api1.aspx?",
							"/nmg/adserver?");
				}

				url2 = new StringBuffer(url2string);
				/*********************************************/
			}

			logger.info("Second Request for Setup and POST: ");
			logger.info(url2.toString());

			int lastRequest = test.getRequests().size() - 1;
			test.getRequests().get(lastRequest).setFinalURL(url2.toString());
			
			httppost = new HttpPost(url2.toString().trim());

			HttpResponse httpresponse = httpclient.execute(httppost);

			setupAndOutput(httpresponse);

			validateHeaders(httpresponse,1);

			utils.logHeaders(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setIDs(HttpEntity entity, String nodes) {

		nodes = nodes.replace("${", "");
		nodes = nodes.replace("}", "");

		if (nodes.contains("currenttime")) {

			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			Date date = new Date(currentDate.getTime());

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy.MM.dd.hh.mm.ss");
			replacements.put("${currenttime}", formatter.format(date));
		}

		String[] nodenames = nodes.split(",");

		try {
			InputStream is = entity.getContent();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while ((output = br.readLine()) != null) {
				logger.info(output);
				for (String node : nodenames) {
					if (output.contains(node)) {
						output = output.trim();
						output = output.replace("<![CDATA[", "");
						output = output.replace("]]>", "");
						output = output.substring(("<" + node + ">").length(),
								output.indexOf("</" + node + ">"));
						replacements.put("${" + node + "}", output);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}