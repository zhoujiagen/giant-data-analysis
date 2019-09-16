package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 prepareStatement
    : PREPARE uid FROM
      (query=STRING_LITERAL | variable=LOCAL_ID)
    ;
 * </pre>
 */
public class PrepareStatement implements PreparedStatement {
  public static enum Type implements RelationalAlgebraEnum {
    STRING_LITERAL, LOCAL_ID;

    @Override
    public String literal() {
      return name();
    }
  }

  public final Uid uid;
  public final PrepareStatement.Type type;
  public final String typeValue;

  PrepareStatement(Uid uid, PrepareStatement.Type type, String typeValue) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(typeValue != null);

    this.uid = uid;
    this.type = type;
    this.typeValue = typeValue;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("PREPARE ").append(uid.literal()).append(" FROM ");
    sb.append(typeValue);
    return sb.toString();
  }
}
