package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * <pre>
 renameUser
    : RENAME USER
      renameUserClause (',' renameUserClause)*
    ;
 * </pre>
 */
public class RenameUser implements AdministrationStatement {

  public final List<RenameUserClause> renameUserClauses;

  RenameUser(List<RenameUserClause> renameUserClauses) {
    Preconditions.checkArgument(renameUserClauses != null && renameUserClauses.size() > 0);
    this.renameUserClauses = renameUserClauses;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("RENAME USER ");
    List<String> literals = Lists.newArrayList();
    for (RenameUserClause renameUserClause : renameUserClauses) {
      literals.add(renameUserClause.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
