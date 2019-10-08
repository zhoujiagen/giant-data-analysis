package com.spike.giantdataanalysis.model.logic.relational.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;

/**
 * 字面量展示标记接口.
 */
public interface Literal {
  String literal();

  public static abstract class MoreLiterals {

    public static String literalAttributes(List<RelationalAttribute> attributes) {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isNotEmpty(attributes)) {
        List<String> literals = Lists.newArrayList();
        for (RelationalAttribute attribute : attributes) {
          literals.add(attribute.name);
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }

    public static String literalValuesList(List<List<Object>> valuesList) {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isNotEmpty(valuesList)) {
        for (List<Object> values : valuesList) {
          sb.append(literalValues(values));
        }
      }
      return sb.toString();
    }

    public static String literalValues(List<Object> values) {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isNotEmpty(values)) {
        List<String> literals = Lists.newArrayList();
        for (Object value : values) {
          if (value != null) {
            literals.add(value.toString());
          } else {
            literals.add("NULL");
          }
        }
        sb.append(Joiner.on(" | ").join(literals));
        sb.append(System.lineSeparator());
      }
      return sb.toString();
    }
  }

}
