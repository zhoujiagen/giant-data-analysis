package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 optimizeTable
    : OPTIMIZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      TABLE tables
    ;
 * </pre>
 */
public class OptimizeTable implements AdministrationStatement {
}
