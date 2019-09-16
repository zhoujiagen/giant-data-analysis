package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

/**
 * <pre>
 optimizeTable
    : OPTIMIZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      TABLE tables
    ;
 * </pre>
 */
public class OptimizeTable implements AdministrationStatement {
  public final AdminTableActionOptionEnum actionOption;
  public final Tables tables;

  OptimizeTable(AdminTableActionOptionEnum actionOption, Tables tables) {
    Preconditions.checkArgument(tables != null);

    this.actionOption = actionOption;
    this.tables = tables;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("OPTIMIZE ");
    if (actionOption != null) {
      sb.append(actionOption.literal()).append(" ");
    }
    sb.append("TABLE ").append(tables.literal());
    return sb.toString();
  }
}
