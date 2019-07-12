package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraBetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraBinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraConditionalExpressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraInPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraIsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraIsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraLogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraNotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraPredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraRegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraSoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraSubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.AggregatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.CaseFuncAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.CaseFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.CharFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.DataTypeFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.ExtractFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.FunctionArgs;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.GetFormatFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.PasswordFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.PositionFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.SimpleFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.SubstrFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.TrimFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.ValuesFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.WeightFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.BooleanLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.CharsetNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.FunctionNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.HexadecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.IntervalTypeBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LengthOneDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LengthTwoDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LengthTwoOptionalDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LevelInWeightListElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LevelWeightList;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LevelWeightRange;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.NullLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.SimpleId;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.AssignmentField;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.IndexHint;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.IndexHint.IndexHintAction;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.IndexHintType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.LimitClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.LimitClauseAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.LockClauseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.OrderByClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.OuterJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.OuterJoinType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.ParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.QueryExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.QueryExpressionNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.QuerySpecificationNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.RelationalAlgebraStatementsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectIntoDumpFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectIntoTextFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectIntoTextFile.TieldsFormatType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectIntoVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectLinesInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectSpecEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.StraightJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SubqueryTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSource;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSourceBase;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSourceItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSourceNested;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSources;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.TableSourcesItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.UnionParenthesis;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.UnionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.UnionTypeEnum;
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
  // RelationalAlgebraPrimitiveExpression
  // ---------------------------------------------------------------------------
  public static UidList makeUidList(List<Uid> uids) {
    return new UidList(uids);
  }

  public static Uid makeUid(Uid.Type type, String literal) {
    return new Uid(type, literal);
  }

  public static SimpleId makeSimpleId(SimpleId.Type type, String literal) {
    return new SimpleId(type, literal);
  }

  public static DottedId makeDottedId(String dotId, Uid uid) {
    return new DottedId(dotId, uid);
  }

  public static SimpleFunctionCall makeSimpleFunctionCall(SimpleFunctionCall.Type type) {
    return new SimpleFunctionCall(type);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      RelationalAlgebraConditionalExpression expression, CharsetName charsetName) {
    return new DataTypeFunctionCall(type, expression, charsetName);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      RelationalAlgebraConditionalExpression expression, ConvertedDataType convertedDataType) {
    return new DataTypeFunctionCall(type, expression, convertedDataType);
  }

  public static CharsetName makeCharsetName(CharsetName.Type type, String value) {
    return new CharsetName(type, value);
  }

  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthOneDimension lengthOneDimension) {
    return new ConvertedDataType(type, lengthOneDimension);
  }

  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthOneDimension lengthOneDimension, CharsetNameBaseEnum charsetName) {
    return new ConvertedDataType(type, lengthOneDimension, charsetName);
  }

  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type) {
    return new ConvertedDataType(type);
  }

  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      LengthTwoDimension lengthTwoDimension) {
    return new ConvertedDataType(type, lengthTwoDimension);
  }

  public static ConvertedDataType makeConvertedDataType(ConvertedDataType.Type type,
      boolean signed) {
    return new ConvertedDataType(type, signed);
  }

  public static LengthOneDimension makeLengthOneDimension(DecimalLiteral decimalLiteral) {
    return new LengthOneDimension(decimalLiteral);
  }

  public static LengthTwoDimension makeLengthTwoDimension(DecimalLiteral first,
      DecimalLiteral second) {
    return new LengthTwoDimension(first, second);
  }

  public static LengthTwoOptionalDimension makeLengthTwoOptionalDimension(DecimalLiteral first,
      DecimalLiteral second) {
    return new LengthTwoOptionalDimension(first, second);
  }

  public static DecimalLiteral makeDecimalLiteral(DecimalLiteral.Type type, String literal) {
    return new DecimalLiteral(type, literal);
  }

  public static ValuesFunctionCall makeValuesFunctionCall(FullColumnName fullColumnName) {
    return new ValuesFunctionCall(fullColumnName);
  }

  public static CaseFunctionCall makeCaseFunctionCall(
      RelationalAlgebraConditionalExpression expression,
      List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
    return new CaseFunctionCall(expression, caseFuncAlternatives, functionArg);
  }

  public static CaseFunctionCall makeCaseFunctionCall(
      List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
    return new CaseFunctionCall(caseFuncAlternatives, functionArg);
  }

  public static CaseFuncAlternative makeCaseFuncAlternative(FunctionArg condition,
      FunctionArg consequent) {
    return new CaseFuncAlternative(condition, consequent);
  }

  public static FunctionArg makeFunctionArg(FunctionArg.Type type, Object value) {
    return new FunctionArg(type, value);
  }

  public static CharFunctionCall makeCharFunctionCall(FunctionArgs functionArgs,
      CharsetName charsetName) {
    return new CharFunctionCall(functionArgs, charsetName);
  }

  public static FunctionArgs makeFunctionArgs(List<FunctionArg> functionArgs) {
    return new FunctionArgs(functionArgs);
  }

  public static PositionFunctionCall makePositionFunctionCall(String positionString,
      RelationalAlgebraConditionalExpression positionExpression, String inString,
      RelationalAlgebraConditionalExpression inExpression) {
    return new PositionFunctionCall(positionString, positionExpression, inString, inExpression);
  }

  public static SubstrFunctionCall makeSubstrFunctionCall(String sourceString,
      RelationalAlgebraConditionalExpression sourceExpression, //
      DecimalLiteral fromDecimal, RelationalAlgebraConditionalExpression fromExpression, //
      DecimalLiteral forDecimal, RelationalAlgebraConditionalExpression forExpression//
  ) {
    return new SubstrFunctionCall(sourceString, sourceExpression, fromDecimal, fromExpression,
        forDecimal, forExpression);
  }

  public static TrimFunctionCall makeTrimFunctionCall(TrimFunctionCall.PositioinFormType type, //
      StringLiteral sourceString, RelationalAlgebraConditionalExpression sourceExpression, //
      StringLiteral fromString, RelationalAlgebraConditionalExpression fromExpression//
  ) {
    return new TrimFunctionCall(type, sourceString, sourceExpression, fromString, fromExpression);
  }

  public static StringLiteral makeStringLiteral(CharsetNameBaseEnum stringCharsetName,
      List<String> stringLiterals, String startNationalStringLiteral, CollationName collationName) {
    return new StringLiteral(stringCharsetName, stringLiterals, startNationalStringLiteral,
        collationName);
  }

  public static HexadecimalLiteral makeHexadecimalLiteral(CharsetNameBaseEnum stringCharsetName,
      String literal) {
    return new HexadecimalLiteral(stringCharsetName, literal);
  }

  public static BooleanLiteral makeBooleanLiteral(Boolean literal) {
    return new BooleanLiteral(literal);
  }

  public static NullLiteral makeNullLiterall(String literal) {
    return new NullLiteral(literal);
  }

  public static CollationName makeCollationName(Uid uid, String stringLiteral) {
    return new CollationName(uid, stringLiteral);
  }

  public static WeightFunctionCall makeWeightFunctionCall(StringLiteral stringLiteral,
      RelationalAlgebraConditionalExpression expression, WeightFunctionCall.StringFormatType type,
      DecimalLiteral decimalLiteral, LevelsInWeightString levelsInWeightString) {
    return new WeightFunctionCall(stringLiteral, expression, type, decimalLiteral,
        levelsInWeightString);
  }

  public static LevelWeightList
      makeLevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
    return new LevelWeightList(levelInWeightListElements);
  }

  public static LevelWeightRange makeLevelWeightRange(DecimalLiteral firstLevel,
      DecimalLiteral lastLevel) {
    return new LevelWeightRange(firstLevel, lastLevel);
  }

  public static LevelInWeightListElement makeLevelInWeightListElement(DecimalLiteral decimalLiteral,
      LevelInWeightListElement.OrderType orderType) {
    return new LevelInWeightListElement(decimalLiteral, orderType);
  }

  public static ExtractFunctionCall makeExtractFunctionCall(IntervalType intervalType,
      StringLiteral sourceString, RelationalAlgebraConditionalExpression sourceExpression) {
    return new ExtractFunctionCall(intervalType, sourceString, sourceExpression);
  }

  public static IntervalType makeIntervalType(IntervalType.Type type,
      IntervalTypeBaseEnum intervalTypeBase) {
    return new IntervalType(type, intervalTypeBase);
  }

  public static GetFormatFunctionCall makeGetFormatFunctionCall(
      GetFormatFunctionCall.DatetimeFormatType type, StringLiteral stringLiteral) {
    return new GetFormatFunctionCall(type, stringLiteral);
  }

  public static AggregateWindowedFunction makeAggregateWindowedFunction(
      AggregateWindowedFunction.Type type, AggregatorEnum aggregator, FunctionArg functionArg,
      FunctionArgs functionArgs, List<OrderByExpression> orderByExpression, String separator) {
    return new AggregateWindowedFunction(type, aggregator, functionArg, functionArgs,
        orderByExpression, separator);
  }

  public static OrderByExpression makeOrderByExpression(
      RelationalAlgebraConditionalExpression expression, OrderByExpression.OrderType order) {
    return new OrderByExpression(expression, order);
  }

  public static ScalarFunctionCall makeScalarFunctionCall(ScalarFunctionCall.Type type,
      FunctionNameBaseEnum functionNameBase, FunctionArgs functionArgs) {
    return new ScalarFunctionCall(type, functionNameBase, functionArgs);
  }

  public static UdfFunctionCall makeUdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
    return new UdfFunctionCall(fullId, functionArgs);
  }

  public static FullId makeFullId(List<Uid> uids, String dotId) {
    return new FullId(uids, dotId);
  }

  public static PasswordFunctionCall makePasswordFunctionCall(PasswordFunctionCall.Type type,
      FunctionArg functionArg) {
    return new PasswordFunctionCall(type, functionArg);
  }

  // ---------------------------------------------------------------------------
  // RelationalAlgebraConditionalExpression
  // ---------------------------------------------------------------------------
  public static RelationalAlgebraConditionalExpressions
      makeExpressions(List<RelationalAlgebraConditionalExpression> expressions) {
    return new RelationalAlgebraConditionalExpressions(expressions);
  }

  public static RelationalAlgebraNotExpression
      makeNotExpression(RelationalAlgebraConditionalExpression expression) {
    return new RelationalAlgebraNotExpression(expression);
  }

  public static RelationalAlgebraLogicalExpression makeLogicalExpression(
      RelationalAlgebraConditionalExpression first, RelationalLogicalOperatorEnum operator,
      RelationalAlgebraConditionalExpression second) {
    return new RelationalAlgebraLogicalExpression(first, operator, second);
  }

  public static RelationalAlgebraIsExpression makeIsExpression(
      RelationalAlgebraPredicateExpression predicate, Boolean not,
      RelationalAlgebraIsExpression.TestValue testValue) {
    return new RelationalAlgebraIsExpression(predicate, not, testValue);
  }

  public static RelationalAlgebraInPredicate makeInPredicate(
      RelationalAlgebraPredicateExpression predicate, Boolean not, SelectStatement selectStatement,
      RelationalAlgebraConditionalExpressions expressions) {
    return new RelationalAlgebraInPredicate(predicate, not, selectStatement, expressions);
  }

  public static RelationalAlgebraIsNullPredicate
      makeIsNullPredicate(RelationalAlgebraPredicateExpression predicate, Boolean notNull) {
    return new RelationalAlgebraIsNullPredicate(predicate, notNull);
  }

  public static RelationalAlgebraBinaryComparasionPredicate makeBinaryComparasionPredicate(
      RelationalAlgebraPredicateExpression left,
      RelationalComparisonOperatorEnum comparisonOperator,
      RelationalAlgebraPredicateExpression right) {
    return new RelationalAlgebraBinaryComparasionPredicate(left, comparisonOperator, right);
  }

  public static RelationalAlgebraSubqueryComparasionPredicate
      makeSubqueryComparasionPredicatePredicate(RelationalAlgebraPredicateExpression predicate,
          RelationalComparisonOperatorEnum comparisonOperator,
          RelationalAlgebraSubqueryComparasionPredicate.QuantifierEnum quantifier,
          SelectStatement selectStatement) {
    return new RelationalAlgebraSubqueryComparasionPredicate(predicate, comparisonOperator,
        quantifier, selectStatement);
  }

  public static RelationalAlgebraBetweenPredicate makeBetweenPredicate(
      RelationalAlgebraPredicateExpression first, Boolean not,
      RelationalAlgebraPredicateExpression second, RelationalAlgebraPredicateExpression third) {
    return new RelationalAlgebraBetweenPredicate(first, not, second, third);
  }

  public static RelationalAlgebraSoundsLikePredicate makeSoundsLikePredicate(
      RelationalAlgebraPredicateExpression first, RelationalAlgebraPredicateExpression second) {
    return new RelationalAlgebraSoundsLikePredicate(first, second);
  }

  public static RelationalAlgebraLikePredicate makeLikePredicate(
      RelationalAlgebraPredicateExpression first, Boolean not,
      RelationalAlgebraPredicateExpression second, String stringLiteral) {
    return new RelationalAlgebraLikePredicate(first, not, second, stringLiteral);
  }

  public static RelationalAlgebraRegexpPredicate makeRegexpPredicate(
      RelationalAlgebraPredicateExpression first, Boolean not,
      RelationalAlgebraRegexpPredicate.RegexType regex,
      RelationalAlgebraPredicateExpression second) {
    return new RelationalAlgebraRegexpPredicate(first, not, regex, second);
  }

  public static RelationalAlgebraExpressionAtomPredicate makeExpressionAtomPredicate(String localId,
      RelationalAlgebraExpressionAtom expressionAtom) {
    return new RelationalAlgebraExpressionAtomPredicate(localId, expressionAtom);
  }

  // ---------------------------------------------------------------------------
  // RelationalAlgebraExpressionAtom
  // ---------------------------------------------------------------------------

  public static Constant makeConstant(Constant.Type type, String literal, Boolean not) {
    return new Constant(type, literal, not);
  }

  public static FullColumnName makeFullColumnName(Uid uid, List<DottedId> dottedIds) {
    return new FullColumnName(uid, dottedIds);
  }

  public static Collate makeCollateExpressionAtom(RelationalAlgebraExpressionAtom expressionAtom,
      CollationName collationName) {
    return new Collate(expressionAtom, collationName);
  }

  public static MysqlVariable makeMysqlVariable(String localId, String globalId) {
    return new MysqlVariable(localId, globalId);
  }

  public static UnaryExpressionAtom makeUnaryExpressionAtom(
      RelationalUnaryOperatorEnum unaryOperator, RelationalAlgebraExpressionAtom expressionAtom) {
    return new UnaryExpressionAtom(unaryOperator, expressionAtom);
  }

  public static BinaryExpressionAtom
      makeBinaryExpressionAtom(RelationalAlgebraExpressionAtom expressionAtom) {
    return new BinaryExpressionAtom(expressionAtom);
  }

  public static NestedExpressionAtom
      makeNestedExpressionAtom(List<RelationalAlgebraConditionalExpression> expressions) {
    return new NestedExpressionAtom(expressions);
  }

  public static NestedRowExpressionAtom
      makeNestedRowExpressionAtom(List<RelationalAlgebraConditionalExpression> expressions) {
    return new NestedRowExpressionAtom(expressions);
  }

  public static ExistsExpessionAtom makeExistsExpessionAtom(SelectStatement selectStatement) {
    return new ExistsExpessionAtom(selectStatement);
  }

  public static SubqueryExpessionAtom makeSubqueryExpessionAtom(SelectStatement selectStatement) {
    return new SubqueryExpessionAtom(selectStatement);
  }

  public static IntervalExpressionAtom makeIntervalExpressionAtom(
      RelationalAlgebraConditionalExpression expression, IntervalType intervalType) {
    return new IntervalExpressionAtom(expression, intervalType);
  }

  public static BitExpressionAtom makeBitExpressionAtom(RelationalAlgebraExpressionAtom left,
      RelationalBitOperatorEnum bitOperator, RelationalAlgebraExpressionAtom right) {
    return new BitExpressionAtom(left, bitOperator, right);
  }

  public static MathExpressionAtom makeMathExpressionAtom(RelationalAlgebraExpressionAtom left,
      RelationalMathOperatorEnum mathOperator, RelationalAlgebraExpressionAtom right) {
    return new MathExpressionAtom(left, mathOperator, right);
  }

  // ---------------------------------------------------------------------------
  // RelationalAlgebraStatementExpression
  // ---------------------------------------------------------------------------
  public static RelationalAlgebraStatementsExpression
      makeSqlStatements(List<RelationalAlgebraStatementExpression> sqlStatements) {
    if (CollectionUtils.isNotEmpty(sqlStatements)) {
      for (RelationalAlgebraStatementExpression item : sqlStatements) {
        Preconditions.checkArgument(item != null);
      }
    }

    return new RelationalAlgebraStatementsExpression(sqlStatements);
  }

  public static SimpleSelect makeSimpleSelect(QuerySpecification querySpecification,
      LockClauseEnum lockClause) {
    return new SimpleSelect(querySpecification, lockClause);
  }

  public static ParenthesisSelect makeParenthesisSelect(QueryExpression queryExpression,
      LockClauseEnum lockClause) {
    return new ParenthesisSelect(queryExpression, lockClause);
  }

  public static UnionSelect makeUnionSelect(QuerySpecificationNointo querySpecificationNointo,
      List<UnionStatement> unionStatements, UnionTypeEnum unionType,
      QuerySpecification querySpecification, QueryExpression queryExpression,
      OrderByClause orderByClause, LimitClause limitClause, LockClauseEnum lockClause) {
    return new UnionSelect(querySpecificationNointo, unionStatements, unionType, querySpecification,
        queryExpression, orderByClause, limitClause, lockClause);
  }

  public static UnionParenthesisSelect makeUnionParenthesisSelect(
      QueryExpressionNointo queryExpressionNointo, List<UnionParenthesis> unionParenthesisList,
      UnionTypeEnum unionType, QueryExpression queryExpression, OrderByClause orderByClause,
      LimitClause limitClause, LockClauseEnum lockClause) {
    return new UnionParenthesisSelect(queryExpressionNointo, unionParenthesisList, unionType,
        queryExpression, orderByClause, limitClause, lockClause);
  }

  public static UnionStatement makeUnionStatement(UnionTypeEnum unionType,
      QuerySpecificationNointo querySpecificationNointo,
      QueryExpressionNointo queryExpressionNointo) {
    return new UnionStatement(unionType, querySpecificationNointo, queryExpressionNointo);
  }

  public static UnionParenthesis makeUnionParenthesis(UnionTypeEnum unionType,
      QueryExpressionNointo queryExpressionNointo) {
    return new UnionParenthesis(unionType, queryExpressionNointo);
  }

  public static QuerySpecificationNointo makeQuerySpecificationNointo(
      List<SelectSpecEnum> selectSpecs, SelectElements selectElements, FromClause fromClause,
      OrderByClause orderByClause, LimitClause limitClause) {
    return new QuerySpecificationNointo(selectSpecs, selectElements, fromClause, orderByClause,
        limitClause);
  }

  public static QuerySpecification makeQuerySpecification(List<SelectSpecEnum> selectSpecs,
      SelectElements selectElements, SelectIntoExpression selectIntoExpression,
      FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
    return new QuerySpecification(selectSpecs, selectElements, selectIntoExpression, fromClause,
        orderByClause, limitClause);
  }

  public static QueryExpressionNointo
      makeQueryExpressionNointo(QuerySpecificationNointo querySpecificationNointo) {
    return new QueryExpressionNointo(querySpecificationNointo);
  }

  public static FromClause makeFromClause(TableSources tableSources,
      RelationalAlgebraConditionalExpression whereExpr, List<GroupByItem> groupByItems,
      Boolean withRollup, RelationalAlgebraConditionalExpression havingExpr) {
    return new FromClause(tableSources, whereExpr, groupByItems, withRollup, havingExpr);
  }

  public static GroupByItem makeGroupByItem(RelationalAlgebraConditionalExpression expression,
      GroupByItem.OrderType order) {
    return new GroupByItem(expression, order);
  }

  public static OrderByClause makeOrderByClause(List<OrderByExpression> orderByExpressions) {
    return new OrderByClause(orderByExpressions);
  }

  public static LimitClause makeLimitClause(LimitClauseAtom limit, LimitClauseAtom offset) {
    return new LimitClause(limit, offset);
  }

  public static LimitClauseAtom makeLimitClauseAtom(DecimalLiteral decimalLiteral,
      MysqlVariable mysqlVariable) {
    return new LimitClauseAtom(decimalLiteral, mysqlVariable);
  }

  public static TableSources makeTableSources(List<TableSource> tableSources) {
    return new TableSources(tableSources);
  }

  public static TableSourceBase makeTableSourceBase(TableSourceItem tableSourceItem,
      List<JoinPart> joinParts) {
    return new TableSourceBase(tableSourceItem, joinParts);
  }

  public static TableSourceNested makeTableSourceNested(TableSourceItem tableSourceItem,
      List<JoinPart> joinParts) {
    return new TableSourceNested(tableSourceItem, joinParts);
  }

  public static AtomTableItem makeAtomTableItem(TableName tableName, UidList uidList, Uid alias,
      List<IndexHint> indexHints) {
    return new AtomTableItem(tableName, uidList, alias, indexHints);
  }

  public static SubqueryTableItem makeSubqueryTableItem(SelectStatement selectStatement,
      SelectStatement parenthesisSubquery) {
    return new SubqueryTableItem(selectStatement, parenthesisSubquery);
  }

  public static TableSourcesItem makeTableSourcesItem(TableSources tableSources) {
    return new TableSourcesItem(tableSources);
  }

  public static IndexHint makeIndexHint(IndexHintAction indexHintAction,
      IndexHint.KeyFormat keyFormat, IndexHintType indexHintType, UidList uidList) {
    return new IndexHint(indexHintAction, keyFormat, indexHintType, uidList);
  }

  public static TableName makeTableName(FullId fullId) {
    return new TableName(fullId);
  }

  public static InnerJoin makeInnerJoin(TableSourceItem tableSourceItem,
      RelationalAlgebraConditionalExpression expression, UidList uidList) {
    return new InnerJoin(tableSourceItem, expression, uidList);
  }

  public static StraightJoin makeStraightJoin(TableSourceItem tableSourceItem,
      RelationalAlgebraConditionalExpression expression) {
    return new StraightJoin(tableSourceItem, expression);
  }

  public static OuterJoin makeOuterJoin(OuterJoinType type, TableSourceItem tableSourceItem,
      RelationalAlgebraConditionalExpression expression, UidList uidList) {
    return new OuterJoin(type, tableSourceItem, expression, uidList);
  }

  public static NaturalJoin makeNaturalJoin(OuterJoinType outerJoinType,
      TableSourceItem tableSourceItem) {
    return new NaturalJoin(outerJoinType, tableSourceItem);
  }

  public static SelectElements makeSelectElements(Boolean star,
      List<SelectElement> selectElements) {
    return new SelectElements(star, selectElements);
  }

  public static SelectStarElement makeSelectStarElement(FullId fullId) {
    return new SelectStarElement(fullId);
  }

  public static SelectColumnElement makeSelectColumnElement(FullColumnName fullColumnName,
      Uid uid) {
    return new SelectColumnElement(fullColumnName, uid);
  }

  public static SelectFunctionElement makeSelectFunctionElement(FunctionCall functionCall,
      Uid uid) {
    return new SelectFunctionElement(functionCall, uid);
  }

  public static SelectExpressionElement makeSelectExpressionElement(String localId,
      RelationalAlgebraConditionalExpression expression, Uid uid) {
    return new SelectExpressionElement(localId, expression, uid);
  }

  public static QueryExpression makeQueryExpression(QuerySpecification querySpecification,
      QueryExpression queryExpression) {
    return new QueryExpression(querySpecification, queryExpression);
  }

  public static SelectIntoVariables
      makeSelectIntoVariables(List<AssignmentField> assignmentFields) {
    return new SelectIntoVariables(assignmentFields);
  }

  public static AssignmentField makeAssignmentField(Uid uid, String localId) {
    return new AssignmentField(uid, localId);
  }

  public static SelectIntoDumpFile makeSelectIntoDumpFile(String stringLiteral) {
    return new SelectIntoDumpFile(stringLiteral);
  }

  public static SelectIntoTextFile makeSelectIntoTextFile(String filename, CharsetName charsetName,
      TieldsFormatType fieldsFormat, List<SelectFieldsInto> selectFieldsIntos,
      List<SelectLinesInto> selectLinesInto) {
    return new SelectIntoTextFile(filename, charsetName, fieldsFormat, selectFieldsIntos,
        selectLinesInto);
  }

  public static SelectFieldsInto makeSelectFieldsInto(SelectFieldsInto.Type type,
      Boolean optionally, String stringLiteral) {
    return new SelectFieldsInto(type, optionally, stringLiteral);
  }

  public static SelectLinesInto makeSelectLinesInto(SelectLinesInto.Type type,
      String stringLiteral) {
    return new SelectLinesInto(type, stringLiteral);
  }

  // ---------------------------------------------------------------------------
  // Classes
  // ---------------------------------------------------------------------------

  /** 关系代数基本表达式. */
  public static class RelationalAlgebraBasicExpression implements RelationalAlgebraExpression {
    protected final RelationalTuples tuples;

    RelationalAlgebraBasicExpression(RelationalTuples tuples) {
      this.tuples = tuples;
    }

    public RelationalTuples tuples() {
      return tuples;
    }
  }

  /** 关系代数中二元表达式. */
  public static class RelationalAlgebraBinaryOperandExpression
      implements RelationalAlgebraExpression {
    protected final RelationalAlgebraExpression first;
    protected final RelationalAlgebraExpression second;

    RelationalAlgebraBinaryOperandExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      this.first = first;
      this.second = second;
    }

    public RelationalAlgebraExpression first() {
      return first;
    }

    public RelationalAlgebraExpression second() {
      return second;
    }
  }

  /** 并表达式. */
  public static class RelationalAlgebraUnionExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraUnionExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 交表达式. */
  public static class RelationalAlgebraIntersectionExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraIntersectionExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 差表达式. */
  public static class RelationalAlgebraDifferenceExpression
      extends RelationalAlgebraBinaryOperandExpression {

    RelationalAlgebraDifferenceExpression(RelationalAlgebraExpression first,
        RelationalAlgebraExpression second) {
      super(first, second);
    }
  }

  /** 投影表达式. */
  public static class RelationalAlgebraProjectExpression implements RelationalAlgebraExpression {
    final RelationalAlgebraExpression first;
    final List<RelationalAttribute> attributes;

    RelationalAlgebraProjectExpression(RelationalAlgebraExpression first,
        List<RelationalAttribute> attributes) {
      this.first = first;
      this.attributes = attributes;
    }

    public RelationalAlgebraExpression first() {
      return first;
    }

    public List<RelationalAttribute> attributes() {
      return attributes;
    }
  }

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
