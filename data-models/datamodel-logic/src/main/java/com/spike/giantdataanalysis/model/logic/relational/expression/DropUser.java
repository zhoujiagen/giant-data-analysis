package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 dropUser
    : DROP USER ifExists? userName (',' userName)*
    ;
 * </pre>
 */
public class DropUser implements AdministrationStatement {
}
