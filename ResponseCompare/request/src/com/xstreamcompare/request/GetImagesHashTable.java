package com.xstreamcompare.request;

import org.apache.log4j.Logger;

public class GetImagesHashTable extends GetHashTable {

	static Logger logger = Logger.getLogger(GetImages.class);

	public GetImagesHashTable(TestCase test) {
		super(test);
	}

	public void makeRequests() throws Exception {	
		super.makeRequests();
		
		getImages();
	}
}
