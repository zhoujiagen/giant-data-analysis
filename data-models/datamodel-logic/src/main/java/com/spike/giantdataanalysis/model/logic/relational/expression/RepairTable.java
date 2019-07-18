package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;

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
    return sb.toString();
  }
}
