package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 dropLogfileGroup
    : DROP LOGFILE GROUP uid ENGINE '=' engineName
    ;
 * </pre>
 */
public class DropLogfileGroup implements DdlStatement {
  public final Uid uid;
  public final EngineName engineName;

  DropLogfileGroup(Uid uid, EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(engineName != null);

    this.uid = uid;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP LOGFILE GROUP ").append(uid.literal()).append(" ENGINE = ")
        .append(engineName.literal());
    return sb.toString();
  }
}
