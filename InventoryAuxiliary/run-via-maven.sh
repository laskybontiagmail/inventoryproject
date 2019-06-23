#!/bin/bash

export PG_DB_HOST=localhost
export PG_DB_PORT=5432
export PG_DB_DBNAME=artanis_inventory_auxiliary
export PG_DB_USER=artanis
export PG_DB_PASSWORD=pansit

httpPort=8081
currentDIR=`pwd`

#mvn wildfly-swarm:run -Dswarm.http.port=$httpPort
#mvn wildfly-swarm:run
#mvn wildfly-swarm:run -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Dswarm.mainClass=com.aiur.EntryPoint
mvn wildfly-swarm:run -Dmaven.test.skip=true -Dswarm.mainClass=com.aiur.EntryPoint -Dswarm.http.port=$httpPort
