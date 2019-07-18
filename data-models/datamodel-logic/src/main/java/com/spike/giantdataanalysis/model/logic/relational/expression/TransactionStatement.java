package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
      GLOBAL, SESSION
    }

    public final TransactionContextEnum transactionContext;
    public final List<TransactionOption> transactionOptions;

    SetTransactionStatement(TransactionContextEnum transactionContext,
        List<TransactionOption> transactionOptions) {
      Preconditions.checkArgument(transactionOptions != null && transactionOptions.size() > 0);

      this.transactionContext = transactionContext;
      this.transactionOptions = transactionOptions;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    WITH_CONSISTENT_SNAPSHOT, READ_WRITE, READ_ONLY
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
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    }

    public final Type type;
    public final Boolean local;
    public final Boolean lowPriority;

    LockAction(Type type, Boolean local, Boolean lowPriority) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.local = local;
      this.lowPriority = lowPriority;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    }

    public final Type type;
    public final TransactionLevelEnum transactionLevel;

    TransactionOption(Type type, TransactionLevelEnum transactionLevel) {
      Preconditions.checkArgument(type != null);
      if (Type.ISOLATION_LEVEL.equals(type)) {
        Preconditions.checkArgument(transactionLevel != null);
      }

      this.type = type;
      this.transactionLevel = transactionLevel;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
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
    REPEATABLE_READ, READ_COMMITTED, READ_UNCOMMITTED, SERIALIZABLE
  }
}