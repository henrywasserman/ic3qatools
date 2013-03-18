package com.xstreamcompare.compare;

import com.xstreamcompare.request.TestCase;

public class CompareFactory {
	private Compare compare = null;
	private static CompareFactory cf = null;
	
	public static CompareFactory getInstance() {
		if (cf == null) {
			cf = new CompareFactory();
		}
		return cf;
	}	

	public Compare getCompare(TestCase test) {
		if (test.getComparisonType().equals("xml")) {
			compare = new XmlCompare(test);
		} else if (test.getComparisonType().equals("image")) {
			compare = new ImageCompare(test);
		}
		return compare;
	}
}
