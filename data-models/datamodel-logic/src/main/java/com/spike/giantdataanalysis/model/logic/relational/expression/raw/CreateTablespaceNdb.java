package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;

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
  public final FileSizeLiteral extentSize;
  public final FileSizeLiteral initialSize;
  public final FileSizeLiteral autoextendSize;
  public final FileSizeLiteral maxSize;
  public final Uid nodeGroupUid;
  public final Boolean wait;
  public final String comment;
  public final EngineName engineName;

  CreateTablespaceNdb(Uid uid, String datafile, Uid logFileGroupUid, FileSizeLiteral extentSize,
      FileSizeLiteral initialSize, FileSizeLiteral autoextendSize, FileSizeLiteral maxSize,
      Uid nodeGroupUid, Boolean wait, String comment, EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(datafile != null);
    Preconditions.checkArgument(logFileGroupUid != null);
    Preconditions.checkArgument(engineName != null);

    this.uid = uid;
    this.datafile = datafile;
    this.logFileGroupUid = logFileGroupUid;
    this.extentSize = extentSize;
    this.initialSize = initialSize;
    this.autoextendSize = autoextendSize;
    this.maxSize = maxSize;
    this.nodeGroupUid = nodeGroupUid;
    this.wait = wait;
    this.comment = comment;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLESPACE ").append(uid.literal()).append(" ");
    sb.append("ADD DATAFILE ").append(datafile).append(" ");
    sb.append("USE LOGFILE GROUP ").append(logFileGroupUid.literal()).append(" ");
    if (extentSize != null) {
      sb.append("EXTENT_SIZE = ").append(extentSize.literal()).append(" ");
    }
    if (initialSize != null) {
      sb.append("INITIAL_SIZE = ").append(initialSize.literal()).append(" ");
    }
    if (autoextendSize != null) {
      sb.append("AUTOEXTEND_SIZE = ").append(autoextendSize.literal()).append(" ");
    }
    if (maxSize != null) {
      sb.append("MAX_SIZE = ").append(maxSize.literal()).append(" ");
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
    sb.append("ENGINE = ").append(engineName.literal()).append(" ");
    sb.append("").append(" ");
    return sb.toString();
  }
}
