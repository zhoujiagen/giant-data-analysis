# 1 开发使用的资源

+ Java

一般是Maven项目, Code Template和Formatter使用dev-resources/[STS]*.

父项目: `java-parent`.

$M2_HOME/conf/settings.xml中添加国内镜像:

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

父项目: `scala-parent`.

设置阿里云镜像:

    $ cat ~/.sbt/repositories
    [repositories]
      local
      aliyun: http://maven.aliyun.com/nexus/content/groups/public/
      central: http://repo1.maven.org/maven2/

or in build.sbt:

    // resolver
    resolvers += "aliyun Maven" at "http://maven.aliyun.com/nexus/content/groups/public/"

stuck at getting 0.13.13:

scala-parent/project/build.properties

    # or another version you have in local
    sbt.version=0.13.12


**TODO(zhoujiagen)** add more notes here.[20180507]

+ Go

使用默认的go formatter工具.

+ Python

Code Template和Formatter使用dev-resources/[PyDev]*.
