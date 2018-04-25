#!/bin/bash
#Author: William Deming
#sendreport.sh: sends an attached report to a specified email address

report_dir=$1
email_address=$2

echo -e $report_dir | sendmail -v $email_address