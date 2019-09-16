package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
 showStatement
    : SHOW logFormat=(BINARY | MASTER) LOGS                         #showMasterLogs
    | SHOW logFormat=(BINLOG | RELAYLOG)
      EVENTS (IN filename=STRING_LITERAL)?
        (FROM fromPosition=decimalLiteral)?
        (LIMIT
          (offset=decimalLiteral ',')?
          rowCount=decimalLiteral
        )?                                                          #showLogEvents
    | SHOW showCommonEntity showFilter?                             #showObjectFilter
    | SHOW FULL? columnsFormat=(COLUMNS | FIELDS)
      tableFormat=(FROM | IN) tableName
        (schemaFormat=(FROM | IN) uid)? showFilter?                 #showColumns
    | SHOW CREATE schemaFormat=(DATABASE | SCHEMA)
      ifNotExists? uid                                              #showCreateDb
    | SHOW CREATE
        namedEntity=(
          EVENT | FUNCTION | PROCEDURE
          | TABLE | TRIGGER | VIEW
        )
        fullId                                                      #showCreateFullIdObject
    | SHOW CREATE USER userName                                     #showCreateUser
    | SHOW ENGINE engineName engineOption=(STATUS | MUTEX)          #showEngine
    | SHOW showGlobalInfoClause                                     #showGlobalInfo
    | SHOW errorFormat=(ERRORS | WARNINGS)
        (LIMIT
          (offset=decimalLiteral ',')?
          rowCount=decimalLiteral
        )                                                           #showErrors
    | SHOW COUNT '(' '*' ')' errorFormat=(ERRORS | WARNINGS)        #showCountErrors
    | SHOW showSchemaEntity
        (schemaFormat=(FROM | IN) uid)? showFilter?                 #showSchemaFilter
    | SHOW routine=(FUNCTION | PROCEDURE) CODE fullId               #showRoutine
    | SHOW GRANTS (FOR userName)?                                   #showGrants
    | SHOW indexFormat=(INDEX | INDEXES | KEYS)
      tableFormat=(FROM | IN) tableName
        (schemaFormat=(FROM | IN) uid)? (WHERE expression)?         #showIndexes
    | SHOW OPEN TABLES ( schemaFormat=(FROM | IN) uid)?
      showFilter?                                                   #showOpenTables
    | SHOW PROFILE showProfileType (',' showProfileType)*
        (FOR QUERY queryCount=decimalLiteral)?
        (LIMIT
          (offset=decimalLiteral ',')?
          rowCount=decimalLiteral
        )                                                           #showProfile
    | SHOW SLAVE STATUS (FOR CHANNEL STRING_LITERAL)?               #showSlaveStatus
    ;
 * </pre>
 */
public interface ShowStatement extends AdministrationStatement {
  public static class ShowMasterLogs implements ShowStatement {
    public static enum LogFormatEnum implements RelationalAlgebraEnum {
      BINARY, MASTER;

      @Override
      public String literal() {
        return name();
      }
    }

    public final LogFormatEnum logFormat;

    ShowMasterLogs(ShowMasterLogs.LogFormatEnum logFormat) {
      Preconditions.checkArgument(logFormat != null);

      this.logFormat = logFormat;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(logFormat.literal()).append(" LOGS");
      return sb.toString();
    }

  }

  public static class ShowLogEvents implements ShowStatement {
    public static enum LogFormatEnum implements RelationalAlgebraEnum {
      BINLOG, RELAYLOG;
      @Override
      public String literal() {
        return name();
      }
    }

    public final LogFormatEnum logFormat;
    public final String filename;
    public final DecimalLiteral fromPosition;
    public final DecimalLiteral offset;
    public final DecimalLiteral rowCount;

    ShowLogEvents(ShowLogEvents.LogFormatEnum logFormat, String filename,
        DecimalLiteral fromPosition, DecimalLiteral offset, DecimalLiteral rowCount) {
      Preconditions.checkArgument(logFormat != null);

      this.logFormat = logFormat;
      this.filename = filename;
      this.fromPosition = fromPosition;
      this.offset = offset;
      this.rowCount = rowCount;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(logFormat.literal()).append(" EVENTS ");
      if (filename != null) {
        sb.append("IN ").append(filename).append(" ");
      }
      if (fromPosition != null) {
        sb.append("FROM ").append(fromPosition.literal()).append(" ");
      }
      if (rowCount != null) {
        sb.append("LIMIT ");
        if (offset != null) {
          sb.append(offset.literal()).append(", ");
        }
        sb.append(rowCount.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowObjectFilter implements ShowStatement {
    public final ShowCommonEntityEnum showCommonEntity;
    public final ShowFilter showFilter;

    ShowObjectFilter(ShowCommonEntityEnum showCommonEntity, ShowFilter showFilter) {
      Preconditions.checkArgument(showCommonEntity != null);

      this.showCommonEntity = showCommonEntity;
      this.showFilter = showFilter;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(showCommonEntity.literal());
      if (showFilter != null) {
        sb.append(" ").append(showFilter.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowColumns implements ShowStatement {
    public static enum ColumnsFormatEnum implements RelationalAlgebraEnum {
      COLUMNS, FIELDS;
      @Override
      public String literal() {
        return name();
      }
    }

    public static enum TableFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public static enum SchemaFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Boolean full;
    public final ColumnsFormatEnum columnsFormat;
    public final TableFormatEnum tableFormat;
    public final TableName tableName;
    public final SchemaFormatEnum schemaFormat;
    public final Uid uid;
    public final ShowFilter showFilter;

    ShowColumns(Boolean full, ShowColumns.ColumnsFormatEnum columnsFormat,
        ShowColumns.TableFormatEnum tableFormat, TableName tableName,
        ShowColumns.SchemaFormatEnum schemaFormat, Uid uid, ShowFilter showFilter) {
      Preconditions.checkArgument(columnsFormat != null);
      Preconditions.checkArgument(tableFormat != null);
      Preconditions.checkArgument(tableName != null);

      this.full = full;
      this.columnsFormat = columnsFormat;
      this.tableFormat = tableFormat;
      this.tableName = tableName;
      this.schemaFormat = schemaFormat;
      this.uid = uid;
      this.showFilter = showFilter;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ");
      if (Boolean.TRUE.equals(full)) {
        sb.append("FULL ");
      }
      sb.append(columnsFormat.literal()).append(" ");
      if (uid != null) {
        sb.append(schemaFormat.literal()).append(" ").append(uid.literal());
      }
      if (showFilter != null) {
        sb.append(showFilter.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowCreateDb implements ShowStatement {
    public static enum SchemaFormatEnum implements RelationalAlgebraEnum {
      DATABASE, SCHEMA;
      @Override
      public String literal() {
        return name();
      }
    }

    public final SchemaFormatEnum schemaFormat;
    public final IfNotExists ifNotExists;
    public final Uid uid;

    ShowCreateDb(ShowCreateDb.SchemaFormatEnum schemaFormat, IfNotExists ifNotExists, Uid uid) {
      Preconditions.checkArgument(schemaFormat != null);
      Preconditions.checkArgument(uid != null);

      this.schemaFormat = schemaFormat;
      this.ifNotExists = ifNotExists;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW CREATE ").append(schemaFormat.literal());
      if (ifNotExists != null) {
        sb.append(ifNotExists.literal()).append(" ");
      }
      sb.append(uid.literal());
      return sb.toString();
    }

  }

  public static class ShowCreateFullIdObject implements ShowStatement {
    public static enum NamedEntityEnum implements RelationalAlgebraEnum {
      EVENT, FUNCTION, PROCEDURE, TABLE, TRIGGER, VIEW;
      @Override
      public String literal() {
        return name();
      }
    }

    public final NamedEntityEnum namedEntity;
    public final FullId fullId;

    ShowCreateFullIdObject(ShowCreateFullIdObject.NamedEntityEnum namedEntity, FullId fullId) {
      Preconditions.checkArgument(namedEntity != null);
      Preconditions.checkArgument(fullId != null);

      this.namedEntity = namedEntity;
      this.fullId = fullId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW CREATE ").append(namedEntity.literal());
      sb.append(" ").append(fullId.literal());
      return sb.toString();
    }

  }

  public static class ShowCreateUser implements ShowStatement {
    public final UserName userName;

    ShowCreateUser(UserName userName) {
      Preconditions.checkArgument(userName != null);
      this.userName = userName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW CREATE USER ").append(userName.literal());
      return sb.toString();
    }

  }

  public static class ShowEngine implements ShowStatement {
    public static enum EngineOptionEnum implements RelationalAlgebraEnum {
      STATUS, MUTEX;
      @Override
      public String literal() {
        return name();
      }
    }

    public final EngineName engineName;
    public final EngineOptionEnum engineOption;

    ShowEngine(EngineName engineName, ShowEngine.EngineOptionEnum engineOption) {
      Preconditions.checkArgument(engineName != null);
      Preconditions.checkArgument(engineOption != null);

      this.engineName = engineName;
      this.engineOption = engineOption;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ENGINE ").append(engineName.literal());
      sb.append(" ").append(engineOption.literal());
      return sb.toString();
    }

  }

  public static class ShowGlobalInfo implements ShowStatement {
    public final ShowGlobalInfoClauseEnum showGlobalInfoClause;

    ShowGlobalInfo(ShowGlobalInfoClauseEnum showGlobalInfoClause) {
      Preconditions.checkArgument(showGlobalInfoClause != null);

      this.showGlobalInfoClause = showGlobalInfoClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(showGlobalInfoClause.literal());
      return sb.toString();
    }

  }

  public static class ShowErrors implements ShowStatement {
    public static enum ErrorFormatEnum implements RelationalAlgebraEnum {
      ERRORS, WARNINGS;
      @Override
      public String literal() {
        return name();
      }
    }

    public final ErrorFormatEnum errorFormat;
    public final DecimalLiteral offset;
    public final DecimalLiteral rowCount;

    ShowErrors(ShowErrors.ErrorFormatEnum errorFormat, DecimalLiteral offset,
        DecimalLiteral rowCount) {
      Preconditions.checkArgument(errorFormat != null);
      Preconditions.checkArgument(rowCount != null);

      this.errorFormat = errorFormat;
      this.offset = offset;
      this.rowCount = rowCount;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(errorFormat.literal()).append(" ");
      sb.append("LIMIT ");
      if (offset != null) {
        sb.append(offset.literal()).append(", ");
      }
      sb.append(rowCount.literal());
      return sb.toString();
    }

  }

  public static class ShowCountErrors implements ShowStatement {
    public static enum ErrorFormatEnum implements RelationalAlgebraEnum {
      ERRORS, WARNINGS;
      @Override
      public String literal() {
        return name();
      }
    }

    public final ErrorFormatEnum errorFormat;

    ShowCountErrors(ShowCountErrors.ErrorFormatEnum errorFormat) {
      Preconditions.checkArgument(errorFormat != null);

      this.errorFormat = errorFormat;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW COUNT(*) ").append(errorFormat.literal());
      return sb.toString();
    }
  }

  public static class ShowSchemaFilter implements ShowStatement {
    public static enum SchemaFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public final ShowSchemaEntityEnum showSchemaEntity;
    public final SchemaFormatEnum schemaFormat;
    public final Uid uid;
    public final ShowFilter showFilter;

    ShowSchemaFilter(ShowSchemaFilter.ShowSchemaEntityEnum showSchemaEntity,
        SchemaFormatEnum schemaFormat, Uid uid, ShowFilter showFilter) {
      Preconditions.checkArgument(showSchemaEntity != null);

      this.showSchemaEntity = showSchemaEntity;
      this.schemaFormat = schemaFormat;
      this.uid = uid;
      this.showFilter = showFilter;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(showSchemaEntity.literal()).append(" ");
      if (uid != null) {
        sb.append(schemaFormat.literal()).append(" ").append(uid.literal());
      }
      if (showFilter != null) {
        sb.append(" ").append(showFilter.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowRoutine implements ShowStatement {
    public static enum RoutineEnum implements RelationalAlgebraEnum {
      FUNCTION, PROCEDURE;
      @Override
      public String literal() {
        return name();
      }
    }

    public final RoutineEnum routine;
    public final FullId fullId;

    ShowRoutine(ShowRoutine.RoutineEnum routine, FullId fullId) {
      Preconditions.checkArgument(routine != null);
      Preconditions.checkArgument(fullId != null);

      this.routine = routine;
      this.fullId = fullId;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(routine.literal()).append(" CODE ").append(fullId.literal());
      return sb.toString();
    }

  }

  public static class ShowGrants implements ShowStatement {
    public final UserName userName;

    ShowGrants(UserName userName) {
      this.userName = userName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW GRANTS");
      if (userName != null) {
        sb.append(" FOR ").append(userName.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowIndexes implements ShowStatement {
    public static enum IndexFormatEnum implements RelationalAlgebraEnum {
      INDEX, INDEXES, KEYS;
      @Override
      public String literal() {
        return name();
      }
    }

    public static enum TableFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public static enum SchemaFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public final IndexFormatEnum indexFormat;
    public final TableFormatEnum tableFormat;
    public final TableName tableName;
    public final SchemaFormatEnum schemaFormat;
    public final Uid uid;
    public final Expression where;

    ShowIndexes(ShowIndexes.IndexFormatEnum indexFormat, ShowIndexes.TableFormatEnum tableFormat,
        TableName tableName, ShowIndexes.SchemaFormatEnum schemaFormat, Uid uid, Expression where) {
      Preconditions.checkArgument(indexFormat != null);
      Preconditions.checkArgument(tableFormat != null);
      Preconditions.checkArgument(tableName != null);

      this.indexFormat = indexFormat;
      this.tableFormat = tableFormat;
      this.tableName = tableName;
      this.schemaFormat = schemaFormat;
      this.uid = uid;
      this.where = where;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW ").append(indexFormat.literal()).append(" ");
      sb.append(tableFormat.literal()).append(" ").append(tableName.literal());
      if (uid != null) {
        sb.append(" ").append(schemaFormat.literal()).append(" ").append(uid.literal());
      }
      if (where != null) {
        sb.append(" WHERE ").append(where.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowOpenTables implements ShowStatement {
    public static enum SchemaFormatEnum implements RelationalAlgebraEnum {
      FROM, IN;
      @Override
      public String literal() {
        return name();
      }
    }

    public final SchemaFormatEnum schemaFormat;
    public final Uid uid;
    public final ShowFilter showFilter;

    ShowOpenTables(ShowOpenTables.SchemaFormatEnum schemaFormat, Uid uid, ShowFilter showFilter) {
      this.schemaFormat = schemaFormat;
      this.uid = uid;
      this.showFilter = showFilter;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW OPEN TABLES");
      if (uid != null) {
        sb.append(" ").append(schemaFormat.literal()).append(" ").append(uid.literal());
      }
      if (showFilter != null) {
        sb.append(" ").append(showFilter.literal());
      }
      return sb.toString();
    }

  }

  public static class ShowProfile implements ShowStatement {
    public final List<ShowProfileTypeEnum> showProfileTypes;
    public final DecimalLiteral queryCount;
    public final DecimalLiteral offset;
    public final DecimalLiteral rowCount;

    ShowProfile(List<ShowProfileTypeEnum> showProfileTypes, DecimalLiteral queryCount,
        DecimalLiteral offset, DecimalLiteral rowCount) {
      Preconditions.checkArgument(showProfileTypes != null && showProfileTypes.size() > 0);
      Preconditions.checkArgument(rowCount != null);

      this.showProfileTypes = showProfileTypes;
      this.queryCount = queryCount;
      this.offset = offset;
      this.rowCount = rowCount;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW PROFILE ");
      List<String> literals = Lists.newArrayList();
      for (ShowProfileTypeEnum showProfileType : showProfileTypes) {
        literals.add(showProfileType.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
      if (queryCount != null) {
        sb.append("FOR QUERY ").append(queryCount.literal()).append(" ");
      }
      sb.append("LIMIT ");
      if (offset != null) {
        sb.append(offset.literal()).append(", ");
      }
      sb.append(rowCount.literal());
      return sb.toString();
    }

  }

  public static class ShowSlaveStatus implements ShowStatement {
    public final String channel;

    ShowSlaveStatus(String channel) {
      this.channel = channel;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SHOW SLAVE STATUS");
      if (channel != null) {
        sb.append(" FOR CHANNEL ").append(channel);
      }
      return sb.toString();
    }

  }

}
