package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

/**
 * <pre>
 createTable
    : CREATE TEMPORARY? TABLE ifNotExists?
       tableName
       (
         LIKE tableName
         | '(' LIKE parenthesisTable=tableName ')'
       )                                                            #copyCreateTable
    | CREATE TEMPORARY? TABLE ifNotExists?
       tableName createDefinitions?
       ( tableOption (','? tableOption)* )?
       partitionDefinitions? keyViolate=(IGNORE | REPLACE)?
       AS? selectStatement                                          #queryCreateTable
    | CREATE TEMPORARY? TABLE ifNotExists?
       tableName createDefinitions
       ( tableOption (','? tableOption)* )?
       partitionDefinitions?                                        #columnCreateTable
    ;
 * </pre>
 */
public interface CreateTable extends DdlStatement {

  public static class CopyCreateTable implements CreateTable {
    public final Boolean temporary;
    public final IfNotExists ifNotExists;
    public final TableName tableName;
    public final TableName likeTableName;

    CopyCreateTable(Boolean temporary, IfNotExists ifNotExists, TableName tableName,
        TableName likeTableName) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(likeTableName != null);

      this.temporary = temporary;
      this.ifNotExists = ifNotExists;
      this.tableName = tableName;
      this.likeTableName = likeTableName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CREATE ");
      if (Boolean.TRUE.equals(temporary)) {
        sb.append("TEMPORARY ");
      }
      sb.append("TABLE ");
      if (ifNotExists != null) {
        sb.append(ifNotExists.literal()).append(" ");
      }
      sb.append(tableName.literal()).append(" ");
      sb.append("LIKE ").append(likeTableName.literal());
      return sb.toString();
    }

  }

  public static class QueryCreateTable implements CreateTable {
    public static enum KeyViolateEnum implements RelationalAlgebraEnum {
      IGNORE, REPLACE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final Boolean temporary;
    public final IfNotExists ifNotExists;
    public final TableName tableName;
    public final CreateDefinitions createDefinitions;
    public final List<TableOption> tableOptions;
    public final PartitionDefinitions partitionDefinitions;
    public final QueryCreateTable.KeyViolateEnum keyViolate;
    public final SelectStatement selectStatement;

    QueryCreateTable(Boolean temporary, IfNotExists ifNotExists, TableName tableName,
        CreateDefinitions createDefinitions, List<TableOption> tableOptions,
        PartitionDefinitions partitionDefinitions, KeyViolateEnum keyViolate,
        SelectStatement selectStatement) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(selectStatement != null);

      this.temporary = temporary;
      this.ifNotExists = ifNotExists;
      this.tableName = tableName;
      this.createDefinitions = createDefinitions;
      this.tableOptions = tableOptions;
      this.partitionDefinitions = partitionDefinitions;
      this.keyViolate = keyViolate;
      this.selectStatement = selectStatement;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CREATE ");
      if (Boolean.TRUE.equals(temporary)) {
        sb.append("TEMPORARY ");
      }
      sb.append("TABLE ");
      if (ifNotExists != null) {
        sb.append(ifNotExists.literal()).append(" ");
      }
      sb.append(tableName.literal()).append(" ");
      if (createDefinitions != null) {
        sb.append(createDefinitions.literal()).append(" ");
      }
      if (CollectionUtils.isNotEmpty(tableOptions)) {
        List<String> literals = Lists.newArrayList();
        for (TableOption tableOption : tableOptions) {
          literals.add(tableOption.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      if (partitionDefinitions != null) {
        sb.append(partitionDefinitions.literal()).append(" ");
      }
      if (keyViolate != null) {
        sb.append(keyViolate.literal()).append(" ");
      }
      sb.append("AS ").append(selectStatement.literal());
      return sb.toString();
    }

  }

  public static class ColumnCreateTable implements CreateTable {
    public final Boolean temporary;
    public final IfNotExists ifNotExists;
    public final TableName tableName;
    public final CreateDefinitions createDefinitions;
    public final List<TableOption> tableOptions;
    public final PartitionDefinitions partitionDefinitions;

    ColumnCreateTable(Boolean temporary, IfNotExists ifNotExists, TableName tableName,
        CreateDefinitions createDefinitions, List<TableOption> tableOptions,
        PartitionDefinitions partitionDefinitions) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(createDefinitions != null);

      this.temporary = temporary;
      this.ifNotExists = ifNotExists;
      this.tableName = tableName;
      this.createDefinitions = createDefinitions;
      this.tableOptions = tableOptions;
      this.partitionDefinitions = partitionDefinitions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CREATE ");
      if (Boolean.TRUE.equals(temporary)) {
        sb.append("TEMPORARY ");
      }
      sb.append("TABLE ");
      if (ifNotExists != null) {
        sb.append(ifNotExists.literal()).append(" ");
      }
      sb.append(tableName.literal()).append(" ");
      sb.append(createDefinitions.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(tableOptions)) {
        List<String> literals = Lists.newArrayList();
        for (TableOption tableOption : tableOptions) {
          literals.add(tableOption.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      if (partitionDefinitions != null) {
        sb.append(partitionDefinitions.literal());
      }
      return sb.toString();
    }
  }
}
