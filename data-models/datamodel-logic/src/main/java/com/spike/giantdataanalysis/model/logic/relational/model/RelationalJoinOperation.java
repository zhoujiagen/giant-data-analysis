package com.spike.giantdataanalysis.model.logic.relational.model;

import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 连接操作.
 */
public abstract class RelationalJoinOperation extends RelationalBinaryOperation {

  public RelationalJoinOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}