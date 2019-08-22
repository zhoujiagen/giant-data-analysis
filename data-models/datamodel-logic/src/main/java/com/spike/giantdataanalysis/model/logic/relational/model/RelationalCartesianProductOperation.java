package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 笛卡尔积操作.
 */
public class RelationalCartesianProductOperation extends RelationalMultipleRelationOperation {

  public RelationalCartesianProductOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalCartesianProductOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalCartesianProductOperation(List<RelationalRelation> relations) {
    super(relations);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.CARTESIAN_PRODUCT;
  }

  @Override
  public RelationalRelation result(String alias) {
    List<RelationalAttribute> attributes = Lists.newArrayList();
    for (RelationalRelation relation : relations) {
      for (RelationalAttribute attribute : relation.attributes) {
        attributes.add(attribute.copy(relation.name + "." + attribute.name));
      }
    }
    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}