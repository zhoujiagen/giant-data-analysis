package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CASE ");
    if (uid != null || expression != null) {
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      if (expression != null) {
        sb.append(expression.literal()).append(" ");
      }
    }
    List<String> literals = Lists.newArrayList();
    for (CaseAlternative caseAlternative : caseAlternatives) {
      literals.add(caseAlternative.literal());
    }
    sb.append(Joiner.on(" ").join(literals)).append(" ");
    if (CollectionUtils.isNotEmpty(procedureSqlStatements)) {
      sb.append("ELSE ");
      List<String> literals2 = Lists.newArrayList();
      for (ProcedureSqlStatement procedureSqlStatement : procedureSqlStatements) {
        literals2.add(procedureSqlStatement.literal());
      }
      sb.append(Joiner.on(" ").join(literals2)).append(" ");
    }
    sb.append("END CASE");
    return sb.toString();
  }
}
