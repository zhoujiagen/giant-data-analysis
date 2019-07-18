package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
  public final Uid uid;
  public final List<ProcedureSqlStatement> procedureSqlStatements;
  public final Expression untilExpression;
  public final Uid endRepeatUid;

  RepeatStatement(Uid uid, List<ProcedureSqlStatement> procedureSqlStatements,
      Expression untilExpression, Uid endRepeatUid) {
    Preconditions
        .checkArgument(procedureSqlStatements != null && procedureSqlStatements.size() > 0);
    Preconditions.checkArgument(untilExpression != null);

    this.uid = uid;
    this.procedureSqlStatements = procedureSqlStatements;
    this.untilExpression = untilExpression;
    this.endRepeatUid = endRepeatUid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
