package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 cursorStatement
    : CLOSE uid                                                     #CloseCursor
    | FETCH (NEXT? FROM)? uid INTO uidList                          #FetchCursor
    | OPEN uid                                                      #OpenCursor
    ;
 * </pre>
 */
public interface CursorStatement extends CompoundStatement {

  public static class CloseCursor implements CursorStatement {
    public final Uid uid;

    CloseCursor(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CLOSE ").append(uid.literal());
      return sb.toString();
    }

  }

  public static class FetchCursor implements CursorStatement {
    public final Boolean isNext;
    public final Uid uid;
    public final UidList uidList;

    FetchCursor(Boolean isNext, Uid uid, UidList uidList) {
      Preconditions.checkArgument(uid != null);
      Preconditions.checkArgument(uidList != null);

      this.isNext = isNext;
      this.uid = uid;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("FETCH ");
      if (Boolean.TRUE.equals(isNext)) {
        sb.append("NEXT FROM ");
      }
      sb.append(uid.literal()).append(" INTO ").append(uidList.literal());
      return sb.toString();
    }

  }

  public static class OpenCursor implements CursorStatement {
    public final Uid uid;

    OpenCursor(Uid uid) {
      Preconditions.checkArgument(uid != null);

      this.uid = uid;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("OPEN ").append(uid.literal());
      return sb.toString();
    }
  }
}