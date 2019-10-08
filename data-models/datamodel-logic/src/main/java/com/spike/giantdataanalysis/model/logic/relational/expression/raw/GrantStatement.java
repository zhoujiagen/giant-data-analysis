package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
  public final List<PrivelegeClause> privelegeClauses;
  public final PrivilegeObjectEnum privilegeObject;
  public final PrivilegeLevel privilegeLevel;
  public final List<UserAuthOption> userAuthOptions;
  public final Boolean tlsNone;
  public final List<TlsOption> tlsOptions;
  public final List<UserResourceOption> userResourceOptions;

  GrantStatement(List<PrivelegeClause> privelegeClauses, PrivilegeObjectEnum privilegeObject,
      PrivilegeLevel privilegeLevel, List<UserAuthOption> userAuthOptions, Boolean tlsNone,
      List<TlsOption> tlsOptions, List<UserResourceOption> userResourceOptions) {
    Preconditions.checkArgument(privelegeClauses != null && privelegeClauses.size() > 0);
    Preconditions.checkArgument(privilegeLevel != null);
    Preconditions.checkArgument(userAuthOptions != null && userAuthOptions.size() > 0);

    this.privelegeClauses = privelegeClauses;
    this.privilegeObject = privilegeObject;
    this.privilegeLevel = privilegeLevel;
    this.userAuthOptions = userAuthOptions;
    this.tlsNone = tlsNone;
    this.tlsOptions = tlsOptions;
    this.userResourceOptions = userResourceOptions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("GRANT ");
    List<String> literals = Lists.newArrayList();
    for (PrivelegeClause privelegeClause : privelegeClauses) {
      literals.add(privelegeClause.literal());
    }
    sb.append(Joiner.on(", ").join(literals)).append(" ");
    sb.append("ON ");
    if (privilegeObject != null) {
      sb.append(privilegeObject.literal()).append(" ");
    }
    sb.append(privilegeLevel.literal()).append(" ");
    sb.append("TO ");
    List<String> literals2 = Lists.newArrayList();
    for (UserAuthOption userAuthOption : userAuthOptions) {
      literals.add(userAuthOption.literal());
    }
    sb.append(Joiner.on(", ").join(literals2)).append(" ");
    if (tlsNone != null) {
      sb.append("REQUIRE NONE ");
    }
    if (CollectionUtils.isNotEmpty(tlsOptions)) {
      sb.append("REQUIRE ");
      List<String> literals3 = Lists.newArrayList();
      for (TlsOption tlsOption : tlsOptions) {
        literals3.add(tlsOption.literal());
        sb.append(Joiner.on(", ").join(literals3)).append(" ");
      }
    }
    if (CollectionUtils.isNotEmpty(userResourceOptions)) {
      sb.append("WITH GRANT OPTION ");
      List<String> literals4 = Lists.newArrayList();
      for (UserResourceOption userResourceOption : userResourceOptions) {
        literals4.add(userResourceOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals4));
    }
    return sb.toString();
  }
}
