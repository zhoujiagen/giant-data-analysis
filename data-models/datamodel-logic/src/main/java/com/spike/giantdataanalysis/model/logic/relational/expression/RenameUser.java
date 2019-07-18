package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

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
    return sb.toString();
  }
}
