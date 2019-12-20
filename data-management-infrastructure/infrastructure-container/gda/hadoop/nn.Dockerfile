FROM hadoop:2.10.0

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_VERSION 2.10.0
ENV HADOOP_CONF_DIR /etc/hadoop
ENV HADOOP_PID_DIR /gda/pid/hadoop
ENV HADOOP_LOG_DIR /gda/logs/hadoop
ENV HADOOP_PREFIX /gda/hadoop-${HADOOP_VERSION}

# namenode
RUN $HADOOP_PREFIX/bin/hdfs namenode -format -force
ENTRYPOINT $HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode && tail -f /dev/null
