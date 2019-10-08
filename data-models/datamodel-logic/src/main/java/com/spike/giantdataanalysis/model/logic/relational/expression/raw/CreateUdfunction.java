package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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
    STRING, INTEGER, REAL, DECIMAL;

    @Override
    public String literal() {
      return name();
    }
  }

  public final Boolean aggregate;
  public final Uid uid;
  public final CreateUdfunction.ReturnTypeEnum returnType;
  public final String soName;

  CreateUdfunction(Boolean aggregate, Uid uid, CreateUdfunction.ReturnTypeEnum returnType,
      String soName) {
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
    sb.append("CREATE ");
    if (Boolean.TRUE.equals(aggregate)) {
      sb.append("AGGREGATE ");
    }
    sb.append("FUNCTION ").append(uid.literal()).append(" ");
    sb.append("RETURNS ").append(returnType.literal()).append(" ");
    sb.append("SONAME ").append(soName);
    return sb.toString();
  }
}
