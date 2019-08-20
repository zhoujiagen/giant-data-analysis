package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
    if (!symbolMap.isEmpty()) {
      builder.append("Symbols=");
      builder.append("{");
      builder.append(Joiner.on(", ").join(symbolMap.values()));
      builder.append("}");
      if (!symbolLinks.isEmpty()) {
        builder.append(", Links=");
        List<String> symbolLinkStringList = Lists.newArrayList();
        for (Entry<MultiKey<? extends String>, Set<RESymbolLinkTypeEnum>> entry : symbolLinks
            .entrySet()) {
          MultiKey<? extends String> key = entry.getKey();
          Set<RESymbolLinkTypeEnum> value = entry.getValue();
          symbolLinkStringList.add(key.getKey(0) + "=" + value + "=>" + key.getKey(1));
        }
        // builder.append(symbolLinks);
        builder.append(Joiner.on(", ").join(symbolLinkStringList));
      }

    }
    return builder.toString();
  }

}
