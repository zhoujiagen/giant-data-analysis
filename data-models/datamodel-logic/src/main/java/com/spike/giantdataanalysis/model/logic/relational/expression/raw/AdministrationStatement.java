package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.AuthPlugin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement.IndexFormatEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ChannelOption;

/**
 * <pre>
administrationStatement
  : alterUser | createUser | dropUser | grantStatement
  | grantProxy | renameUser | revokeStatement
  | revokeProxy | analyzeTable | checkTable
  | checksumTable | optimizeTable | repairTable
  | createUdfunction | installPlugin | uninstallPlugin
  | setStatement | showStatement | binlogStatement
  | cacheIndexStatement | flushStatement | killStatement
  | loadIndexIntoCache | resetStatement
  | shutdownStatement
  ;
 * </pre>
 */
public interface AdministrationStatement extends SqlStatement {
  public static enum PrivilegeObjectEnum implements RelationalAlgebraEnum {
    TABLE, FUNCTION, PROCEDURE;

    @Override
    public String literal() {
      return name();
    }
  }

  public static enum AdminTableActionOptionEnum implements RelationalAlgebraEnum {
    NO_WRITE_TO_BINLOG, LOCAL;

    @Override
    public String literal() {
      return name();
    }
  }

  public static enum FlushFormatEnum implements RelationalAlgebraEnum {
    NO_WRITE_TO_BINLOG, LOCAL;

    @Override
    public String literal() {
      return name();
    }
  }

  public static enum ConnectionFormatEnum implements RelationalAlgebraEnum {
    CONNECTION, QUERY;

    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
   userSpecification
    : userName userPasswordOption
    ;
   * </pre>
   */
  public static class UserSpecification implements PrimitiveExpression {
    public final UserName userName;
    public final UserPasswordOption userPasswordOption;

    UserSpecification(UserName userName, UserPasswordOption userPasswordOption) {
      Preconditions.checkArgument(userName != null);
      Preconditions.checkArgument(userPasswordOption != null);

      this.userName = userName;
      this.userPasswordOption = userPasswordOption;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(userName.literal()).append(" ").append(userPasswordOption.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   userAuthOption
    : userName IDENTIFIED BY PASSWORD hashed=STRING_LITERAL         #passwordAuthOption
    | userName
      IDENTIFIED (WITH authPlugin)? BY STRING_LITERAL               #stringAuthOption
    | userName
      IDENTIFIED WITH authPlugin
      (AS STRING_LITERAL)?                                          #hashAuthOption
    | userName                                                      #simpleAuthOption
    ;
   * </pre>
   */
  public static interface UserAuthOption extends PrimitiveExpression {
  }

  public static class PasswordAuthOption implements UserAuthOption {
    public final UserName userName;
    public final String hashed;

    PasswordAuthOption(UserName userName, String hashed) {
      Preconditions.checkArgument(userName != null);
      Preconditions.checkArgument(hashed != null);

      this.userName = userName;
      this.hashed = hashed;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(userName.literal()).append(" IDENTIFIED BY PASSWORD ").append(hashed);
      return sb.toString();
    }

  }

  public static class StringAuthOption implements UserAuthOption {
    public final UserName userName;
    public final AuthPlugin authPlugin;
    public final String by;

    StringAuthOption(UserName userName, AuthPlugin authPlugin, String by) {
      Preconditions.checkArgument(userName != null);
      Preconditions.checkArgument(by != null);

      this.userName = userName;
      this.authPlugin = authPlugin;
      this.by = by;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(userName.literal()).append(" IDENTIFIED ");
      if (authPlugin != null) {
        sb.append("WITH ").append(authPlugin.literal()).append(" ");
      }
      sb.append("BY ").append(by);
      return sb.toString();
    }

  }

  public static class HashAuthOption implements UserAuthOption {
    public final UserName userName;
    public final AuthPlugin authPlugin;
    public final String as;

    HashAuthOption(UserName userName, AuthPlugin authPlugin, String as) {
      Preconditions.checkArgument(userName != null);
      Preconditions.checkArgument(authPlugin != null);

      this.userName = userName;
      this.authPlugin = authPlugin;
      this.as = as;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(userName.literal()).append(" IDENTIFIED WITH ").append(authPlugin.literal());
      if (as != null) {
        sb.append(" AS ").append(as);
      }
      return sb.toString();
    }

  }

  public static class SimpleAuthOption implements UserAuthOption {
    public final UserName userName;

    SimpleAuthOption(UserName userName) {
      Preconditions.checkArgument(userName != null);

      this.userName = userName;
    }

    @Override
    public String literal() {
      return userName.literal();
    }
  }

  /**
   * <pre>
   tlsOption
    : SSL
    | X509
    | CIPHER STRING_LITERAL
    | ISSUER STRING_LITERAL
    | SUBJECT STRING_LITERAL
    ;
   * </pre>
   */
  public static class TlsOption implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      SSL, X509, CIPHER, ISSUER, SUBJECT;
      @Override
      public String literal() {
        return name();
      }
    }

    public final TlsOption.Type type;
    public final String value;

    TlsOption(TlsOption.Type type, String value) {
      Preconditions.checkArgument(type != null);
      switch (type) {
      case SSL:
      case X509:
        break;
      case CIPHER:
      case ISSUER:
      case SUBJECT:
        Preconditions.checkArgument(value != null);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.value = value;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case SSL:
      case X509:
        sb.append(type.literal());
        break;
      case CIPHER:
      case ISSUER:
      case SUBJECT:
        sb.append(type.literal()).append(" ");
        sb.append(value);
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   userResourceOption
    : MAX_QUERIES_PER_HOUR decimalLiteral
    | MAX_UPDATES_PER_HOUR decimalLiteral
    | MAX_CONNECTIONS_PER_HOUR decimalLiteral
    | MAX_USER_CONNECTIONS decimalLiteral
    ;
   * </pre>
   */
  public static class UserResourceOption implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      MAX_QUERIES_PER_HOUR, MAX_UPDATES_PER_HOUR, MAX_CONNECTIONS_PER_HOUR, MAX_USER_CONNECTIONS;
      @Override
      public String literal() {
        return name();
      }
    }

    public final UserResourceOption.Type type;
    public final DecimalLiteral decimalLiteral;

    UserResourceOption(UserResourceOption.Type type, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(decimalLiteral != null);

      this.type = type;
      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" ").append(decimalLiteral.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   userPasswordOption
    : PASSWORD EXPIRE
      (expireType=DEFAULT
      | expireType=NEVER
      | expireType=INTERVAL decimalLiteral DAY
      )?
    ;
   * </pre>
   */
  public static class UserPasswordOption implements PrimitiveExpression {
    public static enum ExpireType implements RelationalAlgebraEnum {
      DEFAULT, NEVER, INTERVAL;
      @Override
      public String literal() {
        return name();
      }
    }

    public final UserPasswordOption.ExpireType expireType;
    public final DecimalLiteral day;

    UserPasswordOption(UserPasswordOption.ExpireType expireType, DecimalLiteral day) {
      if (ExpireType.INTERVAL.equals(expireType)) {
        Preconditions.checkArgument(day != null);
      }

      this.expireType = expireType;
      this.day = day;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("PASSWORD EXPIRE ");
      if (expireType != null) {
        switch (expireType) {
        case DEFAULT:
          sb.append(expireType.literal());
          break;
        case NEVER:
          sb.append(expireType.literal());
          break;
        case INTERVAL:
          sb.append(expireType.literal()).append(" ");
          sb.append(day.literal()).append(" DAY");
          break;
        default:
          break;
        }
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   userLockOption
    : ACCOUNT lockType=(LOCK | UNLOCK)
    ;
   * </pre>
   */
  public static enum UserLockOptionEnum implements RelationalAlgebraEnum {
    LOCK, UNLOCK;

    @Override
    public String literal() {
      return "ACCOUNT " + name();
    }
  }

  /**
   * <pre>
   privelegeClause
    : privilege ( '(' uidList ')' )?
    ;
   * </pre>
   */
  public static class PrivelegeClause implements PrimitiveExpression {
    public final PrivilegeEnum privilege;
    public final UidList uidList;

    PrivelegeClause(PrivilegeEnum privilege, UidList uidList) {
      Preconditions.checkArgument(privilege != null);

      this.privilege = privilege;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(privilege.literal());
      if (uidList != null) {
        sb.append("(").append(uidList.literal()).append(")");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   privilege
    : ALL PRIVILEGES?
    | ALTER ROUTINE?
    | CREATE
      (TEMPORARY TABLES | ROUTINE | VIEW | USER | TABLESPACE)?
    | DELETE | DROP | EVENT | EXECUTE | FILE | GRANT OPTION
    | INDEX | INSERT | LOCK TABLES | PROCESS | PROXY
    | REFERENCES | RELOAD
    | REPLICATION (CLIENT | SLAVE)
    | SELECT
    | SHOW (VIEW | DATABASES)
    | SHUTDOWN | SUPER | TRIGGER | UPDATE | USAGE
    ;
   * </pre>
   */
  public static enum PrivilegeEnum implements RelationalAlgebraEnum {
    ALL("ALL"), ALL_PRIVILEGES("ALL PRIVILEGES"), //
    ALTER("ALTER"), ALTER_ROUTINE("ALTER ROUTINE"), //
    CREATE("CREATE"), CREATE_TEMPORARY_TABLES("CREATE TEMPORARY TABLES"),
    CREATE_ROUTINE("CREATE ROUTINE"), CREATE_VIEW("CREATE VIEW"), CREATE_USER("CREATE USER"),
    CREATE_TABLESPACE("CREATE TABLESPACE"), //
    DELETE("DELETE"), DROP("DROP"), EVENT("EVENT"), EXECUTE("EXECUTE"), FILE("FILE"),
    GRANT_OPTION("GRANT OPTION"), //
    INDEX("INDEX"), INSERT("INSERT"), LOCK_TABLES("LOCK TABLES"), PROCESS("PROCESS"),
    PROXY("PROXY"), REFERENCES("REFERENCES"), RELOAD("RELOAD"), //
    REPLICATION_CLIENT("REPLICATION CLIENT"), REPLICATION_SLAVE("REPLICATION SLAVE"), //
    SELECT("SELECT"), //
    SHOW_VIEW("SHOW VIEW"), SHOW_DATABASES("SHOW DATABASES"), //
    SHUTDOWN("SHUTDOWN"), SUPER("SUPER"), TRIGGER("TRIGGER"), UPDATE("UPDATE"), USAGE("USAGE");

    public String literal;

    PrivilegeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   privilegeLevel
    : '*'                                                           #currentSchemaPriviLevel
    | '*' '.' '*'                                                   #globalPrivLevel
    | uid '.' '*'                                                   #definiteSchemaPrivLevel
    | uid '.' uid                                                   #definiteFullTablePrivLevel
    | uid                                                           #definiteTablePrivLevel
    ;
   * </pre>
   */
  public static interface PrivilegeLevel extends PrimitiveExpression {
  }

  public static class CurrentSchemaPriviLevel implements PrivilegeLevel {

    CurrentSchemaPriviLevel() {
    }

    @Override
    public String literal() {
      return "*";
    }
  }

  public static class GlobalPrivLevel implements PrivilegeLevel {
    GlobalPrivLevel() {
    }

    @Override
    public String literal() {
      return "*.*";
    }
  }

  public static class DefiniteSchemaPrivLevel implements PrivilegeLevel {
    public final Uid uid;

    DefiniteSchemaPrivLevel(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uid.literal()).append(".*");
      return sb.toString();
    }
  }

  public static class DefiniteFullTablePrivLevel implements PrivilegeLevel {
    public final Uid uid1;
    public final Uid uid2;

    DefiniteFullTablePrivLevel(Uid uid1, Uid uid2) {
      Preconditions.checkArgument(uid1 != null);
      Preconditions.checkArgument(uid2 != null);

      this.uid1 = uid1;
      this.uid2 = uid2;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(uid1.literal()).append(".").append(uid2.literal());
      return sb.toString();
    }

  }

  public static class DefiniteTablePrivLevel implements PrivilegeLevel {
    public final Uid uid;

    DefiniteTablePrivLevel(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      return uid.literal();
    }

  }

  /**
   * <pre>
   renameUserClause
    : fromFirst=userName TO toFirst=userName
    ;
   * </pre>
   */
  public static class RenameUserClause implements PrimitiveExpression {
    public final UserName fromFirst;
    public final UserName toFirst;

    RenameUserClause(UserName fromFirst, UserName toFirst) {
      Preconditions.checkArgument(fromFirst != null);
      Preconditions.checkArgument(toFirst != null);

      this.fromFirst = fromFirst;
      this.toFirst = toFirst;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(fromFirst.literal()).append(" TO ").append(toFirst.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
  checkTableOption
    : FOR UPGRADE | QUICK | FAST | MEDIUM | EXTENDED | CHANGED
    ;
   * </pre>
   */
  public static enum CheckTableOptionEnum implements RelationalAlgebraEnum {
    FOR_UPGRADE("FOR UPGRADE"), QUICK("QUICK"), FAST("FAST"), MEDIUM("MEDIUM"),
    EXTENDED("EXTENDED"), CHANGED("CHANGED");

    public String literal;

    CheckTableOptionEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return name();
    }
  }

  /**
   * <pre>
   variableClause
    : LOCAL_ID | GLOBAL_ID | ( ('@' '@')? (GLOBAL | SESSION | LOCAL)  )? uid
    ;
   * </pre>
   */
  public static class VariableClause implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      LOCAL_ID, GLOBAL_ID, UID;
      @Override
      public String literal() {
        return name();
      }
    }

    public static enum ScopeType implements RelationalAlgebraEnum {
      GLOBAL, SESSION, LOCAL;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Type type;
    public final String id;
    public final Boolean has;
    public final ScopeType scopeType;
    public final Uid uid;

    VariableClause(VariableClause.Type type, String id) {
      Preconditions.checkArgument(Type.LOCAL_ID.equals(type) || Type.GLOBAL_ID.equals(type));
      Preconditions.checkArgument(id != null);

      this.type = type;
      this.id = id;
      this.has = null;
      this.scopeType = null;
      this.uid = null;
    }

    VariableClause(VariableClause.Type type, String id, Boolean has,
        VariableClause.ScopeType scopeType, Uid uid) {
      Preconditions.checkArgument(Type.UID.equals(type));
      Preconditions.checkArgument(uid != null);

      this.type = type;
      this.id = id;
      this.has = has;
      this.scopeType = scopeType;
      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case LOCAL_ID:
        sb.append(type.literal());
        break;
      case GLOBAL_ID:
        sb.append(type.literal());
        break;
      case UID:
        if (Boolean.TRUE.equals(has)) {
          sb.append("@@ ");
        }
        if (scopeType != null) {
          sb.append(scopeType.literal()).append(" ");
        }
        sb.append(uid.literal());
        break;
      default:
        break;
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
   showCommonEntity
    : CHARACTER SET | COLLATION | DATABASES | SCHEMAS
    | FUNCTION STATUS | PROCEDURE STATUS
    | (GLOBAL | SESSION)? (STATUS | VARIABLES)
    ;
   * </pre>
   */
  public static enum ShowCommonEntityEnum implements RelationalAlgebraEnum {
    CHARACTER_SET("CHARACTER SET"), COLLATION("COLLATION"), DATABASES("DATABASES"),
    SCHEMAS("SCHEMAS"), //
    FUNCTION_STATUS("FUNCTION STATUS"), PROCEDURE_STATUS("PROCEDURE STATUS"), //
    STATUS("STATUS"), VARIABLES("VARIABLES"), GLOBAL_STATUS("GLOBAL STATUS"),
    GLOBAL_VARIABLES("GLOBAL VARIABLES"), SESSION_STATUS("SESSION STATUS"),
    SESSION_VARIABLES("SESSION VARIABLES");

    public String literal;

    ShowCommonEntityEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   showFilter
    : LIKE STRING_LITERAL
    | WHERE expression
    ;
   * </pre>
   */
  public static class ShowFilter implements PrimitiveExpression {
    public final String like;
    public final Expression where;

    ShowFilter(String like, Expression where) {
      Preconditions.checkArgument(!(like == null && where == null));

      this.like = like;
      this.where = where;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LIKE ").append(like).append(" WHERE ").append(where.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   showGlobalInfoClause
    : STORAGE? ENGINES | MASTER STATUS | PLUGINS
    | PRIVILEGES | FULL? PROCESSLIST | PROFILES
    | SLAVE HOSTS | AUTHORS | CONTRIBUTORS
    ;
   * </pre>
   */
  public static enum ShowGlobalInfoClauseEnum implements RelationalAlgebraEnum {
    ENGINES("ENGINES"), STORAGE_ENGINES("STORAGE ENGINES"), MASTER_STATUS("MASTER STATUS"),
    PLUGINS("PLUGINS"), //
    PRIVILEGES("PRIVILEGES"), PROCESSLIST("PROCESSLIST"), FULL_PROCESSLIST("FULL PROCESSLIST"),
    PROFILES("PROFILES"), //
    SLAVE_HOSTS("SLAVE HOSTS"), AUTHORS("AUTHORS"), CONTRIBUTORS("CONTRIBUTORS");

    public String literal;

    ShowGlobalInfoClauseEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   showSchemaEntity
    : EVENTS | TABLE STATUS | FULL? TABLES | TRIGGERS
    ;
   * </pre>
   */
  public static enum ShowSchemaEntityEnum implements RelationalAlgebraEnum {
    EVENTS("EVENTS"), TABLE_STATUS("TABLE STATUS"), TABLES("TABLES"), FULL_TABLES("FULL TABLES"),
    TRIGGERS("TRIGGERS");

    public String literal;

    ShowSchemaEntityEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   showProfileType
    : ALL | BLOCK IO | CONTEXT SWITCHES | CPU | IPC | MEMORY
    | PAGE FAULTS | SOURCE | SWAPS
    ;
   * </pre>
   */
  public static enum ShowProfileTypeEnum implements RelationalAlgebraEnum {
    ALL("ALL"), BLOCK_IO("BLOCK IO"), CONTEXT_SWITCHES("CONTEXT SWITCHES"), CPU("CPU"), IPC("IPC"),
    MEMORY("MEMORY"), //
    PAGE_FAULTS("PAGE FAULTS"), SOURCE("SOURCE"), SWAPS("SWAPS");

    public String literal;

    ShowProfileTypeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   tableIndexes
    : tableName ( indexFormat=(INDEX | KEY)? '(' uidList ')' )?
    ;
   * </pre>
   */
  public static class TableIndexes implements PrimitiveExpression {
    public final TableName tableName;
    public final IndexFormatEnum indexFormat;
    public final UidList uidList;

    TableIndexes(TableName tableName, IndexFormatEnum indexFormat, UidList uidList) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.indexFormat = indexFormat;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(tableName.literal()).append(" ");
      if (uidList != null) {
        if (indexFormat != null) {
          sb.append(indexFormat.literal()).append(" ");
        }
        sb.append("(").append(uidList.literal()).append(")");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   flushOption
    : (
        DES_KEY_FILE 
        | HOSTS
        | (
            BINARY | ENGINE | ERROR | GENERAL | RELAY | SLOW
          )? LOGS
        | OPTIMIZER_COSTS 
        | PRIVILEGES 
        | QUERY CACHE 
        | STATUS
        | USER_RESOURCES 
        | TABLES (WITH READ LOCK)?
       )                                                            #simpleFlushOption
    | RELAY LOGS channelOption?                                     #channelFlushOption
    | TABLES tables flushTableOption?                               #tableFlushOption
    ;
   * </pre>
   */
  public static interface FlushOption extends PrimitiveExpression {
  }

  public static class SimpleFlushOption implements FlushOption {
    public static enum Type implements RelationalAlgebraEnum {
      DES_KEY_FILE("DES_KEY_FILE"), HOSTS("HOSTS"), LOGS("LOGS"),
      OPTIMIZER_COSTS("OPTIMIZER_COSTS"), PRIVILEGES("PRIVILEGES"), QUERY_CACHE("QUERY CACHE"),
      STATUS("STATUS"), USER_RESOURCES("USER_RESOURCES"), TABLES("TABLES");

      public String literal;

      Type(String literal) {
        this.literal = literal;
      }

      @Override
      public String literal() {
        return literal;
      }
    }

    public static enum LogType implements RelationalAlgebraEnum {
      BINARY, ENGINE, ERROR, GENERAL, RELAY, SLOW;

      @Override
      public String literal() {
        return name();
      }
    }

    public final Type type;
    public final LogType logType;
    public final Boolean tablesWithReadLock;

    SimpleFlushOption(SimpleFlushOption.Type type, SimpleFlushOption.LogType logType,
        Boolean tablesWithReadLock) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.logType = logType;
      this.tablesWithReadLock = tablesWithReadLock;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case DES_KEY_FILE:
      case HOSTS:
      case OPTIMIZER_COSTS:
      case PRIVILEGES:
      case QUERY_CACHE:
      case STATUS:
      case USER_RESOURCES:
        sb.append(type.literal());
        break;
      case LOGS:
        if (logType != null) {
          sb.append(logType.literal()).append(" ");
        }
        sb.append(type.literal());
        break;
      case TABLES:
        sb.append("TABLES");
        if (Boolean.TRUE.equals(tablesWithReadLock)) {
          sb.append(" WITH READ LOCK");
        }
      default:
        break;
      }
      return sb.toString();
    }

  }

  public static class ChannelFlushOption implements FlushOption {
    public final ChannelOption channelOption;

    ChannelFlushOption(ChannelOption channelOption) {
      this.channelOption = channelOption;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RELAY LOGS ").append(channelOption.literal());
      return sb.toString();
    }

  }

  public static class TableFlushOption implements FlushOption {
    public final Tables tables;
    public final FlushTableOptionEnum flushTableOption;

    TableFlushOption(Tables tables, FlushTableOptionEnum flushTableOption) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
      this.flushTableOption = flushTableOption;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("TABLES ").append(tables.literal());
      if (flushTableOption != null) {
        sb.append(" ").append(flushTableOption.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   flushTableOption
    : WITH READ LOCK
    | FOR EXPORT
    ;
   * </pre>
   */
  public static enum FlushTableOptionEnum implements RelationalAlgebraEnum {
    WITH_READ_LOCK("WITH READ LOCK"), FOR_EXPORT("FOR EXPORT");

    public String literal;

    FlushTableOptionEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   loadedTableIndexes
    : tableName
      ( PARTITION '(' (partitionList=uidList | ALL) ')' )?
      ( indexFormat=(INDEX | KEY)? '(' indexList=uidList ')' )?
      (IGNORE LEAVES)?
    ;
   * </pre>
   */
  public static class LoadedTableIndexes implements PrimitiveExpression {
    public final TableName tableName;
    public final UidList partitionList;
    public final Boolean partitionAll;
    public final IndexFormatEnum indexFormat;
    public final UidList indexList;
    public final Boolean ignoreLeaves;

    LoadedTableIndexes(TableName tableName, UidList partitionList, Boolean partitionAll,
        IndexFormatEnum indexFormat, UidList indexList, Boolean ignoreLeaves) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.partitionList = partitionList;
      this.partitionAll = partitionAll;
      this.indexFormat = indexFormat;
      this.indexList = indexList;
      this.ignoreLeaves = ignoreLeaves;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(tableName.literal()).append(" ");
      if (partitionList != null) {
        sb.append("PARTITION (").append(partitionList.literal()).append(") ");
      } else if (Boolean.TRUE.equals(partitionAll)) {
        sb.append("PARTITION (ALL) ");
      }
      if (indexList != null) {
        if (indexFormat != null) {
          sb.append(indexFormat.literal()).append(" ");
        }
        sb.append(indexList.literal());
      }
      if (Boolean.TRUE.equals(ignoreLeaves)) {
        sb.append("IGNORE LEAVES");
      }
      return sb.toString();
    }

  }
}