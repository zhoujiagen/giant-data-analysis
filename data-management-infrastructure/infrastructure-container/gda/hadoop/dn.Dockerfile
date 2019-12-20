FROM hadoop:2.10.0

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_VERSION 2.10.0
ENV HADOOP_CONF_DIR /etc/hadoop
ENV HADOOP_PID_DIR /gda/pid/hadoop
ENV HADOOP_LOG_DIR /gda/logs/hadoop
ENV HADOOP_PREFIX /gda/hadoop-${HADOOP_VERSION}

# datanode
#CMD service ssh restart && sleep 3
#CMD ssh localhost
#ENTRYPOINT $HADOOP_PREFIX/sbin/hadoop-daemons.sh --config $HADOOP_CONF_DIR --script hdfs start datanode && tail -f /dev/null
ENTRYPOINT tail -f /dev/null
