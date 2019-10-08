package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    if (uid != null) {
      sb.append(uid.literal()).append(" :");
    }
    sb.append("WHILE ").append(whileExpression.literal()).append(" ");
    List<String> literals = Lists.newArrayList();
    for (ProcedureSqlStatement procedureSqlStatement : procedureSqlStatements) {
      literals.add(procedureSqlStatement.literal());
    }
    sb.append(Joiner.on(" ").join(literals)).append(" ");
    sb.append("END WHILE ");
    if (endWhileUid != null) {
      sb.append(endWhileUid.literal());
    }
    return sb.toString();
  }
}
