package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 cacheIndexStatement
    : CACHE INDEX tableIndexes (',' tableIndexes)*
      ( PARTITION '(' (uidList | ALL) ')' )?
      IN schema=uid
    ;
 * </pre>
 */
public class CacheIndexStatement implements AdministrationStatement {
  public final List<TableIndexes> tableIndexes;
  public final UidList partitionUidList;
  public final Boolean partitionAll;
  public final Uid schema;

  CacheIndexStatement(List<TableIndexes> tableIndexes, UidList partitionUidList,
      Boolean partitionAll, Uid schema) {
    Preconditions.checkArgument(tableIndexes != null && tableIndexes.size() > 0);
    Preconditions.checkArgument(schema != null);

    this.tableIndexes = tableIndexes;
    this.partitionUidList = partitionUidList;
    this.partitionAll = partitionAll;
    this.schema = schema;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CACHE INDEX ");
    List<String> literals = Lists.newArrayList();
    for (TableIndexes ti : tableIndexes) {
      literals.add(ti.literal());
    }
    sb.append(Joiner.on(", ").join(literals)).append(" ");
    if (partitionUidList != null) {
      sb.append("PARTITION (").append(partitionUidList).append(") ");
    }
    if (Boolean.TRUE.equals(partitionAll)) {
      sb.append("PARTITION (ALL) ");
    }
    sb.append("IN ").append(schema.literal());
    return sb.toString();
  }
}
