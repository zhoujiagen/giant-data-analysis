package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

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

}
