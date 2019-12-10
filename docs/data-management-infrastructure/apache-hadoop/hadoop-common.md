# Hadoop Common

## Protocol

```
$ tree -f | grep "\.proto" | grep -v "test" | grep -v "META\-INFO"
```

### hadoop-common-project

```
hadoop-common/src/main/proto/:
GenericRefreshProtocol.proto              // 刷新用户定义的特性的协议
GetUserMappingsProtocol.proto             // 将用户映射到组的协议
HAServiceProtocol.proto                   // 监控和failover服务的HA原语
IpcConnectionContext.proto                // RPC连接上下文的消息定义
ProtobufRpcEngine.proto                   // Hadoop RPC引擎在RPC层解析PB消息定义
ProtocolInfo.proto                        // 获取协议信息的协议
RefreshAuthorizationPolicyProtocol.proto  // 刷新授权策略的协议
RefreshCallQueueProtocol.proto            // 刷新调用队列的协议
RefreshUserMappingsProtocol.proto         // 刷新用户组映射的协议
RpcHeader.proto                           // RPC请求和响应的头的消息定义
Security.proto                            // 安全token的消息定义
TraceAdmin.proto                          // trace管理的协议
ZKFCProtocol.proto                        // 手动控制ZooKeeper failover控制器的协议
```

### hadoop-hdfs-project/

```
hadoop-hdfs/src/contrib/bkjournal/src/main/proto/bkjournal.proto

hadoop-hdfs/src/main/proto/:
DatanodeLifelineProtocol.proto  // DN生命线协议
DatanodeProtocol.proto          // DN到NN的协议
HAZKInfo.proto                  // HA ZooKeeper节点的消息定义
HdfsServer.proto                // HDFS server的消息定义
InterDatanodeProtocol.proto     // DN之间块恢复的协议
JournalProtocol.proto           // 将edits发送到远程节点的协议: 当前是NN到BackupNode
NamenodeProtocol.proto          // 次NN访问主/活跃NN的协议
QJournalProtocol.proto          // 将edits发送到JournalNode的协议
editlog.proto                   // ACL和扩展属性编辑日志
fsimage.proto                   // 文件系统镜像的磁盘布局
```

```
hadoop-hdfs-client/src/main/proto/:
ClientDatanodeProtocol.proto  // 客户端到DN的协议
ClientNamenodeProtocol.proto  // 客户端到NN的协议
ReconfigurationProtocol.proto // 重新配置NN和DN的协议
acl.proto                     // ACL消息定义
datatransfer.proto            // DN数据传输消息定义
encryption.proto              // 加密域消息定义
hdfs.proto                    // HDFS消息: 客户端/服务端和数据传输协议
inotify.proto                 // inotyfy系统消息定义
xattr.proto                   // HDFS扩展属性消息定义
```

```
# rbf: Router-based Federation
hadoop-hdfs-rbf/src/main/proto/:
FederationProtocol.proto      // federation的消息定义
RouterProtocol.proto          // federation的路由管理协议
```

### hadoop-mapreduce-project/hadoop-mapreduce-client

```
hadoop-mapreduce-client-common/src/main/proto/:
HSAdminRefreshProtocol.proto
MRClientProtocol.proto
mr_protos.proto
mr_service_protos.proto

hadoop-mapreduce-client-shuffle/src/main/proto/ShuffleHandlerRecovery.proto
```

### hadoop-yarn-project/hadoop-yarn

```
hadoop-yarn-api/src/main/proto/:
application_history_client.proto
applicationclient_protocol.proto
applicationmaster_protocol.proto
client_SCM_protocol.proto
containermanagement_protocol.proto
yarn_protos.proto
yarn_service_protos.proto

hadoop-yarn-api/src/main/proto/server/:
SCM_Admin_protocol.proto
application_history_server.proto
resourcemanager_administration_protocol.proto
yarn_server_resourcemanager_service_protos.proto
```

```
hadoop-yarn-common/src/main/proto/yarn_security_token.proto
```

```
hadoop-yarn-server/hadoop-yarn-server-applicationhistoryservice/src/main/proto/yarn_server_timelineserver_recovery.proto

hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/:
ResourceTracker.proto
SCMUploader.proto
collectornodemanager_protocol.proto
distributed_scheduling_am_protocol.proto
yarn_server_common_protos.proto
yarn_server_common_service_protos.proto
yarn_server_federation_protos.proto

hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/proto/:
LocalizationProtocol.proto
yarn_server_nodemanager_recovery.proto
yarn_server_nodemanager_service_protos.proto

hadoop-yarn-server/hadoop-yarn-server-resourcemanager/src/main/proto/yarn_server_resourcemanager_recovery.proto
```



## Packages

```
$ tree -d hadoop-common-project/hadoop-common/src/main/java/org/apache/hadoop
hadoop-common-project/hadoop-common/src/main/java/org/apache/hadoop
├── conf                  // 系统参数配置
├── crypto                // 加密
│   ├── key
│   │   └── kms
│   └── random
├── fs                    // 文件系统
│   ├── crypto
│   ├── ftp
│   ├── http
│   ├── local
│   ├── permission
│   ├── sftp
│   ├── shell
│   │   └── find
│   └── viewfs
├── ha                    // NN HA
│   └── protocolPB
├── http                  // Web UI
│   └── lib
├── io                    // 文件IO
│   ├── compress
│   │   ├── bzip2
│   │   ├── lz4
│   │   ├── snappy
│   │   ├── zlib
│   │   └── zstd
│   ├── file
│   │   └── tfile
│   ├── nativeio
│   ├── retry
│   └── serializer
│       └── avro
├── ipc                   // IPC
│   ├── metrics
│   └── protocolPB
├── jmx                   // JMX
├── log                   // 日志
│   └── metrics
├── metrics               // 报告性能度量信息
│   ├── file
│   ├── ganglia
│   ├── jvm
│   ├── spi
│   └── util
├── metrics2              // metrics 2.0
│   ├── annotation
│   ├── filter
│   ├── impl
│   ├── lib
│   ├── sink
│   │   └── ganglia
│   ├── source
│   └── util
├── net                   // 网络拓扑
│   └── unix
├── record                // Hadoop Record I/O, 已由Avro替代
│   ├── compiler
│   │   ├── ant
│   │   └── generated
│   └── meta
├── security              // 安全相关
│   ├── alias
│   ├── authorize
│   ├── http
│   ├── protocolPB
│   ├── ssl
│   └── token
│       └── delegation
│           └── web
├── service               // 带生命周期管理的服务
│   └── launcher
├── tools                 // 用户组映射工具
│   └── protocolPB
├── tracing               // 跟踪工具
└── util                  // 通用工具
    ├── bloom             // Bloom filter实现
    ├── concurrent        // 自定义java.util.concurrent.Executors实现
    ├── curator           // Curator工具
    └── hash              // 哈希实现: Jenkins, Murmur
```

### org.apache.hadoop.conf

> Configuration of system parameters.

### org.apache.hadoop.util

- `GenericOptionsParser`: [hadoop命令](https://hadoop.apache.org/docs/r2.10.0/hadoop-project-dist/hadoop-common/CommandsManual.html)
- `Tool`: MapReduce工具或应用的标准
- `ToolRunner`

### org.apache.hadoop.io

> Generic i/o code for use when reading and writing data to the network, to databases, and to files.

- 序列化: `LongWritable`, `IntWritable`, `DoubleWritable`, `IntWritable`, `Text`, `ArrayWritable`, `VIntWritable`, `VLongWritable`, `NullWritable`, `VersionedWritable`, `ObjectWritable`
- 基于文件的数据结构: `SequenceFile`, `MapFile`, `ArrayFile`, `BloomMapFile`, , `SetFile`
- `IOUtils`: 工具类

#### org.apache.hadoop.io.compress

- `DefaultCodec`
- `GzipCodec`
- `BZip2Codec`
- `Lz4Codec`
- `SnappyCodec`
- [LzoCodec](https://github.com/twitter/hadoop-lzo/blob/master/src/main/java/com/hadoop/compression/lzo/LzoCodec.java)

### org.apache.hadoop.fs

> Implementations of AbstractFileSystem for hdfs over rpc and hdfs over web.
