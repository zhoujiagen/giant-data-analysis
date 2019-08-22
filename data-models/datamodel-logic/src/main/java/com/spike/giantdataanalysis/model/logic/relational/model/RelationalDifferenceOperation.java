package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 差操作.
 */
public class RelationalDifferenceOperation extends RelationalMultipleRelationOperation {

  public RelationalDifferenceOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalDifferenceOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalDifferenceOperation(List<RelationalRelation> relations) {
    super(relations);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.DIFFERENCE;
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, this.relations.get(0).attributes);
  }

}