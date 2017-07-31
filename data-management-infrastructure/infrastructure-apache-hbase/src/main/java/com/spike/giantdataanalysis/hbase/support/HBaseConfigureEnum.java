package com.spike.giantdataanalysis.hbase.support;

import java.util.TreeMap;

public enum HBaseConfigureEnum {
  hbase_tmp_dir(
      "hbase.tmp.dir",
      "${java.io.tmpdir}/hbase-${user.name}",
      "Temporary directory on the local filesystem. Change this setting to point to a location more permanent than '/tmp' the usual resolve for java.io.tmpdir as the '/tmp' directory is cleared on machine restart. "),

  hbase_rootdir(
      "hbase.rootdir",
      "${hbase.tmp.dir}/hbase",
      "The directory shared by region servers and into which HBase persists. The URL should be 'fully-qualified' to include the filesystem scheme. For example to specify the HDFS directory '/hbase' where the HDFS instance's namenode is running at namenode.example.org on port 9000 set this value to: hdfs://namenode.example.org:9000/hbase. By default we write to whatever ${hbase.tmp.dir} is set too -- usually /tmp -- so change this configuration or else all data will be lost on machine restart. "),

  hbase_fs_tmp_dir("hbase.fs.tmp.dir", "/user/${user.name}/hbase-staging",
      "A staging directory in default file system (HDFS) for keeping temporary data. "),

  hbase_bulkload_staging_dir("hbase.bulkload.staging.dir", "${hbase.fs.tmp.dir}",
      "A staging directory in default file system (HDFS) for bulk loading. "),

  hbase_cluster_distributed(
      "hbase.cluster.distributed",
      "false",
      "The mode the cluster will be in. Possible values are false for standalone mode and true for distributed mode. If false startup will run all HBase and ZooKeeper daemons together in the one JVM. "),

  hbase_zookeeper_quorum(
      "hbase.zookeeper.quorum",
      "localhost",
      "Comma separated list of servers in the ZooKeeper ensemble (This config. should have been named hbase.zookeeper.ensemble). For example host1.mydomain.com host2.mydomain.com host3.mydomain.com. By default this is set to localhost for local and pseudo-distributed modes of operation. For a fully-distributed setup this should be set to a full list of ZooKeeper ensemble servers. If HBASE_MANAGES_ZK is set in hbase-env.sh this is the list of servers which hbase will start/stop ZooKeeper on as part of cluster start/stop. Client-side we will take this list of ensemble members and put it together with the hbase.zookeeper.clientPort config. and pass it into zookeeper constructor as the connectString parameter. "),

  hbase_local_dir("hbase.local.dir", "${hbase.tmp.dir}/local/",
      "Directory on the local filesystem to be used as a local storage. "),

  hbase_master_port("hbase.master.port", "16000", "The port the HBase Master should bind to. "),

  hbase_master_info_port("hbase.master.info.port", "16010",
      "The port for the HBase Master web UI. Set to -1 if you do not want a UI instance run. "),

  hbase_master_info_bindAddress("hbase.master.info.bindAddress", "0.0.0.0",
      "The bind address for the HBase Master web UI "),

  hbase_master_logcleaner_plugins(
      "hbase.master.logcleaner.plugins",
      "org.apache.hadoop.hbase.master.cleaner.TimeToLiveLogCleaner",
      "A comma-separated list of BaseLogCleanerDelegate invoked by the LogsCleaner service. These WAL cleaners are called in order so put the cleaner that prunes the most files in front. To implement your own BaseLogCleanerDelegate just put it in HBase's classpath and add the fully qualified class name here. Always add the above default log cleaners in the list. "),

  hbase_master_logcleaner_ttl(
      "hbase.master.logcleaner.ttl",
      "600000",
      "Maximum time a WAL can stay in the .oldlogdir directory after which it will be cleaned by a Master thread. "),

  hbase_master_hfilecleaner_plugins(
      "hbase.master.hfilecleaner.plugins",
      "org.apache.hadoop.hbase.master.cleaner.TimeToLiveHFileCleaner",
      "A comma-separated list of BaseHFileCleanerDelegate invoked by the HFileCleaner service. These HFiles cleaners are called in order so put the cleaner that prunes the most files in front. To implement your own BaseHFileCleanerDelegate just put it in HBase's classpath and add the fully qualified class name here. Always add the above default log cleaners in the list as they will be overwritten in hbase-site.xml. "),

  hbase_master_catalog_timeout("hbase.master.catalog.timeout", "600000",
      "Timeout value for the Catalog Janitor from the master to META. "),

  hbase_master_infoserver_redirect(
      "hbase.master.infoserver.redirect",
      "true",
      "Whether or not the Master listens to the Master web UI port (hbase.master.info.port) and redirects requests to the web UI server shared by the Master and RegionServer. "),

  hbase_regionserver_port("hbase.regionserver.port", "16020",
      "The port the HBase RegionServer binds to. "),

  hbase_regionserver_info_port(
      "hbase.regionserver.info.port",
      "16030",
      "The port for the HBase RegionServer web UI Set to -1 if you do not want the RegionServer UI to run. "),

  hbase_regionserver_info_bindAddress("hbase.regionserver.info.bindAddress", "0.0.0.0",
      "The address for the HBase RegionServer web UI "),

  hbase_regionserver_info_port_auto(
      "hbase.regionserver.info.port.auto",
      "false",
      "Whether or not the Master or RegionServer UI should search for a port to bind to. Enables automatic port search if hbase.regionserver.info.port is already in use. Useful for testing turned off by default. "),

  hbase_regionserver_handler_count(
      "hbase.regionserver.handler.count",
      "30",
      "Count of RPC Listener instances spun up on RegionServers. Same property is used by the Master for count of master handlers. "),

  hbase_ipc_server_callqueue_handler_factor(
      "hbase.ipc.server.callqueue.handler.factor",
      "0.1",
      "Factor to determine the number of call queues. A value of 0 means a single queue shared between all the handlers. A value of 1 means that each handler has its own queue. "),

  hbase_ipc_server_callqueue_read_ratio(
      "hbase.ipc.server.callqueue.read.ratio",
      "0",
      "Split the call queues into read and write queues. The specified interval (which should be between 0.0 and 1.0) will be multiplied by the number of call queues. A value of 0 indicate to not split the call queues meaning that both read and write requests will be pushed to the same set of queues. A value lower than 0.5 means that there will be less read queues than write queues. A value of 0.5 means there will be the same number of read and write queues. A value greater than 0.5 means that there will be more read queues than write queues. A value of 1.0 means that all the queues except one are used to dispatch read requests. Example: Given the total number of call queues being 10 a read.ratio of 0 means that: the 10 queues will contain both read/write requests. a read.ratio of 0.3 means that: 3 queues will contain only read requests and 7 queues will contain only write requests. a read.ratio of 0.5 means that: 5 queues will contain only read requests and 5 queues will contain only write requests. a read.ratio of 0.8 means that: 8 queues will contain only read requests and 2 queues will contain only write requests. a read.ratio of 1 means that: 9 queues will contain only read requests and 1 queues will contain only write requests. "),

  hbase_ipc_server_callqueue_scan_ratio(
      "hbase.ipc.server.callqueue.scan.ratio",
      "0",
      "Given the number of read call queues calculated from the total number of call queues multiplied by the callqueue.read.ratio the scan.ratio property will split the read call queues into small-read and long-read queues. A value lower than 0.5 means that there will be less long-read queues than short-read queues. A value of 0.5 means that there will be the same number of short-read and long-read queues. A value greater than 0.5 means that there will be more long-read queues than short-read queues A value of 0 or 1 indicate to use the same set of queues for gets and scans. Example: Given the total number of read call queues being 8 a scan.ratio of 0 or 1 means that: 8 queues will contain both long and short read requests. a scan.ratio of 0.3 means that: 2 queues will contain only long-read requests and 6 queues will contain only short-read requests. a scan.ratio of 0.5 means that: 4 queues will contain only long-read requests and 4 queues will contain only short-read requests. a scan.ratio of 0.8 means that: 6 queues will contain only long-read requests and 2 queues will contain only short-read requests. "),

  hbase_regionserver_msginterval("hbase.regionserver.msginterval", "3000",
      "Interval between messages from the RegionServer to Master in milliseconds. "),

  hbase_regionserver_logroll_period("hbase.regionserver.logroll.period", "3600000",
      "Period at which we will roll the commit log regardless of how many edits it has. "),

  hbase_regionserver_logroll_errors_tolerated(
      "hbase.regionserver.logroll.errors.tolerated",
      "2",
      "The number of consecutive WAL close errors we will allow before triggering a server abort. A setting of 0 will cause the region server to abort if closing the current WAL writer fails during log rolling. Even a small value (2 or 3) will allow a region server to ride over transient HDFS errors. "),

  hbase_regionserver_hlog_reader_impl("hbase.regionserver.hlog.reader.impl",
      "org.apache.hadoop.hbase.regionserver.wal.ProtobufLogReader",
      "The WAL file reader implementation. "),

  hbase_regionserver_hlog_writer_impl("hbase.regionserver.hlog.writer.impl",
      "org.apache.hadoop.hbase.regionserver.wal.ProtobufLogWriter",
      "The WAL file writer implementation. "),

  hbase_regionserver_global_memstore_size(
      "hbase.regionserver.global.memstore.size",
      "",
      "Maximum size of all memstores in a region server before new updates are blocked and flushes are forced. Defaults to 40% of heap (0.4). Updates are blocked and flushes are forced until size of all memstores in a region server hits hbase.regionserver.global.memstore.size.lower.limit. The default value in this configuration has been intentionally left emtpy in order to honor the old hbase.regionserver.global.memstore.upperLimit property if present. "),

  hbase_regionserver_global_memstore_size_lower_limit(
      "hbase.regionserver.global.memstore.size.lower.limit",
      "",
      "Maximum size of all memstores in a region server before flushes are forced. Defaults to 95% of hbase.regionserver.global.memstore.size (0.95). A 100% value for this value causes the minimum possible flushing to occur when updates are blocked due to memstore limiting. The default value in this configuration has been intentionally left emtpy in order to honor the old hbase.regionserver.global.memstore.lowerLimit property if present. "),

  hbase_regionserver_optionalcacheflushinterval(
      "hbase.regionserver.optionalcacheflushinterval",
      "3600000",
      "Maximum amount of time an edit lives in memory before being automatically flushed. Default 1 hour. Set it to 0 to disable automatic flushing. "),

  hbase_regionserver_catalog_timeout("hbase.regionserver.catalog.timeout", "600000",
      "Timeout value for the Catalog Janitor from the regionserver to META. "),

  hbase_regionserver_dns_interface("hbase.regionserver.dns.interface", "default",
      "The name of the Network Interface from which a region server should report its IP address. "),

  hbase_regionserver_dns_nameserver(
      "hbase.regionserver.dns.nameserver",
      "default",
      "The host name or IP address of the name server (DNS) which a region server should use to determine the host name used by the master for communication and display purposes. "),

  hbase_regionserver_region_split_policy(
      "hbase.regionserver.region.split.policy",
      "org.apache.hadoop.hbase.regionserver.IncreasingToUpperBoundRegionSplitPolicy",
      "A split policy determines when a region should be split. The various other split policies that are available currently are ConstantSizeRegionSplitPolicy DisabledRegionSplitPolicy DelimitedKeyPrefixRegionSplitPolicy KeyPrefixRegionSplitPolicy etc. "),

  hbase_regionserver_regionSplitLimit(
      "hbase.regionserver.regionSplitLimit",
      "1000",
      "Limit for the number of regions after which no more region splitting should take place. This is not hard limit for the number of regions but acts as a guideline for the regionserver to stop splitting after a certain limit. Default is set to 1000. "),

  zookeeper_session_timeout(
      "zookeeper.session.timeout",
      "90000",
      "ZooKeeper session timeout in milliseconds. It is used in two different ways. First this value is used in the ZK client that HBase uses to connect to the ensemble. It is also used by HBase when it starts a ZK server and it is passed as the 'maxSessionTimeout'. See http://hadoop.apache.org/zookeeper/docs/current/zookeeperProgrammers.html#ch_zkSessions. For example if a HBase region server connects to a ZK ensemble that's also managed by HBase then the session timeout will be the one specified by this configuration. But a region server that connects to an ensemble managed with a different configuration will be subjected that ensemble's maxSessionTimeout. So even though HBase might propose using 90 seconds the ensemble can have a max timeout lower than this and it will take precedence. The current default that ZK ships with is 40 seconds which is lower than HBase's. "),

  zookeeper_znode_parent(
      "zookeeper.znode.parent",
      "/hbase",
      "Root ZNode for HBase in ZooKeeper. All of HBase's ZooKeeper files that are configured with a relative path will go under this node. By default all of HBase's ZooKeeper file path are configured with a relative path so they will all go under this directory unless changed. "),

  zookeeper_znode_rootserver(
      "zookeeper.znode.rootserver",
      "root-region-server",
      "Path to ZNode holding root region location. This is written by the master and read by clients and region servers. If a relative path is given the parent folder will be ${zookeeper.znode.parent}. By default this means the root location is stored at /hbase/root-region-server. "),

  zookeeper_znode_acl_parent("zookeeper.znode.acl.parent", "acl",
      "Root ZNode for access control lists. "),

  hbase_zookeeper_dns_interface("hbase.zookeeper.dns.interface", "default",
      "The name of the Network Interface from which a ZooKeeper server should report its IP address. "),

  hbase_zookeeper_dns_nameserver(
      "hbase.zookeeper.dns.nameserver",
      "default",
      "The host name or IP address of the name server (DNS) which a ZooKeeper server should use to determine the host name used by the master for communication and display purposes. "),

  hbase_zookeeper_peerport(
      "hbase.zookeeper.peerport",
      "2888",
      "Port used by ZooKeeper peers to talk to each other. See http://hadoop.apache.org/zookeeper/docs/r3.1.1/zookeeperStarted.html#sc_RunningReplicatedZooKeeper for more information. "),

  hbase_zookeeper_leaderport(
      "hbase.zookeeper.leaderport",
      "3888",
      "Port used by ZooKeeper for leader election. See http://hadoop.apache.org/zookeeper/docs/r3.1.1/zookeeperStarted.html#sc_RunningReplicatedZooKeeper for more information. "),

  hbase_zookeeper_useMulti(
      "hbase.zookeeper.useMulti",
      "true",
      "Instructs HBase to make use of ZooKeeper's multi-update functionality. This allows certain ZooKeeper operations to complete more quickly and prevents some issues with rare Replication failure scenarios (see the release note of HBASE-2611 for an example). IMPORTANT: only set this to true if all ZooKeeper servers in the cluster are on version 3.4+ and will not be downgraded. ZooKeeper versions before 3.4 do not support multi-update and will not fail gracefully if multi-update is invoked (see ZOOKEEPER-1495). "),

  hbase_config_read_zookeeper_config(
      "hbase.config.read.zookeeper.config",
      "false",
      "Set to true to allow HBaseConfiguration to read the zoo.cfg file for ZooKeeper properties. Switching this to true is not recommended since the functionality of reading ZK properties from a zoo.cfg file has been deprecated. "),

  hbase_zookeeper_property_initLimit(
      "hbase.zookeeper.property.initLimit",
      "10",
      "Property from ZooKeeper's config zoo.cfg. The number of ticks that the initial synchronization phase can take. "),

  hbase_zookeeper_property_syncLimit(
      "hbase.zookeeper.property.syncLimit",
      "5",
      "Property from ZooKeeper's config zoo.cfg. The number of ticks that can pass between sending a request and getting an acknowledgment. "),

  hbase_zookeeper_property_dataDir("hbase.zookeeper.property.dataDir",
      "${hbase.tmp.dir}/zookeeper",
      "Property from ZooKeeper's config zoo.cfg. The directory where the snapshot is stored. "),

  hbase_zookeeper_property_clientPort("hbase.zookeeper.property.clientPort", "2181",
      "Property from ZooKeeper's config zoo.cfg. The port at which the clients will connect. "),

  hbase_zookeeper_property_maxClientCnxns(
      "hbase.zookeeper.property.maxClientCnxns",
      "300",
      "Property from ZooKeeper's config zoo.cfg. Limit on number of concurrent connections (at the socket level) that a single client identified by IP address may make to a single member of the ZooKeeper ensemble. Set high to avoid zk connection issues running standalone and pseudo-distributed. "),

  hbase_client_write_buffer(
      "hbase.client.write.buffer",
      "2097152",
      "Default size of the HTable client write buffer in bytes. A bigger buffer takes more memory -- on both the client and server side since server instantiates the passed write buffer to process it -- but a larger buffer size reduces the number of RPCs made. For an estimate of server-side memory-used evaluate hbase.client.write.buffer * hbase.regionserver.handler.count "),

  hbase_client_pause(
      "hbase.client.pause",
      "100",
      "General client pause value. Used mostly as value to wait before running a retry of a failed get region lookup etc. See hbase.client.retries.number for description of how we backoff from this initial pause amount and how this pause works w/ retries. "),

  hbase_client_retries_number(
      "hbase.client.retries.number",
      "35",
      "Maximum retries. Used as maximum for all retryable operations such as the getting of a cell's value starting a row update etc. Retry interval is a rough function based on hbase.client.pause. At first we retry at this interval but then with backoff we pretty quickly reach retrying every ten seconds. See HConstants#RETRY_BACKOFF for how the backup ramps up. Change this setting and hbase.client.pause to suit your workload. "),

  hbase_client_max_total_tasks("hbase.client.max.total.tasks", "100",
      "The maximum number of concurrent tasks a single HTable instance will send to the cluster. "),

  hbase_client_max_perserver_tasks(
      "hbase.client.max.perserver.tasks",
      "5",
      "The maximum number of concurrent tasks a single HTable instance will send to a single region server. "),

  hbase_client_max_perregion_tasks(
      "hbase.client.max.perregion.tasks",
      "1",
      "The maximum number of concurrent connections the client will maintain to a single Region. That is if there is already hbase.client.max.perregion.tasks writes in progress for this region new puts won't be sent to this region until some writes finishes. "),

  hbase_client_scanner_caching(
      "hbase.client.scanner.caching",
      "2147483647",
      "Number of rows that we try to fetch when calling next on a scanner if it is not served from (local client) memory. This configuration works together with hbase.client.scanner.max.result.size to try and use the network efficiently. The default value is Integer.MAX_VALUE by default so that the network will fill the chunk size defined by hbase.client.scanner.max.result.size rather than be limited by a particular number of rows since the size of rows varies table to table. If you know ahead of time that you will not require more than a certain number of rows from a scan this configuration should be set to that row limit via Scan#setCaching. Higher caching values will enable faster scanners but will eat up more memory and some calls of next may take longer and longer times when the cache is empty. Do not set this value such that the time between invocations is greater than the scanner timeout; i.e. hbase.client.scanner.timeout.period "),

  hbase_client_keyvalue_maxsize(
      "hbase.client.keyvalue.maxsize",
      "10485760",
      "Specifies the combined maximum allowed size of a KeyValue instance. This is to set an upper boundary for a single entry saved in a storage file. Since they cannot be split it helps avoiding that a region cannot be split any further because the data is too large. It seems wise to set this to a fraction of the maximum region size. Setting it to zero or less disables the check. "),

  hbase_client_scanner_timeout_period("hbase.client.scanner.timeout.period", "60000",
      "Client scanner lease period in milliseconds. "),

  hbase_client_localityCheck_threadPoolSize("hbase.client.localityCheck.threadPoolSize", "2", ""),

  hbase_bulkload_retries_number(
      "hbase.bulkload.retries.number",
      "10",
      "Maximum retries. This is maximum number of iterations to atomic bulk loads are attempted in the face of splitting operations 0 means never give up. "),

  hbase_balancer_period("hbase.balancer.period", "300000",
      "Period at which the region balancer runs in the Master. "),

  hbase_normalizer_period("hbase.normalizer.period", "1800000",
      "Period at which the region normalizer runs in the Master. "),

  hbase_regions_slop("hbase.regions.slop", "0.2",
      "Rebalance if any regionserver has average + (average * slop) regions. "),

  hbase_server_thread_wakefrequency(
      "hbase.server.thread.wakefrequency",
      "10000",
      "Time to sleep in between searches for work (in milliseconds). Used as sleep interval by service threads such as log roller. "),

  hbase_server_versionfile_writeattempts(
      "hbase.server.versionfile.writeattempts",
      "3",
      "How many time to retry attempting to write a version file before just aborting. Each attempt is seperated by the hbase.server.thread.wakefrequency milliseconds. "),

  hbase_hregion_memstore_flush_size(
      "hbase.hregion.memstore.flush.size",
      "134217728",
      "Memstore will be flushed to disk if size of the memstore exceeds this number of bytes. Value is checked by a thread that runs every hbase.server.thread.wakefrequency. "),

  hbase_hregion_percolumnfamilyflush_size_lower_bound(
      "hbase.hregion.percolumnfamilyflush.size.lower.bound",
      "16777216",
      "If FlushLargeStoresPolicy is used then every time that we hit the total memstore limit we find out all the column families whose memstores exceed this value and only flush them while retaining the others whose memstores are lower than this limit. If none of the families have their memstore size more than this all the memstores will be flushed (just as usual). This value should be less than half of the total memstore threshold (hbase.hregion.memstore.flush.size). "),

  hbase_hregion_preclose_flush_size(
      "hbase.hregion.preclose.flush.size",
      "5242880",
      "If the memstores in a region are this size or larger when we go to close run a pre-flush to clear out memstores before we put up the region closed flag and take the region offline. On close a flush is run under the close flag to empty memory. During this time the region is offline and we are not taking on any writes. If the memstore content is large this flush could take a long time to complete. The preflush is meant to clean out the bulk of the memstore before putting up the close flag and taking the region offline so the flush that runs under the close flag has little to do. "),

  hbase_hregion_memstore_block_multiplier(
      "hbase.hregion.memstore.block.multiplier",
      "4",
      "Block updates if memstore has hbase.hregion.memstore.block.multiplier times hbase.hregion.memstore.flush.size bytes. Useful preventing runaway memstore during spikes in update traffic. Without an upper-bound memstore fills such that when it flushes the resultant flush files take a long time to compact or split or worse we OOME. "),

  hbase_hregion_memstore_mslab_enabled(
      "hbase.hregion.memstore.mslab.enabled",
      "true",
      "Enables the MemStore-Local Allocation Buffer a feature which works to prevent heap fragmentation under heavy write loads. This can reduce the frequency of stop-the-world GC pauses on large heaps. "),

  hbase_hregion_max_filesize(
      "hbase.hregion.max.filesize",
      "10737418240",
      "Maximum HStoreFile size. If any one of a column families' HStoreFiles has grown to exceed this value the hosting HRegion is split in two. "),

  hbase_hregion_majorcompaction(
      "hbase.hregion.majorcompaction",
      "604800000",
      "The time (in miliseconds) between 'major' compactions of all HStoreFiles in a region. Default: Set to 7 days. Major compactions tend to happen exactly when you need them least so enable them such that they run at off-peak for your deploy; or since this setting is on a periodicity that is unlikely to match your loading run the compactions via an external invocation out of a cron job or some such. "),

  hbase_hregion_majorcompaction_jitter(
      "hbase.hregion.majorcompaction.jitter",
      "0.50",
      "Jitter outer bound for major compactions. On each regionserver we multiply the hbase.region.majorcompaction interval by some random fraction that is inside the bounds of this maximum. We then add this + or - product to when the next major compaction is to run. The idea is that major compaction does happen on every regionserver at exactly the same time. The smaller this number the closer the compactions come together. "),

  hbase_hstore_compactionThreshold(
      "hbase.hstore.compactionThreshold",
      "3",
      "If more than this number of HStoreFiles in any one HStore (one HStoreFile is written per flush of memstore) then a compaction is run to rewrite all HStoreFiles files as one. Larger numbers put off compaction but when it runs it takes longer to complete. "),

  hbase_hstore_flusher_count(
      "hbase.hstore.flusher.count",
      "2",
      "The number of flush threads. With less threads the memstore flushes will be queued. With more threads the flush will be executed in parallel increasing the hdfs load. This can lead as well to more compactions. "),

  hbase_hstore_blockingStoreFiles(
      "hbase.hstore.blockingStoreFiles",
      "10",
      "If more than this number of StoreFiles in any one Store (one StoreFile is written per flush of MemStore) then updates are blocked for this HRegion until a compaction is completed or until hbase.hstore.blockingWaitTime has been exceeded. "),

  hbase_hstore_blockingWaitTime(
      "hbase.hstore.blockingWaitTime",
      "90000",
      "The time an HRegion will block updates for after hitting the StoreFile limit defined by hbase.hstore.blockingStoreFiles. After this time has elapsed the HRegion will stop blocking updates even if a compaction has not been completed. "),

  hbase_hstore_compaction_max("hbase.hstore.compaction.max", "10",
      "Max number of HStoreFiles to compact per 'minor' compaction. "),

  hbase_hstore_compaction_kv_max(
      "hbase.hstore.compaction.kv.max",
      "10",
      "How many KeyValues to read and then write in a batch when flushing or compacting. Do less if big KeyValues and problems with OOME. Do more if wide small rows. "),

  hbase_hstore_time_to_purge_deletes(
      "hbase.hstore.time.to.purge.deletes",
      "0",
      "The amount of time to delay purging of delete markers with future timestamps. If unset or set to 0 all delete markers including those with future timestamps are purged during the next major compaction. Otherwise a delete marker is kept until the major compaction which occurs after the marker's timestamp plus the value of this setting in milliseconds. "),

  hbase_storescanner_parallel_seek_enable(
      "hbase.storescanner.parallel.seek.enable",
      "false",
      "Enables StoreFileScanner parallel-seeking in StoreScanner a feature which can reduce response latency under special conditions. "),

  hbase_storescanner_parallel_seek_threads("hbase.storescanner.parallel.seek.threads", "10",
      "The default thread pool size if parallel-seeking feature enabled. "),

  hfile_block_cache_size(
      "hfile.block.cache.size",
      "0.4",
      "Percentage of maximum heap (-Xmx setting) to allocate to block cache used by HFile/StoreFile. Default of 0.4 means allocate 40%. Set to 0 to disable but it's not recommended; you need at least enough cache to hold the storefile indices. "),

  hfile_block_index_cacheonwrite(
      "hfile.block.index.cacheonwrite",
      "false",
      "This allows to put non-root multi-level index blocks into the block cache at the time the index is being written. "),

  hfile_index_block_max_size(
      "hfile.index.block.max.size",
      "131072",
      "When the size of a leaf-level intermediate-level or root-level index block in a multi-level block index grows to this size the block is written out and a new block is started. "),

  hbase_bucketcache_ioengine(
      "hbase.bucketcache.ioengine",
      "",
      "Where to store the contents of the bucketcache. One of: heap offheap or file. If a file set it to file:PATH_TO_FILE. See http://hbase.apache.org/book.html#offheap.blockcache for more information. "),

  hbase_bucketcache_combinedcache_enabled(
      "hbase.bucketcache.combinedcache.enabled",
      "true",
      "Whether or not the bucketcache is used in league with the LRU on-heap block cache. In this mode indices and blooms are kept in the LRU blockcache and the data blocks are kept in the bucketcache. "),

  hbase_bucketcache_size(
      "hbase.bucketcache.size",
      "",
      "A float that EITHER represents a percentage of total heap memory size to give to the cache (if < 1.0) OR it is the total capacity in megabytes of BucketCache. Default: 0.0 "),

  hbase_bucketcache_sizes(
      "hbase.bucketcache.sizes",
      "",
      "A comma-separated list of sizes for buckets for the bucketcache. Can be multiple sizes. List block sizes in order from smallest to largest. The sizes you use will depend on your data access patterns. Must be a multiple of 1024 else you will run into 'java.io.IOException: Invalid HFile block magic' when you go to read from cache. If you specify no values here then you pick up the default bucketsizes set in code (See BucketAllocator#DEFAULT_BUCKET_SIZES). "),

  hfile_format_version(
      "hfile.format.version",
      "3",
      "The HFile format version to use for new files. Version 3 adds support for tags in hfiles (See http://hbase.apache.org/book.html#hbase.tags). Distributed Log Replay requires that tags are enabled. Also see the configuration 'hbase.replication.rpc.codec'. "),

  hfile_block_bloom_cacheonwrite("hfile.block.bloom.cacheonwrite", "false",
      "Enables cache-on-write for inline blocks of a compound Bloom filter. "),

  io_storefile_bloom_block_size(
      "io.storefile.bloom.block.size",
      "131072",
      "The size in bytes of a single block (chunk) of a compound Bloom filter. This size is approximate because Bloom blocks can only be inserted at data block boundaries and the number of keys per data block varies. "),

  hbase_rs_cacheblocksonwrite("hbase.rs.cacheblocksonwrite", "false",
      "Whether an HFile block should be added to the block cache when the block is finished. "),

  hbase_rpc_timeout(
      "hbase.rpc.timeout",
      "60000",
      "This is for the RPC layer to define how long (millisecond) HBase client applications take for a remote call to time out. It uses pings to check connections but will eventually throw a TimeoutException. "),

  hbase_client_operation_timeout(
      "hbase.client.operation.timeout",
      "1200000",
      "Operation timeout is a top-level restriction (millisecond) that makes sure a blocking operation in Table will not be blocked more than this. In each operation if rpc request fails because of timeout or other reason it will retry until success or throw RetriesExhaustedException. But if the total time being blocking reach the operation timeout before retries exhausted it will break early and throw SocketTimeoutException. "),

  hbase_cells_scanned_per_heartbeat_check(
      "hbase.cells.scanned.per.heartbeat.check",
      "10000",
      "The number of cells scanned in between heartbeat checks. Heartbeat checks occur during the processing of scans to determine whether or not the server should stop scanning in order to send back a heartbeat message to the client. Heartbeat messages are used to keep the client-server connection alive during long running scans. Small values mean that the heartbeat checks will occur more often and thus will provide a tighter bound on the execution time of the scan. Larger values mean that the heartbeat checks occur less frequently "),

  hbase_rpc_shortoperation_timeout(
      "hbase.rpc.shortoperation.timeout",
      "10000",
      "This is another version of hbase.rpc.timeout. For those RPC operation within cluster we rely on this configuration to set a short timeout limitation for short operation. For example short rpc timeout for region server's trying to report to active master can benefit quicker master failover process. "),

  hbase_ipc_client_tcpnodelay(
      "hbase.ipc.client.tcpnodelay",
      "true",
      "Set no delay on rpc socket connections. See http://docs.oracle.com/javase/1.5.0/docs/api/java/net/Socket.html#getTcpNoDelay() "),

  hbase_regionserver_hostname(
      "hbase.regionserver.hostname",
      "",
      "This config is for experts: don't set its value unless you really know what you are doing. When set to a non-empty value this represents the (external facing) hostname for the underlying server. See https://issues.apache.org/jira/browse/HBASE-12954 for details. "),

  hbase_master_keytab_file(
      "hbase.master.keytab.file",
      "",
      "Full path to the kerberos keytab file to use for logging in the configured HMaster server principal. "),

  hbase_master_kerberos_principal(
      "hbase.master.kerberos.principal",
      "",
      "Ex. hbase/_HOST@EXAMPLE.COM. The kerberos principal name that should be used to run the HMaster process. The principal name should be in the form: user/hostname@DOMAIN. If _HOST is used as the hostname portion it will be replaced with the actual hostname of the running instance. "),

  hbase_regionserver_keytab_file(
      "hbase.regionserver.keytab.file",
      "",
      "Full path to the kerberos keytab file to use for logging in the configured HRegionServer server principal. "),

  hbase_regionserver_kerberos_principal(
      "hbase.regionserver.kerberos.principal",
      "",
      "Ex. hbase/_HOST@EXAMPLE.COM. The kerberos principal name that should be used to run the HRegionServer process. The principal name should be in the form: user/hostname@DOMAIN. If _HOST is used as the hostname portion it will be replaced with the actual hostname of the running instance. An entry for this principal must exist in the file specified in hbase.regionserver.keytab.file "),

  hadoop_policy_file(
      "hadoop.policy.file",
      "hbase-policy.xml",
      "The policy configuration file used by RPC servers to make authorization decisions on client requests. Only used when HBase security is enabled. "),

  hbase_superuser(
      "hbase.superuser",
      "",
      "List of users or groups (comma-separated) who are allowed full privileges regardless of stored ACLs across the cluster. Only used when HBase security is enabled. "),

  hbase_auth_key_update_interval(
      "hbase.auth.key.update.interval",
      "86400000",
      "The update interval for master key for authentication tokens in servers in milliseconds. Only used when HBase security is enabled. "),

  hbase_auth_token_max_lifetime(
      "hbase.auth.token.max.lifetime",
      "604800000",
      "The maximum lifetime in milliseconds after which an authentication token expires. Only used when HBase security is enabled. "),

  hbase_ipc_client_fallback_to_simple_auth_allowed(
      "hbase.ipc.client.fallback-to-simple-auth-allowed",
      "false",
      "When a client is configured to attempt a secure connection but attempts to connect to an insecure server that server may instruct the client to switch to SASL SIMPLE (unsecure) authentication. This setting controls whether or not the client will accept this instruction from the server. When false (the default) the client will not allow the fallback to SIMPLE authentication and will abort the connection. "),

  hbase_ipc_server_fallback_to_simple_auth_allowed(
      "hbase.ipc.server.fallback-to-simple-auth-allowed",
      "false",
      "When a server is configured to require secure connections it will reject connection attempts from clients using SASL SIMPLE (unsecure) authentication. This setting allows secure servers to accept SASL SIMPLE connections from clients when the client requests. When false (the default) the server will not allow the fallback to SIMPLE authentication and will reject the connection. WARNING: This setting should ONLY be used as a temporary measure while converting clients over to secure authentication. It MUST BE DISABLED for secure operation. "),

  hbase_coprocessor_enabled(
      "hbase.coprocessor.enabled",
      "true",
      "Enables or disables coprocessor loading. If 'false' (disabled) any other coprocessor related configuration will be ignored. "),

  hbase_coprocessor_user_enabled(
      "hbase.coprocessor.user.enabled",
      "true",
      "Enables or disables user (aka. table) coprocessor loading. If 'false' (disabled) any table coprocessor attributes in table descriptors will be ignored. If hbase.coprocessor.enabled is 'false' this setting has no effect. "),

  hbase_coprocessor_region_classes(
      "hbase.coprocessor.region.classes",
      "",
      "A comma-separated list of Coprocessors that are loaded by default on all tables. For any override coprocessor method these classes will be called in order. After implementing your own Coprocessor just put it in HBase's classpath and add the fully qualified class name here. A coprocessor can also be loaded on demand by setting HTableDescriptor. "),

  hbase_rest_port("hbase.rest.port", "8080", "The port for the HBase REST server. "),

  hbase_rest_readonly(
      "hbase.rest.readonly",
      "false",
      "Defines the mode the REST server will be started in. Possible values are: false: All HTTP methods are permitted - GET/PUT/POST/DELETE. true: Only the GET method is permitted. "),

  hbase_rest_threads_max(
      "hbase.rest.threads.max",
      "100",
      "The maximum number of threads of the REST server thread pool. Threads in the pool are reused to process REST requests. This controls the maximum number of requests processed concurrently. It may help to control the memory used by the REST server to avoid OOM issues. If the thread pool is full incoming requests will be queued up and wait for some free threads. "),

  hbase_rest_threads_min(
      "hbase.rest.threads.min",
      "2",
      "The minimum number of threads of the REST server thread pool. The thread pool always has at least these number of threads so the REST server is ready to serve incoming requests. "),

  hbase_rest_support_proxyuser("hbase.rest.support.proxyuser", "false",
      "Enables running the REST server to support proxy-user mode. "),

  hbase_defaults_for_version(
      "hbase.defaults.for.version",
      "1.2.6",
      "This defaults file was compiled for version ${project.version}. This variable is used to make sure that a user doesn't have an old version of hbase-default.xml on the classpath. "),

  hbase_defaults_for_version_skip(
      "hbase.defaults.for.version.skip",
      "false",
      "Set to true to skip the 'hbase.defaults.for.version' check. Setting this to true can be useful in contexts other than the other side of a maven generation; i.e. running in an ide. You'll want to set this boolean to true to avoid seeing the RuntimException complaint: hbase-default.xml file seems to be for and old version of HBase (\\${hbase.version}) this version is X.X.X-SNAPSHOT "),

  hbase_coprocessor_master_classes(
      "hbase.coprocessor.master.classes",
      "",
      "A comma-separated list of org.apache.hadoop.hbase.coprocessor.MasterObserver coprocessors that are loaded by default on the active HMaster process. For any implemented coprocessor methods the listed classes will be called in order. After implementing your own MasterObserver just put it in HBase's classpath and add the fully qualified class name here. "),

  hbase_coprocessor_abortonerror(
      "hbase.coprocessor.abortonerror",
      "true",
      "Set to true to cause the hosting server (master or regionserver) to abort if a coprocessor fails to load fails to initialize or throws an unexpected Throwable object. Setting this to false will allow the server to continue execution but the system wide state of the coprocessor in question will become inconsistent as it will be properly executing in only a subset of servers so this is most useful for debugging only. "),

  hbase_online_schema_update_enable("hbase.online.schema.update.enable", "true",
      "Set true to enable online schema changes. "),

  hbase_table_lock_enable(
      "hbase.table.lock.enable",
      "true",
      "Set to true to enable locking the table in zookeeper for schema change operations. Table locking from master prevents concurrent schema modifications to corrupt table state. "),

  hbase_table_max_rowsize(
      "hbase.table.max.rowsize",
      "1073741824",
      "Maximum size of single row in bytes (default is 1 Gb) for Get'ting or Scan'ning without in-row scan flag set. If row size exceeds this limit RowTooBigException is thrown to client. "),

  hbase_thrift_minWorkerThreads(
      "hbase.thrift.minWorkerThreads",
      "16",
      "The core size of the thread pool. New threads are created on every connection until this many threads are created. "),

  hbase_thrift_maxWorkerThreads(
      "hbase.thrift.maxWorkerThreads",
      "1000",
      "The maximum size of the thread pool. When the pending request queue overflows new threads are created until their number reaches this number. After that the server starts dropping connections. "),

  hbase_thrift_maxQueuedRequests(
      "hbase.thrift.maxQueuedRequests",
      "1000",
      "The maximum number of pending Thrift connections waiting in the queue. If there are no idle threads in the pool the server queues requests. Only when the queue overflows new threads are added up to hbase.thrift.maxQueuedRequests threads. "),

  hbase_thrift_htablepool_size_max(
      "hbase.thrift.htablepool.size.max",
      "1000",
      "The upper bound for the table pool used in the Thrift gateways server. Since this is per table name we assume a single table and so with 1000 default worker threads max this is set to a matching number. For other workloads this number can be adjusted as needed. "),

  hbase_regionserver_thrift_framed(
      "hbase.regionserver.thrift.framed",
      "false",
      "Use Thrift TFramedTransport on the server side. This is the recommended transport for thrift servers and requires a similar setting on the client side. Changing this to false will select the default transport vulnerable to DoS when malformed requests are issued due to THRIFT-601. "),

  hbase_regionserver_thrift_framed_max_frame_size_in_mb(
      "hbase.regionserver.thrift.framed.max_frame_size_in_mb", "2",
      "Default frame size when using framed transport "),

  hbase_regionserver_thrift_compact("hbase.regionserver.thrift.compact", "false",
      "Use Thrift TCompactProtocol binary serialization protocol. "),

  hbase_rootdir_perms(
      "hbase.rootdir.perms",
      "700",
      "FS Permissions for the root directory in a secure(kerberos) setup. When master starts it creates the rootdir with this permissions or sets the permissions if it does not match. "),

  hbase_data_umask_enable("hbase.data.umask.enable", "false",
      "Enable if true that file permissions should be assigned to the files written by the regionserver "),

  hbase_data_umask("hbase.data.umask", "000",
      "File permissions that should be used to write data files when hbase.data.umask.enable is true "),

  hbase_metrics_showTableName(
      "hbase.metrics.showTableName",
      "true",
      "Whether to include the prefix tbl.tablename in per-column family metrics. If true for each metric M per-cf metrics will be reported for tbl.T.cf.CF.M if false per-cf metrics will be aggregated by column-family across tables and reported for cf.CF.M. In both cases the aggregated metric M across tables and cfs will be reported. "),

  hbase_metrics_exposeOperationTimes(
      "hbase.metrics.exposeOperationTimes",
      "true",
      "Whether to report metrics about time taken performing an operation on the region server. Get Put Delete Increment and Append can all have their times exposed through Hadoop metrics per CF and per region. "),

  hbase_snapshot_enabled("hbase.snapshot.enabled", "true",
      "Set to true to allow snapshots to be taken / restored / cloned. "),

  hbase_snapshot_restore_take_failsafe_snapshot(
      "hbase.snapshot.restore.take.failsafe.snapshot",
      "true",
      "Set to true to take a snapshot before the restore operation. The snapshot taken will be used in case of failure to restore the previous state. At the end of the restore operation this snapshot will be deleted "),

  hbase_snapshot_restore_failsafe_name(
      "hbase.snapshot.restore.failsafe.name",
      "hbase-failsafe-{snapshot.name}-{restore.timestamp}",
      "Name of the failsafe snapshot taken by the restore operation. You can use the {snapshot.name} {table.name} and {restore.timestamp} variables to create a name based on what you are restoring. "),

  hbase_server_compactchecker_interval_multiplier(
      "hbase.server.compactchecker.interval.multiplier",
      "1000",
      "The number that determines how often we scan to see if compaction is necessary. Normally compactions are done after some events (such as memstore flush) but if region didn't receive a lot of writes for some time or due to different compaction policies it may be necessary to check it periodically. The interval between checks is hbase.server.compactchecker.interval.multiplier multiplied by hbase.server.thread.wakefrequency. "),

  hbase_lease_recovery_timeout("hbase.lease.recovery.timeout", "900000",
      "How long we wait on dfs lease recovery in total before giving up. "),

  hbase_lease_recovery_dfs_timeout(
      "hbase.lease.recovery.dfs.timeout",
      "64000",
      "How long between dfs recover lease invocations. Should be larger than the sum of the time it takes for the namenode to issue a block recovery command as part of datanode; dfs.heartbeat.interval and the time it takes for the primary datanode performing block recovery to timeout on a dead datanode; usually dfs.client.socket-timeout. See the end of HBASE-8389 for more. "),

  hbase_column_max_version("hbase.column.max.version", "1",
      "New column family descriptors will use this value as the default number of versions to keep. "),

  hbase_dfs_client_read_shortcircuit_buffer_size(
      "hbase.dfs.client.read.shortcircuit.buffer.size",
      "131072",
      "If the DFSClient configuration dfs.client.read.shortcircuit.buffer.size is unset we will use what is configured here as the short circuit read default direct byte buffer size. DFSClient native default is 1MB; HBase keeps its HDFS files open so number of file blocks * 1MB soon starts to add up and threaten OOME because of a shortage of direct memory. So we set it down from the default. Make it > the default hbase block size set in the HColumnDescriptor which is usually 64k. "),

  hbase_regionserver_checksum_verify(
      "hbase.regionserver.checksum.verify",
      "true",
      "If set to true (the default) HBase verifies the checksums for hfile blocks. HBase writes checksums inline with the data when it writes out hfiles. HDFS (as of this writing) writes checksums to a separate file than the data file necessitating extra seeks. Setting this flag saves some on i/o. Checksum verification by HDFS will be internally disabled on hfile streams when this flag is set. If the hbase-checksum verification fails we will switch back to using HDFS checksums (so do not disable HDFS checksums! And besides this feature applies to hfiles only not to WALs). If this parameter is set to false then hbase will not verify any checksums instead it will depend on checksum verification being done in the HDFS client. "),

  hbase_hstore_bytes_per_checksum("hbase.hstore.bytes.per.checksum", "16384",
      "Number of bytes in a newly created checksum chunk for HBase-level checksums in hfile blocks. "),

  hbase_hstore_checksum_algorithm("hbase.hstore.checksum.algorithm", "CRC32C",
      "Name of an algorithm that is used to compute checksums. Possible values are NULL CRC32 CRC32C. "),

  hbase_client_scanner_max_result_size(
      "hbase.client.scanner.max.result.size",
      "2097152",
      "Maximum number of bytes returned when calling a scanner's next method. Note that when a single row is larger than this limit the row is still returned completely. The default value is 2MB which is good for 1ge networks. With faster and/or high latency networks this value should be increased. "),

  hbase_server_scanner_max_result_size(
      "hbase.server.scanner.max.result.size",
      "104857600",
      "Maximum number of bytes returned when calling a scanner's next method. Note that when a single row is larger than this limit the row is still returned completely. The default value is 100MB. This is a safety setting to protect the server from OOM situations. "),

  hbase_status_published(
      "hbase.status.published",
      "false",
      "This setting activates the publication by the master of the status of the region server. When a region server dies and its recovery starts the master will push this information to the client application to let them cut the connection immediately instead of waiting for a timeout. "),

  hbase_status_publisher_class("hbase.status.publisher.class",
      "org.apache.hadoop.hbase.master.ClusterStatusPublisher$MulticastPublisher",
      "Implementation of the status publication with a multicast message. "),

  hbase_status_listener_class("hbase.status.listener.class",
      "org.apache.hadoop.hbase.client.ClusterStatusListener$MulticastListener",
      "Implementation of the status listener with a multicast message. "),

  hbase_status_multicast_address_ip("hbase.status.multicast.address.ip", "226.1.1.3",
      "Multicast address to use for the status publication by multicast. "),

  hbase_status_multicast_address_port("hbase.status.multicast.address.port", "16100",
      "Multicast port to use for the status publication by multicast. "),

  hbase_dynamic_jars_dir(
      "hbase.dynamic.jars.dir",
      "${hbase.rootdir}/lib",
      "The directory from which the custom filter/co-processor jars can be loaded dynamically by the region server without the need to restart. However an already loaded filter/co-processor class would not be un-loaded. See HBASE-1936 for more details. "),

  hbase_security_authentication(
      "hbase.security.authentication",
      "simple",
      "Controls whether or not secure authentication is enabled for HBase. Possible values are 'simple' (no authentication) and 'kerberos'. "),

  hbase_rest_filter_classes("hbase.rest.filter.classes",
      "org.apache.hadoop.hbase.rest.filter.GzipFilter", "Servlet filters for REST service. "),

  hbase_master_loadbalancer_class(
      "hbase.master.loadbalancer.class",
      "org.apache.hadoop.hbase.master.balancer.StochasticLoadBalancer",
      "Class used to execute the regions balancing when the period occurs. See the class comment for more on how it works http://hbase.apache.org/devapidocs/org/apache/hadoop/hbase/master/balancer/StochasticLoadBalancer.html It replaces the DefaultLoadBalancer as the default (since renamed as the SimpleLoadBalancer). "),

  hbase_security_exec_permission_checks(
      "hbase.security.exec.permission.checks",
      "false",
      "If this setting is enabled and ACL based access control is active (the AccessController coprocessor is installed either as a system coprocessor or on a table as a table coprocessor) then you must grant all relevant users EXEC privilege if they require the ability to execute coprocessor endpoint calls. EXEC privilege like any other permission can be granted globally to a user or to a user on a per table or per namespace basis. For more information on coprocessor endpoints see the coprocessor section of the HBase online manual. For more information on granting or revoking permissions using the AccessController see the security section of the HBase online manual. "),

  hbase_procedure_regionserver_classes(
      "hbase.procedure.regionserver.classes",
      "",
      "A comma-separated list of org.apache.hadoop.hbase.procedure.RegionServerProcedureManager procedure managers that are loaded by default on the active HRegionServer process. The lifecycle methods (init/start/stop) will be called by the active HRegionServer process to perform the specific globally barriered procedure. After implementing your own RegionServerProcedureManager just put it in HBase's classpath and add the fully qualified class name here. "),

  hbase_procedure_master_classes(
      "hbase.procedure.master.classes",
      "",
      "A comma-separated list of org.apache.hadoop.hbase.procedure.MasterProcedureManager procedure managers that are loaded by default on the active HMaster process. A procedure is identified by its signature and users can use the signature and an instant name to trigger an execution of a globally barriered procedure. After implementing your own MasterProcedureManager just put it in HBase's classpath and add the fully qualified class name here. "),

  hbase_coordinated_state_manager_class("hbase.coordinated.state.manager.class",
      "org.apache.hadoop.hbase.coordination.ZkCoordinatedStateManager",
      "Fully qualified name of class implementing coordinated state manager. "),

  hbase_regionserver_storefile_refresh_period(
      "hbase.regionserver.storefile.refresh.period",
      "0",
      "The period (in milliseconds) for refreshing the store files for the secondary regions. 0 means this feature is disabled. Secondary regions sees new files (from flushes and compactions) from primary once the secondary region refreshes the list of files in the region (there is no notification mechanism). But too frequent refreshes might cause extra Namenode pressure. If the files cannot be refreshed for longer than HFile TTL (hbase.master.hfilecleaner.ttl) the requests are rejected. Configuring HFile TTL to a larger value is also recommended with this setting. "),

  hbase_region_replica_replication_enabled(
      "hbase.region.replica.replication.enabled",
      "false",
      "Whether asynchronous WAL replication to the secondary region replicas is enabled or not. If this is enabled a replication peer named region_replica_replication will be created which will tail the logs and replicate the mutatations to region replicas for tables that have region replication > 1. If this is enabled once disabling this replication also requires disabling the replication peer using shell or ReplicationAdmin java class. Replication to secondary region replicas works over standard inter-cluster replication. So replication if disabled explicitly also has to be enabled by setting hbase.replication to true for this feature to work. "),

  hbase_http_filter_initializers(
      "hbase.http.filter.initializers",
      "org.apache.hadoop.hbase.http.lib.StaticUserWebFilter",
      "A comma separated list of class names. Each class in the list must extend org.apache.hadoop.hbase.http.FilterInitializer. The corresponding Filter will be initialized. Then the Filter will be applied to all user facing jsp and servlet web pages. The ordering of the list defines the ordering of the filters. The default StaticUserWebFilter add a user principal as defined by the hbase.http.staticuser.user property. "),

  hbase_security_visibility_mutations_checkauths(
      "hbase.security.visibility.mutations.checkauths",
      "false",
      "This property if enabled will check whether the labels in the visibility expression are associated with the user issuing the mutation "),

  hbase_http_max_threads("hbase.http.max.threads", "10",
      "The maximum number of threads that the HTTP Server will create in its ThreadPool. "),

  hbase_replication_rpc_codec(
      "hbase.replication.rpc.codec",
      "org.apache.hadoop.hbase.codec.KeyValueCodecWithTags",
      "The codec that is to be used when replication is enabled so that the tags are also replicated. This is used along with HFileV3 which supports tags in them. If tags are not used or if the hfile version used is HFileV2 then KeyValueCodec can be used as the replication codec. Note that using KeyValueCodecWithTags for replication when there are no tags causes no harm. "),

  hbase_replication_source_maxthreads(
      "hbase.replication.source.maxthreads",
      "10",
      "The maximum number of threads any replication source will use for shipping edits to the sinks in parallel. This also limits the number of chunks each replication batch is broken into. Larger values can improve the replication throughput between the master and slave clusters. The default of 10 will rarely need to be changed. "),

  hbase_http_staticuser_user(
      "hbase.http.staticuser.user",
      "dr.stack",
      "The user name to filter as on static web filters while rendering content. An example use is the HDFS web UI (user to be used for browsing files). "),

  hbase_master_normalizer_class(
      "hbase.master.normalizer.class",
      "org.apache.hadoop.hbase.master.normalizer.SimpleRegionNormalizer",
      "Class used to execute the region normalization when the period occurs. See the class comment for more on how it works http://hbase.apache.org/devapidocs/org/apache/hadoop/hbase/master/normalizer/SimpleRegionNormalizer.html "),

  hbase_regionserver_handler_abort_on_error_percent(
      "hbase.regionserver.handler.abort.on.error.percent",
      "0.5",
      "The percent of region server RPC threads failed to abort RS. -1 Disable aborting; 0 Abort if even a single handler has died; 0.x Abort only when this percent of handlers have died; 1 Abort only all of the handers have died. "),

  hbase_snapshot_master_timeout_millis("hbase.snapshot.master.timeout.millis", "300000",
      "Timeout for master for the snapshot procedure execution "),

  hbase_snapshot_region_timeout("hbase.snapshot.region.timeout", "300000",
      "Timeout for regionservers to keep threads in snapshot request pool waiting ");

  private String key;
  private String value;
  private String description;

  private HBaseConfigureEnum(String key, String value, String description) {
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

  public static void main(String[] args) {
    TreeMap<String, HBaseConfigureEnum> nameEntityMap = new TreeMap<>();
    for (HBaseConfigureEnum e : HBaseConfigureEnum.values()) {
      nameEntityMap.put(e.key(), e);

    }
    HBaseConfigureEnum e = null;
    for (String name : nameEntityMap.keySet()) {
      e = nameEntityMap.get(name);
      System.out.println(e.key() + "\t" + e.value() + "\t" + e.description());
    }
  }
}
