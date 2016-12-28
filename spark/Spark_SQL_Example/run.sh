### 1 package jar
#sbt clean package


### 2 submit spark tasks
#...
#[info] Packaging {..}/{..}/target/scala-2.10/simple-project_2.10-1.0.jar
#
## Use spark-submit to run your application
#$ YOUR_SPARK_HOME/bin/spark-submit \
#  --class "SimpleApp" \
#  --master local[4] \
#target/scala-2.10/simple-project_2.10-1.0.jar
#...
#Lines with a: 46, Lines with b: 23
/home/zhoujiagen/devtools/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
  --class "JsonHandleExample" \
  target/scala-2.10/spark_sql_example_2.10-1.0.jar

