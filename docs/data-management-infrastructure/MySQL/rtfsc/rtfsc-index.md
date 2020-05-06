# RTFSC MySQL

- [mysql/mysql-server](https://github.com/mysql/mysql-server): MySQL Server, the world's most popular open source database, and MySQL Cluster, a real-time, open source transactional database.
- [MySQL Internals Manual](https://dev.mysql.com/doc/internals/en/)
- [MySQL Server Doxygen Documentation](https://dev.mysql.com/doc/dev/mysql-server/latest/)

## Terminology

!!! info "in memory structures"
    - buffer pool
    - change buffer
    - log buffer
    - adaptive hash index

!!! info "on disk structures"
    - system tablespace: InnoDB Data Dictionary, Doublewrite Buffer, Change Buffer, Undo Logs
    - undo tablespace
    - file-per-table tablespace
    - general tablespace
    - temporaty tablespace
    - redo log


## Tool and Environment Settings

```
Eclipse
perference -> C/C++ -> Build -> Environment .And add
PATH /usr/local/bin

Ninja
https://ninja-build.org/
https://github.com/ninja-build/ninja/releases
$ brew install ninja

Boost
MySQL currently requires boost_1_70_0
$ brew install boost

Run MySQL
mkdir data
bin/mysqld --initialize-insecure --user=zhang
```


## InnoDB Storage Engine

- [How to optimize MySQL for large BLOB updates](https://dba.stackexchange.com/questions/46543/how-to-optimize-mysql-for-large-blob-updates)


### univ.i

|header|description|
|:---|:---|
| univ.i | Version control for database, common definitions, and include files |


!!! info "hot backup"
    refman-5.7-en.html-chapter/glossary.html#glos_hot_backup

    A backup taken while the database is running and applications are reading and writing to it. The backup involves more than simply copying data files: it must include any data that was inserted or updated while the backup was in process; it must exclude any data that was deleted while the backup was in process; and it must ignore any changes that were not committed.

    The Oracle product that performs hot backups, of InnoDB tables especially but also tables from MyISAM and other storage engines, is known as MySQL Enterprise Backup.

    The hot backup process consists of two stages. The initial copying of the data files produces a raw backup. The apply step incorporates any changes to the database that happened while the backup was running. Applying the changes produces a prepared backup; these files are ready to be restored whenever necessary.

``` CPP
univ.i

#ifdef _WIN64
typedef unsigned __int64	ulint;
typedef __int64			lint;
# define ULINTPF		UINT64PF
#else
typedef unsigned long int	ulint;
typedef long int		lint;
# define ULINTPF		"%lu"
#endif /* _WIN64 */
```


### db0err.h

|header|description|
|:---|:---|
| db0err.h | Global error codes for the database |

###  api: InnoDB Native API

|header|description|
|:---|:---|
| api0api.h | InnoDB Native API |
| api0misc.h | InnoDB Native API |


###  [btr: The B-Tree](rtfsc-innodb-page-row-record.md)

|header|description|
|:---|:---|
| btr0btr.h | The B-tree |
| btr0bulk.h | The B-tree bulk load |
| btr0cur.h | The index tree cursor |
| btr0pcur.h | The index tree persistent cursor |
| btr0sea.h | The index tree adaptive search |
| btr0types.h | The index tree general types |


###  [buf: The database buffer](rtfsc-innodb-buffer.md)

|header|description|
|:---|:---|
| buf0buddy.h | Binary buddy allocator for compressed pages |
| buf0buf.h | The database buffer pool high-level routines |
| buf0checksum.h | Buffer pool checksum functions, also linked from /extra/innochecksum.cc |
| buf0dblwr.h | Doublewrite buffer module |
| buf0dump.h | Implements a buffer pool dump/load. |
| buf0flu.h | The database buffer pool flush algorithm |
| buf0lru.h | The database buffer pool LRU replacement algorithm |
| buf0rea.h | The database buffer read |
| buf0types.h | The database buffer pool global types for the directory |


###  data: SQL data field and tuple, data types

|header|description|
|:---|:---|
| data0data.h | SQL data field and tuple |
| data0type.h | Data types |
| data0types.h | Some type definitions |



###  dict: Data dictionary system

|header|description|
|:---|:---|
| dict0boot.h | Data dictionary creation and booting |
| dict0crea.h | Database object creation |
| dict0dict.h | Data dictionary system |
| dict0load.h | Loads to the memory cache database object definitions from dictionary tables |
| dict0mem.h | Data dictionary memory object creation |
| dict0priv.h | Data dictionary private functions |
| dict0stats.h | Code used for calculating and manipulating table statistics. |
| dict0stats_bg.h | Code used for background table and index stats gathering. |
| dict0types.h | Data dictionary global types |


###  eval: SQL evaluator

|header|description|
|:---|:---|
| eval0eval.h | SQL evaluator: evaluates simple data structures, like expressions, in a query graph |
| eval0proc.h | Executes SQL stored procedures and their control structures |

###  [fil: The low-level file system](rtfsc-innodb-file-space-mgmt.md)

|header|description|
|:---|:---|
| fil0fil.h | The low-level file system |

###  [fsp: File space management](rtfsc-innodb-file-space-mgmt.md)

|header|description|
|:---|:---|
| fsp0file.h | Tablespace data file implementation. |
| fsp0fsp.h | File space management |
| fsp0space.h | General shared tablespace implementation. |
| fsp0sysspace.h | Multi file, shared, system tablespace implementation. |
| fsp0types.h | File space management types |

###  fts: Full text search

|header|description|
|:---|:---|
| fts0ast.h | The FTS query parser (AST) abstract syntax tree routines |
| fts0blex.h | FTS parser lexical analyzer |
| fts0fts.h | Full text search header file |
| fts0opt.h | Full Text Search optimize thread |
| fts0pars.h | FTS parser tokens |
| fts0plugin.h | Full text search plugin header file  |
| fts0priv.h | Full text search internal header file |
| fts0tlex.h | FTS parser lexical analyzer |
| fts0tokenize.h | Full Text Search plugin tokenizer refer to MyISAM |
| fts0types.h | Full text search types file |

###  [fut: File-based utilities](rtfsc-innodb-utilities.md)

|header|description|
|:---|:---|
| fut0fut.h | File-based utilities |
| fut0lst.h | File-based list utilities |


###  gis: The R-tree

|header|description|
|:---|:---|
| gis0geo.h | The r-tree define from MyISAM |
| gis0rtree.h | R-tree header file |
| gis0type.h | R-tree header file |


###  handler: The InnoDB handler

|header|description|
|:---|:---|
| handler0alter.h | Smart ALTER TABLE |

###  [ha: The hash table](rtfsc-innodb-utilities.md)

|header|description|
|:---|:---|
| ha0ha.h | The hash table with external chains|
| ha0storage.h | Hash storage. Provides a data structure that stores chunks of data in its own storage, avoiding duplicates.|
| ha_prototypes.h | Prototypes for global functions in ha_innodb.cc that are called by InnoDB C code. |
| hash0hash.h | The simple hash table utility |


###  [ibuf: Insert buffer](rtfsc-innodb-buffer.md)

|header|description|
|:---|:---|
| ibuf0ibuf.h | Insert buffer |
| ibuf0types.h | Insert buffer global types |


###  [lock: The transaction lock system](rtfsc-innodb-lock.md)

|header|description|
|:---|:---|
| lock0iter.h | Lock queue iterator type and function prototypes. |
| lock0lock.h | The transaction lock system |
| lock0prdt.h | The predicate lock system |
| lock0priv.h | Lock module internal structures and methods. |
| lock0types.h | The transaction lock system global types |


###  [log: Database log](rtfsc-innodb-redo-log.md)

|header|description|
|:---|:---|
| log0log.h | Database log |
| log0recv.h | Recovery |
| log0types.h | Log types |


###  mach: Utilities for converting data from the database file to the machine format.

|header|description|
|:---|:---|
| mach0data.h | Utilities for converting data from the database file to the machine format. |


###  [mem: The memory management](rtfsc-innodb-memory-mgmt.md)

|header|description|
|:---|:---|
| mem0mem.h | The memory management |
| dyn0buf.h | The dynamically allocated buffer implementation |
| dyn0types.h | The dynamically allocated buffer types and constants|


###  [mtr: Mini-transaction buffer](rtfsc-innodb-trx.md)

|header|description|
|:---|:---|
| mtr0log.h | Mini-transaction logging routines |
| mtr0mtr.h | Mini-transaction buffer |
| mtr0types.h | Mini-transaction buffer global types |


###  os: The interface to the operating system

|header|description|
|:---|:---|
| os0atomic.h | Macros for using atomics |
| os0event.h | The interface to the operating system condition variables |
| os0file.h | [The interface to the operating system file io](rtfsc-innodb-file-space-mgmt.md) |
| os0once.h | A class that aids executing a given function exactly once in a multi-threaded environment. |
| os0proc.h | The interface to the operating system process control primitives |
| os0thread.h | The interface to the operating system process and thread control primitives |


``` CPP
os0atomic.h

```

###  [page: Index page routines](rtfsc-innodb-page-row-record.md)

|header|description|
|:---|:---|
| page0cur.h | The page cursor |
| page0page.h | Index page routines |
| page0size.h | A class describing a page size. |
| page0types.h | Index page routines |
| page0zip.h | Compressed page interface |


###  [pars: SQL parser](rtfsc-innodb-query.md)

|header|description|
|:---|:---|
| pars0grm.h | Bison parser tokens|
| pars0opt.h | Simple SQL optimizer |
| pars0pars.h | SQL parser |
| pars0sym.h | SQL parser symbol table |
| pars0types.h | SQL parser global types |

###  [que: Query graph](rtfsc-innodb-query.md)

|header|description|
|:---|:---|
| que0que.h | Query graph |
| que0types.h | Query graph global types |


###  [read: Cursor read](rtfsc-innodb-query.md)

|header|description|
|:---|:---|
| read0read.h | Cursor read |
| read0types.h | Cursor read |

###  [rem: Record manager](rtfsc-innodb-page-row-record.md)

|header|description|
|:---|:---|
| rem0cmp.h | Comparison services for records |
| rem0rec.h | Record manager |
| rem0types.h | Record manager global types |

###  [row: Row routines](rtfsc-innodb-page-row-record.md)

|header|description|
|:---|:---|
| row0ext.h | Caching of externally stored column prefixes |
| row0ftsort.h | Create Full Text Index with (parallel) merge sort |
| row0import.h | Header file for import tablespace functions. |
| row0ins.h | Insert into a table |
| row0log.h | Modification log for online index creation and online table rebuild |
| row0merge.h | Index build routines using a merge sort |
| row0mysql.h | Interface between Innobase row operations and MySQL. Contains also create table and other data dictionary operations. |
| row0purge.h | Purge obsolete records |
| row0quiesce.h | Header file for tablespace quiesce functions. |
| row0row.h | General row routines |
| row0sel.h | Select |
| row0trunc.h | TRUNCATE implementation |
| row0types.h | Row operation global types |
| row0uins.h | Fresh insert undo |
| row0umod.h | Undo modify of a row |
| row0undo.h | Row undo |
| row0upd.h | Update of a row |
| row0vers.h | Row versions |


###  srv: The InnoDB database server

|header|description|
|:---|:---|
| srv0conc.h | InnoDB concurrency manager header file |
| srv0mon.h | Server monitor counter related defines |
| srv0srv.h | The server main program |
| srv0start.h | Starts the Innobase database server |

###  [sync: synchronization routines](rtfsc-innodb-sync.md)

|header|description|
|:---|:---|
| sync0arr.h | The wait array used in synchronization primitives |
| sync0debug.h | Debug checks for latches, header file |
| sync0policy.h | Policies for mutexes. |
| sync0rw.h | The read-write lock (for threads, not for database transactions) |
| sync0sync.h | Mutex, the basic synchronization primitive |
| sync0types.h | Global types for sync |

###  [trx: Transaction system](rtfsc-innodb-trx.md)

|header|description|
|:---|:---|
| trx0i_s.h | INFORMATION SCHEMA innodb_trx, innodb_locks and innodb_lock_waits tables cache structures and public functions.|
| trx0purge.h | Purge old versions |
| trx0rec.h | Transaction undo log record |
| trx0roll.h | Transaction rollback |
| trx0rseg.h | Rollback segment |
| trx0sys.h | Transaction system |
| trx0trx.h | The transaction |
| trx0types.h | Transaction system global type definitions |
| trx0undo.h | Transaction undo log |
| trx0xa.h | XA transaction |


###  usr: Users and Sessions

|header|description|
|:---|:---|
| sess0sess.h | InnoDB session state tracker. Multi file, shared, system tablespace implementation. |
| usr0sess.h | Sessions |
| usr0types.h | Users and sessions global types |


###  [ut: Utilities](rtfsc-innodb-utilities.md)

|header|description|
|:---|:---|

| ut0byte.h | Utilities for byte operations |
| ut0counter.h | Counter utility class |
| ut0crc32.h | CRC32 implementation |
| ut0dbg.h | Debug utilities for Innobase |
| ut0list.h | A double-linked list |
| ut0lst.h | List utilities |
| ut0mem.h | Memory primitives |
| ut0mutex.h | Policy based mutexes. |
| ut0new.h | Instrumented memory allocator. |
| ut0pool.h | Object pool. |
| ut0rbt.h | Various utilities: Red black tree |
| ut0rnd.h | Random numbers and hashing |
| ut0sort.h | Sort utility |
| ut0stage.h | Supplementary code to performance schema stage instrumentation. |
| ut0ut.h | Various utilities |
| ut0vec.h | A vector of pointers to data items |
| ut0wqueue.h | A work queue |
