package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  public static class AlterSimpleDatabase implements AlterDatabase {
    public final DbFormatEnum dbFormat;
    public final Uid uid;
    public final List<CreateDatabaseOption> createDatabaseOptions;

    AlterSimpleDatabase(DbFormatEnum dbFormat, Uid uid,
        List<CreateDatabaseOption> createDatabaseOptions) {
      Preconditions.checkArgument(dbFormat != null);
      Preconditions
          .checkArgument(createDatabaseOptions != null && createDatabaseOptions.size() > 0);

      this.dbFormat = dbFormat;
      this.uid = uid;
      this.createDatabaseOptions = createDatabaseOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ALTER ").append(dbFormat.literal()).append(" ");
      if (uid != null) {
        sb.append(uid.literal()).append(" ");
      }
      List<String> literals = Lists.newArrayList();
      for (CreateDatabaseOption createDatabaseOption : createDatabaseOptions) {
        literals.add(createDatabaseOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
      return sb.toString();
    }
  }

  public static class AlterUpgradeName implements AlterDatabase {
    public final DbFormatEnum dbFormat;
    public final Uid uid;

    AlterUpgradeName(DbFormatEnum dbFormat, Uid uid) {
      Preconditions.checkArgument(dbFormat != null);
      Preconditions.checkArgument(uid != null);

      this.dbFormat = dbFormat;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ALTER ").append(dbFormat.literal()).append(" ").append(uid.literal()).append(" ");
      sb.append("UPGRADE DATA DIRECTORY NAME");
      return sb.toString();
    }

  }
}
