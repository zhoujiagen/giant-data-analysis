# Apache Sqoop with MySQL


```
$ cat import.txt
import
--connect
jdbc:mysql://192.168.56.1/test
--username
root
--password
admin
--table
example
--incremental
append
--check-column
id
--last-value
0
--autoreset-to-one-mapper
--as-parquetfile
```

```
$ export HADOOP_COMMON_HOME=/home/zhoujiagen/hadoop-2.7.6
$ export HADOOP_MAPRED_HOME=/home/zhoujiagen/hadoop-2.7.6
$ bin/sqoop --options-file import.txt
```

Output:

```
19/11/23 02:13:40 INFO mapreduce.ImportJobBase: Transferred 39.75 KB in 5.8689 seconds (6.773 KB/sec)
19/11/23 02:13:40 INFO mapreduce.ImportJobBase: Retrieved 9 records.
19/11/23 02:13:40 INFO util.AppendUtils: Creating missing output directory - example
19/11/23 02:13:40 INFO tool.ImportTool: Incremental import complete! To run another incremental import of all data following this import, supply the following arguments:
19/11/23 02:13:40 INFO tool.ImportTool:  --incremental append
19/11/23 02:13:40 INFO tool.ImportTool:   --check-column id
19/11/23 02:13:40 INFO tool.ImportTool:   --last-value 9
19/11/23 02:13:40 INFO tool.ImportTool: (Consider saving this with 'sqoop job --create')
Nov 23, 2019 2:13:37 AM INFO: parquet.hadoop.InternalParquetRecordWriter: Flushing mem columnStore to file. allocated memory: 98
Nov 23, 2019 2:13:37 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [id] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:37 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 61B for [name] BINARY: 2 values, 24B raw, 26B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:37 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [create_at] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:37 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [update_at] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.InternalParquetRecordWriter: Flushing mem columnStore to file. allocated memory: 82
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [id] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 61B for [name] BINARY: 2 values, 24B raw, 26B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 51B for [create_at] INT64: 2 values, 8B raw, 10B comp, 1 pages, encodings: [RLE, BIT_PACKED, PLAIN_DICTIONARY], dic { 1 entries, 8B raw, 1B comp}
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 51B for [update_at] INT64: 2 values, 8B raw, 10B comp, 1 pages, encodings: [RLE, BIT_PACKED, PLAIN_DICTIONARY], dic { 1 entries, 8B raw, 1B comp}
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.InternalParquetRecordWriter: Flushing mem columnStore to file. allocated memory: 98
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [id] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 61B for [name] BINARY: 2 values, 24B raw, 26B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [create_at] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:38 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [update_at] INT64: 2 values, 22B raw, 24B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:39 AM INFO: parquet.hadoop.InternalParquetRecordWriter: Flushing mem columnStore to file. allocated memory: 115
Nov 23, 2019 2:13:39 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 70B for [id] INT64: 3 values, 30B raw, 29B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:39 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 65B for [name] BINARY: 3 values, 33B raw, 30B comp, 1 pages, encodings: [RLE, PLAIN, BIT_PACKED]
Nov 23, 2019 2:13:39 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 51B for [create_at] INT64: 3 values, 8B raw, 10B comp, 1 pages, encodings: [RLE, BIT_PACKED, PLAIN_DICTIONARY], dic { 1 entries, 8B raw, 1B comp}
Nov 23, 2019 2:13:39 AM INFO: parquet.hadoop.ColumnChunkPageWriteStore: written 51B for [update_at] INT64: 3 values, 8B raw, 10B comp, 1 pages, encodings: [RLE, BIT_PACKED, PLAIN_DICTIONARY], dic { 1 entries, 8B raw, 1B comp}
```
