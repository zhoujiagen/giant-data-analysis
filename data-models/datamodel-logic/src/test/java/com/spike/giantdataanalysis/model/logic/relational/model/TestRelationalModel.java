package com.spike.giantdataanalysis.model.logic.relational.model;

import static com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeAttribute;
import static com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeKey;
import static com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory.makeRelation;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

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

    RelationalRelation movies =
        makeRelation("Movies", Lists.newArrayList(title, year, length, genre));

    makeKey(movies, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
      Lists.newArrayList(title, year));
    System.out.println(movies);
  }

}
