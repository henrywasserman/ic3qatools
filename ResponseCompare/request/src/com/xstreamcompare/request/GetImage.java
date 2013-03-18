package com.xstreamcompare.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class GetImage extends Request {

	static Logger logger = Logger.getLogger(GetImage.class);
	Integer TestNumber = null;
	static int testnumber = 0;

	public GetImage(TestCase test) {
		super(test);
	}

	public GetImage() {
		super();
	}

	/*
	 * This contains logic to handle testcases with either just a get_images or
	 * with a get followed by a get_images We should refactor this later on to
	 * be more flexible
	 */
	public void makeRequests() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String getImageUrl = "";
		String infoUrl = "";
		String variables = "";

	if (test.getRequests().size() == 2) {
			infoUrl = test.getRequests().get(0).getURL().trim();
			getImageUrl = test.getRequests().get(1).getURL().trim();
			infoUrl = utils.compensateForF5(infoUrl);
			variables = utils.extractVariablesFromRequest(getImageUrl);

			infoUrl = "http://" + infoUrl;
			logger.info("Info gathering request: " + infoUrl);
			httpget = new HttpGet(infoUrl.trim());
			HttpResponse response = httpclient.execute(httpget);
			validateHeaders(response,0);
			HttpEntity entity = response.getEntity();

			setIDs(entity, variables);

			String fixreplace = "";
			for (String replace : replacements.keySet()) {
				fixreplace = replace.replace("$", "\\$");
				fixreplace = fixreplace.replace("{", "\\{");
				fixreplace = fixreplace.replace("}", "\\}");
				getImageUrl = getImageUrl.replaceAll(fixreplace,
						replacements.get(replace));
			}
		} else {
			getImageUrl = test.getRequests().get(0).getURL().trim();
		}

		getImageUrl = utils.compensateForF5(getImageUrl);
		int lastRequest = test.getRequests().size() - 1;
		test.getRequests().get(lastRequest).setFinalURL(getImageUrl);

		getImageUrl = "http://" + getImageUrl.trim();

		logger.info("Request: " + getImageUrl);
		logger.info("\n");

		TestNumber = new Integer(++testnumber);
		requestAndSaveImageToFile(this, getImageUrl, false, "");
	}

	protected void setIDs(HttpEntity entity, String nodes) {

		nodes = nodes.replace("${", "");
		nodes = nodes.replace("}", "");

		String[] nodenames = nodes.split(",");

		if (nodes.contains("currenttime")) {

			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			Date date = new Date(currentDate.getTime());

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy.MM.dd.hh.mm.ss");
			replacements.put("${currenttime}", formatter.format(date));
		}

		try {
			InputStream is = entity.getContent();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while ((output = br.readLine()) != null) {
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
