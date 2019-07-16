package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createUser
    : CREATE USER userAuthOption (',' userAuthOption)*              #createUserMysqlV56
    | CREATE USER ifNotExists?
        userAuthOption (',' userAuthOption)*
        (
          REQUIRE
          (tlsNone=NONE | tlsOption (AND? tlsOption)* )
        )?
        (WITH userResourceOption+)?
        (userPasswordOption | userLockOption)*                      #createUserMysqlV57
    ;
 * </pre>
 */
public class CreateUser implements AdministrationStatement {
}
