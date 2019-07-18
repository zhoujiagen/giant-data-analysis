package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;

/**
 * <pre>
 checkTable
    : CHECK TABLE tables checkTableOption*
    ;
 * </pre>
 */
public class CheckTable implements AdministrationStatement {
  public final Tables tables;
  public final List<CheckTableOptionEnum> checkTableOptions;

  CheckTable(Tables tables, List<CheckTableOptionEnum> checkTableOptions) {
    Preconditions.checkArgument(tables != null);

    this.tables = tables;
    this.checkTableOptions = checkTableOptions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
