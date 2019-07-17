package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;

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
  public final String comment;
  public final EngineName engineName;

  CreateLogfileGroup(Uid logFileGroupUid, String undoFile, FileSizeLiteral initSize,
      FileSizeLiteral undoSize, FileSizeLiteral redoSize, Uid nodeGroupUid, String comment,
      EngineName engineName) {
    Preconditions.checkArgument(logFileGroupUid != null);
    Preconditions.checkArgument(undoFile != null);
    Preconditions.checkArgument(engineName != null);

    this.logFileGroupUid = logFileGroupUid;
    this.undoFile = undoFile;
    this.initSize = initSize;
    this.undoSize = undoSize;
    this.redoSize = redoSize;
    this.nodeGroupUid = nodeGroupUid;
    this.comment = comment;
    this.engineName = engineName;
  }

}
