#!/bin/bash
#Author: William Deming
#init-settings.sh: create xml for addresses and ports if files aren't present


node_dir=/home/admin/Downloads/PortScanner/settings/nodes.xml

if [ ! -e "$node_dir" ]; then
    echo -e "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--PSentry Nodes-->\n<nodelist>" >> $node_dir
    echo -e "  <node>\n    <address>10.0.8.11</address>\n    <port>\n      <number>21</number>\n      <expectedstatus>closed</expectedstatus>\n    </port>\n  </node>" >> $node_dir
    echo -e "</nodelist>" >> $node_dir
fi

chmod -R 0777 /home/admin/Downloads/PortScanner/settings