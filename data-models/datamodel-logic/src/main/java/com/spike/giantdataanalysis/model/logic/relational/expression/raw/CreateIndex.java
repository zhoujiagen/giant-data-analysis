package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  public final IntimeActionEnum intimeAction;
  public final IndexCategoryEnum indexCategory;
  public final Uid uid;
  public final IndexTypeEnum indexType;
  public final TableName tableName;
  public final IndexColumnNames indexColumnNames;
  public final List<IndexOption> indexOptions;
  public final List<IndexAlgorithmOrLock> algorithmOrLocks;

  CreateIndex(IntimeActionEnum intimeAction, IndexCategoryEnum indexCategory, Uid uid,
      IndexTypeEnum indexType, TableName tableName, IndexColumnNames indexColumnNames,
      List<IndexOption> indexOptions, List<IndexAlgorithmOrLock> algorithmOrLocks) {
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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ");
    if (intimeAction != null) {
      sb.append(intimeAction.literal()).append(" ");
    }
    if (indexCategory != null) {
      sb.append(indexCategory.literal()).append(" ");
    }
    sb.append("INDEX ").append(uid.literal()).append(" ");
    if (indexType != null) {
      sb.append(indexType.literal()).append(" ");
    }
    sb.append("ON ").append(tableName.literal()).append(" ").append(indexColumnNames.literal())
        .append(" ");
    if (CollectionUtils.isNotEmpty(indexOptions)) {
      List<String> literals = Lists.newArrayList();
      for (IndexOption indexOption : indexOptions) {
        literals.add(indexOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    if (CollectionUtils.isNotEmpty(algorithmOrLocks)) {
      List<String> literals = Lists.newArrayList();
      for (IndexAlgorithmOrLock indexAlgorithmOrLock : algorithmOrLocks) {
        literals.add(indexAlgorithmOrLock.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }

    return sb.toString();
  }
}
