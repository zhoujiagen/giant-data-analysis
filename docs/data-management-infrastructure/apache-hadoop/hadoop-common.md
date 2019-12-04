# Hadoop Common

## Packages

### org.apache.hadoop.conf

> Configuration of system parameters.

### org.apache.hadoop.util



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
