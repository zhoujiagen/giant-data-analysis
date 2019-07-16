package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 revokeStatement
    : REVOKE privelegeClause (',' privelegeClause)*
      ON
      privilegeObject=(TABLE | FUNCTION | PROCEDURE)?
      privilegeLevel
      FROM userName (',' userName)*                                 #detailRevoke
    | REVOKE ALL PRIVILEGES? ',' GRANT OPTION
      FROM userName (',' userName)*                                 #shortRevoke
    ;
 * </pre>
 */
public class RevokeStatement implements AdministrationStatement {
}
