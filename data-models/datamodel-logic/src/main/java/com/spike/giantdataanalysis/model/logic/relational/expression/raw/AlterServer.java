package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER SERVER ").append(uid.literal()).append(" OPTIONS ");
    if (CollectionUtils.isNotEmpty(serverOptions)) {
      List<String> literals = Lists.newArrayList();
      for (ServerOption serverOption : serverOptions) {
        literals.add(serverOption.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    return sb.toString();
  }
}
