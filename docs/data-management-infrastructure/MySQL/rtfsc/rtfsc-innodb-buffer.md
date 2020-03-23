# InnoDB Buffer

###  buf: The database buffer

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

###  ibuf: Insert buffer

|header|description|
|:---|:---|
| ibuf0ibuf.h | Insert buffer |
| ibuf0types.h | Insert buffer global types |
