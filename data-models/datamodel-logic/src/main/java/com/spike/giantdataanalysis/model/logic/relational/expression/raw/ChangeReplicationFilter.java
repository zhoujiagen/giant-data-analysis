package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * <pre>
 changeReplicationFilter
    : CHANGE REPLICATION FILTER
      replicationFilter (',' replicationFilter)*
    ;
 * </pre>
 */
public class ChangeReplicationFilter implements ReplicationStatement {

  public final List<ReplicationFilter> replicationFilters;

  ChangeReplicationFilter(List<ReplicationFilter> replicationFilters) {
    Preconditions.checkArgument(replicationFilters != null && replicationFilters.size() > 0);

    this.replicationFilters = replicationFilters;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CHANGE REPLICATION FILTER ");
    List<String> literals = Lists.newArrayList();
    for (ReplicationFilter replicationFilter : replicationFilters) {
      literals.add(replicationFilter.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
