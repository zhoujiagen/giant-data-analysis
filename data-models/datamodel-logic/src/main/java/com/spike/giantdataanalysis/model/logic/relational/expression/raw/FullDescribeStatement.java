package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;

/**
 * <pre>
 fullDescribeStatement
    : command=(EXPLAIN | DESCRIBE | DESC)
      (
        formatType=(EXTENDED | PARTITIONS | FORMAT )
        '='
        formatValue=(TRADITIONAL | JSON)
      )?
      describeObjectClause
    ;
 * </pre>
 */
public class FullDescribeStatement implements UtilityStatement {

  public static enum CommandEnum implements RelationalAlgebraEnum {
    EXPLAIN, DESCRIBE, DESC;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum FormatTypeEnum implements RelationalAlgebraEnum {
    EXTENDED, PARTITIONS, FORMAT;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum FormatValueEnum implements RelationalAlgebraEnum {
    TRADITIONAL, JSON;
    @Override
    public String literal() {
      return name();
    }
  }

  public final FullDescribeStatement.CommandEnum command;
  public final FullDescribeStatement.FormatTypeEnum formatType;
  public final FullDescribeStatement.FormatValueEnum formatValue;
  public final DescribeObjectClause describeObjectClause;

  FullDescribeStatement(FullDescribeStatement.CommandEnum command,
      FullDescribeStatement.FormatTypeEnum formatType,
      FullDescribeStatement.FormatValueEnum formatValue,
      DescribeObjectClause describeObjectClause) {
    Preconditions.checkArgument(command != null);
    Preconditions.checkArgument(describeObjectClause != null);

    this.command = command;
    this.formatType = formatType;
    this.formatValue = formatValue;
    this.describeObjectClause = describeObjectClause;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(command.literal()).append(" ");
    if (formatType != null) {
      sb.append(formatType.literal()).append(" = ").append(formatValue.literal()).append(" ");
    }
    sb.append(describeObjectClause.literal());
    return sb.toString();
  }
}
