package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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

}
