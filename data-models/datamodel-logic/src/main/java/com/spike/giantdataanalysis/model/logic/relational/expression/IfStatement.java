package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

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

}
