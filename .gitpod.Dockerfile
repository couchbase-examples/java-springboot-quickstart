FROM couchbase:latest

RUN rm -rf /opt/couchbase/var/lib && \
     chmod -R 777 /opt/couchbase && \
     addgroup --gid 33333 gitpod && \
     useradd --no-log-init --create-home --home-dir /home/gitpod --shell /bin/bash --uid 33333 --gid 33333 gitpod && \
     usermod -a -G gitpod,couchbase gitpod && \
     chown -R gitpod:gitpod /opt/couchbase/var

RUN echo "* soft nproc 20000\n"\
"* hard nproc 20000\n"\
"* soft nofile 200000\n"\
"* hard nofile 200000\n" >> /etc/security/limits.conf

#Simple example on how to extend the image to install Java and maven
RUN apt-get -qq update && \
     apt-get install -yq maven default-jdk

USER gitpod
