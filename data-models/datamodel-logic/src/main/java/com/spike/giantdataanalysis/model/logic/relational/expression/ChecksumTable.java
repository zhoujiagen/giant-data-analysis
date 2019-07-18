package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;

/**
 * <pre>
 checksumTable
    : CHECKSUM TABLE tables actionOption=(QUICK | EXTENDED)?
    ;
 * </pre>
 */
public class ChecksumTable implements AdministrationStatement {
  public static enum ActionOptionEnum implements RelationalAlgebraEnum {
    QUICK, EXTENDED
  }

  public final Tables tables;
  public final ActionOptionEnum actionOption;

  ChecksumTable(Tables tables, ActionOptionEnum actionOption) {
    Preconditions.checkArgument(tables != null);

    this.tables = tables;
    this.actionOption = actionOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
