package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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
  public final Uid uid;
  public final List<ProcedureSqlStatement> procedureSqlStatements;
  public final Uid endLoopUid;

  LoopStatement(Uid uid, List<ProcedureSqlStatement> procedureSqlStatements, Uid endLoopUid) {
    Preconditions
        .checkArgument(procedureSqlStatements != null && procedureSqlStatements.size() > 0);

    this.uid = uid;
    this.procedureSqlStatements = procedureSqlStatements;
    this.endLoopUid = endLoopUid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    if (uid != null) {
      sb.append(uid.literal()).append(" :");
    }
    sb.append("LOOP ");
    List<String> literals = Lists.newArrayList();
    for (ProcedureSqlStatement procedureSqlStatement : procedureSqlStatements) {
      literals.add(procedureSqlStatement.literal());
    }
    sb.append(Joiner.on(" ").join(literals)).append(" ");
    sb.append("END LOOP");
    if (endLoopUid != null) {
      sb.append(" ").append(endLoopUid.literal());
    }
    return sb.toString();
  }
}
