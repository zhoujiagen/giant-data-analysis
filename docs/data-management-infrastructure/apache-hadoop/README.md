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

### 配置文件

- [配置项备注 on Google doc](https://docs.google.com/spreadsheets/d/1xkn2421q2AvAWHxml_hsBFWzh6PA5WzOCZyreihL_gE/edit?usp=sharing)

```
$ tree etc/hadoop/ | grep -v "cmd"
etc/hadoop/
├── capacity-scheduler.xml
├── configuration.xsl
├── container-executor.cfg
├── core-site.xml
├── hadoop-env.sh                   // Hadoop环境变量
├── hadoop-metrics.properties
├── hadoop-metrics2.properties
├── hadoop-policy.xml
├── hdfs-site.xml
├── httpfs-env.sh
├── httpfs-log4j.properties
├── httpfs-signature.secret
├── httpfs-site.xml
├── kms-acls.xml
├── kms-env.sh
├── kms-log4j.properties
├── kms-site.xml
├── log4j.properties
├── mapred-env.sh
├── mapred-queues.xml.template
├── mapred-site.xml.template
├── slaves
├── ssl-client.xml.example
├── ssl-server.xml.example
├── yarn-env.sh
└── yarn-site.xml
```

### 启停脚本

```
$ ls bin | grep -v "cmd"
hadoop                        // 见文档中Hadoop Commands Guide
hdfs                          // 见文档中HDFS Commands Guide
mapred
rcc
yarn
```

```
$ ls sbin | grep -v "cmd"
FederationStateStore
distribute-exclude.sh
hadoop-daemon.sh              // 运行Hadoop命令
hadoop-daemons.sh             // 在所有slave上运行Hadoop命令: namenode|secondarynamenode|datanode|journalnode|dfs|dfsadmin|fsck|balancer|zkfc|portmap|nfs3|dfsrouter
hdfs-config.sh
httpfs.sh
kms.sh
mr-jobhistory-daemon.sh
refresh-namenodes.sh
slaves.sh                     // 在所有slave上执行脚本
start-all.sh
start-balancer.sh
start-dfs.sh
start-secure-dns.sh
start-yarn.sh
stop-all.sh
stop-balancer.sh
stop-dfs.sh
stop-secure-dns.sh
stop-yarn.sh
yarn-daemon.sh
yarn-daemons.sh
```

```
$ ls libexec | grep -v "cmd"
hadoop-config.sh              // 包含在hadoop脚本中
hdfs-config.sh                // 包含在hdfs脚本中
httpfs-config.sh
kms-config.sh
mapred-config.sh
yarn-config.sh
```
