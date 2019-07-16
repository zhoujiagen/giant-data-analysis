package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 repeatStatement
    : (uid ':')?
      REPEAT procedureSqlStatement+
      UNTIL expression
      END REPEAT uid?
    ;
 * </pre>
 */
public class RepeatStatement implements CompoundStatement {
}
