package com.xstreamcompare.request;

public class SiloNAtoEU extends Silo {
	
	  public SiloNAtoEU(TestCase test) {
		  super(test);
	  }
	  
	  public void makeRequests() throws Exception {
		  silorequestport = props.getProperty("nmg-silo-na-port");
		  testSilo("${na_host_port}",props.getProperty("nmg-silo-eu-port"));

	  }

}
