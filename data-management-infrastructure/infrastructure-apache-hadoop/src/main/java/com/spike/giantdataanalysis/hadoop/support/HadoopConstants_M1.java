package com.spike.giantdataanalysis.hadoop.support;

//@formatter:off
/**
 * Hadoop V1版本中的常量
 * 
 * @author zhoujiagen
 *
 */
public interface HadoopConstants_M1 {

	enum Core {
		hadoop_tmp_dir
		(
		"hadoop.tmp.dir",
		"/tmp/hadoop-${user.name}",
		"A base for other temporary directories. "
		),

		hadoop_native_lib
		(
		"hadoop.native.lib",
		"true",
		"Should native hadoop libraries if present be used. "
		),

		hadoop_http_filter_initializers
		(
		"hadoop.http.filter.initializers",
		"",
		"A comma separated list of class names. Each class in the list must extend org.apache.hadoop.http.FilterInitializer. The corresponding Filter will be initialized. Then the Filter will be applied to all user facing jsp and servlet web pages. The ordering of the list defines the ordering of the filters. "
		),

		hadoop_security_group_mapping
		(
		"hadoop.security.group.mapping",
		"org.apache.hadoop.security.ShellBasedUnixGroupsMapping",
		"Class for user to group mapping (get groups for a given user) "
		),

		hadoop_security_authorization
		(
		"hadoop.security.authorization",
		"false",
		"Is service-level authorization enabled? "
		),

		hadoop_security_instrumentation_requires_admin
		(
		"hadoop.security.instrumentation.requires.admin",
		"false",
		"Indicates if administrator ACLs are required to access instrumentation servlets (JMX METRICS CONF STACKS). "
		),

		hadoop_security_authentication
		(
		"hadoop.security.authentication",
		"simple",
		"Possible values are simple (no authentication) and kerberos "
		),

		hadoop_security_token_service_use_ip
		(
		"hadoop.security.token.service.use_ip",
		"true",
		"Controls whether tokens always use IP addresses. DNS changes will not be detected if this option is enabled. Existing client connections that break will always reconnect to the IP of the original host. New clients will connect to the host's new IP but fail to locate a token. Disabling this option will allow existing and new clients to detect an IP change and continue to locate the new host's token. "
		),

		hadoop_security_use_weak_http_crypto
		(
		"hadoop.security.use-weak-http-crypto",
		"false",
		"If enabled use KSSL to authenticate HTTP connections to the NameNode. Due to a bug in JDK6 using KSSL requires one to configure Kerberos tickets to use encryption types that are known to be cryptographically weak. If disabled SPNEGO will be used for HTTP authentication which supports stronger encryption types. "
		),

		hadoop_logfile_size
		(
		"hadoop.logfile.size",
		"10000000",
		"The max size of each log file "
		),

		hadoop_logfile_count
		(
		"hadoop.logfile.count",
		"10",
		"The max number of log files "
		),

		io_file_buffer_size
		(
		"io.file.buffer.size",
		"4096",
		"The size of buffer for use in sequence files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		io_bytes_per_checksum
		(
		"io.bytes.per.checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than io.file.buffer.size. "
		),

		io_skip_checksum_errors
		(
		"io.skip.checksum.errors",
		"false",
		"If true when a checksum error is encountered while reading a sequence file entries are skipped instead of throwing an exception. "
		),

		io_compression_codecs
		(
		"io.compression.codecs",
		"org.apache.hadoop.io.compress.DefaultCodec,org.apache.hadoop.io.compress.GzipCodec,org.apache.hadoop.io.compress.BZip2Codec,org.apache.hadoop.io.compress.SnappyCodec",
		"A list of the compression codec classes that can be used for compression/decompression. "
		),

		io_serializations
		(
		"io.serializations",
		"org.apache.hadoop.io.serializer.WritableSerialization",
		"A list of serialization classes that can be used for obtaining serializers and deserializers. "
		),

		fs_default_name
		(
		"fs.default.name",
		"file:///",
		"The name of the default file system. A URI whose scheme and authority determine the FileSystem implementation. The uri's scheme determines the config property (fs.SCHEME.impl) naming the FileSystem implementation class. The uri's authority is used to determine the host port etc. for a filesystem. "
		),

		fs_trash_interval
		(
		"fs.trash.interval",
		"0",
		"Number of minutes between trash checkpoints. If zero the trash feature is disabled. "
		),

		fs_file_impl
		(
		"fs.file.impl",
		"org.apache.hadoop.fs.LocalFileSystem",
		"The FileSystem for file: uris. "
		),

		fs_hdfs_impl
		(
		"fs.hdfs.impl",
		"org.apache.hadoop.hdfs.DistributedFileSystem",
		"The FileSystem for hdfs: uris. "
		),

		fs_s3_impl
		(
		"fs.s3.impl",
		"org.apache.hadoop.fs.s3.S3FileSystem",
		"The FileSystem for s3: uris. "
		),

		fs_s3n_impl
		(
		"fs.s3n.impl",
		"org.apache.hadoop.fs.s3native.NativeS3FileSystem",
		"The FileSystem for s3n: (Native S3) uris. "
		),

		fs_kfs_impl
		(
		"fs.kfs.impl",
		"org.apache.hadoop.fs.kfs.KosmosFileSystem",
		"The FileSystem for kfs: uris. "
		),

		fs_hftp_impl
		(
		"fs.hftp.impl",
		"org.apache.hadoop.hdfs.HftpFileSystem",
		""
		),

		fs_hsftp_impl
		(
		"fs.hsftp.impl",
		"org.apache.hadoop.hdfs.HsftpFileSystem",
		""
		),

		fs_webhdfs_impl
		(
		"fs.webhdfs.impl",
		"org.apache.hadoop.hdfs.web.WebHdfsFileSystem",
		""
		),

		fs_ftp_impl
		(
		"fs.ftp.impl",
		"org.apache.hadoop.fs.ftp.FTPFileSystem",
		"The FileSystem for ftp: uris. "
		),

		fs_ramfs_impl
		(
		"fs.ramfs.impl",
		"org.apache.hadoop.fs.InMemoryFileSystem",
		"The FileSystem for ramfs: uris. "
		),

		fs_har_impl
		(
		"fs.har.impl",
		"org.apache.hadoop.fs.HarFileSystem",
		"The filesystem for Hadoop archives. "
		),

		fs_har_impl_disable_cache
		(
		"fs.har.impl.disable.cache",
		"true",
		"Don't cache 'har' filesystem instances. "
		),

		fs_checkpoint_dir
		(
		"fs.checkpoint.dir",
		"${hadoop.tmp.dir}/dfs/namesecondary",
		"Determines where on the local filesystem the DFS secondary name node should store the temporary images to merge. If this is a comma-delimited list of directories then the image is replicated in all of the directories for redundancy. "
		),

		fs_checkpoint_edits_dir
		(
		"fs.checkpoint.edits.dir",
		"${fs.checkpoint.dir}",
		"Determines where on the local filesystem the DFS secondary name node should store the temporary edits to merge. If this is a comma-delimited list of directoires then teh edits is replicated in all of the directoires for redundancy. Default value is same as fs.checkpoint.dir "
		),

		fs_checkpoint_period
		(
		"fs.checkpoint.period",
		"3600",
		"The number of seconds between two periodic checkpoints. "
		),

		fs_checkpoint_size
		(
		"fs.checkpoint.size",
		"67108864",
		"The size of the current edit log (in bytes) that triggers a periodic checkpoint even if the fs.checkpoint.period hasn't expired. "
		),

		fs_s3_block_size
		(
		"fs.s3.block.size",
		"67108864",
		"Block size to use when writing files to S3. "
		),

		fs_s3_buffer_dir
		(
		"fs.s3.buffer.dir",
		"${hadoop.tmp.dir}/s3",
		"Determines where on the local filesystem the S3 filesystem should store files before sending them to S3 (or after retrieving them from S3). "
		),

		fs_s3_maxRetries
		(
		"fs.s3.maxRetries",
		"4",
		"The maximum number of retries for reading or writing files to S3 before we signal failure to the application. "
		),

		fs_s3_sleepTimeSeconds
		(
		"fs.s3.sleepTimeSeconds",
		"10",
		"The number of seconds to sleep between each S3 retry. "
		),

		local_cache_size
		(
		"local.cache.size",
		"10737418240",
		"The limit on the size of cache you want to keep set by default to 10GB. This will act as a soft limit on the cache directory for out of band data. "
		),

		io_seqfile_compress_blocksize
		(
		"io.seqfile.compress.blocksize",
		"1000000",
		"The minimum block size for compression in block compressed SequenceFiles. "
		),

		io_seqfile_lazydecompress
		(
		"io.seqfile.lazydecompress",
		"true",
		"Should values of block-compressed SequenceFiles be decompressed only when necessary. "
		),

		io_seqfile_sorter_recordlimit
		(
		"io.seqfile.sorter.recordlimit",
		"1000000",
		"The limit on number of records to be kept in memory in a spill in SequenceFiles.Sorter "
		),

		io_mapfile_bloom_size
		(
		"io.mapfile.bloom.size",
		"1048576",
		"The size of BloomFilter-s used in BloomMapFile. Each time this many keys is appended the next BloomFilter will be created (inside a DynamicBloomFilter). Larger values minimize the number of filters which slightly increases the performance but may waste too much space if the total number of keys is usually much smaller than this number. "
		),

		io_mapfile_bloom_error_rate
		(
		"io.mapfile.bloom.error.rate",
		"0.005",
		"The rate of false positives in BloomFilter-s used in BloomMapFile. As this value decreases the size of BloomFilter-s increases exponentially. This value is the probability of encountering false positives (default is 0.5%). "
		),

		hadoop_util_hash_type
		(
		"hadoop.util.hash.type",
		"murmur",
		"The default implementation of Hash. Currently this can take one of the two values: 'murmur' to select MurmurHash and 'jenkins' to select JenkinsHash. "
		),

		ipc_client_idlethreshold
		(
		"ipc.client.idlethreshold",
		"4000",
		"Defines the threshold number of connections after which connections will be inspected for idleness. "
		),

		ipc_client_kill_max
		(
		"ipc.client.kill.max",
		"10",
		"Defines the maximum number of clients to disconnect in one go. "
		),

		ipc_client_connection_maxidletime
		(
		"ipc.client.connection.maxidletime",
		"10000",
		"The maximum time in msec after which a client will bring down the connection to the server. "
		),

		ipc_client_connect_max_retries
		(
		"ipc.client.connect.max.retries",
		"10",
		"Indicates the number of retries a client will make to establish a server connection. "
		),

		ipc_server_listen_queue_size
		(
		"ipc.server.listen.queue.size",
		"128",
		"Indicates the length of the listen queue for servers accepting client connections. "
		),

		ipc_server_tcpnodelay
		(
		"ipc.server.tcpnodelay",
		"false",
		"Turn on/off Nagle's algorithm for the TCP socket connection on the server. Setting to true disables the algorithm and may decrease latency with a cost of more/smaller packets. "
		),

		ipc_client_tcpnodelay
		(
		"ipc.client.tcpnodelay",
		"false",
		"Turn on/off Nagle's algorithm for the TCP socket connection on the client. Setting to true disables the algorithm and may decrease latency with a cost of more/smaller packets. "
		),

		webinterface_private_actions
		(
		"webinterface.private.actions",
		"false",
		"If set to true the web interfaces of JT and NN may contain actions such as kill job delete file etc. that should not be exposed to public. Enable this option if the interfaces are only reachable by those who have the right authorization. "
		),

		hadoop_rpc_socket_factory_class_default
		(
		"hadoop.rpc.socket.factory.class.default",
		"org.apache.hadoop.net.StandardSocketFactory",
		"Default SocketFactory to use. This parameter is expected to be formatted as package.FactoryClassName. "
		),

		hadoop_rpc_socket_factory_class_ClientProtocol
		(
		"hadoop.rpc.socket.factory.class.ClientProtocol",
		"",
		"SocketFactory to use to connect to a DFS. If null or empty use hadoop.rpc.socket.class.default. This socket factory is also used by DFSClient to create sockets to DataNodes. "
		),

		hadoop_socks_server
		(
		"hadoop.socks.server",
		"",
		"Address (host:port) of the SOCKS server to be used by the SocksSocketFactory. "
		),

		topology_node_switch_mapping_impl
		(
		"topology.node.switch.mapping.impl",
		"org.apache.hadoop.net.ScriptBasedMapping",
		"The default implementation of the DNSToSwitchMapping. It invokes a script specified in topology.script.file.name to resolve node names. If the value for topology.script.file.name is not set the default value of DEFAULT_RACK is returned for all node names. "
		),

		net_topology_impl
		(
		"net.topology.impl",
		"org.apache.hadoop.net.NetworkTopology",
		"The default implementation of NetworkTopology which is classic three layer one. "
		),

		topology_script_file_name
		(
		"topology.script.file.name",
		"",
		"The script name that should be invoked to resolve DNS names to NetworkTopology names. Example: the script would take host.foo.bar as an argument and return /rack1 as the output. "
		),

		topology_script_number_args
		(
		"topology.script.number.args",
		"100",
		"The max number of args that the script configured with topology.script.file.name should be run with. Each arg is an IP address. "
		),

		hadoop_security_uid_cache_secs
		(
		"hadoop.security.uid.cache.secs",
		"14400",
		"NativeIO maintains a cache from UID to UserName. This is the timeout for an entry in that cache. "
		),

		hadoop_http_authentication_type
		(
		"hadoop.http.authentication.type",
		"simple",
		"Defines authentication used for Oozie HTTP endpoint. Supported values are: simple | kerberos | #AUTHENTICATION_HANDLER_CLASSNAME# "
		),

		hadoop_http_authentication_token_validity
		(
		"hadoop.http.authentication.token.validity",
		"36000",
		"Indicates how long (in seconds) an authentication token is valid before it has to be renewed. "
		),

		hadoop_http_authentication_signature_secret_file
		(
		"hadoop.http.authentication.signature.secret.file",
		"${user.home}/hadoop-http-auth-signature-secret",
		"The signature secret for signing the authentication tokens. If not set a random secret is generated at startup time. The same secret should be used for JT/NN/DN/TT configurations. "
		),

		hadoop_http_authentication_cookie_domain
		(
		"hadoop.http.authentication.cookie.domain",
		"",
		"The domain to use for the HTTP cookie that stores the authentication token. In order to authentiation to work correctly across all Hadoop nodes web-consoles the domain must be correctly set. IMPORTANT: when using IP addresses browsers ignore cookies with domain settings. For this setting to work properly all nodes in the cluster must be configured to generate URLs with hostname.domain names on it. "
		),

		hadoop_http_authentication_simple_anonymous_allowed
		(
		"hadoop.http.authentication.simple.anonymous.allowed",
		"true",
		"Indicates if anonymous requests are allowed when using 'simple' authentication. "
		),

		hadoop_http_authentication_kerberos_principal
		(
		"hadoop.http.authentication.kerberos.principal",
		"HTTP/localhost@LOCALHOST",
		"Indicates the Kerberos principal to be used for HTTP endpoint. The principal MUST start with 'HTTP/' as per Kerberos HTTP SPNEGO specification. "
		),

		hadoop_http_authentication_kerberos_keytab
		(
		"hadoop.http.authentication.kerberos.keytab",
		"${user.home}/hadoop.keytab",
		"Location of the keytab file with the credentials for the principal. Referring to the same keytab file Oozie uses for its Kerberos credentials for Hadoop. "
		),

		hadoop_relaxed_worker_version_check
		(
		"hadoop.relaxed.worker.version.check",
		"false",
		"By default datanodes refuse to connect to namenodes if their build revision (svn revision) do not match and tasktrackers refuse to connect to jobtrackers if their build version (version revision user and source checksum) do not match. This option changes the behavior of hadoop workers to only check for a version match (eg 1.0.2) but ignore the other build fields (revision user and source checksum). "
		),

		hadoop_skip_worker_version_check
		(
		"hadoop.skip.worker.version.check",
		"false",
		"By default datanodes refuse to connect to namenodes if their build revision (svn revision) do not match and tasktrackers refuse to connect to jobtrackers if their build version (version revision user and source checksum) do not match. This option changes the behavior of hadoop workers to skip doing a version check at all. This option supersedes the 'hadoop.relaxed.worker.version.check' option. "
		),

		hadoop_jetty_logs_serve_aliases
		(
		"hadoop.jetty.logs.serve.aliases",
		"true",
		"Enable/Disable aliases serving from jetty "
		),

		ipc_client_fallback_to_simple_auth_allowed
		(
		"ipc.client.fallback-to-simple-auth-allowed",
		"false",
		"When a client is configured to attempt a secure connection but attempts to connect to an insecure server that server may instruct the client to switch to SASL SIMPLE (unsecure) authentication. This setting controls whether or not the client will accept this instruction from the server. When false (the default) the client will not allow the fallback to SIMPLE authentication and will abort the connection. "
		);

		
		private String key;
		private String value;
		private String description;
		
		private Core(String key, String value, String description) {
			this.key = key;
			this.value = value;
			this.description = description;
		}

		public String key() {
			return key;
		}

		public String value() {
			return value;
		}

		public String description() {
			return description;
		}
		
	}

	enum HDFS {
		dfs_namenode_logging_level
		(
		"dfs.namenode.logging.level",
		"info",
		"The logging level for dfs namenode. Other values are dir(trace namespace mutations) block(trace block under/over replications and blockcreations/deletions) or all. "
		),

		dfs_namenode_rpc_address
		(
		"dfs.namenode.rpc-address",
		"",
		"RPC address that handles all clients requests. If empty then we'll get the value from fs.default.name. The value of this property will take the form of hdfs://nn-host1:rpc-port. "
		),

		dfs_secondary_http_address
		(
		"dfs.secondary.http.address",
		"0.0.0.0:50090",
		"The secondary namenode http server address and port. If the port is 0 then the server will start on a free port. "
		),

		dfs_datanode_address
		(
		"dfs.datanode.address",
		"0.0.0.0:50010",
		"The datanode server address and port for data transfer. If the port is 0 then the server will start on a free port. "
		),

		dfs_datanode_http_address
		(
		"dfs.datanode.http.address",
		"0.0.0.0:50075",
		"The datanode http server address and port. If the port is 0 then the server will start on a free port. "
		),

		dfs_datanode_ipc_address
		(
		"dfs.datanode.ipc.address",
		"0.0.0.0:50020",
		"The datanode ipc server address and port. If the port is 0 then the server will start on a free port. "
		),

		dfs_datanode_handler_count
		(
		"dfs.datanode.handler.count",
		"3",
		"The number of server threads for the datanode. "
		),

		dfs_http_address
		(
		"dfs.http.address",
		"0.0.0.0:50070",
		"The address and the base port where the dfs namenode web ui will listen on. If the port is 0 then the server will start on a free port. "
		),

		dfs_https_enable
		(
		"dfs.https.enable",
		"false",
		"Decide if HTTPS(SSL) is supported on HDFS "
		),

		dfs_https_need_client_auth
		(
		"dfs.https.need.client.auth",
		"false",
		"Whether SSL client certificate authentication is required "
		),

		dfs_https_server_keystore_resource
		(
		"dfs.https.server.keystore.resource",
		"ssl-server.xml",
		"Resource file from which ssl server keystore information will be extracted "
		),

		dfs_https_client_keystore_resource
		(
		"dfs.https.client.keystore.resource",
		"ssl-client.xml",
		"Resource file from which ssl client keystore information will be extracted "
		),

		dfs_datanode_https_address
		(
		"dfs.datanode.https.address",
		"0.0.0.0:50475",
		""
		),

		dfs_https_address
		(
		"dfs.https.address",
		"0.0.0.0:50470",
		""
		),

		dfs_datanode_dns_interface
		(
		"dfs.datanode.dns.interface",
		"default",
		"The name of the Network Interface from which a data node should report its IP address. "
		),

		dfs_datanode_dns_nameserver
		(
		"dfs.datanode.dns.nameserver",
		"default",
		"The host name or IP address of the name server (DNS) which a DataNode should use to determine the host name used by the NameNode for communication and display purposes. "
		),

		dfs_replication_considerLoad
		(
		"dfs.replication.considerLoad",
		"true",
		"Decide if chooseTarget considers the target's load or not "
		),

		dfs_default_chunk_view_size
		(
		"dfs.default.chunk.view.size",
		"32768",
		"The number of bytes to view for a file on the browser. "
		),

		dfs_datanode_du_reserved
		(
		"dfs.datanode.du.reserved",
		"0",
		"Reserved space in bytes per volume. Always leave this much space free for non dfs use. "
		),

		dfs_name_dir
		(
		"dfs.name.dir",
		"${hadoop.tmp.dir}/dfs/name",
		"Determines where on the local filesystem the DFS name node should store the name table(fsimage). If this is a comma-delimited list of directories then the name table is replicated in all of the directories for redundancy. "
		),

		dfs_name_edits_dir
		(
		"dfs.name.edits.dir",
		"${dfs.name.dir}",
		"Determines where on the local filesystem the DFS name node should store the transaction (edits) file. If this is a comma-delimited list of directories then the transaction file is replicated in all of the directories for redundancy. Default value is same as dfs.name.dir "
		),

		dfs_namenode_edits_toleration_length
		(
		"dfs.namenode.edits.toleration.length",
		"0",
		"The length in bytes that namenode is willing to tolerate when the edit log is corrupted. The edit log toleration feature checks the entire edit log. It computes read length (the length of valid data) corruption length and padding length. In case that corruption length is non-zero the corruption will be tolerated only if the corruption length is less than or equal to the toleration length. For disabling edit log toleration feature set this property to -1. When the feature is disabled the end of edit log will not be checked. In this case namenode will startup normally even if the end of edit log is corrupted. "
		),

		dfs_web_ugi
		(
		"dfs.web.ugi",
		"webuser,webgroup",
		"The user account used by the web interface. Syntax: USERNAME GROUP1 GROUP2 ... "
		),

		dfs_permissions
		(
		"dfs.permissions",
		"true",
		"If true enable permission checking in HDFS. If false permission checking is turned off but all other behavior is unchanged. Switching from one parameter value to the other does not change the mode owner or group of files or directories. "
		),

		dfs_permissions_supergroup
		(
		"dfs.permissions.supergroup",
		"supergroup",
		"The name of the group of super-users. "
		),

		dfs_block_access_token_enable
		(
		"dfs.block.access.token.enable",
		"false",
		"If true access tokens are used as capabilities for accessing datanodes. If false no access tokens are checked on accessing datanodes. "
		),

		dfs_block_access_key_update_interval
		(
		"dfs.block.access.key.update.interval",
		"600",
		"Interval in minutes at which namenode updates its access keys. "
		),

		dfs_block_access_token_lifetime
		(
		"dfs.block.access.token.lifetime",
		"600",
		"The lifetime of access tokens in minutes. "
		),

		dfs_data_dir
		(
		"dfs.data.dir",
		"${hadoop.tmp.dir}/dfs/data",
		"Determines where on the local filesystem an DFS data node should store its blocks. If this is a comma-delimited list of directories then data will be stored in all named directories typically on different devices. Directories that do not exist are ignored. "
		),

		dfs_datanode_data_dir_perm
		(
		"dfs.datanode.data.dir.perm",
		"755",
		"Permissions for the directories on on the local filesystem where the DFS data node store its blocks. The permissions can either be octal or symbolic. "
		),

		dfs_replication
		(
		"dfs.replication",
		"3",
		"Default block replication. The actual number of replications can be specified when the file is created. The default is used if replication is not specified in create time. "
		),

		dfs_replication_max
		(
		"dfs.replication.max",
		"512",
		"Maximal block replication. "
		),

		dfs_replication_min
		(
		"dfs.replication.min",
		"1",
		"Minimal block replication. "
		),

		dfs_block_size
		(
		"dfs.block.size",
		"67108864",
		"The default block size for new files. "
		),

		dfs_df_interval
		(
		"dfs.df.interval",
		"60000",
		"Disk usage statistics refresh interval in msec. "
		),

		dfs_client_block_write_retries
		(
		"dfs.client.block.write.retries",
		"3",
		"The number of retries for writing blocks to the data nodes before we signal failure to the application. "
		),

		dfs_blockreport_intervalMsec
		(
		"dfs.blockreport.intervalMsec",
		"3600000",
		"Determines block reporting interval in milliseconds. "
		),

		dfs_blockreport_initialDelay
		(
		"dfs.blockreport.initialDelay",
		"0",
		"Delay for first block report in seconds. "
		),

		dfs_heartbeat_interval
		(
		"dfs.heartbeat.interval",
		"3",
		"Determines datanode heartbeat interval in seconds. "
		),

		dfs_namenode_handler_count
		(
		"dfs.namenode.handler.count",
		"10",
		"The number of server threads for the namenode. "
		),

		dfs_safemode_threshold_pct
		(
		"dfs.safemode.threshold.pct",
		"0.999f",
		"Specifies the percentage of blocks that should satisfy the minimal replication requirement defined by dfs.replication.min. Values less than or equal to 0 mean not to wait for any particular percentage of blocks before exiting safemode. Values greater than 1 will make safe mode permanent. "
		),

		dfs_namenode_safemode_min_datanodes
		(
		"dfs.namenode.safemode.min.datanodes",
		"0",
		"Specifies the number of datanodes that must be considered alive before the name node exits safemode. Values less than or equal to 0 mean not to take the number of live datanodes into account when deciding whether to remain in safe mode during startup. Values greater than the number of datanodes in the cluster will make safe mode permanent. "
		),

		dfs_safemode_extension
		(
		"dfs.safemode.extension",
		"30000",
		"Determines extension of safe mode in milliseconds after the threshold level is reached. "
		),

		dfs_balance_bandwidthPerSec
		(
		"dfs.balance.bandwidthPerSec",
		"1048576",
		"Specifies the maximum amount of bandwidth that each datanode can utilize for the balancing purpose in term of the number of bytes per second. "
		),

		dfs_hosts
		(
		"dfs.hosts",
		"",
		"Names a file that contains a list of hosts that are permitted to connect to the namenode. The full pathname of the file must be specified. If the value is empty all hosts are permitted. "
		),

		dfs_hosts_exclude
		(
		"dfs.hosts.exclude",
		"",
		"Names a file that contains a list of hosts that are not permitted to connect to the namenode. The full pathname of the file must be specified. If the value is empty no hosts are excluded. "
		),

		dfs_max_objects
		(
		"dfs.max.objects",
		"0",
		"The maximum number of files directories and blocks dfs supports. A value of zero indicates no limit to the number of objects that dfs supports. "
		),

		dfs_namenode_decommission_interval
		(
		"dfs.namenode.decommission.interval",
		"30",
		"Namenode periodicity in seconds to check if decommission is complete. "
		),

		dfs_namenode_decommission_nodes_per_interval
		(
		"dfs.namenode.decommission.nodes.per.interval",
		"5",
		"The number of nodes namenode checks if decommission is complete in each dfs.namenode.decommission.interval. "
		),

		dfs_replication_interval
		(
		"dfs.replication.interval",
		"3",
		"The periodicity in seconds with which the namenode computes repliaction work for datanodes. "
		),

		dfs_access_time_precision
		(
		"dfs.access.time.precision",
		"3600000",
		"The access time for HDFS file is precise upto this value. The default value is 1 hour. Setting a value of 0 disables access times for HDFS. "
		),

		dfs_support_append
		(
		"dfs.support.append",
		"",
		"This option is no longer supported. HBase no longer requires that this option be enabled as sync is now enabled by default. See HADOOP-8230 for additional information. "
		),

		dfs_namenode_delegation_key_update_interval
		(
		"dfs.namenode.delegation.key.update-interval",
		"86400000",
		"The update interval for master key for delegation tokens in the namenode in milliseconds. "
		),

		dfs_namenode_delegation_token_max_lifetime
		(
		"dfs.namenode.delegation.token.max-lifetime",
		"604800000",
		"The maximum lifetime in milliseconds for which a delegation token is valid. "
		),

		dfs_namenode_delegation_token_renew_interval
		(
		"dfs.namenode.delegation.token.renew-interval",
		"86400000",
		"The renewal interval for delegation token in milliseconds. "
		),

		dfs_datanode_failed_volumes_tolerated
		(
		"dfs.datanode.failed.volumes.tolerated",
		"0",
		"The number of volumes that are allowed to fail before a datanode stops offering service. By default any volume failure will cause a datanode to shutdown. "
		),

		dfs_datanode_max_xcievers
		(
		"dfs.datanode.max.xcievers",
		"4096",
		"Specifies the maximum number of threads to use for transferring data in and out of the DN. "
		),

		dfs_datanode_readahead_bytes
		(
		"dfs.datanode.readahead.bytes",
		"4193404",
		"While reading block files if the Hadoop native libraries are available the datanode can use the posix_fadvise system call to explicitly page data into the operating system buffer cache ahead of the current reader's position. This can improve performance especially when disks are highly contended. This configuration specifies the number of bytes ahead of the current read position which the datanode will attempt to read ahead. This feature may be disabled by configuring this property to 0. If the native libraries are not available this configuration has no effect. "
		),

		dfs_datanode_drop_cache_behind_reads
		(
		"dfs.datanode.drop.cache.behind.reads",
		"false",
		"In some workloads the data read from HDFS is known to be significantly large enough that it is unlikely to be useful to cache it in the operating system buffer cache. In this case the DataNode may be configured to automatically purge all data from the buffer cache after it is delivered to the client. This behavior is automatically disabled for workloads which read only short sections of a block (e.g HBase random-IO workloads). This may improve performance for some workloads by freeing buffer cache spage usage for more cacheable data. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_datanode_drop_cache_behind_writes
		(
		"dfs.datanode.drop.cache.behind.writes",
		"false",
		"In some workloads the data written to HDFS is known to be significantly large enough that it is unlikely to be useful to cache it in the operating system buffer cache. In this case the DataNode may be configured to automatically purge all data from the buffer cache after it is written to disk. This may improve performance for some workloads by freeing buffer cache spage usage for more cacheable data. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_datanode_sync_behind_writes
		(
		"dfs.datanode.sync.behind.writes",
		"false",
		"If this configuration is enabled the datanode will instruct the operating system to enqueue all written data to the disk immediately after it is written. This differs from the usual OS policy which may wait for up to 30 seconds before triggering writeback. This may improve performance for some workloads by smoothing the IO profile for data written to disk. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_client_use_datanode_hostname
		(
		"dfs.client.use.datanode.hostname",
		"false",
		"Whether clients should use datanode hostnames when connecting to datanodes. "
		),

		dfs_datanode_use_datanode_hostname
		(
		"dfs.datanode.use.datanode.hostname",
		"false",
		"Whether datanodes should use datanode hostnames when connecting to other datanodes for data transfer. "
		),

		dfs_client_local_interfaces
		(
		"dfs.client.local.interfaces",
		"",
		"A comma separated list of network interface names to use for data transfer between the client and datanodes. When creating a connection to read from or write to a datanode the client chooses one of the specified interfaces at random and binds its socket to the IP of that interface. Individual names may be specified as either an interface name (eg eth0) a subinterface name (eg eth0:0) or an IP address (which may be specified using CIDR notation to match a range of IPs). "
		),

		dfs_image_transfer_bandwidthPerSec
		(
		"dfs.image.transfer.bandwidthPerSec",
		"0",
		"Specifies the maximum amount of bandwidth that can be utilized for image transfer in term of the number of bytes per second. A default value of 0 indicates that throttling is disabled. "
		),

		dfs_webhdfs_enabled
		(
		"dfs.webhdfs.enabled",
		"false",
		"Enable WebHDFS (REST API) in Namenodes and Datanodes. "
		),

		dfs_namenode_kerberos_internal_spnego_principal
		(
		"dfs.namenode.kerberos.internal.spnego.principal",
		"${dfs.web.authentication.kerberos.principal}",
		""
		),

		dfs_secondary_namenode_kerberos_internal_spnego_principal
		(
		"dfs.secondary.namenode.kerberos.internal.spnego.principal",
		"${dfs.web.authentication.kerberos.principal}",
		""
		),

		dfs_namenode_invalidate_work_pct_per_iteration
		(
		"dfs.namenode.invalidate.work.pct.per.iteration",
		"0.32f",
		"*Note*: Advanced property. Change with caution. This determines the percentage amount of block invalidations (deletes) to do over a single DN heartbeat deletion command. The final deletion count is determined by applying this percentage to the number of live nodes in the system. The resultant number is the number of blocks from the deletion list chosen for proper invalidation over a single heartbeat of a single DN. Value should be a positive non-zero percentage in float notation (X.Yf) with 1.0f meaning 100%. "
		),

		dfs_namenode_replication_work_multiplier_per_iteration
		(
		"dfs.namenode.replication.work.multiplier.per.iteration",
		"2",
		"*Note*: Advanced property. Change with caution. This determines the total amount of block transfers to begin in parallel at a DN for replication when such a command list is being sent over a DN heartbeat by the NN. The actual number is obtained by multiplying this multiplier with the total number of live nodes in the cluster. The result number is the number of blocks to begin transfers immediately for per DN heartbeat. This number can be any positive non-zero integer. "
		),

		dfs_namenode_avoid_read_stale_datanode
		(
		"dfs.namenode.avoid.read.stale.datanode",
		"false",
		"Indicate whether or not to avoid reading from stale datanodes whose heartbeat messages have not been received by the namenode for more than a specified time interval. Stale datanodes will be moved to the end of the node list returned for reading. See dfs.namenode.avoid.write.stale.datanode for a similar setting for writes. "
		),

		dfs_namenode_avoid_write_stale_datanode
		(
		"dfs.namenode.avoid.write.stale.datanode",
		"false",
		"Indicate whether or not to avoid writing to stale datanodes whose heartbeat messages have not been received by the namenode for more than a specified time interval. Writes will avoid using stale datanodes unless more than a configured ratio (dfs.namenode.write.stale.datanode.ratio) of datanodes are marked as stale. See dfs.namenode.avoid.read.stale.datanode for a similar setting for reads. "
		),

		dfs_namenode_stale_datanode_interval
		(
		"dfs.namenode.stale.datanode.interval",
		"30000",
		"Default time interval for marking a datanode as stale i.e. if the namenode has not received heartbeat msg from a datanode for more than this time interval the datanode will be marked and treated as stale by default. The stale interval cannot be too small since otherwise this may cause too frequent change of stale states. We thus set a minimum stale interval value (the default value is 3 times of heartbeat interval) and guarantee that the stale interval cannot be less than the minimum value. "
		),

		dfs_namenode_write_stale_datanode_ratio
		(
		"dfs.namenode.write.stale.datanode.ratio",
		"0.5f",
		"When the ratio of number stale datanodes to total datanodes marked is greater than this ratio stop avoiding writing to stale nodes so as to prevent causing hotspots. "
		),

		dfs_datanode_plugins
		(
		"dfs.datanode.plugins",
		"",
		"Comma-separated list of datanode plug-ins to be activated. "
		),

		dfs_namenode_plugins
		(
		"dfs.namenode.plugins",
		"",
		"Comma-separated list of namenode plug-ins to be activated. "
		);


		
		private String key;
		private String value;
		private String description;
		
		private HDFS(String key, String value, String description) {
			this.key = key;
			this.value = value;
			this.description = description;
		}

		public String key() {
			return key;
		}

		public String value() {
			return value;
		}

		public String description() {
			return description;
		}
	}

	enum MapReduce {
		
		hadoop_job_history_location
		(
		"hadoop.job.history.location",
		"",
		"The location where jobtracker history files are stored. The value for this key is treated as a URI meaning that the files can be stored either on HDFS or the local file system. If no value is set here the location defaults to the local file system at file:///${hadoop.log.dir}/history. If the URI is missing a scheme fs.default.name is used for the file system. "
		),

		hadoop_job_history_user_location
		(
		"hadoop.job.history.user.location",
		"",
		"User can specify a location to store the history files of a particular job. If nothing is specified the logs are stored in output directory. The files are stored in _logs/history/ in the directory. User can stop logging by giving the value none. "
		),

		mapred_job_tracker_history_completed_location
		(
		"mapred.job.tracker.history.completed.location",
		"",
		"The completed job history files are stored at this single well known location. If nothing is specified the files are stored at ${hadoop.job.history.location}/done. "
		),

		mapreduce_jobhistory_max_age_ms
		(
		"mapreduce.jobhistory.max-age-ms",
		"2592000000",
		"Job history files older than this many milliseconds will be deleted when the history cleaner runs. Defaults to 2592000000 (30 days). "
		),

		mapreduce_jobhistory_cleaner_interval_ms
		(
		"mapreduce.jobhistory.cleaner.interval-ms",
		"86400000",
		"How often the job history cleaner checks for files to delete in milliseconds. Defaults to 86400000 (one day). Files are only deleted if they are older than mapreduce.jobhistory.max-age-ms. "
		),

		io_sort_factor
		(
		"io.sort.factor",
		"10",
		"The number of streams to merge at once while sorting files. This determines the number of open file handles. "
		),

		io_sort_mb
		(
		"io.sort.mb",
		"100",
		"The total amount of buffer memory to use while sorting files in megabytes. By default gives each merge stream 1MB which should minimize seeks. "
		),

		io_sort_record_percent
		(
		"io.sort.record.percent",
		"0.05",
		"The percentage of io.sort.mb dedicated to tracking record boundaries. Let this value be r io.sort.mb be x. The maximum number of records collected before the collection thread must block is equal to (r * x) / 4 "
		),

		io_sort_spill_percent
		(
		"io.sort.spill.percent",
		"0.80",
		"The soft limit in either the buffer or record collection buffers. Once reached a thread will begin to spill the contents to disk in the background. Note that this does not imply any chunking of data to the spill. A value less than 0.5 is not recommended. "
		),

		io_map_index_skip
		(
		"io.map.index.skip",
		"0",
		"Number of index entries to skip between each entry. Zero by default. Setting this to values larger than zero can facilitate opening large map files using less memory. "
		),

		mapred_job_tracker
		(
		"mapred.job.tracker",
		"local",
		"The host and port that the MapReduce job tracker runs at. If local then jobs are run in-process as a single map and reduce task. "
		),

		mapred_job_tracker_http_address
		(
		"mapred.job.tracker.http.address",
		"0.0.0.0:50030",
		"The job tracker http server address and port the server will listen on. If the port is 0 then the server will start on a free port. "
		),

		mapred_job_tracker_handler_count
		(
		"mapred.job.tracker.handler.count",
		"10",
		"The number of server threads for the JobTracker. This should be roughly 4% of the number of tasktracker nodes. "
		),

		mapred_task_tracker_report_address
		(
		"mapred.task.tracker.report.address",
		"127.0.0.1:0",
		"The interface and port that task tracker server listens on. Since it is only connected to by the tasks it uses the local interface. EXPERT ONLY. Should only be changed if your host does not have the loopback interface. "
		),

		mapred_local_dir
		(
		"mapred.local.dir",
		"${hadoop.tmp.dir}/mapred/local",
		"The local directory where MapReduce stores intermediate data files. May be a comma-separated list of directories on different devices in order to spread disk i/o. Directories that do not exist are ignored. "
		),

		mapred_system_dir
		(
		"mapred.system.dir",
		"${hadoop.tmp.dir}/mapred/system",
		"The directory where MapReduce stores control files. "
		),

		mapreduce_jobtracker_staging_root_dir
		(
		"mapreduce.jobtracker.staging.root.dir",
		"${hadoop.tmp.dir}/mapred/staging",
		"The root of the staging area for users' job files In practice this should be the directory where users' home directories are located (usually /user) "
		),

		mapred_temp_dir
		(
		"mapred.temp.dir",
		"${hadoop.tmp.dir}/mapred/temp",
		"A shared directory for temporary files. "
		),

		mapred_local_dir_minspacestart
		(
		"mapred.local.dir.minspacestart",
		"0",
		"If the space in mapred.local.dir drops under this do not ask for more tasks. Value in bytes. "
		),

		mapred_local_dir_minspacekill
		(
		"mapred.local.dir.minspacekill",
		"0",
		"If the space in mapred.local.dir drops under this do not ask more tasks until all the current ones have finished and cleaned up. Also to save the rest of the tasks we have running kill one of them to clean up some space. Start with the reduce tasks then go with the ones that have finished the least. Value in bytes. "
		),

		mapred_tasktracker_expiry_interval
		(
		"mapred.tasktracker.expiry.interval",
		"600000",
		"Expert: The time-interval in miliseconds after which a tasktracker is declared 'lost' if it doesn't send heartbeats. "
		),

		mapred_tasktracker_resourcecalculatorplugin
		(
		"mapred.tasktracker.resourcecalculatorplugin",
		"",
		"Name of the class whose instance will be used to query resource information on the tasktracker. The class must be an instance of org.apache.hadoop.util.ResourceCalculatorPlugin. If the value is null the tasktracker attempts to use a class appropriate to the platform. Currently the only platform supported is Linux. "
		),

		mapred_tasktracker_taskmemorymanager_monitoring_interval
		(
		"mapred.tasktracker.taskmemorymanager.monitoring-interval",
		"5000",
		"The interval in milliseconds for which the tasktracker waits between two cycles of monitoring its tasks' memory usage. Used only if tasks' memory management is enabled via mapred.tasktracker.tasks.maxmemory. "
		),

		mapred_tasktracker_tasks_sleeptime_before_sigkill
		(
		"mapred.tasktracker.tasks.sleeptime-before-sigkill",
		"5000",
		"The time in milliseconds the tasktracker waits for sending a SIGKILL to a process after it has been sent a SIGTERM. "
		),

		mapred_map_tasks
		(
		"mapred.map.tasks",
		"2",
		"The default number of map tasks per job. Ignored when mapred.job.tracker is local. "
		),

		mapred_reduce_tasks
		(
		"mapred.reduce.tasks",
		"1",
		"The default number of reduce tasks per job. Typically set to 99% of the cluster's reduce capacity so that if a node fails the reduces can still be executed in a single wave. Ignored when mapred.job.tracker is local. "
		),

		mapreduce_tasktracker_outofband_heartbeat
		(
		"mapreduce.tasktracker.outofband.heartbeat",
		"false",
		"Expert: Set this to true to let the tasktracker send an out-of-band heartbeat on task-completion for better latency. "
		),

		mapreduce_tasktracker_outofband_heartbeat_damper
		(
		"mapreduce.tasktracker.outofband.heartbeat.damper",
		"1000000",
		"When out-of-band heartbeats are enabled provides damping to avoid overwhelming the JobTracker if too many out-of-band heartbeats would occur. The damping is calculated such that the heartbeat interval is divided by (T*D + 1) where T is the number of completed tasks and D is the damper value. Setting this to a high value like the default provides no damping -- as soon as any task finishes a heartbeat will be sent. Setting this parameter to 0 is equivalent to disabling the out-of-band heartbeat feature. A value of 1 would indicate that after one task has completed the time to wait before the next heartbeat would be 1/2 the usual time. After two tasks have finished it would be 1/3 the usual time etc. "
		),

		mapred_jobtracker_restart_recover
		(
		"mapred.jobtracker.restart.recover",
		"false",
		"true to enable (job) recovery upon restart false to start afresh "
		),

		mapreduce_job_restart_recover
		(
		"mapreduce.job.restart.recover",
		"true",
		"A per-job override for job recovery. If set to false for a job then job recovery will not be attempted for that job upon restart even if mapred.jobtracker.restart.recover is enabled. Defaults to true so that jobs are recovered by default if mapred.jobtracker.restart.recover is enabled. "
		),

		mapred_jobtracker_job_history_block_size
		(
		"mapred.jobtracker.job.history.block.size",
		"3145728",
		"The block size of the job history file. Since the job recovery uses job history its important to dump job history to disk as soon as possible. Note that this is an expert level parameter. The default value is set to 3 MB. "
		),

		mapreduce_job_split_metainfo_maxsize
		(
		"mapreduce.job.split.metainfo.maxsize",
		"10000000",
		"The maximum permissible size of the split metainfo file. The JobTracker won't attempt to read split metainfo files bigger than the configured value. No limits if set to -1. "
		),

		mapred_jobtracker_taskScheduler
		(
		"mapred.jobtracker.taskScheduler",
		"org.apache.hadoop.mapred.JobQueueTaskScheduler",
		"The class responsible for scheduling the tasks. "
		),

		mapred_jobtracker_nodegroup_aware
		(
		"mapred.jobtracker.nodegroup.aware",
		"false",
		"Identify if jobtracker is aware of nodegroup layer. "
		),

		mapred_jobtracker_jobSchedulable
		(
		"mapred.jobtracker.jobSchedulable",
		"org.apache.hadoop.mapred.JobSchedulable",
		"The class responsible for an entity in FairScheduler that can launch tasks. "
		),

		mapred_jobtracker_taskScheduler_maxRunningTasksPerJob
		(
		"mapred.jobtracker.taskScheduler.maxRunningTasksPerJob",
		"",
		"The maximum number of running tasks for a job before it gets preempted. No limits if undefined. "
		),

		mapred_map_max_attempts
		(
		"mapred.map.max.attempts",
		"4",
		"Expert: The maximum number of attempts per map task. In other words framework will try to execute a map task these many number of times before giving up on it. "
		),

		mapred_reduce_max_attempts
		(
		"mapred.reduce.max.attempts",
		"4",
		"Expert: The maximum number of attempts per reduce task. In other words framework will try to execute a reduce task these many number of times before giving up on it. "
		),

		mapred_reduce_parallel_copies
		(
		"mapred.reduce.parallel.copies",
		"5",
		"The default number of parallel transfers run by reduce during the copy(shuffle) phase. "
		),

		mapreduce_reduce_shuffle_maxfetchfailures
		(
		"mapreduce.reduce.shuffle.maxfetchfailures",
		"10",
		"The maximum number of times a reducer tries to fetch a map output before it reports it. "
		),

		mapreduce_reduce_shuffle_connect_timeout
		(
		"mapreduce.reduce.shuffle.connect.timeout",
		"180000",
		"Expert: The maximum amount of time (in milli seconds) a reduce task spends in trying to connect to a tasktracker for getting map output. "
		),

		mapreduce_reduce_shuffle_read_timeout
		(
		"mapreduce.reduce.shuffle.read.timeout",
		"180000",
		"Expert: The maximum amount of time (in milli seconds) a reduce task waits for map output data to be available for reading after obtaining connection. "
		),

		mapred_task_timeout
		(
		"mapred.task.timeout",
		"600000",
		"The number of milliseconds before a task will be terminated if it neither reads an input writes an output nor updates its status string. "
		),

		mapred_tasktracker_map_tasks_maximum
		(
		"mapred.tasktracker.map.tasks.maximum",
		"2",
		"The maximum number of map tasks that will be run simultaneously by a task tracker. "
		),

		mapred_tasktracker_reduce_tasks_maximum
		(
		"mapred.tasktracker.reduce.tasks.maximum",
		"2",
		"The maximum number of reduce tasks that will be run simultaneously by a task tracker. "
		),

		mapred_jobtracker_completeuserjobs_maximum
		(
		"mapred.jobtracker.completeuserjobs.maximum",
		"100",
		"The maximum number of complete jobs per user to keep around before delegating them to the job history. "
		),

		mapreduce_reduce_input_limit
		(
		"mapreduce.reduce.input.limit",
		"-1",
		"The limit on the input size of the reduce. If the estimated input size of the reduce is greater than this value job is failed. A value of -1 means that there is no limit set. "
		),

		mapred_job_tracker_retiredjobs_cache_size
		(
		"mapred.job.tracker.retiredjobs.cache.size",
		"1000",
		"The number of retired job status to keep in the cache. "
		),

		mapred_job_tracker_jobhistory_lru_cache_size
		(
		"mapred.job.tracker.jobhistory.lru.cache.size",
		"5",
		"The number of job history files loaded in memory. The jobs are loaded when they are first accessed. The cache is cleared based on LRU. "
		),

		mapred_child_java_opts
		(
		"mapred.child.java.opts",
		"-Xmx200m",
		"Java opts for the task tracker child processes. The following symbol if present will be interpolated: @taskid@ is replaced by current TaskID. Any other occurrences of '@' will go unchanged. For example to enable verbose gc logging to a file named for the taskid in /tmp and to set the heap maximum to be a gigabyte pass a 'value' of: -Xmx1024m -verbose:gc -Xloggc:/tmp/@taskid@.gc The configuration variable mapred.child.ulimit can be used to control the maximum virtual memory of the child processes. "
		),

		mapred_child_env
		(
		"mapred.child.env",
		"",
		"User added environment variables for the task tracker child processes. Example : 1) A=foo This will set the env variable A to foo 2) B=$B:c This is inherit tasktracker's B env variable. "
		),

		mapred_child_ulimit
		(
		"mapred.child.ulimit",
		"",
		"The maximum virtual memory in KB of a process launched by the Map-Reduce framework. This can be used to control both the Mapper/Reducer tasks and applications using Hadoop Pipes Hadoop Streaming etc. By default it is left unspecified to let cluster admins control it via limits.conf and other such relevant mechanisms. Note: mapred.child.ulimit must be greater than or equal to the -Xmx passed to JavaVM else the VM might not start. "
		),

		mapred_cluster_map_memory_mb
		(
		"mapred.cluster.map.memory.mb",
		"-1",
		"The size in terms of virtual memory of a single map slot in the Map-Reduce framework used by the scheduler. A job can ask for multiple slots for a single map task via mapred.job.map.memory.mb upto the limit specified by mapred.cluster.max.map.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off. "
		),

		mapred_cluster_reduce_memory_mb
		(
		"mapred.cluster.reduce.memory.mb",
		"-1",
		"The size in terms of virtual memory of a single reduce slot in the Map-Reduce framework used by the scheduler. A job can ask for multiple slots for a single reduce task via mapred.job.reduce.memory.mb upto the limit specified by mapred.cluster.max.reduce.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off. "
		),

		mapred_cluster_max_map_memory_mb
		(
		"mapred.cluster.max.map.memory.mb",
		"-1",
		"The maximum size in terms of virtual memory of a single map task launched by the Map-Reduce framework used by the scheduler. A job can ask for multiple slots for a single map task via mapred.job.map.memory.mb upto the limit specified by mapred.cluster.max.map.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off. "
		),

		mapred_cluster_max_reduce_memory_mb
		(
		"mapred.cluster.max.reduce.memory.mb",
		"-1",
		"The maximum size in terms of virtual memory of a single reduce task launched by the Map-Reduce framework used by the scheduler. A job can ask for multiple slots for a single reduce task via mapred.job.reduce.memory.mb upto the limit specified by mapred.cluster.max.reduce.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off. "
		),

		mapred_job_map_memory_mb
		(
		"mapred.job.map.memory.mb",
		"-1",
		"The size in terms of virtual memory of a single map task for the job. A job can ask for multiple slots for a single map task rounded up to the next multiple of mapred.cluster.map.memory.mb and upto the limit specified by mapred.cluster.max.map.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off iff mapred.cluster.map.memory.mb is also turned off (-1). "
		),

		mapred_job_reduce_memory_mb
		(
		"mapred.job.reduce.memory.mb",
		"-1",
		"The size in terms of virtual memory of a single reduce task for the job. A job can ask for multiple slots for a single map task rounded up to the next multiple of mapred.cluster.reduce.memory.mb and upto the limit specified by mapred.cluster.max.reduce.memory.mb if the scheduler supports the feature. The value of -1 indicates that this feature is turned off iff mapred.cluster.reduce.memory.mb is also turned off (-1). "
		),

		mapred_child_tmp
		(
		"mapred.child.tmp",
		"./tmp",
		"To set the value of tmp directory for map and reduce tasks. If the value is an absolute path it is directly assigned. Otherwise it is prepended with task's working directory. The java tasks are executed with option -Djava.io.tmpdir='the absolute path of the tmp dir'. Pipes and streaming are set with environment variable TMPDIR='the absolute path of the tmp dir' "
		),

		mapred_inmem_merge_threshold
		(
		"mapred.inmem.merge.threshold",
		"1000",
		"The threshold in terms of the number of files for the in-memory merge process. When we accumulate threshold number of files we initiate the in-memory merge and spill to disk. A value of 0 or less than 0 indicates we want to DON'T have any threshold and instead depend only on the ramfs's memory consumption to trigger the merge. "
		),

		mapred_job_shuffle_merge_percent
		(
		"mapred.job.shuffle.merge.percent",
		"0.66",
		"The usage threshold at which an in-memory merge will be initiated expressed as a percentage of the total memory allocated to storing in-memory map outputs as defined by mapred.job.shuffle.input.buffer.percent. "
		),

		mapred_job_shuffle_input_buffer_percent
		(
		"mapred.job.shuffle.input.buffer.percent",
		"0.70",
		"The percentage of memory to be allocated from the maximum heap size to storing map outputs during the shuffle. "
		),

		mapred_job_reduce_input_buffer_percent
		(
		"mapred.job.reduce.input.buffer.percent",
		"0.0",
		"The percentage of memory- relative to the maximum heap size- to retain map outputs during the reduce. When the shuffle is concluded any remaining map outputs in memory must consume less than this threshold before the reduce can begin. "
		),

		mapred_map_tasks_speculative_execution
		(
		"mapred.map.tasks.speculative.execution",
		"true",
		"If true then multiple instances of some map tasks may be executed in parallel. "
		),

		mapred_reduce_tasks_speculative_execution
		(
		"mapred.reduce.tasks.speculative.execution",
		"true",
		"If true then multiple instances of some reduce tasks may be executed in parallel. "
		),

		mapred_job_reuse_jvm_num_tasks
		(
		"mapred.job.reuse.jvm.num.tasks",
		"1",
		"How many tasks to run per jvm. If set to -1 there is no limit. "
		),

		mapred_min_split_size
		(
		"mapred.min.split.size",
		"0",
		"The minimum size chunk that map input should be split into. Note that some file formats may have minimum split sizes that take priority over this setting. "
		),

		mapred_jobtracker_maxtasks_per_job
		(
		"mapred.jobtracker.maxtasks.per.job",
		"-1",
		"The maximum number of tasks for a single job. A value of -1 indicates that there is no maximum. "
		),

		mapred_submit_replication
		(
		"mapred.submit.replication",
		"10",
		"The replication level for submitted job files. This should be around the square root of the number of nodes. "
		),

		mapred_tasktracker_dns_interface
		(
		"mapred.tasktracker.dns.interface",
		"default",
		"The name of the Network Interface from which a task tracker should report its IP address. "
		),

		mapred_tasktracker_dns_nameserver
		(
		"mapred.tasktracker.dns.nameserver",
		"default",
		"The host name or IP address of the name server (DNS) which a TaskTracker should use to determine the host name used by the JobTracker for communication and display purposes. "
		),

		tasktracker_http_threads
		(
		"tasktracker.http.threads",
		"40",
		"The number of worker threads that for the http server. This is used for map output fetching "
		),

		mapred_task_tracker_http_address
		(
		"mapred.task.tracker.http.address",
		"0.0.0.0:50060",
		"The task tracker http server address and port. If the port is 0 then the server will start on a free port. "
		),

		keep_failed_task_files
		(
		"keep.failed.task.files",
		"false",
		"Should the files for failed tasks be kept. This should only be used on jobs that are failing because the storage is never reclaimed. It also prevents the map outputs from being erased from the reduce directory as they are consumed. "
		),

		mapred_output_compress
		(
		"mapred.output.compress",
		"false",
		"Should the job outputs be compressed? "
		),

		mapred_output_compression_type
		(
		"mapred.output.compression.type",
		"RECORD",
		"If the job outputs are to compressed as SequenceFiles how should they be compressed? Should be one of NONE RECORD or BLOCK. "
		),

		mapred_output_compression_codec
		(
		"mapred.output.compression.codec",
		"org.apache.hadoop.io.compress.DefaultCodec",
		"If the job outputs are compressed how should they be compressed? "
		),

		mapred_compress_map_output
		(
		"mapred.compress.map.output",
		"false",
		"Should the outputs of the maps be compressed before being sent across the network. Uses SequenceFile compression. "
		),

		mapred_map_output_compression_codec
		(
		"mapred.map.output.compression.codec",
		"org.apache.hadoop.io.compress.DefaultCodec",
		"If the map outputs are compressed how should they be compressed? "
		),

		map_sort_class
		(
		"map.sort.class",
		"org.apache.hadoop.util.QuickSort",
		"The default sort class for sorting keys. "
		),

		mapred_userlog_limit_kb
		(
		"mapred.userlog.limit.kb",
		"0",
		"The maximum size of user-logs of each task in KB. 0 disables the cap. "
		),

		mapred_userlog_retain_hours
		(
		"mapred.userlog.retain.hours",
		"24",
		"The maximum time in hours for which the user-logs are to be retained after the job completion. "
		),

		mapred_user_jobconf_limit
		(
		"mapred.user.jobconf.limit",
		"5242880",
		"The maximum allowed size of the user jobconf. The default is set to 5 MB "
		),

		mapred_hosts
		(
		"mapred.hosts",
		"",
		"Names a file that contains the list of nodes that may connect to the jobtracker. If the value is empty all hosts are permitted. "
		),

		mapred_hosts_exclude
		(
		"mapred.hosts.exclude",
		"",
		"Names a file that contains the list of hosts that should be excluded by the jobtracker. If the value is empty no hosts are excluded. "
		),

		mapred_heartbeats_in_second
		(
		"mapred.heartbeats.in.second",
		"100",
		"Expert: Approximate number of heart-beats that could arrive at JobTracker in a second. Assuming each RPC can be processed in 10msec the default value is made 100 RPCs in a second. "
		),

		mapred_max_tracker_blacklists
		(
		"mapred.max.tracker.blacklists",
		"4",
		"The number of blacklists for a tasktracker by various jobs after which the tasktracker will be marked as potentially faulty and is a candidate for graylisting across all jobs. (Unlike blacklisting this is advisory; the tracker remains active. However it is reported as graylisted in the web UI with the expectation that chronically graylisted trackers will be manually decommissioned.) This value is tied to mapred.jobtracker.blacklist.fault-timeout-window; faults older than the window width are forgiven so the tracker will recover from transient problems. It will also become healthy after a restart. "
		),

		mapred_jobtracker_blacklist_fault_timeout_window
		(
		"mapred.jobtracker.blacklist.fault-timeout-window",
		"180",
		"The timeout (in minutes) after which per-job tasktracker faults are forgiven. The window is logically a circular buffer of time-interval buckets whose width is defined by mapred.jobtracker.blacklist.fault-bucket-width; when the now pointer moves across a bucket boundary the previous contents (faults) of the new bucket are cleared. In other words the timeout's granularity is determined by the bucket width. "
		),

		mapred_jobtracker_blacklist_fault_bucket_width
		(
		"mapred.jobtracker.blacklist.fault-bucket-width",
		"15",
		"The width (in minutes) of each bucket in the tasktracker fault timeout window. Each bucket is reused in a circular manner after a full timeout-window interval (defined by mapred.jobtracker.blacklist.fault-timeout-window). "
		),

		mapred_max_tracker_failures
		(
		"mapred.max.tracker.failures",
		"4",
		"The number of task-failures on a tasktracker of a given job after which new tasks of that job aren't assigned to it. "
		),

		jobclient_output_filter
		(
		"jobclient.output.filter",
		"FAILED",
		"The filter for controlling the output of the task's userlogs sent to the console of the JobClient. The permissible options are: NONE KILLED FAILED SUCCEEDED and ALL. "
		),

		mapred_job_tracker_persist_jobstatus_active
		(
		"mapred.job.tracker.persist.jobstatus.active",
		"false",
		"Indicates if persistency of job status information is active or not. "
		),

		mapred_job_tracker_persist_jobstatus_hours
		(
		"mapred.job.tracker.persist.jobstatus.hours",
		"0",
		"The number of hours job status information is persisted in DFS. The job status information will be available after it drops of the memory queue and between jobtracker restarts. With a zero value the job status information is not persisted at all in DFS. "
		),

		mapred_job_tracker_persist_jobstatus_dir
		(
		"mapred.job.tracker.persist.jobstatus.dir",
		"/jobtracker/jobsInfo",
		"The directory where the job status information is persisted in a file system to be available after it drops of the memory queue and between jobtracker restarts. "
		),

		mapreduce_job_complete_cancel_delegation_tokens
		(
		"mapreduce.job.complete.cancel.delegation.tokens",
		"true",
		"if false - do not unregister/cancel delegation tokens from renewal because same tokens may be used by spawned jobs "
		),

		mapred_task_profile
		(
		"mapred.task.profile",
		"false",
		"To set whether the system should collect profiler information for some of the tasks in this job? The information is stored in the user log directory. The value is true if task profiling is enabled. "
		),

		mapred_task_profile_maps
		(
		"mapred.task.profile.maps",
		"0-2",
		"To set the ranges of map tasks to profile. mapred.task.profile has to be set to true for the value to be accounted. "
		),

		mapred_task_profile_reduces
		(
		"mapred.task.profile.reduces",
		"0-2",
		"To set the ranges of reduce tasks to profile. mapred.task.profile has to be set to true for the value to be accounted. "
		),

		mapred_line_input_format_linespermap
		(
		"mapred.line.input.format.linespermap",
		"1",
		"Number of lines per split in NLineInputFormat. "
		),

		mapred_skip_attempts_to_start_skipping
		(
		"mapred.skip.attempts.to.start.skipping",
		"2",
		"The number of Task attempts AFTER which skip mode will be kicked off. When skip mode is kicked off the tasks reports the range of records which it will process next to the TaskTracker. So that on failures TT knows which ones are possibly the bad records. On further executions those are skipped. "
		),

		mapred_skip_map_auto_incr_proc_count
		(
		"mapred.skip.map.auto.incr.proc.count",
		"true",
		"The flag which if set to true SkipBadRecords.COUNTER_MAP_PROCESSED_RECORDS is incremented by MapRunner after invoking the map function. This value must be set to false for applications which process the records asynchronously or buffer the input records. For example streaming. In such cases applications should increment this counter on their own. "
		),

		mapred_skip_reduce_auto_incr_proc_count
		(
		"mapred.skip.reduce.auto.incr.proc.count",
		"true",
		"The flag which if set to true SkipBadRecords.COUNTER_REDUCE_PROCESSED_GROUPS is incremented by framework after invoking the reduce function. This value must be set to false for applications which process the records asynchronously or buffer the input records. For example streaming. In such cases applications should increment this counter on their own. "
		),

		mapred_skip_out_dir
		(
		"mapred.skip.out.dir",
		"",
		"If no value is specified here the skipped records are written to the output directory at _logs/skip. User can stop writing skipped records by giving the value none. "
		),

		mapred_skip_map_max_skip_records
		(
		"mapred.skip.map.max.skip.records",
		"0",
		"The number of acceptable skip records surrounding the bad record PER bad record in mapper. The number includes the bad record as well. To turn the feature of detection/skipping of bad records off set the value to 0. The framework tries to narrow down the skipped range by retrying until this threshold is met OR all attempts get exhausted for this task. Set the value to Long.MAX_VALUE to indicate that framework need not try to narrow down. Whatever records(depends on application) get skipped are acceptable. "
		),

		mapred_skip_reduce_max_skip_groups
		(
		"mapred.skip.reduce.max.skip.groups",
		"0",
		"The number of acceptable skip groups surrounding the bad group PER bad group in reducer. The number includes the bad group as well. To turn the feature of detection/skipping of bad groups off set the value to 0. The framework tries to narrow down the skipped range by retrying until this threshold is met OR all attempts get exhausted for this task. Set the value to Long.MAX_VALUE to indicate that framework need not try to narrow down. Whatever groups(depends on application) get skipped are acceptable. "
		),

		mapreduce_ifile_readahead
		(
		"mapreduce.ifile.readahead",
		"true",
		"Configuration key to enable/disable IFile readahead. "
		),

		mapreduce_ifile_readahead_bytes
		(
		"mapreduce.ifile.readahead.bytes",
		"4194304",
		"Configuration key to set the IFile readahead length in bytes. "
		),

		job_end_retry_attempts
		(
		"job.end.retry.attempts",
		"0",
		"Indicates how many times hadoop should attempt to contact the notification URL "
		),

		job_end_retry_interval
		(
		"job.end.retry.interval",
		"30000",
		"Indicates time in milliseconds between notification URL retry calls "
		),

		hadoop_rpc_socket_factory_class_JobSubmissionProtocol
		(
		"hadoop.rpc.socket.factory.class.JobSubmissionProtocol",
		"",
		"SocketFactory to use to connect to a Map/Reduce master (JobTracker). If null or empty then use hadoop.rpc.socket.class.default. "
		),

		mapred_task_cache_levels
		(
		"mapred.task.cache.levels",
		"2",
		"This is the max level of the task cache. For example if the level is 2 the tasks cached are at the host level and at the rack level. "
		),

		mapred_queue_names
		(
		"mapred.queue.names",
		"default",
		"Comma separated list of queues configured for this jobtracker. Jobs are added to queues and schedulers can configure different scheduling properties for the various queues. To configure a property for a queue the name of the queue must match the name specified in this value. Queue properties that are common to all schedulers are configured here with the naming convention mapred.queue.$QUEUE-NAME.$PROPERTY-NAME for e.g. mapred.queue.default.submit-job-acl. The number of queues configured in this parameter could depend on the type of scheduler being used as specified in mapred.jobtracker.taskScheduler. For example the JobQueueTaskScheduler supports only a single queue which is the default configured here. Before adding more queues ensure that the scheduler you've configured supports multiple queues. "
		),

		mapred_acls_enabled
		(
		"mapred.acls.enabled",
		"false",
		"Specifies whether ACLs should be checked for authorization of users for doing various queue and job level operations. ACLs are disabled by default. If enabled access control checks are made by JobTracker and TaskTracker when requests are made by users for queue operations like submit job to a queue and kill a job in the queue and job operations like viewing the job-details (See mapreduce.job.acl-view-job) or for modifying the job (See mapreduce.job.acl-modify-job) using Map/Reduce APIs RPCs or via the console and web user interfaces. "
		),

		mapred_queue_default_state
		(
		"mapred.queue.default.state",
		"RUNNING",
		"This values defines the state default queue is in. the values can be either STOPPED or RUNNING This value can be changed at runtime. "
		),

		mapred_job_queue_name
		(
		"mapred.job.queue.name",
		"default",
		"Queue to which a job is submitted. This must match one of the queues defined in mapred.queue.names for the system. Also the ACL setup for the queue must allow the current user to submit a job to the queue. Before specifying a queue ensure that the system is configured with the queue and access is allowed for submitting jobs to the queue. "
		),

		mapreduce_job_acl_modify_job
		(
		"mapreduce.job.acl-modify-job",
		"",
		"Job specific access-control list for 'modifying' the job. It is only used if authorization is enabled in Map/Reduce by setting the configuration property mapred.acls.enabled to true. This specifies the list of users and/or groups who can do modification operations on the job. For specifying a list of users and groups the format to use is user1 user2 group1 group. If set to '*' it allows all users/groups to modify this job. If set to ' '(i.e. space) it allows none. This configuration is used to guard all the modifications with respect to this job and takes care of all the following operations: o killing this job o killing a task of this job failing a task of this job o setting the priority of this job Each of these operations are also protected by the per-queue level ACL acl-administer-jobs configured via mapred-queues.xml. So a caller should have the authorization to satisfy either the queue-level ACL or the job-level ACL. Irrespective of this ACL configuration job-owner the user who started the cluster cluster administrators configured via mapreduce.cluster.administrators and queue administrators of the queue to which this job is submitted to configured via mapred.queue.queue-name.acl-administer-jobs in mapred-queue-acls.xml can do all the modification operations on a job. By default nobody else besides job-owner the user who started the cluster cluster administrators and queue administrators can perform modification operations on a job. "
		),

		mapreduce_job_acl_view_job
		(
		"mapreduce.job.acl-view-job",
		"",
		"Job specific access-control list for 'viewing' the job. It is only used if authorization is enabled in Map/Reduce by setting the configuration property mapred.acls.enabled to true. This specifies the list of users and/or groups who can view private details about the job. For specifying a list of users and groups the format to use is user1 user2 group1 group. If set to '*' it allows all users/groups to modify this job. If set to ' '(i.e. space) it allows none. This configuration is used to guard some of the job-views and at present only protects APIs that can return possibly sensitive information of the job-owner like o job-level counters o task-level counters o tasks' diagnostic information o task-logs displayed on the TaskTracker web-UI and o job.xml showed by the JobTracker's web-UI Every other piece of information of jobs is still accessible by any other user for e.g. JobStatus JobProfile list of jobs in the queue etc. Irrespective of this ACL configuration job-owner the user who started the cluster cluster administrators configured via mapreduce.cluster.administrators and queue administrators of the queue to which this job is submitted to configured via mapred.queue.queue-name.acl-administer-jobs in mapred-queue-acls.xml can do all the view operations on a job. By default nobody else besides job-owner the user who started the cluster cluster administrators and queue administrators can perform view operations on a job. "
		),

		mapred_tasktracker_indexcache_mb
		(
		"mapred.tasktracker.indexcache.mb",
		"10",
		"The maximum memory that a task tracker allows for the index cache that is used when serving map outputs to reducers. "
		),

		mapred_combine_recordsBeforeProgress
		(
		"mapred.combine.recordsBeforeProgress",
		"10000",
		"The number of records to process during combine output collection before sending a progress notification to the TaskTracker. "
		),

		mapred_merge_recordsBeforeProgress
		(
		"mapred.merge.recordsBeforeProgress",
		"10000",
		"The number of records to process during merge before sending a progress notification to the TaskTracker. "
		),

		mapred_reduce_slowstart_completed_maps
		(
		"mapred.reduce.slowstart.completed.maps",
		"0.05",
		"Fraction of the number of maps in the job which should be complete before reduces are scheduled for the job. "
		),

		mapred_task_tracker_task_controller
		(
		"mapred.task.tracker.task-controller",
		"org.apache.hadoop.mapred.DefaultTaskController",
		"TaskController which is used to launch and manage task execution "
		),

		mapreduce_tasktracker_group
		(
		"mapreduce.tasktracker.group",
		"",
		"Expert: Group to which TaskTracker belongs. If LinuxTaskController is configured via mapreduce.tasktracker.taskcontroller the group owner of the task-controller binary should be same as this group. "
		),

		mapred_disk_healthChecker_interval
		(
		"mapred.disk.healthChecker.interval",
		"60000",
		"How often the TaskTracker checks the health of its local directories. Configuring this to a value smaller than the heartbeat interval is equivalent to setting this to heartbeat interval value. "
		),

		mapred_healthChecker_script_path
		(
		"mapred.healthChecker.script.path",
		"",
		"Absolute path to the script which is periodicallyrun by the node health monitoring service to determine if the node is healthy or not. If the value of this key is empty or the file does not exist in the location configured here the node health monitoring service is not started. "
		),

		mapred_healthChecker_interval
		(
		"mapred.healthChecker.interval",
		"60000",
		"Frequency of the node health script to be run in milliseconds "
		),

		mapred_healthChecker_script_timeout
		(
		"mapred.healthChecker.script.timeout",
		"600000",
		"Time after node health script should be killed if unresponsive and considered that the script has failed. "
		),

		mapred_healthChecker_script_args
		(
		"mapred.healthChecker.script.args",
		"",
		"List of arguments which are to be passed to node health script when it is being launched comma seperated. "
		),

		mapreduce_job_counters_max
		(
		"mapreduce.job.counters.max",
		"120",
		"Limit on the number of counters allowed per job. "
		),

		mapreduce_job_counters_groups_max
		(
		"mapreduce.job.counters.groups.max",
		"50",
		"Limit on the number of counter groups allowed per job. "
		),

		mapreduce_job_counters_counter_name_max
		(
		"mapreduce.job.counters.counter.name.max",
		"64",
		"Limit on the length of counter names in jobs. Names exceeding this limit will be truncated. "
		),

		mapreduce_job_counters_group_name_max
		(
		"mapreduce.job.counters.group.name.max",
		"128",
		"Limit on the length of counter group names in jobs. Names exceeding this limit will be truncated. "
		);

		private String key;
		private String value;
		private String description;
		
		private MapReduce(String key, String value, String description) {
			this.key = key;
			this.value = value;
			this.description = description;
		}

		public String key() {
			return key;
		}

		public String value() {
			return value;
		}

		public String description() {
			return description;
		}
	}

}
