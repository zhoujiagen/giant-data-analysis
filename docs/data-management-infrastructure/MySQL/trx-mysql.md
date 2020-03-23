# Transaction in MySQL

- 产生隐式提交的语句: SQL Statements > Transactional and Locking Statements > Statements That Cause an Implicit Commit

## Theory

- SQL2003
- Serializable Snapshot Isolation in PostgreSQL
- A Critique of ANSI SQL Isolation Levels
- On Optimistic Methods for Concurrency Control
- Database Replication Using Generialized Snapshot Isolation
- Generialized Snapshot Isolation and a Prefix-Consistent Implementation
- Weak Consistency: A Generialized Theory and Optimistic Implementation for Distributed Transactions
- Making snapshot isolation serializable
- A critique of snapshot isolation: write-snapshot isolation

## INFORMATION_SCHEMA

```
# query lock blocking in trx
SELECT
	r.trx_isolation_level,
	r.trx_id AS w_trx, r.trx_mysql_thread_id AS w_t,
    b.trx_id AS b_trx, b.trx_mysql_thread_id as b_t,

    r.trx_wait_started, TIMESTAMPDIFF(SECOND, r.trx_wait_started, CURRENT_TIMESTAMP) AS wait_time,

    pr.host AS w_host, r.trx_query AS w_query,
    p.host AS b_host, b.trx_query AS b_query,

    IF(p.command = 'Sleep', p.time, 0) AS idle_in_trx,

     l.lock_table AS waiting_table_lock


FROM information_schema.INNODB_LOCK_WAITS w
	INNER JOIN information_schema.INNODB_TRX b ON b.trx_id = w.blocking_trx_id -- blocking
    INNER JOIN information_schema.INNODB_TRX r ON r.trx_id = w.requesting_trx_id -- requesting
    INNER JOIN information_schema.INNODB_LOCKS l ON w.requested_lock_id = l.lock_id
    LEFT JOIN information_schema.PROCESSLIST p ON p.id = b.trx_mysql_thread_id
    LEFT JOIN information_schema.PROCESSLIST pr ON pr.id = r.trx_mysql_thread_id
ORDER BY wait_time DESC;

# kill thread
-- KILL 663590;
```
