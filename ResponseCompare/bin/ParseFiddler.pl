my $testcasenumber = 216;
open (RESULTS,"<","1_Full.txt") or die "Could not open 1_Full.txt";
@lines = <RESULTS>;
close (RESULTS);

for (@lines) { 
	print "TESTCASE 0" . $testcasenumber++ ."\, \n" if ($_ =~ /^POST/);
	print "  POST \$\{peopleuri-port}\/\$\{peopleuri-end-point\}",/.*http\:\/\/localhost\:1133\/v1\/people(.*)\sHTTP.*/,"\n" if ($_ =~ /^POST/);
	next if($_ =~ /^{\"MatchCount.*/);
	next if($_ =~ /^{\"DiagnosticsData.*/);
	print "  SET_HEADER ".$_ if ($_ =~ /Accept\:/);
	print "  BODY ", /.*(\<PersonSearch.*)/,"\n" if ($_ =~ /PersonSearch/);
	print "  BODY ".$_ if ($_ =~ /^{/);
	print "  STATUS 400\n" if ($_ =~/Bad Request/);
	print "  SKIP_COMPARE\n" if ($_ =~/Bad Request/);
	print "\n" if ($_ =~ /Connection\:\sClose/);
}
