package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
  public final Uid beginUid;
  public final List<DeclareVariable> declareVariables;
  public final List<DeclareCondition> declareConditions;
  public final List<DeclareCursor> declareCursors;
  public final List<DeclareHandler> declareHandlers;
  public final List<ProcedureSqlStatement> procedureSqlStatements;
  public final Uid endUid;

  BlockStatement(Uid beginUid, List<DeclareVariable> declareVariables,
      List<DeclareCondition> declareConditions, List<DeclareCursor> declareCursors,
      List<DeclareHandler> declareHandlers, List<ProcedureSqlStatement> procedureSqlStatements,
      Uid endUid) {
    this.beginUid = beginUid;
    this.declareVariables = declareVariables;
    this.declareConditions = declareConditions;
    this.declareCursors = declareCursors;
    this.declareHandlers = declareHandlers;
    this.procedureSqlStatements = procedureSqlStatements;
    this.endUid = endUid;
  }

}
