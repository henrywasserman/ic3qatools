package com.xstreamcompare.request;

public class RequestFactory {
	private Request req = null;
	private static RequestFactory rf = null;

	public static RequestFactory getInstance() {
		if (rf == null) {
			rf = new RequestFactory();
		}
		return rf;
	}

	public Request getRequest(TestCase test) {
		String type = getRequestType(test);

		if (type.toLowerCase().equals("body")) {
		} else if (type.toLowerCase().equals("get")) {
			req = new Get(test);
		} else if (type.toLowerCase().equals("post")) {
			req = new Post(test);
		} else if (type.toLowerCase().equals("post_xml")) {
			req = new PostXML(test);
		} else if (type.toLowerCase().equals("setup_and_get")) {
			req = new SetupAndGet(test);
		} else if (type.toLowerCase().equals("setup_and_get_hashtable")) {
			req = new SetupAndGetHashTable(test);
		} else if (type.toLowerCase().equals("setup_and_post")) {
			req = new SetupAndPost(test);
		} else if (type.toLowerCase().equals("setup_and_post_hashtable")) {
			req = new SetupAndPostHashTable(test);
		} else if (type.toLowerCase().equals("get_hashtable")) {
			req = new GetHashTable(test);
		} else if (type.toLowerCase().equals("post_hashtable")) {
			req = new PostHashTable(test);
		} else if (type.toLowerCase().equals("post_xml_hashtable")) {
			req = new PostXMLHashTable(test);
//		} else if (type.toLowerCase().equals("sample_keyword")) {
//			req = new SampleKeywordClassfile(test);
		} else if (type.toLowerCase().equals("get_mini")) {
			req = new GetMini(test);
		} else if (type.toLowerCase().equals("expect_error")) {
			req = new ExpectError(test);
		} else if (type.toLowerCase().equals("getlist_jason")) {
			req = new GetListJason(test);			
		} else if (type.toLowerCase().equals("silo_na_na")) {
			req = new SiloNAtoNA(test);
		} else if (type.toLowerCase().equals("silo_na_eu")) {
			req = new SiloNAtoEU(test);
		} else if (type.toLowerCase().equals("silo_eu_eu")) {
			req = new SiloEUtoEU(test);
		} else if (type.toLowerCase().equals("silo_eu_na")) {
			req = new SiloEUtoNA(test);
		} else if (type.toLowerCase().equals("get_image")) {
			req = new GetImage(test);
		} else if (type.toLowerCase().equals("post_image")) {
			req = new PostImage(test);
		} else if (type.toLowerCase().equals("get_images")) {
			req = new GetImages(test);
		} else if (type.toLowerCase().equals("post_images")) {
			req = new PostImages(test);
		} else if (type.toLowerCase().equals("get_images_hashtable")) {
			req = new GetImagesHashTable(test);
		} else if (type.toLowerCase().equals("post_images_hashtable")) {
			req = new PostImagesHashTable(test);
		} else if (type.toLowerCase().equals("get_wapi_image")) {
			req = new GetWapiImage(test);
		} else if (type.toLowerCase().equals("get_adrotation")) {
			req = new GetAdRotation(test);
		} else if (type.toLowerCase().equals("get_with_sql_tracetables")) {
			req = new GetWithSqlTraceTables(test);
		}	

		return req;
	}

	private String getRequestType(TestCase testcase) {
		String requesttype = "";
		try {
			boolean hash = testcase.getHash();
			int size = testcase.getRequests().size();
			requesttype = testcase.getRequests().get(size - 1).getRequestType()
					.toLowerCase();

			if (size == 1) {
				if (requesttype.equals("get")) {
					requesttype = hash ? "get_hashtable" : "get";
				} else if (requesttype.equals("post")) {
					requesttype = hash ? "post_hashtable" : "post";
				} else if (requesttype.equals("get_images")) {
					requesttype = hash ? "get_images_hashtable" : "get_images";
				} else if (requesttype.equals("post_images")) {
					requesttype = hash ? "post_images_hashtable"
							: "post_images";
				} else if (requesttype.equals("get_adrotation")) {
					requesttype = hash ? "get_adrotation_hashtable"
							: "get_adrotation";
				}
				 else if (requesttype.equals("get_with_sql_tracetables")) {
						requesttype = hash ? "get_with_sql_tracetables_hashtable"
								: "get_with_sql_tracetables";
					}
				
			} else if (size == 2) {
				if (requesttype.equals("get")) {
					requesttype = hash ? "setup_and_get_hashtable" : "setup_and_get";
				} else if (requesttype.equals("post")) {
					requesttype = hash ? "setup_and_post_hashtable" : "setup_and_post";
				} else if (requesttype.equals("post_xml")) {
					requesttype = hash ? "post_xml_hashtable" : "post_xml";
				} else if (requesttype.equals("get_images")) {
					requesttype = hash ? "get_images_hashtable" : "get_images";
				} else if (requesttype.equals("post_images")) {
					requesttype = hash ? "post_images_hashtable" : "post_images";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requesttype;
	}
}