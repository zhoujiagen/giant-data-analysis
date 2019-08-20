package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 解释器中上下文.
 */
public class REInterpreterContext {

  private static final Logger LOG = LoggerFactory.getLogger(REInterpreterContext.class);

  /** 全局符号表. */
  public final RESymbolTable globalSymbolTable = new RESymbolTable();

  /** 作用域缓存. */
  public final Map<String, REScope> scopeCache = Maps.newHashMap();
  /** 作用域中的符号表: scope name => symbol table */
  public final Map<String, RESymbolTable> scopeSymbolTable = Maps.newHashMap();

  /** 根作用域. */
  private final REScope rootScope = REScope.ROOT;

  /** 当前作用域. */
  public REScope currentScope = rootScope;
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
    REScope scope = scopeCache.get(newName);
    if (scope == null) {
      scope = new REScope(currentScope, paddedShortName);
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
    if (REScope.ROOT_NAME.equals(currentScope.name)) {
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
    this.toString(builder, rootScope, 0);
    return builder.toString();
  }

  public void toString(StringBuilder builder, REScope scope, int level) {
    if (scope == null) {
      return;
    }

    String tabs = StringUtils.repeat("_|", level);

    builder.append(tabs);
    builder.append(scope.shortName()).append(" ");

    RESymbolTable symbolTable = scopeSymbolTable.get(scope.name);
    builder.append(symbolTable).append(System.lineSeparator());

    List<REScope> children = scope.children;
    if (CollectionUtils.isNotEmpty(children)) {
      for (REScope child : children) {
        this.toString(builder, child, level + 1);
      }
    }
  }

}
