package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 deallocatePrepare
    : dropFormat=(DEALLOCATE | DROP) PREPARE uid
    ;
 * </pre>
 */
public class DeallocatePrepare implements PreparedStatement {
  public static enum DropFormatEnum implements RelationalAlgebraEnum {
    DEALLOCATE, DROP
  }

  public final DropFormatEnum dropFormat;
  public final Uid uid;

  DeallocatePrepare(DropFormatEnum dropFormat, Uid uid) {
    Preconditions.checkArgument(dropFormat != null);
    Preconditions.checkArgument(uid != null);

    this.dropFormat = dropFormat;
    this.uid = uid;
  }

}
