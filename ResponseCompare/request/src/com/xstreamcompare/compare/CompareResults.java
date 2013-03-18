package com.xstreamcompare.compare;

import com.xstreamcompare.request.TestCase;

public class CompareResults {
	public CompareResults(TestCase test) throws Exception {
		CompareFactory cf = new CompareFactory();
		Compare compare = cf.getCompare(test);
		compare.results();
	}
}
