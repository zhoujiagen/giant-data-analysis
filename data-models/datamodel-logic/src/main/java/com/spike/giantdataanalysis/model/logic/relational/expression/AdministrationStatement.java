package com.spike.giantdataanalysis.model.logic.relational.expression;

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

  /**
   * <pre>
   userSpecification
    : userName userPasswordOption
    ;
   * </pre>
   */
  public static class UserSpecification implements PrimitiveExpression {

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
  public static class UserAuthOption implements PrimitiveExpression {

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

  }

  /**
   * <pre>
   userLockOption
    : ACCOUNT lockType=(LOCK | UNLOCK)
    ;
   * </pre>
   */
  public static class UserLockOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   privelegeClause
    : privilege ( '(' uidList ')' )?
    ;
   * </pre>
   */
  public static class PrivelegeClause implements PrimitiveExpression {

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
  public static class Privilege implements PrimitiveExpression {

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
  public static class PrivilegeLevel implements PrimitiveExpression {

  }

  /**
   * <pre>
   renameUserClause
    : fromFirst=userName TO toFirst=userName
    ;
   * </pre>
   */
  public static class RenameUserClause implements PrimitiveExpression {

  }

  /**
   * <pre>
  checkTableOption
    : FOR UPGRADE | QUICK | FAST | MEDIUM | EXTENDED | CHANGED
    ;
   * </pre>
   */
  public static class CheckTableOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   variableClause
    : LOCAL_ID | GLOBAL_ID | ( ('@' '@')? (GLOBAL | SESSION | LOCAL)  )? uid
    ;
   * </pre>
   */
  public static class VariableClause implements PrimitiveExpression {

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
  public static class ShowCommonEntity implements PrimitiveExpression {

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
  public static class ShowGlobalInfoClause implements PrimitiveExpression {

  }

  /**
   * <pre>
   showSchemaEntity
    : EVENTS | TABLE STATUS | FULL? TABLES | TRIGGERS
    ;
   * </pre>
   */
  public static class ShowSchemaEntity implements PrimitiveExpression {

  }

  /**
   * <pre>
   showProfileType
    : ALL | BLOCK IO | CONTEXT SWITCHES | CPU | IPC | MEMORY
    | PAGE FAULTS | SOURCE | SWAPS
    ;
   * </pre>
   */
  public static class ShowProfileType implements PrimitiveExpression {

  }

  /**
   * <pre>
   tableIndexes
    : tableName ( indexFormat=(INDEX | KEY)? '(' uidList ')' )?
    ;
   * </pre>
   */
  public static class TableIndexes implements PrimitiveExpression {

  }

  /**
   * <pre>
   flushOption
    : (
        DES_KEY_FILE | HOSTS
        | (
            BINARY | ENGINE | ERROR | GENERAL | RELAY | SLOW
          )? LOGS
        | OPTIMIZER_COSTS | PRIVILEGES | QUERY CACHE | STATUS
        | USER_RESOURCES | TABLES (WITH READ LOCK)?
       )                                                            #simpleFlushOption
    | RELAY LOGS channelOption?                                     #channelFlushOption
    | TABLES tables flushTableOption?                               #tableFlushOption
    ;
   * </pre>
   */
  public static class FlushOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   flushTableOption
    : WITH READ LOCK
    | FOR EXPORT
    ;
   * </pre>
   */
  public static class FlushTableOption implements PrimitiveExpression {

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

  }
}