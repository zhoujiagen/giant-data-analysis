package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

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
    this.threadTypes = threadTypes;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("STOP SLAVE ");
    if (CollectionUtils.isNotEmpty(threadTypes)) {
      List<String> literals = Lists.newArrayList();
      for (ThreadTypeEnum threadType : threadTypes) {
        literals.add(threadType.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    return sb.toString();
  }
}
