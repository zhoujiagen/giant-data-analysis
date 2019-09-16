package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("RENAME TABLE ");
    List<String> literals = Lists.newArrayList();
    for (RenameTableClause renameTableClause : renameTableClauses) {
      literals.add(renameTableClause.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
