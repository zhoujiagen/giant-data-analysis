package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 loadIndexIntoCache
    : LOAD INDEX INTO CACHE
      loadedTableIndexes (',' loadedTableIndexes)*
    ;
 * </pre>
 */
public class LoadIndexIntoCache implements AdministrationStatement {
  public final List<LoadedTableIndexes> loadedTableIndexes;

  LoadIndexIntoCache(List<LoadedTableIndexes> loadedTableIndexes) {
    Preconditions.checkArgument(loadedTableIndexes != null && loadedTableIndexes.size() > 0);

    this.loadedTableIndexes = loadedTableIndexes;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
