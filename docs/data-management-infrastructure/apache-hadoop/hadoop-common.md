# Hadoop Common

## Protocol

```
$ tree -f | grep "\.proto" | grep -v "test" | grep -v "META\-INFO"
```

### hadoop-common-project

```
hadoop-common/src/main/proto/GenericRefreshProtocol.proto
hadoop-common/src/main/proto/GetUserMappingsProtocol.proto
hadoop-common/src/main/proto/HAServiceProtocol.proto
hadoop-common/src/main/proto/IpcConnectionContext.proto
hadoop-common/src/main/proto/ProtobufRpcEngine.proto
hadoop-common/src/main/proto/ProtocolInfo.proto
hadoop-common/src/main/proto/RefreshAuthorizationPolicyProtocol.proto
hadoop-common/src/main/proto/RefreshCallQueueProtocol.proto
hadoop-common/src/main/proto/RefreshUserMappingsProtocol.proto
hadoop-common/src/main/proto/RpcHeader.proto
hadoop-common/src/main/proto/Security.proto
hadoop-common/src/main/proto/TraceAdmin.proto
hadoop-common/src/main/proto/ZKFCProtocol.proto
```

### hadoop-hdfs-project/

```
hadoop-hdfs/src/contrib/bkjournal/src/main/proto/bkjournal.proto
hadoop-hdfs/src/main/proto/DatanodeLifelineProtocol.proto
hadoop-hdfs/src/main/proto/DatanodeProtocol.proto
hadoop-hdfs/src/main/proto/HAZKInfo.proto
hadoop-hdfs/src/main/proto/HdfsServer.proto
hadoop-hdfs/src/main/proto/InterDatanodeProtocol.proto
hadoop-hdfs/src/main/proto/JournalProtocol.proto
hadoop-hdfs/src/main/proto/NamenodeProtocol.proto
hadoop-hdfs/src/main/proto/QJournalProtocol.proto
hadoop-hdfs/src/main/proto/editlog.proto
hadoop-hdfs/src/main/proto/fsimage.proto
```

```
hadoop-hdfs-client/src/main/proto/ClientDatanodeProtocol.proto
hadoop-hdfs-client/src/main/proto/ClientNamenodeProtocol.proto
hadoop-hdfs-client/src/main/proto/ReconfigurationProtocol.proto
hadoop-hdfs-client/src/main/proto/acl.proto
hadoop-hdfs-client/src/main/proto/datatransfer.proto
hadoop-hdfs-client/src/main/proto/encryption.proto
hadoop-hdfs-client/src/main/proto/hdfs.proto                    // 客户端/服务端和数据传输协议中使用
hadoop-hdfs-client/src/main/proto/inotify.proto
hadoop-hdfs-client/src/main/proto/xattr.proto
```

```
# rbf: Router-based Federation
hadoop-hdfs-rbf/src/main/proto/FederationProtocol.proto
hadoop-hdfs-rbf/src/main/proto/RouterProtocol.proto
```

### hadoop-mapreduce-project/hadoop-mapreduce-client

```
hadoop-mapreduce-client-common/src/main/proto/HSAdminRefreshProtocol.proto
hadoop-mapreduce-client-common/src/main/proto/MRClientProtocol.proto
hadoop-mapreduce-client-common/src/main/proto/mr_protos.proto
hadoop-mapreduce-client-common/src/main/proto/mr_service_protos.proto
hadoop-mapreduce-client-shuffle/src/main/proto/ShuffleHandlerRecovery.proto
```

### hadoop-yarn-project/hadoop-yarn

```
hadoop-yarn-api/src/main/proto/application_history_client.proto
hadoop-yarn-api/src/main/proto/applicationclient_protocol.proto
hadoop-yarn-api/src/main/proto/applicationmaster_protocol.proto
hadoop-yarn-api/src/main/proto/client_SCM_protocol.proto
hadoop-yarn-api/src/main/proto/containermanagement_protocol.proto
hadoop-yarn-api/src/main/proto/server/SCM_Admin_protocol.proto
hadoop-yarn-api/src/main/proto/server/application_history_server.proto
hadoop-yarn-api/src/main/proto/server/resourcemanager_administration_protocol.proto
hadoop-yarn-api/src/main/proto/server/yarn_server_resourcemanager_service_protos.proto
hadoop-yarn-api/src/main/proto/yarn_protos.proto
hadoop-yarn-api/src/main/proto/yarn_service_protos.proto
```

```
hadoop-yarn-common/src/main/proto/yarn_security_token.proto
```

```
hadoop-yarn-server/hadoop-yarn-server-applicationhistoryservice/src/main/proto/yarn_server_timelineserver_recovery.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/ResourceTracker.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/SCMUploader.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/collectornodemanager_protocol.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/distributed_scheduling_am_protocol.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/yarn_server_common_protos.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/yarn_server_common_service_protos.proto
hadoop-yarn-server/hadoop-yarn-server-common/src/main/proto/yarn_server_federation_protos.proto
hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/proto/LocalizationProtocol.proto
hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/proto/yarn_server_nodemanager_recovery.proto
hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/proto/yarn_server_nodemanager_service_protos.proto
hadoop-yarn-server/hadoop-yarn-server-resourcemanager/src/main/proto/yarn_server_resourcemanager_recovery.proto
```



## Packages

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
