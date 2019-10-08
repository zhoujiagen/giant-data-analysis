package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 useStatement
    : USE uid
    ;
 * </pre>
 */
public class UseStatement implements UtilityStatement {
  public final Uid uid;

  UseStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("USE ").append(uid.literal());
    return sb.toString();
  }
}
