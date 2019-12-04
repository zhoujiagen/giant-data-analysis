# Apache hadoop

## 0 实践版本

```
1.2.1
2.7.4
2.10.0 // 20191204 using 2.x
```

## 1 资源

+ [Apache Hadoop 2.10.0](https://hadoop.apache.org/docs/r2.10.0/index.html)
+ Hadoop: The Definitive Guide, by Tom White, 2015.

## 2 运行实例

- [Common](hadoop-common.md)
- [HDFS](hadoop-hdfs.md)
- [MapReduce](hadoop-mr.md)
- [Yarn](hadoop-yarn.md)
- [FAQ](hadoop-faq.md)

### RTFSC

项目结构:

``` shell
$ tree -L 2 hadoop-2.10.0-src
├── BUILDING.txt
├── LICENSE.txt
├── NOTICE.txt
├── README.txt
├── dev-support
│   ├── README.md
│   ├── bin
│   ├── determine-flaky-tests-hadoop.py
│   ├── docker
│   ├── test-patch.properties
│   └── verify-xml.sh
├── hadoop-assemblies
│   ├── pom.xml
│   └── src
├── hadoop-build-tools
│   ├── pom.xml
│   └── src
├── hadoop-client                   // Client
│   └── pom.xml
├── hadoop-cloud-storage-project
│   ├── hadoop-cloud-storage
│   └── pom.xml
├── hadoop-common-project           // Common
│   ├── dev-support
│   ├── hadoop-annotations
│   ├── hadoop-auth
│   ├── hadoop-auth-examples
│   ├── hadoop-common
│   ├── hadoop-kms
│   ├── hadoop-minikdc
│   ├── hadoop-nfs
│   └── pom.xml
├── hadoop-dist
│   └── pom.xml
├── hadoop-hdfs-project           // HDFS
│   ├── dev-support
│   ├── hadoop-hdfs
│   ├── hadoop-hdfs-client
│   ├── hadoop-hdfs-httpfs
│   ├── hadoop-hdfs-native-client
│   ├── hadoop-hdfs-nfs
│   ├── hadoop-hdfs-rbf
│   └── pom.xml
├── hadoop-mapreduce-project     // MapReduce
│   ├── bin
│   ├── conf
│   ├── dev-support
│   ├── hadoop-mapreduce-client
│   ├── hadoop-mapreduce-examples
│   ├── lib
│   └── pom.xml
├── hadoop-maven-plugins
│   ├── pom.xml
│   └── src
├── hadoop-minicluster
│   └── pom.xml
├── hadoop-project
│   ├── pom.xml
│   └── src
├── hadoop-project-dist
│   ├── README.txt
│   └── pom.xml
├── hadoop-tools
│   ├── hadoop-aliyun
│   ├── hadoop-ant
│   ├── hadoop-archive-logs
│   ├── hadoop-archives
│   ├── hadoop-aws
│   ├── hadoop-azure
│   ├── hadoop-azure-datalake
│   ├── hadoop-datajoin
│   ├── hadoop-distcp
│   ├── hadoop-extras
│   ├── hadoop-gridmix
│   ├── hadoop-openstack
│   ├── hadoop-pipes
│   ├── hadoop-resourceestimator
│   ├── hadoop-rumen
│   ├── hadoop-sls
│   ├── hadoop-streaming
│   ├── hadoop-tools-dist
│   └── pom.xml
├── hadoop-yarn-project         // Yarn
│   ├── hadoop-yarn
│   └── pom.xml
├── patchprocess
│   ├── KEYS_YETUS
│   ├── gpgagent.conf
│   ├── yetus-0.3.0
│   ├── yetus-0.3.0-bin.tar.gz
│   └── yetus-0.3.0-bin.tar.gz.asc
├── pom.xml
└── start-build-env.sh
```

## 3 安装和配置
