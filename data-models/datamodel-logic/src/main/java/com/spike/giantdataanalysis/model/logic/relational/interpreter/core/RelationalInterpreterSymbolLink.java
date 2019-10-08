package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

/**
 * 符号链接.
 */
public class RelationalInterpreterSymbolLink {
  public final RelationalInterpreterSymbol from;
  public final RelationalInterpreterSymbolLinkTypeEnum linkType;
  public final RelationalInterpreterSymbol to;

  public RelationalInterpreterSymbolLink(RelationalInterpreterSymbol from,
      RelationalInterpreterSymbolLinkTypeEnum linkType, RelationalInterpreterSymbol to) {
    this.from = from;
    this.linkType = linkType;
    this.to = to;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(from.toString());
    builder.append("=");
    builder.append(linkType);
    builder.append("=>");
    builder.append(to);
    return builder.toString();
  }

}