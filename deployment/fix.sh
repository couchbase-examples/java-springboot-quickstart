#!/bin/bash

sed -i "s|127\.0\.0\.1|$1|" src/main/java/org/couchbase/quickstart/configs/ConfigDb.java

sed -i "s|127\.0\.0\.1|$1|" src/main/java/resources/application.properties

cat src/main/java/org/couchbase/quickstart/configs/ConfigDb.java

cat src/main/resources/application.properties
