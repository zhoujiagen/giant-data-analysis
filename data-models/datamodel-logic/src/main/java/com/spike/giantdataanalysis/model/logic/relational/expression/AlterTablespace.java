package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.FileSizeLiteral;

/**
 * <pre>
 alterTablespace
    : ALTER TABLESPACE uid
      objectAction=(ADD | DROP) DATAFILE STRING_LITERAL
      (INITIAL_SIZE '=' fileSizeLiteral)?
      WAIT?
      ENGINE '='? engineName
    ;
 * </pre>
 */
public class AlterTablespace implements DdlStatement {
  public static enum ObjectActionEnum implements RelationalAlgebraEnum {
    ADD, DROP
  }

  public final Uid uid;
  public final AlterTablespace.ObjectActionEnum objectAction;
  public final String dataFile;
  public final FileSizeLiteral fileSizeLiteral;
  public final Boolean wait;
  public final EngineName engineName;

  AlterTablespace(Uid uid, ObjectActionEnum objectAction, String dataFile,
      FileSizeLiteral fileSizeLiteral, Boolean wait, EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(objectAction != null);
    Preconditions.checkArgument(dataFile != null);
    Preconditions.checkArgument(engineName != null);

    this.uid = uid;
    this.objectAction = objectAction;
    this.dataFile = dataFile;
    this.fileSizeLiteral = fileSizeLiteral;
    this.wait = wait;
    this.engineName = engineName;
  }

}
