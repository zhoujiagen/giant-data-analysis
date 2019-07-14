package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.RelationalAlgebraPredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.FunctionNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.PrimitiveExpression.Uid;

/**
 * 原子表达式:
 * 
 * <pre>
expressionAtom
    : constant                                                      #constantExpressionAtom
    | fullColumnName                                                #fullColumnNameExpressionAtom
    | functionCall                                                  #functionCallExpressionAtom
    | expressionAtom COLLATE collationName                          #collateExpressionAtom
    | mysqlVariable                                                 #mysqlVariableExpressionAtom
    | unaryOperator expressionAtom                                  #unaryExpressionAtom
    | BINARY expressionAtom                                         #binaryExpressionAtom
    | '(' expression (',' expression)* ')'                          #nestedExpressionAtom
    | ROW '(' expression (',' expression)+ ')'                      #nestedRowExpressionAtom
    | EXISTS '(' selectStatement ')'                                #existsExpessionAtom
    | '(' selectStatement ')'                                       #subqueryExpessionAtom
    | INTERVAL expression intervalType                              #intervalExpressionAtom
    | left=expressionAtom bitOperator right=expressionAtom          #bitExpressionAtom
    | left=expressionAtom mathOperator right=expressionAtom         #mathExpressionAtom
    ;
 * </pre>
 */
public interface ExpressionAtom extends RelationalAlgebraPredicateExpression {

  /**
   * <pre>
   constant
    : stringLiteral | decimalLiteral
    | '-' decimalLiteral
    | hexadecimalLiteral | booleanLiteral
    | REAL_LITERAL | BIT_STRING
    | NOT? nullLiteral=(NULL_LITERAL | NULL_SPEC_LITERAL)
    ;
   * </pre>
   */
  public static class Constant implements ExpressionAtom {

    public static enum Type {
      STRING_LITERAL, //
      DECIMAL_LITERAL, //
      HEXADECIMAL_LITERAL, //
      BOOLEAN_LITERAL, //
      REAL_LITERAL, //
      BIT_STRING, //
      NULL_LITERAL;
    }

    public final Constant.Type type;
    public final String literal;

    public final Boolean not; // maybe null

    Constant(Constant.Type type, String literal, Boolean not) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
      this.not = not;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();

      if (Boolean.TRUE.equals(not)) {
        builder.append("NOT ");
      }
      builder.append(literal);
      return builder.toString();
    }

  }

  // fullColumnName: uid (dottedId dottedId? )?
  public static class FullColumnName implements ExpressionAtom {
    public final Uid uid;
    public final List<DottedId> dottedIds;

    FullColumnName(Uid uid, List<DottedId> dottedIds) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
      this.dottedIds = dottedIds;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(uid);
      if (CollectionUtils.isNotEmpty(dottedIds)) {
        builder.append(Joiner.on(" ").join(dottedIds).toString());
      }
      return builder.toString();
    }

  }

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
  public static interface FunctionCall extends ExpressionAtom {
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

  /// ( CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP | CURRENT_USER | LOCALTIME)
  /// #simpleFunctionCall
  public static class SimpleFunctionCall implements SpecificFunction {
    public static enum Type {
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

  /// CONVERT '(' expression separator=',' convertedDataType ')' #dataTypeFunctionCall
  public static class DataTypeFunctionCall implements SpecificFunction {
    public static enum Type {
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

  /// VALUES '(' fullColumnName ')' #valuesFunctionCall
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

  /// caseFunctionCall:
  // CASE expression caseFuncAlternative+ (ELSE elseArg=functionArg)? END
  // CASE caseFuncAlternative+ (ELSE elseArg=functionArg)? END
  public static class CaseFunctionCall implements SpecificFunction {
    Expression expression;
    public final List<CaseFuncAlternative> caseFuncAlternatives;
    FunctionArg functionArg;

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

  public static class FunctionArgs implements SpecificFunction {
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
   POSITION
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
      ')'                         #positionFunctionCall
   * </pre>
   */
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

  /**
   * <pre>
   (SUBSTR | SUBSTRING)
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
      ')'                      #substrFunctionCall
   * </pre>
   */
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

  /// trimFunctionCall
  /**
   * <pre>
   TRIM
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
      ')'
   * </pre>
   */
  public static class TrimFunctionCall implements SpecificFunction {
    public static enum PositioinFormType {
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

  /// CHAR '(' functionArgs (USING charsetName)? ')' #charFunctionCall
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

  /**
   * <pre>
   EXTRACT
      '('
        intervalType
        FROM
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
      ')'                    #extractFunctionCall
   * </pre>
   */
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

  /**
   * <pre>
  GET_FORMAT
      '('
        datetimeFormat=(DATE | TIME | DATETIME)
        ',' stringLiteral
      ')'      #getFormatFunctionCall
   * </pre>
   */
  public static class GetFormatFunctionCall implements SpecificFunction {
    public static enum DatetimeFormatType {
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
    public static enum Type {
      AVG, MAX, MIN, SUM, //
      COUNT, COUNT_DISTINCT, //
      BIT_AND, BIT_OR, BIT_XOR, STD, STDDEV, STDDEV_POP, STDDEV_SAMP, VAR_POP, VAR_SAMP, VARIANCE, //
      GROUP_CONCAT;
    }

    public final AggregateWindowedFunction.Type type;
    public final AggregatorEnum aggregator;
    public final FunctionArg functionArg;
    public final FunctionArgs functionArgs;
    public final List<OrderByExpression> orderByExpression;
    public final String separator;

    AggregateWindowedFunction(AggregateWindowedFunction.Type type, AggregatorEnum aggregator,
        FunctionArg functionArg, FunctionArgs functionArgs,
        List<OrderByExpression> orderByExpression, String separator) {
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

  public static enum AggregatorEnum {
    ALL, DISTINCT
  }

  // passwordFunctionClause : functionName=(PASSWORD | OLD_PASSWORD) '(' functionArg ')'
  public static class PasswordFunctionCall implements FunctionCall {
    public static enum Type {
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

  // scalarFunctionName '(' functionArgs? ')' #scalarFunctionCall
  public static class ScalarFunctionCall implements FunctionCall {
    public static enum Type {
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

  // fullId '(' functionArgs? ')' #udfFunctionCall
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

  /**
   * <pre>
  WEIGHT_STRING
      '('
        (stringLiteral | expression)
        (AS stringFormat=(CHAR | BINARY)
        '(' decimalLiteral ')' )?  levelsInWeightString?
      ')'                           #weightFunctionCall
   * </pre>
   */
  public static class WeightFunctionCall implements SpecificFunction {
    public static enum StringFormatType {
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

  // caseFuncAlternative: WHEN condition=functionArg THEN consequent=functionArg
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

  // functionArg: constant | fullColumnName | functionCall | expression
  public static class FunctionArg implements PrimitiveExpression {
    public static enum Type {
      CONSTANT, FULL_COLUMN_NAME, FUNCTION_CALL, EXPRESSION
    }

    public final FunctionArg.Type type;
    Constant constant;
    FullColumnName fullColumnName;
    FunctionCall functionCall;
    Expression expression;

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
        throw new UnsupportedOperationException();
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

  // expressionAtom COLLATE collationName #collateExpressionAtom
  public static class Collate implements ExpressionAtom {
    public final ExpressionAtom expressionAtom;
    public final CollationName collationName;

    Collate(ExpressionAtom expressionAtom, CollationName collationName) {
      Preconditions.checkArgument(expressionAtom != null);
      Preconditions.checkArgument(collationName != null);

      this.expressionAtom = expressionAtom;
      this.collationName = collationName;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CollateExpressionAtom [expressionAtom=");
      builder.append(expressionAtom);
      builder.append(", collationName=");
      builder.append(collationName);
      builder.append("]");
      return builder.toString();
    }

  }

  // mysqlVariable: LOCAL_ID | GLOBAL_ID
  public static class MysqlVariable implements ExpressionAtom {
    public final String localId;
    public final String globalId;

    MysqlVariable(String localId, String globalId) {
      Preconditions.checkArgument(!(localId == null && globalId == null));

      this.localId = localId;
      this.globalId = globalId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MysqlVariable [localId=");
      builder.append(localId);
      builder.append(", globalId=");
      builder.append(globalId);
      builder.append("]");
      return builder.toString();
    }

  }

  // unaryOperator expressionAtom #unaryExpressionAtom
  public static class UnaryExpressionAtom implements ExpressionAtom {
    public final RelationalUnaryOperatorEnum unaryOperator;
    public final ExpressionAtom expressionAtom;

    UnaryExpressionAtom(RelationalUnaryOperatorEnum unaryOperator, ExpressionAtom expressionAtom) {
      Preconditions.checkArgument(unaryOperator != null);
      Preconditions.checkArgument(expressionAtom != null);

      this.unaryOperator = unaryOperator;
      this.expressionAtom = expressionAtom;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UnaryExpressionAtom [unaryOperator=");
      builder.append(unaryOperator);
      builder.append(", expressionAtom=");
      builder.append(expressionAtom);
      builder.append("]");
      return builder.toString();
    }

  }

  // BINARY expressionAtom #binaryExpressionAtom
  public static class BinaryExpressionAtom implements ExpressionAtom {
    public final ExpressionAtom expressionAtom;

    BinaryExpressionAtom(ExpressionAtom expressionAtom) {
      Preconditions.checkArgument(expressionAtom != null);

      this.expressionAtom = expressionAtom;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("RelationalAlgebraBinaryExpressionAtom [expressionAtom=");
      builder.append(expressionAtom);
      builder.append("]");
      return builder.toString();
    }

  }

  // '(' expression (',' expression)* ')' #nestedExpressionAtom
  public static class NestedExpressionAtom implements ExpressionAtom {
    public final List<Expression> expressions;

    NestedExpressionAtom(List<Expression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("NestedExpressionAtom [expressions=");
      builder.append(expressions);
      builder.append("]");
      return builder.toString();
    }

  }

  // ROW '(' expression (',' expression)+ ')' #nestedRowExpressionAtom
  public static class NestedRowExpressionAtom implements ExpressionAtom {
    public final List<Expression> expressions;

    NestedRowExpressionAtom(List<Expression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("NestedRowExpressionAtom [expressions=");
      builder.append(expressions);
      builder.append("]");
      return builder.toString();
    }

  }

  // EXISTS '(' selectStatement ')' #existsExpessionAtom
  public static class ExistsExpessionAtom implements ExpressionAtom {
    public final SelectStatement selectStatement;

    ExistsExpessionAtom(SelectStatement selectStatement) {
      Preconditions.checkArgument(selectStatement != null);

      this.selectStatement = selectStatement;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ExistsExpessionAtom [selectStatement=");
      builder.append(selectStatement);
      builder.append("]");
      return builder.toString();
    }

  }

  // '(' selectStatement ')' #subqueryExpessionAtom
  public static class SubqueryExpessionAtom implements ExpressionAtom {
    public final SelectStatement selectStatement;

    SubqueryExpessionAtom(SelectStatement selectStatement) {
      Preconditions.checkArgument(selectStatement != null);

      this.selectStatement = selectStatement;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SubqueryExpessionAtom [selectStatement=");
      builder.append(selectStatement);
      builder.append("]");
      return builder.toString();
    }

  }

  // INTERVAL expression intervalType #intervalExpressionAtom
  public static class IntervalExpressionAtom implements ExpressionAtom {
    public final Expression expression;
    public final IntervalType intervalType;

    IntervalExpressionAtom(Expression expression, IntervalType intervalType) {
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(intervalType != null);

      this.expression = expression;
      this.intervalType = intervalType;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("IntervalExpressionAtom [expression=");
      builder.append(expression);
      builder.append(", intervalType=");
      builder.append(intervalType);
      builder.append("]");
      return builder.toString();
    }

  }

  // left=expressionAtom bitOperator right=expressionAtom #bitExpressionAtom
  public static class BitExpressionAtom implements ExpressionAtom {
    public final ExpressionAtom left;
    public final RelationalBitOperatorEnum bitOperator;
    public final ExpressionAtom right;

    BitExpressionAtom(ExpressionAtom left, RelationalBitOperatorEnum bitOperator,
        ExpressionAtom right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(bitOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.bitOperator = bitOperator;
      this.right = right;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("BitExpressionAtom [left=");
      builder.append(left);
      builder.append(", bitOperator=");
      builder.append(bitOperator);
      builder.append(", right=");
      builder.append(right);
      builder.append("]");
      return builder.toString();
    }

  }

  // left=expressionAtom mathOperator right=expressionAtom #mathExpressionAtom
  public static class MathExpressionAtom implements ExpressionAtom {
    public final ExpressionAtom left;
    public final RelationalMathOperatorEnum mathOperator;
    public final ExpressionAtom right;

    MathExpressionAtom(ExpressionAtom left, RelationalMathOperatorEnum mathOperator,
        ExpressionAtom right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(mathOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.mathOperator = mathOperator;
      this.right = right;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MathExpressionAtom [left=");
      builder.append(left);
      builder.append(", mathOperator=");
      builder.append(mathOperator);
      builder.append(", right=");
      builder.append(right);
      builder.append("]");
      return builder.toString();
    }

  }

}