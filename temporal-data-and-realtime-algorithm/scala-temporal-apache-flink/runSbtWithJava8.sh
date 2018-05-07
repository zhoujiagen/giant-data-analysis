# Ubuntu
#sbt -java-home /home/zhoujiagen/devtools/jdk1.8.0_74

# Mac
#export JAVA_7_HOME=`/usr/libexec/java_home -v 1.7`
export JAVA_8_HOME=`/usr/libexec/java_home -v 1.8`
sbt -java-home $JAVA_8_HOME
