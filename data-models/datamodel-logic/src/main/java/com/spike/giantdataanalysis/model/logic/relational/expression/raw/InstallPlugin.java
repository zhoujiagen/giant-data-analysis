package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 installPlugin
    : INSTALL PLUGIN uid SONAME STRING_LITERAL
    ;
 * </pre>
 */
public class InstallPlugin implements AdministrationStatement {
  public final Uid uid;
  public final String soName;

  InstallPlugin(Uid uid, String soName) {
    Preconditions.checkArgument(uid != null);
    Preconditions.checkArgument(soName != null);

    this.uid = uid;
    this.soName = soName;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("INSTALL PLUGIN ").append(uid.literal()).append(" SONAME ").append(soName);
    return sb.toString();
  }
}
