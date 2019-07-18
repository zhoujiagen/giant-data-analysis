package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
    return sb.toString();
  }
}
