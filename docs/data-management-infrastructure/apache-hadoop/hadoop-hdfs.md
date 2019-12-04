# HDFS: Hadoop Distributed Filesystem

## 资源

+ The Google File System by Sanjay Ghemawat and Howard Gobioff and Shun-Tak Leung, 2003.


## 图例

### 客户端从HDFS中读取数据

![Figure 3-2. A client reading data from HDFS](./images/read-data-from-hdfs.png)

1. 客户端调用`FileSystem.open()`打开文件, 在HDFS中是`DistributedFileSystem`;
2. `DistributedFileSystem`使用RPC访问namenode, 确定文件的头几个块的位置, 返回`FSDataInputStream`, 它封装了`DFSInputStream`, 后者负责管理与datanode和namenode的I/O;
3. 客户端调用`FSDataInputStream.read()`, `DFSInputStream`连接到文件第一个块最近的datanode.
4. 数据从datanode流向客户端;
5. 读完一块时, `DFSInputStream`关闭到datanode的连接, 找到下一个块的最近的datanode;
6. 客户端读取完毕, 调用`FSDataInputStream.close()`.

### 客户端写入数据到HDFS中

![Figure 3-4. A client writing data to HDFS](./images/read-data-from-hdfs.png)

1. 客户端调用`DistributedFileSystem.create()`创建文件;
2. `DistributedFileSystem`使用RPC访问namenode创建没有块的新文件; 返回`FSDataOutputStream`, 它封装了`DFSOutputStream`, 后者负责管理与datanode和namenode的I/O;
3. 客户端调用`FSDataOutputStream.write()`写入数据, `DFSOutputStream`将其拆分为包, 写入内部数据队列(data queue); 数据队列被`DataStreamer`(hadoop-hdfs-client)消费, 它负责请求namenode分配新块, 并选择合适的datanode放置数据副本, 这些datanode构成数据管道;
4. `DataStreamer`将包发送到管道中的第一个datanode, 这个datanode存储包并转发到下一个datanode, 下一个datanode存储包并转发, 依次类推;
5. `DFSOutputStream`维护了需要被datanode确认的包队列(ack queue), 在包被数据管道中所有datanode确认后才可移除; 这里有数据管道失败的处理;
6. 客户端写入完毕, 调用`FSDataOutputStream.close()`, 刷出到datanode管道的剩余的包, 在告知namenode文件已结束之前等待确认.

## 概念

- block
- namenode, datanode, secondary namenode
- HDFS Federation
- HDFS High Availability: QJM(quorum journal manager)
- 支持的文件系统见`org.apache.hadoop.fs.AbstractFileSystem`的具体实现
- 应用使用`org.apache.hadoop.fs.FileSystem`: `FSDataInputStream`, `FSDataOutputStream`, `FileStatus`

java.io.DataOutputStream.flush(): Flushes this data output stream.<br>
FSDataOutputStream.hflush(): Flush out the data in client's user buffer.<br>
FSDataOutputStream.hsync(): Similar to posix fsync, flush out the data in client's user buffer all the way to the disk device (but the disk may have it in its cache).

- distcp: 并行拷贝工具
- 数据完整性: 校验和 CRC-32C
- 压缩格式: DEFLATE, gzip, bzip2, LZO, LZ4, Snappy
