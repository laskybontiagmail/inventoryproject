#!/bin/bash

export PG_DB_HOST=TBD
export PG_DB_PORT=5432
export PG_DB_DBNAME=TBD
export PG_DB_USER=TBD
export PG_DB_PASSWORD=TBD

httpPort=8080
currentDIR=`pwd`

#if [ $buildCore -eq 1 ]; then
#	cd ../InventoryCore
#	./cleanAndBuild.sh
#fi
#
#cd $currentDIR
#echo "Building Inventory!"
#
#rm -fvR target
#status=$?
#
#if [ $status -eq 0 ]; then
#	mvn clean
#	status=$?
#else
#	echo "removing target failed!"
#fi
#
#if [ $status -eq 0 ]; then
#	mvn install
#	status=$?
#else
#	echo "mvn clean failed!"
#fi
#
#if [ $status -eq 0 ]; then
#	mvn package
#	status=$?
#else
#	echo "mvn install failed!"
#fi

./cleanAndBuild.sh
status=$?

if [ $status -eq 0 ]; then
	#mvn wildfly-swarm:start
	mvn wildfly-swarm:run -Dswarm.http.port=$httpPort -Dmaven.test.skip=true
	status=$?
else
	echo "mvn package failed!"
fi





