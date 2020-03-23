# High Performance MySQL

## 资源

- [MySQL Documentation](https://dev.mysql.com/doc/)
- 示例代码: [High Performance MySQL](http://www.highperfmysql.com/)
- [MySQL性能博客](https://www.percona.com/blog/category/mysql/)
- [TPC](http://www.tpc.org/): Transaction Processing Performance Council

## 基准测试(Benchmark)

基准测试要尽量简单直接, 结果之间容易互相比较, 成本低且易于执行.

两种策略:

- full-stack: 针对整个系统的整体测试
- single-component: 单独测试MySQL

测试的指标:

- 吞吐量: 每秒事务数(TPS)
- 响应时间, 延迟
- 并发性
- 可扩展性

基准测试工具:

- ab
- http_load
- JMeter
- mysqlslap
- MySQL Benchmark Suite(sql-bench)
- Super Smack
- Database Test Suite: dbt2
- Percona's TPCC-MySQL Tool
- sysbench: [Scriptable database and system performance benchmark](https://github.com/akopytov/sysbench)

## 性能剖析(Profiling)

性能相关的问题:

- 如何确认服务器是否达到了性能最佳的状态
- 找出某条语句为什么执行不够快
- 诊断间歇性疑难故障

完成任务所需要的时间: 执行时间和等待时间.

性能剖析的步骤:

- 测量任务所花费的时间
- 统计和分析测量结果, 将重要的任务排到前面

性能剖析工具:

- Percona Toolkit: [Percona Toolkit Documentation](https://www.percona.com/doc/percona-toolkit/LATEST/index.html)
- oprofile
- tcpdump
- gdb
- INFORMATION_SCHEMA, PERFORMANCE_SCHEMA
- strace
- [innotop](https://github.com/innotop/innotop): A realtime terminal-based top-like monitor for MySQL


## 服务器配置

- [v5.7 服务器选项, 系统变量, 状态变量](https://dev.mysql.com/doc/refman/5.7/en/server-option-variable-reference.html)


管理工具:

- [mycli](https://github.com/dbcli/mycli): A Terminal Client for MySQL with AutoCompletion and Syntax Highlighting.
- [gh-ost](https://github.com/github/gh-ost): GitHub's Online Schema Migrations for MySQL
- PhpMyAdmin
- [sqlcheck](https://github.com/jarulraj/sqlcheck): Automatically identify anti-patterns in SQL queries
- [orchestrator](https://github.com/openark/orchestrator): MySQL replication topology management and HA
- [Percona Monitoring and Management (PMM)](https://www.percona.com/software/database-tools/percona-monitoring-and-management): Single pane of glass for managing and monitoring the performance of your MySQL, MariaDB, PostgreSQL, and MongoDB databases.


## 复制(Replication)

- [Tools for Monitoring and Managing MySQL Replication](https://www.percona.com/blog/2015/03/09/5-free-handy-tools-for-monitoring-and-managing-mysql-replication/): pt-heartbeat, pt-slave-find, pt-slave-restart, pt-table-checksum, pt-table-sync


## 备份和恢复

工具:

- MySQL Enterprise Backup
- Percona XtraBackup
- mylvmbackup
- Zmanda Recovery Manager
- mydumper
- mysqldump

## DB Medtadata

### INFORMATION_SCHEMA

``` sql
-- 表名称
SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.TABLES t
WHERE t.TABLE_SCHEMA='information_schema' ORDER BY  t.TABLE_NAME;

-- 表字段
SELECT ORDINAL_POSITION, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='information_schema' AND TABLE_NAME='INNODB_LOCKS'
ORDER BY ORDINAL_POSITION;
```

|Table Name| Description|
|:----------|:-----------|
|CHARACTER_SETS	| |
|COLLATIONS	| |
|COLLATION_CHARACTER_SET_APPLICABILITY	| |
|COLUMNS	| |
|COLUMN_PRIVILEGES	| |
|ENGINES	| |
|EVENTS	| |
|FILES	| |
|GLOBAL_STATUS	| |
|GLOBAL_VARIABLES	| |
|INNODB_BUFFER_PAGE	| |
|INNODB_BUFFER_PAGE_LRU	| |
|INNODB_BUFFER_POOL_STATS	| |
|INNODB_CMP	| |
|INNODB_CMPMEM	| |
|INNODB_CMPMEM_RESET	| |
|INNODB_CMP_PER_INDEX	| |
|INNODB_CMP_PER_INDEX_RESET	| |
|INNODB_CMP_RESET	| |
|INNODB_FT_BEING_DELETED	| |
|INNODB_FT_CONFIG	| |
|INNODB_FT_DEFAULT_STOPWORD	| |
|INNODB_FT_DELETED	| |
|INNODB_FT_INDEX_CACHE	| |
|INNODB_FT_INDEX_TABLE	| |
|INNODB_LOCKS	| |
|INNODB_LOCK_WAITS	| |
|INNODB_METRICS	| |
|INNODB_SYS_COLUMNS	| |
|INNODB_SYS_DATAFILES	| |
|INNODB_SYS_FIELDS	| |
|INNODB_SYS_FOREIGN	| |
|INNODB_SYS_FOREIGN_COLS	| |
|INNODB_SYS_INDEXES	| |
|INNODB_SYS_TABLES	| |
|INNODB_SYS_TABLESPACES	| |
|INNODB_SYS_TABLESTATS	| |
|INNODB_SYS_VIRTUAL	| |
|INNODB_TEMP_TABLE_INFO	| |
|INNODB_TRX	| |
|KEY_COLUMN_USAGE	| |
|OPTIMIZER_TRACE	| |
|PARAMETERS	| |
|PARTITIONS	| |
|PLUGINS	| |
|PROCESSLIST	| |
|PROFILING	| |
|REFERENTIAL_CONSTRAINTS	| |
|ROUTINES	| |
|SCHEMATA	| |
|SCHEMA_PRIVILEGES	| |
|SESSION_STATUS	| |
|SESSION_VARIABLES	| |
|STATISTICS	| |
|TABLES	| |
|TABLESPACES	| |
|TABLE_CONSTRAINTS	| |
|TABLE_PRIVILEGES	| |
|TRIGGERS	| |
|USER_PRIVILEGES	| |
|VIEWS	| |


### PERFORMANCE_SCHEMA

``` sql
-- 表名称
SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.TABLES t
WHERE t.TABLE_SCHEMA='performance_schema' ORDER BY  t.TABLE_NAME;

-- 表字段
SELECT ORDINAL_POSITION, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='performance_schema' AND TABLE_NAME='events_transactions_history'
ORDER BY ORDINAL_POSITION;
```

|Table Name| Description|
|:----------|:-----------|
|accounts | Connection statistics per client account|
|cond_instances| synchronization object instances|
|events_stages_current| Current stage events|
|events_stages_history| Most recent stage events per thread|
|events_stages_history_long| Most recent stage events overall|
|events_stages_summary_by_account_by_event_name| Stage events per account and event name|
|events_stages_summary_by_host_by_event_name| Stage events per host name and event name|
|events_stages_summary_by_thread_by_event_name| Stage waits per thread and event name|
|events_stages_summary_by_user_by_event_name| Stage events per user name and event name|
|events_stages_summary_global_by_event_name| Stage waits per event name|
|events_statements_current| Current statement events|
|events_statements_history| Most recent statement events per thread|
|events_statements_history_long| Most recent statement events overall|
|events_statements_summary_by_account_by_event_name| Statement events per account and event name|
|events_statements_summary_by_digest| Statement events per schema and digest value|
|events_statements_summary_by_host_by_event_name| Statement events per host name and event name|
|events_statements_summary_by_program| Statement events per stored program|
|events_statements_summary_by_thread_by_event_name| Statement events per thread and event name|
|events_statements_summary_by_user_by_event_name| Statement events per user name and event name|
|events_statements_summary_global_by_event_name| Statement events per event name|
|events_transactions_current| Current transaction events|
|events_transactions_history| Most recent transaction events per thread|
|events_transactions_history_long| Most recent transaction events overall|
|events_transactions_summary_by_account_by_event_name| Transaction events per account and event name|
|events_transactions_summary_by_host_by_event_name| Transaction events per host name and event name|
|events_transactions_summary_by_thread_by_event_name| Transaction events per thread and event name|
|events_transactions_summary_by_user_by_event_name| Transaction events per user name and event name|
|events_transactions_summary_global_by_event_name| Transaction events per event name|
|events_waits_current| Current wait events|
|events_waits_history| Most recent wait events per thread|
|events_waits_history_long| Most recent wait events overall|
|events_waits_summary_by_account_by_event_name| Wait events per account and event name|
|events_waits_summary_by_host_by_event_name| Wait events per host name and event name|
|events_waits_summary_by_instance| Wait events per instance|
|events_waits_summary_by_thread_by_event_name| Wait events per thread and event name|
|events_waits_summary_by_user_by_event_name| Wait events per user name and event name|
|events_waits_summary_global_by_event_name| Wait events per event name|
|file_instances| File instances|
|file_summary_by_event_name| File events per event name|
|file_summary_by_instance| File events per file instance|
|global_status| Global status variables|
|global_variables| Global system variables|
|host_cache| Information from the internal host cache|
|hosts| Connection statistics per client host name|
|memory_summary_by_account_by_event_name| Memory operations per account and event name|
|memory_summary_by_host_by_event_name| Memory operations per host and event name|
|memory_summary_by_thread_by_event_name| Memory operations per thread and event name|
|memory_summary_by_user_by_event_name| Memory operations per user and event name|
|memory_summary_global_by_event_name| Memory operations globally per event name|
|metadata_locks| Metadata locks and lock requests|
|mutex_instances| Mutex synchronization object instances|
|objects_summary_global_by_type| Object summaries|
|performance_timers| Which event timers are available|
|prepared_statements_instances| Prepared statement instances and statistics|
|replication_applier_configuration| Configuration parameters for the transaction applier on the slave|
|replication_applier_status| Current status of the transaction applier on the slave|
|replication_applier_status_by_coordinator| SQL or coordinator thread applier status|
|replication_applier_status_by_worker| Worker thread applier status (empty unless slave is multithreaded)|
|replication_connection_configuration| Configuration parameters for connecting to the master|
|replication_connection_status| Current status of the connection to the master|
|rwlock_instances| Lock synchronization object instances|
|session_account_connect_attrs| Connection attributes per for the current session|
|session_connect_attrs| Connection attributes for all sessions|
|session_status| Status variables for current session|
|session_variables| System variables for current session|
|setup_actors| How to initialize monitoring for new foreground threads|
|setup_consumers| Consumers for which event information can be stored|
|setup_instruments| Classes of instrumented objects for which events can be collected|
|setup_objects| Which objects should be monitored|
|setup_timers| Current event timer|
|socket_instances| Active connection instances|
|socket_summary_by_event_name| Socket waits and I/O per event name|
|socket_summary_by_instance| Socket waits and I/O per instance|
|status_by_account| Session status variables per account|
|status_by_host| Session status variables per host name|
|status_by_thread| Session status variables per session|
|status_by_user| Session status variables per user name|
|table_handles| Table locks and lock requests|
|table_io_waits_summary_by_index_usage| Table I/O waits per index|
|table_io_waits_summary_by_table| Table I/O waits per table|
|table_lock_waits_summary_by_table| Table lock waits per table|
|threads| Information about server threads|
|user_variables_by_thread| User-defined variables per thread|
|users| Connection statistics per client user name|
|variables_by_thread| Session system variables per session|

### SYS

``` sql
-- 表名称
SELECT t.TABLE_NAME, t.TABLE_COMMENT FROM information_schema.TABLES t
WHERE t.TABLE_SCHEMA='sys' ORDER BY  t.TABLE_NAME;

-- 表字段
SELECT ORDINAL_POSITION, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='sys' AND TABLE_NAME='processlist'
ORDER BY ORDINAL_POSITION;
```

|Table Name| Description|
|:----------|:-----------|
|host_summary| |
|host_summary_by_file_io| |
|host_summary_by_file_io_type| |
|host_summary_by_stages| |
|host_summary_by_statement_latency| |
|host_summary_by_statement_type| |
|innodb_buffer_stats_by_schema| |
|innodb_buffer_stats_by_table| |
|innodb_lock_waits| |
|io_by_thread_by_latency| |
|io_global_by_file_by_bytes| |
|io_global_by_file_by_latency| |
|io_global_by_wait_by_bytes| |
|io_global_by_wait_by_latency| |
|latest_file_io| |
|memory_by_host_by_current_bytes| |
|memory_by_thread_by_current_bytes| |
|memory_by_user_by_current_bytes| |
|memory_global_by_current_bytes| |
|memory_global_total| |
|metrics| |
|processlist| |
|ps_check_lost_instrumentation| |
|schema_auto_increment_columns| |
|schema_index_statistics| |
|schema_object_overview| |
|schema_redundant_indexes| |
|schema_tables_with_full_table_scans| |
|schema_table_lock_waits| |
|schema_table_statistics| |
|schema_table_statistics_with_buffer| |
|schema_unused_indexes| |
|session| |
|session_ssl_status| |
|statements_with_errors_or_warnings| |
|statements_with_full_table_scans| |
|statements_with_runtimes_in_95th_percentile| |
|statements_with_sorting| |
|statements_with_temp_tables| |
|statement_analysis| |
|sys_config
|user_summary| |
|user_summary_by_file_io| |
|user_summary_by_file_io_type| |
|user_summary_by_stages| |
|user_summary_by_statement_latency| |
|user_summary_by_statement_type| |
|version| |
|waits_by_host_by_latency| |
|waits_by_user_by_latency| |
|waits_global_by_latency| |
|wait_classes_global_by_avg_latency| |
|wait_classes_global_by_latency| |
|x$host_summary| |
|x$host_summary_by_file_io| |
|x$host_summary_by_file_io_type| |
|x$host_summary_by_stages| |
|x$host_summary_by_statement_latency| |
|x$host_summary_by_statement_type| |
|x$innodb_buffer_stats_by_schema| |
|x$innodb_buffer_stats_by_table| |
|x$innodb_lock_waits| |
|x$io_by_thread_by_latency| |
|x$io_global_by_file_by_bytes| |
|x$io_global_by_file_by_latency| |
|x$io_global_by_wait_by_bytes| |
|x$io_global_by_wait_by_latency| |
|x$latest_file_io| |
|x$memory_by_host_by_current_bytes| |
|x$memory_by_thread_by_current_bytes| |
|x$memory_by_user_by_current_bytes| |
|x$memory_global_by_current_bytes| |
|x$memory_global_total| |
|x$processlist| |
|x$ps_digest_95th_percentile_by_avg_us| |
|x$ps_digest_avg_latency_distribution| |
|x$ps_schema_table_statistics_io| |
|x$schema_flattened_keys| |
|x$schema_index_statistics| |
|x$schema_tables_with_full_table_scans| |
|x$schema_table_lock_waits| |
|x$schema_table_statistics| |
|x$schema_table_statistics_with_buffer| |
|x$session| |
|x$statements_with_errors_or_warnings| |
|x$statements_with_full_table_scans| |
|x$statements_with_runtimes_in_95th_percentile| |
|x$statements_with_sorting| |
|x$statements_with_temp_tables| |
|x$statement_analysis| |
|x$user_summary| |
|x$user_summary_by_file_io| |
|x$user_summary_by_file_io_type| |
|x$user_summary_by_stages| |
|x$user_summary_by_statement_latency| |
|x$user_summary_by_statement_type| |
|x$waits_by_host_by_latency| |
|x$waits_by_user_by_latency| |
|x$waits_global_by_latency| |
|x$wait_classes_global_by_avg_latency| |
|x$wait_classes_global_by_latency| |
