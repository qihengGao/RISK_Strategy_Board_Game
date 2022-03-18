#!/bin/bash
if netstat -tuplen | grep 1777
then
   exit 1
else
   exit 0
fi