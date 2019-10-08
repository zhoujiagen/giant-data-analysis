package com.spike.giantdataanalysis.model.logic.relational;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 求值上下文.
 */
public final class RelationalEvaluationContext {

  RelationalEvaluationContext() {
  }

  /** 实际关系. */
  public final List<RelationalRelation> relations = Lists.newArrayList();
}