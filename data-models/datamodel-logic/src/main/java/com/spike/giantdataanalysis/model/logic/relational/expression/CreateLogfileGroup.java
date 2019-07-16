package com.spike.giantdataanalysis.model.logic.relational.expression;

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
  // TODO(zhoujiagen) restart here!!!

}
