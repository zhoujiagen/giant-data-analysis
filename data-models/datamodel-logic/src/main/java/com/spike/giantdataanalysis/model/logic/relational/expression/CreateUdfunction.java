package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 createUdfunction
    : CREATE AGGREGATE? FUNCTION uid
      RETURNS returnType=(STRING | INTEGER | REAL | DECIMAL)
      SONAME STRING_LITERAL
    ;
 * </pre>
 */
public class CreateUdfunction implements AdministrationStatement {

  public static enum ReturnTypeEnum implements RelationalAlgebraEnum {
    STRING, INTEGER, REAL, DECIMAL
  }

  public final Boolean aggregate;
  public final Uid uid;
  public final ReturnTypeEnum returnType;
  public final String soName;

  CreateUdfunction(Boolean aggregate, Uid uid, ReturnTypeEnum returnType, String soName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(returnType != null);
    Preconditions.checkArgument(soName != null);

    this.aggregate = aggregate;
    this.uid = uid;
    this.returnType = returnType;
    this.soName = soName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
