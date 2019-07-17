package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

/**
 * <pre>
insertStatement
  : INSERT
    priority=(LOW_PRIORITY | DELAYED | HIGH_PRIORITY)?
    IGNORE? INTO? tableName
    (PARTITION '(' partitions=uidList ')' )?
    (
      ('(' columns=uidList ')')? insertStatementValue
      | SET
          setFirst=updatedElement
          (',' setElements+=updatedElement)*
    )
    (
      ON DUPLICATE KEY UPDATE
      duplicatedFirst=updatedElement
      (',' duplicatedElements+=updatedElement)*
    )?
  ;
 * </pre>
 */
public class InsertStatement implements DmlStatement {
  public static enum PriorityType implements RelationalAlgebraEnum {
    LOW_PRIORITY, DELAYED, HIGH_PRIORITY
  }

  public final PriorityType priority;
  public final Boolean ignore;
  public final Boolean into;
  public final TableName tableName;
  public final UidList partitions;

  public final UidList columns;
  public final InsertStatementValue insertStatementValue;
  public final List<UpdatedElement> setList;

  public final List<UpdatedElement> duplicatedList;

  InsertStatement(PriorityType priority, Boolean ignore, Boolean into, TableName tableName,
      UidList partitions, UidList columns, InsertStatementValue insertStatementValue,
      List<UpdatedElement> setList, List<UpdatedElement> duplicatedList) {
    Preconditions.checkArgument(tableName != null);
    Preconditions
        .checkArgument(!(insertStatementValue == null && (setList == null || setList.isEmpty())));

    this.priority = priority;
    this.ignore = ignore;
    this.into = into;
    this.tableName = tableName;
    this.partitions = partitions;
    this.columns = columns;
    this.insertStatementValue = insertStatementValue;
    this.setList = setList;
    this.duplicatedList = duplicatedList;
  }

}