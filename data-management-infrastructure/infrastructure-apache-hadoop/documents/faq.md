# 0 HADOOP_HOME or hadoop.home.dir are not set.

Run AS -> Run Configurations -> Environment

add variable `HADOOP_HOME`, for example: HADOOP_HOME=/Users/jiedong/software/hadoop-2.7.2

# 1 setXIncludeAware is not supported on this JAXP implementation

https://github.com/spring-projects/spring-boot/issues/5035

原因: 类路径中有xerces的旧版本, 移除旧版本, 使用hadoop提供的版本.
