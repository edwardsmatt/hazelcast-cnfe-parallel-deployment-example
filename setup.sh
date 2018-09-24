#!/bin/bash
rm -rf cluster/lib
mkdir -p cluster/lib
curl http://central.maven.org/maven2/com/hazelcast/hazelcast-tomcat7-sessionmanager/1.1.3/hazelcast-tomcat7-sessionmanager-1.1.3.jar > cluster/lib/hazelcast-tomcat7-sessionmanager-1.1.3.jar
curl http://central.maven.org/maven2/com/hazelcast/hazelcast-all/3.9.1/hazelcast-all-3.9.1.jar > cluster/lib/hazelcast-all-3.9.1.jar
mvn clean package
docker-compose up --force-recreate
# cd gatling-test
# mvn gatling:execute

