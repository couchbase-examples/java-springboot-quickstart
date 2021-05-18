#!/bin/bash

chmod 666 src/main/java/org/couchbase/quickstart/configs/ConfigDb.java
sed -i "s|localhost|$1|" src/main/java/org/couchbase/quickstart/configs/ConfigDb.java

chmod 666 src/main/resources/application.properties 
sed -i "s|localhost|$1|" src/main/resources/application.properties

cat src/main/java/org/couchbase/quickstart/configs/ConfigDb.java

cat src/main/resources/application.properties
