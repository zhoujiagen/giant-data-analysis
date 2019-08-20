// package com.spike.giantdataanalysis.model.logic.relational;
//
// import java.util.List;
//
// import com.google.common.collect.Lists;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.LockClauseEnum;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByClause;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByExpression;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSources;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsExpression;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LogicalExpression;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.NotExpression;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.Expression.PredicateExpression;
// import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.BinaryExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.BitExpressionAtom;
// import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.Collate;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.ExistsExpessionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.IntervalExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.MathExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.NestedExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.NestedRowExpressionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.SubqueryExpessionAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.UnaryExpressionAtom;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionCall;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
// import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement;
// import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.FromClause;
// import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.GroupByItem;
// import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClause;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClauseAtom;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.ParenthesisSelect;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecification;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElement;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElements;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoExpression;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectSpecEnum;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SimpleSelect;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesisSelect;
// import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionSelect;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalTuples;
//
/// **
// * 关系表达式求值器: 关系, 元组.
// * <p>
// * 注意: 没有将求值下推到具体的构造中, 在这里统一处理.
// */
// @Deprecated
// public class RelationalExpressionEvaluator {
//
// /**
// * 求值: 查询语句.
// * @param context
// * @param selectStatement
// * @return
// */
// public static List<RelationalTuples> eval(RelationalEvaluationContext context,
// SelectStatement selectStatement) {
// if (selectStatement == null) {
// return Lists.newArrayList();
// }
//
// if (selectStatement instanceof SimpleSelect) {
// SimpleSelect simpleSelect = (SimpleSelect) selectStatement;
//
// QuerySpecification querySpecification = simpleSelect.querySpecification;
// // select specification: all, distinct, distinct row, ...
// List<SelectSpecEnum> selectSpecs = querySpecification.selectSpecs;
// boolean distinct = false;
// boolean distinctRow = false;
// for (SelectSpecEnum selectSpecEnum : selectSpecs) {
// if (SelectSpecEnum.DISTINCT.equals(selectSpecEnum)) {
// distinct = true;
// } else if (SelectSpecEnum.DISTINCTROW.equals(selectSpecEnum)) {
// distinctRow = true;
// }
// }
// // result columns
// SelectElements selectElements = querySpecification.selectElements;
// List<SelectElement> selectElementList = selectElements.selectElements;
// // result representation: variable or file
// SelectIntoExpression selectIntoExpression = querySpecification.selectIntoExpression;
// if (selectIntoExpression != null) {
// throw new UnsupportedOperationException();
// }
// FromClause fromClause = querySpecification.fromClause;
// if (fromClause != null) {
// // tables, filtering
// TableSources tableSources = fromClause.tableSources;
//
// // filtering
// Expression whereExpr = fromClause.whereExpr;
// if (whereExpr != null) {
//
// }
// // grouping, filtering
// List<GroupByItem> groupByItems = fromClause.groupByItems;
// if (groupByItems != null) {
// Boolean withRollup = fromClause.withRollup;
// if (Boolean.TRUE.equals(withRollup)) {
//
// }
// }
// Expression havingExpr = fromClause.havingExpr;
// if (havingExpr != null) {
//
// }
// }
// // ordering
// OrderByClause orderByClause = querySpecification.orderByClause;
// if (orderByClause != null) {
// List<OrderByExpression> orderByExpressions = orderByClause.orderByExpressions;
// }
// // limiting
// LimitClause limitClause = querySpecification.limitClause;
// if (limitClause != null) {
// LimitClauseAtom limit = limitClause.limit;
// LimitClauseAtom offset = limitClause.offset;
// }
//
// // locking
// LockClauseEnum lockClause = simpleSelect.lockClause;
// if (lockClause != null) {
// throw new UnsupportedOperationException();
// }
//
// throw new UnsupportedOperationException();
// } else if (selectStatement instanceof ParenthesisSelect) {
// throw new UnsupportedOperationException();
// } else if (selectStatement instanceof UnionSelect) {
// throw new UnsupportedOperationException();
// } else if (selectStatement instanceof UnionParenthesisSelect) {
// throw new UnsupportedOperationException();
// } else {
// return Lists.newArrayList();
// }
// }
//
// public static List<RelationalTuples> eval(RelationalEvaluationContext context,
// Expression expression) {
// if (expression == null) {
// throw new UnsupportedOperationException();
// }
//
// if (expression instanceof NotExpression) {
// // List<RelationalTuples> result = Lists.newArrayList(context.tuples);
// // NotExpression notExpression = (NotExpression) expression;
// // List<RelationalTuples> negativeResult = eval(context, notExpression.expression);
// // result.removeAll(negativeResult);
// // return result;
// throw new UnsupportedOperationException();
// } else if (expression instanceof LogicalExpression) {
// LogicalExpression logicalExpression = (LogicalExpression) expression;
// switch (logicalExpression.operator) {
// case AND1:
// case AND2:
// break;
// case XOR:
// break;
// case OR1:
// case OR2:
// break;
//
// default:
// break;
// }
//
// throw new UnsupportedOperationException();
// } else if (expression instanceof IsExpression) {
// throw new UnsupportedOperationException();
// } else if (expression instanceof PredicateExpression) {
// throw new UnsupportedOperationException();
// } else {
// return Lists.newArrayList();
// }
// }
//
// public static List<RelationalTuples> eval(RelationalEvaluationContext context,
// ExpressionAtom expressionAtom) {
// if (expressionAtom == null) {
// throw new UnsupportedOperationException();
// }
//
// if (expressionAtom instanceof Constant) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof FullColumnName) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof FunctionCall) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof Collate) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof MysqlVariable) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof UnaryExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof BinaryExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof NestedExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof NestedRowExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof ExistsExpessionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof SubqueryExpessionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof IntervalExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof BitExpressionAtom) {
// throw new UnsupportedOperationException();
// } else if (expressionAtom instanceof MathExpressionAtom) {
// throw new UnsupportedOperationException();
// } else {
// return Lists.newArrayList();
// }
//
// }
//
// }
