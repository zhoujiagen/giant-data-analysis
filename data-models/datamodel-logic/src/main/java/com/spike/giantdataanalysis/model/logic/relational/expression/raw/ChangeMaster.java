package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CHANGE MASTER TO ");
    List<String> literals = Lists.newArrayList();
    for (MasterOption masterOption : masterOptions) {
      literals.add(masterOption.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    if (channelOption != null) {
      sb.append(channelOption.literal());
    }
    return sb.toString();
  }
}
