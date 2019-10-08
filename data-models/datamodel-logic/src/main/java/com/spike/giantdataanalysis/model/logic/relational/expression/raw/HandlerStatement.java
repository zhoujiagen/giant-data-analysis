package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
handlerStatement
  : handlerOpenStatement
  | handlerReadIndexStatement
  | handlerReadStatement
  | handlerCloseStatement
  ;
 * </pre>
 */
public interface HandlerStatement extends DmlStatement {

  /**
   * <pre>
   handlerOpenStatement
    : HANDLER tableName OPEN (AS? uid)?
    ;
   * </pre>
   */
  public static class HandlerOpenStatement implements HandlerStatement {
    public final TableName tableName;
    public final Uid uid;

    HandlerOpenStatement(TableName tableName, Uid uid) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("HANDLER ").append(tableName.literal()).append(" OPEN");
      if (uid != null) {
        sb.append(" AS ").append(uid.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   handlerReadIndexStatement
    : HANDLER tableName READ index=uid
      (
        comparisonOperator '(' constants ')'
        | moveOrder=(FIRST | NEXT | PREV | LAST)
      )
      (WHERE expression)? (LIMIT decimalLiteral)?
    ;
   * </pre>
   */
  public static class HandlerReadIndexStatement implements HandlerStatement {
    public static enum MoveOrderEnum implements RelationalAlgebraEnum {
      FIRST, NEXT, PREV, LAST;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableName tableName;
    public final Uid index;
    public final RelationalComparisonOperatorEnum comparisonOperator;
    public final Constants constants;
    public final MoveOrderEnum moveOrder;
    public final Expression where;
    public final DecimalLiteral limit;

    HandlerReadIndexStatement(TableName tableName, Uid index,
        RelationalComparisonOperatorEnum comparisonOperator, Constants constants,
        HandlerReadIndexStatement.MoveOrderEnum moveOrder, Expression where, DecimalLiteral limit) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(index != null);
      Preconditions.checkArgument(!(comparisonOperator == null && moveOrder == null));

      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.index = index;
      this.comparisonOperator = comparisonOperator;
      this.constants = constants;
      this.moveOrder = moveOrder;
      this.where = where;
      this.limit = limit;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("HANDLER ").append(tableName.literal());
      sb.append(" READ ").append(index.literal()).append(" ");
      if (comparisonOperator != null) {
        sb.append(comparisonOperator.symbol).append("(").append(constants.literal()).append(") ");
      } else {
        sb.append(moveOrder.literal()).append(" ");
      }
      if (where != null) {
        sb.append("WHERE ").append(where.literal()).append(" ");
      }
      if (limit != null) {
        sb.append("LIMIT ").append(limit.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   handlerReadStatement
    : HANDLER tableName READ moveOrder=(FIRST | NEXT)
      (WHERE expression)? (LIMIT decimalLiteral)?
    ;
   * </pre>
   */
  public static class HandlerReadStatement implements HandlerStatement {
    public static enum MoveOrderEnum implements RelationalAlgebraEnum {
      FIRST, NEXT;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TableName tableName;
    public final MoveOrderEnum moveOrder;
    public final Expression where;
    public final DecimalLiteral limit;

    HandlerReadStatement(TableName tableName, HandlerReadStatement.MoveOrderEnum moveOrder,
        Expression where, DecimalLiteral limit) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(moveOrder != null);

      this.tableName = tableName;
      this.moveOrder = moveOrder;
      this.where = where;
      this.limit = limit;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("HANDLER ").append(tableName.literal());
      sb.append(" READ ").append(moveOrder.literal()).append(" ");
      if (where != null) {
        sb.append("WHERE ").append(where.literal()).append(" ");
      }
      if (limit != null) {
        sb.append("LIMIT ").append(limit.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   handlerCloseStatement
    : HANDLER tableName CLOSE
    ;
   * </pre>
   */
  public static class HandlerCloseStatement implements HandlerStatement {
    public final TableName tableName;

    HandlerCloseStatement(TableName tableName) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("HANDLER ").append(tableName.literal()).append(" CLOSE");
      return sb.toString();
    }

  }
}