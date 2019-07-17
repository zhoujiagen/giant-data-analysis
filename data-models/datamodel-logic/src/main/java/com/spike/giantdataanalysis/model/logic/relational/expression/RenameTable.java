package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
renameTable
    : RENAME TABLE
    renameTableClause (',' renameTableClause)*
    ;
 * </pre>
 */
public class RenameTable implements DdlStatement {
  public final List<RenameTableClause> renameTableClauses;

  RenameTable(List<RenameTableClause> renameTableClauses) {
    Preconditions.checkArgument(renameTableClauses != null && renameTableClauses.size() > 0);

    this.renameTableClauses = renameTableClauses;
  }

}
