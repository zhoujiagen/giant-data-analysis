package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;

/**
 * <pre>
 createLogfileGroup
    : CREATE LOGFILE GROUP uid
      ADD UNDOFILE undoFile=STRING_LITERAL
      (INITIAL_SIZE '='? initSize=fileSizeLiteral)?
      (UNDO_BUFFER_SIZE '='? undoSize=fileSizeLiteral)?
      (REDO_BUFFER_SIZE '='? redoSize=fileSizeLiteral)?
      (NODEGROUP '='? uid)?
      WAIT?
      (COMMENT '='? comment=STRING_LITERAL)?
      ENGINE '='? engineName
    ;
 * </pre>
 */
public class CreateLogfileGroup implements DdlStatement {
  public final Uid logFileGroupUid;
  public final String undoFile;
  public final FileSizeLiteral initSize;
  public final FileSizeLiteral undoSize;
  public final FileSizeLiteral redoSize;
  public final Uid nodeGroupUid;
  public final Boolean wait;
  public final String comment;
  public final EngineName engineName;

  CreateLogfileGroup(Uid logFileGroupUid, String undoFile, FileSizeLiteral initSize,
      FileSizeLiteral undoSize, FileSizeLiteral redoSize, Uid nodeGroupUid, Boolean wait,
      String comment, EngineName engineName) {
    Preconditions.checkArgument(logFileGroupUid != null);
    Preconditions.checkArgument(undoFile != null);
    Preconditions.checkArgument(engineName != null);

    this.logFileGroupUid = logFileGroupUid;
    this.undoFile = undoFile;
    this.initSize = initSize;
    this.undoSize = undoSize;
    this.redoSize = redoSize;
    this.nodeGroupUid = nodeGroupUid;
    this.wait = wait;
    this.comment = comment;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE LOGFILE GROUP ").append(logFileGroupUid.literal()).append(" ");
    sb.append("ADD UNDOFILE ").append(undoFile).append(" ");
    if (initSize != null) {
      sb.append("INITIAL_SIZE = ").append(initSize.literal()).append(" ");
    }
    if (undoSize != null) {
      sb.append("UNDO_BUFFER_SIZE = ").append(undoSize.literal()).append(" ");
    }
    if (redoSize != null) {
      sb.append("REDO_BUFFER_SIZE = ").append(redoSize.literal()).append(" ");
    }
    if (nodeGroupUid != null) {
      sb.append("NODEGROUP = ").append(nodeGroupUid.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(wait)) {
      sb.append("WAIT ");
    }
    if (comment != null) {
      sb.append("COMMENT = ").append(comment).append(" ");
    }
    sb.append("ENGINE = ").append(engineName.literal());
    return sb.toString();
  }
}
