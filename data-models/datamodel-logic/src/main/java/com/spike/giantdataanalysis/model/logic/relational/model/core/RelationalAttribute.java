package com.spike.giantdataanalysis.model.logic.relational.model.core;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;

/**
 * 属性
 */
public class RelationalAttribute implements Literal, Comparable<RelationalAttribute> {

  public static final RelationalAttribute EMPTY =
      new RelationalAttribute("EMPTY", RelationalAttributeTypeEnum.NULL, true);

  public static final String NAME_SEP = ".";

  // 属性名称
  public final String name;
  // 属性类型
  public final RelationalAttributeTypeEnum dataType;
  // 属性是否可空
  public final boolean nullable;

  RelationalAttribute(String name, RelationalAttributeTypeEnum dataType, boolean nullable) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(dataType != null);

    this.name = name;
    this.dataType = dataType;
    this.nullable = nullable;
  }

  /** 拷贝: 同名. */
  public RelationalAttribute copy() {
    return new RelationalAttribute(name, dataType, nullable);
  }

  /** 拷贝: 别名. */
  public RelationalAttribute copy(String alias) {
    Preconditions.checkArgument(StringUtils.isNotBlank(alias));

    return new RelationalAttribute(alias, dataType, nullable);
  }

  @Override
  public int compareTo(RelationalAttribute o) {
    if (o == null) {
      return 1;
    } else {
      if (name.compareTo(o.name) != 0) {
        return name.compareTo(o.name);
      } else {
        return dataType.id - o.dataType.id;
      }
    }
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append("(");
    sb.append(dataType.name());
    sb.append(")");
    return sb.toString();

  }

  @Override
  public String toString() {
    return this.literal();
  }

}