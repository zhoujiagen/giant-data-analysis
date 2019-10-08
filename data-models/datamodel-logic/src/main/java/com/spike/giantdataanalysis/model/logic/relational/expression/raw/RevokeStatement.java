package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;

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
      StringBuilder sb = new StringBuilder();
      sb.append("REVOKE ");
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
      sb.append("FROM ");
      List<String> literals2 = Lists.newArrayList();
      for (UserName userName : userNames) {
        literals2.add(userName.literal());
      }
      sb.append(Joiner.on(", ").join(literals2));
      return sb.toString();
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
      StringBuilder sb = new StringBuilder();
      sb.append("REVOKE ALL PRIVILEGES, GRANT OPTION ");
      sb.append("FROM ");
      List<String> literals = Lists.newArrayList();
      for (UserName userName : userNames) {
        literals.add(userName.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

}
