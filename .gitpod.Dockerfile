FROM couchbase:latest

RUN addgroup --gid 33333 gitpod && \
     useradd --no-log-init --create-home --home-dir /home/gitpod --shell /bin/bash --uid 33333 --gid 33333 gitpod && \
     usermod -a -G gitpod,couchbase gitpod && \
     rm -rf /opt/couchbase/var/lib && \
     chmod -R g+rwx /opt/couchbase && \
     mkdir /opt/couchbase/gitpod && \
     chown gitpod:gitpod /opt/couchbase/gitpod && \
     sed -i 's/var/gitpod/g' /opt/couchbase/bin/couchbase-server

RUN echo "* soft nproc 20000\n"\
"* hard nproc 20000\n"\
"* soft nofile 200000\n"\
"* hard nofile 200000\n" >> /etc/security/limits.conf

#Simple example on how to extend the image to install Java and maven
RUN apt-get -qq update && \
     apt-get install -yq maven default-jdk
