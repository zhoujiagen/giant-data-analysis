# InnoDB Synchronization Mechanisms

###  sync: synchronization routines

|header|description|
|:---|:---|
| ib0mutex.h | Policy based mutexes. |
| sync0arr.h | The wait array used in synchronization primitives |
| sync0debug.h | Debug checks for latches, header file |
| sync0policy.h | Policies for mutexes. |
| sync0rw.h | The read-write lock (for threads, not for database transactions) |
| sync0sync.h | Mutex, the basic synchronization primitive |
| sync0types.h | Global types for sync |

``` CPP
ib0mutex.h

/** OS mutex for tracking lock/unlock for debugging */
template <template <typename> class Policy = NoPolicy>
struct OSTrackMutex {

/** Mutex implementation that used the Linux futex. */
template <template <typename> class Policy = NoPolicy>
struct TTASFutexMutex {

template <template <typename> class Policy = NoPolicy>
struct TTASMutex {

template <template <typename> class Policy = NoPolicy>
struct TTASEventMutex {

/** Mutex interface for all policy mutexes. This class handles the interfacing
with the Performance Schema instrumentation. */
template <typename MutexImpl>
struct PolicyMutex
```

``` CPP
sync0types.h

/*
		LATCHING ORDER WITHIN THE DATABASE
		==================================

The mutex or latch in the central memory object, for instance, a rollback
segment object, must be acquired before acquiring the latch or latches to
the corresponding file data structure. In the latching order below, these
file page object latches are placed immediately below the corresponding
central memory object latch or mutex.

Synchronization object			Notes
----------------------			-----

Dictionary mutex			If we have a pointer to a dictionary
|					object, e.g., a table, it can be
|					accessed without reserving the
|					dictionary mutex. We must have a
|					reservation, a memoryfix, to the
|					appropriate table object in this case,
|					and the table must be explicitly
|					released later.
V
Dictionary header
|
V
Secondary index tree latch		The tree latch protects also all
|					the B-tree non-leaf pages. These
V					can be read with the page only
Secondary index non-leaf		bufferfixed to save CPU time,
|					no s-latch is needed on the page.
|					Modification of a page requires an
|					x-latch on the page, however. If a
|					thread owns an x-latch to the tree,
|					it is allowed to latch non-leaf pages
|					even after it has acquired the fsp
|					latch.
V
Secondary index leaf			The latch on the secondary index leaf
|					can be kept while accessing the
|					clustered index, to save CPU time.
V
Clustered index tree latch		To increase concurrency, the tree
|					latch is usually released when the
|					leaf page latch has been acquired.
V
Clustered index non-leaf
|
V
Clustered index leaf
|
V
Transaction system header
|
V
Transaction undo mutex			The undo log entry must be written
|					before any index page is modified.
|					Transaction undo mutex is for the undo
|					logs the analogue of the tree latch
|					for a B-tree. If a thread has the
|					trx undo mutex reserved, it is allowed
|					to latch the undo log pages in any
|					order, and also after it has acquired
|					the fsp latch.
V
Rollback segment mutex			The rollback segment mutex must be
|					reserved, if, e.g., a new page must
|					be added to an undo log. The rollback
|					segment and the undo logs in its
|					history list can be seen as an
|					analogue of a B-tree, and the latches
|					reserved similarly, using a version of
|					lock-coupling. If an undo log must be
|					extended by a page when inserting an
|					undo log record, this corresponds to
|					a pessimistic insert in a B-tree.
V
Rollback segment header
|
V
Purge system latch
|
V
Undo log pages				If a thread owns the trx undo mutex,
|					or for a log in the history list, the
|					rseg mutex, it is allowed to latch
|					undo log pages in any order, and even
|					after it has acquired the fsp latch.
|					If a thread does not have the
|					appropriate mutex, it is allowed to
|					latch only a single undo log page in
|					a mini-transaction.
V
File space management latch		If a mini-transaction must allocate
|					several file pages, it can do that,
|					because it keeps the x-latch to the
|					file space management in its memo.
V
File system pages
|
V
lock_sys_wait_mutex			Mutex protecting lock timeout data
|
V
lock_sys_mutex				Mutex protecting lock_sys_t
|
V
trx_sys->mutex				Mutex protecting trx_sys_t
|
V
Threads mutex				Background thread scheduling mutex
|
V
query_thr_mutex				Mutex protecting query threads
|
V
trx_mutex				Mutex protecting trx_t fields
|
V
Search system mutex
|
V
Buffer pool mutex
|
V
Log mutex
|
Any other latch
|
V
Memory pool mutex */

/** Latching order levels. If you modify these, you have to also update
LatchDebug internals in sync0debug.cc */

enum latch_level_t {
	SYNC_UNKNOWN = 0,

	SYNC_MUTEX = 1,

	RW_LOCK_SX,
	RW_LOCK_X_WAIT,
	RW_LOCK_S,
	RW_LOCK_X,
	RW_LOCK_NOT_LOCKED,

	SYNC_MONITOR_MUTEX,

	SYNC_ANY_LATCH,

	SYNC_DOUBLEWRITE,

	SYNC_BUF_FLUSH_LIST,

	SYNC_BUF_BLOCK,
	SYNC_BUF_PAGE_HASH,

	SYNC_BUF_POOL,

	SYNC_POOL,
	SYNC_POOL_MANAGER,

	SYNC_SEARCH_SYS,

	SYNC_WORK_QUEUE,

	SYNC_FTS_TOKENIZE,
	SYNC_FTS_OPTIMIZE,
	SYNC_FTS_BG_THREADS,
	SYNC_FTS_CACHE_INIT,
	SYNC_RECV,
	SYNC_LOG_FLUSH_ORDER,
	SYNC_LOG,
	SYNC_LOG_WRITE,
	SYNC_PAGE_CLEANER,
	SYNC_PURGE_QUEUE,
	SYNC_TRX_SYS_HEADER,
	SYNC_REC_LOCK,
	SYNC_THREADS,
	SYNC_TRX,
	SYNC_TRX_SYS,
	SYNC_LOCK_SYS,
	SYNC_LOCK_WAIT_SYS,

	SYNC_INDEX_ONLINE_LOG,

	SYNC_IBUF_BITMAP,
	SYNC_IBUF_BITMAP_MUTEX,
	SYNC_IBUF_TREE_NODE,
	SYNC_IBUF_TREE_NODE_NEW,
	SYNC_IBUF_INDEX_TREE,

	SYNC_IBUF_MUTEX,

	SYNC_FSP_PAGE,
	SYNC_FSP,
	SYNC_EXTERN_STORAGE,
	SYNC_TRX_UNDO_PAGE,
	SYNC_RSEG_HEADER,
	SYNC_RSEG_HEADER_NEW,
	SYNC_NOREDO_RSEG,
	SYNC_REDO_RSEG,
	SYNC_TRX_UNDO,
	SYNC_PURGE_LATCH,
	SYNC_TREE_NODE,
	SYNC_TREE_NODE_FROM_HASH,
	SYNC_TREE_NODE_NEW,
	SYNC_INDEX_TREE,

	SYNC_IBUF_PESS_INSERT_MUTEX,
	SYNC_IBUF_HEADER,
	SYNC_DICT_HEADER,
	SYNC_STATS_AUTO_RECALC,
	SYNC_DICT_AUTOINC_MUTEX,
	SYNC_DICT,
	SYNC_FTS_CACHE,

	SYNC_DICT_OPERATION,

	SYNC_FILE_FORMAT_TAG,

	SYNC_TRX_I_S_LAST_READ,

	SYNC_TRX_I_S_RWLOCK,

	SYNC_RECV_WRITER,

	/** Level is varying. Only used with buffer pool page locks, which
	do not have a fixed level, but instead have their level set after
	the page is locked; see e.g.  ibuf_bitmap_get_map_page(). */

	SYNC_LEVEL_VARYING,

	/** This can be used to suppress order checking. */
	SYNC_NO_ORDER_CHECK,

	/** Maximum level value */
	SYNC_LEVEL_MAX = SYNC_NO_ORDER_CHECK
};
```
