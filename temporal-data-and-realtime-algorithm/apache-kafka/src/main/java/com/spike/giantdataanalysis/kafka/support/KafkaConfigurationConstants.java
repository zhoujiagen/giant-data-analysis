package com.spike.giantdataanalysis.kafka.support;

//@formatter:off
/**
 * <pre>
 * Kafka配置常量
 * 
 * Broker: http://kafka.apache.org/documentation.html#brokerconfigs
 * Consumer: http://kafka.apache.org/documentation.html#newconsumerconfigs
 * Connect: http://kafka.apache.org/documentation.html#connectconfigs
 * Producer: http://kafka.apache.org/documentation.html#producerconfigs
 * Topic: http://kafka.apache.org/documentation.html#topic-config
 * </pre>
 * @author zhoujiagen
 */
public interface KafkaConfigurationConstants {

	/**
	 * http://kafka.apache.org/documentation.html#brokerconfigs
	 */
	enum Broker {
		ZOOKEEPER_CONNECT("zookeeper.connect","Zookeeper host string","string","","","high"),
		ADVERTISED_HOST_NAME("advertised.host.name","Hostname to publish to ZooKeeper for clients to use. In IaaS environments, this may need to be different from the interface to which the broker binds. If this is not set, it will use the value for \"host.name\" if configured. Otherwise it will use the value returned from java.net.InetAddress.getCanonicalHostName().","string","null","","high"),
		ADVERTISED_LISTENERS("advertised.listeners","Listeners to publish to ZooKeeper for clients to use, if different than the listeners above. In IaaS environments, this may need to be different from the interface to which the broker binds. If this is not set, the value for \"listeners\" will be used.","string","null","","high"),
		ADVERTISED_PORT("advertised.port","The port to publish to ZooKeeper for clients to use. In IaaS environments, this may need to be different from the port to which the broker binds. If this is not set, it will publish the same port that the broker binds to.","int","null","","high"),
		AUTO_CREATE_TOPICS_ENABLE("auto.create.topics.enable","Enable auto creation of topic on the server","boolean","true","","high"),
		AUTO_LEADER_REBALANCE_ENABLE("auto.leader.rebalance.enable","Enables auto leader balancing. A background thread checks and triggers leader balance if required at regular intervals","boolean","true","","high"),
		BACKGROUND_THREADS("background.threads","The number of threads to use for various background processing tasks","int","10","[1,...]","high"),
		BROKER_ID("broker.id","The broker id for this server. To avoid conflicts between zookeeper generated brokerId and user's config.brokerId added MaxReservedBrokerId and zookeeper sequence starts from MaxReservedBrokerId + 1.","int","-1","","high"),
		COMPRESSION_TYPE("compression.type","Specify the final compression type for a given topic. This configuration accepts the standard compression codecs ('gzip', 'snappy', lz4). It additionally accepts 'uncompressed' which is equivalent to no compression; and 'producer' which means retain the original compression codec set by the producer.","string","producer","","high"),
		DELETE_TOPIC_ENABLE("delete.topic.enable","Enables delete topic. Delete topic through the admin tool will have no effect if this config is turned off","boolean","false","","high"),
		HOST_NAME("host.name","hostname of broker. If this is set, it will only bind to this address. If this is not set, it will bind to all interfaces","string","","","high"),
		LEADER_IMBALANCE_CHECK_INTERVAL_SECONDS("leader.imbalance.check.interval.seconds","The frequency with which the partition rebalance check is triggered by the controller","long","300","","high"),
		LEADER_IMBALANCE_PER_BROKER_PERCENTAGE("leader.imbalance.per.broker.percentage","The ratio of leader imbalance allowed per broker. The controller would trigger a leader balance if it goes above this value per broker. The value is specified in percentage.","int","10","","high"),
		LISTENERS("listeners","Listener List - Comma-separated list of URIs we will listen on and their protocols. Specify hostname as 0.0.0.0 to bind to all interfaces. Leave hostname empty to bind to default interface. Examples of legal listener lists: PLAINTEXT://myhost:9092,TRACE://:9091 PLAINTEXT://0.0.0.0:9092, TRACE://localhost:9093","string","null","","high"),
		LOG_DIR("log.dir","The directory in which the log data is kept (supplemental for log.dirs property)","string","/tmp/kafka-logs","","high"),
		LOG_DIRS("log.dirs","The directories in which the log data is kept. If not set, the value in log.dir is used","string","null","","high"),
		LOG_FLUSH_INTERVAL_MESSAGES("log.flush.interval.messages","The number of messages accumulated on a log partition before messages are flushed to disk","long","9223372036854775807","[1,...]","high"),
		LOG_FLUSH_INTERVAL_MS("log.flush.interval.ms","The maximum time in ms that a message in any topic is kept in memory before flushed to disk. If not set, the value in log.flush.scheduler.interval.ms is used","long","null","","high"),
		LOG_FLUSH_OFFSET_CHECKPOINT_INTERVAL_MS("log.flush.offset.checkpoint.interval.ms","The frequency with which we update the persistent record of the last flush which acts as the log recovery point","int","60000","[0,...]","high"),
		LOG_FLUSH_SCHEDULER_INTERVAL_MS("log.flush.scheduler.interval.ms","The frequency in ms that the log flusher checks whether any log needs to be flushed to disk","long","9223372036854775807","","high"),
		LOG_RETENTION_BYTES("log.retention.bytes","The maximum size of the log before deleting it","long","-1","","high"),
		LOG_RETENTION_HOURS("log.retention.hours","The number of hours to keep a log file before deleting it (in hours), tertiary to log.retention.ms property","int","168","","high"),
		LOG_RETENTION_MINUTES("log.retention.minutes","The number of minutes to keep a log file before deleting it (in minutes), secondary to log.retention.ms property. If not set, the value in log.retention.hours is used","int","null","","high"),
		LOG_RETENTION_MS("log.retention.ms","The number of milliseconds to keep a log file before deleting it (in milliseconds), If not set, the value in log.retention.minutes is used","long","null","","high"),
		LOG_ROLL_HOURS("log.roll.hours","The maximum time before a new log segment is rolled out (in hours), secondary to log.roll.ms property","int","168","[1,...]","high"),
		LOG_ROLL_JITTER_HOURS("log.roll.jitter.hours","The maximum jitter to subtract from logRollTimeMillis (in hours), secondary to log.roll.jitter.ms property","int","0","[0,...]","high"),
		LOG_ROLL_JITTER_MS("log.roll.jitter.ms","The maximum jitter to subtract from logRollTimeMillis (in milliseconds). If not set, the value in log.roll.jitter.hours is used","long","null","","high"),
		LOG_ROLL_MS("log.roll.ms","The maximum time before a new log segment is rolled out (in milliseconds). If not set, the value in log.roll.hours is used","long","null","","high"),
		LOG_SEGMENT_BYTES("log.segment.bytes","The maximum size of a single log file","int","1073741824","[14,...]","high"),
		LOG_SEGMENT_DELETE_DELAY_MS("log.segment.delete.delay.ms","The amount of time to wait before deleting a file from the filesystem","long","60000","[0,...]","high"),
		MESSAGE_MAX_BYTES("message.max.bytes","The maximum size of message that the server can receive","int","1000012","[0,...]","high"),
		MIN_INSYNC_REPLICAS("min.insync.replicas","define the minimum number of replicas in ISR needed to satisfy a produce request with required.acks=-1 (or all)","int","1","[1,...]","high"),
		NUM_IO_THREADS("num.io.threads","The number of io threads that the server uses for carrying out network requests","int","8","[1,...]","high"),
		NUM_NETWORK_THREADS("num.network.threads","the number of network threads that the server uses for handling network requests","int","3","[1,...]","high"),
		NUM_RECOVERY_THREADS_PER_DATA_DIR("num.recovery.threads.per.data.dir","The number of threads per data directory to be used for log recovery at startup and flushing at shutdown","int","1","[1,...]","high"),
		NUM_REPLICA_FETCHERS("num.replica.fetchers","Number of fetcher threads used to replicate messages from a source broker. Increasing this value can increase the degree of I/O parallelism in the follower broker.","int","1","","high"),
		OFFSET_METADATA_MAX_BYTES("offset.metadata.max.bytes","The maximum size for a metadata entry associated with an offset commit","int","4096","","high"),
		OFFSETS_COMMIT_REQUIRED_ACKS("offsets.commit.required.acks","The required acks before the commit can be accepted. In general, the default (-1) should not be overridden","short","-1","","high"),
		OFFSETS_COMMIT_TIMEOUT_MS("offsets.commit.timeout.ms","Offset commit will be delayed until all replicas for the offsets topic receive the commit or this timeout is reached. This is similar to the producer request timeout.","int","5000","[1,...]","high"),
		OFFSETS_LOAD_BUFFER_SIZE("offsets.load.buffer.size","Batch size for reading from the offsets segments when loading offsets into the cache.","int","5242880","[1,...]","high"),
		OFFSETS_RETENTION_CHECK_INTERVAL_MS("offsets.retention.check.interval.ms","Frequency at which to check for stale offsets","long","600000","[1,...]","high"),
		OFFSETS_RETENTION_MINUTES("offsets.retention.minutes","Log retention window in minutes for offsets topic","int","1440","[1,...]","high"),
		OFFSETS_TOPIC_COMPRESSION_CODEC("offsets.topic.compression.codec","Compression codec for the offsets topic - compression may be used to achieve \"atomic\" commits","int","0","","high"),
		OFFSETS_TOPIC_NUM_PARTITIONS("offsets.topic.num.partitions","The number of partitions for the offset commit topic (should not change after deployment)","int","50","[1,...]","high"),
		OFFSETS_TOPIC_REPLICATION_FACTOR("offsets.topic.replication.factor","The replication factor for the offsets topic (set higher to ensure availability). To ensure that the effective replication factor of the offsets topic is the configured value, the number of alive brokers has to be at least the replication factor at the time of the first request for the offsets topic. If not, either the offsets topic creation will fail or it will get a replication factor of min(alive brokers, configured replication factor)","short","3","[1,...]","high"),
		OFFSETS_TOPIC_SEGMENT_BYTES("offsets.topic.segment.bytes","The offsets topic segment bytes should be kept relatively small in order to facilitate faster log compaction and cache loads","int","104857600","[1,...]","high"),
		PORT("port","the port to listen and accept connections on","int","9092","","high"),
		QUEUED_MAX_REQUESTS("queued.max.requests","The number of queued requests allowed before blocking the network threads","int","500","[1,...]","high"),
		QUOTA_CONSUMER_DEFAULT("quota.consumer.default","Any consumer distinguished by clientId/consumer group will get throttled if it fetches more bytes than this value per-second","long","9223372036854775807","[1,...]","high"),
		QUOTA_PRODUCER_DEFAULT("quota.producer.default","Any producer distinguished by clientId will get throttled if it produces more bytes than this value per-second","long","9223372036854775807","[1,...]","high"),
		REPLICA_FETCH_MAX_BYTES("replica.fetch.max.bytes","The number of byes of messages to attempt to fetch","int","1048576","","high"),
		REPLICA_FETCH_MIN_BYTES("replica.fetch.min.bytes","Minimum bytes expected for each fetch response. If not enough bytes, wait up to replicaMaxWaitTimeMs","int","1","","high"),
		REPLICA_FETCH_WAIT_MAX_MS("replica.fetch.wait.max.ms","max wait time for each fetcher request issued by follower replicas. This value should always be less than the replica.lag.time.max.ms at all times to prevent frequent shrinking of ISR for low throughput topics","int","500","","high"),
		REPLICA_HIGH_WATERMARK_CHECKPOINT_INTERVAL_MS("replica.high.watermark.checkpoint.interval.ms","The frequency with which the high watermark is saved out to disk","long","5000","","high"),
		REPLICA_LAG_TIME_MAX_MS("replica.lag.time.max.ms","If a follower hasn't sent any fetch requests or hasn't consumed up to the leaders log end offset for at least this time, the leader will remove the follower from isr","long","10000","","high"),
		REPLICA_SOCKET_RECEIVE_BUFFER_BYTES("replica.socket.receive.buffer.bytes","The socket receive buffer for network requests","int","65536","","high"),
		REPLICA_SOCKET_TIMEOUT_MS("replica.socket.timeout.ms","The socket timeout for network requests. Its value should be at least replica.fetch.wait.max.ms","int","30000","","high"),
		REQUEST_TIMEOUT_MS("request.timeout.ms","The configuration controls the maximum amount of time the client will wait for the response of a request. If the response is not received before the timeout elapses the client will resend the request if necessary or fail the request if retries are exhausted.","int","30000","","high"),
		SOCKET_RECEIVE_BUFFER_BYTES("socket.receive.buffer.bytes","The SO_RCVBUF buffer of the socket sever sockets","int","102400","","high"),
		SOCKET_REQUEST_MAX_BYTES("socket.request.max.bytes","The maximum number of bytes in a socket request","int","104857600","[1,...]","high"),
		SOCKET_SEND_BUFFER_BYTES("socket.send.buffer.bytes","The SO_SNDBUF buffer of the socket sever sockets","int","102400","","high"),
		UNCLEAN_LEADER_ELECTION_ENABLE("unclean.leader.election.enable","Indicates whether to enable replicas not in the ISR set to be elected as leader as a last resort, even though doing so may result in data loss","boolean","true","","high"),
		ZOOKEEPER_CONNECTION_TIMEOUT_MS("zookeeper.connection.timeout.ms","The max time that the client waits to establish a connection to zookeeper. If not set, the value in zookeeper.session.timeout.ms is used","int","null","","high"),
		ZOOKEEPER_SESSION_TIMEOUT_MS("zookeeper.session.timeout.ms","Zookeeper session timeout","int","6000","","high"),
		ZOOKEEPER_SET_ACL("zookeeper.set.acl","Set client to use secure ACLs","boolean","false","","high"),
		BROKER_ID_GENERATION_ENABLE("broker.id.generation.enable","Enable automatic broker id generation on the server? When enabled the value configured for reserved.broker.max.id should be reviewed.","boolean","true","","medium"),
		CONNECTIONS_MAX_IDLE_MS("connections.max.idle.ms","Idle connections timeout: the server socket processor threads close the connections that idle more than this","long","600000","","medium"),
		CONTROLLED_SHUTDOWN_ENABLE("controlled.shutdown.enable","Enable controlled shutdown of the server","boolean","true","","medium"),
		CONTROLLED_SHUTDOWN_MAX_RETRIES("controlled.shutdown.max.retries","Controlled shutdown can fail for multiple reasons. This determines the number of retries when such failure happens","int","3","","medium"),
		CONTROLLED_SHUTDOWN_RETRY_BACKOFF_MS("controlled.shutdown.retry.backoff.ms","Before each retry, the system needs time to recover from the state that caused the previous failure (Controller fail over, replica lag etc). This config determines the amount of time to wait before retrying.","long","5000","","medium"),
		CONTROLLER_SOCKET_TIMEOUT_MS("controller.socket.timeout.ms","The socket timeout for controller-to-broker channels","int","30000","","medium"),
		DEFAULT_REPLICATION_FACTOR("default.replication.factor","default replication factors for automatically created topics","int","1","","medium"),
		FETCH_PURGATORY_PURGE_INTERVAL_REQUESTS("fetch.purgatory.purge.interval.requests","The purge interval (in number of requests) of the fetch request purgatory","int","1000","","medium"),
		GROUP_MAX_SESSION_TIMEOUT_MS("group.max.session.timeout.ms","The maximum allowed session timeout for registered consumers","int","30000","","medium"),
		GROUP_MIN_SESSION_TIMEOUT_MS("group.min.session.timeout.ms","The minimum allowed session timeout for registered consumers","int","6000","","medium"),
		INTER_BROKER_PROTOCOL_VERSION("inter.broker.protocol.version","Specify which version of the inter-broker protocol will be used. This is typically bumped after all brokers were upgraded to a new version. Example of some valid values are: 0.8.0, 0.8.1, 0.8.1.1, 0.8.2, 0.8.2.0, 0.8.2.1, 0.9.0.0, 0.9.0.1 Check ApiVersion for the full list.","string","0.9.0.X","","medium"),
		LOG_CLEANER_BACKOFF_MS("log.cleaner.backoff.ms","The amount of time to sleep when there are no logs to clean","long","15000","[0,...]","medium"),
		LOG_CLEANER_DEDUPE_BUFFER_SIZE("log.cleaner.dedupe.buffer.size","The total memory used for log deduplication across all cleaner threads","long","134217728","","medium"),
		LOG_CLEANER_DELETE_RETENTION_MS("log.cleaner.delete.retention.ms","How long are delete records retained?","long","86400000","","medium"),
		LOG_CLEANER_ENABLE("log.cleaner.enable","Enable the log cleaner process to run on the server? Should be enabled if using any topics with a cleanup.policy=compact including the internal offsets topic. If disabled those topics will not be compacted and continually grow in size.","boolean","true","","medium"),
		LOG_CLEANER_IO_BUFFER_LOAD_FACTOR("log.cleaner.io.buffer.load.factor","Log cleaner dedupe buffer load factor. The percentage full the dedupe buffer can become. A higher value will allow more log to be cleaned at once but will lead to more hash collisions","double","0.9","","medium"),
		LOG_CLEANER_IO_BUFFER_SIZE("log.cleaner.io.buffer.size","The total memory used for log cleaner I/O buffers across all cleaner threads","int","524288","[0,...]","medium"),
		LOG_CLEANER_IO_MAX_BYTES_PER_SECOND("log.cleaner.io.max.bytes.per.second","The log cleaner will be throttled so that the sum of its read and write i/o will be less than this value on average","double","1.7976931348623157E308","","medium"),
		LOG_CLEANER_MIN_CLEANABLE_RATIO("log.cleaner.min.cleanable.ratio","The minimum ratio of dirty log to total log for a log to eligible for cleaning","double","0.5","","medium"),
		LOG_CLEANER_THREADS("log.cleaner.threads","The number of background threads to use for log cleaning","int","1","[0,...]","medium"),
		LOG_CLEANUP_POLICY("log.cleanup.policy","The default cleanup policy for segments beyond the retention window, must be either \"delete\" or \"compact\"","string","delete","[compact, delete]","medium"),
		LOG_INDEX_INTERVAL_BYTES("log.index.interval.bytes","The interval with which we add an entry to the offset index","int","4096","[0,...]","medium"),
		LOG_INDEX_SIZE_MAX_BYTES("log.index.size.max.bytes","The maximum size in bytes of the offset index","int","10485760","[4,...]","medium"),
		LOG_PREALLOCATE("log.preallocate","Should pre allocate file when create new segment? If you are using Kafka on Windows, you probably need to set it to true.","boolean","false","","medium"),
		LOG_RETENTION_CHECK_INTERVAL_MS("log.retention.check.interval.ms","The frequency in milliseconds that the log cleaner checks whether any log is eligible for deletion","long","300000","[1,...]","medium"),
		MAX_CONNECTIONS_PER_IP("max.connections.per.ip","The maximum number of connections we allow from each ip address","int","2147483647","[1,...]","medium"),
		MAX_CONNECTIONS_PER_IP_OVERRIDES("max.connections.per.ip.overrides","Per-ip or hostname overrides to the default maximum number of connections","string","","","medium"),
		NUM_PARTITIONS("num.partitions","The default number of log partitions per topic","int","1","[1,...]","medium"),
		PRINCIPAL_BUILDER_CLASS("principal.builder.class","The fully qualified name of a class that implements the PrincipalBuilder interface, which is currently used to build the Principal for connections with the SSL SecurityProtocol.","class","class org.apache.kafka.common.security.auth.DefaultPrincipalBuilder","","medium"),
		PRODUCER_PURGATORY_PURGE_INTERVAL_REQUESTS("producer.purgatory.purge.interval.requests","The purge interval (in number of requests) of the producer request purgatory","int","1000","","medium"),
		REPLICA_FETCH_BACKOFF_MS("replica.fetch.backoff.ms","The amount of time to sleep when fetch partition error occurs.","int","1000","[0,...]","medium"),
		RESERVED_BROKER_MAX_ID("reserved.broker.max.id","Max number that can be used for a broker.id","int","1000","[0,...]","medium"),
		SASL_KERBEROS_KINIT_CMD("sasl.kerberos.kinit.cmd","Kerberos kinit command path.","string","/usr/bin/kinit","","medium"),
		SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN("sasl.kerberos.min.time.before.relogin","Login thread sleep time between refresh attempts.","long","60000","","medium"),
		SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES("sasl.kerberos.principal.to.local.rules","A list of rules for mapping from principal names to short names (typically operating system usernames). The rules are evaluated in order and the first rule that matches a principal name is used to map it to a short name. Any later rules in the list are ignored. By default, principal names of the form {username}/{hostname}@{REALM} are mapped to {username}. For more details on the format please see security authorization and acls.","list","[DEFAULT]","","medium"),
		SASL_KERBEROS_SERVICE_NAME("sasl.kerberos.service.name","The Kerberos principal name that Kafka runs as. This can be defined either in Kafka's JAAS config or in Kafka's config.","string","null","","medium"),
		SASL_KERBEROS_TICKET_RENEW_JITTER("sasl.kerberos.ticket.renew.jitter","Percentage of random jitter added to the renewal time.","double","0.05","","medium"),
		SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR("sasl.kerberos.ticket.renew.window.factor","Login thread will sleep until the specified window factor of time from last refresh to ticket's expiry has been reached, at which time it will try to renew the ticket.","double","0.8","","medium"),
		SECURITY_INTER_BROKER_PROTOCOL("security.inter.broker.protocol","Security protocol used to communicate between brokers. Valid values are: PLAINTEXT, SSL, SASL_PLAINTEXT, SASL_SSL.","string","PLAINTEXT","","medium"),
		SSL_CIPHER_SUITES("ssl.cipher.suites","A list of cipher suites. This is a named combination of authentication, encryption, MAC and key exchange algorithm used to negotiate the security settings for a network connection using TLS or SSL network protocol.By default all the available cipher suites are supported.","list","null","","medium"),
		SSL_CLIENT_AUTH("ssl.client.auth","Configures kafka broker to request client authentication. The following settings are common: "
				+ "1 ssl.client.auth=required If set to required client authentication is required. "
				+ "2 ssl.client.auth=requested This means client authentication is optional. unlike requested , if this option is set client can choose not to provide authentication information about itself."
				+ "3 ssl.client.auth=none This means client authentication is not needed.","string","none","[required, requested, none]","medium"),
		SSL_ENABLED_PROTOCOLS("ssl.enabled.protocols","The list of protocols enabled for SSL connections.","list","[TLSv1.2, TLSv1.1, TLSv1]","","medium"),
		SSL_KEY_PASSWORD("ssl.key.password","The password of the private key in the key store file. This is optional for client.","password","null","","medium"),
		SSL_KEYMANAGER_ALGORITHM("ssl.keymanager.algorithm","The algorithm used by key manager factory for SSL connections. Default value is the key manager factory algorithm configured for the Java Virtual Machine.","string","SunX509","","medium"),
		SSL_KEYSTORE_LOCATION("ssl.keystore.location","The location of the key store file. This is optional for client and can be used for two-way authentication for client.","string","null","","medium"),
		SSL_KEYSTORE_PASSWORD("ssl.keystore.password","The store password for the key store file.This is optional for client and only needed if ssl.keystore.location is configured.","password","null","","medium"),
		SSL_KEYSTORE_TYPE("ssl.keystore.type","The file format of the key store file. This is optional for client.","string","JKS","","medium"),
		SSL_PROTOCOL("ssl.protocol","The SSL protocol used to generate the SSLContext. Default setting is TLS, which is fine for most cases. Allowed values in recent JVMs are TLS, TLSv1.1 and TLSv1.2. SSL, SSLv2 and SSLv3 may be supported in older JVMs, but their usage is discouraged due to known security vulnerabilities.","string","TLS","","medium"),
		SSL_PROVIDER("ssl.provider","The name of the security provider used for SSL connections. Default value is the default security provider of the JVM.","string","null","","medium"),
		SSL_TRUSTMANAGER_ALGORITHM("ssl.trustmanager.algorithm","The algorithm used by trust manager factory for SSL connections. Default value is the trust manager factory algorithm configured for the Java Virtual Machine.","string","PKIX","","medium"),
		SSL_TRUSTSTORE_LOCATION("ssl.truststore.location","The location of the trust store file.","string","null","","medium"),
		SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password","The password for the trust store file.","password","null","","medium"),
		SSL_TRUSTSTORE_TYPE("ssl.truststore.type","The file format of the trust store file.","string","JKS","","medium"),
		AUTHORIZER_CLASS_NAME("authorizer.class.name","The authorizer class that should be used for authorization","string","","","low"),
		METRIC_REPORTERS("metric.reporters","A list of classes to use as metrics reporters. Implementing the MetricReporter interface allows plugging in classes that will be notified of new metric creation. The JmxReporter is always included to register JMX statistics.","list","[]","","low"),
		METRICS_NUM_SAMPLES("metrics.num.samples","The number of samples maintained to compute metrics.","int","2","[1,...]","low"),
		METRICS_SAMPLE_WINDOW_MS("metrics.sample.window.ms","The number of samples maintained to compute metrics.","long","30000","[1,...]","low"),
		QUOTA_WINDOW_NUM("quota.window.num","The number of samples to retain in memory","int","11","[1,...]","low"),
		QUOTA_WINDOW_SIZE_SECONDS("quota.window.size.seconds","The time span of each sample","int","1","[1,...]","low"),
		SSL_ENDPOINT_IDENTIFICATION_ALGORITHM("ssl.endpoint.identification.algorithm","The endpoint identification algorithm to validate server hostname using server certificate.","string","null","","low"),
		ZOOKEEPER_SYNC_TIME_MS("zookeeper.sync.time.ms","How far a ZK follower can be behind a ZK leader","int","2000","","low");

		private String key;
		private String description;
		private String type;
		private String value;
		private String validValues;
		private String importance;

		private Broker(String key, String description, String type, String value, String validValues, String importance) {
			this.key = key;
			this.description = description;
			this.type = type;
			this.value = value;
			this.validValues = validValues;
			this.importance = importance;
		}

		public String getKey() {
			return key;
		}

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public String getValidValues() {
			return validValues;
		}

		public String getImportance() {
			return importance;
		}
		
	}

	/**
	 * http://kafka.apache.org/documentation.html#topic-config
	 */
	enum TopicLevelBroker {
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


		private TopicLevelBroker(String key, String value, String serverDefaultProperty, String description) {
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
	
	/**
	 * http://kafka.apache.org/documentation.html#producerconfigs
	 */
	enum Producer {
		BOOTSTRAP_SERVERS("bootstrap.servers", "A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The client will make use of all servers irrespective of which servers are specified here for bootstrapping—this list only impacts the initial hosts used to discover the full set of servers. This list should be in the form host1:port1,host2:port2,.... Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).", "list", "", "", "high"),
		KEY_SERIALIZER("key.serializer", "Serializer class for key that implements the Serializer interface.", "class", "", "", "high"),
		VALUE_SERIALIZER("value.serializer", "Serializer class for value that implements the Serializer interface.", "class", "", "", "high"),
		ACKS("acks", "The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the durability of records that are sent. The following settings are common: acks=0 If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the retries configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to -1.), acks=1 This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost. acks=all This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee.", "string", "1", "[all, -1, 0, 1]", "high"),
		BUFFER_MEMORY("buffer.memory", "The total bytes of memory the producer can use to buffer records waiting to be sent to the server. If records are sent faster than they can be delivered to the server the producer will either block or throw an exception based on the preference specified by block.on.buffer.full. This setting should correspond roughly to the total memory the producer will use, but is not a hard bound since not all memory the producer uses is used for buffering. Some additional memory will be used for compression (if compression is enabled) as well as for maintaining in-flight requests.","long", "33554432", "[0,...]", "high"),
		COMPRESSION_TYPE("compression.type", "The compression type for all data generated by the producer. The default is none (i.e. no compression). Valid values are none, gzip, snappy, or lz4. Compression is of full batches of data, so the efficacy of batching will also impact the compression ratio (more batching means better compression).", "string", "none", "", "high"),
		RETRIES("retries", "Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. Note that this retry is no different than if the client resent the record upon receiving the error. Allowing retries will potentially change the ordering of records because if two records are sent to a single partition, and the first fails and is retried but the second succeeds, then the second record may appear first.", "int", "0", "[0,...,2147483647]", "high"),
		SSL_KEY_PASSWORD("ssl.key.password", "The password of the private key in the key store file. This is optional for client.", "password", "null", "", "high"),
		SSL_KEYSTORE_LOCATION("ssl.keystore.location", "The location of the key store file. This is optional for client and can be used for two-way authentication for client.", "string", "null", "", "high"),
		SSL_KEYSTORE_PASSWORD("ssl.keystore.password", "The store password for the key store file.This is optional for client and only needed if ssl.keystore.location is configured.", "password", "null", "", "high"),
		SSL_TRUSTSTORE_LOCATION("ssl.truststore.location", "The location of the trust store file.", "string", "null", "", "high"),
		SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password", "The password for the trust store file.", "password", "null", "", "high"),
		BATCH_SIZE("batch.size", "The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition. This helps performance on both the client and the server. This configuration controls the default batch size in bytes, No attempt will be made to batch records larger than this size.Requests sent to brokers will contain multiple batches, one for each partition with data available to be sent. A small batch size will make batching less common and may reduce throughput (a batch size of zero will disable batching entirely). A very large batch size may use memory a bit more wastefully as we will always allocate a buffer of the specified batch size in anticipation of additional records.", "int", "16384", "[0,...]", "medium"),
		CLIENT_ID("client.id", "An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.", "string", "\"\"", "", "medium"),
		CONNECTIONS_MAX_IDLE_MS("connections.max.idle.ms", "Close idle connections after the number of milliseconds specified by this config.", "long", "540000", "", "medium"),
		LINGER_MS("linger.ms", "The producer groups together any records that arrive in between request transmissions into a single batched request. Normally this occurs only under load when records arrive faster than they can be sent out. However in some circumstances the client may want to reduce the number of requests even under moderate load. This setting accomplishes this by adding a small amount of artificial delay—that is, rather than immediately sending out a record the producer will wait for up to the given delay to allow other records to be sent so that the sends can be batched together. This can be thought of as analogous to Nagle's algorithm in TCP. This setting gives the upper bound on the delay for batching: once we get batch.size worth of records for a partition it will be sent immediately regardless of this setting, however if we have fewer than this many bytes accumulated for this partition we will 'linger' for the specified time waiting for more records to show up. This setting defaults to 0 (i.e. no delay). Setting linger.ms=5, for example, would have the effect of reducing the number of requests sent but would add up to 5ms of latency to records sent in the absense of load.", "long", "0", "[0,...]", "medium"),
		MAX_BLOCK_MS("max.block.ms", "The configuration controls how long {@link KafkaProducer#send()} and {@link KafkaProducer#partitionsFor} will block.These methods can be blocked either because the buffer is full or metadata unavailable.Blocking in the user-supplied serializers or partitioner will not be counted against this timeout.", "long", "60000", "[0,...]", "medium"),
		MAX_REQUEST_SIZE("max.request.size", "The maximum size of a request. This is also effectively a cap on the maximum record size. Note that the server has its own cap on record size which may be different from this. This setting will limit the number of record batches the producer will send in a single request to avoid sending huge requests.", "int", "1048576", "[0,...]", "medium"),
		PARTITIONER_CLASS("partitioner.class", "Partitioner class that implements the Partitioner interface.", "class", "class org.apache.kafka.clients.producer.internals.DefaultPartitioner", "", "medium"),
		RECEIVE_BUFFER_BYTES("receive.buffer.bytes", "The size of the TCP receive buffer (SO_RCVBUF) to use when reading data.", "int", "32768", "[0,...]", "medium"),
		REQUEST_TIMEOUT_MS("request.timeout.ms", "The configuration controls the maximum amount of time the client will wait for the response of a request. If the response is not received before the timeout elapses the client will resend the request if necessary or fail the request if retries are exhausted.", "int", "30000", "[0,...]", "medium"),
		SASL_KERBEROS_SERVICE_NAME("sasl.kerberos.service.name", "The Kerberos principal name that Kafka runs as. This can be defined either in Kafka's JAAS config or in Kafka's config.", "string", "null", "", "medium"),
		SECURITY_PROTOCOL("security.protocol", "Protocol used to communicate with brokers. Valid values are: PLAINTEXT, SSL, SASL_PLAINTEXT, SASL_SSL.", "string", "PLAINTEXT", "", "medium"),
		SEND_BUFFER_BYTES("send.buffer.bytes", "The size of the TCP send buffer (SO_SNDBUF) to use when sending data.", "int", "131072", "[0,...]", "medium"),
		SSL_ENABLED_PROTOCOLS("ssl.enabled.protocols", "The list of protocols enabled for SSL connections.", "list", "[TLSv1.2, TLSv1.1, TLSv1]", "", "medium"),
		SSL_KEYSTORE_TYPE("ssl.keystore.type", "The file format of the key store file. This is optional for client.", "string", "JKS", "", "medium"),
		SSL_PROTOCOL("ssl.protocol", "The SSL protocol used to generate the SSLContext. Default setting is TLS, which is fine for most cases. Allowed values in recent JVMs are TLS, TLSv1.1 and TLSv1.2. SSL, SSLv2 and SSLv3 may be supported in older JVMs, but their usage is discouraged due to known security vulnerabilities.", "string", "TLS", "", "medium"),
		SSL_PROVIDER("ssl.provider", "The name of the security provider used for SSL connections. Default value is the default security provider of the JVM.", "string", "null", "", "medium"),
		SSL_TRUSTSTORE_TYPE("ssl.truststore.type", "The file format of the trust store file.", "string", "JKS", "", "medium"),
		TIMEOUT_MS("timeout.ms", "The configuration controls the maximum amount of time the server will wait for acknowledgments from followers to meet the acknowledgment requirements the producer has specified with the acks configuration. If the requested number of acknowledgments are not met when the timeout elapses an error will be returned. This timeout is measured on the server side and does not include the network latency of the request.", "int", "30000", "[0,...]", "medium"),
		BLOCK_ON_BUFFER_FULL("block.on.buffer.full", "When our memory buffer is exhausted we must either stop accepting new records (block) or throw errors. By default this setting is true and we block, however in some scenarios blocking is not desirable and it is better to immediately give an error. Setting this to false will accomplish that: the producer will throw a BufferExhaustedException if a recrord is sent and the buffer space is full.", "boolean", "false", "", "low"),
		MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION("max.in.flight.requests.per.connection", "The maximum number of unacknowledged requests the client will send on a single connection before blocking. Note that if this setting is set to be greater than 1 and there are failed sends, there is a risk of message re-ordering due to retries (i.e., if retries are enabled).", "int", "5", "[1,...]", "low"),
		METADATA_FETCH_TIMEOUT_MS("metadata.fetch.timeout.ms", "The first time data is sent to a topic we must fetch metadata about that topic to know which servers host the topic's partitions. This fetch to succeed before throwing an exception back to the client.", "long", "60000", "[0,...]", "low"),
		METADATA_MAX_AGE_MS("metadata.max.age.ms", "The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition leadership changes to proactively discover any new brokers or partitions.", "long", "300000", "[0,...]", "low"),
		METRIC_REPORTERS("metric.reporters", "A list of classes to use as metrics reporters. Implementing the MetricReporter interface allows plugging in classes that will be notified of new metric creation. The JmxReporter is always included to register JMX statistics.", "list", "[]", "", "low"),
		METRICS_NUM_SAMPLES("metrics.num.samples", "The number of samples maintained to compute metrics.", "int", "2", "[1,...]", "low"),
		METRICS_SAMPLE_WINDOW_MS("metrics.sample.window.ms", "The number of samples maintained to compute metrics.", "long", "30000", "[0,...]", "low"),
		RECONNECT_BACKOFF_MS("reconnect.backoff.ms", "The amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting to a host in a tight loop. This backoff applies to all requests sent by the consumer to the broker.", "long", "50", "[0,...]", "low"),
		RETRY_BACKOFF_MS("retry.backoff.ms", "The amount of time to wait before attempting to retry a failed fetch request to a given topic partition. This avoids repeated fetching-and-failing in a tight loop.", "long", "100", "[0,...]", "low"),
		SASL_KERBEROS_KINIT_CMD("sasl.kerberos.kinit.cmd", "Kerberos kinit command path.", "string", "/usr/bin/kinit", "", "low"),
		SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN("sasl.kerberos.min.time.before.relogin", "Login thread sleep time between refresh attempts.", "long", "60000", "", "low"),
		SASL_KERBEROS_TICKET_RENEW_JITTER("sasl.kerberos.ticket.renew.jitter", "Percentage of random jitter added to the renewal time.", "double", "0.05", "", "low"),
		SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR("sasl.kerberos.ticket.renew.window.factor", "Login thread will sleep until the specified window factor of time from last refresh to ticket's expiry has been reached, at which time it will try to renew the ticket.", "double", "0.8", "", "low"),
		SSL_CIPHER_SUITES("ssl.cipher.suites", "A list of cipher suites. This is a named combination of authentication, encryption, MAC and key exchange algorithm used to negotiate the security settings for a network connection using TLS or SSL network protocol.By default all the available cipher suites are supported.", "list", "null", "", "low"),
		SSL_ENDPOINT_IDENTIFICATION_ALGORITHM("ssl.endpoint.identification.algorithm", "The endpoint identification algorithm to validate server hostname using server certificate.", "string", "null", "", "low"),
		SSL_KEYMANAGER_ALGORITHM("ssl.keymanager.algorithm", "The algorithm used by key manager factory for SSL connections. Default value is the key manager factory algorithm configured for the Java Virtual Machine.", "string", "SunX509", "", "low"),
		SSL_TRUSTMANAGER_ALGORITHM("ssl.trustmanager.algorithm", "The algorithm used by trust manager factory for SSL connections. Default value is the trust manager factory algorithm configured for the Java Virtual Machine.", "string", "PKIX", "", "low");
		
		private String key;
		private String description;
		private String type;
		private String value;
		private String validValues;
		private String importance;

		private Producer(String key, String description, String type, String value, String validValues, String importance) {
			this.key = key;
			this.description = description;
			this.type = type;
			this.value = value;
			this.validValues = validValues;
			this.importance = importance;
		}

		public String getKey() {
			return key;
		}

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public String getValidValues() {
			return validValues;
		}

		public String getImportance() {
			return importance;
		}
	}

	/**
	 * http://kafka.apache.org/documentation.html#newconsumerconfigs
	 */
	enum Consumer {
		BOOTSTRAP_SERVERS("bootstrap.servers","A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The client will make use of all servers irrespective of which servers are specified here for bootstrapping—this list only impacts the initial hosts used to discover the full set of servers. This list should be in the form host1:port1,host2:port2,.... Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).","list","","","high"),
		KEY_DESERIALIZER("key.deserializer","Deserializer class for key that implements the Deserializer interface.","class","","","high"),
		VALUE_DESERIALIZER("value.deserializer","Deserializer class for value that implements the Deserializer interface.","class","","","high"),
		FETCH_MIN_BYTES("fetch.min.bytes","The minimum amount of data the server should return for a fetch request. If insufficient data is available the request will wait for that much data to accumulate before answering the request. The default setting of 1 byte means that fetch requests are answered as soon as a single byte of data is available or the fetch request times out waiting for data to arrive. Setting this to something greater than 1 will cause the server to wait for larger amounts of data to accumulate which can improve server throughput a bit at the cost of some additional latency.","int","1","[0,...]","high"),
		GROUP_ID("group.id","A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using subscribe(topic) or the Kafka-based offset management strategy.","string","\"\"","","high"),
		HEARTBEAT_INTERVAL_MS("heartbeat.interval.ms","The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities. Heartbeats are used to ensure that the consumer's session stays active and to facilitate rebalancing when new consumers join or leave the group. The value must be set lower than session.timeout.ms, but typically should be set no higher than 1/3 of that value. It can be adjusted even lower to control the expected time for normal rebalances.","int","3000","","high"),
		MAX_PARTITION_FETCH_BYTES("max.partition.fetch.bytes","The maximum amount of data per-partition the server will return. The maximum total memory used for a request will be #partitions * max.partition.fetch.bytes. This size must be at least as large as the maximum message size the server allows or else it is possible for the producer to send messages larger than the consumer can fetch. If that happens, the consumer can get stuck trying to fetch a large message on a certain partition.","int","1048576","[0,...]","high"),
		SESSION_TIMEOUT_MS("session.timeout.ms","The timeout used to detect failures when using Kafka's group management facilities.","int","30000","","high"),
		SSL_KEY_PASSWORD("ssl.key.password","The password of the private key in the key store file. This is optional for client.","password","null","","high"),
		SSL_KEYSTORE_LOCATION("ssl.keystore.location","The location of the key store file. This is optional for client and can be used for two-way authentication for client.","string","null","","high"),
		SSL_KEYSTORE_PASSWORD("ssl.keystore.password","The store password for the key store file.This is optional for client and only needed if ssl.keystore.location is configured.","password","null","","high"),
		SSL_TRUSTSTORE_LOCATION("ssl.truststore.location","The location of the trust store file.","string","null","","high"),
		SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password","The password for the trust store file.","password","null","","high"),
		AUTO_OFFSET_RESET("auto.offset.reset","What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server (e.g. because that data has been deleted): earliest: automatically reset the offset to the earliest offset latest: automatically reset the offset to the latest offset none: throw exception to the consumer if no previous offset is found for the consumer's group anything else: throw exception to the consumer.", "string","latest","[latest, earliest, none]","medium"),
		CONNECTIONS_MAX_IDLE_MS("connections.max.idle.ms","Close idle connections after the number of milliseconds specified by this config.","long","540000","","medium"),
		ENABLE_AUTO_COMMIT("enable.auto.commit","If true the consumer's offset will be periodically committed in the background.","boolean","true","","medium"),
		PARTITION_ASSIGNMENT_STRATEGY("partition.assignment.strategy","The class name of the partition assignment strategy that the client will use to distribute partition ownership amongst consumer instances when group management is used","list","[org.apache.kafka.clients.consumer.RangeAssignor]","","medium"),
		RECEIVE_BUFFER_BYTES("receive.buffer.bytes","The size of the TCP receive buffer (SO_RCVBUF) to use when reading data.","int","32768","[0,...]","medium"),
		REQUEST_TIMEOUT_MS("request.timeout.ms","The configuration controls the maximum amount of time the client will wait for the response of a request. If the response is not received before the timeout elapses the client will resend the request if necessary or fail the request if retries are exhausted.","int","40000","[0,...]","medium"),
		SASL_KERBEROS_SERVICE_NAME("sasl.kerberos.service.name","The Kerberos principal name that Kafka runs as. This can be defined either in Kafka's JAAS config or in Kafka's config.","string","null","","medium"),
		SECURITY_PROTOCOL("security.protocol","Protocol used to communicate with brokers. Valid values are: PLAINTEXT, SSL, SASL_PLAINTEXT, SASL_SSL.","string","PLAINTEXT","","medium"),
		SEND_BUFFER_BYTES("send.buffer.bytes","The size of the TCP send buffer (SO_SNDBUF) to use when sending data.","int","131072","[0,...]","medium"),
		SSL_ENABLED_PROTOCOLS("ssl.enabled.protocols","The list of protocols enabled for SSL connections.","list","[TLSv1.2, TLSv1.1, TLSv1]","","medium"),
		SSL_KEYSTORE_TYPE("ssl.keystore.type","The file format of the key store file. This is optional for client.","string","JKS","","medium"),
		SSL_PROTOCOL("ssl.protocol","The SSL protocol used to generate the SSLContext. Default setting is TLS, which is fine for most cases. Allowed values in recent JVMs are TLS, TLSv1.1 and TLSv1.2. SSL, SSLv2 and SSLv3 may be supported in older JVMs, but their usage is discouraged due to known security vulnerabilities.","string","TLS","","medium"),
		SSL_PROVIDER("ssl.provider","The name of the security provider used for SSL connections. Default value is the default security provider of the JVM.","string","null","","medium"),
		SSL_TRUSTSTORE_TYPE("ssl.truststore.type","The file format of the trust store file.","string","JKS","","medium"),
		AUTO_COMMIT_INTERVAL_MS("auto.commit.interval.ms","The frequency in milliseconds that the consumer offsets are auto-committed to Kafka if enable.auto.commit is set to true.","long","5000","[0,...]","low"),
		CHECK_CRCS("check.crcs","Automatically check the CRC32 of the records consumed. This ensures no on-the-wire or on-disk corruption to the messages occurred. This check adds some overhead, so it may be disabled in cases seeking extreme performance.","boolean","true","","low"),
		CLIENT_ID("client.id","An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.","string","\"\"","","low"),
		FETCH_MAX_WAIT_MS("fetch.max.wait.ms","The maximum amount of time the server will block before answering the fetch request if there isn't sufficient data to immediately satisfy the requirement given by fetch.min.bytes.","int","500","[0,...]","low"),
		METADATA_MAX_AGE_MS("metadata.max.age.ms","The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition leadership changes to proactively discover any new brokers or partitions.","long","300000","[0,...]","low"),
		METRIC_REPORTERS("metric.reporters","A list of classes to use as metrics reporters. Implementing the MetricReporter interface allows plugging in classes that will be notified of new metric creation. The JmxReporter is always included to register JMX statistics.","list","[]","","low"),
		METRICS_NUM_SAMPLES("metrics.num.samples","The number of samples maintained to compute metrics.","int","2","[1,...]","low"),
		METRICS_SAMPLE_WINDOW_MS("metrics.sample.window.ms","The number of samples maintained to compute metrics.","long","30000","[0,...]","low"),
		RECONNECT_BACKOFF_MS("reconnect.backoff.ms","The amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting to a host in a tight loop. This backoff applies to all requests sent by the consumer to the broker.","long","50","[0,...]","low"),
		RETRY_BACKOFF_MS("retry.backoff.ms","The amount of time to wait before attempting to retry a failed fetch request to a given topic partition. This avoids repeated fetching-and-failing in a tight loop.","long","100","[0,...]","low"),
		SASL_KERBEROS_KINIT_CMD("sasl.kerberos.kinit.cmd","Kerberos kinit command path.","string","/usr/bin/kinit","","low"),
		SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN("sasl.kerberos.min.time.before.relogin","Login thread sleep time between refresh attempts.","long","60000","","low"),
		SASL_KERBEROS_TICKET_RENEW_JITTER("sasl.kerberos.ticket.renew.jitter","Percentage of random jitter added to the renewal time.","double","0.05","","low"),
		SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR("sasl.kerberos.ticket.renew.window.factor","Login thread will sleep until the specified window factor of time from last refresh to ticket's expiry has been reached, at which time it will try to renew the ticket.","double","0.8","","low"),
		SSL_CIPHER_SUITES("ssl.cipher.suites","A list of cipher suites. This is a named combination of authentication, encryption, MAC and key exchange algorithm used to negotiate the security settings for a network connection using TLS or SSL network protocol.By default all the available cipher suites are supported.","list","null","","low"),
		SSL_ENDPOINT_IDENTIFICATION_ALGORITHM("ssl.endpoint.identification.algorithm","The endpoint identification algorithm to validate server hostname using server certificate.","string","null","","low"),
		SSL_KEYMANAGER_ALGORITHM("ssl.keymanager.algorithm","The algorithm used by key manager factory for SSL connections. Default value is the key manager factory algorithm configured for the Java Virtual Machine.","string","SunX509","","low"),
		SSL_TRUSTMANAGER_ALGORITHM("ssl.trustmanager.algorithm","The algorithm used by trust manager factory for SSL connections. Default value is the trust manager factory algorithm configured for the Java Virtual Machine.","string","PKIX","","low");

		private String key;
		private String description;
		private String type;
		private String value;
		private String validValues;
		private String importance;

		private Consumer(String key, String description, String type, String value, String validValues, String importance) {
			this.key = key;
			this.description = description;
			this.type = type;
			this.value = value;
			this.validValues = validValues;
			this.importance = importance;
		}

		public String getKey() {
			return key;
		}

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public String getValidValues() {
			return validValues;
		}

		public String getImportance() {
			return importance;
		}
	}

	/**
	 * http://kafka.apache.org/documentation.html#connectconfigs
	 */
	enum KafkaConnect {
		GROUP_ID("group.id","A unique string that identifies the Connect cluster group this worker belongs to.","string","","","high"),
		INTERNAL_KEY_CONVERTER("internal.key.converter","Converter class for internal key Connect data that implements the Converter interface. Used for converting data like offsets and configs.","class","","","high"),
		INTERNAL_VALUE_CONVERTER("internal.value.converter","Converter class for offset value Connect data that implements the Converter interface. Used for converting data like offsets and configs.","class","","","high"),
		KEY_CONVERTER("key.converter","Converter class for key Connect data that implements the Converter interface.","class","","","high"),
		VALUE_CONVERTER("value.converter","Converter class for value Connect data that implements the Converter interface.","class","","","high"),
		BOOTSTRAP_SERVERS("bootstrap.servers","A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The client will make use of all servers irrespective of which servers are specified here for bootstrapping—this list only impacts the initial hosts used to discover the full set of servers. This list should be in the form host1:port1,host2:port2,.... Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).","list","[localhost:9092]","","high"),
		CLUSTER("cluster","ID for this cluster, which is used to provide a namespace so multiple Kafka Connect clusters or instances may co-exist while sharing a single Kafka cluster.","string","connect","","high"),
		HEARTBEAT_INTERVAL_MS("heartbeat.interval.ms","The expected time between heartbeats to the group coordinator when using Kafka's group management facilities. Heartbeats are used to ensure that the worker's session stays active and to facilitate rebalancing when new members join or leave the group. The value must be set lower than session.timeout.ms, but typically should be set no higher than 1/3 of that value. It can be adjusted even lower to control the expected time for normal rebalances.","int","3000","","high"),
		SESSION_TIMEOUT_MS("session.timeout.ms","The timeout used to detect failures when using Kafka's group management facilities.","int","30000","","high"),
		SSL_KEY_PASSWORD("ssl.key.password","The password of the private key in the key store file. This is optional for client.","password","null","","high"),
		SSL_KEYSTORE_LOCATION("ssl.keystore.location","The location of the key store file. This is optional for client and can be used for two-way authentication for client.","string","null","","high"),
		SSL_KEYSTORE_PASSWORD("ssl.keystore.password","The store password for the key store file.This is optional for client and only needed if ssl.keystore.location is configured.","password","null","","high"),
		SSL_TRUSTSTORE_LOCATION("ssl.truststore.location","The location of the trust store file.","string","null","","high"),
		SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password","The password for the trust store file.","password","null","","high"),
		CONNECTIONS_MAX_IDLE_MS("connections.max.idle.ms","Close idle connections after the number of milliseconds specified by this config.","long","540000","","medium"),
		RECEIVE_BUFFER_BYTES("receive.buffer.bytes","The size of the TCP receive buffer (SO_RCVBUF) to use when reading data.","int","32768","[0,...]","medium"),
		REQUEST_TIMEOUT_MS("request.timeout.ms","The configuration controls the maximum amount of time the client will wait for the response of a request. If the response is not received before the timeout elapses the client will resend the request if necessary or fail the request if retries are exhausted.","int","40000","[0,...]","medium"),
		SASL_KERBEROS_SERVICE_NAME("sasl.kerberos.service.name","The Kerberos principal name that Kafka runs as. This can be defined either in Kafka's JAAS config or in Kafka's config.","string","null","","medium"),
		SECURITY_PROTOCOL("security.protocol","Protocol used to communicate with brokers. Valid values are: PLAINTEXT, SSL, SASL_PLAINTEXT, SASL_SSL.","string","PLAINTEXT","","medium"),
		SEND_BUFFER_BYTES("send.buffer.bytes","The size of the TCP send buffer (SO_SNDBUF) to use when sending data.","int","131072","[0,...]","medium"),
		SSL_ENABLED_PROTOCOLS("ssl.enabled.protocols","The list of protocols enabled for SSL connections.","list","[TLSv1.2, TLSv1.1, TLSv1]","","medium"),
		SSL_KEYSTORE_TYPE("ssl.keystore.type","The file format of the key store file. This is optional for client.","string","JKS","","medium"),
		SSL_PROTOCOL("ssl.protocol","The SSL protocol used to generate the SSLContext. Default setting is TLS, which is fine for most cases. Allowed values in recent JVMs are TLS, TLSv1.1 and TLSv1.2. SSL, SSLv2 and SSLv3 may be supported in older JVMs, but their usage is discouraged due to known security vulnerabilities.","string","TLS","","medium"),
		SSL_PROVIDER("ssl.provider","The name of the security provider used for SSL connections. Default value is the default security provider of the JVM.","string","null","","medium"),
		SSL_TRUSTSTORE_TYPE("ssl.truststore.type","The file format of the trust store file.","string","JKS","","medium"),
		WORKER_SYNC_TIMEOUT_MS("worker.sync.timeout.ms","When the worker is out of sync with other workers and needs to resynchronize configurations, wait up to this amount of time before giving up, leaving the group, and waiting a backoff period before rejoining.","int","3000","","medium"),
		WORKER_UNSYNC_BACKOFF_MS("worker.unsync.backoff.ms","When the worker is out of sync with other workers and fails to catch up within worker.sync.timeout.ms, leave the Connect cluster for this long before rejoining.","int","300000","","medium"),
		CLIENT_ID("client.id","An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.","string","\"\"","","low"),
		METADATA_MAX_AGE_MS("metadata.max.age.ms","The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition leadership changes to proactively discover any new brokers or partitions.","long","300000","[0,...]","low"),
		METRIC_REPORTERS("metric.reporters","A list of classes to use as metrics reporters. Implementing the MetricReporter interface allows plugging in classes that will be notified of new metric creation. The JmxReporter is always included to register JMX statistics.","list","[]","","low"),
		METRICS_NUM_SAMPLES("metrics.num.samples","The number of samples maintained to compute metrics.","int","2","[1,...]","low"),
		METRICS_SAMPLE_WINDOW_MS("metrics.sample.window.ms","The number of samples maintained to compute metrics.","long","30000","[0,...]","low"),
		OFFSET_FLUSH_INTERVAL_MS("offset.flush.interval.ms","Interval at which to try committing offsets for tasks.","long","60000","","low"),
		OFFSET_FLUSH_TIMEOUT_MS("offset.flush.timeout.ms","Maximum number of milliseconds to wait for records to flush and partition offset data to be committed to offset storage before cancelling the process and restoring the offset data to be committed in a future attempt.","long","5000","","low"),
		RECONNECT_BACKOFF_MS("reconnect.backoff.ms","The amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting to a host in a tight loop. This backoff applies to all requests sent by the consumer to the broker.","long","50","[0,...]","low"),
		REST_ADVERTISED_HOST_NAME("rest.advertised.host.name","If this is set, this is the hostname that will be given out to other workers to connect to.","string","null","","low"),
		REST_ADVERTISED_PORT("rest.advertised.port","If this is set, this is the port that will be given out to other workers to connect to.","int","null","","low"),
		REST_HOST_NAME("rest.host.name","Hostname for the REST API. If this is set, it will only bind to this interface.","string","null","","low"),
		REST_PORT("rest.port","Port for the REST API to listen on.","int","8083","","low"),
		RETRY_BACKOFF_MS("retry.backoff.ms","The amount of time to wait before attempting to retry a failed fetch request to a given topic partition. This avoids repeated fetching-and-failing in a tight loop.","long","100","[0,...]","low"),
		SASL_KERBEROS_KINIT_CMD("sasl.kerberos.kinit.cmd","Kerberos kinit command path.","string","/usr/bin/kinit","","low"),
		SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN("sasl.kerberos.min.time.before.relogin","Login thread sleep time between refresh attempts.","long","60000","","low"),
		SASL_KERBEROS_TICKET_RENEW_JITTER("sasl.kerberos.ticket.renew.jitter","Percentage of random jitter added to the renewal time.","double","0.05","","low"),
		SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR("sasl.kerberos.ticket.renew.window.factor","Login thread will sleep until the specified window factor of time from last refresh to ticket's expiry has been reached, at which time it will try to renew the ticket.","double","0.8","","low"),
		SSL_CIPHER_SUITES("ssl.cipher.suites","A list of cipher suites. This is a named combination of authentication, encryption, MAC and key exchange algorithm used to negotiate the security settings for a network connection using TLS or SSL network protocol.By default all the available cipher suites are supported.","list","null","","low"),
		SSL_ENDPOINT_IDENTIFICATION_ALGORITHM("ssl.endpoint.identification.algorithm","The endpoint identification algorithm to validate server hostname using server certificate.","string","null","","low"),
		SSL_KEYMANAGER_ALGORITHM("ssl.keymanager.algorithm","The algorithm used by key manager factory for SSL connections. Default value is the key manager factory algorithm configured for the Java Virtual Machine.","string","SunX509","","low"),
		SSL_TRUSTMANAGER_ALGORITHM("ssl.trustmanager.algorithm","The algorithm used by trust manager factory for SSL connections. Default value is the trust manager factory algorithm configured for the Java Virtual Machine.","string","PKIX","","low"),
		TASK_SHUTDOWN_GRACEFUL_TIMEOUT_MS("task.shutdown.graceful.timeout.ms","Amount of time to wait for tasks to shutdown gracefully. This is the total amount of time, not per task. All task have shutdown triggered, then they are waited on sequentially.","long","5000","","low");

		private String key;
		private String description;
		private String type;
		private String value;
		private String validValues;
		private String importance;

		private KafkaConnect(String key, String description, String type, String value, String validValues, String importance) {
			this.key = key;
			this.description = description;
			this.type = type;
			this.value = value;
			this.validValues = validValues;
			this.importance = importance;
		}

		public String getKey() {
			return key;
		}

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public String getValidValues() {
			return validValues;
		}

		public String getImportance() {
			return importance;
		}
	}

}