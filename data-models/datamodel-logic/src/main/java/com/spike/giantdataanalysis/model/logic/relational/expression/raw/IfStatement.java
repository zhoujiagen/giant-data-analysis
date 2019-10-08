package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * <pre>
 ifStatement
    : IF expression
      THEN thenStatements+=procedureSqlStatement+
      elifAlternative*
      (ELSE elseStatements+=procedureSqlStatement+ )?
      END IF
    ;
 * </pre>
 */
public class IfStatement implements CompoundStatement {

  public final Expression ifExpression;
  public final List<ProcedureSqlStatement> thenStatements;
  public final List<ElifAlternative> elifAlternatives;
  public final List<ProcedureSqlStatement> elseStatements;

  IfStatement(Expression ifExpression, List<ProcedureSqlStatement> thenStatements,
      List<ElifAlternative> elifAlternatives, List<ProcedureSqlStatement> elseStatements) {
    Preconditions.checkArgument(ifExpression != null);
    Preconditions.checkArgument(thenStatements != null && thenStatements.size() > 0);

    this.ifExpression = ifExpression;
    this.thenStatements = thenStatements;
    this.elifAlternatives = elifAlternatives;
    this.elseStatements = elseStatements;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("IF ").append(ifExpression.literal()).append(" ");
    sb.append("THEN ");
    List<String> literals = Lists.newArrayList();
    for (ProcedureSqlStatement thenStatement : thenStatements) {
      literals.add(thenStatement.literal());
    }
    sb.append(Joiner.on(" ").join(literals)).append(" ");

    if (CollectionUtils.isNotEmpty(elifAlternatives)) {
      List<String> literals2 = Lists.newArrayList();
      for (ElifAlternative elifAlternative : elifAlternatives) {
        literals2.add(elifAlternative.literal());
      }
      sb.append(Joiner.on(" ").join(literals2)).append(" ");
    }

    if (CollectionUtils.isNotEmpty(elseStatements)) {
      sb.append("ELSE ");
      List<String> literals2 = Lists.newArrayList();
      for (ProcedureSqlStatement elseStatement : elseStatements) {
        literals2.add(elseStatement.literal());
      }
      sb.append(Joiner.on(" ").join(literals2)).append(" ");
    }

    sb.append("END IF");
    return sb.toString();
  }
}
