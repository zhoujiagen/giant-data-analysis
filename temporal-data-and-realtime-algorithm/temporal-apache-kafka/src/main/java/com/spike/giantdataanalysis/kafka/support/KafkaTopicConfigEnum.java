package com.spike.giantdataanalysis.kafka.support;

//@formatter:off
/**
 * http://kafka.apache.org/documentation.html#topic-config
 */
public enum KafkaTopicConfigEnum {
  CLEANUP_POLICY("cleanup.policy","delete","log.cleanup.policy","A string that is either \"delete\" or \"compact\". This string designates the retention policy to use on old log segments. The default policy (\"delete\") will discard old segments when their retention time or size limit has been reached. The \"compact\" setting will enable log compaction on the topic."),
  DELETE_RETENTION_MS("delete.retention.ms","86400000 (24 hours)","log.cleaner.delete.retention.ms","The amount of time to retain delete tombstone markers for log compacted topics. This setting also gives a bound on the time in which a consumer must complete a read if they begin from offset 0 to ensure that they get a valid snapshot of the final stage (otherwise delete tombstones may be collected before they complete their scan)."),
  FLUSH_MESSAGES("flush.messages","None","log.flush.interval.messages","This setting allows specifying an interval at which we will force an fsync of data written to the log. For example if this was set to 1 we would fsync after every message; if it were 5 we would fsync after every five messages. In general we recommend you not set this and use replication for durability and allow the operating system's background flush capabilities as it is more efficient. This setting can be overridden on a per-topic basis (see the per-topic configuration section)."),
  FLUSH_MS("flush.ms","None","log.flush.interval.ms","This setting allows specifying a time interval at which we will force an fsync of data written to the log. For example if this was set to 1000 we would fsync after 1000 ms had passed. In general we recommend you not set this and use replication for durability and allow the operating system's background flush capabilities as it is more efficient."),
  INDEX_INTERVAL_BYTES("index.interval.bytes","4096","log.index.interval.bytes","This setting controls how frequently Kafka adds an index entry to it's offset index. The default setting ensures that we index a message roughly every 4096 bytes. More indexing allows reads to jump closer to the exact position in the log but makes the index larger. You probably don't need to change this."),
  MAX_MESSAGE_BYTES("max.message.bytes","1,000,000","message.max.bytes","This is largest message size Kafka will allow to be appended to this topic. Note that if you increase this size you must also increase your consumer's fetch size so they can fetch messages this large."),
  MIN_CLEANABLE_DIRTY_RATIO("min.cleanable.dirty.ratio","0.5","log.cleaner.min.cleanable.ratio","This configuration controls how frequently the log compactor will attempt to clean the log (assuming log compaction is enabled). By default we will avoid cleaning a log where more than 50% of the log has been compacted. This ratio bounds the maximum space wasted in the log by duplicates (at 50% at most 50% of the log could be duplicates). A higher ratio will mean fewer, more efficient cleanings but will mean more wasted space in the log."),
  MIN_INSYNC_REPLICAS("min.insync.replicas","1","min.insync.replicas","When a producer sets request.required.acks to -1, min.insync.replicas specifies the minimum number of replicas that must acknowledge a write for the write to be considered successful. If this minimum cannot be met, then the producer will raise an exception (either NotEnoughReplicas or NotEnoughReplicasAfterAppend). When used together, min.insync.replicas and request.required.acks allow you to enforce greater durability guarantees. A typical scenario would be to create a topic with a replication factor of 3, set min.insync.replicas to 2, and produce with request.required.acks of -1. This will ensure that the producer raises an exception if a majority of replicas do not receive a write."),
  RETENTION_BYTES("retention.bytes","None","log.retention.bytes","This configuration controls the maximum size a log can grow to before we will discard old log segments to free up space if we are using the \"delete\" retention policy. By default there is no size limit only a time limit."),
  RETENTION_MS("retention.ms","7 days","log.retention.minutes","This configuration controls the maximum time we will retain a log before we will discard old log segments to free up space if we are using the \"delete\" retention policy. This represents an SLA on how soon consumers must read their data."),
  SEGMENT_BYTES("segment.bytes","1 GB","log.segment.bytes","This configuration controls the segment file size for the log. Retention and cleaning is always done a file at a time so a larger segment size means fewer files but less granular control over retention."),
  SEGMENT_INDEX_BYTES("segment.index.bytes","10 MB","log.index.size.max.bytes","This configuration controls the size of the index that maps offsets to file positions. We preallocate this index file and shrink it only after log rolls. You generally should not need to change this setting."),
  SEGMENT_MS("segment.ms","7 days","log.roll.hours","This configuration controls the period of time after which Kafka will force the log to roll even if the segment file isn't full to ensure that retention can delete or compact old data."),
  SEGMENT_JITTER_MS("segment.jitter.ms","0","log.roll.jitter.{ms,hours}","The maximum jitter to subtract from logRollTimeMillis.");

  private String key;
  private String value;
  private String serverDefaultProperty;
  private String description;

  private KafkaTopicConfigEnum(String key, String value, String serverDefaultProperty,
      String description) {
    this.key = key;
    this.value = value;
    this.serverDefaultProperty = serverDefaultProperty;
    this.description = description;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public String getServerDefaultProperty() {
    return serverDefaultProperty;
  }

  public String getDescription() {
    return description;
  }

	}