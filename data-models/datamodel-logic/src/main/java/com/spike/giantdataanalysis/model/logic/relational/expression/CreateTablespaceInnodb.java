package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;

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

}
