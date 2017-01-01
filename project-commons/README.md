# 1 开发使用的资源

+ Java

一般是Maven项目, Code Template和Formatter使用dev-resources/[STS]*.


$M2_HOME/conf/settins.xml中添加国内镜像:

    <mirrors>
      <!--国内镜像 -->
      <!-- 阿里云 -->
      <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>
      </mirror>
    </mirrors>


+ Scala

一般是SBT项目, Formatter使用dev-resources/[ScalaIDE]*.

+ Go

使用默认的go formatter工具.

+ Python

Code Template和Formatter使用dev-resources/[PyDev]*.
