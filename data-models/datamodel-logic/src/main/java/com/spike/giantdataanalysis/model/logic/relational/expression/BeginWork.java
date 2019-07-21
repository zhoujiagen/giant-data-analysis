package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 beginWork
    : BEGIN WORK?
    ;
 * </pre>
 */
public class BeginWork implements TransactionStatement {

  BeginWork() {
  }

  @Override
  public String literal() {
    return "BEGIN WORK";
  }
}
