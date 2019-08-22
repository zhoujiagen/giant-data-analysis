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
    symbolMap.put(text, new RESymbol(text));
  }

  public void addSymbol(String text, RESymbolTypeEnum symbolType) {
    symbolMap.put(text, new RESymbol(symbolType, text));
  }

  public void addSymbol(String text, RESymbolTypeEnum symbolType, Object object) {
    symbolMap.put(text, new RESymbol(symbolType, text, object));
  }

  public void updateSymbol(String text, RESymbolTypeEnum symbolType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    RESymbol symbol = symbolMap.get(text);
    if (symbol != null) {
      symbol.type = symbolType;
    }
  }

  public boolean isEmpty() {
    return symbolMap.isEmpty();
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

  /**
   * FIXME(zhoujiagen) 从链接中获取符号.
   * @param from
   * @param linkType
   * @return to, 不存在返回null
   */
  public String getSymbolThroughLink(RESymbol from, RESymbolLinkTypeEnum linkType) {
    for (Entry<MultiKey<? extends String>, Set<RESymbolLinkTypeEnum>> entry : symbolLinks
        .entrySet()) {
      MultiKey<? extends String> key = entry.getKey();
      Set<RESymbolLinkTypeEnum> value = entry.getValue();
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
      for (Entry<MultiKey<? extends String>, Set<RESymbolLinkTypeEnum>> entry : symbolLinks
          .entrySet()) {
        MultiKey<? extends String> key = entry.getKey();
        Set<RESymbolLinkTypeEnum> value = entry.getValue();
        symbolLinkStringList.add(key.getKey(0) + " -" + value + "-> " + key.getKey(1));
      }
      // builder.append(symbolLinks);
      builder.append(Joiner.on(", ").join(symbolLinkStringList));
    }
    return builder.toString();
  }

  // ---------------------------------------------------------------------------
  // Symbol
  // ---------------------------------------------------------------------------

  /** 符号类型. */
  public static enum RESymbolTypeEnum implements REInterpreterEnum {
    CONSTANT, //
    LOCAL_ID, //
    MYSQL_VARIABLE, //
    DATABASE_NAME, //
    TABLE_NAME, //
    ATTRIBUTE_NAME, //
    FUNCTION_ATTRIBUTE_NAME, // 函数结果作为属性
    ALL_ATTRIBUTE_NAME, // *
    FUNCTION_NAME, //
    OPERATOR_NAME, //
    COLLATION_NAME, //

    SPECIFIER_SELECT, // SELECT限定符
    SPECIFIER_ORDER, // ORDER的限定符
    SPEFICIER_GROUP_BY, // GROUP BY的限定符
    SPEFICIER_JOIN, // LEFT, RIGHT
    SPEFICIER_TEST_VALUE, // NOT
    SPEFICIER_AGG, // AggregateWindowedFunction.AggregatorEnum
    QUALIFIER_PREDICATE, // SOME, ANY, ALL
    REGEX_TYPE, // RegexpPredicate.RegexType
    INTERVAL_TYPE, // DdlStatement.IntervalType

    FUNCTION_CALL, // 函数调用
    EXPRESSION, // 谓词表达式
    EXPRESSION_ATOM, // 原子谓词表达式

    // others
    ;

    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * 符号.
   */
  public static class RESymbol implements Comparable<RESymbol> {

    // 名称
    public final String text;
    // 符号附带信息
    public Object object;
    // 可以被重新赋值
    public RESymbolTypeEnum type;

    public RESymbol(String text) {
      Preconditions.checkArgument(StringUtils.isNotBlank(text));

      this.type = null;
      this.text = text;
      this.object = null;
    }

    public RESymbol(RESymbolTypeEnum type, String text) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(text != null);

      this.type = type;
      this.text = text;
      this.object = null;
    }

    public RESymbol(RESymbolTypeEnum type, String text, Object object) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(text != null);

      this.type = type;
      this.text = text;
      this.object = object;
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

  // ---------------------------------------------------------------------------
  // Symbol Link
  // ---------------------------------------------------------------------------

  /** 符号间链接类型. */
  public static enum RESymbolLinkTypeEnum implements REInterpreterEnum {
    ALIAS_OF, //
    ARGUMENT_OF, //
    ATTRIBUTE_OF, //
    TABLE_OF_DATABASE, //
    TYPE, // Constant.Type
    SPECIFIER // 限定符
    ;
    // others

    @Override
    public String literal() {
      return name();
    }

  }

  /**
   * 符号链接.
   */
  public static class RESymbolLink {
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
}
