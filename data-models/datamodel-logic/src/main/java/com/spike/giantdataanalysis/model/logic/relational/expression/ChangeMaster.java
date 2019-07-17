package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 changeMaster
    : CHANGE MASTER TO
      masterOption (',' masterOption)* channelOption?
    ;
 * </pre>
 */
public class ChangeMaster implements ReplicationStatement {
  public final List<MasterOption> masterOptions;
  public final ChannelOption channelOption;

  ChangeMaster(List<MasterOption> masterOptions, ChannelOption channelOption) {
    Preconditions.checkArgument(masterOptions != null && masterOptions.size() > 0);

    this.masterOptions = masterOptions;
    this.channelOption = channelOption;
  }

}
