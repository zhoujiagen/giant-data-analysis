package com.spike.giantdataanalysis.kafka.support;

//@formatter:off
/**
 * http://kafka.apache.org/documentation.html#brokerconfigs
 */
public	enum KafkaBrokerConfigEnum {
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

		private KafkaBrokerConfigEnum(String key, String description, String type, String value, String validValues, String importance) {
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

	