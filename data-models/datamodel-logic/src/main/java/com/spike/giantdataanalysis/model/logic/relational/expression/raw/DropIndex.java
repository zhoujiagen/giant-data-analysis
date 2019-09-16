package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP INDEX ");
    if (intimeAction != null) {
      sb.append(intimeAction.literal()).append(" ");
    }
    sb.append(uid.literal()).append(" ON").append(tableName.literal()).append(" ");
    if (CollectionUtils.isNotEmpty(algorithmOrLocks)) {
      List<String> literals = Lists.newArrayList();
      for (IndexAlgorithmOrLock indexAlgorithmOrLock : algorithmOrLocks) {
        literals.add(indexAlgorithmOrLock.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    return sb.toString();
  }
}
