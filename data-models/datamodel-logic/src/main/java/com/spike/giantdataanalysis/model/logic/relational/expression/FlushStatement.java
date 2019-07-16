package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 flushStatement
    : FLUSH flushFormat=(NO_WRITE_TO_BINLOG | LOCAL)?
      flushOption (',' flushOption)*
    ;
 * </pre>
 */
public class FlushStatement implements AdministrationStatement {
}
