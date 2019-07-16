package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 createDatabase
    : CREATE dbFormat=(DATABASE | SCHEMA)
      ifNotExists? uid createDatabaseOption*
    ;
 * </pre>
 */
public class CreateDatabase implements DdlStatement {

  public final DbFormatEnum dbFormat;
  public final IfNotExists ifNotExists;
  public final Uid uid;
  public final List<CreateDatabaseOption> createDatabaseOptions;

  CreateDatabase(DbFormatEnum dbFormat, IfNotExists ifNotExists, Uid uid,
      List<CreateDatabaseOption> createDatabaseOptions) {
    Preconditions.checkArgument(dbFormat != null);
    Preconditions.checkArgument(uid != null);

    this.dbFormat = dbFormat;
    this.ifNotExists = ifNotExists;
    this.uid = uid;
    this.createDatabaseOptions = createDatabaseOptions;
  }

}
