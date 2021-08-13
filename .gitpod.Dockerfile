FROM couchbase:latest

RUN echo "couchbase soft nproc 20000\n"\
"couchbase hard nproc 20000\n"\
"couchbase soft nofile 200000\n"\
"couchbase hard nofile 200000\n" >> /etc/security/limits.conf

#Simple example on how to extend the image to install Java and maven
RUN apt-get -qq update && \
     apt-get install -yq maven default-jdk
