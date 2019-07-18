package com.spike.giantdataanalysis.model.logic.relational.expression;

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
    EXPLAIN, DESCRIBE, DESC
  }

  public static enum FormatTypeEnum implements RelationalAlgebraEnum {
    EXTENDED, PARTITIONS, FORMAT
  }

  public static enum FormatValueEnum implements RelationalAlgebraEnum {
    TRADITIONAL, JSON
  }

  public final CommandEnum command;
  public final FormatTypeEnum formatType;
  public final FormatValueEnum formatValue;
  public final DescribeObjectClause describeObjectClause;

  FullDescribeStatement(CommandEnum command, FormatTypeEnum formatType, FormatValueEnum formatValue,
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
    return sb.toString();
  }
}
