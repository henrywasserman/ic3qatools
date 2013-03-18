package com.xstreamcompare.hashtable;

import javax.xml.bind.Marshaller;


import com.xstreamcompare.hashtable.generated.response_24.Ilapresponse;

public interface IlapresponseImpl {

	Marshaller getMarshaller();
	
	Ilapresponse getIlapresponse();
	
	void setMessageIndex(int message);
	
	void setStorefrontIndex(int storefronts);
	
	void setActionIndex(int actions);
	
	void setCampaignIndex(int campaign);
	
	void setCampaignPlaceid(String value);
	
	void setplaceidCampaignIndex(int placeidcampaign);
	
	void setDefaultActionIndex(int defaultaction);
	
	Integer getDefaultActionIndex();
	
	void setMessage();
	
	void setCallstoaction();
	
	void createActions(int amount);
	
	void setCalltoAction(String action);
	
	void setCalltoActionData(String data);
	
	void setCalltoActionTrackUrl(String trackurl);
	
	void createStorefronts(int storefronts);
	
	void createDefaultUserAction(String action);
	
	void setStorefrontid(String value);
	
	void setNametag(String value);
	
	void setName(String value);
	
	void setAddr1(String value);
	
	void setAddr2(String value);
	
	void setAddrcity(String value);
	
	void setAddrstate(String value);
	
	void setAddrzip(String value);
	
	void setPhone(String value);
	
	void setLongitude(int value);
	
	void setLatitude(int value);
	
	void setStorefrontCountrycode(String value);
	
	void setUrl(String value);
	
	void setPlaceid(String value);
	
	void setDistance(String value);
	
	void setAdvertisername(String value);

	void setCouponcode(String value);
	
	void setCountrycode(String value);
		
	void setExpirydate(String value);
	
	void setLanguage(String value);
	
	void setMainmessagetext(String value);
	
	void setMainmessagephonetics(String value);
	
	void setIntrotext(String value);
	
	void setCampaignid(String value);
	
	void setCreativeid(long value);
	
	void setNoStorefront(String value);
	
	void setHtmltemplateid(String value);
	
	void createPlaceids();
	
	void setPlaceidIndex(int value);

	void setImageIndex(int i);
	
	public void setImageType(String value);
	
	public void setImageUrl (String value);
	
	public void setImageData (byte[] data);	
	
	public void createImages(int images);
}