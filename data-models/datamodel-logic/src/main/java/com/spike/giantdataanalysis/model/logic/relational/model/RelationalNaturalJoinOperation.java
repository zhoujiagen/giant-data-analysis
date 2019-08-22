package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 自然连接操作.
 */
public class RelationalNaturalJoinOperation extends RelationalJoinOperation {

  public RelationalNaturalJoinOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  public RelationalNaturalJoinOperation(RelationalRelation... relations) {
    super(relations);
  }

  public RelationalNaturalJoinOperation(List<RelationalRelation> relations) {
    super(relations);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.NATURAL_JOIN;
  }

  @Override
  public RelationalRelation result(String alias) {
    // example see P.46
    int relationSize = relations.size();
    List<RelationalAttribute> attributes = relations.get(0).attributes;
    for (int i = 1; i < relationSize; i++) {
      attributes = RelationalUtils.getNaturalJoin(attributes, relations.get(i).attributes);
    }

    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}