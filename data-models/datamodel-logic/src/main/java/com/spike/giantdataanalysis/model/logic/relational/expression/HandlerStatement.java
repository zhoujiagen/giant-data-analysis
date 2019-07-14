package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
handlerStatement
  : handlerOpenStatement
  | handlerReadIndexStatement
  | handlerReadStatement
  | handlerCloseStatement
  ;
 * </pre>
 */
public interface HandlerStatement extends DmlStatement {

}