# MapReduce

## 资源

+ MapReduce: simplified data processing on large clusters, by Jeffrey Dean and Sanjay Ghemawat, 2008.

## 图例

### Hadoop如何运行MapReduce作业

![Figure 7-1. How Hadoop runs a Mapreduce job](./images/how-hadoop-run-a-mapreduce-job.png)


### MapReduce系统中的状态是如何传播的

![Figure 7-3. How status updates are propagated through the MapReduce system](./images/how-staus-update-propageted-in-mapreduce-system.png)

### MapReduce中shuffle和sort

![Figure 7-4. Shuffle and sort in MapReduce](./images/shuffle-and-sort-in-mapreduce.png)



## Packages

### org.apache.hadoop.mapred

### org.apache.hadoop.mapreduce

- `Job`, `JobID`, `JobStatus`, `JobContext`
- `InputSplit`, `InputFormat<K,V>`, `OutputFormat<K,V>`
- `TaskID`, `TaskAttemptID`, `TaskCompletionEvent`, `TaskAttemptContext`, `TaskInputOutputContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`, `TaskTrackerInfo`
- `Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`, `MapContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`, `RecordReader<KEYIN,VALUEIN>`
- `Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`, `ReduceContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT>`, `RecordWriter<K,V>`
