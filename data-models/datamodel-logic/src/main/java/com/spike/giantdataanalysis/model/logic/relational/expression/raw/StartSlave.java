package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * <pre>
 startSlave
    : START SLAVE (threadType (',' threadType)*)?
      (UNTIL untilOption)?
      connectionOption* channelOption?
    ;
 * </pre>
 */
public class StartSlave implements ReplicationStatement {
  public final List<ThreadTypeEnum> threadTypes;
  public final UntilOption untilOption;
  public final List<ConnectionOption> connectionOptions;
  public final ChannelOption channelOption;

  StartSlave(List<ThreadTypeEnum> threadTypes, UntilOption untilOption,
      List<ConnectionOption> connectionOptions, ChannelOption channelOption) {

    this.threadTypes = threadTypes;
    this.untilOption = untilOption;
    this.connectionOptions = connectionOptions;
    this.channelOption = channelOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("START SLAVE ");
    if (CollectionUtils.isNotEmpty(threadTypes)) {
      List<String> literals = Lists.newArrayList();
      for (ThreadTypeEnum threadType : threadTypes) {
        literals.add(threadType.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
    }
    if (untilOption != null) {
      sb.append("UNTIL ").append(untilOption.literal()).append(" ");
    }
    if (CollectionUtils.isNotEmpty(connectionOptions)) {
      List<String> literals = Lists.newArrayList();
      for (ConnectionOption connectionOption : connectionOptions) {
        literals.add(connectionOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    if (channelOption != null) {
      sb.append(channelOption.literal());
    }
    return sb.toString();
  }
}
