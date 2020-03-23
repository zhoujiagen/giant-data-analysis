# InnoDB Memory Management

###  mem: The memory management

|header|description|
|:---|:---|
| mem0mem.h | The memory management |
| dyn0buf.h | The dynamically allocated buffer implementation |
| dyn0types.h | The dynamically allocated buffer types and constants|


``` CPP
/** A block of a memory heap consists of the info structure
followed by an area of memory */
typedef struct mem_block_info_t	mem_block_t;

/** A memory heap is a nonempty linear list of memory blocks */
typedef mem_block_t		mem_heap_t;

/** Types of allocation for memory heaps: DYNAMIC means allocation from the
dynamic memory pool of the C compiler, BUFFER means allocation from the
buffer pool; the latter method is used for very big heaps */

#define MEM_HEAP_DYNAMIC	0	/* the most common type */
#define MEM_HEAP_BUFFER		1
#define MEM_HEAP_BTR_SEARCH	2	/* this flag can optionally be
					ORed to MEM_HEAP_BUFFER, in which
					case heap->free_block is used in
					some cases for memory allocations,
					and if it's NULL, the memory
					allocation functions can return
					NULL. */
```
