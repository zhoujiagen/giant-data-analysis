package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 simpleDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC) tableName
      (column=uid | pattern=STRING_LITERAL)?
    ;
 * </pre>
 */
public class SimpleDescribeStatement implements UtilityStatement {
  public static enum CommandEnum implements RelationalAlgebraEnum {
    EXPLAIN, DESCRIBE, DESC;
    @Override
    public String literal() {
      return name();
    }
  }

  public final SimpleDescribeStatement.CommandEnum command;
  public final TableName tableName;
  public final Uid column;
  public final String pattern;

  SimpleDescribeStatement(SimpleDescribeStatement.CommandEnum command, TableName tableName,
      Uid column, String pattern) {
    Preconditions.checkArgument(command != null);
    Preconditions.checkArgument(tableName != null);

    this.command = command;
    this.tableName = tableName;
    this.column = column;
    this.pattern = pattern;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(command.literal()).append(" ").append(tableName.literal());
    if (column != null) {
      sb.append(" ").append(column.literal());
    } else if (pattern != null) {
      sb.append(" ").append(pattern);
    }
    return sb.toString();
  }
}
