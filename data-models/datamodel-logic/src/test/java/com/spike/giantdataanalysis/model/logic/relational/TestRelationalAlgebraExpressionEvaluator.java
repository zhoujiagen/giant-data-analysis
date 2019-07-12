package com.spike.giantdataanalysis.model.logic.relational;

import static com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.makeBasicExpression;
import static com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.makeDifference;
import static com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.makeIntersection;
import static com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.makeProject;
import static com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.makeUnion;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeAttribute;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeKey;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeRelation;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeTuples;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraBasicExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraDifferenceExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraIntersectionExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraProjectExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraUnionExpression;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalRelation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalRelationKey;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * examples from <strong>A First Course in Database Systems</strong>
 */
public class TestRelationalAlgebraExpressionEvaluator {

  @Test
  public void testEvalBasicExpression() {

    List<RelationalAttribute> attributes = Lists.newArrayList();
    attributes.add(makeAttribute("name", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("address", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("gender", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("birthdate", RelationalAttributeTypeEnum.VARCHAR, true));

    List<List<Object>> values = Lists.newArrayList();
    values.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
    values.add(Lists.newArrayList("Mark Hamill", "456 Oak Rd., Brentwood", "M", "8/8/88"));
    RelationalTuples tuples = makeTuples(attributes, values);

    RelationalAlgebraBasicExpression expression = makeBasicExpression(tuples);

    RelationalTuples evalResult = RelationalAlgebraExpressionEvaluator.eval(expression);
    System.out.println(evalResult);
  }

  private List<RelationalAttribute> makeTupleAttributes() {
    List<RelationalAttribute> attributes = Lists.newArrayList();
    attributes.add(makeAttribute("name", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("address", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("gender", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("birthdate", RelationalAttributeTypeEnum.VARCHAR, true));
    return attributes;
  }

  private RelationalTuples makeTupleR() {
    List<RelationalAttribute> attributes = this.makeTupleAttributes();

    List<List<Object>> valuesR = Lists.newArrayList();
    valuesR.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
    valuesR.add(Lists.newArrayList("Mark Hamill", "456 Oak Rd., Brentwood", "M", "8/8/88"));
    return makeTuples(attributes, valuesR);
  }

  private RelationalTuples makeTupleS() {
    List<RelationalAttribute> attributes = this.makeTupleAttributes();

    List<List<Object>> valuesS = Lists.newArrayList();
    valuesS.add(Lists.newArrayList("Carrier Fisher", "123 Maple St., Hollywood", "F", "9/9/99"));
    valuesS.add(Lists.newArrayList("Harrison Ford", "789 Palm Dr., Beverly Hills", "M", "7/7/77"));
    return makeTuples(attributes, valuesS);
  }

  private RelationalTuples makeTupleMovie() {
    RelationalRelation relation = makeRelation("Movies");
    RelationalAttribute attribute_title =
        makeAttribute("title", RelationalAttributeTypeEnum.VARCHAR, true);
    RelationalAttribute attribute_year =
        makeAttribute("year", RelationalAttributeTypeEnum.INT24, true);
    relation.addAttribute(attribute_title);
    relation.addAttribute(attribute_year);
    relation.addAttribute(makeAttribute("length", RelationalAttributeTypeEnum.INT24, true));
    relation.addAttribute(makeAttribute("genre", RelationalAttributeTypeEnum.VARCHAR, true));
    relation.addAttribute(makeAttribute("studioName", RelationalAttributeTypeEnum.VARCHAR, true));
    relation.addAttribute(makeAttribute("producerC#", RelationalAttributeTypeEnum.INT24, true));

    RelationalRelationKey key = makeKey(RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
      Lists.newArrayList(attribute_title, attribute_year));
    relation.addKey(key);

    List<List<Object>> values = Lists.newArrayList();
    values.add(Lists.newArrayList("Star Wars", 1977, 124, "sciFi", "Fox", 12345));
    values.add(Lists.newArrayList("Galaxy Quest", 1999, 104, "comedy", "DreamWorks", 67890));
    values.add(Lists.newArrayList("Wayne's World", 1992, 95, "comedy", "Paramount", 99999));

    return relation.asTuples(values);
  }

  @Test
  public void testEvalIntersectionExpression() {
    RelationalTuples tuplesR = this.makeTupleR();
    RelationalTuples tuplesS = this.makeTupleS();

    RelationalAlgebraBasicExpression expressionR = makeBasicExpression(tuplesR);
    RelationalAlgebraBasicExpression expressionS = makeBasicExpression(tuplesS);

    RelationalAlgebraIntersectionExpression expression = makeIntersection(expressionR, expressionS);
    RelationalTuples evalResult = RelationalAlgebraExpressionEvaluator.eval(expression);
    System.out.println(evalResult);
  }

  @Test
  public void testEvalUnionExpression() {
    RelationalTuples tuplesR = this.makeTupleR();
    RelationalTuples tuplesS = this.makeTupleS();

    RelationalAlgebraBasicExpression expressionR = makeBasicExpression(tuplesR);
    RelationalAlgebraBasicExpression expressionS = makeBasicExpression(tuplesS);

    RelationalAlgebraUnionExpression expression = makeUnion(expressionR, expressionS);
    RelationalTuples evalResult = RelationalAlgebraExpressionEvaluator.eval(expression);
    System.out.println(evalResult);
  }

  @Test
  public void testEvalDifferenceExpression() {
    RelationalTuples tuplesR = this.makeTupleR();
    RelationalTuples tuplesS = this.makeTupleS();

    RelationalAlgebraBasicExpression expressionR = makeBasicExpression(tuplesR);
    RelationalAlgebraBasicExpression expressionS = makeBasicExpression(tuplesS);

    RelationalAlgebraDifferenceExpression expression = makeDifference(expressionR, expressionS);
    RelationalTuples evalResult = RelationalAlgebraExpressionEvaluator.eval(expression);
    System.out.println(evalResult);
  }

  @Test
  public void testEvalProjectExpression() {
    RelationalTuples tuples = this.makeTupleMovie();
    RelationalAlgebraBasicExpression movies = makeBasicExpression(tuples);

    List<RelationalAttribute> attributes = Lists.newArrayList();
    attributes.add(makeAttribute("title", RelationalAttributeTypeEnum.VARCHAR, true));
    attributes.add(makeAttribute("year", RelationalAttributeTypeEnum.INT24, true));
    attributes.add(makeAttribute("length", RelationalAttributeTypeEnum.INT24, true));

    RelationalAlgebraProjectExpression expression = makeProject(movies, attributes);
    RelationalTuples evalResult = RelationalAlgebraExpressionEvaluator.eval(expression);
    System.out.println(evalResult);
  }

}
