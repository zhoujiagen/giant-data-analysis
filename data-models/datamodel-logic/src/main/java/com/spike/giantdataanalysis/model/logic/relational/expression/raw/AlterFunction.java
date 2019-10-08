package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 alterFunction
    : ALTER FUNCTION fullId routineOption*
    ;
 * </pre>
 */
public class AlterFunction implements DdlStatement {
  public final FullId fullId;
  public final List<RoutineOption> routineOptions;

  AlterFunction(FullId fullId, List<RoutineOption> routineOptions) {
    Preconditions.checkArgument(fullId != null);

    this.fullId = fullId;
    this.routineOptions = routineOptions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER FUNCTION ");
    sb.append(fullId.literal());
    if (CollectionUtils.isNotEmpty(routineOptions)) {
      List<String> literals = Lists.newArrayList();
      for (RoutineOption routineOption : routineOptions) {
        literals.add(routineOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    return sb.toString();
  }
}
