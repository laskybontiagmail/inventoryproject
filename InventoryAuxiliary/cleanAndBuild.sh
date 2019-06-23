#!/bin/bash

export PG_DB_HOST=localhost
export PG_DB_PORT=5432
export PG_DB_DBNAME=artanis_inventory_auxiliary
export PG_DB_USER=artanis
export PG_DB_PASSWORD=pansit

currentDIR=`pwd`
buildCore=0

if [ $buildCore -eq 1 ]; then
	cd ../InventoryCore
	./cleanAndBuild.sh
fi

cd $currentDIR
echo "Building Inventory Auxiliary!"

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
	mvn package -Dmaven.test.skip=true
	status=$?
else
	echo "mvn install failed!"
fi

exit $status



