package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

/**
 * <pre>
 checksumTable
    : CHECKSUM TABLE tables actionOption=(QUICK | EXTENDED)?
    ;
 * </pre>
 */
public class ChecksumTable implements AdministrationStatement {
  public static enum ActionOptionEnum implements RelationalAlgebraEnum {
    QUICK, EXTENDED;

    @Override
    public String literal() {
      return name();
    }
  }

  public final Tables tables;
  public final ChecksumTable.ActionOptionEnum actionOption;

  ChecksumTable(Tables tables, ChecksumTable.ActionOptionEnum actionOption) {
    Preconditions.checkArgument(tables != null);

    this.tables = tables;
    this.actionOption = actionOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CHECKSUM TABLE ").append(tables);
    if (actionOption != null) {
      sb.append(" ").append(actionOption.literal());
    }
    return sb.toString();
  }
}
