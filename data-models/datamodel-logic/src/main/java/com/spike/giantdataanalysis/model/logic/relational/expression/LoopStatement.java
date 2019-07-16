package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 loopStatement
    : (uid ':')?
      LOOP procedureSqlStatement+
      END LOOP uid?
    ;
 * </pre>
 */
public class LoopStatement implements CompoundStatement {
}
