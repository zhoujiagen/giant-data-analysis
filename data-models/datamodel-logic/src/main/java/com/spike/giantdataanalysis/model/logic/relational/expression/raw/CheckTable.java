package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;

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
    sb.append("CHECK TABLE ").append(tables.literal());
    if (CollectionUtils.isNotEmpty(checkTableOptions)) {
      List<String> literals = Lists.newArrayList();
      for (CheckTableOptionEnum checkTableOption : checkTableOptions) {
        literals.add(checkTableOption.literal());
      }
      sb.append(" ").append(Joiner.on(" ").join(literals));
    }
    return sb.toString();
  }
}
