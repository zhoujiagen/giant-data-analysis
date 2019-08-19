package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 解释器符号表抽象.
 */
public final class RESymbolTable {

  /** 符号字典: text => entity */
  public final Map<String, RESymbol> symbolMap = Maps.newHashMap();
  /** 符号间链接字典: from text => to text => link type */
  public final MultiKeyMap<String, Set<RESymbolLinkTypeEnum>> symbolLinks = new MultiKeyMap<>();

  public void addSymbol(String text) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    symbolMap.put(text, new RESymbol(text));
  }

  public void addSymbol(String text, RESymbolTypeEnum symbolType) {
    Preconditions.checkArgument(symbolType != null);
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    symbolMap.put(text, new RESymbol(symbolType, text));
  }

  public void updateSymbol(String text, RESymbolTypeEnum symbolType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    RESymbol symbol = symbolMap.get(text);
    if (symbol != null) {
      symbol.type = symbolType;
    }
  }

  public RESymbol getSymbol(String text) {
    return symbolMap.get(text);
  }

  public void addLink(RESymbolLinkTypeEnum linkType, String from, String to) {
    Set<RESymbolLinkTypeEnum> linkTypes = symbolLinks.get(from, to);
    if (linkTypes == null) {
      linkTypes = Sets.newHashSet(linkType);
    }
    symbolLinks.put(from, to, linkTypes);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Symbols=");
    if (symbolMap.isEmpty()) {
      builder.append(symbolMap);
    } else {
      for (RESymbol symbol : symbolMap.values()) {
        builder.append(symbol.toString());
      }
    }
    builder.append(System.lineSeparator());
    builder.append("Links=");
    builder.append(symbolLinks);
    return builder.toString();
  }

}
