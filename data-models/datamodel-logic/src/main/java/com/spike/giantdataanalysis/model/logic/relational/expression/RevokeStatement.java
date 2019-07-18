package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;

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
public interface RevokeStatement extends AdministrationStatement {

  public static class DetailRevoke implements RevokeStatement {
    public final List<PrivelegeClause> privelegeClauses;
    public final PrivilegeObjectEnum privilegeObject;
    public final PrivilegeLevel privilegeLevel;
    public final List<UserName> userNames;

    DetailRevoke(List<PrivelegeClause> privelegeClauses, PrivilegeObjectEnum privilegeObject,
        PrivilegeLevel privilegeLevel, List<UserName> userNames) {
      Preconditions.checkArgument(privelegeClauses != null && privelegeClauses.size() > 0);
      Preconditions.checkArgument(privilegeLevel != null);
      Preconditions.checkArgument(userNames != null && userNames.size() > 0);

      this.privelegeClauses = privelegeClauses;
      this.privilegeObject = privilegeObject;
      this.privilegeLevel = privilegeLevel;
      this.userNames = userNames;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class ShortRevoke implements RevokeStatement {
    public final List<UserName> userNames;

    ShortRevoke(List<UserName> userNames) {
      Preconditions.checkArgument(userNames != null && userNames.size() > 0);

      this.userNames = userNames;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

}
