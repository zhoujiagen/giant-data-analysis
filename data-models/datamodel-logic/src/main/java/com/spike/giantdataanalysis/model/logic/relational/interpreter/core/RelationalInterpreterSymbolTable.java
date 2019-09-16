package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

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
public final class RelationalInterpreterSymbolTable {

  /** 符号字典: text => entity */
  public final Map<String, RelationalInterpreterSymbol> symbolMap = Maps.newHashMap();
  /** 符号间链接字典: from text => to text => link type */
  public final MultiKeyMap<String, Set<RelationalInterpreterSymbolLinkTypeEnum>> symbolLinks =
      new MultiKeyMap<>();

  public void addSymbol(String text) {
    symbolMap.put(text, new RelationalInterpreterSymbol(text));
  }

  public void addSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType) {
    symbolMap.put(text, new RelationalInterpreterSymbol(symbolType, text));
  }

  public void addSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType,
      Object object) {
    symbolMap.put(text, new RelationalInterpreterSymbol(symbolType, text, object));
  }

  public void updateSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    RelationalInterpreterSymbol symbol = symbolMap.get(text);
    if (symbol != null) {
      symbol.type = symbolType;
    }
  }

  public boolean isEmpty() {
    return symbolMap.isEmpty();
  }

  public RelationalInterpreterSymbol getSymbol(String text) {
    return symbolMap.get(text);
  }

  public void addLink(RelationalInterpreterSymbolLinkTypeEnum linkType, String from, String to) {
    Set<RelationalInterpreterSymbolLinkTypeEnum> linkTypes = symbolLinks.get(from, to);
    if (linkTypes == null) {
      linkTypes = Sets.newHashSet(linkType);
    }
    symbolLinks.put(from, to, linkTypes);
  }

  /**
   * FIXME(zhoujiagen) 从链接中获取符号.
   * @param from
   * @param linkType
   * @return to, 不存在返回null
   */
  public String getSymbolThroughLink(RelationalInterpreterSymbol from,
      RelationalInterpreterSymbolLinkTypeEnum linkType) {
    for (Entry<MultiKey<? extends String>, Set<RelationalInterpreterSymbolLinkTypeEnum>> entry : symbolLinks
        .entrySet()) {
      MultiKey<? extends String> key = entry.getKey();
      Set<RelationalInterpreterSymbolLinkTypeEnum> value = entry.getValue();
      if (key.getKey(0).equals(from.text) && value.contains(linkType)) {
        return key.getKey(1);
      }
    }

    return null;
  }

  @Override
  public String toString() {
    if (symbolMap.isEmpty()) {
      return StringUtils.EMPTY;
    }

    StringBuilder builder = new StringBuilder();
    builder.append("Symbols=");
    builder.append("{");
    builder.append(Joiner.on(", ").join(symbolMap.values()));
    builder.append("}");
    if (!symbolLinks.isEmpty()) {
      builder.append(", Links=");
      List<String> symbolLinkStringList = Lists.newArrayList();
      for (Entry<MultiKey<? extends String>, Set<RelationalInterpreterSymbolLinkTypeEnum>> entry : symbolLinks
          .entrySet()) {
        MultiKey<? extends String> key = entry.getKey();
        Set<RelationalInterpreterSymbolLinkTypeEnum> value = entry.getValue();
        symbolLinkStringList.add(key.getKey(0) + " -" + value + "-> " + key.getKey(1));
      }
      // builder.append(symbolLinks);
      builder.append(Joiner.on(", ").join(symbolLinkStringList));
    }
    return builder.toString();
  }
}
