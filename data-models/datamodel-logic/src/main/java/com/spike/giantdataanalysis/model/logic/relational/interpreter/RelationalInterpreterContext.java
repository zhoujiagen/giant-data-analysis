package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RelationalAlgebraExpression;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterScope;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbol;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbolLinkTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbolTable;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbolTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 解释器中上下文.
 */
public class RelationalInterpreterContext {

  private static final Logger LOG = LoggerFactory.getLogger(RelationalInterpreterContext.class);

  /** 全局符号表. */
  public final RelationalInterpreterSymbolTable globalSymbolTable =
      new RelationalInterpreterSymbolTable();

  /** 作用域缓存. */
  public final Map<String, RelationalInterpreterScope> scopeCache = Maps.newHashMap();
  /** 作用域中的符号表: scope name => symbol table */
  public final Map<String, RelationalInterpreterSymbolTable> scopeSymbolTable = Maps.newHashMap();

  /** 根作用域. */
  public final RelationalInterpreterScope rootScope = RelationalInterpreterScope.ROOT;
  /** 当前作用域. */
  public RelationalInterpreterScope currentScope = rootScope;
  /** 当前作用域中的符号表. */
  public RelationalInterpreterSymbolTable currentSymbolTable =
      new RelationalInterpreterSymbolTable();

  /** 临时关系字典: relation name => relation */
  public final Map<String, RelationalRelation> temporaryRelationMap = Maps.newHashMap();

  public RelationalInterpreterContext() {
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
    RelationalInterpreterScope scope = scopeCache.get(newName);
    if (scope == null) {
      scope = new RelationalInterpreterScope(currentScope, paddedShortName);
      scopeCache.put(newName, scope);
    }
    currentScope = scope;

    RelationalInterpreterSymbolTable symbolTable = scopeSymbolTable.get(newName);
    if (symbolTable == null) {
      symbolTable = new RelationalInterpreterSymbolTable();
      scopeSymbolTable.put(newName, symbolTable);
    }
    currentSymbolTable = symbolTable;
  }

  public final void leaveScope() {
    String currentScopeName = currentScope.name;
    if (RelationalInterpreterScope.ROOT_NAME.equals(currentScope.name)) {
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
  public void addSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType) {
    LOG.debug("add symbol {}/type {} in scope {}", text, symbolType, currentScope.name);

    currentSymbolTable.addSymbol(text, symbolType);
  }

  // delegate to RESymbolTable
  public void addSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType,
      Object object) {
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
  public void updateSymbol(String text, RelationalInterpreterSymbolTypeEnum symbolType) {
    LOG.debug("update symbol {}/type {} in scope {}", text, symbolType, currentScope.name);

    currentSymbolTable.updateSymbol(text, symbolType);
  }

  // delegate to RESymbolTable
  public RelationalInterpreterSymbol getSymbol(String text) {
    return currentSymbolTable.getSymbol(text);
  }

  // delegate to RESymbolTable
  public void addLink(RelationalInterpreterSymbolLinkTypeEnum linkType, String from, String to) {
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
    // this.toString(false, builder, rootScope, 0);// 完整输出
    this.toString(true, builder, rootScope, 0);
    return builder.toString();
  }

  /**
   * @param compact 带空符号表的作用域是否输出
   * @param builder
   * @param scope
   * @param level
   */
  public void toString(boolean compact, StringBuilder builder, RelationalInterpreterScope scope,
      int level) {
    if (scope == null) {
      return;
    }

    RelationalInterpreterSymbolTable symbolTable = scopeSymbolTable.get(scope.name);

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

    List<RelationalInterpreterScope> children = scope.children;
    if (CollectionUtils.isNotEmpty(children)) {
      for (RelationalInterpreterScope child : children) {
        this.toString(compact, builder, child, level + 1);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Collect
  // ---------------------------------------------------------------------------

  public List<RelationalInterpreterSymbol> collect(RelationalInterpreterSymbolTypeEnum symbolType,
      boolean recursive) {
    return this.collect(currentScope, symbolType, recursive);
  }

  public List<RelationalInterpreterSymbol> collect(String childScopeName, String postfix,
      RelationalInterpreterSymbolTypeEnum symbolType, boolean recursive) {
    return collect(currentScope.childName(childScopeName + postfix), symbolType, recursive);
  }

  public List<RelationalInterpreterSymbol> collect(String scopeName,
      RelationalInterpreterSymbolTypeEnum symbolType, boolean recursive) {
    RelationalInterpreterScope scope = scopeCache.get(scopeName);
    if (scope == null) {
      return Lists.newArrayList();
    } else {
      return collect(scope, symbolType, recursive);
    }
  }

  public List<RelationalInterpreterSymbol> collect(RelationalInterpreterScope scope,
      RelationalInterpreterSymbolTypeEnum symbolType, boolean recursive) {
    List<RelationalInterpreterSymbol> result = Lists.newArrayList();

    RelationalInterpreterSymbolTable symbolTable = scopeSymbolTable.get(scope.name);
    result.addAll(this.collect(symbolTable, symbolType));

    if (recursive) {
      for (RelationalInterpreterScope childScope : scope.children) {
        result.addAll(this.collect(childScope, symbolType, recursive));
      }
    }

    return result;
  }

  public List<RelationalInterpreterSymbol> collect(RelationalInterpreterSymbolTable symbolTable,
      RelationalInterpreterSymbolTypeEnum symbolType) {
    List<RelationalInterpreterSymbol> result = Lists.newArrayList();
    if (symbolTable == null || symbolTable.symbolMap.isEmpty()) {
      return result;
    }

    for (RelationalInterpreterSymbol symbol : symbolTable.symbolMap.values()) {
      if (symbol.type.equals(symbolType)) {
        result.add(symbol);
      }
    }
    return result;
  }

  public List<String> collect(RelationalInterpreterSymbol from,
      RelationalInterpreterSymbolLinkTypeEnum linkType, boolean recursive) {
    return this.collect(currentScope, from, linkType, recursive);
  }

  public List<String> collect(RelationalInterpreterScope scope, RelationalInterpreterSymbol from,
      RelationalInterpreterSymbolLinkTypeEnum linkType, boolean recursive) {
    List<String> result = Lists.newArrayList();

    RelationalInterpreterSymbolTable symbolTable = scopeSymbolTable.get(scope.name);
    String toSymbol = symbolTable.getSymbolThroughLink(from, linkType);
    if (toSymbol != null) {
      result.add(toSymbol);
    }

    if (recursive) {
      for (RelationalInterpreterScope childScope : scope.children) {
        result.addAll(this.collect(childScope, from, linkType, recursive));
      }
    }

    return result;
  }
}
