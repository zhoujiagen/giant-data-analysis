export JAVA_8_HOME=`/usr/libexec/java_home -v 1.8`
export JAVA_HOME=$JAVA_8_HOME

mvn clean install -Dmaven.test.skip=true
mvn eclipse:clean eclipse:eclipse
mvn dependency:sources -DdownloadSources=true
#mvn dependency:resolve -Dclassifier=javadoc
