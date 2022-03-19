#!/bin/bash
x=""
count=0
while [ "$x" == "" ]
do
    if [ "$count" == "20" ]
    then
        echo "Giving up after 20 attempts to connect!"
        exit 1
    fi
    x=`netstat -tuplen | grep 1777`
    let count=count+1
    sleep 1s
done