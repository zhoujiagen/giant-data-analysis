package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfNotExists;

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
public interface CreateUser extends AdministrationStatement {
  public static class AlterUserMysqlV56 implements AlterUser {
    public final List<UserAuthOption> userAuthOptions;

    AlterUserMysqlV56(List<UserAuthOption> userAuthOptions) {
      Preconditions.checkArgument(userAuthOptions != null && userAuthOptions.size() > 0);

      this.userAuthOptions = userAuthOptions;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class AlterUserMysqlV57 implements AlterUser {
    public final IfNotExists ifNotExists;
    public final List<UserAuthOption> userAuthOptions;
    public final Boolean tlsNone;
    public final List<TlsOption> tlsOptions;
    public final List<UserResourceOption> userResourceOptions;
    public final List<UserPasswordOption> userPasswordOptions;
    public final List<UserLockOptionEnum> userLockOptions;

    AlterUserMysqlV57(IfNotExists ifNotExists, List<UserAuthOption> userAuthOptions,
        Boolean tlsNone, List<TlsOption> tlsOptions, List<UserResourceOption> userResourceOptions,
        List<UserPasswordOption> userPasswordOptions, List<UserLockOptionEnum> userLockOptions) {
      Preconditions.checkArgument(userAuthOptions != null && userAuthOptions.size() > 0);

      this.ifNotExists = ifNotExists;
      this.userAuthOptions = userAuthOptions;
      this.tlsNone = tlsNone;
      this.tlsOptions = tlsOptions;
      this.userResourceOptions = userResourceOptions;
      this.userPasswordOptions = userPasswordOptions;
      this.userLockOptions = userLockOptions;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }
}
