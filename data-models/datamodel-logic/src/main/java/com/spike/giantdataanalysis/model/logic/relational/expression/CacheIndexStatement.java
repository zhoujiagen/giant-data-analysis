package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 cacheIndexStatement
    : CACHE INDEX tableIndexes (',' tableIndexes)*
      ( PARTITION '(' (uidList | ALL) ')' )?
      IN schema=uid
    ;
 * </pre>
 */
public class CacheIndexStatement implements AdministrationStatement {
}
