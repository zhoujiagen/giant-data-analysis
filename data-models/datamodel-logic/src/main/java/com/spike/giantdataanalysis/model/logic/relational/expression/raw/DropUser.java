package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;

/**
 * <pre>
 dropUser
    : DROP USER ifExists? userName (',' userName)*
    ;
 * </pre>
 */
public class DropUser implements AdministrationStatement {
  public final IfExists ifExists;
  public final List<UserName> userNames;

  DropUser(IfExists ifExists, List<UserName> userNames) {
    Preconditions.checkArgument(userNames != null && userNames.size() > 0);

    this.ifExists = ifExists;
    this.userNames = userNames;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP USER ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    List<String> literals = Lists.newArrayList();
    for (UserName userName : userNames) {
      literals.add(userName.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
