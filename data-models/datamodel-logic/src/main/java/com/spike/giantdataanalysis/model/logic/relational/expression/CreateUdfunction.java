package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createUdfunction
    : CREATE AGGREGATE? FUNCTION uid
      RETURNS returnType=(STRING | INTEGER | REAL | DECIMAL)
      SONAME STRING_LITERAL
    ;
 * </pre>
 */
public class CreateUdfunction implements AdministrationStatement {
}
