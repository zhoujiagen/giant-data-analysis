package com.spike.giantdataanalysis.model.logic.relational.model;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;

/**
 * 属性
 */
public class RelationalAttribute implements Comparable<RelationalAttribute> {
  protected final String name;
  protected final RelationalAttributeTypeEnum dataType;
  protected final boolean nullable;

  protected RelationalRelation relation;

  RelationalAttribute(String name, RelationalAttributeTypeEnum dataType, boolean nullable) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(dataType != null);

    this.name = name;
    this.dataType = dataType;
    this.nullable = nullable;
  }

  public RelationalAttribute copy() {
    return new RelationalAttribute(name, dataType, nullable);
  }

  public RelationalAttribute copy(String alias) {
    Preconditions.checkArgument(StringUtils.isNotBlank(alias));

    return new RelationalAttribute(alias, dataType, nullable);
  }

  public String name() {
    return name;
  }

  public RelationalAttributeTypeEnum dataType() {
    return dataType;
  }

  public boolean nullable() {
    return nullable;
  }

  public RelationalRelation relation() {
    return relation;
  }

  public void setRelation(RelationalRelation relation) {
    this.relation = relation;
  }

  @Override
  public int compareTo(RelationalAttribute o) {
    if (o == null) {
      return 1;
    } else {
      return name().compareTo(o.name());
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (relation != null) {
      builder.append(relation.name());
      builder.append(".");
    }
    builder.append(name());
    builder.append("(");
    builder.append(dataType().name());
    builder.append(",");
    if (nullable()) {
      builder.append(" NULL");
    } else {
      builder.append(" NOT NULL");
    }
    builder.append(")");
    return builder.toString();
  }

}