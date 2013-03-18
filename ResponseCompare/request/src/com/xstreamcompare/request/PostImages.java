package com.xstreamcompare.request;

import org.apache.log4j.Logger;

public class PostImages extends Post {

	static Logger logger = Logger.getLogger(PostImages.class);
	
	public PostImages(TestCase test) {
		super(test);
	}

	public void makeRequests() throws Exception {	
		super.makeRequests();
		
		getImages();
	}
}