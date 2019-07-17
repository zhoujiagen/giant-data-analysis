package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
  public final Uid uid;
  public final Expression expression;
  public final List<CaseAlternative> caseAlternatives;
  public final List<ProcedureSqlStatement> procedureSqlStatements;

  CaseStatement(Uid uid, Expression expression, List<CaseAlternative> caseAlternatives,
      List<ProcedureSqlStatement> procedureSqlStatements) {
    Preconditions.checkArgument(caseAlternatives != null && caseAlternatives.size() > 0);

    this.uid = uid;
    this.expression = expression;
    this.caseAlternatives = caseAlternatives;
    this.procedureSqlStatements = procedureSqlStatements;
  }

}
