package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

/**
 * <pre>
compoundStatement
    : blockStatement
    | caseStatement | ifStatement | leaveStatement
    | loopStatement | repeatStatement | whileStatement
    | iterateStatement | returnStatement | cursorStatement
    ;
 * </pre>
 */
public interface CompoundStatement extends RelationalAlgebraExpression {

  /**
   * <pre>
   routineBody
    : blockStatement | sqlStatement
    ;
   * </pre>
   */
  public static class RoutineBody implements PrimitiveExpression {
    public final BlockStatement blockStatement;
    public final SqlStatement sqlStatement;

    RoutineBody(BlockStatement blockStatement, SqlStatement sqlStatement) {
      Preconditions.checkArgument(!(blockStatement == null && sqlStatement == null));

      this.blockStatement = blockStatement;
      this.sqlStatement = sqlStatement;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  declareVariable
    : DECLARE uidList dataType (DEFAULT defaultValue)?
    ;
   * </pre>
   */
  public static class DeclareVariable implements PrimitiveExpression {
    public final UidList uidList;
    public final DataType dataType;
    public final DefaultValue defaultValue;

    DeclareVariable(UidList uidList, DataType dataType, DefaultValue defaultValue) {
      Preconditions.checkArgument(uidList != null);
      Preconditions.checkArgument(dataType != null);

      this.uidList = uidList;
      this.dataType = dataType;
      this.defaultValue = defaultValue;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  declareCondition
    : DECLARE uid CONDITION FOR
      ( decimalLiteral | SQLSTATE VALUE? STRING_LITERAL)
    ;
   * </pre>
   */
  public static class DeclareCondition implements PrimitiveExpression {
    public final Uid uid;
    public final DecimalLiteral decimalLiteral;
    public final String sqlState;

    DeclareCondition(Uid uid, DecimalLiteral decimalLiteral, String sqlState) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(!(decimalLiteral == null && sqlState == null));

      this.uid = uid;
      this.decimalLiteral = decimalLiteral;
      this.sqlState = sqlState;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  declareCursor
    : DECLARE uid CURSOR FOR selectStatement
    ;
   * </pre>
   */
  public static class DeclareCursor implements PrimitiveExpression {
    public final Uid uid;
    public final SelectStatement selectStatement;

    DeclareCursor(Uid uid, SelectStatement selectStatement) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(selectStatement != null);

      this.uid = uid;
      this.selectStatement = selectStatement;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  declareHandler
    : DECLARE handlerAction=(CONTINUE | EXIT | UNDO)
      HANDLER FOR
      handlerConditionValue (',' handlerConditionValue)*
      routineBody
    ;
   * </pre>
   */
  public static class DeclareHandler implements PrimitiveExpression {
    public static enum HandlerActionEnum implements RelationalAlgebraEnum {
      CONTINUE, EXIT, UNDO
    }

    public final HandlerActionEnum handlerAction;
    public final List<HandlerConditionValue> handlerConditionValues;
    public final RoutineBody routineBody;

    DeclareHandler(HandlerActionEnum handlerAction,
        List<HandlerConditionValue> handlerConditionValues, RoutineBody routineBody) {
      Preconditions.checkArgument(handlerAction != null);
      Preconditions
          .checkArgument(handlerConditionValues != null && handlerConditionValues.size() > 0);
      Preconditions.checkArgument(routineBody != null);

      this.handlerAction = handlerAction;
      this.handlerConditionValues = handlerConditionValues;
      this.routineBody = routineBody;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  handlerConditionValue
    : decimalLiteral                                                #handlerConditionCode
    | SQLSTATE VALUE? STRING_LITERAL                                #handlerConditionState
    | uid                                                           #handlerConditionName
    | SQLWARNING                                                    #handlerConditionWarning
    | NOT FOUND                                                     #handlerConditionNotfound
    | SQLEXCEPTION                                                  #handlerConditionException
    ;
   * </pre>
   */
  public static interface HandlerConditionValue extends PrimitiveExpression {
  }

  public static class HandlerConditionCode implements HandlerConditionValue {
    public final DecimalLiteral decimalLiteral;

    HandlerConditionCode(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }

  public static class HandlerConditionState implements HandlerConditionValue {
    public final String stringLiteral;

    HandlerConditionState(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class HandlerConditionName implements HandlerConditionValue {
    public final Uid uid;

    HandlerConditionName(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class HandlerConditionWarning implements HandlerConditionValue {

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }

  public static class HandlerConditionNotfound implements HandlerConditionValue {

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }

  public static class HandlerConditionException implements HandlerConditionValue {

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }

  /**
   * <pre>
  procedureSqlStatement
    : (compoundStatement | sqlStatement) SEMI
    ;
   * </pre>
   */
  public static class ProcedureSqlStatement implements PrimitiveExpression {
    public final CompoundStatement compoundStatement;
    public final SqlStatement sqlStatement;

    ProcedureSqlStatement(CompoundStatement compoundStatement, SqlStatement sqlStatement) {
      Preconditions.checkArgument(!(compoundStatement == null && sqlStatement == null));

      this.compoundStatement = compoundStatement;
      this.sqlStatement = sqlStatement;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  caseAlternative
    : WHEN (constant | expression)
      THEN procedureSqlStatement+
    ;
   * </pre>
   */
  public static class CaseAlternative implements PrimitiveExpression {
    public final Constant whenConstant;
    public final Expression whenExpression;
    public final List<ProcedureSqlStatement> procedureSqlStatements;

    CaseAlternative(Constant whenConstant, Expression whenExpression,
        List<ProcedureSqlStatement> procedureSqlStatements) {
      Preconditions.checkArgument(!(whenConstant == null && whenExpression == null));
      Preconditions
          .checkArgument(procedureSqlStatements != null && procedureSqlStatements.size() > 0);

      this.whenConstant = whenConstant;
      this.whenExpression = whenExpression;
      this.procedureSqlStatements = procedureSqlStatements;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  elifAlternative
    : ELSEIF expression
      THEN procedureSqlStatement+
    ;
   * </pre>
   */
  public static class ElifAlternative implements PrimitiveExpression {
    public final Expression elseIfExpression;
    public final List<ProcedureSqlStatement> procedureSqlStatements;

    ElifAlternative(Expression elseIfExpression,
        List<ProcedureSqlStatement> procedureSqlStatements) {
      Preconditions.checkArgument(elseIfExpression != null);
      Preconditions
          .checkArgument(procedureSqlStatements != null && procedureSqlStatements.size() > 0);

      this.elseIfExpression = elseIfExpression;
      this.procedureSqlStatements = procedureSqlStatements;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }
}
