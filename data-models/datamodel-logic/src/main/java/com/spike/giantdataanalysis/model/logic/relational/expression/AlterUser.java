package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 alterUser
    : ALTER USER
      userSpecification (',' userSpecification)*                    #alterUserMysqlV56
    | ALTER USER ifExists?
        userAuthOption (',' userAuthOption)*
        (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
        (WITH userResourceOption+)?
        (userPasswordOption | userLockOption)*                      #alterUserMysqlV57
    ;
 * </pre>
 */
public class AlterUser implements AdministrationStatement {
}
