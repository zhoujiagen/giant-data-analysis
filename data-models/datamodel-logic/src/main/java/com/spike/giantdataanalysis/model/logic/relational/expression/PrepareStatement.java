package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
    STRING_LITERAL, LOCAL_ID
  }

  public final Uid uid;
  public final Type type;
  public final String typeValue;

  PrepareStatement(Uid uid, Type type, String typeValue) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(type != null);
    Preconditions.checkArgument(typeValue != null);

    this.uid = uid;
    this.type = type;
    this.typeValue = typeValue;
  }

}
