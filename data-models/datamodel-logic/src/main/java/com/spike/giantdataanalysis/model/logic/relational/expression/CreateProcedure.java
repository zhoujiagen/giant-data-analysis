package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createProcedure
    : CREATE ownerStatement?
    PROCEDURE fullId
      '(' procedureParameter? (',' procedureParameter)* ')'
      routineOption*
    routineBody
    ;
 * </pre>
 */
public class CreateProcedure implements DdlStatement {
}
