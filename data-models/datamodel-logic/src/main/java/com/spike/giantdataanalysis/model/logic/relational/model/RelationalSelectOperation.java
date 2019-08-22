package com.spike.giantdataanalysis.model.logic.relational.model;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.REInterpreterScope;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 选择操作.
 */
public class RelationalSelectOperation implements RelationalOperation {
  public final RelationalRelation relation;

  public final Expression condition;
  // may be a higher scope than 'expression'
  public final REInterpreterScope interpreteScope;

  public RelationalSelectOperation(RelationalRelation relation, Expression condition,
      REInterpreterScope interpreteScope) {
    Preconditions.checkArgument(relation != null);

    this.relation = relation;
    this.condition = condition;
    this.interpreteScope = interpreteScope;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.SELECT;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[");
    if (condition != null) {
      sb.append(condition.literal());
    }
    sb.append("]");
    sb.append("(").append(relation.literal()).append(")");
    return sb.toString();
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, relation.attributes);
  }

}