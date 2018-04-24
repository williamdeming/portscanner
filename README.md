# portscanner
Authors: Billy and Matt

JavaFX app that scans ports and saves changes in port states as historical data, sending alerts upon changes. Backend uses C libraries libnet and libpcap to construct and receive network packets, run scans.

This program uses:  
libnet from https://github.com/sam-github/libnet  
Libpcap from https://github.com/the-tcpdump-group/libpcap     
Connector/c from https://dev.mysql.com/downloads/connector/c/ 


SETUP:

Change userDir & srcDir in SettingsManager.java
DatabaseUtils.java needs mysql username and password