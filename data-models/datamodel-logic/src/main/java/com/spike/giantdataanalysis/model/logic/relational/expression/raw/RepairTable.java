package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

/**
 * <pre>
 repairTable
    : REPAIR actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      TABLE tables
      QUICK? EXTENDED? USE_FRM?
    ;
 * </pre>
 */
public class RepairTable implements AdministrationStatement {
  public final AdminTableActionOptionEnum actionOption;
  public final Tables tables;
  public final Boolean quick;
  public final Boolean extended;
  public final Boolean useFrm;

  RepairTable(AdminTableActionOptionEnum actionOption, Tables tables, Boolean quick,
      Boolean extended, Boolean useFrm) {
    Preconditions.checkArgument(tables != null);

    this.actionOption = actionOption;
    this.tables = tables;
    this.quick = quick;
    this.extended = extended;
    this.useFrm = useFrm;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("REPAIR ");
    if (actionOption != null) {
      sb.append(actionOption.literal()).append(" ");
    }
    sb.append("TABLE ").append(tables.literal()).append(" ");
    if (Boolean.TRUE.equals(quick)) {
      sb.append("QUICK ");
    }
    if (Boolean.TRUE.equals(extended)) {
      sb.append("EXTENDED ");
    }
    if (Boolean.TRUE.equals(useFrm)) {
      sb.append("USE_FRM");
    }
    return sb.toString();
  }
}
