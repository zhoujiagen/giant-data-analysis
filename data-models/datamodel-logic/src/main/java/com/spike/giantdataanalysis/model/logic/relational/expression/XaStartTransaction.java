package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

/**
 * <pre>
 xaStartTransaction
    : XA xaStart=(START | BEGIN) xid xaAction=(JOIN | RESUME)?
    ;
 * </pre>
 */
public class XaStartTransaction implements ReplicationStatement {
  public static enum XaStartEnum implements RelationalAlgebraEnum {
    START, BEGIN
  }

  public static enum XaActionEnum implements RelationalAlgebraEnum {
    JOIN, RESUME
  }

  public final XaStartEnum xaStart;
  public final Xid xid;
  public final XaActionEnum xaAction;

  XaStartTransaction(XaStartEnum xaStart, Xid xid, XaActionEnum xaAction) {
    Preconditions.checkArgument(xaStart != null);
    Preconditions.checkArgument(xid != null);

    this.xaStart = xaStart;
    this.xid = xid;
    this.xaAction = xaAction;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
