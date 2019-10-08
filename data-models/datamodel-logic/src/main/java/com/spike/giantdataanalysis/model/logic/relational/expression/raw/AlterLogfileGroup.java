package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;

/**
 * <pre>
 alterLogfileGroup
    : ALTER LOGFILE GROUP uid
      ADD UNDOFILE STRING_LITERAL
      (INITIAL_SIZE '='? fileSizeLiteral)?
      WAIT? ENGINE '='? engineName
    ;
 * </pre>
 */
public class AlterLogfileGroup implements DdlStatement {

  public final Uid uid;
  public final String undoFile;
  public final FileSizeLiteral fileSizeLiteral;
  public final Boolean wait;
  public final EngineName engineName;

  AlterLogfileGroup(Uid uid, String undoFile, FileSizeLiteral fileSizeLiteral, Boolean wait,
      EngineName engineName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(undoFile != null);
    Preconditions.checkArgument(engineName != null);

    this.uid = uid;
    this.undoFile = undoFile;
    this.fileSizeLiteral = fileSizeLiteral;
    this.wait = wait;
    this.engineName = engineName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER LOGFILE GROUP ").append(uid.literal()).append(" ");
    sb.append("ADD UNDOFILE ").append(undoFile).append(" ");
    if (fileSizeLiteral != null) {
      sb.append("INITIAL_SIZE = ").append(fileSizeLiteral.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(wait)) {
      sb.append("WAIT ");
    }
    sb.append("ENGINE = ").append(engineName.literal());
    return sb.toString();
  }
}
