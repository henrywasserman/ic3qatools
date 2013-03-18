package com.xstreamcompare.request.test;



import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


public class StringsTest {

	@Test
	public void test() {
		String source = " SQL SELECT COUNT(*) FROM [${lpa_db_name}].dbo.CheckinTrace WHERE Creatives = '${creativeid1},${creativeid2},${creativeid3},${creativeid4},${creativeid5},${creativeid6},' AND (CreateDate >'${db_start_time}') ";
		String result = StringsManipulator.replaceCreativeIds(getIds(), source);
	    Assert.assertTrue(result.contains("42,"));
	    Assert.assertFalse(result.contains("creativeid"));
	    System.out.println(result);
	}
	private List<String> getIds(){
		List<String> ids = new ArrayList<String>();
		ids.add("32");
		ids.add("33");
		ids.add("34");
		ids.add("39");
		ids.add("40");
		ids.add("42");
		return ids;
		
	}
}
