package com.spike.giantdataanalysis.model.logic.relational.model;

import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeAttribute;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeKey;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeRelation;
import static com.spike.giantdataanalysis.model.logic.relational.model.RelationalModelFactory.makeTuple;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;

/**
 * 
 */
public class TestRelationalModel {

  @Test
  public void construct() {
    RelationalAttribute title = makeAttribute("title", RelationalAttributeTypeEnum.STRING, false);
    RelationalAttribute year = makeAttribute("year", RelationalAttributeTypeEnum.INT24, false);
    RelationalAttribute length = makeAttribute("length", RelationalAttributeTypeEnum.INT24, false);
    RelationalAttribute genre = makeAttribute("genre", RelationalAttributeTypeEnum.STRING, false);

    List<RelationalAttribute> attributes = Lists.newArrayList(title, year, length, genre);
    List<RelationalRelationKey> keys = Lists.newArrayList();
    keys.add(
      makeKey(RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY", Lists.newArrayList(title, year)));
    RelationalRelation movies = makeRelation("Movies", attributes, keys);
    System.out.println(movies);

    RelationalTuple tuple1 =
        makeTuple(movies.attributes(), Lists.newArrayList("Star Wars", 1977, 124, "sciFi"));
    System.out.println(tuple1);
  }

  @Test
  public void relationAsTuples() {
    RelationalAttribute title = makeAttribute("title", RelationalAttributeTypeEnum.STRING, false);
    RelationalAttribute year = makeAttribute("year", RelationalAttributeTypeEnum.INT24, false);
    RelationalAttribute length = makeAttribute("length", RelationalAttributeTypeEnum.INT24, false);
    RelationalAttribute genre = makeAttribute("genre", RelationalAttributeTypeEnum.STRING, false);

    List<RelationalAttribute> attributes = Lists.newArrayList(title, year, length, genre);
    List<RelationalRelationKey> keys = Lists.newArrayList();
    keys.add(
      makeKey(RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY", Lists.newArrayList(title, year)));
    RelationalRelation movies = makeRelation("Movies", attributes, keys);

    @SuppressWarnings("unchecked")
    List<List<Object>> values =
        Lists.<List<Object>> newArrayList(Lists.newArrayList("Star Wars", 1977, 124, "sciFi"));
    System.out.println(movies.asTuples(values));
  }

}
