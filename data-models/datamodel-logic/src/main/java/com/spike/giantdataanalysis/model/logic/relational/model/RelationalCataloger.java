package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 关系定义Catalog.
 */
public interface RelationalCataloger {

  public static class DummyRelationalCataloger implements RelationalCataloger {
    public static Map<String, RelationalRelation> relationMap = Maps.newHashMap();

    static {
      // Movies
      RelationalAttribute Movies_title = RelationalModelFactory.makeAttribute("title",
        RelationalAttributeTypeEnum.VARCHAR, 100, false);
      RelationalAttribute Movies_year =
          RelationalModelFactory.makeAttribute("year", RelationalAttributeTypeEnum.INT, 11, false);
      RelationalAttribute Movies_length =
          RelationalModelFactory.makeAttribute("length", RelationalAttributeTypeEnum.INT, 11, true);
      RelationalAttribute Movies_genre = RelationalModelFactory.makeAttribute("genre",
        RelationalAttributeTypeEnum.VARCHAR, 10, true);
      RelationalAttribute Movies_studioName = RelationalModelFactory.makeAttribute("studioName",
        RelationalAttributeTypeEnum.VARCHAR, 30, true);
      RelationalAttribute Movies_producerC = RelationalModelFactory.makeAttribute("producerC#",
        RelationalAttributeTypeEnum.INT, 11, true);
      RelationalRelation Movies = RelationalModelFactory.makeRelation("Movies", Lists.newArrayList(//
        Movies_title, Movies_year, Movies_length, Movies_genre, Movies_studioName,
        Movies_producerC));
      Movies.addKeys(Lists.newArrayList(//
        RelationalModelFactory.makeKey(Movies, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
          Lists.newArrayList(Movies_title, Movies_year))));
      relationMap.put("Movies", Movies);

      // MovieStar
      RelationalAttribute MovieStar_name = RelationalModelFactory.makeAttribute("name",
        RelationalAttributeTypeEnum.VARCHAR, 30, false);
      RelationalAttribute MovieStar_address = RelationalModelFactory.makeAttribute("address",
        RelationalAttributeTypeEnum.VARCHAR, 255, true);
      RelationalAttribute MovieStar_gender =
          RelationalModelFactory.makeAttribute("gender", RelationalAttributeTypeEnum.CHAR, 1, true);
      RelationalAttribute MovieStar_birthdate = RelationalModelFactory.makeAttribute("birthdate",
        RelationalAttributeTypeEnum.DATE, null, true);
      RelationalRelation MovieStar =
          RelationalModelFactory.makeRelation("MovieStar", Lists.newArrayList(//
            MovieStar_name, MovieStar_address, MovieStar_gender, MovieStar_birthdate));
      MovieStar.addKeys(Lists.newArrayList(//
        RelationalModelFactory.makeKey(MovieStar, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
          Lists.newArrayList(MovieStar_name))));
      relationMap.put("MovieStar", MovieStar);

      // StarsIn
      RelationalAttribute StarsIn_movieTitle = RelationalModelFactory.makeAttribute("movieTitle",
        RelationalAttributeTypeEnum.VARCHAR, 100, false);
      RelationalAttribute StarsIn_movieYear = RelationalModelFactory.makeAttribute("movieYear",
        RelationalAttributeTypeEnum.INT, 11, false);
      RelationalAttribute StarsIn_starName = RelationalModelFactory.makeAttribute("starName",
        RelationalAttributeTypeEnum.VARCHAR, 30, false);
      RelationalRelation StarsIn =
          RelationalModelFactory.makeRelation("StarsIn", Lists.newArrayList(//
            StarsIn_movieTitle, StarsIn_movieYear, StarsIn_starName));
      StarsIn.addKeys(Lists.newArrayList(//
        RelationalModelFactory.makeKey(StarsIn, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
          Lists.newArrayList(StarsIn_movieTitle, StarsIn_movieYear, StarsIn_starName))));
      relationMap.put("StarsIn", StarsIn);

      // MovieExec
      RelationalAttribute MovieExec_name = RelationalModelFactory.makeAttribute("name",
        RelationalAttributeTypeEnum.VARCHAR, 100, false);
      RelationalAttribute MovieExec_address = RelationalModelFactory.makeAttribute("address",
        RelationalAttributeTypeEnum.VARCHAR, 255, true);
      RelationalAttribute MovieExec_cert =
          RelationalModelFactory.makeAttribute("cert#", RelationalAttributeTypeEnum.INT, 11, true);
      RelationalAttribute MovieExec_netWorth = RelationalModelFactory.makeAttribute("netWorth",
        RelationalAttributeTypeEnum.INT, 11, true);
      RelationalRelation MovieExec =
          RelationalModelFactory.makeRelation("MovieExec", Lists.newArrayList(//
            MovieExec_name, MovieExec_address, MovieExec_cert, MovieExec_netWorth));
      MovieExec.addKeys(Lists.newArrayList(//
        RelationalModelFactory.makeKey(MovieExec, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
          Lists.newArrayList(MovieExec_cert))));
      relationMap.put("MovieExec", MovieExec);

      // Studio
      RelationalAttribute Studioc_name = RelationalModelFactory.makeAttribute("name",
        RelationalAttributeTypeEnum.VARCHAR, 30, false);
      RelationalAttribute Studioc_address = RelationalModelFactory.makeAttribute("address",
        RelationalAttributeTypeEnum.VARCHAR, 255, true);
      RelationalAttribute Studioc_presC =
          RelationalModelFactory.makeAttribute("presC#", RelationalAttributeTypeEnum.INT, 11, true);
      RelationalRelation Studio = RelationalModelFactory.makeRelation("Studio", Lists.newArrayList(//
        Studioc_name, Studioc_address, Studioc_presC));
      Studio.addKeys(Lists.newArrayList(//
        RelationalModelFactory.makeKey(Studio, RelationalRelationKeyTypeEnum.PRIMARY, "PRIMARY",
          Lists.newArrayList(Studioc_name))));
      relationMap.put("Studio", Studio);
    }

    public RelationalRelation relation(String relationName) {
      return relationMap.get(relationName);
    }

    public RelationalAttribute attribute(String relationName, String attributeName) {
      RelationalRelation relation = this.relation(relationName);

      if (relation != null) {
        for (RelationalAttribute attribute : relation.attributes) {
          if (attribute.name.equals(attributeName)) {
            return attribute;
          }
        }
      }

      return null;
    }

    // TODO(zhoujiagen) hard coded here, may introduce context information
    public Pair<RelationalRelation, RelationalAttribute> guessAttribute(String attributeName) {
      for (String relationName : relationMap.keySet()) {
        RelationalRelation relation = relationMap.get(relationName);
        for (RelationalAttribute attribute : relation.attributes) {
          if (attribute.name.equals(attributeName)) {
            return Pair.of(relation, attribute);
          }
        }
      }
      return null;
    }

  }

}
