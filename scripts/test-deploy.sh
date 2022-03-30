#!/bin/bash
x=""
count=0
while [ "$x" != "Public Content." ]
do
    if [ "$count" == "20" ]
    then
        echo "Giving up after 20 attempts to connect!"
        exit 1
    fi
    x=`curl -X GET http://localhost:8080/api/test/all`
    let count=count+1
    sleep 1s
done