package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
replicationStatement
  : changeMaster | changeReplicationFilter | purgeBinaryLogs
  | resetMaster | resetSlave | startSlave | stopSlave
  | startGroupReplication | stopGroupReplication
  | xaStartTransaction | xaEndTransaction | xaPrepareStatement
  | xaCommitWork | xaRollbackWork | xaRecoverWork
  ;
 * </pre>
 */
public interface ReplicationStatement extends SqlStatement {

  /**
   * <pre>
   masterOption
    : stringMasterOption '=' STRING_LITERAL                         #masterStringOption
    | decimalMasterOption '=' decimalLiteral                        #masterDecimalOption
    | boolMasterOption '=' boolVal=('0' | '1')                      #masterBoolOption
    | MASTER_HEARTBEAT_PERIOD '=' REAL_LITERAL                      #masterRealOption
    | IGNORE_SERVER_IDS '=' '(' (uid (',' uid)*)? ')'               #masterUidListOption
    ;
   * </pre>
   */
  public static class MasterOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   stringMasterOption
    : MASTER_BIND | MASTER_HOST | MASTER_USER | MASTER_PASSWORD
    | MASTER_LOG_FILE | RELAY_LOG_FILE | MASTER_SSL_CA
    | MASTER_SSL_CAPATH | MASTER_SSL_CERT | MASTER_SSL_CRL
    | MASTER_SSL_CRLPATH | MASTER_SSL_KEY | MASTER_SSL_CIPHER
    | MASTER_TLS_VERSION
    ;
   * </pre>
   */
  public static class StringMasterOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   decimalMasterOption
    : MASTER_PORT | MASTER_CONNECT_RETRY | MASTER_RETRY_COUNT
    | MASTER_DELAY | MASTER_LOG_POS | RELAY_LOG_POS
    ;
   * </pre>
   */
  public static class DecimalMasterOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   boolMasterOption
    : MASTER_AUTO_POSITION | MASTER_SSL
    | MASTER_SSL_VERIFY_SERVER_CERT
    ;
   * </pre>
   */
  public static class BoolMasterOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   channelOption
    : FOR CHANNEL STRING_LITERAL
    ;
   * </pre>
   */
  public static class ChannelOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   replicationFilter
    : REPLICATE_DO_DB '=' '(' uidList ')'                           #doDbReplication
    | REPLICATE_IGNORE_DB '=' '(' uidList ')'                       #ignoreDbReplication
    | REPLICATE_DO_TABLE '=' '(' tables ')'                         #doTableReplication
    | REPLICATE_IGNORE_TABLE '=' '(' tables ')'                     #ignoreTableReplication
    | REPLICATE_WILD_DO_TABLE '=' '(' simpleStrings ')'             #wildDoTableReplication
    | REPLICATE_WILD_IGNORE_TABLE
       '=' '(' simpleStrings ')'                                    #wildIgnoreTableReplication
    | REPLICATE_REWRITE_DB '='
      '(' tablePair (',' tablePair)* ')'                            #rewriteDbReplication
    ;
   * </pre>
   */
  public static class ReplicationFilter implements PrimitiveExpression {

  }

  /**
   * <pre>
   tablePair
    : '(' firstTable=tableName ',' secondTable=tableName ')'
    ;
   * </pre>
   */
  public static class TablePair implements PrimitiveExpression {

  }

  /**
   * <pre>
   threadType
    : IO_THREAD | SQL_THREAD
    ;
   * </pre>
   */
  public static class ThreadType implements PrimitiveExpression {

  }

  /**
   * <pre>
   untilOption
    : gtids=(SQL_BEFORE_GTIDS | SQL_AFTER_GTIDS)
      '=' gtuidSet                                                  #gtidsUntilOption
    | MASTER_LOG_FILE '=' STRING_LITERAL
      ',' MASTER_LOG_POS '=' decimalLiteral                         #masterLogUntilOption
    | RELAY_LOG_FILE '=' STRING_LITERAL
      ',' RELAY_LOG_POS '=' decimalLiteral                          #relayLogUntilOption
    | SQL_AFTER_MTS_GAPS                                            #sqlGapsUntilOption
    ;
   * </pre>
   */
  public static class UntilOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   connectionOption
    : USER '=' conOptUser=STRING_LITERAL                            #userConnectionOption
    | PASSWORD '=' conOptPassword=STRING_LITERAL                    #passwordConnectionOption
    | DEFAULT_AUTH '=' conOptDefAuth=STRING_LITERAL                 #defaultAuthConnectionOption
    | PLUGIN_DIR '=' conOptPluginDir=STRING_LITERAL                 #pluginDirConnectionOption
    ;
   * </pre>
   */
  public static class ConnectionOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   gtuidSet
    : uuidSet (',' uuidSet)*
    | STRING_LITERAL
    ;
   * </pre>
   */
  public static class GtuidSet implements PrimitiveExpression {

  }
}