package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
    sb.append("LOAD INDEX INTO CACHE ");
    List<String> literals = Lists.newArrayList();
    for (LoadedTableIndexes loadedTableIndex : loadedTableIndexes) {
      literals.add(loadedTableIndex.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
