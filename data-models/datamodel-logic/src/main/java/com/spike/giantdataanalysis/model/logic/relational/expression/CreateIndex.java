package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 createIndex
    : CREATE
      intimeAction=(ONLINE | OFFLINE)?
      indexCategory=(UNIQUE | FULLTEXT | SPATIAL)?
      INDEX uid indexType?
      ON tableName indexColumnNames
      indexOption*
      (
        ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
        | LOCK '='?
          lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
      )*
    ;
 * </pre>
 */
public class CreateIndex implements DdlStatement {
  public static enum AlgTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, INPLACE, COPY
  }

  public static enum LockTypeEnum implements RelationalAlgebraEnum {
    DEFAULT, NONE, SHARED, EXCLUSIVE
  }

  public static class AlgorithmOrLock {
    public final CreateIndex.AlgTypeEnum algType;
    public final CreateIndex.LockTypeEnum lockType;

    AlgorithmOrLock(AlgTypeEnum algType, LockTypeEnum lockType) {
      this.algType = algType;
      this.lockType = lockType;
    }
  }

  public final IntimeActionEnum intimeAction;
  public final IndexCategoryEnum indexCategory;
  public final Uid uid;
  public final IndexTypeEnum indexType;
  public final TableName tableName;
  public final IndexColumnNames indexColumnNames;
  public final List<IndexOption> indexOptions;
  public final List<AlgorithmOrLock> algorithmOrLocks;

  CreateIndex(IntimeActionEnum intimeAction, IndexCategoryEnum indexCategory, Uid uid,
      IndexTypeEnum indexType, TableName tableName, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions, List<AlgorithmOrLock> algorithmOrLocks) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(tableName != null);
    Preconditions.checkArgument(indexColumnNames != null);
    
    this.intimeAction = intimeAction;
    this.indexCategory = indexCategory;
    this.uid = uid;
    this.indexType = indexType;
    this.tableName = tableName;
    this.indexColumnNames = indexColumnNames;
    this.indexOptions = indexOptions;
    this.algorithmOrLocks = algorithmOrLocks;
  }

}
