package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

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

}
