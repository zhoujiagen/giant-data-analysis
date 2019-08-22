package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 并操作.
 */
public class RelationalUnionOperation extends RelationalMultipleRelationOperation {

  public RelationalUnionOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalUnionOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalUnionOperation(List<RelationalRelation> relations) {
    super(relations);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.UNION;
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, this.relations.get(0).attributes);
  }

}