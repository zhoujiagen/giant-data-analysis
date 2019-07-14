package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
callStatement
  : CALL fullId
    (
      '(' (constants | expressions)? ')'
    )?
  ;
 * </pre>
 */
public class CallStatement implements DmlStatement {

}