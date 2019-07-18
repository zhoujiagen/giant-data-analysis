package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

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
    return sb.toString();
  }
}
