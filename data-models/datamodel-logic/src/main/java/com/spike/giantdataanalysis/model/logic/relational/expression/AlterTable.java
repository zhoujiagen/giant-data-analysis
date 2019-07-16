package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

/**
 * <pre>
 alterTable
    : ALTER intimeAction=(ONLINE | OFFLINE)?
      IGNORE? TABLE tableName
      (alterSpecification (',' alterSpecification)*)?
      partitionDefinitions?
    ;
 * </pre>
 */
public class AlterTable implements DdlStatement {

  public final IntimeActionEnum intimeAction;
  public final Boolean ignore;
  public final TableName tableName;
  public final List<AlterSpecification> alterSpecifications;
  public PartitionDefinitions partitionDefinitions;

  AlterTable(IntimeActionEnum intimeAction, Boolean ignore, TableName tableName,
      List<AlterSpecification> alterSpecifications, PartitionDefinitions partitionDefinitions) {
    Preconditions.checkArgument(tableName != null);
    Preconditions.checkArgument(alterSpecifications != null && alterSpecifications.size() > 0);

    this.intimeAction = intimeAction;
    this.ignore = ignore;
    this.tableName = tableName;
    this.alterSpecifications = alterSpecifications;
    this.partitionDefinitions = partitionDefinitions;
  }

}
