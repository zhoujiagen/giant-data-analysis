package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;

/**
 * <pre>
 grantProxy
    : GRANT PROXY ON fromFirst=userName
      TO toFirst=userName (',' toOther+=userName)*
      (WITH GRANT OPTION)?
    ;
 * </pre>
 */
public class GrantProxy implements AdministrationStatement {
  public final UserName fromFirst;
  public final UserName toFirst;
  public final List<UserName> toOther;
  public final Boolean withGrantOption;

  GrantProxy(UserName fromFirst, UserName toFirst, List<UserName> toOther,
      Boolean withGrantOption) {
    Preconditions.checkArgument(fromFirst != null);
    Preconditions.checkArgument(toFirst != null);

    this.fromFirst = fromFirst;
    this.toFirst = toFirst;
    this.toOther = toOther;
    this.withGrantOption = withGrantOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
