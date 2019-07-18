package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;

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
public interface AlterUser extends AdministrationStatement {
  public static class AlterUserMysqlV56 implements AlterUser {
    public final List<UserSpecification> userSpecifications;

    AlterUserMysqlV56(List<UserSpecification> userSpecifications) {
      Preconditions.checkArgument(userSpecifications != null && userSpecifications.size() > 0);

      this.userSpecifications = userSpecifications;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class AlterUserMysqlV57 implements AlterUser {
    public final IfExists ifExists;
    public final List<UserAuthOption> userAuthOptions;
    public final Boolean tlsNone;
    public final List<TlsOption> tlsOptions;
    public final List<UserResourceOption> userResourceOptions;
    public final List<UserPasswordOption> userPasswordOptions;
    public final List<UserLockOptionEnum> userLockOptions;

    AlterUserMysqlV57(IfExists ifExists, List<UserAuthOption> userAuthOptions, Boolean tlsNone,
        List<TlsOption> tlsOptions, List<UserResourceOption> userResourceOptions,
        List<UserPasswordOption> userPasswordOptions, List<UserLockOptionEnum> userLockOptions) {
      Preconditions.checkArgument(userAuthOptions != null && userAuthOptions.size() > 0);

      this.ifExists = ifExists;
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
