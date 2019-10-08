package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

/**
 * <pre>
truncateTable
    : TRUNCATE TABLE? tableName
    ;
 * </pre>
 */
public class TruncateTable implements DdlStatement {
  public final TableName tableName;

  TruncateTable(TableName tableName) {
    Preconditions.checkArgument(tableName != null);

    this.tableName = tableName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("TRUNCATE TABLE ").append(tableName.literal());
    return sb.toString();
  }
}
