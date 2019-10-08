package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 resetSlave
    : RESET SLAVE ALL? channelOption?
    ;
 * </pre>
 */
public class ResetSlave implements ReplicationStatement {

  public final Boolean all;
  public final ChannelOption channelOption;

  ResetSlave(Boolean all, ChannelOption channelOption) {
    this.all = all;
    this.channelOption = channelOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("RESET SLAVE ");
    if (Boolean.TRUE.equals(all)) {
      sb.append("ALL ");
    }
    if (channelOption != null) {
      sb.append(channelOption.literal());
    }
    return sb.toString();
  }
}
