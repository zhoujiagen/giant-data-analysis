package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 dropServer
    : DROP SERVER ifExists? uid
    ;
 * </pre>
 */
public class DropServer implements DdlStatement {
  public final IfExists ifExists;
  public final Uid uid;

  DropServer(IfExists ifExists, Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.ifExists = ifExists;
    this.uid = uid;
  }
}
