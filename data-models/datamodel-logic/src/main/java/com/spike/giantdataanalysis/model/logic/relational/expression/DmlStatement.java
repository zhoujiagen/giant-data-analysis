package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
dmlStatement
  : selectStatement | insertStatement | updateStatement
  | deleteStatement | replaceStatement | callStatement
  | loadDataStatement | loadXmlStatement | doStatement
  | handlerStatement
  ;
 * </pre>
 */
public interface DmlStatement extends SqlStatement {

}