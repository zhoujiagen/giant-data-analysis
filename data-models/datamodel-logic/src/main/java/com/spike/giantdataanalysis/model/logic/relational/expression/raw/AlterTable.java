package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER ");
    if (intimeAction != null) {
      sb.append(intimeAction.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(ignore)) {
      sb.append("IGNORE ");
    }
    sb.append("TABLE ").append(tableName.literal()).append(" ");
    if (CollectionUtils.isNotEmpty(alterSpecifications)) {
      List<String> literals = Lists.newArrayList();
      for (AlterSpecification alterSpecification : alterSpecifications) {
        literals.add(alterSpecification.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
    }
    if (partitionDefinitions != null) {
      sb.append(partitionDefinitions.literal());
    }
    return sb.toString();
  }
}
