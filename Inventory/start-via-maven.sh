#!/bin/bash

export PG_DB_HOST=TBD
export PG_DB_PORT=5432
export PG_DB_DBNAME=TBD
export PG_DB_USER=TBD
export PG_DB_PASSWORD=TBD

currentDIR=`pwd`
$httpPort=8080

mvn wildfly-swarm:start -Dswarm.http.port=$httpPort
