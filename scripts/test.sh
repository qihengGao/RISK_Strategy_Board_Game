#!/bin/bash

./gradlew build || exit 1
./gradlew cloverAggregateReports || exit 1
ls build/reports
pip3 install bs4
python3 scripts/clover.py
cp -r build/reports/clover/html/* /coverage-out/ || exit 1

