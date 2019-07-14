package com.spike.giantdataanalysis.model.logic.boole;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.boole.core.BooleanAlgebraExpression;

/**
 * 或表达式.
 */
public abstract class BooleanAlgebraOrExpression<T1 extends BooleanAlgebraExpression, T2 extends BooleanAlgebraExpression>
    implements BooleanAlgebraExpression {
  public abstract T1 first();

  public abstract T2 second();

  @Override
  public final boolean eval() {
    Preconditions.checkState(first() != null);
    Preconditions.checkState(second() != null);

    return first().eval() && second().eval();
  }
}
