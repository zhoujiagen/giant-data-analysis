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
