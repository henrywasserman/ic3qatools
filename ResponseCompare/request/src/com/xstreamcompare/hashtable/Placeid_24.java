package com.xstreamcompare.hashtable;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;
import com.xstreamcompare.hashtable.generated.response_24.CTAActionType;
import com.xstreamcompare.hashtable.generated.response_24.Callstoaction;
import com.xstreamcompare.hashtable.generated.response_24.Calltoaction;
import com.xstreamcompare.hashtable.generated.response_24.Campaign;
import com.xstreamcompare.hashtable.generated.response_24.DefaultUserAction;
import com.xstreamcompare.hashtable.generated.response_24.Ilapresponse;
import com.xstreamcompare.hashtable.generated.response_24.Message;
import com.xstreamcompare.hashtable.generated.response_24.ObjectFactory;
import com.xstreamcompare.hashtable.generated.response_24.Placeidcampaign;
import com.xstreamcompare.hashtable.generated.response_24.Placeidcampaignlist;
import com.xstreamcompare.hashtable.generated.response_24.Storefront;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Placeid_24 implements IlapresponseImpl {
	
	private JAXBContext jaxbContext = null;
	private Marshaller marshaller = null;
	private ObjectFactory factory = null;
	private int messageIndex = -1;
	private int storefrontIndex = -1;
	private int actionIndex = -1;
	private int placeidIndex = -1;
	private int placeidCampaignIndex = -1;
	private int campaignIndex = -1;
	private Integer defaultactionIndex = null;
	
	private Ilapresponse ilapresponse = null;

	public Placeid_24() {
		
		try {
			jaxbContext = JAXBContext.newInstance(
			"com.xstreamcompare.hashtable.generated.response_24");
			
			marshaller = jaxbContext.createMarshaller();
			
			factory = new ObjectFactory();
			ilapresponse = (Ilapresponse)factory.createIlapresponse();
			
			//this is for pretty formatting of xml
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			
			//this is for keeping namespaces out of the xml
			
			marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
                @Override
                public String[] getPreDeclaredNamespaceUris() {
                    return new String[] { WellKnownNamespace.XML_SCHEMA_INSTANCE, 
                    		WellKnownNamespace.XML_SCHEMA,
                    		WellKnownNamespace.XML_MIME_URI};
                }

                @Override
                public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                    if (namespaceUri.equals(WellKnownNamespace.XML_SCHEMA_INSTANCE))
                        return "xsi";
                    if (namespaceUri.equals(WellKnownNamespace.XML_SCHEMA))
                        return "xs";
                    if (namespaceUri.equals(WellKnownNamespace.XML_MIME_URI))
                        return "xmime";
                    
                    return suggestion;
                }
            });
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Marshaller getMarshaller() {
		return marshaller;
	}
	
	public Ilapresponse getIlapresponse() {
		return ilapresponse;
	}
	
	public void setMessageIndex(int message) {
		messageIndex = message;
	}
	
	public void setStorefrontIndex(int storefronts) {
		storefrontIndex = storefronts - 1;
	}
	
	public void setActionIndex(int actions) {
		actionIndex = actions;
	}
	
	public void setPlaceidIndex(int placeids) {
		placeidIndex = placeids;
	}
	
	public void setCampaignIndex(int campaign) {
		campaignIndex = campaign;
	}
	
	public void setDefaultActionIndex(int defaultaction) {
		defaultactionIndex = Integer.valueOf(defaultaction);
	}
	
	public Integer getDefaultActionIndex() {
		return defaultactionIndex;
	}
	
	public void createPlaceids() {
		//hackadociousness this is just to get by with testing a single
		//nested placeidcampaignlist
		for (int n = 0; n < placeidIndex; n++) {
			ilapresponse.setPlaceidcampaignlist((Placeidcampaignlist)
				factory.createPlaceidcampaignlist());
			ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
				.add((Placeidcampaign)factory.createPlaceidcampaign());
			ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidCampaignIndex).getCampaign()
				.add((Campaign)factory.createCampaign());
		}
	}
	
	public void setplaceidCampaignIndex(int placeidcampaignindex) {
		placeidCampaignIndex = placeidcampaignindex;
	}
	
	public void setCampaignPlaceid(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign().get(placeidIndex - 1)
			.setPlaceid(value);
	}
	
	public void setPlacidCampaign(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidIndex).getCampaign().get(campaignIndex)
			.setCampaignid(value);
	}
	
	public void setPlaceidCampaignMessage() {
		Message message = (Message)factory.createMessage();
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidCampaignIndex ).getCampaign().get(campaignIndex)
			.getMessage().add(message);
	}
	
	public void setMessage() {
		setPlaceidCampaignMessage();
	}
	
	public void setCallstoaction() {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setCallstoaction(
			(Callstoaction)factory.createCallstoaction());
	}
	
	public void createActions(int amount) {
		for (int count = 0; count <= amount; count++ ) {
			Calltoaction calltoaction = (Calltoaction)factory.createCalltoaction();
			ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
			.getMessage().get(messageIndex).
				getCallstoaction().getCalltoaction().add(calltoaction);
		}
	}
	
	public void setCalltoAction(String action) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex)
			.getCallstoaction().getCalltoaction().get(actionIndex)
			.setAction(CTAActionType.valueOf(action));		
	}
	
	public void setCalltoActionData(String data) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex)
			.getCallstoaction().getCalltoaction().get(actionIndex)
			.setData(data);		
	}
	
	public void setCalltoActionTrackUrl(String trackurl) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex)
		.getCallstoaction().getCalltoaction().get(actionIndex)
		.setTrackurl(trackurl);
	}
	
	public void createStorefronts(int storefronts) {
		for (int count = 0; count < storefronts; count++) {
			Storefront storefront = (Storefront)factory.createStorefront();
			ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
			.getMessage().get(messageIndex).getStorefront().add(storefront);
		}
	}
	
	public void createDefaultUserAction(String action) {
		DefaultUserAction dua = 
			(DefaultUserAction) factory.createDefaultUserAction();
		dua.setUserAction(CTAActionType.valueOf(action));
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setDefaultUserAction(dua);
		
	}
	
	public void setStorefrontid(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
			.get(storefrontIndex).setStorefrontid(value);
	}
	
	public void setNametag(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setNametag(value);
	}
	
	public void setName(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setName(value);
	}
	
	public void setAddr1(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddr1(value);
	}
	
	public void setAddr2(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddr2(value);
	}
	
	public void setAddrcity(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrcity(value);		
	}
	
	public void setAddrstate(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrstate(value);		
	}
	
	public void setAddrzip(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrzip(value);
	}
	
	public void setPhone(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setPhone(value);
	}
	
	public void setLongitude(int value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setLongitude(value);
	}
	
	public void setLatitude(int value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setLatitude(value);
	}
	
	public void setStorefrontCountrycode(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setCountrycode(value);
	}
	
	public void setUrl(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setUrl(value);
	}
	
	public void setPlaceid(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setPlaceid(value);
	}
	
	public void setDistance(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setDistance(value);
	}
	
	public void setAdvertisername(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setAdvertisername(value);
	}
	public void setCouponcode(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setCouponcode(value);
	}
	
	public void setCountrycode(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setCountrycode(value);
	}
	
	public void setExpirydate(String value){
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setExpirydate(value);
	}
	
	public void setLanguage(String value){
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setLanguage(value);
	}
	
	public void setMainmessagetext(String value){
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setMainmessagetext(value);
	}
	
	public void setMainmessagephonetics(String value){
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setMainmessagephonetics(value);
	}
	
	public void setIntrotext(String value){
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setIntrotext(value);
	}
	
	public void setCampaignid(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
			.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
			.getMessage().get(messageIndex).setCampaignid(value);
	}
	
	public void setCreativeid(long value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setCreativeid(value);
	}
	public void setNoStorefront(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setNostorefront(value);
	}
	
	public void setHtmltemplateid(String value) {
		ilapresponse.getPlaceidcampaignlist().getPlaceidcampaign()
		.get(placeidCampaignIndex).getCampaign().get(campaignIndex)
		.getMessage().get(messageIndex).setHtmltemplateid(value);
	}

	@Override
	public void setImageIndex(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageType(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageUrl(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageData(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createImages(int images) {
		// TODO Auto-generated method stub

	}
}