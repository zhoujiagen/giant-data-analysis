package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 grantStatement
    : GRANT privelegeClause (',' privelegeClause)*
      ON
      privilegeObject=(TABLE | FUNCTION | PROCEDURE)?
      privilegeLevel
      TO userAuthOption (',' userAuthOption)*
      (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
      (WITH (GRANT OPTION | userResourceOption)* )?
    ;
 * </pre>
 */
public class GrantStatement implements AdministrationStatement {
}
