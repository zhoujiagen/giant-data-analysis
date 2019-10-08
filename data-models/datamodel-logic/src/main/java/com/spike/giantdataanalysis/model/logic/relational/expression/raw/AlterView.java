package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 alterView
    : ALTER
      (
        ALGORITHM '=' algType=(UNDEFINED | MERGE | TEMPTABLE)
      )?
      ownerStatement?
      (SQL SECURITY secContext=(DEFINER | INVOKER))?
      VIEW fullId ('(' uidList ')')? AS selectStatement
      (WITH checkOpt=(CASCADED | LOCAL)? CHECK OPTION)?
    ;
 * </pre>
 */
public class AlterView implements DdlStatement {
  public static enum AlgTypeEnum implements RelationalAlgebraEnum {
    UNDEFINED, MERGE, TEMPTABLE;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum SecContextEnum implements RelationalAlgebraEnum {
    DEFINER, INVOKER;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum CheckOptEnum implements RelationalAlgebraEnum {
    CASCADED, LOCAL;
    @Override
    public String literal() {
      return name();
    }
  }

  public final AlterView.AlgTypeEnum algType;
  public final OwnerStatement ownerStatement;
  public final Boolean sqlSecurity;
  public final SecContextEnum secContext;
  public final FullId fullId;
  public final UidList uidList;
  public final SelectStatement selectStatement;
  public Boolean withCheckOption;
  public final CheckOptEnum checkOpt;

  AlterView(AlgTypeEnum algType, OwnerStatement ownerStatement, Boolean sqlSecurity,
      SecContextEnum secContext, FullId fullId, UidList uidList, SelectStatement selectStatement,
      Boolean withCheckOption, CheckOptEnum checkOpt) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(selectStatement != null);

    this.algType = algType;
    this.ownerStatement = ownerStatement;
    this.sqlSecurity = sqlSecurity;
    this.secContext = secContext;
    this.fullId = fullId;
    this.uidList = uidList;
    this.selectStatement = selectStatement;
    this.withCheckOption = withCheckOption;
    this.checkOpt = checkOpt;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER ");
    if (algType != null) {
      sb.append("ALGORITHM = ").append(algType.literal()).append(" ");
    }
    if (ownerStatement != null) {
      sb.append(ownerStatement.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(sqlSecurity)) {
      sb.append("SQL SECURITY ").append(secContext.literal()).append(" ");
    }
    sb.append("VIEW ").append(fullId.literal());
    if (uidList != null) {
      sb.append("(").append(uidList.literal()).append(") ");
    }
    sb.append("AS ").append(selectStatement.literal()).append(" ");
    if (Boolean.TRUE.equals(withCheckOption)) {
      sb.append("WITH ");
      if (checkOpt != null) {
        sb.append(checkOpt.literal()).append(" ");
      }
      sb.append("CHECK OPTION");
    }
    return sb.toString();
  }
}
