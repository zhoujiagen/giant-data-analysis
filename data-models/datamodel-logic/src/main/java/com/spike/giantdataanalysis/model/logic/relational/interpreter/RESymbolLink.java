package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/**
 * 符号链接.
 */
public class RESymbolLink {
  public final RESymbol from;
  public final RESymbolLinkTypeEnum linkType;
  public final RESymbol to;

  public RESymbolLink(RESymbol from, RESymbolLinkTypeEnum linkType, RESymbol to) {
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
