package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

/**
 * <pre>
 analyzeTable
    : ANALYZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
       TABLE tables
    ;
 * </pre>
 */
public class AnalyzeTable implements AdministrationStatement {

  public final AdminTableActionOptionEnum actionOption;
  public final Tables tables;

  AnalyzeTable(AdminTableActionOptionEnum actionOption, Tables tables) {
    Preconditions.checkArgument(tables != null);

    this.actionOption = actionOption;
    this.tables = tables;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ANALYZE ");
    if (actionOption != null) {
      sb.append(actionOption.literal()).append(" ");
    }
    sb.append("TABLE ").append(tables.literal());
    return sb.toString();
  }

}
