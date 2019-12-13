# Hudi Concepts


## Timeline

- timeline: `org.apache.hudi.common.table.HoodieTimeline`

```
HoodieTimeline
|-- HoodieDefaultTimeline
|--|-- HoodieActiveTimeline
|--|--|-- RollbackTimeline
|--|--|-- MockHoodieTimeline - test
|--|-- HoodieArchivedTimeline
```

- instant: `org.apache.hudi.common.table.timeline.HoodieInstant`

```
public class HoodieInstant implements Serializable {

  /**
   * Instant State
   */
  public enum State {
    // Requested State (valid state for Compaction)
    REQUESTED,
    // Inflight instant
    INFLIGHT,
    // Committed instant
    COMPLETED,
    // Invalid instant
    INVALID
  }

  private State state = State.COMPLETED;
  private String action;
  private String timestamp;
```

```
// HoodieInstant.action
public interface HoodieTimeline extends Serializable {

  String COMMIT_ACTION = "commit";
  String DELTA_COMMIT_ACTION = "deltacommit";
  String CLEAN_ACTION = "clean";
  String ROLLBACK_ACTION = "rollback";
  String SAVEPOINT_ACTION = "savepoint";
  String INFLIGHT_EXTENSION = ".inflight";
  // With Async Compaction, compaction instant can be in 3 states :
  // (compaction-requested), (compaction-inflight), (completed)
  String COMPACTION_ACTION = "compaction";
  String REQUESTED_EXTENSION = ".requested";
  String RESTORE_ACTION = "restore";
```

## Storage Type & Views

- table: `org.apache.hudi.table.HoodieTable`
- index: `org.apache.hudi.index.HoodieIndex`

```
HoodieTable
|-- HoodieCopyOnWriteTable
|-- HoodieMergeOnReadTable
```

```
HoodieIndex
|-- InMemoryHashIndex - local test
|-- HoodieBloomIndex
|-- HoodieGlobalBloomIndex
|-- HBaseIndex
```

- vies: read optimized, incremental, realtime

```
org.apache.hudi.common.table.TableFileSystemView

-- ReadOptimizedView
-- RealtimeView

|-- SyncableFileSystemView
|--|-- AbstractTableFileSystemView
|--|--|-- IncrementalTimelineSyncFileSystemView
|--|--|--|-- HoodieTableFileSystemView
|--|--|--|--|-- SpillableMapBasedFileSystemView
|--|--|--|-- RocksDbBasedFileSystemView
|--|-- PriorityBasedFileSystemView
|--|-- RemoteHoodieTableFileSystemView

```

### COW: Copy On Write

### MOR: Merge On Read

#### log

- 文件格式: `org.apache.hudi.common.table.log.HoodieLogFormat`

```
HoodieLogFormat
-- interface Writer extends Closeable
-- interface Reader extends Closeable, Iterator<HoodieLogBlock>
```

#### compaction

- 入口: `org.apache.hudi.HoodieWriteClient#scheduleCompaction`
- `org.apache.hudi.avro.model.HoodieCompactionPlan`


```
HoodieCompactionPlan
-- HoodieCompactionOperation
```

## File Management

- basepath, partitionpath

```
org.apache.spark.Partitioner
|-- org.apache.hudi.table.HoodieCopyOnWriteTable.UpsertPartitioner
|-- org.apache.hudi.table.HoodieMergeOnReadTable.MergeOnReadUpsertPartitioner
|-- org.apache.hudi.index.bloom.BucketizedBloomCheckPartitioner
```

- file: `org.apache.hudi.common.model.HoodieDataFile`, `org.apache.hudi.common.model.HoodieLogFile`

```
// 工具类
org.apache.hudi.common.util.FSUtils
```

- file group: `org.apache.hudi.common.model.HoodieFileGroup`

```
// 构造
org.apache.hudi.common.table.view.AbstractTableFileSystemView#buildFileGroups(java.util.stream.Stream<org.apache.hudi.common.model.HoodieDataFile>, java.util.stream.Stream<org.apache.hudi.common.model.HoodieLogFile>, org.apache.hudi.common.table.HoodieTimeline, boolean)
```

- file slice: `org.apache.hudi.common.model.FileSlice`
