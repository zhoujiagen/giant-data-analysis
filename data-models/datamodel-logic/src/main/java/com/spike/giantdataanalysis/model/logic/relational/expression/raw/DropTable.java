package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

/**
 * <pre>
 dropTable
    : DROP TEMPORARY? TABLE ifExists?
      tables dropType=(RESTRICT | CASCADE)?
    ;
 * </pre>
 */
public class DropTable implements DdlStatement {

  public final Boolean temporary;
  public final IfExists ifExists;
  public final Tables tables;
  public final DropTypeEnum dropType;

  DropTable(Boolean temporary, IfExists ifExists, Tables tables, DropTypeEnum dropType) {
    Preconditions.checkArgument(tables != null);

    this.temporary = temporary;
    this.ifExists = ifExists;
    this.tables = tables;
    this.dropType = dropType;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP ");
    if (Boolean.TRUE.equals(temporary)) {
      sb.append("TEMPORARY ");
    }
    sb.append("TABLE ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(tables.literal()).append(" ");
    if (dropType != null) {
      sb.append(dropType.literal());
    }
    return sb.toString();
  }
}
