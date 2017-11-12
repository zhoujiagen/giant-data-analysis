mvn clean install -Dmaven.test.skip=true
mvn eclipse:clean eclipse:eclipse
mvn dependency:sources -DdownloadSources=true
#mvn dependency:resolve -Dclassifier=javadoc
