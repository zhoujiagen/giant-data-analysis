# InnoDB Utilities

###  fut: File-based utilities

|header|description|
|:---|:---|
| fut0fut.h | File-based utilities |
| fut0lst.h | File-based list utilities |

``` CPP
fut0lst.h

flst_base_node_t
flst_node_t
```

###  ha: The hash table

|header|description|
|:---|:---|
| ha0ha.h | The hash table with external chains|
| ha0storage.h | Hash storage. Provides a data structure that stores chunks of data in its own storage, avoiding duplicates.|
| ha_prototypes.h | Prototypes for global functions in ha_innodb.cc that are called by InnoDB C code. |
| hash0hash.h | The simple hash table utility |


``` CPP
hash0hash.h

hash_cell_t
hash_table_t
```

###  ut: Utilities

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


``` CPP
ut0lst.h

ut_list_base
ut_list_node

ut0list.h
ib_list_t
ib_list_node_t
```

```
ut0sort.h

UT_SORT_FUNCTION_BODY
```
