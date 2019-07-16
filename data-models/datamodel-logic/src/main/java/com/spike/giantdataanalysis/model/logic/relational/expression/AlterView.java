package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

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
    UNDEFINED, MERGE, TEMPTABLE
  }

  public static enum SecContextEnum implements RelationalAlgebraEnum {
    DEFINER, INVOKER
  }

  public static enum CheckOptEnum implements RelationalAlgebraEnum {
    CASCADED, LOCAL
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

}
