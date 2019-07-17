package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

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
      FIRST, NEXT, PREV, LAST
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
        MoveOrderEnum moveOrder, Expression where, DecimalLiteral limit) {
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
      FIRST, NEXT
    }

    public final TableName tableName;
    public final MoveOrderEnum moveOrder;
    public final Expression where;
    public final DecimalLiteral limit;

    HandlerReadStatement(TableName tableName, MoveOrderEnum moveOrder, Expression where,
        DecimalLiteral limit) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(moveOrder != null);

      this.tableName = tableName;
      this.moveOrder = moveOrder;
      this.where = where;
      this.limit = limit;
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

  }
}