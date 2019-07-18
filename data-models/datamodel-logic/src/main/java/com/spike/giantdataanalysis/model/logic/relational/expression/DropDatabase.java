package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 dropDatabase
    : DROP dbFormat=(DATABASE | SCHEMA) ifExists? uid
    ;
 * </pre>
 */
public class DropDatabase implements DdlStatement {
  public final DbFormatEnum dbFormat;
  public final IfExists ifExists;
  public final Uid uid;

  DropDatabase(DbFormatEnum dbFormat, IfExists ifExists, Uid uid) {
    Preconditions.checkArgument(dbFormat != null);
    Preconditions.checkArgument(uid != null);

    this.dbFormat = dbFormat;
    this.ifExists = ifExists;
    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP ").append(dbFormat.name()).append(" ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(uid.literal());
    return sb.toString();
  }
}
