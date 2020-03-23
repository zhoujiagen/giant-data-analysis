# InnoDB Page, Row, Record

###  btr: The B-Tree

|header|description|
|:---|:---|
| btr0btr.h | The B-tree |
| btr0bulk.h | The B-tree bulk load |
| btr0cur.h | The index tree cursor |
| btr0pcur.h | The index tree persistent cursor |
| btr0sea.h | The index tree adaptive search |
| btr0types.h | The index tree general types |


###  page: Index page routines

|header|description|
|:---|:---|
| page0cur.h | The page cursor |
| page0page.h | Index page routines |
| page0size.h | A class describing a page size. |
| page0types.h | Index page routines |
| page0zip.h | Compressed page interface |


###  rem: Record manager

|header|description|
|:---|:---|
| rem0cmp.h | Comparison services for records |
| rem0rec.h | Record manager |
| rem0types.h | Record manager global types |

###  row: Row routines

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
