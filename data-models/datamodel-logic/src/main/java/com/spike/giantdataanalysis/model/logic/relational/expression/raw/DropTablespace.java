package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 dropTablespace
    : DROP TABLESPACE uid (ENGINE '='? engineName)?
    ;
 * </pre>
 */
public class DropTablespace implements DdlStatement {
  public final Uid uid;
  public final EngineName engineName;

  DropTablespace(Uid uid, EngineName engineName) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP TABLESPACE ").append(uid.literal()).append(" ");
    if (engineName != null) {
      sb.append("ENGINE = ").append(engineName.literal());
    }
    return sb.toString();
  }
}
