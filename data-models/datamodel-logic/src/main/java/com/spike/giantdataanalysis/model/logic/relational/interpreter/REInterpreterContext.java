package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpression;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RESymbolTable.RESymbol;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RESymbolTable.RESymbolLinkTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RESymbolTable.RESymbolTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 解释器中上下文.
 */
public class REInterpreterContext {

  private static final Logger LOG = LoggerFactory.getLogger(REInterpreterContext.class);

  /** 全局符号表. */
  public final RESymbolTable globalSymbolTable = new RESymbolTable();

  /** 作用域缓存. */
  public final Map<String, REInterpreterScope> scopeCache = Maps.newHashMap();
  /** 作用域中的符号表: scope name => symbol table */
  public final Map<String, RESymbolTable> scopeSymbolTable = Maps.newHashMap();

  /** 根作用域. */
  public final REInterpreterScope rootScope = REInterpreterScope.ROOT;
  /** 当前作用域. */
  public REInterpreterScope currentScope = rootScope;
  /** 当前作用域中的符号表. */
  public RESymbolTable currentSymbolTable = new RESymbolTable();

  /** 临时关系字典: relation name => relation */
  public final Map<String, RelationalRelation> temporaryRelationMap = Maps.newHashMap();

  public REInterpreterContext() {
    scopeCache.put(currentScope.name, currentScope);
    scopeSymbolTable.put(currentScope.name, currentSymbolTable);
  }

  public final void enterScope(final String shortName, final String postfix) {
    String finalPostfix = postfix;
    if (finalPostfix == null) {
      finalPostfix = "";
    }

    String paddedShortName = shortName + finalPostfix;

    String newName = currentScope.newName(currentScope, paddedShortName);
    LOG.debug("enter scope: {} => {}", currentScope.name, newName);
    REInterpreterScope scope = scopeCache.get(newName);
    if (scope == null) {
      scope = new REInterpreterScope(currentScope, paddedShortName);
      scopeCache.put(newName, scope);
    }
    currentScope = scope;

    RESymbolTable symbolTable = scopeSymbolTable.get(newName);
    if (symbolTable == null) {
      symbolTable = new RESymbolTable();
      scopeSymbolTable.put(newName, symbolTable);
    }
    currentSymbolTable = symbolTable;
  }

  public final void leaveScope() {
    String currentScopeName = currentScope.name;
    if (REInterpreterScope.ROOT_NAME.equals(currentScope.name)) {
      return;
    }
    currentScope = currentScope.parent;
    LOG.debug("leave scope: {} => {}; symbol table=\n{}", //
      currentScopeName, currentScope.name, scopeSymbolTable.get(currentScopeName));
    currentSymbolTable = scopeSymbolTable.get(currentScope.name);
  }

  // delegate to RESymbolTable
  public void addSymbol(String text) {
    LOG.debug("add symbol {} in scope {}", text, currentScope.name);

    currentSymbolTable.addSymbol(text);
  }

  // delegate to RESymbolTable
  public void addSymbol(String text, RESymbolTypeEnum symbolType) {
    LOG.debug("add symbol {}/type {} in scope {}", text, symbolType, currentScope.name);

    currentSymbolTable.addSymbol(text, symbolType);
  }

  // delegate to RESymbolTable
  public void addSymbol(String text, RESymbolTypeEnum symbolType, Object object) {
    String objectString = "";
    if (object != null) {
      if (object instanceof RelationalAlgebraExpression) {
        objectString = ((RelationalAlgebraExpression) object).literal();
      } else {
        objectString = object.toString();
      }
    }
    LOG.debug("add symbol {}/type {}/value {} in scope {}", text, symbolType, objectString,
      currentScope.name);

    currentSymbolTable.addSymbol(text, symbolType, object);
  }

  // delegate to RESymbolTable
  public void updateSymbol(String text, RESymbolTypeEnum symbolType) {
    LOG.debug("update symbol {}/type {} in scope {}", text, symbolType, currentScope.name);

    currentSymbolTable.updateSymbol(text, symbolType);
  }

  // delegate to RESymbolTable
  public RESymbol getSymbol(String text) {
    return currentSymbolTable.getSymbol(text);
  }

  // delegate to RESymbolTable
  public void addLink(RESymbolLinkTypeEnum linkType, String from, String to) {
    LOG.debug("add link: {} -{}- {}", from, to, linkType);

    currentSymbolTable.addLink(linkType, from, to);
  }

  // TODO(zhoujiagen) 放到作用域中???
  public void addTemporaryRelation(String name, RelationalRelation relation) {
    LOG.debug("add temporary relation: {}, {}", name, relation);

    temporaryRelationMap.put(name, relation);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    this.toString(false, builder, rootScope, 0);
    return builder.toString();
  }

  /**
   * @param compact 带空符号表的作用域是否输出
   * @param builder
   * @param scope
   * @param level
   */
  public void toString(boolean compact, StringBuilder builder, REInterpreterScope scope,
      int level) {
    if (scope == null) {
      return;
    }

    RESymbolTable symbolTable = scopeSymbolTable.get(scope.name);

    if (compact) {
      if (!symbolTable.isEmpty() || rootScope.name.equals(scope.name)) {
        String tabs = StringUtils.repeat("_|", level);
        builder.append(tabs);
        builder.append(scope.shortName()).append(" ");
        builder.append(symbolTable).append(System.lineSeparator());
      }
    } else {
      String tabs = StringUtils.repeat("_|", level);
      builder.append(tabs);
      builder.append(scope.shortName()).append(" ");
      builder.append(symbolTable).append(System.lineSeparator());
    }

    List<REInterpreterScope> children = scope.children;
    if (CollectionUtils.isNotEmpty(children)) {
      for (REInterpreterScope child : children) {
        this.toString(compact, builder, child, level + 1);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Collect
  // ---------------------------------------------------------------------------

  public List<RESymbol> collect(RESymbolTypeEnum symbolType, boolean recursive) {
    return this.collect(currentScope, symbolType, recursive);
  }

  public List<RESymbol> collect(String childScopeName, String postfix, RESymbolTypeEnum symbolType,
      boolean recursive) {
    return collect(currentScope.childName(childScopeName + postfix), symbolType, recursive);
  }

  public List<RESymbol> collect(String scopeName, RESymbolTypeEnum symbolType, boolean recursive) {
    REInterpreterScope scope = scopeCache.get(scopeName);
    if (scope == null) {
      return Lists.newArrayList();
    } else {
      return collect(scope, symbolType, recursive);
    }
  }

  public List<RESymbol> collect(REInterpreterScope scope, RESymbolTypeEnum symbolType,
      boolean recursive) {
    List<RESymbol> result = Lists.newArrayList();

    RESymbolTable symbolTable = scopeSymbolTable.get(scope.name);
    result.addAll(this.collect(symbolTable, symbolType));

    if (recursive) {
      for (REInterpreterScope childScope : scope.children) {
        result.addAll(this.collect(childScope, symbolType, recursive));
      }
    }

    return result;
  }

  public List<RESymbol> collect(RESymbolTable symbolTable, RESymbolTypeEnum symbolType) {
    List<RESymbol> result = Lists.newArrayList();
    if (symbolTable == null || symbolTable.symbolMap.isEmpty()) {
      return result;
    }

    for (RESymbol symbol : symbolTable.symbolMap.values()) {
      if (symbol.type.equals(symbolType)) {
        result.add(symbol);
      }
    }
    return result;
  }

  public List<String> collect(RESymbol from, RESymbolLinkTypeEnum linkType, boolean recursive) {
    return this.collect(currentScope, from, linkType, recursive);
  }

  public List<String> collect(REInterpreterScope scope, RESymbol from,
      RESymbolLinkTypeEnum linkType, boolean recursive) {
    List<String> result = Lists.newArrayList();

    RESymbolTable symbolTable = scopeSymbolTable.get(scope.name);
    String toSymbol = symbolTable.getSymbolThroughLink(from, linkType);
    if (toSymbol != null) {
      result.add(toSymbol);
    }

    if (recursive) {
      for (REInterpreterScope childScope : scope.children) {
        result.addAll(this.collect(childScope, from, linkType, recursive));
      }
    }

    return result;
  }
}
