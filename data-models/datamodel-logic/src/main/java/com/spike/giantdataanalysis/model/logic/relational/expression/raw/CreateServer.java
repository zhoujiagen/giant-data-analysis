package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE SERVER ").append(uid.literal()).append(" ");
    sb.append("FOREIGN DATA WRAPPER ").append(wrapperName).append(" ");
    sb.append("OPTIONS ");
    sb.append("(");
    List<String> literals = Lists.newArrayList();
    for (ServerOption serverOption : serverOptions) {
      literals.add(serverOption.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    sb.append(")");
    return sb.toString();
  }
}
