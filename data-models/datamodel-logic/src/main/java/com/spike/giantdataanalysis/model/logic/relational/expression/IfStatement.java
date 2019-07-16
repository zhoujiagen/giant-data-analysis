package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 ifStatement
    : IF expression
      THEN thenStatements+=procedureSqlStatement+
      elifAlternative*
      (ELSE elseStatements+=procedureSqlStatement+ )?
      END IF
    ;
 * </pre>
 */
public class IfStatement implements CompoundStatement {
}
