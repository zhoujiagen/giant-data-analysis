package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;

/**
 * <pre>
 revokeProxy
    : REVOKE PROXY ON onUser=userName
      FROM fromFirst=userName (',' fromOther+=userName)*
    ;
 * </pre>
 */
public class RevokeProxy implements AdministrationStatement {
  public final UserName onUser;
  public final UserName fromFirst;
  public final List<UserName> fromOther;

  RevokeProxy(UserName onUser, UserName fromFirst, List<UserName> fromOther) {
    Preconditions.checkArgument(onUser != null);
    Preconditions.checkArgument(fromFirst != null);

    this.onUser = onUser;
    this.fromFirst = fromFirst;
    this.fromOther = fromOther;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
