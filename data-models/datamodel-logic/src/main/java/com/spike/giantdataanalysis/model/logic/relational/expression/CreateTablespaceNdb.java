package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;

/**
 * <pre>
 createTablespaceNdb
    : CREATE TABLESPACE uid
      ADD DATAFILE datafile=STRING_LITERAL
      USE LOGFILE GROUP uid
      (EXTENT_SIZE '='? extentSize=fileSizeLiteral)?
      (INITIAL_SIZE '='? initialSize=fileSizeLiteral)?
      (AUTOEXTEND_SIZE '='? autoextendSize=fileSizeLiteral)?
      (MAX_SIZE '='? maxSize=fileSizeLiteral)?
      (NODEGROUP '='? uid)?
      WAIT?
      (COMMENT '='? comment=STRING_LITERAL)?
      ENGINE '='? engineName
    ;
 * </pre>
 */
public class CreateTablespaceNdb implements DdlStatement {
  public final Uid uid;
  public final String datafile;
  public final Uid logFileGroupUid;
  public final FileSizeLiteral extendSize;
  public final FileSizeLiteral initialSize;
  public final FileSizeLiteral autoextendSize;
  public final FileSizeLiteral maxSize;
  public final Uid nodeGroupUid;
  public final Boolean wait;
  public final String comment;
  public final EngineName engineName;

  CreateTablespaceNdb(Uid uid, String datafile, Uid logFileGroupUid, FileSizeLiteral extendSize,
      FileSizeLiteral initialSize, FileSizeLiteral autoextendSize, FileSizeLiteral maxSize,
      Uid nodeGroupUid, Boolean wait, String comment, EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(datafile != null);
    Preconditions.checkArgument(logFileGroupUid != null);
    Preconditions.checkArgument(engineName != null);

    this.uid = uid;
    this.datafile = datafile;
    this.logFileGroupUid = logFileGroupUid;
    this.extendSize = extendSize;
    this.initialSize = initialSize;
    this.autoextendSize = autoextendSize;
    this.maxSize = maxSize;
    this.nodeGroupUid = nodeGroupUid;
    this.wait = wait;
    this.comment = comment;
    this.engineName = engineName;
  }

}
