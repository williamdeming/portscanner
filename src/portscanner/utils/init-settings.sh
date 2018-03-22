#!/bin/bash
#Author: William Deming

addr_dir=/home/admin/Downloads/PortScanner/addresses.txt
sett_dir=/home/admin/Downloads/PortScanner/settings.txt

if [ ! -e "$addr_dir" ]; then
    echo -e "PSentry Network Address Book" >> $addr_dir
fi
if [ ! -e "$sett_dir" ]; then
    echo -e "PSentry User Settings" >> $sett_dir
    echo -e "Always check ports: 21:closed, 22:closed, 23:closed, 25:closed, 53:closed, 80:closed, 443:closed\n" >> $sett_dir
fi