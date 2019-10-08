package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 dropView
    : DROP VIEW ifExists?
      fullId (',' fullId)* dropType=(RESTRICT | CASCADE)?
    ;
 * </pre>
 */
public class DropView implements DdlStatement {
  public final IfExists ifExists;
  public final List<FullId> fullIds;
  public final DropTypeEnum dropType;

  DropView(IfExists ifExists, List<FullId> fullIds, DropTypeEnum dropType) {
    Preconditions.checkArgument(fullIds != null && fullIds.size() > 0);

    this.ifExists = ifExists;
    this.fullIds = fullIds;
    this.dropType = dropType;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP VIEW ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    List<String> literals = Lists.newArrayList();
    for (FullId fullId : fullIds) {
      literals.add(fullId.literal());
    }
    sb.append(Joiner.on(", ").join(literals)).append(" ");
    if (dropType != null) {
      sb.append(dropType.literal());
    }
    return sb.toString();
  }
}
