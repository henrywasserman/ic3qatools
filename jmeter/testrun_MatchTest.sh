#!/bin/sh
rm /home/ubuntu/apache-jmeter-2.8/bin/samples.jtl
/home/ubuntu/apache-jmeter-2.8/bin/jmeter -n -l /home/ubuntu/apache-jmeter-2.8/bin/samples.jtl -t /home/ubuntu/apache-jmeter-2.8/bin/MatchTest.jmx
