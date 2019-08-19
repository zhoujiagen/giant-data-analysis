package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 符号.
 */
public class RESymbol implements Comparable<RESymbol> {

  public final String text;
  // 可以被重新赋值
  public RESymbolTypeEnum type;

  public RESymbol(String text) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    this.text = text;
    this.type = null;
  }

  public RESymbol(RESymbolTypeEnum type, String text) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    this.type = type;
    this.text = text;
  }

  @Override
  public int compareTo(RESymbol o) {
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