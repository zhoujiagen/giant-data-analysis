###################################################################
# environment
###################################################################
# Ubuntu
#sbt -java-home /home/zhoujiagen/devtools/jdk1.8.0_74
# Mac
#export JAVA_7_HOME=`/usr/libexec/java_home -v 1.7`
export JAVA_8_HOME=`/usr/libexec/java_home -v 1.8`
# Mac at work
export SPARK_HOME=/Users/jiedong/software/spark-1.5.2-bin-hadoop2.6
# jar filepath
export TARGET_JAR=target/scala-2.11/scala-infrastructure-apache-spark_2.11-1.0.0.jar

### 1 package jar
cd ..
sbt -java-home $JAVA_8_HOME package

### 2 submit spark tasks
$SPARK_HOME/bin/spark-submit \
  --class "com.spike.giantdataanalysis.spark.example.JsonHandleExample" \
  --master local[4] \
  $TARGET_JAR
