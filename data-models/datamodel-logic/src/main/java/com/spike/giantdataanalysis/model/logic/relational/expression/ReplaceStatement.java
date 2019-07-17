package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

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
    LOW_PRIORITY, DELAYED
  }

  public final PriorityEnum priority;
  public final TableName tableName;
  public final UidList partitions;
  public final InsertStatementValue insertStatementValue;
  public final List<UpdatedElement> setList;

  ReplaceStatement(PriorityEnum priority, TableName tableName, UidList partitions,
      InsertStatementValue insertStatementValue, List<UpdatedElement> setList) {
    Preconditions.checkArgument(tableName != null);
    Preconditions
        .checkArgument(!(insertStatementValue == null && (setList == null || setList.isEmpty())));

    this.priority = priority;
    this.tableName = tableName;
    this.partitions = partitions;
    this.insertStatementValue = insertStatementValue;
    this.setList = setList;
  }

}