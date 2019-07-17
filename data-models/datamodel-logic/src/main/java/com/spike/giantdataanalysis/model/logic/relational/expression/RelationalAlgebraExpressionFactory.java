package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.SimpleId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthOneDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthTwoDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DataType.LengthTwoOptionalDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.AssignmentField;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.IndexHint;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.IndexHint.IndexHintAction;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.IndexHintTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.LockClauseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OuterJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OuterJoinType;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.StraightJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.SubqueryTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSource;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceBase;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceNested;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSources;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourcesItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.RelationalAlgebraNotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CaseFuncAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CaseFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.CharFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.DataTypeFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ExtractFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionArgs;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.GetFormatFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelInWeightListElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelWeightList;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelWeightRange;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PasswordFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PositionFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.SimpleFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.SubstrFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.TrimFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ValuesFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.WeightFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.BooleanLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.HexadecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClauseAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.ParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QueryExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QueryExpressionNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecificationNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoDumpFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoTextFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoTextFile.TieldsFormatType;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectLinesInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectSpecEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesis;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.CharsetNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.FunctionNameBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.IntervalTypeBaseEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * 关系代数表达式工厂.
 */
public abstract class RelationalAlgebraExpressionFactory {
  // ---------------------------------------------------------------------------
  // RelationalAlgebraExpression
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
  // DBObjects
  // ---------------------------------------------------------------------------
  public static FullId makeFullId(List<Uid> uids, String dotId) {
    return new FullId(uids, dotId);
  }

  public static TableName makeTableName(FullId fullId) {
    return new TableName(fullId);
  }

  public static FullColumnName makeFullColumnName(Uid uid, List<DottedId> dottedIds) {
    return new FullColumnName(uid, dottedIds);
  }

  public static MysqlVariable makeMysqlVariable(String localId, String globalId) {
    return new MysqlVariable(localId, globalId);
  }

  public static CharsetName makeCharsetName(CharsetName.Type type, String value) {
    return new CharsetName(type, value);
  }

  public static CollationName makeCollationName(Uid uid, String stringLiteral) {
    return new CollationName(uid, stringLiteral);
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

  // ---------------------------------------------------------------------------
  // Literals
  // ---------------------------------------------------------------------------
  public static DecimalLiteral makeDecimalLiteral(DecimalLiteral.Type type, String literal) {
    return new DecimalLiteral(type, literal);
  }

  public static StringLiteral makeStringLiteral(CharsetNameBaseEnum stringCharsetName,
      List<String> stringLiterals, String startNationalStringLiteral, CollationName collationName) {
    return new StringLiteral(stringCharsetName, stringLiterals, startNationalStringLiteral,
        collationName);
  }

  public static BooleanLiteral makeBooleanLiteral(Boolean literal) {
    return new BooleanLiteral(literal);
  }

  public static HexadecimalLiteral makeHexadecimalLiteral(CharsetNameBaseEnum stringCharsetName,
      String literal) {
    return new HexadecimalLiteral(stringCharsetName, literal);
  }

  public static Constant makeConstant(Constant.Type type, String literal, Boolean not) {
    return new Constant(type, literal, not);
  }

  // ---------------------------------------------------------------------------
  // DataTypes
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // CommonLists
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // CommonExpressons
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Functions
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // PrimitiveExpression
  // ---------------------------------------------------------------------------
  public static UidList makeUidList(List<Uid> uids) {
    return new UidList(uids);
  }

  public static SimpleFunctionCall makeSimpleFunctionCall(SimpleFunctionCall.Type type) {
    return new SimpleFunctionCall(type);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      Expression expression, CharsetName charsetName) {
    return new DataTypeFunctionCall(type, expression, charsetName);
  }

  public static DataTypeFunctionCall makeDataTypeFunctionCall(DataTypeFunctionCall.Type type,
      Expression expression, ConvertedDataType convertedDataType) {
    return new DataTypeFunctionCall(type, expression, convertedDataType);
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

  public static ValuesFunctionCall makeValuesFunctionCall(FullColumnName fullColumnName) {
    return new ValuesFunctionCall(fullColumnName);
  }

  public static CaseFunctionCall makeCaseFunctionCall(Expression expression,
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
      Expression positionExpression, String inString, Expression inExpression) {
    return new PositionFunctionCall(positionString, positionExpression, inString, inExpression);
  }

  public static SubstrFunctionCall makeSubstrFunctionCall(String sourceString,
      Expression sourceExpression, //
      DecimalLiteral fromDecimal, Expression fromExpression, //
      DecimalLiteral forDecimal, Expression forExpression//
  ) {
    return new SubstrFunctionCall(sourceString, sourceExpression, fromDecimal, fromExpression,
        forDecimal, forExpression);
  }

  public static TrimFunctionCall makeTrimFunctionCall(TrimFunctionCall.PositioinFormType type, //
      StringLiteral sourceString, Expression sourceExpression, //
      StringLiteral fromString, Expression fromExpression//
  ) {
    return new TrimFunctionCall(type, sourceString, sourceExpression, fromString, fromExpression);
  }

  public static WeightFunctionCall makeWeightFunctionCall(StringLiteral stringLiteral,
      Expression expression, WeightFunctionCall.StringFormatType type,
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
      StringLiteral sourceString, Expression sourceExpression) {
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
      AggregateWindowedFunction.Type type, AggregateWindowedFunction.Aggregator aggregator,
      FunctionArg functionArg, FunctionArgs functionArgs, List<OrderByExpression> orderByExpression,
      String separator) {
    return new AggregateWindowedFunction(type, aggregator, functionArg, functionArgs,
        orderByExpression, separator);
  }

  public static OrderByExpression makeOrderByExpression(Expression expression,
      OrderByExpression.OrderType order) {
    return new OrderByExpression(expression, order);
  }

  public static ScalarFunctionCall makeScalarFunctionCall(ScalarFunctionCall.Type type,
      FunctionNameBaseEnum functionNameBase, FunctionArgs functionArgs) {
    return new ScalarFunctionCall(type, functionNameBase, functionArgs);
  }

  public static UdfFunctionCall makeUdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
    return new UdfFunctionCall(fullId, functionArgs);
  }

  public static PasswordFunctionCall makePasswordFunctionCall(PasswordFunctionCall.Type type,
      FunctionArg functionArg) {
    return new PasswordFunctionCall(type, functionArg);
  }

  // ---------------------------------------------------------------------------
  // Expression
  // ---------------------------------------------------------------------------
  public static Expressions makeExpressions(List<Expression> expressions) {
    return new Expressions(expressions);
  }

  public static RelationalAlgebraNotExpression makeNotExpression(Expression expression) {
    return new RelationalAlgebraNotExpression(expression);
  }

  public static LogicalExpression makeLogicalExpression(Expression first,
      RelationalLogicalOperatorEnum operator, Expression second) {
    return new LogicalExpression(first, operator, second);
  }

  public static IsExpression makeIsExpression(PredicateExpression predicate, Boolean not,
      IsExpression.TestValue testValue) {
    return new IsExpression(predicate, not, testValue);
  }

  public static InPredicate makeInPredicate(PredicateExpression predicate, Boolean not,
      SelectStatement selectStatement, Expressions expressions) {
    return new InPredicate(predicate, not, selectStatement, expressions);
  }

  public static IsNullPredicate makeIsNullPredicate(PredicateExpression predicate,
      Boolean notNull) {
    return new IsNullPredicate(predicate, notNull);
  }

  public static BinaryComparasionPredicate makeBinaryComparasionPredicate(PredicateExpression left,
      RelationalComparisonOperatorEnum comparisonOperator, PredicateExpression right) {
    return new BinaryComparasionPredicate(left, comparisonOperator, right);
  }

  public static SubqueryComparasionPredicate makeSubqueryComparasionPredicatePredicate(
      PredicateExpression predicate, RelationalComparisonOperatorEnum comparisonOperator,
      SubqueryComparasionPredicate.QuantifierEnum quantifier, SelectStatement selectStatement) {
    return new SubqueryComparasionPredicate(predicate, comparisonOperator, quantifier,
        selectStatement);
  }

  public static BetweenPredicate makeBetweenPredicate(PredicateExpression first, Boolean not,
      PredicateExpression second, PredicateExpression third) {
    return new BetweenPredicate(first, not, second, third);
  }

  public static SoundsLikePredicate makeSoundsLikePredicate(PredicateExpression first,
      PredicateExpression second) {
    return new SoundsLikePredicate(first, second);
  }

  public static LikePredicate makeLikePredicate(PredicateExpression first, Boolean not,
      PredicateExpression second, String stringLiteral) {
    return new LikePredicate(first, not, second, stringLiteral);
  }

  public static RegexpPredicate makeRegexpPredicate(PredicateExpression first, Boolean not,
      RegexpPredicate.RegexType regex, PredicateExpression second) {
    return new RegexpPredicate(first, not, regex, second);
  }

  public static ExpressionAtomPredicate makeExpressionAtomPredicate(String localId,
      ExpressionAtom expressionAtom) {
    return new ExpressionAtomPredicate(localId, expressionAtom);
  }

  public static Collate makeCollateExpressionAtom(ExpressionAtom expressionAtom,
      CollationName collationName) {
    return new Collate(expressionAtom, collationName);
  }

  public static UnaryExpressionAtom makeUnaryExpressionAtom(
      RelationalUnaryOperatorEnum unaryOperator, ExpressionAtom expressionAtom) {
    return new UnaryExpressionAtom(unaryOperator, expressionAtom);
  }

  public static BinaryExpressionAtom makeBinaryExpressionAtom(ExpressionAtom expressionAtom) {
    return new BinaryExpressionAtom(expressionAtom);
  }

  public static NestedExpressionAtom makeNestedExpressionAtom(List<Expression> expressions) {
    return new NestedExpressionAtom(expressions);
  }

  public static NestedRowExpressionAtom makeNestedRowExpressionAtom(List<Expression> expressions) {
    return new NestedRowExpressionAtom(expressions);
  }

  public static ExistsExpessionAtom makeExistsExpessionAtom(SelectStatement selectStatement) {
    return new ExistsExpessionAtom(selectStatement);
  }

  public static SubqueryExpessionAtom makeSubqueryExpessionAtom(SelectStatement selectStatement) {
    return new SubqueryExpessionAtom(selectStatement);
  }

  public static IntervalExpressionAtom makeIntervalExpressionAtom(Expression expression,
      IntervalType intervalType) {
    return new IntervalExpressionAtom(expression, intervalType);
  }

  public static BitExpressionAtom makeBitExpressionAtom(ExpressionAtom left,
      RelationalBitOperatorEnum bitOperator, ExpressionAtom right) {
    return new BitExpressionAtom(left, bitOperator, right);
  }

  public static MathExpressionAtom makeMathExpressionAtom(ExpressionAtom left,
      RelationalMathOperatorEnum mathOperator, ExpressionAtom right) {
    return new MathExpressionAtom(left, mathOperator, right);
  }

  // ---------------------------------------------------------------------------
  // SimpleIdSets
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // SQLStatement
  // ---------------------------------------------------------------------------
  public static SqlStatements makeSqlStatements(List<SqlStatement> sqlStatements) {
    if (CollectionUtils.isNotEmpty(sqlStatements)) {
      for (SqlStatement item : sqlStatements) {
        Preconditions.checkArgument(item != null);
      }
    }

    return new SqlStatements(sqlStatements);
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

  public static FromClause makeFromClause(TableSources tableSources, Expression whereExpr,
      List<GroupByItem> groupByItems, Boolean withRollup, Expression havingExpr) {
    return new FromClause(tableSources, whereExpr, groupByItems, withRollup, havingExpr);
  }

  public static GroupByItem makeGroupByItem(Expression expression, GroupByItem.OrderType order) {
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
      IndexHint.KeyFormat keyFormat, IndexHintTypeEnum indexHintType, UidList uidList) {
    return new IndexHint(indexHintAction, keyFormat, indexHintType, uidList);
  }

  public static InnerJoin makeInnerJoin(TableSourceItem tableSourceItem, Expression expression,
      UidList uidList) {
    return new InnerJoin(tableSourceItem, expression, uidList);
  }

  public static StraightJoin makeStraightJoin(TableSourceItem tableSourceItem,
      Expression expression) {
    return new StraightJoin(tableSourceItem, expression);
  }

  public static OuterJoin makeOuterJoin(OuterJoinType type, TableSourceItem tableSourceItem,
      Expression expression, UidList uidList) {
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
      Expression expression, Uid uid) {
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

}
