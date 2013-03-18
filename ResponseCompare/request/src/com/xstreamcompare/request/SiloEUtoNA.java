package com.xstreamcompare.request;

public class SiloEUtoNA extends Silo {
	
	  public SiloEUtoNA(TestCase test) {
		  super(test);
	  }
	  
	  public void makeRequests() throws Exception {
		  silorequestport = props.getProperty("nmg-silo-eu-port");
		  testSilo("${eu_host_port}",props.getProperty("nmg-silo-na-port"));

	  }

}
