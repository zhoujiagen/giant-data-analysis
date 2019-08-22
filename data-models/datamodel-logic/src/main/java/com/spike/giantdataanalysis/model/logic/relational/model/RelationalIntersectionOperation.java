package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 交操作.
 */
public class RelationalIntersectionOperation extends RelationalMultipleRelationOperation {

  public RelationalIntersectionOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalIntersectionOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalIntersectionOperation(List<RelationalRelation> relations) {
    super(relations);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.INTERSECTION;
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, this.relations.get(0).attributes);
  }

}