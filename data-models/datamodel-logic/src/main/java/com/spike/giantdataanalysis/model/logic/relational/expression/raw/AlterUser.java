package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;

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
      StringBuilder sb = new StringBuilder();
      sb.append("ALTER USER ");
      List<String> literals = Lists.newArrayList();
      for (UserSpecification userSpecification : userSpecifications) {
        literals.add(userSpecification.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
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
      StringBuilder sb = new StringBuilder();
      sb.append("ALTER USER ");
      if (ifExists != null) {
        sb.append(ifExists.literal()).append(" ");
      }
      List<String> literals = Lists.newArrayList();
      for (UserAuthOption userAuthOption : userAuthOptions) {
        literals.add(userAuthOption.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
      if (tlsNone != null) {
        sb.append("REQUIRE NONE ");
      }
      if (CollectionUtils.isNotEmpty(tlsOptions)) {
        sb.append("REQUIRE ");
        List<String> literals2 = Lists.newArrayList();
        for (TlsOption tlsOption : tlsOptions) {
          literals2.add(tlsOption.literal());
          sb.append(Joiner.on("AND ").join(literals2)).append(" ");
        }
      }
      if (CollectionUtils.isNotEmpty(userResourceOptions)) {
        sb.append("WITH ");
        List<String> literals3 = Lists.newArrayList();
        for (UserResourceOption userResourceOption : userResourceOptions) {
          literals3.add(userResourceOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals3)).append(" ");
      }
      if (CollectionUtils.isNotEmpty(userPasswordOptions)) {
        List<String> literals4 = Lists.newArrayList();
        for (UserPasswordOption userPasswordOption : userPasswordOptions) {
          literals4.add(userPasswordOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals4)).append(" ");
      }
      if (CollectionUtils.isNotEmpty(userLockOptions)) {
        sb.append("WITH ");
        List<String> literals5 = Lists.newArrayList();
        for (UserLockOptionEnum userLockOption : userLockOptions) {
          literals5.add(userLockOption.literal());
        }
        sb.append(Joiner.on(" ").join(literals5)).append(" ");
      }

      return sb.toString();
    }

  }
}
