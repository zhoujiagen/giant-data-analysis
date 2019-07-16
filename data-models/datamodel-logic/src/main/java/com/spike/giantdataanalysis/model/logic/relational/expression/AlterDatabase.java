package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 alterDatabase
    : ALTER dbFormat=(DATABASE | SCHEMA) uid?
      createDatabaseOption+                                         #alterSimpleDatabase
    | ALTER dbFormat=(DATABASE | SCHEMA) uid
      UPGRADE DATA DIRECTORY NAME                                   #alterUpgradeName
    ;
 * </pre>
 */
public interface AlterDatabase extends DdlStatement {

  public static class alterSimpleDatabase implements AlterDatabase {
    public final DbFormatEnum dbFormat;
    public final Uid uid;
    public final List<CreateDatabaseOption> createDatabaseOptions;

    alterSimpleDatabase(DbFormatEnum dbFormat, Uid uid,
        List<CreateDatabaseOption> createDatabaseOptions) {
      Preconditions.checkArgument(dbFormat != null);
      Preconditions
          .checkArgument(createDatabaseOptions != null && createDatabaseOptions.size() > 0);

      this.dbFormat = dbFormat;
      this.uid = uid;
      this.createDatabaseOptions = createDatabaseOptions;
    }

  }

  public static class alterUpgradeName implements AlterDatabase {
    public final DbFormatEnum dbFormat;
    public final Uid uid;

    alterUpgradeName(DbFormatEnum dbFormat, Uid uid) {
      Preconditions.checkArgument(dbFormat != null);
      Preconditions.checkArgument(uid != null);

      this.dbFormat = dbFormat;
      this.uid = uid;
    }

  }
}
