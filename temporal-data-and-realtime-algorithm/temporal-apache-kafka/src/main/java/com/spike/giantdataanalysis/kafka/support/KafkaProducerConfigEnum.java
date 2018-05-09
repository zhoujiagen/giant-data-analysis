package com.spike.giantdataanalysis.kafka.support;

//@formatter:off
/**
 * http://kafka.apache.org/documentation.html#producerconfigs
 * @see org.apache.kafka.clients.producer.ProducerConfig
 */
public enum KafkaProducerConfigEnum {
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

	private KafkaProducerConfigEnum(String key, String description, String type, String value, String validValues, String importance) {
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