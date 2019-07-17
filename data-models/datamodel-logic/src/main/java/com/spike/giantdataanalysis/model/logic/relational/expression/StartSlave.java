package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

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

}
