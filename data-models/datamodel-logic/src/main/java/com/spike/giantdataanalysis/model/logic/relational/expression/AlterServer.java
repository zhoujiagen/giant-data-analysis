package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 alterServer
    : ALTER SERVER uid OPTIONS
      '(' serverOption (',' serverOption)* ')'
    ;
 * </pre>
 */
public class AlterServer implements DdlStatement {

  public final Uid uid;
  public final List<ServerOption> serverOptions;

  AlterServer(Uid uid, List<ServerOption> serverOptions) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(serverOptions != null && serverOptions.size() > 0);

    this.uid = uid;
    this.serverOptions = serverOptions;
  }

}
