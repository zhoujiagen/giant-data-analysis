package com.spike.giantdataanalysis.model.logic.relational.model;

import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 关系操作.
 */
public interface RelationalOperation extends Literal {

  public RelationalAlgebraOperationEnum operationType();

  public RelationalRelation result(String alias);
  
  /**
   * 求值.
   * @param context 求值上下文; 其中{@link RelationalEvaluationContext#stage}确定是否物理求值
   * @return 元组列表.
   * @throws RelationalEvaluationError
   */
  public RelationalRelation eval(RelationalEvaluationContext context)
      throws RelationalEvaluationError;
}
