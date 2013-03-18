package com.xstreamcompare.hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.xstreamcompare.hashtable.generated.useractions.Ilapresponse;
import com.xstreamcompare.hashtable.generated.useractions.ObjectFactory;

public class UserActions {
	
	private JAXBContext jaxbContext = null;
	private Marshaller marshaller = null;
	private ObjectFactory factory = null;
	
	private Ilapresponse ilapresponse = null;
	
	public UserActions () {
		try {

			jaxbContext = JAXBContext.newInstance(
			"com.xstreamcompare.hashtable.generated.useractions");
			
			marshaller = jaxbContext.createMarshaller();
			factory = new ObjectFactory();
		
			ilapresponse = (Ilapresponse)factory.createIlapresponse();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	Ilapresponse getIlapresponse() {
		return ilapresponse;
	}
	
	Marshaller getMarshaller() {
		return marshaller;
	}
}