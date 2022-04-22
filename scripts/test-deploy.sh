#!/bin/sh
x=""
count=0
while [ "$x" != "Public Content." ]
do
    if [ "$count" == "60" ]
    then
        echo "Giving up after 150 attempts/s to connect!"
        exit 1
    fi
    x=`curl -s -X GET http://localhost:3000/api/test/all`
    echo $x
    let count=count+1
    sleep 1s
done
