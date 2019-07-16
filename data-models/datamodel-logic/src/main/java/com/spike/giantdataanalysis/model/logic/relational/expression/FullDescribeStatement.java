package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 fullDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC)
      (
        formatType=(EXTENDED | PARTITIONS | FORMAT )
        '='
        formatValue=(TRADITIONAL | JSON)
      )?
      describeObjectClause
    ;
 * </pre>
 */
public class FullDescribeStatement implements UtilityStatement {
}
