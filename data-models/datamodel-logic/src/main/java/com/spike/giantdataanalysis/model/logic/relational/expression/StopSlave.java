package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 stopSlave
    : STOP SLAVE (threadType (',' threadType)*)?
    ;
 * </pre>
 */
public class StopSlave implements ReplicationStatement {

  public final List<ThreadTypeEnum> threadTypes;

  StopSlave(List<ThreadTypeEnum> threadTypes) {
    Preconditions.checkArgument(threadTypes != null && threadTypes.size() > 0);

    this.threadTypes = threadTypes;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
