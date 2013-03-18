package com.xstreamcompare.request;

import org.apache.log4j.Logger;

public class PostImagesHashTable extends PostHashTable {

	static Logger logger = Logger.getLogger(PostImages.class);

	public PostImagesHashTable(TestCase test) {
		super(test);
	}

	public void makeRequests() throws Exception {	
		super.makeRequests();
		
		getImages();
	}
}
