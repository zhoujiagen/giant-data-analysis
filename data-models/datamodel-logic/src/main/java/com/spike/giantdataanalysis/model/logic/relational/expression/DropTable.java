package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;

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

}
