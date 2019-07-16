package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 prepareStatement
    : PREPARE uid FROM
      (query=STRING_LITERAL | variable=LOCAL_ID)
    ;
 * </pre>
 */
public class PrepareStatement implements PreparedStatement {
}
