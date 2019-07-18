package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 beginWork
    : BEGIN WORK?
    ;
 * </pre>
 */
public class BeginWork implements TransactionStatement {
  public final Boolean work;

  BeginWork(Boolean work) {
    this.work = work;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
