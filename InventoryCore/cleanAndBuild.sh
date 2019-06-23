#!/bin/bash

currentDIR=`pwd`



cd $currentDIR
echo "Building Inventory Core!"

rm -fvR target
status=$?

if [ $status -eq 0 ]; then
	mvn clean -Dmaven.test.skip=true
	status=$?
else
	echo "removing target failed!"
fi

if [ $status -eq 0 ]; then
	mvn install -Dmaven.test.skip=true
	status=$?
else
	echo "mvn clean failed!"
fi

if [ $status -eq 0 ]; then
	mvn package -Dmaven.test.skip=true -Dswarm.http.port=$httpPort
	status=$?
else
	echo "mvn install failed!"
fi


