package com.xstreamcompare.request.test;

import java.util.List;

public class StringsManipulator {
	public static String replaceCreativeIds(List<String> ids, String source) {
		
	
		int k = 0;
		for (String id : ids) {
			k++;
			source = source.replace("${creativeid" + k + "}", id);
		}

		return source;
	}
	
}

