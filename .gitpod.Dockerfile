FROM public.ecr.aws/z2f7n8a1/couchbase-da-containers:couchbase-neo

RUN echo "* soft nproc 20000\n"\
"* hard nproc 20000\n"\
"* soft nofile 200000\n"\
"* hard nofile 200000\n" >> /etc/security/limits.conf

#Simple example on how to extend the image to install Java and maven
RUN apt-get -qq update && \
     apt-get install -yq maven default-jdk sudo git

RUN chmod -R g+rwX /opt/couchbase && \
     addgroup --gid 33333 gitpod && \
     useradd --no-log-init --create-home --home-dir /home/gitpod --shell /bin/bash --uid 33333 --gid 33333 gitpod && \
     usermod -a -G gitpod,couchbase,sudo gitpod && \
     echo 'gitpod ALL=(ALL) NOPASSWD:ALL'>> /etc/sudoers

COPY startcb.sh /opt/couchbase/bin/startcb.sh
USER gitpod
     
