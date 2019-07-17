package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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

}
