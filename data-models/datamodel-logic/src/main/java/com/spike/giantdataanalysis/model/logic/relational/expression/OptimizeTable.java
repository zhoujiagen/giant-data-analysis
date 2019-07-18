package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;

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
    return sb.toString();
  }
}
