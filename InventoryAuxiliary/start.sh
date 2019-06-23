#!/bin/bash

export PG_DB_HOST=localhost
export PG_DB_PORT=5432
export PG_DB_DBNAME=artanis_inventory_auxiliary
export PG_DB_USER=artanis
export PG_DB_PASSWORD=pansit

httpPort=8081
currentDIR=`pwd`

# reference: https://wildfly-swarm.gitbooks.io/wildfly-swarm-users-guide/configuration_properties.html

#java -jar target/InventoryAuxiliary-swarm.jar -Dswarm.http.port=$httpPort

# [2018/02/13]: this is crashing
#java -jar target/InventoryAuxiliary-swarm.jar -Dswarm.mainClass=com.aiur.EntryPoint
java -jar target/InventoryAuxiliary-swarm.jar -Dswarm.http.port=$httpPort
