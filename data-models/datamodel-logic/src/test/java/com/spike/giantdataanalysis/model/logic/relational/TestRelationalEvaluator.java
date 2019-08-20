// package com.spike.giantdataanalysis.model.logic.relational;
//
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeAttribute;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeBasicExpression;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeCondition;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeDifference;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeIntersection;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeKey;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeProject;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeRelation;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeSelect;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeTuples;
// import static
// com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeUnion;
//
// import java.util.List;
//
// import org.junit.Assert;
// import org.junit.Test;
//
// import com.google.common.collect.Lists;
// import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
// import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
// import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
// import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.Expression.PredicateExpression;
// import com.spike.giantdataanalysis.model.logic.relational.expression.Literals;
// import
// com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalBasicExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalConditionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalDifferenceExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalIntersectionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalProjectExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalSelectExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.RelationalUnionExpression;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;
// import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalTuples;
//
/// **
// * examples from <strong>A First Course in Database Systems</strong>
// */
// public class TestRelationalEvaluator {
//
// private List<RelationalAttribute> makeTupleAttributes() {
// List<RelationalAttribute> attributes = Lists.newArrayList();
// attributes.add(makeAttribute("name", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("address", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("gender", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("birthdate", RelationalAttributeTypeEnum.VARCHAR, true));
// return attributes;
// }
//
// private List<List<Object>> makeTupleR() {
// List<List<Object>> valuesR = Lists.newArrayList();
// valuesR.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
// valuesR.add(Lists.newArrayList("Mark Hamill", "456 Oak Rd., Brentwood", "M", "8/8/88"));
// return valuesR;
// }
//
// private List<List<Object>> makeTupleS() {
// List<List<Object>> valuesS = Lists.newArrayList();
// valuesS.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
// valuesS.add(Lists.newArrayList("Harrison Ford", "789 Palm Dr., Beverly Hills", "M", "7/7/77"));
// return valuesS;
// }
//
// private RelationalTuples makeTupleMovie() {
// RelationalRelation relation = makeRelation("Movies");
// RelationalAttribute attribute_title =
// makeAttribute("title", RelationalAttributeTypeEnum.VARCHAR, true);
// RelationalAttribute attribute_year =
// makeAttribute("year", RelationalAttributeTypeEnum.INT24, true);
// relation.addAttribute(attribute_title);
// relation.addAttribute(attribute_year);
// relation.addAttribute(makeAttribute("length", RelationalAttributeTypeEnum.INT24, true));
// relation.addAttribute(makeAttribute("genre", RelationalAttributeTypeEnum.VARCHAR, true));
// relation.addAttribute(makeAttribute("studioName", RelationalAttributeTypeEnum.VARCHAR, true));
// relation.addAttribute(makeAttribute("producerC#", RelationalAttributeTypeEnum.INT24, true));
//
// makeKey(relation, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
// Lists.newArrayList(attribute_title, attribute_year));
//
// List<List<Object>> values = Lists.newArrayList();
// values.add(Lists.newArrayList("Star Wars", 1977, 124, "sciFi", "Fox", 12345));
// values.add(Lists.newArrayList("Galaxy Quest", 1999, 104, "comedy", "DreamWorks", 67890));
// values.add(Lists.newArrayList("Wayne's World", 1992, 95, "comedy", "Paramount", 99999));
//
// return makeTuples(relation, values);
// }
//
// @Test
// public void testEvalBasicExpression() {
// List<RelationalAttribute> attributes = Lists.newArrayList();
// attributes.add(makeAttribute("name", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("address", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("gender", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("birthdate", RelationalAttributeTypeEnum.VARCHAR, true));
//
// List<List<Object>> values = Lists.newArrayList();
// values.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
// values.add(Lists.newArrayList("Mark Hamill", "456 Oak Rd., Brentwood", "M", "8/8/88"));
//
// RelationalTuples tuples = makeTuples(attributes, values);
//
// RelationalBasicExpression expression = makeBasicExpression(tuples);
//
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertTrue(evalResult.getValuesList().size() == 2);
// }
//
// @Test
// public void testEvalIntersectionExpression() {
// RelationalTuples tuplesR = this.makeTupleR();
// RelationalTuples tuplesS = this.makeTupleS();
//
// RelationalBasicExpression expressionR = makeBasicExpression(tuplesR);
// RelationalBasicExpression expressionS = makeBasicExpression(tuplesS);
//
// RelationalIntersectionExpression expression = makeIntersection(expressionR, expressionS);
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertTrue(evalResult.getValuesList().size() == 2); // bag
// }
//
// @Test
// public void testEvalUnionExpression() {
// RelationalTuples tuplesR = this.makeTupleR();
// RelationalTuples tuplesS = this.makeTupleS();
//
// RelationalBasicExpression expressionR = makeBasicExpression(tuplesR);
// RelationalBasicExpression expressionS = makeBasicExpression(tuplesS);
//
// RelationalUnionExpression expression = makeUnion(expressionR, expressionS);
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertTrue(evalResult.getValuesList().size() == 4); // bag
// }
//
// @Test
// public void testEvalDifferenceExpression() {
// RelationalTuples tuplesR = this.makeTupleR();
// RelationalTuples tuplesS = this.makeTupleS();
//
// RelationalBasicExpression expressionR = makeBasicExpression(tuplesR);
// RelationalBasicExpression expressionS = makeBasicExpression(tuplesS);
//
// RelationalDifferenceExpression expression = makeDifference(expressionR, expressionS);
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertTrue(evalResult.getValuesList().size() == 1);
// }
//
// @Test
// public void testEvalProjectExpression() {
// RelationalTuples tuples = this.makeTupleMovie();
// RelationalBasicExpression movies = makeBasicExpression(tuples);
//
// List<RelationalAttribute> attributes = Lists.newArrayList();
// attributes.add(makeAttribute("title", RelationalAttributeTypeEnum.VARCHAR, true));
// attributes.add(makeAttribute("year", RelationalAttributeTypeEnum.INT24, true));
// attributes.add(makeAttribute("length", RelationalAttributeTypeEnum.INT24, true));
//
// RelationalProjectExpression expression = makeProject(movies, attributes);
// System.out.println(expression.literal());
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertEquals(tuples.getValuesList().size(), evalResult.getValuesList().size());
// }
//
// @Test
// public void testEvalSelectExpression() {
// RelationalTuples tuples = this.makeTupleMovie();
// RelationalBasicExpression movies = makeBasicExpression(tuples);
//
// RelationalConditionExpression conditionExpression =
// makeCondition(this.makeMovieLengthGE100Expression());
// RelationalSelectExpression expression = makeSelect(movies, conditionExpression);
// System.out.println(expression.literal());
//
// RelationalTuples evalResult = RelationalEvaluator.eval(expression);
// System.out.println(evalResult);
// Assert.assertTrue(evalResult.getValuesList().size() == 2);
// }
//
// // MOVIES.LENGTH >= 100
// private Expression makeMovieLengthGE100Expression() {
// PredicateExpression left = RelationalAlgebraExpressionFactory.makeExpressionAtomPredicate(//
// null, //
// RelationalAlgebraExpressionFactory.makeFullColumnName(//
// RelationalAlgebraExpressionFactory.makeUid(Uid.Type.SIMPLE_ID, "Movies"), //
// Lists.newArrayList(RelationalAlgebraExpressionFactory.makeDottedId(".length", null))));
// RelationalComparisonOperatorEnum comparisonOperator = RelationalComparisonOperatorEnum.GE;
// PredicateExpression right = RelationalAlgebraExpressionFactory.makeExpressionAtomPredicate(//
// null, //
// RelationalAlgebraExpressionFactory.makeConstant(//
// Literals.Constant.Type.DECIMAL_LITERAL, //
// "100", null));
// Expression condition = RelationalAlgebraExpressionFactory.makeBinaryComparasionPredicate(//
// left, comparisonOperator, right);
// return condition;
// }
//
// }
