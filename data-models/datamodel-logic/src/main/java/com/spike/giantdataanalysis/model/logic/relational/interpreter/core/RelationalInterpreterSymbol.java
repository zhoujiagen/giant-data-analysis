package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 符号.
 */
public class RelationalInterpreterSymbol implements Comparable<RelationalInterpreterSymbol> {

  // 名称
  public final String text;
  // 符号附带信息
  public Object object;
  // 可以被重新赋值
  public RelationalInterpreterSymbolTypeEnum type;

  public RelationalInterpreterSymbol(String text) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    this.type = null;
    this.text = text;
    this.object = null;
  }

  public RelationalInterpreterSymbol(RelationalInterpreterSymbolTypeEnum type, String text) {
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(text != null);

    this.type = type;
    this.text = text;
    this.object = null;
  }

  public RelationalInterpreterSymbol(RelationalInterpreterSymbolTypeEnum type, String text,
      Object object) {
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(text != null);

    this.type = type;
    this.text = text;
    this.object = object;
  }

  @Override
  public int compareTo(RelationalInterpreterSymbol o) {
    if (o == null) {
      return 1;
    } else {
      return this.text.compareTo(o.text);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(text).append(": ").append(type);
    return builder.toString();
  }

}