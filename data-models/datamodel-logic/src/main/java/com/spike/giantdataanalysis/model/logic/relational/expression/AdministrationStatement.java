package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.AuthPlugin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.IndexFormatEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.ReplicationStatement.ChannelOption;

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
    TABLE, FUNCTION, PROCEDURE
  }

  public static enum AdminTableActionOptionEnum implements RelationalAlgebraEnum {
    NO_WRITE_TO_BINLOG, LOCAL
  }

  public static enum FlushFormatEnum implements RelationalAlgebraEnum {
    NO_WRITE_TO_BINLOG, LOCAL
  }

  public static enum ConnectionFormatEnum implements RelationalAlgebraEnum {
    CONNECTION, QUERY
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      SSL, X509, CIPHER, ISSUER, SUBJECT
    }

    public final Type type;
    public final String value;

    TlsOption(Type type, String value) {
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      MAX_QUERIES_PER_HOUR, MAX_UPDATES_PER_HOUR, MAX_CONNECTIONS_PER_HOUR, MAX_USER_CONNECTIONS
    }

    public final Type type;
    public final DecimalLiteral decimalLiteral;

    UserResourceOption(Type type, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(decimalLiteral != null);

      this.type = type;
      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      DEFAULT, NEVER, INTERVAL
    }

    public final ExpireType expireType;
    public final DecimalLiteral day;

    UserPasswordOption(ExpireType expireType, DecimalLiteral day) {
      if (ExpireType.INTERVAL.equals(expireType)) {
        Preconditions.checkArgument(day != null);
      }

      this.expireType = expireType;
      this.day = day;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    LOCK, UNLOCK
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    ALL, ALL_PRIVILEGES, //
    ALTER, ALTER_ROUTINE, //
    CREATE, CREATE_TEMPORARY_TABLES, CREATE_ROUTINE, CREATE_VIEW, CREATE_USER, CREATE_TABLESPACE, //
    DELETE, DROP, EVENT, EXECUTE, FILE, GRANT_OPTION, //
    INDEX, INSERT, LOCK_TABLES, PROCESS, PROXY, REFERENCES, RELOAD, //
    REPLICATION_CLIENT, REPLICATION_SLAVE, //
    SELECT, //
    SHOW_VIEW, SHOW_DATABASES, //
    SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE
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

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }

  public static class GlobalPrivLevel implements PrivilegeLevel {

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    FOR_UPGRADE, QUICK, FAST, MEDIUM, EXTENDED, CHANGED
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
      LOCAL_ID, GLOBAL_ID, UID
    }

    public static enum ScopeType implements RelationalAlgebraEnum {
      GLOBAL, SESSION, LOCAL
    }

    public final Type type;
    public final String id;
    public final Boolean has;
    public final ScopeType scopeType;
    public final Uid uid;

    VariableClause(Type type, String id) {
      Preconditions.checkArgument(Type.LOCAL_ID.equals(type) || Type.GLOBAL_ID.equals(type));
      Preconditions.checkArgument(id != null);

      this.type = type;
      this.id = id;
      this.has = null;
      this.scopeType = null;
      this.uid = null;
    }

    VariableClause(Type type, String id, Boolean has, ScopeType scopeType, Uid uid) {
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    CHARACTER_SET, COLLATION, DATABASES, SCHEMAS, FUNCTION_STATUS, PROCEDURE_STATUS, //
    STATUS, VARIABLES, GLOBAL_STATUS, GLOBAL_VARIABLES, SESSION_STATUS, SESSION_VARIABLES,
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    ENGINES, STORAGE_ENGINES, MASTER_STATUS, PLUGINS, PRIVILEGES, PROCESSLIST, FULL_PROCESSLIST,
    PROFILES, SLAVE_HOSTS, AUTHORS, CONTRIBUTORS
  }

  /**
   * <pre>
   showSchemaEntity
    : EVENTS | TABLE STATUS | FULL? TABLES | TRIGGERS
    ;
   * </pre>
   */
  public static enum ShowSchemaEntityEnum implements RelationalAlgebraEnum {
    EVENTS, TABLE_STATUS, TABLES, FULL_TABLES, TRIGGERS
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
    ALL, BLOCK_IO, CONTEXTSWITCHES, CPU, IPC, MEMORY, PAGE_FAULTS, SOURCE, SWAPS
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      DES_KEY_FILE, HOSTS, LOGS, OPTIMIZER_COSTS, PRIVILEGES, QUERY_CACHE, STATUS, USER_RESOURCES,
      TABLES
    }

    public static enum LogType implements RelationalAlgebraEnum {
      BINARY, ENGINE, ERROR, GENERAL, RELAY, SLOW
    }

    public final Type type;
    public final LogType logType;
    public final Boolean tablesWithReadLock;

    SimpleFlushOption(Type type, LogType logType, Boolean tablesWithReadLock) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.logType = logType;
      this.tablesWithReadLock = tablesWithReadLock;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class ChannelFlushOption implements FlushOption {
    public final ChannelOption channelOption;

    ChannelFlushOption(ChannelOption channelOption) {
      this.channelOption = channelOption;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    WITH_READ_LOCK, FOR_EXPORT
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
    public final Boolean IgnoreLeaves;

    LoadedTableIndexes(TableName tableName, UidList partitionList, Boolean partitionAll,
        IndexFormatEnum indexFormat, UidList indexList, Boolean ignoreLeaves) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.partitionList = partitionList;
      this.partitionAll = partitionAll;
      this.indexFormat = indexFormat;
      this.indexList = indexList;
      IgnoreLeaves = ignoreLeaves;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }
}