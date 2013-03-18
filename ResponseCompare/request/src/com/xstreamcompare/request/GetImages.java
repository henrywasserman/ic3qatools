package com.xstreamcompare.request;

import org.apache.log4j.Logger;

public class GetImages extends Get {

	static Logger logger = Logger.getLogger(GetImages.class);
	
	public GetImages(TestCase test) {
		super(test);
	}

	public void makeRequests() throws Exception {	
		super.makeRequests();
		getImages();
	}
}