package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 createServer
    : CREATE SERVER uid
    FOREIGN DATA WRAPPER wrapperName=(MYSQL | STRING_LITERAL)
    OPTIONS '(' serverOption (',' serverOption)* ')'
    ;
 * </pre>
 */
public class CreateServer implements DdlStatement {

  public final Uid uid;
  public final String wrapperName;
  public final List<ServerOption> serverOptions;

  CreateServer(Uid uid, String wrapperName, List<ServerOption> serverOptions) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(wrapperName != null);
    Preconditions.checkArgument(serverOptions != null && serverOptions.size() > 0);

    this.uid = uid;
    this.wrapperName = wrapperName;
    this.serverOptions = serverOptions;
  }

}
