package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 whileStatement
    : (uid ':')?
      WHILE expression
      DO procedureSqlStatement+
      END WHILE uid?
    ;
 * </pre>
 */
public class WhileStatement implements CompoundStatement {
}
