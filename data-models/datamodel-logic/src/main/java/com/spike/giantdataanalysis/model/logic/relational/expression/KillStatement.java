package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 killStatement
    : KILL connectionFormat=(CONNECTION | QUERY)?
      decimalLiteral+
    ;
 * </pre>
 */
public class KillStatement implements AdministrationStatement {
}
