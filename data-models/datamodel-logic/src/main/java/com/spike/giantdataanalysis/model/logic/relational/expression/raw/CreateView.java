package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 createView
    : CREATE (OR REPLACE)?
      (
        ALGORITHM '=' algType=(UNDEFINED | MERGE | TEMPTABLE)
      )?
      ownerStatement?
      (SQL SECURITY secContext=(DEFINER | INVOKER))?
      VIEW fullId ('(' uidList ')')? AS selectStatement
      (WITH checkOption=(CASCADED | LOCAL)? CHECK OPTION)?
    ;
 * </pre>
 */
public class CreateView implements DdlStatement {

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

  public static enum CheckOptionEnum implements RelationalAlgebraEnum {
    CASCADED, LOCAL;
    @Override
    public String literal() {
      return name();
    }
  }

  public final Boolean replace;
  public final CreateView.AlgTypeEnum algType;
  public final OwnerStatement ownerStatement;
  public final Boolean sqlSecurity;
  public final CreateView.SecContextEnum secContext;
  public final FullId fullId;
  public final UidList uidList;
  public final SelectStatement selectStatement;
  public Boolean withCheckOption;
  public final CreateView.CheckOptionEnum checkOption;

  CreateView(Boolean replace, CreateView.AlgTypeEnum algType, OwnerStatement ownerStatement,
      Boolean sqlSecurity, CreateView.SecContextEnum secContext, FullId fullId, UidList uidList,
      SelectStatement selectStatement, Boolean withCheckOption,
      CreateView.CheckOptionEnum checkOption) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(selectStatement != null);

    this.replace = replace;
    this.algType = algType;
    this.ownerStatement = ownerStatement;
    this.sqlSecurity = sqlSecurity;
    this.secContext = secContext;
    this.fullId = fullId;
    this.uidList = uidList;
    this.selectStatement = selectStatement;
    this.withCheckOption = withCheckOption;
    this.checkOption = checkOption;
  }

  @Override
  public String literal() {

    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ");
    if (algType != null) sb.append("ALGORITHM = ").append(algType.literal()).append(" ");
    if (ownerStatement != null) {
      sb.append(ownerStatement.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(sqlSecurity)) {
      sb.append("SQL SECURITY ").append(secContext.literal()).append(" ");
    }
    sb.append("VIEW ").append(fullId.literal()).append(" ");
    if (uidList != null) {
      sb.append("(").append(uidList.literal()).append(") ");
    }
    sb.append("AS ").append(selectStatement.literal()).append(" ");
    if (Boolean.TRUE.equals(withCheckOption)) {
      sb.append("WITH ");
      if (checkOption != null) {
        sb.append(checkOption.literal()).append(" ");
      }
      sb.append("CHECK OPTION");
    }
    return sb.toString();

  }

}
