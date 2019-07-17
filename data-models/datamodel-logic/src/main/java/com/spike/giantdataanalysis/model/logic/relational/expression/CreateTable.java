package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

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

  }

  public static class QueryCreateTable implements CreateTable {
    public static enum KeyViolateEnum implements RelationalAlgebraEnum {
      IGNORE, REPLACE
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

  }
}
