FROM java8:ubuntu16.04

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_VERSION 2.10.0

RUN apt-get install -y iputils-ping && apt-get install -y openssh-client && apt-get install -y openssh-server && apt-get install -y rsync
RUN ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && chmod 0600 ~/.ssh/authorized_keys

WORKDIR /gda
COPY hadoop-${HADOOP_VERSION}.tar.gz .
RUN tar zxf hadoop-*.tar.gz && rm -rf hadoop-*.tar.gz

COPY etc/hadoop /etc/hadoop
#RUN cp /etc/hadoop/* etc/hadoop/
ENV HADOOP_CONF_DIR /etc/hadoop

WORKDIR /gda/pid/hadoop
ENV HADOOP_PID_DIR /gda/pid/hadoop

WORKDIR /gda/logs/hadoop
ENV HADOOP_LOG_DIR /gda/logs/hadoop

WORKDIR /gda/hadoop-${HADOOP_VERSION}
ENV HADOOP_PREFIX /gda/hadoop-${HADOOP_VERSION}

# namenode Web UI
EXPOSE 50070 50470
# namenode metadata service
EXPOSE 9000
# secondary namenode
EXPOSE 50090 50091
# backup node
EXPOSE 50100 50105
# journal node
EXPOSE 8485 8480 8481
# datanode
EXPOSE 50010 50020 50075 50475

VOLUME ["/gda/logs/hadoop/", "/gda/pid/hadoop/", "/etc/hadoop/"]

CMD service ssh restart && ssh -o stricthostkeychecking=no -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=INFO -C localhost
