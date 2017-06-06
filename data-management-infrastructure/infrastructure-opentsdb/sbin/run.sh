VM_ARGS="-server -d64 -Xss256k -Xms512m -Xmx512m -Xloggc:gc.log -XX:PermSize=32m -XX:MaxPermSize=32m -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/root/opentsdb/oom.hprof -XX:+PrintGCDetails"
LIB_PATH=./lib
LIBS=
LIBS=$LIBS:$LIB_PATH/validation-api-1.0.0.GA.jar:$LIB_PATH/validation-api-1.0.0.GA-sources.jar:$LIB_PATH/opentsdb-2.3.0.jar
LIBS=$LIBS:$LIB_PATH/guava-19.0.jar:$LIB_PATH/jackson-annotations-2.4.3.jar:$LIB_PATH/jackson-core-2.4.3.jar
LIBS=$LIBS:$LIB_PATH/jackson-databind-2.4.3.jar:$LIB_PATH/netty-3.9.4.Final.jar:$LIB_PATH/async-1.4.0.jar
LIBS=$LIBS:$LIB_PATH/slf4j-api-1.7.7.jar:$LIB_PATH/commons-math3-3.4.1.jar:$LIB_PATH/commons-jexl-2.1.1.jar
LIBS=$LIBS:$LIB_PATH/commons-logging-1.1.1.jar:$LIB_PATH/jgrapht-core-0.9.1.jar:$LIB_PATH/gwt-user-2.6.0.jar
LIBS=$LIBS:$LIB_PATH/json-20090211.jar:$LIB_PATH/opentsdb_gwt_theme-1.0.0.jar:$LIB_PATH/asynchbase-1.7.2.jar
LIBS=$LIBS:$LIB_PATH/protobuf-java-2.5.0.jar:$LIB_PATH/zookeeper-3.4.6.jar:$LIB_PATH/slf4j-log4j12-1.7.7.jar:$LIB_PATH/log4j-1.2.17.jar
echo $LIBS

########################################
# without output to nohup.out
#nohup java $VM_ARGS -cp .:$LIBS:infrastructure-opentsdb-0.0.1-SNAPSHOT.jar com.spike.giantdataanalysis.opentsdb.example.OpenTSDBExample opentsdb.conf >/dev/null 2>&1 &
# output to nohup.out
#nohup java $VM_ARGS -cp .:$LIBS:infrastructure-opentsdb-0.0.1-SNAPSHOT.jar com.spike.giantdataanalysis.opentsdb.example.OpenTSDBExample opentsdb.conf &

########################################
nohup java $VM_ARGS -cp .:$LIBS:infrastructure-opentsdb-0.0.1-SNAPSHOT.jar com.spike.giantdataanalysis.opentsdb.example.OpenTSDBBenchmark 2 opentsdb.conf &






