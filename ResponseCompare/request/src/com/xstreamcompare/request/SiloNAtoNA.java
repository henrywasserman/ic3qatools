package com.xstreamcompare.request;


public class SiloNAtoNA extends Silo {
	  public SiloNAtoNA(TestCase test) {
		  super(test);
	  }
	  
	  public void makeRequests() throws Exception {
		  silorequestport = props.getProperty("nmg-silo-na-port");
		  testSilo("${na_host_port}",props.getProperty("nmg-silo-na-port"));
	  }
}
