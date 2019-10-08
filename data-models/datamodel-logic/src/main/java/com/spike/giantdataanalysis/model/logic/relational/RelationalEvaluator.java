// package com.spike.giantdataanalysis.model.logic.relational;
//
// import java.math.BigDecimal;
// import java.math.BigInteger;
// import java.util.Collections;
// import java.util.Date;
// import java.util.HashSet;
// import java.util.List;
//
// import org.apache.commons.collections4.CollectionUtils;
// import org.apache.commons.lang3.tuple.Triple;
//
// import com.google.common.base.Preconditions;
// import com.google.common.collect.Lists;
// import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAggregateExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalBasicExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalBlobAttributeValue;
// import
// com.spike.giantdataanalysis.model.logic.relational.model.RelationalCartesianProductExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalConditionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalDifferenceExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalIntersectionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalJoinExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalNaturalJoinExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalProjectExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalRenameExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalSelectExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalThetaJoinExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalUnionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalTuple;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalTuples;
//
/// **
// * 关系代数求值器.
// */
// @Deprecated
// public final class RelationalEvaluator {
//
// public static RelationalTuples eval(RelationalExpression expression) {
// // Basic
// if (expression instanceof RelationalBasicExpression) {
// return ((RelationalBasicExpression) expression).tuples();
// }
// // Aggregate
// else if (expression instanceof RelationalAggregateExpression) {
// throw RelationalEvaluationError.make(expression);
// }
// // Cartesian
// else if (expression instanceof RelationalCartesianProductExpression) {
// throw RelationalEvaluationError.make(expression);
// }
// // Difference
// else if (expression instanceof RelationalDifferenceExpression) {
// RelationalDifferenceExpression actualExpression = (RelationalDifferenceExpression) expression;
// RelationalTuples firstTuples = eval(actualExpression.first());
// RelationalTuples secondTuples = eval(actualExpression.second());
// return evalDifference(firstTuples, secondTuples);
// }
// // Intersection
// else if (expression instanceof RelationalIntersectionExpression) {
// RelationalIntersectionExpression actualExpression =
// (RelationalIntersectionExpression) expression;
// RelationalTuples firstTuples = eval(actualExpression.first());
// RelationalTuples secondTuples = eval(actualExpression.second());
// return evalIntersection(firstTuples, secondTuples);
// }
// // Union
// else if (expression instanceof RelationalUnionExpression) {
// RelationalUnionExpression actualExpression = (RelationalUnionExpression) expression;
// RelationalTuples firstTuples = eval(actualExpression.first());
// RelationalTuples secondTuples = eval(actualExpression.second());
// return evalUnion(firstTuples, secondTuples);
// }
// // Join
// else if (expression instanceof RelationalJoinExpression) {
// if (expression instanceof RelationalNaturalJoinExpression) {
// throw RelationalEvaluationError.make(expression);
// } else if (expression instanceof RelationalThetaJoinExpression) {
// throw RelationalEvaluationError.make(expression);
// } else {
// throw RelationalEvaluationError.make(expression);
// }
// }
// // Rename
// else if (expression instanceof RelationalRenameExpression) {
// throw RelationalEvaluationError.make(expression);
// }
// // Condition
// else if (expression instanceof RelationalConditionExpression) {
// throw RelationalEvaluationError.make(expression);
// }
// // Selection
// else if (expression instanceof RelationalSelectExpression) {
// RelationalSelectExpression actualExpression = (RelationalSelectExpression) expression;
// return evalSelect(eval(actualExpression.expression), actualExpression.condition);
// }
// // Project
// else if (expression instanceof RelationalProjectExpression) {
// RelationalProjectExpression actualExpression = (RelationalProjectExpression) expression;
// return evalProject(eval(actualExpression.expression), actualExpression.attributes);
// }
// // Others
// else {
// throw RelationalEvaluationError.make(expression);
// }
// }
//
// // ---------------------------------------------------------------------------
// // 求值.
// // ---------------------------------------------------------------------------
//
// /**
// * 求值: 交.
// * @param first
// * @param second
// * @return
// */
// public static RelationalTuples evalIntersection(RelationalTuples first, RelationalTuples second)
// {
// if (first == null || second == null) {
// throw RelationalEvaluationError.make("evalIntersection");
// }
//
// if (!equals(first.getAttributes(), second.getAttributes())) {
// return RelationalTuples.EMPTY;
// }
//
// if (CollectionUtils.isEmpty(first.lazyValuesList())) {
// if (CollectionUtils.isEmpty(second.lazyValuesList())) {
// return RelationalTuples.EMPTY;
// } else {
// return RelationalModelFactory.makeTuples(first.getAttributes(), second.getValuesList());
// }
// } else {
// if (CollectionUtils.isEmpty(second.getValuesList())) {
// return RelationalModelFactory.makeTuples(first.getAttributes(), first.getValuesList());
// }
// }
//
// final int firstSize = first.getValuesList().size();
// final int secondSize = second.getValuesList().size();
// boolean[] foundsFirst = new boolean[firstSize];
// boolean[] foundsSecond = new boolean[secondSize];
//
// for (int i = 0; i < firstSize; i++) {
// for (int j = 0; j < secondSize; j++) {
// if (equals(first.getAttributes(), first.getValuesList().get(i),
// second.getValuesList().get(j))) {
// foundsFirst[i] = true;
// foundsSecond[j] = true;
// }
// }
// }
//
// List<List<Object>> values = Lists.newArrayList();
//
// for (int i = 0; i < firstSize; i++) {
// if (foundsFirst[i]) {
// values.add(first.getValuesList().get(i));
// }
// }
// for (int j = 0; j < secondSize; j++) {
// if (foundsSecond[j]) {
// values.add(second.getValuesList().get(j));
// }
// }
//
// return RelationalModelFactory.makeTuples(first.getAttributes(), values);
// }
//
// /**
// * 求值: 并.
// * @param first
// * @param second
// * @return
// */
// public static RelationalTuples evalUnion(RelationalTuples first, RelationalTuples second) {
// if (first == null || second == null) {
// return RelationalTuples.EMPTY;
// }
//
// if (!equals(first.getAttributes(), second.getAttributes())) {
// return RelationalTuples.EMPTY;
// }
//
// if (CollectionUtils.isEmpty(first.getValuesList())) {
// if (CollectionUtils.isEmpty(second.getValuesList())) {
// return RelationalTuples.EMPTY;
// } else {
// return RelationalModelFactory.makeTuples(first.getAttributes(), second.getValuesList());
// }
// } else {
// if (CollectionUtils.isEmpty(second.getValuesList())) {
// return RelationalModelFactory.makeTuples(first.getAttributes(), first.getValuesList());
// }
// }
//
// // bag semantics
// List<List<Object>> values = Lists.newArrayList();
// values.addAll(first.getValuesList());
// values.addAll(second.getValuesList());
//
// return RelationalModelFactory.makeTuples(first.getAttributes(), values);
// }
//
// /**
// * 求值: 差.
// * @param first
// * @param second
// * @return
// */
// public static RelationalTuples evalDifference(RelationalTuples first, RelationalTuples second) {
// if (first == null || second == null) {
// return RelationalTuples.EMPTY;
// }
//
// if (!equals(first.getAttributes(), second.getAttributes())) {
// return RelationalTuples.EMPTY;
// }
//
// if (!equals(first.getAttributes(), second.getAttributes())) {
// return RelationalTuples.EMPTY;
// }
//
// if (CollectionUtils.isEmpty(first.getValuesList())) {
// return RelationalTuples.EMPTY;
// } else {
// if (CollectionUtils.isEmpty(second.getValuesList())) {
// return RelationalModelFactory.makeTuples(first.getAttributes(), first.getValuesList());
// }
// }
//
// final int firstSize = first.getValuesList().size();
// final int secondSize = second.getValuesList().size();
// boolean[] foundsFirst = new boolean[firstSize];
//
// for (int i = 0; i < firstSize; i++) {
// for (int j = 0; j < secondSize; j++) {
// if (equals(first.getAttributes(), first.getValuesList().get(i),
// second.getValuesList().get(j))) {
// foundsFirst[i] = true;
// }
// }
// }
//
// List<List<Object>> values = Lists.newArrayList();
//
// for (int i = 0; i < firstSize; i++) {
// if (!foundsFirst[i]) {
// values.add(first.getValuesList().get(i));
// }
// }
//
// return RelationalModelFactory.makeTuples(first.getAttributes(), values);
// }
//
// /**
// * 求值: 投影.
// * @param first
// * @param attributes
// * @return
// */
// public static RelationalTuples evalProject(RelationalTuples first,
// List<RelationalAttribute> attributes) {
// return project(first, attributes);
//
// }
//
// public static RelationalTuple project(RelationalTuple relationalTuple,
// List<RelationalAttribute> attributes) {
// Preconditions.checkArgument(relationalTuple != null);
// Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));
//
// List<Integer> attributeIndexList = Lists.newArrayList();
// List<Object> values = Lists.newArrayList();
//
// boolean found = false;
// for (RelationalAttribute attribute : attributes) {
// found = false;
// for (int i = 0, size = relationalTuple.getAttributes().size(); i < size; i++) {
// // 按名称比较
// if (relationalTuple.getAttributes().get(i).getName().equals(attribute.getName())) {
// attributeIndexList.add(i);
// found = true;
// break;
// }
// }
// Preconditions.checkArgument(found);
// }
//
// for (Integer attributeIndex : attributeIndexList) {
// values.add(relationalTuple.getValues().get(attributeIndex));
// }
//
// return RelationalModelFactory.makeTuple(attributes, values);
// }
//
// public static RelationalTuples project(RelationalTuples relationalTuples,
// List<RelationalAttribute> attributes) {
// Preconditions.checkArgument(relationalTuples != null);
// Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));
//
// List<List<Object>> resultvaluesList = Lists.newArrayList();
//
// List<Integer> attributeIndexList = Lists.newArrayList();
//
// boolean found = false;
// for (RelationalAttribute attribute : attributes) {
// found = false;
// for (int i = 0, size = relationalTuples.getAttributes().size(); i < size; i++) {
// // 按名称比较
// if (relationalTuples.getAttributes().get(i).getName().equals(attribute.getName())) {
// attributeIndexList.add(i);
// found = true;
// break;
// }
// }
// Preconditions.checkArgument(found);
// }
//
// for (List<Object> values : relationalTuples.lazyValuesList()) {
// List<Object> resultValues = Lists.newArrayList();
// for (Integer attributeIndex : attributeIndexList) {
// resultValues.add(values.get(attributeIndex));
// }
// resultvaluesList.add(resultValues);
// }
//
// return RelationalModelFactory.makeTuples(attributes, resultvaluesList);
// }
//
// /**
// * 求值: 选择.
// * @param tuples
// * @param condition
// * @return
// */
// public static RelationalTuples evalSelect(RelationalTuples tuples,
// RelationalConditionExpression condition) {
// if (tuples == null || condition == null) {
// throw RelationalEvaluationError.make("evalSelect");
// }
//
// RelationalRelation selectResultRelation = RelationalModelFactory.makeRelation(
// RelationalRelation.TEMPORARY_NAME_PREFIX + tuples.getRelation().getName(),
// tuples.getAttributes());
//
// List<List<Object>> resultValuesList = tuples.lazyValuesList();
// for (List<Object> values : tuples.getValuesList()) {
// if (evalCondition(tuples.getAttributes(), values, condition)) {
// resultValuesList.add(values);
// }
// }
// return RelationalModelFactory.makeTuples(selectResultRelation, resultValuesList);
// }
//
// public static boolean evalCondition(List<RelationalAttribute> attributes, List<Object> values,
// RelationalConditionExpression condition) {
// if (CollectionUtils.isEmpty(attributes) || CollectionUtils.isEmpty(values)
// || condition == null) {
// throw RelationalEvaluationError.make("evalCondition");
// }
//
// Expression expression = condition.condition;
// // TODO(zhoujiagen) translate parse result constructs to model constructs
// throw new UnsupportedOperationException();
// }
//
//
// }
