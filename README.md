# portscanner
JavaFX app that scans ports and saves changes in port states as historical data, sending alerts upon changes. Backend uses C libraries libnet and libpcap to construct and receive network packets, run scans.

This program uses:  
[libnet](https://github.com/sam-github/libnet)  
[libpcap](https://github.com/the-tcpdump-group/libpcap)     
[connector/c](https://dev.mysql.com/downloads/connector/c/) 
[modififed SYN scan, Network Security Tools (Clarke, Dhanjani)](books.gigatux.nl/mirror/networksecuritytools/0596007949/toc.html)
