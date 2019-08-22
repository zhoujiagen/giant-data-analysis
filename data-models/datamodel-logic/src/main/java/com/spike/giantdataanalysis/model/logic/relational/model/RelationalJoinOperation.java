package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 连接操作.
 */
public abstract class RelationalJoinOperation extends RelationalMultipleRelationOperation {

  public RelationalJoinOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalJoinOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalJoinOperation(List<RelationalRelation> relations) {
    super(relations);
  }

}