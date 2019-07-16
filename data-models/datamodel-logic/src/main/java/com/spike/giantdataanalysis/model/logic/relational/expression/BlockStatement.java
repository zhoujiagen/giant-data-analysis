package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 blockStatement
    : (uid ':')? BEGIN
      (
        (declareVariable SEMI)*
        (declareCondition SEMI)*
        (declareCursor SEMI)*
        (declareHandler SEMI)*
        procedureSqlStatement*
      )?
      END uid?
    ;
 * </pre>
 */
public class BlockStatement implements CompoundStatement {
}
