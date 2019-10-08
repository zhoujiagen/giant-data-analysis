package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ").append(dbFormat.literal()).append(" ");
    if (ifNotExists != null) {
      sb.append(ifNotExists.literal()).append(" ");
    }
    sb.append(uid.literal());
    if (CollectionUtils.isNotEmpty(createDatabaseOptions)) {
      List<String> literals = Lists.newArrayList();
      for (CreateDatabaseOption createDatabaseOption : createDatabaseOptions) {
        literals.add(createDatabaseOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    return sb.toString();
  }

}
