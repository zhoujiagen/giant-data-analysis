FROM ubuntu:16.04

RUN apt-get update && apt-get -y install openjdk-8-jdk
RUN echo "JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" | tee -a /etc/environment

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME
