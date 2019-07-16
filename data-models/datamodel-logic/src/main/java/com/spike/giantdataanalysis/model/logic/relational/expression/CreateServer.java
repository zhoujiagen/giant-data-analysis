package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createServer
    : CREATE SERVER uid
    FOREIGN DATA WRAPPER wrapperName=(MYSQL | STRING_LITERAL)
    OPTIONS '(' serverOption (',' serverOption)* ')'
    ;
 * </pre>
 */
public class CreateServer implements DdlStatement {
}
