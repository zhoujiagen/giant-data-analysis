package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.SimpleStrings;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

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
  public static interface MasterOption extends PrimitiveExpression {
  }

  public static class MasterStringOption implements MasterOption {
    public final StringMasterOptionEnum stringMasterOption;
    public final String value;

    MasterStringOption(StringMasterOptionEnum stringMasterOption, String value) {
      Preconditions.checkArgument(stringMasterOption != null);
      Preconditions.checkArgument(value != null);

      this.stringMasterOption = stringMasterOption;
      this.value = value;
    }

  }

  public static class MasterDecimalOption implements MasterOption {
    public final DecimalMasterOptionEnum decimalMasterOption;
    public final DecimalLiteral value;

    MasterDecimalOption(DecimalMasterOptionEnum decimalMasterOption, DecimalLiteral value) {
      Preconditions.checkArgument(decimalMasterOption != null);
      Preconditions.checkArgument(value != null);

      this.decimalMasterOption = decimalMasterOption;
      this.value = value;
    }

  }

  public static class MasterBoolOption implements MasterOption {
    public final BoolMasterOptionEnum boolMasterOption;
    public final boolean value;

    MasterBoolOption(BoolMasterOptionEnum boolMasterOption, boolean value) {
      Preconditions.checkArgument(boolMasterOption != null);

      this.boolMasterOption = boolMasterOption;
      this.value = value;
    }

  }

  public static class MasterRealOption implements MasterOption {
    public final String realLiteral;

    MasterRealOption(String realLiteral) {
      Preconditions.checkArgument(realLiteral != null);

      this.realLiteral = realLiteral;
    }

  }

  public static class MasterUidListOption implements MasterOption {
    public final List<Uid> uids;

    MasterUidListOption(List<Uid> uids) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
    }

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
  public static enum StringMasterOptionEnum implements RelationalAlgebraEnum {
    MASTER_BIND, MASTER_HOST, MASTER_USER, MASTER_PASSWORD, MASTER_LOG_FILE, RELAY_LOG_FILE,
    MASTER_SSL_CA, MASTER_SSL_CAPATH, MASTER_SSL_CERT, MASTER_SSL_CRL, MASTER_SSL_CRLPATH,
    MASTER_SSL_KEY, MASTER_SSL_CIPHER, MASTER_TLS_VERSION
  }

  /**
   * <pre>
   decimalMasterOption
    : MASTER_PORT | MASTER_CONNECT_RETRY | MASTER_RETRY_COUNT
    | MASTER_DELAY | MASTER_LOG_POS | RELAY_LOG_POS
    ;
   * </pre>
   */
  public static enum DecimalMasterOptionEnum implements RelationalAlgebraEnum {
    MASTER_PORT, MASTER_CONNECT_RETRY, MASTER_RETRY_COUNT, MASTER_DELAY, MASTER_LOG_POS,
    RELAY_LOG_POS
  }

  /**
   * <pre>
   boolMasterOption
    : MASTER_AUTO_POSITION | MASTER_SSL
    | MASTER_SSL_VERIFY_SERVER_CERT
    ;
   * </pre>
   */
  public static enum BoolMasterOptionEnum implements RelationalAlgebraEnum {
    MASTER_AUTO_POSITION, MASTER_SSL, MASTER_SSL_VERIFY_SERVER_CERT
  }

  /**
   * <pre>
   channelOption
    : FOR CHANNEL STRING_LITERAL
    ;
   * </pre>
   */
  public static class ChannelOption implements PrimitiveExpression {
    public String channel;

    ChannelOption(String channel) {
      Preconditions.checkArgument(channel != null);

      this.channel = channel;
    }

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
  public static interface ReplicationFilter extends PrimitiveExpression {
  }

  public static class DoDbReplication implements ReplicationFilter {
    public final UidList uidList;

    DoDbReplication(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }
  }

  public static class IgnoreDbReplication implements ReplicationFilter {
    public final UidList uidList;

    IgnoreDbReplication(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }

  }

  public static class DoTableReplication implements ReplicationFilter {
    public final Tables tables;

    DoTableReplication(Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
    }
  }

  public static class IgnoreTableReplication implements ReplicationFilter {
    public final Tables tables;

    IgnoreTableReplication(Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
    }
  }

  public static class WildDoTableReplication implements ReplicationFilter {
    public final SimpleStrings simpleStrings;

    WildDoTableReplication(SimpleStrings simpleStrings) {
      Preconditions.checkArgument(simpleStrings != null);

      this.simpleStrings = simpleStrings;
    }

  }

  public static class WildIgnoreTableReplication implements ReplicationFilter {
    public final SimpleStrings simpleStrings;

    WildIgnoreTableReplication(SimpleStrings simpleStrings) {
      Preconditions.checkArgument(simpleStrings != null);

      this.simpleStrings = simpleStrings;
    }

  }

  public static class RewriteDbReplication implements ReplicationFilter {
    public final List<TablePair> tablePairs;

    RewriteDbReplication(List<TablePair> tablePairs) {
      Preconditions.checkArgument(tablePairs != null && tablePairs.size() > 0);

      this.tablePairs = tablePairs;
    }

  }

  /**
   * <pre>
   tablePair
    : '(' firstTable=tableName ',' secondTable=tableName ')'
    ;
   * </pre>
   */
  public static class TablePair implements PrimitiveExpression {
    public final TableName firstTable;
    public final TableName secondTable;

    TablePair(TableName firstTable, TableName secondTable) {
      Preconditions.checkArgument(firstTable != null);
      Preconditions.checkArgument(secondTable != null);

      this.firstTable = firstTable;
      this.secondTable = secondTable;
    }

  }

  /**
   * <pre>
   threadType
    : IO_THREAD | SQL_THREAD
    ;
   * </pre>
   */
  public static enum ThreadTypeEnum implements RelationalAlgebraEnum {
    IO_THREAD, SQL_THREAD
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
  public static interface UntilOption extends PrimitiveExpression {
  }

  public static class GtidsUntilOption implements UntilOption {
    public static enum Type {
      SQL_BEFORE_GTIDS, SQL_AFTER_GTIDS
    }

    public final Type type;
    public final GtuidSet gtuidSet;

    GtidsUntilOption(Type type, GtuidSet gtuidSet) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(gtuidSet != null);

      this.type = type;
      this.gtuidSet = gtuidSet;
    }

  }

  public static class MasterLogUntilOption implements UntilOption {
    public final String logFile;
    public final DecimalLiteral pos;

    MasterLogUntilOption(String logFile, DecimalLiteral pos) {
      Preconditions.checkArgument(logFile != null);
      Preconditions.checkArgument(pos != null);

      this.logFile = logFile;
      this.pos = pos;
    }

  }

  public static class RelayLogUntilOption implements UntilOption {
    public final String logFile;
    public final DecimalLiteral pos;

    RelayLogUntilOption(String logFile, DecimalLiteral pos) {
      Preconditions.checkArgument(logFile != null);
      Preconditions.checkArgument(pos != null);

      this.logFile = logFile;
      this.pos = pos;
    }
  }

  public static class SqlGapsUntilOption implements UntilOption {
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
    public static enum Type implements RelationalAlgebraEnum {
      USER, PASSWORD, DEFAULT_AUTH, PLUGIN_DIR
    }

    public final Type type;
    public final String stringLiteral;

    ConnectionOption(Type type, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

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
    public final List<UuidSet> uuidSets;
    public final String stringLiteral;

    GtuidSet(List<UuidSet> uuidSets, String stringLiteral) {
      Preconditions
          .checkArgument(!(stringLiteral == null && (uuidSets == null || uuidSets.size() == 0)));

      this.uuidSets = uuidSets;
      this.stringLiteral = stringLiteral;
    }

  }
}