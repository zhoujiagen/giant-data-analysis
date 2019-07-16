package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 simpleDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC) tableName
      (column=uid | pattern=STRING_LITERAL)?
    ;
 * </pre>
 */
public class SimpleDescribeStatement implements UtilityStatement {
}
