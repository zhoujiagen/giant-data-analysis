package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;

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
  public final List<UserName> tos;
  public final Boolean withGrantOption;

  GrantProxy(UserName fromFirst, List<UserName> tos, Boolean withGrantOption) {
    Preconditions.checkArgument(fromFirst != null);
    Preconditions.checkArgument(tos != null && tos.size() > 0);

    this.fromFirst = fromFirst;
    this.tos = tos;
    this.withGrantOption = withGrantOption;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("GRANT PROXY ON ").append(fromFirst.literal()).append(" ");
    sb.append("TO ");
    List<String> literals = Lists.newArrayList();
    for (UserName userName : tos) {
      literals.add(userName.literal());
    }
    sb.append(Joiner.on(", ").join(literals)).append(" ");
    if (Boolean.TRUE.equals(withGrantOption)) {
      sb.append("WITH GRANT OPTION");
    }
    return sb.toString();
  }
}
