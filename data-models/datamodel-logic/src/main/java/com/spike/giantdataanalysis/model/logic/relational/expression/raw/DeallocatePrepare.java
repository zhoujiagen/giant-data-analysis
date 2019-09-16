package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 deallocatePrepare
    : dropFormat=(DEALLOCATE | DROP) PREPARE uid
    ;
 * </pre>
 */
public class DeallocatePrepare implements PreparedStatement {
  public static enum DropFormatEnum implements RelationalAlgebraEnum {
    DEALLOCATE, DROP;
    @Override
    public String literal() {
      return name();
    }
  }

  public final DeallocatePrepare.DropFormatEnum dropFormat;
  public final Uid uid;

  DeallocatePrepare(DeallocatePrepare.DropFormatEnum dropFormat, Uid uid) {
    Preconditions.checkArgument(dropFormat != null);
    Preconditions.checkArgument(uid != null);

    this.dropFormat = dropFormat;
    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(dropFormat.literal()).append(" PREPARE ").append(uid.literal());
    return sb.toString();
  }
}
