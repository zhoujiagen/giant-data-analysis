package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 分组操作.
 */
public class RelationalGroupingOperation implements RelationalOperation {

  public static class AttributeAggregation implements Literal {
    public final RelationalAttribute attribute;
    /** 为空时表示使用常规属性. */
    public final RelationalAggregateOperatorEnum aggregateOperator;
    public String attributeAlias; // 聚合属性别名

    public AttributeAggregation(RelationalAttribute attribute,
        RelationalAggregateOperatorEnum aggregateOperator, String attributeAlias) {
      Preconditions.checkArgument(attribute != null);

      this.attribute = attribute;
      this.aggregateOperator = aggregateOperator;
      this.attributeAlias = attributeAlias;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (aggregateOperator != null) {
        sb.append(aggregateOperator.name()).append("(");
      }
      sb.append(attribute.name);
      if (aggregateOperator != null) {
        sb.append(")");
      }
      return sb.toString();
    }
  }

  public final RelationalRelation relation;
  public final List<AttributeAggregation> attributeAggregations;

  public RelationalGroupingOperation(RelationalRelation relation,
      List<AttributeAggregation> attributeAggregations) {
    Preconditions.checkArgument(relation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributeAggregations));

    for (AttributeAggregation attributeAggregation : attributeAggregations) {
      Preconditions.checkArgument(
        RelationalUtils.contains(relation.attributes, attributeAggregation.attribute));
    }

    this.relation = relation;
    this.attributeAggregations = attributeAggregations;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.GROUP;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    List<String> attributeAggregationsStrings = Lists.newArrayList();
    for (AttributeAggregation attributeAggregation : attributeAggregations) {
      attributeAggregationsStrings.add(attributeAggregation.literal());
    }
    sb.append("[").append(Joiner.on(", ").join(attributeAggregationsStrings)).append("]");
    sb.append("(").append(relation.literal()).append(")");
    return sb.toString();
  }

  @Override
  public RelationalRelation result(String alias) {
    List<RelationalAttribute> attributes = Lists.newArrayList();
    for (AttributeAggregation attributeAggregation : attributeAggregations) {
      if (attributeAggregation.attributeAlias != null) {
        attributes.add(attributeAggregation.attribute.copy(attributeAggregation.attributeAlias));
      } else {
        attributes
            .add(attributeAggregation.attribute.copy(attributeAggregation.aggregateOperator.name()
                + "(" + attributeAggregation.attribute.name + ")"));
      }
    }
    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}
