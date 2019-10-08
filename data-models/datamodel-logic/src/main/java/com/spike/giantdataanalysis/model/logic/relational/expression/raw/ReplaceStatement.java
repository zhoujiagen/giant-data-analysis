package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

/**
 * <pre>
replaceStatement
  : REPLACE priority=(LOW_PRIORITY | DELAYED)?
    INTO? tableName
    (PARTITION '(' partitions=uidList ')' )?
    (
      ('(' columns=uidList ')')? insertStatementValue
      | SET
        setFirst=updatedElement
        (',' setElements+=updatedElement)*
    )
  ;
 * </pre>
 */
public class ReplaceStatement implements DmlStatement {
  public static enum PriorityEnum implements RelationalAlgebraEnum {
    LOW_PRIORITY, DELAYED;

    @Override
    public String literal() {
      return name();
    }
  }

  public final PriorityEnum priority;
  public final TableName tableName;
  public final UidList partitions;
  public final UidList columns;
  public final InsertStatementValue insertStatementValue;
  public final List<UpdatedElement> setList;

  ReplaceStatement(PriorityEnum priority, TableName tableName, UidList partitions, UidList columns,
      InsertStatementValue insertStatementValue, List<UpdatedElement> setList) {
    Preconditions.checkArgument(tableName != null);
    Preconditions
        .checkArgument(!(insertStatementValue == null && (setList == null || setList.isEmpty())));

    this.priority = priority;
    this.tableName = tableName;
    this.partitions = partitions;
    this.columns = columns;
    this.insertStatementValue = insertStatementValue;
    this.setList = setList;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("REPLACE ");
    if (priority != null) {
      sb.append(priority.literal()).append(" ");
    }
    sb.append("INTO ").append(tableName.literal()).append(" ");
    if (partitions != null) {
      sb.append("PARTITION (").append(partitions.literal()).append(") ");
    }
    if (insertStatementValue != null) {
      if (columns != null) {
        sb.append("(").append(columns.literal()).append(") ");
      }
      sb.append(insertStatementValue.literal());
    } else {
      sb.append("SET ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : setList) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }

    return sb.toString();
  }
}