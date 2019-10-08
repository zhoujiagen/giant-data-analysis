package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

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
    LOW_PRIORITY, DELAYED, HIGH_PRIORITY;

    @Override
    public String literal() {
      return name();
    }
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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT").append(" ");
    if (priority != null) {
      sb.append(priority.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(ignore)) {
      sb.append("IGNORE ");
    }
    if (Boolean.TRUE.equals(into)) {
      sb.append("INTO ");
    }
    sb.append(tableName.literal()).append(" ");
    if (partitions != null) {
      sb.append("PARTITION (").append(partitions.literal()).append(") ");
    }

    if (insertStatementValue != null) {
      if (columns != null) {
        sb.append("(").append(columns.literal()).append(") ");
      }
      sb.append(insertStatementValue.literal()).append(" ");
    } else {
      sb.append("SET ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : setList) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }

    if (CollectionUtils.isNotEmpty(duplicatedList)) {
      sb.append("ON DUPLICATE KEY UPDATE ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : duplicatedList) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    return sb.toString();
  }

}