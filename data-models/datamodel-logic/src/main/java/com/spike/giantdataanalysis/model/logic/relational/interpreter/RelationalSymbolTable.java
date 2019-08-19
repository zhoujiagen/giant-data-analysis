package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;

/**
 * 解释器符号表抽象.
 */
public final class RelationalSymbolTable {

  public final Map<String, Scope> scopes = Maps.newHashMap();

  /** 符号字典: text => entity */
  public final Map<String, RelationalSymbolTable.Symbol> symbolMap = Maps.newHashMap();
  /** 符号间链接字典: from text => to text => link type */
  public final MultiKeyMap<String, Set<RelationalSymbolTable.SymbolLinkTypeEnum>> symbolLinks =
      new MultiKeyMap<>();

  public void addSymbol(String text) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    symbolMap.put(text, new RelationalSymbolTable.Symbol(text));
  }

  public void addSymbol(RelationalSymbolTable.SymbolTypeEnum symbolType, String text) {
    Preconditions.checkArgument(symbolType != null);
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    symbolMap.put(text, new RelationalSymbolTable.Symbol(symbolType, text));
  }

  public void updateSymbol(String text, RelationalSymbolTable.SymbolTypeEnum symbolType) {
    Preconditions.checkArgument(StringUtils.isNotBlank(text));

    RelationalSymbolTable.Symbol symbol = symbolMap.get(text);
    if (symbol != null) {
      symbol.type = symbolType;
    }
  }

  public RelationalSymbolTable.Symbol getSymbol(String text) {
    return symbolMap.get(text);
  }

  public void addLink(RelationalSymbolTable.SymbolLinkTypeEnum linkType, String from, String to) {
    Set<RelationalSymbolTable.SymbolLinkTypeEnum> linkTypes = symbolLinks.get(from, to);
    if (linkTypes == null) {
      linkTypes = Sets.newHashSet(linkType);
    }
    symbolLinks.put(from, to, linkTypes);
  }

  /** 解释器枚举标记接口. */
  public static interface RelationalInterpreterEnum extends Literal {
  }

  /** 符号类型. */
  public static enum SymbolTypeEnum implements RelationalInterpreterEnum {
    CONSTANT, //
    DATABASE_NAME, //
    TABLE_NAME, //
    ATTRIBUTE_NAME, //
    FUNCTION_NAME//
    ;
    // others

    @Override
    public String literal() {
      return name();
    }
  }

  /** 符号间链接类型. */
  public static enum SymbolLinkTypeEnum implements RelationalInterpreterEnum {
    ALIAS_OF, //
    ARGUMENT_OF, //
    ATTRIBUTE_OF, //
    TABLE_OF_DATABASE;
    // others

    @Override
    public String literal() {
      return name();
    }

  }

  /**
   * 作用域.
   */
  public static class Scope implements Comparable<Scope> {

    public static final String ROOT = "<ROOT>";
    public static final String SEP = "$";

    public final String name;
    public final Scope parent;

    public Scope(String name) {
      Preconditions.checkArgument(name != null);

      this.parent = null;
      this.name = name;
    }

    public Scope(Scope parent, String shortName) {
      Preconditions.checkArgument(parent != null);
      Preconditions.checkArgument(shortName != null);

      this.parent = parent;
      this.name = parent.name + SEP + shortName;
    }

    @Override
    public int compareTo(Scope o) {
      // TODO Implement Comparable<Scope>.compareTo
      return 0;
    }

  }

  /**
   * 符号.
   */
  public static class Symbol implements Comparable<Symbol> {

    public final String text;
    // 可以被重新赋值
    public SymbolTypeEnum type;

    // public final Map<RelationalInterpreterSymbolLinkTypeEnum, List<String>> links =
    // Maps.newHashMap();

    public Symbol(String text) {
      Preconditions.checkArgument(StringUtils.isNotBlank(text));

      this.text = text;
    }

    public Symbol(SymbolTypeEnum type, String text) {
      Preconditions.checkArgument(StringUtils.isNotBlank(text));

      this.type = type;
      this.text = text;
    }

    // public void addLink(RelationalInterpreterSymbolLinkTypeEnum linkType, String symbol) {
    // Preconditions.checkArgument(linkType != null);
    // Preconditions.checkArgument(StringUtils.isNotBlank(symbol));
    //
    // List<String> symbols = links.get(linkType);
    // if (symbols == null) {
    // symbols = Lists.newArrayList(symbol);
    // links.put(linkType, symbols);
    // } else {
    // symbols.add(symbol);
    // }
    // }

    @Override
    public int compareTo(Symbol o) {
      if (o == null) {
        return 1;
      } else {
        return this.text.compareTo(o.text);
      }
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Symbol [");
      builder.append(text);
      builder.append(", ");
      builder.append(type);
      builder.append("]");
      return builder.toString();
    }

  }
}
