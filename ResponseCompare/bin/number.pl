my $testcasenumber = $ARGV[1];
open (RESULTS,"<",$ARGV[0]) or die "Could not open $ARGV[0]";
@lines = <RESULTS>;
close (RESULTS);

for (@lines) { 
	printf "TESTCASE 04d",$testcasenumber++),"\, ", /.*,\s(.*)/,"\n" if ($_ =~ /^TESTCASE/);
	next if ($_ =~ /^TESTCASE/);
	print $_
}
