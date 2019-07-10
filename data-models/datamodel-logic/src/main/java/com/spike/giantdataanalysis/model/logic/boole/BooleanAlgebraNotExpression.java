package com.spike.giantdataanalysis.model.logic.boole;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.boole.core.BooleanAlgebraExpression;

/**
 * 非表达式.
 */
public abstract class BooleanAlgebraNotExpression<T extends BooleanAlgebraExpression>
    implements BooleanAlgebraExpression {
  public abstract T first();

  @Override
  public final boolean eval() {
    Preconditions.checkState(first() != null);

    return first().eval();
  }
}
