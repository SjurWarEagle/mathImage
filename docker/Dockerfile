FROM ubuntu:22.04 as buildstep
RUN mkdir -p /project/
RUN mkdir -p /project/client
RUN mkdir -p /project/server
RUN mkdir -p /project/dist/

RUN apt update -y && \
    apt install -y --no-install-recommends --no-install-suggests \
    openjdk-17-jdk \
    curl wget \
    ca-certificates &&\
    rm -rf /var/cache/apt/ &&\
    rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
RUN apt install -y --no-install-recommends --no-install-suggests \
    nodejs

WORKDIR /project/client
ADD client /project/client
RUN npm install --no-audi && \
    npm run build:prod && \
    rm -r node_modules

RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
RUN tar -xvf apache-maven-3.8.5-bin.tar.gz
RUN mv apache-maven-3.8.5 /opt/
RUN rm apache-maven-3.8.5-bin.tar.gz
#RUN M2_HOME='/opt/apache-maven-3.8.5'
#RUN PATH="$M2_HOME/bin:$PATH"

WORKDIR /project/server
ADD server /project/server
RUN /opt/apache-maven-3.8.5/bin/mvn package


FROM ubuntu:22.04
RUN apt update -y && \
    apt install -y --no-install-recommends --no-install-suggests \
        openjdk-17-jre \
        ca-certificates \
        nginx \
        wget curl
WORKDIR /project
WORKDIR /project/server
WORKDIR /project/client
COPY --from=buildstep /project/client/dist /project/client
COPY --from=buildstep /project/server/target/*server*jar /project/server
#ADD docker/nginx/nginx.conf /etc/nginx/nginx.conf
ADD docker/nginx/nginx.conf /etc/nginx/sites-available/default
RUN mkdir -p /scripts
ADD docker/scripts/start_all.sh /scripts
RUN rm -rf /etc/apt/apt.conf.d/90nginx
EXPOSE 98
EXPOSE 9292

#RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
#RUN tar -xvf apache-maven-3.8.5-bin.tar.gz
#RUN mv apache-maven-3.8.5 /opt/
#RUN rm apache-maven-3.8.5-bin.tar.gz

STOPSIGNAL SIGTERM

ENTRYPOINT ["bash","/scripts/start_all.sh"]
