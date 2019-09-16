package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
transactionStatement
  : startTransaction
  | beginWork | commitWork | rollbackWork
  | savepointStatement | rollbackStatement
  | releaseStatement | lockTables | unlockTables
  ;
 * </pre>
 */
public interface TransactionStatement extends SqlStatement {

  /**
   * <pre>
   setAutocommitStatement
    : SET AUTOCOMMIT '=' autocommitValue=('0' | '1')
    ;
   * </pre>
   */
  public static class SetAutocommitStatement implements SetStatement {
    public final boolean autocommitValue;

    SetAutocommitStatement(boolean autocommitValue) {

      this.autocommitValue = autocommitValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SET AUTOCOMMIT = ");
      if (autocommitValue) {
        sb.append("1");
      } else {
        sb.append("0");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   setTransactionStatement
    : SET transactionContext=(GLOBAL | SESSION)? TRANSACTION
      transactionOption (',' transactionOption)*
    ;
   * </pre>
   */
  public static class SetTransactionStatement implements SetStatement {
    public static enum TransactionContextEnum implements RelationalAlgebraEnum {
      GLOBAL, SESSION;
      @Override
      public String literal() {
        return name();
      }
    }

    public final SetTransactionStatement.TransactionContextEnum transactionContext;
    public final List<TransactionOption> transactionOptions;

    SetTransactionStatement(SetTransactionStatement.TransactionContextEnum transactionContext,
        List<TransactionOption> transactionOptions) {
      Preconditions.checkArgument(transactionOptions != null && transactionOptions.size() > 0);

      this.transactionContext = transactionContext;
      this.transactionOptions = transactionOptions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SET ");
      if (transactionContext != null) {
        sb.append(transactionContext.literal()).append(" ");
      }
      sb.append("TRANSACTION ");
      List<String> literals = Lists.newArrayList();
      for (TransactionOption transactionOption : transactionOptions) {
        literals.add(transactionOption.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

  /**
   * <pre>
   transactionMode
    : WITH CONSISTENT SNAPSHOT
    | READ WRITE
    | READ ONLY
    ;
   * </pre>
   */
  public static enum TransactionModeEnum implements RelationalAlgebraEnum {
    WITH_CONSISTENT_SNAPSHOT("WITH CONSISTENT SNAPSHOT"), READ_WRITE("READ WRITE"),
    READ_ONLY("READ ONLY");

    public String literal;

    TransactionModeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
   lockTableElement
    : tableName (AS? uid)? lockAction
    ;
   * </pre>
   */
  public static class LockTableElement implements PrimitiveExpression {
    public final TableName tableName;
    public final Uid uid;
    public final LockAction lockAction;

    LockTableElement(TableName tableName, Uid uid, LockAction lockAction) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(lockAction != null);

      this.tableName = tableName;
      this.uid = uid;
      this.lockAction = lockAction;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(tableName.literal()).append(" ");
      if (uid != null) {
        sb.append("AS ").append(uid.literal()).append(" ");
      }
      sb.append(lockAction.literal());
      return sb.toString();
    }

  }

  /**
   * <pre>
   lockAction
    : READ LOCAL? | LOW_PRIORITY? WRITE
    ;
   * </pre>
   */
  public static class LockAction implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      READ, WRITE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final LockAction.Type type;
    public final Boolean local;
    public final Boolean lowPriority;

    LockAction(LockAction.Type type, Boolean local, Boolean lowPriority) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.local = local;
      this.lowPriority = lowPriority;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case READ:
        sb.append("READ");
        if (Boolean.TRUE.equals(local)) {
          sb.append(" LOCAL");
        }
        break;
      case WRITE:
        if (Boolean.TRUE.equals(lowPriority)) {
          sb.append(" LOW_PRIORITY");
        }
        sb.append("WRITE");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   transactionOption
    : ISOLATION LEVEL transactionLevel
    | READ WRITE
    | READ ONLY
    ;
   * </pre>
   */
  public static class TransactionOption implements PrimitiveExpression {
    public static enum Type implements RelationalAlgebraEnum {
      ISOLATION_LEVEL, READ_WRITE, READ_ONLY;

      @Override
      public String literal() {
        return name();
      }
    }

    public final TransactionOption.Type type;
    public final TransactionLevelEnum transactionLevel;

    TransactionOption(TransactionOption.Type type, TransactionLevelEnum transactionLevel) {
      Preconditions.checkArgument(type != null);
      if (Type.ISOLATION_LEVEL.equals(type)) {
        Preconditions.checkArgument(transactionLevel != null);
      }

      this.type = type;
      this.transactionLevel = transactionLevel;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      switch (type) {
      case ISOLATION_LEVEL:
        sb.append("ISOLATION LEVEL ").append(transactionLevel.literal());
        break;
      case READ_WRITE:
        sb.append("READ WRITE");
        break;
      case READ_ONLY:
        sb.append("READ ONLY");
        break;
      default:
        break;
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   transactionLevel
    : REPEATABLE READ
    | READ COMMITTED
    | READ UNCOMMITTED
    | SERIALIZABLE
    ;
   * </pre>
   */
  public static enum TransactionLevelEnum implements RelationalAlgebraEnum {
    REPEATABLE_READ("REPEATABLE READ"), READ_COMMITTED("READ COMMITTED"),
    READ_UNCOMMITTED("READ UNCOMMITTED"), SERIALIZABLE("SERIALIZABLE");

    public String literal;

    TransactionLevelEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }

  }
}