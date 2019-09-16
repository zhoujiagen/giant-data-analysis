package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;

/**
 * <pre>
 purgeBinaryLogs
    : PURGE purgeFormat=(BINARY | MASTER) LOGS
       (
           TO fileName=STRING_LITERAL
           | BEFORE timeValue=STRING_LITERAL
       )
    ;
 * </pre>
 */
public class PurgeBinaryLogs implements ReplicationStatement {
  public static enum PurgeFormatEnum implements RelationalAlgebraEnum {
    BINARY, MASTER;

    @Override
    public String literal() {
      return name();
    }
  }

  public static enum Type implements RelationalAlgebraEnum {
    TO_FILE, BEFORE_TIME;

    @Override
    public String literal() {
      return name();
    }
  }

  public final PurgeBinaryLogs.PurgeFormatEnum purgeFormat;
  public final PurgeBinaryLogs.Type type;
  public final String typeValue;

  PurgeBinaryLogs(PurgeBinaryLogs.PurgeFormatEnum purgeFormat, PurgeBinaryLogs.Type type,
      String typeValue) {
    Preconditions.checkArgument(purgeFormat != null);
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(typeValue != null);

    this.purgeFormat = purgeFormat;
    this.type = type;
    this.typeValue = typeValue;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("PURGE ").append(purgeFormat.literal()).append(" LOGS ");
    switch (type) {
    case TO_FILE:
      sb.append("TO ").append(typeValue);
      break;
    case BEFORE_TIME:
      sb.append("BEFORE ").append(typeValue);
      break;
    default:
      break;
    }
    return sb.toString();
  }
}
