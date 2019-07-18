package com.spike.giantdataanalysis.model.logic.relational.expression;

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
    BINARY, MASTER
  }

  public static enum Type implements RelationalAlgebraEnum {
    TO_FILE, BEFORE_TIME
  }

  public final PurgeFormatEnum purgeFormat;
  public final String fileName;
  public final Type type;
  public final String typeValue;

  PurgeBinaryLogs(PurgeFormatEnum purgeFormat, String fileName, Type type, String typeValue) {
    Preconditions.checkArgument(purgeFormat != null);
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(typeValue != null);

    this.purgeFormat = purgeFormat;
    this.fileName = fileName;
    this.type = type;
    this.typeValue = typeValue;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
