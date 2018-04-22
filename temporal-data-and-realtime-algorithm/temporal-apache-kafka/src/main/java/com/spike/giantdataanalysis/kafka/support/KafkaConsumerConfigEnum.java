package com.spike.giantdataanalysis.kafka.support;

//@formatter:off
/**
 * http://kafka.apache.org/documentation.html#newconsumerconfigs
 */
public enum KafkaConsumerConfigEnum {
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

	private KafkaConsumerConfigEnum(String key, String description, String type, String value, String validValues, String importance) {
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