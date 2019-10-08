package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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
    if (uid != null) {
      sb.append(uid.literal()).append(" :");
    }
    sb.append("REPEAT ");
    List<String> literals = Lists.newArrayList();
    for (ProcedureSqlStatement procedureSqlStatement : procedureSqlStatements) {
      literals.add(procedureSqlStatement.literal());
    }
    sb.append(Joiner.on(" ").join(literals)).append(" ");
    sb.append("UNTIL ").append(untilExpression.literal()).append(" ");
    if (endRepeatUid != null) {
      sb.append(" ").append(endRepeatUid.literal());
    }
    return sb.toString();
  }
}
