package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * 关系代数表达式工厂.
 */
public abstract class RelationalAlgebraExpressionFactory {

  // ---------------------------------------------------------------------------
  // Methods
  // ---------------------------------------------------------------------------

  public static RelationalAlgebraBasicExpression
      makeBasicExpression(final RelationalTuples tuples) {
    Preconditions.checkArgument(tuples != null);

    return new RelationalAlgebraBasicExpression(tuples);
  }

  public static RelationalAlgebraIntersectionExpression
      makeIntersection(RelationalAlgebraExpression first, RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraIntersectionExpression(first, second);
  }

  public static RelationalAlgebraUnionExpression makeUnion(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraUnionExpression(first, second);
  }

  public static RelationalAlgebraDifferenceExpression
      makeDifference(RelationalAlgebraExpression first, RelationalAlgebraExpression second) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(second != null);

    return new RelationalAlgebraDifferenceExpression(first, second);
  }

  public static RelationalAlgebraProjectExpression makeProject(RelationalAlgebraExpression first,
      List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(first != null);
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());

    return new RelationalAlgebraProjectExpression(first, attributes);
  }

  // ---------------------------------------------------------------------------
  // Classes
  // ---------------------------------------------------------------------------

  /** 选择表达式. */
  public static class RelationalAlgebraSelectExpression implements RelationalAlgebraExpression {
    final RelationalAlgebraExpression first;
    final List<RelationalAttribute> attributes;

    RelationalAlgebraSelectExpression(RelationalAlgebraExpression first,
        List<RelationalAttribute> attributes) {
      this.first = first;
      this.attributes = attributes;
    }
  }

  /** 笛卡尔积表达式. */
  public static class RelationalAlgebraCartesianProductExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraCartesianProductExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 更名表达式. */
  public static class RelationalAlgebraRenameExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraRenameExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 连接表达式. */
  public static class RelationalAlgebraJoinExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraJoinExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 聚集表达式. */
  public static class RelationalAlgebraAggregateExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraAggregateExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

}
