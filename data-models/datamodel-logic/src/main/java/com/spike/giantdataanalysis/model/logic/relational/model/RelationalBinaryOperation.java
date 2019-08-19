package com.spike.giantdataanalysis.model.logic.relational.model;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 关系二元操作.
 */
public abstract class RelationalBinaryOperation implements RelationalOperation {
  public final RelationalRelation first;
  public final RelationalRelation second;

  public RelationalBinaryOperation(RelationalRelation first, RelationalRelation second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    this.first = first;
    this.second = second;
  }
}