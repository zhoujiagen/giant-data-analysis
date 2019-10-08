package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.SimpleStrings;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(stringMasterOption.literal()).append(" = ").append(value);
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(decimalMasterOption.literal()).append(" = ").append(value.literal());
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(boolMasterOption.literal()).append(" = ");
      if (value) {
        sb.append("1");
      } else {
        sb.append("0");
      }
      return sb.toString();
    }

  }

  public static class MasterRealOption implements MasterOption {
    public final String realLiteral;

    MasterRealOption(String realLiteral) {
      Preconditions.checkArgument(realLiteral != null);

      this.realLiteral = realLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MASTER_HEARTBEAT_PERIOD = ").append(realLiteral);
      return sb.toString();
    }

  }

  public static class MasterUidListOption implements MasterOption {
    public final List<Uid> uids;

    MasterUidListOption(List<Uid> uids) {
      this.uids = uids;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("IGNORE_SERVER_IDS = (");
      if (CollectionUtils.isNotEmpty(uids)) {
        List<String> literals = Lists.newArrayList();
        for (Uid uid : uids) {
          literals.add(uid.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      sb.append(")");
      return sb.toString();
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
    MASTER_SSL_KEY, MASTER_SSL_CIPHER, MASTER_TLS_VERSION;

    @Override
    public String literal() {
      return name();
    }
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
    RELAY_LOG_POS;

    @Override
    public String literal() {
      return name();
    }
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
    MASTER_AUTO_POSITION, MASTER_SSL, MASTER_SSL_VERIFY_SERVER_CERT;

    @Override
    public String literal() {
      return name();
    }
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("FOR CHANNEL ").append(channel);
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_DO_DB = (").append(uidList.literal()).append(")");
      return sb.toString();
    }
  }

  public static class IgnoreDbReplication implements ReplicationFilter {
    public final UidList uidList;

    IgnoreDbReplication(UidList uidList) {
      Preconditions.checkArgument(uidList != null);

      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_IGNORE_DB = (").append(uidList.literal()).append(")");
      return sb.toString();
    }

  }

  public static class DoTableReplication implements ReplicationFilter {
    public final Tables tables;

    DoTableReplication(Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_DO_TABLE = (").append(tables.literal()).append(")");
      return sb.toString();
    }
  }

  public static class IgnoreTableReplication implements ReplicationFilter {
    public final Tables tables;

    IgnoreTableReplication(Tables tables) {
      Preconditions.checkArgument(tables != null);

      this.tables = tables;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_IGNORE_TABLE = (").append(tables.literal()).append(")");
      return sb.toString();
    }
  }

  public static class WildDoTableReplication implements ReplicationFilter {
    public final SimpleStrings simpleStrings;

    WildDoTableReplication(SimpleStrings simpleStrings) {
      Preconditions.checkArgument(simpleStrings != null);

      this.simpleStrings = simpleStrings;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_WILD_DO_TABLE = (").append(simpleStrings.literal()).append(")");
      return sb.toString();
    }

  }

  public static class WildIgnoreTableReplication implements ReplicationFilter {
    public final SimpleStrings simpleStrings;

    WildIgnoreTableReplication(SimpleStrings simpleStrings) {
      Preconditions.checkArgument(simpleStrings != null);

      this.simpleStrings = simpleStrings;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_WILD_IGNORE_TABLE = (").append(simpleStrings.literal()).append(")");
      return sb.toString();
    }

  }

  public static class RewriteDbReplication implements ReplicationFilter {
    public final List<TablePair> tablePairs;

    RewriteDbReplication(List<TablePair> tablePairs) {
      Preconditions.checkArgument(tablePairs != null && tablePairs.size() > 0);

      this.tablePairs = tablePairs;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("REPLICATE_REWRITE_DB = ");
      sb.append("(");
      List<String> literals = Lists.newArrayList();
      for (TablePair tablePair : tablePairs) {
        literals.add(tablePair.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(firstTable.literal()).append(", ").append(secondTable.literal())
          .append(")");
      return sb.toString();
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
    IO_THREAD, SQL_THREAD;

    @Override
    public String literal() {
      return name();
    }
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
    public static enum Type implements RelationalAlgebraEnum {
      SQL_BEFORE_GTIDS, SQL_AFTER_GTIDS;

      @Override
      public String literal() {
        return name();
      }
    }

    public final GtidsUntilOption.Type type;
    public final GtuidSet gtuidSet;

    GtidsUntilOption(GtidsUntilOption.Type type, GtuidSet gtuidSet) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(gtuidSet != null);

      this.type = type;
      this.gtuidSet = gtuidSet;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" = ").append(gtuidSet.literal());
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("MASTER_LOG_FILE = ").append(logFile).append(", ");
      sb.append("MASTER_LOG_POS = ").append(pos.literal());
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("RELAY_LOG_FILE = ").append(logFile).append(", ");
      sb.append("RELAY_LOG_POS = ").append(pos.literal());
      return sb.toString();
    }
  }

  public static class SqlGapsUntilOption implements UntilOption {

    SqlGapsUntilOption() {
    }

    @Override
    public String literal() {
      return "SQL_AFTER_MTS_GAPS";
    }
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
      USER, PASSWORD, DEFAULT_AUTH, PLUGIN_DIR;

      @Override
      public String literal() {
        return name();
      }
    }

    public final ConnectionOption.Type type;
    public final String stringLiteral;

    ConnectionOption(ConnectionOption.Type type, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" ").append(stringLiteral);
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (stringLiteral != null) {
        sb.append(stringLiteral);
      } else {
        List<String> literals = Lists.newArrayList();
        for (UuidSet uuidSet : uuidSets) {
          literals.add(uuidSet.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
      }
      return sb.toString();
    }

  }
}