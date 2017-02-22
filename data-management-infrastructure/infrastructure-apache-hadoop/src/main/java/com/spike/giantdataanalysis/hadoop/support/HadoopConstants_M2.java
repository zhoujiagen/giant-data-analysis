package com.spike.giantdataanalysis.hadoop.support;

//@formatter:off
/**
 * Hadoop V2版本中的常量
 * 
 * @author zhoujiagen
 *
 */
public interface HadoopConstants_M2 {
	enum Core {
		hadoop_common_configuration_version
		(
		"hadoop.common.configuration.version",
		"0.23.0",
		"version of this configuration file "
		),

		hadoop_tmp_dir
		(
		"hadoop.tmp.dir",
		"/tmp/hadoop-${user.name}",
		"A base for other temporary directories. "
		),

		io_native_lib_available
		(
		"io.native.lib.available",
		"true",
		"Controls whether to use native libraries for bz2 and zlib compression codecs or not. The property does not control any other native libraries. "
		),

		hadoop_http_filter_initializers
		(
		"hadoop.http.filter.initializers",
		"org.apache.hadoop.http.lib.StaticUserWebFilter",
		"A comma separated list of class names. Each class in the list must extend org.apache.hadoop.http.FilterInitializer. The corresponding Filter will be initialized. Then the Filter will be applied to all user facing jsp and servlet web pages. The ordering of the list defines the ordering of the filters. "
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

		hadoop_security_group_mapping
		(
		"hadoop.security.group.mapping",
		"org.apache.hadoop.security.JniBasedUnixGroupsMappingWithFallback",
		"Class for user to group mapping (get groups for a given user) for ACL. The default implementation org.apache.hadoop.security.JniBasedUnixGroupsMappingWithFallback will determine if the Java Native Interface (JNI) is available. If JNI is available the implementation will use the API within hadoop to resolve a list of groups for a user. If JNI is not available then the shell implementation ShellBasedUnixGroupsMapping is used. This implementation shells out to the Linux/Unix environment with the command to resolve a list of groups for a user. "
		),

		hadoop_security_groups_cache_secs
		(
		"hadoop.security.groups.cache.secs",
		"300",
		"This is the config controlling the validity of the entries in the cache containing the user->group mapping. When this duration has expired then the implementation of the group mapping provider is invoked to get the groups of the user and then cached back. "
		),

		hadoop_security_groups_negative_cache_secs
		(
		"hadoop.security.groups.negative-cache.secs",
		"30",
		"Expiration time for entries in the the negative user-to-group mapping caching in seconds. This is useful when invalid users are retrying frequently. It is suggested to set a small value for this expiration since a transient error in group lookup could temporarily lock out a legitimate user. Set this to zero or negative value to disable negative user-to-group caching. "
		),

		hadoop_security_groups_cache_warn_after_ms
		(
		"hadoop.security.groups.cache.warn.after.ms",
		"5000",
		"If looking up a single user to group takes longer than this amount of milliseconds we will log a warning message. "
		),

		hadoop_security_group_mapping_ldap_url
		(
		"hadoop.security.group.mapping.ldap.url",
		"",
		"The URL of the LDAP server to use for resolving user groups when using the LdapGroupsMapping user to group mapping. "
		),

		hadoop_security_group_mapping_ldap_ssl
		(
		"hadoop.security.group.mapping.ldap.ssl",
		"false",
		"Whether or not to use SSL when connecting to the LDAP server. "
		),

		hadoop_security_group_mapping_ldap_ssl_keystore
		(
		"hadoop.security.group.mapping.ldap.ssl.keystore",
		"",
		"File path to the SSL keystore that contains the SSL certificate required by the LDAP server. "
		),

		hadoop_security_group_mapping_ldap_ssl_keystore_password_file
		(
		"hadoop.security.group.mapping.ldap.ssl.keystore.password.file",
		"",
		"The path to a file containing the password of the LDAP SSL keystore. IMPORTANT: This file should be readable only by the Unix user running the daemons. "
		),

		hadoop_security_group_mapping_ldap_bind_user
		(
		"hadoop.security.group.mapping.ldap.bind.user",
		"",
		"The distinguished name of the user to bind as when connecting to the LDAP server. This may be left blank if the LDAP server supports anonymous binds. "
		),

		hadoop_security_group_mapping_ldap_bind_password_file
		(
		"hadoop.security.group.mapping.ldap.bind.password.file",
		"",
		"The path to a file containing the password of the bind user. IMPORTANT: This file should be readable only by the Unix user running the daemons. "
		),

		hadoop_security_group_mapping_ldap_base
		(
		"hadoop.security.group.mapping.ldap.base",
		"",
		"The search base for the LDAP connection. This is a distinguished name and will typically be the root of the LDAP directory. "
		),

		hadoop_security_group_mapping_ldap_search_filter_user
		(
		"hadoop.security.group.mapping.ldap.search.filter.user",
		"(&(objectClass=user)(sAMAccountName={0}))",
		"An additional filter to use when searching for LDAP users. The default will usually be appropriate for Active Directory installations. If connecting to an LDAP server with a non-AD schema this should be replaced with (&(objectClass=inetOrgPerson)(uid={0}). {0} is a special string used to denote where the username fits into the filter. "
		),

		hadoop_security_group_mapping_ldap_search_filter_group
		(
		"hadoop.security.group.mapping.ldap.search.filter.group",
		"(objectClass=group)",
		"An additional filter to use when searching for LDAP groups. This should be changed when resolving groups against a non-Active Directory installation. posixGroups are currently not a supported group class. "
		),

		hadoop_security_group_mapping_ldap_search_attr_member
		(
		"hadoop.security.group.mapping.ldap.search.attr.member",
		"member",
		"The attribute of the group object that identifies the users that are members of the group. The default will usually be appropriate for any LDAP installation. "
		),

		hadoop_security_group_mapping_ldap_search_attr_group_name
		(
		"hadoop.security.group.mapping.ldap.search.attr.group.name",
		"cn",
		"The attribute of the group object that identifies the group name. The default will usually be appropriate for all LDAP systems. "
		),

		hadoop_security_group_mapping_ldap_directory_search_timeout
		(
		"hadoop.security.group.mapping.ldap.directory.search.timeout",
		"10000",
		"The attribute applied to the LDAP SearchControl properties to set a maximum time limit when searching and awaiting a result. Set to 0 if infinite wait period is desired. Default is 10 seconds. Units in milliseconds. "
		),

		hadoop_security_service_user_name_key
		(
		"hadoop.security.service.user.name.key",
		"",
		"For those cases where the same RPC protocol is implemented by multiple servers this configuration is required for specifying the principal name to use for the service when the client wishes to make an RPC call. "
		),

		hadoop_security_uid_cache_secs
		(
		"hadoop.security.uid.cache.secs",
		"14400",
		"This is the config controlling the validity of the entries in the cache containing the userId to userName and groupId to groupName used by NativeIO getFstat(). "
		),

		hadoop_rpc_protection
		(
		"hadoop.rpc.protection",
		"authentication",
		"A comma-separated list of protection values for secured sasl connections. Possible values are authentication integrity and privacy. authentication means authentication only and no integrity or privacy; integrity implies authentication and integrity are enabled; and privacy implies all of authentication integrity and privacy are enabled. hadoop.security.saslproperties.resolver.class can be used to override the hadoop.rpc.protection for a connection at the server side. "
		),

		hadoop_security_saslproperties_resolver_class
		(
		"hadoop.security.saslproperties.resolver.class",
		"",
		"SaslPropertiesResolver used to resolve the QOP used for a connection. If not specified the full set of values specified in hadoop.rpc.protection is used while determining the QOP used for the connection. If a class is specified then the QOP values returned by the class will be used while determining the QOP used for the connection. "
		),

		hadoop_work_around_non_threadsafe_getpwuid
		(
		"hadoop.work.around.non.threadsafe.getpwuid",
		"false",
		"Some operating systems or authentication modules are known to have broken implementations of getpwuid_r and getpwgid_r such that these calls are not thread-safe. Symptoms of this problem include JVM crashes with a stack trace inside these functions. If your system exhibits this issue enable this configuration parameter to include a lock around the calls as a workaround. An incomplete list of some systems known to have this issue is available at http://wiki.apache.org/hadoop/KnownBrokenPwuidImplementations "
		),

		hadoop_kerberos_kinit_command
		(
		"hadoop.kerberos.kinit.command",
		"kinit",
		"Used to periodically renew Kerberos credentials when provided to Hadoop. The default setting assumes that kinit is in the PATH of users running the Hadoop client. Change this to the absolute path to kinit if this is not the case. "
		),

		hadoop_security_auth_to_local
		(
		"hadoop.security.auth_to_local",
		"",
		"Maps kerberos principals to local user names "
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
		"",
		"A comma-separated list of the compression codec classes that can be used for compression/decompression. In addition to any classes specified with this property (which take precedence) codec classes on the classpath are discovered using a Java ServiceLoader. "
		),

		io_compression_codec_bzip2_library
		(
		"io.compression.codec.bzip2.library",
		"system-native",
		"The native-code library to be used for compression and decompression by the bzip2 codec. This library could be specified either by by name or the full pathname. In the former case the library is located by the dynamic linker usually searching the directories specified in the environment variable LD_LIBRARY_PATH. The value of system-native indicates that the default system library should be used. To indicate that the algorithm should operate entirely in Java specify java-builtin. "
		),

		io_serializations
		(
		"io.serializations",
		"org.apache.hadoop.io.serializer.WritableSerialization,org.apache.hadoop.io.serializer.avro.AvroSpecificSerialization,org.apache.hadoop.io.serializer.avro.AvroReflectSerialization",
		"A list of serialization classes that can be used for obtaining serializers and deserializers. "
		),

		io_seqfile_local_dir
		(
		"io.seqfile.local.dir",
		"${hadoop.tmp.dir}/io/local",
		"The local directory where sequence file stores intermediate data files during merge. May be a comma-separated list of directories on different devices in order to spread disk i/o. Directories that do not exist are ignored. "
		),

		io_map_index_skip
		(
		"io.map.index.skip",
		"0",
		"Number of index entries to skip between each entry. Zero by default. Setting this to values larger than zero can facilitate opening large MapFiles using less memory. "
		),

		io_map_index_interval
		(
		"io.map.index.interval",
		"128",
		"MapFile consist of two files - data file (tuples) and index file (keys). For every io.map.index.interval records written in the data file an entry (record-key data-file-position) is written in the index file. This is to allow for doing binary search later within the index file to look up records by their keys and get their closest positions in the data file. "
		),

		fs_defaultFS
		(
		"fs.defaultFS",
		"file:///",
		"The name of the default file system. A URI whose scheme and authority determine the FileSystem implementation. The uri's scheme determines the config property (fs.SCHEME.impl) naming the FileSystem implementation class. The uri's authority is used to determine the host port etc. for a filesystem. "
		),

		fs_default_name
		(
		"fs.default.name",
		"file:///",
		"Deprecated. Use (fs.defaultFS) property instead "
		),

		fs_trash_interval
		(
		"fs.trash.interval",
		"0",
		"Number of minutes after which the checkpoint gets deleted. If zero the trash feature is disabled. This option may be configured both on the server and the client. If trash is disabled server side then the client side configuration is checked. If trash is enabled on the server side then the value configured on the server is used and the client configuration value is ignored. "
		),

		fs_trash_checkpoint_interval
		(
		"fs.trash.checkpoint.interval",
		"0",
		"Number of minutes between trash checkpoints. Should be smaller or equal to fs.trash.interval. If zero the value is set to the value of fs.trash.interval. Every time the checkpointer runs it creates a new checkpoint out of current and removes checkpoints created more than fs.trash.interval minutes ago. "
		),

		fs_AbstractFileSystem_file_impl
		(
		"fs.AbstractFileSystem.file.impl",
		"org.apache.hadoop.fs.local.LocalFs",
		"The AbstractFileSystem for file: uris. "
		),

		fs_AbstractFileSystem_har_impl
		(
		"fs.AbstractFileSystem.har.impl",
		"org.apache.hadoop.fs.HarFs",
		"The AbstractFileSystem for har: uris. "
		),

		fs_AbstractFileSystem_hdfs_impl
		(
		"fs.AbstractFileSystem.hdfs.impl",
		"org.apache.hadoop.fs.Hdfs",
		"The FileSystem for hdfs: uris. "
		),

		fs_AbstractFileSystem_viewfs_impl
		(
		"fs.AbstractFileSystem.viewfs.impl",
		"org.apache.hadoop.fs.viewfs.ViewFs",
		"The AbstractFileSystem for view file system for viewfs: uris (ie client side mount table:). "
		),

		fs_AbstractFileSystem_ftp_impl
		(
		"fs.AbstractFileSystem.ftp.impl",
		"org.apache.hadoop.fs.ftp.FtpFs",
		"The FileSystem for Ftp: uris. "
		),

		fs_ftp_host
		(
		"fs.ftp.host",
		"0.0.0.0",
		"FTP filesystem connects to this server "
		),

		fs_ftp_host_port
		(
		"fs.ftp.host.port",
		"21",
		"FTP filesystem connects to fs.ftp.host on this port "
		),

		fs_df_interval
		(
		"fs.df.interval",
		"60000",
		"Disk usage statistics refresh interval in msec. "
		),

		fs_du_interval
		(
		"fs.du.interval",
		"600000",
		"File space usage statistics refresh interval in msec. "
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

		fs_swift_impl
		(
		"fs.swift.impl",
		"org.apache.hadoop.fs.swift.snative.SwiftNativeFileSystem",
		"The implementation class of the OpenStack Swift Filesystem "
		),

		fs_automatic_close
		(
		"fs.automatic.close",
		"true",
		"By default FileSystem instances are automatically closed at program exit using a JVM shutdown hook. Setting this property to false disables this behavior. This is an advanced option that should only be used by server applications requiring a more carefully orchestrated shutdown sequence. "
		),

		fs_s3n_block_size
		(
		"fs.s3n.block.size",
		"67108864",
		"Block size to use when reading files using the native S3 filesystem (s3n: URIs). "
		),

		fs_s3n_multipart_uploads_enabled
		(
		"fs.s3n.multipart.uploads.enabled",
		"false",
		"Setting this property to true enables multiple uploads to native S3 filesystem. When uploading a file it is split into blocks if the size is larger than fs.s3n.multipart.uploads.block.size. "
		),

		fs_s3n_multipart_uploads_block_size
		(
		"fs.s3n.multipart.uploads.block.size",
		"67108864",
		"The block size for multipart uploads to native S3 filesystem. Default size is 64MB. "
		),

		fs_s3n_multipart_copy_block_size
		(
		"fs.s3n.multipart.copy.block.size",
		"5368709120",
		"The block size for multipart copy in native S3 filesystem. Default size is 5GB. "
		),

		fs_s3n_server_side_encryption_algorithm
		(
		"fs.s3n.server-side-encryption-algorithm",
		"",
		"Specify a server-side encryption algorithm for S3. The default is NULL and the only other currently allowable value is AES256. "
		),

		fs_s3a_awsAccessKeyId
		(
		"fs.s3a.awsAccessKeyId",
		"",
		"AWS access key ID. Omit for Role-based authentication. "
		),

		fs_s3a_awsSecretAccessKey
		(
		"fs.s3a.awsSecretAccessKey",
		"",
		"AWS secret key. Omit for Role-based authentication. "
		),

		fs_s3a_connection_maximum
		(
		"fs.s3a.connection.maximum",
		"15",
		"Controls the maximum number of simultaneous connections to S3. "
		),

		fs_s3a_connection_ssl_enabled
		(
		"fs.s3a.connection.ssl.enabled",
		"true",
		"Enables or disables SSL connections to S3. "
		),

		fs_s3a_endpoint
		(
		"fs.s3a.endpoint",
		"",
		"AWS S3 endpoint to connect to. An up-to-date list is provided in the AWS Documentation: regions and endpoints. Without this property the standard region (s3.amazonaws.com) is assumed. "
		),

		fs_s3a_proxy_host
		(
		"fs.s3a.proxy.host",
		"",
		"Hostname of the (optional) proxy server for S3 connections. "
		),

		fs_s3a_proxy_port
		(
		"fs.s3a.proxy.port",
		"",
		"Proxy server port. If this property is not set but fs.s3a.proxy.host is port 80 or 443 is assumed (consistent with the value of fs.s3a.connection.ssl.enabled). "
		),

		fs_s3a_proxy_username
		(
		"fs.s3a.proxy.username",
		"",
		"Username for authenticating with proxy server. "
		),

		fs_s3a_proxy_password
		(
		"fs.s3a.proxy.password",
		"",
		"Password for authenticating with proxy server. "
		),

		fs_s3a_proxy_domain
		(
		"fs.s3a.proxy.domain",
		"",
		"Domain for authenticating with proxy server. "
		),

		fs_s3a_proxy_workstation
		(
		"fs.s3a.proxy.workstation",
		"",
		"Workstation for authenticating with proxy server. "
		),

		fs_s3a_attempts_maximum
		(
		"fs.s3a.attempts.maximum",
		"10",
		"How many times we should retry commands on transient errors. "
		),

		fs_s3a_connection_establish_timeout
		(
		"fs.s3a.connection.establish.timeout",
		"5000",
		"Socket connection setup timeout in milliseconds. "
		),

		fs_s3a_connection_timeout
		(
		"fs.s3a.connection.timeout",
		"50000",
		"Socket connection timeout in milliseconds. "
		),

		fs_s3a_paging_maximum
		(
		"fs.s3a.paging.maximum",
		"5000",
		"How many keys to request from S3 when doing directory listings at a time. "
		),

		fs_s3a_threads_max
		(
		"fs.s3a.threads.max",
		"256",
		"Maximum number of concurrent active (part)uploads which each use a thread from the threadpool. "
		),

		fs_s3a_threads_core
		(
		"fs.s3a.threads.core",
		"15",
		"Number of core threads in the threadpool. "
		),

		fs_s3a_threads_keepalivetime
		(
		"fs.s3a.threads.keepalivetime",
		"60",
		"Number of seconds a thread can be idle before being terminated. "
		),

		fs_s3a_max_total_tasks
		(
		"fs.s3a.max.total.tasks",
		"1000",
		"Number of (part)uploads allowed to the queue before blocking additional uploads. "
		),

		fs_s3a_multipart_size
		(
		"fs.s3a.multipart.size",
		"104857600",
		"How big (in bytes) to split upload or copy operations up into. "
		),

		fs_s3a_multipart_threshold
		(
		"fs.s3a.multipart.threshold",
		"2147483647",
		"Threshold before uploads or copies use parallel multipart operations. "
		),

		fs_s3a_acl_default
		(
		"fs.s3a.acl.default",
		"",
		"Set a canned ACL for newly created and copied objects. Value may be private public-read public-read-write authenticated-read log-delivery-write bucket-owner-read or bucket-owner-full-control. "
		),

		fs_s3a_multipart_purge
		(
		"fs.s3a.multipart.purge",
		"false",
		"True if you want to purge existing multipart uploads that may not have been completed/aborted correctly "
		),

		fs_s3a_multipart_purge_age
		(
		"fs.s3a.multipart.purge.age",
		"86400",
		"Minimum age in seconds of multipart uploads to purge "
		),

		fs_s3a_buffer_dir
		(
		"fs.s3a.buffer.dir",
		"${hadoop.tmp.dir}/s3a",
		"Comma separated list of directories that will be used to buffer file uploads to. "
		),

		fs_s3a_fast_upload
		(
		"fs.s3a.fast.upload",
		"false",
		"Upload directly from memory instead of buffering to disk first. Memory usage and parallelism can be controlled as up to fs.s3a.multipart.size memory is consumed for each (part)upload actively uploading (fs.s3a.threads.max) or queueing (fs.s3a.max.total.tasks) "
		),

		fs_s3a_fast_buffer_size
		(
		"fs.s3a.fast.buffer.size",
		"1048576",
		"Size of initial memory buffer in bytes allocated for an upload. No effect if fs.s3a.fast.upload is false. "
		),

		fs_s3a_impl
		(
		"fs.s3a.impl",
		"org.apache.hadoop.fs.s3a.S3AFileSystem",
		"The implementation class of the S3A Filesystem "
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

		ipc_client_connect_retry_interval
		(
		"ipc.client.connect.retry.interval",
		"1000",
		"Indicates the number of milliseconds a client will wait for before retrying to establish a server connection. "
		),

		ipc_client_connect_timeout
		(
		"ipc.client.connect.timeout",
		"20000",
		"Indicates the number of milliseconds a client will wait for the socket to establish a server connection. "
		),

		ipc_client_connect_max_retries_on_timeouts
		(
		"ipc.client.connect.max.retries.on.timeouts",
		"45",
		"Indicates the number of retries a client will make on socket timeout to establish a server connection. "
		),

		ipc_server_listen_queue_size
		(
		"ipc.server.listen.queue.size",
		"128",
		"Indicates the length of the listen queue for servers accepting client connections. "
		),

		hadoop_security_impersonation_provider_class
		(
		"hadoop.security.impersonation.provider.class",
		"",
		"A class which implements ImpersonationProvider interface used to authorize whether one user can impersonate a specific user. If not specified the DefaultImpersonationProvider will be used. If a class is specified then that class will be used to determine the impersonation capability. "
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

		net_topology_node_switch_mapping_impl
		(
		"net.topology.node.switch.mapping.impl",
		"org.apache.hadoop.net.ScriptBasedMapping",
		"The default implementation of the DNSToSwitchMapping. It invokes a script specified in net.topology.script.file.name to resolve node names. If the value for net.topology.script.file.name is not set the default value of DEFAULT_RACK is returned for all node names. "
		),

		net_topology_impl
		(
		"net.topology.impl",
		"org.apache.hadoop.net.NetworkTopology",
		"The default implementation of NetworkTopology which is classic three layer one. "
		),

		net_topology_script_file_name
		(
		"net.topology.script.file.name",
		"",
		"The script name that should be invoked to resolve DNS names to NetworkTopology names. Example: the script would take host.foo.bar as an argument and return /rack1 as the output. "
		),

		net_topology_script_number_args
		(
		"net.topology.script.number.args",
		"100",
		"The max number of args that the script configured with net.topology.script.file.name should be run with. Each arg is an IP address. "
		),

		net_topology_table_file_name
		(
		"net.topology.table.file.name",
		"",
		"The file name for a topology file which is used when the net.topology.node.switch.mapping.impl property is set to org.apache.hadoop.net.TableMapping. The file format is a two column text file with columns separated by whitespace. The first column is a DNS or IP address and the second column specifies the rack where the address maps. If no entry corresponding to a host in the cluster is found then /default-rack is assumed. "
		),

		file_stream_buffer_size
		(
		"file.stream-buffer-size",
		"4096",
		"The size of buffer to stream files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		file_bytes_per_checksum
		(
		"file.bytes-per-checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than file.stream-buffer-size "
		),

		file_client_write_packet_size
		(
		"file.client-write-packet-size",
		"65536",
		"Packet size for clients to write "
		),

		file_blocksize
		(
		"file.blocksize",
		"67108864",
		"Block size "
		),

		file_replication
		(
		"file.replication",
		"1",
		"Replication factor "
		),

		s3_stream_buffer_size
		(
		"s3.stream-buffer-size",
		"4096",
		"The size of buffer to stream files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		s3_bytes_per_checksum
		(
		"s3.bytes-per-checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than s3.stream-buffer-size "
		),

		s3_client_write_packet_size
		(
		"s3.client-write-packet-size",
		"65536",
		"Packet size for clients to write "
		),

		s3_blocksize
		(
		"s3.blocksize",
		"67108864",
		"Block size "
		),

		s3_replication
		(
		"s3.replication",
		"3",
		"Replication factor "
		),

		s3native_stream_buffer_size
		(
		"s3native.stream-buffer-size",
		"4096",
		"The size of buffer to stream files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		s3native_bytes_per_checksum
		(
		"s3native.bytes-per-checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than s3native.stream-buffer-size "
		),

		s3native_client_write_packet_size
		(
		"s3native.client-write-packet-size",
		"65536",
		"Packet size for clients to write "
		),

		s3native_blocksize
		(
		"s3native.blocksize",
		"67108864",
		"Block size "
		),

		s3native_replication
		(
		"s3native.replication",
		"3",
		"Replication factor "
		),

		ftp_stream_buffer_size
		(
		"ftp.stream-buffer-size",
		"4096",
		"The size of buffer to stream files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		ftp_bytes_per_checksum
		(
		"ftp.bytes-per-checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than ftp.stream-buffer-size "
		),

		ftp_client_write_packet_size
		(
		"ftp.client-write-packet-size",
		"65536",
		"Packet size for clients to write "
		),

		ftp_blocksize
		(
		"ftp.blocksize",
		"67108864",
		"Block size "
		),

		ftp_replication
		(
		"ftp.replication",
		"3",
		"Replication factor "
		),

		tfile_io_chunk_size
		(
		"tfile.io.chunk.size",
		"1048576",
		"Value chunk size in bytes. Default to 1MB. Values of the length less than the chunk size is guaranteed to have known value length in read time (See also TFile.Reader.Scanner.Entry.isValueLengthKnown()). "
		),

		tfile_fs_output_buffer_size
		(
		"tfile.fs.output.buffer.size",
		"262144",
		"Buffer size used for FSDataOutputStream in bytes. "
		),

		tfile_fs_input_buffer_size
		(
		"tfile.fs.input.buffer.size",
		"262144",
		"Buffer size used for FSDataInputStream in bytes. "
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
		"The signature secret for signing the authentication tokens. The same secret should be used for JT/NN/DN/TT configurations. "
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
		"HTTP/_HOST@LOCALHOST",
		"Indicates the Kerberos principal to be used for HTTP endpoint. The principal MUST start with 'HTTP/' as per Kerberos HTTP SPNEGO specification. "
		),

		hadoop_http_authentication_kerberos_keytab
		(
		"hadoop.http.authentication.kerberos.keytab",
		"${user.home}/hadoop.keytab",
		"Location of the keytab file with the credentials for the principal. Referring to the same keytab file Oozie uses for its Kerberos credentials for Hadoop. "
		),

		dfs_ha_fencing_methods
		(
		"dfs.ha.fencing.methods",
		"",
		"List of fencing methods to use for service fencing. May contain builtin methods (eg shell and sshfence) or user-defined method. "
		),

		dfs_ha_fencing_ssh_connect_timeout
		(
		"dfs.ha.fencing.ssh.connect-timeout",
		"30000",
		"SSH connection timeout in milliseconds to use with the builtin sshfence fencer. "
		),

		dfs_ha_fencing_ssh_private_key_files
		(
		"dfs.ha.fencing.ssh.private-key-files",
		"",
		"The SSH private key files to use with the builtin sshfence fencer. "
		),

		hadoop_http_staticuser_user
		(
		"hadoop.http.staticuser.user",
		"dr.who",
		"The user name to filter as on static web filters while rendering content. An example use is the HDFS web UI (user to be used for browsing files). "
		),

		ha_zookeeper_quorum
		(
		"ha.zookeeper.quorum",
		"",
		"A list of ZooKeeper server addresses separated by commas that are to be used by the ZKFailoverController in automatic failover. "
		),

		ha_zookeeper_session_timeout_ms
		(
		"ha.zookeeper.session-timeout.ms",
		"5000",
		"The session timeout to use when the ZKFC connects to ZooKeeper. Setting this value to a lower value implies that server crashes will be detected more quickly but risks triggering failover too aggressively in the case of a transient error or network blip. "
		),

		ha_zookeeper_parent_znode
		(
		"ha.zookeeper.parent-znode",
		"/hadoop-ha",
		"The ZooKeeper znode under which the ZK failover controller stores its information. Note that the nameservice ID is automatically appended to this znode so it is not normally necessary to configure this even in a federated environment. "
		),

		ha_zookeeper_acl
		(
		"ha.zookeeper.acl",
		"world:anyone:rwcda",
		"A comma-separated list of ZooKeeper ACLs to apply to the znodes used by automatic failover. These ACLs are specified in the same format as used by the ZooKeeper CLI. If the ACL itself contains secrets you may instead specify a path to a file prefixed with the '@' symbol and the value of this configuration will be loaded from within. "
		),

		ha_zookeeper_auth
		(
		"ha.zookeeper.auth",
		"",
		"A comma-separated list of ZooKeeper authentications to add when connecting to ZooKeeper. These are specified in the same format as used by the addauth command in the ZK CLI. It is important that the authentications specified here are sufficient to access znodes with the ACL specified in ha.zookeeper.acl. If the auths contain secrets you may instead specify a path to a file prefixed with the '@' symbol and the value of this configuration will be loaded from within. "
		),

		hadoop_ssl_keystores_factory_class
		(
		"hadoop.ssl.keystores.factory.class",
		"org.apache.hadoop.security.ssl.FileBasedKeyStoresFactory",
		"The keystores factory to use for retrieving certificates. "
		),

		hadoop_ssl_require_client_cert
		(
		"hadoop.ssl.require.client.cert",
		"false",
		"Whether client certificates are required "
		),

		hadoop_ssl_hostname_verifier
		(
		"hadoop.ssl.hostname.verifier",
		"DEFAULT",
		"The hostname verifier to provide for HttpsURLConnections. Valid values are: DEFAULT STRICT STRICT_I6 DEFAULT_AND_LOCALHOST and ALLOW_ALL "
		),

		hadoop_ssl_server_conf
		(
		"hadoop.ssl.server.conf",
		"ssl-server.xml",
		"Resource file from which ssl server keystore information will be extracted. This file is looked up in the classpath typically it should be in Hadoop conf/ directory. "
		),

		hadoop_ssl_client_conf
		(
		"hadoop.ssl.client.conf",
		"ssl-client.xml",
		"Resource file from which ssl client keystore information will be extracted This file is looked up in the classpath typically it should be in Hadoop conf/ directory. "
		),

		hadoop_ssl_enabled
		(
		"hadoop.ssl.enabled",
		"false",
		"Deprecated. Use dfs.http.policy and yarn.http.policy instead. "
		),

		hadoop_ssl_enabled_protocols
		(
		"hadoop.ssl.enabled.protocols",
		"TLSv1",
		"Protocols supported by the ssl. "
		),

		hadoop_jetty_logs_serve_aliases
		(
		"hadoop.jetty.logs.serve.aliases",
		"true",
		"Enable/Disable aliases serving from jetty "
		),

		fs_permissions_umask_mode
		(
		"fs.permissions.umask-mode",
		"022",
		"The umask used when creating files and directories. Can be in octal or in symbolic. Examples are: 022 (octal for u=rwx g=r-x o=r-x in symbolic) or u=rwx g=rwx o= (symbolic for 007 in octal). "
		),

		ha_health_monitor_connect_retry_interval_ms
		(
		"ha.health-monitor.connect-retry-interval.ms",
		"1000",
		"How often to retry connecting to the service. "
		),

		ha_health_monitor_check_interval_ms
		(
		"ha.health-monitor.check-interval.ms",
		"1000",
		"How often to check the service. "
		),

		ha_health_monitor_sleep_after_disconnect_ms
		(
		"ha.health-monitor.sleep-after-disconnect.ms",
		"1000",
		"How long to sleep after an unexpected RPC error. "
		),

		ha_health_monitor_rpc_timeout_ms
		(
		"ha.health-monitor.rpc-timeout.ms",
		"45000",
		"Timeout for the actual monitorHealth() calls. "
		),

		ha_failover_controller_new_active_rpc_timeout_ms
		(
		"ha.failover-controller.new-active.rpc-timeout.ms",
		"60000",
		"Timeout that the FC waits for the new active to become active "
		),

		ha_failover_controller_graceful_fence_rpc_timeout_ms
		(
		"ha.failover-controller.graceful-fence.rpc-timeout.ms",
		"5000",
		"Timeout that the FC waits for the old active to go to standby "
		),

		ha_failover_controller_graceful_fence_connection_retries
		(
		"ha.failover-controller.graceful-fence.connection.retries",
		"1",
		"FC connection retries for graceful fencing "
		),

		ha_failover_controller_cli_check_rpc_timeout_ms
		(
		"ha.failover-controller.cli-check.rpc-timeout.ms",
		"20000",
		"Timeout that the CLI (manual) FC waits for monitorHealth getServiceState "
		),

		ipc_client_fallback_to_simple_auth_allowed
		(
		"ipc.client.fallback-to-simple-auth-allowed",
		"false",
		"When a client is configured to attempt a secure connection but attempts to connect to an insecure server that server may instruct the client to switch to SASL SIMPLE (unsecure) authentication. This setting controls whether or not the client will accept this instruction from the server. When false (the default) the client will not allow the fallback to SIMPLE authentication and will abort the connection. "
		),

		fs_client_resolve_remote_symlinks
		(
		"fs.client.resolve.remote.symlinks",
		"true",
		"Whether to resolve symlinks when accessing a remote Hadoop filesystem. Setting this to false causes an exception to be thrown upon encountering a symlink. This setting does not apply to local filesystems which automatically resolve local symlinks. "
		),

		nfs_exports_allowed_hosts
		(
		"nfs.exports.allowed.hosts",
		"* rw",
		"By default the export can be mounted by any client. The value string contains machine name and access privilege separated by whitespace characters. The machine name format can be a single host a Java regular expression or an IPv4 address. The access privilege uses rw or ro to specify read/write or read-only access of the machines to exports. If the access privilege is not provided the default is read-only. Entries are separated by ;. For example: 192.168.0.0/22 rw ; host.*\\.example\\.com ; host1.test.org ro;. Only the NFS gateway needs to restart after this property is updated. "
		),

		hadoop_user_group_static_mapping_overrides
		(
		"hadoop.user.group.static.mapping.overrides",
		"dr.who=;",
		"Static mapping of user to groups. This will override the groups if available in the system for the specified user. In otherwords groups look-up will not happen for these users instead groups mapped in this configuration will be used. Mapping should be in this format. user1=group1 group2;user2=;user3=group2; Default dr.who=; will consider dr.who as user without groups. "
		),

		rpc_metrics_quantile_enable
		(
		"rpc.metrics.quantile.enable",
		"false",
		"Setting this property to true and rpc.metrics.percentiles.intervals to a comma-separated list of the granularity in seconds the 50/75/90/95/99th percentile latency for rpc queue/processing time in milliseconds are added to rpc metrics. "
		),

		rpc_metrics_percentiles_intervals
		(
		"rpc.metrics.percentiles.intervals",
		"",
		"A comma-separated list of the granularity in seconds for the metrics which describe the 50/75/90/95/99th percentile latency for rpc queue/processing time. The metrics are outputted if rpc.metrics.quantile.enable is set to true. "
		),

		hadoop_security_crypto_codec_classes_EXAMPLECIPHERSUITE
		(
		"hadoop.security.crypto.codec.classes.EXAMPLECIPHERSUITE",
		"",
		"The prefix for a given crypto codec contains a comma-separated list of implementation classes for a given crypto codec (eg EXAMPLECIPHERSUITE). The first implementation will be used if available others are fallbacks. "
		),

		hadoop_security_crypto_codec_classes_aes_ctr_nopadding
		(
		"hadoop.security.crypto.codec.classes.aes.ctr.nopadding",
		"org.apache.hadoop.crypto.OpensslAesCtrCryptoCodec,org.apache.hadoop.crypto.JceAesCtrCryptoCodec",
		"Comma-separated list of crypto codec implementations for AES/CTR/NoPadding. The first implementation will be used if available others are fallbacks. "
		),

		hadoop_security_crypto_cipher_suite
		(
		"hadoop.security.crypto.cipher.suite",
		"AES/CTR/NoPadding",
		"Cipher suite for crypto codec. "
		),

		hadoop_security_crypto_jce_provider
		(
		"hadoop.security.crypto.jce.provider",
		"",
		"The JCE provider name used in CryptoCodec. "
		),

		hadoop_security_crypto_buffer_size
		(
		"hadoop.security.crypto.buffer.size",
		"8192",
		"The buffer size used by CryptoInputStream and CryptoOutputStream. "
		),

		hadoop_security_java_secure_random_algorithm
		(
		"hadoop.security.java.secure.random.algorithm",
		"SHA1PRNG",
		"The java secure random algorithm. "
		),

		hadoop_security_secure_random_impl
		(
		"hadoop.security.secure.random.impl",
		"",
		"Implementation of secure random. "
		),

		hadoop_security_random_device_file_path
		(
		"hadoop.security.random.device.file.path",
		"/dev/urandom",
		"OS security random device file path. "
		),

		fs_har_impl_disable_cache
		(
		"fs.har.impl.disable.cache",
		"true",
		"Don't cache 'har' filesystem instances. "
		),

		hadoop_security_kms_client_authentication_retry_count
		(
		"hadoop.security.kms.client.authentication.retry-count",
		"1",
		"Number of time to retry connecting to KMS on authentication failure "
		),

		hadoop_security_kms_client_encrypted_key_cache_size
		(
		"hadoop.security.kms.client.encrypted.key.cache.size",
		"500",
		"Size of the EncryptedKeyVersion cache Queue for each key "
		),

		hadoop_security_kms_client_encrypted_key_cache_low_watermark
		(
		"hadoop.security.kms.client.encrypted.key.cache.low-watermark",
		"0.3f",
		"If size of the EncryptedKeyVersion cache Queue falls below the low watermark this cache queue will be scheduled for a refill "
		),

		hadoop_security_kms_client_encrypted_key_cache_num_refill_threads
		(
		"hadoop.security.kms.client.encrypted.key.cache.num.refill.threads",
		"2",
		"Number of threads to use for refilling depleted EncryptedKeyVersion cache Queues "
		),

		hadoop_security_kms_client_encrypted_key_cache_expiry
		(
		"hadoop.security.kms.client.encrypted.key.cache.expiry",
		"43200000",
		"Cache expiry time for a Key after which the cache Queue for this key will be dropped. Default = 12hrs "
		),

		hadoop_htrace_spanreceiver_classes
		(
		"hadoop.htrace.spanreceiver.classes",
		"",
		"A comma separated list of the fully-qualified class name of classes implementing SpanReceiver. The tracing system works by collecting information in structs called 'Spans'. It is up to you to choose how you want to receive this information by implementing the SpanReceiver interface. "
		),

		ipc_server_max_connections
		(
		"ipc.server.max.connections",
		"0",
		"The maximum number of concurrent connections a server is allowed to accept. If this limit is exceeded incoming connections will first fill the listen queue and then may go to an OS-specific listen overflow queue. The client may fail or timeout but the server can avoid running out of file descriptors using this feature. 0 means no limit. "
		),

		hadoop_registry_rm_enabled
		(
		"hadoop.registry.rm.enabled",
		"false",
		"Is the registry enabled in the YARN Resource Manager? If true the YARN RM will as needed. create the user and system paths and purge service records when containers application attempts and applications complete. If false the paths must be created by other means and no automatic cleanup of service records will take place. "
		),

		hadoop_registry_zk_root
		(
		"hadoop.registry.zk.root",
		"/registry",
		"The root zookeeper node for the registry "
		),

		hadoop_registry_zk_session_timeout_ms
		(
		"hadoop.registry.zk.session.timeout.ms",
		"60000",
		"Zookeeper session timeout in milliseconds "
		),

		hadoop_registry_zk_connection_timeout_ms
		(
		"hadoop.registry.zk.connection.timeout.ms",
		"15000",
		"Zookeeper connection timeout in milliseconds "
		),

		hadoop_registry_zk_retry_times
		(
		"hadoop.registry.zk.retry.times",
		"5",
		"Zookeeper connection retry count before failing "
		),

		hadoop_registry_zk_retry_interval_ms
		(
		"hadoop.registry.zk.retry.interval.ms",
		"1000",
		" "
		),

		hadoop_registry_zk_retry_ceiling_ms
		(
		"hadoop.registry.zk.retry.ceiling.ms",
		"60000",
		"Zookeeper retry limit in milliseconds during exponential backoff. This places a limit even if the retry times and interval limit combined with the backoff policy result in a long retry period "
		),

		hadoop_registry_zk_quorum
		(
		"hadoop.registry.zk.quorum",
		"localhost:2181",
		"List of hostname:port pairs defining the zookeeper quorum binding for the registry "
		),

		hadoop_registry_secure
		(
		"hadoop.registry.secure",
		"false",
		"Key to set if the registry is secure. Turning it on changes the permissions policy from open access to restrictions on kerberos with the option of a user adding one or more auth key pairs down their own tree. "
		),

		hadoop_registry_system_acls
		(
		"hadoop.registry.system.acls",
		"sasl:yarn@,sasl:mapred@,sasl:hdfs@",
		"A comma separated list of Zookeeper ACL identifiers with system access to the registry in a secure cluster. These are given full access to all entries. If there is an @ at the end of a SASL entry it instructs the registry client to append the default kerberos domain. "
		),

		hadoop_registry_kerberos_realm
		(
		"hadoop.registry.kerberos.realm",
		"",
		"The kerberos realm: used to set the realm of system principals which do not declare their realm and any other accounts that need the value. If empty the default realm of the running process is used. If neither are known and the realm is needed then the registry service/client will fail. "
		),

		hadoop_registry_jaas_context
		(
		"hadoop.registry.jaas.context",
		"Client",
		"Key to define the JAAS context. Used in secure mode "
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
		hadoop_hdfs_configuration_version
		(
		"hadoop.hdfs.configuration.version",
		"1",
		"version of this configuration file "
		),

		dfs_namenode_rpc_address
		(
		"dfs.namenode.rpc-address",
		"",
		"RPC address that handles all clients requests. In the case of HA/Federation where multiple namenodes exist the name service id is added to the name e.g. dfs.namenode.rpc-address.ns1 dfs.namenode.rpc-address.EXAMPLENAMESERVICE The value of this property will take the form of nn-host1:rpc-port. "
		),

		dfs_namenode_rpc_bind_host
		(
		"dfs.namenode.rpc-bind-host",
		"",
		"The actual address the RPC server will bind to. If this optional address is set it overrides only the hostname portion of dfs.namenode.rpc-address. It can also be specified per name node or name service for HA/Federation. This is useful for making the name node listen on all interfaces by setting it to 0.0.0.0. "
		),

		dfs_namenode_servicerpc_address
		(
		"dfs.namenode.servicerpc-address",
		"",
		"RPC address for HDFS Services communication. BackupNode Datanodes and all other services should be connecting to this address if it is configured. In the case of HA/Federation where multiple namenodes exist the name service id is added to the name e.g. dfs.namenode.servicerpc-address.ns1 dfs.namenode.rpc-address.EXAMPLENAMESERVICE The value of this property will take the form of nn-host1:rpc-port. If the value of this property is unset the value of dfs.namenode.rpc-address will be used as the default. "
		),

		dfs_namenode_servicerpc_bind_host
		(
		"dfs.namenode.servicerpc-bind-host",
		"",
		"The actual address the service RPC server will bind to. If this optional address is set it overrides only the hostname portion of dfs.namenode.servicerpc-address. It can also be specified per name node or name service for HA/Federation. This is useful for making the name node listen on all interfaces by setting it to 0.0.0.0. "
		),

		dfs_namenode_secondary_http_address
		(
		"dfs.namenode.secondary.http-address",
		"0.0.0.0:50090",
		"The secondary namenode http server address and port. "
		),

		dfs_namenode_secondary_https_address
		(
		"dfs.namenode.secondary.https-address",
		"0.0.0.0:50091",
		"The secondary namenode HTTPS server address and port. "
		),

		dfs_datanode_address
		(
		"dfs.datanode.address",
		"0.0.0.0:50010",
		"The datanode server address and port for data transfer. "
		),

		dfs_datanode_http_address
		(
		"dfs.datanode.http.address",
		"0.0.0.0:50075",
		"The datanode http server address and port. "
		),

		dfs_datanode_ipc_address
		(
		"dfs.datanode.ipc.address",
		"0.0.0.0:50020",
		"The datanode ipc server address and port. "
		),

		dfs_datanode_handler_count
		(
		"dfs.datanode.handler.count",
		"10",
		"The number of server threads for the datanode. "
		),

		dfs_namenode_http_address
		(
		"dfs.namenode.http-address",
		"0.0.0.0:50070",
		"The address and the base port where the dfs namenode web ui will listen on. "
		),

		dfs_namenode_http_bind_host
		(
		"dfs.namenode.http-bind-host",
		"",
		"The actual adress the HTTP server will bind to. If this optional address is set it overrides only the hostname portion of dfs.namenode.http-address. It can also be specified per name node or name service for HA/Federation. This is useful for making the name node HTTP server listen on all interfaces by setting it to 0.0.0.0. "
		),

		dfs_namenode_heartbeat_recheck_interval
		(
		"dfs.namenode.heartbeat.recheck-interval",
		"300000",
		"This time decides the interval to check for expired datanodes. With this value and dfs.heartbeat.interval the interval of deciding the datanode is stale or not is also calculated. The unit of this configuration is millisecond. "
		),

		dfs_http_policy
		(
		"dfs.http.policy",
		"HTTP_ONLY",
		"Decide if HTTPS(SSL) is supported on HDFS This configures the HTTP endpoint for HDFS daemons: The following values are supported: - HTTP_ONLY : Service is provided only on http - HTTPS_ONLY : Service is provided only on https - HTTP_AND_HTTPS : Service is provided both on http and https "
		),

		dfs_client_https_need_auth
		(
		"dfs.client.https.need-auth",
		"false",
		"Whether SSL client certificate authentication is required "
		),

		dfs_client_cached_conn_retry
		(
		"dfs.client.cached.conn.retry",
		"3",
		"The number of times the HDFS client will pull a socket from the cache. Once this number is exceeded the client will try to create a new socket. "
		),

		dfs_https_server_keystore_resource
		(
		"dfs.https.server.keystore.resource",
		"ssl-server.xml",
		"Resource file from which ssl server keystore information will be extracted "
		),

		dfs_client_https_keystore_resource
		(
		"dfs.client.https.keystore.resource",
		"ssl-client.xml",
		"Resource file from which ssl client keystore information will be extracted "
		),

		dfs_datanode_https_address
		(
		"dfs.datanode.https.address",
		"0.0.0.0:50475",
		"The datanode secure http server address and port. "
		),

		dfs_namenode_https_address
		(
		"dfs.namenode.https-address",
		"0.0.0.0:50470",
		"The namenode secure http server address and port. "
		),

		dfs_namenode_https_bind_host
		(
		"dfs.namenode.https-bind-host",
		"",
		"The actual adress the HTTPS server will bind to. If this optional address is set it overrides only the hostname portion of dfs.namenode.https-address. It can also be specified per name node or name service for HA/Federation. This is useful for making the name node HTTPS server listen on all interfaces by setting it to 0.0.0.0. "
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

		dfs_namenode_backup_address
		(
		"dfs.namenode.backup.address",
		"0.0.0.0:50100",
		"The backup node server address and port. If the port is 0 then the server will start on a free port. "
		),

		dfs_namenode_backup_http_address
		(
		"dfs.namenode.backup.http-address",
		"0.0.0.0:50105",
		"The backup node http server address and port. If the port is 0 then the server will start on a free port. "
		),

		dfs_namenode_replication_considerLoad
		(
		"dfs.namenode.replication.considerLoad",
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

		dfs_namenode_name_dir
		(
		"dfs.namenode.name.dir",
		"file://${hadoop.tmp.dir}/dfs/name",
		"Determines where on the local filesystem the DFS name node should store the name table(fsimage). If this is a comma-delimited list of directories then the name table is replicated in all of the directories for redundancy. "
		),

		dfs_namenode_name_dir_restore
		(
		"dfs.namenode.name.dir.restore",
		"false",
		"Set to true to enable NameNode to attempt recovering a previously failed dfs.namenode.name.dir. When enabled a recovery of any failed directory is attempted during checkpoint. "
		),

		dfs_namenode_fs_limits_max_component_length
		(
		"dfs.namenode.fs-limits.max-component-length",
		"255",
		"Defines the maximum number of bytes in UTF-8 encoding in each component of a path. A value of 0 will disable the check. "
		),

		dfs_namenode_fs_limits_max_directory_items
		(
		"dfs.namenode.fs-limits.max-directory-items",
		"1048576",
		"Defines the maximum number of items that a directory may contain. Cannot set the property to a value less than 1 or more than 6400000. "
		),

		dfs_namenode_fs_limits_min_block_size
		(
		"dfs.namenode.fs-limits.min-block-size",
		"1048576",
		"Minimum block size in bytes enforced by the Namenode at create time. This prevents the accidental creation of files with tiny block sizes (and thus many blocks) which can degrade performance. "
		),

		dfs_namenode_fs_limits_max_blocks_per_file
		(
		"dfs.namenode.fs-limits.max-blocks-per-file",
		"1048576",
		"Maximum number of blocks per file enforced by the Namenode on write. This prevents the creation of extremely large files which can degrade performance. "
		),

		dfs_namenode_edits_dir
		(
		"dfs.namenode.edits.dir",
		"${dfs.namenode.name.dir}",
		"Determines where on the local filesystem the DFS name node should store the transaction (edits) file. If this is a comma-delimited list of directories then the transaction file is replicated in all of the directories for redundancy. Default value is same as dfs.namenode.name.dir "
		),

		dfs_namenode_shared_edits_dir
		(
		"dfs.namenode.shared.edits.dir",
		"",
		"A directory on shared storage between the multiple namenodes in an HA cluster. This directory will be written by the active and read by the standby in order to keep the namespaces synchronized. This directory does not need to be listed in dfs.namenode.edits.dir above. It should be left empty in a non-HA cluster. "
		),

		dfs_namenode_edits_journal_plugin_qjournal
		(
		"dfs.namenode.edits.journal-plugin.qjournal",
		"org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager",
		""
		),

		dfs_permissions_enabled
		(
		"dfs.permissions.enabled",
		"true",
		"If true enable permission checking in HDFS. If false permission checking is turned off but all other behavior is unchanged. Switching from one parameter value to the other does not change the mode owner or group of files or directories. "
		),

		dfs_permissions_superusergroup
		(
		"dfs.permissions.superusergroup",
		"supergroup",
		"The name of the group of super-users. "
		),

		dfs_namenode_acls_enabled
		(
		"dfs.namenode.acls.enabled",
		"false",
		"Set to true to enable support for HDFS ACLs (Access Control Lists). By default ACLs are disabled. When ACLs are disabled the NameNode rejects all RPCs related to setting or getting ACLs. "
		),

		dfs_namenode_lazypersist_file_scrub_interval_sec
		(
		"dfs.namenode.lazypersist.file.scrub.interval.sec",
		"300",
		"The NameNode periodically scans the namespace for LazyPersist files with missing blocks and unlinks them from the namespace. This configuration key controls the interval between successive scans. Set it to a negative value to disable this behavior. "
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

		dfs_datanode_data_dir
		(
		"dfs.datanode.data.dir",
		"file://${hadoop.tmp.dir}/dfs/data",
		"Determines where on the local filesystem an DFS data node should store its blocks. If this is a comma-delimited list of directories then data will be stored in all named directories typically on different devices. Directories that do not exist are ignored. "
		),

		dfs_datanode_data_dir_perm
		(
		"dfs.datanode.data.dir.perm",
		"700",
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

		dfs_namenode_replication_min
		(
		"dfs.namenode.replication.min",
		"1",
		"Minimal block replication. "
		),

		dfs_blocksize
		(
		"dfs.blocksize",
		"134217728",
		"The default block size for new files in bytes. You can use the following suffix (case insensitive): k(kilo) m(mega) g(giga) t(tera) p(peta) e(exa) to specify the size (such as 128k 512m 1g etc.) Or provide complete size in bytes (such as 134217728 for 128 MB). "
		),

		dfs_client_block_write_retries
		(
		"dfs.client.block.write.retries",
		"3",
		"The number of retries for writing blocks to the data nodes before we signal failure to the application. "
		),

		dfs_client_block_write_replace_datanode_on_failure_enable
		(
		"dfs.client.block.write.replace-datanode-on-failure.enable",
		"true",
		"If there is a datanode/network failure in the write pipeline DFSClient will try to remove the failed datanode from the pipeline and then continue writing with the remaining datanodes. As a result the number of datanodes in the pipeline is decreased. The feature is to add new datanodes to the pipeline. This is a site-wide property to enable/disable the feature. When the cluster size is extremely small e.g. 3 nodes or less cluster administrators may want to set the policy to NEVER in the default configuration file or disable this feature. Otherwise users may experience an unusually high rate of pipeline failures since it is impossible to find new datanodes for replacement. See also dfs.client.block.write.replace-datanode-on-failure.policy "
		),

		dfs_client_block_write_replace_datanode_on_failure_policy
		(
		"dfs.client.block.write.replace-datanode-on-failure.policy",
		"DEFAULT",
		"This property is used only if the value of dfs.client.block.write.replace-datanode-on-failure.enable is true. ALWAYS: always add a new datanode when an existing datanode is removed. NEVER: never add a new datanode. DEFAULT: Let r be the replication number. Let n be the number of existing datanodes. Add a new datanode only if r is greater than or equal to 3 and either (1) floor(r/2) is greater than or equal to n; or (2) r is greater than n and the block is hflushed/appended. "
		),

		dfs_client_block_write_replace_datanode_on_failure_best_effort
		(
		"dfs.client.block.write.replace-datanode-on-failure.best-effort",
		"false",
		"This property is used only if the value of dfs.client.block.write.replace-datanode-on-failure.enable is true. Best effort means that the client will try to replace a failed datanode in write pipeline (provided that the policy is satisfied) however it continues the write operation in case that the datanode replacement also fails. Suppose the datanode replacement fails. false: An exception should be thrown so that the write will fail. true : The write should be resumed with the remaining datandoes. Note that setting this property to true allows writing to a pipeline with a smaller number of datanodes. As a result it increases the probability of data loss. "
		),

		dfs_blockreport_intervalMsec
		(
		"dfs.blockreport.intervalMsec",
		"21600000",
		"Determines block reporting interval in milliseconds. "
		),

		dfs_blockreport_initialDelay
		(
		"dfs.blockreport.initialDelay",
		"0",
		"Delay for first block report in seconds. "
		),

		dfs_blockreport_split_threshold
		(
		"dfs.blockreport.split.threshold",
		"1000000",
		"If the number of blocks on the DataNode is below this threshold then it will send block reports for all Storage Directories in a single message. If the number of blocks exceeds this threshold then the DataNode will send block reports for each Storage Directory in separate messages. Set to zero to always split. "
		),

		dfs_datanode_directoryscan_interval
		(
		"dfs.datanode.directoryscan.interval",
		"21600",
		"Interval in seconds for Datanode to scan data directories and reconcile the difference between blocks in memory and on the disk. "
		),

		dfs_datanode_directoryscan_threads
		(
		"dfs.datanode.directoryscan.threads",
		"1",
		"How many threads should the threadpool used to compile reports for volumes in parallel have. "
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

		dfs_namenode_safemode_threshold_pct
		(
		"dfs.namenode.safemode.threshold-pct",
		"0.999f",
		"Specifies the percentage of blocks that should satisfy the minimal replication requirement defined by dfs.namenode.replication.min. Values less than or equal to 0 mean not to wait for any particular percentage of blocks before exiting safemode. Values greater than 1 will make safe mode permanent. "
		),

		dfs_namenode_safemode_min_datanodes
		(
		"dfs.namenode.safemode.min.datanodes",
		"0",
		"Specifies the number of datanodes that must be considered alive before the name node exits safemode. Values less than or equal to 0 mean not to take the number of live datanodes into account when deciding whether to remain in safe mode during startup. Values greater than the number of datanodes in the cluster will make safe mode permanent. "
		),

		dfs_namenode_safemode_extension
		(
		"dfs.namenode.safemode.extension",
		"30000",
		"Determines extension of safe mode in milliseconds after the threshold level is reached. "
		),

		dfs_namenode_resource_check_interval
		(
		"dfs.namenode.resource.check.interval",
		"5000",
		"The interval in milliseconds at which the NameNode resource checker runs. The checker calculates the number of the NameNode storage volumes whose available spaces are more than dfs.namenode.resource.du.reserved and enters safemode if the number becomes lower than the minimum value specified by dfs.namenode.resource.checked.volumes.minimum. "
		),

		dfs_namenode_resource_du_reserved
		(
		"dfs.namenode.resource.du.reserved",
		"104857600",
		"The amount of space to reserve/require for a NameNode storage directory in bytes. The default is 100MB. "
		),

		dfs_namenode_resource_checked_volumes
		(
		"dfs.namenode.resource.checked.volumes",
		"",
		"A list of local directories for the NameNode resource checker to check in addition to the local edits directories. "
		),

		dfs_namenode_resource_checked_volumes_minimum
		(
		"dfs.namenode.resource.checked.volumes.minimum",
		"1",
		"The minimum number of redundant NameNode storage volumes required. "
		),

		dfs_datanode_balance_bandwidthPerSec
		(
		"dfs.datanode.balance.bandwidthPerSec",
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

		dfs_namenode_max_objects
		(
		"dfs.namenode.max.objects",
		"0",
		"The maximum number of files directories and blocks dfs supports. A value of zero indicates no limit to the number of objects that dfs supports. "
		),

		dfs_namenode_datanode_registration_ip_hostname_check
		(
		"dfs.namenode.datanode.registration.ip-hostname-check",
		"true",
		"If true (the default) then the namenode requires that a connecting datanode's address must be resolved to a hostname. If necessary a reverse DNS lookup is performed. All attempts to register a datanode from an unresolvable address are rejected. It is recommended that this setting be left on to prevent accidental registration of datanodes listed by hostname in the excludes file during a DNS outage. Only set this to false in environments where there is no infrastructure to support reverse DNS lookup. "
		),

		dfs_namenode_decommission_interval
		(
		"dfs.namenode.decommission.interval",
		"30",
		"Namenode periodicity in seconds to check if decommission is complete. "
		),

		dfs_namenode_decommission_blocks_per_interval
		(
		"dfs.namenode.decommission.blocks.per.interval",
		"500000",
		"The approximate number of blocks to process per decommission interval as defined in dfs.namenode.decommission.interval. "
		),

		dfs_namenode_decommission_max_concurrent_tracked_nodes
		(
		"dfs.namenode.decommission.max.concurrent.tracked.nodes",
		"100",
		"The maximum number of decommission-in-progress datanodes nodes that will be tracked at one time by the namenode. Tracking a decommission-in-progress datanode consumes additional NN memory proportional to the number of blocks on the datnode. Having a conservative limit reduces the potential impact of decomissioning a large number of nodes at once. A value of 0 means no limit will be enforced. "
		),

		dfs_namenode_replication_interval
		(
		"dfs.namenode.replication.interval",
		"3",
		"The periodicity in seconds with which the namenode computes replication work for datanodes. "
		),

		dfs_namenode_accesstime_precision
		(
		"dfs.namenode.accesstime.precision",
		"3600000",
		"The access time for HDFS file is precise upto this value. The default value is 1 hour. Setting a value of 0 disables access times for HDFS. "
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
		),

		dfs_stream_buffer_size
		(
		"dfs.stream-buffer-size",
		"4096",
		"The size of buffer to stream files. The size of this buffer should probably be a multiple of hardware page size (4096 on Intel x86) and it determines how much data is buffered during read and write operations. "
		),

		dfs_bytes_per_checksum
		(
		"dfs.bytes-per-checksum",
		"512",
		"The number of bytes per checksum. Must not be larger than dfs.stream-buffer-size "
		),

		dfs_client_write_packet_size
		(
		"dfs.client-write-packet-size",
		"65536",
		"Packet size for clients to write "
		),

		dfs_client_write_exclude_nodes_cache_expiry_interval_millis
		(
		"dfs.client.write.exclude.nodes.cache.expiry.interval.millis",
		"600000",
		"The maximum period to keep a DN in the excluded nodes list at a client. After this period in milliseconds the previously excluded node(s) will be removed automatically from the cache and will be considered good for block allocations again. Useful to lower or raise in situations where you keep a file open for very long periods (such as a Write-Ahead-Log (WAL) file) to make the writer tolerant to cluster maintenance restarts. Defaults to 10 minutes. "
		),

		dfs_namenode_checkpoint_dir
		(
		"dfs.namenode.checkpoint.dir",
		"file://${hadoop.tmp.dir}/dfs/namesecondary",
		"Determines where on the local filesystem the DFS secondary name node should store the temporary images to merge. If this is a comma-delimited list of directories then the image is replicated in all of the directories for redundancy. "
		),

		dfs_namenode_checkpoint_edits_dir
		(
		"dfs.namenode.checkpoint.edits.dir",
		"${dfs.namenode.checkpoint.dir}",
		"Determines where on the local filesystem the DFS secondary name node should store the temporary edits to merge. If this is a comma-delimited list of directories then the edits is replicated in all of the directories for redundancy. Default value is same as dfs.namenode.checkpoint.dir "
		),

		dfs_namenode_checkpoint_period
		(
		"dfs.namenode.checkpoint.period",
		"3600",
		"The number of seconds between two periodic checkpoints. "
		),

		dfs_namenode_checkpoint_txns
		(
		"dfs.namenode.checkpoint.txns",
		"1000000",
		"The Secondary NameNode or CheckpointNode will create a checkpoint of the namespace every 'dfs.namenode.checkpoint.txns' transactions regardless of whether 'dfs.namenode.checkpoint.period' has expired. "
		),

		dfs_namenode_checkpoint_check_period
		(
		"dfs.namenode.checkpoint.check.period",
		"60",
		"The SecondaryNameNode and CheckpointNode will poll the NameNode every 'dfs.namenode.checkpoint.check.period' seconds to query the number of uncheckpointed transactions. "
		),

		dfs_namenode_checkpoint_max_retries
		(
		"dfs.namenode.checkpoint.max-retries",
		"3",
		"The SecondaryNameNode retries failed checkpointing. If the failure occurs while loading fsimage or replaying edits the number of retries is limited by this variable. "
		),

		dfs_namenode_num_checkpoints_retained
		(
		"dfs.namenode.num.checkpoints.retained",
		"2",
		"The number of image checkpoint files (fsimage_*) that will be retained by the NameNode and Secondary NameNode in their storage directories. All edit logs (stored on edits_* files) necessary to recover an up-to-date namespace from the oldest retained checkpoint will also be retained. "
		),

		dfs_namenode_num_extra_edits_retained
		(
		"dfs.namenode.num.extra.edits.retained",
		"1000000",
		"The number of extra transactions which should be retained beyond what is minimally necessary for a NN restart. It does not translate directly to file's age or the number of files kept but to the number of transactions (here edits means transactions). One edit file may contain several transactions (edits). During checkpoint NameNode will identify the total number of edits to retain as extra by checking the latest checkpoint transaction value subtracted by the value of this property. Then it scans edits files to identify the older ones that don't include the computed range of retained transactions that are to be kept around and purges them subsequently. The retainment can be useful for audit purposes or for an HA setup where a remote Standby Node may have been offline for some time and need to have a longer backlog of retained edits in order to start again. Typically each edit is on the order of a few hundred bytes so the default of 1 million edits should be on the order of hundreds of MBs or low GBs. NOTE: Fewer extra edits may be retained than value specified for this setting if doing so would mean that more segments would be retained than the number configured by dfs.namenode.max.extra.edits.segments.retained. "
		),

		dfs_namenode_max_extra_edits_segments_retained
		(
		"dfs.namenode.max.extra.edits.segments.retained",
		"10000",
		"The maximum number of extra edit log segments which should be retained beyond what is minimally necessary for a NN restart. When used in conjunction with dfs.namenode.num.extra.edits.retained this configuration property serves to cap the number of extra edits files to a reasonable value. "
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

		dfs_image_compress
		(
		"dfs.image.compress",
		"false",
		"Should the dfs image be compressed? "
		),

		dfs_image_compression_codec
		(
		"dfs.image.compression.codec",
		"org.apache.hadoop.io.compress.DefaultCodec",
		"If the dfs image is compressed how should they be compressed? This has to be a codec defined in io.compression.codecs. "
		),

		dfs_image_transfer_timeout
		(
		"dfs.image.transfer.timeout",
		"60000",
		"Socket timeout for image transfer in milliseconds. This timeout and the related dfs.image.transfer.bandwidthPerSec parameter should be configured such that normal image transfer can complete successfully. This timeout prevents client hangs when the sender fails during image transfer. This is socket timeout during image transfer. "
		),

		dfs_image_transfer_bandwidthPerSec
		(
		"dfs.image.transfer.bandwidthPerSec",
		"0",
		"Maximum bandwidth used for image transfer in bytes per second. This can help keep normal namenode operations responsive during checkpointing. The maximum bandwidth and timeout in dfs.image.transfer.timeout should be set such that normal image transfers can complete successfully. A default value of 0 indicates that throttling is disabled. "
		),

		dfs_image_transfer_chunksize
		(
		"dfs.image.transfer.chunksize",
		"65536",
		"Chunksize in bytes to upload the checkpoint. Chunked streaming is used to avoid internal buffering of contents of image file of huge size. "
		),

		dfs_namenode_support_allow_format
		(
		"dfs.namenode.support.allow.format",
		"true",
		"Does HDFS namenode allow itself to be formatted? You may consider setting this to false for any production cluster to avoid any possibility of formatting a running DFS. "
		),

		dfs_datanode_max_transfer_threads
		(
		"dfs.datanode.max.transfer.threads",
		"4096",
		"Specifies the maximum number of threads to use for transferring data in and out of the DN. "
		),

		dfs_datanode_scan_period_hours
		(
		"dfs.datanode.scan.period.hours",
		"0",
		"If this is 0 or negative the DataNode's block scanner will be disabled. If this is positive the DataNode will not scan any individual block more than once in the specified scan period. "
		),

		dfs_block_scanner_volume_bytes_per_second
		(
		"dfs.block.scanner.volume.bytes.per.second",
		"1048576",
		"If this is 0 the DataNode's block scanner will be disabled. If this is positive this is the number of bytes per second that the DataNode's block scanner will try to scan from each volume. "
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
		"In some workloads the data read from HDFS is known to be significantly large enough that it is unlikely to be useful to cache it in the operating system buffer cache. In this case the DataNode may be configured to automatically purge all data from the buffer cache after it is delivered to the client. This behavior is automatically disabled for workloads which read only short sections of a block (e.g HBase random-IO workloads). This may improve performance for some workloads by freeing buffer cache space usage for more cacheable data. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_datanode_drop_cache_behind_writes
		(
		"dfs.datanode.drop.cache.behind.writes",
		"false",
		"In some workloads the data written to HDFS is known to be significantly large enough that it is unlikely to be useful to cache it in the operating system buffer cache. In this case the DataNode may be configured to automatically purge all data from the buffer cache after it is written to disk. This may improve performance for some workloads by freeing buffer cache space usage for more cacheable data. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_datanode_sync_behind_writes
		(
		"dfs.datanode.sync.behind.writes",
		"false",
		"If this configuration is enabled the datanode will instruct the operating system to enqueue all written data to the disk immediately after it is written. This differs from the usual OS policy which may wait for up to 30 seconds before triggering writeback. This may improve performance for some workloads by smoothing the IO profile for data written to disk. If the Hadoop native libraries are not available this configuration has no effect. "
		),

		dfs_client_failover_max_attempts
		(
		"dfs.client.failover.max.attempts",
		"15",
		"Expert only. The number of client failover attempts that should be made before the failover is considered failed. "
		),

		dfs_client_failover_sleep_base_millis
		(
		"dfs.client.failover.sleep.base.millis",
		"500",
		"Expert only. The time to wait in milliseconds between failover attempts increases exponentially as a function of the number of attempts made so far with a random factor of +/- 50%. This option specifies the base value used in the failover calculation. The first failover will retry immediately. The 2nd failover attempt will delay at least dfs.client.failover.sleep.base.millis milliseconds. And so on. "
		),

		dfs_client_failover_sleep_max_millis
		(
		"dfs.client.failover.sleep.max.millis",
		"15000",
		"Expert only. The time to wait in milliseconds between failover attempts increases exponentially as a function of the number of attempts made so far with a random factor of +/- 50%. This option specifies the maximum value to wait between failovers. Specifically the time between two failover attempts will not exceed +/- 50% of dfs.client.failover.sleep.max.millis milliseconds. "
		),

		dfs_client_failover_connection_retries
		(
		"dfs.client.failover.connection.retries",
		"0",
		"Expert only. Indicates the number of retries a failover IPC client will make to establish a server connection. "
		),

		dfs_client_failover_connection_retries_on_timeouts
		(
		"dfs.client.failover.connection.retries.on.timeouts",
		"0",
		"Expert only. The number of retry attempts a failover IPC client will make on socket timeout when establishing a server connection. "
		),

		dfs_client_datanode_restart_timeout
		(
		"dfs.client.datanode-restart.timeout",
		"30",
		"Expert only. The time to wait in seconds from reception of an datanode shutdown notification for quick restart until declaring the datanode dead and invoking the normal recovery mechanisms. The notification is sent by a datanode when it is being shutdown using the shutdownDatanode admin command with the upgrade option. "
		),

		dfs_nameservices
		(
		"dfs.nameservices",
		"",
		"Comma-separated list of nameservices. "
		),

		dfs_nameservice_id
		(
		"dfs.nameservice.id",
		"",
		"The ID of this nameservice. If the nameservice ID is not configured or more than one nameservice is configured for dfs.nameservices it is determined automatically by matching the local node's address with the configured address. "
		),

		dfs_internal_nameservices
		(
		"dfs.internal.nameservices",
		"",
		"Comma-separated list of nameservices that belong to this cluster. Datanode will report to all the nameservices in this list. By default this is set to the value of dfs.nameservices. "
		),

		dfs_ha_namenodes_EXAMPLENAMESERVICE
		(
		"dfs.ha.namenodes.EXAMPLENAMESERVICE",
		"",
		"The prefix for a given nameservice contains a comma-separated list of namenodes for a given nameservice (eg EXAMPLENAMESERVICE). "
		),

		dfs_ha_namenode_id
		(
		"dfs.ha.namenode.id",
		"",
		"The ID of this namenode. If the namenode ID is not configured it is determined automatically by matching the local node's address with the configured address. "
		),

		dfs_ha_log_roll_period
		(
		"dfs.ha.log-roll.period",
		"120",
		"How often in seconds the StandbyNode should ask the active to roll edit logs. Since the StandbyNode only reads from finalized log segments the StandbyNode will only be as up-to-date as how often the logs are rolled. Note that failover triggers a log roll so the StandbyNode will be up to date before it becomes active. "
		),

		dfs_ha_tail_edits_period
		(
		"dfs.ha.tail-edits.period",
		"60",
		"How often in seconds the StandbyNode should check for new finalized log segments in the shared edits log. "
		),

		dfs_ha_automatic_failover_enabled
		(
		"dfs.ha.automatic-failover.enabled",
		"false",
		"Whether automatic failover is enabled. See the HDFS High Availability documentation for details on automatic HA configuration. "
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

		dfs_datanode_shared_file_descriptor_paths
		(
		"dfs.datanode.shared.file.descriptor.paths",
		"/dev/shm,/tmp",
		"A comma-separated list of paths to use when creating file descriptors that will be shared between the DataNode and the DFSClient. Typically we use /dev/shm so that the file descriptors will not be written to disk. Systems that don't have /dev/shm will fall back to /tmp by default. "
		),

		dfs_short_circuit_shared_memory_watcher_interrupt_check_ms
		(
		"dfs.short.circuit.shared.memory.watcher.interrupt.check.ms",
		"60000",
		"The length of time in milliseconds that the short-circuit shared memory watcher will go between checking for java interruptions sent from other threads. This is provided mainly for unit tests. "
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
		"Default time interval for marking a datanode as stale i.e. if the namenode has not received heartbeat msg from a datanode for more than this time interval the datanode will be marked and treated as stale by default. The stale interval cannot be too small since otherwise this may cause too frequent change of stale states. We thus set a minimum stale interval value (the default value is 3 times of heartbeat interval) and guarantee that the stale interval cannot be less than the minimum value. A stale data node is avoided during lease/block recovery. It can be conditionally avoided for reads (see dfs.namenode.avoid.read.stale.datanode) and for writes (see dfs.namenode.avoid.write.stale.datanode). "
		),

		dfs_namenode_write_stale_datanode_ratio
		(
		"dfs.namenode.write.stale.datanode.ratio",
		"0.5f",
		"When the ratio of number stale datanodes to total datanodes marked is greater than this ratio stop avoiding writing to stale nodes so as to prevent causing hotspots. "
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

		nfs_server_port
		(
		"nfs.server.port",
		"2049",
		"Specify the port number used by Hadoop NFS. "
		),

		nfs_mountd_port
		(
		"nfs.mountd.port",
		"4242",
		"Specify the port number used by Hadoop mount daemon. "
		),

		nfs_dump_dir
		(
		"nfs.dump.dir",
		"/tmp/.hdfs-nfs",
		"This directory is used to temporarily save out-of-order writes before writing to HDFS. For each file the out-of-order writes are dumped after they are accumulated to exceed certain threshold (e.g. 1MB) in memory. One needs to make sure the directory has enough space. "
		),

		nfs_rtmax
		(
		"nfs.rtmax",
		"1048576",
		"This is the maximum size in bytes of a READ request supported by the NFS gateway. If you change this make sure you also update the nfs mount's rsize(add rsize= # of bytes to the mount directive). "
		),

		nfs_wtmax
		(
		"nfs.wtmax",
		"1048576",
		"This is the maximum size in bytes of a WRITE request supported by the NFS gateway. If you change this make sure you also update the nfs mount's wsize(add wsize= # of bytes to the mount directive). "
		),

		nfs_keytab_file
		(
		"nfs.keytab.file",
		"",
		"*Note*: Advanced property. Change with caution. This is the path to the keytab file for the hdfs-nfs gateway. This is required when the cluster is kerberized. "
		),

		nfs_kerberos_principal
		(
		"nfs.kerberos.principal",
		"",
		"*Note*: Advanced property. Change with caution. This is the name of the kerberos principal. This is required when the cluster is kerberized.It must be of this format: nfs-gateway-user/nfs-gateway-host@kerberos-realm "
		),

		nfs_allow_insecure_ports
		(
		"nfs.allow.insecure.ports",
		"true",
		"When set to false client connections originating from unprivileged ports (those above 1023) will be rejected. This is to ensure that clients connecting to this NFS Gateway must have had root privilege on the machine where they're connecting from. "
		),

		dfs_webhdfs_enabled
		(
		"dfs.webhdfs.enabled",
		"true",
		"Enable WebHDFS (REST API) in Namenodes and Datanodes. "
		),

		hadoop_fuse_connection_timeout
		(
		"hadoop.fuse.connection.timeout",
		"300",
		"The minimum number of seconds that we'll cache libhdfs connection objects in fuse_dfs. Lower values will result in lower memory consumption; higher values may speed up access by avoiding the overhead of creating new connection objects. "
		),

		hadoop_fuse_timer_period
		(
		"hadoop.fuse.timer.period",
		"5",
		"The number of seconds between cache expiry checks in fuse_dfs. Lower values will result in fuse_dfs noticing changes to Kerberos ticket caches more quickly. "
		),

		dfs_metrics_percentiles_intervals
		(
		"dfs.metrics.percentiles.intervals",
		"",
		"Comma-delimited set of integers denoting the desired rollover intervals (in seconds) for percentile latency metrics on the Namenode and Datanode. By default percentile latency metrics are disabled. "
		),

		hadoop_user_group_metrics_percentiles_intervals
		(
		"hadoop.user.group.metrics.percentiles.intervals",
		"",
		"A comma-separated list of the granularity in seconds for the metrics which describe the 50/75/90/95/99th percentile latency for group resolution in milliseconds. By default percentile latency metrics are disabled. "
		),

		dfs_encrypt_data_transfer
		(
		"dfs.encrypt.data.transfer",
		"false",
		"Whether or not actual block data that is read/written from/to HDFS should be encrypted on the wire. This only needs to be set on the NN and DNs clients will deduce this automatically. It is possible to override this setting per connection by specifying custom logic via dfs.trustedchannel.resolver.class. "
		),

		dfs_encrypt_data_transfer_algorithm
		(
		"dfs.encrypt.data.transfer.algorithm",
		"",
		"This value may be set to either 3des or rc4. If nothing is set then the configured JCE default on the system is used (usually 3DES.) It is widely believed that 3DES is more cryptographically secure but RC4 is substantially faster. Note that if AES is supported by both the client and server then this encryption algorithm will only be used to initially transfer keys for AES. (See dfs.encrypt.data.transfer.cipher.suites.) "
		),

		dfs_encrypt_data_transfer_cipher_suites
		(
		"dfs.encrypt.data.transfer.cipher.suites",
		"",
		"This value may be either undefined or AES/CTR/NoPadding. If defined then dfs.encrypt.data.transfer uses the specified cipher suite for data encryption. If not defined then only the algorithm specified in dfs.encrypt.data.transfer.algorithm is used. By default the property is not defined. "
		),

		dfs_encrypt_data_transfer_cipher_key_bitlength
		(
		"dfs.encrypt.data.transfer.cipher.key.bitlength",
		"128",
		"The key bitlength negotiated by dfsclient and datanode for encryption. This value may be set to either 128 192 or 256. "
		),

		dfs_trustedchannel_resolver_class
		(
		"dfs.trustedchannel.resolver.class",
		"",
		"TrustedChannelResolver is used to determine whether a channel is trusted for plain data transfer. The TrustedChannelResolver is invoked on both client and server side. If the resolver indicates that the channel is trusted then the data transfer will not be encrypted even if dfs.encrypt.data.transfer is set to true. The default implementation returns false indicating that the channel is not trusted. "
		),

		dfs_data_transfer_protection
		(
		"dfs.data.transfer.protection",
		"",
		"A comma-separated list of SASL protection values used for secured connections to the DataNode when reading or writing block data. Possible values are authentication integrity and privacy. authentication means authentication only and no integrity or privacy; integrity implies authentication and integrity are enabled; and privacy implies all of authentication integrity and privacy are enabled. If dfs.encrypt.data.transfer is set to true then it supersedes the setting for dfs.data.transfer.protection and enforces that all connections must use a specialized encrypted SASL handshake. This property is ignored for connections to a DataNode listening on a privileged port. In this case it is assumed that the use of a privileged port establishes sufficient trust. "
		),

		dfs_data_transfer_saslproperties_resolver_class
		(
		"dfs.data.transfer.saslproperties.resolver.class",
		"",
		"SaslPropertiesResolver used to resolve the QOP used for a connection to the DataNode when reading or writing block data. If not specified the value of hadoop.security.saslproperties.resolver.class is used as the default value. "
		),

		dfs_datanode_hdfs_blocks_metadata_enabled
		(
		"dfs.datanode.hdfs-blocks-metadata.enabled",
		"false",
		"Boolean which enables backend datanode-side support for the experimental DistributedFileSystem#getFileVBlockStorageLocations API. "
		),

		dfs_client_file_block_storage_locations_num_threads
		(
		"dfs.client.file-block-storage-locations.num-threads",
		"10",
		"Number of threads used for making parallel RPCs in DistributedFileSystem#getFileBlockStorageLocations(). "
		),

		dfs_client_file_block_storage_locations_timeout_millis
		(
		"dfs.client.file-block-storage-locations.timeout.millis",
		"1000",
		"Timeout (in milliseconds) for the parallel RPCs made in DistributedFileSystem#getFileBlockStorageLocations(). "
		),

		dfs_journalnode_rpc_address
		(
		"dfs.journalnode.rpc-address",
		"0.0.0.0:8485",
		"The JournalNode RPC server address and port. "
		),

		dfs_journalnode_http_address
		(
		"dfs.journalnode.http-address",
		"0.0.0.0:8480",
		"The address and port the JournalNode HTTP server listens on. If the port is 0 then the server will start on a free port. "
		),

		dfs_journalnode_https_address
		(
		"dfs.journalnode.https-address",
		"0.0.0.0:8481",
		"The address and port the JournalNode HTTPS server listens on. If the port is 0 then the server will start on a free port. "
		),

		dfs_namenode_audit_loggers
		(
		"dfs.namenode.audit.loggers",
		"default",
		"List of classes implementing audit loggers that will receive audit events. These should be implementations of org.apache.hadoop.hdfs.server.namenode.AuditLogger. The special value default can be used to reference the default audit logger which uses the configured log system. Installing custom audit loggers may affect the performance and stability of the NameNode. Refer to the custom logger's documentation for more details. "
		),

		dfs_datanode_available_space_volume_choosing_policy_balanced_space_threshold
		(
		"dfs.datanode.available-space-volume-choosing-policy.balanced-space-threshold",
		"10737418240",
		"Only used when the dfs.datanode.fsdataset.volume.choosing.policy is set to org.apache.hadoop.hdfs.server.datanode.fsdataset.AvailableSpaceVolumeChoosingPolicy. This setting controls how much DN volumes are allowed to differ in terms of bytes of free disk space before they are considered imbalanced. If the free space of all the volumes are within this range of each other the volumes will be considered balanced and block assignments will be done on a pure round robin basis. "
		),

		dfs_datanode_available_space_volume_choosing_policy_balanced_space_preference_fraction
		(
		"dfs.datanode.available-space-volume-choosing-policy.balanced-space-preference-fraction",
		"0.75f",
		"Only used when the dfs.datanode.fsdataset.volume.choosing.policy is set to org.apache.hadoop.hdfs.server.datanode.fsdataset.AvailableSpaceVolumeChoosingPolicy. This setting controls what percentage of new block allocations will be sent to volumes with more available disk space than others. This setting should be in the range 0.0 - 1.0 though in practice 0.5 - 1.0 since there should be no reason to prefer that volumes with less available disk space receive more block allocations. "
		),

		dfs_namenode_edits_noeditlogchannelflush
		(
		"dfs.namenode.edits.noeditlogchannelflush",
		"false",
		"Specifies whether to flush edit log file channel. When set expensive FileChannel#force calls are skipped and synchronous disk writes are enabled instead by opening the edit log file with RandomAccessFile(rws) flags. This can significantly improve the performance of edit log writes on the Windows platform. Note that the behavior of the rws flags is platform and hardware specific and might not provide the same level of guarantees as FileChannel#force. For example the write will skip the disk-cache on SAS and SCSI devices while it might not on SATA devices. This is an expert level setting change with caution. "
		),

		dfs_client_cache_drop_behind_writes
		(
		"dfs.client.cache.drop.behind.writes",
		"",
		"Just like dfs.datanode.drop.cache.behind.writes this setting causes the page cache to be dropped behind HDFS writes potentially freeing up more memory for other uses. Unlike dfs.datanode.drop.cache.behind.writes this is a client-side setting rather than a setting for the entire datanode. If present this setting will override the DataNode default. If the native libraries are not available to the DataNode this configuration has no effect. "
		),

		dfs_client_cache_drop_behind_reads
		(
		"dfs.client.cache.drop.behind.reads",
		"",
		"Just like dfs.datanode.drop.cache.behind.reads this setting causes the page cache to be dropped behind HDFS reads potentially freeing up more memory for other uses. Unlike dfs.datanode.drop.cache.behind.reads this is a client-side setting rather than a setting for the entire datanode. If present this setting will override the DataNode default. If the native libraries are not available to the DataNode this configuration has no effect. "
		),

		dfs_client_cache_readahead
		(
		"dfs.client.cache.readahead",
		"",
		"When using remote reads this setting causes the datanode to read ahead in the block file using posix_fadvise potentially decreasing I/O wait times. Unlike dfs.datanode.readahead.bytes this is a client-side setting rather than a setting for the entire datanode. If present this setting will override the DataNode default. When using local reads this setting determines how much readahead we do in BlockReaderLocal. If the native libraries are not available to the DataNode this configuration has no effect. "
		),

		dfs_namenode_enable_retrycache
		(
		"dfs.namenode.enable.retrycache",
		"true",
		"This enables the retry cache on the namenode. Namenode tracks for non-idempotent requests the corresponding response. If a client retries the request the response from the retry cache is sent. Such operations are tagged with annotation @AtMostOnce in namenode protocols. It is recommended that this flag be set to true. Setting it to false will result in clients getting failure responses to retried request. This flag must be enabled in HA setup for transparent fail-overs. The entries in the cache have expiration time configurable using dfs.namenode.retrycache.expirytime.millis. "
		),

		dfs_namenode_retrycache_expirytime_millis
		(
		"dfs.namenode.retrycache.expirytime.millis",
		"600000",
		"The time for which retry cache entries are retained. "
		),

		dfs_namenode_retrycache_heap_percent
		(
		"dfs.namenode.retrycache.heap.percent",
		"0.03f",
		"This parameter configures the heap size allocated for retry cache (excluding the response cached). This corresponds to approximately 4096 entries for every 64MB of namenode process java heap size. Assuming retry cache entry expiration time (configured using dfs.namenode.retrycache.expirytime.millis) of 10 minutes this enables retry cache to support 7 operations per second sustained for 10 minutes. As the heap size is increased the operation rate linearly increases. "
		),

		dfs_client_mmap_enabled
		(
		"dfs.client.mmap.enabled",
		"true",
		"If this is set to false the client won't attempt to perform memory-mapped reads. "
		),

		dfs_client_mmap_cache_size
		(
		"dfs.client.mmap.cache.size",
		"256",
		"When zero-copy reads are used the DFSClient keeps a cache of recently used memory mapped regions. This parameter controls the maximum number of entries that we will keep in that cache. The larger this number is the more file descriptors we will potentially use for memory-mapped files. mmaped files also use virtual address space. You may need to increase your ulimit virtual address space limits before increasing the client mmap cache size. Note that you can still do zero-copy reads when this size is set to 0. "
		),

		dfs_client_mmap_cache_timeout_ms
		(
		"dfs.client.mmap.cache.timeout.ms",
		"3600000",
		"The minimum length of time that we will keep an mmap entry in the cache between uses. If an entry is in the cache longer than this and nobody uses it it will be removed by a background thread. "
		),

		dfs_client_mmap_retry_timeout_ms
		(
		"dfs.client.mmap.retry.timeout.ms",
		"300000",
		"The minimum amount of time that we will wait before retrying a failed mmap operation. "
		),

		dfs_client_short_circuit_replica_stale_threshold_ms
		(
		"dfs.client.short.circuit.replica.stale.threshold.ms",
		"1800000",
		"The maximum amount of time that we will consider a short-circuit replica to be valid if there is no communication from the DataNode. After this time has elapsed we will re-fetch the short-circuit replica even if it is in the cache. "
		),

		dfs_namenode_path_based_cache_block_map_allocation_percent
		(
		"dfs.namenode.path.based.cache.block.map.allocation.percent",
		"0.25",
		"The percentage of the Java heap which we will allocate to the cached blocks map. The cached blocks map is a hash map which uses chained hashing. Smaller maps may be accessed more slowly if the number of cached blocks is large; larger maps will consume more memory. "
		),

		dfs_datanode_max_locked_memory
		(
		"dfs.datanode.max.locked.memory",
		"0",
		"The amount of memory in bytes to use for caching of block replicas in memory on the datanode. The datanode's maximum locked memory soft ulimit (RLIMIT_MEMLOCK) must be set to at least this value else the datanode will abort on startup. By default this parameter is set to 0 which disables in-memory caching. If the native libraries are not available to the DataNode this configuration has no effect. "
		),

		dfs_namenode_list_cache_directives_num_responses
		(
		"dfs.namenode.list.cache.directives.num.responses",
		"100",
		"This value controls the number of cache directives that the NameNode will send over the wire in response to a listDirectives RPC. "
		),

		dfs_namenode_list_cache_pools_num_responses
		(
		"dfs.namenode.list.cache.pools.num.responses",
		"100",
		"This value controls the number of cache pools that the NameNode will send over the wire in response to a listPools RPC. "
		),

		dfs_namenode_path_based_cache_refresh_interval_ms
		(
		"dfs.namenode.path.based.cache.refresh.interval.ms",
		"30000",
		"The amount of milliseconds between subsequent path cache rescans. Path cache rescans are when we calculate which blocks should be cached and on what datanodes. By default this parameter is set to 30 seconds. "
		),

		dfs_namenode_path_based_cache_retry_interval_ms
		(
		"dfs.namenode.path.based.cache.retry.interval.ms",
		"30000",
		"When the NameNode needs to uncache something that is cached or cache something that is not cached it must direct the DataNodes to do so by sending a DNA_CACHE or DNA_UNCACHE command in response to a DataNode heartbeat. This parameter controls how frequently the NameNode will resend these commands. "
		),

		dfs_datanode_fsdatasetcache_max_threads_per_volume
		(
		"dfs.datanode.fsdatasetcache.max.threads.per.volume",
		"4",
		"The maximum number of threads per volume to use for caching new data on the datanode. These threads consume both I/O and CPU. This can affect normal datanode operations. "
		),

		dfs_cachereport_intervalMsec
		(
		"dfs.cachereport.intervalMsec",
		"10000",
		"Determines cache reporting interval in milliseconds. After this amount of time the DataNode sends a full report of its cache state to the NameNode. The NameNode uses the cache report to update its map of cached blocks to DataNode locations. This configuration has no effect if in-memory caching has been disabled by setting dfs.datanode.max.locked.memory to 0 (which is the default). If the native libraries are not available to the DataNode this configuration has no effect. "
		),

		dfs_namenode_edit_log_autoroll_multiplier_threshold
		(
		"dfs.namenode.edit.log.autoroll.multiplier.threshold",
		"2.0",
		"Determines when an active namenode will roll its own edit log. The actual threshold (in number of edits) is determined by multiplying this value by dfs.namenode.checkpoint.txns. This prevents extremely large edit files from accumulating on the active namenode which can cause timeouts during namenode startup and pose an administrative hassle. This behavior is intended as a failsafe for when the standby or secondary namenode fail to roll the edit log by the normal checkpoint threshold. "
		),

		dfs_namenode_edit_log_autoroll_check_interval_ms
		(
		"dfs.namenode.edit.log.autoroll.check.interval.ms",
		"300000",
		"How often an active namenode will check if it needs to roll its edit log in milliseconds. "
		),

		dfs_webhdfs_user_provider_user_pattern
		(
		"dfs.webhdfs.user.provider.user.pattern",
		"^[A-Za-z_][A-Za-z0-9._-]*[$]?$",
		"Valid pattern for user and group names for webhdfs it must be a valid java regex. "
		),

		dfs_client_context
		(
		"dfs.client.context",
		"default",
		"The name of the DFSClient context that we should use. Clients that share a context share a socket cache and short-circuit cache among other things. You should only change this if you don't want to share with another set of threads. "
		),

		dfs_client_read_shortcircuit
		(
		"dfs.client.read.shortcircuit",
		"false",
		"This configuration parameter turns on short-circuit local reads. "
		),

		dfs_domain_socket_path
		(
		"dfs.domain.socket.path",
		"",
		"Optional. This is a path to a UNIX domain socket that will be used for communication between the DataNode and local HDFS clients. If the string _PORT is present in this path it will be replaced by the TCP port of the DataNode. "
		),

		dfs_client_read_shortcircuit_skip_checksum
		(
		"dfs.client.read.shortcircuit.skip.checksum",
		"false",
		"If this configuration parameter is set short-circuit local reads will skip checksums. This is normally not recommended but it may be useful for special setups. You might consider using this if you are doing your own checksumming outside of HDFS. "
		),

		dfs_client_read_shortcircuit_streams_cache_size
		(
		"dfs.client.read.shortcircuit.streams.cache.size",
		"256",
		"The DFSClient maintains a cache of recently opened file descriptors. This parameter controls the size of that cache. Setting this higher will use more file descriptors but potentially provide better performance on workloads involving lots of seeks. "
		),

		dfs_client_read_shortcircuit_streams_cache_expiry_ms
		(
		"dfs.client.read.shortcircuit.streams.cache.expiry.ms",
		"300000",
		"This controls the minimum amount of time file descriptors need to sit in the client cache context before they can be closed for being inactive for too long. "
		),

		dfs_datanode_shared_file_descriptor_paths2
		(
		"dfs.datanode.shared.file.descriptor.paths",
		"/dev/shm,/tmp",
		"Comma separated paths to the directory on which shared memory segments are created. The client and the DataNode exchange information via this shared memory segment. It tries paths in order until creation of shared memory segment succeeds. "
		),

		dfs_client_use_legacy_blockreader_local
		(
		"dfs.client.use.legacy.blockreader.local",
		"false",
		"Legacy short-circuit reader implementation based on HDFS-2246 is used if this configuration parameter is true. This is for the platforms other than Linux where the new implementation based on HDFS-347 is not available. "
		),

		dfs_block_local_path_access_user
		(
		"dfs.block.local-path-access.user",
		"",
		"Comma separated list of the users allowd to open block files on legacy short-circuit local read. "
		),

		dfs_client_domain_socket_data_traffic
		(
		"dfs.client.domain.socket.data.traffic",
		"false",
		"This control whether we will try to pass normal data traffic over UNIX domain socket rather than over TCP socket on node-local data transfer. This is currently experimental and turned off by default. "
		),

		dfs_namenode_reject_unresolved_dn_topology_mapping
		(
		"dfs.namenode.reject-unresolved-dn-topology-mapping",
		"false",
		"If the value is set to true then namenode will reject datanode registration if the topology mapping for a datanode is not resolved and NULL is returned (script defined by net.topology.script.file.name fails to execute). Otherwise datanode will be registered and the default rack will be assigned as the topology path. Topology paths are important for data resiliency since they define fault domains. Thus it may be unwanted behavior to allow datanode registration with the default rack if the resolving topology failed. "
		),

		dfs_client_slow_io_warning_threshold_ms
		(
		"dfs.client.slow.io.warning.threshold.ms",
		"30000",
		"The threshold in milliseconds at which we will log a slow io warning in a dfsclient. By default this parameter is set to 30000 milliseconds (30 seconds). "
		),

		dfs_datanode_slow_io_warning_threshold_ms
		(
		"dfs.datanode.slow.io.warning.threshold.ms",
		"300",
		"The threshold in milliseconds at which we will log a slow io warning in a datanode. By default this parameter is set to 300 milliseconds. "
		),

		dfs_namenode_xattrs_enabled
		(
		"dfs.namenode.xattrs.enabled",
		"true",
		"Whether support for extended attributes is enabled on the NameNode. "
		),

		dfs_namenode_fs_limits_max_xattrs_per_inode
		(
		"dfs.namenode.fs-limits.max-xattrs-per-inode",
		"32",
		"Maximum number of extended attributes per inode. "
		),

		dfs_namenode_fs_limits_max_xattr_size
		(
		"dfs.namenode.fs-limits.max-xattr-size",
		"16384",
		"The maximum combined size of the name and value of an extended attribute in bytes. "
		),

		dfs_namenode_startup_delay_block_deletion_sec
		(
		"dfs.namenode.startup.delay.block.deletion.sec",
		"0",
		"The delay in seconds at which we will pause the blocks deletion after Namenode startup. By default it's disabled. In the case a directory has large number of directories and files are deleted suggested delay is one hour to give the administrator enough time to notice large number of pending deletion blocks and take corrective action. "
		),

		dfs_namenode_list_encryption_zones_num_responses
		(
		"dfs.namenode.list.encryption.zones.num.responses",
		"100",
		"When listing encryption zones the maximum number of zones that will be returned in a batch. Fetching the list incrementally in batches improves namenode performance. "
		),

		dfs_namenode_inotify_max_events_per_rpc
		(
		"dfs.namenode.inotify.max.events.per.rpc",
		"1000",
		"Maximum number of events that will be sent to an inotify client in a single RPC response. The default value attempts to amortize away the overhead for this RPC while avoiding huge memory requirements for the client and NameNode (1000 events should consume no more than 1 MB.) "
		),

		dfs_user_home_dir_prefix
		(
		"dfs.user.home.dir.prefix",
		"/user",
		"The directory to prepend to user name to get the user's home direcotry. "
		),

		dfs_datanode_cache_revocation_timeout_ms
		(
		"dfs.datanode.cache.revocation.timeout.ms",
		"900000",
		"When the DFSClient reads from a block file which the DataNode is caching the DFSClient can skip verifying checksums. The DataNode will keep the block file in cache until the client is done. If the client takes an unusually long time though the DataNode may need to evict the block file from the cache anyway. This value controls how long the DataNode will wait for the client to release a replica that it is reading without checksums. "
		),

		dfs_datanode_cache_revocation_polling_ms
		(
		"dfs.datanode.cache.revocation.polling.ms",
		"500",
		"How often the DataNode should poll to see if the clients have stopped using a replica that the DataNode wants to uncache. "
		),

		dfs_datanode_block_id_layout_upgrade_threads
		(
		"dfs.datanode.block.id.layout.upgrade.threads",
		"12",
		"The number of threads to use when creating hard links from current to previous blocks during upgrade of a DataNode to block ID-based block layout (see HDFS-6482 for details on the layout). "
		),

		dfs_encryption_key_provider_uri
		(
		"dfs.encryption.key.provider.uri",
		"",
		"The KeyProvider to use when interacting with encryption keys used when reading and writing to an encryption zone. "
		),

		dfs_storage_policy_enabled
		(
		"dfs.storage.policy.enabled",
		"true",
		"Allow users to change the storage policy on files and directories. "
		),

		dfs_namenode_legacy_oiv_image_dir
		(
		"dfs.namenode.legacy-oiv-image.dir",
		"",
		"Determines where to save the namespace in the old fsimage format during checkpointing by standby NameNode or SecondaryNameNode. Users can dump the contents of the old format fsimage by oiv_legacy command. If the value is not specified old format fsimage will not be saved in checkpoint. "
		),

		dfs_namenode_top_enabled
		(
		"dfs.namenode.top.enabled",
		"true",
		"Enable nntop: reporting top users on namenode "
		),

		dfs_namenode_top_window_num_buckets
		(
		"dfs.namenode.top.window.num.buckets",
		"10",
		"Number of buckets in the rolling window implementation of nntop "
		),

		dfs_namenode_top_num_users
		(
		"dfs.namenode.top.num.users",
		"10",
		"Number of top users returned by the top tool "
		),

		dfs_namenode_top_windows_minutes
		(
		"dfs.namenode.top.windows.minutes",
		"1,5,25",
		"comma separated list of nntop reporting periods in minutes "
		),

		dfs_namenode_blocks_per_postponedblocks_rescan
		(
		"dfs.namenode.blocks.per.postponedblocks.rescan",
		"10000",
		"Number of blocks to rescan for each iteration of postponedMisreplicatedBlocks. "
		),

		dfs_datanode_block_pinning_enabled
		(
		"dfs.datanode.block-pinning.enabled",
		"false",
		"Whether pin blocks on favored DataNode. "
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
		mapreduce_jobtracker_jobhistory_location
		(
		"mapreduce.jobtracker.jobhistory.location",
		"",
		"If job tracker is static the history files are stored in this single well known place. If No value is set here by default it is in the local file system at ${hadoop.log.dir}/history. "
		),

		mapreduce_jobtracker_jobhistory_task_numberprogresssplits
		(
		"mapreduce.jobtracker.jobhistory.task.numberprogresssplits",
		"12",
		"Every task attempt progresses from 0.0 to 1.0 [unless it fails or is killed]. We record for each task attempt certain statistics over each twelfth of the progress range. You can change the number of intervals we divide the entire range of progress into by setting this property. Higher values give more precision to the recorded data but costs more memory in the job tracker at runtime. Each increment in this attribute costs 16 bytes per running task. "
		),

		mapreduce_job_userhistorylocation
		(
		"mapreduce.job.userhistorylocation",
		"",
		"User can specify a location to store the history files of a particular job. If nothing is specified the logs are stored in output directory. The files are stored in _logs/history/ in the directory. User can stop logging by giving the value none. "
		),

		mapreduce_jobtracker_jobhistory_completed_location
		(
		"mapreduce.jobtracker.jobhistory.completed.location",
		"",
		"The completed job history files are stored at this single well known location. If nothing is specified the files are stored at ${mapreduce.jobtracker.jobhistory.location}/done. "
		),

		mapreduce_job_committer_setup_cleanup_needed
		(
		"mapreduce.job.committer.setup.cleanup.needed",
		"true",
		"true if job needs job-setup and job-cleanup. false otherwise "
		),

		mapreduce_task_io_sort_factor
		(
		"mapreduce.task.io.sort.factor",
		"10",
		"The number of streams to merge at once while sorting files. This determines the number of open file handles. "
		),

		mapreduce_task_io_sort_mb
		(
		"mapreduce.task.io.sort.mb",
		"100",
		"The total amount of buffer memory to use while sorting files in megabytes. By default gives each merge stream 1MB which should minimize seeks. "
		),

		mapreduce_map_sort_spill_percent
		(
		"mapreduce.map.sort.spill.percent",
		"0.80",
		"The soft limit in the serialization buffer. Once reached a thread will begin to spill the contents to disk in the background. Note that collection will not block if this threshold is exceeded while a spill is already in progress so spills may be larger than this threshold when it is set to less than .5 "
		),

		mapreduce_jobtracker_address
		(
		"mapreduce.jobtracker.address",
		"local",
		"The host and port that the MapReduce job tracker runs at. If local then jobs are run in-process as a single map and reduce task. "
		),

		mapreduce_local_clientfactory_class_name
		(
		"mapreduce.local.clientfactory.class.name",
		"org.apache.hadoop.mapred.LocalClientFactory",
		"This the client factory that is responsible for creating local job runner client "
		),

		mapreduce_jobtracker_http_address
		(
		"mapreduce.jobtracker.http.address",
		"0.0.0.0:50030",
		"The job tracker http server address and port the server will listen on. If the port is 0 then the server will start on a free port. "
		),

		mapreduce_jobtracker_handler_count
		(
		"mapreduce.jobtracker.handler.count",
		"10",
		"The number of server threads for the JobTracker. This should be roughly 4% of the number of tasktracker nodes. "
		),

		mapreduce_tasktracker_report_address
		(
		"mapreduce.tasktracker.report.address",
		"127.0.0.1:0",
		"The interface and port that task tracker server listens on. Since it is only connected to by the tasks it uses the local interface. EXPERT ONLY. Should only be changed if your host does not have the loopback interface. "
		),

		mapreduce_cluster_local_dir
		(
		"mapreduce.cluster.local.dir",
		"${hadoop.tmp.dir}/mapred/local",
		"The local directory where MapReduce stores intermediate data files. May be a comma-separated list of directories on different devices in order to spread disk i/o. Directories that do not exist are ignored. "
		),

		mapreduce_jobtracker_system_dir
		(
		"mapreduce.jobtracker.system.dir",
		"${hadoop.tmp.dir}/mapred/system",
		"The directory where MapReduce stores control files. "
		),

		mapreduce_jobtracker_staging_root_dir
		(
		"mapreduce.jobtracker.staging.root.dir",
		"${hadoop.tmp.dir}/mapred/staging",
		"The root of the staging area for users' job files In practice this should be the directory where users' home directories are located (usually /user) "
		),

		mapreduce_cluster_temp_dir
		(
		"mapreduce.cluster.temp.dir",
		"${hadoop.tmp.dir}/mapred/temp",
		"A shared directory for temporary files. "
		),

		mapreduce_tasktracker_local_dir_minspacestart
		(
		"mapreduce.tasktracker.local.dir.minspacestart",
		"0",
		"If the space in mapreduce.cluster.local.dir drops under this do not ask for more tasks. Value in bytes. "
		),

		mapreduce_tasktracker_local_dir_minspacekill
		(
		"mapreduce.tasktracker.local.dir.minspacekill",
		"0",
		"If the space in mapreduce.cluster.local.dir drops under this do not ask more tasks until all the current ones have finished and cleaned up. Also to save the rest of the tasks we have running kill one of them to clean up some space. Start with the reduce tasks then go with the ones that have finished the least. Value in bytes. "
		),

		mapreduce_jobtracker_expire_trackers_interval
		(
		"mapreduce.jobtracker.expire.trackers.interval",
		"600000",
		"Expert: The time-interval in miliseconds after which a tasktracker is declared 'lost' if it doesn't send heartbeats. "
		),

		mapreduce_tasktracker_instrumentation
		(
		"mapreduce.tasktracker.instrumentation",
		"org.apache.hadoop.mapred.TaskTrackerMetricsInst",
		"Expert: The instrumentation class to associate with each TaskTracker. "
		),

		mapreduce_tasktracker_resourcecalculatorplugin
		(
		"mapreduce.tasktracker.resourcecalculatorplugin",
		"",
		"Name of the class whose instance will be used to query resource information on the tasktracker. The class must be an instance of org.apache.hadoop.util.ResourceCalculatorPlugin. If the value is null the tasktracker attempts to use a class appropriate to the platform. Currently the only platform supported is Linux. "
		),

		mapreduce_tasktracker_taskmemorymanager_monitoringinterval
		(
		"mapreduce.tasktracker.taskmemorymanager.monitoringinterval",
		"5000",
		"The interval in milliseconds for which the tasktracker waits between two cycles of monitoring its tasks' memory usage. Used only if tasks' memory management is enabled via mapred.tasktracker.tasks.maxmemory. "
		),

		mapreduce_tasktracker_tasks_sleeptimebeforesigkill
		(
		"mapreduce.tasktracker.tasks.sleeptimebeforesigkill",
		"5000",
		"The time in milliseconds the tasktracker waits for sending a SIGKILL to a task after it has been sent a SIGTERM. This is currently not used on WINDOWS where tasks are just sent a SIGTERM. "
		),

		mapreduce_job_maps
		(
		"mapreduce.job.maps",
		"2",
		"The default number of map tasks per job. Ignored when mapreduce.jobtracker.address is local. "
		),

		mapreduce_job_reduces
		(
		"mapreduce.job.reduces",
		"1",
		"The default number of reduce tasks per job. Typically set to 99% of the cluster's reduce capacity so that if a node fails the reduces can still be executed in a single wave. Ignored when mapreduce.jobtracker.address is local. "
		),

		mapreduce_jobtracker_restart_recover
		(
		"mapreduce.jobtracker.restart.recover",
		"false",
		"true to enable (job) recovery upon restart false to start afresh "
		),

		mapreduce_jobtracker_jobhistory_block_size
		(
		"mapreduce.jobtracker.jobhistory.block.size",
		"3145728",
		"The block size of the job history file. Since the job recovery uses job history its important to dump job history to disk as soon as possible. Note that this is an expert level parameter. The default value is set to 3 MB. "
		),

		mapreduce_jobtracker_taskscheduler
		(
		"mapreduce.jobtracker.taskscheduler",
		"org.apache.hadoop.mapred.JobQueueTaskScheduler",
		"The class responsible for scheduling the tasks. "
		),

		mapreduce_job_running_map_limit
		(
		"mapreduce.job.running.map.limit",
		"0",
		"The maximum number of simultaneous map tasks per job. There is no limit if this value is 0 or negative. "
		),

		mapreduce_job_running_reduce_limit
		(
		"mapreduce.job.running.reduce.limit",
		"0",
		"The maximum number of simultaneous reduce tasks per job. There is no limit if this value is 0 or negative. "
		),

		mapreduce_job_reducer_preempt_delay_sec
		(
		"mapreduce.job.reducer.preempt.delay.sec",
		"0",
		"The threshold in terms of seconds after which an unsatisfied mapper request triggers reducer preemption to free space. Default 0 implies that the reduces should be preempted immediately after allocation if there is currently no room for newly allocated mappers. "
		),

		mapreduce_job_max_split_locations
		(
		"mapreduce.job.max.split.locations",
		"10",
		"The max number of block locations to store for each split for locality calculation. "
		),

		mapreduce_job_split_metainfo_maxsize
		(
		"mapreduce.job.split.metainfo.maxsize",
		"10000000",
		"The maximum permissible size of the split metainfo file. The JobTracker won't attempt to read split metainfo files bigger than the configured value. No limits if set to -1. "
		),

		mapreduce_jobtracker_taskscheduler_maxrunningtasks_perjob
		(
		"mapreduce.jobtracker.taskscheduler.maxrunningtasks.perjob",
		"",
		"The maximum number of running tasks for a job before it gets preempted. No limits if undefined. "
		),

		mapreduce_map_maxattempts
		(
		"mapreduce.map.maxattempts",
		"4",
		"Expert: The maximum number of attempts per map task. In other words framework will try to execute a map task these many number of times before giving up on it. "
		),

		mapreduce_reduce_maxattempts
		(
		"mapreduce.reduce.maxattempts",
		"4",
		"Expert: The maximum number of attempts per reduce task. In other words framework will try to execute a reduce task these many number of times before giving up on it. "
		),

		mapreduce_reduce_shuffle_fetch_retry_enabled
		(
		"mapreduce.reduce.shuffle.fetch.retry.enabled",
		"${yarn.nodemanager.recovery.enabled}",
		"Set to enable fetch retry during host restart. "
		),

		mapreduce_reduce_shuffle_fetch_retry_interval_ms
		(
		"mapreduce.reduce.shuffle.fetch.retry.interval-ms",
		"1000",
		"Time of interval that fetcher retry to fetch again when some non-fatal failure happens because of some events like NM restart. "
		),

		mapreduce_reduce_shuffle_fetch_retry_timeout_ms
		(
		"mapreduce.reduce.shuffle.fetch.retry.timeout-ms",
		"30000",
		"Timeout value for fetcher to retry to fetch again when some non-fatal failure happens because of some events like NM restart. "
		),

		mapreduce_reduce_shuffle_retry_delay_max_ms
		(
		"mapreduce.reduce.shuffle.retry-delay.max.ms",
		"60000",
		"The maximum number of ms the reducer will delay before retrying to download map data. "
		),

		mapreduce_reduce_shuffle_parallelcopies
		(
		"mapreduce.reduce.shuffle.parallelcopies",
		"5",
		"The default number of parallel transfers run by reduce during the copy(shuffle) phase. "
		),

		mapreduce_reduce_shuffle_connect_timeout
		(
		"mapreduce.reduce.shuffle.connect.timeout",
		"180000",
		"Expert: The maximum amount of time (in milli seconds) reduce task spends in trying to connect to a tasktracker for getting map output. "
		),

		mapreduce_reduce_shuffle_read_timeout
		(
		"mapreduce.reduce.shuffle.read.timeout",
		"180000",
		"Expert: The maximum amount of time (in milli seconds) reduce task waits for map output data to be available for reading after obtaining connection. "
		),

		mapreduce_shuffle_connection_keep_alive_enable
		(
		"mapreduce.shuffle.connection-keep-alive.enable",
		"false",
		"set to true to support keep-alive connections. "
		),

		mapreduce_shuffle_connection_keep_alive_timeout
		(
		"mapreduce.shuffle.connection-keep-alive.timeout",
		"5",
		"The number of seconds a shuffle client attempts to retain http connection. Refer Keep-Alive: timeout= header in Http specification "
		),

		mapreduce_task_timeout
		(
		"mapreduce.task.timeout",
		"600000",
		"The number of milliseconds before a task will be terminated if it neither reads an input writes an output nor updates its status string. A value of 0 disables the timeout. "
		),

		mapreduce_tasktracker_map_tasks_maximum
		(
		"mapreduce.tasktracker.map.tasks.maximum",
		"2",
		"The maximum number of map tasks that will be run simultaneously by a task tracker. "
		),

		mapreduce_tasktracker_reduce_tasks_maximum
		(
		"mapreduce.tasktracker.reduce.tasks.maximum",
		"2",
		"The maximum number of reduce tasks that will be run simultaneously by a task tracker. "
		),

		mapreduce_map_memory_mb
		(
		"mapreduce.map.memory.mb",
		"1024",
		"The amount of memory to request from the scheduler for each map task. "
		),

		mapreduce_map_cpu_vcores2
		(
		"mapreduce.map.cpu.vcores",
		"1",
		"The number of virtual cores to request from the scheduler for each map task. "
		),

		mapreduce_reduce_memory_mb
		(
		"mapreduce.reduce.memory.mb",
		"1024",
		"The amount of memory to request from the scheduler for each reduce task. "
		),

		mapreduce_reduce_cpu_vcores2
		(
		"mapreduce.reduce.cpu.vcores",
		"1",
		"The number of virtual cores to request from the scheduler for each reduce task. "
		),

		mapreduce_jobtracker_retiredjobs_cache_size
		(
		"mapreduce.jobtracker.retiredjobs.cache.size",
		"1000",
		"The number of retired job status to keep in the cache. "
		),

		mapreduce_tasktracker_outofband_heartbeat
		(
		"mapreduce.tasktracker.outofband.heartbeat",
		"false",
		"Expert: Set this to true to let the tasktracker send an out-of-band heartbeat on task-completion for better latency. "
		),

		mapreduce_jobtracker_jobhistory_lru_cache_size
		(
		"mapreduce.jobtracker.jobhistory.lru.cache.size",
		"5",
		"The number of job history files loaded in memory. The jobs are loaded when they are first accessed. The cache is cleared based on LRU. "
		),

		mapreduce_jobtracker_instrumentation
		(
		"mapreduce.jobtracker.instrumentation",
		"org.apache.hadoop.mapred.JobTrackerMetricsInst",
		"Expert: The instrumentation class to associate with each JobTracker. "
		),

		mapred_child_java_opts
		(
		"mapred.child.java.opts",
		"-Xmx200m",
		"Java opts for the task processes. The following symbol if present will be interpolated: @taskid@ is replaced by current TaskID. Any other occurrences of '@' will go unchanged. For example to enable verbose gc logging to a file named for the taskid in /tmp and to set the heap maximum to be a gigabyte pass a 'value' of: -Xmx1024m -verbose:gc -Xloggc:/tmp/@taskid@.gc Usage of -Djava.library.path can cause programs to no longer function if hadoop native libraries are used. These values should instead be set as part of LD_LIBRARY_PATH in the map / reduce JVM env using the mapreduce.map.env and mapreduce.reduce.env config settings. "
		),

		mapred_child_env
		(
		"mapred.child.env",
		"",
		"User added environment variables for the task processes. Example : 1) A=foo This will set the env variable A to foo 2) B=$B:c This is inherit nodemanager's B env variable on Unix. 3) B=%B%;c This is inherit nodemanager's B env variable on Windows. "
		),

		mapreduce_admin_user_env
		(
		"mapreduce.admin.user.env",
		"",
		"Expert: Additional execution environment entries for map and reduce task processes. This is not an additive property. You must preserve the original value if you want your map and reduce tasks to have access to native libraries (compression etc). When this value is empty the command to set execution envrionment will be OS dependent: For linux use LD_LIBRARY_PATH=$HADOOP_COMMON_HOME/lib/native. For windows use PATH = %PATH%;%HADOOP_COMMON_HOME%\bin. "
		),

		mapreduce_map_log_level
		(
		"mapreduce.map.log.level",
		"INFO",
		"The logging level for the map task. The allowed levels are: OFF FATAL ERROR WARN INFO DEBUG TRACE and ALL. The setting here could be overridden if mapreduce.job.log4j-properties-file is set. "
		),

		mapreduce_reduce_log_level
		(
		"mapreduce.reduce.log.level",
		"INFO",
		"The logging level for the reduce task. The allowed levels are: OFF FATAL ERROR WARN INFO DEBUG TRACE and ALL. The setting here could be overridden if mapreduce.job.log4j-properties-file is set. "
		),

		mapreduce_map_cpu_vcores
		(
		"mapreduce.map.cpu.vcores",
		"1",
		"The number of virtual cores required for each map task. "
		),

		mapreduce_reduce_cpu_vcores
		(
		"mapreduce.reduce.cpu.vcores",
		"1",
		"The number of virtual cores required for each reduce task. "
		),

		mapreduce_reduce_merge_inmem_threshold
		(
		"mapreduce.reduce.merge.inmem.threshold",
		"1000",
		"The threshold in terms of the number of files for the in-memory merge process. When we accumulate threshold number of files we initiate the in-memory merge and spill to disk. A value of 0 or less than 0 indicates we want to DON'T have any threshold and instead depend only on the ramfs's memory consumption to trigger the merge. "
		),

		mapreduce_reduce_shuffle_merge_percent
		(
		"mapreduce.reduce.shuffle.merge.percent",
		"0.66",
		"The usage threshold at which an in-memory merge will be initiated expressed as a percentage of the total memory allocated to storing in-memory map outputs as defined by mapreduce.reduce.shuffle.input.buffer.percent. "
		),

		mapreduce_reduce_shuffle_input_buffer_percent
		(
		"mapreduce.reduce.shuffle.input.buffer.percent",
		"0.70",
		"The percentage of memory to be allocated from the maximum heap size to storing map outputs during the shuffle. "
		),

		mapreduce_reduce_input_buffer_percent
		(
		"mapreduce.reduce.input.buffer.percent",
		"0.0",
		"The percentage of memory- relative to the maximum heap size- to retain map outputs during the reduce. When the shuffle is concluded any remaining map outputs in memory must consume less than this threshold before the reduce can begin. "
		),

		mapreduce_reduce_shuffle_memory_limit_percent
		(
		"mapreduce.reduce.shuffle.memory.limit.percent",
		"0.25",
		"Expert: Maximum percentage of the in-memory limit that a single shuffle can consume "
		),

		mapreduce_shuffle_ssl_enabled
		(
		"mapreduce.shuffle.ssl.enabled",
		"false",
		"Whether to use SSL for for the Shuffle HTTP endpoints. "
		),

		mapreduce_shuffle_ssl_file_buffer_size
		(
		"mapreduce.shuffle.ssl.file.buffer.size",
		"65536",
		"Buffer size for reading spills from file when using SSL. "
		),

		mapreduce_shuffle_max_connections
		(
		"mapreduce.shuffle.max.connections",
		"0",
		"Max allowed connections for the shuffle. Set to 0 (zero) to indicate no limit on the number of connections. "
		),

		mapreduce_shuffle_max_threads
		(
		"mapreduce.shuffle.max.threads",
		"0",
		"Max allowed threads for serving shuffle connections. Set to zero to indicate the default of 2 times the number of available processors (as reported by Runtime.availableProcessors()). Netty is used to serve requests so a thread is not needed for each connection. "
		),

		mapreduce_shuffle_transferTo_allowed
		(
		"mapreduce.shuffle.transferTo.allowed",
		"",
		"This option can enable/disable using nio transferTo method in the shuffle phase. NIO transferTo does not perform well on windows in the shuffle phase. Thus with this configuration property it is possible to disable it in which case custom transfer method will be used. Recommended value is false when running Hadoop on Windows. For Linux it is recommended to set it to true. If nothing is set then the default value is false for Windows and true for Linux. "
		),

		mapreduce_shuffle_transfer_buffer_size
		(
		"mapreduce.shuffle.transfer.buffer.size",
		"131072",
		"This property is used only if mapreduce.shuffle.transferTo.allowed is set to false. In that case this property defines the size of the buffer used in the buffer copy code for the shuffle phase. The size of this buffer determines the size of the IO requests. "
		),

		mapreduce_reduce_markreset_buffer_percent
		(
		"mapreduce.reduce.markreset.buffer.percent",
		"0.0",
		"The percentage of memory -relative to the maximum heap size- to be used for caching values when using the mark-reset functionality. "
		),

		mapreduce_map_speculative
		(
		"mapreduce.map.speculative",
		"true",
		"If true then multiple instances of some map tasks may be executed in parallel. "
		),

		mapreduce_reduce_speculative
		(
		"mapreduce.reduce.speculative",
		"true",
		"If true then multiple instances of some reduce tasks may be executed in parallel. "
		),

		mapreduce_job_speculative_speculative_cap_running_tasks
		(
		"mapreduce.job.speculative.speculative-cap-running-tasks",
		"0.1",
		"The max percent (0-1) of running tasks that can be speculatively re-executed at any time. "
		),

		mapreduce_job_speculative_speculative_cap_total_tasks
		(
		"mapreduce.job.speculative.speculative-cap-total-tasks",
		"0.01",
		"The max percent (0-1) of all tasks that can be speculatively re-executed at any time. "
		),

		mapreduce_job_speculative_minimum_allowed_tasks
		(
		"mapreduce.job.speculative.minimum-allowed-tasks",
		"10",
		"The minimum allowed tasks that can be speculatively re-executed at any time. "
		),

		mapreduce_job_speculative_retry_after_no_speculate
		(
		"mapreduce.job.speculative.retry-after-no-speculate",
		"1000",
		"The waiting time(ms) to do next round of speculation if there is no task speculated in this round. "
		),

		mapreduce_job_speculative_retry_after_speculate
		(
		"mapreduce.job.speculative.retry-after-speculate",
		"15000",
		"The waiting time(ms) to do next round of speculation if there are tasks speculated in this round. "
		),

		mapreduce_job_map_output_collector_class
		(
		"mapreduce.job.map.output.collector.class",
		"org.apache.hadoop.mapred.MapTask$MapOutputBuffer",
		"The MapOutputCollector implementation(s) to use. This may be a comma-separated list of class names in which case the map task will try to initialize each of the collectors in turn. The first to successfully initialize will be used. "
		),

		mapreduce_job_speculative_slowtaskthreshold
		(
		"mapreduce.job.speculative.slowtaskthreshold",
		"1.0",
		"The number of standard deviations by which a task's ave progress-rates must be lower than the average of all running tasks' for the task to be considered too slow. "
		),

		mapreduce_job_jvm_numtasks
		(
		"mapreduce.job.jvm.numtasks",
		"1",
		"How many tasks to run per jvm. If set to -1 there is no limit. "
		),

		mapreduce_job_ubertask_enable
		(
		"mapreduce.job.ubertask.enable",
		"false",
		"Whether to enable the small-jobs ubertask optimization which runs sufficiently small jobs sequentially within a single JVM. Small is defined by the following maxmaps maxreduces and maxbytes settings. Note that configurations for application masters also affect the Small definition - yarn.app.mapreduce.am.resource.mb must be larger than both mapreduce.map.memory.mb and mapreduce.reduce.memory.mb and yarn.app.mapreduce.am.resource.cpu-vcores must be larger than both mapreduce.map.cpu.vcores and mapreduce.reduce.cpu.vcores to enable ubertask. Users may override this value. "
		),

		mapreduce_job_ubertask_maxmaps
		(
		"mapreduce.job.ubertask.maxmaps",
		"9",
		"Threshold for number of maps beyond which job is considered too big for the ubertasking optimization. Users may override this value but only downward. "
		),

		mapreduce_job_ubertask_maxreduces
		(
		"mapreduce.job.ubertask.maxreduces",
		"1",
		"Threshold for number of reduces beyond which job is considered too big for the ubertasking optimization. CURRENTLY THE CODE CANNOT SUPPORT MORE THAN ONE REDUCE and will ignore larger values. (Zero is a valid max however.) Users may override this value but only downward. "
		),

		mapreduce_job_ubertask_maxbytes
		(
		"mapreduce.job.ubertask.maxbytes",
		"",
		"Threshold for number of input bytes beyond which job is considered too big for the ubertasking optimization. If no value is specified dfs.block.size is used as a default. Be sure to specify a default value in mapred-site.xml if the underlying filesystem is not HDFS. Users may override this value but only downward. "
		),

		mapreduce_job_emit_timeline_data
		(
		"mapreduce.job.emit-timeline-data",
		"false",
		"Specifies if the Application Master should emit timeline data to the timeline server. Individual jobs can override this value. "
		),

		mapreduce_input_fileinputformat_split_minsize
		(
		"mapreduce.input.fileinputformat.split.minsize",
		"0",
		"The minimum size chunk that map input should be split into. Note that some file formats may have minimum split sizes that take priority over this setting. "
		),

		mapreduce_input_fileinputformat_list_status_num_threads
		(
		"mapreduce.input.fileinputformat.list-status.num-threads",
		"1",
		"The number of threads to use to list and fetch block locations for the specified input paths. Note: multiple threads should not be used if a custom non thread-safe path filter is used. "
		),

		mapreduce_jobtracker_maxtasks_perjob
		(
		"mapreduce.jobtracker.maxtasks.perjob",
		"-1",
		"The maximum number of tasks for a single job. A value of -1 indicates that there is no maximum. "
		),

		mapreduce_input_lineinputformat_linespermap
		(
		"mapreduce.input.lineinputformat.linespermap",
		"1",
		"When using NLineInputFormat the number of lines of input data to include in each split. "
		),

		mapreduce_client_submit_file_replication
		(
		"mapreduce.client.submit.file.replication",
		"10",
		"The replication level for submitted job files. This should be around the square root of the number of nodes. "
		),

		mapreduce_tasktracker_dns_interface
		(
		"mapreduce.tasktracker.dns.interface",
		"default",
		"The name of the Network Interface from which a task tracker should report its IP address. "
		),

		mapreduce_tasktracker_dns_nameserver
		(
		"mapreduce.tasktracker.dns.nameserver",
		"default",
		"The host name or IP address of the name server (DNS) which a TaskTracker should use to determine the host name used by the JobTracker for communication and display purposes. "
		),

		mapreduce_tasktracker_http_threads
		(
		"mapreduce.tasktracker.http.threads",
		"40",
		"The number of worker threads that for the http server. This is used for map output fetching "
		),

		mapreduce_tasktracker_http_address
		(
		"mapreduce.tasktracker.http.address",
		"0.0.0.0:50060",
		"The task tracker http server address and port. If the port is 0 then the server will start on a free port. "
		),

		mapreduce_task_files_preserve_failedtasks
		(
		"mapreduce.task.files.preserve.failedtasks",
		"false",
		"Should the files for failed tasks be kept. This should only be used on jobs that are failing because the storage is never reclaimed. It also prevents the map outputs from being erased from the reduce directory as they are consumed. "
		),

		mapreduce_output_fileoutputformat_compress
		(
		"mapreduce.output.fileoutputformat.compress",
		"false",
		"Should the job outputs be compressed? "
		),

		mapreduce_output_fileoutputformat_compress_type
		(
		"mapreduce.output.fileoutputformat.compress.type",
		"RECORD",
		"If the job outputs are to compressed as SequenceFiles how should they be compressed? Should be one of NONE RECORD or BLOCK. "
		),

		mapreduce_output_fileoutputformat_compress_codec
		(
		"mapreduce.output.fileoutputformat.compress.codec",
		"org.apache.hadoop.io.compress.DefaultCodec",
		"If the job outputs are compressed how should they be compressed? "
		),

		mapreduce_map_output_compress
		(
		"mapreduce.map.output.compress",
		"false",
		"Should the outputs of the maps be compressed before being sent across the network. Uses SequenceFile compression. "
		),

		mapreduce_map_output_compress_codec
		(
		"mapreduce.map.output.compress.codec",
		"org.apache.hadoop.io.compress.DefaultCodec",
		"If the map outputs are compressed how should they be compressed? "
		),

		map_sort_class
		(
		"map.sort.class",
		"org.apache.hadoop.util.QuickSort",
		"The default sort class for sorting keys. "
		),

		mapreduce_task_userlog_limit_kb
		(
		"mapreduce.task.userlog.limit.kb",
		"0",
		"The maximum size of user-logs of each task in KB. 0 disables the cap. "
		),

		yarn_app_mapreduce_am_container_log_limit_kb
		(
		"yarn.app.mapreduce.am.container.log.limit.kb",
		"0",
		"The maximum size of the MRAppMaster attempt container logs in KB. 0 disables the cap. "
		),

		yarn_app_mapreduce_task_container_log_backups
		(
		"yarn.app.mapreduce.task.container.log.backups",
		"0",
		"Number of backup files for task logs when using ContainerRollingLogAppender (CRLA). See org.apache.log4j.RollingFileAppender.maxBackupIndex. By default ContainerLogAppender (CLA) is used and container logs are not rolled. CRLA is enabled for tasks when both mapreduce.task.userlog.limit.kb and yarn.app.mapreduce.task.container.log.backups are greater than zero. "
		),

		yarn_app_mapreduce_am_container_log_backups
		(
		"yarn.app.mapreduce.am.container.log.backups",
		"0",
		"Number of backup files for the ApplicationMaster logs when using ContainerRollingLogAppender (CRLA). See org.apache.log4j.RollingFileAppender.maxBackupIndex. By default ContainerLogAppender (CLA) is used and container logs are not rolled. CRLA is enabled for the ApplicationMaster when both mapreduce.task.userlog.limit.kb and yarn.app.mapreduce.am.container.log.backups are greater than zero. "
		),

		yarn_app_mapreduce_shuffle_log_separate
		(
		"yarn.app.mapreduce.shuffle.log.separate",
		"true",
		"If enabled ('true') logging generated by the client-side shuffle classes in a reducer will be written in a dedicated log file 'syslog.shuffle' instead of 'syslog'. "
		),

		yarn_app_mapreduce_shuffle_log_limit_kb
		(
		"yarn.app.mapreduce.shuffle.log.limit.kb",
		"0",
		"Maximum size of the syslog.shuffle file in kilobytes (0 for no limit). "
		),

		yarn_app_mapreduce_shuffle_log_backups
		(
		"yarn.app.mapreduce.shuffle.log.backups",
		"0",
		"If yarn.app.mapreduce.shuffle.log.limit.kb and yarn.app.mapreduce.shuffle.log.backups are greater than zero then a ContainerRollngLogAppender is used instead of ContainerLogAppender for syslog.shuffle. See org.apache.log4j.RollingFileAppender.maxBackupIndex "
		),

		mapreduce_job_userlog_retain_hours
		(
		"mapreduce.job.userlog.retain.hours",
		"24",
		"The maximum time in hours for which the user-logs are to be retained after the job completion. "
		),

		mapreduce_jobtracker_hosts_filename
		(
		"mapreduce.jobtracker.hosts.filename",
		"",
		"Names a file that contains the list of nodes that may connect to the jobtracker. If the value is empty all hosts are permitted. "
		),

		mapreduce_jobtracker_hosts_exclude_filename
		(
		"mapreduce.jobtracker.hosts.exclude.filename",
		"",
		"Names a file that contains the list of hosts that should be excluded by the jobtracker. If the value is empty no hosts are excluded. "
		),

		mapreduce_jobtracker_heartbeats_in_second
		(
		"mapreduce.jobtracker.heartbeats.in.second",
		"100",
		"Expert: Approximate number of heart-beats that could arrive at JobTracker in a second. Assuming each RPC can be processed in 10msec the default value is made 100 RPCs in a second. "
		),

		mapreduce_jobtracker_tasktracker_maxblacklists
		(
		"mapreduce.jobtracker.tasktracker.maxblacklists",
		"4",
		"The number of blacklists for a taskTracker by various jobs after which the task tracker could be blacklisted across all jobs. The tracker will be given a tasks later (after a day). The tracker will become a healthy tracker after a restart. "
		),

		mapreduce_job_maxtaskfailures_per_tracker
		(
		"mapreduce.job.maxtaskfailures.per.tracker",
		"3",
		"The number of task-failures on a tasktracker of a given job after which new tasks of that job aren't assigned to it. It MUST be less than mapreduce.map.maxattempts and mapreduce.reduce.maxattempts otherwise the failed task will never be tried on a different node. "
		),

		mapreduce_client_output_filter
		(
		"mapreduce.client.output.filter",
		"FAILED",
		"The filter for controlling the output of the task's userlogs sent to the console of the JobClient. The permissible options are: NONE KILLED FAILED SUCCEEDED and ALL. "
		),

		mapreduce_client_completion_pollinterval
		(
		"mapreduce.client.completion.pollinterval",
		"5000",
		"The interval (in milliseconds) between which the JobClient polls the JobTracker for updates about job status. You may want to set this to a lower value to make tests run faster on a single node system. Adjusting this value in production may lead to unwanted client-server traffic. "
		),

		mapreduce_client_progressmonitor_pollinterval
		(
		"mapreduce.client.progressmonitor.pollinterval",
		"1000",
		"The interval (in milliseconds) between which the JobClient reports status to the console and checks for job completion. You may want to set this to a lower value to make tests run faster on a single node system. Adjusting this value in production may lead to unwanted client-server traffic. "
		),

		mapreduce_jobtracker_persist_jobstatus_active
		(
		"mapreduce.jobtracker.persist.jobstatus.active",
		"true",
		"Indicates if persistency of job status information is active or not. "
		),

		mapreduce_jobtracker_persist_jobstatus_hours
		(
		"mapreduce.jobtracker.persist.jobstatus.hours",
		"1",
		"The number of hours job status information is persisted in DFS. The job status information will be available after it drops of the memory queue and between jobtracker restarts. With a zero value the job status information is not persisted at all in DFS. "
		),

		mapreduce_jobtracker_persist_jobstatus_dir
		(
		"mapreduce.jobtracker.persist.jobstatus.dir",
		"/jobtracker/jobsInfo",
		"The directory where the job status information is persisted in a file system to be available after it drops of the memory queue and between jobtracker restarts. "
		),

		mapreduce_task_profile
		(
		"mapreduce.task.profile",
		"false",
		"To set whether the system should collect profiler information for some of the tasks in this job? The information is stored in the user log directory. The value is true if task profiling is enabled. "
		),

		mapreduce_task_profile_maps
		(
		"mapreduce.task.profile.maps",
		"0-2",
		"To set the ranges of map tasks to profile. mapreduce.task.profile has to be set to true for the value to be accounted. "
		),

		mapreduce_task_profile_reduces
		(
		"mapreduce.task.profile.reduces",
		"0-2",
		"To set the ranges of reduce tasks to profile. mapreduce.task.profile has to be set to true for the value to be accounted. "
		),

		mapreduce_task_profile_params
		(
		"mapreduce.task.profile.params",
		"-agentlib:hprof=cpu=samples,heap=sites,force=n,thread=y,verbose=n,file=%s",
		"JVM profiler parameters used to profile map and reduce task attempts. This string may contain a single format specifier %s that will be replaced by the path to profile.out in the task attempt log directory. To specify different profiling options for map tasks and reduce tasks more specific parameters mapreduce.task.profile.map.params and mapreduce.task.profile.reduce.params should be used. "
		),

		mapreduce_task_profile_map_params
		(
		"mapreduce.task.profile.map.params",
		"${mapreduce.task.profile.params}",
		"Map-task-specific JVM profiler parameters. See mapreduce.task.profile.params "
		),

		mapreduce_task_profile_reduce_params
		(
		"mapreduce.task.profile.reduce.params",
		"${mapreduce.task.profile.params}",
		"Reduce-task-specific JVM profiler parameters. See mapreduce.task.profile.params "
		),

		mapreduce_task_skip_start_attempts
		(
		"mapreduce.task.skip.start.attempts",
		"2",
		"The number of Task attempts AFTER which skip mode will be kicked off. When skip mode is kicked off the tasks reports the range of records which it will process next to the TaskTracker. So that on failures TT knows which ones are possibly the bad records. On further executions those are skipped. "
		),

		mapreduce_map_skip_proc_count_autoincr
		(
		"mapreduce.map.skip.proc.count.autoincr",
		"true",
		"The flag which if set to true SkipBadRecords.COUNTER_MAP_PROCESSED_RECORDS is incremented by MapRunner after invoking the map function. This value must be set to false for applications which process the records asynchronously or buffer the input records. For example streaming. In such cases applications should increment this counter on their own. "
		),

		mapreduce_reduce_skip_proc_count_autoincr
		(
		"mapreduce.reduce.skip.proc.count.autoincr",
		"true",
		"The flag which if set to true SkipBadRecords.COUNTER_REDUCE_PROCESSED_GROUPS is incremented by framework after invoking the reduce function. This value must be set to false for applications which process the records asynchronously or buffer the input records. For example streaming. In such cases applications should increment this counter on their own. "
		),

		mapreduce_job_skip_outdir
		(
		"mapreduce.job.skip.outdir",
		"",
		"If no value is specified here the skipped records are written to the output directory at _logs/skip. User can stop writing skipped records by giving the value none. "
		),

		mapreduce_map_skip_maxrecords
		(
		"mapreduce.map.skip.maxrecords",
		"0",
		"The number of acceptable skip records surrounding the bad record PER bad record in mapper. The number includes the bad record as well. To turn the feature of detection/skipping of bad records off set the value to 0. The framework tries to narrow down the skipped range by retrying until this threshold is met OR all attempts get exhausted for this task. Set the value to Long.MAX_VALUE to indicate that framework need not try to narrow down. Whatever records(depends on application) get skipped are acceptable. "
		),

		mapreduce_reduce_skip_maxgroups
		(
		"mapreduce.reduce.skip.maxgroups",
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

		mapreduce_jobtracker_taskcache_levels
		(
		"mapreduce.jobtracker.taskcache.levels",
		"2",
		"This is the max level of the task cache. For example if the level is 2 the tasks cached are at the host level and at the rack level. "
		),

		mapreduce_job_queuename
		(
		"mapreduce.job.queuename",
		"default",
		"Queue to which a job is submitted. This must match one of the queues defined in mapred-queues.xml for the system. Also the ACL setup for the queue must allow the current user to submit a job to the queue. Before specifying a queue ensure that the system is configured with the queue and access is allowed for submitting jobs to the queue. "
		),

		mapreduce_job_tags
		(
		"mapreduce.job.tags",
		"",
		"Tags for the job that will be passed to YARN at submission time. Queries to YARN for applications can filter on these tags. "
		),

		mapreduce_cluster_acls_enabled
		(
		"mapreduce.cluster.acls.enabled",
		"false",
		"Specifies whether ACLs should be checked for authorization of users for doing various queue and job level operations. ACLs are disabled by default. If enabled access control checks are made by JobTracker and TaskTracker when requests are made by users for queue operations like submit job to a queue and kill a job in the queue and job operations like viewing the job-details (See mapreduce.job.acl-view-job) or for modifying the job (See mapreduce.job.acl-modify-job) using Map/Reduce APIs RPCs or via the console and web user interfaces. For enabling this flag(mapreduce.cluster.acls.enabled) this is to be set to true in mapred-site.xml on JobTracker node and on all TaskTracker nodes. "
		),

		mapreduce_job_acl_modify_job
		(
		"mapreduce.job.acl-modify-job",
		"",
		"Job specific access-control list for 'modifying' the job. It is only used if authorization is enabled in Map/Reduce by setting the configuration property mapreduce.cluster.acls.enabled to true. This specifies the list of users and/or groups who can do modification operations on the job. For specifying a list of users and groups the format to use is user1 user2 group1 group. If set to '*' it allows all users/groups to modify this job. If set to ' '(i.e. space) it allows none. This configuration is used to guard all the modifications with respect to this job and takes care of all the following operations: o killing this job o killing a task of this job failing a task of this job o setting the priority of this job Each of these operations are also protected by the per-queue level ACL acl-administer-jobs configured via mapred-queues.xml. So a caller should have the authorization to satisfy either the queue-level ACL or the job-level ACL. Irrespective of this ACL configuration (a) job-owner (b) the user who started the cluster (c) members of an admin configured supergroup configured via mapreduce.cluster.permissions.supergroup and (d) queue administrators of the queue to which this job was submitted to configured via acl-administer-jobs for the specific queue in mapred-queues.xml can do all the modification operations on a job. By default nobody else besides job-owner the user who started the cluster members of supergroup and queue administrators can perform modification operations on a job. "
		),

		mapreduce_job_acl_view_job
		(
		"mapreduce.job.acl-view-job",
		"",
		"Job specific access-control list for 'viewing' the job. It is only used if authorization is enabled in Map/Reduce by setting the configuration property mapreduce.cluster.acls.enabled to true. This specifies the list of users and/or groups who can view private details about the job. For specifying a list of users and groups the format to use is user1 user2 group1 group. If set to '*' it allows all users/groups to modify this job. If set to ' '(i.e. space) it allows none. This configuration is used to guard some of the job-views and at present only protects APIs that can return possibly sensitive information of the job-owner like o job-level counters o task-level counters o tasks' diagnostic information o task-logs displayed on the TaskTracker web-UI and o job.xml showed by the JobTracker's web-UI Every other piece of information of jobs is still accessible by any other user for e.g. JobStatus JobProfile list of jobs in the queue etc. Irrespective of this ACL configuration (a) job-owner (b) the user who started the cluster (c) members of an admin configured supergroup configured via mapreduce.cluster.permissions.supergroup and (d) queue administrators of the queue to which this job was submitted to configured via acl-administer-jobs for the specific queue in mapred-queues.xml can do all the view operations on a job. By default nobody else besides job-owner the user who started the cluster memebers of supergroup and queue administrators can perform view operations on a job. "
		),

		mapreduce_tasktracker_indexcache_mb
		(
		"mapreduce.tasktracker.indexcache.mb",
		"10",
		"The maximum memory that a task tracker allows for the index cache that is used when serving map outputs to reducers. "
		),

		mapreduce_job_token_tracking_ids_enabled
		(
		"mapreduce.job.token.tracking.ids.enabled",
		"false",
		"Whether to write tracking ids of tokens to job-conf. When true the configuration property mapreduce.job.token.tracking.ids is set to the token-tracking-ids of the job "
		),

		mapreduce_job_token_tracking_ids
		(
		"mapreduce.job.token.tracking.ids",
		"",
		"When mapreduce.job.token.tracking.ids.enabled is set to true this is set by the framework to the token-tracking-ids used by the job. "
		),

		mapreduce_task_merge_progress_records
		(
		"mapreduce.task.merge.progress.records",
		"10000",
		"The number of records to process during merge before sending a progress notification to the TaskTracker. "
		),

		mapreduce_task_combine_progress_records
		(
		"mapreduce.task.combine.progress.records",
		"10000",
		"The number of records to process during combine output collection before sending a progress notification. "
		),

		mapreduce_job_reduce_slowstart_completedmaps
		(
		"mapreduce.job.reduce.slowstart.completedmaps",
		"0.05",
		"Fraction of the number of maps in the job which should be complete before reduces are scheduled for the job. "
		),

		mapreduce_job_complete_cancel_delegation_tokens
		(
		"mapreduce.job.complete.cancel.delegation.tokens",
		"true",
		"if false - do not unregister/cancel delegation tokens from renewal because same tokens may be used by spawned jobs "
		),

		mapreduce_tasktracker_taskcontroller
		(
		"mapreduce.tasktracker.taskcontroller",
		"org.apache.hadoop.mapred.DefaultTaskController",
		"TaskController which is used to launch and manage task execution "
		),

		mapreduce_tasktracker_group
		(
		"mapreduce.tasktracker.group",
		"",
		"Expert: Group to which TaskTracker belongs. If LinuxTaskController is configured via mapreduce.tasktracker.taskcontroller the group owner of the task-controller binary should be same as this group. "
		),

		mapreduce_shuffle_port
		(
		"mapreduce.shuffle.port",
		"13562",
		"Default port that the ShuffleHandler will run on. ShuffleHandler is a service run at the NodeManager to facilitate transfers of intermediate Map outputs to requesting Reducers. "
		),

		mapreduce_job_reduce_shuffle_consumer_plugin_class
		(
		"mapreduce.job.reduce.shuffle.consumer.plugin.class",
		"org.apache.hadoop.mapreduce.task.reduce.Shuffle",
		"Name of the class whose instance will be used to send shuffle requests by reducetasks of this job. The class must be an instance of org.apache.hadoop.mapred.ShuffleConsumerPlugin. "
		),

		mapreduce_tasktracker_healthchecker_script_path
		(
		"mapreduce.tasktracker.healthchecker.script.path",
		"",
		"Absolute path to the script which is periodicallyrun by the node health monitoring service to determine if the node is healthy or not. If the value of this key is empty or the file does not exist in the location configured here the node health monitoring service is not started. "
		),

		mapreduce_tasktracker_healthchecker_interval
		(
		"mapreduce.tasktracker.healthchecker.interval",
		"60000",
		"Frequency of the node health script to be run in milliseconds "
		),

		mapreduce_tasktracker_healthchecker_script_timeout
		(
		"mapreduce.tasktracker.healthchecker.script.timeout",
		"600000",
		"Time after node health script should be killed if unresponsive and considered that the script has failed. "
		),

		mapreduce_tasktracker_healthchecker_script_args
		(
		"mapreduce.tasktracker.healthchecker.script.args",
		"",
		"List of arguments which are to be passed to node health script when it is being launched comma seperated. "
		),

		mapreduce_job_counters_limit
		(
		"mapreduce.job.counters.limit",
		"120",
		"Limit on the number of user counters allowed per job. "
		),

		mapreduce_framework_name
		(
		"mapreduce.framework.name",
		"local",
		"The runtime framework for executing MapReduce jobs. Can be one of local classic or yarn. "
		),

		yarn_app_mapreduce_am_staging_dir
		(
		"yarn.app.mapreduce.am.staging-dir",
		"/tmp/hadoop-yarn/staging",
		"The staging dir used while submitting jobs. "
		),

		mapreduce_am_max_attempts
		(
		"mapreduce.am.max-attempts",
		"2",
		"The maximum number of application attempts. It is a application-specific setting. It should not be larger than the global number set by resourcemanager. Otherwise it will be override. The default number is set to 2 to allow at least one retry for AM. "
		),

		mapreduce_job_end_notification_url
		(
		"mapreduce.job.end-notification.url",
		"",
		"Indicates url which will be called on completion of job to inform end status of job. User can give at most 2 variables with URI : $jobId and $jobStatus. If they are present in URI then they will be replaced by their respective values. "
		),

		mapreduce_job_end_notification_retry_attempts
		(
		"mapreduce.job.end-notification.retry.attempts",
		"0",
		"The number of times the submitter of the job wants to retry job end notification if it fails. This is capped by mapreduce.job.end-notification.max.attempts "
		),

		mapreduce_job_end_notification_retry_interval
		(
		"mapreduce.job.end-notification.retry.interval",
		"1000",
		"The number of milliseconds the submitter of the job wants to wait before job end notification is retried if it fails. This is capped by mapreduce.job.end-notification.max.retry.interval "
		),

		mapreduce_job_end_notification_max_attempts
		(
		"mapreduce.job.end-notification.max.attempts",
		"5",
		"The maximum number of times a URL will be read for providing job end notification. Cluster administrators can set this to limit how long after end of a job the Application Master waits before exiting. Must be marked as final to prevent users from overriding this. "
		),

		mapreduce_job_log4j_properties_file
		(
		"mapreduce.job.log4j-properties-file",
		"",
		"Used to override the default settings of log4j in container-log4j.properties for NodeManager. Like container-log4j.properties it requires certain framework appenders properly defined in this overriden file. The file on the path will be added to distributed cache and classpath. If no-scheme is given in the path it defaults to point to a log4j file on the local FS. "
		),

		mapreduce_job_end_notification_max_retry_interval
		(
		"mapreduce.job.end-notification.max.retry.interval",
		"5000",
		"The maximum amount of time (in milliseconds) to wait before retrying job end notification. Cluster administrators can set this to limit how long the Application Master waits before exiting. Must be marked as final to prevent users from overriding this. "
		),

		yarn_app_mapreduce_am_env
		(
		"yarn.app.mapreduce.am.env",
		"",
		"User added environment variables for the MR App Master processes. Example : 1) A=foo This will set the env variable A to foo 2) B=$B:c This is inherit tasktracker's B env variable. "
		),

		yarn_app_mapreduce_am_admin_user_env
		(
		"yarn.app.mapreduce.am.admin.user.env",
		"",
		"Environment variables for the MR App Master processes for admin purposes. These values are set first and can be overridden by the user env (yarn.app.mapreduce.am.env) Example : 1) A=foo This will set the env variable A to foo 2) B=$B:c This is inherit app master's B env variable. "
		),

		yarn_app_mapreduce_am_command_opts
		(
		"yarn.app.mapreduce.am.command-opts",
		"-Xmx1024m",
		"Java opts for the MR App Master processes. The following symbol if present will be interpolated: @taskid@ is replaced by current TaskID. Any other occurrences of '@' will go unchanged. For example to enable verbose gc logging to a file named for the taskid in /tmp and to set the heap maximum to be a gigabyte pass a 'value' of: -Xmx1024m -verbose:gc -Xloggc:/tmp/@taskid@.gc Usage of -Djava.library.path can cause programs to no longer function if hadoop native libraries are used. These values should instead be set as part of LD_LIBRARY_PATH in the map / reduce JVM env using the mapreduce.map.env and mapreduce.reduce.env config settings. "
		),

		yarn_app_mapreduce_am_admin_command_opts
		(
		"yarn.app.mapreduce.am.admin-command-opts",
		"",
		"Java opts for the MR App Master processes for admin purposes. It will appears before the opts set by yarn.app.mapreduce.am.command-opts and thus its options can be overridden user. Usage of -Djava.library.path can cause programs to no longer function if hadoop native libraries are used. These values should instead be set as part of LD_LIBRARY_PATH in the map / reduce JVM env using the mapreduce.map.env and mapreduce.reduce.env config settings. "
		),

		yarn_app_mapreduce_am_job_task_listener_thread_count
		(
		"yarn.app.mapreduce.am.job.task.listener.thread-count",
		"30",
		"The number of threads used to handle RPC calls in the MR AppMaster from remote tasks "
		),

		yarn_app_mapreduce_am_job_client_port_range
		(
		"yarn.app.mapreduce.am.job.client.port-range",
		"",
		"Range of ports that the MapReduce AM can use when binding. Leave blank if you want all possible ports. For example 50000-50050 50100-50200 "
		),

		yarn_app_mapreduce_am_job_committer_cancel_timeout
		(
		"yarn.app.mapreduce.am.job.committer.cancel-timeout",
		"60000",
		"The amount of time in milliseconds to wait for the output committer to cancel an operation if the job is killed "
		),

		yarn_app_mapreduce_am_job_committer_commit_window
		(
		"yarn.app.mapreduce.am.job.committer.commit-window",
		"10000",
		"Defines a time window in milliseconds for output commit operations. If contact with the RM has occurred within this window then commits are allowed otherwise the AM will not allow output commits until contact with the RM has been re-established. "
		),

		mapreduce_fileoutputcommitter_algorithm_version
		(
		"mapreduce.fileoutputcommitter.algorithm.version",
		"1",
		"The file output committer algorithm version valid algorithm version number: 1 or 2 default to 1 which is the original algorithm In algorithm version 1 1. commitTask will rename directory $joboutput/_temporary/$appAttemptID/_temporary/$taskAttemptID/ to $joboutput/_temporary/$appAttemptID/$taskID/ 2. recoverTask will also do a rename $joboutput/_temporary/$appAttemptID/$taskID/ to $joboutput/_temporary/($appAttemptID + 1)/$taskID/ 3. commitJob will merge every task output file in $joboutput/_temporary/$appAttemptID/$taskID/ to $joboutput/ then it will delete $joboutput/_temporary/ and write $joboutput/_SUCCESS It has a performance regression which is discussed in MAPREDUCE-4815. If a job generates many files to commit then the commitJob method call at the end of the job can take minutes. the commit is single-threaded and waits until all tasks have completed before commencing. algorithm version 2 will change the behavior of commitTask recoverTask and commitJob. 1. commitTask will rename all files in $joboutput/_temporary/$appAttemptID/_temporary/$taskAttemptID/ to $joboutput/ 2. recoverTask actually doesn't require to do anything but for upgrade from version 1 to version 2 case it will check if there are any files in $joboutput/_temporary/($appAttemptID - 1)/$taskID/ and rename them to $joboutput/ 3. commitJob can simply delete $joboutput/_temporary and write $joboutput/_SUCCESS This algorithm will reduce the output commit time for large jobs by having the tasks commit directly to the final output directory as they were completing and commitJob had very little to do. "
		),

		yarn_app_mapreduce_am_scheduler_heartbeat_interval_ms
		(
		"yarn.app.mapreduce.am.scheduler.heartbeat.interval-ms",
		"1000",
		"The interval in ms at which the MR AppMaster should send heartbeats to the ResourceManager "
		),

		yarn_app_mapreduce_client_am_ipc_max_retries
		(
		"yarn.app.mapreduce.client-am.ipc.max-retries",
		"3",
		"The number of client retries to the AM - before reconnecting to the RM to fetch Application Status. "
		),

		yarn_app_mapreduce_client_am_ipc_max_retries_on_timeouts
		(
		"yarn.app.mapreduce.client-am.ipc.max-retries-on-timeouts",
		"3",
		"The number of client retries on socket timeouts to the AM - before reconnecting to the RM to fetch Application Status. "
		),

		yarn_app_mapreduce_client_max_retries
		(
		"yarn.app.mapreduce.client.max-retries",
		"3",
		"The number of client retries to the RM/HS before throwing exception. This is a layer above the ipc. "
		),

		yarn_app_mapreduce_am_resource_mb
		(
		"yarn.app.mapreduce.am.resource.mb",
		"1536",
		"The amount of memory the MR AppMaster needs. "
		),

		yarn_app_mapreduce_am_resource_cpu_vcores
		(
		"yarn.app.mapreduce.am.resource.cpu-vcores",
		"1",
		"The number of virtual CPU cores the MR AppMaster needs. "
		),

		yarn_app_mapreduce_am_hard_kill_timeout_ms
		(
		"yarn.app.mapreduce.am.hard-kill-timeout-ms",
		"10000",
		"Number of milliseconds to wait before the job client kills the application. "
		),

		mapreduce_application_classpath
		(
		"mapreduce.application.classpath",
		"",
		"CLASSPATH for MR applications. A comma-separated list of CLASSPATH entries. If mapreduce.application.framework is set then this must specify the appropriate classpath for that archive and the name of the archive must be present in the classpath. If mapreduce.app-submission.cross-platform is false platform-specific environment vairable expansion syntax would be used to construct the default CLASSPATH entries. For Linux: $HADOOP_MAPRED_HOME/share/hadoop/mapreduce/* $HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*. For Windows: %HADOOP_MAPRED_HOME%/share/hadoop/mapreduce/* %HADOOP_MAPRED_HOME%/share/hadoop/mapreduce/lib/*. If mapreduce.app-submission.cross-platform is true platform-agnostic default CLASSPATH for MR applications would be used: {{HADOOP_MAPRED_HOME}}/share/hadoop/mapreduce/* {{HADOOP_MAPRED_HOME}}/share/hadoop/mapreduce/lib/* Parameter expansion marker will be replaced by NodeManager on container launch based on the underlying OS accordingly. "
		),

		mapreduce_app_submission_cross_platform
		(
		"mapreduce.app-submission.cross-platform",
		"false",
		"If enabled user can submit an application cross-platform i.e. submit an application from a Windows client to a Linux/Unix server or vice versa. "
		),

		mapreduce_application_framework_path
		(
		"mapreduce.application.framework.path",
		"",
		"Path to the MapReduce framework archive. If set the framework archive will automatically be distributed along with the job and this path would normally reside in a public location in an HDFS filesystem. As with distributed cache files this can be a URL with a fragment specifying the alias to use for the archive name. For example hdfs:/mapred/framework/hadoop-mapreduce-2.1.1.tar.gz#mrframework would alias the localized archive as mrframework. Note that mapreduce.application.classpath must include the appropriate classpath for the specified framework. The base name of the archive or alias of the archive if an alias is used must appear in the specified classpath. "
		),

		mapreduce_job_classloader
		(
		"mapreduce.job.classloader",
		"false",
		"Whether to use a separate (isolated) classloader for user classes in the task JVM. "
		),

		mapreduce_job_classloader_system_classes
		(
		"mapreduce.job.classloader.system.classes",
		"",
		"Used to override the default definition of the system classes for the job classloader. The system classes are a comma-separated list of patterns that indicate whether to load a class from the system classpath instead from the user-supplied JARs when mapreduce.job.classloader is enabled. A positive pattern is defined as: 1. A single class name 'C' that matches 'C' and transitively all nested classes 'C$*' defined in C; 2. A package name ending with a '.' (e.g. com.example.) that matches all classes from that package. A negative pattern is defined by a '-' in front of a positive pattern (e.g. -com.example.). A class is considered a system class if and only if it matches one of the positive patterns and none of the negative ones. More formally: A class is a member of the inclusion set I if it matches one of the positive patterns. A class is a member of the exclusion set E if it matches one of the negative patterns. The set of system classes S = I \\ E. "
		),

		mapreduce_jobhistory_address
		(
		"mapreduce.jobhistory.address",
		"0.0.0.0:10020",
		"MapReduce JobHistory Server IPC host:port "
		),

		mapreduce_jobhistory_webapp_address
		(
		"mapreduce.jobhistory.webapp.address",
		"0.0.0.0:19888",
		"MapReduce JobHistory Server Web UI host:port "
		),

		mapreduce_jobhistory_keytab
		(
		"mapreduce.jobhistory.keytab",
		"/etc/security/keytab/jhs.service.keytab",
		"Location of the kerberos keytab file for the MapReduce JobHistory Server. "
		),

		mapreduce_jobhistory_principal
		(
		"mapreduce.jobhistory.principal",
		"jhs/_HOST@REALM.TLD",
		"Kerberos principal name for the MapReduce JobHistory Server. "
		),

		mapreduce_jobhistory_intermediate_done_dir
		(
		"mapreduce.jobhistory.intermediate-done-dir",
		"${yarn.app.mapreduce.am.staging-dir}/history/done_intermediate",
		" "
		),

		mapreduce_jobhistory_done_dir
		(
		"mapreduce.jobhistory.done-dir",
		"${yarn.app.mapreduce.am.staging-dir}/history/done",
		" "
		),

		mapreduce_jobhistory_cleaner_enable
		(
		"mapreduce.jobhistory.cleaner.enable",
		"true",
		" "
		),

		mapreduce_jobhistory_cleaner_interval_ms
		(
		"mapreduce.jobhistory.cleaner.interval-ms",
		"86400000",
		"How often the job history cleaner checks for files to delete in milliseconds. Defaults to 86400000 (one day). Files are only deleted if they are older than mapreduce.jobhistory.max-age-ms. "
		),

		mapreduce_jobhistory_max_age_ms
		(
		"mapreduce.jobhistory.max-age-ms",
		"604800000",
		"Job history files older than this many milliseconds will be deleted when the history cleaner runs. Defaults to 604800000 (1 week). "
		),

		mapreduce_jobhistory_client_thread_count
		(
		"mapreduce.jobhistory.client.thread-count",
		"10",
		"The number of threads to handle client API requests "
		),

		mapreduce_jobhistory_datestring_cache_size
		(
		"mapreduce.jobhistory.datestring.cache.size",
		"200000",
		"Size of the date string cache. Effects the number of directories which will be scanned to find a job. "
		),

		mapreduce_jobhistory_joblist_cache_size
		(
		"mapreduce.jobhistory.joblist.cache.size",
		"20000",
		"Size of the job list cache "
		),

		mapreduce_jobhistory_loadedjobs_cache_size
		(
		"mapreduce.jobhistory.loadedjobs.cache.size",
		"5",
		"Size of the loaded job cache "
		),

		mapreduce_jobhistory_move_interval_ms
		(
		"mapreduce.jobhistory.move.interval-ms",
		"180000",
		"Scan for history files to more from intermediate done dir to done dir at this frequency. "
		),

		mapreduce_jobhistory_move_thread_count
		(
		"mapreduce.jobhistory.move.thread-count",
		"3",
		"The number of threads used to move files. "
		),

		mapreduce_jobhistory_store_class
		(
		"mapreduce.jobhistory.store.class",
		"",
		"The HistoryStorage class to use to cache history data. "
		),

		mapreduce_jobhistory_minicluster_fixed_ports
		(
		"mapreduce.jobhistory.minicluster.fixed.ports",
		"false",
		"Whether to use fixed ports with the minicluster "
		),

		mapreduce_jobhistory_admin_address
		(
		"mapreduce.jobhistory.admin.address",
		"0.0.0.0:10033",
		"The address of the History server admin interface. "
		),

		mapreduce_jobhistory_admin_acl
		(
		"mapreduce.jobhistory.admin.acl",
		"*",
		"ACL of who can be admin of the History server. "
		),

		mapreduce_jobhistory_recovery_enable
		(
		"mapreduce.jobhistory.recovery.enable",
		"false",
		"Enable the history server to store server state and recover server state upon startup. If enabled then mapreduce.jobhistory.recovery.store.class must be specified. "
		),

		mapreduce_jobhistory_recovery_store_class
		(
		"mapreduce.jobhistory.recovery.store.class",
		"org.apache.hadoop.mapreduce.v2.hs.HistoryServerFileSystemStateStoreService",
		"The HistoryServerStateStoreService class to store history server state for recovery. "
		),

		mapreduce_jobhistory_recovery_store_fs_uri
		(
		"mapreduce.jobhistory.recovery.store.fs.uri",
		"${hadoop.tmp.dir}/mapred/history/recoverystore",
		"The URI where history server state will be stored if HistoryServerFileSystemStateStoreService is configured as the recovery storage class. "
		),

		mapreduce_jobhistory_recovery_store_leveldb_path
		(
		"mapreduce.jobhistory.recovery.store.leveldb.path",
		"${hadoop.tmp.dir}/mapred/history/recoverystore",
		"The URI where history server state will be stored if HistoryServerLeveldbSystemStateStoreService is configured as the recovery storage class. "
		),

		mapreduce_jobhistory_http_policy
		(
		"mapreduce.jobhistory.http.policy",
		"HTTP_ONLY",
		"This configures the HTTP endpoint for JobHistoryServer web UI. The following values are supported: - HTTP_ONLY : Service is provided only on http - HTTPS_ONLY : Service is provided only on https "
		),

		yarn_app_mapreduce_am_containerlauncher_threadpool_initial_size
		(
		"yarn.app.mapreduce.am.containerlauncher.threadpool-initial-size",
		"10",
		"The initial size of thread pool to launch containers in the app master. "
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

	enum YARN {
		yarn_ipc_client_factory_class
		(
		"yarn.ipc.client.factory.class",
		"",
		"Factory to create client IPC classes. "
		),

		yarn_ipc_server_factory_class
		(
		"yarn.ipc.server.factory.class",
		"",
		"Factory to create server IPC classes. "
		),

		yarn_ipc_record_factory_class
		(
		"yarn.ipc.record.factory.class",
		"",
		"Factory to create serializeable records. "
		),

		yarn_ipc_rpc_class
		(
		"yarn.ipc.rpc.class",
		"org.apache.hadoop.yarn.ipc.HadoopYarnProtoRPC",
		"RPC class implementation "
		),

		yarn_resourcemanager_hostname
		(
		"yarn.resourcemanager.hostname",
		"0.0.0.0",
		"The hostname of the RM. "
		),

		yarn_resourcemanager_address
		(
		"yarn.resourcemanager.address",
		"${yarn.resourcemanager.hostname}:8032",
		"The address of the applications manager interface in the RM. "
		),

		yarn_resourcemanager_bind_host
		(
		"yarn.resourcemanager.bind-host",
		"",
		"The actual address the server will bind to. If this optional address is set the RPC and webapp servers will bind to this address and the port specified in yarn.resourcemanager.address and yarn.resourcemanager.webapp.address respectively. This is most useful for making RM listen to all interfaces by setting to 0.0.0.0. "
		),

		yarn_resourcemanager_client_thread_count
		(
		"yarn.resourcemanager.client.thread-count",
		"50",
		"The number of threads used to handle applications manager requests. "
		),

		yarn_am_liveness_monitor_expiry_interval_ms
		(
		"yarn.am.liveness-monitor.expiry-interval-ms",
		"600000",
		"The expiry interval for application master reporting. "
		),

		yarn_resourcemanager_principal
		(
		"yarn.resourcemanager.principal",
		"",
		"The Kerberos principal for the resource manager. "
		),

		yarn_resourcemanager_scheduler_address
		(
		"yarn.resourcemanager.scheduler.address",
		"${yarn.resourcemanager.hostname}:8030",
		"The address of the scheduler interface. "
		),

		yarn_resourcemanager_scheduler_client_thread_count
		(
		"yarn.resourcemanager.scheduler.client.thread-count",
		"50",
		"Number of threads to handle scheduler interface. "
		),

		yarn_http_policy
		(
		"yarn.http.policy",
		"HTTP_ONLY",
		"This configures the HTTP endpoint for Yarn Daemons.The following values are supported: - HTTP_ONLY : Service is provided only on http - HTTPS_ONLY : Service is provided only on https "
		),

		yarn_resourcemanager_webapp_address
		(
		"yarn.resourcemanager.webapp.address",
		"${yarn.resourcemanager.hostname}:8088",
		"The http address of the RM web application. "
		),

		yarn_resourcemanager_webapp_https_address
		(
		"yarn.resourcemanager.webapp.https.address",
		"${yarn.resourcemanager.hostname}:8090",
		"The https adddress of the RM web application. "
		),

		yarn_resourcemanager_resource_tracker_address
		(
		"yarn.resourcemanager.resource-tracker.address",
		"${yarn.resourcemanager.hostname}:8031",
		""
		),

		yarn_acl_enable
		(
		"yarn.acl.enable",
		"false",
		"Are acls enabled. "
		),

		yarn_admin_acl
		(
		"yarn.admin.acl",
		"*",
		"ACL of who can be admin of the YARN cluster. "
		),

		yarn_resourcemanager_admin_address
		(
		"yarn.resourcemanager.admin.address",
		"${yarn.resourcemanager.hostname}:8033",
		"The address of the RM admin interface. "
		),

		yarn_resourcemanager_admin_client_thread_count
		(
		"yarn.resourcemanager.admin.client.thread-count",
		"1",
		"Number of threads used to handle RM admin interface. "
		),

		yarn_resourcemanager_connect_max_wait_ms
		(
		"yarn.resourcemanager.connect.max-wait.ms",
		"900000",
		"Maximum time to wait to establish connection to ResourceManager. "
		),

		yarn_resourcemanager_connect_retry_interval_ms
		(
		"yarn.resourcemanager.connect.retry-interval.ms",
		"30000",
		"How often to try connecting to the ResourceManager. "
		),

		yarn_resourcemanager_am_max_attempts
		(
		"yarn.resourcemanager.am.max-attempts",
		"2",
		"The maximum number of application attempts. It's a global setting for all application masters. Each application master can specify its individual maximum number of application attempts via the API but the individual number cannot be more than the global upper bound. If it is the resourcemanager will override it. The default number is set to 2 to allow at least one retry for AM. "
		),

		yarn_resourcemanager_container_liveness_monitor_interval_ms
		(
		"yarn.resourcemanager.container.liveness-monitor.interval-ms",
		"600000",
		"How often to check that containers are still alive. "
		),

		yarn_resourcemanager_keytab
		(
		"yarn.resourcemanager.keytab",
		"/etc/krb5.keytab",
		"The keytab for the resource manager. "
		),

		yarn_resourcemanager_webapp_delegation_token_auth_filter_enabled
		(
		"yarn.resourcemanager.webapp.delegation-token-auth-filter.enabled",
		"true",
		"Flag to enable override of the default kerberos authentication filter with the RM authentication filter to allow authentication using delegation tokens(fallback to kerberos if the tokens are missing). Only applicable when the http authentication type is kerberos. "
		),

		yarn_nm_liveness_monitor_expiry_interval_ms
		(
		"yarn.nm.liveness-monitor.expiry-interval-ms",
		"600000",
		"How long to wait until a node manager is considered dead. "
		),

		yarn_resourcemanager_nodes_include_path
		(
		"yarn.resourcemanager.nodes.include-path",
		"",
		"Path to file with nodes to include. "
		),

		yarn_resourcemanager_nodes_exclude_path
		(
		"yarn.resourcemanager.nodes.exclude-path",
		"",
		"Path to file with nodes to exclude. "
		),

		yarn_resourcemanager_resource_tracker_client_thread_count
		(
		"yarn.resourcemanager.resource-tracker.client.thread-count",
		"50",
		"Number of threads to handle resource tracker calls. "
		),

		yarn_resourcemanager_scheduler_class
		(
		"yarn.resourcemanager.scheduler.class",
		"org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler",
		"The class to use as the resource scheduler. "
		),

		yarn_scheduler_minimum_allocation_mb
		(
		"yarn.scheduler.minimum-allocation-mb",
		"1024",
		"The minimum allocation for every container request at the RM in MBs. Memory requests lower than this will throw a InvalidResourceRequestException. "
		),

		yarn_scheduler_maximum_allocation_mb
		(
		"yarn.scheduler.maximum-allocation-mb",
		"8192",
		"The maximum allocation for every container request at the RM in MBs. Memory requests higher than this will throw a InvalidResourceRequestException. "
		),

		yarn_scheduler_minimum_allocation_vcores
		(
		"yarn.scheduler.minimum-allocation-vcores",
		"1",
		"The minimum allocation for every container request at the RM in terms of virtual CPU cores. Requests lower than this will throw a InvalidResourceRequestException. "
		),

		yarn_scheduler_maximum_allocation_vcores
		(
		"yarn.scheduler.maximum-allocation-vcores",
		"32",
		"The maximum allocation for every container request at the RM in terms of virtual CPU cores. Requests higher than this will throw a InvalidResourceRequestException. "
		),

		yarn_resourcemanager_recovery_enabled
		(
		"yarn.resourcemanager.recovery.enabled",
		"false",
		"Enable RM to recover state after starting. If true then yarn.resourcemanager.store.class must be specified. "
		),

		yarn_resourcemanager_work_preserving_recovery_enabled
		(
		"yarn.resourcemanager.work-preserving-recovery.enabled",
		"true",
		"Enable RM work preserving recovery. This configuration is private to YARN for experimenting the feature. "
		),

		yarn_resourcemanager_work_preserving_recovery_scheduling_wait_ms
		(
		"yarn.resourcemanager.work-preserving-recovery.scheduling-wait-ms",
		"10000",
		"Set the amount of time RM waits before allocating new containers on work-preserving-recovery. Such wait period gives RM a chance to settle down resyncing with NMs in the cluster on recovery before assigning new containers to applications. "
		),

		yarn_resourcemanager_store_class
		(
		"yarn.resourcemanager.store.class",
		"org.apache.hadoop.yarn.server.resourcemanager.recovery.FileSystemRMStateStore",
		"The class to use as the persistent store. If org.apache.hadoop.yarn.server.resourcemanager.recovery.ZKRMStateStore is used the store is implicitly fenced; meaning a single ResourceManager is able to use the store at any point in time. More details on this implicit fencing along with setting up appropriate ACLs is discussed under yarn.resourcemanager.zk-state-store.root-node.acl. "
		),

		yarn_resourcemanager_state_store_max_completed_applications
		(
		"yarn.resourcemanager.state-store.max-completed-applications",
		"${yarn.resourcemanager.max-completed-applications}",
		"The maximum number of completed applications RM state store keeps less than or equals to ${yarn.resourcemanager.max-completed-applications}. By default it equals to ${yarn.resourcemanager.max-completed-applications}. This ensures that the applications kept in the state store are consistent with the applications remembered in RM memory. Any values larger than ${yarn.resourcemanager.max-completed-applications} will be reset to ${yarn.resourcemanager.max-completed-applications}. Note that this value impacts the RM recovery performance.Typically a smaller value indicates better performance on RM recovery. "
		),

		yarn_resourcemanager_zk_address
		(
		"yarn.resourcemanager.zk-address",
		"",
		"Host:Port of the ZooKeeper server to be used by the RM. This must be supplied when using the ZooKeeper based implementation of the RM state store and/or embedded automatic failover in a HA setting. "
		),

		yarn_resourcemanager_zk_num_retries
		(
		"yarn.resourcemanager.zk-num-retries",
		"1000",
		"Number of times RM tries to connect to ZooKeeper. "
		),

		yarn_resourcemanager_zk_retry_interval_ms
		(
		"yarn.resourcemanager.zk-retry-interval-ms",
		"1000",
		"Retry interval in milliseconds when connecting to ZooKeeper. When HA is enabled the value here is NOT used. It is generated automatically from yarn.resourcemanager.zk-timeout-ms and yarn.resourcemanager.zk-num-retries. "
		),

		yarn_resourcemanager_zk_state_store_parent_path
		(
		"yarn.resourcemanager.zk-state-store.parent-path",
		"/rmstore",
		"Full path of the ZooKeeper znode where RM state will be stored. This must be supplied when using org.apache.hadoop.yarn.server.resourcemanager.recovery.ZKRMStateStore as the value for yarn.resourcemanager.store.class "
		),

		yarn_resourcemanager_zk_timeout_ms
		(
		"yarn.resourcemanager.zk-timeout-ms",
		"10000",
		"ZooKeeper session timeout in milliseconds. Session expiration is managed by the ZooKeeper cluster itself not by the client. This value is used by the cluster to determine when the client's session expires. Expirations happens when the cluster does not hear from the client within the specified session timeout period (i.e. no heartbeat). "
		),

		yarn_resourcemanager_zk_acl
		(
		"yarn.resourcemanager.zk-acl",
		"world:anyone:rwcda",
		"ACL's to be used for ZooKeeper znodes. "
		),

		yarn_resourcemanager_zk_state_store_root_node_acl
		(
		"yarn.resourcemanager.zk-state-store.root-node.acl",
		"",
		"ACLs to be used for the root znode when using ZKRMStateStore in a HA scenario for fencing. ZKRMStateStore supports implicit fencing to allow a single ResourceManager write-access to the store. For fencing the ResourceManagers in the cluster share read-write-admin privileges on the root node but the Active ResourceManager claims exclusive create-delete permissions. By default when this property is not set we use the ACLs from yarn.resourcemanager.zk-acl for shared admin access and rm-address:random-number for username-based exclusive create-delete access. This property allows users to set ACLs of their choice instead of using the default mechanism. For fencing to work the ACLs should be carefully set differently on each ResourceManger such that all the ResourceManagers have shared admin access and the Active ResourceManger takes over (exclusively) the create-delete access. "
		),

		yarn_resourcemanager_zk_auth
		(
		"yarn.resourcemanager.zk-auth",
		"",
		"Specify the auths to be used for the ACL's specified in both the yarn.resourcemanager.zk-acl and yarn.resourcemanager.zk-state-store.root-node.acl properties. This takes a comma-separated list of authentication mechanisms each of the form 'scheme:auth' (the same syntax used for the 'addAuth' command in the ZK CLI). "
		),

		yarn_resourcemanager_fs_state_store_uri
		(
		"yarn.resourcemanager.fs.state-store.uri",
		"${hadoop.tmp.dir}/yarn/system/rmstore",
		"URI pointing to the location of the FileSystem path where RM state will be stored. This must be supplied when using org.apache.hadoop.yarn.server.resourcemanager.recovery.FileSystemRMStateStore as the value for yarn.resourcemanager.store.class "
		),

		yarn_resourcemanager_fs_state_store_retry_policy_spec
		(
		"yarn.resourcemanager.fs.state-store.retry-policy-spec",
		"2000,500",
		"hdfs client retry policy specification. hdfs client retry is always enabled. Specified in pairs of sleep-time and number-of-retries and (t0 n0) (t1 n1) ... the first n0 retries sleep t0 milliseconds on average the following n1 retries sleep t1 milliseconds on average and so on. "
		),

		yarn_resourcemanager_fs_state_store_num_retries
		(
		"yarn.resourcemanager.fs.state-store.num-retries",
		"0",
		"the number of retries to recover from IOException in FileSystemRMStateStore. "
		),

		yarn_resourcemanager_fs_state_store_retry_interval_ms
		(
		"yarn.resourcemanager.fs.state-store.retry-interval-ms",
		"1000",
		"Retry interval in milliseconds in FileSystemRMStateStore. "
		),

		yarn_resourcemanager_leveldb_state_store_path
		(
		"yarn.resourcemanager.leveldb-state-store.path",
		"${hadoop.tmp.dir}/yarn/system/rmstore",
		"Local path where the RM state will be stored when using org.apache.hadoop.yarn.server.resourcemanager.recovery.LeveldbRMStateStore as the value for yarn.resourcemanager.store.class "
		),

		yarn_resourcemanager_ha_enabled
		(
		"yarn.resourcemanager.ha.enabled",
		"false",
		"Enable RM high-availability. When enabled (1) The RM starts in the Standby mode by default and transitions to the Active mode when prompted to. (2) The nodes in the RM ensemble are listed in yarn.resourcemanager.ha.rm-ids (3) The id of each RM either comes from yarn.resourcemanager.ha.id if yarn.resourcemanager.ha.id is explicitly specified or can be figured out by matching yarn.resourcemanager.address.{id} with local address (4) The actual physical addresses come from the configs of the pattern - {rpc-config}.{id} "
		),

		yarn_resourcemanager_ha_automatic_failover_enabled
		(
		"yarn.resourcemanager.ha.automatic-failover.enabled",
		"true",
		"Enable automatic failover. By default it is enabled only when HA is enabled "
		),

		yarn_resourcemanager_ha_automatic_failover_embedded
		(
		"yarn.resourcemanager.ha.automatic-failover.embedded",
		"true",
		"Enable embedded automatic failover. By default it is enabled only when HA is enabled. The embedded elector relies on the RM state store to handle fencing and is primarily intended to be used in conjunction with ZKRMStateStore. "
		),

		yarn_resourcemanager_ha_automatic_failover_zk_base_path
		(
		"yarn.resourcemanager.ha.automatic-failover.zk-base-path",
		"/yarn-leader-election",
		"The base znode path to use for storing leader information when using ZooKeeper based leader election. "
		),

		yarn_resourcemanager_cluster_id
		(
		"yarn.resourcemanager.cluster-id",
		"",
		"Name of the cluster. In a HA setting this is used to ensure the RM participates in leader election for this cluster and ensures it does not affect other clusters "
		),

		yarn_resourcemanager_ha_rm_ids
		(
		"yarn.resourcemanager.ha.rm-ids",
		"",
		"The list of RM nodes in the cluster when HA is enabled. See description of yarn.resourcemanager.ha .enabled for full details on how this is used. "
		),

		yarn_resourcemanager_ha_id
		(
		"yarn.resourcemanager.ha.id",
		"",
		"The id (string) of the current RM. When HA is enabled this is an optional config. The id of current RM can be set by explicitly specifying yarn.resourcemanager.ha.id or figured out by matching yarn.resourcemanager.address.{id} with local address See description of yarn.resourcemanager.ha.enabled for full details on how this is used. "
		),

		yarn_client_failover_proxy_provider
		(
		"yarn.client.failover-proxy-provider",
		"org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider",
		"When HA is enabled the class to be used by Clients AMs and NMs to failover to the Active RM. It should extend org.apache.hadoop.yarn.client.RMFailoverProxyProvider "
		),

		yarn_client_failover_max_attempts
		(
		"yarn.client.failover-max-attempts",
		"",
		"When HA is enabled the max number of times FailoverProxyProvider should attempt failover. When set this overrides the yarn.resourcemanager.connect.max-wait.ms. When not set this is inferred from yarn.resourcemanager.connect.max-wait.ms. "
		),

		yarn_client_failover_sleep_base_ms
		(
		"yarn.client.failover-sleep-base-ms",
		"",
		"When HA is enabled the sleep base (in milliseconds) to be used for calculating the exponential delay between failovers. When set this overrides the yarn.resourcemanager.connect.* settings. When not set yarn.resourcemanager.connect.retry-interval.ms is used instead. "
		),

		yarn_client_failover_sleep_max_ms
		(
		"yarn.client.failover-sleep-max-ms",
		"",
		"When HA is enabled the maximum sleep time (in milliseconds) between failovers. When set this overrides the yarn.resourcemanager.connect.* settings. When not set yarn.resourcemanager.connect.retry-interval.ms is used instead. "
		),

		yarn_client_failover_retries
		(
		"yarn.client.failover-retries",
		"0",
		"When HA is enabled the number of retries per attempt to connect to a ResourceManager. In other words it is the ipc.client.connect.max.retries to be used during failover attempts "
		),

		yarn_client_failover_retries_on_socket_timeouts
		(
		"yarn.client.failover-retries-on-socket-timeouts",
		"0",
		"When HA is enabled the number of retries per attempt to connect to a ResourceManager on socket timeouts. In other words it is the ipc.client.connect.max.retries.on.timeouts to be used during failover attempts "
		),

		yarn_resourcemanager_max_completed_applications
		(
		"yarn.resourcemanager.max-completed-applications",
		"10000",
		"The maximum number of completed applications RM keeps. "
		),

		yarn_resourcemanager_delayed_delegation_token_removal_interval_ms
		(
		"yarn.resourcemanager.delayed.delegation-token.removal-interval-ms",
		"30000",
		"Interval at which the delayed token removal thread runs "
		),

		yarn_resourcemanager_proxy_user_privileges_enabled
		(
		"yarn.resourcemanager.proxy-user-privileges.enabled",
		"false",
		"If true ResourceManager will have proxy-user privileges. Use case: In a secure cluster YARN requires the user hdfs delegation-tokens to do localization and log-aggregation on behalf of the user. If this is set to true ResourceManager is able to request new hdfs delegation tokens on behalf of the user. This is needed by long-running-service because the hdfs tokens will eventually expire and YARN requires new valid tokens to do localization and log-aggregation. Note that to enable this use case the corresponding HDFS NameNode has to configure ResourceManager as the proxy-user so that ResourceManager can itself ask for new tokens on behalf of the user when tokens are past their max-life-time. "
		),

		yarn_resourcemanager_am_rm_tokens_master_key_rolling_interval_secs
		(
		"yarn.resourcemanager.am-rm-tokens.master-key-rolling-interval-secs",
		"86400",
		"Interval for the roll over for the master key used to generate application tokens "
		),

		yarn_resourcemanager_container_tokens_master_key_rolling_interval_secs
		(
		"yarn.resourcemanager.container-tokens.master-key-rolling-interval-secs",
		"86400",
		"Interval for the roll over for the master key used to generate container tokens. It is expected to be much greater than yarn.nm.liveness-monitor.expiry-interval-ms and yarn.resourcemanager.rm.container-allocation.expiry-interval-ms. Otherwise the behavior is undefined. "
		),

		yarn_resourcemanager_nodemanagers_heartbeat_interval_ms
		(
		"yarn.resourcemanager.nodemanagers.heartbeat-interval-ms",
		"1000",
		"The heart-beat interval in milliseconds for every NodeManager in the cluster. "
		),

		yarn_resourcemanager_nodemanager_minimum_version
		(
		"yarn.resourcemanager.nodemanager.minimum.version",
		"NONE",
		"The minimum allowed version of a connecting nodemanager. The valid values are NONE (no version checking) EqualToRM (the nodemanager's version is equal to or greater than the RM version) or a Version String. "
		),

		yarn_resourcemanager_scheduler_monitor_enable
		(
		"yarn.resourcemanager.scheduler.monitor.enable",
		"false",
		"Enable a set of periodic monitors (specified in yarn.resourcemanager.scheduler.monitor.policies) that affect the scheduler. "
		),

		yarn_resourcemanager_scheduler_monitor_policies
		(
		"yarn.resourcemanager.scheduler.monitor.policies",
		"org.apache.hadoop.yarn.server.resourcemanager.monitor.capacity.ProportionalCapacityPreemptionPolicy",
		"The list of SchedulingEditPolicy classes that interact with the scheduler. A particular module may be incompatible with the scheduler other policies or a configuration of either. "
		),

		yarn_resourcemanager_configuration_provider_class
		(
		"yarn.resourcemanager.configuration.provider-class",
		"org.apache.hadoop.yarn.LocalConfigurationProvider",
		"The class to use as the configuration provider. If org.apache.hadoop.yarn.LocalConfigurationProvider is used the local configuration will be loaded. If org.apache.hadoop.yarn.FileSystemBasedConfigurationProvider is used the configuration which will be loaded should be uploaded to remote File system first. "
		),

		yarn_resourcemanager_system_metrics_publisher_enabled
		(
		"yarn.resourcemanager.system-metrics-publisher.enabled",
		"false",
		"The setting that controls whether yarn system metrics is published on the timeline server or not by RM. "
		),

		yarn_resourcemanager_system_metrics_publisher_dispatcher_pool_size
		(
		"yarn.resourcemanager.system-metrics-publisher.dispatcher.pool-size",
		"10",
		"Number of worker threads that send the yarn system metrics data. "
		),

		yarn_nodemanager_hostname
		(
		"yarn.nodemanager.hostname",
		"0.0.0.0",
		"The hostname of the NM. "
		),

		yarn_nodemanager_address
		(
		"yarn.nodemanager.address",
		"${yarn.nodemanager.hostname}:0",
		"The address of the container manager in the NM. "
		),

		yarn_nodemanager_bind_host
		(
		"yarn.nodemanager.bind-host",
		"",
		"The actual address the server will bind to. If this optional address is set the RPC and webapp servers will bind to this address and the port specified in yarn.nodemanager.address and yarn.nodemanager.webapp.address respectively. This is most useful for making NM listen to all interfaces by setting to 0.0.0.0. "
		),

		yarn_nodemanager_admin_env
		(
		"yarn.nodemanager.admin-env",
		"MALLOC_ARENA_MAX=$MALLOC_ARENA_MAX",
		"Environment variables that should be forwarded from the NodeManager's environment to the container's. "
		),

		yarn_nodemanager_env_whitelist
		(
		"yarn.nodemanager.env-whitelist",
		"JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,HADOOP_YARN_HOME",
		"Environment variables that containers may override rather than use NodeManager's default. "
		),

		yarn_nodemanager_container_executor_class
		(
		"yarn.nodemanager.container-executor.class",
		"org.apache.hadoop.yarn.server.nodemanager.DefaultContainerExecutor",
		"who will execute(launch) the containers. "
		),

		yarn_nodemanager_container_manager_thread_count
		(
		"yarn.nodemanager.container-manager.thread-count",
		"20",
		"Number of threads container manager uses. "
		),

		yarn_nodemanager_delete_thread_count
		(
		"yarn.nodemanager.delete.thread-count",
		"4",
		"Number of threads used in cleanup. "
		),

		yarn_nodemanager_delete_debug_delay_sec
		(
		"yarn.nodemanager.delete.debug-delay-sec",
		"0",
		"Number of seconds after an application finishes before the nodemanager's DeletionService will delete the application's localized file directory and log directory. To diagnose Yarn application problems set this property's value large enough (for example to 600 = 10 minutes) to permit examination of these directories. After changing the property's value you must restart the nodemanager in order for it to have an effect. The roots of Yarn applications' work directories is configurable with the yarn.nodemanager.local-dirs property (see below) and the roots of the Yarn applications' log directories is configurable with the yarn.nodemanager.log-dirs property (see also below). "
		),

		yarn_nodemanager_keytab
		(
		"yarn.nodemanager.keytab",
		"/etc/krb5.keytab",
		"Keytab for NM. "
		),

		yarn_nodemanager_local_dirs
		(
		"yarn.nodemanager.local-dirs",
		"${hadoop.tmp.dir}/nm-local-dir",
		"List of directories to store localized files in. An application's localized file directory will be found in: ${yarn.nodemanager.local-dirs}/usercache/${user}/appcache/application_${appid}. Individual containers' work directories called container_${contid} will be subdirectories of this. "
		),

		yarn_nodemanager_local_cache_max_files_per_directory
		(
		"yarn.nodemanager.local-cache.max-files-per-directory",
		"8192",
		"It limits the maximum number of files which will be localized in a single local directory. If the limit is reached then sub-directories will be created and new files will be localized in them. If it is set to a value less than or equal to 36 [which are sub-directories (0-9 and then a-z)] then NodeManager will fail to start. For example; [for public cache] if this is configured with a value of 40 ( 4 files + 36 sub-directories) and the local-dir is /tmp/local-dir1 then it will allow 4 files to be created directly inside /tmp/local-dir1/filecache. For files that are localized further it will create a sub-directory 0 inside /tmp/local-dir1/filecache and will localize files inside it until it becomes full. If a file is removed from a sub-directory that is marked full then that sub-directory will be used back again to localize files. "
		),

		yarn_nodemanager_localizer_address
		(
		"yarn.nodemanager.localizer.address",
		"${yarn.nodemanager.hostname}:8040",
		"Address where the localizer IPC is. "
		),

		yarn_nodemanager_localizer_cache_cleanup_interval_ms
		(
		"yarn.nodemanager.localizer.cache.cleanup.interval-ms",
		"600000",
		"Interval in between cache cleanups. "
		),

		yarn_nodemanager_localizer_cache_target_size_mb
		(
		"yarn.nodemanager.localizer.cache.target-size-mb",
		"10240",
		"Target size of localizer cache in MB per nodemanager. It is a target retention size that only includes resources with PUBLIC and PRIVATE visibility and excludes resources with APPLICATION visibility "
		),

		yarn_nodemanager_localizer_client_thread_count
		(
		"yarn.nodemanager.localizer.client.thread-count",
		"5",
		"Number of threads to handle localization requests. "
		),

		yarn_nodemanager_localizer_fetch_thread_count
		(
		"yarn.nodemanager.localizer.fetch.thread-count",
		"4",
		"Number of threads to use for localization fetching. "
		),

		yarn_nodemanager_log_dirs
		(
		"yarn.nodemanager.log-dirs",
		"${yarn.log.dir}/userlogs",
		"Where to store container logs. An application's localized log directory will be found in ${yarn.nodemanager.log-dirs}/application_${appid}. Individual containers' log directories will be below this in directories named container_{$contid}. Each container directory will contain the files stderr stdin and syslog generated by that container. "
		),

		yarn_log_aggregation_enable
		(
		"yarn.log-aggregation-enable",
		"false",
		"Whether to enable log aggregation. Log aggregation collects each container's logs and moves these logs onto a file-system for e.g. HDFS after the application completes. Users can configure the yarn.nodemanager.remote-app-log-dir and yarn.nodemanager.remote-app-log-dir-suffix properties to determine where these logs are moved to. Users can access the logs via the Application Timeline Server. "
		),

		yarn_log_aggregation_retain_seconds
		(
		"yarn.log-aggregation.retain-seconds",
		"-1",
		"How long to keep aggregation logs before deleting them. -1 disables. Be careful set this too small and you will spam the name node. "
		),

		yarn_log_aggregation_retain_check_interval_seconds
		(
		"yarn.log-aggregation.retain-check-interval-seconds",
		"-1",
		"How long to wait between aggregated log retention checks. If set to 0 or a negative value then the value is computed as one-tenth of the aggregated log retention time. Be careful set this too small and you will spam the name node. "
		),

		yarn_nodemanager_log_retain_seconds
		(
		"yarn.nodemanager.log.retain-seconds",
		"10800",
		"Time in seconds to retain user logs. Only applicable if log aggregation is disabled "
		),

		yarn_nodemanager_remote_app_log_dir
		(
		"yarn.nodemanager.remote-app-log-dir",
		"/tmp/logs",
		"Where to aggregate logs to. "
		),

		yarn_nodemanager_remote_app_log_dir_suffix
		(
		"yarn.nodemanager.remote-app-log-dir-suffix",
		"logs",
		"The remote log dir will be created at {yarn.nodemanager.remote-app-log-dir}/${user}/{thisParam} "
		),

		yarn_nodemanager_resource_memory_mb
		(
		"yarn.nodemanager.resource.memory-mb",
		"8192",
		"Amount of physical memory in MB that can be allocated for containers. "
		),

		yarn_nodemanager_pmem_check_enabled
		(
		"yarn.nodemanager.pmem-check-enabled",
		"true",
		"Whether physical memory limits will be enforced for containers. "
		),

		yarn_nodemanager_vmem_check_enabled
		(
		"yarn.nodemanager.vmem-check-enabled",
		"true",
		"Whether virtual memory limits will be enforced for containers. "
		),

		yarn_nodemanager_vmem_pmem_ratio
		(
		"yarn.nodemanager.vmem-pmem-ratio",
		"2.1",
		"Ratio between virtual memory to physical memory when setting memory limits for containers. Container allocations are expressed in terms of physical memory and virtual memory usage is allowed to exceed this allocation by this ratio. "
		),

		yarn_nodemanager_resource_cpu_vcores
		(
		"yarn.nodemanager.resource.cpu-vcores",
		"8",
		"Number of vcores that can be allocated for containers. This is used by the RM scheduler when allocating resources for containers. This is not used to limit the number of physical cores used by YARN containers. "
		),

		yarn_nodemanager_resource_percentage_physical_cpu_limit
		(
		"yarn.nodemanager.resource.percentage-physical-cpu-limit",
		"100",
		"Percentage of CPU that can be allocated for containers. This setting allows users to limit the amount of CPU that YARN containers use. Currently functional only on Linux using cgroups. The default is to use 100% of CPU. "
		),

		yarn_nodemanager_webapp_address
		(
		"yarn.nodemanager.webapp.address",
		"${yarn.nodemanager.hostname}:8042",
		"NM Webapp address. "
		),

		yarn_nodemanager_container_monitor_interval_ms
		(
		"yarn.nodemanager.container-monitor.interval-ms",
		"3000",
		"How often to monitor containers. "
		),

		yarn_nodemanager_container_monitor_resource_calculator_class
		(
		"yarn.nodemanager.container-monitor.resource-calculator.class",
		"",
		"Class that calculates containers current resource utilization. "
		),

		yarn_nodemanager_health_checker_interval_ms
		(
		"yarn.nodemanager.health-checker.interval-ms",
		"600000",
		"Frequency of running node health script. "
		),

		yarn_nodemanager_health_checker_script_timeout_ms
		(
		"yarn.nodemanager.health-checker.script.timeout-ms",
		"1200000",
		"Script time out period. "
		),

		yarn_nodemanager_health_checker_script_path
		(
		"yarn.nodemanager.health-checker.script.path",
		"",
		"The health check script to run. "
		),

		yarn_nodemanager_health_checker_script_opts
		(
		"yarn.nodemanager.health-checker.script.opts",
		"",
		"The arguments to pass to the health check script. "
		),

		yarn_nodemanager_disk_health_checker_interval_ms
		(
		"yarn.nodemanager.disk-health-checker.interval-ms",
		"120000",
		"Frequency of running disk health checker code. "
		),

		yarn_nodemanager_disk_health_checker_min_healthy_disks
		(
		"yarn.nodemanager.disk-health-checker.min-healthy-disks",
		"0.25",
		"The minimum fraction of number of disks to be healthy for the nodemanager to launch new containers. This correspond to both yarn-nodemanager.local-dirs and yarn.nodemanager.log-dirs. i.e. If there are less number of healthy local-dirs (or log-dirs) available then new containers will not be launched on this node. "
		),

		yarn_nodemanager_disk_health_checker_max_disk_utilization_per_disk_percentage
		(
		"yarn.nodemanager.disk-health-checker.max-disk-utilization-per-disk-percentage",
		"90.0",
		"The maximum percentage of disk space utilization allowed after which a disk is marked as bad. Values can range from 0.0 to 100.0. If the value is greater than or equal to 100 the nodemanager will check for full disk. This applies to yarn-nodemanager.local-dirs and yarn.nodemanager.log-dirs. "
		),

		yarn_nodemanager_disk_health_checker_min_free_space_per_disk_mb
		(
		"yarn.nodemanager.disk-health-checker.min-free-space-per-disk-mb",
		"0",
		"The minimum space that must be available on a disk for it to be used. This applies to yarn-nodemanager.local-dirs and yarn.nodemanager.log-dirs. "
		),

		yarn_nodemanager_linux_container_executor_path
		(
		"yarn.nodemanager.linux-container-executor.path",
		"",
		"The path to the Linux container executor. "
		),

		yarn_nodemanager_linux_container_executor_resources_handler_class
		(
		"yarn.nodemanager.linux-container-executor.resources-handler.class",
		"org.apache.hadoop.yarn.server.nodemanager.util.DefaultLCEResourcesHandler",
		"The class which should help the LCE handle resources. "
		),

		yarn_nodemanager_linux_container_executor_cgroups_hierarchy
		(
		"yarn.nodemanager.linux-container-executor.cgroups.hierarchy",
		"/hadoop-yarn",
		"The cgroups hierarchy under which to place YARN proccesses (cannot contain commas). If yarn.nodemanager.linux-container-executor.cgroups.mount is false (that is if cgroups have been pre-configured) then this cgroups hierarchy must already exist and be writable by the NodeManager user otherwise the NodeManager may fail. Only used when the LCE resources handler is set to the CgroupsLCEResourcesHandler. "
		),

		yarn_nodemanager_linux_container_executor_cgroups_mount
		(
		"yarn.nodemanager.linux-container-executor.cgroups.mount",
		"false",
		"Whether the LCE should attempt to mount cgroups if not found. Only used when the LCE resources handler is set to the CgroupsLCEResourcesHandler. "
		),

		yarn_nodemanager_linux_container_executor_cgroups_mount_path
		(
		"yarn.nodemanager.linux-container-executor.cgroups.mount-path",
		"",
		"Where the LCE should attempt to mount cgroups if not found. Common locations include /sys/fs/cgroup and /cgroup; the default location can vary depending on the Linux distribution in use. This path must exist before the NodeManager is launched. Only used when the LCE resources handler is set to the CgroupsLCEResourcesHandler and yarn.nodemanager.linux-container-executor.cgroups.mount is true. "
		),

		yarn_nodemanager_linux_container_executor_nonsecure_mode_limit_users
		(
		"yarn.nodemanager.linux-container-executor.nonsecure-mode.limit-users",
		"true",
		"This determines which of the two modes that LCE should use on a non-secure cluster. If this value is set to true then all containers will be launched as the user specified in yarn.nodemanager.linux-container-executor.nonsecure-mode.local-user. If this value is set to false then containers will run as the user who submitted the application. "
		),

		yarn_nodemanager_linux_container_executor_nonsecure_mode_local_user
		(
		"yarn.nodemanager.linux-container-executor.nonsecure-mode.local-user",
		"nobody",
		"The UNIX user that containers will run as when Linux-container-executor is used in nonsecure mode (a use case for this is using cgroups) if the yarn.nodemanager.linux-container-executor.nonsecure-mode.limit-users is set to true. "
		),

		yarn_nodemanager_linux_container_executor_nonsecure_mode_user_pattern
		(
		"yarn.nodemanager.linux-container-executor.nonsecure-mode.user-pattern",
		"^[_.A-Za-z0-9][-@_.A-Za-z0-9]{0,255}?[$]?$",
		"The allowed pattern for UNIX user names enforced by Linux-container-executor when used in nonsecure mode (use case for this is using cgroups). The default value is taken from /usr/sbin/adduser "
		),

		yarn_nodemanager_linux_container_executor_cgroups_strict_resource_usage
		(
		"yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage",
		"false",
		"This flag determines whether apps should run with strict resource limits or be allowed to consume spare resources if they need them. For example turning the flag on will restrict apps to use only their share of CPU even if the node has spare CPU cycles. The default value is false i.e. use available resources. Please note that turning this flag on may reduce job throughput on the cluster. "
		),

		yarn_nodemanager_windows_container_memory_limit_enabled
		(
		"yarn.nodemanager.windows-container.memory-limit.enabled",
		"false",
		"This flag determines whether memory limit will be set for the Windows Job Object of the containers launched by the default container executor. "
		),

		yarn_nodemanager_windows_container_cpu_limit_enabled
		(
		"yarn.nodemanager.windows-container.cpu-limit.enabled",
		"false",
		"This flag determines whether CPU limit will be set for the Windows Job Object of the containers launched by the default container executor. "
		),

		yarn_nodemanager_log_aggregation_compression_type
		(
		"yarn.nodemanager.log-aggregation.compression-type",
		"none",
		"T-file compression types used to compress aggregated logs. "
		),

		yarn_nodemanager_principal
		(
		"yarn.nodemanager.principal",
		"",
		"The kerberos principal for the node manager. "
		),

		yarn_nodemanager_aux_services
		(
		"yarn.nodemanager.aux-services",
		"",
		"A comma separated list of services where service name should only contain a-zA-Z0-9_ and can not start with numbers "
		),

		yarn_nodemanager_sleep_delay_before_sigkill_ms
		(
		"yarn.nodemanager.sleep-delay-before-sigkill.ms",
		"250",
		"No. of ms to wait between sending a SIGTERM and SIGKILL to a container "
		),

		yarn_nodemanager_process_kill_wait_ms
		(
		"yarn.nodemanager.process-kill-wait.ms",
		"2000",
		"Max time to wait for a process to come up when trying to cleanup a container "
		),

		yarn_nodemanager_resourcemanager_minimum_version
		(
		"yarn.nodemanager.resourcemanager.minimum.version",
		"NONE",
		"The minimum allowed version of a resourcemanager that a nodemanager will connect to. The valid values are NONE (no version checking) EqualToNM (the resourcemanager's version is equal to or greater than the NM version) or a Version String. "
		),

		yarn_client_nodemanager_client_async_thread_pool_max_size
		(
		"yarn.client.nodemanager-client-async.thread-pool-max-size",
		"500",
		"Max number of threads in NMClientAsync to process container management events "
		),

		yarn_client_nodemanager_connect_max_wait_ms
		(
		"yarn.client.nodemanager-connect.max-wait-ms",
		"900000",
		"Max time to wait to establish a connection to NM "
		),

		yarn_client_nodemanager_connect_retry_interval_ms
		(
		"yarn.client.nodemanager-connect.retry-interval-ms",
		"10000",
		"Time interval between each attempt to connect to NM "
		),

		yarn_client_max_cached_nodemanagers_proxies
		(
		"yarn.client.max-cached-nodemanagers-proxies",
		"0",
		"Maximum number of proxy connections to cache for node managers. If set to a value greater than zero then the cache is enabled and the NMClient and MRAppMaster will cache the specified number of node manager proxies. There will be at max one proxy per node manager. Ex. configuring it to a value of 5 will make sure that client will at max have 5 proxies cached with 5 different node managers. These connections for these proxies will be timed out if idle for more than the system wide idle timeout period. Note that this could cause issues on large clusters as many connections could linger simultaneously and lead to a large number of connection threads. The token used for authentication will be used only at connection creation time. If a new token is received then the earlier connection should be closed in order to use the new token. This and (yarn.client.nodemanager-client-async.thread-pool-max-size) are related and should be in sync (no need for them to be equal). If the value of this property is zero then the connection cache is disabled and connections will use a zero idle timeout to prevent too many connection threads on large clusters. "
		),

		yarn_nodemanager_recovery_enabled
		(
		"yarn.nodemanager.recovery.enabled",
		"false",
		"Enable the node manager to recover after starting "
		),

		yarn_nodemanager_recovery_dir
		(
		"yarn.nodemanager.recovery.dir",
		"${hadoop.tmp.dir}/yarn-nm-recovery",
		"The local filesystem directory in which the node manager will store state when recovery is enabled. "
		),

		yarn_nodemanager_docker_container_executor_exec_name
		(
		"yarn.nodemanager.docker-container-executor.exec-name",
		"/usr/bin/docker",
		"Name or path to the Docker client. "
		),

		yarn_nodemanager_aux_services_mapreduce_shuffle_class
		(
		"yarn.nodemanager.aux-services.mapreduce_shuffle.class",
		"org.apache.hadoop.mapred.ShuffleHandler",
		""
		),

		mapreduce_job_jar
		(
		"mapreduce.job.jar",
		"",
		""
		),

		mapreduce_job_hdfs_servers
		(
		"mapreduce.job.hdfs-servers",
		"${fs.defaultFS}",
		""
		),

		yarn_web_proxy_principal
		(
		"yarn.web-proxy.principal",
		"",
		"The kerberos principal for the proxy if the proxy is not running as part of the RM. "
		),

		yarn_web_proxy_keytab
		(
		"yarn.web-proxy.keytab",
		"",
		"Keytab for WebAppProxy if the proxy is not running as part of the RM. "
		),

		yarn_web_proxy_address
		(
		"yarn.web-proxy.address",
		"",
		"The address for the web proxy as HOST:PORT if this is not given then the proxy will run as part of the RM "
		),

		yarn_application_classpath
		(
		"yarn.application.classpath",
		"",
		"CLASSPATH for YARN applications. A comma-separated list of CLASSPATH entries. When this value is empty the following default CLASSPATH for YARN applications would be used. For Linux: $HADOOP_CONF_DIR $HADOOP_COMMON_HOME/share/hadoop/common/* $HADOOP_COMMON_HOME/share/hadoop/common/lib/* $HADOOP_HDFS_HOME/share/hadoop/hdfs/* $HADOOP_HDFS_HOME/share/hadoop/hdfs/lib/* $HADOOP_YARN_HOME/share/hadoop/yarn/* $HADOOP_YARN_HOME/share/hadoop/yarn/lib/* For Windows: %HADOOP_CONF_DIR% %HADOOP_COMMON_HOME%/share/hadoop/common/* %HADOOP_COMMON_HOME%/share/hadoop/common/lib/* %HADOOP_HDFS_HOME%/share/hadoop/hdfs/* %HADOOP_HDFS_HOME%/share/hadoop/hdfs/lib/* %HADOOP_YARN_HOME%/share/hadoop/yarn/* %HADOOP_YARN_HOME%/share/hadoop/yarn/lib/* "
		),

		yarn_timeline_service_enabled
		(
		"yarn.timeline-service.enabled",
		"false",
		"Indicate to clients whether timeline service is enabled or not. If enabled clients will put entities and events to the timeline server. "
		),

		yarn_timeline_service_hostname
		(
		"yarn.timeline-service.hostname",
		"0.0.0.0",
		"The hostname of the timeline service web application. "
		),

		yarn_timeline_service_address
		(
		"yarn.timeline-service.address",
		"${yarn.timeline-service.hostname}:10200",
		"This is default address for the timeline server to start the RPC server. "
		),

		yarn_timeline_service_webapp_address
		(
		"yarn.timeline-service.webapp.address",
		"${yarn.timeline-service.hostname}:8188",
		"The http address of the timeline service web application. "
		),

		yarn_timeline_service_webapp_https_address
		(
		"yarn.timeline-service.webapp.https.address",
		"${yarn.timeline-service.hostname}:8190",
		"The https address of the timeline service web application. "
		),

		yarn_timeline_service_bind_host
		(
		"yarn.timeline-service.bind-host",
		"",
		"The actual address the server will bind to. If this optional address is set the RPC and webapp servers will bind to this address and the port specified in yarn.timeline-service.address and yarn.timeline-service.webapp.address respectively. This is most useful for making the service listen to all interfaces by setting to 0.0.0.0. "
		),

		yarn_timeline_service_store_class
		(
		"yarn.timeline-service.store-class",
		"org.apache.hadoop.yarn.server.timeline.LeveldbTimelineStore",
		"Store class name for timeline store. "
		),

		yarn_timeline_service_ttl_enable
		(
		"yarn.timeline-service.ttl-enable",
		"true",
		"Enable age off of timeline store data. "
		),

		yarn_timeline_service_ttl_ms
		(
		"yarn.timeline-service.ttl-ms",
		"604800000",
		"Time to live for timeline store data in milliseconds. "
		),

		yarn_timeline_service_leveldb_timeline_store_path
		(
		"yarn.timeline-service.leveldb-timeline-store.path",
		"${hadoop.tmp.dir}/yarn/timeline",
		"Store file name for leveldb timeline store. "
		),

		yarn_timeline_service_leveldb_timeline_store_ttl_interval_ms
		(
		"yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms",
		"300000",
		"Length of time to wait between deletion cycles of leveldb timeline store in milliseconds. "
		),

		yarn_timeline_service_leveldb_timeline_store_read_cache_size
		(
		"yarn.timeline-service.leveldb-timeline-store.read-cache-size",
		"104857600",
		"Size of read cache for uncompressed blocks for leveldb timeline store in bytes. "
		),

		yarn_timeline_service_leveldb_timeline_store_start_time_read_cache_size
		(
		"yarn.timeline-service.leveldb-timeline-store.start-time-read-cache-size",
		"10000",
		"Size of cache for recently read entity start times for leveldb timeline store in number of entities. "
		),

		yarn_timeline_service_leveldb_timeline_store_start_time_write_cache_size
		(
		"yarn.timeline-service.leveldb-timeline-store.start-time-write-cache-size",
		"10000",
		"Size of cache for recently written entity start times for leveldb timeline store in number of entities. "
		),

		yarn_timeline_service_handler_thread_count
		(
		"yarn.timeline-service.handler-thread-count",
		"10",
		"Handler thread count to serve the client RPC requests. "
		),

		yarn_timeline_service_http_authentication_type
		(
		"yarn.timeline-service.http-authentication.type",
		"simple",
		"Defines authentication used for the timeline server HTTP endpoint. Supported values are: simple | kerberos | #AUTHENTICATION_HANDLER_CLASSNAME# "
		),

		yarn_timeline_service_http_authentication_simple_anonymous_allowed
		(
		"yarn.timeline-service.http-authentication.simple.anonymous.allowed",
		"true",
		"Indicates if anonymous requests are allowed by the timeline server when using 'simple' authentication. "
		),

		yarn_timeline_service_principal
		(
		"yarn.timeline-service.principal",
		"",
		"The Kerberos principal for the timeline server. "
		),

		yarn_timeline_service_keytab
		(
		"yarn.timeline-service.keytab",
		"/etc/krb5.keytab",
		"The Kerberos keytab for the timeline server. "
		),

		yarn_timeline_service_client_max_retries
		(
		"yarn.timeline-service.client.max-retries",
		"30",
		"Default maximum number of retires for timeline servive client and value -1 means no limit. "
		),

		yarn_timeline_service_client_best_effort
		(
		"yarn.timeline-service.client.best-effort",
		"false",
		"Client policy for whether timeline operations are non-fatal "
		),

		yarn_timeline_service_client_retry_interval_ms
		(
		"yarn.timeline-service.client.retry-interval-ms",
		"1000",
		"Default retry time interval for timeline servive client. "
		),

		yarn_timeline_service_recovery_enabled
		(
		"yarn.timeline-service.recovery.enabled",
		"false",
		"Enable timeline server to recover state after starting. If true then yarn.timeline-service.state-store-class must be specified. "
		),

		yarn_timeline_service_state_store_class
		(
		"yarn.timeline-service.state-store-class",
		"org.apache.hadoop.yarn.server.timeline.recovery.LeveldbTimelineStateStore",
		"Store class name for timeline state store. "
		),

		yarn_timeline_service_leveldb_state_store_path
		(
		"yarn.timeline-service.leveldb-state-store.path",
		"${hadoop.tmp.dir}/yarn/timeline",
		"Store file name for leveldb state store. "
		),

		yarn_sharedcache_enabled
		(
		"yarn.sharedcache.enabled",
		"false",
		"Whether the shared cache is enabled "
		),

		yarn_sharedcache_root_dir
		(
		"yarn.sharedcache.root-dir",
		"/sharedcache",
		"The root directory for the shared cache "
		),

		yarn_sharedcache_nested_level
		(
		"yarn.sharedcache.nested-level",
		"3",
		"The level of nested directories before getting to the checksum directories. It must be non-negative. "
		),

		yarn_sharedcache_store_class
		(
		"yarn.sharedcache.store.class",
		"org.apache.hadoop.yarn.server.sharedcachemanager.store.InMemorySCMStore",
		"The implementation to be used for the SCM store "
		),

		yarn_sharedcache_app_checker_class
		(
		"yarn.sharedcache.app-checker.class",
		"org.apache.hadoop.yarn.server.sharedcachemanager.RemoteAppChecker",
		"The implementation to be used for the SCM app-checker "
		),

		yarn_sharedcache_store_in_memory_staleness_period_mins
		(
		"yarn.sharedcache.store.in-memory.staleness-period-mins",
		"10080",
		"A resource in the in-memory store is considered stale if the time since the last reference exceeds the staleness period. This value is specified in minutes. "
		),

		yarn_sharedcache_store_in_memory_initial_delay_mins
		(
		"yarn.sharedcache.store.in-memory.initial-delay-mins",
		"10",
		"Initial delay before the in-memory store runs its first check to remove dead initial applications. Specified in minutes. "
		),

		yarn_sharedcache_store_in_memory_check_period_mins
		(
		"yarn.sharedcache.store.in-memory.check-period-mins",
		"720",
		"The frequency at which the in-memory store checks to remove dead initial applications. Specified in minutes. "
		),

		yarn_sharedcache_admin_address
		(
		"yarn.sharedcache.admin.address",
		"0.0.0.0:8047",
		"The address of the admin interface in the SCM (shared cache manager) "
		),

		yarn_sharedcache_admin_thread_count
		(
		"yarn.sharedcache.admin.thread-count",
		"1",
		"The number of threads used to handle SCM admin interface (1 by default) "
		),

		yarn_sharedcache_webapp_address
		(
		"yarn.sharedcache.webapp.address",
		"0.0.0.0:8788",
		"The address of the web application in the SCM (shared cache manager) "
		),

		yarn_sharedcache_cleaner_period_mins
		(
		"yarn.sharedcache.cleaner.period-mins",
		"1440",
		"The frequency at which a cleaner task runs. Specified in minutes. "
		),

		yarn_sharedcache_cleaner_initial_delay_mins
		(
		"yarn.sharedcache.cleaner.initial-delay-mins",
		"10",
		"Initial delay before the first cleaner task is scheduled. Specified in minutes. "
		),

		yarn_sharedcache_cleaner_resource_sleep_ms
		(
		"yarn.sharedcache.cleaner.resource-sleep-ms",
		"0",
		"The time to sleep between processing each shared cache resource. Specified in milliseconds. "
		),

		yarn_sharedcache_uploader_server_address
		(
		"yarn.sharedcache.uploader.server.address",
		"0.0.0.0:8046",
		"The address of the node manager interface in the SCM (shared cache manager) "
		),

		yarn_sharedcache_uploader_server_thread_count
		(
		"yarn.sharedcache.uploader.server.thread-count",
		"50",
		"The number of threads used to handle shared cache manager requests from the node manager (50 by default) "
		),

		yarn_sharedcache_client_server_address
		(
		"yarn.sharedcache.client-server.address",
		"0.0.0.0:8045",
		"The address of the client interface in the SCM (shared cache manager) "
		),

		yarn_sharedcache_client_server_thread_count
		(
		"yarn.sharedcache.client-server.thread-count",
		"50",
		"The number of threads used to handle shared cache manager requests from clients (50 by default) "
		),

		yarn_sharedcache_checksum_algo_impl
		(
		"yarn.sharedcache.checksum.algo.impl",
		"org.apache.hadoop.yarn.sharedcache.ChecksumSHA256Impl",
		"The algorithm used to compute checksums of files (SHA-256 by default) "
		),

		yarn_sharedcache_nm_uploader_replication_factor
		(
		"yarn.sharedcache.nm.uploader.replication.factor",
		"10",
		"The replication factor for the node manager uploader for the shared cache (10 by default) "
		),

		yarn_sharedcache_nm_uploader_thread_count
		(
		"yarn.sharedcache.nm.uploader.thread-count",
		"20",
		"The number of threads used to upload files from a node manager instance (20 by default) "
		),

		yarn_client_application_client_protocol_poll_interval_ms
		(
		"yarn.client.application-client-protocol.poll-interval-ms",
		"200",
		"The interval that the yarn client library uses to poll the completion status of the asynchronous API of application client protocol. "
		),

		yarn_nodemanager_container_monitor_procfs_tree_smaps_based_rss_enabled
		(
		"yarn.nodemanager.container-monitor.procfs-tree.smaps-based-rss.enabled",
		"false",
		"RSS usage of a process computed via /proc/pid/stat is not very accurate as it includes shared pages of a process. /proc/pid/smaps provides useful information like Private_Dirty Private_Clean Shared_Dirty Shared_Clean which can be used for computing more accurate RSS. When this flag is enabled RSS is computed as Min(Shared_Dirty Pss) + Private_Clean + Private_Dirty. It excludes read-only shared mappings in RSS computation. "
		),

		yarn_nodemanager_log_aggregation_roll_monitoring_interval_seconds
		(
		"yarn.nodemanager.log-aggregation.roll-monitoring-interval-seconds",
		"-1",
		"Defines how often NMs wake up to upload log files. The default value is -1. By default the logs will be uploaded when the application is finished. By setting this configure logs can be uploaded periodically when the application is running. The minimum rolling-interval-seconds can be set is 3600. "
		);

		private String key;
		private String value;
		private String description;

		private YARN(String key, String value, String description) {
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
