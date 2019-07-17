package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 dropIndex
    : DROP INDEX intimeAction=(ONLINE | OFFLINE)?
      uid ON tableName
      (
        ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
        | LOCK '='?
          lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
      )*
    ;
 * </pre>
 */
public class DropIndex implements DdlStatement {
  public final IntimeActionEnum intimeAction;
  public final Uid uid;
  public final TableName tableName;
  public final List<IndexAlgorithmOrLock> algorithmOrLocks;

  DropIndex(IntimeActionEnum intimeAction, Uid uid, TableName tableName,
      List<IndexAlgorithmOrLock> algorithmOrLocks) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(tableName != null);

    this.intimeAction = intimeAction;
    this.uid = uid;
    this.tableName = tableName;
    this.algorithmOrLocks = algorithmOrLocks;
  }

}
