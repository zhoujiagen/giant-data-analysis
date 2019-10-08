package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;

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
  public final List<UserName> froms;

  RevokeProxy(UserName onUser, List<UserName> froms) {
    Preconditions.checkArgument(onUser != null);
    Preconditions.checkArgument(froms != null && froms.size() > 0);

    this.onUser = onUser;
    this.froms = froms;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("REVOKE PROXY ON ").append(onUser.literal()).append(" ");
    sb.append("FROM ");
    List<String> literals = Lists.newArrayList();
    for (UserName userName : froms) {
      literals.add(userName.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
