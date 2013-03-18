package com.xstreamcompare.hashtable;

public class IlapresponseFactory {
	
	private IlapresponseImpl ilapresponse = null;
	
	IlapresponseFactory(String type) {

		if (type.equals("endusercheckin")) {
			ilapresponse = new Checkin_24();
		}
		else if (type.equals("placeid")) {
			ilapresponse = new Placeid_24();
		}
	}
	
	 public IlapresponseImpl getIlapresponse() {
		return ilapresponse;
	}
}