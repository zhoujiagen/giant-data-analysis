package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
    EXPLAIN, DESCRIBE, DESC
  }

  public final CommandEnum command;
  public final TableName tableName;
  public final Uid column;
  public final String pattern;

  SimpleDescribeStatement(CommandEnum command, TableName tableName, Uid column, String pattern) {
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
    return sb.toString();
  }
}
