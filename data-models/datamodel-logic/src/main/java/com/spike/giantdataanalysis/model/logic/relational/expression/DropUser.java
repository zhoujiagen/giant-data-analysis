package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;

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
    return sb.toString();
  }
}
