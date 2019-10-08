package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 blockStatement
    : (uid ':')? BEGIN
      (
        (declareVariable SEMI)*
        (declareCondition SEMI)*
        (declareCursor SEMI)*
        (declareHandler SEMI)*
        procedureSqlStatement*
      )?
      END uid?
    ;
 * </pre>
 */
public class BlockStatement implements CompoundStatement {
  public final Uid beginUid;
  public final List<DeclareVariable> declareVariables;
  public final List<DeclareCondition> declareConditions;
  public final List<DeclareCursor> declareCursors;
  public final List<DeclareHandler> declareHandlers;
  public final List<ProcedureSqlStatement> procedureSqlStatements;
  public final Uid endUid;

  BlockStatement(Uid beginUid, List<DeclareVariable> declareVariables,
      List<DeclareCondition> declareConditions, List<DeclareCursor> declareCursors,
      List<DeclareHandler> declareHandlers, List<ProcedureSqlStatement> procedureSqlStatements,
      Uid endUid) {
    this.beginUid = beginUid;
    this.declareVariables = declareVariables;
    this.declareConditions = declareConditions;
    this.declareCursors = declareCursors;
    this.declareHandlers = declareHandlers;
    this.procedureSqlStatements = procedureSqlStatements;
    this.endUid = endUid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    if (beginUid != null) {
      sb.append(beginUid.literal()).append(" : ");
    }
    sb.append("BEGIN ");
    if (CollectionUtils.isNotEmpty(declareVariables)) {
      List<String> literals = Lists.newArrayList();
      for (DeclareVariable declareVariable : declareVariables) {
        literals.add(declareVariable.literal() + ";");
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    if (CollectionUtils.isNotEmpty(declareConditions)) {
      List<String> literals = Lists.newArrayList();
      for (DeclareCondition declareCondition : declareConditions) {
        literals.add(declareCondition.literal() + ";");
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    if (CollectionUtils.isNotEmpty(declareCursors)) {
      List<String> literals = Lists.newArrayList();
      for (DeclareCursor declareCursor : declareCursors) {
        literals.add(declareCursor.literal() + ";");
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    if (CollectionUtils.isNotEmpty(declareHandlers)) {
      List<String> literals = Lists.newArrayList();
      for (DeclareHandler declareHandler : declareHandlers) {
        literals.add(declareHandler.literal() + ";");
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    if (CollectionUtils.isNotEmpty(procedureSqlStatements)) {
      List<String> literals = Lists.newArrayList();
      for (ProcedureSqlStatement procedureSqlStatement : procedureSqlStatements) {
        literals.add(procedureSqlStatement.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }

    sb.append("END ");
    if (endUid != null) {
      sb.append(endUid.literal()).append(" : ");
    }
    return sb.toString();
  }
}
