package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.FunctionNameBaseEnum;

/**
 * Functions
 */
public interface Functions extends PrimitiveExpression {
  /**
   * <pre>
   functionCall
    : specificFunction                                              #specificFunctionCall
    | aggregateWindowedFunction                                     #aggregateFunctionCall
    | scalarFunctionName '(' functionArgs? ')'                      #scalarFunctionCall
    | fullId '(' functionArgs? ')'                                  #udfFunctionCall
    | passwordFunctionClause                                        #passwordFunctionCall
    ;
   * </pre>
   */
  public static interface FunctionCall extends ExpressionAtom, Functions {
  }

  /**
   * <pre>
   specificFunction
    : (
      CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP
      | CURRENT_USER | LOCALTIME
      )                                                             #simpleFunctionCall
    | CONVERT '(' expression separator=',' convertedDataType ')'    #dataTypeFunctionCall
    | CONVERT '(' expression USING charsetName ')'                  #dataTypeFunctionCall
    | CAST '(' expression AS convertedDataType ')'                  #dataTypeFunctionCall
    | VALUES '(' fullColumnName ')'                                 #valuesFunctionCall
    | CASE expression caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CASE caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CHAR '(' functionArgs  (USING charsetName)? ')'               #charFunctionCall
    | POSITION
      '('
          (
            positionString=stringLiteral
            | positionExpression=expression
          )
          IN
          (
            inString=stringLiteral
            | inExpression=expression
          )
      ')'                                                           #positionFunctionCall
    | (SUBSTR | SUBSTRING)
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        ) FROM
        (
          fromDecimal=decimalLiteral
          | fromExpression=expression
        )
        (
          FOR
          (
            forDecimal=decimalLiteral
            | forExpression=expression
          )
        )?
      ')'                                                           #substrFunctionCall
    | TRIM
      '('
        positioinForm=(BOTH | LEADING | TRAILING)
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )?
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | TRIM
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | WEIGHT_STRING
      '('
        (stringLiteral | expression)
        (AS stringFormat=(CHAR | BINARY)
        '(' decimalLiteral ')' )?  levelsInWeightString?
      ')'                                                           #weightFunctionCall
    | EXTRACT
      '('
        intervalType
        FROM
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
      ')'                                                           #extractFunctionCall
    | GET_FORMAT
      '('
        datetimeFormat=(DATE | TIME | DATETIME)
        ',' stringLiteral
      ')'                                                           #getFormatFunctionCall
    ;
   * </pre>
   */
  public static interface SpecificFunction extends FunctionCall {
  }

  public static class SimpleFunctionCall implements SpecificFunction {
    public static enum Type implements RelationalAlgebraEnum {
      CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_USER, LOCALTIME
    }

    public final SimpleFunctionCall.Type type;

    SimpleFunctionCall(SimpleFunctionCall.Type type) {
      this.type = type;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SimpleFunctionCall [type=");
      builder.append(type);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class DataTypeFunctionCall implements SpecificFunction {
    public static enum Type implements RelationalAlgebraEnum {
      // CONVERT '(' expression separator=',' convertedDataType ')' #dataTypeFunctionCall
      CONVERT_DATATYPE,
      // CONVERT '(' expression USING charsetName ')' #dataTypeFunctionCall
      CONVERT_CHARSET,
      // CAST '(' expression AS convertedDataType ')' #dataTypeFunctionCall
      CAST
    }

    public final DataTypeFunctionCall.Type type;
    public final Expression expression;
    public final ConvertedDataType convertedDataType;
    public final CharsetName charsetName;

    DataTypeFunctionCall(DataTypeFunctionCall.Type type, Expression expression,
        CharsetName charsetName) {
      Preconditions.checkArgument(Type.CONVERT_CHARSET.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(charsetName != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = null;
      this.charsetName = charsetName;
    }

    DataTypeFunctionCall(DataTypeFunctionCall.Type type, Expression expression,
        ConvertedDataType convertedDataType) {
      Preconditions.checkArgument(Type.CONVERT_DATATYPE.equals(type) || Type.CAST.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(convertedDataType != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = convertedDataType;
      this.charsetName = null;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("DataTypeFunctionCall [type=");
      builder.append(type);
      builder.append(", expression=");
      builder.append(expression);
      builder.append(", convertedDataType=");
      builder.append(convertedDataType);
      builder.append(", charsetName=");
      builder.append(charsetName);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class ValuesFunctionCall implements SpecificFunction {
    public final FullColumnName fullColumnName;

    ValuesFunctionCall(FullColumnName fullColumnName) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ValuesFunctionCall [fullColumnName=");
      builder.append(fullColumnName);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class CaseFunctionCall implements SpecificFunction {
    public final Expression expression;
    public final List<CaseFuncAlternative> caseFuncAlternatives;
    public final FunctionArg functionArg;

    CaseFunctionCall(Expression expression, List<CaseFuncAlternative> caseFuncAlternatives,
        FunctionArg functionArg) {
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(caseFuncAlternatives != null && caseFuncAlternatives.size() > 0);

      this.expression = expression;
      this.caseFuncAlternatives = caseFuncAlternatives;
      this.functionArg = functionArg;
    }

    CaseFunctionCall(List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
      Preconditions.checkArgument(caseFuncAlternatives != null && caseFuncAlternatives.size() > 0);

      this.expression = null;
      this.caseFuncAlternatives = caseFuncAlternatives;
      this.functionArg = functionArg;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CaseFunctionCall [expression=");
      builder.append(expression);
      builder.append(", caseFuncAlternatives=");
      builder.append(caseFuncAlternatives);
      builder.append(", functionArg=");
      builder.append(functionArg);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class CharFunctionCall implements SpecificFunction {
    public final FunctionArgs functionArgs;
    public final CharsetName charsetName;

    CharFunctionCall(FunctionArgs functionArgs, CharsetName charsetName) {
      Preconditions.checkArgument(functionArgs != null);

      this.functionArgs = functionArgs;
      this.charsetName = charsetName;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CharFunctionCall [functionArgs=");
      builder.append(functionArgs);
      builder.append(", charsetName=");
      builder.append(charsetName);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class PositionFunctionCall implements SpecificFunction {
    public final String positionString;
    public final Expression positionExpression;
    public final String inString;
    public final Expression inExpression;

    PositionFunctionCall(String positionString, Expression positionExpression, String inString,
        Expression inExpression) {
      Preconditions.checkArgument(!(positionString == null && positionExpression == null));
      Preconditions.checkArgument(!(inString == null && inExpression == null));

      this.positionString = positionString;
      this.positionExpression = positionExpression;
      this.inString = inString;
      this.inExpression = inExpression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PositionFunctionCall [positionString=");
      builder.append(positionString);
      builder.append(", positionExpression=");
      builder.append(positionExpression);
      builder.append(", inString=");
      builder.append(inString);
      builder.append(", inExpression=");
      builder.append(inExpression);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class SubstrFunctionCall implements SpecificFunction {
    public final String sourceString;
    public final Expression sourceExpression;
    public final DecimalLiteral fromDecimal;
    public final Expression fromExpression;
    public final DecimalLiteral forDecimal;
    public final Expression forExpression;

    SubstrFunctionCall(//
        String sourceString, Expression sourceExpression, //
        DecimalLiteral fromDecimal, Expression fromExpression, //
        DecimalLiteral forDecimal, Expression forExpression//
    ) {
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromDecimal == null && fromExpression == null));
      Preconditions.checkArgument(!(forDecimal == null && forExpression == null));

      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromDecimal = fromDecimal;
      this.fromExpression = fromExpression;
      this.forDecimal = forDecimal;
      this.forExpression = forExpression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SubstrFunctionCall [sourceString=");
      builder.append(sourceString);
      builder.append(", sourceExpression=");
      builder.append(sourceExpression);
      builder.append(", fromDecimal=");
      builder.append(fromDecimal);
      builder.append(", fromExpression=");
      builder.append(fromExpression);
      builder.append(", forDecimal=");
      builder.append(forDecimal);
      builder.append(", forExpression=");
      builder.append(forExpression);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class TrimFunctionCall implements SpecificFunction {
    public static enum PositioinFormType implements RelationalAlgebraEnum {
      BOTH, LEADING, TRAILING
    }

    public final TrimFunctionCall.PositioinFormType type;
    public final StringLiteral sourceString;
    public final Expression sourceExpression;
    public final StringLiteral fromString;
    public final Expression fromExpression;

    TrimFunctionCall(TrimFunctionCall.PositioinFormType type, //
        StringLiteral sourceString, Expression sourceExpression, //
        StringLiteral fromString, Expression fromExpression//
    ) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromString == null && fromExpression == null));

      this.type = type;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromString = fromString;
      this.fromExpression = fromExpression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TrimFunctionCall [type=");
      builder.append(type);
      builder.append(", sourceString=");
      builder.append(sourceString);
      builder.append(", sourceExpression=");
      builder.append(sourceExpression);
      builder.append(", fromString=");
      builder.append(fromString);
      builder.append(", fromExpression=");
      builder.append(fromExpression);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class WeightFunctionCall implements SpecificFunction {
    public static enum StringFormatType implements RelationalAlgebraEnum {
      CHAR, BINARY
    };

    public final StringLiteral stringLiteral;
    public final Expression expression;
    public final WeightFunctionCall.StringFormatType type;
    public final DecimalLiteral decimalLiteral;
    public final LevelsInWeightString levelsInWeightString;

    WeightFunctionCall(StringLiteral stringLiteral, Expression expression,
        WeightFunctionCall.StringFormatType type, DecimalLiteral decimalLiteral,
        LevelsInWeightString levelsInWeightString) {
      this.stringLiteral = stringLiteral;
      this.expression = expression;
      this.type = type;
      this.decimalLiteral = decimalLiteral;
      this.levelsInWeightString = levelsInWeightString;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("WeightFunctionCall [stringLiteral=");
      builder.append(stringLiteral);
      builder.append(", expression=");
      builder.append(expression);
      builder.append(", type=");
      builder.append(type);
      builder.append(", decimalLiteral=");
      builder.append(decimalLiteral);
      builder.append(", levelsInWeightString=");
      builder.append(levelsInWeightString);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class ExtractFunctionCall implements SpecificFunction {
    public final IntervalType intervalType;
    public final StringLiteral sourceString;
    public final Expression sourceExpression;

    ExtractFunctionCall(IntervalType intervalType, StringLiteral sourceString,
        Expression sourceExpression) {
      Preconditions.checkArgument(intervalType != null);
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));

      this.intervalType = intervalType;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ExtractFunctionCall [intervalType=");
      builder.append(intervalType);
      builder.append(", sourceString=");
      builder.append(sourceString);
      builder.append(", sourceExpression=");
      builder.append(sourceExpression);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class GetFormatFunctionCall implements SpecificFunction {
    public static enum DatetimeFormatType implements RelationalAlgebraEnum {
      DATE, TIME, DATETIME
    }

    public final GetFormatFunctionCall.DatetimeFormatType type;
    public final StringLiteral stringLiteral;

    GetFormatFunctionCall(GetFormatFunctionCall.DatetimeFormatType type,
        StringLiteral stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("GetFormatFunctionCall [type=");
      builder.append(type);
      builder.append(", stringLiteral=");
      builder.append(stringLiteral);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  caseFuncAlternative
    : WHEN condition=functionArg
      THEN consequent=functionArg
    ;
   * </pre>
   */
  public static class CaseFuncAlternative implements PrimitiveExpression {
    public final FunctionArg condition;
    public final FunctionArg consequent;

    CaseFuncAlternative(FunctionArg condition, FunctionArg consequent) {
      Preconditions.checkArgument(condition != null);
      Preconditions.checkArgument(consequent != null);

      this.condition = condition;
      this.consequent = consequent;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CaseFuncAlternative [condition=");
      builder.append(condition);
      builder.append(", consequent=");
      builder.append(consequent);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class ScalarFunctionCall implements FunctionCall {
    public static enum Type implements RelationalAlgebraEnum {
      FUNCTION_NAME_BASE, //
      ASCII, CURDATE, CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURTIME, DATE_ADD, DATE_SUB,
      IF, INSERT, LOCALTIME, LOCALTIMESTAMP, MID, NOW, REPLACE, SUBSTR, SUBSTRING, SYSDATE, TRIM,
      UTC_DATE, UTC_TIME, UTC_TIMESTAMP
    }

    public final ScalarFunctionCall.Type type;
    public final FunctionNameBaseEnum functionNameBase;
    public final FunctionArgs functionArgs;

    ScalarFunctionCall(ScalarFunctionCall.Type type, FunctionNameBaseEnum functionNameBase,
        FunctionArgs functionArgs) {
      Preconditions.checkArgument(type != null);
      this.type = type;

      if (Type.FUNCTION_NAME_BASE.equals(type)) {
        Preconditions.checkArgument(functionNameBase != null);
        this.functionNameBase = functionNameBase;
      } else {
        this.functionNameBase = null;
      }
      this.functionArgs = functionArgs;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ScalarFunctionCall [type=");
      builder.append(type);
      builder.append(", functionNameBase=");
      builder.append(functionNameBase);
      builder.append(", functionArgs=");
      builder.append(functionArgs);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class UdfFunctionCall implements FunctionCall {
    public final FullId fullId;
    public final FunctionArgs functionArgs;

    UdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
      Preconditions.checkArgument(fullId != null);

      this.fullId = fullId;
      this.functionArgs = functionArgs;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UdfFunctionCall [fullId=");
      builder.append(fullId);
      builder.append(", functionArgs=");
      builder.append(functionArgs);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class PasswordFunctionCall implements FunctionCall {
    public static enum Type implements RelationalAlgebraEnum {
      PASSWORD, OLD_PASSWORD
    }

    public final PasswordFunctionCall.Type type;
    public final FunctionArg functionArg;

    PasswordFunctionCall(PasswordFunctionCall.Type type, FunctionArg functionArg) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(functionArg != null);

      this.type = type;
      this.functionArg = functionArg;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PasswordFunctionCall [type=");
      builder.append(type);
      builder.append(", functionArg=");
      builder.append(functionArg);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   levelsInWeightString
    : LEVEL levelInWeightListElement
      (',' levelInWeightListElement)*                               #levelWeightList
    | LEVEL
      firstLevel=decimalLiteral '-' lastLevel=decimalLiteral        #levelWeightRange
    ;
   * </pre>
   */
  public static interface LevelsInWeightString extends Functions {
  }

  // LEVEL levelInWeightListElement (',' levelInWeightListElement)* #levelWeightList
  public static class LevelWeightList implements LevelsInWeightString {
    public final List<LevelInWeightListElement> levelInWeightListElements;

    LevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
      Preconditions.checkArgument(levelInWeightListElements != null //
          && levelInWeightListElements.size() > 0);
      this.levelInWeightListElements = levelInWeightListElements;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelWeightList [levelInWeightListElements=");
      builder.append(levelInWeightListElements);
      builder.append("]");
      return builder.toString();
    }

  }

  // LEVEL firstLevel=decimalLiteral '-' lastLevel=decimalLiteral #levelWeightRange
  public static class LevelWeightRange implements LevelsInWeightString {
    public final DecimalLiteral firstLevel;
    public final DecimalLiteral lastLevel;

    LevelWeightRange(DecimalLiteral firstLevel, DecimalLiteral lastLevel) {
      Preconditions.checkArgument(firstLevel != null);
      Preconditions.checkArgument(lastLevel != null);

      this.firstLevel = firstLevel;
      this.lastLevel = lastLevel;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelWeightRange [firstLevel=");
      builder.append(firstLevel);
      builder.append(", lastLevel=");
      builder.append(lastLevel);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * <pre>
   levelInWeightListElement
    : decimalLiteral orderType=(ASC | DESC | REVERSE)?
    ;
   * </pre>
   */
  public static class LevelInWeightListElement implements Functions {
    public static enum OrderType implements RelationalAlgebraEnum {
      ASC, DESC, REVERSE
    }

    public final DecimalLiteral decimalLiteral;
    public final LevelInWeightListElement.OrderType orderType;

    LevelInWeightListElement(DecimalLiteral decimalLiteral,
        LevelInWeightListElement.OrderType orderType) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
      this.orderType = orderType;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelInWeightListElement [decimalLiteral=");
      builder.append(decimalLiteral);
      builder.append(", orderType=");
      builder.append(orderType);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  aggregateWindowedFunction
    : (AVG | MAX | MIN | SUM)
      '(' aggregator=(ALL | DISTINCT)? functionArg ')'
    | COUNT '(' (starArg='*' | aggregator=ALL? functionArg) ')'
    | COUNT '(' aggregator=DISTINCT functionArgs ')'
    | (
        BIT_AND | BIT_OR | BIT_XOR | STD | STDDEV | STDDEV_POP
        | STDDEV_SAMP | VAR_POP | VAR_SAMP | VARIANCE
      ) '(' aggregator=ALL? functionArg ')'
    | GROUP_CONCAT '('
        aggregator=DISTINCT? functionArgs
        (ORDER BY
          orderByExpression (',' orderByExpression)*
        )? (SEPARATOR separator=STRING_LITERAL)?
      ')'
    ;
   * </pre>
   */
  public static class AggregateWindowedFunction implements FunctionCall {
    public static enum Type implements RelationalAlgebraEnum {
      AVG, MAX, MIN, SUM, //
      COUNT, COUNT_DISTINCT, //
      BIT_AND, BIT_OR, BIT_XOR, STD, STDDEV, STDDEV_POP, STDDEV_SAMP, VAR_POP, VAR_SAMP, VARIANCE, //
      GROUP_CONCAT;
    }

    public static enum Aggregator implements RelationalAlgebraEnum {
      ALL, DISTINCT
    }

    public final AggregateWindowedFunction.Type type;
    public final AggregateWindowedFunction.Aggregator aggregator;
    public final FunctionArg functionArg;
    public final FunctionArgs functionArgs;
    public final List<OrderByExpression> orderByExpression;
    public final String separator;

    AggregateWindowedFunction(AggregateWindowedFunction.Type type,
        AggregateWindowedFunction.Aggregator aggregator, FunctionArg functionArg,
        FunctionArgs functionArgs, List<OrderByExpression> orderByExpression, String separator) {
      Preconditions.checkArgument(type != null);
      this.type = type;
      switch (type) {
      case AVG:
      case MAX:
      case MIN:
      case SUM:
        Preconditions.checkArgument(functionArg != null);
        break;
      case COUNT:
        Preconditions.checkArgument(functionArg != null);
        break;
      case COUNT_DISTINCT:
        Preconditions.checkArgument(functionArgs != null);
        break;
      case BIT_AND:
      case BIT_OR:
      case BIT_XOR:
      case STD:
      case STDDEV:
      case STDDEV_POP:
      case STDDEV_SAMP:
      case VAR_POP:
        Preconditions.checkArgument(functionArg != null);
        break;
      case GROUP_CONCAT:
        Preconditions.checkArgument(functionArgs != null);
      default:
        throw new UnsupportedOperationException();
      }

      this.aggregator = aggregator;
      this.functionArg = functionArg;
      this.functionArgs = functionArgs;
      this.orderByExpression = orderByExpression;
      this.separator = separator;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("AggregateWindowedFunction [type=");
      builder.append(type);
      builder.append(", aggregator=");
      builder.append(aggregator);
      builder.append(", functionArg=");
      builder.append(functionArg);
      builder.append(", functionArgs=");
      builder.append(functionArgs);
      builder.append(", orderByExpression=");
      builder.append(orderByExpression);
      builder.append(", separator=");
      builder.append(separator);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   passwordFunctionClause
    : functionName=(PASSWORD | OLD_PASSWORD) '(' functionArg ')'
    ;
   * </pre>
   */
  public static class PasswordFunctionClause implements Functions {
    public static enum Type implements RelationalAlgebraEnum {
      PASSWORD, OLD_PASSWORD
    }

    public final PasswordFunctionClause.Type functionName;
    public final FunctionArg functionArg;

    PasswordFunctionClause(Type functionName, FunctionArg functionArg) {
      Preconditions.checkArgument(functionName != null);
      Preconditions.checkArgument(functionArg != null);

      this.functionName = functionName;
      this.functionArg = functionArg;
    }

  }

  /**
   * <pre>
   functionArgs
    : (constant | fullColumnName | functionCall | expression)
    (
      ','
      (constant | fullColumnName | functionCall | expression)
    )*
    ;
   * </pre>
   */
  public static class FunctionArgs implements SpecificFunction, Functions {
    public final List<FunctionArg> functionArgs;

    FunctionArgs(List<FunctionArg> functionArgs) {
      Preconditions.checkArgument(functionArgs != null && functionArgs.size() > 0);

      this.functionArgs = functionArgs;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("FunctionArgs [functionArgs=");
      builder.append(functionArgs);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   functionArg
    : constant | fullColumnName | functionCall | expression
    ;
   * </pre>
   */
  public static class FunctionArg implements Functions {
    public static enum Type implements RelationalAlgebraEnum {
      CONSTANT, FULL_COLUMN_NAME, FUNCTION_CALL, EXPRESSION
    }

    public final FunctionArg.Type type;
    public Constant constant;
    public FullColumnName fullColumnName;
    public FunctionCall functionCall;
    public Expression expression;

    FunctionArg(FunctionArg.Type type, Object value) {
      Preconditions.checkArgument(type != null);

      this.type = type;

      switch (type) {
      case CONSTANT:
        constant = (Constant) value;
        break;
      case FULL_COLUMN_NAME:
        fullColumnName = (FullColumnName) value;
        break;
      case FUNCTION_CALL:
        functionCall = (FunctionCall) value;
        break;
      case EXPRESSION:
        expression = (Expression) value;
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("FunctionArg [type=");
      builder.append(type);
      builder.append(", constant=");
      builder.append(constant);
      builder.append(", fullColumnName=");
      builder.append(fullColumnName);
      builder.append(", functionCall=");
      builder.append(functionCall);
      builder.append(", expression=");
      builder.append(expression);
      builder.append("]");
      return builder.toString();
    }

  }
}
