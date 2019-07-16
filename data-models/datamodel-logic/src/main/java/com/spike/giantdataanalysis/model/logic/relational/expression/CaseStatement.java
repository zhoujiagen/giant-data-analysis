package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 caseStatement
    : CASE (uid | expression)? caseAlternative+
      (ELSE procedureSqlStatement+)?
      END CASE
    ;
 * </pre>
 */
public class CaseStatement implements CompoundStatement {
}
