package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;

/**
 * <pre>
 createTablespaceInnodb
    : CREATE TABLESPACE uid
      ADD DATAFILE datafile=STRING_LITERAL
      (FILE_BLOCK_SIZE '=' fileBlockSize=fileSizeLiteral)?
      (ENGINE '='? engineName)?
    ;
 * </pre>
 */
public class CreateTablespaceInnodb implements DdlStatement {
  public final Uid uid;
  public final String datafile;
  public final FileSizeLiteral fileBlockSize;
  public final EngineName engineName;

  CreateTablespaceInnodb(Uid uid, String datafile, FileSizeLiteral fileBlockSize,
      EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(fileBlockSize != null);

    this.uid = uid;
    this.datafile = datafile;
    this.fileBlockSize = fileBlockSize;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLESPACE ").append(uid.literal()).append(" ");
    sb.append("ADD DATAFILE ").append(datafile).append(" ");
    if (fileBlockSize != null) {
      sb.append("FILE_BLOCK_SIZE = ").append(fileBlockSize.literal()).append(" ");
    }
    if (engineName != null) {
      sb.append("ENGINE = ").append(engineName.literal());
    }
    return sb.toString();
  }
}
