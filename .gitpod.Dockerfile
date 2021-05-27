FROM deniswsrosa/couchbase6.6.2-gitpod

#Simple example on how to extend the image to install Java and maven
RUN apt-get -qq update && \
     apt-get install -yq maven default-jdk
