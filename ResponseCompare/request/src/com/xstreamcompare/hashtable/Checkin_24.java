package com.xstreamcompare.hashtable;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;
import com.xstreamcompare.hashtable.generated.response_24.CTAActionType;
import com.xstreamcompare.hashtable.generated.response_24.Image;
import com.xstreamcompare.hashtable.generated.response_24.Callstoaction;
import com.xstreamcompare.hashtable.generated.response_24.Calltoaction;
import com.xstreamcompare.hashtable.generated.response_24.DefaultUserAction;
import com.xstreamcompare.hashtable.generated.response_24.Ilapresponse;
import com.xstreamcompare.hashtable.generated.response_24.Message;
import com.xstreamcompare.hashtable.generated.response_24.ObjectFactory;
import com.xstreamcompare.hashtable.generated.response_24.Storefront;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Checkin_24 implements IlapresponseImpl{
	
	private JAXBContext jaxbContext = null;
	private Marshaller marshaller = null;
	private ObjectFactory factory = null;
	private int messageIndex = -1;
	private int storefrontIndex = -1;
	private int actionIndex = -1;
	private int imageIndex = -1;
	private Integer defaultactionIndex = null;
	
	private Ilapresponse ilapresponse = null;

	public Checkin_24() {
		
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
	
	public void setImageIndex(int image) {
		imageIndex = image;
	}
	
	public void setDefaultActionIndex(int defaultaction) {
		defaultactionIndex = Integer.valueOf(defaultaction);
	}
	
	public Integer getDefaultActionIndex() {
		return defaultactionIndex;
	}
	
	public void setMessage() {
		ilapresponse.getMessage().add((Message)factory.createMessage());
	}
	
	public void createImages(int imagecount) {
		for (int count = 0; count < imagecount; count++) {
			Image image = (Image)factory.createImage();
			ilapresponse.getMessage().get(messageIndex).getImage().add(
					image);
		}
	}
	
	public void setCallstoaction() {
		ilapresponse.getMessage().get(messageIndex).setCallstoaction(
			(Callstoaction)factory.createCallstoaction());
	}
	
	public void createActions(int amount) {
		for (int count = 0; count <= amount; count++ ) {
			Calltoaction calltoaction = (Calltoaction)factory.createCalltoaction();
			ilapresponse.getMessage().get(messageIndex).
				getCallstoaction().getCalltoaction().add(calltoaction);
		}
	}
	
	public void setCalltoAction(String action) {
		ilapresponse.getMessage().get(messageIndex)
			.getCallstoaction().getCalltoaction().get(actionIndex)
			.setAction(CTAActionType.valueOf(action));		
	}
	
	public void setCalltoActionData(String data) {
		ilapresponse.getMessage().get(messageIndex)
			.getCallstoaction().getCalltoaction().get(actionIndex)
			.setData(data);		
	}
	
	public void setCalltoActionTrackUrl(String trackurl) {
		ilapresponse.getMessage().get(messageIndex)
		.getCallstoaction().getCalltoaction().get(actionIndex)
		.setTrackurl(trackurl);
	}
	
	public void createStorefronts(int storefronts) {
		for (int count = 0; count < storefronts; count++) {
			Storefront storefront = (Storefront)factory.createStorefront();
			ilapresponse.getMessage().get(messageIndex).getStorefront().add(storefront);
		}
	}
	
	public void createDefaultUserAction(String action) {
		DefaultUserAction dua = 
			(DefaultUserAction) factory.createDefaultUserAction();
		dua.setUserAction(CTAActionType.valueOf(action));
		ilapresponse.getMessage().get(messageIndex).setDefaultUserAction(dua);
		
	}
	
	public void setStorefrontid(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
			.get(storefrontIndex).setStorefrontid(value);
	}
	
	public void setNametag(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setNametag(value);
	}
	
	public void setName(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setName(value);
	}
	
	public void setAddr1(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddr1(value);
	}
	
	public void setAddr2(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddr2(value);
	}
	
	public void setAddrcity(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrcity(value);		
	}
	
	public void setAddrstate(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrstate(value);		
	}
	
	public void setAddrzip(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setAddrzip(value);
	}
	
	public void setPhone(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setPhone(value);
	}
	
	public void setLongitude(int value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setLongitude(value);
	}
	
	public void setLatitude(int value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setLatitude(value);
	}
	
	public void setImageType(String value) {
		ilapresponse.getMessage().get(messageIndex).getImage()
		.get(imageIndex).setType(value);
	}
	
	public void setImageUrl (String value) {
		ilapresponse.getMessage().get(messageIndex).getImage()
		.get(imageIndex).setUrl(value);
	}
	
	public void setImageData (byte[] data) {
		ilapresponse.getMessage().get(messageIndex).getImage()
		.get(imageIndex).setData(data);
	}
	
	public void setStorefrontCountrycode(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setCountrycode(value);
	}
	
	public void setUrl(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setUrl(value);
	}
	
	public void setPlaceid(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setPlaceid(value);
	}
	
	public void setDistance(String value) {
		ilapresponse.getMessage().get(messageIndex).getStorefront()
		.get(storefrontIndex).setDistance(value);
	}
	
	public void setAdvertisername(String value) {
		ilapresponse.getMessage().get(messageIndex).setAdvertisername(value);
	}
	public void setCouponcode(String value) {
		ilapresponse.getMessage().get(messageIndex).setCouponcode(value);
	}
	
	public void setCountrycode(String value) {
		ilapresponse.getMessage().get(messageIndex).setCountrycode(value);
	}
	
	public void setExpirydate(String value){
		ilapresponse.getMessage().get(messageIndex).setExpirydate(value);
	}
	
	public void setLanguage(String value){
		ilapresponse.getMessage().get(messageIndex).setLanguage(value);
	}
	
	public void setMainmessagetext(String value){
		ilapresponse.getMessage().get(messageIndex).setMainmessagetext(value);
	}
	
	public void setMainmessagephonetics(String value){
		ilapresponse.getMessage().get(messageIndex).setMainmessagephonetics(value);
	}
	
	public void setIntrotext(String value){
		ilapresponse.getMessage().get(messageIndex).setIntrotext(value);
	}
	
	public void setCampaignid(String value) {
		ilapresponse.getMessage().get(messageIndex).setCampaignid(value);
	}
	
	public void setCreativeid(long value) {
		ilapresponse.getMessage().get(messageIndex).setCreativeid(value);
	}
	
	public void setNoStorefront(String value) {
		ilapresponse.getMessage().get(messageIndex).setNostorefront(value);
	}
	
	public void setHtmltemplateid(String value) {
		ilapresponse.getMessage().get(messageIndex).setHtmltemplateid(value);
	}
	
	@Override
	public void createPlaceids() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlaceidIndex(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCampaignIndex(int campaign) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCampaignPlaceid(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setplaceidCampaignIndex(int placeidcampaign) {
		// TODO Auto-generated method stub
		
	}
}