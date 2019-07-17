package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
  public final Uid uid;
  public final Expression whileExpression;
  public final List<ProcedureSqlStatement> procedureSqlStatements;
  public final Uid endWhileUid;

  WhileStatement(Uid uid, Expression whileExpression,
      List<ProcedureSqlStatement> procedureSqlStatements, Uid endWhileUid) {
    Preconditions.checkArgument(whileExpression != null);
    Preconditions
        .checkArgument(procedureSqlStatements != null && procedureSqlStatements.size() > 0);

    this.uid = uid;
    this.whileExpression = whileExpression;
    this.procedureSqlStatements = procedureSqlStatements;
    this.endWhileUid = endWhileUid;
  }

}
