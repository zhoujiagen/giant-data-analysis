package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 dropDatabase
    : DROP dbFormat=(DATABASE | SCHEMA) ifExists? uid
    ;
 * </pre>
 */
public class DropDatabase implements DdlStatement {
}
