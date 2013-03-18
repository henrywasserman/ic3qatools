package com.xstreamcompare.request;

public class SiloEUtoEU extends Silo {

	  public SiloEUtoEU(TestCase test) {
		  super(test);
	  }
	  
	  public void makeRequests() throws Exception {
		  silorequestport = props.getProperty("nmg-silo-eu-port");
		  testSilo("${eu_host_port}",props.getProperty("nmg-silo-eu-port"));

	  }
}
